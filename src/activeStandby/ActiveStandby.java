package activeStandby;

import java.util.HashMap;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import device.DeviceDiscovery;
import extra.Database;
import extra.ScpTo;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ClusterMethods;
import methods.ConnectionsMethods;
import methods.SwitchMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import objects.Users;
import objects.Cluster;
import objects.Cluster.CLUSTER_INFO_TABLE_COLUMNS;
import objects.Cluster.CLUSTER_NODE_COLUMNS;
import testNG.Utilities;

/**
 * This class will run tests against the Boxilla 'Cluster' feature.
 * It will run tests to set up a cluster and verify the cluster is 
 * running as expected. These tests have to run in a certain order
 * so a priority is set on each test case
 * @author Boxilla(arup)
 *
 */
public class ActiveStandby extends StartupTestCase {

	final static Logger log = Logger.getLogger(ActiveStandby.class);
	private ClusterMethods cluster = new ClusterMethods();
	private Cluster clusterobj = new Cluster();
	private ConnectionsMethods connectmethods = new ConnectionsMethods();//remove it while integrating
	private String clusterId = "cluster1";
	private String masterNodeId = "1";
	private String masterNodeName = "node1";
	private String standbyNodeName = "node2";
	private String standbyNodeId = "2";
	private String boxilla  = boxillaManager;
	private String boxillastandby;
	private String standbyboxilla = boxillaManager2;
	private String dbUsername = dbUser;
	private String dataPassword = dbPassword;
	private UsersMethods users = new UsersMethods();
	private ConnectionsMethods connection = new ConnectionsMethods();
	private String oldConnection = "tx_testconnection1";
	private String oldUser = "test1";
	private SystemMethods system = new SystemMethods();
	
	/**
	 * Test that will set up the master Boxilla in a cluster
	 * @throws InterruptedException 
	 */
	@Test(priority=1)
	public void test01_createMaster() throws InterruptedException {
		cluster.prepareMasterBoxilla(driver, clusterId, virtualIp, masterNodeId, masterNodeName);
		log.info("Getting node ID from UI and asserting if it matches");
		
		String nodeIdFromUI = cluster.getNodeInfoTableColumn(driver, boxillaManager, CLUSTER_NODE_COLUMNS.NODE_ID);
		Assert.assertTrue(masterNodeId.equals(nodeIdFromUI), "Node ID from UI did not match Node ID used to create master, actual " + 
				nodeIdFromUI);
		log.info("Node id matched. Checking node name");
		
		String nodeNameFromUi = cluster.getNodeInfoTableColumn(driver, boxillaManager, CLUSTER_NODE_COLUMNS.NODE_NAME);
		Assert.assertTrue(masterNodeName.equals(nodeNameFromUi), "Node Name from UI did not match Node name "
				+ "used to create master, actual " + nodeNameFromUi);
		log.info("Node name matched. Checking Virtual IP");
		
		String ipFromUI = cluster.getClusterInfoTableColumn(driver, clusterId, CLUSTER_INFO_TABLE_COLUMNS.VIRTUAL_IP);
		Assert.assertTrue(virtualIp.equals(ipFromUI), "IP from UI did not match the IP used to create the master, actual"
				+ " " + virtualIp);
		log.info("Virtual IP matched, checking cluster ID");
		
		String clusterIdFromUI = cluster.getClusterInfoTableColumn(driver, clusterId, CLUSTER_INFO_TABLE_COLUMNS.CLUSTER_ID);
		Assert.assertTrue(clusterIdFromUI.equals(clusterId), "Cluster ID from UI did not match the cluster ID from "
				+ " creation, actual " + clusterIdFromUI);
		log.info("Cluster ID checked. Test passed");	
		//set boxilla for next test	
		//set boxillaManager to 2nd boxilla
		log.info("TEST FINISHED");
	}

	
//	 * Test that will set up a standby Boxilla 
//	 * and add it to the cluster created in test1
//	 * @throws InterruptedException 
	
