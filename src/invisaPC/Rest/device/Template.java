package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Timer;

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
 * Contains tests for setting single head device properties to template properties
 * @author Boxilla
 *
 */
public class Template extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(Template.class);
	
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
	private boolean isHttpEnabled = false;			//sets HTTP Enabled to Enabled
	private String templateTx = "TemplateTestTX";
	private String templateRx = "TemplateTestRX";
	String audio = "DisplayPort Audio";
	String audio1="Analog Audio";
	
	
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
//		deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
//				audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
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
			Thread.sleep(65000);
			driver.navigate().refresh();
			deviceMethods.setTxToSystemProperties(driver, txIp);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			//Thread.sleep(timeout);
//			checkSystemProperties(true, true);
			if((!StartupTestCase.isZeroU)) {
			deviceMethods.addTemplateTransmitter(driver, templateTx, "Best Quality",
					"Off", "Default",audio1, "5", "1920x1200", "1680x1050");
			}else {
				deviceMethods.addTemplateTransmitter(driver, templateTx, "Best Quality",
						"Off", "Default",audio, "5", "1920x1200", "1680x1050");
			}
			deviceMethods.addTemplateReceiver(driver, templateRx, true, true);
			deviceMethods.setTxToTemplateProperties(driver, txIp, templateTx);
			deviceMethods.setRxToTemplateProperties(driver, rxIp, templateRx);
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
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
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(0));
	}
	
	/**
	 * Check video_source_optimisation matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test02_checkTemplateVideoSource() {
		log.info("***** test02_checkTemplateVideoSource *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(0));
	} 
	
	/**
	 * Check head_1_edid matches template properties
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test03_checkTemplateEdidDvi1() {
		log.info("***** test03_checkTemplateEdidDvi1 *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(0));
	}
	
	/**
	 * Check power_mode matches template properties
	 */
	// SE,PE,4K Emralde dose not support power mode
	
	//	@Test(groups= {"rest", "emerald", "noSE"})
	public void test00_checkTemplatePowerMode() {
		log.info("***** test00_checkTemplatePowerMode *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("power_mode", equalTo(""));
	}
	
	/**
	 * Check http_enable matches template properties
	 */
	@Test(groups= {"rest", "emerald"})
	public void test00_checkTemplateHttpEnabled() {
		log.info("***** test00_checkTemplateHttpEnabled *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
		.then().assertThat().statusCode(200)
		.body("http_enable", equalTo(1));
	}
	
	/**
	 * Check video_quality matches template properties after changing template
	 */
	@Test(groups= {"rest", "smoke", "emerald2", "notEmerald"})
	public void test05_checkTemplateChangeVideoQualityBestQuality() throws InterruptedException {
		log.info("***** test05_checkTemplateChangeVideoQualityBestQuality *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "Best Quality", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(4));	
	}
	
	/**
	 * Check video_source_optimisation matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test10_checkTemplateChangeVideoSourceOff() throws InterruptedException {
		log.info("***** test10_checkTemplateChangeVideoSourceOff *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(0));	
	}
	
	/**
	 * Check video_source_optimisation matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test11_checkTemplateChangeVideoSourceDviOptimised() throws InterruptedException {
		log.info("***** test11_checkTemplateChangeVideoSourceDviOptimised *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "DVI/DP Optimised", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(1));	
	}
	
	
	/**
	 * Check video_source_optimisation matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test12_checkTemplateChangeVideoSourceVgaHighPerformance() throws InterruptedException {
		log.info("***** test12_checkTemplateChangeVideoSourceVgaHighPerformance *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - High Performance", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(2));	
	}
	
	/**
	 * Check video_source_optimisation matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test13_checkTemplateChangeVideoSourceVgaOptimised() throws InterruptedException {
		log.info("***** test13_checkTemplateChangeVideoSourceVgaOptimised *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Optimised", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(3));	
	}
	
	/**
	 * Check video_source_optimisation matches template properties after changing template
	 */
	//Already commented
	//@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test14_checkTemplateChangeVideoSourceVgaLowBandwidth() throws InterruptedException {
		log.info("***** test13_checkTemplateChangeVideoSourceVgaOptimised *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_source_optimisation", equalTo(4));	
	}
	
	/**
	 * Check hid matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test15_checkTemplateChangeHidDefault() throws InterruptedException {
		log.info("***** test15_checkTemplateChangeHidDefault *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Default", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(2));	
	}
	

	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test17_checkTemplateChangeHidAbsolute() throws InterruptedException {
		log.info("***** test17_checkTemplateChangeHidMac *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "Absolute", "Analog", "5", "1920x1200", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
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
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test24_checkMouseKeyboard() {
		log.info("***** test24_checkMouseKeyboard *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test25_checkMouseKeyboard0() throws InterruptedException {
		log.info("***** test25_checkMouseKeyboard0 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "0", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(0));	
	}
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test26_checkMouseKeyboard1() throws InterruptedException {
		log.info("***** test26_checkMouseKeyboard1 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "1", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(1));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test27_checkMouseKeyboard2() throws InterruptedException {
		log.info("***** test27_checkMouseKeyboard2 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "2", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(2));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test28_checkMouseKeyboard3() throws InterruptedException {
		log.info("***** test28_checkMouseKeyboard3 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "3", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(3));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test29_checkMouseKeyboard4() throws InterruptedException {
		log.info("***** test29_checkMouseKeyboard4 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "4", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(4));	
	}
	
	/**
	 * Check mouse_keyboard_timeout matches template properties after changing template
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test30_checkMouseKeyboard5() throws InterruptedException {
		log.info("***** test30_checkMouseKeyboard5 *****");
		deviceMethods.editTemplateTransmitter(driver, templateTx, "4", "VGA - Low Bandwidth", "MAC", "Analog", "5", "1024x768", "1920x1200");
		log.info("Sleeping while device reconfigures");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(5));	
	}
	
	
	/**
	 * Deletes template and checks system properties have been reapplied
	 * @throws InterruptedException 
	 */
	//removing test. As of 4.0 deleting a template
	//that is assigned to a device is invalid. Need to change
	//test
	//test is not valid
	//until we edit the template
	//if it is used by any device we can't delete the template
	
	@Test(groups= {"rest", "smoke", "emerald"})
	public void test31_deleteTemplateTX() throws InterruptedException {
		log.info("***** test31_deleteTemplateTX *****");
		deviceMethods.setTxToSystemProperties(driver, txIp);
		deviceMethods.deleteTemplate(driver, templateTx);
		log.info("Sleeping while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		checkSystemProperties(true, false);
	}
	
	/**
	 * Deletes template and checks system properties have been reapplied
	 * @throws InterruptedException 
	 */
	@Test(groups= {"rest", "emerald", "noSE"})
	public void test32_deleteTemplateRX() throws InterruptedException {
		log.info("***** test32_deleteTemplateRX *****");
		deviceMethods.setRxToSystemProperty(driver, rxIp);
		deviceMethods.deleteTemplate(driver, templateRx);
		log.info("Sleeping while device reboots..");
		Thread.sleep(70000);
		checkSystemProperties(false, true);
	}
	
	
	
	/**
	 * Checks the initial system properties after managing a device
	 * We need to be certain that these values are correct so that 
	 * when a template is applied, we know the values have changed
	 */
	private void checkSystemProperties(boolean checkTX, boolean checkRX) {
		log.info("Checking system properties are applied");
		
		
		if(checkTX) {
			log.info("video_quality...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then().assertThat().statusCode(200)
			.body("video_quality", equalTo(1));
			log.info("OK");
			
			log.info("video_source_optimisation...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then().assertThat().statusCode(200)
			.body("video_source_optimisation", equalTo(1));
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
			
			log.info("mouse_keyboard_timeout...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
			.then().assertThat().statusCode(200)
			.body("mouse_keyboard_timeout", equalTo(3));
			log.info("OK");
		}
		// Emerald-4K/SE/PE RX dose not support powermode
		if(checkRX) {
//		//rx
//			log.info("power mode...");
//			given().header(getHead())
//			.when()
//			.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
//			.then().assertThat().statusCode(200)
//			.body("power_mode", equalTo(1));
//			log.info("OK");
			
			//rx
			log.info("http enable...");
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp + getPort() + "/control/configuration/rx_settings")
			.then().assertThat().statusCode(200)
			.body("http_enable", equalTo(0));
			log.info("OK");	
		}
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
//				deviceMethods.setTxToSystemProperties(driver, txIp);
				deviceMethods.deleteTemplate(driver, templateTx);
				deviceMethods.deleteTemplate(driver, templateRx);
			}catch(Exception e1) {
				log.info("templates already deleted");
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
	
}
