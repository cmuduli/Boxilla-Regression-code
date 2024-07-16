package northbound.post;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.SystemFacts;
import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

import java.io.IOException;
import java.lang.reflect.Method;

import static org.hamcrest.Matchers.contains;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;

public class CreateKvmUser extends StartupTestCase {

	private UsersMethods users = new UsersMethods();
	private ConnectionsMethods connections = new ConnectionsMethods();
	private String autoConnectCon = "createUserCon";
	private String dataFile = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\get\\usercreate2.txt";
	final static Logger log = Logger.getLogger(CreateKvmUser.class);
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/users/kvm";
	}
	 class GetUser {
		public String[] usernames;
	}
	class UserCreation {
		public String username;
		public String password;
		public String privilege;
		public String remote_access;
		public String auto_connect;
		public String auto_connect_name;
	}
	
	@DataProvider(name = "createUser")
	public Object[][] createUserData() throws IOException {
		return readData(dataFile);
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		//RestAssured.authentication = basic(boxillaRestUser, boxillaRestPassword);
	
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
	private String[] convert(String priv, String isAutoCon, String autoConName,  String isRemote) {
		String[] converted = new String[4];
		
		//privilege
		if(priv.equals("admin")) {
			converted[0] = "Administrator";
		}else if(priv.equals("power")) {
			converted[0] = "PowerUser";
		}else if(priv.equals("general")) {
			converted[0] = "User";
		}
		
		//autoconnect
		if(isAutoCon.equals("true")) {
			converted[1] = "Yes";
			converted[3] = autoConName;
		}else {
			converted[1] = "No";
			converted[3] = null;
		}
		
		//remote
		if(isRemote.equals("true")) {
			converted[2] = "Yes";
		}else {
			converted[2] = "No";
		}
		return converted;
	}

	@Test(dataProvider="createUser")
	public void test01_createUser(String name, String password, String isTemplate, String isActiveDir, String privilege, 
			String isAutoConnect, String autoConnectName, String isRemote) throws InterruptedException {
		UserCreation user = new UserCreation();
		
		String[] values = convert(privilege, isAutoConnect,autoConnectName, isRemote);
		
		user.username = name;
		user.password = password;
		user.privilege = values[0];
		user.remote_access = values[2];
		user.auto_connect = values[1];
		user.auto_connect_name = values[3];

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created user " + user.username + ".")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		log.info("Checking user creation using REST ");
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = name;
		String [] valuestoCheck = convertToStandard(privilege,  isAutoConnect,  autoConnectName,  isRemote);
		response =  given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(name))
		.body("message.users[0].privilege", equalTo(valuestoCheck[0]))
		.body("message.users[0].auto_connect", equalTo(valuestoCheck[1]))
		.body("message.users[0].auto_connect_name", equalTo(valuestoCheck[2]))
		.body("message.users[0].remote_access", equalTo(valuestoCheck[3])).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		log.info("Check users in boxilla");
		
		String[] userDetails = users.getUserDetails(driver, name);
		Assert.assertTrue(userDetails[0].equals(name), "Username from table does not match. Expected:" + name + " , Actual:" + userDetails[0]);
		Assert.assertTrue(userDetails[1].equals(valuestoCheck[0]), "Privilege from table does not match. Expected:" + valuestoCheck[0] + ", Actual:" + userDetails[1]);
		Assert.assertTrue(userDetails[2].equals(valuestoCheck[1]), "Auto connect from table does not match. Expected:" + valuestoCheck[1] + " , Actual:" + userDetails[2]);
		if(userDetails[3].equals("-")) {
			Assert.assertTrue(valuestoCheck[2] == null, "Autoconnect name did not match. Expected null, Actual:" + userDetails[3]);
		}else {
			Assert.assertTrue(valuestoCheck[2].equals(userDetails[3]), "Autoconnect name did not match. Expected:" +  valuestoCheck[2] + ", Actual:" + userDetails[3]);
		}
		
		Assert.assertTrue(valuestoCheck[3].equals(userDetails[4]), "Remote access from table did not match. Expected:" + valuestoCheck[3] + ", Actual:" + userDetails[4]);
	}
	
	@Test
	public void test02_createDuplicateUser() {
		String userName = "test02_createDuplicateUser";
		UserCreation user = new UserCreation();
		
		user.username = userName;
		user.password = "test";
		user.privilege = "Administrator";
		user.remote_access = "No";
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
		
		log.info("Checking user creation using REST ");
		GetUser getUser = new GetUser();
		getUser.usernames  = new String[1];
		getUser.usernames[0] = userName;
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(getUser)
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.users[0].username", equalTo(userName))
		.body("message.users[0].privilege", equalTo("Administrator"))
		.body("message.users[0].auto_connect", equalTo("No"))
		.body("message.users[0].auto_connect_name", equalTo(null))
		.body("message.users[0].remote_access", equalTo("No")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
		//create user with same name but different options
		user.remote_access = "Yes";
		user.privilege = "PowerUser";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Duplicate name received.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test03_autoConnectConNameNotConnection() {
		String userName = "test03_autoConnect";
		UserCreation user = new UserCreation();
		String fakeConnectionName = "invalidCon";
		user.username = userName;
		user.password = "test";
		user.privilege = "Administrator";
		user.remote_access = "No";
		user.auto_connect = "Yes";
		user.auto_connect_name = fakeConnectionName;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: auto_connect_name '" + fakeConnectionName + "' does not point to any existing KVM connections.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test04_autoConnectOffConNamePassed() {
		String userName = "test03_autoConnect";
		UserCreation user = new UserCreation();
	
		user.username = userName;
		user.password = "test";
		user.privilege = "Administrator";
		user.remote_access = "No";
		user.auto_connect = "No";
		user.auto_connect_name = autoConnectCon;

		log.info("Creating user");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(user)
		.post(getUri())
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: auto_connect_name '" + autoConnectCon + "' cannot be set when auto_connect is disabled.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);

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
	
//	@AfterClass
	public void afterclass() 
	{
		log.info("Deleting all users");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all users.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
	}
	
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	//@Parameters({ "browser" })
//	public void login( Method method) {
//		
//	}
//	
//	/**
//	 * Tests in the class do not use the browser so 
//	 * this superclass method gets overridden and logout removed.
//	 * Also no screen shot is taken on fail
//	 */
//	@Override
//	@AfterMethod(alwaysRun = true)
//	public void logout(ITestResult result) {
//		log.info("********* @ After Method Started ************");
//		// Taking screen shot on failure
//		//String url = "https://" + boxillaManager + "/";
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//print result
//		if(ITestResult.FAILURE == result.getStatus())
//			log.info(result.getName() + " :FAIL");
//		
//		if(ITestResult.SKIP == result.getStatus())
//			log.info(result.getName() + " :SKIP");
//		
//		if(ITestResult.SUCCESS == result.getStatus())
//			log.info(result.getName() + " :PASS");
//		
//		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
//			//collectLogs(result);
//			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
//			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
//		}
//	}
	
}
