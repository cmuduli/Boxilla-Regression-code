package connection.emeraldOsd;

import static io.restassured.RestAssured.basic;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import invisaPC.Connection;
import invisaPC.Rest.Process;
import io.restassured.RestAssured;
import methods.DevicesMethods;
import methods.EmeraldOsd;

public class AddConnectionOsd extends StartupTestCase2 {
	
	private EmeraldOsd emerald;
	final static Logger log = Logger.getLogger(AddConnectionOsd.class);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		emerald = new EmeraldOsd(txIp, "root", "barrow1admin_12");
		
	}
	
	
	/**
	 * We dont need to log into boxilla after each method in this suite
	 * so override with an empty method
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	//@Parameters({ "browser" })
	public void login(Method method) {
	
	}
	
	@Test
	public void test01_makeTxConnectionAllOptionsEnabled() {
		String connectionName = "test01";
		String connectionIp = "10.212.123.121";
		emerald.createConnection(connectionName, connectionIp, "tx", "", "", "", "", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getName().equals(connectionName), "Connection name did not match. Actual, " + c.getName());
		Assert.assertTrue(c.getViaTx().equals("true"), "Connection was not via TX, actual " + c.getViaTx());
		Assert.assertTrue(c.getIp_address().equals(connectionIp), "Connection IP did not match. Actual, " + c.getIp_address());
		Assert.assertTrue(c.getPersistent().equals("true"), "Persistence was not enabled");
		Assert.assertTrue(c.getUsb_redirection().equals("Yes"), "USBR was not enabled");
	}
	
	@Test
	public void test02_makeTxConnectionUsbRdisabled() {
		String connectionName = "test02";
		String connectionIp = "192.168.1.2";
		emerald.createConnection(connectionName, connectionIp, "tx", "", "", "", "", false, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getName().equals(connectionName), "Connection name did not match. Actual, " + c.getName());
		Assert.assertTrue(c.getIp_address().equals(connectionIp), "Connection IP did not match. Actual, " + c.getIp_address());
		Assert.assertTrue(c.getPersistent().equals("true"), "Persistence was not enabled");
		Assert.assertTrue(c.getUsb_redirection().equals(""), "USBR was not enabled");
	}
	
	@Test
	public void test03_makeTxConnectionPersistenceDisabled() {
		String connectionName = "test03";
		String connectionIp = "152.221.656.84";
		emerald.createConnection(connectionName, connectionIp, "tx", "", "", "", "", true, false, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getPersistent().equals("false"), "Persistence was enabled");
		Assert.assertTrue(c.getUsb_redirection().equals("Yes"), "USBR was not enabled");
	}
	
	@Test
	public void test04_makeTxConnectionAllOptionsDisabled() {
		String connectionName = "test04";
		String connectionIp = "145.235.87.25";
		emerald.createConnection(connectionName, connectionIp, "tx", "", "", "", "", false, false, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getPersistent().equals("false"), "Persistence was enabled");
		Assert.assertTrue(c.getUsb_redirection().equals(""), "USBR was not enabled");
	}
	
	@Test
	public void test05_makeDirectConnectionAllOptionsEnabled() {
		String connectionName = "test05";
		String connectionIp = "111.220.254.363";
		emerald.createConnection(connectionName, connectionIp, "direct", "username", "password",
				"3939", "blackbox.com", false, false, true);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getName().equals(connectionName), "Connection name did not match, actual " + c.getName());
		Assert.assertTrue(c.getIp_address().equals(connectionIp), "IP address did not match, actual " + c.getIp_address());
		Assert.assertTrue(c.getUser_name().equals("username"), "Username did not match, actual " + c.getUser_name());
		Assert.assertTrue(c.getPassword().equals("password"), "Passwords did not match, actual " + c.getPassword());
		Assert.assertTrue(c.getAudio().equals("Yes"), "Audio did not match, actual " + c.getAudio());
		Assert.assertTrue(c.getDomain().equals("blackbox.com"), "Domain did not match, actual " + c.getDomain());
		Assert.assertTrue(c.getPort().equals("3939"), "Ports did not match, actual " + c.getPort());
		Assert.assertTrue(c.getViaTx().equals(""), "Via TX did not match, actual " + c.getViaTx());
		Assert.assertTrue(c.getHorizon().equals(""), "Horizon did not match, actual " + c.getHorizon());
	}
	
	@Test
	public void test06_makeDirectConnectionAllOptionsDisabled() {
			String connectionName = "test06";
			String connectionIp = "hostname";
			emerald.createConnection(connectionName, connectionIp, "direct", "username", "password",
					"3939", "blackbox.com", false, false, false);
			Connection c = emerald.getConnectionFromXml(connectionName);
			System.out.println(c.toString());
			Assert.assertTrue(c.getAudio().equals(""), "Audio did not match, actual " + c.getAudio());
	}
	@Test
	public void test07_makeHorizonConnection() {
		String connectionName = "test07";
		String connectionIp = "10.211.127.362";
		emerald.createConnection(connectionName, connectionIp, "horizon", "username", "password",
				"3939", "blackbox.com", false, false, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		Assert.assertTrue(c.getName().equals(connectionName), "Connection name did not match, actual " + c.getName());
		Assert.assertTrue(c.getIp_address().equals(connectionIp), "IP address did not match, actual " + c.getIp_address());
		Assert.assertTrue(c.getUser_name().equals("username"), "Username did not match, actual " + c.getUser_name());
		Assert.assertTrue(c.getPassword().equals("password"), "Passwords did not match, actual " + c.getPassword());
		Assert.assertTrue(c.getHorizon().equals("true"), "Horizon did not match, actual " + c.getHorizon());
	}
//	
	@Test
	public void test08_editConnectionName() {
		String connectionName = "test08";
		String newConnectionName = "newtest08";
		String connectionIp = "10.211.111.111";
		emerald.createConnection(connectionName, connectionIp, "tx", "username", "password",
				"3939", "blackbox.com", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection
		Assert.assertTrue(c.getName().equals(connectionName), "Connection name does not match");
		emerald.editConnection(1, "tx", "name", newConnectionName);
		//check first connection does not exist
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c == null, "Old connection still exists");
		//check new connection exists
		c  =emerald.getConnectionFromXml(newConnectionName);
		Assert.assertTrue(c.getName().equals(newConnectionName), "New Connection was not created");
	}
	
	@Test
	public void test09_editConnectionName() {
		String connectionName = "test09";
		String connectionIp = "10.211.111.111";
		String connectionIpNew = "10.211.111.112";
		emerald.createConnection(connectionName, connectionIp, "tx", "username", "password",
				"3939", "blackbox.com", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection ip
		Assert.assertTrue(c.getIp_address().equals(connectionIp), "IP address did not match");
		emerald.editConnection(1, "tx", "ip", connectionIpNew);
		//check IP change
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c.getIp_address().equals(connectionIpNew), "IP address did not change");
	}
//	
	@Test
	public void test10_editVmUsername() {
		String connectionName = "test10";
		String connectionIp = "10.211.111.111";
		String vmUsername = "user01";
		String vmUsernameNew = "user01new";
		emerald.createConnection(connectionName, connectionIp, "direct", vmUsername, "password",
				"3939", "blackbox.com", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection ip
		Assert.assertTrue(c.getUser_name().equals(vmUsername), "VM username did not match");
		emerald.editConnection(1, "direct", "username", vmUsernameNew);
		//check IP change
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c.getUser_name().equals(vmUsernameNew), "VM username did not change");
	}
	
	@Test
	public void test11_editVmPassword() {
		String connectionName = "test11";
		String connectionIp = "10.211.111.111";
		String vmUsername = "user01";
		String vmPassword = "password55";
		String vmPasswordNew = "password55new";
		emerald.createConnection(connectionName, connectionIp, "direct", vmUsername, vmPassword,
				"3939", "blackbox.com", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection ip
		Assert.assertTrue(c.getPassword().equals(vmPassword), "VM password did not match");
		emerald.editConnection(1, "direct", "password", vmPasswordNew);
		//check IP change
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c.getPassword().equals(vmPasswordNew), "VM password did not change");
	}
	
	@Test
	public void test12_editVmPort() {
		String connectionName = "test12";
		String connectionIp = "10.211.111.111";
		String vmUsername = "user01";
		String vmPassword = "password55";
		String vmPort = "4565";
		String vmPortNew = "2929";
		emerald.createConnection(connectionName, connectionIp, "direct", vmUsername, vmPassword,
				vmPort, "blackbox.com", true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection ip
		Assert.assertTrue(c.getPort().equals(vmPort), "VM port did not match");
		emerald.editConnection(1, "direct", "port", vmPortNew);
		//check IP change
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c.getPort().equals(vmPortNew), "VM port did not change");
	}
	
	@Test
	public void test13_editVmDomain() {
		String connectionName = "test13";
		String connectionIp = "10.211.111.111";
		String vmUsername = "user01";
		String vmPassword = "password55";
		String vmPort = "4565";
		String domain = "blackbox.com";
		String domainNew = "blackbox.comnew";
		emerald.createConnection(connectionName, connectionIp, "direct", vmUsername, vmPassword,
				vmPort, domain, true, true, false);
		Connection c = emerald.getConnectionFromXml(connectionName);
		System.out.println(c.toString());
		
		//check first connection ip
		Assert.assertTrue(c.getDomain().equals(domain), "VM domain did not match");
		emerald.editConnection(1, "direct", "domain", domainNew);
		//check IP change
		c = emerald.getConnectionFromXml(connectionName);
		Assert.assertTrue(c.getDomain().equals(domainNew), "VM domain did not change");
	}
	
	@Test
	public void test14_makeTxConnection() throws InterruptedException {
		String connectionName = "realConnect";
		String connectionIp = "10.212.129.86";
		emerald.createConnection(connectionName, connectionIp, "tx", "", "", "", "", true, true, false);
		emerald.connectToConnection(1);
		Thread.sleep(5000);
		Assert.assertTrue(emerald.checkForConnection(rxIp), "Freerdp script not running");
	}
	
	
	@Override
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		emerald.removeConnection(1);
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			log.info(result.getName() + " :FAIL");
		
		if(ITestResult.SKIP == result.getStatus())
			log.info(result.getName() + " :SKIP");
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	@Override
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		printSuitetDetails(true);
		//reboot the device
//	  DevicesMethods device = new DevicesMethods();
//	  device.rebootDeviceSSH(txIp, "root", "barrow1admin_12");
//	  device.rebootDeviceSSH(rxIp, "root", "barrow1admin_12");
	}
	

}
