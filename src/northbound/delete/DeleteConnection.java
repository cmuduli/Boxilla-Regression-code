package northbound.delete;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.Device;
import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import objects.Connections;

public class DeleteConnection extends StartupTestCase {

	private CreateKvmConnectionsConfig conConfig = new CreateKvmConnectionsConfig();
	private ConnectionStatusConfig conStatusConfig = new ConnectionStatusConfig();
	private ConnectionsMethods methods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private UsersMethods userMethods = new UsersMethods();
//	private ConnectionsMethods con=new ConnectionsMethods();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	public class DeleteCon {
		String[] connection_names;
	} 
	@Test
	public void test01_deleteSingleConnection() {
		//create the connection
		String name = "test01Delete";
		String host = txIp;
		String connection_type = "Private";
		String view_only = "Yes";
		String group = "ConnectViaTx";
		String ext_desk = "Yes";
		String audio =  "Yes";
		String persistent = "Yes";
		String usb = "Yes";
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check the connection exists
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//delete connection
		DeleteCon con = new DeleteCon();
		con.connection_names = new String[1];
		con.connection_names[0] = name;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted connection [\"" + name + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
		
		
		//check connection is deleted
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(conStatusConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following connection does not exist: [\"" + name + "\"].")).extract().response();
		SaveResponseStatistics(conStatusConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	@Test
	public void test02_deleteMultipleConnections() {
		String name = "test02Delete1";
		String host = txIp;
		String connection_type = "Private";
		String view_only = "Yes";
		String group = "ConnectViaTx";
		String ext_desk = "Yes";
		String audio =  "Yes";
		String persistent = "Yes";
		String usb = "Yes";
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check the connection exists
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		String name2 = "test02Delete2";
		conConfig.createViaTxConnection(name2, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check the connection exists
		conStatusConfig.checkViaTxConnection(name2, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb,  "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		String name3 = "test02Delete3";
		conConfig.createViaTxConnection(name3, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check the connection exists
		conStatusConfig.checkViaTxConnection(name3, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		DeleteCon con = new DeleteCon();
		con.connection_names = new String[3];
		con.connection_names[0] = name;
		con.connection_names[1] = name2;
		con.connection_names[2] = name3;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted connections [\"test02Delete1\", \"test02Delete2\", \"test02Delete3\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
		
		String json = "{ \"connection_names\": [\"" + name + "\", \"test02Delete2\", \"test02Delete3\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(conStatusConfig.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following connections do not exist: [\"" + name + "\", \"test02Delete2\", \"test02Delete3\"].")).extract().response();
	
		SaveResponseStatistics(conStatusConfig.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test03_deleteConnectionNotExist() {
		DeleteCon con = new DeleteCon();
		con.connection_names = new String[1];
		con.connection_names[0] = "noExist";
		
		Response response =  given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following connection does not exist: [\"" + con.connection_names[0] + "\"].")).extract().response();
	
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
	}
	
	@Test
	public void test04_deleteNonUnique() throws InterruptedException {
		//create template based connection
		String template = "testTem04Del";
		String connection = template + "_con";
		methods.masterCreateTemplate(template, "tx", "private", "true", "true", "true", "true",
				"true", driver);
		
		methods.addConnection(driver, connection, "yes");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		methods.selectTemplateForConnection(driver, template, "tx", connection);
		DeleteCon con = new DeleteCon();
		con.connection_names = new String[1];
		con.connection_names[0] = connection;
		
		Response response =  given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(403)
		.body("message", equalTo("The following connections are with non-unique properties: [\"testTem04Del_con\"].")).extract().response();
	
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
	}
	
	@Test
	public void test05_deleteActiveConnection() throws InterruptedException {
		//create connection
		String name = "test05Delete";
		String host = txIp;
		String connection_type = "Private";
		String view_only = "Yes";
		String group = "ConnectViaTx";
		String ext_desk = "Yes";
		String audio =  "Yes";
		String persistent = "No";
		String usb = "Yes";
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, ext_desk,
				audio, persistent, usb, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		//add connection to user and update xml file
		userMethods.addAllConnectionsToUser(driver, "admin");
		deviceMethods.recreateCloudData(rxIp);
		deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Thread.sleep(30000);
		//con.createRealConnection(name,deviceUserName, devicePassword, rxIp, txIp);

		//String body = getBody("admin", name, rxSingle.getDeviceName(), "admin");
		String body = getBody("admin", name, rxEmerald.getDeviceName(), "admin");
		System.out.println(body);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(body)
		.post("https://" + boxillaManager  + "/bxa-api/connections/kvm/active")
		.then().assertThat().statusCode(201)
		.body("message", containsString("Active connection " + name + " is successfully launched.")).extract().response();
		waitForBoxillaPoll();	//sleep and wait for the next poll before checking connection
		DeleteCon con = new DeleteCon();
		con.connection_names = new String[1];
		con.connection_names[0] = name;
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm/active", REQUEST_TYPE.POST, response);
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(409)
		.body("message", equalTo("Fail to delete connection [\"" + name + "\"] because one or more connections are active.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
		
		//reboot device to kill connection and then delete
		deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		waitForBoxillaPoll();
		 response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted connection [\"" + name + "\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/connections/kvm", REQUEST_TYPE.DELETE, response);
	}
	private String getBody(String username, String connectionName, String receiverName, String password) {
		String body = "{\"username\": \""+ username + "\",\"password\": \"" + password  + "\",\"connection_name\": \"" + connectionName + "\", \"receiver_name\":\"" + receiverName + "\"}";
		return body;
	}
}
