package invisaPC.Rest;

/**
 * Class used to test the return values from the
 * device REST API. Some values are not known beforehand 
 * and just the parameter is checked. Some values are constantly 
 * changing so the check is within a +/- tolerance
 */

import static io.restassured.RestAssured.basic;

import java.lang.reflect.Method;
import java.util.Arrays;

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

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;

public class SystemFacts extends StartupTestCase {
	
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String jsonReturned;
	private String jsonReturnedTX;
	
	final static Logger log = Logger.getLogger(SystemFacts.class);
	
	
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts")
		.getStatusCode();
		log.info("RETURN CODE:" + statusCode);
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	/**
	 * Will test that the ID parameter is returned. Value is not 
	 * checked as this is dynamic
	 */
	@Test(groups = {"rest", "emerald"})
	public void test02_verifyId () {
		//check for the id
		log.info("***** test02_verifyJsonReturned *****");
		Assert.assertTrue(jsonReturned.contains("\"id\":"), "Check for \"id\": failed, actual:" + jsonReturned);
	}

	/**
	 * Will check that the MAC address returned matches the 
	 * MAC address of the device
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test03_verifyMacAddress() {
		log.info("***** test03_verifyMacAddress *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts")
		.then().assertThat().statusCode(200)
		.body("kvm_system_facts.mac[0]", equalTo(rxSingle.getMac().toLowerCase()));
	}
	
	/**
	 * Will check that the uptime returned matches the device uptime.
	 * NOTE: This is not going to be an exact match so
	 * check is +/- 60 seconds
	 */
	@Test(groups = {"rest", "emerald"})
	public void test04_verifyUpTime() {
		log.info("***** test04_verifyUpTime *****");
		JsonPath path = 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").jsonPath();
		System.out.println("PATH:" + path);
		int upTime = path.get("kvm_system_facts.uptime[0]");
		//time will never be exact but will check for within one min
		int upTimeFromDevice = parseUpTime(rxIp);
		log.info("Uptime from REST: " + upTime + ", upTime from device: " + upTimeFromDevice);
		Assert.assertTrue(checkRange(upTimeFromDevice, upTime - 60, upTime + 60) ,
				"Uptime differed by more or less than minutes, values - from REST: "
		+ upTime + " from Device: " + upTimeFromDevice );
	}
	
	/**
	 * Will check REST total memory return against what
	 * comes back from the device itself
	 */
	@Test(groups = {"rest", "emerald"})
	public void test14_verifyTotalMemory() {
		log.info("***** test14_verifyTotalMemory *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts")
		.then().assertThat().statusCode(200)
		.body("kvm_system_facts.'total memory'[0]", equalTo(244108));		
	}
	
	/**
	 * Will check REST free memory return against what
	 * comes back from the device itself. 
	 * NOTE: This is not an exact match as free memory changes, 
	 * it is within a tolerance of +/- 500k
	 */
//	@Test(groups = {"rest","emerald"})
//	public void test15_verifyFreeMemory() {
//		log.info("***** test15_verifyFreeMemory *****");
//		JsonPath path = 
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").jsonPath();
//		int freeMemory = path.get("kvm_system_facts.'free memory'[0]");	
//		int freeMemoryFromDevice = getFreeMemory();
//		Assert.assertTrue(checkRange(freeMemoryFromDevice, freeMemory - 10000, freeMemory + 10000), "Free memory differed by +/- 5000k, values - from REST: "
//				+ freeMemory + " from Device: " + freeMemoryFromDevice);
//	}
	
