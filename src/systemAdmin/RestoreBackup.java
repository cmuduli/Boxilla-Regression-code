package systemAdmin;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.SystemMethods;
import objects.ActiveConnectionElements;
import objects.Landingpage;
import objects.SystemAll;
import soak.RestoreResetDB;
import testNG.Utilities;

public class RestoreBackup extends StartupTestCase {
	
	
	private SystemMethods methods = new SystemMethods();
	final static Logger log = Logger.getLogger(RestoreBackup.class);
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods device = new DevicesMethods();
	private String privateConnectionName = "RestoreConnection";
	 DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH");
	 String todayDate = dateFormat.format(today());
	 String timeZone="Asia/Kolkata";
	SoftAssert asst=new SoftAssert();
	
	private Date today() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, 0);
		return cal.getTime();
	}	
	
	
	
	/**
	 * Test that will take the current date backup of the Boxilla. 
	 * @throws InterruptedException 
	 * 
	 * 
	 */
	
	
	 @Test(priority = 1)
	 public void test01_backUpDB() throws InterruptedException
	 {
		 log.info("Test Preparation - Taking Database Backup");	
	     methods.backupDatabase(driver);
	     log.info("Checking BackUp file  is Generated or Not");
//	     DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
//		 String todayDate = dateFormat.format(today());
			log.info("Todays BackUp file Date is:  "+ todayDate);
			methods.checkBackup(driver, todayDate);
			String time=SystemAll.backupTime(driver).getText();
			log.info(time);
			Assert.assertTrue(time.contains(todayDate),"Today back_up is not avilable"); 
			log.info("Deleting the connection");
			connections.deleteConnection(driver, privateConnectionName);
			log.info("Unmanageing the RX Device");
//			device.unManageDevice(driver, rxIp);
//			log.info("Unmanageing the TX Device");
//			device.unManageDevice(driver, txIp);
			
			
	 }
	 
	 @Test(priority = 2)
	 public void test02_restoreDBBackUp() throws InterruptedException 
	 {
		 log.info("Test Preparation - Checking connection and appiance after restore Backup");	
		 methods.navigateToSystemAdmin(driver);
			methods.timer(driver);
			SystemAll.backupRestoreTab(driver).click();
			log.info("Backup and Restore Tab Clicked");
			methods.activateBackup(driver, todayDate);
			log.info("Now check the connection is alive or not after restoring the DB");
			connections.checkConnectionExists(driver, privateConnectionName);
			connections.navigateToActiveConnection(driver);
			log.info("Checking that number of devices on line equals 4");
			//reload page for up to 2 min

			String text = ActiveConnectionElements.noOfDevicesOnline(driver).getText();
			Assert.assertTrue(text.contains("4"), 
					"The number of devices online did not equal 4, actual text: " + text);
			log.info("Check all the devices are online or not");
					device.checkDeviceOnline(driver, txIp);
					device.checkDeviceOnline(driver, rxIp);
					device.checkDeviceOnline(driver, txIpDual);
					device.checkDeviceOnline(driver, rxIpDual);
//			log.info("Now check the appliaces are online or not");
//			device.navigateToOptions(driver, txIp);
//			device.navigateToOptions(driver, rxIp);
			
		 
	 }
	 
	 
	 
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		try {
			cleanUpLogin();
			methods.navigateToSystemSettings(driver);
			log.info("Navigating to clock Tab");
			SeleniumActions.seleniumClick(driver,SystemAll.clocktab);
			log.info("Clearing the TimeZone field");
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll. timeZoneSearchbox);
			log.info("Adding the Asian TimeZone");
			
			SeleniumActions.seleniumSendKeys(driver, SystemAll.timeZoneSearchbox,timeZone);
//			SeleniumActions.seleniumSendKeys(driver, timeZone,SystemAll. timeZoneSearchbox);
			SeleniumActions.seleniumClick(driver, SystemAll. timeZoneSubmitbtn);
			new WebDriverWait(driver, 130).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
			log.info("Discovery complete");
			
			
			
			
//			device.navigateToOptions(driver, txIp);
//			device.navigateToOptions(driver, rxIp);
			connections.navigateToActiveConnection(driver);
			log.info("Checking that number of devices on line equals 4");
			//reload page for up to 2 min

			String text = ActiveConnectionElements.noOfDevicesOnline(driver).getText();
			Assert.assertTrue(text.contains("4"), 
					"The number of devices online did not equal 4, actual text: " + text);
			connections.createTxConnection(privateConnectionName, "private", driver, txIp);
		} catch (Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}


}
