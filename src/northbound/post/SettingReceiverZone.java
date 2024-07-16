package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneReceiversConfig;
import northbound.put.config.EditZoneDetailsConfig;

public class SettingReceiverZone extends StartupTestCase {

	public class SetReceiver {
		public String device_name;
		public String zone;
	}
	
	public String getUri(String boxillaManager) {
		 return "https://" + boxillaManager  + "/bxa-api/devices/kvm/rx/zones";
	}
	
	final static Logger log = Logger.getLogger(EditZoneReceivers.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	private EditZoneDetailsConfig editZoneConfig = new EditZoneDetailsConfig();
	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
		
		
		log.info("Deleting all zones before starting testcases");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);


	}
	
	@Test
	public void test01_assignDeviceToZone() throws InterruptedException {
		String zoneName = "test01AssingDevice";
		String zoneDescription = "this is a zone";
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		SetReceiver set = new SetReceiver();
		set.device_name = rxEmerald.getDeviceName();
		set.zone = zoneName;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " has been successfully reassigned to zone " + zoneName + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, rxEmerald.getDeviceName(), driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to the zone");
		
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
	public void test02_unassignDeviceFromZone() throws InterruptedException {
		String zoneName = "test02UnassingDevice";
		String zoneDescription = "this is a zone";
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		SetReceiver set = new SetReceiver();
		set.device_name = rxEmerald.getDeviceName();
		set.zone = zoneName;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " has been successfully reassigned to zone " + zoneName + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, rxEmerald.getDeviceName(), driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to the zone");
		
		set.zone = "";
		response =given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " has been successfully unassigned from zone " + zoneName + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, rxEmerald.getDeviceName(), driver);
		 Assert.assertFalse(isAssigned, "Device was assigned to the zone");
		 
		 editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
			log.info("Deleting all zones to clean up");
			response =given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all zones.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
		
	}
	
	@Test
	public void test03_assignDeviceInvalid() {
		String zoneName = "test03DeviceInvalid";
		String zoneDescription = "this is a zone";
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		SetReceiver set = new SetReceiver();
		set.device_name = "invalid Device";
		set.zone = zoneName;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Receiver " + set.device_name + " does not exist.")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		log.info("Deleting all zones to clean up");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
		
	}
	
	@Test
	public void test04_assignDeviceInvalidZone() {
		SetReceiver set = new SetReceiver();
		set.device_name = rxEmerald.getDeviceName();
		set.zone = "invalidZone";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Zone " + set.zone + " does not exist.")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test05_assignDeviceSwapZone() throws InterruptedException {
		String zoneName1 = "test05SwaoZone1";
		String zoneName2 = "test05SwaoZone2";
		String zoneDescription = "this is a zone";
		config.createZone(zoneName1, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		config.createZone(zoneName2, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		SetReceiver set = new SetReceiver();
		set.device_name = rxEmerald.getDeviceName();
		set.zone = zoneName1;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " has been successfully reassigned to zone " + zoneName1 + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName1, rxEmerald.getDeviceName(), driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to the zone");
		
		log.info("Run Set Receiver zone again with second zone and check assignment");
		
		set.zone = zoneName2;
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(set)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Receiver " + rxEmerald.getDeviceName() + " has been successfully reassigned to zone " + zoneName2 + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName1, rxEmerald.getDeviceName(), driver);
		Assert.assertFalse(isAssigned, "Device was still assigned to the zone");
		
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName2, rxEmerald.getDeviceName(), driver);
			Assert.assertTrue(isAssigned, "Device was still assigned to the zone");
			
			 editReceiver.removeAllReceivers(zoneName1, boxillaManager, boxillaRestUser, boxillaRestPassword);
			 editReceiver.removeAllReceivers(zoneName2, boxillaManager, boxillaRestUser, boxillaRestPassword);
				
				log.info("Deleting all zones to clean up");
				response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
				.then().assertThat().statusCode(200)
				.body("message", equalTo("Successfully deleted all zones.")).extract().response();
				
				SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
				
	
	}
	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) throws InterruptedException {
		log.info("Deleting all connections");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
		
		log.info("Deleting all zones before starting testcases");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);

		super.login(browser, method);
	}
	
}
