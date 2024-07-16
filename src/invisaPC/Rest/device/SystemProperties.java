package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.Assert;
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
import methods.SystemMethods;
import testNG.Utilities;
/**
 * Contains tests for setting device parameters to system properties for single head devices
 * @author Boxilla
 *
 */
public class SystemProperties extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(SystemProperties.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	AppliancePool devicePool = new AppliancePool();
	Device txSingle, rxSingle;
	

	
	//system properties
	String videoQuality = "2";				///return from rest : 1
	String videoSource = "DVI/DP Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
//	String audio = "USB";	
	String audio = "DisplayPort Audio";                                     //return from rest : 2
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private int timeout = 120000;
	String audio1="Analog Audio";
//	String AudioSource="Analog Audio";
	
	
	
	/**
	 * Overriding superclass method to do test specific set up
	 * Sets devices to system properties
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		
		try {
			cleanUpLogin();
			if(!StartupTestCase.isZeroU) {
			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
					audio1, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			}else {
				deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
						audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			}
			//deviceManageTestPrep();
			deviceMethods.setTxToSystemProperties(driver, txIp);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			log.info("Sleeping for 90 seconds while device reboots..");
			
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
			//Thread.sleep(timeout);
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}


	/**
	 * Checks that video_quality matches system properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald", "chrome"})
	public void test01_checkVideoQuality() {
		log.info("***** test_01checkVideoQuality *****");
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	/**
	 * check that video_source_optimisation matches system properties
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test02_checkDefaultVideoSource() {
		log.info("***** test02_checkDefaultVideoSource *****");
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(1));
	}
	/**
	 * check that hid matches system properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test03_checkHidConfiguration() {
		log.info("***** test03_checkHidConfiguration *****");
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
//	AUDIO WILL BE NEXT RELEASE
//	@Test
//	public void test04_checkAudio() {
//		log.info("***** test04_checkAudio *****");
//		//check device REST API
//	}
	/**
	 * Check that edid1 matches system properties 
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test05_checkEdidDvi1() {
		log.info("***** test05_checkEdidDvi1 *****");
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}

	/**
	 * Check that powermode matches system properties
	 */
//	@Test(groups= {"rest", "emerald", "chrome", "noSE"})
	public void test08_checkPowerMode() {
		log.info("***** test08_checkPowerMode *****");
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	/**
	 * Check that http matches system properties
	 */
	@Test(groups= {"rest", "emerald"})
	public void test09_checkHttpEnabled() {
		log.info("***** test09_checkHttpEnabled *****");
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Check that video_quality matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "smoke", "emerald2", "notEmerald"})
	public void test10_changeVideoQualtiyBestQuality() throws InterruptedException {
		log.info("***** test10_changeVideoQualtiyBestQuality *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Best Quality");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	
	/**
	 * Check that video_quality matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test11_changeVideoQualtiy2() throws InterruptedException {
		log.info("***** test11_changeVideoQualtiy2 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "2");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	
	/**
	 * Check that video_quality matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test12_changeVideoQualtiyDefault() throws InterruptedException {
		log.info("***** test12_changeVideoQualtiyDefault *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Default");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(2));
	}
	
	/**
	 * Check that video_quality matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test13_changeVideoQualtiy4() throws InterruptedException {
		log.info("***** test13_changeVideoQualtiy4 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "4");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(3));
	}
	
	/**
	 * Check that video_quality matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test14_changeVideoQualtiyBestCompression() throws InterruptedException {
		log.info("***** test14_changeVideoQualtiyBestCompression *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Best Compression");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(4));
	}
	
	/**
	 * Check that video_source_optimisation matches system properties after changing 
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test15_changeVideoSourceOff() throws InterruptedException {
		log.info("***** test15_changeVideoSourceOff *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Source", "Off");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(0));
	}
	
	/**
	 * Check that video_source_optimisation matches system properties after changing 
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test16_changeVideoSourceDviOptimised() throws InterruptedException {
		log.info("***** test16_changeVideoSourceDviOptimised *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Source", "DVI Optimised");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(1));
	}
	
	/**
	 * Check that video_source_optimisation matches system properties after changing 
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test17_changeVideoSourceVgaHighPerformance () throws InterruptedException {
		log.info("***** test17_changeVideoSourceVgaHighPerformance *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Source", "VGA - High Performance");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(2));
	}
	
	/**
	 * Check that video_source_optimisation matches system properties after changing 
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test18_changeVideoSourceVgaOptimised () throws InterruptedException {
		log.info("***** test18_changeVideoSourceVgaOptimised *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Source", "VGA - Optimised");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(3));
	}
	
	/**
	 * Check that video_source_optimisation matches system properties after changing 
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test19_changeVideoSourceVgaLowBandwidth () throws InterruptedException {
		log.info("***** test19_changeVideoSourceVgaLowBandwidth *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Source", "VGA - Low Bandwidth");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(4));
	}
	
	/**
	 * Check that hid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test20_changeHidDefault() throws InterruptedException {
		log.info("***** test20_changeHidDefault *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Default");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * Check that hid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test21_changeHidBasic() throws InterruptedException {
		log.info("***** test21_changeHidBasic *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Basic");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
	/**
	 * Check that hid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test22_changeHidMac() throws InterruptedException {
		log.info("***** test22_changeHidMac *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "MAC");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));
	}
	
	/**
	 * Check that hid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test45_changeHidAbsolute() throws InterruptedException {
		log.info("***** test22_changeHidMac *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Absolute");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(3));
	}
	
	
	//NOT SUPPORTED THIS RELEASE
//	public void test13_changeAudio() throws InterruptedException {
//		log.info("***** test13_changeAudio *****");
//		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Audio", "Analog");
//		Thread.sleep(60000);
//		//check device REST API
//	}

	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test23_changeEdidDvi1_1920x1080() throws InterruptedException {
		log.info("***** test23_changeEdidDvi1_1920x1080 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1920x1080");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test24_changeEdidDvi1_1920x1200() throws InterruptedException {
		log.info("***** test24_changeEdidDvi1_1920x1200 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1920x1200");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	
	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test25_changeEdidDvi1_1680x1050() throws InterruptedException {
		log.info("***** test25_changeEdidDvi1_1680x1050 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1680x1050");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(2));
	}
	
	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test26_changeEdidDvi1_1280x1024() throws InterruptedException {
		log.info("***** test25_changeEdidDvi1_1280x1024 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1280x1024");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(3));
	}
	
	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test27_changeEdidDvi1_1024x768() throws InterruptedException {
		log.info("***** test27_changeEdidDvi1_1024x768 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1024x768");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	/**
	 * Check that power_mode matches system properties after changing 
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "smoke", "emerald", "noSE"})
	public void test33_changePowerModeManual() throws InterruptedException {
		log.info("***** test33_changePowerModeManual *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "Power Mode", true);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	
	/**
	 * Check that power_mode matches system properties after changing 
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "emerald", "noSE"})
	public void test34_changePowerModeAuto() throws InterruptedException {
		log.info("***** test34_changePowerModeAuto *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "Power Mode", false);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	/**
	 * Check that http_enable matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald"})
	public void test35_changeHttpEnabledDisabled() throws InterruptedException {
		log.info("***** test35_changeHttpEnabledDisabled *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "HTTP Enabled", false);
		deviceMethods.checkDeviceOnline(driver, rxIp);
//		log.info("Sleeping for 90 seconds while device reboots..");
//		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	
	/**
	 * Check that http_enable matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald"})
	public void test36_changeHttpEnabledEnabled() throws InterruptedException {
		log.info("***** test35_changeHttpEnabledDisabled *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "HTTP Enabled", true);
		deviceMethods.checkDeviceOnline(driver, rxIp);
//		log.info("Sleeping for 90 seconds while device reboots..");
//		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test37_checkMouseTimeout() {
		log.info("***** test37_checkMouseTimeout *****");;
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test38_changeMouseTimeout0() throws InterruptedException {
		log.info("***** test38_changeMouseTimeout0 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "0");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test39_changeMouseTimeout1() throws InterruptedException {
		log.info("***** test39_changeMouseTimeout1 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "1");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test40_changeMouseTimeout2() throws InterruptedException {
		log.info("***** test40_changeMouseTimeout2 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "2");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test42_changeMouseTimeout3() throws InterruptedException {
		log.info("***** test42_changeMouseTimeout3 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "3");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test43_changeMouseTimeout4() throws InterruptedException {
		log.info("***** test43_changeMouseTimeout4 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "4");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));
	}
	
	/**
	 * Check that mouse_keyboard_timeout matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test44_changeMouseTimeout5() throws InterruptedException {
		log.info("***** test44_changeMouseTimeout5 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "5");
		 deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	
	/**
	 * Checks the return code of /control/configuration/tx_settings equals 200
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void statusCodeOk() throws InterruptedException {
		int status = given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.getStatusCode();
		Assert.assertTrue(status == 200, "Return code did mnot equal 200, actual " + status);
	}
	
	/**
	 * Checks the return code of /control/configuration/tx_settings  does not equal 400
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "emerald"})
	public void statusCodeBadRequest() throws InterruptedException {
		int status = given().header(getHead())
		.when()
		.get(http + "://" + rxIp + port + "/control/configuration/tx_settings")
		.getStatusCode();
		Assert.assertTrue(status == 400, "Return code did mnot equal 400, actual " + status);
	}
	
	/**
	 * Checks the return code of /control/configuration/tx_settings equals 401 after passing wrong password
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void statusCodeUnauthorized() throws InterruptedException {
		
		RestAssured.authentication = basic(restuser, "badPassword");
		
		int status = given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.getStatusCode();
		Assert.assertTrue(status == 401, "Return code did mnot equal 401, actual " + status);
		RestAssured.authentication = basic(restuser, restPassword);
	}
	
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"), rxIp, rxSingle.getGateway(), rxSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", rxSingle.getMac(), prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"), txIp, txSingle.getGateway(), txSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", txSingle.getMac(), prop.getProperty("ipCheck"));
		
		log.info("Appliance Managed Successfully - Test Preparation Completed");
	}
	
}
