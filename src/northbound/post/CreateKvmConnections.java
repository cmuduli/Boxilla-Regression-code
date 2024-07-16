package northbound.post;

import org.testng.annotations.BeforeClass;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

import java.io.IOException;
import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import static io.restassured.RestAssured.basic;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import methods.ZoneMethods;
import northbound.get.ApplianceStatus;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import objects.Connections;

public class CreateKvmConnections extends StartupTestCase {

	private CreateKvmConnectionsConfig config = new CreateKvmConnectionsConfig();
	private ConnectionStatusConfig connectionConfig = new ConnectionStatusConfig();
	private static int counter = 0;
	private String datafilePair2 = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\Pair2Target.txt";
	private String datafilePair = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\Pair1Target.txt";
	private String datafile = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\viaTx.txt";
	private String datafileVM = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\VM.txt";
	private String datafilePool = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\Pool.txt";
	private String datafileEmptyTx = "C:\\Test_Workstation\\SeleniumAutomation\\src\\northbound\\post\\emptyTx.txt";
	final static Logger log = Logger.getLogger(CreateKvmConnections.class);
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods devices = new DevicesMethods();
	
	

	@DataProvider(name="datafilePair2")
	public Object[][] createDataPair2Target() throws IOException {
		return readData(datafilePair2);
	}
	
	@DataProvider(name = "datafilePair")
	public Object[][] createUserDataPair1Target() throws IOException {
		return readData(datafilePair);
	}
	@DataProvider(name = "createEmptyTx")
	public Object[][] createUserDataEmptyTx() throws IOException {
		return readData(datafileEmptyTx);
	}
	
	@DataProvider(name = "createviatx")
	public Object[][] createUserData() throws IOException {
		return readData(datafile);
	}
	
	@DataProvider(name = "createVM")
	public Object[][] createUserDataVM() throws IOException {
		return readData(datafileVM);
	}
	
	@DataProvider(name = "createPool")
	public Object[][] createUserDataPool() throws IOException {
		return readData(datafilePool);
	}
	

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections.")).extract().response();
	}
	
	@Test
	public void test10_VMHorizonInvalidProtocol() {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = "test10_VMHorizon";
		con.group  = "VMHorizon";
		con.connection_type = "Private";
		con.host = txIp;
		con.view_only = "No";
		con.username = "test";
		con.password = "test";
		con.protocol = "TCoIP";
		con.zone = "";
		
		log.info("Creating connection ");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message",equalTo("Invalid parameter: {\"protocol\"=>\"TCoIP\"}.")).extract().response();
	}
	
	@Test
	public void test09_VMHorizonConnection() {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = "test09_VMHorizon";
		con.group  = "VMHorizon";
		con.connection_type = "Private";
		con.host = txIp;
		con.view_only = "No";
		con.username = "test";
		con.password = "test";
		con.protocol = "PCoIP";
		con.zone = "";
		
		log.info("Creating connection ");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created VMHorizon connection " + con.name + ".")).extract().response();
		
		log.info("Checking if connection is created");
		String json = "{ \"connection_names\": [\"" + con.name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(con.name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(con.view_only))
		.body("message.connections[0].group", equalTo(con.group))
		.body("message.connections[0].protocol", equalTo(con.protocol))
		.body("message.connections[0].username", equalTo(con.username)).extract().response();

	}
	
	@Test
	public void test08_duplicateHostNamePairedConnection() {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		String source = "1";
		con.name = "test08_deplicate";
		con.group  = "TXPair";
		con.connection_type = "Private";
		con.host = txIp;
		con.host_2 = txIp;
		con.audio = "Yes";
		Integer aSource = 0;
		con.zone = "";
		
		if(con.audio.equals("No")) {
			aSource = null;
			con.audio_source = null;
		}else {
			con.audio_source = source;
			aSource = Integer.parseInt(source);
		}
		//con.orientation = "H21";
		con.pairing_type = "2";
		con.persistent = "No";
		con.view_only = "No";
		con.orientation = "H12";
	
		
		log.info("Creating connection through rest");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Parameters {\"host\"=>\"" + con.host + "\", \"host_2\"=>\"" + con.host_2 + "\"} are incompatible with TXPair connection " + con.name + "." )).extract().response();
	}
	
	@Test
	public void test07_duplicateConnection() {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = "test07";
		con.group  = "ConnectViaTx";
		con.connection_type = "Private";
		con.host = txIp;
		con.audio = "Yes";
		con.extended_desktop = "Yes";
		con.persistent = "Yes";
		con.view_only = "Yes";
		con.usb_redirection = "Yes";
		con.cmode = "10";
		con.zone = "";
		 
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201).extract().response();
		
		String json = "{ \"connection_names\": [\"" + con.name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(con.name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(con.view_only))
		.body("message.connections[0].group", equalTo("ConnectViaTx"))
		.body("message.connections[0].extended_desktop", equalTo(con.extended_desktop))
		.body("message.connections[0].audio", equalTo(con.audio))
		.body("message.connections[0].persistent", equalTo(con.persistent))
		.body("message.connections[0].usb_redirection", equalTo(con.usb_redirection))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
		//try to create connection with smae name but different options
		con.usb_redirection = "No";
		con.connection_type = "Shared";
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Duplicate name received.")).extract().response();
	}
	
	@Test
	public void test06_nameSpecialCharacters() {
		
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = "*Brendan";
		con.group  = "ConnectViaTx";
		con.connection_type = "Private";
		con.host = txIp;
		con.audio = "Yes";
		con.extended_desktop = "Yes";
		con.persistent = "Yes";
		con.view_only = "Yes";
		con.usb_redirection = "Yes";
		con.cmode = "10";
		con.zone = "";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"name\"=>\"*Brendan\"}.")).extract().response();
		
	}
	
