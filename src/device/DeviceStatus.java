package device;


import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.ZoneMethods;
import objects.Devices;
/**
 * Class that contains tests for device status
 * @author Brendan O Regan
 *
 */
public class DeviceStatus extends StartupTestCase {
	private DevicesMethods methods = new DevicesMethods();
	private DiscoveryMethods discoverymethods = new DiscoveryMethods();
	private ZoneMethods zones = new ZoneMethods();
	final static Logger log = Logger.getLogger(DeviceStatus.class);

	/**
	 * Test that retrieves managed device details through Boxilla
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald", "chrome", "quick"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_getManagedDeviceDetails() throws InterruptedException { // Retrieve Managed Device Details
		getDevices();
		log.info("Test Case-66 Started - Retrieve Managed Device Details");
		methods.retrieveDetails(driver, txIp, txSingle.getMac(), txSingle.getDeviceName());
		log.info("Device Details Retrieved - Test Case-66 is Completed");
	}

	/**
	 * Test that pings managed device through Boxilla
	 * @throws InterruptedException
	 */

	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test02_pingManagedDevice() throws InterruptedException { // Ping managed device
		getDevices();
		log.info("Test Case-67 Started - Ping Managed Device");
		methods.pingDevice(driver, txIp);
		log.info("Managed device ping - Test Case-67 is Completed");
	}

	/**
	 * Test that reboots device through Boxilla
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test03_rebootDevice() throws InterruptedException { // Reboot device
		getDevices();
		log.info("Test Case-68 Started - Reboot device");
		methods.retrieveDetails(driver, txIp, txSingle.getMac(), txSingle.getDeviceName());
		float oldUptime = methods.uptime(driver);
		//driver.findElement(By.xpath("//div[@id='appliance-details-modal']//button[@type='button'][normalize-space()='Close']")).click();
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(3000);
		methods.rebootDevice(driver, txIp);
		methods.retrieveDetails(driver, txIp, txSingle.getMac(), txSingle.getDeviceName());
		float newUptime = methods.uptime(driver);
		Assert.assertTrue(newUptime < oldUptime, "***** New Uptime is higher than Old Uptime *****");
		log.info("Reboot Device - Test Case-68 is Completed");
	}

	/**
	 * Test that edits device through Boxilla
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test04_editDeviceIp() throws InterruptedException { // Edit Device
		getDevices();
		log.info("Test Case-69 Started - Edit Device");
		String oldIp = txIp;
		// Change IP address to New IP and revert it back to original IP address
		methods.editDevice1(driver, txIp, prop.getProperty("extraIP1"));
			methods.editDevice1(driver, prop.getProperty("extraIP1"), oldIp);
		log.info("Edit device - Test Case-69 is Completed");
	}

	/**
	 * Test that changes device name through Boxilla
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test05_changeDeviceName() throws InterruptedException { // Change Device name
		getDevices();
		log.info("Test Case-70 Started - Change Device name");
		// Using IP address to search for device

		methods.changeDeviceName(driver, txIp, "editedHostName");
		log.info("device name changed. CHanging back to original");
//		String deviceOriginalName = "";
		//just awful testing
		if(StartupTestCase.isEmerald) {
			String deviceOriginalName = txEmerald.getDeviceName();
			log.info("Device original name is "+ deviceOriginalName);
			methods.changeDeviceName(driver, txIp,deviceOriginalName);
		}else {
			String deviceOriginalName = txSingle.getDeviceName();
			log.info("Device original name is "+ deviceOriginalName);
			methods.changeDeviceName(driver, txIp,deviceOriginalName);
		}
//		methods.changeDeviceName(driver, txIp,deviceOriginalName);

		log.info("Edit device - Test Case-70 is Completed");
	}
	/*
	 * This is an awful test where you create an appliance template without giving it a name.
	 * */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test06_AddDeviceTemplate_WithaoutTemplateName() throws InterruptedException 
	{
		log.info("Attempting to add a template for transmitter without adding Template Name");
		methods.navigateToDevicePropertyCreateTemplate(driver);
		Devices.applianceTypeDropdown(driver, false);
		Devices.templateNameTextBox(driver).sendKeys("");
		SeleniumActions.seleniumDropdown(driver, Devices.videoQualityDropdownXpath,"Best Quality" );
		Devices.videoSourceDropdown(driver, "Off");
		Devices.HIDConfigurationDropdown(driver,"Basic");
		Devices.mouseTimeoutDropdown(driver, "0");
		Devices.EdidSettingsDvi1Dropdown(driver,"1920x1080");
		Devices.EdidSettingsDvi2Dropdown(driver, "1920x1080");
		Thread.sleep(3000);
		Devices.saveTemplateTxBtn(driver).click();
		Thread.sleep(4000);

		// assert template is created
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Error"), "Toast message did not contain Error, actual " + message);
		
		log.info("The system does not allow creating templates without a template name. Test pass");
	}
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test07_addBulkUpdate_withoutTemlate() throws InterruptedException 
	{
		log.info("Attempting to add a Bulk_Update for a Reciver without adding Template");
		log.info("Attempting bulk update");
		methods.navigateToBulkUpdate(driver);
		Devices.bulkUpdateApplianceTypeDropdown(driver, "Receiver");
		SeleniumActions.seleniumDropdown(driver, Devices.bulkUpdateTemplateNameDropdownXpath, "");
		Devices.bulkUpdateSearchBox(driver).sendKeys(rxSingle.getDeviceName());
		SeleniumActions.seleniumClick(driver, Devices.getBulkUpdateDeviceCheckboxXpath());
		Devices.bulkUpdateSearchBox(driver).clear();
		SeleniumActions.seleniumClick(driver, Devices.getBulkUpdateSaveBtnXpath());
		String message=Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Error"), "Toast message did not contain Error, actual " + message);
		log.info("The system does not allow to update Bulk Update setting  without a template. Test pass");
		
	}
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test08_addBulkUpdate_WithOutaddingDevice() throws InterruptedException 
	{
		log.info("Attempting to add a Bulk_Update for a Reciver/Transmitter without adding Device");
		log.info("Attempting bulk update");
		methods.navigateToBulkUpdate(driver);
		Devices.bulkUpdateApplianceTypeDropdown(driver, "Receiver");
		SeleniumActions.seleniumDropdown(driver, Devices.bulkUpdateTemplateNameDropdownXpath, "System Properties");
		SeleniumActions.seleniumClick(driver, Devices.getBulkUpdateSaveBtnXpath());
		String message=Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Error"), "Toast message did not contain Error, actual " + message);
		log.info("The System does not allow to update Bulk Update setting  without adding the device name. "
				+ "Test pass");
		
	}
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test09_forceLogOutRX() throws InterruptedException
	{
		log.info("Attempting to force logout from RX");
		methods.forceLogout(driver, rxIp);
		log.info("Force logout Device - Test Case is Completed");
		
	}
//	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test10_Change_Device_Zone_for_Rx() throws InterruptedException 
	{
		String zoneName = "TestZoneA";
		String zoneDescription = "This is a zone used for testing";
		zones.addZone(driver, zoneName, zoneDescription);
        methods.changeDeviceZone(driver, rxIp);	
        log.info("Change Device Zone- Test case is completed ");
		
	}
	
	
	
	
}

