package autoRefresh;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import objects.Users;
import testNG.Utilities;

/**
 * This class contains tests for checking Boxillas auto refresh 
 * feature on the user page
 * @author Boxilla
 *
 */
public class AutoRefreshUser extends StartupTestCase {

	final static Logger log = Logger.getLogger(AutoRefreshUser.class);
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String connectionName1 = "autoRefreshUser";
	private UsersMethods users = new UsersMethods();

	/**
	 * Inner class to model the json request sent to log a user off
	 * an appliance
	 * @author Boxilla
	 *
	 */
	class LogOffUser {
		public String action = "";
	}

	/**
	 * This set up will run once before any tests in the class run.
	 * This will add a connection in Boxilla, update the appliances xml
	 * files, launch the connection and check the connection is active
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		try {
			cleanUpLogin();
			addConnection(connectionName1);
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 100000 );
			connectionMethods.createRealConnection(connectionName1,deviceUserName, devicePassword, rxIp, txIp) ;
			connectionMethods.checkConnectionIsActiveSsh(deviceUserName, devicePassword, rxIp, connectionName1);
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	/**
	 * Test that kills a connection and checks the users page in Boxilla
	 * for an auto update
	 * @throws InterruptedException
	 */
	@Test
	public void test01_closeConnectionAutoRefresh() throws InterruptedException {
		String currentConnection = users.getActiveUsersCurrentConnection(driver);
		log.info("Current connection name:" + currentConnection);
		deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 10);
		String currentConnection2 = connectionMethods.autoRefreshPoll(currentConnection,  Users.getCurrentConnectionActiveUsersTable(),driver);
		log.info("New Connection:" + currentConnection2);
		Assert.assertFalse(currentConnection.equals(currentConnection2), "Connection did not change");	
	}

	/**
	 * Test that launches a connection and checks that the user
	 * page in Boxilla will auto refreh after sitting idle
	 * @throws InterruptedException
	 */
	@Test 
	public void test02_startConnectionAutoRefresh() throws InterruptedException {
		String current = users.getActiveUsersCurrentConnection(driver);
		log.info("Current connection name:" + current);
		connectionMethods.createRealConnection(connectionName1,deviceUserName, devicePassword, rxIp, txIp) ;
		connectionMethods.checkConnectionIsActiveSsh(deviceUserName, devicePassword, rxIp, connectionName1);
		String current2 = connectionMethods.autoRefreshPoll(current, Users.getCurrentConnectionActiveUsersTable(), driver);
		log.info("New connection name:" + current2);
		Assert.assertFalse(current.equals(current2), "Page did not auto refresh");
	}

	/**
	 * Test that will log a user on then log the user off
	 * and check that the user page auto refreshes
	 * @throws InterruptedException
	 */
	@Test
	public void test03_logOffAutoRefresh() throws InterruptedException {
		//reboot device so its clean and logged in as admin
		deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 10);
		Thread.sleep(20000);
		String currentUser = users.getActiveUsersCurrentUser(driver);
		LogOffUser logOff = new LogOffUser();
		logOff.action = "log_off";
		int status2 = given().header(getHead()).body(logOff)
				.when()
				.contentType(ContentType.JSON)
				.put(getHttp() + "://" + rxIp + getPort() + "/control/user").andReturn().statusCode();
		log.info("Status code from log off user:" + status2);
		//sleep while we wait for table to update
		String newUser = connectionMethods.autoRefreshPoll(currentUser, Users.getUserNameActiveUsersTable(), driver);
		log.info("New user name:" + newUser);
		Assert.assertFalse(currentUser.equals(newUser), "Page did not automatically refresh");
	}

	/**
	 * Method to adding connections in boxilla
	 * @throws InterruptedException
	 */
	private void addConnection(String name) throws InterruptedException {
		connectionMethods.addConnection(driver, name, "no"); // connection name, user template
		connectionMethods.connectionInfo(driver, "tx", "user", "user", txIp); // connection via, name, host ip
		connectionMethods.chooseCoonectionType(driver, "private"); // connection type
		connectionMethods.enableExtendedDesktop(driver);
		connectionMethods.enableUSBRedirection(driver);
		connectionMethods.enableAudio(driver);
		connectionMethods.propertyInfoClickNext(driver);
		connectionMethods.saveConnection(driver, name); // Connection name to assert

		users.manageUser(driver, "admin");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "admin").click();
		Thread.sleep(2000);
		Select options = new Select(Users.nonSelectedActiveConnectionList(driver)); // List of available
		// Connection to add
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Add Connection if Connection available to add
			options.selectByVisibleText(name);
			Thread.sleep(2000);
			Users.moveSelectedConenction(driver).click();
			users.saveConnection(driver);
		} else { // if no connection available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 0, "****** Sufficient connection not available to add ********");
		}
	}
}
