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
 * This class will test swapping between the device property types.
 * It will switch between system, unique and template properties 
 * and assert that the values have changed
 * @author Brendan O'Regan
 *
 */

public class ChangePropertySource extends StartupTestCase {
	
	
	final static Logger log = Logger.getLogger(ChangePropertySource.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	AppliancePool devicePool = new AppliancePool();
	Device txSingle, rxSingle;
	
	//test properties
	
	
	//system properties
	String videoQuality = "2";				///return from rest : 1
	String videoSource = "DVI Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private int timeout = 140000;
	

	/**
	 * Checks the initial system properties after managing a device
	 * We need to be certain that these values are correct so that 
	 * when a template is applied, we know the values have changed
	 */
	private void checkSystemProperties(int videoQ, int videoS, int hid, int edid1, int mouseKeyboard) {
		log.info("Checking system properties are applied");
		
		log.info("video_quality...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("video_quality", equalTo(videoQ));
		log.info("OK");
		

//		log.info("video_source_optimisation...");
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
//		.then().assertThat().statusCode(200)
//		.body("video_source_optimisation", equalTo(videoS));
//		log.info("OK");

		
		log.info("hid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("hid", equalTo(hid));
		log.info("OK");
		
		log.info("head_1_edid...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("head_1_edid", equalTo(edid1));
		log.info("OK");
		
		log.info("mouse_keyboard_timeout...");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/configuration/tx_settings")
		.then().assertThat().statusCode(200)
		.body("mouse_keyboard_timeout", equalTo(mouseKeyboard));
		log.info("OK");
		
		
	}
	
	
	/**
	 * Sets system properties, manages devices and checks the system properties 
	 * are correct. Also creates a template to be used later in the tests
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
			deviceMethods.setTxToSystemProperties(driver, txIp);
			deviceMethods.setRxToSystemProperty(driver, rxIp);
			log.info("Sleeping while waiting for device to reboot...");
			deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
			Thread.sleep(60000);
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			deviceMethods.addTemplateTransmitter(driver, "TEST_TX", "Best Quality",
					"Off", "Default", "Analog", "5", "1920x1200", "1920x1200");
			// checkSystemProperties(1, 1, 1, 0, 3);

			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	/**
	 * Swap from system to template properties
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "smoke", "notEmerald", "chrome"})
	public void test01_SystemToTemplate() throws InterruptedException {
		log.info("***** test01_SystemToTemplate *****");
		deviceMethods.setTxToTemplateProperties(driver, txIp, "TEST_TX");
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(0, 0, 0, 1, 5);
	}
	
	
	/**
	 * Swap from template to unique properties
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test02_TemplateToUnique() throws InterruptedException {
		log.info("***** test02_TemplateToUnique *****");
		setAllUniqueProperties("4", "Off", "Basic", "1024x768" );
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(3, 0, 1, 4, 5);
	}
	
	/**
	 * Swap from unique to system
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test03_UniqueToSystem() throws InterruptedException {
		log.info("***** test03_UniqueToSystem *****");
		deviceMethods.setTxToSystemProperties(driver, txIp);
		log.info("Sleeping while waiting for device to reboot...");
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(1, 1, 1, 0, 3);	
	}
	
	/**
	 * Swap from system to unique
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test04_SystemToUnique() throws InterruptedException {
		log.info("***** test04_SystemToUnique *****");
		setAllUniqueProperties("4", "Off", "Basic", "1024x768" );
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(3, 0, 1, 4, 3);
	}
	
	/**
	 * Swap from unique to template
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test05_UniqueToTemplate() throws InterruptedException {
		log.info("***** test05_UniqueToTemplate *****");
		deviceMethods.setTxToTemplateProperties(driver, txIp, "TEST_TX");
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(0, 0, 0, 1, 5);
	}
	
	/**
	 * Swap from template to system
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "notEmerald"})
	public void test06_TemplateToSystem() throws InterruptedException {
		log.info("***** test06_TemplateToSystem *****");
		deviceMethods.setTxToSystemProperties(driver, txIp);
		log.info("Sleeping while waiting for device to reboot...");
		deviceMethods.checkReboot(txIp, deviceUserName, devicePassword);
		Thread.sleep(60000);
		checkSystemProperties(1, 1, 1, 0, 3);
		
	}
//	
//	/**
//	 * Gives devices IP addresses and manages them
//	 * @throws InterruptedException
//	 */
//	public void deviceManageTestPrep() throws InterruptedException {
//		log.info("Test Preparation Manage Device");
//		
//		//RX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"), rxIp , rxSingle.getGateway(), rxSingle.getNetmask());
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", rxSingle.getMac(), prop.getProperty("ipCheck"));
//		
//		//TX
//		discoveryMethods.discoverDevices(driver);
//		discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"), txIp, txSingle.getGateway(), txSingle.getNetmask());
//		discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", txSingle.getMac(), prop.getProperty("ipCheck"));
//		
//		log.info("Appliance Managed Successfully - Test Preparation Completed");
//	}
	
	/**
	 * Sets all unique properties
	 * @param videoQ
	 * @param videoS
	 * @param hid
	 * @param edid1
	 * @throws InterruptedException
	 */
	private void setAllUniqueProperties(String videoQ, String videoS, String hid, String edid1) throws InterruptedException {
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", videoQ);
		//Thread.sleep(60000);
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Source", videoS);
		//Thread.sleep(60000);
		deviceMethods.setUniquePropertyTx(driver, txIp, "HID", hid);
		//Thread.sleep(60000);
		deviceMethods.setUniquePropertyTx(driver, txIp, "EDID1", edid1);
		Thread.sleep(70000);
	}

}
