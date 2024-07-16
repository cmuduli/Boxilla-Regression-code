package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.HttpConnections;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Connections;

public class LoginUser extends StartupTestCase {

	final static Logger log = Logger.getLogger(LoginUser.class);
	private UsersMethods user = new UsersMethods();
	private DevicesMethods devices = new DevicesMethods();
	private ConnectionsMethods connection = new ConnectionsMethods();
	private SystemMethods sysMethods = new SystemMethods();

	private String adIp = "10.211.129.213";
	private String adPort = "389";
	private String adDomain = "limerick.lab.autotest.com";
	private String adUsername = "Administrator";
//	private String adPassword = "Blackbox31!!";
	private String adPassword = "Blackbox1234!!";

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}

	private void logUserOut(String ip, String deviceName, String restUser, String restPassword)
			throws InterruptedException {
		String x = "x";
		System.out.print(x);
		Response r = given().auth().preemptive().basic(restUser, restPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.get("https://" + ip + ":8888/control/user").then().assertThat().statusCode(200).extract().response();

		x = r.jsonPath().getString("user_name");
		System.out.print("User:" + x);
		Thread.sleep(20000);

		if (x != null && !x.equals("")) {
			log.info("Logging out user:" + x);
			Logout l = new Logout();
			l.username = x;
			l.rx_list = new String[1];
			l.rx_list[0] = deviceName;
			l.forced = "No";
			Thread.sleep(3000);
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
					.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(l)
					.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout").then().assertThat().statusCode(200)
					.extract().response();

			SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST,
					response);
			log.info("Successfully logged out user:" + x);
		}
	}

	public class Logout {
		public String username;
		public String[] rx_list;
		public String forced;
	}

	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}

	@Test
	public void test01_loginUserSingleReceiver() throws InterruptedException {
		String username = "loginTest01";
		String password = "mypassword";
		user.masterCreateUser(driver, username, password, "false", "false", "admin", "false", "", "false");
		devices.recreateCloudData(rxIpDual, "");

		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		Thread.sleep(1500);
		logUserOut(rxIpDual, rxDual.getDeviceName(), restuser, restPassword);
		// log user in
		Login login = new Login();
		login.username = username;
		login.password = password;
		login.rx_list = new String[1];
		login.rx_list[0] = rxDual.getDeviceName();
		login.forced = "No";
		log.info("NB Rest login details:" + boxillaRestUser + "/" + boxillaRestPassword);
		log.info("Logging in user:" + username);
		Thread.sleep(3000);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		log.info("User " + username + " successfully logged in");
		Thread.sleep(10000);
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIpDual);
		log.info("User name is "+loggedInUser);
		Assert.assertTrue(loggedInUser.equals(username), "The user was not logged in");
		
		logUserOut(rxIpDual, rxDual.getDeviceName(), restuser, restPassword);

		// clean up
		user.deleteUser(username, driver);
	}

	@Test
	public void test02_loginUserMultipleReceivers() throws InterruptedException {

		String username = "loginTest02";
		String password = "mypassword";
		user.masterCreateUser(driver, username, password, "false", "false", "admin", "false", "", "false");
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);

		//logUserOut(rxIp, rxEmerald.getDeviceName(), restuser, restPassword);
		logUserOut(rxIp, rxSingle.getDeviceName(), restuser, restPassword);
		devices.recreateCloudData(rxIpDual, "");
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		Thread.sleep(1500);

		logUserOut(rxIpDual, rxDual.getDeviceName(), restuser, restPassword);
		// log user in
		Login login = new Login();
		login.username = username;
		login.password = password;
		login.rx_list = new String[2];
		login.rx_list[0] = rxSingle.getDeviceName();
		//login.rx_list[0] = rxEmerald.getDeviceName();
		login.rx_list[1] = rxDual.getDeviceName();
		
		login.forced = "No";
		log.info("Logging in user:" + username);
		Thread.sleep(30000);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);
		Thread.sleep(10000);

		log.info("User " + username + " successfully logged in");
		
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIp);
		System.out.println(loggedInUser);
		Thread.sleep(5000);
		Assert.assertTrue(loggedInUser.equalsIgnoreCase(username), "The user was not logged in");
