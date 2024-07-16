package user;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.UsersMethods;
import objects.Devices;
import objects.Users;
import testNG.Utilities;

public class ManageUsers extends StartupTestCase {

	private UsersMethods methods = new UsersMethods();
	final static Logger log = Logger.getLogger(ManageUsers.class);

	public ManageUsers() throws IOException {
		super();
	}

	@Test(groups= {"chrome"})
	public void test01_editUserChangeUsername() throws InterruptedException { // Edit User - Change Username
		log.info("Test Case-53 Started - Edit Username of existing User");
		methods.manageUser(driver, "test6");
		methods.userEditName(driver, "test6", "nameedited");
		log.info("Edited Username Successfully - Test Case-53 Completed");
	}

	@Test
	public void test02_deleteUser() throws InterruptedException { // Delete User
		log.info("Test Case-54 Started - Delete User");
		methods.manageUser(driver, "test4");
		Thread.sleep(2000);
		Users.userDeleteTab(driver, "test4").click();
		log.info("User Delete Tab Clicked");
		Thread.sleep(2000);
		Alert alert = driver.switchTo().alert();
		alert.accept();
//		Thread.sleep(3000);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Devices.getDeviceToastMessage(driver)));
		String message=Users.notificationMessage(driver).getText();
		System.out.println(message);
		Assert.assertTrue(message.contains("Successfully deleted user"),
				"***** Error in Deleting User *****");
		log.info("User Deleted Successfully - Test Case-54 Completed");
	}

	@Test
	public void test03_addSingleConnectionToUser() throws InterruptedException { // Manage Connection - add single connection to user
		log.info("Test Case-55 Started - Add Connection to User");
		methods.manageUser(driver, "test1");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "test1").click();
		Thread.sleep(2000);
		Select options = new Select(Users.nonSelectedActiveConnectionList(driver)); // List of available
		// Connection to add
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Add Connection if Connection available to add
			options.selectByIndex(0);
			Thread.sleep(2000);
			Users.moveSelectedConenction(driver).click();
			methods.saveConnection(driver);
			log.info("Connection added Successfully - Test Case-55 Completed");
		} else { // if no connection available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 0, "****** Sufficient connection not available to add ********");
		}
	}

	@Test
	public void test04_addMultipleConnectionsToUser() throws InterruptedException { // Manage Connection - add multiple connection to user
		log.info("Test Case-56 Started - Add Multiple Connection to User");
		methods.manageUser(driver, "test1");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "test1").click();
		Thread.sleep(2000);
		Select options = new Select(Users.nonSelectedActiveConnectionList(driver)); // List of available
		// Connection to add
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 1) { // Add connection if Multiple Connection available to add
			options.selectByIndex(0);
			Thread.sleep(1000);
			options.selectByIndex(1);
			Thread.sleep(2000);
			Users.moveSelectedConenction(driver).click();
			methods.saveConnection(driver);
			log.info("Connection added Successfully - Test Case-56 Completed");
		} else { // if multiple connection not available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 1, "****** Sufficient connection not available to add ********");
		}
	}

	@Test
	public void test05_addAllConnectionToUser() throws InterruptedException { // Manage Connection - add all connection to user
		log.info("Test Case-57 Started - Add All Connection to User");
		methods.manageUser(driver, "test1");
		Thread.sleep(5000);
		Users.userManageConnectionTab(driver, "test1").click();
		log.info("Manage Connection option Clicked");
		Thread.sleep(2000);
		// List of non-selected connections
		Select options = new Select(Users.nonSelectedActiveConnectionList(driver));
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Add all Connection, if Connection available to add
			Users.moveAllConnection(driver).click();
			methods.saveConnection(driver);
			log.info("Connection added Successfully - Test Case-57 Completed");
		} else { // if no connection available, exit test and mark test case -failure
			Assert.assertTrue(connectionListSize > 0, "****** Sufficient connection not available to add ********");
		}
	}

	@Test
	public void test06_removeSingleAssignedConnection() throws InterruptedException { // Manage User : Remove Single Assigned Connection
		log.info("Test Case-58 Started - Removing Single Assigned Connection");
		methods.manageUser(driver, "test1");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "test1").click();
		log.info("Manage Connections option Clicked");
		Thread.sleep(2000);
		Select options = new Select(Users.selectedActiveConnectionList(driver)); // List of Selected Connections
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Remove Connection if Connection available to remove
			options.selectByIndex(0);
			Thread.sleep(2000);
			Users.removeSelectedConnection(driver).click();
			methods.saveConnection(driver);
			log.info("Single Assigned Connection removed - Test Case-58 Completed");
		} else { // if no connection available, exit test and mark test case -failure
			/*Assert.assertTrue(connectionListSize > 0,
						"****** Sufficient connection not available to remove ********");*/
			throw new SkipException("****** Sufficient connection not available to remove ********");
		}
	}

	@Test
	public void test07_removeMultipleAssignedConnection() throws InterruptedException { // Manage User : Remove Multiple Assigned Connection
		log.info("Test Case-59 Started - Removing Multiple Assigned Connection");
		methods.manageUser(driver, "test1");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "test1").click();
		log.info("Manage Connections option Clicked");
		Thread.sleep(2000);
		Select options = new Select(Users.selectedActiveConnectionList(driver)); // List of Selected Connections
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 1) { // Remove Connection if Connection available to remove
			options.selectByIndex(0);
			Thread.sleep(2000);
			options.selectByIndex(1);
			Thread.sleep(2000);
			Users.removeSelectedConnection(driver).click();
			methods.saveConnection(driver);
			log.info("Multiple Assigned Connection removed - Test Case-59 Completed");
		} else { // if no connection available, exit test and mark test case -failure
			/*Assert.assertTrue(connectionListSize > 0,
				"****** Sufficient connection not available to remove ********");*/
			throw new SkipException("****** Sufficient connection not available to remove ********");
		}
	}

	@Test
	public void test08_removeAllAssignedConnections() throws InterruptedException { // Manage User : Remove All Assigned Connection
		log.info("Test Case-60 Started - Removing All Assigned Connection");
		methods.manageUser(driver, "test1");
		Thread.sleep(2000);
		Users.userManageConnectionTab(driver, "test1").click();
		log.info("Manage Connections option Clicked");
		Thread.sleep(2000);
		Select options = new Select(Users.selectedActiveConnectionList(driver)); // List of Selected Connections
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Remove Connection if Connection available to remove
			Users.removeAllConnection(driver).click();
			methods.saveConnection(driver);
			log.info("All Assigned Connections removed - Test Case-60 Completed");
		} else { // if no connection available, exit test and mark test case -failure
			/*Assert.assertTrue(connectionListSize > 0,
				"****** Sufficient connection not available to remove ********");*/
			throw new SkipException("****** Sufficient connection not available to remove ********");
		}
	}
}
