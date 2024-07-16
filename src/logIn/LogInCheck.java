package logIn;


import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.Assert;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Rest.AlertsTransmitter.Login;
import objects.Loginpage;

public class LogInCheck extends StartupTestCase {
	final static Logger log = Logger.getLogger(LogInCheck.class);
	
	
	
	@Test(priority=1)
	public void checkLogin() throws InterruptedException 
	{
		log.info("Test case-1-check login fuctionality with valid inputs ");
		logInWithValidData(driver);
		String pageTitle=driver.getTitle();
		log.info("Title of the page is "+ pageTitle);
		log.info("Asserting the Home page");
		Assert.assertTrue(driver.getTitle().contains("Boxilla - Dashboard"), "Not logged in to Boxilla");
		driver.close();
		
	}
	@Test(priority=2)
	public void checkinvalidPassword()
	{
		log.info("Test case-2-check login with invalid password ");
		logInWithInvalidPassword(driver);
		
	}	
	
	@Test(priority=3)
	public void checkInValidUserName() 
	{
		log.info("Test case-3-check login with invalid username ");
		logInWithInvalidUsername(driver);
	}
	
	@Test(priority=4)
	public void checkInvalidUserPassword() {
		
		log.info("Test case-4-check login with invalid password and username ");
		logInWithInvalidUsernamePassword(driver);
		
	}
	@Test(priority=5)
	public void checkempty()
	{
		log.info("Test case-5-check login with emety field ");
		emptyUsernamePassword(driver);
		
	}
	
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	@Parameters({ "browser" })
//	public void login(String browser, Method method) {
//		
//	}
	
	public void logInWithValidData(WebDriver driver) 
	{
		String url = "https://" + boxillaManager + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true);
		driver = new FirefoxDriver(caps);
		driver.manage().window().maximize();
		driver.get(url);
		log.info("A valid User name send to Username field ");
		Loginpage.username(driver).sendKeys(userName);	
		log.info("A valid password  send to password  field ");
		Loginpage.password(driver).sendKeys(password);
		Loginpage.loginbtn(driver).click();
		driver.close();
//		
		
	}
	public void logInWithInvalidPassword(WebDriver driver) 
	{
		String url = "https://" + boxillaManager + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true);
		driver = new FirefoxDriver(caps);
		driver.manage().window().maximize();
		driver.get(url);
		log.info("Enter valid user name ");
		Loginpage.username(driver).sendKeys(userName);
		log.info("Enter invalid password");
		Loginpage.password(driver).sendKeys("1234");
		Loginpage.loginbtn(driver).click();
		Assert.assertTrue(Loginpage.errorMsg(driver).getText().contains("Error:"), "The Log in page did not contain an error message.");
		driver.close();
		
	}
	public void logInWithInvalidUsername(WebDriver driver) 
	{
		String url = "https://" + boxillaManager + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true);
		driver = new FirefoxDriver(caps);
		driver.manage().window().maximize();
		driver.get(url);
		log.info("An invalid username entered into the username field");
		Loginpage.username(driver).sendKeys("admin1");	
		log.info("An invalid password entered into the password  field");
		Loginpage.password(driver).sendKeys(password);
		Loginpage.loginbtn(driver).click();
		Assert.assertTrue(Loginpage.errorMsg(driver).getText().contains("Error:"), "The Log in page did not contain an error message.");
		driver.close();
	}
	public void logInWithInvalidUsernamePassword(WebDriver driver) 
	{
		String url = "https://" + boxillaManager + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true);
		driver = new FirefoxDriver(caps);
		driver.manage().window().maximize();
		driver.get(url);
		log.info("An invalid user name and passwored entered into the username and password field");
		Loginpage.username(driver).sendKeys("admin1");	
		Loginpage.password(driver).sendKeys("1234");
		Loginpage.loginbtn(driver).click();
		Assert.assertTrue(Loginpage.errorMsg(driver).getText().contains("Error:"), "The Log in page did not contain an error message.");
		driver.close();
	}
	public void emptyUsernamePassword(WebDriver driver) 
	{
		String url = "https://" + boxillaManager + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true);
		driver = new FirefoxDriver(caps);
		driver.manage().window().maximize();
		driver.get(url);
		Loginpage.username(driver).sendKeys("");	
		Loginpage.password(driver).sendKeys("");
		Loginpage.loginbtn(driver).click();
		Assert.assertTrue(Loginpage.errorMsg(driver).getText().contains("Error:"), "The Log in page did not contain an error message.");
		driver.close();
		
	}
}
