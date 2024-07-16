package northbound.get;

import static io.restassured.RestAssured.given;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import io.restassured.matcher.*;
import io.restassured.response.Response;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import methods.DevicesMethods;
import northbound.get.config.AppliancePropertiesConfig;
import northbound.get.config.AppliancePropertiesConfig.GetProperties;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneReceiversConfig;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

public class ApplianceProperties extends StartupTestCase{

	private AppliancePropertiesConfig config = new AppliancePropertiesConfig();
	
	private String dataFileSingleTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\txSettingsSingle.txt";
	private String dataFileDualTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\txSettingsDual.txt";
	private DevicesMethods deviceMethods = new DevicesMethods();
	final static Logger log = Logger.getLogger(ApplianceProperties.class);
	String videoQuality = "4";				///return from rest : 1
	String videoSource = "DVI/DP Optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
//	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = false;
	String audio = "DisplayPort Audio";
	String audio1="Analog Audio";
	
	@DataProvider(name="dataFileSingleTx")
	public Object[][] createTxSingleSettings() throws IOException {
		return readData(dataFileSingleTx);
	}
	@DataProvider(name="dataFileDualTx")
	public Object[][] createTxDualSettings() throws IOException {
		return readData(dataFileDualTx);
	}

	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices(); 
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		try {
			cleanUpLogin();
//			deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig, audio, 
//					mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
			if(!StartupTestCase.isZeroU) {
				deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
						audio1, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}else {
					deviceMethods.setSystemProperties(driver, videoQuality, videoSource, hidConfig,
							audio, mouseTimeout, edidDvi1, edidDvi2, isManual, isHttpEnabled);
				}
			
			deviceMethods.bulkUpdate(driver, "Transmitter", "System Properties", new String[]{txEmerald.getDeviceName(), txDual.getDeviceName()});
			deviceMethods.timer(driver);
			deviceMethods.bulkUpdate(driver, "Receiver", "System Properties", new String[]{rxEmerald.getDeviceName(), rxDual.getDeviceName()});
			deviceMethods.checkDeviceOnline(driver, txIp);

			cleanUpLogout();
		}catch(Exception e) {
			cleanUpLogout();
		}
	}
	 
	@Test(dataProvider="dataFileSingleTx")
	
	public void test01_setSingleHeadTx(String videoQ, String videoOpt, String hid, String mouseT, String edid1) throws InterruptedException {
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		log.info("Check transmitter system properties");
//		deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid, "", mouseT, edid1, edid1, false, false);
		if (!StartupTestCase.isZeroU) {
			
		deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid,audio1, mouseT, edid1, edid1, false, false);
		}else {
			deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid,audio, mouseT, edid1, edid1, false, false);
		}
		// this will check the device is configured and online 
		deviceMethods.checkDeviceOnline(driver, txIp);
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = txEmerald.getDeviceName();
		Integer mouseTimeout = Integer.parseInt(mouseT);		//convert mouseT to an int as thats what is returned
		
		log.info("Get properties through REST");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceVideoQuality(0), equalTo(videoQ))
		//.body(config.getDeviceVideoSourceOptimization(0), equalTo(videoOpt))
		.body(config.getDeviceHidConfigurations(0), equalTo(hid))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo(edid1))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(mouseTimeout)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test(dataProvider="dataFileDualTx", groups = {"noZero"})
	public void test02_setDualHeadTx(String videoQ, String videoOpt, String hid, String mouseT, String edid1, String edid2) throws InterruptedException {
		log.info("Set dual head TX system properties");
//		deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid, "", mouseT, edid1, edid2, false, false);
		if (!StartupTestCase.isZeroU) {
			
			deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid,audio1, mouseT, edid1, edid2, false, false);
			}else {
				deviceMethods.setSystemProperties(driver, videoQ, videoOpt, hid,audio, mouseT, edid1, edid2, false, false);
			}
		deviceMethods.checkDeviceOnline(driver, txIpDual);

		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = txDual.getDeviceName();
		Integer mouseTimeout = Integer.parseInt(mouseT);		//convert mouseT to an int as thats what is returned
		
		log.info("Get properties through REST");
		Response response =given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(txDual.getDeviceName()))
		.body(config.getDeviceVideoQuality(0), equalTo(videoQ))
		.body(config.getDeviceHidConfigurations(0), equalTo(hid))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo(edid1))
		.body(config.getDeviceEdidSettingsDvi2(0), equalTo(edid2))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(mouseTimeout)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test03_setHttpEnabled() throws InterruptedException {
		log.info("Set RX system properties");
//		deviceMethods.setSystemProperties(driver, "2", "Off", "Basic", "", "0", "1920x1080", "1920x1080", true, true);
		if (!StartupTestCase.isZeroU) {
			
			deviceMethods.setSystemProperties(driver, "2", "Off", "Basic",audio1, "0", "1920x1080", "1920x1080", true, true);
			}else {
				deviceMethods.setSystemProperties(driver, "2", "Off", "Basic",audio, "0", "1920x1080", "1920x1080", true, true);
			}
		deviceMethods.checkDeviceOnline(driver, rxIp);

		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = rxEmerald.getDeviceName();
		
		log.info("Getting properties through REST");
		Response response =given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceHttpEnabled(0), equalTo("Enabled")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
//	@Test
	public void test04_setHttpDisabled() throws InterruptedException {
		log.info("Setting RX system properties");
		deviceMethods.setSystemProperties(driver, "2", "Off", "Basic", "", "0", "1920x1080", "1920x1080", true, false);
		
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		
		log.info("Getting properties through REST");
		getProp.device_names[0] = rxEmerald.getDeviceName();
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceHttpEnabled(0), equalTo("Disabled")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	
	@Test
	public void test05_checkDeviceStatusTX() throws InterruptedException {
		if(!StartupTestCase.isZeroU) {
		deviceMethods.setSystemProperties(driver, "4", "DVI/DP Optimised", "Basic",audio1, "0", "1920x1080", "1920x1080", true, false);
		}else {
			deviceMethods.setSystemProperties(driver, "4", "DVI/DP Optimised", "Basic",audio, "0", "1920x1080", "1920x1080", true, false);

		}
		config.checkDeviceState(txEmerald.getDeviceName(), boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test06_checkDeviceStatusRX() throws InterruptedException {
//		deviceMethods.setSystemProperties(driver, "4", "DVI/DP Optimised", "Basic", "", "0", "1920x1080", "1920x1080", true, true);
		if (!StartupTestCase.isZeroU) {
			deviceMethods.setSystemProperties(driver, "4", "DVI/DP Optimised", "Basic",audio1, "0", "1920x1080", "1920x1080", true, true);
		}else {
			deviceMethods.setSystemProperties(driver, "4", "DVI/DP Optimised", "Basic",audio, "0", "1920x1080", "1920x1080", true, true);

		}
		config.checkDeviceState(rxEmerald.getDeviceName(), boxillaManager,  boxillaRestUser, boxillaRestPassword);
	}
	
//	@Test
	public void test07_getMultipleDeviceProperties() throws InterruptedException {
		log.info("Setting system properties");
		deviceMethods.setSystemProperties(driver, "2", "DVI/DP Optimised", "Basic", "", "0", "1920x1080", "1920x1080", false, false);
		
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[2];
		getProp.device_names[0] = txEmerald.getDeviceName();
		getProp.device_names[1] = rxEmerald.getDeviceName();
		
		log.info("Getting multiple device properties through REST");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceVideoQuality(0), equalTo("2"))
		//.body(config.getDeviceVideoSourceOptimization(0), equalTo( "DVI Optimised"))
		.body(config.getDeviceHidConfigurations(0), equalTo("Basic"))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo("1920x1080"))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(0))
		
		.body(config.getDeviceName(1), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceHttpEnabled(1), equalTo("Disabled")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);

	}
	
	@Test(groups = {"noZero"})
	public void test08_getApplianceInZone() {
		 CreateZoneConfig zoneConfig = new CreateZoneConfig();
		 EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
		 String zoneName = "test08GetAppliance";
		 zoneConfig.createZone(zoneName, "This is a zone" , boxillaManager, boxillaRestUser, boxillaRestPassword);
		 String[] receivers = {rxEmerald.getDeviceName()};
		 editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		 
		 
		 AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
			getProp.device_names = new String[1];
			getProp.device_names[0] = rxEmerald.getDeviceName();
			
			log.info("Get properties through REST");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getProp)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message.properties[0].zone", equalTo(zoneName)).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
			log.info("Deleting all zones to clean up");
		    response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		    
		    SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		 
		 
		  
	}
	
}
