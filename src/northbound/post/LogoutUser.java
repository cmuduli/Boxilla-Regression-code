package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;

import io.restassured.response.Response;
import methods.DevicesMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.post.LoginUser.Login;
import northbound.post.LoginUser.Logout;

public class LogoutUser extends StartupTestCase {

	private UsersMethods user = new UsersMethods();
	private DevicesMethods devices = new DevicesMethods();
	private SystemMethods sysMethods = new SystemMethods();
	final static Logger log = Logger.getLogger(LogoutUser.class);
	
	private String adIp = "10.211.129.213";
	private String adPort = "389";
	private String adDomain = "autotest.com";
	private String adUsername = "Administrator";
//	private String adPassword = "Blackbox2023!!";
	private String adPassword = "Blackbox1234!!";
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	private String getLoggedInUser(String deviceIp) {
		String loggedInUser = "";
		Response r = 	given().auth().preemptive().basic(restuser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.get("https://" + deviceIp + ":8888/control/user")
				.then().assertThat().statusCode(200)
				.extract().response();
			
		loggedInUser = r.jsonPath().getString("user_name");
		log.info("Logged in user:" + loggedInUser);
		return loggedInUser;
	}
	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
	public class Logout {
		public String username;
		public String[] rx_list;
		public String forced;
	}
	
	private void logUserOut(String ip, String deviceName) throws InterruptedException {
		String x = "x";
		System.out.print(x);
	Response r = 	given().auth().preemptive().basic(restuser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get("https://" + ip + ":8888/control/user")
		.then().assertThat().statusCode(200)
		.extract().response();
	
	x = r.jsonPath().getString("user_name");
	System.out.print("User:" + x);
	Thread.sleep(20000);
	
	if(x != null && !x.equals("")) {
		log.info("Logging out user:" + x);
		Logout l = new Logout();
		l.username = x;
		l.rx_list = new String[1];
		l.rx_list[0] = deviceName;
		l.forced = "No";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(l)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);

		log.info("Successfully logged out user:" + x);
	}
	}
	
	@Test
	public void test01_logoutSingleDeviceNoForce() throws InterruptedException {
		String username = "test01_logout";
		String password = "mypassword";
		user.masterCreateUser(driver, username, password, "false", "false", "false", "false", "", "false");
		
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		//log out any autologin user first
		logUserOut(rxIp, rxEmerald.getDeviceName());
		
		Login login = new Login();
		login.username = username;
		login.password = password;
		String[] list = {rxEmerald.getDeviceName()};
		login.rx_list = list;
		login.forced = "Yes";
		
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged in to the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);
		
		log.info("User " + username + " successfully logged in");
		Thread.sleep(20000);
		String loggedIn = getLoggedInUser(rxIp);
		
		Assert.assertTrue(loggedIn.equals(username), "The correct user was not logged in. Expected:" + username + ", Actual:" + loggedIn );
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "No";
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
		String loggedIn2 = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedIn2.equals(""), "The correct user was not logged in. Expected:" + " no user" + ", Actual:" + loggedIn2 );
		user.deleteUser(username, driver);
	}
	//already commented
	//@Test
	public void test02_logoutSingleDeviceForce() throws InterruptedException {
		String username = "test02_logout";
		String password = "mypassword";
		String connectionName = "";
		
		user.masterCreateUser(driver, username, password, "false", "false", "false", "false", "", "false");
		
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		//log out any autologin user first
		logUserOut(rxIp, rxEmerald.getDeviceName());
		
		Login login = new Login();
		login.username = username;
		login.password = password;
		String[] list = {rxEmerald.getDeviceName()};
		login.rx_list = list;
		login.forced = "Yes";
		
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged in to the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		
		log.info("User " + username + " successfully logged in");
		Thread.sleep(20000);
		String loggedIn = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedIn.equals(username), "The correct user was not logged in. Expected:" + username + ", Actual:" + loggedIn );
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "Yes";
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
		String loggedIn2 = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedIn2.equals(""), "The correct user was not logged in. Expected:" + " no user" + ", Actual:" + loggedIn2 );
		user.deleteUser(username, driver);
	}
	
	@Test
	public void test03_logoutMultipleDeviceNoForce() throws InterruptedException {
		String username = "test03_logout";
		String password = "mypassword";
		user.masterCreateUser(driver, username, password, "false", "false", "false", "false", "", "false");
		
		devices.recreateCloudData(rxIp, "");
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		//log out any autologin user first
		logUserOut(rxIp, rxEmerald.getDeviceName());
		
		devices.recreateCloudData(rxIpDual, "");
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		//log out any autologin user first
		logUserOut(rxIp, rxDual.getDeviceName());
		
		Login login = new Login();
		login.username = username;
		login.password = password;
		String[] list = {rxEmerald.getDeviceName(), rxDual.getDeviceName()};
		login.rx_list = list;
		login.forced = "Yes";
		
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged in to the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		
		log.info("User " + username + " successfully logged in");
		Thread.sleep(40000);
		String loggedIn = getLoggedInUser(rxIp);
		String loggedInDual = getLoggedInUser(rxIpDual);
		Assert.assertTrue(loggedIn.equals(username), "The correct user was not logged in. Expected:" + username + ", Actual:" + loggedIn );
		Assert.assertTrue(loggedInDual.equals(username), "The correct user was not logged in. Expected:" + username + ", Actual:" + loggedInDual );
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[2];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.rx_list[1] = rxDual.getDeviceName();
		logout.forced = "No";
		
		 response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
		String loggedIn2 = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedIn2.equals(""), "The correct user was not logged in. Expected:" + " no user" + ", Actual:" + loggedIn2 );
		
		String loggedIn3 = getLoggedInUser(rxIpDual);
		Assert.assertTrue(loggedIn3.equals(""), "The correct user was not logged in. Expected:" + " no user" + ", Actual:" + loggedIn3 );
		
		
		user.deleteUser(username, driver);
	}
	// Already commented
	//@Test
	public void test04_logoutMultipleDeviceNoForce() {
		Logout logout = new Logout();
		logout.username = "admin";
		logout.rx_list = new String[2];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.rx_list[1] = rxDual.getDeviceName();
		logout.forced = "No";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User admin has successfully logged in to the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
	}
	@Test
	public void test05_invalidDevice() {
		Logout logout = new Logout();
		logout.username = "admin";
		logout.rx_list = new String[1];
		logout.rx_list[0] = "Invalid Device";
		logout.forced = "No";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following receiver does not exist: [\"Invalid Device\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
	}

//	@Test
	public void test06_invalidUser() {
		Logout logout = new Logout();
		logout.username = "Invalid";
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "No";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("User Invalid does not exist.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test07_invalidForced() {
		Logout logout = new Logout();
		logout.username = "admin";
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "false";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"forced\"=>\"false\"}.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test08_userNotLoggedIn() throws InterruptedException {
		String username = "test08NotLoggedIn";
		user.masterCreateUser(driver, username, "test", "false", "false", "admin", "false", "", "false");
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "No";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(403)
		.body("message", equalTo("User " + username + " is not logged in to one or more of the following receivers: [\"" + rxEmerald.getDeviceName() + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
	}
	
	@Test
	public void test09_logoutAdUser() throws InterruptedException {
		sysMethods.turnOnActiveDirectorySupport(driver);
		sysMethods.enterActiveDirectorySettings(adIp, adPort, adDomain, adUsername, adPassword, driver);
		String username = "clark";
		user.masterCreateUser(driver, username, "D@ughnuts123", "false", "false", "admin", "false", "", "false");
		logUserOut(rxIp, rxEmerald.getDeviceName());
		//log user in
		Login login = new Login();
		login.username = username;
		login.password = "D@ughnuts123";
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + username);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged in to the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/login", REQUEST_TYPE.POST, response);
		Thread.sleep(40000);
		String loggedInUser = getLoggedInUser(rxIp);
		System.out.println(loggedInUser);
		Assert.assertTrue(loggedInUser.equals(username), "AD user was not logged in");
		
		//logout
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "Yes";
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
		
		String loggedIn2 = getLoggedInUser(rxIp);
		Assert.assertTrue(loggedIn2.equals(""), "The correct user was not logged in. Expected:" + " no user" + ", Actual:" + loggedIn2 );
		user.deleteUser(username, driver);
		sysMethods.turnOffActiveDirectorySupport(driver);
	}
	
//	@Test
//	public void test09_offlineDevice() throws InterruptedException {
//		devices.interfacesDownUp(rxIp, "root", "barrow1admin_12", "eth0", "60");
//	}
	
}
