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
public class Unique4k extends StartupTestCase { 
	
final static Logger log = Logger.getLogger(Unique4k.class);
	
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	

	private String hidConfig = "Basic";				///return from rest : 1
	private String edidDvi1 = "Default";			///return from rest : 0
	private String mouseTimeout = "3";				///return from rest : 3
	private boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	
	
	private int timeout = 140000;
	

	/**
	 * Checks the initial system properties after managing a device
	 * We need to be certain that these values are correct so that 
	 * when a template is applied, we know the values have changed
	 */
	private void checkSystemProperties() {
		log.info("Checking system properties are applied");
		
		
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
			deviceMethods.setSystemProperties(driver, "2", "DVI Optimised", hidConfig,
					"USB", mouseTimeout, edidDvi1, "1920x1200", false, isHttpEnabled);
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
	public void test14_changeEdid1_3840x2160p60Hz() throws InterruptedException {
		log.info("***** test14_changeEdid1_3840x2160p60Hz*****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "3840x2160p-60Hz");
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
	public void test15_changeEdid1_3840x2160p30Hz() throws InterruptedException {
		log.info("***** test15_changeEdid1_3840x2160p30Hz *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "3840x2160p-30Hz");
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
	public void test16_changeEdid1_2560x1440p60Hz() throws InterruptedException {
		log.info("***** test16_changeEdid1_2560x1440p60Hz *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "2560x1440p-60Hz");
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
	public void test17_changeEdid1_1920x1080p60Hz() throws InterruptedException {
		log.info("***** test17_changeEdid1_1920x1080p60Hz *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1920x1080p-60Hz");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test18_changeEdid1_Default() throws InterruptedException {
		log.info("***** test16_changeEdid1_Default *****");
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "Default");
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));
	}
	
//	
//	/**
//	 * Checks head_1_edid matches unique properties after changing
//	 * @throws InterruptedException
//	 */
//	@Test(groups= {"rest", "emerald2", "notEmerald"})
//	public void test18_changeEdid1_1024x768() throws InterruptedException {
//		log.info("***** test18_changeEdid1_1024x768 *****");
//		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", "1024x768");
//		log.info("Sleeping while device reboots..");
//		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
//		.then().assertThat().statusCode(200)
//		.body("head_1_edid", equalTo(4));
//	}
//	
//	/**
//	 * Checks power_mode matches unique properties after changing
//	 * @throws InterruptedException
//	 */
//	@Test(groups= {"rest", "smoke", "emerald2", "notEmerald", "noSE"})
//	public void test24_changePowerModeManual() throws InterruptedException {
//		log.info("***** test24_changePowerModeEnabled *****");
//		deviceMethods.setUniquePropertyRx(driver, rxIp, "Power Mode", true);
//		log.info("Sleeping while device reboots..");
//		Thread.sleep(70000);
//		
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
//		.then().assertThat().statusCode(200)
//		.body("power_mode", equalTo(0));
//	}
//	
//	/**
//	 * Checks power_mode matches unique properties after changing
//	 * @throws InterruptedException
//	 */
//	@Test(groups= {"rest", "emerald2", "notEmerald", "noSE"})
//	public void test25_changePowerModeAuto() throws InterruptedException {
//		log.info("***** test25_changePowerModeAuto *****");
//		deviceMethods.setUniquePropertyRx(driver, rxIp, "Power Mode", false);
//		log.info("Sleeping while device reboots..");
//		Thread.sleep(70000);
//		
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
//		.then().assertThat().statusCode(200)
//		.body("power_mode", equalTo(1));
//	}
//	
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
