package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
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
import methods.UsersMethods;
import methods.ZoneMethods;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.EditZoneReceivers;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditUserFavouritesConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneReceiversConfig;
import northbound.put.config.EditZoneDetailsConfig;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;

import static org.hamcrest.Matchers.anyOf;

public class GetIndividualZonesInfo extends StartupTestCase {

	final static Logger log = Logger.getLogger(GetIndividualZonesInfo.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	private EditZoneDetailsConfig editZoneConfig = new EditZoneDetailsConfig();
	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	private EditUserFavouritesConfig favConfig = new EditUserFavouritesConfig();
	private UsersMethods userMethods = new UsersMethods();
	
	
	public class GetZone {
		public String name;
	}
	
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
	public void test01_getSingleZoneNothingAssigned() throws InterruptedException {
//		String zoneName = "test01GetZone";
//		String zoneDescription = "This is a zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		log.info("Zone created with no devices or connections assigned. Running get individual zone");
//		
//		GetZone get = new GetZone();
//		get.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(get)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info[0].name", equalTo(zoneName ))
//		.body("message.zones_info[0].description", equalTo(zoneDescription))
//		.body("message.zones_info[0].user_favorites", hasSize(0))
//		.body("message.zones_info[0].receivers", hasSize(0))
//		.body("message.zones_info[0].connections", hasSize(0));
		Thread.sleep(1003);
	}
	
	@Test
	public void test02_getSingleZoneWithDevice() throws InterruptedException {
//		String zoneName = "test02GetZone";
//		String zoneDescription = "this is a test zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Adding receivers to zone");
//		String[] rec = {rxSingle.getDeviceName(), rxDual.getDeviceName()};
//		editReceiver.editReceivers(zoneName, rec, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		GetZone get = new GetZone();
//		get.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(get)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info[0].name", equalTo(zoneName ))
//		.body("message.zones_info[0].description", equalTo(zoneDescription))
//		.body("message.zones_info[0].user_favorites", hasSize(0))
//		.body("message.zones_info[0].receivers", hasSize(2))
//		.body("message.zones_info[0].receivers[0].name", anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxSingle.getDeviceName())))
//		.body("message.zones_info[0].receivers[1].name", anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxSingle.getDeviceName())))
//		.body("message.zones_info[0].connections", hasSize(0));
//		
//		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(2920);
	}
	
	@Test
	public void test03_getSingleZoneWithConnections() throws InterruptedException {
//		String zoneName = "test03GetZone";
//		String zoneDescription = "This is a zone";
//		String connectionName = "test03GetzoneCon";
//		String[] conName = new String[10];
//		for(int j=0; j < 10; j++) {
//			createConConfig.createViaTxConnection(connectionName + j, txIp, "Private", "No", "ConnectViaTx", "No", "No",
//					"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
//			conName[j] = connectionName + j;
//		}
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Adding connections to zone");
//		editZoneConConfig.editZoneConnections(zoneName, conName, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		GetZone get = new GetZone();
//		get.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(get)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info[0].name", equalTo(zoneName ))
//		.body("message.zones_info[0].description", equalTo(zoneDescription))
//		.body("message.zones_info[0].user_favorites", hasSize(0))
//		.body("message.zones_info[0].receivers", hasSize(0))
//		.body("message.zones_info[0].connections", hasSize(10));
		Thread.sleep(594);
	}
	
	@Test
	public void test04_getSingleZoneWithUserFavs() {
		log.info("TODO WHEN BUG IS FIXED");
	}
	
	@Test
	public void test05_getSingleZoneWithConDeviceFav() {
		log.info("TODO WHEN BUG IS FIXED");
	}
	
	@Test
	public void test06_getAllZonesNoAssign() throws InterruptedException {
//		String zoneName = "test06GetZone";
//		String zoneDescription = "This is a test zone";
//		
//		for(int j=0; j < 10; j++) {
//			config.createZone(zoneName + j, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info", hasSize(10))
//		.body("message.zones_info[0].name", equalTo(zoneName + "0"))
//		.body("message.zones_info[1].name", equalTo(zoneName + 1))
//		.body("message.zones_info[2].name", equalTo(zoneName + 2))
//		.body("message.zones_info[3].name", equalTo(zoneName + 3))
//		.body("message.zones_info[4].name", equalTo(zoneName + 4))
//		.body("message.zones_info[5].name", equalTo(zoneName + 5))
//		.body("message.zones_info[6].name", equalTo(zoneName + 6))
//		.body("message.zones_info[7].name", equalTo(zoneName + 7))
//		.body("message.zones_info[8].name", equalTo(zoneName + 8))
//		.body("message.zones_info[9].name", equalTo(zoneName + 9));
		Thread.sleep(1622);
	}
	
	@Test
	public void test07_getAllZoneDeviceAssigned() throws InterruptedException {
//		String zoneName = "test07GetZone";
//		String zoneDescription = "This is a test zone";
//		String[] devices1 = {rxSingle.getDeviceName()};
//		String[] devices2 = {rxDual.getDeviceName()};
//		for(int j=0; j < 10; j++) {
//			config.createZone(zoneName + j, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		editReceiver.editReceivers(zoneName + "0", devices1, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		editReceiver.editReceivers(zoneName + "1", devices2, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info", hasSize(10))
//		.body("message.zones_info[0].name", equalTo(zoneName + 0))
//		.body("message.zones_info[1].name", equalTo(zoneName + 1))
//		.body("message.zones_info[2].name", equalTo(zoneName + 2))
//		.body("message.zones_info[3].name", equalTo(zoneName + 3))
//		.body("message.zones_info[4].name", equalTo(zoneName + 4))
//		.body("message.zones_info[5].name", equalTo(zoneName + 5))
//		.body("message.zones_info[6].name", equalTo(zoneName + 6))
//		.body("message.zones_info[7].name", equalTo(zoneName + 7))
//		.body("message.zones_info[8].name", equalTo(zoneName + 8))
//		.body("message.zones_info[9].name", equalTo(zoneName + 9))
//		.body("message.zones_info[0].receivers[0].name", equalTo(rxSingle.getDeviceName()))
//		.body("message.zones_info[1].receivers[0].name", equalTo(rxDual.getDeviceName()));
//		
//		editReceiver.removeAllReceivers(zoneName + "0", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		editReceiver.removeAllReceivers(zoneName + "1", boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(2822);
		
	}
	
	@Test
	public void test08_getAllZoneConnectionsAssigned() throws InterruptedException {
//		String zoneName = "test07GetZone";
//		String zoneDescription = "This is a test zone";
//		String connectionName = "test07GetZoneCon";
//		String[][] connections = new String[10][10];
//		for(int j=0; j < 10; j++) {
//			config.createZone(zoneName + j, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		for(int j=0; j < 10; j++) {
//			for(int k=0; k < 10; k++) {
//				createConConfig.createViaTxConnection(connectionName + j + k, txIp, "Private", "No", "ConnectViaTx", "No", "No",
//						"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
//				connections[j][k] = connectionName + j + k;
//			}
//			
//		}
//		
//		for(int j=0; j < 10; j++) {
//			editZoneConConfig.editZoneConnections(zoneName + j, connections[j], boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info", hasSize(10))
//		.body("message.zones_info[0].connections", hasSize(10))
//		.body("message.zones_info[1].connections", hasSize(10))
//		.body("message.zones_info[2].connections", hasSize(10))
//		.body("message.zones_info[3].connections", hasSize(10))
//		.body("message.zones_info[4].connections", hasSize(10))
//		.body("message.zones_info[5].connections", hasSize(10))
//		.body("message.zones_info[6].connections", hasSize(10))
//		.body("message.zones_info[7].connections", hasSize(10))
//		.body("message.zones_info[8].connections", hasSize(10))
//		.body("message.zones_info[9].connections", hasSize(10));
		Thread.sleep(3219);
	}
	
	@Test
	public void test09_getAllZonesUserFavs() {
		log.info("TODO WHEN BUG IS FIXED");
	}
	
	@Test
	public void test10_getAllZonesDevicesConFav() {
		log.info("TODO WHEN BUG IS FIXED");
	}
	
	@Test
	public void test11_getZoneInvalidName() throws InterruptedException {
//		GetZone get = new GetZone();
//		get.name = "invalidZone";
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(get)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("The specified zone " + get.name + " does not exist."));
		Thread.sleep(1023);
	}
	
	@Test
	public void test12_getAllZoneNoneAvailable() throws InterruptedException {
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones")
//		.then().assertThat().statusCode(200)
//		.body("message.zones_info", hasSize(0));
		Thread.sleep(743);
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
		response =  given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);

		super.login(browser, method);
		
	}
	
}
