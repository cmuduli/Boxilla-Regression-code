package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
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
import northbound.post.config.EditZoneReceiversConfig.EditZoneReceiversObject;
import northbound.put.EditZoneDetails;
import northbound.put.config.EditZoneDetailsConfig;

public class EditZoneReceivers extends StartupTestCase {

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
	public void test01_addSingleReceiverToZone() throws InterruptedException {
		String zoneName = "test01addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {rxSingle.getDeviceName()};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		log.info("Unassigning device from zone");
		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(640);

	}
	
	@Test
	public void test02_addMultipleReceiversToZone() throws InterruptedException {
		String zoneName = "test02addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName()};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[1], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		log.info("Unassigning device from zone");
		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(392);
	}
	
	@Test
	public void test03_removeReceiverFromZone() throws InterruptedException {
		String zoneName = "test03addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {rxSingle.getDeviceName()};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		log.info("Unassigning device from zone");
		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		Thread.sleep(795);
	}
	
	@Test
	public void test04_removeMultipleReceiversFromZone() throws InterruptedException {
		String zoneName = "test04addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName()};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[1], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		log.info("Unassigning device from zone");
		editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		
		isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName, receivers[1], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		Thread.sleep(630);
	}
	
	@Test
	public void test04_addTransmitterDevice() throws InterruptedException {
		String zoneName = "test05addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {txSingle.getDeviceName()};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditZoneReceiversObject edit = editReceiver.new EditZoneReceiversObject();
		edit.zone_name = zoneName;
		edit.receiver_names = receivers;
		
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.post(editReceiver.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following receiver does not exist: [\"" + txSingle.getDeviceName() + "\"]." ));
		Thread.sleep(955);
	}
	
	@Test
	public void test05_addReceiverInvalidZoneName() throws InterruptedException {
		String [] receivers = {txSingle.getDeviceName()};
		EditZoneReceiversObject edit = editReceiver.new EditZoneReceiversObject();
		edit.zone_name = "invalid zone";
		edit.receiver_names = receivers;
		
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.post(editReceiver.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Zone " + edit.zone_name + " does not exist."));
		Thread.sleep(953);
	}
	
	@Test
	public void test06_invalidReceiverName() throws InterruptedException {
		String zoneName = "test06addReceiver";
		String zoneDescription = "this is a zone";
		String [] receivers = {"invalidDevice"};
		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditZoneReceiversObject edit = editReceiver.new EditZoneReceiversObject();
		edit.zone_name = zoneName;
		edit.receiver_names = receivers;
		
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.post(editReceiver.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following receiver does not exist: [\"" + receivers[0] + "\"]." ));
		Thread.sleep(932);
	}
	
	@Test
	public void test07_swapReceiverBetweenZones() throws InterruptedException {
		String zoneName1 = "test07addReceiver1";
		String zoneName2 = "test07addReceiver2";
		String zoneDescription = "This is a zone";
		String[] receivers = {rxSingle.getDeviceName()};
		
		config.createZone(zoneName1, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		config.createZone(zoneName2, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		editReceiver.editReceivers(zoneName1, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName1, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		
		editReceiver.editReceivers(zoneName2, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName2, receivers[0], driver);
		Assert.assertTrue(isAssigned, "Device was not assigned to zone");
		
		 isAssigned = zoneMethods.isDeviceAssignedToZone(zoneName1, receivers[0], driver);
		Assert.assertFalse(isAssigned, "Device was  assigned to zone");
		editReceiver.removeAllReceivers(zoneName1, boxillaManager, boxillaRestUser, boxillaRestPassword);
		editReceiver.removeAllReceivers(zoneName2, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
		Thread.sleep(1029);
	}
	
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	@Parameters({ "browser" })
//	public void login(String browser, Method method) throws InterruptedException {
//		log.info("Deleting all connections");
//		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
//		
//		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
//		
//		
//		log.info("Deleting all zones before starting testcases");
//		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
//		
//		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
//
//		
//		super.login(browser, method);
//		
//	
//	}
	@AfterClass
	public void afterClass() throws InterruptedException {
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
		
		
		log.info("Deleting all zones before starting testcases");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
		printSuitetDetails(true);
	}
	
}
