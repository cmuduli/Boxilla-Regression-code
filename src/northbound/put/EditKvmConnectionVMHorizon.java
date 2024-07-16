package northbound.put;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.put.config.EditKvmConnectionsConfig;
import northbound.put.config.EditKvmConnectionsConfig.EditConnection;

public class EditKvmConnectionVMHorizon extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditKvmConnectionVMHorizon.class);
	EditKvmConnectionsConfig config = new EditKvmConnectionsConfig();
	CreateKvmConnectionsConfig conConfig = new CreateKvmConnectionsConfig();
	private ConnectionStatusConfig conStatusConfig = new ConnectionStatusConfig();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test
	public void test01_editConnectionName() {
		String name = "editHorizon01";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, new_name, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(new_name, connection_type, host, view_only, username,
				password, protocol, "", boxillaManager,  boxillaRestUser, boxillaRestPassword);	
	}
	
	@Test
	public void test02_editConnectionHost() {
		String name = "editHorizon02";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		host = "hostName";
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(name, connection_type, host, view_only, username,
				password, protocol, "", boxillaManager, boxillaRestUser, boxillaRestPassword);	
	}
	
	@Test
	public void test03_editConnectionViewOnlyOnOff() {
		String name = "editHorizon03";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol,"", boxillaManager, boxillaRestUser, boxillaRestPassword);
		view_only = "No";
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(name, connection_type, host, view_only, username,
				password, protocol, "", boxillaManager, boxillaRestUser, boxillaRestPassword);	
	}
	@Test
	public void test04_editConnectionViewOnlyOffOn() {
		String name = "editHorizon04";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "No";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol, "",boxillaManager, boxillaRestUser, boxillaRestPassword);
		view_only = "Yes";
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, null,"", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(name, connection_type, host, view_only, username,
				password, protocol, "", boxillaManager, boxillaRestUser, boxillaRestPassword);	
	}
	@Test
	public void test05_editConnectionUsername() {
		String name = "editHorizon05";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol,"", boxillaManager, boxillaRestUser, boxillaRestPassword);
		username = "myusername";
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(name, connection_type, host, view_only, username,
				password, protocol, "",  boxillaManager, boxillaRestUser, boxillaRestPassword);	
	}
	
	@Test
	public void test06_editConnectionPassword() {
		String name = "editHorizon06";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol,"", boxillaManager, boxillaRestUser, boxillaRestPassword);
		password = "p@ssword";
		config.editVMHorizonConnection(name, connection_type, host, view_only, username, password,
				protocol, null,"",  boxillaManager, boxillaRestUser, boxillaRestPassword);
		conStatusConfig.checkHorizonConnections(name, connection_type, host, view_only, username,
				password, protocol, "",  boxillaManager, boxillaRestUser, boxillaRestPassword);	
	}
	
	
	
	@Test
	public void test08_editConnectionProtocol() {
		String name = "editHorizon08";
		String connection_type = "Private";
		String host = txIp;
		String view_only = "Yes";
		String username = "test";
		String password = "test";
		String protocol = "PCoIP";
		String new_name = name + "new";
		
		conConfig.createVMHorizonConnection(name, connection_type, host, view_only, username, 
				password, protocol, "",boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.view_only = view_only;
		edit.username = username;
		edit.password = password;
		edit.protocol = "Invalid";
		edit.new_name = new_name;
		edit.group  = "VMHorizon";
		edit.zone = "";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"protocol\"=>\"Invalid\"}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
}
