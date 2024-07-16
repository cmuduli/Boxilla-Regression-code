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
 * Contains tests for setting dual head device properties to template properties
 * @author Boxilla
 *
 */
public class TemplateDual extends StartupTestCase {

final static Logger log = Logger.getLogger(TemplateDual.class);
	
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();

	
	private String videoQuality = "2";				///return from rest : 1
	private String videoSource = "DVI/DP Optimised";	///return from rest : 1
	private String hidConfig = "Basic";				///return from rest : 1
//	private String audio = "USB";					//return from rest : 2
	private String audio="Analog Audio";
	private String edidDvi1 = "1920x1080";			///return from rest : 0
	private String edidDvi2 = "1920x1200";			///return from rest : 1
	private String mouseTimeout = "3";				///return from rest : 3
	private boolean isManual = false;				//sets Power Mode to Auto
	private boolean isHttpEnabled = false;			//sets HTTP Enabled to Enabled
	private String templateTx = "templateDualTx";
	private String templateRx = "tempplateDualRx";
	
	
	private int timeout = 120000;
	
	
	
	/**
	 * Overriding superclass method to do test specific setup
	 * Creates a device parameter template and sets devices to it
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
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
//			deviceMethods.setTxToSystemProperties(driver, txIpDual);
//			deviceMethods.setRxToSystemProperty(driver, rxIpDual);
//			Thread.sleep(timeout);
			//checkSystemProperties();
			
			deviceMethods.addTemplateTransmitter(driver, templateTx, "Best Quality",
					"Off", "Default",audio, "5", "1920x1200", "1680x1050");
			
			deviceMethods.addTemplateReceiver(driver, templateRx, true, true);
			deviceMethods.setTxToTemplateProperties(driver, txIpDual, templateTx);
			deviceMethods.setRxToTemplateProperties(driver, rxIpDual, templateRx);
			deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	/**
	 * Check video_quality matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald", "chrome"})
	public void test01_checkTemplateVideoQuality() {
		log.info("***** test01_checkTemplateVideoQuality *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	 
	/**
	 * Check edid1 matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test03_checkTemplateEdidDvi1() {
		log.info("***** test03_checkTemplateEdidDvi1 *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));
	}
	
	/**
	 * Check hid matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test04_checkTemplateHid() {
		log.info("***** test04_checkTemplateHid *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * Check edid2 matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test00_checkTemplateEdid2() {
		log.info("***** test00_checkTemplateEdid2 *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(2));
	}
	
	/**
	 * Check power_mode matches template properties
	 */
	//Need to cross check 
	//@Test(groups= {"rest", "emerald2", "notEmerald", "noSE"})
	public void test00_checkTemplatePowerMode() {
		log.info("***** test00_checkTemplatePowerMode *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(0));
	}
	
	/**
	 * Check http_enable matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test00_checkTemplateHttpEnabled() {
		log.info("***** test00_checkTemplateHttpEnabled *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test05_checkTemplateChangeVideoQualityBestQuality() throws InterruptedException {
		log.info("***** test05_checkTemplateChangeVideoQualityBestQuality *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "Best Quality", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));	
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test06_checkTemplateChangeVideoQuality2() throws InterruptedException {
		log.info("***** test06_checkTemplateChangeVideoQuality2 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "2", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(1));	
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test07_checkTemplateChangeVideoQualityDefault() throws InterruptedException {
		log.info("***** test07_checkTemplateChangeVideoQualityDefault *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "Default", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(2));	
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test08_checkTemplateChangeVideoQuality4() throws InterruptedException {
		log.info("***** test08_checkTemplateChangeVideoQuality4 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(3));	
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test09_checkTemplateChangeVideoQualityBestCompression() throws InterruptedException {
		log.info("***** test09_checkTemplateChangeVideoQualityBestCompression *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "Best Compression", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(4));	
	}
	
	/**
	 * Check hid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test15_checkTemplateChangeHidDefault() throws InterruptedException {
		log.info("***** test15_checkTemplateChangeHidDefault *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));	
	}
	
	/**
	 * Check hid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test16_checkTemplateChangeHidBasic() throws InterruptedException {
		log.info("***** test16_checkTemplateChangeHidBasic *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Basic", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(1));	
	}
	
	/**
	 * Check hid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test17_checkTemplateChangeHidMac() throws InterruptedException {
		log.info("***** test17_checkTemplateChangeHidMac *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));	
	}
	
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test17_checkTemplateChangeHidAbsolute() throws InterruptedException {
		log.info("***** test17_checkTemplateChangeHidMac *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Absolute", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(3));	
	}
	
	/**
	 * Check head_1_edid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test18_checkTemplateChangeEdid1_1920x1080() throws InterruptedException {
		log.info("***** test18_checkTemplateChangeEdid1_1920x1080 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1920x1080", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(0));	
	}
	
	/**
	 * Check head_1_edid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test19_checkTemplateChangeEdid1_1920x1200() throws InterruptedException {
		log.info("***** test19_checkTemplateChangeEdid1_1920x1200 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(1));	
	}
	
	/**
	 * Check head_1_edid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test20_checkTemplateChangeEdid1_1680x1050() throws InterruptedException {
		log.info("***** test20_checkTemplateChangeEdid1_1680x1050 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1680x1050", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(2));	
	}

	/**
	 * Check head_1_edid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test21_checkTemplateChangeEdid1_1280x1024() throws InterruptedException {
		log.info("***** test21_checkTemplateChangeEdid1_1280x1024 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1280x1024", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(3));	
	}
	
	/**
	 * Check head_1_edid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test23_checkTemplateChangeEdid1_1024x768() throws InterruptedException {
		log.info("***** test23_checkTemplateChangeEdid1_1024x768 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));	
	}
	
	/**
	 * Check head_2_edid matches template properties after changing template
	 */
	
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test24_checkTemplateChangeEdid2_1920x1080() throws InterruptedException {
		log.info("***** test24_checkTemplateChangeEdid2_1920x1080 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1920x1080");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(0));	
	}
	
