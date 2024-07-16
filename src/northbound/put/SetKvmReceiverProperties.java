package northbound.put;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.containsString;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.DevicesMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.AppliancePropertiesConfig;
import northbound.get.config.AppliancePropertiesConfig.GetProperties;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneReceiversConfig;

public class SetKvmReceiverProperties extends StartupTestCase{

	private AppliancePropertiesConfig config = new AppliancePropertiesConfig();
	private DevicesMethods deviceMethods = new DevicesMethods();
	final static Logger log = Logger.getLogger(SetKvmReceiverProperties.class);
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
	}
	
	public class ReceiverProperties {
		public String device_name;
		public String http_enabled;
		public String zone;
	}
	
	@Test
	public void test01_setHttpEnabled() {
		
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "Enabled";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver settings have been updated successfully.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
		
		//check properties
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = rxEmerald.getDeviceName();
		
		log.info("Getting properties through REST");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceHttpEnabled(0), equalTo("Enabled")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}
	
	@Test
	public void test02_setHttpDisabled() {
		
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "Disabled";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver settings have been updated successfully.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
		
		//check properties
		AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
		getProp.device_names = new String[1];
		getProp.device_names[0] = rxEmerald.getDeviceName();
		
		log.info("Getting properties through REST");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getProp)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceHttpEnabled(0), equalTo("Disabled")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}
	
	@Test
	public void test03_invalidDeviceName() {
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = "InvalidDevice";
		prop.http_enabled = "Disabled";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Device " + prop.device_name + " does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test04_invalidHttpProperty() {
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "true";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(400)
		.body("message", containsString("Parameters {\"http_enabled\"=>\"true\"} are invalid or incompatible")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test05_editDeviceSystemProperties() throws InterruptedException {
		deviceMethods.setRxToSystemProperty(driver, rxIp);
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "Disabled";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " is with template/system properties."
				+ " Editing is enabled for unique properties only.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test06_editDeivceTemplateProperties() throws InterruptedException {
		String templateName = "test06Tem";
		deviceMethods.addTemplateReceiver(driver, templateName, true, true);
		deviceMethods.setRxToTemplateProperties(driver, rxIp, templateName);
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "Disabled";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " is with template/system properties."
				+ " Editing is enabled for unique properties only.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
	}
	
	
	@Test
	public void test07_assignReceiverToZone() throws InterruptedException {
		deviceMethods.setUniquePropertyRx(driver, rxEmerald.getIpAddress(), "HTTP Enabled", false);
		 CreateZoneConfig zoneConfig = new CreateZoneConfig();
		 String zoneName = "test07AssignReceiver";
		 zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		 
			ReceiverProperties prop = new ReceiverProperties();
			prop.device_name = rxEmerald.getDeviceName();
			prop.http_enabled = "Disabled";
			prop.zone = zoneName;
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(prop)
			.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Receiver settings have been updated successfully.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
			
			AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
			getProp.device_names = new String[1];
			getProp.device_names[0] = rxEmerald.getDeviceName();
			
			log.info("Get properties through REST and chekcing for zone");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getProp)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message.properties[0].zone", equalTo(zoneName)).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			
			EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
			editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
			
			log.info("Deleting all zones to clean up");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all zones.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
	}
	
	@Test
	public void test08_unassignReceiverFromZone() {
		 CreateZoneConfig zoneConfig = new CreateZoneConfig();
		 String zoneName = "test08UnassignReceiver";
		 zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		 
			ReceiverProperties prop = new ReceiverProperties();
			prop.device_name = rxEmerald.getDeviceName();
			prop.http_enabled = "Disabled";
			prop.zone = zoneName;
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(prop)
			.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Receiver settings have been updated successfully.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
			
			AppliancePropertiesConfig.GetProperties getProp = config.new GetProperties();
			getProp.device_names = new String[1];
			getProp.device_names[0] = rxEmerald.getDeviceName();
			
			log.info("Get properties through REST and chekcing for zone");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getProp)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message.properties[0].zone", equalTo(zoneName)).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			prop.device_name = rxEmerald.getDeviceName();
			prop.http_enabled = "Disabled";
			prop.zone = "";
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(prop)
			.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Receiver settings have been updated successfully.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
			
			log.info("Get properties through REST and chekcing for zone");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getProp)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message.properties[0].zone", equalTo(null)).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
			editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
			
			log.info("Deleting all zones to clean up");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all zones.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
			
			
	}
	
	@Test
	public void test09_invalidZone() {
		ReceiverProperties prop = new ReceiverProperties();
		prop.device_name = rxEmerald.getDeviceName();
		prop.http_enabled = "Disabled";
		prop.zone = "invalidZone";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(prop)
		.put("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Zone " + prop.zone + " does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/properties/rx", REQUEST_TYPE.PUT, response);
	}

}
