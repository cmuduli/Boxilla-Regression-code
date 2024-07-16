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

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.XMLParserSAX;
import invisaPC.Rest.AlertsTransmitter.Logout;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import northbound.get.BoxillaHeaders;
import objects.Devices;
import testNG.Utilities;
/**
 * Class that contains all the tests for appliance REST API active_connections
 * @author Boxilla
 *
 */
public class ActiveConnections extends StartupTestCase {
	
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String jsonReturned;
	private String jsonReturnedTX;
	private String rxHostName = "RX_008C101B081E";
	final static Logger log = Logger.getLogger(ActiveConnections.class);
	
	
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald", "quick"})
	public void test01_verifyReturnCode () {
			log.info("***** test01_verifyReturnCode *****");
			int statusCode = given()
			 .header(getHead())
			.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
			.getStatusCode();
			log.info("RETURN: " + statusCode );
			Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
	}
	
	@Test(groups = {"rest", "emerald", "quick"})
	public void test01_verifyReturnCodeTransmitter () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given()
		 .header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.getStatusCode();
		log.info("RETURN: " + statusCode );
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	
	/**
	 * User InvisaPC rest API to check that the the transmitter
	 * is reporting back the right receiver ip address
	 */
	@Test(groups = {"rest", "smoke","emerald", "quick"})
	public void test02_transmitterRestCheckRecieverIp() {
		log.info("***** test02_transmitterRestCheckRecieverIp *****");
		log.info("Checking if rx_hostname is equal to " + rxIp);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.rx_hostname[0]", equalTo(rxIp));
	}
	
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct connection.name
	 */
	@Test(groups = {"rest","emerald"})
	public void test03_receiverRestCheckConnectionName() {
		log.info("***** test03_receiverRestCheckConnectionName *****");
		log.info("Checking if name is equal to Test_TX_Registry");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name[0]", equalTo("Test_TX_Registry"));
	}
	
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct MAC
	 */
	@Test(groups = {"rest","emerald"})
	public void test04_receiverRestCheckReceiverMac() {
		log.info("***** test04_receiverRestCheckReceiverMac *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.mac[0]", equalTo(rxSingle.getMac().toLowerCase()));
	}
	/**
	 * Uses invisaPC rest api to check that the transmitter is reporting back the correct MAC
	 */
	@Test(groups = {"rest","emerald"})
	public void test05_transmitterRestCheckTransmitterMac() {
		log.info("***** test05_transmitterRestCheckTransmitterMac *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.mac[0]", equalTo(txSingle.getMac().toLowerCase()));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct username
	 */
	@Test(groups = {"rest","emerald"})
	public void test06_verifyUsername() {
		log.info("***** test06_verifyUsername *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.username[0]", equalTo("admin"));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct hostname
	 */
	@Test(groups = {"rest"})
	public void test07_verifyRxHostname() {
		log.info("***** test07_verifyRxHostname *****");
		Assert.assertTrue(jsonReturned.contains("\"rx_hostname\":"), 
				"Check for \"rx_hostname\": failed");
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct tx_hostname
	 */
	@Test(groups = {"rest","emerald"})
	public void test08_verifyTxHostname() {
		log.info("***** test08_verifyTxHostname *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct device_type
	 */
	@Test(groups = {"rest","emerald"})
	public void test09_verifyDeviceType() {
		log.info("***** test09_verifyDeviceType *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.device_type[0]", equalTo("decoder"));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct connection_type
	 */
	@Test(groups = {"rest","emerald"})
	public void test10_verifyConnectionType() {
		log.info("***** test10_verifyConnectionType *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.connection_type[0]", equalTo(1));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct group_name
	 */
	@Test(groups = {"rest","emerald"})
	public void test11_verifyGroupName() {
		log.info("***** test11_verifyGroupName *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.group_name[0]", equalTo("group"));
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is reporting back the correct alert_state
	 */
	@Test(groups = {"rest","emerald"})
	public void test12_verifyAlertState() {
		log.info("***** test12_verifyAlertState *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.alert_state[0]", equalTo(0));
	}
	
	//parameter checks
	/**
	 * Uses invisaPC rest api to check that the receiver is returning duration field
	 */
	@Test(groups = {"rest","emerald"})
	public void test13_verifyDuration() {
		log.info("***** test13_verifyDuration *****");
		Assert.assertTrue(jsonReturned.contains("\"duration\":"), 
				"Check for \"duration\": failed");
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is returning start_time field
	 */
	@Test(groups = {"rest","emerald"}) 
	public void test14_verifyStartTime() {
		log.info("***** test14_verifyStartTime *****");
		Assert.assertTrue(jsonReturned.contains("\"start_time\":"), 
				"Check for \"start_time\": failed");
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is returning timestamp field
	 */
	@Test(groups = {"rest","emerald"}) 
	public void test15_verifyTimestamp() {
		log.info("***** test15_verifyTimestamp *****");
		Assert.assertTrue(jsonReturned.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed");
	}
	
	@Test(groups = {"rest","emerald"}) 
	public void test16_verifyIdReceiver() {
		log.info("***** test16_verifyIdReceiver *****");
		Assert.assertTrue(jsonReturned.contains("\"id\":"), 
				"Check for \"id\": failed");
	}
	
	@Test(groups = {"rest","emerald"}) 
	public void test17_verifyIdTransmitter() {
		log.info("***** test17_verifyIdTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"id\":"), 
				"Check for \"id\": failed");
	}
	
	@Test(groups = {"rest","emerald"}) 
	public void test18_verifyConnectionIdReceiver() {
		log.info("***** test18_verifyConnectionIdReceiver *****");
		Assert.assertTrue(jsonReturned.contains("\"connection_id\":"), 
				"Check for \"connection_id\": failed");
	}
	
	@Test(groups = {"rest","emerald"}) 
	public void test19_verifyConnectionIdTransmitter() {
		log.info("***** test19_verifyConnectionIdTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"connection_id\":"), 
				"Check for \"connection_id\": failed");
	}
	
	@Test(groups = {"rest","emerald"})
	public void test20_TransmitterRestCheckConnectionName() {
		log.info("***** test20_TransmitterRestCheckConnectionName *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name[0]", equalTo("unknown"));
	}
	
	@Test(groups = {"rest","emerald"}) 
	public void test21_checkTx_hostNameTransmitter() {
		log.info("***** test21_checkTx_hostNameTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"tx_hostname\":"), 
				"Check for \"tx_hostname\": failed");
	}
	
	@Test(groups = {"rest","emerald"})
	public void test22_verifyDeviceTypeTransmitter() {
		log.info("***** test22_verifyDeviceTypeTransmitter *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.device_type[0]", equalTo("encoder"));
	}
	
	@Test(groups = {"rest","emerald"})
	public void test23_verifyConnectionTypeTransmitter() {
		log.info("***** test23_verifyConnectionTypeTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"connection_type\":"), 
				"Check for \"connection_type\": failed");
	}
	
	@Test(groups = {"rest","emerald"})
	public void test24_verifyGroupNameTransmitter() {
		log.info("***** test24_verifyGroupNameTransmitter *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.group_name[0]", equalTo("group"));
	}

	@Test(groups = {"rest","emerald"})
	public void test25_verifyAlertStateTransmitter() {
		log.info("***** test25_verifyAlertStateTransmitter *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.alert_state[0]", equalTo(0));
	}
	
	@Test(groups = {"rest","emerald"})
	public void test26_verifyDurationTransmitter() {
		log.info("***** test26_verifyDurationTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"duration\":"), 
				"Check for \"duration\": failed");
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is returning start_time field
	 */
	@Test(groups = {"rest","emerald"}) 
	public void test27_verifyStartTimeTransmitter() {
		log.info("***** test27_verifyStartTimeTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"start_time\":"), 
				"Check for \"start_time\": failed");
	}
	/**
	 * Uses invisaPC rest api to check that the receiver is returning timestamp field
	 */
	@Test(groups = {"rest","emerald"}) 
	public void test28_verifyTimestampTransmitter() {
		log.info("***** test28_verifyTimestampTransmitter *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed");
	}
	
	@Test(groups = {"rest","emerald"})
	public void test29_verifyUsernameTransmitter() {
		log.info("***** test29_verifyUsernameTransmitter *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.username[0]", equalTo("unknown"));
	}
	
	/**
	 * Overriding superclass method to create a test specific connection
	 * @throws InterruptedException 
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		printSuitetDetails(false);
		if(isEmerald) {
			rxHostName = "RX_008C101ECA98";
		}
		try {
			
			log.info("Starting test setup....");
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			log.info("Attempting to make a real connection");
			conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp) ;
			
			log.info("Sleeping while test attached");
			Thread.sleep(10000);
			log.info("Returns from active connections");
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
			jsonReturnedTX = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections").asString();
			log.info("RETURN: " + jsonReturned);
			log.info("RETURN TX: " + jsonReturnedTX);
			if(!jsonReturned.contains("id") && !jsonReturnedTX.contains("id")) {
				log.info("One of the returned did not have any values. Retrying in 30 seconds");
				Thread.sleep(30000);
				int x = 0;
				int y = 0;
				while(x < 100) {
				jsonReturned = given().header(getHead())
						.when()
						.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
				log.info("RETURN: " + jsonReturned);
				if(jsonReturned.contains("kvm_active_connections")) {
					x = 100;
				}else {
					log.info("rx rest was not initialised. Waiting and trying again. " + x);
					Thread.sleep(1000);
					x++;
				}
				}
				while(y < 100) {
					jsonReturnedTX = given().header(getHead())
							.when()
							.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections").asString();
					log.info("RETURN TX: " + jsonReturnedTX);
					if(jsonReturnedTX.contains("kvm_active_connections")) {
						y = 100;
					}else {
						log.info("tx rest was not initialised. Waiting and trying again. " + y);
						Thread.sleep(1000);
						y++;
					}
				}
			}
			
		}catch(Exception | AssertionError e) {
			log.info("Before Class threw an error. Waiting 30 seconds and trying once more");
			Thread.sleep(30000);
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
			jsonReturnedTX = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections").asString();
			log.info("RETURN: " + jsonReturned);
			log.info("RETURN TX: " + jsonReturnedTX);
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			//cleanUpLogout();
		}
	}
	public class Logout {
		public String username;
		public String[] rx_list;
		public String forced;
	}
	
//	@AfterClass
	public void afterclass() throws InterruptedException{
		String username = "admin";
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "No";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		log.info("logout from the RX");
		
	}
	

	
	
	/**
	 * Overriding superclass method as login to boxilla is not needed
	 */
	@Override
	@BeforeMethod
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	/**
	 * Overriding superclass method as logout of boxilla is not needed
	 */
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

	
}
