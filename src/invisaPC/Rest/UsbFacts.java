package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

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

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;
/**
 * Class that contains all the appliance REST api usb_facts
 * @author Boxilla
 *
 */
public class UsbFacts extends StartupTestCase {

	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	String rxReturn = "";
	String txReturn = "";



	final static Logger log = Logger.getLogger(UsbFacts.class);


	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest", "emerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts")
				.getStatusCode();
	
		 log.info("STATUS CODE:" + statusCode);
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
//
	//these tests will check the video facts parameters

	/**
	 * Check rest api returns id
	 */
	@Test(groups = {"rest", "smoke", "emerald"})
	public void test02_checkKvmUsbFactsId () {
		log.info("***** test02_checkKvmUsbFactsId *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		System.out.println(returnValue);
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"id\":"), "USB facts did not contain id, actual:" + returnValue );
	}

	/**
	 * Check rest api returns mac
	 */
	@Test(groups = {"rest", "emerald"})
	public void test03_checkKvmUsbFactsMac () {
		log.info("***** test03_checkKvmUsbFactsMac *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		log.info("MAC: " + txSingle.getMac());
		Assert.assertTrue(returnValue.contains("\"mac\":\""), 
				"USB facts did not contain \"mac\":\", actual:" + returnValue );
	}

	/**
	 * check rest api returns min_usb_egress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test04_checkKvmUsbFactsMinUsbEgressBw () {
		log.info("***** test04_checkKvmUsbFactsMinUsbEgressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_usb_egress_bw\""), 
				"USB facts did not contain \"min_usb_egress_bw\", actual:" + returnValue);
	}

	/**
	 * Checks the rest api returns max_usb_egress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test05_checkKvmUsbFactsMaxUsbEgressBw () {
		log.info("***** test05_checkKvmUsbFactsMaxUsbEgressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_usb_egress_bw\""), 
				"USB facts did not contain \"max_usb_egress_bw\", actual:" + returnValue);
	}

	/**
	 * Check rest api returns max_usb_egress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test06_checkKvmUsbFactsAvgUsbEgressBw () {
		log.info("***** test06_checkKvmUsbFactsAvgUsbEgressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_usb_egress_bw\""), 
				"USB facts did not contain \"avg_usb_egress_bw\", actual:" + returnValue);
	}

	/**
	 * Check rest api returns usb_egress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test07_checkKvmUsbFactsUsbEgressBw () {
		log.info("***** test07_checkKvmUsbFactsUsbEgressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"usb_egress_bw\""), 
				"USB facts did not contain \"usb_egress_bw\", actual:" + returnValue);
	}	

	/**
	 * Check rest api returns min_usb_ingress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test08_checkKvmUsbFactsMinUsbIngressBw () {
		log.info("***** test08_checkKvmUsbFactsMinUsbIngressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"min_usb_ingress_bw\""), 
				"USB facts did not contain \"min_usb_ingress_bw\", actual:" + returnValue);
	}

	/**
	 * Checks rest api returns max_usb_ingress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test09_checkKvmUsbFactsMaxUsbIngressBw () {
		log.info("***** test09_checkKvmUsbFactsMaxUsbIngressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"max_usb_ingress_bw\""), 
				"USB facts did not contain \"max_usb_ingress_bw\", actual:" + returnValue);
	}

	/**
	 * Check rest api returns avg_usb_ingress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test10_checkKvmUsbFactsAvgUsbIngressBw () {
		log.info("***** test10_checkKvmUsbFactsAvgUsbIngressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"avg_usb_ingress_bw\""), 
				"USB facts did not contain \"avg_usb_ingress_bw\", actual:" + returnValue);
	}

	/**
	 * Check rest api returns usb_ingress_bw
	 */
	@Test(groups = {"rest", "emerald"})
	public void test11_checkKvmUsbFactsUsbIngressBw () {
		log.info("***** test11_checkKvmUsbFactsUsbIngressBw *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"usb_ingress_bw\""), 
				"USB facts did not contain \"usb_ingress_bw\" actual, " + returnValue);
	}

	/**
	 * Checks rest api returns timestamp
	 */
	@Test(groups = {"rest", "emerald"})
	public void test12_checkKvmUsbFactsTimestamp () {
		log.info("***** test12_checkKvmUsbFactsTimestamp *****");
		String returnValue = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
		log.info("RETURN : " + returnValue);
		Assert.assertTrue(returnValue.contains("\"timestamp\""), 
				"USB facts did not contain \"timestamp\" actual, " + returnValue);
	}


	/**
	 * Overrides superclass method to set up a connection for tests
	 * @throws InterruptedException 
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		printSuitetDetails(false);
		try {
			Thread.sleep(100000);
			log.info("**** Starting test setup for " + this.getClass().getSimpleName() + " ****");
		//	cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 100000 );
			deviceMethods.rebootDeviceSSH(txIp, "root", "barrow1admin_12", 100000 );
			log.info("Creating connection through ssh");
			conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp);
			log.info("Sleeping while USB attaches...");
			Thread.sleep(100000);			
			log.info("Waking and getting json returned");
			String connection = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
			
			 rxReturn =  given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/facts/usb_facts").asString();
			
			 txReturn =  given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
			log.info("Connection:" + connection);
			log.info("TX:" + txReturn);
			log.info("RX:" + rxReturn);
			int count=0;
			while(count<2) {
			if(txReturn.contains("id") && rxReturn.contains("id")) 
			{ 
				break;
				}
			else {
			
				log.info("Returns came back empty. Waiting and trying again");
				Thread.sleep(30000);
				deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 100000 );
				
				 rxReturn =  given().header(getHead())
						.when()
						.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/facts/usb_facts").asString();
				 deviceMethods.rebootDeviceSSH(txIp, "root", "barrow1admin_12", 100000 );
				 txReturn =  given().header(getHead())
						.when()
						.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
				log.info("TX:" + txReturn);
				log.info("RX:" + rxReturn);
			}
			count++;
			}

		}catch(Exception | AssertionError e) {
			log.info("Before class threw an error. In case its rest, run them again.");
			Thread.sleep(30000);
			 rxReturn =  given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/facts/usb_facts").asString();
			
			 txReturn =  given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp +  getPort() + "/statistics/facts/usb_facts").asString();
			log.info("TX:" + txReturn);
			log.info("RX:" + rxReturn);
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
//	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String brower, Method method) {
		
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




}
