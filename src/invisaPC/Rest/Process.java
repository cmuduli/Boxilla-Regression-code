package invisaPC.Rest;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.device.Connections;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;

/**
 * Class that contains tests for checking the appliance REST api process
 * @author Boxilla
 *
 */
public class Process extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(Process.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	ConnectionsMethods connections = new ConnectionsMethods();
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	/**
	 * Checks the rest api for a running process on RX device
	 */
	@Test(groups = {"rest", "emerald"})
	public void test01_checkProcessRunningRx() {
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/process/?process_name=init")
		.then().assertThat().statusCode(200)
		.body("init", equalTo(1));
	}
	/**
	 * Checks the rest api for a process not running on RX device
	 */
	//@Test(groups = {"rest", "emerald"})
	public void test02_checkProcessNotRunningRx() {
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/control/process/?process_name=badData")
		.then().assertThat().statusCode(200)
		.body("badData", equalTo(0));
	}
	
	/**
	 * Checks the rest api for a process running on TX device
	 */
	@Test(groups = {"rest", "emerald"})
	public void test03_checkProcessRunningTx() {
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/process/?process_name=init")
		.then().assertThat().statusCode(200)
		.body("init", equalTo(1));
	}
	
	/**
	 * Checks the rest api for a process notr running on a TX device
	 */
	//@Test(groups = {"rest", "emerald"})
	public void test04_checkProcessNotRunningTx() {
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + txIp + getPort() + "/control/process/?process_name=badData")
		.then().assertThat().statusCode(200)
		.body("badData", equalTo(0));
	}

}
