package bonding;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.post.LoginUser.Login;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneReceiversConfig;
import testNG.Utilities;
/**
 * This class contains tests for the launching of Bonded connections. Extra 
 * appliances need to be managed in order for these tests to run successfully
 * @author Boxilla
 *
 */
public class LaunchBondedConnections extends StartupTestCase{
	SoftAssert Assert = new SoftAssert();
	final static Logger log = Logger.getLogger(LaunchBondedConnections.class);

	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	private CreateZoneConfig zoneConfig = new CreateZoneConfig();
	private UsersMethods userMethods = new UsersMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private CreateKvmConnectionsConfig createCon = new CreateKvmConnectionsConfig();
	private String txConnectionName = "createLaunchBonded";
	String seTxSingleIp = "10.231.128.245";
	private String seTxDualIp = "10.231.128.73";
	private String seRxSingleIp = "10.231.128.87";
	private String seRxDualIp = "10.231.128.141";
	private String bondedConnectionGroup  = "launchConBonded";
	private String bondedReceiverGroup = "launchRecBonded1";
	private String[] connections = new String[4];
	private String[] receivers = new String[4];
	private ArrayList<String> groupNames = new ArrayList<String>();

	/**
	 * Inner class to model the json payload for 
	 * a Northbound login REST call
	 * @author Boxilla
	 *
	 */
	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
	/**
	 * This setup will run once before any tests in the class run,.
	 * It will create some connections in Boxilla and assign them to a connection array. Then it 
	 * gets all devices managed and assigns them to a device array. Bonded connection groups and
	 * bonded receiver groups are then created from these arrays
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());


		createCon.createViaTxConnection(txConnectionName + "1", txIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionName + "2", txIpDual, "Private", "Yes", "ConnectViaTx", "Yes", "Yes",
				"Yes", "Yes", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionName + "3", seTxSingleIp, "Private", "Yes", "ConnectViaTx", "Yes", "Yes",
				"Yes", "No", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionName + "4", seTxDualIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		log.info("Created all connections");
		connections[0] = txConnectionName + "1";
		connections[1] = txConnectionName + "2";
		connections[2] = txConnectionName + "3";
		connections[3] = txConnectionName + "4";

		try {	
			cleanUpLogin();
			conMethods.createBondedConnection(driver, bondedConnectionGroup, "", connections, "");
			log.info("Created bonded connection");
			receivers[0] = rxEmerald.getDeviceName();
			receivers[1] = rxDual.getDeviceName();
			receivers[2] = "SE_SH_RX";
			receivers[3] = "SE_DH_RX";
			log.info("RECEVERS ASSIGNED to group");
			deviceMethods.createBondedGroup(driver, bondedReceiverGroup, "", receivers);
			log.info("Assigning bonded connection to user");
			userMethods.addConnectionToUser(driver, "admin", bondedConnectionGroup);
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
		log.info("bonded receiver group created");
		updateReceivers(receivers);
		log.info("Updated receivers");

	}
	/**
	 * Inner class used to model the json object passed to 
	 * the launch bonded connection REST API
	 * @author Boxilla
	 *
	 */
	public class LaunchBondedConnection {
		public String bonded_connection_name;
		public String user_name;
		public String mac;
	}

	/**
	 * User the REST API to launch a bonded connection
	 * @param connection - The name of the bonded connection to be launched
	 * @throws InterruptedException
	 */
	public void launchConnection(LaunchBondedConnection connection) throws InterruptedException {
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(connection)
		.post("https://" + boxillaManager + "/bxa-api/connections/bonded")
		.then().assertThat().statusCode(200);
		log.info("Sleeping for 60 seconds while connection attaches");
		Thread.sleep(61000);
	}

	/**
	 * Test that launches a bonded connetion and verifies the connection is running in Boxilla UI
	 * @throws InterruptedException
	 */
	@Test
	public void test01_launchBondedConnection() throws InterruptedException {
		LaunchBondedConnection con = new LaunchBondedConnection();
		con.bonded_connection_name = bondedConnectionGroup;
		con.user_name = "admin";
		con.mac = rxEmerald.getMac();

		launchConnection(con);
		conMethods.checkExtendedBondedConnections(driver, connections, receivers );
		conMethods.checkActiveBondedConnection(driver, bondedConnectionGroup, "4");
	}

