package northbound.put;

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
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.get.config.UsersConfig;
import northbound.post.CreateKvmConnections;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;

public class EditKvmUser extends StartupTestCase {
	
	CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private UsersMethods users = new UsersMethods();
	final static Logger log = Logger.getLogger(EditKvmUser.class);
	private ConnectionStatusConfig connectionConfig = new ConnectionStatusConfig();
	private UsersConfig config = new UsersConfig();
	private String username = "editUserTest1";
	private String password = "test";
	private String privilege = "Administrator";
	private String remote = "No";
	
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/users/kvm";
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
	

		
	}
	
	class UserCreation {
		public String username;
		public String password;
		public String privilege;
		public String remote_access;
		public String auto_connect;
		public String auto_connect_name;
	}
	
	class UserEdit {
		public String username;
		public String password;
		public String privilege;
		public String remote_access;
		public String auto_connect;
		public String auto_connect_name;
		public String new_username;
	}
	
	
	@Test
	public void test01_editUserPrivilegePowerUser() throws InterruptedException {
		UserCreation user = new UserCreation();
		user.username = username;
		user.password = password;
		user.privilege = privilege;
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		
		UserEdit edit = new UserEdit();
		edit.username = username;
		edit.password = password;
		edit.privilege = "PowerUser";
		edit.remote_access = "Yes";
		edit.auto_connect = "No";
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated profile/properties for user " + edit.username +  ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		
		log.info("Check edited user in rest");
		
		
		UsersConfig.GetUser getUser = config.new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(username))
		.body("message.users[0].privilege", equalTo("PowerUser"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		log.info("Check users in boxilla");
		String [] valuestoCheck = convertToStandard(privilege,  "No",  null,  "Yes");
		String[] userDetails = users.getUserDetails(driver, username);
		System.out.println("User details are "+userDetails);
		Assert.assertTrue(userDetails[0].equals(username), "Username from table does not match. Expected:" + username + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "No" + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" +  valuestoCheck[2] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
		
	}
	
	
	@Test
	public void test02_editUserPrivilegeUser() throws InterruptedException {
		UserCreation user = new UserCreation();
		user.username = "test02Edit";
		user.password = password;
		user.privilege = privilege;
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		log.info("Checking user in rest");
		UsersConfig.GetUser getUser2 = config.new GetUser();
		getUser2.usernames  = new String[1];
		getUser2.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser2)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo("Administrator"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = "User";
		edit.remote_access = user.remote_access;
		edit.auto_connect = user.auto_connect;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		
		log.info("Check edited user in rest");
		
		
		UsersConfig.GetUser getUser = config.new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo("User"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		log.info("Check users in boxilla");
		String [] valuestoCheck = convertToStandard("User",  "No",  null,  "Yes");
		String[] userDetails = users.getUserDetails(driver, user.username);
		Assert.assertTrue(userDetails[0].equals(user.username), "Username from table does not match. Expected:" + user.username + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals("User"), "Privilege from table does not match. Expected:" + "User" + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "No" + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" +  valuestoCheck[2] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
		
	}
	
	@Test
	public void test03_editUserPrivilegeAdmin() throws InterruptedException {
		UserCreation user = new UserCreation();
		user.username = "test03Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		log.info("Checking user in rest");
		UsersConfig.GetUser getUser2 = config.new GetUser();
		getUser2.usernames  = new String[1];
		getUser2.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser2)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo("PowerUser"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = "Administrator";
		edit.remote_access = user.remote_access;
		edit.auto_connect = user.auto_connect;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		
		log.info("Check edited user in rest");
		
		
		UsersConfig.GetUser getUser = config.new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo("Administrator"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		log.info("Check users in boxilla");
		String [] valuestoCheck = convertToStandard("Administrator",  "No",  null,  "Yes");
		String[] userDetails = users.getUserDetails(driver, user.username);
		Assert.assertTrue(userDetails[0].equals(user.username), "Username from table does not match. Expected:" + user.username + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals("Administrator"), "Privilege from table does not match. Expected:" + "Administrator" + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "No" + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" +  valuestoCheck[2] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
		
	}
	
	//No way of checking if the password actually changed,. Must be done manually. Just checking response here
	@Test
	public void test04_changePassword() {
		UserCreation user = new UserCreation();
		user.username = "test04Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		log.info("Checking user in rest");
		UsersConfig.GetUser getUser2 = config.new GetUser();
		getUser2.usernames  = new String[1];
		getUser2.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser2)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo("PowerUser"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = "editPass";
		edit.privilege = "Administrator";
		edit.remote_access = user.remote_access;
		edit.auto_connect = user.auto_connect;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	
	
	@Test
		public void test05_editUserRemoteAccess() throws InterruptedException {
			UserCreation user = new UserCreation();
			user.username = "test05Edit";
			user.password = password;
			user.privilege = "PowerUser";
			user.remote_access = "Yes";
			user.auto_connect = "No";
			user.auto_connect_name = null;

			log.info("Creating user");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(user)
			.post(getUri())
			.then().assertThat().statusCode(201)
			.body("message", equalTo("Created user " + user.username + ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
			
			log.info("Checking user in rest");
			UsersConfig.GetUser getUser2 = config.new GetUser();
			getUser2.usernames  = new String[1];
			getUser2.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser2)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo("PowerUser"))
			.body("message.users[0].auto_connect", equalTo("No"))
			.body("message.users[0].auto_connect_name", equalTo(null))
			.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			
			UserEdit edit = new UserEdit();
			edit.username = user.username;
			edit.password = user.password;
			edit.privilege = user.privilege;
			edit.remote_access = "No";
			edit.auto_connect = user.auto_connect;
			
			log.info("Editing user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(edit)
			.put(getUri())
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
			
			log.info("Check edited user in rest");
			
			
			UsersConfig.GetUser getUser = config.new GetUser();
			getUser.usernames  = new String[1];
			getUser.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo(user.privilege))
			.body("message.users[0].auto_connect", equalTo("No"))
			.body("message.users[0].auto_connect_name", equalTo(null))
			.body("message.users[0].remote_access", equalTo("No")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			log.info("Check users in boxilla");
			String [] valuestoCheck = convertToStandard("Administrator",  "No",  null,  "Yes");
			String[] userDetails = users.getUserDetails(driver, user.username);
			Assert.assertTrue(userDetails[0].equals(user.username), "Username from table does not match. Expected:" + user.username + " , Actual:" + userDetails[0]);
			Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
			Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "No" + " , Actual:" + userDetails[2]);
			if(userDetails[3].equals("-")) {
				Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
			}else {
				Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" +  valuestoCheck[2] + ", Actual:" + userDetails[3]);
			}
			
			Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
			
		}
	
		
		@Test
		public void test06_editUserAutoConnect() throws InterruptedException {
			
			CreateKvmConnectionsConfig.CreateConnection con = createConConfig.new CreateConnection();
			con.name = "test06_editCon";
			con.group  = "ConnectViaTx";
			con.connection_type = "Private";
			con.host = txIp;
			con.audio = "Yes";
			con.extended_desktop = "Yes";
			con.persistent = "Yes";
			con.view_only = "Yes";
			con.usb_redirection = "Yes";
			con.cmode = "10";
			con.zone = "";
			log.info("Creating connection to be used as auto con");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(con)
			.post(createConConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(201).extract().response();
			
			SaveResponseStatistics(createConConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);
			
			log.info("Checking connection through REST");
			String json = "{ \"connection_names\": [\"" + con.name + "\"]}";
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(json)
			.when().contentType(ContentType.JSON)
			.get(createConConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(connectionConfig.getConnectionName(0), equalTo(con.name))
			.body("message.connections[0].host", equalTo(txIp))
			.body("message.connections[0].connection_type", equalTo(con.connection_type))
			.body("message.connections[0].view_only", equalTo(con.view_only))
			.body("message.connections[0].group", equalTo("ConnectViaTx"))
			.body("message.connections[0].extended_desktop", equalTo(con.extended_desktop))
			.body("message.connections[0].audio", equalTo(con.audio))
			.body("message.connections[0].persistent", equalTo(con.persistent))
			.body("message.connections[0].usb_redirection", equalTo(con.usb_redirection))
			.body("message.connections[0].config", equalTo("Unique")).extract().response();
			
			SaveResponseStatistics(createConConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			log.info("Creating auto connect no user");
			UserCreation user = new UserCreation();
			user.username = "test06Edit";
			user.password = password;
			user.privilege = "PowerUser";
			user.remote_access = "Yes";
			user.auto_connect = "No";
			user.auto_connect_name = null;

			log.info("Creating user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(user)
			.post(getUri())
			.then().assertThat().statusCode(201)
			.body("message", equalTo("Created user " + user.username + ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
			
			log.info("Checking user in rest");
			UsersConfig.GetUser getUser2 = config.new GetUser();
			getUser2.usernames  = new String[1];
			getUser2.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser2)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo(user.privilege))
			.body("message.users[0].auto_connect", equalTo(user.auto_connect))
			.body("message.users[0].auto_connect_name", equalTo(null))
			.body("message.users[0].remote_access", equalTo(user.remote_access)).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			
			UserEdit edit = new UserEdit();
			edit.username = user.username;
			edit.password = user.password;
			edit.privilege = user.privilege;
			edit.remote_access = user.remote_access;
			edit.auto_connect = "Yes";
			edit.auto_connect_name = con.name;
			
			log.info("Editing user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(edit)
			.put(getUri())
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
			
			log.info("Check edited user in rest");
			
			
			UsersConfig.GetUser getUser = config.new GetUser();
			getUser.usernames  = new String[1];
			getUser.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo(user.privilege))
			.body("message.users[0].auto_connect", equalTo("Yes"))
			.body("message.users[0].auto_connect_name", equalTo(con.name))
			.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			log.info("Check users in boxilla");
			String [] valuestoCheck = convertToStandard("Administrator",  "true",  con.name,  "true");
			String[] userDetails = users.getUserDetails(driver, user.username);
			Assert.assertTrue(userDetails[0].equals(user.username), "Username from table does not match. Expected:" + user.username + " , Actual:" + userDetails[0]);
			Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
			Assert.assertTrue(userDetails[2].equals("Yes"), "Auto connect from table does not match. Expected:" + "Yes" + " , Actual:" + userDetails[2]);
			if(userDetails[3].equals("-")) {
				Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
			}else {
				Assert.assertTrue(valuestoCheck[2].equals(con.name), "Autoconnect name did not match. Expected:" + con.name + ", Actual:" + userDetails[3]);
			}
			
			Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
			
		}
		
		
		@Test
		public void test07_editUserAutoConnectName() throws InterruptedException {
			CreateKvmConnectionsConfig.CreateConnection con2 = createConConfig.new CreateConnection();
			con2.name = "EditAutoConName";
			con2.group  = "ConnectViaTx";
			con2.connection_type = "Private";
			con2.host = txIp;
			con2.audio = "Yes";
			con2.extended_desktop = "Yes";
			con2.persistent = "Yes";
			con2.view_only = "Yes";
			con2.usb_redirection = "Yes";
			con2.cmode = "10";
			con2.zone = "";
			log.info("Creating connection to be used as auto con");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(con2)
			.post(createConConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(201).extract().response();
			
			SaveResponseStatistics(createConConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);
			
			
			CreateKvmConnectionsConfig.CreateConnection con = createConConfig.new CreateConnection();
			con.name = "test07_editCon";
			con.group  = "ConnectViaTx";
			con.connection_type = "Private";
			con.host = txIp;
			con.audio = "Yes";
			con.extended_desktop = "Yes";
			con.persistent = "Yes";
			con.view_only = "Yes";
			con.usb_redirection = "Yes";
			con.cmode = "10";
			con.zone = "";
			log.info("Creating connection to be used as auto con");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(con)
			.post(createConConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(201).extract().response();
			
			SaveResponseStatistics(createConConfig.getUri(boxillaManager), REQUEST_TYPE.POST, response);
			
			log.info("Checking connection through REST");
			String json = "{ \"connection_names\": [\"" + con.name + "\"]}";
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(json)
			.when().contentType(ContentType.JSON)
			.get(createConConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(connectionConfig.getConnectionName(0), equalTo(con.name))
			.body("message.connections[0].host", equalTo(txIp))
			.body("message.connections[0].connection_type", equalTo(con.connection_type))
			.body("message.connections[0].view_only", equalTo(con.view_only))
			.body("message.connections[0].group", equalTo("ConnectViaTx"))
			.body("message.connections[0].extended_desktop", equalTo(con.extended_desktop))
			.body("message.connections[0].audio", equalTo(con.audio))
			.body("message.connections[0].persistent", equalTo(con.persistent))
			.body("message.connections[0].usb_redirection", equalTo(con.usb_redirection))
			.body("message.connections[0].config", equalTo("Unique")).extract().response();
			
			SaveResponseStatistics(createConConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			log.info("Creating auto connect no user");
			UserCreation user = new UserCreation();
			user.username = "test07Edit";
			user.password = password;
			user.privilege = "PowerUser";
			user.remote_access = "Yes";
			user.auto_connect = "Yes";
			user.auto_connect_name = con.name;

			log.info("Creating user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(user)
			.post(getUri())
			.then().assertThat().statusCode(201)
			.body("message", equalTo("Created user " + user.username + ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
			
			log.info("Checking user in rest");
			UsersConfig.GetUser getUser2 = config.new GetUser();
			getUser2.usernames  = new String[1];
			getUser2.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser2)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo(user.privilege))
			.body("message.users[0].auto_connect", equalTo(user.auto_connect))
			.body("message.users[0].auto_connect_name", equalTo(user.auto_connect_name))
			.body("message.users[0].remote_access", equalTo(user.remote_access)).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			
			UserEdit edit = new UserEdit();
			edit.username = user.username;
			edit.password = user.password;
			edit.privilege = user.privilege;
			edit.remote_access = user.remote_access;
			edit.auto_connect = "Yes";
			edit.auto_connect_name = con2.name;
			
			log.info("Editing user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(edit)
			.put(getUri())
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
			
			log.info("Check edited user in rest");
			
			
			UsersConfig.GetUser getUser = config.new GetUser();
			getUser.usernames  = new String[1];
			getUser.usernames[0] = user.username;

			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(getUser)
			.when().contentType(ContentType.JSON)
			.get(getUri())
			.then().assertThat().statusCode(200)
			.body("message.users[0].username", equalTo(user.username))
			.body("message.users[0].privilege", equalTo(user.privilege))
			.body("message.users[0].auto_connect", equalTo("Yes"))
			.body("message.users[0].auto_connect_name", equalTo(con2.name))
			.body("message.users[0].remote_access", equalTo("Yes")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
			
			log.info("Check users in boxilla");
			String [] valuestoCheck = convertToStandard("Administrator",  "true",  con2.name,  "true");
			String[] userDetails = users.getUserDetails(driver, user.username);
			Assert.assertTrue(userDetails[0].equals(user.username), "Username from table does not match. Expected:" + user.username + " , Actual:" + userDetails[0]);
			Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
			Assert.assertTrue(userDetails[2].equals("Yes"), "Auto connect from table does not match. Expected:" + "Yes" + " , Actual:" + userDetails[2]);
			if(userDetails[3].equals("-")) {
				Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
			}else {
				Assert.assertTrue(valuestoCheck[2].equals(con2.name), "Autoconnect name did not match. Expected:" + con2.name + ", Actual:" + userDetails[3]);
			}
			
			Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
			
		}
		
	@Test
	public void test08_editUsername() throws InterruptedException {
		log.info("Creating user");
		UserCreation user = new UserCreation();
		user.username = "test08Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		log.info("Checking user in rest");
		UsersConfig.GetUser getUser2 = config.new GetUser();
		getUser2.usernames  = new String[1];
		getUser2.usernames[0] = user.username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser2)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(user.username))
		.body("message.users[0].privilege", equalTo(user.privilege))
		.body("message.users[0].auto_connect", equalTo(user.auto_connect))
		.body("message.users[0].auto_connect_name", equalTo(user.auto_connect_name))
		.body("message.users[0].remote_access", equalTo(user.remote_access)).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = user.remote_access;
		edit.auto_connect = user.auto_connect;
		edit.auto_connect_name = null;
		edit.new_username = "test08Edit_new";
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated profile/properties for user " + user.username +  ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		
		log.info("Check edited user in rest");
		
		
		UsersConfig.GetUser getUser = config.new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = edit.new_username;

		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(edit.new_username))
		.body("message.users[0].privilege", equalTo(user.privilege))
		.body("message.users[0].auto_connect", equalTo(user.auto_connect))
		.body("message.users[0].auto_connect_name", equalTo(user.auto_connect_name))
		.body("message.users[0].remote_access", equalTo(user.remote_access)).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		log.info("Check users in boxilla");
		String [] valuestoCheck = convertToStandard("Administrator",  "true",  null,  "true");
		String[] userDetails = users.getUserDetails(driver, edit.new_username);
		Assert.assertTrue(userDetails[0].equals(edit.new_username), "Username from table does not match. Expected:" + edit.new_username + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "Yes" + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" + userDetails[3] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
		

	}
		
	//@Test
	public void test09_editUsernameDuplicate() {
		log.info("Creating user");
		UserCreation user = new UserCreation();
		user.username = "test09Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
//				
		user.username = "test09EditDup";
		
		log.info("Creating second user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		
		
		UserEdit edit = new UserEdit();
		edit.username = "test09Edit";
		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = "No";
		edit.auto_connect = user.auto_connect;
		edit.auto_connect_name = null;
		edit.new_username = "test09EditDup";
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test10_editAutoConnectNameAutoConDisabled() {
		UserCreation user = new UserCreation();
		user.username = "test10Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = "No";
		edit.auto_connect = user.auto_connect;
		edit.auto_connect_name = "testCon";
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: auto_connect_name 'testCon' cannot be set when auto_connect is disabled.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	
		@Test
		public void test11_editAutoConnectNameInvalid() {
			UserCreation user = new UserCreation();
			user.username = "test11Edit";
			user.password = password;
			user.privilege = "PowerUser";
			user.remote_access = "Yes";
			user.auto_connect = "No";
			user.auto_connect_name = null;

			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(user)
			.post(getUri())
			.then().assertThat().statusCode(201)
			.body("message", equalTo("Created user " + user.username + ".")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
			
			UserEdit edit = new UserEdit();
			edit.username = user.username;
			edit.password = user.password;
			edit.privilege = user.privilege;
			edit.remote_access = "No";
			edit.auto_connect = "Yes";
			edit.auto_connect_name = "testCon";
			
			log.info("Editing user");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(edit)
			.put(getUri())
			.then().assertThat().statusCode(400)
			.body("message", equalTo("Invalid parameter: auto_connect_name 'testCon' does not point to any existing KVM connections.")).extract().response();
			
			SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		}
	
	@Test
	public void test12_editUserNotExist() {
		UserEdit edit = new UserEdit();
		edit.username = "test12Edit";
		edit.password = "test";
		edit.privilege = "PowerUser";
		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		log.info("Editing user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("User test12Edit does not exist.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test13_missingParametersUsername() {
		UserCreation user = new UserCreation();
		user.username = "test13Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();

		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"username\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test14_missingParametersPassword() {
		UserCreation user = new UserCreation();
		user.username = "test14Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;

		edit.privilege = user.privilege;
		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"password\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test15_missingParametersPrivilege() {
		UserCreation user = new UserCreation();
		user.username = "test15Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;

		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"privilege\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test16_missingParametersRemoteAccess() {
		UserCreation user = new UserCreation();
		user.username = "test16Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = user.privilege;
	
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"remote_access\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test17_missingParametersAutoConnect() {
		UserCreation user = new UserCreation();
		user.username = "test17Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "No";
		user.auto_connect_name = null;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = user.remote_access;

		edit.auto_connect_name = null;
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"auto_connect\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	//@Test
	public void test18_missingParametersAutoConnectName() {
		UserCreation user = new UserCreation();
		user.username = "test18Edit";
		user.password = password;
		user.privilege = "PowerUser";
		user.remote_access = "Yes";
		user.auto_connect = "Yes";
		user.auto_connect_name = "Test_TX_Emerald_default";

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		UserEdit edit = new UserEdit();
		edit.username = user.username;
		edit.password = user.password;
		edit.privilege = user.privilege;
		edit.remote_access = user.remote_access;
		edit.auto_connect = user.auto_connect;
	
		
		log.info("Editing user");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"auto_connect_name\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test19_editBoxillaCreatedUser() throws InterruptedException {
		users.masterCreateUser(driver, "test19Edit", "test", "false", "false", "admin", "false", "", "false");
		log.info("Editing user");
		UserEdit edit = new UserEdit();
		edit.username = "test19Edit";
		edit.password = "test";
		edit.privilege = "PowerUser";
		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(200).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
		
		log.info("Checking edited user in Boxilla");
		String [] valuestoCheck = convertToStandard("power",  "false",  null,  "false");
		String[] userDetails = users.getUserDetails(driver, edit.username);
		Assert.assertTrue(userDetails[0].equals(edit.username), "Username from table does not match. Expected:" + edit.username + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals("PowerUser"), "Privilege from table does not match. Expected:" + "PowerUser" + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals("No"), "Auto connect from table does not match. Expected:" + "Yes" + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" + userDetails[3] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(edit.remote_access.equals(userDetails[4]), "Remote access from table did not match. Expected:" + edit.remote_access + ", Actual:" + userDetails[4]);
	}
	
	//@Test
	public void test20_editAdminUsser() {
		UserEdit edit = new UserEdit();
		edit.username = "admin";
		edit.password = "admin";
		edit.privilege = "PowerUser";
		edit.remote_access = "No";
		edit.auto_connect = "No";
		edit.auto_connect_name = null;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser,boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri())
		.then().assertThat().statusCode(400).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.PUT, response);
	}
	
	private String[] convertToStandard(String privilege, String isAutoConnect, String autoConnectName, String isRemote) {
		String [] returnArray = new String[4];
		String priv = "";
		if(privilege.equals("admin")) {
			priv = "Administrator";
		}else if(privilege.equals("power")) {
			priv = "PowerUser";
		}else if(privilege.equals("general")) {
			priv = "User";
		}
		
		String autoCon = "";
		String autoConName = "";
		if(isAutoConnect.equals("true")) {
			autoCon = "Yes";
			autoConName = autoConnectName;
		}else {
			autoCon = "No";
			autoConName = null;
		}
		String remote = "";
		if(isRemote.equals("true")) {
			remote = "Yes";
		}else {
			remote = "No";
		}
		returnArray[0] = priv;
		returnArray[1] = autoCon;
		returnArray[2] = autoConName;
		returnArray[3] = remote;
		return returnArray;
	}
}
