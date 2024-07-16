package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.AppliancePool;
import extra.Device;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import objects.Devices;
import testNG.Utilities;
/**
 * Class contains tests for updating device parameters to system properties through boxilla bulk update
 * @author Boxilla
 *
 */
public class BulkUpdateSystemProperties4k extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(BulkUpdateSystemProperties4k.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	
	//system properties
	String videoQuality = "4";				///return from rest : 1
	String videoSource = "DVI Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "3840x2160p-60Hz";			///return from rest : 1
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = false;			//sets HTTP Enabled to Enabled
	private int timeout = 120000;
	private String txTemplate = "bulkUpdateSystemTX4k";
	private String rxTemplate = "bulkUpdateSystemRX4k"; 
	
	/**
	 * Overrides superclass method to set up test specific data
	 * 
	 * Creates a template for RX and TX devices and sets each device to this template.
	 * Then runs bulk update for system properties on each device
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		System.out.println(getHttp() + getPort());
		
		try {	
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);		
			RestAssured.useRelaxedHTTPSValidation();
			deviceMethods.addTemplateTransmitter(driver, txTemplate, "Best Quality",
					"Off", "Default", "Analog", "5", "Default", "1680x1050");
			deviceMethods.addTemplateReceiver(driver, rxTemplate, true, true);
			deviceMethods.setTxToTemplateProperties(driver, txIp, txTemplate);
			deviceMethods.setRxToTemplateProperties(driver, rxIp, rxTemplate);
			deviceMethods.setTxToTemplateProperties(driver, txIpDual, txTemplate);
			deviceMethods.setRxToTemplateProperties(driver, rxIpDual,rxTemplate);
			log.info("Sleeping while devices reboot");
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);

			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig, audio, 
					mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			
			deviceMethods.bulkUpdate(driver, "Transmitter", "System Properties", new String[]{txSingle.getDeviceName(), txDual.getDeviceName()});
			deviceMethods.timer(driver);
			deviceMethods.bulkUpdate(driver, "Receiver", "System Properties", new String[]{rxSingle.getDeviceName(), rxDual.getDeviceName()});
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
//			
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	

	
	/**
	 * Checks hid on single head equals system properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test04_checkHidSingle() {
		log.info("***** test04_checkHidSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	/**
	 * checks hid on dual head equals system properties
	 */
	//@Test(groups = {"rest", "integration", "notEmerald", "chrome"})
	public void test05_checkHidDual() {
		log.info("***** test05_checkHidDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	/**
	 * Checks edid1 on single head equals system properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald", "quick"})
	public void test06_checkEdid1Single() {
		log.info("***** test06_checkEdid1Single *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	/**
	 * Checks edid1 on dual head equals system properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test07_checkEdid1Dual() {
		log.info("***** test07_checkEdid1Dual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	

	
	/**
	 * check http equals system properties
	 */
	@Test(groups = {"rest", "integration", "emerald"})
	public void test12_checkHttpEnableSingle() {
		log.info("***** test12_checkHttpEnableSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	/**
	 * Checks http equals system properties 
	 */
	@Test(groups = {"rest", "integration", "smoke", "notEmerald"})
	public void test13_checkHttpEnableDual() {
		log.info("***** test13_checkHttpEnableDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	/**
	 * Check mouse keyboard equals system properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test14_checkMouseKeyboardSingle() {
		log.info("***** test14_checkMouseKeyboardSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	/**
	 * Check mouse keyboard equals system properties 
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test15_checkMouseKeyboardDual() {
		log.info("***** test15_checkMouseKeyboardDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	
	/**
	 * Checks tx and rx properties
	 */
	private void checkSystemProperties(boolean checkTX, boolean checkRX) {
		log.info("Checking system properties are applied");
		
		if(checkTX) {
			log.info("Checking single head...");
			log.info("video_quality...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("video_quality", equalTo(0));
			log.info("OK");
			
			log.info("video_source_optimisation...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("video_source_optimisation", equalTo(0));
			log.info("OK");
			
			log.info("hid...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("hid", equalTo(0));
			log.info("OK");
			
			log.info("head_1_edid...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_1_edid", equalTo(1));
			log.info("OK");
			
			log.info("mouse_keyboard_timeout...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("mouse_keyboard_timeout", equalTo(5));
			log.info("OK");
			
			
			//dual
			log.info("Checking dual head...");
			log.info("video_quality dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("video_quality", equalTo(0));
			log.info("OK");
			
			
			log.info("hid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("hid", equalTo(0));
			log.info("OK");
			
			log.info("head_1_edid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_1_edid", equalTo(1));
			log.info("OK");
			
			log.info("head_2_edid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_2_edid", equalTo(2));
			log.info("OK");
			
			log.info("mouse_keyboard_timeout...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("mouse_keyboard_timeout", equalTo(5));
			log.info("OK");
			
		}
		if(checkRX) {
		//rx
			log.info("Checking single head...");
			log.info("power mode...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("power_mode", equalTo(0));
			log.info("OK");
			
			//rx
			log.info("http enable...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("http_enable", equalTo(1));
			log.info("OK");	
			
			log.info("Checking dual head...");
			log.info("power mode dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("power_mode", equalTo(0));
			log.info("OK");
			
			//rx
			log.info("http enable dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("http_enable", equalTo(1));
			log.info("OK");	
			
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
		
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			log.info(result.getName() + " :FAIL");
		
		if(ITestResult.SKIP == result.getStatus())
			log.info(result.getName() + " :SKIP");
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//collectLogs(result);
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
	/**
	 * Logs into boxilla and unmanages the two devicves
	 * @throws InterruptedException 
	 */
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		try {
			cleanUpLogin();
			deviceMethods.deleteTemplate(driver, txTemplate);
			deviceMethods.deleteTemplate(driver, rxTemplate);
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
		super.afterClass();
		printSuitetDetails(true);
	}


}
