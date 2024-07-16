package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.AppliancePool;
import extra.Device;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;
/**
 * Contains tests for setting and changing single head devices properties using boxilla unique settings
 * @author Boxilla
 *
 */
public class Unique extends StartupTestCase { 
	
final static Logger log = Logger.getLogger(Unique.class);
	
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	
	private String videoQuality = "2";				///return from rest : 1
//	private String videoSource = "DVI Optimised";
	private String videoSource = "DVI/DP Optimised";    ///return from rest : 1
	private String hidConfig = "Basic";				///return from rest : 1
//	private String audio = "USB";					//return from rest : 2
	private String edidDvi1 = "1920x1080";			///return from rest : 0
	private String edidDvi2 = "1920x1200";			///return from rest : 1
	private String mouseTimeout = "3";				///return from rest : 3
	private boolean isManual = false;				//sets Power Mode to Auto
	private boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	String audio = "DisplayPort Audio";
	String audio1="Analog Audio";
	
	private int timeout = 140000;
	

	/**
	 * Checks the initial system properties after managing a device
	 * We need to be certain that these values are correct so that 
	 * when a template is applied, we know the values have changed
	 */
	private void checkSystemProperties() {
		log.info("Checking system properties are applied");
		
		log.info("video_quality...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
		log.info("OK");
		

//		log.info("video_source_optimisation...");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
//		.then().assertThat().statusCode(200)
//		.body("video_source_optimisation", equalTo(1));
		log.info("OK");
		
		log.info("hid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
		log.info("OK");
		
		log.info("head_1_edid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
		log.info("OK");
		
		//rx
		log.info("power mode...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
		log.info("OK");
		
		//rx
		log.info("http enable...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
		log.info("OK");	
		
		log.info("mouse_keyboard_timeout...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
		log.info("OK");
		
		
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		System.out.println(getHttp() + getPort());
		
		try {
			cleanUpLogin();
//			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
//					audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			if(!StartupTestCase.isZeroU) {
				deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
						audio1, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}else {
					deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
							audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}
			//deviceManageTestPrep();
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			deviceMethods.setTxToSystemProperties(driver, txIp);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			log.info("Sleeping while device reboots..");
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
			checkSystemProperties();
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	/**
	 * Checks video_quality matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "smoke", "emerald2", "notEmerald", "chrome"})
	public void test01_changeVideoQualityBestQuality() throws InterruptedException {
		log.info("***** test01_changeVideoQualityBestQuality *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "Best Quality");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	
	/**
	 * Checks video_quality matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test02_changeVideoQuality2() throws InterruptedException {
		log.info("***** test02_changeVideoQuality2 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "2");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	
	/**
	 * Checks video_quality matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test03_changeVideoQualityDefault() throws InterruptedException {
		log.info("***** test03_changeVideoQualityDefault *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "Default");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(2));
	}
	
	/**
	 * Checks video_quality matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test04_changeVideoQuality4() throws InterruptedException {
		log.info("***** test04_changeVideoQuality4 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "4");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(3));
	}
	
	/**
	 * Checks video_quality matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test05_changeVideoQualityBestCompression() throws InterruptedException {
		log.info("***** test05_changeVideoQualityBestCompression *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "Best Compression");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("video_quality", equalTo(4));
	}
	
	/**
//	 * Checks video_source_optimisation matches unique properties after changing
//	 * @throws InterruptedException
//	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test06_changeVideoSourceOptimisationOff() throws InterruptedException {
		log.info("***** test06_changeVideoSourceOptimisationOff *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", "Off");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(0));
	}
	
	/**
	 * Checks video_source_optimisation matches unique properties after changing
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test07_changeVideoSourceOptimisationDviOptimised() throws InterruptedException {
		log.info("***** test07_changeVideoSourceOptimisationDviOptimised *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", "DVI Optimised");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(1));
	}
	
	/**
	 * Checks video_source_optimisation matches unique properties after changing
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test08_changeVideoSourceOptimisationVgaHighPerformance() throws InterruptedException {
		log.info("***** test08_changeVideoSourceOptimisationVgaHighPerformance *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", "VGA - High Performance");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(2));
	}
	
	/**
	 * Checks video_source_optimisation matches unique properties after changing
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test09_changeVideoSourceOptimisationVgaOptimised() throws InterruptedException {
		log.info("***** test09_changeVideoSourceOptimisationVgaOptimised *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", "VGA - Optimised");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(3));
	}
	
	/**
	 * Checks video_source_optimisation matches unique properties after changing
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test10_changeVideoSourceOptimisationVgaLowBandwidth() throws InterruptedException {
		log.info("***** test10_changeVideoSourceOptimisationVgaLowBandwidth *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", "VGA - Low Bandwidth");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(4));
	}
	
	/**
	 * Checks hid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test11_changeHidDefault() throws InterruptedException {
		log.info("***** test11_changeHidDefault *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "HID", "Default");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * Checks hid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test12_changeHidBasic() throws InterruptedException {
		log.info("***** test12_changeHidBasic *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "HID", "Basic");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
	/**
	 * Checks hid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test13_changeHidMac() throws InterruptedException {
		log.info("***** test13_changeHidMac *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "HID", "MAC");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));
	}
	
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test13_changeHidAbsolute() throws InterruptedException {
		log.info("***** test13_changeHidMac *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "HID", "Absolute");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(3));
	}
	
	/**
	 * Checks head_1_edid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test14_changeEdid1_1920x1080() throws InterruptedException {
		log.info("***** test14_changeEdid1_1920x1080 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1920x1080");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
	/**
	 * Checks head_1_edid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test15_changeEdid1_1920x1200() throws InterruptedException {
		log.info("***** test15_changeEdid1_1920x1200 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1920x1200");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	
	/**
	 * Checks head_1_edid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test16_changeEdid1_1680x1050() throws InterruptedException {
		log.info("***** test16_changeEdid1_1680x1050 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1680x1050");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(2));
	}
	
	/**
	 * Checks head_1_edid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test17_changeEdid1_1280x1024() throws InterruptedException {
		log.info("***** test17_changeEdid1_1280x1024 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1280x1024");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(3));
	}
	
	/**
	 * Checks head_1_edid matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test18_changeEdid1_1024x768() throws InterruptedException {
		log.info("***** test18_changeEdid1_1024x768 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1024x768");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	/**
	 * Checks power_mode matches unique properties after changing
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "smoke", "emerald2", "notEmerald", "noSE"})
	public void test24_changePowerModeManual() throws InterruptedException {
		log.info("***** test24_changePowerModeEnabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIp, "Power Mode", true);
		log.info("Sleeping while device reboots..");
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	
	/**
	 * Checks power_mode matches unique properties after changing
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "emerald2", "notEmerald", "noSE"})
	public void test25_changePowerModeAuto() throws InterruptedException {
		log.info("***** test25_changePowerModeAuto *****");
		deviceMethods.setUniquePropertyRx(driver, rxIp, "Power Mode", false);
		log.info("Sleeping while device reboots..");
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	/**
	 * Checks http_enable matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test26_changeHttpEnabledEnabled() throws InterruptedException {
		log.info("***** test26_changeHttpEnableEnabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", true);
		log.info("Sleeping while device reboots..");
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Checks http_enable matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test27_changeHttpEnabledDisabled() throws InterruptedException {
		log.info("***** test27_changeHttpEnabledDisabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", false);
		log.info("Sleeping while device reboots..");
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test28_changeMouseKeyboard0() throws InterruptedException {
		log.info("***** test28_changeMouseKeyboard0 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "0");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test29_changeMouseKeyboard1() throws InterruptedException {
		log.info("***** test29_changeMouseKeyboard1 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "1");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test30_changeMouseKeyboard2() throws InterruptedException {
		log.info("***** test30_changeMouseKeyboard2 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "2");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test31_changeMouseKeyboard3() throws InterruptedException {
		log.info("***** test31_changeMouseKeyboard3 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "3");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test32_changeMouseKeyboard4() throws InterruptedException {
		log.info("***** test32_changeMouseKeyboard4 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "4");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches unique properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test33_changeMouseKeyboard5() throws InterruptedException {
		log.info("***** test33_changeMouseKeyboard5 *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "Mouse Timeout", "5");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	
}
