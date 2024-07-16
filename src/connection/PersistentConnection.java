package connection;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import java.util.Arrays;

import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.ForceConnect;
import extra.ScpTo;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.device.Connections;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import testNG.Utilities;

public class PersistentConnection extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(PersistentConnection.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	ConnectionsMethods connections = new ConnectionsMethods();
	
	//test properties
	private String connectionOne = "persistenceConnectionOne";	
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		try {	
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			cleanUpLogin();
			connections.createTxConnection(connectionOne, "private", driver, txIp);
			deviceMethods.recreateCloudData(rxIp, rxIpDual);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000);
			
			//create the connection
			ForceConnect fCon = new ForceConnect();
			fCon.action = "force_connection";
			fCon.user = "Boxilla";
			fCon.connection = connectionOne;
			
			int status = given().header(getHead()).body(fCon)
					.when()
					.contentType(ContentType.JSON)
					.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();
			
			log.info("Return code from force connection:" + status);
			log.info("Checking if connection is running");
			Thread.sleep(30000);
			
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + txIp +  getPort() + "/statistics/active_connections")
			.then()
			.body("kvm_active_connections.rx_hostname", equalTo(Arrays.asList(rxIp)));
			
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections")
			.then()
			.body("kvm_active_connections.name", equalTo(Arrays.asList(connectionOne)));
			
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	
	
	
	@Test
	public void test01_transmitterDown() throws InterruptedException {
		//deviceMethods.interfacesDownUp(txIp, "root", "barrow1admin_12", "eth1", "60");
		System.out.println("test");
	}
	

	
}