	@Test(priority=2)
	public void test02_createStandby() throws InterruptedException {
		cleanUpLogin(boxillaManager2);
		log.info("boxilla standby is on - "+standbyboxilla);
		cluster.prepareStandByBoxilla(driver, boxilla, standbyNodeId, standbyNodeName);
		log.info("Killing webdriver and starting a new session with virtual IP");
//		Thread.sleep(10000);
//		driver.close();
		
		cleanUpLogin(virtualIp);
		log.info("Getting node ID from UI and asserting if it matches");
		String nodeIdFromUI = cluster.getNodeInfoTableColumn(driver, standbyboxilla, CLUSTER_NODE_COLUMNS.NODE_ID);
		Assert.assertTrue(standbyNodeId.equals(nodeIdFromUI), "node id from UI did not match node id used "
				+ "to create standby,expected," + nodeIdFromUI + " actual " + nodeIdFromUI);
		log.info("Node ids match. Checking node name");
		String nodeNameFromUi = cluster.getNodeInfoTableColumn(driver, standbyboxilla, CLUSTER_NODE_COLUMNS.NODE_NAME);
		Assert.assertTrue(standbyNodeName.equals(nodeNameFromUi), "Node Name from UI did not match Node name "
				+ "used to create master, actual " + nodeNameFromUi);
		log.info("Checking node state for standby");
		String nodeStateFromUi = cluster.getNodeInfoTableColumn(driver, standbyboxilla, CLUSTER_NODE_COLUMNS.STATE);
		Assert.assertTrue(nodeStateFromUi.equals("standby"), "Node state was not standby, actual :" + nodeStateFromUi);

		log.info("Node name checked. Test complete");
	}

	
//	 * Test that will create a connection in the Active Boxilla
//	 * and check that the connection is replicated on the standby Boxillas
//	 * database
	 
	@Test(priority=3)
	public void test03_checkOldConnection() {
		log.info("Checking connection that existed in master is now on master and slave");
		Database masterDb = new Database();
		masterDb.connectToDatabase(boxilla, dbUsername, dataPassword, "foreman");
		invisaPC.Connection masterCon = masterDb.findConnectionByName(oldConnection);
		masterDb.closeDatabase();

		Database slaveDb = new Database();
		slaveDb.connectToDatabase(standbyboxilla, dbUsername, dataPassword, "foreman");
		invisaPC.Connection slaveCon = slaveDb.findConnectionByName(oldConnection);
		slaveDb.closeDatabase();

		Assert.assertTrue(masterCon.equals(slaveCon), "Old connection was not added to slave DB");
	}

	
//	 * Test that will check the standby boxillas database for a user
//	 * that existed in the active Boxilla before the cluster was set up
	 
	@Test(priority=4)
	public void test04_checkOldUser() {
		log.info("Checking user that existed in master is now on master and slave");
		Database masterDatabase = new Database();
		masterDatabase.connectToDatabase(boxilla,  dbUsername, dataPassword, "foreman");
		invisaPC.User masterUser = masterDatabase.findUserByName(oldUser);
		masterDatabase.closeDatabase();
		Database slaveDatabase = new Database();
		slaveDatabase.connectToDatabase(standbyboxilla, dbUsername, dataPassword, "foreman");
		invisaPC.User slaveUser = slaveDatabase.findUserByName(oldUser);
		slaveDatabase.closeDatabase();
		log.info("Master User:" + masterUser.toString());
		log.info("Slave User:" + slaveUser.toString());
		Assert.assertTrue(masterUser.equals(slaveUser), "Master did not equal slave, actual master user " + masterUser.toString() + 
				" slave user " + slaveUser.toString());

	}
	
	
//	 * Test that will check the standby boxillas database for a device that
//	 * existed on the active Boxillas database before the cluster was set up
	 
	 
	@Test(priority=5)
	public void test05_checkOldDevice() {
		log.info("Checking device that existed in master is now on master and slave");
		Database masterDatabase = new Database();
		masterDatabase.connectToDatabase(boxilla,  dbUsername, dataPassword, "foreman");
		invisaPC.Device device = masterDatabase.findDeviceByIp(txIp);
		masterDatabase.closeDatabase();
		log.info("Master device:" + device.toString());

		Database slaveDatabase = new Database();
		slaveDatabase.connectToDatabase(standbyboxilla, dbUsername, dataPassword, "foreman");
		invisaPC.Device slaveDevice = slaveDatabase.findDeviceByIp(txIp);
		slaveDatabase.closeDatabase();
		log.info("Slave Device:" + slaveDevice.toString());

		Assert.assertTrue(device.equals(slaveDevice), "Device on master did not match device on slave");
	}
	
	
//	 * Test that will compare the number of rows in each 
//	 * table on the active and standby Boxillas.
	 
