package user;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.UsersMethods;
import objects.Users;
import testNG.Utilities;

public class DeleteUserTemplate extends StartupTestCase {

	private UsersMethods methods = new UsersMethods();
	final static Logger log = Logger.getLogger(DeleteUserTemplate.class);

	public DeleteUserTemplate() throws IOException {
		super();
	}

	@Test(groups= { "chrome"})
	public void test01_deleteSingleUserTemplate() throws InterruptedException { // Delete Single User Template
		log.info("Test Case-50 Started - Delete Single User Template");
		methods.deleteTemplate(driver);

		Thread.sleep(2000);
		Select options = new Select(Users.nonSelectedList(driver)); // List of available User Templates
		if (options.getOptions().size() < 1) { // getting size of the List which contains all available options
			throw new SkipException("Skipping the test case-50 - There is not enough Template to delete");
		} else {
			options.selectByIndex(0);
			Thread.sleep(2000);
			Users.moveSelectedTemplate(driver).click();
			methods.deleteSelectedTemplate(driver);
			log.info("Deleted Single User Template - Test Case-50 Completed");
		}
	}

	@Test
	public void test02_deleteMultipleUserTemplates() throws InterruptedException { // Delete Multiple User Template
		log.info("Test Case-51 Started - Delete Multiple User Template");
		methods.deleteTemplate(driver);

		Select options = new Select(Users.nonSelectedList(driver));
		if (options.getOptions().size() < 2) { // getting size of the List which contains all available options
			throw new SkipException("Skipping the test case-51 - There is not enough Template to delete");
		} else {
			options.selectByIndex(0);
			options.selectByIndex(1);

			Thread.sleep(2000);
			Users.moveSelectedTemplate(driver).click();
			methods.deleteSelectedTemplate(driver);
			log.info("Deleted Multiple User Template - Test Case-51 Completed");
		}
	}

	/*		@Test
				public void testcase52() throws InterruptedException { // Delete All User Template
					log.info("Test Case-52 Started - Delete All User Template");
					methods.deleteTemplate(driver);

					Thread.sleep(2000);
					Select options = new Select(Users.nonSelectedList(driver)); // List of available User Templates
					if (options.getOptions().size() < 1) { // getting size of the List which contains all available options
						throw new SkipException("Skipping the test case-52 - There is not enough Template to delete");
					} else {
						Users.moveAllTemplate(driver).click();
						methods.deleteSelectedTemplate(driver);
						log.info("Deleted All User Template - Test Case-52 Completed");
					}
				}*/
}
