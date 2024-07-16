package connection;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.ActiveConnectionElements;
import objects.Users;
import testNG.Utilities;

/**
 * Contains all tests to do with Boxilla Active Connections
 * @author Brendan O Regan
 *
 */
public class ActiveConnections extends StartupTestCase {

	private UsersMethods user = new UsersMethods();
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods(); 
	final static Logger log = Logger.getLogger(ActiveConnections.class);
	private String connectionName = "activeConnections_connection";

	/**
	 * Overriding beforeClass in superclass to create connections to be used in tests.
	 * @throws InterruptedException 
	 * 
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		RestAssured.useRelaxedHTTPSValidation(); 
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		try {
			log.info("Deleting all users");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all users.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
			cleanUpLogin();
			addConnection();
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 100000 );
			connectionMethods.createRealConnection(connectionName,deviceUserName, devicePassword, rxIp, txIp) ;
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}

	/**
	 * Test that the webpage is showing the correct number of devices online
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration", "smoke","emerald", "chrome"})
	public void test01_checkNumberOfDevicesOnline() throws InterruptedException {
		connectionMethods.navigateToActiveConnection(driver);
		log.info("Checking that number of devices on line equals 4");
		//reload page for up to 2 min

		String text = ActiveConnectionElements.noOfDevicesOnline(driver).getText();
		Assert.assertTrue(text.contains("4"), 
				"The number of devices online did not equal 4, actual text: " + text);
	}

	/**
	 * Tests that the page is showing the correct number of active connections
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration", "emerald", "chrome"})
	public void test02_checkNumberOfActiveConnections() throws InterruptedException {
		connectionMethods.navigateToActiveConnection(driver);
		log.info("Checking that number of active connections equals 1");
		Thread.sleep(4000);
		String text = ActiveConnectionElements.noOfActiveConnections(driver).getText();
		log.info("Number of connections are "+text);
		Assert.assertTrue(text.contains("1"), 
				"The number of active connections did not equal 1, actual text: " + text);
	}

	/**
	 * Check active connections table for the correct connection name
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration","emerald", "chrome"})
	public void test03_checkTableForConnectionName() throws InterruptedException {
		log.info("Checking that table contains connection name: " + connectionName);
		String name = connectionMethods.getConnectionNameFromActiveConnectionTable(driver);
		Assert.assertTrue(name.equals(connectionName), "Table does not contain"
				+ " connection name: " + connectionName + ", actual text: " + name);		
	}

	/**
	 * Check active connections table for the correct receiver name
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration", "emerald"})
	public void test04_checkTableForReceiverName() throws InterruptedException {
		log.info("Checking that table contains receiver:" + singleRxName);
		String name = connectionMethods.getReceiverFromActiveConnectionTable(driver);
		Assert.assertTrue(name.equals(singleRxName), "Table does not contain"
				+ " receiver: Test_RX, actual text: " + name);		
	}

	/**
	 * Check active connections table for the correct transmitter name
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration","emerald"})
	public void test05_checkTableForTransmitterName() throws InterruptedException {
		log.info("Checking that table contains transmitter name:" + singleTxName);
		String name = connectionMethods.getTransmitterFromActiveConnectionTable(driver);
		Assert.assertTrue(name.equals(singleTxName), "Table does not contain"
				+ " receiver: Test_TX, actual text: " + name);		
	}

	/**
	 * Check active connections table for the correct user logged in name
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "integration", "emerald"})
	public void test06_checkTableForUserLoggedIn() throws InterruptedException {
		log.info("Checking that table contains user logged in: admin");
		String textFromRow = connectionMethods.getUserFromActiveConnectionTable(driver);
		Assert.assertTrue(textFromRow.contains("admin"), "Table does not contain"
				+ " user logged in: admin, actual test: " + textFromRow);		
	}


	//@Test(groups = {"boxillaFunctional", "integration"})
	public void test07_checkNumberOfDevicesReduced() throws InterruptedException {
		//unmanage a device
		log.info("Test08 - unmanage device and check number of devices online reduces");
		deviceMethods.unManageDevice(driver, txIp);
		deviceMethods.unManageDevice(driver, rxIp);
		//check active connections is 0
		connectionMethods.navigateToActiveConnection(driver);
		String deviceText = ActiveConnectionElements.noOfDevicesOnline(driver).getText();
		Assert.assertTrue(deviceText.contains("0"), 
				"Number of devices online did not contain 0, actual text " + deviceText);
	}


	//there is a bug in the InviaPC rest api where if the connection between devices 
	//goes down, the active connections are not updated. This is causing issues with this test as the active 
	//connection remains active. 
	// [Bug 4879] When connection goes down unexpectedly, receiver REST api is still reporting that a connection is active

	//	@Test(dependsOnMethods = { "test08" })
	//	public void test09() throws InterruptedException {
	//		log.info("Test09 - unmanage device and check number of active connections is 0");
	//		connectionMethods.navigateToActiveConnection(driver);
	//		Assert.assertTrue(ActiveConnectionElements.noOfActiveConnections(driver).getText().contains("0"), 
	//		"The number of active connections did not equal 0");
	//	}


	/**
	 * Method to add connections in boxilla
	 * @throws InterruptedException
	 */
	private void addConnection() throws InterruptedException {
		connectionMethods.addConnection(driver, connectionName, "no"); // connection name, user template
		connectionMethods.connectionInfo(driver, "tx", "user", "user", txIp); // connection via, name, host ip
		connectionMethods.chooseCoonectionType(driver, "private"); // connection type
		connectionMethods.enableExtendedDesktop(driver);
		connectionMethods.enableUSBRedirection(driver);
		connectionMethods.enableAudio(driver);
		connectionMethods.enablePersistenConnection(driver);
		connectionMethods.propertyInfoClickNext(driver);
		connectionMethods.saveConnection(driver, connectionName); // Connection name to assert

		user.manageUser(driver, "admin");
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
			user.saveConnection(driver);
		} else { // if no connection available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 0, "****** Sufficient connection not available to add ********");
		}
	}
}
