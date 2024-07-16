package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasKey;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import northbound.delete.DeleteConnection.DeleteCon;
import northbound.get.config.KvmActiveConnectionsConfig;
import northbound.post.CreateKvmConnections;
import objects.Connections;

public class KvmActiveConnections extends StartupTestCase {

	private ConnectionsMethods connection = new ConnectionsMethods();
	private DevicesMethods devices = new DevicesMethods();
	final static Logger log = Logger.getLogger(KvmActiveConnections.class);
	private String connectionName1 = "KvmActiveConnections_private";
	private String connectionName2 = "KvmActiveConnections_private2";
	private String connectionName3 = "KvmActiveConnections_shared";
	private KvmActiveConnectionsConfig config = new KvmActiveConnectionsConfig();
	
	

	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		//RestAssured.authentication = basic(boxillaRestUser, boxillaRestPassword);
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		
		try {
		//make private connection
			cleanUpLogin();
		connection.createMasterConnection(connectionName1, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
		connection.createMasterConnection(connectionName2, "tx", "private", "true", "true", "true", "false", "false", txIpDual, driver);
		connection.createMasterConnection(connectionName3, "tx", "shared", "true", "true", "true", "false", "false", txIp, driver);
		devices.recreateCloudData(rxIp);
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		
		devices.recreateCloudData(rxIpDual);
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		cleanUpLogout();
		}catch(Exception e) {
			cleanUpLogout();
		}
		
		
		
		
	}
	public class DeleteCon {
		String[] connection_names;
	} 
	
