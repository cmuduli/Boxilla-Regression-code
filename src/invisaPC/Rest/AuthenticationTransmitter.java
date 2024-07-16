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
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import invisaPC.Rest.PreviousConnection.Login;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Users;
import testNG.Utilities;

/**
 * This class contains test cases for checking the appliance REST API auth on a transmitter
 * @author Boxilla
 *
 */
public class AuthenticationTransmitter extends StartupTestCase {

	final static Logger log = Logger.getLogger(AuthenticationTransmitter.class);
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private UsersMethods usrMethods=new UsersMethods();
	private String jsonReturn = "";
	private String connection = "authenticationTx1";
	
	/**
	 * Config method that will run once, before any tests in the class run. 
	 * This will create a connection in boxilla and then launch the connection and verify 
	 * it is running
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
			log.info("Starting test setup....");
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			//deviceManageTestPrep();
			conMethods.createTxConnection(connection, "private", driver, txIp);
			//Add the connection  to user 
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
			
			//makeConnection(connection, "private");
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
			
			//create conenction with rest
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
			//create connection in boxilla 
//			String[] connectionSources = {connection};
//			conMethods.addSources(driver, connectionSources);
//			conMethods.addPrivateDestination(driver, connection, singleRxName);
			Thread.sleep(60000);
			jsonReturn = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication").asString();
			log.info("RETURNED: " + jsonReturn);
			if(!jsonReturn.contains("id")) {
				log.info("Json return was blank. Waiting and trying again");
				Thread.sleep(30000);
				jsonReturn = given().header(getHead())
						.when()
						.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication").asString();
				log.info("RETURNED: " + jsonReturn);
				cleanUpLogout();
			}
			
			
		}catch(Exception e) {
			log.info("Before class threw an error. Running rest call again to try to recover");
			Thread.sleep(30000);
			jsonReturn = given().header(getHead())
					.when()
					.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication").asString();
			log.info("RETURNED: " + jsonReturn);
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	
	/**
	 * Will check that the server returned a status of 200
	 */
	@Test(groups = {"rest","emerald"})
	public void test01_verifyReturnCode () {
		log.info("***** test01_verifyReturnCode *****");
		int statusCode = 0;
		for(int j=0; j<500; j++) {
		 statusCode = given().header(getHead())
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication")
		.getStatusCode();
		
		Assert.assertTrue(statusCode == 200, "Status code did not equal 200, actual " + statusCode);
		}
	}
	
	/**
	 * Will check that the ID field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test02_verifyId() {
		log.info("***** test02_verifyId *****");
		Assert.assertTrue(jsonReturn.contains("\"id\":"),
				"Check for \"id\": failed");
	}
	
	/**
	 * Will check that the mac address returned is the same as the device MAC address
	 */
	@Test(groups = {"rest", "smoke","emerald"})
	public void test03_verifyMacAddress() {
		log.info("***** test03_verifyMacAddress *****");
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication")
		.then()
		.body("kvm_authentication_stats.mac[0]", equalTo(txSingle.getMac().toLowerCase()));
	}
	
	/**
	 * Will check that the successful_logins field is returned 
	 */
	@Test(groups = {"rest","emerald"})
	public void test04_verifySuccessfulLogins() {
		log.info("***** test04_verifySuccessfulLogins *****");
		Assert.assertTrue(jsonReturn.contains("\"successful_logins\":"), 
				"Check for \"successful_logins\": failed");
	}
	
	/**
	 * Will check that the refused logins field is returned 
	 */
	@Test(groups = {"rest","emerald"})
	public void test05_verifyRefusedLogins() {
		log.info("***** test05_verifyRefusedLogins *****");
		Assert.assertTrue(jsonReturn.contains("\"refused_logins\":"), 
				"Check for \"refused_logins\": failed");
	}
	
	/**
	 * will check that the manager unreachable field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test06_verifyManagerUnreachable() {
		log.info("***** test06_verifyManagerUnreachable *****");
		Assert.assertTrue(jsonReturn.contains("\"manager_unreachable\":"), 
				"Check for \"manager_unreachable\": failed");
	}
	
	/**
	 * Will check that the timestamp field is returned
	 */
	@Test(groups = {"rest","emerald"})
	public void test07_verifyTimestamp() {
		log.info("***** test07_verifyTimestamp *****");
		Assert.assertTrue(jsonReturn.contains("\"timestamp\":"), 
				"Check for \"timestamp\": failed");
	}
	/**
	 * Uses invisaPCs rest api to check flush authentication stats
	 */
	@Test(groups = {"rest","emerald"})
	public void test08_flushAuthentication() {
		int status = given().header(getHead()).body("{\"action\": \"flush_authentication_stats\"}")
				.when()
				.contentType(ContentType.JSON)
				.put("https://" + txIp + ":8888/control").andReturn().statusCode();
		log.info("Return code from flush : " + status);
		String authReturn = given().header(getHead())
				.when()
				.get(getHttp() + "://" + txIp + getPort() + "/statistics/authentication").asString();
		Assert.assertFalse(authReturn.contains("\"id\":"), "Flush did not work. Rest returned values: " + authReturn);
		
	}
	
	/**
	 * We dont need to log into boxilla after each method in this suite so override with an empty method
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
		
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			log.info(result.getName() + " :FAIL");
		
		if(ITestResult.SKIP == result.getStatus())
			log.info(result.getName() + " :SKIP");
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
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
	
	
//	@AfterClass(alwaysRun=true)
//	public void afterClass() throws InterruptedException {
//		log.info("Starting test teardown....");
//		try {
//			cleanUpLogin();
//			//break the connection
//			conMethods.breakConnection(driver, connection);
//			
//		}catch(Exception | AssertionError e) {
//			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
//			e.printStackTrace();
//			cleanUpLogout();
//		}
//		cleanUpLogout();
//		super.afterClass();
//		printSuitetDetails(true);
//	}
	
}
