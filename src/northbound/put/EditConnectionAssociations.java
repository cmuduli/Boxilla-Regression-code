package northbound.put;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.delete.DeleteAllUsers;
import northbound.get.BoxillaHeaders;
import northbound.get.config.UsersConfig;
import northbound.get.config.UsersConfig.GetUser;
import northbound.post.config.CreateKvmConnectionsConfig;

public class EditConnectionAssociations extends StartupTestCase{
	
	final static Logger log = Logger.getLogger(EditConnectionAssociations.class);
	private String assUsername = "editAssUser";
	private CreateKvmConnectionsConfig conConfig = new CreateKvmConnectionsConfig();
	private UsersConfig userConfig = new UsersConfig();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	
		UserCreation user = new UserCreation();
		user.username = assUsername;
		user.password = "password";
		user.privilege = "Administrator";
		user.remote_access = "No";
		user.auto_connect = "No";
		

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.POST, response);
		log.info("User 1 created");
	}
	
	public class AssConnection {
		public String username;
		public String[] connection_names;
	}
	
	@Test
	public void test01_addSingleConnectionToUser() {
		conConfig.createViaTxConnection("assCon1", txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
				"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		log.info("Adding single connection to user");
		AssConnection assCon = new AssConnection(); 
		assCon.username = assUsername;
		assCon.connection_names = new String[1];
		assCon.connection_names[0] = "assCon1";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(assCon)
		.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated connection associations for user " + assCon.username + ".")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
		
		log.info("Checking user has the associated connection");
		UsersConfig.GetUser user = userConfig.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = assUsername;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(userConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(userConfig.getUsersUsername(0), equalTo(assUsername))
		.body(userConfig.getUsersConnectionsConnectionName(0, 0), equalTo(assCon.connection_names[0])).extract().response();
		
		SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test02_addMultipleConnectionsToUser() {
		String connection1 = "1assConnTest2";
		String connection2 = "2assConnTest2";
		String connection3 = "3assConnTest2";
		String connection4 = "4assConnTest2";
		
		conConfig.createViaTxConnection(connection1, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
				"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conConfig.createViaTxConnection(connection2, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
				"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conConfig.createViaTxConnection(connection3, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
				"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conConfig.createViaTxConnection(connection4, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
				"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		log.info("Adding single connection to user");
		AssConnection assCon = new AssConnection();
		assCon.username = assUsername;
		assCon.connection_names = new String[4];
		assCon.connection_names[0] = connection1;
		assCon.connection_names[1] = connection2;
		assCon.connection_names[2] = connection3;
		assCon.connection_names[3] = connection4;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(assCon)
		.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated connection associations for user " + assCon.username + ".")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
		
		log.info("Checking user has the associated connection");
		UsersConfig.GetUser user = userConfig.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = assUsername;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(userConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(userConfig.getUsersUsername(0), equalTo(assUsername))
		.body(userConfig.getUsersConnectionsConnectionName(0, 0), equalTo(assCon.connection_names[0]))
		.body(userConfig.getUsersConnectionsConnectionName(0, 1), equalTo(assCon.connection_names[1]))
		.body(userConfig.getUsersConnectionsConnectionName(0, 2), equalTo(assCon.connection_names[2]))
		.body(userConfig.getUsersConnectionsConnectionName(0, 3), equalTo(assCon.connection_names[3])).extract().response();
		
		SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	@Test
	public void test03_deleteAllAssociations() {
			String connection1 = "1assConnTest3";
			String connection2 = "2assConnTest3";
			String connection3 = "3assConnTest3";
			String connection4 = "4assConnTest3";
			
			conConfig.createViaTxConnection(connection1, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
					"No", "10", "",boxillaManager, boxillaRestUser, boxillaRestPassword);
			conConfig.createViaTxConnection(connection2, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
					"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
			conConfig.createViaTxConnection(connection3, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
					"No","10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
			conConfig.createViaTxConnection(connection4, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No",
					"No", "10", "",boxillaManager, boxillaRestUser, boxillaRestPassword);
			
			log.info("Adding single connection to user");
			AssConnection assCon = new AssConnection();
			assCon.username = assUsername;
			assCon.connection_names = new String[4];
			assCon.connection_names[0] = connection1;
			assCon.connection_names[1] = connection2;
			assCon.connection_names[2] = connection3;
			assCon.connection_names[3] = connection4;
			
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(assCon)
			.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated connection associations for user " + assCon.username + ".")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
			
			log.info("Checking user has the associated connection");
			UsersConfig.GetUser user = userConfig.new GetUser();
			user.usernames = new String[1];
			user.usernames[0] = assUsername;
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
			.headers(BoxillaHeaders.getBoxillaHeaders())
			.body(user)
			.when().contentType(ContentType.JSON)
			.get(userConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(userConfig.getUsersUsername(0), equalTo(assUsername))
			.body(userConfig.getUsersConnectionsConnectionName(0, 0), equalTo(assCon.connection_names[0]))
			.body(userConfig.getUsersConnectionsConnectionName(0, 1), equalTo(assCon.connection_names[1]))
			.body(userConfig.getUsersConnectionsConnectionName(0, 2), equalTo(assCon.connection_names[2]))
			.body(userConfig.getUsersConnectionsConnectionName(0, 3), equalTo(assCon.connection_names[3])).extract().response();
			
			SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			log.info("Removing all associated connecitons");
			assCon.connection_names = new String[4];
			
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(assCon)
			.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated connection associations for user " + assCon.username + ".")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
		
			log.info("Check connections have been removed");
			 response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
			.headers(BoxillaHeaders.getBoxillaHeaders())
			.body(user)
			.when().contentType(ContentType.JSON)
			.get(userConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(userConfig.getUsersUsername(0), equalTo(assUsername))
			.body("message.users[0].connections", hasSize(0)).extract().response();
			
			SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test04_addInvalidConnections() {
		AssConnection assCon = new AssConnection();
		assCon.username = assUsername;
		assCon.connection_names = new String[2];
		assCon.connection_names[0] = "invalid1";
		assCon.connection_names[1] = "invalid2";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(assCon)
		.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following connections do not exist: [\"invalid1\", \"invalid2\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test05_invalidUser() {
		AssConnection assCon = new AssConnection();
		assCon.username = "invalidUser";
		assCon.connection_names = new String[2];
		assCon.connection_names[0] = "invalid1";
		assCon.connection_names[1] = "invalid2";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(assCon)
		.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("User " + assCon.username + " does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test06_emptyUsername() {
		AssConnection assCon = new AssConnection();
		assCon.username = null;
		assCon.connection_names = new String[1];
		assCon.connection_names[0] = null;

		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(assCon)
		.put("https://" + boxillaManager  + "/bxa-api/users/kvm/connections")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("User  does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/connections", REQUEST_TYPE.PUT, response);
	}
	

	
	class UserCreation {
		public String username;
		public String password;
		public String privilege;
		public String remote_access;
		public String auto_connect;
		public String auto_connect_name;
	}
	
	 class GetUser {
			public String[] usernames;
		}

}
