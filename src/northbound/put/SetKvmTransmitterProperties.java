package northbound.put;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.DevicesMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.AppliancePropertiesConfig;
import northbound.get.config.AppliancePropertiesConfig.GetProperties;

public class SetKvmTransmitterProperties extends StartupTestCase {

	private AppliancePropertiesConfig config = new AppliancePropertiesConfig();
	private DevicesMethods deviceMethods = new DevicesMethods();
	final static Logger log = Logger.getLogger(SetKvmTransmitterProperties.class);
	private String dataFileSingleTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\txSettingsSingleSet.txt";
	private String dataFileDualTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\txSettingsDualSet.txt";
	
	@DataProvider(name="dataFileSingleTx")
	public Object[][] createTxSingleSettings() throws IOException {
		return readData(dataFileSingleTx);
	}
	@DataProvider(name="dataFileDualTx")
	public Object[][] createTxDualSettings() throws IOException {
		return readData(dataFileDualTx);
	}
	
	public class TransmitterProperties {
		public String device_name;
		public String video_quality;
		public String video_source_optimization;
		public String hid_configurations;
		public String mouse_keyboard_timeout;
		public String edid_settings_dvi1;
		public String edid_settings_dvi2;
		public String audio_source;
		
		
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test(dataProvider="dataFileSingleTx")
	public void test01_setTXProperties(String video_quality, String video_source, String hid, String mouse, 
			String edid1) throws InterruptedException {
		deviceMethods.navigateToDeviceStatus(driver);
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txEmerald.getDeviceName();
		prop.video_quality = video_quality;
		prop.video_source_optimization = video_source;
		prop.hid_configurations = hid;
		prop.mouse_keyboard_timeout = mouse;
		prop.edid_settings_dvi1 = edid1;
//		prop.audio_source=audio_source;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Transmitter settings have been updated successfully.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
		
		config.checkDeviceState(txEmerald.getDeviceName(), boxillaManager, boxillaRestUser, boxillaRestPassword);
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = txEmerald.getDeviceName();
		Integer mouseTimeout = Integer.parseInt(mouse);		//convert mouseT to an int as thats what is returned
		
		log.info("Get properties through REST");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceVideoQuality(0), equalTo(video_quality))
		.body(config.getDeviceVideoSourceOptimization(0), equalTo(video_source))
		.body(config.getDeviceHidConfigurations(0), equalTo(convertHid(hid)))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo(edid1))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(mouseTimeout)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	
	}
	
	@Test(dataProvider="dataFileDualTx", groups = {"noZero"})
	public void test02_setTXDualProperties(String video_quality, String video_source, String hid, String mouse, 
			String edid1, String edid2) throws InterruptedException {
		
		deviceMethods.navigateToDeviceStatus(driver);
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txDual.getDeviceName();
		prop.video_quality = video_quality;
	//	prop.video_source_optimization = video_source; //uncomented for 6.3.0 firmware test
		prop.hid_configurations = hid;
		prop.mouse_keyboard_timeout = mouse;
		prop.edid_settings_dvi1 = edid1;
		prop.edid_settings_dvi2 = edid2;
//		prop.audio_source=audio_source;
		
		
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Transmitter settings have been updated successfully.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
		
		config.checkDeviceState(txDual.getDeviceName(), boxillaManager, boxillaRestUser, boxillaRestPassword);
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = txDual.getDeviceName();
		Integer mouseTimeout = Integer.parseInt(mouse);		//convert mouseT to an int as thats what is returned
		
		log.info("Get properties through REST");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(txDual.getDeviceName()))
		.body(config.getDeviceVideoQuality(0), equalTo(video_quality))
	//	.body(config.getDeviceVideoSourceOptimization(0), equalTo(videoOpt))
		.body(config.getDeviceHidConfigurations(0), equalTo(convertHid(hid)))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo(edid1))
		.body(config.getDeviceEdidSettingsDvi2(0), equalTo(edid2))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(mouseTimeout)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	
	}
	
	@Test
	public void test03_invalidTransmitter() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = "invalidTx";
		prop.video_quality = "4";
		prop.video_source_optimization = "Default";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";
		
		
	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Device " + prop.device_name + " does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test already comented
	public void test04_edit2SingleHead() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txEmerald.getDeviceName();
		prop.video_quality = "4";
		prop.video_source_optimization = "Off";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";
		prop.edid_settings_dvi2 = "1920x1080";
	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"edid_settings_dvi2\"=>\"1920x1080\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	// Need to uncomment from test 5 to 11 once the issue has been resolved 
	
//	@Test(groups = {"noZero"})
	public void test05_VideoSourceDualHead() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txDual.getDeviceName();
		prop.video_quality = "4";
		//prop.video_source_optimization = "Off";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";
		prop.edid_settings_dvi2 = "1920x1080";
		
	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"audio_source\"=>nil} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test
	public void test06_invalidParamVideoQuality() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		prop.video_quality = "6";
		prop.video_source_optimization = "Off";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"video_quality\"=>\"6\", \"audio_source\"=>nil} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test
	public void test07_invalidParamVideoSource() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		prop.video_quality = "4";
		prop.video_source_optimization = "Out";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"video_source_optimization\"=>\"Out\", \"audio_source\"=>nil} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test
	public void test08_invalidParamHid() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		prop.video_quality = "4";
		prop.video_source_optimization = "Off";
		prop.hid_configurations = "-1";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"audio_source\"=>nil, \"hid_configurations\"=>\"-1\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test
	public void test09_invalidParamMouse() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		prop.video_quality = "4";
		prop.video_source_optimization = "Off";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "7";
		prop.edid_settings_dvi1 = "1920x1080";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"audio_source\"=>nil, \"mouse_keyboard_timeout\"=>\"7\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test
	public void test10_invalidParamEdid1() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		prop.video_quality = "4";
		prop.video_source_optimization = "Off";
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "Close";
		

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"audio_source\"=>nil, \"edid_settings_dvi1\"=>\"Close\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
//	@Test(groups = {"noZero"})
	public void test11_invalidParamEdid2() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txDual.getDeviceName();
		prop.video_quality = "4";
		
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080";
		prop.edid_settings_dvi2 = "Close";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"audio_source\"=>nil, \"edid_settings_dvi2\"=>\"Close\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
	private String convertHid(String hid) {
		String realHid = "";
		
		switch(hid) {
		case "0" :
			realHid = "Default";
			break;
		case "1" :
			realHid = "Basic";
			break;
		case "2" :
			realHid = "MAC";
			break;
		case "3" :
			realHid = "Absolute";
			break;
		case "4" :
			realHid = "Absolute MAC";
			break;
		case "5" :
			realHid = "Dual Mouse";
			break;
		}
		return realHid;
	}
	
}
