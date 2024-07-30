package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import static org.hamcrest.Matchers.*;
import org.apache.log4j.Logger;
import org.hamcrest.Matchers;
import org.hamcrest.core.IsNull;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.device.BulkUpdateSystemProperties;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;

import northbound.get.config.ApplianceStatusConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneReceiversConfig;
import objects.Connections;

public class ApplianceStatus extends StartupTestCase {

	final static Logger log = Logger.getLogger(ApplianceStatus.class);
	private ApplianceStatusConfig config = new ApplianceStatusConfig();
	private ConnectionsMethods connection = new ConnectionsMethods();
	private DevicesMethods device = new DevicesMethods();
	private String connectionName = "ApplianceStatsCon";
	

	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
	
		try {
			cleanUpLogin();
		connection.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
		device.recreateCloudData(rxIp);
		device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		
		//launch connection 
		String[] connectionSources = {connectionName};
		log.info("Attempting to add soruces");
		connection.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connection.addPrivateDestination(driver, connectionName, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, singleRxName).isDisplayed();
		log.info("Asserting if connections has been created between " + connectionName + " and " + singleRxName);
		Assert.assertTrue(isConnection, "Connection between " + connectionName + " and " + singleRxName + " is not displayed");	
		
		cleanUpLogout();
		}catch(Exception e) {
			cleanUpLogout();
		}
		
	}
	

	
	
	@Test
	public void test01_receiverInConnection() throws InterruptedException {
		
		//check connection on REST 
		ApplianceStatusConfig.GetStats stats = config.new GetStats();
		stats.device_names = new String[1];
		stats.device_names[0] = rxEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(stats)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceType(0), equalTo("receiver"))
		.body(config.getDeviceLoggedInUserUsername(0), anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null)))		//due to bug 2341
		.body(config.getDeviceLoggedInUserType(0), anyOf(equalTo("common user"),equalTo(null)))						//due to bug 2341
		.body(config.getDeviceActiveConnectionName(0, 0), equalTo(connectionName))
		.body(config.getDeviceActiveConnectionType(0, 0), equalTo("Private"))
		.body(config.getDeviceActiveConnectinoGroup(0, 0), equalTo("ConnectViaTx"))
		.body(config.getDeviceState(0), equalTo("OnLine"))
		.body(config.getDeviceStatus(0), equalTo("configured"))
		.body(config.getDeivceDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceMac(0), equalTo(rxEmerald.getMac()))
		.body(config.getDeviceIp(0), equalTo(rxIp)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);

	}
	
	@Test
	public void test02_transmitterInConnection() {
		
		ApplianceStatusConfig.GetStats stats = config.new GetStats();
		stats.device_names = new String[1];
		stats.device_names[0] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(stats)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceType(0), equalTo("transmitter"))
		.body(config.getDeviceActiveConnectionName(0, 0), equalTo(connectionName))
		.body(config.getDeviceActiveConnectionType(0, 0), equalTo("Private"))
		.body(config.getDeviceActiveConnectinoGroup(0, 0), equalTo("ConnectViaTx"))
		.body(config.getDeviceState(0), equalTo("OnLine"))
		.body(config.getDeviceStatus(0), equalTo("configured"))
		.body(config.getDeivceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeviceIp(0), equalTo(txIp)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}
	
	@Test
	public void test03_getMultipleDevicesInConnection() {
		
		ApplianceStatusConfig.GetStats stats = config.new GetStats();
		stats.device_names = new String[2];
		stats.device_names[0] = txEmerald.getDeviceName();
		stats.device_names[1] = rxEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(stats)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceType(0), equalTo("transmitter"))
		.body(config.getDeviceActiveConnectionName(0, 0), equalTo(connectionName))
		.body(config.getDeviceActiveConnectionType(0, 0), equalTo("Private"))
		.body(config.getDeviceActiveConnectinoGroup(0, 0), equalTo("ConnectViaTx"))
		.body(config.getDeviceState(0), equalTo("OnLine"))
		.body(config.getDeviceStatus(0), equalTo("configured"))
		.body(config.getDeivceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeviceIp(0), equalTo(txIp))
		
		.body(config.getDeviceDeviceType(1), equalTo("receiver"))
		.body(config.getDeviceLoggedInUserUsername(0), anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null)))			//due to bug 2341
		.body(config.getDeviceLoggedInUserType(0), anyOf(equalTo("common user"),equalTo(null)))							//due to bug 2341
		.body(config.getDeviceActiveConnectionName(1, 0), equalTo(connectionName))
		.body(config.getDeviceActiveConnectionType(1, 0), equalTo("Private"))
		.body(config.getDeviceActiveConnectinoGroup(1,0), equalTo("ConnectViaTx"))
		.body(config.getDeviceState(1), equalTo("OnLine"))
		.body(config.getDeviceStatus(1), equalTo("configured"))
		.body(config.getDeivceDeviceName(1), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceMac(1), equalTo(rxEmerald.getMac()))
		.body(config.getDeviceIp(1), equalTo(rxIp)).extract().response();	
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test04_receiverOutOfConnection() throws InterruptedException {
		ApplianceStatusConfig.GetStats stats = config.new GetStats();
		stats.device_names = new String[1];
		stats.device_names[0] = rxEmerald.getDeviceName();
		
		device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Thread.sleep(30000);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(stats)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceType(0), equalTo("receiver"))
		.body(config.getDeviceLoggedInUserUsername(0), anyOf(equalTo("Boxilla"), equalTo("admin"), equalTo(null)))			//due to bug 2341
		.body(config.getDeviceLoggedInUserType(0), anyOf(equalTo("common user"),equalTo(null)))							//due to bug 2341
		.body("message.devices[0].active_connections", Matchers.hasSize(0))	
		.body(config.getDeviceState(0), equalTo("OnLine"))
		.body(config.getDeviceStatus(0), equalTo("configured"))
		.body(config.getDeivceDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceMac(0), equalTo(rxEmerald.getMac()))
		.body(config.getDeviceIp(0), equalTo(rxIp)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test05_transmitterOutOfConnection() throws InterruptedException {
		ApplianceStatusConfig.GetStats stats = config.new GetStats();
		stats.device_names = new String[1];
		stats.device_names[0] = txEmerald.getDeviceName();
		
		device.rebootDeviceSSH(txIp, deviceUserName, devicePassword, 0);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(stats)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceType(0), equalTo("transmitter"))
		.body("message.devices[0].active_connections", Matchers.hasSize(0))
		.body(config.getDeviceState(0), equalTo("OnLine"))
		.body(config.getDeviceStatus(0), equalTo("configured"))
		.body(config.getDeivceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeviceIp(0), equalTo(txIp)).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test06_getStatusSingleDeviceInZone() {
		 CreateZoneConfig createZone = new CreateZoneConfig();
		 EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
		 String zoneName = "test06SingleDeviceZone";
		 createZone.createZone(zoneName, "this is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		 String[] receivers = {rxEmerald.getDeviceName()};
		 editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		 
		 ApplianceStatusConfig.GetStats stats = config.new GetStats();
			stats.device_names = new String[1];
			stats.device_names[0] = rxEmerald.getDeviceName();
			
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.body(stats)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message.devices[0].zone", equalTo(zoneName)).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			
			editReceiver.removeAllReceivers(zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
			log.info("Deleting all zones to clean up");
			response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all zones.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
	}
	
	

}
