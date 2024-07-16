package device;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
/**
 * Class that contains tests for upgrading and downgrading of devices
 * @author Boxilla
 *
 */
public class DeviceUpgrades extends StartupTestCase {
	
	DevicesMethods methods = new DevicesMethods();
	final static Logger log = Logger.getLogger(DeviceUpgrades.class);
	private String deviceNewVersion = prop.getProperty("newVersion");
	private String deviceMiddleVersion = prop.getProperty("middleVersion");
	private String deviceOldVersion = prop.getProperty("oldVersion");
	DevicesMethods method=new DevicesMethods();
	

	
	private void setVersion(String ipAddress) {
		methods.getDeviceTypeForUpgrade(driver, ipAddress);		
	}

	/**
	 * Downgrade TX twice. Uses test.properties new, middle and old version properties
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke", "emerald", "chrome" },retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_SetTimeOut() throws InterruptedException { 
		log.info("****test01_SetTimeOut*****");
		method.setTimeOut(driver,"300");
		
//		methods.navigateToUpgrade(driver);
//		methods.upgradeDevice(driver, "tx", txIp, deviceNewVersion, deviceMiddleVersion);
//		methods.upgradeDevice(driver, "tx", txIp, deviceMiddleVersion, deviceOldVersion);
//		Ssh ssh = new Ssh("root", "barrow1admin_12", txIp);
//		ssh.loginToServer();
//		String version = ssh.sendCommand("cat /VERSION");
//		log.info("Output from cat /VERSION:" + version);
//		ssh.disconnect();
//		Assert.assertTrue(version.contains(deviceOldVersion), "Version on device differes from boxilla, expected:" + 
//				deviceOldVersion + " actual:" + version);

	}
	
//	/**
//	 * Upgrade TX twice. Uses test.properties new, middle and old version properties
//	 * @throws InterruptedException
//	 */
	@Test(groups= {"boxillaFunctional", "smoke", "emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test02_maximumAndMinTimeOut() throws InterruptedException { 
//		log.info("****test02_upgradeDeviceTX****");
		method.timeoutRange(driver, "299", "1801");
		
//		setVersion();
//		methods.navigateToUpgrade(driver);
//
//		methods.upgradeDevice(driver, "tx", txIp, deviceOldVersion, deviceMiddleVersion);
//		methods.upgradeDevice(driver, "tx", txIp, deviceMiddleVersion, deviceNewVersion);
//		Ssh ssh = new Ssh(deviceUserName, devicePassword, txIp);
//		ssh.loginToServer();
//		String version = ssh.sendCommand("cat /VERSION");
//		log.info("Output from cat /VERSION:" + version);
//		ssh.disconnect();
//		Assert.assertTrue(version.contains(deviceNewVersion), "Version on device differes from boxilla, expected:" + 
//				deviceNewVersion + " actual:" + version);
	}

//	
	@Test(groups= {"boxillaFunctional", "smoke", "emerald"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test03_imageUpload() throws InterruptedException {
//		log.info("****test03_downgradeDeviceRX*****");
	
		methods.navigateToUpgrade(driver);
		driver.findElement(By.xpath("//a[@class='bb-title-btn']")).click();
		driver.findElement(By.xpath("//input[@type='file']")).sendKeys("C:\\Users\\pcadmin\\Desktop\\Test\\V6.8.0_r11889.clu");
		Thread.sleep(10000);
	WebDriverWait wait=new WebDriverWait(driver, 60);
	wait.until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath("//div[@class='toast-title']"))));
	
	
	
//		setVersion();
//		methods.navigateToUpgrade(driver);
//		methods.upgradeDevice(driver, "rx", rxIp, deviceNewVersion, deviceMiddleVersion);
//		methods.upgradeDevice(driver, "rx", rxIp, deviceMiddleVersion, deviceOldVersion);
//		log.info("deviceNewVersion:" + deviceNewVersion);
//		log.info("deviceMiddleVersion:" + deviceMiddleVersion);
//		log.info("deviceOldVersion:" + deviceOldVersion);
//
//		Ssh ssh = new Ssh("root", "barrow1admin_12", rxIp);
//		ssh.loginToServer();
//		String version = ssh.sendCommand("cat /VERSION");
//		log.info("Output from cat /VERSION:" + version);
//		ssh.disconnect();
//		Assert.assertTrue(version.contains(deviceOldVersion), "Version on device differes from boxilla, expected:" + 
//				deviceMiddleVersion + " actual:" + version);
//		
//
	}
//	
//	@Test(groups= {"boxillaFunctional", "smoke", "emerald" },retryAnalyzer = testNG.RetryAnalyzer.class)
//	public void test04_upgradeDeviceRX() throws InterruptedException {
//		log.info("****test04_upgradeDeviceRX****");
//		setVersion();
//		methods.navigateToUpgrade(driver);
//
//		methods.upgradeDevice(driver, "rx", rxIp, deviceOldVersion, deviceMiddleVersion);
//		methods.upgradeDevice(driver, "rx", rxIp, deviceMiddleVersion, deviceNewVersion);
//
//		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
//		ssh.loginToServer();
//		String version = ssh.sendCommand("cat /VERSION");
//		log.info("Output from cat /VERSION:" + version);
//		ssh.disconnect();
//		Assert.assertTrue(version.contains(deviceNewVersion), "Version on device differes from boxilla, expected:" + 
//				deviceNewVersion + " actual:" + version);
//	}
	
//	@Test(groups= {"boxillaFunctional", "smoke" },retryAnalyzer = com.testNG.RetryAnalyzer.class)
//	public void test05_upgradeDevicesCheckTimes() throws InterruptedException {
//		log.info("****test04_upgradeDeviceRX****");
//		setVersion();
//		methods.upgradeAll(driver, deviceMiddleVersion);
//	}
	
}

