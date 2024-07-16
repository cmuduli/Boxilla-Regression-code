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

public class EditKvmConnectionsPaired extends StartupTestCase {

	final static Logger log = Logger.getLogger(EditKvmConnectionsPaired.class);
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
	public void test01_editConName() {
		//create connection
		String name = "test01EditPairCon";
		String group  = "TXPair";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
//		String host_2 = txIpExtra;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";
		String new_name = name + "New";
		int audioSource = Integer.parseInt(audio_source);
		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, new_name, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(new_name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test02_editConnectionTypePrivateShared() {
		String name = "test02EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";

		int audioSource = Integer.parseInt(audio_source);
		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change connection_type
		connection_type = "Shared";
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test03_editConnectionTypeSharedPrivate() {
		String name = "test03EditPairCon";
		String connection_type = "Shared";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";

		int audioSource = Integer.parseInt(audio_source);
		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change connection_type
		connection_type = "Private";
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test04_editHost1() {
		String name = "test04EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";

		int audioSource = Integer.parseInt(audio_source);
		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change host1
		host = "1.1.1.1";
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test05_editHost2() {
		String name = "test05EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";

		int audioSource = Integer.parseInt(audio_source);
		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change host1
		host_2 = "1.1.1.1";
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test06_editHostDuplicate() {
		
		String name = "test06EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change host1
		host_2 = host;
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.host_2 = host_2;
		edit.audio = audio;
		edit.audio_source = audio_source;
		edit.orientation = orientation;
		edit.pairing_type = pairing_type;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.group = "TXPair";
		edit.zone = "";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Parameters {\"host\"=>\"" + host + "\", \"host_2\"=>\"" + host_2 + "\"} are incompatible with TXPair connection "
				 + edit.name + ".")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	

	@Test
	public void test07_editAudioOnOff() {
		String name = "test07EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "Yes";
		String audio_source = "1";		
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change audio
		audio = "No";
		Integer audioSource =  0;
		audioSource = null;
		audio_source = null;
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test08_editAudioOffOn() {
		String name = "test08EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change audio
		audio = "Yes";
		Integer audioSource =  1;
		String audio_source = "1";
		config.editPairedConnection(name, connection_type, host, host_2, audio, audio_source, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, audioSource, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test09_editTargets2To1() {
		String name = "test09EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H21";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		pairing_type = "1";
		orientation = null;		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test10_editTargets1To2() {
		String name = "test10EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = null;
		String pairing_type = "1";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		pairing_type = "2";
		orientation = "H12";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test11_editOrientationH12() {
		String name = "test11EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "V12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		orientation = "H12";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test12_editOrientationH21() {
		String name = "test12EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "V12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		orientation = "H21";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test13_editOrientationV12() {
		String name = "test13EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		orientation = "V12";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test14_editOrientationV21() {
		String name = "test14EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		orientation = "V21";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null,  "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test15_editOrientationInvalid() {
		String name = "test15EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		orientation = "V22";		//no orientation when pairing_type = 1
		EditKvmConnectionsConfig.EditConnection edit = config.new EditConnection();
		edit.name = name;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.host_2 = host_2;
		edit.audio = audio;
		edit.orientation = "V22";
		edit.pairing_type = pairing_type;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.group = "TXPair";
		edit.zone = "";
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: {\"orientation\"=>\"V22\"}.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	}
	
	@Test
	public void test16_editPersistenceOnOff() {
		String name = "test16EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "Yes";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		persistent = "No";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test17_editPersistenceOffOn() {
		String name = "test17EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "No";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		persistent = "Yes";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	@Test
	public void test18_editViewOnlyOnOff() {
		String name = "test18EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "No";
		String view_only = "Yes";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		view_only = "No";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	
	@Test
	public void test19_editViewOnlyOffOn() {
		String name = "test19EditPairCon";
		String connection_type = "Private";
		String host = txIp;
		String host_2 = txIpDual;
		String audio = "No";
		
		
		String orientation = "H12";
		String pairing_type = "2";
		String persistent = "No";
		String view_only = "No";


		conConfig.createPairConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type, 
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//change pairing type
		view_only = "Yes";		//no orientation when pairing_type = 1
		config.editPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, null, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
		
		conStatusConfig.checkPairedConnection(name, connection_type, host, host_2, audio, null, orientation, pairing_type,
				persistent, view_only, "", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
}