	@Test(priority=6)
	public void test06_checkTableRowCount() {
		log.info("Checking both database tables for number of rows and comparing them");
		Database db = new Database();
		db.connectToDatabase(boxilla,  dbUsername, dataPassword, "foreman");
		HashMap map = db.getAllTableCount(db.getAllTableNames());

		Database db2 = new Database();
		db2.connectToDatabase(standbyboxilla,  dbUsername, dataPassword, "foreman");
		HashMap map2 = db2.getAllTableCount(db2.getAllTableNames());

		Assert.assertTrue(map.equals(map2), "Row counts for both databases did not match");	

	}	
	

	
//	 * Test that creates a user in the active boxilla and
//	 * checks the standby boxillas database for this user
//	 * @throws InterruptedException
	
	@Test(priority=7)
	public void test07_createUser() throws InterruptedException {
		cleanUpLogin(boxilla);
		log.info("Creating user and checking user is in both databases");
		String userName = "clusterUser";
		users.addUser(driver, userName, "admin", "admin");
		Thread.sleep(2000);
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");

		users.addUserNoTemplateAutoConnectOFF(driver, userName);
		//check user it both boxilla databases
		log.info("Checking if user exists in master and standby DB");
		Database masterDatabase = new Database();
		masterDatabase.connectToDatabase(boxilla, dbUsername, dataPassword, "foreman");
		invisaPC.User masterUser = masterDatabase.findUserByName(userName);
		masterDatabase.closeDatabase();
		Database slaveDatabase = new Database();
		slaveDatabase.connectToDatabase(standbyboxilla,dbUsername, dataPassword, "foreman");
		invisaPC.User slaveUser = slaveDatabase.findUserByName(userName);
		slaveDatabase.closeDatabase();

		Assert.assertTrue(masterUser.equals(slaveUser), "Master did not equal slave, actual master user " + masterUser.toString() + 
				" slave user " + slaveUser.toString());
		log.info("Slave and master database entries for users matched");
	}
	
//	 * Test that creates a connection in the active boxilla
//	 * and check the standby Boxillas database for this connection
//	 * @throws InterruptedException
	 
