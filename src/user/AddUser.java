package user;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.Users;
import testNG.Utilities;

public class AddUser extends StartupTestCase{

	private UsersMethods methods = new UsersMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	final static Logger log = Logger.getLogger(AddUser.class);
	String autoConName = "addUserAutoCon";

	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		RestAssured.useRelaxedHTTPSValidation(); 
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		try {
			log.info("Deleting all users");
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully deleted all users.")).extract().response();
			
			SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);
			cleanUpLogin();
			log.info("Creating connection for auto connect tests");
			conMethods.createMasterConnection(autoConName, "tx", "private", "no", "no", "no", "no", "no", txIp, driver);
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}

	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addUserAdminAutoConnectOff() throws InterruptedException { // Add User - Administrator Privilege (no template - auto
		// connect OFF)
		log.info(
				"Test Case-43 Started - Adding user with Administrator Privilege (no template - auto connect OFF)");
		methods.addUser(driver, "test1");

		Thread.sleep(2000);
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");

		methods.addUserNoTemplateAutoConnectOFF(driver, "test1");
		log.info("Admin User Added (no template - auto connect OFF) - Test Case-43 Completed");
	}

	@Test
	public void test02_addUserGeneralAutoConnectOff() throws InterruptedException { // Add User - General User Privilege (no template - auto
		// connect OFF)
		log.info(
				"Test Case-44 Started - Adding user with General User Privilege (no template - auto connect OFF)");
		methods.addUser(driver, "test2");

		Thread.sleep(2000);
		Users.userPrivilegeGeneral(driver).click();
		log.info("User Privilege - General User selected");

		methods.addUserNoTemplateAutoConnectOFF(driver, "test2");
		log.info("General User Added (no template - auto connect OFF) - Test Case-44 Completed");
	}

	@Test
	public void test03_addUserPowerAutoConnectOff() throws InterruptedException { // Add User - Power User Privilege (no template - auto
		// connect OFF)
		log.info(
				"Test Case-45 Started - Adding user with Power User Privilege (no template - auto connect OFF)");
		methods.addUser(driver, "test3");

		Thread.sleep(2000);
		Users.userPrivilegePower(driver).click();
		log.info("User Privilege - Power User selected");

		methods.addUserNoTemplateAutoConnectOFF(driver, "test3");
		log.info("Power User Added (no template - auto connect OFF) - Test Case-45 Completed");
	}

	@Test
	public void test04_addUserAdminAutoConnectOn() throws InterruptedException { // Add User - Administrator Privilege (no template - auto
		// connect ON)
		log.info(
				"Test Case-46 Started - Adding user with Administrator Privilege (no template - auto connect ON)");
		methods.addUser(driver, "test4");

		Thread.sleep(2000);
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");

		methods.addUserNoTemplateAutoConnectON(driver, autoConName);
		log.info("Admin User Added (no template - auto connect ON) - Test Case-46 Completed");
	}

	@Test
	public void test05_addUserGeneralAutoConnectOn() throws InterruptedException { // Add User - General User Privilege (no template - auto
		// connect ON)
		log.info(
				"Test Case-47 Started - Adding user with Administrator Privilege (no template - auto connect ON)");
		methods.addUser(driver, "test5");

		Thread.sleep(2000);
		Users.userPrivilegeGeneral(driver).click();
		log.info("User Privilege - General User selected");

		methods.addUserNoTemplateAutoConnectON(driver, autoConName);
		log.info("General User Added (no template - auto connect ON) - Test Case-47 Completed");
	}

	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test06_addUserPowerAutoConnectOn() throws InterruptedException { // Add User - Power User Privilege (no template - auto
		// connect ON)
		log.info(
				"Test Case-48 Started - Adding user with Power User Privilege (no template - auto connect ON)");
		methods.addUser(driver, "test6");

		Thread.sleep(2000);
		Users.userPrivilegePower(driver).click();
		log.info("User Privilege - Power User selected");

		methods.addUserNoTemplateAutoConnectON(driver, autoConName);
		log.info("Power User Added (no template - auto connect ON) - Test Case-48 Completed");
	}

	@Test
	public void test07_addUserWithTemplate() throws InterruptedException { // Add User Using Template
		log.info("Test Case-49 Started - Adding User Using Template");
		//first add template
		addTemplate();
		methods.addUserWithTemplate(driver, "test7");

		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Users.templates(driver).click();
		// Getting list of available templates to use
		Select templatesList = new Select(Users.templates(driver));
		if (templatesList.getOptions().size() < 1) {
			throw new SkipException("***** No Templates Present *****");
		} else {
			templatesList.selectByIndex(1); // selecting template from the dropdown list
			Thread.sleep(2000);
			Users.templates(driver).click();
			log.info("Template Selected from Dopdown menu");
			Thread.sleep(2000);
			Users.btnNext(driver).click();
			log.info("Add user using Template - Stage 2 completed");
			Thread.sleep(2000);
			Users.btnSave(driver).click(); // Save user
			log.info("User Added Using Template - Test Case-49 Completed");
		}
	}
