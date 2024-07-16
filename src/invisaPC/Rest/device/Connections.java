package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.AppliancePool;
import extra.Device;
import extra.ForceConnect;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;
/**
 * Class contains all the force connection through rest tests
 * @author Boxilla
 *
 */
public class Connections extends StartupTestCase {

	final static Logger log = Logger.getLogger(Connections.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	ConnectionsMethods connections = new ConnectionsMethods();
	
	//test properties
	private String managedConnectionNameShared = "managedConnectionShared";
	private String managedConnectionNamePrivate = "managedConnectionPrivate";
	private String managedConnectionNamePrivate2 = "managedConnectionPrivate2";
	private String extraTxIp = "10.211.128.156";
	private String extraTxMac = "1C:37:BF:00:11:4C";

	

	/**
	 * Inner class to represent the json for forced connect
	 * Can send an instance of this as part of the rest put
	 * @author Boxilla
	 *
	 */
	
	class LogOffUser {
		public String action = "";
	}
	/**
	 * Creates a private connection using force connection and asserts on active connections
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "smoke", "emerald2"})
	public void test01_forceConnectionPrivate() throws InterruptedException {
		log.info("***** test01_forceConnectionPrivate *****");
		ForceConnect con = new ForceConnect();
		con.action = "force_connection";
		con.user = "Boxilla";
		con.connection = managedConnectionNamePrivate;
		log.info("RX:" +  rxIp);
		int status = given().header(getHead()).body(con)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		log.info("Status code from forced_connection:" + status);
		Assert.assertTrue(status == 200,"Return code did not equal 200, actual:" + status);
		log.info("Sleeping while connection is made");
		Thread.sleep(30000);
		log.info("Checking if active connection is up");
		String output = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections").asString();
		
		log.info("Active connection return: " + output);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name", equalTo(Arrays.asList(managedConnectionNamePrivate)));
	}
	
	/**
	 * Terminates a forced connections using terminate_connection
	 * @throws InterruptedException
	 */
	@Test(groups= {"rest", "integration", "emerald2"})
	public void test02_terminateConnectionPrivate() throws InterruptedException {
		
		System.out.println(getHttp() + "://" + rxIp + getPort() + "/control/connections");
		String json = "{\"action\": \"terminate_connection\"}";
		int status = given().header(getHead()).body(json)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		log.info("Status code from terminate_connection:" + status);
		Assert.assertTrue(status ==200, "Return code did not equal 200, actual:" + status);
			Thread.sleep(30000);
			
			String jsonReturned = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections").toString();
			Assert.assertTrue(!jsonReturned.contains(managedConnectionNamePrivate), "The connection name was in active connections");
	}
	/**
	 * Creates a shared connection using force_connection and asserts on active_connection
	 * @throws InterruptedException
	 */
	//@Test(groups= {"rest", "integration", "emerald2", "notEmerald"})
	public void test03_managedConnectionShared() throws InterruptedException {
		ForceConnect con = new ForceConnect();
		con.action = "force_connection";
		con.user = "Boxilla";
		con.connection = managedConnectionNameShared;
		
		Thread.sleep(100000);
		log.info("Making connection");
		int status2 = given().header(getHead()).body(con)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIpDual + getPort() + "/control/connections").andReturn().statusCode();
		log.info("Status code from terminate_connection:" + status2);
		Assert.assertTrue(status2 == 200, "Return code did not equal 200, actual:" + status2);
		Thread.sleep(3000);
		int status = given().header(getHead()).body(con)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		
		log.info("Device 1 connection status: " + status);
		log.info("Device 2 connection status: " + status2);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		Thread.sleep(3000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name", equalTo(Arrays.asList(managedConnectionNameShared)));
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIpDual +  getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name", equalTo(Arrays.asList(managedConnectionNameShared)));
	}
	/**
	 * Checks /control/user returns the correct user name
	 */
	//@Test(groups= {"rest", "emerald2"})
	public void test04_getUser() {
		log.info("***** test04_getUser *****");
		String output = given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/user").asString();
		log.info("Output from control/user:" + output );
		Assert.assertTrue(output.contains("Boxilla") || output.contains("admin"), "Correct user did not return");
	}
	
	/**
	 * Creates a connections, checks its active, then uses the REST API
	 * log off user to break the connection. Check is made to see if the connection has 
	 * been broken
	 * @throws InterruptedException
	 */
	@Test(groups = {"rest", "integration", "smoke", "emerald2"})
	public void test05_logOffUser() throws InterruptedException {
		log.info("***** test05_logOffUser *****");
		ForceConnect con = new ForceConnect();
		con.action = "force_connection";
		con.user = "Boxilla";
		con.connection = managedConnectionNamePrivate;
		
		int status = given().header(getHead()).body(con)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		log.info("Status code from forced_connection:" + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		Thread.sleep(30000);
		log.info("Checking if connection is up");
		//check if connection is up
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name", equalTo(Arrays.asList(managedConnectionNamePrivate)));
		log.info("Connection is up.");
		log.info("Attempting to log off user");
		// log off user
		LogOffUser logOff = new LogOffUser();
		logOff.action = "log_off";
		int status2 = given().header(getHead()).body(logOff)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/user").andReturn().statusCode();
		log.info("Status code from log off user:" + status2);
		Assert.assertTrue(status2 == 200, "Return code did not equal 200, actual:" + status2);
		Thread.sleep(65000);
		log.info("Checking connection is not active any more");
		//check no active connection is up
		String returnJson = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections").asString();
		log.info("Check for active connection :" + returnJson);
		Assert.assertFalse(returnJson.contains("id"), "Active Connection is still running");		
	}
	
//	/**
//	 * Sending a log_off to a TX should return a 400 bad request
//	 * @throws InterruptedException
//	 */
//	@Test(groups = {"rest", "integration", "smoke", "emerald2"})
//	public void test06_logOffUserTx()  throws InterruptedException {
//		LogOffUser logOff = new LogOffUser();
//		logOff.action = "log_off";
//		int status2 = given().header(getHead()).body(logOff)
//				.when()
//				.contentType(ContentType.JSON)
//				.put(getHttp() + "://" + txIp + getPort() + "/control/user").andReturn().statusCode();
//		log.info("Status code from log off user:" + status2);
//		Assert.assertTrue(status2 == 400, "Return code did not equal 400");
//		
//	}
	//@Test(groups = {"rest", "integration", "smoke", "emerald2"})
	public void test07_changeForcedConnection() throws InterruptedException {
		deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
		ForceConnect fCon = new ForceConnect();
		fCon.action = "force_connection";
		fCon.user = "Boxilla";
		fCon.connection = managedConnectionNamePrivate;
		
		//make first connection
		int status = given().header(getHead()).body(fCon)
			.when()
			.contentType(ContentType.JSON)
			.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		
		log.info("Return code from force connect 1 = " + status );
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("sleeping while connection comes up then checking for connection");
		Thread.sleep(15000);
		
		//chceck first connection
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name[0]", equalTo(managedConnectionNamePrivate));
		
		//make second connection 
		fCon.connection = managedConnectionNamePrivate2;
		
		int status2 = given().header(getHead()).body(fCon)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
		
		log.info("Return code from HTTP force connect 2 = " + status2 );
		Assert.assertTrue(status2 == 200, "Return code did not equal 200" + status2);
		log.info("sleeping while connection comes up then checking for connection");
		Thread.sleep(15000);
		
		//check second connection
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name[0]", equalTo(managedConnectionNamePrivate2));
	}
	

	
	/**
	 * Sets system properties, manages devices and checks the system properties 
	 * are correct. Also creates a template to be used later in the tests
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		
		System.out.println(getHttp() + getPort());
		
		try {
			cleanUpLogin();
			connections.createTxConnection(managedConnectionNamePrivate, "private", driver, txIp);
			connections.createTxConnection(managedConnectionNameShared, "shared", driver, txIp);
			connections.createTxConnection(managedConnectionNamePrivate2, "private", driver, extraTxIp);
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
			//deviceMethods.rebootDeviceSSH(rxIpDual, "root", "barrow1admin_12", 100000);
			
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
	private void manageNewTx() throws InterruptedException {
		try {
			discoveryMethods.discoverDevices(driver);
			discoveryMethods.stateAndIPcheck(driver, extraTxMac, prop.getProperty("ipCheck"),
					extraTxIp, rxSingle.getGateway(),rxSingle.getNetmask());
			discoveryMethods.manageApplianceAutomatic(driver, "extraTx",extraTxMac,
					prop.getProperty("ipCheck"));
		}catch(Exception e ) {
			log.info("Problem managing extra TX");
			e.printStackTrace();
		}
	}
	
}