	@Test(priority=8)
	public void test08_createConnection() throws InterruptedException {
		log.info("Creating connection and comparing databases");
		cleanUpLogin(boxilla);
		String connectionName = "clusterConnection";
		//create the connection
		connection.addConnection(driver, connectionName, "no"); // connection name, user template
		connection.connectionInfo(driver, "tx", "user","user", txIp); // connection via, name, host ip
		connection.chooseCoonectionType(driver, "private"); // connection type
		connection.enableExtendedDesktop(driver);
		connection.enableUSBRedirection(driver);
		connection.enableAudio(driver);
		connection.enablePersistenConnection(driver);
		connection.propertyInfoClickNext(driver);
		connection.saveConnection(driver, connectionName); // Connection name to assert

		Database masterDb = new Database();
		masterDb.connectToDatabase(boxilla, dbUsername, dataPassword, "foreman");
		invisaPC.Connection masterCon = masterDb.findConnectionByName(connectionName);
		masterDb.closeDatabase();

		Database slaveDb = new Database();
		slaveDb.connectToDatabase(standbyboxilla,dbUsername, dataPassword, "foreman");
		invisaPC.Connection slaveCon = slaveDb.findConnectionByName(connectionName);
		slaveDb.closeDatabase();

		log.info("Asserting if both connection objects match");
		Assert.assertTrue(masterCon.equals(slaveCon), "Master and salve connection objects did not match, actual master : " + 
				masterCon.toString() + " , slave : " + slaveCon.toString());
	}

	
//	@Test(priority=9)
	public void test09_masterBoxillaDown() throws InterruptedException {
		log.info("Closing browser");
		driver.quit();
		//first copy the script and run it
		Thread t = new Thread() {
			public void run() {
				boxillaDown(boxilla);
			}
		};
		t.start();
		log.info("Master boxilla has been brought down for 5 minutes. Waiting 3 minutes then checking");
		Thread.sleep(300000);
		cleanUpLogin(virtualIp);
		cleanUpLogout();
		driver.quit();
		boxillaManager=boxillaManager2;
	}

	

	private void boxillaDown(String boxillaIp) {
		Ssh shell = new Ssh("root", "barrow1admin_12", boxillaIp);
		shell.loginToServer();
		shell.sendCommandNoReturn("ifdown p3p1 && sleep 300 && ifup p3p1 &");
		log.info("done");
		boxillaManager=boxillaManager2;
	}

	

	@Test(priority=10)
	public void test10_upgradeCluster () throws InterruptedException {
		cleanUpLogin(virtualIp);
		log.info("Attempting to upgrade cluster");
		log.info("Detaching standby first");
	//	log.info("current boxilla is "+boxillaManager);
		cluster.searchNodeInfo(driver, "standby");
		log.info("Getting IP Address from table");
//		//driver.navigate().refresh();
		Thread.sleep(2000);
		String detachstandby = Cluster.getNodeInfoTableColumn(driver, 1).getText();
		System.out.println("Standby IP is "+detachstandby);
		cluster.detachStandBy(driver, detachstandby);
		Thread.sleep(2000);
		String nodeStateFromUi = cluster.getNodeInfoTableColumn(driver, detachstandby, CLUSTER_NODE_COLUMNS.STATE);  
		Assert.assertTrue(nodeStateFromUi.equals("detached"), "Node state was not detached, actual :" + nodeStateFromUi);  //check 147 detatched
		log.info("Standby detached. Logging into standby boxilla and upgrading");
		driver.close();			//quit browser
		Thread.sleep(100000);
		cleanUpLogin(detachstandby);
		system.systemUpgrade(driver, boxillaVersion, detachstandby);  
		cluster.switchoverBoxilla(driver);
		Thread.sleep(100000);
		cleanUpLogin(virtualIp);
		cluster.searchNodeInfo(driver, "active");
		log.info("Getting active IP Address from table");
		Thread.sleep(2000);
		String boxillaManager1 = Cluster.getNodeInfoTableColumn(driver, 1).getText();
		System.out.println("Active Boxilla is  "+	boxillaManager1);
		String nodeStateFromUiActive = cluster.getNodeInfoTableColumn(driver, boxillaManager1, CLUSTER_NODE_COLUMNS.STATE);
		Assert.assertTrue(nodeStateFromUiActive.equals("active"), "Node state was not active, actual :" + nodeStateFromUiActive);
		log.info("Standby has been upgraded and is now master boxilla. Detaching new standby to upgrade");
		driver.close();
		cleanUpLogin(boxillaManager);
		system.systemUpgrade(driver, boxillaVersion, boxillaManager);	
		Thread.sleep(5000);
		cluster.prepareStandByBoxilla(driver, detachstandby, "4", "bxa4");
		driver.close();
		cleanUpLogin(virtualIp);
		String nodeStateFromUiActive3 = cluster.getNodeInfoTableColumn(driver, boxillaManager, CLUSTER_NODE_COLUMNS.STATE);
		Assert.assertTrue(nodeStateFromUiActive3.equals("standby"), "Node state was not standby, actual :" + nodeStateFromUiActive3);
		driver.close();
		cleanUpLogin(virtualIp);
		cluster.searchNodeInfo(driver, "standby");
		log.info("Attampting to Switchover Standby to active");
		log.info("Getting IP Address from table");
		Thread.sleep(5000);
		String switchoverToActiveNode = Cluster.getNodeInfoTableColumn(driver, 1).getText();
		System.out.println("Standby IP is "+switchoverToActiveNode);
		Thread.sleep(3000);
		cluster.switchoverToActive(driver, switchoverToActiveNode);
		String nodeStateFromUi1 = cluster.getNodeInfoTableColumn(driver, switchoverToActiveNode, CLUSTER_NODE_COLUMNS.STATE);
		Assert.assertTrue(nodeStateFromUi1.equals("active"), "Node state was not Activated, actual :" + nodeStateFromUi1);
		driver.close();
		log.info("Waiting for original IP address to become active before trying to log in and out");
		Thread.sleep(120000);		
	}

