package northbound.put;

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
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.EditZoneConnections;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.put.config.EditZoneDetailsConfig;
import northbound.put.config.EditZoneDetailsConfig.EditZone;

public class EditZoneDetails extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditZoneDetails.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	private EditZoneDetailsConfig editZoneConfig = new EditZoneDetailsConfig();
	
	
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
	public void test01_editZone() throws InterruptedException {
//		String zoneName = "test01CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "test01CreateZoneNew";
//		String newDescription = "This is an edited zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		editZoneConfig.editZoneDetails(zoneName, newName, newDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, newName);
//		Assert.assertTrue(details[0].equals(newName), "Zone name from details did not equal " + newName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(newDescription), "Zone description from details did not equal " + newDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
		Thread.sleep(13030);
	}
	
	@Test
	public void test02_editZoneWithConnectionsDevices() {
		
	}
	
	@Test
	public void test03_editZoneZoneNotFound() {
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = "invalidName";
//		edit.description = "This is a zone";
//		edit.new_name = "newName";
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone " + edit.name + " does not exist."));
	}
	
	@Test
	public void test04_editZoneEmptyNewName() {
		
//		String zoneName = "test04CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = null;
//		String newDescription = "This is an edited zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName;
//		edit.description = newDescription;
//		edit.new_name = newName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Invalid parameter: {\"new_name\"=>nil}."));
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test05_editZoneEmptyDescription() {
//		String zoneName = "test05CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "test05New";
//		String newDescription = "";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName;
//		edit.description = newDescription;
//		edit.new_name = newName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone description cannot be empty."));
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test06_editZone32CharacterName() {
//		String zoneName = "test06CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "qwertyuiopoiuytrewqwertyuiopoiuy";
//		String newDescription = "updated description";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		editZoneConfig.editZoneDetails(zoneName, newName, newDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, newName);
//		Assert.assertTrue(details[0].equals(newName), "Zone name from details did not equal " + newName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(newDescription), "Zone description from details did not equal " + newDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test07_editZone33CharacterZoneName() {
//		String zoneName = "test07CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "qwertyuiopoiuytrewqwertyuiopoiuyy";
//		String newDescription = "updated description";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName;
//		edit.description = newDescription;
//		edit.new_name = newName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Zone name length cannot exceed 32 characters."));
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test08_editZone64CharacterDescription() {
//		String zoneName = "test08CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "test08CreateZoneNew";
//		String newDescription = "qwertyuiopoiuytrewqwertyuiopoiuyqwertyuiopoiuytrewqwertyuiopoiuy";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		editZoneConfig.editZoneDetails(zoneName, newName, newDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, newName);
//		Assert.assertTrue(details[0].equals(newName), "Zone name from details did not equal " + newName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(newDescription), "Zone description from details did not equal " + newDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test08_editZone65CharacterDescription() {
//		String zoneName = "test08CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "test08CreateZoneNew";
//		String newDescription = "tqwertyuiopoiuytrewqwertyuiopoiuyqwertyuiopoiuytrewqwertyuiopoiuy";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName;
//		edit.description = newDescription;
//		edit.new_name = newName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Zone description length cannot exceed 64 characters."));	
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test09_editZoneInvalidCharacterName() {
//		String zoneName = "test09CreateZone";
//		String zoneDescription = "This is a new zone";
//		String newName = "test08CreateZoneNew*";
//		String newDescription = "new Zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName;
//		edit.description = newDescription;
//		edit.new_name = newName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone name invalid: " + newName + ". Its characters must be from [0-9a-zA-Z.-] charset"));	
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
	}
	
	@Test
	public void test10_editZoneDuplicateName() {
//		String zoneName1 = "test10createZone1";
//		String zoneDescription = "this is a zone";
//		
//		config.createZone(zoneName1, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		String zoneName2 = "test10createZone2";
//		config.createZone(zoneName2, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		EditZone edit = editZoneConfig.new EditZone();
//		edit.name = zoneName1;
//		edit.description = zoneDescription;
//		edit.new_name = zoneName2;
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(edit)
//		.put(editZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(403);
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