	/**
	 * Check head_2_edid matches template properties after changing template
	 */

	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test25_checkTemplateChangeEdid2_1920x1200() throws InterruptedException {
		log.info("***** test25_checkTemplateChangeEdid2_1920x1200 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(1));	
	}
	
	/**
	 * Check head_2_edid matches template properties after changing template
	 */
	
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test26_checkTemplateChangeEdid2_1680x1050() throws InterruptedException {
		log.info("***** test26_checkTemplateChangeEdid2_1680x1050 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1680x1050");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(2));	
	}
	
	/**
	 * Check head_2_edid matches template properties after changing template
	 */
	
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test27_checkTemplateChangeEdid2_1280x1024() throws InterruptedException {
		log.info("***** test27_checkTemplateChangeEdid2_1280x1024 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1280x1024");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(3));	
	}
	
	/**
	 * Check head_2_edid matches template properties after changing template
	 */
	
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test28_checkTemplateChangeEdid2_1024x768() throws InterruptedException {
		log.info("***** test28_checkTemplateChangeEdid2_1024x768 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1024x768");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_2_edid", equalTo(4));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test29_checkMouseKeyboard() {
		log.info("***** test29_checkMouseKeyboard *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test30_checkMouseKeyboard0() throws InterruptedException {
		log.info("***** test30_checkMouseKeyboard0 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "0", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test31_checkMouseKeyboard1() throws InterruptedException {
		log.info("***** test31_checkMouseKeyboard1 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "1", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test32_checkMouseKeyboard2() throws InterruptedException {
		log.info("***** test32_checkMouseKeyboard2 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "2", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test33_checkMouseKeyboard3() throws InterruptedException {
		log.info("***** test33_checkMouseKeyboard3 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "3", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test34_checkMouseKeyboard4() throws InterruptedException {
		log.info("***** test34_checkMouseKeyboard4 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "4", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test35_checkMouseKeyboard5() throws InterruptedException {
		log.info("***** test35_checkMouseKeyboard5 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIpDual, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));	
	}
	
	
	/**
	 * Logs into boxilla and unmanages the two devicves
	 * @throws InterruptedException 
	 */
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		log.info("**** Starting test teardown for " + this.getClass().getSimpleName() + " ****");
		try {
			cleanUpLogin();
			try {
				deviceMethods.setTxToSystemProperties(driver, txIpDual);
				deviceMethods.deleteTemplate(driver, templateTx);
				deviceMethods.setRxToSystemProperty(driver, rxIpDual);
				deviceMethods.deleteTemplate(driver, templateRx);
			}catch(Exception e1) {
				log.info("Templates have already been deleted");
			}
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
		super.afterClass();
		printSuitetDetails(true);
	}
	
//	public void deviceManageTestPrep() throws InterruptedException {
//		log.info("Test Preparation Manage Device");
//		
//		//RX Dual head
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"), rxIpDual, rxDual.getGateway(), rxDual.getNetmask());
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX_Dual", rxDual.getMac(), prop.getProperty("ipCheck"));
//		
//		//TX Dual head
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, txDual.getMac(), prop.getProperty("ipCheck"), txIpDual, txDual.getGateway(), txDual.getNetmask());
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX_Dual", txDual.getMac(), prop.getProperty("ipCheck"));
//		
//		log.info("Appliance Managed Successfully - Test Preparation Completed");
//	}
	
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
		
		//dual head
		log.info("head_2_edid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("head_2_edid", equalTo(1));
		log.info("OK");
		
		//rx
		log.info("power mode...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then()
		.body("power_mode", equalTo(1));
		log.info("OK");
		
		//rx
		log.info("http enable...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual + getPort() + "/control/configuration/rx_settings")
		.then()
		.body("http_enable", equalTo(0));
		log.info("OK");	
		
		log.info("mouse_keyboard_timeout...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIpDual + getPort() + "/control/configuration/tx_settings")
		.then()
		.body("mouse_keyboard_timeout", equalTo(3));
		log.info("OK");
	}
	
}
