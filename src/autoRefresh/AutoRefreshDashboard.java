package autoRefresh;

import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DashboardMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import objects.Landingpage;
import objects.Users;
import testNG.Utilities;
/**
 * This class contains tests that will check the Boxilla auto 
 * refresh feature on the dashboard page
 * @author Boxilla
 *
 */
public class AutoRefreshDashboard extends StartupTestCase {

	private DashboardMethods dashboard = new DashboardMethods();
	private String connectionName = "dashboardConnection";
	private UsersMethods users = new UsersMethods();
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();

	/**
	 * This method will run before any tests in this class runs.
	 * It will create a connection in Boxilla, then update the appliance xml file, 
	 * launch the connection and verify the connection is running
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		try {
			cleanUpLogin();
			addConnection();
			deviceMethods.recreateCloudData(rxIp, txIp);
			deviceMethods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 100000 );
			connectionMethods.createRealConnection(connectionName,deviceUserName, devicePassword, rxIp, txIp) ;
			connectionMethods.checkConnectionIsActiveSsh(deviceUserName, devicePassword, rxIp, connectionName);
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	/**
	 * Test that will check that the bandwidth displayed on the dashboard for 
	 * connection will auto update if the page is left idle
	 * @throws InterruptedException
	 */
	@Test
	public void test01_bandwidthAutoRefresh() throws InterruptedException {
		String originalBandwidth = dashboard.getDashboardBandwidth(driver);
		String newBandwidth = connectionMethods.autoRefreshPoll(originalBandwidth, Landingpage.getDashboardBandwith(), driver);
		Assert.assertFalse(originalBandwidth.equals(newBandwidth), "Bandwidth did not update, original value:" + 
				originalBandwidth + ", new value:" + newBandwidth);
	}

	/**
	 * Test that kills a running connection and checks that 
	 * the dashboard connection will auto update if left idle
	 * @throws InterruptedException
	 */
	@Test
	public void test02_connectionEndAutoRefresh() throws InterruptedException {
		String originalConnection = dashboard.getConnectionNameFromNetworkBWTable(driver);
		deviceMethods.rebootDevice(driver, rxIp);
		String newName =  connectionMethods.autoRefreshPoll(originalConnection, Landingpage.getNetworkBwTableConName(), driver);
		Assert.assertFalse(originalConnection.equals(newName), "Connection names match, original name:" + 
				originalConnection + ", new name:" + newName);
	}

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
