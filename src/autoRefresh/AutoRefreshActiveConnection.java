package autoRefresh;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import connection.ForcedConnection;
import extra.ForceConnect;
import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import objects.ActiveConnectionElements;
import objects.Connections;
import objects.Users;
import testNG.Utilities;

/**
 * This class contains tests that will verify Boxillas auto refresh 
 * feature on the active connection page.
 * @author Boxilla
 *
 */
public class AutoRefreshActiveConnection extends StartupTestCase{


	final static Logger log = Logger.getLogger(AutoRefreshActiveConnection.class);
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String connectionName = "autoRefreshCon";
	private UsersMethods users = new UsersMethods();

	/**
	 * This set up will run before any tests in the class run.
	 * It will log into boxilla and create a connection, updat ethe xml file 
	 * on the appliance and then launch the connection using force_connection API.
	 * It will then check that the connection is running using the appliance 
	 * REST API
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		RestAssured.authentication = basic(restuser, restPassword);
		RestAssured.useRelaxedHTTPSValidation();
		ForceConnect con = new ForceConnect();
		con.action = "force_connection";
		con.user = "Boxilla";
		con.connection = connectionName;

		try {
			cleanUpLogin();
			addConnection();
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 100000 );
			Thread.sleep(20000);
			int status = given().header(getHead()).body(con)
					.when()
					.contentType(ContentType.JSON)
					.put(getHttp() + "://" + rxIp + getPort() + "/control/connections").andReturn().statusCode();

			log.info("Status code from forced_connection:" + status);
			Assert.assertTrue(status == 200,"Return code did not equal 200, actual:" + status);
			log.info("Sleeping while connection is made");
			Thread.sleep(30000);
			log.info("Checking if active connection is up");
			String output = given().header(getHead())
					.when()
					.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections").asString();

			log.info("Active connection return: " + output);
			given().header(getHead())
			.when()
			.get(getHttp() + "://" + rxIp +  getPort() + "/statistics/active_connections")
			.then().assertThat().statusCode(200)
			.body("kvm_active_connections.name", equalTo(Arrays.asList(connectionName)));
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}

	/**
	 * Test that will check if the active connection table 
	 * auto refreshes after being left idle on the page
	 * @throws InterruptedException
	 */
	@Test
	public void test01_checkActiveConnectionTableAutoRefresh() throws InterruptedException {
		String originalBW = connectionMethods.getNetworkBWFromActiveConnectionTable(driver);
		log.info("First network BW:" + originalBW);
		String newBW = connectionMethods.autoRefreshPoll(originalBW, ActiveConnectionElements.getConnectionTableConnectionNetworkBW(), driver);
		log.info("2nd newwork BW:" + newBW);
		Assert.assertFalse(originalBW.equals(newBW), "Network Bandwidth did not change");
	}
	/**
	 * Test that will check if the active connection frame rate table
	 * auto refreshes after being left idle on the page
	 * @throws InterruptedException
	 */
	@Test
	public void test02_checkActiveConnectionFrameTableAutoRefresh() throws InterruptedException {
		String originalValue = connectionMethods.getNetworkBWFromActiveConnectionFrameTable(driver);
		log.info("first value from frame table:" + originalValue);
		String newValue = connectionMethods.autoRefreshPoll(originalValue, ActiveConnectionElements.getFrameTableConnectionNetworkBW(), driver);
		log.info("Second value from frame table:" + newValue);
		Assert.assertFalse(originalValue.equals(newValue), "Table did not update");
	}

	/**
	 * Test that will check the active connection configuration table 
	 * auto refreshes after being left idle on the page
	 * @throws InterruptedException
	 */
	@Test
	public void test03_checkActiveConnectionConfigTableAutoRefresh() throws InterruptedException {
		String original = connectionMethods.getTimeFromActiveConnectionConfigTable(driver);
		log.info("First value from config table:" + original);
		String newValue = connectionMethods.autoRefreshPoll(original, ActiveConnectionElements.getConfigTableConnectionTime(), driver);
		log.info("Second value from table:" + newValue);
		Assert.assertFalse(original.equals(newValue), "Config table did not refresh");
	}

	/**
	 * Test that will kill a connection and check that the 
	 * active connection table auto refreshes when the page is idle
	 * @throws InterruptedException
	 */
	@Test
	public void test04_checkActiveConnectionDisconnectAutoRefresh() throws InterruptedException {
		String name = connectionMethods.getConnectionNameFromActiveConnectionTable(driver);
		log.info("Connection name:" + name);
		log.info("Reboot RX to kill the connection");
		deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 10);
		String newName = connectionMethods.autoRefreshPoll(name,  ActiveConnectionElements.getConnectionTableConnectionName(), driver);
		log.info("Connection name:" + newName);
		Assert.assertFalse(name.equals(newName), "Page did not auto refresh");
	}

	/**
	 * Method to adding connections in boxilla
	 * @throws InterruptedException
	 */
	private void addConnection() throws InterruptedException {
		connectionMethods.addConnection(driver, connectionName, "no"); // connection name, user template
		connectionMethods.connectionInfo(driver, "tx", "user", "user", txIp); // connection via, name, host ip
		connectionMethods.chooseCoonectionType(driver, "private"); // connection type
		connectionMethods.enableExtendedDesktop(driver);
		connectionMethods.enableUSBRedirection(driver);
		connectionMethods.enableAudio(driver);
		connectionMethods.propertyInfoClickNext(driver);
		connectionMethods.saveConnection(driver, connectionName); // Connection name to assert

		users.manageUser(driver, "admin");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "admin").click();
		Thread.sleep(2000);
		Select options = new Select(Users.nonSelectedActiveConnectionList(driver)); // List of available
		// Connection to add
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Add Connection if Connection available to add
			options.selectByVisibleText(connectionName);
			Thread.sleep(2000);
			Users.moveSelectedConenction(driver).click();
			users.saveConnection(driver);
		} else { // if no connection available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 0, "****** Sufficient connection not available to add ********");
		}
	}

}
