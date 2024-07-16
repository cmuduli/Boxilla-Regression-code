package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;
import org.testng.annotations.Test;
/**
 * Class contains tests for checking the appliance board information 
 * @author Boxilla
 *
 */
public class InvisaTestBoardInformation extends StartupTestCase {

	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String restuser = "rest_user";
	private String restPassword = "18_22_33_AA";
	private String boardInformation;
	private JsonPath path;

	
	final static Logger log = Logger.getLogger(InvisaTestBoardInformation.class);

	/**
	 * Overriding superclass method to get board information for tests
	 */
//	@BeforeClass(alwaysRun = true)
//	public void beforeClass() {
//		getDevices();
//		printSuitetDetails(false);
//		try {
//			log.info("Starting test setup...." + this.getClass().getName());
//			//cleanUpLogin();
//			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
//			RestAssured.useRelaxedHTTPSValidation();
//			//deviceManageTestPrep();
//			boardInformation = getBoardInformation();
//			//get rest information
//			 path = given().header(getHead())
//			.when()
//			.get(getHttp() + "://" + rxIp + getPort() + "/version").jsonPath();
//			
//			
//		}catch(Exception | AssertionError e) {
//			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
//			e.printStackTrace();
//			//cleanUpLogout();
//		}
//		//cleanUpLogout();
//	}
//	
//	/**
//	 * Checks the board MPN
//	 */
//	@Test(groups = {"applianceFunctional", "smoke"})
//	public void test01_checkManufacturingPartnumber() {
//		log.info("***** test01_checkManufacturingPartnumber *****");
//		String toCheck = path.get("manufacturing_partnumber");
//		log.info("To Check: " + toCheck);
//		Assert.assertTrue(boardInformation.contains(toCheck), "Board Information does not contain: " + toCheck);
//	}
//	
//	/**
//	 * Checks the board serial number
//	 */
//	@Test(groups = {"applianceFunctional"})
//	public void test02_checkSerialNumber() {
//		log.info("***** test02_checkSerialNumber *****");
//		String toCheck = path.get("serial_number");
//		log.info("To Check: " + toCheck);
//		Assert.assertTrue(boardInformation.contains(toCheck), "Board Information does not contain: " + toCheck);		
//	}
//	/**
//	 * Checks the board product type
//	 */
//	@Test(groups = {"applianceFunctional"})
//	public void test03_checkProductType() {
//		log.info("***** test03_checkProductType *****");
//		String toCheck = path.get("product_type");
//		toCheck = toCheck.replaceAll("\\.", "");		//the return comes back with a period so removing for the assert
//		log.info("To Check: " + toCheck);
//		Assert.assertTrue(boardInformation.contains(toCheck), "Board Information does not contain: " + toCheck);
//	}
//	
//	/**
//	 * Checks the board product configuration
//	 */
//	@Test(groups = {"applianceFunctional", "notEmerald"})
//	public void test04_checkProductConfiguration() {
//		log.info("***** test04_checkProductConfiguration *****");
//		String toCheck = path.get("product_configuration");
//		//this is missing a dash so we split on space and add the dash
//		String[] splitString = toCheck.split("\\s");
//		toCheck = splitString[0] + "-" + splitString[1];
//		log.info("To Check: " + toCheck);
//		Assert.assertTrue(boardInformation.contains(toCheck), "Board Information does not contain: " + toCheck);
//	}
//
//	/**
//	 * Failing test. Bug logged 4886
//	 */
//	@Test(groups = {"applianceFunctional"})
//	public void test05_checkProductBrand() {
//		log.info("***** test05_checkProductBrand *****");
//		String toCheck = path.get("product_brand");
//		
//		log.info("To Check: " + toCheck);
//		Assert.assertTrue(boardInformation.contains(toCheck), "Board Information does not contain: " + toCheck);
//	}
//	
//	
//
//	
//	/**
//	 * We dont need to log into boxilla after each method in this suite
//	 * so override with an empty method
//	 */
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	//@Parameters({ "browser" })
//	public void login( Method method) {
//		
//	}
//	
//	/**
//	 * Overriding superclass method as we dont need to log out
//	 */
//	@Override
//	@AfterMethod(alwaysRun = true)
//	public void logout(ITestResult result) {
//		log.info("********* @ After Method Started ************");
//		// Taking screen shot on failure
//		//String url = http + "s://" + boxillaManager + "/";
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//print result
//		if(ITestResult.FAILURE == result.getStatus())
//			log.info(result.getName() + " :FAIL");
//		
//		if(ITestResult.SKIP == result.getStatus())
//			log.info(result.getName() + " :SKIP");
//		
//		if(ITestResult.SUCCESS == result.getStatus())
//			log.info(result.getName() + " :PASS");
//		
//		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
//			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
//			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
//		}
//	}
//	
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
//		log.info("Appliance Managed Successfully - Test Preparation Completed");
//		log.info("Sleeping while devices configures");
//		Thread.sleep(100000);
//	}
//	
//	//needs to be checked with an emerald board
//	/**
//	 * logs into the device and runs a script that displays 
//	 * board information. This is returned as a string
//	 * @return
//	 */
//	private String getBoardInformation() {
//		String command = "/opt/cloudium/hw_scripts/factory/e2_read.elf";
//		
//		if(isEmerald) {
//			command = "e2_read";
//		}
//		
//		Ssh shell = new Ssh(deviceUserName, devicePassword,
//				rxIp );
//		shell.loginToServer();
//		String output = shell.sendCommand(command);		//script that gets board information
//		System.out.println(output);
//		shell.disconnect();
//		return output;
//	}
	public static void main(String[] args) {
		getIps();
	}
	
	public String getInactiveIp(ArrayList<String> ips) {
		for(String s : ips) {
			if(!StartupTestCase.isIpReachable(s)) {
				log.info("IP Address:" + s + " is not reachable. Using");
				return s;
			}
		}
		return null;
	}

	public static ArrayList<String>  getIps() {
		BufferedReader reader;
		ArrayList<String> list = new ArrayList<String>();
		try {
			reader = new BufferedReader(new FileReader("./ipaddresses.txt"));
			String line = reader.readLine();
			while(line != null) {
				log.info(line);
				list.add(line);
				line = reader.readLine();
			}
			reader.close();
		}catch(IOException e) {
			log.info("File not found");
			return null;
		}
		return list;
	}
	
}
