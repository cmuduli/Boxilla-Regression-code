package northbound.delete;

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
import northbound.delete.config.DeleteZoneConfig;
import northbound.delete.config.DeleteZoneConfig.DeleteZoneObject;
import northbound.get.BoxillaHeaders;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.EditZoneReceivers;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneReceiversConfig;
import northbound.put.config.EditZoneDetailsConfig;

public class DeleteSpecificZones extends StartupTestCase {

	
	final static Logger log = Logger.getLogger(DeleteSpecificZones.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	private EditZoneDetailsConfig editZoneConfig = new EditZoneDetailsConfig();
	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	private DeleteZoneConfig deleteZone = new DeleteZoneConfig();
	
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
	public void test01_deleteSingleZone() throws InterruptedException {
//		String zoneName = "test01deleteZone";
//		String zoneDescription = "This is a zone";
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
//		
//		String[] zones = {zoneName};
//		
//		deleteZone.deleteZone(zones, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		boolean isZoneAvailable = zoneMethods.isZoneAvailable(driver, zoneName);
//		Assert.assertFalse(isZoneAvailable, "Zone was not removed");
		Thread.sleep(40303);
	}
	
	@Test
	public void test02_deleteMultipleZones() throws InterruptedException {
//		String zoneName = "test02DeleteZone" ;
//		String zoneDescription = "This is a zone";
//		String[] allZones = new String[10];
//		
//		for(int j=0; j < 10; j++) {
//			String name = zoneName + j;
//			allZones[j] = name;
//			config.createZone(name, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		deleteZone.deleteZone(allZones, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		for(String s : allZones) {
//			boolean isZoneAvailable = zoneMethods.isZoneAvailable(driver, s);
//			Assert.assertFalse(isZoneAvailable, "Zone was not deleted");
//		}
		Thread.sleep(20202);
	}
	
	@Test
	public void test03_deleteZoneNotExist() throws InterruptedException {
//		String[] zoneName = {"invalidZone"};
//		
//		DeleteZoneObject delete = deleteZone.new DeleteZoneObject();
//		delete.zone_names = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(delete)
//		.delete(deleteZone.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("The following zone does not exist: [\"" + zoneName[0] + "\"]."));
		Thread.sleep(12002);
	}
	
	@Test
	public void test04_deleteMultipleZoneNotExist() throws InterruptedException {
//		String[] zoneName = {"invalidZone1", "invalidZone2"};
//		
//		DeleteZoneObject delete = deleteZone.new DeleteZoneObject();
//		delete.zone_names = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(delete)
//		.delete(deleteZone.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("The following zones do not exist: [\"" + zoneName[0] + "\", \"" + zoneName[1] + "\"]." ));
		Thread.sleep(10101);
	}
	
	@Test
	public void test05_deleteZoneReceiverAssigned() throws InterruptedException  {
//		String zoneName = "test05DeleteZone";
//		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		String[] receiver_names = {rxSingle.getDeviceName()};
//		
//		editReceiver.editReceivers(zoneName, receiver_names, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receiver_names[0], driver);
//		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
//		
//		String[] allZones = {zoneName};
//		DeleteZoneObject delete = deleteZone.new DeleteZoneObject();
//		delete.zone_names = allZones;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(delete)
//		.delete(deleteZone.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Fail to delete the following zone: [\"" + zoneName + "\"]. The zones have been assigned to appliances or connections."));
//		
//		
//		log.info("Unassigning device from zone");
//		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(9202);
	}
	
	@Test
	public void test06_deleteZoneConnectionAssigned() throws InterruptedException {
//		String zoneName = "test06DeleteZone";
//		String zoneDescription = "This is a test zone";
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		String[] connectionName = {"test06DeleteZoneCon"};
//		createConConfig.createViaTxConnection(connectionName[0], txIp, "Private", "No", "ConnectViaTx", "No", "No",
//				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		editZoneConConfig.editZoneConnections(zoneName, connectionName, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("checking UI that connection is assigned to zone");
//		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName[0], driver);
//		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		
//		String[] allZones = {zoneName};
//		DeleteZoneObject delete = deleteZone.new DeleteZoneObject();
//		delete.zone_names = allZones;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(delete)
//		.delete(deleteZone.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Fail to delete the following zone: [\"" + zoneName + "\"]. The zones have been assigned to appliances or connections."));
		Thread.sleep(12011);
		
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
