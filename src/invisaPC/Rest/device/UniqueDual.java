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
 * Contains tests for setting and changing dual head devices properties using boxilla unique settings
 * @author Boxilla
 *
 */
public class UniqueDual extends StartupTestCase {
	
final static Logger log = Logger.getLogger(UniqueDual.class);
	
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	
	private String videoQuality = "2";				///return from rest : 1
	private String videoSource = "DVI/DP Optimised";	///return from rest : 1
	private String hidConfig = "Basic";				///return from rest : 1
//	private String audio = "USB";					//return from rest : 2
	private String edidDvi1 = "1920x1080";			///return from rest : 0
	private String edidDvi2 = "1920x1200";			///return from rest : 1
	private String mouseTimeout = "3";				///return from rest : 3
	private boolean isManual = false;				//sets Power Mode to Auto
	private boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private int timeout = 120000;
	String audio1 = "DisplayPort Audio";
	String audio="Analog Audio";
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		System.out.println(getHttp() + getPort());
		
		try {
			cleanUpLogin();
			if(!StartupTestCase.isZeroU) {
			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
					audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			}else 
			{
				deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
						audio1, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			}
			//deviceManageTestPrep();
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			deviceMethods.setTxToSystemProperties(driver, txIpDual);
			deviceMethods.setRxToSystemProperty(driver, rxIpDual);
			deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
			//checkSystemProperties();
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	
	@Test(groups= {"rest", "notEmerald", "chrome"})
	public void test01_changeVideoQualityBestQuality() throws InterruptedException {
		log.info("***** test01_changeVideoQualityBestQuality *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Video Quality", "Best Quality");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp()+ "://" +  txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	
	@Test(groups= {"rest", "smoke", "notEmerald"})
	public void test02_changeVideoQuality2() throws InterruptedException {
		log.info("***** test02_changeVideoQuality2 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Video Quality", "2");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test03_changeVideoQualityDefault() throws InterruptedException {
		log.info("***** test03_changeVideoQualityDefault *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Video Quality", "Default");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(2));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test04_changeVideoQuality4() throws InterruptedException {
		log.info("***** test04_changeVideoQuality4 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Video Quality", "4");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(3));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test05_changeVideoQualityBestCompression() throws InterruptedException {
		log.info("***** test05_changeVideoQualityBestCompression *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Video Quality", "Best Compression");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(4));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test11_changeHidDefault() throws InterruptedException {
		log.info("***** test11_changeHidDefault *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "HID", "Default");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test12_changeHidBasic() throws InterruptedException {
		log.info("***** test12_changeHidBasic *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "HID", "Basic");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test13_changeHidMac() throws InterruptedException {
		log.info("***** test13_changeHidMac *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "HID", "MAC");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test13_changeHidAbsolute() throws InterruptedException {
		log.info("***** test13_changeHidMac *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "HID", "Absolute");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(3));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test14_changeEdid1_1920x1080() throws InterruptedException {
		log.info("***** test14_changeEdid1_1920x1080 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID1", "1920x1080");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test15_changeEdid1_1920x1200() throws InterruptedException {
		log.info("***** test15_changeEdid1_1920x1200 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID1", "1920x1200");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test16_changeEdid1_1680x1050() throws InterruptedException {
		log.info("***** test16_changeEdid1_1680x1050 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID1", "1680x1050");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(2));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test17_changeEdid1_1280x1024() throws InterruptedException {
		log.info("***** test17_changeEdid1_1280x1024 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID1", "1280x1024");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(3));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test18_changeEdid1_1024x768() throws InterruptedException {
		log.info("***** test18_changeEdid1_1024x768 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID1", "1024x768");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	//@Test(groups= {"rest", "notEmerald"})
	public void test19_changeEdid2_1920x1080() throws InterruptedException {
		log.info("***** test19_changeEdid2_1920x1080 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID2", "1920x1080");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(0));
	}
	
	//@Test(groups= {"rest", "notEmerald"})
	public void test20_changeEdid2_1920x1200() throws InterruptedException {
		log.info("***** test20_changeEdid2_1920x1200 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID2", "1920x1200");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(1));
	}
	
	//@Test(groups= {"rest", "notEmerald"})
	public void test21_changeEdid2_1680x1050() throws InterruptedException {
		log.info("***** test21_changeEdid2_1680x1050 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID2", "1680x1050");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(2));
	}
	
//	@Test(groups= {"rest", "notEmerald"})
	public void test22_changeEdid2_1280x1024() throws InterruptedException {
		log.info("***** test22_changeEdid2_1280x1024 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID2", "1280x1024");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(3));
	}
	
	//@Test(groups= {"rest", "smoke", "notEmerald"})
	public void test23_changeEdid2_1024x768() throws InterruptedException {
		log.info("***** test23_changeEdid2_1024x768 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "EDID2", "1024x768");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(4));
	}
	
	@Test(groups= {"rest", "notEmerald", "noSE"})
	public void test24_changePowerModeManual() throws InterruptedException {
		log.info("***** test24_changePowerModeEnabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIpDual, "Power Mode", true);
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	
	@Test(groups= {"rest", "notEmerald", "noSE"})
	public void test25_changePowerModeAuto() throws InterruptedException {
		log.info("***** test25_changePowerModeAuto *****");
		deviceMethods.setUniquePropertyRx(driver, rxIpDual, "Power Mode", false);
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(1));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test26_changeHttpEnabledEnabled() throws InterruptedException {
		log.info("***** test26_changeHttpEnableEnabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIpDual, "HTTP Enabled", true);
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	@Test(groups= {"rest", "smoke", "notEmerald"})
	public void test27_changeHttpEnabledDisabled() throws InterruptedException {
		log.info("***** test27_changeHttpEnabledDisabled *****");
		deviceMethods.setUniquePropertyRx(driver, rxIpDual, "HTTP Enabled", false);
		Thread.sleep(70000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(0));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test28_changeMouseKeyboard0() throws InterruptedException {
		log.info("***** test28_changeMouseKeyboard0 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "0");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));
	}
	@Test(groups= {"rest", "notEmerald"})
	public void test29_changeMouseKeyboard1() throws InterruptedException {
		log.info("***** test29_changeMouseKeyboard1 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "1");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));
	}
	@Test(groups= {"rest", "notEmerald"})
	public void test30_changeMouseKeyboard2() throws InterruptedException {
		log.info("***** test30_changeMouseKeyboard2 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "2");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));
	}
	@Test(groups= {"rest", "notEmerald"})
	public void test31_changeMouseKeyboard3() throws InterruptedException {
		log.info("***** test31_changeMouseKeyboard3 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "3");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);;
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test32_changeMouseKeyboard4() throws InterruptedException {
		log.info("***** test32_changeMouseKeyboard4 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "4");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));
	}
	
	@Test(groups= {"rest", "notEmerald"})
	public void test33_changeMouseKeyboard5() throws InterruptedException {
		log.info("***** test33_changeMouseKeyboard5 *****");
		deviceMethods.setUniquePropertyTx(driver, txIpDual, "Mouse Timeout", "5");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	
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
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("video_quality", equalTo(1));
		log.info("OK");
		
		log.info("hid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("hid", equalTo(1));
		log.info("OK");
		
		log.info("head_1_edid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("head_1_edid", equalTo(0));
		log.info("OK");
		
		log.info("head_2_edid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("head_2_edid", equalTo(1));
		log.info("OK");
		
		
		//RX
		log.info("power_mode...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then()
		.body("power_mode", equalTo(1));
		log.info("OK");
		
		log.info("http_enabled..");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then()
		.body("http_enable", equalTo(1));
		
		log.info("mouse_keyboard_timeout...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("mouse_keyboard_timeout", equalTo(3));
		log.info("OK");
	}

}
