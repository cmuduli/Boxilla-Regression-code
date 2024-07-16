package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
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
import invisaPC.Rest.AlertsTransmitter.Logout;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import northbound.get.BoxillaHeaders;
import testNG.Utilities;

/**
 * Class that contains the tests for appliance REST API authentication 
 * @author Brendan O'Regan
 *
 */

public class Authentication extends StartupTestCase {

	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String jsonReturned;	
	final static Logger log = Logger.getLogger(Authentication.class);

	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest","emerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
//	
	/**
	 * Will check that the ID field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test02_verifyId() {
		log.info("***** test02_verifyId *****");
		Assert.assertTrue(jsonReturned.contains("\"id\":"),
				"Check for \"id\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that the mac address returned is the same as the device MAC address
	 */
	@Test(groups = {"rest", "smoke","emerald"})
	public void test03_verifyMacAddress() {
		log.info("***** test03_verifyMacAddress *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication")
		.then().assertThat().statusCode(200)
		.body("kvm_authentication_stats.mac[0]", equalTo(rxSingle.getMac().toLowerCase()));
	}
	
	/**
	 * Will check that the successful_logins field is returned 
	 */
	@Test(groups = {"rest","emerald"})
	public void test04_verifySuccessfulLogins() {
		log.info("***** test04_verifySuccessfulLogins *****");
		Assert.assertTrue(jsonReturned.contains("\"successful_logins\":"), 
				"Check for \"successful_logins\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that the refused logins field is returned 
	 */
	@Test(groups = {"rest","emerald"})
	public void test05_verifyRefusedLogins() {
		log.info("***** test05_verifyRefusedLogins *****");
		Assert.assertTrue(jsonReturned.contains("\"refused_logins\":"), 
				"Check for \"refused_logins\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * will check that the manager unreachable field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test06_verifyManagerUnreachable() {
		log.info("***** test06_verifyManagerUnreachable *****");
		Assert.assertTrue(jsonReturned.contains("\"manager_unreachable\":"), 
				"Check for \"manager_unreachable\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that the timestamp field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test07_verifyTimestamp() {
		log.info("***** test07_verifyTimestamp *****");
		Assert.assertTrue(jsonReturned.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed, actual:" + jsonReturned);
	}
	/**
	 * Uses invisaPCs rest api to check flush authentication stats
	 */
	@Test(groups = {"rest","emerald"})
	public void test08_flushAuthentication() {
		int status = given().header(getHead()).body("{\"action\": \"flush_authentication_stats\"}")
				.when()
				.contentType(ContentType.JSON)
				.put("https://" + rxIp + ":8888/control").andReturn().statusCode();
		log.info("Return code from flush : " + status);
		String authReturn = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication").asString();
		Assert.assertFalse(authReturn.contains("\"id\":"), "Flush did not work. Rest returned values: " + authReturn);
		
	}
	
	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
		
	/**
	 * Overriding superclass method to add device reboot
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
//		String username = "admin";
//		String password = "admin";
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("Starting test setup for " + this.getClass().getSimpleName());
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			deviceMethods.rebootDevice(driver, rxIp);
		
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication").asString();
			log.info("RETURNED: " + jsonReturned);
			
			int count=0;
			while (count<10) { 
				if(jsonReturned.contains("id")) {
					break;
				}
				else
				{
					log.info("JSON returned was empty. Waiting and trying again");
					deviceMethods.rebootDevice(driver, rxIp);
					jsonReturned = given().header(getHead())
							.when()
							.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication").asString();;
					log.info("RETURNED: " + jsonReturned);
				}
				count++;
				
			}
			cleanUpLogout();
			
			/*if(!jsonReturned.contains("id")) {
				log.info("JSON returned was blank. Waiting and trying again");
				Thread.sleep(30000);
				deviceMethods.rebootDevice(driver, rxIp);

				jsonReturned = given().header(getHead())
						.when()
						.get(getHttp() + "://" + rxIp + getPort() + "/statistics/authentication").asString();
				log.info("RETURNED: " + jsonReturned);
				cleanUpLogout();
			}*/
			
		}catch(Exception | AssertionError e) {
			log.info("Before class threw an error. Will try to run REST command again to recover");
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
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
		logout.forced = "Yes";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
	}
	
	
	/**
	 * We dont need to log into boxilla after each method in this suite so override with an empty method
	 */
//	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	
	/**
	 * Tests in the class do not use the browser so 
	 * this superclass method gets overridden and logout removed.
	 * Also no screen shot is taken on fail
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
				Utilities.captureDeviceLog(rxIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + rxIp);
			}
			try {
				Utilities.captureDeviceLog(txIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + txIp);
			}
			try {
				//Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
				//		 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
				}catch(Exception e) {
					System.out.println("Error when trying to capture log file. Catching error and continuing");
					e.printStackTrace();
				}
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
	/**
	 * Method to manage transmitter and receiver device
	 * @throws InterruptedException
	 */
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation - Unamage - Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("rxMac"), prop.getProperty("ipCheck"), prop.getProperty("rxIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", prop.getProperty("rxMac"), prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("txMac"), prop.getProperty("ipCheck"), prop.getProperty("txIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", prop.getProperty("txMac"), prop.getProperty("ipCheck"));
		
		log.info("Appliance Managed Successfully - Test Preparation Completed");
		log.info("Sleeping while devices configures");
		Thread.sleep(100000);
	}
	
	
}