//	@Test(dataProvider="datafilePair2")
	public void test05_create2TargetPairCon(String name, String pairingType, String type, String audio, String source, String persistent, String view,
			String orientation ) {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = name;
		con.group  = "TXPair";
		con.connection_type = type;
		con.host = txIp;
		con.host_2 = txIpDual;
//		con.host_2 = txIpExtra;
		con.audio = audio;
		Integer aSource = 0;
		con.zone = "";
		
		if(audio.equals("No")) {
			aSource = null;
			con.audio_source = null;
		}else {
			con.audio_source = source;
			aSource = Integer.parseInt(source);
		}
		//con.orientation = "H21";
		con.pairing_type = pairingType;
		con.persistent = persistent;
		con.view_only = view;
		con.orientation = orientation;
	
		
		log.info("Creating connection through rest");
		Response response =  given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo(con.group))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].audio_source", equalTo(aSource))
		.body("message.connections[0].host_2", equalTo(txIpDual))
		.body("message.connections[0].pairing_type", equalTo(con.pairing_type))
		.body("message.connections[0].persistent", equalTo(con.persistent))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
	}
	
//	@Test(dataProvider="datafilePair")
	public void test04_create1TargetPairCon(String name, String pairingType, String type, String audio, String source, String persistent, String view) {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = name;
		con.group  = "TXPair";
		con.connection_type = type;
		con.host = txIp;
		con.host_2 = txIpDual;
//		con.host_2 = txIpExtra;
		con.audio = audio;
		Integer aSource = 0;
		con.zone = "";
		
		if(audio.equals("No")) {
			aSource = null;
			con.audio_source = null;
		}else {
			con.audio_source = source;
			aSource = Integer.parseInt(source);
		}
		//con.orientation = "H21";
		con.pairing_type = pairingType;
		con.persistent = persistent;
		con.view_only = view;
	
		
		log.info("Creating connection through rest");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo(con.group))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].audio_source", equalTo(aSource))
		.body("message.connections[0].host_2", equalTo(txIpDual))
		.body("message.connections[0].pairing_type", equalTo(con.pairing_type))
		.body("message.connections[0].persistent", equalTo(con.persistent))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}

	
	
	@Test(dataProvider="createPool")
	public void test03_createPool(String name, String extDesk, String usb, String audio, String view) throws InterruptedException {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection ();
		con.name = name;
		con.extended_desktop = extDesk;
		con.usb_redirection = usb;
		con.view_only = view;
		con.group = "VMPool";
		con.connection_type = "Private";
		con.audio = audio;
		con.zone = "";
		
		
		log.info("Creating VMPool connection");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		log.info("Checking connection in REST");
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(""))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo(con.group))
		.body("message.connections[0].extended_desktop", equalTo(extDesk))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		log.info("Checking connection in boxilla UI");
		connections.checkConnectionOptionsVMPool(driver, name, con.group, con.connection_type, extDesk, audio, usb,  view);
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test(dataProvider="createVM")
	public void test02_createVM(String name, String extDesktop, String usb,String audio, String nla, String view) throws InterruptedException {
		
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection ();
		con.name = name;
		con.extended_desktop = extDesktop;
		con.usb_redirection = usb;
		con.nla = nla;
		con.view_only = view;
		con.port = "556";
		con.domain = "blackbox";
		con.host = txIp;
		con.username = "test";
		con.password = "test";
		con.group = "VM";
		con.connection_type = "Private";
		con.audio = audio;
		con.zone = "";
		
		log.info("Creating VM connection");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
		log.info("Checking VM connection through REST");
		
		ConnectionStatusConfig.GetConnectionStatus conStatus = connectionConfig.new GetConnectionStatus();
		conStatus.connection_names = new String[1];
		conStatus.connection_names[0] = name;
		
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(conStatus)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(con.connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo(con.group))
		.body("message.connections[0].extended_desktop", equalTo(extDesktop))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].port", equalTo(con.port))
		.body("message.connections[0].domain", equalTo(con.domain))
		.body("message.connections[0].nla", equalTo(nla))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		log.info("Checking VM connection in boxilla UI");
		connections.checkConnectionOptionsVM(driver, name, con.group, con.connection_type, extDesktop, audio, usb, nla, view);
		
	}
	@Test(dataProvider="createviatx")
	public void test01_createViaTx(String name, String privilege, String extDesk, String view, String usb, String audio, String persistence) throws InterruptedException {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = name;
		con.group  = "ConnectViaTx";
		con.connection_type = privilege;
		con.host = txIp;
		con.audio = audio;
		con.extended_desktop = extDesk;
		con.persistent = persistence;
		con.view_only = view;
		con.usb_redirection = usb;
		con.cmode = "10";
		con.zone = "";
		
		log.info("Creating connection through rest");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		log.info("Connection created OK- 201");
		log.info("Checking connection through rest");
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(connectionConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(txIp))
		.body("message.connections[0].connection_type", equalTo(privilege))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo("ConnectViaTx"))
		.body("message.connections[0].extended_desktop", equalTo(extDesk))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].persistent", equalTo(persistence))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		log.info("Connection check OK - 200");
		log.info("Checking connection in Boxilla UI");
		connections.checkConnectionOptionsViaTx(driver, name, con.group, con.connection_type, extDesk, audio, usb, persistence, view);
	}
	
	@Test(dataProvider="createEmptyTx")
	public void test04_createViaTxEmptyValues(String name, String group, String type, String host, String audio, String extDesk, 
			String persistence, String view, String usb, String failedParameter) {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = name;
		con.group  = group;
		con.connection_type = type;
		con.host = host;
		con.audio = audio;
		con.extended_desktop = extDesk;
		con.persistent = persistence;
		con.view_only = view;
		con.usb_redirection = usb;
		con.cmode = "10";
		con.zone = "";
		
		log.info("Creating connection through rest");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"" + failedParameter + "\"=>\"\"}.") ).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
	}
	
	//@Test
	public void test05_createAndLaunchConnection() throws InterruptedException {
		CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
		con.name = "test05Launch";
		con.group  = "ConnectViaTx";
		con.connection_type = "Private";
		con.host = txIp;
		con.audio = "Yes";
		con.extended_desktop = "Yes";
		con.persistent = "Yes";
		con.view_only = "Yes";
		con.usb_redirection = "Yes";
		con.zone = "";

		
		log.info("Creating connection through rest");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(config.getUri(boxillaManager))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
		devices.recreateCloudData(rxIp);
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		log.info("Launch connection through Boxilla UI");
		
		
		String[] connectionSources = {con.name};
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, con.name, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, con.name, singleRxName).isDisplayed();
		//Assert connection has been created
		log.info("Asserting if connections has been created between " + con.name + " and " + singleRxName);
		Assert.assertTrue(isConnection, "Connection between " + con.name + " and " + singleRxName + " is not displayed");	
		
	}

	
	@Test
	public void test11_createTxConnectionInZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test11Zone";
		String connectionName  = "test11ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		config.createViaTxConnection(connectionName, txIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connectionConfig.checkViaTxConnection(connectionName, txIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		
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
	public void test12_createVmConnectionInZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test12Zone";
		String connectionName  = "test12ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		config.createVmConnection(connectionName, "Yes", "Yes", "Yes", "Yes", "333", "blackbox.com", "1.1.1.1",
				"test", "test", "Private", "Yes", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);

		connectionConfig.checkVMConnection(connectionName, "Yes", "Yes", "Yes", "Yes", "333", "blackbox.com", "1.1.1.1",
				"test", "test", "Private", "Yes", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		
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
	public void test13_createHorizonInZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test13Zone";
		String connectionName  = "test13ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		
		
		config.createVMHorizonConnection(connectionName, "Private", "1.1.1.1", "Yes", "test", 
				"test", "PCoIP", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		connectionConfig.checkHorizonConnections(connectionName, "Private", "1.1.1.1", "Yes", "test", 
				"test", "PCoIP", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);	
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		
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
	public void test14_createTXPairInZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test14Zone";
		String connectionName  = "test14ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		
		config.createPairConnection(connectionName, "Private", "1.1.1.1", "1.1.1.2", "Yes", "1", "H21", "2", 
				"Yes", "Yes", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		
		connectionConfig.checkPairedConnection(connectionName, "Private", "1.1.1.1", "1.1.1.2", "Yes", 1, "H21", "2", 
				"Yes", "Yes", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		
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

	
	
}
