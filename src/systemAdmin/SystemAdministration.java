package systemAdmin;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.SystemMethods;
import objects.Landingpage;
import objects.SystemAll;
import objects.Users;
import system.SystemSettings;
import testNG.Utilities;

public class SystemAdministration extends StartupTestCase2 {

	public String timeStamp, customBackup, boxillaManager;
	private SystemMethods methods = new SystemMethods();

	final static Logger log = Logger.getLogger(SystemAdministration.class);

	public SystemAdministration() {
		super();
		customBackup = prop.getProperty("customBackup");
		timeStamp = prop.getProperty("timeStamp");
	}

	// get yesterday's date using Calendar
	private Date yesterday() {
		final Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -1);
		return cal.getTime();
	}

	// @Test(retryAnalyzer = com.testNG.RetryAnalyzer.class)
	// @Parameters({ "release" })
	// public void testcase01(String release) throws InterruptedException { //
	// Upgarde version of boxilla
	// log.info("Test Case-01 Started - Uprade Boxilla version");
	// log.info("Upgrading to version : " + release);
	// methods.systemUpgrade(driver, release, boxillaManager);
	// log.info("Assertion check completed Version Successfully Upgraded - Test
	// Case-01 Completed");
	// }

	// @Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_databaseRestoreReset() throws InterruptedException { // Database restore and reset
		log.info("Test Case-02 - Database Restore");
		// create date format and yesterday date using format
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd 02");
		String yesterday = dateFormat.format(yesterday());
		System.out.println(yesterday);
		// calling method to to test restore db feature
		// checks user and connection with name "backupcheck"
		// methods.dbReset(driver);
//		 methods.dbRestore(driver, yesterday, customBackup, timeStamp);
		log.info("Database reset successfully - Test Case-02 Completed");
	}

	@Test(priority=1)
	public void test01_checkEverydaybackup() throws InterruptedException {
		log.info("Test Case-01 - Check every day backup ");
		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		String yesterday = dateFormat.format(yesterday());
		log.info(yesterday);
		methods.checkBackup(driver, yesterday);
		String time=SystemAll.backupTime(driver).getText();
		String shortTime = time.substring(0, time.length() - 6);
		Assert.assertTrue(shortTime.contains(yesterday),
				"Yestrday back_up is not avilable");


	}

	@Test(priority=2)
	public void test02_deletebackup() throws InterruptedException {
		log.info("Test Case-02 - Delete backup");
		methods.navigateToSystemAdmin(driver);
		methods.timer(driver);
		SystemAll.backupRestoreTab(driver).click();
		log.info("Backup and Restore Tab Clicked");
		methods.uploadCustomBackup(driver, customBackup, timeStamp);
		Thread.sleep(5000);
		log.info("Delete the uploade BackUp");
		deletecustomebackup(driver, timeStamp);

	}

	@Test(priority=3)
	public void test03_addBoxillaUser() throws InterruptedException 
	{
		log.info("Test case-03-Add Boxilla User");
		methods.addBoxillaUser(driver, "boxillauser1");
	}
	@Test(priority=4)
	public void test04_editBoxillaUser() throws InterruptedException 
	{
		log.info("Test case-4-edit Boxilla User");
		String olduser="boxillauser1";
		edituser(driver, olduser, "boxillanewuser");
		log.info("Deleting the user");
		methods.deleteBoxillaUser(driver, "boxillanewuser");
		
	}
	@Test(priority=5)
	public void test05_checkuserNamepasswordSame() throws InterruptedException 
	{  
		log.info("Test Case-5-Check Error message when UserName and Passowrd is same");
		checkusernamepasswordsame(driver, "boxillauser");
	}
//	@Test(priority=6)
	public void test_6_expertlogfile() throws InterruptedException 
	{   log.info("Test Case-6-Expert the Boxilla log file ");
		expertLogFile(driver);
	}
	
	@Test(priority=7)
	public void test07_ResetCerts() throws InterruptedException 
	{
		log.info("Test case-7-Reset Certs to Default");
		resetCerts(driver);
	}