//		 loggedInUser = getLoggedInUser(rxIpDual);
		String loggedInUser1 = getLoggedInUser(rxIpDual);
		System.out.println(loggedInUser1);
		Assert.assertTrue(loggedInUser1.equalsIgnoreCase(username), "The user was not logged in");
		// clean up
		logUserOut(rxIp, rxSingle.getDeviceName(), restuser, restPassword);
		logUserOut(rxIpDual, rxDual.getDeviceName(), restuser, restPassword);
		user.deleteUser(username, driver);
	}

	@Test
	public void test03_forceLoginSingleReceiver() throws InterruptedException {
		String username = "test03Login";
		String userPassword = "thepassword";
		String connectionName = "test03Login";
		user.masterCreateUser(driver, username, userPassword, "false", "false", "admin", "false", "", "false");
		connection.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false",
				txIp, driver);
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);

		// launch connection
		String[] connectionSources = { connectionName };
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, singleRxName)
				.isDisplayed();
		// Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + singleRxName);
		Assert.assertTrue(isConnection,
				"Connection between " + connectionName + " and " + singleRxName + " is not displayed");

		Login login = new Login();
		login.username = username;
		login.password = userPassword;
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		log.info("User " + username + " successfully logged in");
		Thread.sleep(5000);
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedInUser.equals(username), "The user was not logged in");
	}

	@Test
	public void test04_forceLoginMultipleReceiver() throws InterruptedException {
		String username = "test04Login";
		String userPassword = "thepassword";
		String connectionName1 = "test04Login1";
		String connectionName2 = "test04Login2";
		user.masterCreateUser(driver, username, userPassword, "false", "false", "admin", "false", "", "false");
		connection.createMasterConnection(connectionName1, "tx", "private", "false", "false", "false", "false", "false",
				txIp, driver);
		connection.createMasterConnection(connectionName2, "tx", "private", "false", "false", "false", "false", "false",
				txIpDual, driver);

		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		devices.recreateCloudData(rxIpDual, "");
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		// launch connection
		String[] connectionSources = { connectionName1 };
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName1, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		driver.navigate().refresh();
		Thread.sleep(10000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName1, singleRxName)
				.isDisplayed();
		// Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName1 + " and " + singleRxName);
		Assert.assertTrue(isConnection,
				"Connection between " + connectionName1 + " and " + singleRxName + " is not displayed");

		// connection2
		String[] connectionSources2 = { connectionName2 };
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources2);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName2, dualRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection2 = Connections.singleSourceDestinationCheck(driver, connectionName2, dualRxName)
				.isDisplayed();
		// Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName2 + " and " + dualRxName);
		Assert.assertTrue(isConnection2,
				"Connection between " + connectionName2 + " and " + dualRxName + " is not displayed");

		Login login = new Login();
		login.username = username;
		login.password = userPassword;
		login.rx_list = new String[2];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.rx_list[1] = rxDual.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		log.info("User " + username + " successfully logged in");
		Thread.sleep(5000);
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedInUser.equals(username), "The user was not logged in");
		loggedInUser = getLoggedInUser(rxIpDual);
		Assert.assertTrue(loggedInUser.equals(username), "The user was not logged in");

	}

	@Test
	public void test05_invalidUser() {
		Login login = new Login();
		String username = "invalid";
		login.username = username;
		login.password = "pass";
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(400)
				.body("message", equalTo("User " + username + " does not exist.")).extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

	}

	@Test
	public void test06_invalidDevice() throws InterruptedException {
		String username = "test06Login";
		String userPassword = "thepassword";

		user.masterCreateUser(driver, username, userPassword, "false", "false", "admin", "false", "", "false");

		Login login = new Login();
		login.username = username;
		login.password = userPassword;
		login.rx_list = new String[1];
		login.rx_list[0] = "invalidDevice";
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(400)
				.body("message", equalTo("The following receiver does not exist: [\"" + login.rx_list[0] + "\"]."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		user.deleteUser(username, driver);
	}

	@Test
	public void test07_invalidPassword() throws InterruptedException {
		String username = "test07Login";
		String userPassword = "thepassword";

		user.masterCreateUser(driver, username, userPassword, "false", "false", "admin", "false", "", "false");
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Login login = new Login();
		login.username = username;
		login.password = "invalidPass";
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(401)
//				.body("message", equalTo("Operation is not authorized due to invalid login credentials.")).extract()
				.body("message", equalTo("Invalid username or password.")).extract()
				.response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		// check and make sure user was not actually logged in
		String loggedInUser = getLoggedInUser(rxIp);

		Assert.assertFalse(loggedInUser.equals(username), "The user was logged in when it should not have been");

		user.deleteUser(username, driver);
	}

	@Test
	public void test08_userAlreadyLoggedIn() throws InterruptedException {
		String username1 = "test08_user1";
		String username2 = "test08_user2";
		String password = "mypassword";

		user.masterCreateUser(driver, username1, password, "false", "false", "admin", "false", "", "false");
		user.masterCreateUser(driver, username2, password, "false", "false", "admin", "false", "", "false");
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Thread.sleep(1500);
//		logUserOut(rxIp, rxEmerald.getDeviceName(), restuser, restPassword);
		logUserOut(rxIp, rxSingle.getDeviceName(), restuser, restPassword);
		// log user in
		Login login = new Login();
		login.username = username1;
		login.password = password;
		login.rx_list = new String[1];
		login.rx_list[0] = rxSingle.getDeviceName();
		login.forced = "No";
		log.info("Logging in user:" + username1);
		Thread.sleep(30000);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username1 + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		log.info("User " + username1 + " successfully logged in");
		Thread.sleep(6000);
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIp);
		Thread.sleep(6000);
		Assert.assertTrue(loggedInUser.equals(username1), "The user was not logged in");

		log.info("Attempting to log in second user");

		login.username = username2;
		login.password = password;
		login.rx_list = new String[1];
		login.rx_list[0] = rxSingle.getDeviceName();
		login.forced = "No";
		log.info("Logging in user:" + username2);
		Thread.sleep(30000);
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(403)
				.body("message",
						equalTo("Another user is logged in target receiver [\"" + rxSingle.getDeviceName() + "\"]."))
				.extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);
		
		log.info("Checking that user1 is still logged in ");
		
		Thread.sleep(10000);
		String isUserStillLoggedIn = getLoggedInUser(rxIp);
		Assert.assertTrue(isUserStillLoggedIn.equals(username1), "Correct user was not logged in");

		logUserOut(rxIp, rxSingle.getDeviceName(), restuser, restPassword);
		
		user.deleteUser(username1, driver);
		user.deleteUser(username2, driver);
	}

