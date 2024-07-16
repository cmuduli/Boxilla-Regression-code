package dashBoard;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
import objects.Devices;
import switches.upgrades.SwitchUpgrades;

public class DashBoard extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(DashBoard.class);
	DevicesMethods method=new DevicesMethods();
     
	
	
	// Test that will veryfy the Boxilla Icon presence 
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" })
	public void test01_verifyIcon() 
	{
		WebElement icon=driver.findElement(By.xpath(Devices.iconimageXpath));
		String  iconUrl=icon.getAttribute("src");
		log.info("icon url is "+ iconUrl);
		Assert.assertTrue(iconUrl.contains("boxilla_logo_white.png"),"The Boxilla image is not being displayed properly on the webpage Actual is:"+ iconUrl);
		log.info("Boxilla Image verifyed Successfylly");
		
		
	}
	// Test that will verify the LaunchIcon
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" })
	public void test02_VerifyLaunchIcon() throws InterruptedException 
	{
		
		WebElement ele=driver.findElement(By.xpath(Devices.lunchIconXpath));
		
		if (ele.isDisplayed()) {
			log.info(" Lunch Icon is displayed in the page");
			String currentWindowHandle = driver.getWindowHandle();
			log.info("Moving to another window");
			ele.click();
			Thread.sleep(5000);
			String currentURL=driver.getCurrentUrl();
			log.info("Current URL is: "+ currentURL);
			Assert.assertTrue(currentURL.contains("10.231.128.72"),"Current window dose not contain the URL Actual: "+currentURL);
			log.info("Sucessfully the Boxilla in launched in another tab ");
			driver.close();
//			driver.switchTo().window(currentWindowHandle);
			
		}
	}
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" })
	public void test03_verifyNotification() throws InterruptedException 
	{
		
		Actions Act=new Actions(driver);
		Act.moveToElement(driver.findElement(By.xpath(Devices.notificationIcon))).perform();
	Thread.sleep(3000);
		String title=driver.findElement(By.xpath(Devices.notificationtitlexpath)).getAttribute("data-original-title");
		System.out.println("Title is "+ title);
	Assert.assertTrue(title.contains("Notifications"),"Notifiction Icon dose not present on the page Actual "+ title);
		log.info("Notification image is visible on the dashboard page test pass successfully");
	}
	
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" })
	public void test04_verifyHelpLogo() throws InterruptedException 
	{
		Actions Act=new Actions(driver);
		Act.moveToElement(driver.findElement(By.id("dropdownMenu1"))).perform();
		Thread.sleep(3000);
		String helplogo=driver.findElement(By.xpath(Devices.helplogoXpath)).getAttribute("data-original-title");
		log.info("Help logo is "+ helplogo);
		Assert.assertTrue(helplogo.contains("Help"),"Help logo is missing from the dashboard page Actual: " + helplogo );
	}
	
	
	
}
