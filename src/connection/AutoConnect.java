package connection;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import objects.Users;
import testNG.Utilities;

class AutoConnect extends StartupTestCase {

	final static Logger log = Logger.getLogger(AutoConnect.class);
	private UsersMethods userMethods = new UsersMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String connectionName = "autoConnect";

	/**
	 * Overrides superclass method to create real connection for tests
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			log.info("**** Starting test setup for " + this.getClass().getSimpleName() + " ****");
			cleanUpLogin();
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();

			//create connection
			conMethods.createTxConnection(connectionName, "private", driver, txIp);
			userMethods.editUser(driver, "admin", connectionName);

			//add connection to user
			userMethods.manageUser(driver, "admin");
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
				userMethods.saveConnection(driver);
			}
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDevice(driver, rxIp);
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}

	@Test
	public void test01() throws InterruptedException {
		given().header(getHead())
		.when()
		.get(getHttp() + "://" + rxIp + getPort() + "/statistics/active_connections")
		.then()
		.body("kvm_active_connections.name[0]", equalTo(connectionName));
		Thread.sleep(30000000);
	}

}
