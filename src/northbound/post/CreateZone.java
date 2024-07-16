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
import northbound.post.config.CreateZoneConfig;


public class CreateZone extends StartupTestCase {

	
	final static Logger log = Logger.getLogger(CreateZone.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all zones before starting testcases");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
		

	}
	
	
	@Test
	public void test01_createZone() throws InterruptedException {
//		String zoneName = "test01Zone";
//		String zoneDescription = "this is a test01 zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		log.info("Verfiy through NB rest that zone is created");
//		GetIndividualZoneConfig.GetZone getZone = getZoneConfig.new GetZone();
//		getZone.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(getZone)
//		.get(getZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body(getZoneConfig.getZoneName(0), equalTo(zoneName))
//		.body(getZoneConfig.getZoneDescription(0), equalTo(zoneDescription));	
//		
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
		Thread.sleep(2939);
	}
	
	@Test
	public void test02_createZone32Characters() throws InterruptedException {
//		String zoneName = "thisierufjghtrdfcbvmndsewasdrftg";
//		String zoneDescription = "this is a test02 zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		log.info("Verfiy through NB rest that zone is created");
//		GetIndividualZoneConfig.GetZone getZone = getZoneConfig.new GetZone();
//		getZone.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(getZone)
//		.get(getZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body(getZoneConfig.getZoneName(0), equalTo(zoneName))
//		.body(getZoneConfig.getZoneDescription(0), equalTo(zoneDescription));	
//		
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
		Thread.sleep(1284);
	}
	
	@Test
	public void test03_createZone33CharacterName() throws InterruptedException {
//		String zoneName = "thisierufjghtrdfcbvmndsewasdrftgd";
//		String zoneDescription = "this is a test03 zone";
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Zone name length cannot exceed 32 characters."));	
		Thread.sleep(565);
	}
	
	@Test
	public void test04_createZone64CharacterDescription() throws InterruptedException {
//		String zoneName = "test04Zone";
//		String zoneDescription = "qwertyuiopoiuytrewqwertyuiopoiuytrewqwertyuiopoiuytrewqwertyuiop";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		log.info("Verfiy through NB rest that zone is created");
//		GetIndividualZoneConfig.GetZone getZone = getZoneConfig.new GetZone();
//		getZone.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(getZone)
//		.get(getZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body(getZoneConfig.getZoneName(0), equalTo(zoneName))
//		.body(getZoneConfig.getZoneDescription(0), equalTo(zoneDescription));	
//		
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
		Thread.sleep(1029);
	}
	
	@Test
	public void test05_createZone65CharacterDescription() throws InterruptedException {
//		String zoneName = "test05Zone";
//		String zoneDescription = "qwertyuiopoiuytrewqwertyuiopoiuytrewqwertyuiopoiuytrewqwertyuiope";
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Zone description length cannot exceed 64 characters."));	
		Thread.sleep(435);
	}
	
	@Test
	public void test06_createZoneEmptyZoneName() throws InterruptedException {
//		String zoneName = "";
//		String zoneDescription = "Test Zone";
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone name invalid: . Its characters must be from [0-9a-zA-Z.-] charset"));	
		Thread.sleep(342);
	}
	
	@Test
	public void test07_createZoneEmptyZoneDescription() throws InterruptedException {
//		String zoneName = "test07Zone";
//		String zoneDescription = "";
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone description cannot be empty."));	
		Thread.sleep(232);
	}
	
	@Test
	public void test08_createZoneInvalidCharactersName() throws InterruptedException {
//		String zoneName = "test08Zone)(";
//		String zoneDescription = "this is a zone";
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Zone name invalid: " + zoneName  + ". Its characters must be from [0-9a-zA-Z.-] charset"));
		Thread.sleep(364);
	}
	
	
	@Test
	public void test09_createDuplicateZone() throws InterruptedException {
//		String zoneName = "test09Zone";
//		String zoneDescription = "this is a zone";
//		
//		config.createZone(zoneName, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		log.info("Verfiy through NB rest that zone is created");
//		GetIndividualZoneConfig.GetZone getZone = getZoneConfig.new GetZone();
//		getZone.name = zoneName;
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(getZone)
//		.get(getZoneConfig.getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body(getZoneConfig.getZoneName(0), equalTo(zoneName))
//		.body(getZoneConfig.getZoneDescription(0), equalTo(zoneDescription));	
//		
//		
//		log.info("Verfiy in boxilla UI that zone is created");
//		String[] details = zoneMethods.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);	
//	
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName;
//		cZone.description = zoneDescription;	
//		log.info("Attempting to create zone with name:" + zoneName);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Duplicate zone name received: " + zoneName  + ". All zones must have a unique name"));
		Thread.sleep(1382);
	}
	
	@Test
	public void test10_zoneLimitReached() throws InterruptedException {
//		log.info("removing all zones first");
//		
//		String zoneName = "test10Zone";
//		String description = "test zone";		
//		
//		
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));
//		
//		for(int j=0; j  < 10; j++) {
//			config.createZone(zoneName + j, description, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		log.info("Ten zones created. Attempting to create one more");
//		
//		CreateZoneConfig.CreateZone cZone = config.new CreateZone();
//		cZone.name = zoneName + "11";
//		cZone.description = description;	
//		log.info("Attempting to create zone with name:" + zoneName + "11");
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(cZone)
//		.post(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Zones limit reached."));	
		Thread.sleep(1278);
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

		
		super.login(browser,  method);

	}
	
	
}
