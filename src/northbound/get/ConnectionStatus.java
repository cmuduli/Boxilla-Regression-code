package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import northbound.get.config.ConnectionStatusConfig;
import objects.Connections;

public class ConnectionStatus extends StartupTestCase {

	private ConnectionsMethods connections = new ConnectionsMethods();
	private ConnectionStatusConfig config = new ConnectionStatusConfig();
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		//RestAssured.authentication = basic(boxillaRestUser, boxillaRestPassword);
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/all", REQUEST_TYPE.DELETE, response);
	}
	

	
	@Test
	public void test01_getSingleConnection() throws InterruptedException {
		
		String connectionName = "test01_getSingleConnection";
		
		ConnectionStatusConfig.GetConnectionStatus con = config.new GetConnectionStatus();
		con.connection_names = new String[1];
		con.connection_names[0] = connectionName;
		connections.createMasterConnection(connectionName, "tx", "shared", "true", "true", "true", "true", "true", txIp, driver);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(con)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getConnectionName(0), equalTo(connectionName))
		.body(config.getConnectionHost(0), equalTo(txIp))
		.body(config.getConnectionConnectionType(0), equalTo("Shared"))
		.body(config.getConnectionViewOnly(0), equalTo("Yes"))
		.body(config.getConnectionGroup(0), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(0), equalTo("Yes"))
		.body(config.getConnectionAudio(0), equalTo("Yes"))
		.body(config.getConnectionPersistent(0), equalTo("Yes"))
		.body(config.getConnectionUsb(0), equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);

	}
	
	@Test
	public void test02_getMultipleConnection() throws InterruptedException {
		String connectionName1 = "test02_getMultipleConnection";
		String connectionName2 = "test02_getMultipleConnection2";
		connections.createMasterConnection(connectionName1, "tx", "private", "false", "false", "false", "false", "false", txIp, driver);
		connections.createMasterConnection(connectionName2, "tx", "private", "true", "true", "true", "true", "true", txIp, driver);
		
		ConnectionStatusConfig.GetConnectionStatus con = config.new GetConnectionStatus();
		con.connection_names = new String[2];
		con.connection_names[0] = connectionName1;
		con.connection_names[1] = connectionName2;
		

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(con)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getConnectionName(0), equalTo(connectionName1))
		.body(config.getConnectionHost(0), equalTo(txIp))
		.body(config.getConnectionConnectionType(0), equalTo("Private"))
		.body(config.getConnectionViewOnly(0), equalTo("No"))
		.body(config.getConnectionGroup(0), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(0), equalTo("No"))
		.body(config.getConnectionAudio(0), equalTo("No"))
		.body(config.getConnectionPersistent(0), equalTo("No"))
		.body(config.getConnectionUsb(0), equalTo("No"))
		
		.body(config.getConnectionName(1), equalTo(connectionName2))
		.body(config.getConnectionHost(1), equalTo(txIp))
		.body(config.getConnectionConnectionType(1), equalTo("Private"))
		.body(config.getConnectionViewOnly(1), equalTo("Yes"))
		.body(config.getConnectionGroup(1), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(1), equalTo("Yes"))
		.body(config.getConnectionAudio(1), equalTo("Yes"))
		.body(config.getConnectionPersistent(1), equalTo("Yes"))
		.body(config.getConnectionUsb(1), equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	//@Test
	public void test03_getAllConnections() {

		
		ConnectionStatusConfig.GetConnectionStatus con = config.new GetConnectionStatus();
		con.connection_names = null;
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(con)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		

		
		.body(config.getConnectionName(0), equalTo("test01_getSingleConnection"))
		.body(config.getConnectionHost(0), equalTo(txIp))
		.body(config.getConnectionConnectionType(0), equalTo("Shared"))
		.body(config.getConnectionViewOnly(0), equalTo("Yes"))
		.body(config.getConnectionGroup(0), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(0), equalTo("Yes"))
		.body(config.getConnectionAudio(0), equalTo("Yes"))
		.body(config.getConnectionPersistent(0), equalTo("Yes"))
		.body(config.getConnectionUsb(0), equalTo("Yes"))
		
		.body(config.getConnectionName(1), equalTo("test02_getMultipleConnection"))
		.body(config.getConnectionHost(1), equalTo(txIp))
		.body(config.getConnectionConnectionType(1), equalTo("Private"))
		.body(config.getConnectionViewOnly(1), equalTo("No"))
		.body(config.getConnectionGroup(1), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(1), equalTo("No"))
		.body(config.getConnectionAudio(1), equalTo("No"))
		.body(config.getConnectionPersistent(1), equalTo("No"))
		.body(config.getConnectionUsb(1), equalTo("No"))
		
		.body(config.getConnectionName(2), equalTo("test02_getMultipleConnection2"))
		.body(config.getConnectionHost(2), equalTo(txIp))
		.body(config.getConnectionConnectionType(2), equalTo("Private"))
		.body(config.getConnectionViewOnly(2), equalTo("Yes"))
		.body(config.getConnectionGroup(2), equalTo("ConnectViaTx"))
		.body(config.getConnectionExtDesk(2), equalTo("Yes"))
		.body(config.getConnectionAudio(2), equalTo("Yes"))
		.body(config.getConnectionPersistent(2), equalTo("Yes"))
		.body(config.getConnectionUsb(2), equalTo("Yes")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		
	}
	
}
