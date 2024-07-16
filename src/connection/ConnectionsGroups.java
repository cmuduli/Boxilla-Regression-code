package connection;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import objects.Connections;
/**
 * Class that contains tests for Connection Groups in boxilla
 * @author Brendan O Regan
 *
 */
public class ConnectionsGroups extends StartupTestCase {

	private ConnectionsMethods methods = new ConnectionsMethods();
	final static Logger log = Logger.getLogger(ConnectionsGroups.class);

	/**
	 * Adds a single connection group
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addSingleConnectionGroup() throws InterruptedException { // add single connection group
		log.info("Test Case-30 - Add Connections Group");
		methods.navigateToGroups(driver);
		methods.addConnectionGroup(driver, "tx_testgroup");
		log.info("Connection Group added - Test Case-30 Completed");
	}

	/**
	 * Adds connection groups until max limit reached
	 * @throws InterruptedException
	 */
	/**
	 * Adds connection groups until max limit reached
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test02_checkMaxinumConnectionGroups() throws InterruptedException { // add connection group until max limit reach
		log.info("Test Case-31 - Maximum connection group check");
		methods.navigateToGroups(driver); // navigate to Groups page
		methods.timer(driver);
		int currentGroups = 0;
		// Extract total number of connection group present on the page
		if (SeleniumActions.seleniumGetText(driver, Connections.totalAvailableGroups2).contains("1")) {
			currentGroups = Integer.parseInt(SeleniumActions.seleniumGetText(driver, Connections.totalAvailableGroups));
		} else if (SeleniumActions.seleniumGetText(driver, Connections.totalAvailableGroups2).contains("0")) {
			currentGroups = Integer.parseInt(SeleniumActions.seleniumGetText(driver, Connections.totalAvailableGroups2));
		} else {
			throw new SkipException("*** Error in Exracting numbers of current available connections");
		}
		log.info("Total available groups are : " + currentGroups + " adding required groups");
		methods.timer(driver);
		// add missing required connection to reach maximum
		while (currentGroups < 100) {
			String groupName = "testgroup" + Integer.toString((currentGroups + 1)); // Convert string value to Int
			methods.addConnectionGroup(driver, groupName);
			methods.timer(driver);
			currentGroups++;
		}
		methods.timer(driver);
		String btn = SeleniumActions.seleniumGetText(driver, Connections.newGroupBtn);
		Assert.assertTrue(btn.equals(""), "Add group button was enabled");

		methods.timer(driver);
		log.info("Add Group Button Disabled - Deleting Group");
		methods.deleteGroup(driver);
		driver.navigate().refresh();
		btn = SeleniumActions.seleniumGetText(driver, Connections.newGroupBtn);
		Assert.assertTrue(btn.contains("+"), "Add group button was not enabled");
		log.info("Maximum connection group check - Test Case-31 Completed");
	}

	/**
	 * Add Connection to group using filter "via connection
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test03_addToGroupFilterViaName() throws InterruptedException {// Add Connection to group using filter "via connection
		// name" - starting with tx
		log.info("Test Case-32 - Groups > Manage Connections - Add Connection to group");
		methods.navigateToGroups(driver);
		// using test group from created in test one
		methods.addConnectionToSelectedGroup(driver, "tx", "tx_testgroup");
		log.info("Groups > Manage Connections - Add Connection to Group - Test Case-32 Completed");
	}

	/**
	 * Edit Connection Group - Change group name
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test04_editConnectionGroupName() throws InterruptedException { // Edit Connection Group - Change group name
		log.info("Test Case-33 - Groups > Edit");
		methods.navigateToGroups(driver);
		methods.editGroupName(driver, "editedName");
		log.info("Groups > Edit - Test Case-33 Completed");
	}

	/**
	 * Deletes connection group
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test05_deleteConnectionGroup() throws InterruptedException { // Delete Connection Group
		log.info("Test Case-34 - Groups > Delete");
		methods.navigateToGroups(driver);
		methods.deleteGroup(driver);
		log.info("Groups > Delete - Test Case-34 Completed");
	}

	/**
	 * Adds group to user
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test06_addGroupToUser() throws InterruptedException { // Add Group to User
		log.info("Test Case-35 - Add Group to User");
		methods.addGroupToUser(driver, "admin", "tx_testgroup");
		log.info("Successfully added group to User - Test Case-35 Completed");
	}

	/**
	 * Dissolves group
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test07_dissolveGroup() throws InterruptedException { // Dissolve Group
		log.info("Test Case-36 - Dissolve Group");
		methods.dissolveGroup(driver, "admin", "tx_testgroup");
		log.info("Dissolve Group - Successfully dissolved the group - Test Case-36 Completed");
	}
}
