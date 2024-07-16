package northbound.get;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.DevicesMethods;
import northbound.get.config.ApplianceConfig;
import testNG.Utilities;

public class Appliances extends StartupTestCase{

	private ApplianceConfig config = new ApplianceConfig();
	private  static DevicesMethods devices = new DevicesMethods();
	final static Logger log = Logger.getLogger(Appliances.class);
	private String serial;
	private String software;
	private String mpn;
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	@Test
	public void test01_getSpecificTxSingleHead() {
		String[] details = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	@Test
	public void test02_getSpecificRxSingleHead() {
		String[] details = config.setApplianceDetails(rxIp, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "receiver";
		app.device_names = new String[1];
		app.device_names[0] = rxEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxEmerald.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxEmerald.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test(groups = {"noZero"})
	public void test03_getSpecificTxDualHead() {
		String[] details = config.setApplianceDetails(txIpDual, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txDual.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(txDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(txDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test04_getSpecificRxDualHead() {
		String[] details = config.setApplianceDetails(rxIpDual, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "receiver";
		app.device_names = new String[1];
		app.device_names[0] = rxDual.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	//already 
//	@Test
	public void test05_getAllAppliances() {
		String[] rxDualDetails = config.setApplianceDetails(rxIpDual, deviceUserName, devicePassword);
		String[] txDualDetails = config.setApplianceDetails(txIpDual, deviceUserName, devicePassword);
		String[] txDetails = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		String[] rxDetails = config.setApplianceDetails(rxIp, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = null;
		app.device_names = null;
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(rxDualDetails[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(rxDualDetails[1]))
		.body(config.getDeivceModel(0), equalTo(rxDualDetails[2]))
		
		.body(config.getDeviceDeviceName(1), equalTo(txDual.getDeviceName()))
		.body(config.getDeviceIp(1), equalTo(txDual.getIpAddress()))
		.body(config.getDeviceMac(1), equalTo(txDual.getMac()))
		.body(config.getDeivceSerialNumber(1), equalTo(txDualDetails[0]))					
		.body(config.getDeviceSoftwareVersion(1), equalTo(txDualDetails[1]))
		.body(config.getDeivceModel(1), equalTo(txDualDetails[2]))
		
		.body(config.getDeviceDeviceName(2), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(2), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(2), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(2), equalTo(txDetails[0]))					
		.body(config.getDeviceSoftwareVersion(2), equalTo(txDetails[1]))
		.body(config.getDeivceModel(2), equalTo(txDetails[2]))
		
		.body(config.getDeviceDeviceName(3), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceIp(3), equalTo(rxEmerald.getIpAddress()))
		.body(config.getDeviceMac(3), equalTo(rxEmerald.getMac()))
		.body(config.getDeivceSerialNumber(3), equalTo(rxDetails[0]))					
		.body(config.getDeviceSoftwareVersion(3), equalTo(rxDetails[1]))
		.body(config.getDeivceModel(3), equalTo(rxDetails[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	//already
//	@Test
	public void test06_getAllTransmitters() {
		String[] txDualDetails = config.setApplianceDetails(txIpDual, deviceUserName, devicePassword);
		String[] txDetails = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = null;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		
		.body(config.getDeviceDeviceName(0), equalTo(txDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(txDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(txDualDetails[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(txDualDetails[1]))
		.body(config.getDeivceModel(0), equalTo(txDualDetails[2]))
		
		.body(config.getDeviceDeviceName(1), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(1), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(1), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(1), equalTo(txDetails[0]))					
		.body(config.getDeviceSoftwareVersion(1), equalTo(txDetails[1]))
		.body(config.getDeivceModel(1), equalTo(txDetails[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
	}
	//already
	//@Test
	public void test07_getAllReceivers() {
		String[] rxDualDetails = config.setApplianceDetails(rxIpDual, deviceUserName, devicePassword);
		String[] rxDetails = config.setApplianceDetails(rxIp, deviceUserName, devicePassword);
		
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "receiver";
		app.device_names = null;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(rxDualDetails[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(rxDualDetails[1]))
		.body(config.getDeivceModel(0), equalTo(rxDualDetails[2]))
		
		.body(config.getDeviceDeviceName(1), equalTo(rxEmerald.getDeviceName()))
		.body(config.getDeviceIp(1), equalTo(rxEmerald.getIpAddress()))
		.body(config.getDeviceMac(1), equalTo(rxEmerald.getMac()))
		.body(config.getDeivceSerialNumber(1), equalTo(rxDetails[0]))					
		.body(config.getDeviceSoftwareVersion(1), equalTo(rxDetails[1]))
		.body(config.getDeivceModel(1), equalTo(rxDetails[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
//	already
//	@Test
	public void test09_getMultipleDevices() {
		String[] rxDualDetails = config.setApplianceDetails(rxIpDual, deviceUserName, devicePassword);
		String[] txDetails = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "receiver";
		app.device_names = new String[2];
		app.device_names[0] = rxDual.getDeviceName();
		app.device_names[1] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(rxDualDetails[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(rxDualDetails[1]))
		.body(config.getDeivceModel(0), equalTo(rxDualDetails[2]))
		
		.body(config.getDeviceDeviceName(1), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(1), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(1), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(1), equalTo(txDetails[0]))					
		.body(config.getDeviceSoftwareVersion(1), equalTo(txDetails[1]))
		.body(config.getDeivceModel(1), equalTo(txDetails[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test10_singleDeviceNotExist() {
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = "invalidDeviceName";
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following device does not exist: [\"invalidDeviceName\"].")).extract().response();
	
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test11_multipleDeviceNotExist() {
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[3];
		app.device_names[0] = "invalidDeviceName";
		app.device_names[1] = "invalidDeviceName2";
		app.device_names[2] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following devices do not exist: [\"invalidDeviceName\", \"invalidDeviceName2\"].")).extract().response();
	
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
//	@Test
	public void test12_invalidParameters() {
		ForceConnect force = new ForceConnect();
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(force)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameters: [\"user\", \"connection\"].")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test13_unauthorizedAccessUsername() {
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic("invalid", "admin")
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(401)
		.body("message", equalTo("Operation is not authorized.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test14_unauthorizedAccessPassword() {
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic("admin", "invalid")
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(401)
		.body("message", equalTo("Operation is not authorized.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test15_editApplianceName() throws InterruptedException {
		String[] details = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txEmerald.getDeviceName();
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		devices.changeDeviceName(driver, txIp, "test15_editAppliance");
		app.device_names[0] = "test15_editAppliance";
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo("test15_editAppliance"))
		.body(config.getDeviceIp(0), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		devices.changeDeviceName(driver, txIp, txEmerald.getDeviceName());
		app.device_names[0] = txEmerald.getDeviceName();
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(txEmerald.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(txEmerald.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test16_editDeviceIp() throws InterruptedException {
		String[] details = config.setApplianceDetails(rxIpDual, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "receiver";
		app.device_names = new String[1];
		app.device_names[0] = rxDual.getDeviceName();
		
		log.info("Checking device1");
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxDual.getIpAddress()))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		
		log.info("First check OK.. Editing ip to 128.133");
		devices.editDevice(driver, rxIpDual, "10.231.128.133", false);
		log.info("IP changed to 128.133");
		log.info("Checking device2");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo("10.231.128.133"))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		log.info("Finished checking second time.. changing up to original:" + rxIpDual);
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
		devices.editDevice(driver, "10.231.128.133", rxIpDual, false);
		
		log.info("RXIPDUAL:" + rxIpDual);
		log.info("Checking device3");
		response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body(config.getDeviceDeviceName(0), equalTo(rxDual.getDeviceName()))
		.body(config.getDeviceIp(0), equalTo(rxIpDual))
		.body(config.getDeviceMac(0), equalTo(rxDual.getMac()))
		.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
		.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
		.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test17_invalidDeviceType() {
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "dkm";
		app.device_names = null;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
		.headers(BoxillaHeaders.getBoxillaHeaders())
		.body(app)
		.when().contentType(ContentType.JSON)
		.get(config.getUri(boxillaManager))
		.then().assertThat().statusCode(400)
		.body("message", equalTo("Invalid parameter: device_type 'dkm'.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
	}
	
	@Test
	public void test18_simpleSoakTest() {
		String[] details = config.setApplianceDetails(txIp, deviceUserName, devicePassword);
		ApplianceConfig.GetAppliance app = config.new GetAppliance();
		app.device_type = "transmitter";
		app.device_names = new String[1];
		app.device_names[0] = txEmerald.getDeviceName();
		
		for(int j=0; j < 2000; j++) {
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
			.headers(BoxillaHeaders.getBoxillaHeaders())
			.body(app)
			.when().contentType(ContentType.JSON)
			.get(config.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body(config.getDeviceDeviceName(0), equalTo(txEmerald.getDeviceName()))
			.body(config.getDeviceIp(0), equalTo(txEmerald.getIpAddress()))
			.body(config.getDeviceMac(0), equalTo(txEmerald.getMac()))
			.body(config.getDeivceSerialNumber(0), equalTo(details[0]))					
			.body(config.getDeviceSoftwareVersion(0), equalTo(details[1]))
			.body(config.getDeivceModel(0), equalTo(details[2])).extract().response();
			
			SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, response);
			log.info("TEST " + j + " OK");
		}
	}
	

}
