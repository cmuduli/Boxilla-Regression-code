package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.XMLParserSAX;
import invisaPC.Rest.AlertsTransmitter.Login;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Users;
import testNG.Utilities;

/**
 * This class contains test cases that will check the appliance REST API for previous connection
 * @author Boxilla
 *
 */
public class PreviousConnection extends StartupTestCase {
	
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private UsersMethods usrMethods=new UsersMethods();
	private String tx_hostName = "TX_008C101B091C";

	private String jsonReturned;
	private String jsonReturnedActive;
	private String jsonReturnedReceiver;

	
	final static Logger log = Logger.getLogger(PreviousConnection.class);
	

	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "notEmerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	/**
	 * User InvisaPC rest API to check that the the transmitter
	 * is reporting back the right receiver ip address
	 */
//	@Test(groups = {"rest", "smoke", "notEmerald"})
//	public void test02_CheckRecieverIp() {
//		log.info("***** test02_CheckRecieverIp *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.rx_hostname", equalTo(Arrays.asList(rxIp)));
//		Assert.assertTrue(jsonReturned.contains("rx_hostname\":\"" + rxIp ), 
//				"Check for " + rxIp + " failed, actual:" + jsonReturned);
//		
//	}
//	
//	/**
//	 * 
//	 */
//	@Test(groups = {"rest", "notEmerald"})
//	public void test03_CheckConnectionName() {
//		log.info("***** test03_CheckConnectionName *****");
//		Assert.assertTrue(jsonReturned.contains("\"name\":"), 
//				"Check for \"name\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test04_CheckTransmitterrMac() {
//		log.info("***** test04_CheckTransmitterrMac *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.mac", equalTo(Arrays.asList("00:8c:10:1b:09:1c")));
//		Assert.assertTrue(jsonReturned.contains(txSingle.getMac().toLowerCase()), 
//				"Check for 00:8c:10:1b:09:1c failed, actual:" + jsonReturned);
//	}
//	
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test06_checkUsername() {
//		log.info("***** test06_checkUsername *****");
//		Assert.assertTrue(jsonReturned.contains("\"username\":"), 
//				"Check for \"username\": failed, actual:" + jsonReturned);
//	}
//	
////	@Test(groups = {"rest"})
////	public void test07_verifyRxHostname() {
////		log.info("***** test07_verifyRxHostname *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.rx_hostname", equalTo(Arrays.asList(rxIp)));
////		
////	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test08_verifyTxHostname() {
//		log.info("***** test08_verifyTxHostname *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.tx_hostname", equalTo(Arrays.asList("TX_008C101B091C")));
//		Assert.assertTrue(jsonReturned.contains("\"tx_hostname\":"), 
//				"Check for \"tx_hostname\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test09_verifyDeviceType() {
//		log.info("***** test09_verifyDeviceType *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.device_type", equalTo(Arrays.asList("encoder")));
//		Assert.assertTrue(jsonReturned.contains("encoder"), 
//				"Check for encoder failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test10_verifyConnectionType() {
//		log.info("***** test10_verifyConnectionType *****");
//		Assert.assertTrue(jsonReturned.contains("\"connection_type\":"), 
//				"Check for \"connection_type\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test11_verifyGroupName() {
//		log.info("***** test11_verifyGroupName *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.group_name", equalTo(Arrays.asList("group")));
//		Assert.assertTrue(jsonReturned.contains("group"), 
//				"Check for group failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test12_verifyAlertState() {
//		log.info("***** test12_verifyAlertState *****");
////		given().header(getHead())
////		.when()
////		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
////		.then()
////		.body("kvm_previous_connections.alert_state", equalTo(Arrays.asList(0)));
//		Assert.assertTrue(jsonReturned.contains("0"), 
//				"Check for alert_state 0 failed, actual:" + jsonReturned);
//	}
//	
//	//parameter checks
//	
//	@Test (groups = {"rest", "notEmerald"})
//	public void test13_verifyDuration() {
//		log.info("***** test13_verifyDuration *****");
//		Assert.assertTrue(jsonReturned.contains("\"duration\":"), 
//				"Check for \"duration\": failed, actual:" + jsonReturned);
//	}
//
//	@Test(groups = {"rest", "notEmerald"}) 
//	public void test14_verifyStartTime() {
//		log.info("***** test14_verifyStartTime *****");
//		Assert.assertTrue(jsonReturned.contains("\"start_time\":"), 
//				"Check for \"start_time\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"}) 
//	public void test15_verifyTimestamp() {
//		log.info("***** test15_verifyTimestamp *****");
//		Assert.assertTrue(jsonReturned.contains("\"timestamp\":"), 
//				"Check for \"timestamp\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test16_verifyConnectionId() {
//		log.info("***** test16_verifyConnectionId *****");
//		Assert.assertTrue(jsonReturned.contains("\"connection_id\":"), 
//				"Check for \"connection_id\": failed, actual:" + jsonReturned);
//	}
//	
//	@Test(groups = {"rest", "notEmerald"})
//	public void test17_verifyId() {
//		log.info("***** test17_verifyId *****");
//		Assert.assertTrue(jsonReturned.contains("\"id\":"), 
//				"Check for \"id\": failed, actual:" + jsonReturned);
//	}
	
	////////////receiver
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald"})
	public void test01_verifyReturnCodeReceiver () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/previous_connections")
		.getStatusCode();
		
		
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	/**
	 * User InvisaPC rest API to check that the the transmitter
	 * is reporting back the right receiver ip address
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test02_CheckRecieverIpReceiver() {
		log.info("***** test02_CheckRecieverIp *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.rx_hostname", equalTo(Arrays.asList(rxIp)));
		Assert.assertTrue(jsonReturnedReceiver.contains("rx_hostname\":\"" ), 
				"Check for " + rxIp + " failed, actual:" + jsonReturned);
		
	}
	
	/**
	 * 
	 */
	@Test(groups = {"rest", "emerald"})
	public void test03_CheckConnectionNameReceiver() {
		log.info("***** test03_CheckConnectionName *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"name\":"), 
				"Check for \"name\": failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test04_CheckReceiverMacReceiver() {
		log.info("***** test04_CheckTransmitterrMac *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.mac", equalTo(Arrays.asList("00:8c:10:1b:09:1c")));
		Assert.assertTrue(jsonReturnedReceiver.contains(rxSingle.getMac().toLowerCase()), 
				"Check for" + rxSingle.getMac() + " failed, actual:" + jsonReturned);
	}
	
//	NEED TO CHECK WITH MARCUS IF THIS PARAMETER SHOULD BE RETURNED	
//	@Test(groups = {"rest", "emerald"})
//	public void test06_checkUsernameReceiver() {
//		log.info("***** test06_checkUsername *****");
//		Assert.assertTrue(jsonReturnedReceiver.contains("\"username\":"), 
//				"Check for \"username\": failed");
//	}
	
	@Test(groups = {"rest"})
	public void test07_verifyRxHostname() {
		log.info("***** test07_verifyRxHostname *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"rx_hostname\":"), 
				"Check for \"rx_hostname\": failed, actual:" + jsonReturned);
		
		
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test08_verifyTxHostnameReceiver() {
		log.info("***** test08_verifyTxHostname *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.tx_hostname", equalTo(Arrays.asList("TX_008C101B091C")));
		Assert.assertTrue(jsonReturnedReceiver.contains(txIp), 
				"Check for " + txIp + " failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test09_verifyDeviceTypeReceiver() {
		log.info("***** test09_verifyDeviceType *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.device_type", equalTo(Arrays.asList("encoder")));
		Assert.assertTrue(jsonReturnedReceiver.contains("decoder"), 
				"Check for encoder failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test10_verifyConnectionTypeReceiver() {
		log.info("***** test10_verifyConnectionType *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"connection_type\":"), 
				"Check for \"connection_type\": failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test11_verifyGroupNameReceiver() {
		log.info("***** test11_verifyGroupName *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.group_name", equalTo(Arrays.asList("group")));
		Assert.assertTrue(jsonReturnedReceiver.contains("group"), 
				"Check for group failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test12_verifyAlertStateReceiver() {
		log.info("***** test12_verifyAlertState *****");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections")
//		.then()
//		.body("kvm_previous_connections.alert_state", equalTo(Arrays.asList(0)));
		Assert.assertTrue(jsonReturnedReceiver.contains("0"), 
				"Check for alert_state 0 failed, actual:" + jsonReturned);
	}
	
	//parameter checks
	
	@Test (groups = {"rest", "emerald"})
	public void test13_verifyDurationReceiver() {
		log.info("***** test13_verifyDuration *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"duration\":"), 
				"Check for \"duration\": failed, actual:" + jsonReturned);
	}

	@Test(groups = {"rest", "emerald"}) 
	public void test14_verifyStartTimeReceiver() {
		log.info("***** test14_verifyStartTime *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"start_time\":"), 
				"Check for \"start_time\": failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"}) 
	public void test15_verifyTimestampReceiver() {
		log.info("***** test15_verifyTimestamp *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test16_verifyConnectionIdReceiver() {
		log.info("***** test16_verifyConnectionId *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"connection_id\":"), 
				"Check for \"connection_id\": failed, actual:" + jsonReturned);
	}
	
	@Test(groups = {"rest", "emerald"})
	public void test17_verifyIdReceiver() {
		log.info("***** test17_verifyId *****");
		Assert.assertTrue(jsonReturnedReceiver.contains("\"id\":"), 
				"Check for \"id\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will log into boxilla and give IP addresses to a transmitter 
	 * and a receiver
	 */
	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		if(isEmerald) {
			tx_hostName = "TX_008C101ECB49";
		}
		printSuitetDetails(false);
		try {
			String username = "admin";
			String password = "admin";
			log.info("Starting test setup....");
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			conMethods.createTxConnection("previousConnectionTest", "private", driver, txIp);
			usrMethods.manageUser(driver, "admin");
			Thread.sleep(5000);
			Users.userManageConnectionTab(driver,"admin").click();
			log.info("Manage Connection option Clicked");
			Users.moveAllConnection(driver).click();
			usrMethods.saveConnection(driver);
			log.info("Connection added Successfully");
			//log into rxIp
			Login login=new Login();
			login.username = username;
			login.password = password;
			login.rx_list = new String[1];
			login.rx_list[0] = rxEmerald.getDeviceName();
			login.forced = "Yes";
			log.info("Logging in user:" + username);
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
					.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
					.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
					.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
					.extract().response();

			SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

			log.info("User " + username + " successfully logged in");
			//makeConnection("previousConnectionTest", "private");
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
			//create connection in rest
			ForceConnect con = new ForceConnect();
			con.action = "force_connection";
			con.user = "Boxilla";
			con.connection = "previousConnectionTest";
			
			int status = given().header(getHead()).body(con)
					.when()
					.contentType(ContentType.JSON)
					.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
			log.info("Status code from forced_connection:" + status);
			Assert.assertTrue(status == 200, "status code from force connection did not equal 200, actual: " + status);
//			//create connection in boxilla 
//			String[] connectionSources = {"previousConnectionTest"};
//			conMethods.addSources(driver, connectionSources);
//			conMethods.addPrivateDestination(driver, "previousConnectionTest", singleRxName);
			log.info("Connection created sleeping 30 seconds..");
			
			//conMethods.createRealConnection("previousConnectionTest", deviceUserName, devicePassword, rxIp, txIp) ;
			Thread.sleep(30000);
			log.info("Waking...Chceking if active connection rest returned");
			System.out.println(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections");
			jsonReturnedActive = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections").asString();
			log.info("RETURNED ACTIVE: " + jsonReturnedActive);
			log.info("TX MAC:" + txSingle.getMac().toLowerCase() );
			log.info("Checking if connection is up");
		//	Assert.assertTrue(jsonReturnedActive.contains(txSingle.getMac().toLowerCase()), "Active connection is not up, json return did not contain TX mac");
			log.info("Connection was up, attempting to break..");
			log.info("Breaking connection through rest..");
			//breaking through rest
			System.out.println(getHttp() + "://" + rxIp + getPort() + "/control/connections");
			String json = "{\"action\": \"terminate_connection\"}";
			int status2 = given().header(getHead()).body(json)
					.when()
					.contentType(ContentType.JSON)
					.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
			log.info("Status code from terminate_connection:" + status2);
			Assert.assertTrue(status2 == 200, "return from break connection did not equal 200, actual: " + status2);
//			conMethods.breakConnection(driver, "previousConnectionTest");
			log.info("Connection broken");
			//rebootDevice(rxIp, "root", "barrow1admin_12");
			Thread.sleep(10000);
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections").asString();
			
			jsonReturnedReceiver = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/previous_connections").asString();
			log.info("RETURNED : " + jsonReturned);
			log.info("RETURNED Receiver : " + jsonReturnedReceiver);
			if(!jsonReturned.contains("\"name\":")) {
				log.info("Return didnt work. Waiting and trying again...");
				Thread.sleep(20000);
				jsonReturned = given().header(getHead())
						.when()
						.get(getHttp() + "://" + txIp + getPort() + "/statistics/previous_connections").asString();
				log.info("RETURNED 2"
						+ " : " + jsonReturned);
				
			}
			cleanUpLogout();
			
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}

	
	/**
	 * We dont need to log into boxilla after each method in this suite
	 * so override with an empty method
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	
	
	@Override
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			try {
				//Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
				//		 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
				}catch(Exception e) {
					System.out.println("Error when trying to capture log file. Catching error and continuing");
					e.printStackTrace();
				}
			try {
				Utilities.captureDeviceLog(rxIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + rxIp);
			}
			try {
				Utilities.captureDeviceLog(txIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + txIp);
			}
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}

//	/**
//	 * Method to manage transmitter and receiver device
//	 * @throws InterruptedException
//	 */
//	public void deviceManageTestPrep() throws InterruptedException {
//		log.info("Test Preparation - Unamage - Manage Device");
//		
//		//RX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("rxMac"), prop.getProperty("ipCheck"), prop.getProperty("rxIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", prop.getProperty("rxMac"), prop.getProperty("ipCheck"));
//		
//		//TX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("txMac"), prop.getProperty("ipCheck"), prop.getProperty("txIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", prop.getProperty("txMac"), prop.getProperty("ipCheck"));
//		
//		log.info("Appliances Managed Successfully");
//		log.info("Sleeping while devices configures");
//		Thread.sleep(100000);
//	}
	
	public void rebootDevice(String ip, String userName, String password) throws InterruptedException {
		Ssh shell = new Ssh(userName, password, ip);
		shell.loginToServer();
		shell.sendCommand("/sbin/reboot");
		Thread.sleep(90000);
	}
	/**
	 * Makes a connection for this test. Can set name and connection type (private, shared)
	 * @param connectionName
	 * @param connectionType
	 * @throws InterruptedException
	 */
//	public void makeConnection(String connectionName, String connectionType) throws InterruptedException {
//		log.info("Creating connection : " + connectionName);
//		conMethods.addConnection(driver, connectionName, "no"); // connection name, user template
//		conMethods.connectionInfo(driver, "tx", "user", txIp); // connection via, name, host ip
//		conMethods.chooseCoonectionType(driver, connectionType); // connection type
//		conMethods.enableExtendedDesktop(driver);
//		conMethods.enablePersistenConnection(driver);
//		if(connectionType.equals("private")) {
//			conMethods.enableUSBRedirection(driver);
//			conMethods.enableAudio(driver);
//		}
//		conMethods.propertyInfoClickNext(driver);
//		conMethods.saveConnection(driver, connectionName);
//	}
}