//	@Test
	public void test09_loginAdUser() throws InterruptedException {
		sysMethods.turnOnActiveDirectorySupport(driver);
		sysMethods.enterActiveDirectorySettings(adIp, adPort, adDomain, adUsername, adPassword, driver);
		String username = "clark";
		logUserOut(rxIp, rxEmerald.getDeviceName(), restuser, restPassword);
		// log user in
		Login login = new Login();
		login.username = username;
		login.password = "D@ughnuts123";
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "No";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		Thread.sleep(5000);
		String loggedInUser = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedInUser.equals(username), "AD user was not logged in");
		sysMethods.turnOffActiveDirectorySupport(driver);
	}

//	@Test
	public void test10_loginAdUserForce() throws InterruptedException {
		sysMethods.turnOnActiveDirectorySupport(driver);
		sysMethods.enterActiveDirectorySettings(adIp, adPort, adDomain, adUsername, adPassword, driver);

		String username = "clark";
		String userPassword = "D@ughnuts123";
		String connectionName = "test10Login";
		connection.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false",
				txIp, driver);

		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);

		// launch connection
		String[] connectionSources = { connectionName };
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, singleRxName)
				.isDisplayed();
		// Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + singleRxName);
		Assert.assertTrue(isConnection,
				"Connection between " + connectionName + " and " + singleRxName + " is not displayed");

		Login login = new Login();
		login.username = username;
		login.password = userPassword;
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(login)
				.post("https://" + boxillaManager + "/bxa-api/users/kvm/login").then().assertThat().statusCode(200)
				.body("message", equalTo("User " + username + " has successfully logged in to the target receivers."))
				.extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);

		log.info("User " + username + " successfully logged in");
		Thread.sleep(10000);
		// confirm user is logged in
		String loggedInUser = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedInUser.equals(username), "The user was not logged in");

		sysMethods.turnOffActiveDirectorySupport(driver);
	}

	// request to appliance-8888
	private String getLoggedInUser(String deviceIp) {
		String loggedInUser = "";
		Response r = given().auth().preemptive().basic(restuser, restPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.get("https://" + deviceIp + ":8888/control/user").then().assertThat().statusCode(200).extract()
				.response();

		loggedInUser = r.jsonPath().getString("user_name");
		log.info("Logged in user:" + loggedInUser);
		return loggedInUser;
	}
}
