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

public class EditKvmConnectionPool extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditKvmConnectionPool.class);
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
	public void test01_editName() throws InterruptedException {
		String name = "editPool01";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";
		String new_name = name + "new";
		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, new_name, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(new_name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, new_name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	@Test
	public void test02_editExtDeskOnOff() throws InterruptedException {
		String name = "editPool02";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		extended_desktop = "No";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	@Test
	public void test03_editExtDeskOffOn() throws InterruptedException {
		String name = "editPool03";
		String extended_desktop = "No";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		extended_desktop = "Yes";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	@Test
	public void test04_editUsbOnOff() throws InterruptedException {
		String name = "editPool04";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		usb_redirection = "No";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	@Test
	public void test05_editUsbOffOn() throws InterruptedException {
		String name = "editPool05";
		String extended_desktop = "Yes";
		String usb_redirection = "No";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		usb_redirection = "Yes";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	@Test
	public void test06_editViewOnOff() throws InterruptedException {
		String name = "editPool06";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		view_only = "No";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	@Test
	public void test07_editViewOffOn() throws InterruptedException {
		String name = "editPool07";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "No";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		view_only = "Yes";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	
	
	@Test
	public void test09_editAudioOnOff() throws InterruptedException {
		String name = "editPool09";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "Yes";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		audio = "No";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	@Test
	public void test10_editAudioOffOn() throws InterruptedException {
		String name = "editPool10";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "No";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		audio = "Yes";
		config.editPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, null, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPoolConnections(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVMPool(driver, name, "VMPool", connection_type, extended_desktop,
				audio, usb_redirection, view_only);	
	}
	
	@Test
	public void test11_editConnectionsType() throws InterruptedException {
		String name = "editPool11";
		String extended_desktop = "Yes";
		String usb_redirection = "Yes";
		String view_only = "Yes";
		String host = txIp;

		String connection_type = "Private";
		String audio = "No";

		conConfig.createPoolConnection(name, extended_desktop, usb_redirection, view_only, host,
				connection_type, audio, boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.extended_desktop = extended_desktop;
		edit.usb_redirection = usb_redirection;
		edit.view_only = view_only;
		
		edit.connection_type = "Shared";
		edit.audio = audio;
		edit.group = "VMPool";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Parameters {\"connection_type\"=>\"Shared\"} are incompatible with VMPool connection " + name + "." )).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		
	}
}
