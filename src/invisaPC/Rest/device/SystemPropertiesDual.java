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
 * Contains tests for setting device parameters to system properties for dual head devices
 * @author Boxilla
 *
 */
public class SystemPropertiesDual extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(SystemPropertiesDual.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	AppliancePool devicePool = new AppliancePool();
	Device txDual, rxDual;
	
	//test properties

	
	//system properties
	String videoQuality = "2";				///return from rest : 1
	String videoSource = "DVI/DP Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
//	String audio = "USB";					//return from rest : 2
	String audio="Analog Audio";
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private int timeout = 120000;
	
	
	
	/**
	 * Overriding superclass method to do test specific setup
	 * Sets the devices to system properties
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		
		try {
			cleanUpLogin();
			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
					audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			//deviceManageTestPrep();
			deviceMethods.setTxToSystemProperties(driver, txIpDual);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			log.info("Sleeping for 90 seconds while device reboots..");
			deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
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
	 * Checks video_quality matches system properties
	 */
	@Test(groups= {"rest", "notEmerald", "chrome"})
	public void test01_checkVideoQuality() {
		log.info("***** test_01checkVideoQuality *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	
	/**
	 * Checks hid matches system properties
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test03_checkHidConfiguration() {
		log.info("***** test03_checkHidConfiguration *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
	/**
	 * Checks head_1_edid matches system properties
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test05_checkEdidDvi1() {
		log.info("***** test05_checkEdidDvi1 *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
	/**
	 * Checks head_2_edid matches system properties
	 * already commented
	 */
	//@Test(groups= {"rest", "notEmerald"})
	public void test06_checkEdidDvi2() {
		log.info("***** test06_checkEdidDvi2 *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(1));
	}
	
	/**
	 * Checks power_mode matches system properties
	 * commented 
	 */
//	@Test(groups= {"rest", "notEmerald", "noSE"})
	public void test00_checkPowerMode() {
		log.info("***** test08_checkPowerMode *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	/**
	 * Checks http_enable matches system properties
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test00_checkHttpEnabled() {
		log.info("***** test09_checkHttpEnabled *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "smoke", "notEmerald"})
	public void test07_changeVideoQualtiyBestQuality() throws InterruptedException {
		log.info("***** test07_changeVideoQualtiyBestQuality *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Best Quality");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test08_changeVideoQualtiy2() throws InterruptedException {
		log.info("***** test08_changeVideoQualtiy2 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "2");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test09_changeVideoQualtiyDefault() throws InterruptedException {
		log.info("***** test09_changeVideoQualtiyDefault *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Default");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(2));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test10_changeVideoQualtiy4() throws InterruptedException {
		log.info("***** test10_changeVideoQualtiy4 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "4");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);			//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(3));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test11_changeVideoQualtiyBestCompression() throws InterruptedException {
		log.info("***** test11_changeVideoQualtiyBestCompression *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Video Quality", "Best Compression");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword); 				//wait for device to reboot 
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(4));
	}
	
	/**
	 * Checks hid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test12_changeHidDefault() throws InterruptedException {
		log.info("***** test12_changeHidDefault *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Default");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * Checks hid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test13_changeHidBasic() throws InterruptedException {
		log.info("***** test13_changeHidBasic *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Basic");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
	/**
	 * Checks video_quality matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test14_changeHidMac() throws InterruptedException {
		log.info("***** test14_changeHidMac *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "MAC");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test14_changeHidAbsolute() throws InterruptedException {
		log.info("***** test14_changeHidMac *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "HID Config", "Absolute");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(3));
	}
	
	/**
	 * Checks head_1_edid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test15_changeEdidDvi1_1920x1080() throws InterruptedException {
		log.info("***** test15_changeEdidDvi1_1920x1080 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1920x1080");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
	/**
	 * Checks head_1_edid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test16_changeEdidDvi1_1920x1200() throws InterruptedException {
		log.info("***** test16_changeEdidDvi1_1920x1200 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1920x1200");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	
	/**
	 * Checks head_1_edid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test17_changeEdidDvi1_1680x1050() throws InterruptedException {
		log.info("***** test17_changeEdidDvi1_1680x1050 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1680x1050");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(2));
	}
	
	/**
	 * Checks head_1_edid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test18_changeEdidDvi1_1280x1024() throws InterruptedException {
		log.info("***** test18_changeEdidDvi1_1280x1024 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1280x1024");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(3));
	}
	
	/**
	 * Checks head_1_edid matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test19_changeEdidDvi1_1024x768() throws InterruptedException {
		log.info("***** test19_changeEdidDvi1_1024x768 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1024x768");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	/////////
	/**
	 * Checks head_2_edid matches system properties after changing
	 * @throws InterruptedException
	 * commented
	 */
	//@Test(groups= {"rest", "smoke", "notEmerald"})
	public void test20_changeEdidDvi2_1920x1080() throws InterruptedException {
		log.info("***** test20_changeEdidDvi2_1920x1080 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI2", "1920x1080");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(0));
	}
	
	/**
	 * Checks head_2_edid matches system properties after changing
	 * @throws InterruptedException
	 * commeneted
	 */
	//@Test(groups= {"rest", "notEmerald"})
	public void test21_changeEdidDvi2_1920x1200() throws InterruptedException {
		log.info("***** test21_changeEdidDvi2_1920x1200 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI2", "1920x1200");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(1));
	}
	
	/**
	 * Checks head_2_edid matches system properties after changing
	 * @throws InterruptedException
	 * commeneted
	 */
	//@Test(groups= {"rest", "notEmerald"})
	public void test22_changeEdidDvi2_1680x1050() throws InterruptedException {
		log.info("***** test22_changeEdidDvi2_1680x1050 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI2", "1680x1050");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(2));
	}
	
	/**
	 * Checks head_2_edid matches system properties after changing
	 * @throws InterruptedException
	 * cmd
	 */
	//@Test(groups= {"rest", "notEmerald"})
	public void test23_changeEdidDvi2_1280x1024() throws InterruptedException {
		log.info("***** test23_changeEdidDvi2_1280x1024 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI2", "1280x1024");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(3));
	}
	
	/**
	 * Checks head_2_edid matches system properties after changing
	 * @throws InterruptedException
	 * cmd
	 */
	//@Test(groups= {"rest", "notEmerald"})
	public void test24_changeEdidDvi2_1024x768() throws InterruptedException {
		log.info("***** test24_changeEdidDvi2_1024x768 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI2", "1024x768");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(4));
	}
	
	/**
	 * Checks power_mode matches system properties after changing
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "notEmerald", "noSE"})
	public void test33_changePowerModeManual() throws InterruptedException {
		log.info("***** test33_changePowerModeManual *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "Power Mode", true);
		deviceMethods.checkDeviceOnline(driver, rxIpDual);
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	
	/**
	 * Checks power_mode matches system properties after changing
	 * @throws InterruptedException
	 */
//	@Test(groups= {"rest", "notEmerald", "noSE"})
	public void test34_changePowerModeAuto() throws InterruptedException {
		log.info("***** test34_changePowerModeAuto *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "Power Mode", false);
		deviceMethods.checkDeviceOnline(driver, rxIpDual);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(70000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	/**
	 * Checks http_enable matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test35_changeHttpEnabledDisabled() throws InterruptedException {
		log.info("***** test35_changeHttpEnabledDisabled *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "HTTP Enabled", false);
		deviceMethods.checkDeviceOnline(driver, rxIp);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(70000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	
	/**
	 * Checks http_enable matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test36_changeHttpEnabledEnabled() throws InterruptedException {
		log.info("***** test35_changeHttpEnabledDisabled *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "HTTP Enabled", true);
		deviceMethods.checkDeviceOnline(driver, rxIp);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(70000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test37_checkMouseTimeout() {
		log.info("***** test37_checkMouseTimeout *****");;
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test38_changeMouseTimeout0() throws InterruptedException {
		log.info("***** test38_changeMouseTimeout0 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "0");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test39_changeMouseTimeout1() throws InterruptedException {
		log.info("***** test39_changeMouseTimeout1 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "1");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test40_changeMouseTimeout2() throws InterruptedException {
		log.info("***** test40_changeMouseTimeout2 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "2");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test42_changeMouseTimeout3() throws InterruptedException {
		log.info("***** test42_changeMouseTimeout3 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "3");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test43_changeMouseTimeout4() throws InterruptedException {
		log.info("***** test43_changeMouseTimeout4 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "4");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));
	}
	
	/**
	 * Checks mouse_keyboard_timeout matches system properties after changing
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "notEmerald"})
	public void test44_changeMouseTimeout5() throws InterruptedException {
		log.info("***** test44_changeMouseTimeout5 *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "Mouse Timeout", "5");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}

	
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"), rxIp, rxDual.getGateway(), rxDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX_Dual", rxDual.getMac(), prop.getProperty("ipCheck"));
		
		//TX Dual head
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txDual.getMac(), prop.getProperty("ipCheck"), txIpDual, txDual.getGateway(), txDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX_Dual", txDual.getMac(), prop.getProperty("ipCheck"));
		
		
		log.info("Appliance Managed Successfully - Test Preparation Completed");
	}

}