	@Test(priority=11)
	public void test11_detachStandBy() throws InterruptedException {
		String detachstandby;
		cleanUpLogin(virtualIp);
		log.info("Attempting to detach standby boxilla");
		log.info("Detaching standby first");
		driver.navigate().refresh();
		Thread.sleep(3000);
		
		cluster.searchNodeInfo(driver, "standby");
		log.info("Getting IP Address from table");
		Thread.sleep(5000);
		detachstandby = Cluster.getNodeInfoTableColumn(driver, 1).getText();
		System.out.println("Standby IP is "+detachstandby);
		Thread.sleep(3000);
		
		
		cluster.detachStandBy(driver, detachstandby);
		Thread.sleep(2000);
		log.info("Checking node state for standby");
		driver.navigate().refresh();
		Thread.sleep(3000);
		String nodeStateFromUi = cluster.getNodeInfoTableColumn(driver, detachstandby, CLUSTER_NODE_COLUMNS.STATE);  
		Assert.assertTrue(nodeStateFromUi.equals("detached"), "Node state was not detached, actual :" + nodeStateFromUi); 
	log.info("Boxilla has been detached. Checking if original IP is active");
	
	driver.close();
	log.info("Waiting for original IP address to become active before trying to log in and out");
	Thread.sleep(120000);
	
	
}
	
