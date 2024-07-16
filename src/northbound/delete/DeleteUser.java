package northbound.delete;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Users;


public class DeleteUser extends StartupTestCase {

	final static Logger log = Logger.getLogger(DeleteUser.class);
	private UsersMethods user = new UsersMethods();
	
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
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
	}
	
//	@Test
	public void test01_deleteSingleUser() {
		//create user first
		UserCreation user = new UserCreation();
		user.username = "test01DeleteUser";
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
		
		log.info("Checking user creation using REST ");
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = user.username;
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo(user.privilege))
		.body("message.users[0].auto_connect", equalTo(user.auto_connect))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo(user.remote_access)).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);
		
		//delete user
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted user [\"" + user.username + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.DELETE, response);
		
		//check again for user
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following user does not exist: [\"" + user.username + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test02_deleteMultipleUsers() {
		String user1 = "test02DeleteUser1";
		String user2 = "test02DeleteUser2";
		String user3 = "test02DeleteUser3";
		//create user first
		UserCreation user = new UserCreation();
		user.username = user1;
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
		log.info("User 1 created");
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.POST, response);
		
		user.username = user2;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		log.info("User 2 created");
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.POST, response);
		
		user.username = user3;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		log.info("User 3 created");
		log.info("Checking user creation using REST ");
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.POST, response);
		
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[3];
		getUser.usernames[0] = user1;
		getUser.usernames[1] = user2;
		getUser.usernames[2] = user3;
		
		
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user1))
		.body("message.users[1].username", equalTo(user2))
		.body("message.users[2].username", equalTo(user3)).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);

		
		//delete user
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted users [\"" + user1 + "\", \"test02DeleteUser2\", \"test02DeleteUser3\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.DELETE, response);
		
		//check again for user
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following users do not exist: [\"" + user1 + "\", \"test02DeleteUser2\", \"test02DeleteUser3\"].")).extract().response();
	
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test03_deleteUserNoExist() {
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = "NoexistUser";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following user does not exist: [\"" + getUser.usernames[0] + "\"].")).extract().response();	
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.DELETE, response);
	}
	
	@Test
	public void test04_deleteNoUniqueUser() throws InterruptedException {
		//create template user
		String userName = "test04Delete";
		user.addTemplate(driver);
		Users.templatePrivilegeGeneral(driver).click();
		SeleniumActions.exectuteJavaScriptClick(driver, Users.getTemplateRemoteAccessBtn(driver));
		//Users.getTemplateRemoteAccessBtn(driver).click();
		user.addTemplateAutoConnectOFF(driver, "test04Template");
		//create user
		user.addUserWithTemplate(driver, userName, "test04Template");
		
		log.info("Attempting to delete non unique user");
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = userName;
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(403)
		.body("message", equalTo("The following users are with non-unique properties: [\"" + userName + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.DELETE, response);
		
		log.info("Deleting user through Boxilla UI to clean up");
		user.deleteUser(userName, driver);
	}
}
