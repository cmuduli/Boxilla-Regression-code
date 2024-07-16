package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Array;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.MakeConnect;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import testNG.Utilities;


/**
 * This class will test legacy HTTP rest calls that are still available to customers
 * @author Brendan O Regan
 *
 */
public class HttpConnections extends StartupTestCase {

	final static Logger log = Logger.getLogger(HttpConnections.class);
	
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods device = new DevicesMethods();
	private String connectionName = "httpForceCon";
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("Starting test setup....");
			cleanUpLogin();
			device.changedHttpToEnabled(driver, rxIp);
			
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
//			//create connection
			connections.createTxConnection(connectionName, "private", driver, txIp);
			device.recreateCloudData(rxIp, rxIpDual);
			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
			
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
//	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_ForcedConnectionHttp() throws InterruptedException  {
		ForceConnect fCon = new ForceConnect();
		fCon.action = "force_connection";
		fCon.user = "Boxilla";
		fCon.connection = connectionName;
		
		int status = given().header(getHead()).body(fCon)
			.when()
			.contentType(ContentType.JSON)
			.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
		
		log.info("Return code from HTTP force connect = " + status );
		Assert.assertTrue(status == 200, "Return did not equal 200, actual:" + status);
		log.info("sleeping while connection comes up then checking for connection");
		Thread.sleep(60000);
		
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.name[0]", equalTo(connectionName));
	}
	
	@Test
	public void test02_terminateForceConnectionHttp() throws InterruptedException {
		String json = "{\"action\": \"terminate_connection\"}";
		int status = given().header(getHead()).body(json)
				.when()
				.contentType(ContentType.JSON)
				.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
		log.info("Status code from terminate connection: " + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting for connection to break and checking no connection is active");
		Thread.sleep(15000);
		String connectionStatus = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
		log.info("Connection status:" + connectionStatus );
		Assert.assertFalse(connectionStatus.contains(connectionName), "Return from rest still had the connection stats:" + connectionStatus);
	}
	
	@Test
	public void test03_makeConnectionHttp() throws InterruptedException {
		MakeConnect mCon = new MakeConnect();
		mCon.action = "make_connection";
		mCon.user = "admin";
		mCon.transmitter = txIp;
		
		int status = given().header(getHead()).body(mCon)
				.when()
				.contentType(ContentType.JSON)
				.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
		log.info("Status code from make_connection:" + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting while connection comes up then checking the connection");
		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
		
	}
//	
	@Test
	public void test04_terminateMakeConnectionHttp () throws InterruptedException {
		String json = "{\"action\": \"terminate_connection\"}";
		int status = given().header(getHead()).body(json)
				.when()
				.contentType(ContentType.JSON)
				.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
		log.info("Status code from terminate connection: " + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting for connection to break and checking no connection is active");
			
	Thread.sleep(15000);
		String connectionStatus = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
		log.info("Connection status:" + connectionStatus );
		Assert.assertFalse(connectionStatus.contains(txIp), "Return from rest still had the connection stats:" + connectionStatus);
	}
	@Test
	public void test05_MakeConnectionHttps() throws InterruptedException
	
	{
		MakeConnect mCon = new MakeConnect();
		mCon.action = "make_connection";
		mCon.user = "admin";
		mCon.transmitter = txIp;
		
		int status = given().header(getHead()).body(mCon)
				.when()
				.contentType(ContentType.JSON)
				.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
		log.info("Status code from make_connection:" + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting while connection comes up then checking the connection");
		Thread.sleep(60000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
	}
	@Test
	public void test06_terminateMakeConnectionHttps() throws InterruptedException 
	{
		String json = "{\"action\": \"terminate_connection\"}";
		int status = given().header(getHead()).body(json)
				.when()
				.contentType(ContentType.JSON)
				.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
		log.info("Status code from terminate connection: " + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting for connection to break and checking no connection is active");
			
	Thread.sleep(15000);
		String connectionStatus = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
		log.info("Connection status:" + connectionStatus );
		Assert.assertFalse(connectionStatus.contains(txIp), "Return from rest still had the connection stats:" + connectionStatus);
	}
//	@Test
	public void test07_terminate_No_OSD() throws InterruptedException 
	{
		ArrayList arup=new ArrayList();
		arup.add(rxIp);
		arup.add(rxIpDual);
		System.out.println("Devices are"+ arup);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		String Tx="devmem 0x800c8303 8";
		for (int i = 0; i <arup.size(); i++) 
		{
//			String kishor= arup.get(i).toString();
			Ssh shell = new Ssh(deviceUserName, devicePassword,arup.get(i).toString());
			System.out.println("print ip"+ arup.get(i));
			MakeConnect mCon = new MakeConnect();
			mCon.action = "make_connection";
			mCon.user = "admin";
			mCon.transmitter = txIp;
			
			int status = given().header(getHead()).body(mCon)
					.when()
					.contentType(ContentType.JSON)
					.put("https://" + arup.get(i)+ ":8888/control/connections").andReturn().statusCode();
			log.info("Status code from make_connection:" + status);
			Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
			log.info("Waiting while connection comes up then checking the connection");
			Thread.sleep(60000);
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + arup.get(i) + getPort() + "/statistics/active_connections")
			.then().assertThat().statusCode(200)
			.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
			
			
			//Terminate No OSD
			String json = "{\"action\": \"terminate_connection_no_osd\"}";
			int status1 = given().header(getHead()).body(json)
					.when()
					.contentType(ContentType.JSON)
					.put("https://" + arup.get(i) + ":8888/control/connections").andReturn().statusCode();
			log.info("Status code from terminate connection: " + status);
			Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
			log.info("waiting and cheking OSD to terminate");
			if (isEmerald) 
			{
				shell.loginToServer();
				String output = shell.sendCommand("devmem 0x80001304 32");
				log.info("xml out put"+ output); 
				Assert.assertTrue(output.contains("0x10"),   "GUI is  not displayed" + output);
			
			}else {
				if (arup.get(i)==rxIp)
				{
					shell.loginToServer();
					String output = shell.sendCommand("devmem 0x800c8303 8");
					log.info("Xml output" + output);
					Assert.assertTrue(output.contains("0x10"),   "GUI is not  displayed");	
				}else {
					shell.loginToServer();
					String output = shell.sendCommand(" devmem 0x800c8303 8");
					log.info("Xml out put" + output);
					Assert.assertTrue(output.contains("0x10"),   "GUI is not displayed");	
				}
			}
			
		}
		}
	
		
//		Ssh shell = new Ssh(userName, password, rxIp);
		
//		//Make Connection 
//		MakeConnect mCon = new MakeConnect();
//		mCon.action = "make_connection";
//		mCon.user = "admin";
//		mCon.transmitter = txIp;
//		
//		int status = given().header(getHead()).body(mCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
//		log.info("Status code from make_connection:" + status);
//		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		log.info("Waiting while connection comes up then checking the connection");
//		Thread.sleep(60000);
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
//		.then().assertThat().statusCode(200)
//		.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
//		
//		//Terminate No OSD
//		String json = "{\"action\": \"terminate_connection_no_osd\"}";
//		int status1 = given().header(getHead()).body(json)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
//		log.info("Status code from terminate connection: " + status);
//		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		log.info("waiting and cheking OSD to terminate");
//		shell.loginToServer();
//		String output = shell.sendCommand("ps -ax");
//		if (output.contains("/usr/bin/cloudgui -qws -display directfb")) 
//		{
//			log.info("osd has not been terminated");
//			Assert.fail("osd has not been terminated");
//		}else 
//		{
//			log.info("OSD has been terminated sucessfully");
//		}
//		//force connection 
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		int status2 = given().header(getHead()).body(fCon)
//			.when()
//			.contentType(ContentType.JSON)
//			.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect = " + status );
//		Assert.assertTrue(status == 200, "Return did not equal 200, actual:" + status);
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(60000);
//		
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
//		.then().assertThat().statusCode(200)
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//		
//		//Terminate force connection
//		String json1 = "{\"action\": \"terminate_connection\"}";
//		int status3 = given().header(getHead()).body(json1)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("https://" + rxIp + ":8888/control/connections").andReturn().statusCode();
//		log.info("Status code from terminate connection: " + status);
//		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		log.info("Waiting for connection to break and checking no connection is active");
//		Thread.sleep(15000);
//		String connectionStatus = given().header(getHead())
//				.when()
//				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
//		log.info("Connection status:" + connectionStatus );
//		Assert.assertFalse(connectionStatus.contains(connectionName), "Return from rest still had the connection stats:" + connectionStatus);
//			}
////
////	
////	
////	////corrib versions
////	
//	@Test
//	public void test05_ForcedConnectionHttpCorrib() throws InterruptedException  {
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		int status = given().header(getHead()).body(fCon)
//			.when()
//			.contentType(ContentType.JSON)
//			.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect = " + status );
//		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(15000);
//		
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
//		.then().assertThat().statusCode(200)
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//	}
////	
//	@Test
//	public void test06_terminateForceConnectionHttpCorrib() throws InterruptedException {
//		String json = "{\"action\": \"terminate_connection\"}";
//		int status = given().header(getHead()).body(json)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
//		log.info("Status code from terminate connection: " + status);
//		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		log.info("Waiting for connection to break and checking no connection is active");
//		Thread.sleep(20000);
//		String connectionStatus = given().header(getHead())
//				.when()
//				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
//		log.info("Connection status:" + connectionStatus );
//		Assert.assertFalse(connectionStatus.contains(connectionName), "Return from rest still had the connection stats:" + connectionStatus);
//	}
//	
	@Test
	public void test08_makeConnectionHttpCorrib() throws InterruptedException {
		MakeConnect mCon = new MakeConnect();
		mCon.action = "make_connection";
		mCon.user = "admin";
		mCon.transmitter = txIp;
		
		int status = given().header(getHead()).body(mCon)
				.when()
				.contentType(ContentType.JSON)
				.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
		log.info("Status code from make_connection:" + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting while connection comes up then checking the connection");
		Thread.sleep(15000);
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then().assertThat().statusCode(200)
		.body("kvm_active_connections.tx_hostname[0]", equalTo(txIp));
		
	}
//	
	@Test
	public void test09_terminateMakeConnectionHttpCorrib () throws InterruptedException 
	{
		
		String json = "{\"action\": \"terminate_connection\"}";
		int status = given().header(getHead()).body(json)
				.when()
				.contentType(ContentType.JSON)
				.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
		log.info("Status code from terminate connection: " + status);
		Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
		log.info("Waiting for connection to break and checking no connection is active");
		Thread.sleep(15000);
		String connectionStatus = given().header(getHead())
				.when()
				.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections").asString();
		log.info("Connection status:" + connectionStatus );
		Assert.assertFalse(connectionStatus.contains(txIp), "Return from rest still had the connection stats:" + connectionStatus);
	}
//	
//	
//	//negative tests. Turn off http enabled and make sure connections cannot be created
//	@Test
//	public void test09_forceConnectionHttpDisabled() throws InterruptedException {
//		device.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", false);
//		Thread.sleep(60000);
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		boolean isError = false;
//		try {
//			int status = given().header(getHead()).body(fCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
//			log.info("status code: " + status);
//			//Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		}catch(Exception  e1) {
//			log.info("REST failed");
//			isError = true;
//		}
//		// if a connection is made, reboot device to break the connection for the next test
//		if(!isError) {
//			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
//		}
//		Assert.assertTrue(isError, "Rest worked when http was disabled");
//	}
//	
//	@Test
//	public void test10_forceConnectionHttpDisabledCorrib() throws InterruptedException {
//		device.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", false);
//		Thread.sleep(60000);
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		boolean isError = false;
//		try {
//			int status = given().header(getHead()).body(fCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
//			//Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//			log.info("Return code:" + status);
//		}catch(Exception  e1) {
//			log.info("REST failed");
//			isError = true;
//		}
//		// if a connection is made, reboot device to break the connection for the next test
//		if(!isError) {
//			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
//		}
//		Assert.assertTrue(isError, "Rest worked when http was disabled");
//	}
//	
//	///////////////////////////////////////
//	
//	@Test
//	public void test11_makeConnectionHttpDisabled() throws InterruptedException {
//		device.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", false);
//		Thread.sleep(60000);
//		MakeConnect mCon = new MakeConnect();
//		mCon.action = "make_connection";
//		mCon.user = "admin";
//		mCon.transmitter = txIp;
//		
//		boolean isError = false;
//		try {
//			int status = given().header(getHead()).body(mCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
//			//Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//			log.info("return code:" + status);
//		}catch(Exception  e1) {
//			log.info("REST failed");
//			isError = true;
//		}
//		// if a connection is made, reboot device to break the connection for the next test
//		if(!isError) {
//			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
//		}
//		Assert.assertTrue(isError, "Rest worked when http was disabled");
//	}
//	
//	@Test
//	public void test12_makeConnectionHttpDisabledCorrib() throws InterruptedException {
//		device.setUniquePropertyRx(driver, rxIp, "HTTP Enabled", false);
//		Thread.sleep(60000);
//		MakeConnect mCon = new MakeConnect();
//		mCon.action = "make_connection";
//		mCon.user = "admin";
//		mCon.transmitter = txIp;
//		
//		boolean isError = false;
//		try {
//			int status =  given().header(getHead()).body(mCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
//			log.info("Return code:" + status);
//			// Assert.assertTrue(status == 200, "Return code did not equal 200, actual:" + status);
//		}catch(Exception  e1) {
//			log.info("REST failed");
//			isError = true;
//		}
//		// if a connection is made, reboot device to break the connection for the next test
//		if(!isError) {
//			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
//		}
//		Assert.assertTrue(isError, "Rest worked when http was disabled");
//	}
//	
//	@Test
//	public void test13_changeForceConnection() throws InterruptedException {
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		//make first connection
//		int status = given().header(getHead()).body(fCon)
//			.when()
//			.contentType(ContentType.JSON)
//			.put("http://" + rxIp + ":7778/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect 1 = " + status );
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(15000);
//		
//		//chceck first connection
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
//		.then()
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//		
//		//make second connection 
//		int status2 = given().header(getHead()).body(fCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIpDual + ":7778/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect 2 = " + status2 );
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(15000);
//		
//		//check second connection
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIpDual + getPort() + "/statistics/active_connections")
//		.then()
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//	}
//	
//	@Test
//	public void test14_changeForceConnectionCorrib() throws InterruptedException {
//		ForceConnect fCon = new ForceConnect();
//		fCon.action = "force_connection";
//		fCon.user = "Boxilla";
//		fCon.connection = connectionName;
//		
//		//make first connection
//		int status = given().header(getHead()).body(fCon)
//			.when()
//			.contentType(ContentType.JSON)
//			.put("http://" + rxIp + ":7778/corrib/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect 1 = " + status );
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(15000);
//		
//		//chceck first connection
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
//		.then()
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//		
//		//make second connection 
//		int status2 = given().header(getHead()).body(fCon)
//				.when()
//				.contentType(ContentType.JSON)
//				.put("http://" + rxIpDual + ":7778/corrib/control/connections").andReturn().statusCode();
//		
//		log.info("Return code from HTTP force connect 2 = " + status2 );
//		log.info("sleeping while connection comes up then checking for connection");
//		Thread.sleep(15000);
//		
//		//check second connection
//		given().header(getHead())
//		.when()
//		.get(getHttp() + "://" + rxIpDual + getPort() + "/statistics/active_connections")
//		.then()
//		.body("kvm_active_connections.name[0]", equalTo(connectionName));
//	}
	
}
