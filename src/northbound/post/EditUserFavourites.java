package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

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
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditUserFavouritesConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneReceiversConfig;
import northbound.post.config.EditUserFavouritesConfig.EditUserFavouriteObject;
import northbound.put.config.EditZoneDetailsConfig;

public class EditUserFavourites extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditZoneReceivers.class);
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
	public void test01_addFavouriteToGlobal() throws InterruptedException {
		Map<String, Object> conFavSlots = new HashMap<String, Object>();
		String username = "test01AddFav";
		String connectionName = "test01AddFavCon";
		userMethods.masterCreateUser(driver, username, "test", "false", "false", "admin", "false", "", "false");
		String[] allConnections = new String[10];
		log.info("Creating 10 connections and adding them to user connections");
		for(int j=0; j < 10; j++) {
			createConConfig.createViaTxConnection(connectionName + j, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
					"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
			allConnections[j] = connectionName + j;
		}
		
		userMethods.addAllConnectionsToUser(driver, username);
		
		log.info("Setting up JSON request by adding connections to slots");
		for(int j=0; j < 10; j++) {
			conFavSlots.put(Integer.toString(j), allConnections[j]);
		}
		
		
		favConfig.editUserFavourite(username, "", conFavSlots, boxillaManager, boxillaRestUser, boxillaRestPassword);
		log.info("Getting connection favs fro UI and verifying");
		for(int j=0; j < 10; j++) {
			String conName = userMethods.getCurrentConnectionFavourite(driver, username, j);
			Assert.assertTrue(conName.equals(allConnections[j]), "Connection " + allConnections[j] + " was not set to user favs");
		}
		
		userMethods.deleteUser(username, driver);
	}
	
	@Test
	public void test02_addFavouriteToZone() throws InterruptedException {
		Map<String, Object> conFavSlots = new HashMap<String, Object> ();
		String username  = "test02AddFav";
		String connectionName = "test02AddFavCon";
		String zoneName = "test02AddFavZone";
		userMethods.masterCreateUser(driver, username, "test", "false", "false", "admin", "false", "", "false");
		String[] allConnections = new String[10];
		log.info("Creating 10 connections and adding them to user connections");
		for(int j=0; j < 10; j++) {
			createConConfig.createViaTxConnection(connectionName + j, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
					"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
			allConnections[j] = connectionName + j;
			conFavSlots.put(Integer.toString(j), allConnections[j]);
		}
		
		log.info("Create zone");
		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		log.info("Adding all connection to zone");
		editZoneConConfig.editZoneConnections(zoneName, allConnections, boxillaManager, boxillaRestUser, boxillaRestPassword);
		userMethods.addAllConnectionsToUser(driver, username);
		favConfig.editUserFavourite(username, zoneName, conFavSlots, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		log.info("Getting connection favs fro UI and verifying");
		for(int j=0; j < 10; j++) {
			String conName = userMethods.getCurrentConnectionFavourite(driver, username, j, zoneName);
			Assert.assertTrue(conName.equals(allConnections[j]), "Connection " + allConnections[j] + " was not set to user favs");
		}
		
		//TO DO ONCE USER FAV BUG HAS BEEN FIXED IN BOXILLA
//		log.info("Getting user fav infor from zone screen");
//		String[] favsFromZone = zoneMethods.getUserFavourites(driver, zoneName, username);
//		
//		for(int j=0; j < 10; j++) {
//			Assert.assertTrue(favsFromZone[j].equals(allConnections[j]), "Favourites from zone screen did not match");
//		}

	}
	
	@Test
	public void test03_unassignFavourite() {
		//TODO
	}
	
	@Test
	public void test04_invalidUsername() {
		String zoneName = "test04UserFav";
		String[] connectionName = {"test04UserFavCon"};
		String username = "invalidUser";
		Map<String, Object> conFavSlots = new HashMap<String, Object> ();
		conFavSlots.put("0", connectionName[0]);
		
		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		createConConfig.createViaTxConnection(connectionName[0], txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditUserFavouriteObject obj = favConfig.new EditUserFavouriteObject();
		obj.username = username;
		obj.scope = "";
		obj.settings = conFavSlots;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(obj)
		.post(favConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("User invalidUser does not exist.")).extract().response();
		
		SaveResponseStatistics(favConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);

	}
	
	@Test
	public void test05_invalidScope() throws InterruptedException {
		Map<String, Object> conFavSlots = new HashMap<String, Object> ();
		String username  = "test05AddFav";
		String connectionName = "test05AddFavCon";
		userMethods.masterCreateUser(driver, username, "test", "false", "false", "admin", "false", "", "false");
		createConConfig.createViaTxConnection(connectionName, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		userMethods.addAllConnectionsToUser(driver, username);
		
		
		EditUserFavouriteObject obj = favConfig.new EditUserFavouriteObject();
		obj.username = username;
		obj.scope = "invalidScope";
		obj.settings = conFavSlots;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(obj)
		.post(favConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Zone invalidScope does not exist for favorite settings for user " + username + "." )).extract().response();
		
		SaveResponseStatistics(favConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test06_invalidConnection() throws InterruptedException {
		Map<String, Object> conFavSlots = new HashMap<String, Object> ();
		String username  = "test05AddFav";
		userMethods.masterCreateUser(driver, username, "test", "false", "false", "admin", "false", "", "false");
		conFavSlots.put("0", "invalidConnection");
		
		EditUserFavouriteObject obj = favConfig.new EditUserFavouriteObject();
		obj.username = username;
		obj.scope = "";
		obj.settings = conFavSlots;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(obj)
		.post(favConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Invalid user's favorite received - no connection for 'connection_name' :  invalidConnection" )).extract().response();

		SaveResponseStatistics(favConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);

	}
	
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	@Parameters({ "browser" })
//	public void login(String browser, Method method) throws InterruptedException {
//		log.info("Deleting all connections");
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all connections."));
//		
//		log.info("Deleting all zones before starting testcases");
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones."));
//		
//		super.login(browser, method);
//		
//	}
	
}
