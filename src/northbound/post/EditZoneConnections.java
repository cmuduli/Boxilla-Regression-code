package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.GetIndividualZoneConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneConnectionsConfig;
import northbound.post.config.EditZoneConnectionsConfig.EditConnections;

public class EditZoneConnections extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditZoneConnections.class);
	private CreateZoneConfig config = new CreateZoneConfig();
	private GetIndividualZoneConfig getZoneConfig = new GetIndividualZoneConfig();
	private ZoneMethods zoneMethods = new ZoneMethods();
	private CreateKvmConnectionsConfig createConConfig = new CreateKvmConnectionsConfig();
	private EditZoneConnectionsConfig editZoneConConfig = new EditZoneConnectionsConfig();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	 
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
		
		
		log.info("Deleting all zones before starting testcases");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);

	}
	
	@Test
	public void test01_addSingleConnectionToZone() throws InterruptedException {
		String[] connectionName = {"test01ZoneCon"};
		String zoneName = "test01Zone";
				
		createConConfig.createViaTxConnection(connectionName[0], txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		config.createZone(zoneName, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		editZoneConConfig.editZoneConnections(zoneName, connectionName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		log.info("Checking connection was assigned to zone in UI");
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName[0], driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		Thread.sleep(1204);
	}
	
	@Test
	public void test02_addMultipleConnectionsToZone() throws InterruptedException {
		String[] connectionNames = {"test02Con1", "test02Con2", "test02Con3", "test02Con4", "test02Con5"};
		String zoneName = "test02Zone";
		
		for(String s : connectionNames) {
			createConConfig.createViaTxConnection(s, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
					"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		}
		
		config.createZone(zoneName, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		for(String s : connectionNames) {
			boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, s, driver);
			Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
	
		}
		Thread.sleep(955);
		
	}
	
	@Test
	public void test03_removeConnectionFromZone() throws InterruptedException {
		String[] connectionNames = {"test03ConZone"};
		String zoneName = "test03Zone";
		createConConfig.createViaTxConnection(connectionNames[0], txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		config.createZone(zoneName, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionNames[0], driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		String[] unassignCons = {};
		editZoneConConfig.editZoneConnections(zoneName, unassignCons, boxillaManager, boxillaRestUser, boxillaRestPassword);
		isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionNames[0], driver);
		Assert.assertTrue(isAssigned, "Connection was not unassigned from zone");
		Thread.sleep(1392);
			
	}
	
	@Test
	public void test04_removeMultipleConnectionsToZone() throws InterruptedException {
		String[] connectionNames = {"test04Con1", "test04Con2", "test04Con3", "test04Con4", "test04Con5"};
		String zoneName = "test04Zone";
		
		for(String s : connectionNames) {
			createConConfig.createViaTxConnection(s, txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
					"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		}
		
		config.createZone(zoneName, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		for(String s : connectionNames) {
			boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, s, driver);
			Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		}
		
		String[] unassignCons = {};
		editZoneConConfig.editZoneConnections(zoneName, unassignCons, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		for(String s : connectionNames) {
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, s, driver);
		Assert.assertTrue(isAssigned, "Connection was not unassigned from zone");
		}
//		Thread.sleep(1955);

	}
	
	@Test
	public void test05_invalidZoneName() throws InterruptedException {
		String[] connectionNames = {"test05Con1"};
		createConConfig.createViaTxConnection(connectionNames[0], txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditConnections editCon = editZoneConConfig.new EditConnections();
		editCon.zone_name = "invalidZone";
		editCon.connection_names = connectionNames;
		
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(editCon)
		.post(editZoneConConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Zone invalidZone does not exist."));
//		Thread.sleep(931);
	}
	
	@Test
	public void test06_invalidConnectionName() throws InterruptedException {
		String[] connectionNames = {"invalidConnectionName"};
		String zoneName = "test06Zone";
		
		config.createZone(zoneName, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditConnections editCon = editZoneConConfig.new EditConnections();
		editCon.zone_name = zoneName;
		editCon.connection_names = connectionNames;
		
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(editCon)
		.post(editZoneConConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following connection does not exist: [\"invalidConnectionName\"]."));
//		Thread.sleep(452);
	}
	
//	@Test
	public void test07_swapConnectionBetweenZones() throws InterruptedException {
//		String zoneName1 = "test07Zone1";
//		String zoneName2 = "test07Zone2";
//		
//		String[] connectionNames = {"test07ZoneCon"};
//		config.createZone(zoneName1, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		config.createZone(zoneName2, "A zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		createConConfig.createViaTxConnection(connectionNames[0], txIp, "Private", "No", "ConnectViaTx", "No", "No", "No", "No",
//				"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
//		
//		editZoneConConfig.editZoneConnections(zoneName1, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName1, connectionNames[0], driver);
//		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		
//		editZoneConConfig.editZoneConnections(zoneName2, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
//		 isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName2, connectionNames[0], driver);
//		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		
//		 isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName1, connectionNames[0], driver);
//		Assert.assertFalse(isAssigned, "Connection was assigned to zone");	
		Thread.sleep(1055);
	}
	
	@Test
	public void test08_addVMConnectionToZone() throws InterruptedException {
		String zoneName = "test08Zone";
		String connectionName = "vmZoneTest";
		createConConfig.createVmConnection(connectionName, "No", "No", "No", "No", "156", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No", "", 
				boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		String[] connectionNames = {connectionName};
		
		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		 boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionNames[0], driver);
			Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		Thread.sleep(954);
	}
	
	@Test
	public void test09_addVMHorizonConnectionToZone() throws InterruptedException {
		String zoneName = "test09Zone";
		String connectionName = "vmHorizonZoneTest";
		createConConfig.createVMHorizonConnection(connectionName, "Private", txIp, "No", "test", "test", "PCoIP",
				"",boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		String[] connectionNames = {connectionName};
		
		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		 boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionNames[0], driver);
			Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		Thread.sleep(838);
	}
	
	@Test
	public void test10_addTXPairConnectionToZone() throws InterruptedException {
		String zoneName = "test10Zone";
		String connectionName = "txPairZoneCon";
		createConConfig.createPairConnection(connectionName, "Private", txIp, txIpDual, "Yes", "1", "H12",
				"2", "No", "No", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		String[] connectionNames = {connectionName};
		
		config.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		editZoneConConfig.editZoneConnections(zoneName, connectionNames, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		 boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionNames[0], driver);
			Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
//		Thread.sleep(956);
	}
	
////@Override
//	@BeforeMethod(alwaysRun = true)
//	@Parameters({ "browser" })
//	public void login(String browser, Method method) throws InterruptedException {
//		log.info("Deleting all connections");
//		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
//		
//		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
//		
//		log.info("Deleting all zones before starting testcases");
//		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully deleted all zones.")).extract().response();
//		
//		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/zones/all", REQUEST_TYPE.DELETE, response);
		
//		super.login(browser, method);
		
		
//	}
	@AfterClass
	public void afterClass() throws InterruptedException {
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
		
		
		log.info("Deleting all zones before starting testcases");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
		printSuitetDetails(true);
	}
	
}
