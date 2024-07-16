package northbound.delete;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
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
import northbound.delete.DeleteUser.GetUser;
import northbound.delete.DeleteUser.UserCreation;
import northbound.get.BoxillaHeaders;
import objects.Users;

public class DeleteAllUsers extends StartupTestCase{

	final static Logger log = Logger.getLogger(DeleteAllUsers.class);
	private UsersMethods user = new UsersMethods();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		createUsers();
	
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
	private void createUsers() {
		String user1 = "test02DeleteAllUser1";
		String user2 = "test02DeleteAllUser2";
		String user3 = "test02DeleteAllUser3";
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
		user.privilege = "PowerUser";
		response =given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.POST, response);
		
		log.info("User 2 created");
		user.username = user3;
		user.privilege = "User";
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
	}
	
	@Test
	public void test01_deleteAllUsers() {
		log.info("Deleting all users");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all users.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
		
		log.info("Getting all users and asserting size is 0");
	    response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message.users", hasSize(0)).extract().response();
	    
	    SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test02_deleteAllUsersNonUnique() throws InterruptedException {
		String userName = "test02DeleteAllUnique";
		user.addTemplate(driver);
		Users.templatePrivilegeGeneral(driver).click();
		SeleniumActions.exectuteJavaScriptClick(driver, Users.getTemplateRemoteAccessBtn(driver));
		//Users.getTemplateRemoteAccessBtn(driver).click();
		user.addTemplateAutoConnectOFF(driver, "deleteAllTemp");
		//create user
		user.addUserWithTemplate(driver, userName, "deleteAllTemp");
		
		log.info("Deleting all users");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all users.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
		
		log.info("Getting all users and asserting size is 0");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get("https://" + boxillaManager  + "/bxa-api/users/kvm")
		.then().assertThat().statusCode(200)
		.body("message.users", hasSize(1)).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm", REQUEST_TYPE.GET, response);
		
		user.deleteUser(userName, driver);
		
	}
}
