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

public class SetKvmTransmitterProperties4k extends StartupTestCase {

	private AppliancePropertiesConfig config = new AppliancePropertiesConfig();
	private DevicesMethods deviceMethods = new DevicesMethods();
	final static Logger log = Logger.getLogger(SetKvmTransmitterProperties4k.class);
	private String dataFileSingleTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\txSettingsSingleSet4k.txt";
	
	
	@DataProvider(name="dataFileSingleTx")
	public Object[][] createTxSingleSettings() throws IOException {
		return readData(dataFileSingleTx);
	}

	
	public class TransmitterProperties {
		public String device_name;
	
		public String hid_configurations;
		public String mouse_keyboard_timeout;
		public String edid_settings_dvi1;
	
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test(dataProvider="dataFileSingleTx")
	public void test01_setTXProperties( String hid, String mouse, String edid1) throws InterruptedException {
		deviceMethods.navigateToDeviceStatus(driver);
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txEmerald.getDeviceName();
		
		prop.hid_configurations = hid;
		prop.mouse_keyboard_timeout = mouse;
		prop.edid_settings_dvi1 = edid1;
		
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
		.body(config.getDeviceHidConfigurations(0), equalTo(convertHid(hid)))
		.body(config.getDeviceEdidSettingsDvi1(0), equalTo(edid1))
		.body(config.getDeviceMouseKeyTimeout(0), equalTo(mouseTimeout)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	
	}
	
	
	
	@Test
	public void test03_invalidTransmitter() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = "invalidTx";
		
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
	
	
	@Test
	public void test08_invalidParamHid() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
	
		prop.hid_configurations = "-1";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "1920x1080p-60Hz";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"hid_configurations\"=>\"-1\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test09_invalidParamMouse() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "7";
		prop.edid_settings_dvi1 = "1920x1080p-60Hz";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"mouse_keyboard_timeout\"=>\"7\"} are invalid")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test10_invalidParamEdid1() {
		TransmitterProperties prop = new TransmitterProperties();
		prop.device_name = txSingle.getDeviceName();
		
		prop.hid_configurations = "0";
		prop.mouse_keyboard_timeout = "0";
		prop.edid_settings_dvi1 = "Clone";

	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/tx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"edid_settings_dvi1\"=>\"Clone\"} are invalid")).extract().response();
		
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