	@Test(priority=12)
	public void test12_reattachDetachedStandby() throws InterruptedException {
		cleanUpLogin(virtualIp);
		String detachedboxilla, ActiveBoxilla;
		log.info("Attempting to reattach standby boxilla");
		
			
			driver.navigate().refresh();
			cluster.searchNodeInfo(driver, "detached");
			Thread.sleep(3000);
			log.info("Getting IP Address from table");
			detachedboxilla = Cluster.getNodeInfoTableColumn(driver, 1).getText();
			String detachedNodeID = Cluster.getNodeInfoTableColumn(driver, 3).getText();
			String detachedNodeName = Cluster.getNodeInfoTableColumn(driver, 4).getText();
			System.out.println("Detached boxilla IP is "+detachedboxilla);
			System.out.println("Detached boxilla Node ID is "+detachedNodeID);
			System.out.println("Detached boxilla Node Name is "+detachedNodeName);
			
		
		
			driver.navigate().refresh();
			cluster.searchNodeInfo(driver, "active");
			Thread.sleep(3000);
			log.info("Getting IP Address of Active boxilla from table");
			ActiveBoxilla = Cluster.getNodeInfoTableColumn(driver, 1).getText();
			System.out.println("Active boxilla IP is "+ActiveBoxilla);
			
	cleanUpLogout();
	//driver.quit();
	cleanUpLogin(detachedboxilla);
	cluster.prepareStandByBoxilla(driver, ActiveBoxilla, detachedNodeID, detachedNodeName);
	cleanUpLogout();
	//driver.quit();
	cleanUpLogin(virtualIp);
	log.info("Getting node ID from UI and asserting if it matches");
	String nodeIdFromUI = cluster.getNodeInfoTableColumn(driver, detachedboxilla, CLUSTER_NODE_COLUMNS.NODE_ID);
	Assert.assertTrue(detachedNodeID.equals(nodeIdFromUI), "node id from UI did not match node id used "
			+ "to create standby, actual " + nodeIdFromUI);
	log.info("Node ids match. Checking node name");
	String nodeNameFromUi = cluster.getNodeInfoTableColumn(driver, detachedboxilla, CLUSTER_NODE_COLUMNS.NODE_NAME);
	Assert.assertTrue(detachedNodeName.equals(nodeNameFromUi), "Node Name from UI did not match Node name "
			+ "used to create master, actual " + nodeNameFromUi);
	log.info("Checking node state for standby");
	String nodeStateFromUi = cluster.getNodeInfoTableColumn(driver, detachedboxilla, CLUSTER_NODE_COLUMNS.STATE);
	Assert.assertTrue(nodeStateFromUi.equals("standby"), "Node state was not standby, actual :" + nodeStateFromUi);
	cleanUpLogout();
	
	//driver.quit();
	
	log.info("Waiting for original IP address to become active before trying to log in and out");
	Thread.sleep(120000);
}
	
	@Test(priority=13)
	public void test13_makeStandbyStandalone() throws InterruptedException {
		cleanUpLogin(virtualIp);
		String StandByBoxilla;
		
			driver.navigate().refresh();
			cluster.searchNodeInfo(driver, "standby");
			log.info("Getting IP Address of standby boxilla from table");
			Thread.sleep(3000);
			StandByBoxilla = Cluster.getNodeInfoTableColumn(driver, 1).getText();
			String StandByNodeID = Cluster.getNodeInfoTableColumn(driver, 3).getText();
			String StandByNodeName = Cluster.getNodeInfoTableColumn(driver, 4).getText();
			System.out.println("StandBy boxilla IP is "+StandByBoxilla);
			System.out.println("StandBy boxilla Node ID is "+StandByNodeID);
			System.out.println("StandBy boxilla Node Name is "+StandByNodeName);
			Thread.sleep(3000);
			
		 
		cluster.makeStandbyStandAlone(driver, StandByBoxilla);
		Thread.sleep(3000);
		String nodeIdFromUI = cluster.getNodeInfoTableColumn(driver, StandByBoxilla, CLUSTER_NODE_COLUMNS.NODE_ID);
		Assert.assertFalse(standbyNodeId.equals(nodeIdFromUI), "node id from UI contained standby details when it shouldnt have ");
		driver.close();
		
		log.info("Waiting for original IP address to become active before trying to log in and out");
		Thread.sleep(120000);
//		cleanUpLogin();
//		cleanUpLogout();
	}
	
	
	

	
	@Test(priority=14)
	public void test14_dissolveCluster() throws InterruptedException {
		log.info("Attempting to dissolve cluster");
		cluster.dissolveCluster(driver);
		driver.close();
		Thread.sleep(30000);
		boxillaManager = boxilla;
		cleanUpLogin();
		log.info("checking if VIP is still active");
		boolean vipActive = isIpReachable(virtualIp);	
		Assert.assertFalse(vipActive, "Virtual IP was still active. Dissolve cluster failed");

	}

