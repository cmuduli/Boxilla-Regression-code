package northbound.put;

import static io.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import static org.hamcrest.Matchers.equalTo;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;
import northbound.put.config.EditKvmConnectionsConfig;

public class EditKvmConnectionsViaTx extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditKvmConnectionsViaTx.class);
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
	public void test01_editTXConnectionUSBEnabled() throws InterruptedException  {
		String name = "test01EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		usb_redirection = "Yes";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test02_editTXConnectionUSBDisabled() throws InterruptedException  {
		String name = "test02EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "Yes";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		usb_redirection = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "Optimized", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test03_editTXConnectionHost() throws InterruptedException  {
		String name = "test03EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		host = "124.122.11.11";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	@Test
	public void test04_editTXConnectionAudioEnabled() throws InterruptedException  {
		String name = "test04EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		audio = "Yes";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test05_editTXConnectionAudioDisabled() throws InterruptedException  {
		String name = "test05EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		audio = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test06_editTXConnectionExtDeskEnabled() throws InterruptedException  {
		String name = "test06EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		extended_desktop = "Yes";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test07_editTXConnectionExtDeskDisabled() throws InterruptedException  {
		String name = "test07EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "Yes";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		extended_desktop = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	
	@Test
	public void test08_editTXConnectionPersistentEnabled() throws InterruptedException  {
		String name = "test08EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		persistent = "Yes";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test09_editTXConnectionPersistentDisabled() throws InterruptedException  {
		String name = "test09EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "Yes";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		persistent = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test10_editTXConnectionViewOnlyEnabled() throws InterruptedException  {
		String name = "test10EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		view_only = "Yes";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test11_editTXConnectionViewOnlyDisabled() throws InterruptedException  {
		String name = "test11EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "Yes";
		String usb_redirection = "No";
		
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		//change usb to yes
		view_only = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test12_editTXConnectionName() throws InterruptedException  {
		String name = "test12EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "No";
		String extended_desktop = "No";
		String persistent = "No";
		String view_only = "No";
		String usb_redirection = "No";
		String new_name = "test12EditConEdited";
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
	
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				new_name, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(new_name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, new_name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	
	@Test
	public void test13_editTXConnectionTypePrivateToShared() throws InterruptedException  {
		String name = "test13EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		connection_type = "Shared";
		//for shared connection usb should be automatically disabled so setting this to no
		usb_redirection = "No";
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
//	@Test
	public void test14_editTXConnectionTypeSharedToPrivate() throws InterruptedException  {
		String name = "test14EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Shared";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "No";			//disable due to shared mode
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		connection_type = "Private";
		usb_redirection = "Yes";
		//for shared connection usb should be automatically disabled so setting this to no
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	@Test
	public void test15_editTXConnectionAllProperties() throws InterruptedException  {
		String name = "test15EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			//disable due to shared mode
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	
		
		usb_redirection = "No";
		host = "1.1.1.1";
		audio = "No";
		extended_desktop = "No";
		persistent = "No";
		view_only = "No";
		String new_name = "test15EditConEdited";
		//for shared connection usb should be automatically disabled so setting this to no
		//edit connection 
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				new_name, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection
		conStatusConfig.checkViaTxConnection(new_name, host, connection_type, view_only, group, extended_desktop,
				audio, persistent, usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//check edited connection in UI
		conMethods.checkConnectionOptionsViaTx(driver, new_name, group, connection_type, extended_desktop, audio,
				usb_redirection, persistent, view_only);
	}
	@Test
	public void test16_editTXConMissingName() {
		String name = "test16EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.group = group;
		edit.connection_type = connection_type;
		edit.group = group;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb_redirection;
		edit.zone = "";
		edit.cmode = "10";
		
		//try to edit the connection with missing connection name
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Connection  does not exist.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test17_editTXConMissingGroup() {
		String name = "test17EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;

		edit.connection_type = connection_type;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb_redirection;
		edit.cmode = "10";
		edit.zone = "";
		
		//try to edit the connection with missing connection name
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"group\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test18_editTXConMissingConnectionType() {
		String name = "test18EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.group = group;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb_redirection;
		edit.cmode = "10";
		edit.zone = "";
		
		
		//try to edit the connection with missing connection_type
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"connection_type\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test19_editTXConMissingHost() {
		String name = "test19EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.group = group;
		edit.connection_type = connection_type;

		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb_redirection;
		edit.cmode = "10";
		edit.zone = "";
		//try to edit the connection with missing connection_type
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"host\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	@Test
	public void test20_editTXConMissingViewOnly() {
		String name = "test20EditCon";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection,"10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.group = group;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;

		edit.usb_redirection = usb_redirection;
		edit.cmode = "10";
		edit.zone = "";
		
		//try to edit the connection with missing connection_type
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"view_only\"=>nil}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test21_editTXConNameDuplicate() {
		String name = "test21EditCon";
		String name2 = "test21EditCon2";
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conConfig.createViaTxConnection(name2, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.group = group;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = extended_desktop;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb_redirection;
		edit.new_name = name2;
		edit.cmode = "10";
		edit.zone = "";
		
		//try to edit the connection with missing connection_type
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(403)
		.body("message", equalTo("Cannot update connection name to test21EditCon2. Connection test21EditCon2 already exists.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test22_editTxAssignZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test22ConZoneEdit";
		String connectionName  = "test12ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		String name = connectionName;
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
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
	public void test23_editTxUnassignZone() throws InterruptedException {
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		ZoneMethods zoneMethods = new ZoneMethods();
		String zoneName = "test23ConZoneEdit";
		String connectionName  = "test12ConZone";
		zoneConfig.createZone(zoneName, "This is a zone", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		String name = connectionName;
		String group  = "ConnectViaTx";
		String connection_type = "Private";
		String host = txIp;
		String audio = "Yes";
		String extended_desktop = "Yes";
		String persistent = "Yes";
		String view_only = "Yes";
		String usb_redirection = "Yes";			
		//create connection with usb no
		conConfig.createViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent,
				usb_redirection, "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		boolean isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
		Assert.assertTrue(isAssigned, "Connection was not assigned to zone");
		
		config.editViaTxConnection(name, host, connection_type, view_only, group, extended_desktop, audio, persistent, usb_redirection,
				null, "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		 isAssigned = zoneMethods.isConnectionAssignedToZone(zoneName, connectionName, driver);
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
