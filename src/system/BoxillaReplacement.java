package system;



import java.io.File;


import org.apache.log4j.Logger;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.AppliancePool;
import extra.Device;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.device.SystemProperties;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import objects.Landingpage;
import objects.Loginpage;
import objects.SystemAll;
import objects.Users;
import testNG.Utilities;

public class BoxillaReplacement extends StartupTestCase{
	

	private String txIp = "";
	private String rxIp = "";
	private String txIpDual = "";
	private String rxIpDual = "";
	private String txSingleName = "Test_TX_Single";
	private String txDualName = "Test_TX_Dual";
	private String rxSingleName = "Test_RX_Single";
	private String rxDualName = "Test_RX_Dual";
	private String user1 = "replacementUser1";
	private String user2 = "replacementUser2";
	private String connection1 = "replacementConnection1";
	private String connection2 = "replacementConnection2";
	private SystemMethods sysMethods = new SystemMethods();
	final static Logger log = Logger.getLogger(BoxillaReplacement.class);
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private UsersMethods userMethods = new UsersMethods();
	private ConnectionsMethods connections = new ConnectionsMethods();
	Thread killBoxilla, killDevice;
	private String dbBackupFullName = "";
	private String dbBackUppartialName = "";
	private String oldBoxilla = "10.10.11.250";
	
	AppliancePool devicePool = new AppliancePool();
	Device txSingle, rxSingle, txDual, rxDual;
	
	public BoxillaReplacement () {
		boxillaManager = oldBoxilla;
		userName = "admin";
		password = "admin";
		url = "https://" + boxillaManager + "/";
	}
	
