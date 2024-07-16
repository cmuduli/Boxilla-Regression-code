package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.basic;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import northbound.get.ApplianceStatus;
import northbound.get.BoxillaHeaders;

public class LaunchActiveConnection extends StartupTestCase {

	
	
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/connections/kvm/active";
	}
	final static Logger log = Logger.getLogger(LaunchActiveConnection.class);
	private ConnectionsMethods connection = new ConnectionsMethods();
	private DevicesMethods devices = new DevicesMethods();
	private String connectionName1 = "LaunchActiveConnection_private";
	private String connectionName2 = "LaunchActiveConnection_shared";
	private UsersMethods user = new UsersMethods();
	 
	
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
			connection.createMasterConnection(connectionName2, "tx", "shared", "true", "true", "true", "false", "false", txIpDual, driver);
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
	
	private String getBody(String username, String connectionName, String receiverName, String password) {
		String body = "{\"username\": \""+ username + "\",\"password\": \"" + password  + "\",\"connection_name\": \"" + connectionName + "\", \"receiver_name\":\"" + receiverName + "\"}";
		return body;
	}
	@Test
	public void test01_launchPrivateConnection() throws InterruptedException {
			
//		devices.recreateCloudData(rxIp);
	String body = getBody("admin", connectionName1, rxEmerald.getDeviceName(), "admin");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.post(getUri())
		.then().assertThat().statusCode(201)
		.body("message", containsString("Active connection " + connectionName1 + " is successfully launched.")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		waitForBoxillaPoll();	//sleep and wait for the next poll before checking connection
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
		.body("message.active_connections[0]", hasKey("audio_bandwidth"))
		.body("message.active_connections[0]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body("message.active_connections[0]", hasKey("rtt"))
		.body("message.active_connections[0]", hasKey("fps"))
		.body("message.active_connections[0]", hasKey("dropped_fps"))
		.body("message.active_connections[0]", hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test02_launchSharedConnection() throws InterruptedException {
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		log.info("Waited for few seconds after rebooting the device");
		Thread.sleep(60000);
		String body = getBody("admin", connectionName2, rxEmerald.getDeviceName(), "admin");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.post(getUri())
		.then().assertThat().statusCode(201).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		body = getBody("admin", connectionName2, rxDual.getDeviceName(), "admin");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.post(getUri())
		.then().assertThat().statusCode(201).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
		
		waitForBoxillaPoll();	//sleep and wait for the next poll before checking connection
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.active_connections[0].connection_name", equalTo(connectionName2))
		.body("message.active_connections[0].receiver_name", anyOf(is(rxEmerald.getDeviceName()), is(rxDual.getDeviceName())))
		.body("message.active_connections[0].host.type", equalTo("ConnectViaTx"))
		.body("message.active_connections[0].host.value", equalTo(txDual.getDeviceName()))			//bug logged change when fixed
		.body("message.active_connections[0].active_user",anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body("message.active_connections[0].type", equalTo("Shared"))
		.body("message.active_connections[0]", hasKey("duration"))
		.body("message.active_connections[0]", hasKey("total_bandwidth"))
		.body("message.active_connections[0]", hasKey("video_bandwidth"))
		.body("message.active_connections[0]", hasKey("audio_bandwidth"))
		.body("message.active_connections[0]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body("message.active_connections[0]", hasKey("rtt"))
		.body("message.active_connections[0]", hasKey("fps"))
		.body("message.active_connections[0]", hasKey("dropped_fps"))
		.body("message.active_connections[0]", hasKey("user_latency"))
		
		.body("message.active_connections[1].connection_name", equalTo(connectionName2))
		.body("message.active_connections[1].receiver_name", anyOf(is(rxEmerald.getDeviceName()), is(rxDual.getDeviceName())))
		.body("message.active_connections[1].host.type", equalTo("ConnectViaTx"))
		.body("message.active_connections[1].host.value", equalTo(txDual.getDeviceName()))			//bug logged change when fixed
		.body("message.active_connections[0].active_user",anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body("message.active_connections[1].type", equalTo("Shared"))
		.body("message.active_connections[1]", hasKey("duration"))
		.body("message.active_connections[1]", hasKey("total_bandwidth"))
		.body("message.active_connections[1]", hasKey("video_bandwidth"))
		.body("message.active_connections[1]", hasKey("audio_bandwidth"))
		.body("message.active_connections[1]", hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body("message.active_connections[1]", hasKey("rtt"))
		.body("message.active_connections[1]", hasKey("fps"))
		.body("message.active_connections[1]", hasKey("dropped_fps"))
		.body("message.active_connections[1]", hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);

	}
	
	
//	@Test
//	public void test03_invalidConnectionName() {
//		String connectionName = "invalidConnection";
//		String body = getBody("admin", connectionName, rxEmerald.getDeviceName());
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.post(getUri())
//		.then().assertThat().statusCode(400)
//		.body("message", equalTo("Active connection " + connectionName + " or receiver " + rxEmerald.getDeviceName() + " does not exist."));
//	}
//	
//	@Test
//	public void test04_connectionNotAssignedToUser() throws InterruptedException {
//		String connectionName = "test04_connectionNotAssignedToUser";
//		connection.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
//		String body = getBody("admin", connectionName, rxEmerald.getDeviceName());
//		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(body)
//		.post(getUri())
//		.then().assertThat().statusCode(403)
//		.body("message", equalTo("Connection " + connectionName + " not assigned to user admin."));
//	}
//	

}
