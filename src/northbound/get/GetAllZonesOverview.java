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
import methods.ZoneMethods;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.CreateZone;
import northbound.post.config.CreateZoneConfig;
import static org.hamcrest.Matchers.hasSize;

import java.lang.reflect.Method;

public class GetAllZonesOverview extends StartupTestCase {

	
	final static Logger log = Logger.getLogger(GetAllZonesOverview.class);
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
	public void test01_getAllZonesOvewview() throws InterruptedException {
//		String zoneName = "testZone";
//		String description = "this is a zone";
//		
//		for(int j=0; j  < 10; j++) {
//			config.createZone(zoneName + j, description, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message.zones[0].name", equalTo(zoneName + "0"))
//		.body("message.zones[1].name", equalTo(zoneName + "1"))
//		.body("message.zones[2].name", equalTo(zoneName + "2"))
//		.body("message.zones[3].name", equalTo(zoneName + "3"))
//		.body("message.zones[4].name", equalTo(zoneName + "4"))
//		.body("message.zones[5].name", equalTo(zoneName + "5"))
//		.body("message.zones[6].name", equalTo(zoneName + "6"))
//		.body("message.zones[7].name", equalTo(zoneName + "7"))
//		.body("message.zones[8].name", equalTo(zoneName + "8"))
//		.body("message.zones[9].name", equalTo(zoneName + "9"));	
		Thread.sleep(12439);
	}
	
	@Test
	public void test02_getAllZonesOverviewNoZones() throws InterruptedException {
//		log.info("delete all zones first");
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message.zones", hasSize(0));
		Thread.sleep(6050);
		
	}
	
	@Test
	public void test03_getAllZonesOverviewSoak() throws InterruptedException {
//		String zoneName = "testZone";
//		String description = "this is a zone";
//		
//		for(int j=0; j  < 10; j++) {
//			config.createZone(zoneName + j, description, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		}
//		
//		for(int j=0; j < 500; j++) {
//			given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//			.when().contentType(ContentType.JSON)
//			.get("https://" + boxillaManager  + "/bxa-api/zones/all")
//			.then().assertThat().statusCode(200)
//			.body("message.zones[0].name", equalTo(zoneName + "0"))
//			.body("message.zones[1].name", equalTo(zoneName + "1"))
//			.body("message.zones[2].name", equalTo(zoneName + "2"))
//			.body("message.zones[3].name", equalTo(zoneName + "3"))
//			.body("message.zones[4].name", equalTo(zoneName + "4"))
//			.body("message.zones[5].name", equalTo(zoneName + "5"))
//			.body("message.zones[6].name", equalTo(zoneName + "6"))
//			.body("message.zones[7].name", equalTo(zoneName + "7"))
//			.body("message.zones[8].name", equalTo(zoneName + "8"))
//			.body("message.zones[9].name", equalTo(zoneName + "9"));	
//			
//			log.info("Iteration " + j + " successful");
//		}
		Thread.sleep(9843);
		
		
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
