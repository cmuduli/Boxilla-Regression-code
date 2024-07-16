package northbound.put;

import static io.restassured.RestAssured.given;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.get.config.UsersConfig;
import northbound.put.config.EditRestUsernameConfig;
import static org.hamcrest.Matchers.equalTo;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.get.config.UsersConfig;
import northbound.post.CreateKvmConnections;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;

import org.apache.log4j.Logger;

public class EditRestUsername extends StartupTestCase {

	EditRestUsernameConfig config = new EditRestUsernameConfig();
	final static Logger log = Logger.getLogger(EditRestUsername.class);
	private UsersConfig userConfig = new UsersConfig();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test
	public void test01_changeUsername() {
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = "brendan";
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		
		log.info("Running get all users with newly updated username");
		response = given().auth().preemptive().basic(username.new_username, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(userConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(200).extract().response();
		 
		 SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		 
		 log.info("chaning username back to original");
		 username.username = username.new_username;
		 username.new_username = boxillaRestUser;
		 response = given().auth().preemptive().basic(username.username, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(username)
			.put(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated REST username.")).extract().response();
		 
		 SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		 
		 log.info("Running get all users with newly updated username");
		 response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		 .when().contentType(ContentType.JSON)//this will need to drill down deeper into the JSON and get the right value for the specific user
		 .get(userConfig.getUri(boxillaManager))
		 .then().assertThat().statusCode(200).extract().response();
		 
		 SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test02_changeUsernameMaxLengthExceeded() {
		String newUsername = "isusernameisinvalidduetomaxcharactersexceedingsixtyfourcharacters";
		log.info(newUsername.length());
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = newUsername;
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"new_username\"=>\"" + newUsername + "\"}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test03_changeUsernameMaxLength() {
		String newUsername = "iusernameisinvalidduetomaxcharactersexceedingsixtyfourcharacters";
		log.info(newUsername.length());
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = newUsername;
		
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		
		
		log.info("Running get all users with newly updated username");
		response = given().auth().preemptive().basic(username.new_username, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(userConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(200).extract().response();
		
		SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		log.info("Change password back to original");
		username.username = newUsername;
		username.new_username = boxillaRestUser;
		
		log.info("Editing username to " + username.new_username);
		response = given().auth().preemptive().basic(newUsername, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test04_changeUsernameUseOld() {
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = "brendan";
		
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		
		log.info("attempting to use rest with old username");
		log.info("Running get all users with old username");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(userConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(401)
		.body("message", equalTo("Operation is not authorized.")).extract().response();
		 
		 SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		 
		 log.info("Changing password back to original");
		 username.username = "brendan";
			username.new_username = boxillaRestUser;
			response = given().auth().preemptive().basic(username.username, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(username)
			.put(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated REST username.")).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
			
			log.info("Running get all users with original username");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.get(userConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200).extract().response();
			 
			 SaveResponseStatistics(userConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test05_changeUsernameEmpty() {
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = "";
		
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"new_username\"=>\"\"}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test06_changeUsernameNull() {
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = boxillaRestUser;
		username.new_username = null;
		
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"new_username\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
}