	/**
	 * Test that kills a bonded connection and verifies the bonded connection is killed
	 * in Boxilla UI
	 * @throws InterruptedException
	 */
	@Test
	public void test02_terminateBondedConnection() throws InterruptedException {
		conMethods.killBondedConnectionFromViewer(driver, bondedConnectionGroup);
		conMethods.checkActiveBondedConnectionNotRunning(driver, bondedConnectionGroup);


	}
	/**
	 * Test that will terminate a bonded connection in Boxilla
	 * by terminating just one of the connections in the Bonded connection
	 * @throws InterruptedException
	 */
	@Test
	public void test03_terminateBondedConnectionSingleConnection() throws InterruptedException {
		LaunchBondedConnection con = new LaunchBondedConnection();
		con.bonded_connection_name = bondedConnectionGroup;
		con.user_name = "admin";
		con.mac = rxEmerald.getMac();

		launchConnection(con);
		conMethods.checkExtendedBondedConnections(driver, connections, receivers );
		conMethods.checkActiveBondedConnection(driver, bondedConnectionGroup, "4");

		//to verfiy after connection is broken
		conMethods.breakConnection(driver, connections[0]);
		conMethods.checkActiveBondedConnectionNotRunning(driver, bondedConnectionGroup);
	}
	/**
	 * Test that launches a bonded connection that consists of one shared connection.
	 * Verifies the bonded connection is running
	 * @throws InterruptedException
	 */
	@Test
	public void test04_launchBondedConnectionShared() throws InterruptedException {
		String connectionName = "bondedShared";
		createCon.createViaTxConnection(connectionName, seTxSingleIp, "Shared", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		connections[0] = connectionName;
		connections[1] = connectionName;
		connections[2] = connectionName;
		connections[3] = connectionName;
		conMethods.createBondedConnection(driver, "test03BondCon", "", connections, "");
		userMethods.addConnectionToUser(driver, "admin", "test03BondCon");
		updateReceivers(receivers);
		Thread.sleep(5000);
		LaunchBondedConnection con = new LaunchBondedConnection();
		con.bonded_connection_name = "test03BondCon";
		con.user_name = "admin";
		con.mac = rxEmerald.getMac();

		launchConnection(con);
		conMethods.checkExtendedBondedConnections(driver, connections, receivers );
		conMethods.checkActiveBondedConnection(driver, "test03BondCon", "4");

		//Terminate
		conMethods.breakConnection(driver, connections[0]);
		Assert.assertAll();
	}
	/**
	 * Test that launches a bonded connection which is in a zone and 
	 * verifies the connection is running 
	 * @throws InterruptedException
	 */
	@Test
	public void test05_launchConnectionInZone() throws InterruptedException {
		String zoneName = "zoneForBonded";
		String bondedConnectionName = "bondedTest05";
		String connectionName = "test05BondedCon";
		zoneConfig.createZone(zoneName, "TestZone", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(connectionName + "0" , seTxSingleIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connections[0] = connectionName + "0";

		createCon.createViaTxConnection(connectionName + "1" , seTxDualIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connections[1] = connectionName + "1";

		createCon.createViaTxConnection(connectionName + "2" , txIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connections[2] = connectionName + "2";

		createCon.createViaTxConnection(connectionName + "3" , txIpDual, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connections[3] = connectionName + "3";

		conMethods.createBondedConnection(driver, bondedConnectionName, zoneName, connections, "");
		userMethods.addConnectionToUser(driver, "admin", bondedConnectionName);
		deviceMethods.deleteGroup(driver, bondedReceiverGroup);

		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		deviceMethods.createBondedGroup(driver, bondedReceiverGroup, zoneName, receivers);


		updateReceivers(receivers);
		Thread.sleep(5000);
		LaunchBondedConnection con = new LaunchBondedConnection();
		con.bonded_connection_name = bondedConnectionName;
		con.user_name = "admin";
		con.mac = rxEmerald.getMac();

		launchConnection(con);
		conMethods.checkExtendedBondedConnections(driver, connections, receivers );
		conMethods.checkActiveBondedConnection(driver, bondedConnectionName, "4");

		//Terminate
		conMethods.breakConnection(driver, connections[0]);
		//empty array to remove all receviers
		String[] noReceivers = {};
		editReceiver.editReceivers(zoneName, noReceivers, boxillaManager, boxillaRestUser, boxillaRestPassword);

	}


	private String getBody(String username, String connectionName, String receiverName, String password) {
		String body = "{\"username\": \""+ username + "\",\"password\": \"" + password  + "\",\"connection_name\": \"" + connectionName + "\", \"receiver_name\":\"" + receiverName + "\"}";
		return body;
	}
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/connections/kvm/active";
	}

	public void terminateConnection(String connectionName) {
		String body = getBody("admin", connectionName, rxEmerald.getDeviceName(), "admin");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.delete(getUri())
		.then().assertThat().statusCode(200);
		log.info("Connection terminated. Waiting and asserting connection is no longer running");
	}

	public void updateReceivers( String[] receiverList) {
		Login login = new Login();
		login.username = "admin";
		login.password = "admin";
		login.rx_list = receiverList;
		login.forced = "Yes";
		log.info("NB Rest login details:" + boxillaRestUser + "/" + boxillaRestPassword);
		log.info("Logging in user:" + "admin");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User admin" + " has successfully logged in to the target receivers."));
	}
	

	
	@AfterClass
	public void afterClass() {
		//delete all connections
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
		
		cleanUpLogin();
		deviceMethods.navigateToDevicesGroups(driver);
		try {
			
			if(deviceMethods.doesGroupExist(driver, bondedReceiverGroup, true)) {
				deviceMethods.deleteGroup(driver, bondedReceiverGroup);
			}
		} catch (InterruptedException e) {

			e.printStackTrace();
		}
		cleanUpLogout();
	}

}