//	@Test(priority=8)
	public void test08_checkPasswordSequance() throws InterruptedException 
	{
		log.info("Test case-8-Check the password sequences by gving input as 12345");
		checkpasswordsequence(driver, "boxillauser1");
		log.info("Password does not accept the character sequence.");
	}
	@Test(priority=9)
	public void test09_systemInfo() throws InterruptedException 
	{
		log.info("Test case-9-check system info");
		checksysteminfo(driver);
		
	}
	
	public void checksysteminfo(WebDriver driver) throws InterruptedException
	{
		
		
		
		methods.navigateToSystemAdmin(driver);
		methods.timer(driver);
		SystemAll.systemInfo(driver).click();
		log.info("System Info Tab clicked");
		methods.timer(driver);
		String systemInfoTable = SystemAll.systemInfoTable(driver).getText();
		log.info(systemInfoTable);
		String version=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("1"));
		Assert.assertTrue(systemInfoTable.contains(version), "System info Table dose not contain version");
		String SerialNo=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("2"));
		Assert.assertTrue(systemInfoTable.contains(SerialNo), "System info Table dose not contain SerialNo");
		String BuildNo=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("3"));
		Assert.assertTrue(systemInfoTable.contains(BuildNo), "System info Table dose not contain BuildNo");
		String ModelNo=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("4"));
		Assert.assertTrue(systemInfoTable.contains(ModelNo), "System info Table dose not contain ModelNo");
		String NetworkStatus=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("5"));
		Assert.assertTrue(systemInfoTable.contains(NetworkStatus), "System info Table dose not contain NetworkStatus");
		String Uptime=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("6"));
		Assert.assertTrue(systemInfoTable.contains(Uptime), "System info Table dose not contain Uptime");
		String ExportlogsBtn=SeleniumActions.seleniumGetInnerText(driver, SystemAll.getSysteminfoTableColumn("7"));
		Assert.assertTrue(systemInfoTable.contains(ExportlogsBtn), "System info Table dose not contain ExportlogsBtn");
		log.info("System info Table contains all the Fields ");

		
	}
	public void resetCerts(WebDriver driver) throws InterruptedException 
	{
		methods.navigateToSystemAdmin(driver);
		methods.timer(driver);
		SystemAll.certificates(driver).click();
		methods.timer(driver);
		SystemAll.resetcert(driver).click();
		int i=0;
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			Thread.sleep(2000);
			log.info(i + " Upgrading in Progress....");
			i++;
		} 
		String successmsg=SystemAll.getActiveDirectoryToast(driver).getText();
		Assert.assertTrue(successmsg.contains("Successfully reset certificates to default."), "Did not contain the success message, Actual: "+ successmsg );
		
	}
	
	public void expertLogFile(WebDriver driver) throws InterruptedException 
	{
		methods.navigateToSystemAdmin(driver);
		SystemAll.systemInfo(driver).click();
		driver.findElement(By.xpath(".//*[@id='export-logs-button']")).click();
		int i=0;
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			Thread.sleep(20000);
			log.info(i + " Upgrading in Progress....");
			i++;
		} 
		String successmsg=driver.findElement(By.xpath("//div[@class='toast-message']")).getText();
		log.info(successmsg);
		driver.findElement(By.xpath(""));
		
		
		
	}
	
	public void checkpasswordsequence(WebDriver driver,String userName) throws InterruptedException 
	{
		methods.navigateToSystemAdmin(driver);
		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users Tab");
		methods.timer(driver);
		SystemAll.newUser(driver).click();
		log.info("Boxilla user add: Clicked on New User Button");
		methods.timer(driver);
		SystemAll.boxillaUserName(driver).sendKeys(userName);
		log.info("Boxilla user add: Username enetered");
		methods.timer(driver);
		SystemAll.boxillaFirstName(driver).sendKeys("firstname");
		log.info("Boxilla user add: Firstname entered");
		methods.timer(driver);
		SystemAll.boxillaSurname(driver).sendKeys("surname");
		log.info("Boxilla user add: Surname entered");
		methods.timer(driver);
		SystemAll.boxillaEmailAdd(driver).sendKeys("test@blackbox.com");
		log.info("Boxilla user add: Email address added");
		methods.timer(driver);
		Select select = new Select(SystemAll.boxillaAuthorizedBy(driver));
		select.selectByValue("1");
		log.info("Boxilla user add: Clicked on Authorized by drop down and selected INTERNAL");
		if (SystemAll.boxillaPassword(driver).isDisplayed()) {
			SystemAll.boxillaPassword(driver).sendKeys("123456");
			log.info("Boxilla user add: Password entered");
            Thread.sleep(5000);
            String errormessage=SystemAll.checksequencesErrormessage(driver).getText();
         Assert.assertTrue(errormessage.contains("Your password contains sequences")," Error is not displayed,Actual:"+ errormessage);
		} else {
			Assert.fail("***** Password textbox disabled *****");
		}
		
	}
	
	
	
	
	public void checkusernamepasswordsame(WebDriver driver, String userName) throws InterruptedException
	{
		methods.navigateToSystemAdmin(driver);

		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users Tab");
		methods.timer(driver);
		SystemAll.newUser(driver).click();
		log.info("Boxilla user add: Clicked on New User Button");
		methods.timer(driver);
		SystemAll.boxillaUserName(driver).sendKeys(userName);
		log.info("Boxilla user add: Username enetered");
		methods.timer(driver);
		SystemAll.boxillaFirstName(driver).sendKeys("firstname");
		log.info("Boxilla user add: Firstname entered");
		methods.timer(driver);
		SystemAll.boxillaSurname(driver).sendKeys("surname");
		log.info("Boxilla user add: Surname entered");
		methods.timer(driver);
		SystemAll.boxillaEmailAdd(driver).sendKeys("test@blackbox.com");
		log.info("Boxilla user add: Email address added");
		methods.timer(driver);
		Select select = new Select(SystemAll.boxillaAuthorizedBy(driver));
		select.selectByValue("1");
		log.info("Boxilla user add: Clicked on Authorized by drop down and selected INTERNAL");
		if (SystemAll.boxillaPassword(driver).isDisplayed()) {
			SystemAll.boxillaPassword(driver).sendKeys(userName);
			log.info("Boxilla user add: Password entered");
            Thread.sleep(5000);
            String errormessage=SystemAll.checkErrormessage(driver).getText();
         Assert.assertTrue(errormessage.contains("Your password cannot contain your username")," Error is not displayed");
		} else {
			Assert.fail("***** Password textbox disabled *****");
		}
		
	}
	
	
	public void edituser(WebDriver driver,String oldUser,String newUser) throws InterruptedException 
	{
		methods.navigateToSystemAdmin(driver);

		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users Tab");
		SystemAll.searchboxBoxillaUsers(driver).sendKeys(oldUser);
		SystemAll.boxillaUserBreadCrumb(driver, oldUser).click();
		SystemAll.editbtnBoxillaUser(driver).click();
		SystemAll.boxillaUserName(driver).clear();
		SystemAll.boxillaUserName(driver).sendKeys(newUser);
		log.info("Boxilla user add: Username enetered");
		SystemAll.btnSubmit(driver).click();
		Thread.sleep(5000);
		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users tab");
		SystemAll.searchboxBoxillaUsers(driver).sendKeys(newUser);
		String boxillaUsersTable = SystemAll.boxillaUsersTable(driver).getText();
		Assert.assertTrue(boxillaUsersTable.contains(newUser),
				"Boxilla users table did not contain: " + newUser + ", actual text: " + boxillaUsersTable);
		log.info("Boxilla user edited: editedUser is present in user table.. Assertion Completed");
		
	}
	
	public void deletecustomebackup(WebDriver driver,String timeStamp) throws InterruptedException 
	{
		methods.timer(driver);
		SystemAll.searchboxBackupTable(driver).clear();
		methods.timer(driver);
		log.info("Uploading custom-made backup");
		SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
		methods.timer(driver);
		SystemAll.breadCrumb(driver).click(); // bread crumb clicked
		log.info("Breadcrumb clicked");
		methods.timer(driver);
		SystemAll.deleteBtn(driver).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(15000);
		SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
		String backuptable=SystemAll.backupTime(driver).getText();
		Assert.assertFalse(backuptable.contains(timeStamp), "table did not contain: "+ timeStamp +" , actual text :"+ timeStamp );
		log.info("Backup Deleted Sucessfully");
	}

}