	public void getDevices() {
		devicePool.getAllDevices();
		txSingle = devicePool.getTxSingle();
		rxSingle = devicePool.getRxSingle();
		txDual = devicePool.getTxDual();
		rxDual = devicePool.getRxDual();
		
		System.out.println("txSingle:" + txSingle.toString());
		System.out.println("rxSingle:" + rxSingle.toString());
		System.out.println("txDual:" + txDual.toString());
		System.out.println("rxDual:" + rxDual.toString());
	}
	
	
	@BeforeClass
	public void beforeClass() {
		
		getDevices();
		txIp = txSingle.getIpAddress();
		rxIp = rxSingle.getIpAddress();
		txIpDual = txDual.getIpAddress();
		rxIpDual = rxDual.getIpAddress();
		
		try {
			cleanUpLogin();
			createUser(user1);
			//makeConnection(connection1, "private");
			deviceManageTestPrep();
			log.info("Sleeping while devices configure");
			Thread.sleep(100000);
			sysMethods.backupDatabase(driver);
			cleanUpLogout();
			// take 10.10.11.123 offline
			killBoxilla = new Thread() {
				public void run() {
					Ssh shell = new Ssh(boxillaUsername, boxillaPassword, oldBoxilla);
					shell.loginToServer();
					shell.sendCommand("./networkKill2.sh");
					shell.disconnect();
				}
			};
			killBoxilla.start();
			
			System.out.println("TESTING HHHHHHHHHHHHHHH");
			//set details back to test boxilla
			boxillaManager = prop.getProperty("boxillaManager");
			userName = prop.getProperty("userName");
			password = prop.getProperty("password");
			url = "https://" + boxillaManager + "/";
			setDatabaseName();
			setDatabasePartialName();
			cleanUpLogin();
			doBoxillaReplacement();
			deleteBackupFromHost();
			
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	public void doBoxillaReplacement() throws InterruptedException {
		sysMethods.navigateToSystemAdmin(driver);
		sysMethods.timer(driver);
		SystemAll.backupRestoreTab(driver).click();
		sysMethods.uploadBackup(driver, dbBackupFullName, dbBackUppartialName, "C:\\temp"); // Upload custom backup
		sysMethods.activateBackup(driver, dbBackUppartialName); // Activate Uploaded backup
	}
	
	@Test
	public void test01_checkSingleTxOnline() throws InterruptedException {
		log.info("***** test01_checkSingleTxOnline *****");
		deviceMethods.checkDeviceOnline(driver, txSingle.getIpAddress());
	}
	
	@Test
	public void test02_checkDualTxOnline() throws InterruptedException {
		log.info("***** test02_checkDualTxOnline *****");
		deviceMethods.checkDeviceOnline(driver, txDual.getIpAddress());
	}
	
	@Test
	public void test03_checkSingleRxOnline() throws InterruptedException {
		log.info("***** test03_checkSingleRxOnline *****");
		deviceMethods.checkDeviceOnline(driver, rxSingle.getIpAddress());
	}
	
	@Test
	public void test04_checkDualRxOnline() throws InterruptedException {
		log.info("***** test03_checkSingleRxOnline *****");
		deviceMethods.checkDeviceOnline(driver, rxDual.getIpAddress());
	}

	@Test
	public void test05_checkConnectionsRestored() throws InterruptedException {
		log.info("***** test05_checkConnectionsRestored *****");
		connections.checkConnectionExists(driver, connection1);
	}
	
	@Test
	public void test06_checkUsersRestored() throws InterruptedException {
		log.info("***** test06_checkUsersRestored *****");
		userMethods.checkUserExists(driver, user1);
	}
	
	@Test
	public void test07_deviceCheckOffline() throws InterruptedException {
		log.info("***** test07_rebootDeviceCheckOnline *****");
		killDevice = new Thread() {
			public void run() {
				Ssh shell = new Ssh(deviceUserName, devicePassword, txIp);
				shell.loginToServer();
				shell.sendCommand("ifconfig eth0 down && sleep 120  && ifconfig eth0 up");
				shell.disconnect();
			}
		};
		killDevice.start();
		deviceMethods.checkDeviceOffline(driver, txIp);
	}
	
	@Test(dependsOnMethods = { "test07_deviceCheckOffline" })
	public void test08_checkDeviceOnline() throws InterruptedException {
		log.info("***** test08_checkDeviceOnline *****");
		Thread.sleep(30000);
		deviceMethods.checkDeviceOnline(driver, txIp);
	}
	

	
	
	public void killNetwork(String ipAddress, String userName, String password) throws InterruptedException {
		killBoxilla = new Thread() {
			public void run() {
				Ssh shell = new Ssh(userName, password, ipAddress);
				shell.loginToServer();
				shell.sendCommand("./networkKill2.sh");
				int counter = 0;
				while(!sysMethods.pingIpAddress(oldBoxilla) && counter < 12) {
					counter++;
					System.out.println(oldBoxilla + " not online.. retrying " + counter);
					try {
						Thread.sleep(5000);
					} catch (InterruptedException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
				shell.disconnect();
				System.out.println(oldBoxilla + " back online online");
			}
		};
		killBoxilla.start();
	}
	
	
	
	public void killNetworkDevice() {
		Ssh shell = new Ssh(userName, password, "10.10.10.155");
		shell.loginToServer();
		shell.sendCommand("ifdown eth0 && sleep 50  && ifup eth0");
	}
	
	public void checkVmOnline() throws InterruptedException {
		int counter = 0;
		while(!sysMethods.pingIpAddress(oldBoxilla) && counter < 25) {
			counter++;
			log.info("10.10.11.123 not online.. retrying " + counter);
			try {
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if(sysMethods.pingIpAddress(oldBoxilla)) {
			log.info(oldBoxilla + " is back online!");
			boxillaManager = oldBoxilla;
			userName = "admin";
			password = "admin";
			//wait for boxilla to come back up
			Thread.sleep(100000);
			url = "https://" + boxillaManager + "/";
			
			try {
				cleanUpLogin();
				sysMethods.dbReset(driver);
				sysMethods.boxillaReboot(driver);
				cleanUpLogout();
			}catch(Exception | AssertionError e1) {
				Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
				e1.printStackTrace();
				cleanUpLogout();
			}
			
		}else {
			log.info(oldBoxilla + " did not come back online. Continuing tests anyway...");
		}
	}
	
	/**
	 * Gives devices IP addresses and manages them
	 * @throws InterruptedException
	 */
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"),
				rxIp, rxSingle.getGateway(),rxSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxSingleName, rxSingle.getMac(),
				prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"),
				txIp, txSingle.getGateway(), txSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txSingleName, txSingle.getMac(), 
				prop.getProperty("ipCheck"));
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(), rxDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxDualName,rxDual.getMac(),
				prop.getProperty("ipCheck"));
				
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txDual.getMac(), prop.getProperty("ipCheck"),
				txIpDual,txDual.getGateway(), txDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txDualName, txDual.getMac(),
				prop.getProperty("ipCheck"));
				
		
		log.info("Appliances Managed Successfully");
	}
	
	public void createUser(String userName) throws InterruptedException {
		userMethods.addUser(driver, userName);

		Thread.sleep(2000);
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");

		userMethods.addUserNoTemplateAutoConnectOFF(driver, userName);
	}
	
//	public void makeConnection(String connectionName, String connectionType) throws InterruptedException {
//		log.info("Creating connection : " + connectionName);
//		connections.addConnection(driver, connectionName, "no"); // connection name, user template
//		connections.connectionInfo(driver, "tx", "user", txIp); // connection via, name, host ip
//		connections.chooseCoonectionType(driver, connectionType); // connection type
//		connections.enableExtendedDesktop(driver);
//		connections.enablePersistenConnection(driver);
//		if(connectionType.equals("private")) {
//			connections.enableUSBRedirection(driver);
//			connections.enableAudio(driver);
//		}
//		connections.propertyInfoClickNext(driver);
//		connections.saveConnection(driver, connectionName);
//	}
	
	
	public void setDatabaseName() {
		File dir = new File("C:\\temp");
		for(File file : dir.listFiles()) {
			if(file.getName().endsWith(".bbx")) {
				dbBackupFullName =  file.getName();
			}
		}
	}
	public void setDatabasePartialName() {
		String name = dbBackupFullName;
		String[] nameSplit = name.split("_");
		System.out.println(nameSplit[3]);
		String timeOnly = nameSplit[3].split("\\.")[0];
		String properTime = timeOnly.replace("-", ":");
		dbBackUppartialName = properTime;
	}
	public void deleteBackupFromHost() {
		File file = new File("C:\\temp\\" + dbBackupFullName);
		file.delete();
	}
	
	/**
	 * Logs into boxilla and unmanages the devices
	 */
	@AfterClass(alwaysRun=true)
	public void afterClass() {
		log.info("**** Starting test teardown for " + this.getClass().getSimpleName() + " ****");
		try {
			cleanUpLogin();
			deviceMethods.unManageDevice(driver, txIp);
			deviceMethods.unManageDevice(driver, rxIp);
			deviceMethods.unManageDevice(driver, txIpDual);
			deviceMethods.unManageDevice(driver, rxIpDual);
			sysMethods.dbReset(driver);
			sysMethods.boxillaReboot(driver);
			cleanUpLogout();
			checkVmOnline();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
}
