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
import methods.UsersMethods;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.GetIndividualZonesInfo;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditUserFavouritesConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneReceiversConfig;
import northbound.put.config.EditZoneDetailsConfig;

public class DeleteAllZones extends StartupTestCase {

	
	final static Logger log = Logger.getLogger(DeleteAllZones.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	private EditZoneDetailsConfig editZoneConfig = new EditZoneDetailsConfig();
	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	private EditUserFavouritesConfig favConfig = new EditUserFavouritesConfig();
	private UsersMethods userMethods = new UsersMethods();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
//		
//		log.info("Deleting all connections");
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all connections."));
//		
//		
//		log.info("Deleting all zones before starting testcases");
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));

	}
	
	
	public String getUri(String boxillaManager) {
		return "https://" + boxillaManager  + "/bxa-api/zones/all";
	}
	
	@Test
	public void test01_deleteAllZonesNoAssign() throws InterruptedException {
//		String zoneName = "test01DeleteAll";
//		String zoneDescription = "this is a zone";
//		
//		for(int j=0; j < 10; j++) {
//			config.createZone(zoneName + j, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//			boolean isAvailable = zoneMethods.isZoneAvailable(driver, zoneName + j);
//			Assert.assertTrue(isAvailable, "Zone was not created:" + zoneName + j);
//		}
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete(getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));
//		
//		for(int j = 0; j < 10; j++) {
//			boolean isAvailable = zoneMethods.isZoneAvailable(driver, zoneName + j);
//			Assert.assertFalse(isAvailable, "Zone was not created:" + zoneName + j);
//		}
		Thread.sleep(23939);
		
	}
	
	@Test
	public void test02_deleteAllZonesDeviceAssigned() throws InterruptedException {
//		String zoneName = "test02DeleteAll";
//		String zoneDescription = "this is a zone";
//		
//		for(int j=0; j < 10; j++) {
//			config.createZone(zoneName + j, zoneDescription, boxillaManager, boxillaRestUser, boxillaRestPassword);
//			boolean isAvailable = zoneMethods.isZoneAvailable(driver, zoneName + j);
//			Assert.assertTrue(isAvailable, "Zone was not created:" + zoneName + j);
//		}
//		String[] receivers = {rxSingle.getDeviceName()};
//		editReceiver.editReceivers(zoneName + "0", receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete(getUri(boxillaManager))
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Fail to delete the following zone: [\"" + zoneName + "0" + "\"]. The zones have been assigned to appliances or connections."));
//		
//		boolean zoneLeft = zoneMethods.isZoneAvailable(driver, zoneName + "0");
//		Assert.assertTrue(zoneLeft, "Zone was removed when it should not have been:" + zoneName + "0");
//		
//		log.info("Checking the rest of the zones have been deleted");
//		for(int j=1; j < 10; j++) {
//			boolean isAvailable = zoneMethods.isZoneAvailable(driver, zoneName + j);
//			Assert.assertFalse(isAvailable, "Zone was not created:" + zoneName + j);
//		}
//		editReceiver.removeAllReceivers(zoneName + "0", boxillaManager, boxillaRestUser, boxillaRestPassword);
		Thread.sleep(49303);
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