//	//NEGATIVE TESTS	
	@Test
	public void test08_addNonUniqueUser() throws InterruptedException {
		log.info("***** test01_addNonUniqueUser *****");
		methods.addUser(driver, "admin", "admin", "admin");
		Users.btnNext(driver).click();
		Users.btnSave(driver).click();
		Thread.sleep(5000);
		String message = Users.notificationMessage(driver).getText();
		System.out.println(message);
		
		Assert.assertTrue(message.contains("Error creating unique user:"), "The error message didnt match, "
				+ "Expected: Error creating unique user., Actual: " + message);
	}

	@Test
	public void test09_missingPassword() throws InterruptedException {
		log.info("***** test02_missingPassword *****");
		methods.addUser(driver, "admin", "", "admin");
		Users.btnNext(driver).click();
		String message = Users.notificationMessage(driver).getText();
		Assert.assertTrue(message.contains("Some fields are missing input"), "The error message didnt match, "
				+ "Expected: Some fields are missing input, Actual: " + message);
	}
	
	@Test
	public void test10_missingPasswordErrorCheck() throws InterruptedException {
		log.info("***** test03_missingPasswordErrorCheck *****");
		methods.addUser(driver, "admin", "", "admin");
		Users.btnNext(driver).click();
		Assert.assertTrue(Users.passwordErrorMessage(driver).isDisplayed(), "Error is not displayed");
	}
	
	@Test
	public void test11_missingUsername() throws InterruptedException {
		log.info("***** test04_missingUsername *****");
		methods.addUser(driver, "");
		Users.btnNext(driver).click();
		String message = Users.notificationMessage(driver).getText();
		Assert.assertTrue(message.contains("Some fields are missing input"), "The error message didnt match, "
				+ "Expected: Some fields are missing input, Actual: " + message);
	}
	
	@Test
	public void test12_missingUsernameErrorCheck() throws InterruptedException {
		log.info("***** test05_missingUsernameErrorCheck *****");
		methods.addUser(driver, "");
		Users.btnNext(driver).click();
		Assert.assertTrue(Users.userNameErrorMessage(driver).isDisplayed(), "Error is not displayed");
	}
	
	@Test
	public void test13_missingConfirmPassword() throws InterruptedException {
		log.info("***** test06_missingConfirmPassword *****");
		methods.addUser(driver, "admin", "admin", "");
		Users.btnNext(driver).click();
		String message = Users.notificationMessage(driver).getText();
		Assert.assertTrue(message.contains("Some fields are missing input"), "The error message didnt match, "
				+ "Expected: Some fields are missing input, Actual: " + message);
	}
	
	@Test
	public void test14_missingConfirmPasswordErrorCheck() throws InterruptedException {
		log.info("***** test07_missingConfirmPasswordErrorCheck *****");
		methods.addUser(driver, "admin", "admin", "");
		Users.btnNext(driver).click();
		Assert.assertTrue(Users.passwordErrorMessage(driver).isDisplayed(), "Error is not displayed");
	}
	@Test
	public void test15_createGeneralRemoteUser() throws InterruptedException {
		methods.addRemoteAppUser(driver, "remote1", "general");
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "remote1");
		Assert.assertTrue(isRemoteUser, "User was not a remote user");
	}
	
	@Test
	public void test16_createPowerRemoteUser() throws InterruptedException {
		methods.addRemoteAppUser(driver, "remote2", "power");
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "remote2");
		Assert.assertTrue(isRemoteUser, "User was not a remote user");
	}
	
	@Test
	public void test17_createAdminRemoteUser() throws InterruptedException {
		methods.addRemoteAppUser(driver, "remote3", "admin");
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "remote3");
		Assert.assertTrue(isRemoteUser, "User was not a remote user");
	}
	
	public void addTemplate() throws InterruptedException {
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Users.templatePrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator Selected");

		methods.addTemplateAutoConnectOFF(driver, "testcase49"); // calling method to enter Field details to add
	}
	
//@AfterClass
public void afterclass() 
{
	log.info("Deleting all users");
	Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
	.when().contentType(ContentType.JSON)
	.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
	.then().assertThat().statusCode(200)
	.body("message", equalTo("Successfully deleted all users.")).extract().response();
	
	SaveResponseStatistics("https://" + boxillaManager  + "/bxa-api/users/kvm/all", REQUEST_TYPE.DELETE, response);	
}


}