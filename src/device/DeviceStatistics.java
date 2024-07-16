package device;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import objects.Landingpage;
import testNG.Utilities;

public class DeviceStatistics extends StartupTestCase2 {
	final static Logger log = Logger.getLogger(DeviceUpgrades.class);
	private static WebElement element = null;
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods device = new DevicesMethods();
	private String privateConnectionName = "privateTest";
	
	
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" },retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_VerifyRequiredFields() throws InterruptedException
	{
		
		
		log.info("***** test01_makePrivateConnection *****");
		String[] connectionSources = { privateConnectionName };
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, privateConnectionName, singleRxName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);
		Landingpage.devicesTab(driver).click();
		log.info("Upgrading device : Devices dropdown clicked");
		driver.findElement(By.xpath("//span[contains(text(),'Statistics')]")).click();
		driver.findElement(By.xpath("//input[@type='search']")).sendKeys(rxIp);
		String tablecontent=tableinfo(driver).getText();
		log.info("Table content is "+ tablecontent);
		
		String deviceName=SeleniumActions.seleniumGetInnerText(driver,tableColumn("1"));
		Assert.assertTrue(tablecontent.contains(deviceName),"Table dose not contain the Device name Actual "+ deviceName );
		String deviceIP=SeleniumActions.seleniumGetInnerText(driver,tableColumn("2"));
		Assert.assertTrue(tablecontent.contains(deviceIP),"Table dose not contain the DeviceIP  Actual "+ deviceIP );
		String deviceModelNo=SeleniumActions.seleniumGetInnerText(driver,tableColumn("3"));
		Assert.assertTrue(tablecontent.contains(deviceModelNo),"Table dose not contain the deviceModelNo  Actual "+ deviceModelNo );
		String currentUser=SeleniumActions.seleniumGetInnerText(driver,tableColumn("4"));
		Assert.assertTrue(tablecontent.contains(currentUser),"Table dose not contain the currentUser name Actual "+ currentUser );
		String ConnectedToDevice=SeleniumActions.seleniumGetInnerText(driver,tableColumn("5"));
		Assert.assertTrue(tablecontent.contains(ConnectedToDevice),"Table dose not contain the ConnectedToDevice name Actual "+ ConnectedToDevice );
		String Timeofconnection=SeleniumActions.seleniumGetInnerText(driver,tableColumn("6"));
		Assert.assertTrue(tablecontent.contains(Timeofconnection),"Table dose not contain the Timeofconnection Actual "+ Timeofconnection );
		String DurationconnectionActive=SeleniumActions.seleniumGetInnerText(driver,tableColumn("7"));
		Assert.assertTrue(tablecontent.contains(DurationconnectionActive),"Table dose not contain the DurationconnectionActive Actual "+ DurationconnectionActive);
		String LastUsrLoggedIn=SeleniumActions.seleniumGetInnerText(driver,tableColumn("8"));
		Assert.assertTrue(tablecontent.contains(LastUsrLoggedIn),"Table dose not contain the LastUsrLoggedIn Actual "+ LastUsrLoggedIn);
		String DurationOFLastConnection=SeleniumActions.seleniumGetInnerText(driver,tableColumn("9"));
		Assert.assertTrue(tablecontent.contains(DurationOFLastConnection),"Table dose not contain the DurationOFLastConnection Actual "+ DurationOFLastConnection);
		String UpTime=SeleniumActions.seleniumGetInnerText(driver,tableColumn("10"));
		Assert.assertTrue(tablecontent.contains(UpTime),"Table dose not contain the UpTime Actual "+ UpTime);		
		
		log.info("Now Breaking the Connection");
		connections.breakConnection(driver, privateConnectionName);
		
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		try {
			cleanUpLogin();
			connections.createTxConnection(privateConnectionName, "private", driver, txIp);
			device.recreateCloudData(rxIp, rxIpDual);
			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
			log.info("Sleep while devices reboot...");
			device.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 100000);
		} catch (Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}
	
	
	public static WebElement tableinfo(WebDriver driver) 
	{
		element=driver.findElement(By.xpath(".//*[@id='appliances-table']"));
		
		return element;
	}
     public String tableColumn(String index) 
     {
    	 return ".//*[@id='appliances-table']//tr//td[" + index + "]";
    	 
     }
}
