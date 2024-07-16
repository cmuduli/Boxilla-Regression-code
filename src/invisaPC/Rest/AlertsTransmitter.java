package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.Rest.Alerts.Logout;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Connections;
import objects.Users;
import testNG.Utilities;

/**
 * Class that contains all the tests for appliance REST API alerts
 */

public class AlertsTransmitter extends StartupTestCase {
	

	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private UsersMethods usrMethods=new UsersMethods();
	private String jsonReturned;
	private String connection = "alertsTransmitter";

	final static Logger log = Logger.getLogger(Alerts.class);
	
	
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest","emerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = given().header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		
	}
	
	/**
	 * Will check that the ID field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test02_verifyId() {
		log.info("***** test02_verifyId *****");
		Assert.assertTrue(jsonReturned.contains("\"id\":"),
				"Check for \"id\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that the mac address returned is the same as the device MAC address
	 */
	@Test(groups = {"rest", "smoke","emerald"})
	public void test03_verifyMacAddress() {
		log.info("***** test03_verifyMacAddress *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.mac[0]", equalTo(txSingle.getMac().toLowerCase()));
	}
	
	/**
	 * Will check that the IP address returned is the same as the device default IP address
	 */
	@Test(groups = {"rest","emerald"})
	public void test04_verifyIpAddress() {
		log.info("***** test04_verifyIpAddress *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.ip_address[0]", equalTo(txIp));
	}
	
	/**
	 * Will check that the source returned is the same as the connection source
	 */
	@Test(groups = {"rest","emerald"})
	public void test05_verifySource() {
		log.info("***** test05_verifySource *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.source[0]", equalTo("appliance"));
	}
	
	/**
	 * Will check that the alert category returned is info
	 */
	@Test(groups = {"rest","emerald"})
	public void test06_verifyAlertCategory() {
		log.info("***** test06_verifyAlertCategory *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_category[0]", equalTo("Info"));
	}
	
	/**
	 * Will check that the alert condition returned is Operation
	 */
	@Test(groups = {"rest","emerald"})
	public void test07_verifyAlertCondition() {
		log.info("***** test07_verifyAlertCondition *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_condition[0]", equalTo("Operation"));
	}
	
	/**
	 * Will check that the alert component returned is User
	 */
	@Test(groups = {"rest","emerald"})
	public void test08_verifyAlertComponent() {
		log.info("***** test08_verifyAlertComponent *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_component[0]", equalTo("Connection"));
	}
	
	/**
	 * Will check that the alert component returned is Auto_Login
	 */
	@Test(groups = {"rest","emerald"})
	public void test09_verifyAlertCommand() {
		log.info("***** test09_verifyAlertCommand *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_command[0]", equalTo("Start"));
	}
	
	/**
	 * Will check that the alert component returned is Success
	 */
	@Test(groups = {"rest","emerald"})
	public void test10_verifyAlertStatus() {
		log.info("***** test10_verifyAlertStatus *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_status[0]", equalTo("Success"));
	}
	
	/**
	 * Will check that the alert description returned is Success
	 */
	@Test(groups = {"rest","emerald"})
	public void test11_verifyAlertDescription() {
		log.info("***** test11_verifyAlertDescription *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/alerts")
		.then().assertThat().statusCode(200)
		.body("kvm_alerts.alert_description[0]", equalTo("New Connection"));
	}
	
	/**
	 * Will check that context_1 field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test12_verifyContext1() {
		log.info("***** test12_verifyContext1 *****");
		Assert.assertTrue(jsonReturned.contains("\"context_1\":"), 
				"Check for \"context_1\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that context_2 field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test12_verifyContext2() {
		log.info("***** test12_verifyContext2 *****");
		Assert.assertTrue(jsonReturned.contains("\"context_2\":"), 
				"Check for \"context_2\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that context_3 field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test13_verifyContext3() {
		log.info("***** test13_verifyContext3 *****");
		Assert.assertTrue(jsonReturned.contains("\"context_3\":"), 
				"Check for \"context_3\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that context_4 field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test14_verifyContext4() {
		log.info("***** test14_verifyContext4 *****");
		Assert.assertTrue(jsonReturned.contains("\"context_4\":"), 
				"Check for \"context_4\": failed, actual:" + jsonReturned);
	}
	
	/**
	 * Will check that timestamp field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test15_verifyTimeStamp() {
		log.info("***** test15_verifyTimeStamp *****");
		Assert.assertTrue(jsonReturned.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed, actual:" + jsonReturned);
	}

	
	/**
	 * Overriding superclass method to add device reboot
	 * @throws InterruptedException 
	 */
	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		String username = "admin";
		String password = "admin";
		getDevices();
		printSuitetDetails(false);
		
		try {
			log.info("Starting test setup for " + this.getClass().getSimpleName());
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			log.info("Creating a TX connection in boxilla");
			//makeConnection(connection, "private");
			conMethods.createTxConnection(connection, "private", driver, txIp);
			//assign the connection to user
			usrMethods.manageUser(driver, "admin");
			Thread.sleep(5000);
			Users.userManageConnectionTab(driver,"admin").click();
			log.info("Manage Connection option Clicked");
			Users.moveAllConnection(driver).click();
			usrMethods.saveConnection(driver);
			log.info("Connection added Successfully");
			//log into rxIp
			Login login=new Login();
			login.username = username;
			login.password = password;
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
			
			
			
			log.info("Pushing xml file details to devices");
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
			//create connection in boxilla 
			
			
			//making connection through rest instead of boxilla
			ForceConnect con = new ForceConnect();
			con.action = "force_connection";
			con.user = "Boxilla";
			con.connection = connection;
			
			int status = given().header(getHead()).body(con)
					.when()
					.contentType(ContentType.JSON)
					.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
			log.info("Status code from forced_connection:" + status);
			Assert.assertTrue(status == 200, "status code from force connection did not equal 200, actual: " + status);
//			String[] connectionSources = {connection};
//			log.info("Attempting to make a connection using connection view");
//			conMethods.addSources(driver, connectionSources);
//			conMethods.addPrivateDestination(driver, connection, singleRxName);
			log.info("Sleeping while waiting for alerts to be generated");
			Thread.sleep(60000);
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/alerts").asString();
			log.info("RETURNED Json: " + jsonReturned);
			int count=0;
			while (count<20) { 
				if(jsonReturned.contains("id")) {
					break;
				}
				else
				{
					log.info("JSON returned was empty. Waiting and trying again");
					deviceMethods.rebootDevice(driver, rxIp);
//					Thread.sleep(30000);
					jsonReturned = given().header(getHead())
							.when()
							.get(getHttp() + "://" + rxIp + getPort() + "/alerts").asString();
					log.info("RETURNED: " + jsonReturned);
				}
				count++;
		
			}
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			log.info("Before class threw an error. Will run rest call again to try to recover");
			Thread.sleep(30000);
			jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/alerts").asString();
			log.info("RETURNED Json: " + jsonReturned);
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	public class Logout {
		public String username;
		public String[] rx_list;
		public String forced;
	}
	
//	@AfterClass
	public void afterclass() throws InterruptedException{
		String username = "admin";
		Logout logout = new Logout();
		logout.username = username;
		logout.rx_list = new String[1];
		logout.rx_list[0] = rxEmerald.getDeviceName();
		logout.forced = "Yes";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(logout)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/logout")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + username + " has successfully logged out from the target receivers.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/users/kvm/logout", REQUEST_TYPE.POST, response);
		Thread.sleep(20000);
		
	}
	
//	
	
	/**
	 * We dont need to log into boxilla after each method in this suite
	 * so override with an empty method
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	
	/**
	 * Tests in the class do not use the browser so 
	 * this superclass method gets overridden and logout removed.
	 * Also no screen shot is taken on fail
	 */
	@Override
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			try {
				//Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
				//		 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
				}catch(Exception e) {
					System.out.println("Error when trying to capture log file. Catching error and continuing");
					e.printStackTrace();
				}
			try {
				Utilities.captureDeviceLog(rxIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + rxIp);
			}
			try {
				Utilities.captureDeviceLog(txIp, result.getName());
			}catch(Exception e) {
				log.info("Error capturing device log." + txIp);
			}
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}

	
//	/**
//	 * Makes a connection for this test. Can set name and connection type (private, shared)
//	 * @param connectionName
//	 * @param connectionType
//	 * @throws InterruptedException
//	 */
//	public void makeConnection(String connectionName, String connectionType) throws InterruptedException {
//		log.info("Creating connection : " + connectionName);
//		conMethods.addConnection(driver, connectionName, "no"); // connection name, user template
//		conMethods.connectionInfo(driver, "tx", "user", txIp); // connection via, name, host ip
//		conMethods.chooseCoonectionType(driver, connectionType); // connection type
//		conMethods.enableExtendedDesktop(driver);
//		conMethods.enablePersistenConnection(driver);
//		if(connectionType.equals("private")) {
//			conMethods.enableUSBRedirection(driver);
//			conMethods.enableAudio(driver);
//		}
//		conMethods.propertyInfoClickNext(driver);
//		conMethods.saveConnection(driver, connectionName);
//	}

}
