package invisaPC.Rest;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.Rest.ActiveConnections.Logout;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import northbound.get.BoxillaHeaders;
import testNG.Utilities;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Method;
import java.util.Arrays;;

/**
 * Class contains all the test related to appliace rest api version
 * @author Boxilla
 *
 */

public class Version extends StartupTestCase {
	
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	
	
	final static Logger log = Logger.getLogger(Version.class);
	

	/**
	 * Overrides superclass metho to create a real connection for tests
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("Starting test setup for " + this.getClass().getSimpleName());
		//	cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			log.info("Creating connection through ssh");
			conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp) ;
			log.info("Connection creating. Setup complete");
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			//cleanUpLogout();
		}
		//cleanUpLogout();
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
		
		//print result
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
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
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
	/**
	 * Check rest api returns api_version
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test01_transmitterRestCheckRestVersion() {
		log.info("***** test01_transmitterRestCheckRestVersion *****");
		log.info("TX - Checking rest_version is not empty");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"api_version\":\"\""));		///this is changing to api_version
	}
	/**
	 * Checks rest api returns software_version
	 */
	@Test(groups = {"rest", "emerald"})
	public void test02_transmitterRestCheckSoftwareVersion() {
		log.info("***** test02_transmitterRestCheckSoftwareVersion *****");
		log.info("TX - Checking software_version is not empty");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"software_version\":\"\""));
	}
	/**
	 * check rest api returns manufacturing_partnumber
	 */
	@Test(groups = {"rest", "emerald"})
	public void test03_transmitterRestCheckPartNumber() {
		log.info("***** test03_transmitterRestCheckPartNumber *****");
		log.info("TX - Checking manufacturing_partnumber is not empty");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		log.info(response);
		Assert.assertFalse(response.contains("\"manufacturing_partnumber\":\"\""));
	}
	
	/**
	 * Checks that rest api returns serial_number
	 */
	@Test(groups = {"rest", "emerald"})
	public void test04_transmitterRestCheckSerialNumber() {
		log.info("***** test04_transmitterRestCheckSerialNumber *****");
		log.info("TX - Checking serial_number is not empty");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("serial_number\":\"\""));
	}
	/**
	 * Check that rest api returns product_type
	 */
	@Test(groups = {"rest", "emerald"})
	public void test05_transmitterRestCheckProductType() {
		log.info("***** test05_transmitterRestCheckProductType *****");
		log.info("TX - Checking product_type is not empty");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() +  "/version").asString();
		
		Assert.assertFalse(response.contains("\"product_type\":\"\""));
	}

	/**
	 * Check that rest api returns product_brand
	 */
	@Test(groups = {"rest", "emerald"})
	public void test06_transmitterRestCheckProductBrand() {
		log.info("***** test06_transmitterRestCheckProductBrand *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("product_brand\":\"\""));
	}
	/**
	 * Checks that rest api returns product_configuration
	 */
	@Test(groups = {"rest", "emerald"})
	public void test07_transmitterRestCheckProductConfiguration() {
		log.info("***** test07_transmitterRestCheckProductConfiguration *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("product_configuration\":\"\""));
	}
	/**
	 * Checks that rest api returns ethernet_speed
	 */
	@Test(groups = {"rest", "emerald"})
	public void test08_transmitterRestCheckEthSpeed() {
		log.info("***** test08_transmitterRestCheckEthSpeed *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"ethernet_speed\":}"));
	}
	////////////////////
	/**
	 * Checks rest api reutrns rest_version
	 */
	@Test(groups = {"rest", "emerald"})
	public void test09_receiverRestCheckRestVersion() {
		log.info("***** test09_receiverRestCheckRestVersion *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"rest_version\":\"\""));
	}
	/**
	 * Checks rest aoi returns software_version
	 */
	@Test(groups = {"rest", "emerald"})
	public void test10_receiverRestCheckSoftwareVersion() {
		log.info("***** test10_receiverRestCheckSoftwareVersion *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"software_version\":\"\""));
	}
	/**
	 * Checks rest api returns manufacturing_partnumber
	 */
	@Test(groups = {"rest", "emerald"})
	public void test11_receiverRestCheckPartNumber() {
		log.info("***** test11_receiverRestCheckPartNumber *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		log.info(response);
		Assert.assertFalse(response.contains("\"manufacturing_partnumber\":\"\""));
	}
	/**
	 * Checks rest api returns serial_number
	 */
	@Test(groups = {"rest", "emerald"})
	public void test12_receiverRestCheckSerialNumber() {
		log.info("***** test12_receiverRestCheckSerialNumber *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("serial_number\":\"\""));
	}
	/**
	 * Checks rest api returns product_type
	 */
	@Test(groups = {"rest", "emerald"})
	public void test13_receiverRestCheckSerialNumber() {
		log.info("***** test13_receiverRestCheckSerialNumber *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"product_type\":\"\""));
	}

	/**
	 * Checks rest api returns product_brand
	 */
	@Test(groups = {"rest", "emerald"})
	public void test14_receiverRestCheckProductBrand() {
		log.info("***** test14_receiverRestCheckProductBrand *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("product_brand\":\"\""));
	}
	/**
	 * Checks rest api returns product_configuration
	 */
	@Test(groups = {"rest", "emerald"})
	public void test15_receiverRestCheckProductConfiguration() {
		log.info("***** test15_receiverRestCheckProductConfiguration *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("product_configuration\":\"\""));
	}
	/**
	 * Checks rest api returns ethernet_speed
	 */
	@Test(groups = {"rest", "emerald"})
	public void test16_receiverRestCheckEthSpeed() {
		log.info("***** test16_receiverRestCheckEthSpeed *****");
		String response = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/version").asString();
		
		Assert.assertFalse(response.contains("\"ethernet_speed\":}"));
	}
	
	/**
	 * Check that the transmitter REST api returns 404 when given
	 * a bad URI
	 */
	@Test(groups = {"rest", "emerald"})
	public void test17_check404ResponseCodeTransmitter() {
		log.info("***** test17_check404ResponseCodeTransmitter *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + rxIp + getPort() + "/invalidURL/version")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 404, "Status code did not equal 404, actual returned " + statusCode);
	}
	/**
	 * Check that the receiver REST api returns 404 when given
	 * a bad URI
	 */
	@Test(groups = {"rest", "emerald"})
	public void test18_check404ResponseCodeReceiver() {
		log.info("***** test18_check404ResponseCodeReceiver *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + rxIp + getPort() + "/invalidURL/version")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 404, "Status code did not equal 404, actual returned " + statusCode);
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
//	
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
//		log.info("Appliance Managed Successfully - Test Preparation Completed");
//		log.info("Sleeping while devices configures");
//		Thread.sleep(100000);
//	}
	


}}
