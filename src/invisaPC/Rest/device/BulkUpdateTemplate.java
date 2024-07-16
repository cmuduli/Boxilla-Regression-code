package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.openqa.selenium.TakesScreenshot;
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
 * Class contains tests for updating device parameters to template properties through boxilla bulk update
 * @author Boxilla
 *
 */
public class BulkUpdateTemplate extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(BulkUpdateTemplate.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods(); 
	AppliancePool devicePool = new AppliancePool();
	
	
	//test properties
	
	private int timeout = 120000;
	
	//system properties
	String videoQuality = "Best Quality";				///return from rest : 1
	String videoSource = "DVI/DP Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
//	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "5";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = false;			//sets HTTP Enabled to Enabled
	String audio = "DisplayPort Audio";
	String audio1="Analog Audio";

	
	
	/**
	 * Overrides superclass method to do test specific set up
	 * Creates templates for rx and tx and runs bulk update setting the 
	 * devices to template
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
//			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig, audio, 
//					mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			if(!StartupTestCase.isZeroU) {
				deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
						audio1, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}else {
					deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
							audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}
			deviceMethods.checkDeviceOnline(driver, txIp);
			deviceMethods.checkDeviceOnline(driver, rxIp);
			
			//deviceManageTestPrep();
//			deviceMethods.setTxToSystemProperties(driver, txIp);
//			deviceMethods.setTxToSystemProperties(driver, txIpDual);
//			deviceMethods.setRxToSystemProperty(driver, rxIp);
//			deviceMethods.setRxToSystemProperty(driver, rxIpDual);
//			Thread.sleep(timeout);
			//check that template properties have been applied
			
			//checkSystemProperties(true, true);	
//			deviceMethods.addTemplateTransmitter(driver, txTemplateName, "Best Quality",
//					"Off", "Default", "Analog", "5", "1920x1200", "1680x1050");
			if(!StartupTestCase.isZeroU) {
				deviceMethods.addTemplateTransmitter(driver, txTemplateName, "Best Quality",
						"Off", "Default",audio1, "5", "1920x1200", "1680x1050");
				}else {
					deviceMethods.addTemplateTransmitter(driver, txTemplateName, "Best Quality",
							"Off", "Default",audio, "5", "1920x1200", "1680x1050");
				}
			deviceMethods.addTemplateReceiver(driver, rxTemplateName, true, true);
			deviceMethods.timer(driver);
			System.out.println("Single: " + rxSingle.getDeviceName());
			System.out.println("Dual: " + rxDual.getDeviceName());
			deviceMethods.bulkUpdate(driver, "Transmitter", txTemplateName, new String[]{txSingle.getDeviceName(), txDual.getDeviceName()});
			deviceMethods.timer(driver);
			deviceMethods.checkDeviceOnline(driver, txIp);
			deviceMethods.bulkUpdate(driver, "Receiver", rxTemplateName, new String[]{rxSingle.getDeviceName(), rxDual.getDeviceName()});
			deviceMethods.checkDeviceOnline(driver, rxIp);
			Utilities.captureScreenShot(driver, "Bulk update", "Before_class");
			
			log.info("Sleeping while devices reboot");
//			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
			log.info("Sleeping for 20 seconds");
//			Thread.sleep(20000);
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	/**
	 * checks video_quality on single head matches template properties
	 */
	@Test(groups = {"rest", "integration", "smoke", "notEmerald", "chrome"})
	public void test01_checkVideoQualitySingle() {
		log.info("***** test01_checkVideoQuality *****");
		log.info("video_quality...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
		log.info("OK");
	}
	/**
	 * checks video_quality on dual head device matches template properties
	 */
	
	@Test(groups = {"rest", "integration", "notEmerald", "chrome"})
	public void test02_checkVideoQualityDual() {
		log.info("***** test02_checkVideoQualityDual *****");
		log.info("video_quality...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
		log.info("OK");
	}
	/**
	 * Checks video_source_optimisation matches template properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test03_checkVideoSource() {
		log.info("***** test03_checkVideoSource *****");
		log.info("video_source_optimisation...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(0));
	}
	/**
	 * Check hid on single head matches template properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test04_checkHidSingle() {
		log.info("***** test04_checkHidSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * CHecks hid on dual head matches template properties
	 */
	//acm
	//@Test(groups = {"rest", "integration", "notEmerald"})
	public void test05_checkHidDual() {
		log.info("***** test05_checkHidDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	/**
	 * Checks edid1 on single head matches template properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test06_checkEdid1Single() {
		log.info("***** test06_checkEdid1Single *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	/**
	 * CHeks edid1 on dual head matches template properties
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
	 * Checks edid2 matches template properties 
	 */
	//acm
	//@Test(groups = {"rest", "integration", "notEmerald"})
	public void test09_checkEdid2Dual() {
		log.info("***** test09_checkEdid2Dual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(2));
	}
	
	/**
	 * Checks power mode matches template properties
	 */
//	@Test(groups = {"rest", "integration", "smoke", "emerald", "chrome", "noSE"})
	public void test10_checkPowerModeSingle() {
		log.info("***** test10_checkPowerModeSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	/**
	 * Checks power mode matches template properties
	 */
	//acm
	//@Test(groups = {"rest", "integration", "notEmerald", "noSE"})
	public void test11_checkPowerModeDual() {
		log.info("***** test11_checkPowerModeDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	/**
	 * Checks http matches template properties 
	 */
	@Test(groups = {"rest", "integration", "emerald"})
	public void test12_checkHttpEnableSingle() {
		log.info("***** test12_checkHttpEnableSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	/**
	 * Checks http matches template properties
	 */

	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test13_checkHttpEnableDual() {
		log.info("***** test13_checkHttpEnableDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	/**
	 * CHecks mouse keyboard matches template properties 
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test14_checkMouseKeyboardSingle() {
		log.info("***** test14_checkMouseKeyboardSingle *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	/**
	 * Checks mouse keyboard matches template properties
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test15_checkMouseKeyboardDual() {
		log.info("***** test15_checkMouseKeyboardDual *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
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
			.body("video_quality", equalTo(3));
			log.info("OK");
			
			log.info("video_source_optimisation...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("video_source_optimisation", equalTo(1));
			log.info("OK");
			
			log.info("hid...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("hid", equalTo(1));
			log.info("OK");
			
			log.info("head_1_edid...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_1_edid", equalTo(0));
			log.info("OK");
			
			log.info("mouse_keyboard_timeout...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("mouse_keyboard_timeout", equalTo(3));
			log.info("OK");
			
			
			//dual
			log.info("Checking dual head...");
			log.info("video_quality dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("video_quality", equalTo(3));
			log.info("OK");
			
			
			log.info("hid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("hid", equalTo(1));
			log.info("OK");
			
			log.info("head_1_edid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_1_edid", equalTo(0));
			log.info("OK");
			
			log.info("head_2_edid dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("head_2_edid", equalTo(1));
			log.info("OK");
			
			log.info("mouse_keyboard_timeout...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
			.then()
			.body("mouse_keyboard_timeout", equalTo(3));
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
			.body("power_mode", equalTo(1));
			log.info("OK");
			
			//rx
			log.info("http enable...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("http_enable", equalTo(0));
			log.info("OK");	
			
			log.info("Checking dual head...");
			log.info("power mode dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("power_mode", equalTo(1));
			log.info("OK");
			
			//rx
			log.info("http enable dual...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
			.then()
			.body("http_enable", equalTo(0));
			log.info("OK");	
			
		}
	}
	
	/**
	 * Gives devices IP addresses and manages them
	 * @throws InterruptedException
	 */
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"),
				rxIp, rxSingle.getGateway(),rxSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxSingle.getDeviceName(), rxSingle.getMac(),
				prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"),
				txIp, txSingle.getGateway(), txSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txSingle.getDeviceName(), txSingle.getMac(), 
				prop.getProperty("ipCheck"));
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(), rxDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxDual.getDeviceName(),rxDual.getMac(),
				prop.getProperty("ipCheck"));
				
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txDual.getMac(), prop.getProperty("ipCheck"),
				txIpDual,txDual.getGateway(), txDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txDual.getDeviceName(), txDual.getMac(),
				prop.getProperty("ipCheck"));
				
		
		log.info("Appliances Managed Successfully");
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
			//collectLogs(result);
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
	/**
	 * Logs into boxilla and unmanages the devices
	 * @throws InterruptedException 
	 */
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		log.info("**** Starting test teardown for " + this.getClass().getSimpleName() + " ****");
		try {
			cleanUpLogin();
			deviceMethods.setTxToSystemProperties(driver, txIpDual);
			deviceMethods.setRxToSystemProperty(driver, rxIpDual);
			Thread.sleep(2000);
			deviceMethods.setTxToSystemProperties(driver, txIp);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			
			deviceMethods.deleteTemplate(driver, txTemplateName);
			deviceMethods.deleteTemplate(driver, rxTemplateName);
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
