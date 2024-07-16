package northbound.delete;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;
import static org.hamcrest.Matchers.hasSize;

import org.apache.log4j.Logger;

import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.contains;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import soak.DeviceUpgradeAll;

public class TerminateActiveConnection extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(TerminateActiveConnection.class);
	private ConnectionsMethods connection = new ConnectionsMethods();
	private UsersMethods user = new UsersMethods();
	private DevicesMethods devices = new DevicesMethods();
	private String connectionName1 = "TerminateActiveConnection";
	private String connectionName2 = "terminateShared";
	 
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/connections/kvm/active";
	}
	
	private String getBody(String username, String connectionName, String receiverName, String password) {
		String body = "{\"username\": \""+ username + "\",\"password\": \"" + password  + "\",\"connection_name\": \"" + connectionName + "\", \"receiver_name\":\"" + receiverName + "\"}";
		return body;
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		//RestAssured.authentication = basic(boxillaRestUser, boxillaRestPassword);
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		//make private connection
		try {
			cleanUpLogin();
			connection.createMasterConnection(connectionName1, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
			connection.createMasterConnection(connectionName2, "tx", "shared", "false", "false", "false", "false", "false", txIp, driver);
			user.addAllConnectionsToUser(driver, "admin");
			devices.recreateCloudData(rxIp);
			devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
			devices.recreateCloudData(rxIpDual);
			devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
			cleanUpLogout();
		}catch(Exception e) {
			cleanUpLogout();
		}
	}
	
	@Test
	public void test01_terminateConnection() throws InterruptedException {
		//launch connection
		log.info("Launching connection");
		String body = getBody("admin", connectionName1, rxEmerald.getDeviceName(), "admin");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Active connection " + connectionName1 + " is successfully launched.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		log.info("Waiting for connection to attach and asserting connection is running");
		
		
		Thread.sleep(65000);
//		//run get all active connections and assert this connection is running
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.active_connections[0].connection_name", equalTo(connectionName1))
		.body("message.active_connections[0].receiver_name", equalTo(rxEmerald.getDeviceName()))
		.body("message.active_connections[0].host.type", equalTo("ConnectViaTx"))
		.body("message.active_connections[0].host.value", equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
		.body("message.active_connections[0].active_user",anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body("message.active_connections[0].type", equalTo("Private"))
		.body("message.active_connections[0]", hasKey("duration"))
		.body("message.active_connections[0]", hasKey("total_bandwidth"))
		.body("message.active_connections[0]", hasKey("video_bandwidth"))
		.body("message.active_connections[0]", hasKey ("audio_bandwidth"))
		.body("message.active_connections[0]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body("message.active_connections[0]", hasKey("rtt"))
		.body("message.active_connections[0]", hasKey("fps"))
		.body("message.active_connections[0]", hasKey("dropped_fps"))
		.body("message.active_connections[0]", hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		log.info("Connection is running. Attempting to terminate");
		//terminate connection
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.delete(getUri())
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Active connection TerminateActiveConnection is successfully terminated.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.DELETE, response);
		log.info("Connection terminated. Waiting and asserting connection is no longer running");
		Thread.sleep(65000);
		//check all active connections is empty
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.active_connections", hasSize(0)).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
	}
	
//	@Test
//	public void test02_terminateSharedConnection() {
//		//launch connection first to two RXs
//		String body = getBody("admin", connectionName2, rxEmerald.getDeviceName());
//		String body2 = getBody("admin", connectionName2, rxDual.getDeviceName());
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.post(getUri())
//		.then().assertThat().statusCode(201)
//		.body("message", equalTo("Active connection " + connectionName2 + " is successfully launched."));
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body2)
//		.post(getUri())
//		.then().assertThat().statusCode(201)
//		.body("message", equalTo("Active connection " + connectionName2 + " is successfully launched."));
//		
//		//check both connections are running
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get(getUri())
//		.then().assertThat().statusCode(200)
//		.body("message.active_connections[0].connection_name", equalTo(connectionName2))
//		.body("message.active_connections[0].receiver_name", equalTo(rxEmerald.getDeviceName()))
//		.body("message.active_connections[0].host.type", equalTo("ConnectViaTx"))
//		.body("message.active_connections[0].host.value", equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
//		.body("message.active_connections[0].active_user",anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
//		.body("message.active_connections[0].type", equalTo("Shared"))
//		.body("message.active_connections[0]", hasKey("duration"))
//		.body("message.active_connections[0]", hasKey("total_bandwidth"))
//		.body("message.active_connections[0]", hasKey("video_bandwidth"))
//		.body("message.active_connections[0]", hasKey("audio_bandwidth"))
//		.body("message.active_connections[0]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
//		.body("message.active_connections[0]", hasKey("rtt"))
//		.body("message.active_connections[0]", hasKey("fps"))
//		.body("message.active_connections[0]", hasKey("dropped_fps"))
//		.body("message.active_connections[0]", hasKey("user_latency"))
//		.body("message.active_connections[1].connection_name", equalTo(connectionName2))
//		.body("message.active_connections[1].receiver_name", equalTo(rxDual.getDeviceName()))
//		.body("message.active_connections[1].host.type", equalTo("ConnectViaTx"))
//		.body("message.active_connections[1].host.value", equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
//		.body("message.active_connections[1].active_user", equalTo("admin"))
//		.body("message.active_connections[1].type", equalTo("Shared"))
//		.body("message.active_connections[1]", hasKey("duration"))
//		.body("message.active_connections[1]", hasKey("total_bandwidth"))
//		.body("message.active_connections[1]", hasKey("video_bandwidth"))
//		.body("message.active_connections[1]", hasKey("audio_bandwidth"))
//		.body("message.active_connections[1]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
//		.body("message.active_connections[1]", hasKey("rtt"))
//		.body("message.active_connections[1]", hasKey("user_latency"))
//		.body("message.active_connections[1]", hasKey("fps"))
//		.body("message.active_connections[1]", hasKey("dropped_fps"));
//		
//		//terminate first RX from connection
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.delete(getUri())
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Active connection terminateShared is successfully terminated."));
//		
//		
//		//check only one connection is now in active connections
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get(getUri())
//		.then().assertThat().statusCode(200)
//		.body("message.active_connections", hasSize(1));
//		
//		//terminate second connection
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body2)
//		.delete(getUri())
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Active connection terminateShared is successfully terminated."));
//		
//		//check no connections are active
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.get(getUri())
//		.then().assertThat().statusCode(200)
//		.body("message.active_connections", hasSize(0));
//		
//	}
//	
//	@Test
//	public void test03_terminateInvalidConnection() {
//		String body = getBody("admin", "test03_terminateInvalidConnection", rxEmerald.getDeviceName());
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.delete(getUri())
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Active connection test03_terminateInvalidConnection or receiver " + rxEmerald.getDeviceName() + " does not exist."));
//	}
//	
//	//@Test  Cannot run until the login user functionality is available
//	public void test03_terminateConnectionNotAssigned() throws InterruptedException {
//		String conName = "test03Terminate";
//		//connection.createMasterConnection(conName, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
//		String body = getBody("bren", conName, rxEmerald.getDeviceName());
//		
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.post(getUri())
//		.then().assertThat().statusCode(201)
//		.body("message", equalTo("Active connection " + conName + " is successfully launched. "));
//		
//		String body2 = getBody("admin", conName, rxEmerald.getDeviceName());
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body2)
//		.delete(getUri())
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Connection " + conName + " not assigned to user admin."));
//		
//	}

}