	/**
	 * Overriding beforeClass in superclass to create connections, upload License to be used in tests.
	 * @throws InterruptedException 
	 * 
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		String originalBoxilla = boxillaManager;
		boxillaManager = boxillaManager2;
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//swap boxilla to set the license to be the same as original boxilla
			
		try {
			log.info(boxillaVersion);
			cleanUpLogin();
			system.deletLicense(driver);
			system.addUnlimitedLicense(driver,boxillaManager);
			//system.addLicense(driver, "25", "300", boxillaManager);
			cleanUpLogout();
			
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
			boxillaManager = originalBoxilla;
		
		}
		try {
			boxillaManager = originalBoxilla;
			log.info("current boxilla is "+boxillaManager);
			cleanUpLogin(boxillaManager);
			system.deletLicense(driver);
			system.addUnlimitedLicense(driver,boxillaManager);
			users.addUser(driver, oldUser, "admin", "admin");
			Users.userPrivilegeAdmin(driver).click();
			log.info("User Privilege - Administrator selected");
			users.addUserNoTemplateAutoConnectOFF(driver, oldUser);
			connection.addConnection(driver, oldConnection, "no"); // connection name, user template
			connection.connectionInfo(driver, "tx", "user","user", txIp); // connection via, name, host ip
			connection.chooseCoonectionType(driver, "private"); // connection type
			connection.enableExtendedDesktop(driver);
			connection.enableUSBRedirection(driver);
			connection.enableAudio(driver);
			connection.enablePersistenConnection(driver);
			connection.propertyInfoClickNext(driver);
			connection.saveConnection(driver, oldConnection); // Connection name to assert
			
			//system.addLicense(driver, "25", "300", boxillaManager);
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
			boxillaManager = originalBoxilla;
		
		}	
		cleanUpLogout();
		boxillaManager = originalBoxilla;
	
	}
	
	
	@AfterClass
	public void afterclass() 
	{
		String originalBoxilla = boxillaManager;
	   cleanUpLogin();	
		try {
			system.systemUpgrade1(driver, boxillaVersionNew, boxillaManager);
			driver.close();
			boxillaManager=boxillaManager2;
			cleanUpLogin(boxillaManager);	
			system.systemUpgrade1(driver, boxillaVersionNew, boxillaManager);
			driver.close();
			boxillaManager = originalBoxilla;
			
			
		} catch (Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
		}
		
		
		
		
	}
	
//	@Test(priority=2)
//	public void test10_bringFailedBoxillaOnline() throws InterruptedException {
//		cleanUpLogin(virtualIp);
//		Thread.sleep(100000);
//		String userName = "clusterUser2";
//		
//		log.info("Creating a user and then Bringing failed boxilla back online");
//		users.addUser(driver, userName, "admin", "admin");
//		Thread.sleep(2000);
//		Users.userPrivilegeAdmin(driver).click();
//		log.info("User Privilege - Administrator selected");
//		users.addUserNoTemplateAutoConnectOFF(driver, userName);
//		cluster.prepareStandbyFailedBoxilla(driver, boxilla);
//		String nodeStateFromUi = cluster.getNodeInfoTableColumn(driver, boxilla, CLUSTER_NODE_COLUMNS.STATE);
//		Assert.assertTrue(nodeStateFromUi.equals("standby"), "Node state was not standby, actual :" + nodeStateFromUi);
//		log.info("Checking database for user created while boxilla was down");
//
//		Database masterDatabase = new Database();
//		masterDatabase.connectToDatabase(boxilla, "postgres", "foreman", "foreman");
//		invisaPC.User masterUser = masterDatabase.findUserByName(userName);
//		masterDatabase.closeDatabase();
//		Database slaveDatabase = new Database();
//		slaveDatabase.connectToDatabase(boxillaManager2,"postgres", "foreman", "foreman");
//		invisaPC.User slaveUser = slaveDatabase.findUserByName(userName);
//		slaveDatabase.closeDatabase();
//
//		Assert.assertTrue(masterUser.equals(slaveUser), "Master did not equal slave, actual master user " + masterUser.toString() + 
//				" slave user " + slaveUser.toString());
//	}

}
