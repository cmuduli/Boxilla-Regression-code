package connection;

import org.apache.log4j.Logger;
import org.openqa.selenium.support.ui.Select;
import org.testng.SkipException;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import methods.ConnectionsMethods;
import objects.Connections;

/**
 * Class that contains tests for deleting connection templates in boxilla
 * @author Boxilla
 *
 */
public class DeleteConnectionTemplate extends StartupTestCase {

	ConnectionsMethods methods = new ConnectionsMethods();
	final static Logger log = Logger.getLogger(DeleteConnectionTemplate.class);

	/**
	 * Delete Single Connection Template
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "smoke", "chrome"})
	public void test01_deleteSingleConnectionTemplate() throws InterruptedException { // Delete Single Connection Template
		log.info("Test Case-27 Started - Deleting Single Connection Template ");
		methods.deleteTemplate(driver);

		Thread.sleep(2000);
		Select options = new Select(SeleniumActions.getElement(driver, Connections.nonSelectedList)); // List of available Connection Templates
		if (options.getOptions().size() < 1) { // getting size of the List which contains all available options
			throw new SkipException("Skipping the test case-27 - There is not enough Template to delete");
		} else {
			options.selectByIndex(0);
			Thread.sleep(2000);
			SeleniumActions.seleniumClick(driver, Connections.moveSelectedBtn);
			methods.deleteSelectedTemplate(driver);
			log.info("Deleted Single Connection Template - Test Case-27 Completed");
		}
	}

	/**
	 * Delete Multiple Connection Template
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test02_deleteMultipleConnectionTemplate() throws InterruptedException { // Delete Multiple Connection Template
		log.info("Test Case-28 Started - Deleting Multiple Connection Template ");
		methods.deleteTemplate(driver);
		Thread.sleep(2000);
		Select options = new Select(SeleniumActions.getElement(driver, Connections.nonSelectedList)); // List of available Connection Templates
		if (options.getOptions().size() < 2) { // getting size of the List which contains all available options
			throw new SkipException("Skipping the test case-28 - There is not enough Template to delete");
		} else {
			options.selectByIndex(0);
			options.selectByIndex(1);
			Thread.sleep(2000);
			SeleniumActions.seleniumClick(driver, Connections.moveSelectedBtn);
			methods.deleteSelectedTemplate(driver);
			log.info("Deleted Multiple Connection Template - Test Case-28 Completed");
		}
	}
}
