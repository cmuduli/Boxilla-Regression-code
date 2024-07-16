package northbound.get;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.testng.ITestResult;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.Rest.SystemFacts;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.UsersMethods;
import northbound.get.config.UsersConfig;
import testNG.Utilities;

public class Users extends StartupTestCase {

	private UsersConfig config = new UsersConfig();
	private String autoConnectCon = "userGet";
	private UsersMethods users = new UsersMethods();
	private ConnectionsMethods connections = new ConnectionsMethods();
	private String dataFile = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\usercreate.txt";
	final static Logger log = Logger.getLogger(Users.class);

		
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		try {
			
			log.info("Deleting all users");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all users.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
			
			cleanUpLogin();
			connections.createMasterConnection(autoConnectCon, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
			cleanUpLogout();
		}catch(Exception e) {
			cleanUpLogout();
		}
	}
	
	@DataProvider(name = "createUser")
	public Object[][] createUserData() throws IOException {
		return readData(dataFile);
	}
	
	
	
	@Test(dataProvider="createUser")
	public void test02_checkSingleUser(String name, String password, String isTemplate, String isActiveDir, String privilege, 
			String isAutoConnect, String autoConnectName, String isRemote) {
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames  = new String[1];
		user.usernames[0] = name;
		
		String [] valuestoCheck = config.convertToStandard(privilege,  isAutoConnect,  autoConnectName,  isRemote);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(name))
		.body(config.getUsersPrivilege(0), equalTo(valuestoCheck[0]))
		.body(config.getUsersAutoConnect(0), equalTo(valuestoCheck[1]))
		.body(config.getUsersAutoConnectName(0), equalTo(valuestoCheck[2]))
		.body(config.getUsersRemoteAccess(0), equalTo(valuestoCheck[3])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test03_getMultipleUsers() {
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[2];
		user.usernames[0] = "usercreate3";
		user.usernames[1] = "usercreate5";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)	
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(user.usernames[0]))
		.body(config.getUsersPrivilege(0), equalTo("Administrator"))
		.body(config.getUsersAutoConnect(0), equalTo("No"))
		.body(config.getUsersAutoConnectName(0), equalTo(null))
		.body(config.getUsersRemoteAccess(0), equalTo("No"))
		.body(config.getUsersUsername(1), equalTo(user.usernames[1]))
		.body(config.getUsersPrivilege(1), equalTo("PowerUser"))
		.body(config.getUsersAutoConnect(1), equalTo("Yes"))
		.body(config.getUsersAutoConnectName(1), equalTo(autoConnectCon))
		.body(config.getUsersRemoteAccess(1), equalTo("Yes")).extract().response()	;
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}

	@Test
	public void test04_getAllUsers() {


		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		 .when().contentType(ContentType.JSON)//this will need to drill down deeper into the JSON and get the right value for the specific user
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
			.body(config.getUsersUsername(0), equalTo("usercreate1"))
			.body(config.getUsersPrivilege(0), equalTo("Administrator"))
			.body(config.getUsersAutoConnect(0), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(0), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(0), equalTo("Yes"))
			
			.body(config.getUsersUsername(1), equalTo("usercreate2"))
			.body(config.getUsersPrivilege(1), equalTo("Administrator"))
			.body(config.getUsersAutoConnect(1), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(1), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(1), equalTo("No"))
			
			.body(config.getUsersUsername(2), equalTo("usercreate3"))
			.body(config.getUsersPrivilege(2), equalTo("Administrator"))
			.body(config.getUsersAutoConnect(2), equalTo("No"))
			.body(config.getUsersAutoConnectName(2), equalTo(null))
			.body(config.getUsersRemoteAccess(2), equalTo("No"))
			
			.body(config.getUsersUsername(3), equalTo("usercreate4"))
			.body(config.getUsersPrivilege(3), equalTo("Administrator"))
			.body(config.getUsersAutoConnect(3), equalTo("No"))
			.body(config.getUsersAutoConnectName(3), equalTo(null))
			.body(config.getUsersRemoteAccess(3), equalTo("Yes"))	
			
			.body(config.getUsersUsername(4), equalTo("usercreate5"))
			.body(config.getUsersPrivilege(4), equalTo("PowerUser"))
			.body(config.getUsersAutoConnect(4), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(4), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(4), equalTo("Yes"))
			
			.body(config.getUsersUsername(5), equalTo("usercreate6"))
			.body(config.getUsersPrivilege(5), equalTo("PowerUser"))
			.body(config.getUsersAutoConnect(5), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(5), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(5), equalTo("No"))	
		 
			.body(config.getUsersUsername(6), equalTo("usercreate7"))
			.body(config.getUsersPrivilege(6), equalTo("PowerUser"))
			.body(config.getUsersAutoConnect(6), equalTo("No"))
			.body(config.getUsersAutoConnectName(6), equalTo(null))
			.body(config.getUsersRemoteAccess(6), equalTo("No"))
			
			.body(config.getUsersUsername(7), equalTo("usercreate8"))
			.body(config.getUsersPrivilege(7), equalTo("PowerUser"))
			.body(config.getUsersAutoConnect(7), equalTo("No"))
			.body(config.getUsersAutoConnectName(7), equalTo(null))
			.body(config.getUsersRemoteAccess(7), equalTo("Yes"))
		
			.body(config.getUsersUsername(8), equalTo("usercreate9"))
			.body(config.getUsersPrivilege(8), equalTo("User"))
			.body(config.getUsersAutoConnect(8), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(8), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(8), equalTo("Yes"))
		
			.body(config.getUsersUsername(9), equalTo("usercreate10"))
			.body(config.getUsersPrivilege(9), equalTo("User"))
			.body(config.getUsersAutoConnect(9), equalTo("Yes"))
			.body(config.getUsersAutoConnectName(9), equalTo(autoConnectCon))
			.body(config.getUsersRemoteAccess(9), equalTo("No"))	
			
			.body(config.getUsersUsername(10), equalTo("usercreate11"))
			.body(config.getUsersPrivilege(10), equalTo("User"))
			.body(config.getUsersAutoConnect(10), equalTo("No"))
			.body(config.getUsersAutoConnectName(10), equalTo(null))
			.body(config.getUsersRemoteAccess(10), equalTo("No"))
			
			.body(config.getUsersUsername(11), equalTo("usercreate12"))
			.body(config.getUsersPrivilege(11), equalTo("User"))
			.body(config.getUsersAutoConnect(11), equalTo("No"))
			.body(config.getUsersAutoConnectName(11), equalTo(null))
			.body(config.getUsersRemoteAccess(11), equalTo("Yes")).extract().response()	;
		
		    SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		 
		 
	}

	@Test
	public void test05_getSingleUserNoExist() {
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = "invalidUser";
		
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following user does not exist: [\"invalidUser\"].")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test06_getMiltipleUserNoExist() {
		UsersConfig.GetUser user = config.new GetUser();
		String invalidUser1 = "invalidUser1";
		String invalidUser2 = "invalidUser2";
		user.usernames = new String[2];
		user.usernames[0] = invalidUser1;
		user.usernames[1] = invalidUser2;
		String error = "The following users do not exist: [\"invalidUser1\", \"invalidUser2\"].";
		log.info("Error:::" + error);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo(error)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test	
	public void test07_emptyArray() {
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[0];		//empty array
	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message.users", hasSize(0)).extract().response();	
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test08_deleteUser() throws InterruptedException {
		//create user first
		String userName = "test08_deleteUser";
		users.masterCreateUser(driver, userName, "test", "false", "false", "admin", "false", "", "false");
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = userName;
		String [] check = config.convertToStandard("admin", "false", "", "false");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(userName))
		.body(config.getUsersPrivilege(0), equalTo(check[0]))
		.body(config.getUsersAutoConnect(0), equalTo(check[1]))
		.body(config.getUsersAutoConnectName(0), equalTo(check[2]))
		.body(config.getUsersRemoteAccess(0), equalTo(check[3])).extract().response();	
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		//delete user
		users.deleteUser(userName, driver);
		//run rest again and check if user is deleted
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following user does not exist: [\"test08_deleteUser\"].")).extract().response();	
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test09_editUser() throws InterruptedException {
		String userName = "test09_editUser";
		String newUserName = "test09_newUserName";
		users.masterCreateUser(driver, userName, "test", "false", "false", "admin", "false", "", "false");
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = userName;
		String [] check = config.convertToStandard("admin", "false", "", "false");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(userName))
		.body(config.getUsersPrivilege(0), equalTo(check[0]))
		.body(config.getUsersAutoConnect(0), equalTo(check[1]))
		.body(config.getUsersAutoConnectName(0), equalTo(check[2]))
		.body(config.getUsersRemoteAccess(0), equalTo(check[3])).extract().response();	
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		users.manageUser(driver, userName);
		users.userEditName(driver, userName, newUserName);
		//check old username doesnt exist any more
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following user does not exist: [\"test09_editUser\"].")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		user.usernames[0] = newUserName;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(newUserName))
		.body(config.getUsersPrivilege(0), equalTo(check[0]))
		.body(config.getUsersAutoConnect(0), equalTo(check[1]))
		.body(config.getUsersAutoConnectName(0), equalTo(check[2]))
		.body(config.getUsersRemoteAccess(0), equalTo(check[3])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test10_invalidParameters() {
		ForceConnect connect = new ForceConnect();//use this as payload. Will be invalid
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(connect)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameters: [\"user\", \"connection\"].")).extract().response();		//update message when i know exactly how it looks
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	//@Test
	public void test11_userWithConnections() throws InterruptedException {
		String userName = "test11_userWithConnections";
		String connectionName = "test11_connection1";
		String connectionName2 = "test11_connection2";
		users.masterCreateUser(driver, userName, "test", "false", "false", "admin", "false", "", "false");
		
		//create connection and assign to user
		connections.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false", "10.1.1.1", driver);
		connections.createMasterConnection(connectionName2, "tx", "shared", "false", "false", "false", "false", "false", "10.1.1.1", driver);
		users.addConnectionToUser(driver, userName, connectionName, connectionName2);
		//run rest and confirm connections
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = userName;
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(user)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getUsersUsername(0), equalTo(userName))
		.body(config.getUsersConnectionsConnectionName(0, 0), equalTo(connectionName))
		.body(config.getUsersConnectionsConnectionName(0, 1), equalTo(connectionName2)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}
	
	@Test
	public void test12_unauthorizedAccessUsername() {
		Response response = given().auth().preemptive().basic("invalid", boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.when()
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(401)
		.body("message", equalTo("Operation is not authorized.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	
	@Test
	public void test13_unauthorizedAccessPassword() {
		Response response = given().auth().preemptive().basic(boxillaRestUser, "admin")
	.headers(BoxillaHeaders.getBoxillaHeaders())
	.when()
	.get(config.getUri(boxillaManager))
	.then().assertThat().statusCode(401)
	.body("message", equalTo("Operation is not authorized.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test14_simpleLoadTest() throws InterruptedException {
		UsersConfig.GetUser user = config.new GetUser();
		user.usernames = new String[1];
		user.usernames[0] = "admin";
		String [] check = config.convertToStandard("admin", "false", "", "false");
		
		for(int j=0; j < 2000; j++) {
			log.info("Sending request:"  + j);
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
			.headers(BoxillaHeaders.getBoxillaHeaders())
			.body(user)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(config.getUsersUsername(0), equalTo("admin"))
			.body(config.getUsersPrivilege(0), equalTo(check[0]))
			.body(config.getUsersAutoConnect(0), equalTo(check[1]))
			.body(config.getUsersAutoConnectName(0), equalTo(null))
			.body(config.getUsersRemoteAccess(0), equalTo(check[3])).extract().response();	
			log.info("Request Successful:"  + j);
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		}
	}
	
	@Test(dataProvider="createUser")
	public void test01_createUsers(String name, String password, String isTemplate, String isActiveDir, String privilege, 
			String isAutoConnect, String autoConnectName, String isRemote) throws InterruptedException {
		users.masterCreateUser(driver, name, password, isTemplate, isActiveDir, privilege, isAutoConnect, autoConnectName, isRemote);
	}
	
}
