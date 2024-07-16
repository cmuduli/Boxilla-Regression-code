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
public class SystemProperties4k extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(SystemProperties4k.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	AppliancePool devicePool = new AppliancePool();
	Device txSingle, rxSingle;
	

	
	//system properties
	String videoQuality = "2";				///return from rest : 1
	String videoSource = "DVI Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "Default";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private int timeout = 120000;
	
	
	
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
			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
					audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
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
	
	


	/**
	 * Check that head_1_edid matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald2", "notEmerald"})
	public void test23_changeEdidDvi1_3840x2160p60Hz() throws InterruptedException {
		log.info("***** test23_changeEdidDvi1_3840x2160p60Hz *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "3840x2160p-60Hz");
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
	public void test24_changeEdidDvi1_3840x2160p30Hz() throws InterruptedException {
		log.info("***** test24_changeEdidDvi1_3840x2160p30Hz *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "3840x2160p-30Hz");
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
	public void test25_changeEdidDvi1_2560x1440p60Hz() throws InterruptedException {
		log.info("***** test25_changeEdidDvi1_2560x1440p60Hz *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "2560x1440p-60Hz");
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
	public void test26_changeEdidDvi1_1920x1080p60Hz() throws InterruptedException {
		log.info("***** test25_changeEdidDvi1_1920x1080p60Hz *****");
		deviceMethods.setSingleSystemPropertyTransmitter(driver, "EDID DVI1", "1920x1080p-60Hz");
		log.info("Sleeping for 90 seconds while device reboots..");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		given().header(getHead())
		.when()
		.get(http + "://" + txIp + port + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(4));
	}
	
	
	/**
	 * Check that http_enable matches system properties after changing 
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "emerald"})
	public void test35_changeHttpEnabledDisabled() throws InterruptedException {
		log.info("***** test35_changeHttpEnabledDisabled *****");
		deviceMethods.setSingleSystemPropertyReceiver(driver, "HTTP Enabled", false);
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(60000);
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
		log.info("Sleeping for 90 seconds while device reboots..");
		Thread.sleep(60000);
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
	

	
}
