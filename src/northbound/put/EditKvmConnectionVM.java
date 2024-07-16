package northbound.put;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.put.config.EditKvmConnectionsConfig;
import northbound.put.config.EditKvmConnectionsConfig.EditConnection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.put.config.EditKvmConnectionsConfig;

public class EditKvmConnectionVM extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditKvmConnectionVM.class);
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

		String name = "editVMCon01";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		String new_name = name + "new";
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, new_name, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(new_name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, new_name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}

	@Test 
	public void test02_editExtDeskOnOff() throws InterruptedException {

		String name = "editVMCon02";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		extDesktop = "No";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}

	@Test
	public void test03_editExtDeskOffOn() throws InterruptedException {

		String name = "editVMCon03";
		String extDesktop = "No";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		extDesktop = "Yes";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	
	@Test
	public void test04_editUsbOnOff() throws InterruptedException {

		String name = "editVMCon04";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		usb = "No";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	@Test
	public void test05_editUsbOffOn() throws InterruptedException {

		String name = "editVMCon05";
		String extDesktop = "Yes";
		String usb = "No";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		usb = "Yes";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test06_editNlaOnOff() throws InterruptedException {

		String name = "editVMCon06";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		nla = "No";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test07_editNlaOffOn() throws InterruptedException {

		String name = "editVMCon07";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "No";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		nla = "Yes";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "",  boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test08_editViewOnOff() throws InterruptedException {

		String name = "editVMCon08";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		view = "No";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test09_editViewOffOn() throws InterruptedException {

		String name = "editVMCon09";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "No";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		view = "Yes";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test10_editPort() throws InterruptedException {

		String name = "editVMCon10";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		port = "5556";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test11_editPortInvalidLargeNumber() throws InterruptedException {

		String name = "editVMCon11";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		port = "555622";
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.extended_desktop = extDesktop;
		edit.usb_redirection = usb;
		edit.nla = nla;
		edit.view_only = view;
		edit.port = port;
		edit.domain = domain;
		edit.host = host;
		edit.username = username;
		edit.password = password;
		edit.connection_type = connection_type;
		edit.audio = audio;
		edit.group = "VM";
		edit.zone = "";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"port\"=>\"" + port + "\"}." )).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		
		
	}
	
	@Test
	public void test12_editPortInvalidText() throws InterruptedException {

		String name = "editVMCon12";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		port = "port";
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.extended_desktop = extDesktop;
		edit.usb_redirection = usb;
		edit.nla = nla;
		edit.view_only = view;
		edit.port = port;
		edit.domain = domain;
		edit.host = host;
		edit.username = username;
		edit.password = password;
		edit.connection_type = connection_type;
		edit.audio = audio;
		edit.group = "VM";
		edit.zone = "";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"port\"=>\"" + port + "\"}." )).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test13_editDomain() throws InterruptedException {

		String name = "editVMCon13";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		domain = "new domain";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test14_editHost() throws InterruptedException {

		String name = "editVMCon14";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		host = "newHostname";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test15_editUsername() throws InterruptedException {

		String name = "editVMCon15";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		username = "newUsername";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "",  boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test16_editPassword() throws InterruptedException {

		String name = "editVMCon16";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio,  "",boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		password = "newPassword";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "",  boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test17_editConnectionPrivateShared() throws InterruptedException {

		String name = "editVMCon17";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		connection_type = "Shared";
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.extended_desktop = extDesktop;
		edit.usb_redirection = "No";
		edit.nla = nla;
		edit.view_only = view;
		edit.port = port;
		edit.domain = domain;
		edit.host = host;
		edit.username = username;
		edit.password = password;
		edit.connection_type = connection_type;
		edit.audio = audio;
		edit.group = "VM";
		edit.zone = "";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Parameters {\"connection_type\"=>\"Shared\"} are incompatible with VM connection " + name + "." )).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test18_editAudioOnOff() throws InterruptedException {

		String name = "editVMCon18";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "Yes";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		audio = "No";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
	
	@Test
	public void test19_editAudioOffOn() throws InterruptedException {

		String name = "editVMCon19";
		String extDesktop = "Yes";
		String usb = "Yes";
		String nla = "Yes";
		String view = "Yes";
		String port = "333";
		String domain = "blackbox.com";
		String host = txIp;
		String username = "test";
		String password = "test";
		String connection_type = "Private";
		String audio = "No";
		
		conConfig.createVmConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		audio = "Yes";
		config.editVMConnection(name, extDesktop, usb, nla, view, port, domain, host, username, password,
				connection_type, audio, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		conStatusConfig.checkVMConnection(name, extDesktop, usb, nla, view, port, domain, host,
				username, password, connection_type, audio, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		conMethods.checkConnectionOptionsVM(driver, name, "VM", connection_type, extDesktop, audio, usb, nla, view);
	}
}