	@Test
	public void test01_getPrivateConnection() throws InterruptedException {
		log.info("Launch connection through Boxilla UI");
		String[] connectionSources = {connectionName1};
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName1, rxEmerald.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName1, rxEmeraldName).isDisplayed();
		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName1 + " and " + rxEmeraldName);
		Assert.assertTrue(isConnection, "Connection between " + connectionName1 + " and " + rxEmeraldName + " is not displayed");	
		
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getActiveConnectionConnectionName(0), equalTo(connectionName1))
		.body(config.getActiveConnectionReceiverName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getActiveConnectionHostType(0), equalTo("ConnectViaTx"))
		.body(config.getActiveConnectionHostValue(0), equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
		.body(config.getActiveConnectionActiveUser(0),anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body(config.getActiveConnectionType(0), equalTo("Private"))
		.body(config.getActiveConnection(0), hasKey("duration"))
		.body(config.getActiveConnection(0), hasKey("total_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("video_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("audio_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body(config.getActiveConnection(0), hasKey("rtt"))
		.body(config.getActiveConnection(0), hasKey("fps"))
		.body(config.getActiveConnection(0), hasKey("dropped_fps"))
		.body(config.getActiveConnection(0), hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
				// Break the connection 
		connection.breakConnection(driver, connectionName1);
				
	}
	@Test
	public void test02_getSharedConnection() throws InterruptedException {
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		
		String[] connectionSources = {connectionName3};
		String[] connectionDestinations = {rxEmerald.getDeviceName(), rxDual.getDeviceName()};
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add shared destinations");
		connection.addSharedDestination(driver, connectionName3, connectionDestinations);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean source = Connections.matrixItem(driver, connectionName3).isDisplayed();
		boolean destination1 = Connections.matrixItem(driver, dualRxName).isDisplayed();
		boolean destination2 = Connections.matrixItem(driver, rxEmeraldName).isDisplayed();
		boolean check = source && destination1 && destination2;
		log.info("Asserting if shared connection has been established");
		Assert.assertTrue(check, "Source and all destinations were not displayed");
		
		Thread.sleep(30000);
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getActiveConnectionConnectionName(0), equalTo(connectionName3))
		.body(config.getActiveConnectionReceiverName(0), anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxEmerald.getDeviceName())))
		.body(config.getActiveConnectionHostType(0), equalTo("ConnectViaTx"))
		.body(config.getActiveConnectionHostValue(0), equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
		.body(config.getActiveConnectionActiveUser(0),anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body(config.getActiveConnectionType(0), equalTo("Shared"))
		.body(config.getActiveConnection(0), hasKey("duration"))
		.body(config.getActiveConnection(0), hasKey("total_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("video_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("audio_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body(config.getActiveConnection(0), hasKey("rtt"))
		.body(config.getActiveConnection(0), hasKey("fps"))
		.body(config.getActiveConnection(0), hasKey("dropped_fps"))
		.body(config.getActiveConnection(0), hasKey("user_latency"))
		
		.body(config.getActiveConnectionConnectionName(1), equalTo(connectionName3))
		.body(config.getActiveConnectionReceiverName(1), anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxEmerald.getDeviceName())))
		.body(config.getActiveConnectionHostType(1), equalTo("ConnectViaTx"))
		.body(config.getActiveConnectionHostValue(1), equalTo(txEmerald.getDeviceName()))			//bug logged change when fixed
		.body(config.getActiveConnectionActiveUser(1),anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body(config.getActiveConnectionType(1), equalTo("Shared"))
		.body(config.getActiveConnection(1), hasKey("duration"))
		.body(config.getActiveConnection(1), hasKey("total_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("video_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("audio_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body(config.getActiveConnection(1), hasKey("rtt"))
		.body(config.getActiveConnection(1), hasKey("fps"))
		.body(config.getActiveConnection(1), hasKey("dropped_fps"))
		.body(config.getActiveConnection(1), hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		connection.breakConnection(driver, connectionName3);
	}
	
	@Test
	public void test03_twoPrivateConnections() throws InterruptedException {
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		//launch connections
		String[] connectionSources = {connectionName1};
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName1, rxEmerald.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		String[] connectionSources2 = {connectionName2};
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources2);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName2, rxDual.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		
		
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName1, rxEmeraldName).isDisplayed();
		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName1 + " and " + rxEmeraldName);
		Assert.assertTrue(isConnection, "Connection between " + connectionName1 + " and " + rxEmeraldName + " is not displayed");
		boolean isConnection2 = Connections.singleSourceDestinationCheck(driver, connectionName2, dualRxName).isDisplayed();
		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName2 + " and " + dualRxName);
		Assert.assertTrue(isConnection2, "Connection between " + connectionName2 + " and " + dualRxName + " is not displayed");
	
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getActiveConnectionConnectionName(0), anyOf(equalTo(connectionName2), equalTo(connectionName1)))
		.body(config.getActiveConnectionReceiverName(0), anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxEmerald.getDeviceName())))
		.body(config.getActiveConnectionHostType(0), equalTo("ConnectViaTx"))
		.body(config.getActiveConnectionHostValue(0), anyOf(equalTo(txDual.getDeviceName()), equalTo(txEmerald.getDeviceName())))			//bug logged change when fixed
		.body(config.getActiveConnectionActiveUser(0),anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body(config.getActiveConnectionType(0), equalTo("Private"))
		.body(config.getActiveConnection(0), hasKey("duration"))
		.body(config.getActiveConnection(0), hasKey("total_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("video_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("audio_bandwidth"))
		.body(config.getActiveConnection(0), hasKey("usb_bandwidth"))				
		.body(config.getActiveConnection(0), hasKey("rtt"))
		.body(config.getActiveConnection(0), hasKey("fps"))
		.body(config.getActiveConnection(0), hasKey("dropped_fps"))
		.body(config.getActiveConnection(0), hasKey("user_latency"))
		
		.body(config.getActiveConnectionConnectionName(1), anyOf(equalTo(connectionName2), equalTo(connectionName1)))
		.body(config.getActiveConnectionReceiverName(1), anyOf(equalTo(rxDual.getDeviceName()), equalTo(rxEmerald.getDeviceName())))
		.body(config.getActiveConnectionHostType(1), equalTo("ConnectViaTx"))
		.body(config.getActiveConnectionHostValue(1), anyOf(equalTo(txDual.getDeviceName()), equalTo(txEmerald.getDeviceName())))			//bug logged change when fixed
		.body(config.getActiveConnectionActiveUser(1),anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null), equalTo("")))
		.body(config.getActiveConnectionType(1), equalTo("Private"))
		.body(config.getActiveConnection(1), hasKey("duration"))
		.body(config.getActiveConnection(1), hasKey("total_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("video_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("audio_bandwidth"))
		.body(config.getActiveConnection(1), hasKey("usb_bandwidth"))				//bug logged. Missing from return
		.body(config.getActiveConnection(1), hasKey("rtt"))
		.body(config.getActiveConnection(1), hasKey("fps"))
		.body(config.getActiveConnection(1), hasKey("dropped_fps"))
		.body(config.getActiveConnection(1), hasKey("user_latency")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		connection.breakConnection(driver, connectionName1);
		connection.breakConnection(driver, connectionName2);
		
	}
	
}