	/**
	 * 	Checks that the rest api returns load_avg_1min
	 */
	@Test(groups = {"rest", "emerald"})
	public void test05_verifyLoadAverage1Min() {
		log.info("***** test05_verifyLoadAverage1Min *****");
		Assert.assertTrue(jsonReturned.contains("\"load_avg_1min\":"), 
				"Check for \"load_avg_1min\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * 	Checks that the rest api returns load_avg_5min
	 */
	@Test(groups = {"rest", "emerald"})
	public void test07_verifyLoadAverage5Min() {
		log.info("***** test07_verifyLoadAverage5Min *****");
		Assert.assertTrue(jsonReturned.contains("\"load_avg_5min\":"), 
				"Check for \"load_avg_5min\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * 	Checks that the rest api returns load_avg_15min
	 */
	@Test(groups = {"rest","emerald"})
	public void test08_verifyLoadAverage15Min() {
		log.info("***** test08_verifyLoadAverage15Min *****");
		Assert.assertTrue(jsonReturned.contains("\"load_avg_15min\":"), 
				"Check for \"load_avg_15min\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns capture_time
	 */
	@Test(groups = {"rest", "emerald"})
	public void test09_verifyCaptureTime() {
		log.info("***** test09_verifyCaptureTime *****");
		Assert.assertTrue(jsonReturned.contains("\"capture_time\":"),
				"Check for \"capture_time\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns crcs
	 */
	@Test(groups = {"rest", "emerald"})
	public void test10_verifyCrcs() {
		log.info("***** test10_verifyCrcs *****");
		Assert.assertTrue(jsonReturned.contains("\"crcs\":"), 
				"Check for \"crcs\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns temperature
	 */
	@Test(groups = {"rest", "emerald"})
	public void test11_verifyTemperature() {
		log.info("***** test11_verifyTemperature *****");
		Assert.assertTrue(jsonReturned.contains("\"temperature\":"), 
				"Check for \"temperature\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns user_response
	 */
	@Test(groups = {"rest", "emerald"})
	public void test12_verifyUserResponse() {
		log.info("***** test12_verifyUserResponse *****");
		Assert.assertTrue(jsonReturned.contains("\"user_response\":"),
				"Check for \"user_response\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns time_stamp
	 */
	@Test(groups = {"rest", "emerald"})
	public void test13_verifyTimeStamp() {
		log.info("***** test13_verifyTimeStamp *****");
		Assert.assertTrue(jsonReturned.contains("\"timestamp\":"),
				"Check for \"timestamp\": failed, actual:" + jsonReturned);
	}
//	
//	
	/////TX
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald"})
	public void test14_verifyReturnCodeTX () {
		log.info("***** test14_verifyReturnCodeTX *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts")
		.getStatusCode();
		log.info("STATUS CODE:" + statusCode);
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
		
	}
	
	/**
	 * Will test that the ID parameter is returned. Value is not 
	 * checked as this is dynamic
	 */
	@Test(groups = {"rest", "emerald"})
	public void test15_verifyIdTX () {
		//check for the id
		log.info("***** test15_verifyIdTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"id\":"),
				"Check for \"id\": failed, actual:" + jsonReturned);
	}

	/**
	 * Will check that the MAC address returned matches the 
	 * MAC address of the device
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test16_verifyMacAddressTX() {
		log.info("***** test16_verifyMacAddressTX *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts")
		.then().assertThat().statusCode(200)
		.body("kvm_system_facts.mac[0]", equalTo(txSingle.getMac().toLowerCase()));
	}
	
	/**
	 * Will check that the uptime returned matches the device uptime.
	 * NOTE: This is not going to be an exact match so
	 * check is +/- 60 seconds
	 */
	@Test(groups = {"rest", "emerald"})
	public void test17_verifyUpTimeTX() {
		log.info("***** test17_verifyUpTimeTX *****");
		JsonPath path = 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts").jsonPath();
		System.out.println("PATH:" + path);
		int upTime = path.get("kvm_system_facts.uptime[0]");
		//time will never be exact but will check for within one min
		int upTimeFromDevice = parseUpTime(txIp);
		log.info("Uptime from REST: " + upTime + ", upTime from device: " + upTimeFromDevice);
		Assert.assertTrue(checkRange(upTimeFromDevice, upTime - 60, upTime + 60) ,
				"Uptime differed by more or less than minutes, values - from REST: "
		+ upTime + " from Device: " + upTimeFromDevice );
	}
	
	/**
	 * Will check REST total memory return against what
	 * comes back from the device itself
	 */
	@Test(groups = {"rest", "emerald"})
	public void test18_verifyTotalMemoryTX() {
		log.info("***** test18_verifyTotalMemoryTX *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts")
		.then().assertThat().statusCode(200)
		.body("kvm_system_facts.'total memory'[0]", equalTo(244108));		
	}
	
	/**
	 * Will check REST free memory return against what
	 * comes back from the device itself. 
	 * NOTE: This is not an exact match as free memory changes, 
	 * it is within a tolerance of +/- 500k
	 */
//	@Test(groups = {"rest","emerald"})
//	public void test15_verifyFreeMemory() {
//		log.info("***** test15_verifyFreeMemory *****");
//		JsonPath path = 
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").jsonPath();
//		int freeMemory = path.get("kvm_system_facts.'free memory'[0]");	
//		int freeMemoryFromDevice = getFreeMemory();
//		Assert.assertTrue(checkRange(freeMemoryFromDevice, freeMemory - 10000, freeMemory + 10000), "Free memory differed by +/- 5000k, values - from REST: "
//				+ freeMemory + " from Device: " + freeMemoryFromDevice);
//	}
	
	/**
	 * 	Checks that the rest api returns load_avg_1min
	 */
	@Test(groups = {"rest", "emerald"})
	public void test19_verifyLoadAverage1MinTX() {
		log.info("***** test19_verifyLoadAverage1MinTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"load_avg_1min\":"),
				"Check for \"load_avg_1min\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * 	Checks that the rest api returns load_avg_5min
	 */
	@Test(groups = {"rest", "emerald"})
	public void test20_verifyLoadAverage5MinTX() {
		log.info("***** test20_verifyLoadAverage5MinTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"load_avg_5min\":"),
				"Check for \"load_avg_5min\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * 	Checks that the rest api returns load_avg_15min
	 */
	@Test(groups = {"rest","emerald"})
	public void test21_verifyLoadAverage15MinTX() {
		log.info("***** test21_verifyLoadAverage15MinTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"load_avg_15min\":"),
				"Check for \"load_avg_15min\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns capture_time
	 */
	@Test(groups = {"rest", "emerald"})
	public void test22_verifyCaptureTimeTX() {
		log.info("***** test22_verifyCaptureTimeTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"capture_time\":"), 
				"Check for \"capture_time\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns crcs
	 */
	@Test(groups = {"rest", "emerald"})
	public void test23_verifyCrcsTX() {
		log.info("***** test23_verifyCrcsTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"crcs\":"), 
				"Check for \"crcs\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns temperature
	 */
	@Test(groups = {"rest", "emerald"})
	public void test24_verifyTemperatureTX() {
		log.info("***** test24_verifyTemperatureTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"temperature\":"), 
				"Check for \"temperature\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns user_response
	 */
	@Test(groups = {"rest", "emerald"})
	public void test25_verifyUserResponseTX() {
		log.info("***** test25_verifyUserResponseTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"user_response\":"),
				"Check for \"user_response\": failed, actual:" + jsonReturned);
	}
	/**
	 * 	Checks that the rest api returns time_stamp
	 */
	@Test(groups = {"rest", "emerald"})
	public void test26_verifyTimeStampTX() {
		log.info("***** test26_verifyTimeStampTX *****");
		Assert.assertTrue(jsonReturnedTX.contains("\"timestamp\":"),
				"Check for \"timestamp\": failed, actual:" + jsonReturned);
	}
	
	
	
	/**
	 * Overrides superclass method to create a connection for tests
	 * and a receiver
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("Starting test setup for " + this.getClass().getSimpleName());
			//cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			log.info("Creating connection through ssh");
			conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp) ;
			log.info("sleeping for 60 seconds");
			Thread.sleep(60000);
			log.info("waking and getting json returned");
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").asString();
			log.info("RETURNED: " + jsonReturned);
			
			jsonReturnedTX = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts").asString();
			log.info("RETURNED TX: " + jsonReturnedTX);
			if(!jsonReturned.contains("id") || !jsonReturnedTX.contains("id")) {
				log.info("JSON return was blank. Waiting and trying again");
				Thread.sleep(30000);
				jsonReturned = given().header(getHead())
						.when()
						.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").asString();
				log.info("RETURNED: " + jsonReturned);
				
				jsonReturnedTX = given().header(getHead())
						.when()
						.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts").asString();
				log.info("RETURNED TX: " + jsonReturnedTX);
			}
			
			
		}catch(Exception | AssertionError e) {
			log.info("Before class threw an error. In case the error was rest related, will try running rest again to recover");
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/facts/system_facts").asString();
			log.info("RETURNED: " + jsonReturned);
			
			jsonReturnedTX = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/facts/system_facts").asString();
			log.info("RETURNED TX: " + jsonReturnedTX);
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
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
	/////UTILITY METHODS
	
	/**
	 * Method to manage transmitter and receiver device
	 * @throws InterruptedException
	 */
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
	
	/**
	 * Utility method to log into a device and return the 
	 * output of a passed in command
	 * @param command
	 * @param ip
	 * @return
	 */
	private String getDataFromDevice(String command, String ip) {
		Ssh shell = new Ssh(deviceUserName, devicePassword, ip);
		shell.loginToServer();
		String output =  shell.sendCommand(command);
		shell.disconnect();
		return output;
	}
	
	/**
	 * Utility method that returns an array list
	 * of all the different memory values returnfrom from the 
	 * meminfo file
	 * @return
	 */
	private String[] getMemoryOutput( ) {
		String output = getDataFromDevice("cat /proc/meminfo", rxIp);
		String[] splitString = output.split("\\r?\\n");
		return splitString;
	}
	
	/**
	 * Returns total memory as an Integer
	 * @return
	 */
	private int getTotalMemory() {
		String total =  getMemoryOutput()[0];
		total = total.trim().replaceAll(" +", " ");
		String[] totalSplit = total.split("\\s");
		return 	Integer.parseInt(totalSplit[1]);
	}
	
	/**
	 * Returns free memory as an integer
	 * @return
	 */
	private int getFreeMemory() {
		String total =  getMemoryOutput()[1];
		total = total.trim().replaceAll(" +", " ");
		String[] totalSplit = total.split("\\s");
		return 	Integer.parseInt(totalSplit[1]);
	}

	/**
	 * utility method to get the system uptime and 
	 * return it in an int
	 * @return
	 */
	private int parseUpTime(String ip) {
		String upTime = "";
		String newUpTime  = getDataFromDevice("uptime", ip);
		System.out.println("newUpTime:" + newUpTime);
		String upTimeSplit[] = newUpTime.split("\\s");
		upTime = upTimeSplit[3];
		System.out.println("Up Time:" + upTime);
		int totalSeconds = Integer.parseInt(upTime) * 60;				//convert min to seconds
		System.out.println("Total seconds:" + totalSeconds);
		return totalSeconds;
	}
//	private int parseUpTimeEmerlad(String ip) {
//		String upTime = "";
//		String newUpTime  = getDataFromDevice("uptime", ip);
//		System.out.println("newUpTime:" + newUpTime);
//	}
	
	/**
	 * Utility method to check if a number is within a certain tolerance
	 * @param toBeChecked
	 * @param minRange
	 * @param maxRange
	 * @return
	 */
	private boolean checkRange(int toBeChecked, int minRange, int maxRange) {
		if(toBeChecked >= minRange && toBeChecked <= maxRange) {
			return true;
		}
		return false;
	}
}
