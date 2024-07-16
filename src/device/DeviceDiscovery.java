package device;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import objects.Discovery;
import objects.Landingpage;
import objects.Users;

/**
 * Class that contains tests for device discovery
 * @author Brendan O Regan
 *
 */

public class DeviceDiscovery extends StartupTestCase {
	private DiscoveryMethods methods = new DiscoveryMethods();
	DevicesMethods devMethods = new DevicesMethods();
	final static Logger log = Logger.getLogger(DeviceDiscovery.class);
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(StartupTestCase.getWaitTime(), TimeUnit.SECONDS);
	}


	// Manage TX device automatically
	/**
	 * Manage TX device automatically
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional"}, retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_addApplianceAutomaticDiscovery() throws InterruptedException {
		log.info("Unmanage device first");
		devMethods.unManageDevice(driver, rxIpDual);
		log.info("Test Case-61 Started - Adding Appliance using Automatic Discovery");
		methods.discoverDevices(driver);
		methods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(),rxDual.getNetmask());
		methods.manageApplianceAutomatic(driver, rxDual.getDeviceName(), rxDual.getMac(),
				prop.getProperty("ipCheck"));
		log.info("Appliance Added using Automatic Discovery - Test Case-61 is Completed");
	}

	/**
	 * Test that will unmanage an appliance and then manage the appliance manually
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test02_manageApplianceManually() throws InterruptedException { // Manage Appliance manually
		log.info("Unmanage device first");
//		devMethods.unManageDevice(driver, rxIpDual);
		log.info("Test Case-62 Started - Adding Appliance Manually");
		methods.discoverDevices(driver);
		methods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(),rxDual.getNetmask());
		methods.addDeviceManually(driver, rxDual.getMac(), rxDual.getDeviceName(), rxIpDual);
		log.info("Appliance Added manually - Test Case-62 is Completed");
	}
	/**
	 * Test that will verify manage a random appliance(Expected an error message)
	 * @throws InterruptedException 
	 * */
	@Test(groups= {"boxillaFunctional"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test03_manageAppliancewithrandomIp() throws InterruptedException 
	{
		
		Landingpage.discoveryTab(driver).click();
		log.info("Landing Page > Discovery tab clicked");
		Thread.sleep(3000);
		SeleniumActions.seleniumClick(driver, Discovery.addManuallyTab);
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Discovery.manualSearchIPaddBox, "192.168.1.28");
		log.info("IP Address Entered");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Discovery.getInfoBtn);
		log.info("Get Information Button Clicked");
		timer(driver);
		int i = 0;
		// Waiting to retrive device information
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			log.info((i + 1) + ". Retrieving Device information...");
			Thread.sleep(2000);
			i++;
		}
		String message=Users.notificationMessage(driver).getText();
		//for bxa 5.0
		Assert.assertTrue(message.contains("Failed to connect to device."),"Notification did not contain Error message Actual: "+message);
		//for 4.9 and others 
		//Assert.assertTrue(message.contains("Failed to retrieve appliance"),"Notification did not contain Error message Actual: "+message);
	}
	/**
	 * Test that will ping the Devices IP manually
	 * @throws InterruptedException 
	 */

	@Test(groups= {"boxillaFunctional"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test04_pingDeviceIP() throws InterruptedException 
	{
		Landingpage.discoveryTab(driver).click();
		log.info("Landing Page > Discovery tab clicked");
		methods.pingDeviceIP(driver, rxIpDual);
		
	}
	/**
	 * Test that will ping an invalid IP
	 * @throws InterruptedException 
	 * 
	 */
	@Test(groups= {"boxillaFunctional"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test05_pingInvaliDeviceIP() throws InterruptedException 
	{
		Landingpage.discoveryTab(driver).click();
		log.info("Landing Page > Discovery tab clicked");
		SeleniumActions.seleniumClick(driver, Discovery.pingBtnXpath);
		log.info("Ping Button clicked ");
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.deviceIpSearchXpath);
		log.info("Clear the Device search field");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Discovery.deviceIpSearchXpath, "10.211.130.866");
		log.info("Send the Device IP");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Discovery.pingBtn);
		log.info("Device IP Pinged and waiting to dissapear the spinning Button ");
		int i = 0;
		// Waiting to retrive device information
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			log.info((i + 1) + ". Retrieving Device information...");
			Thread.sleep(2000);
			i++;
		}
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Unable to Ping"),
				"Notification message did not contain: Error, actual text: " + notificationMessage);
		log.info("Invalid IP Pinged and got the errro message");
		
	}
}
