package extra;

import java.io.IOException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;

import objects.Landingpage;
import objects.Loginpage;
import testNG.Utilities;

public class temp {

	public WebDriver driver;
	public String boxillaManager;

	public temp() throws IOException {
		boxillaManager = Utilities.getCellData(1, 11);
	}

	@BeforeMethod
	//@Parameters({ "browser" })
	public void login() throws InterruptedException {
		// Select driver based on the Browser parameter selected
		String url = "https://" + boxillaManager + "/";
		//if (browser.equalsIgnoreCase("firefox")) {
			/* *************************** Firefox Driver ********************************* */
			System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content
			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.get(url);
//		} else if (browser.equalsIgnoreCase("chrome")) {
//			/* **************************** Chrome Driver ********************************* */
//			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Webdrivers\\chromedriver.exe");
//			driver = new ChromeDriver();
//			driver.manage().window().maximize();
//			driver.get(url);
//		} else if (browser.equalsIgnoreCase("ie")) {
//			// ******************************* IE Driver
//			// ************************************
//			System.setProperty("webdriver.ie.driver", "C:\\Selenium\\Webdrivers\\IEDriverServer.exe");
//			driver = new InternetExplorerDriver();
//			driver.manage().window().maximize();
//			driver.get(url);
//			driver.navigate().to("javascript:document.getElementById('overridelink').click()");
//		}
		Thread.sleep(2000);
		System.out.println("********* @ Before Method Started ************");
		Loginpage.username(driver).sendKeys("admin");

		Thread.sleep(2000);
		Loginpage.password(driver).sendKeys("admin");

		Thread.sleep(2000);
		Loginpage.loginbtn(driver).click();
		System.out.println("********* @ Before Method Completed ************");
	}

	@AfterMethod
	public void logout(ITestResult result) throws InterruptedException {
		System.out.println("********* @ After Method Started ************");
		// Taking screen shot on failure
		String url = "https://" + boxillaManager + "/";
		Thread.sleep(2000);
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
		try {
			Thread.sleep(1000);
			driver.get(url);
			Thread.sleep(2000);
			Landingpage.logoutDropdown(driver).click();
			Thread.sleep(2000);
			Landingpage.logoutbtn(driver).click();
			Thread.sleep(2000);
			driver.quit();
		} catch (Exception e) {
			// TODO: handle exception
			driver.quit();
		}
		System.out.println("********* @ After Method Completed ************");
	}

	
}
