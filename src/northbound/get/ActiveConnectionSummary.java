package northbound.get;

import static io.restassured.RestAssured.given;

import static org.hamcrest.Matchers.*;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;

public class ActiveConnectionSummary extends StartupTestCase2 {

	final static Logger log = Logger.getLogger(ActiveConnectionSummary.class);
	private ConnectionsMethods connection = new ConnectionsMethods();
	private DevicesMethods devices = new DevicesMethods();
	private String connectionName1 = "summaryCon1";
	private String connectionName2 = "summaryCon2";
	
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/connections/kvm/active/summary";
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		try {
			cleanUpLogin();
		//create two connections to use
		connection.createMasterConnection(connectionName1, "tx", "private", "true", "true", "true", "false", "false", txIpDual, driver);
		connection.createMasterConnection(connectionName2, "tx", "shared", "true", "true", "true", "false", "false", txIp, driver);
		devices.recreateCloudData(rxIp);
		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		
		devices.recreateCloudData(rxIpDual);
		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		cleanUpLogout();
		
		}catch(Exception e) {
			cleanUpLogout();
		}

	}
	
	@Test
	public void test01_noActiveConnections() {
		
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.get(getUri())
		.then().assertThat().statusCode(200)
		.body("message.active_connections", hasSize(0)).extract().response();
		
		SaveResponseStatistics(getUri(), REQUEST_TYPE.GET, response);
		
	}
}
