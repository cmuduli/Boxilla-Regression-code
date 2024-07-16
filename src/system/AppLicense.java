package system;

import org.testng.Assert;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.SystemMethods;
import objects.SystemAll;

public class AppLicense extends StartupTestCase{
	
	private SystemMethods system = new SystemMethods();
	private String licenseLocation = "C:\\Test_Workstation\\SeleniumAutomation\\resources\\appLicense\\" 
	+ boxillaManager + "\\"; 
	
	
	@Test
	public void test01_uploadAppLicenseValue1() {
		system.uploadAppLicense(driver, licenseLocation + "AppLicense1.alic");
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 1, "License value was not 1, actual:" + number);
	}
	
	@Test
	public void test02_uploadAppLicenseValue5() {
		system.uploadAppLicense(driver, licenseLocation + "AppLicense5.alic");
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 6, "License value was not 6, actual:" + number);
	}
	
	@Test
	public void test03_uploadAppLicenseValue10() {
		system.uploadAppLicense(driver, licenseLocation + "AppLicense10.alic");
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 16, "License value was not 16, actual:" + number);
	}
	
	@Test
	public void test04_uploadAppLicenseValue20() {
		system.uploadAppLicense(driver, licenseLocation + "AppLicense20.alic");
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 36, "License value was not 36, actual:" + number);
	}
	
	@Test
	public void test05_deleteAllLicenses() {
		system.navigateToAppLicense(driver);
		system.deleteAllAppLicenses(driver);
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 0, "License value was not 0, actual:" + number);
	}
	@Test
	public void test06_uploadDemoLicense() {
		system.uploadAppLicense(driver, licenseLocation + "appLicenseDemo.alic");
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 4, "License value was not 4, actual:" + number);
	}
	
	@Test
	public void test07_deleteDemoLicense() {
		system.navigateToAppLicense(driver);
		system.deleteAllAppLicenses(driver);
		int number = system.getNumberOfAppLicenses(driver);
		Assert.assertTrue(number == 0, "License value was not 0, actual:" + number);
	}
	

}
