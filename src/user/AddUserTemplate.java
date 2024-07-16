package user;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.UsersMethods;
import objects.Users;
import objects.Users.USER_PRIVILEGE;
import testNG.Utilities;

public class AddUserTemplate extends StartupTestCase {

	private UsersMethods methods = new UsersMethods();
	final static Logger log = Logger.getLogger(AddUserTemplate.class);
	String templateName = "editTemplateGeneral";
	String username = "editTemplateUser";

	public AddUserTemplate() throws IOException {
		super();
	}
	
	@Test
	public void test01_addUserTemplateAdminAutoConnectOff() throws InterruptedException { // Add User Template - Administrator Privilege (auto
		// connect OFF)
		log.info("Test Case-37 Started - Adding User Template with Administrator Privilege");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Users.templatePrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator Selected");

		methods.addTemplateAutoConnectOFF(driver, "testtemplate1"); // calling method to enter Field details to add
		// Admin User template with auto connect OFF
		log.info("Admin User Template Added (Auto Connect OFF) - Test Case-37 Completed");
	}

	@Test
	public void test02_addUserTemplateGeneralAutoConnectOff() throws InterruptedException { // Add User Template - General User Privilege (auto
		// connect OFF)
		log.info("Test Case-38 Started - Adding User Template with General User Privilege");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		Users.templatePrivilegeGeneral(driver).click();
		log.info("User Privilege - General User Selected");

		methods.addTemplateAutoConnectOFF(driver, "testtemplate2"); // calling method to enter Field details to add
		// User template with auto connect OFF
		log.info("General User Template Added (Auto Connect OFF) - Test Case-38 Completed");
	}

	@Test
	public void test03_addUserTemplatePowerAutoConnectOff() throws InterruptedException { // Add User Template - Power User Privilege (auto connect
		// OFF)
		log.info("Test Case-39 Started - Adding User Template with Power User Privilege");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		Users.templatePrivilegePower(driver).click();
		log.info("User Privilege - Power Selected");

		methods.addTemplateAutoConnectOFF(driver, "testtemplate3"); // calling method to enter Field details to add
		// User template with auto connect OFF
		log.info("Power User Template Added (Auto Connect OFF) - Test Case-39 Completed");
	}

	@Test
	public void test04_addUserTemplateAdminAutoConnectOn() throws InterruptedException { // Add User Template - Administrator Privilege (auto
		// connect ON)
		log.info(
				"Test Case-40 Started - Adding User Template with Administrator Privilege (auto connect ON)");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		Users.templatePrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator Selected");

		methods.addTemplateAutoConnectON(driver, "testtemplate4"); // calling method to enter Field details to add
		// User template with auto connect ON
		log.info("Admin User Template (Auto Connect ON)- Test Case-40 Completed");
	}

	@Test
	public void test05_addUserTemplateGeneralAutoConnectOn() throws InterruptedException { // Add User Template - General User Privilege (auto
		// connect ON)
		log.info(
				"Test Case-41 Started - Adding User Template with General User Privilege (auto connect ON)");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		Users.templatePrivilegeGeneral(driver).click();
		log.info("User Privilege - General User Selected");

		methods.addTemplateAutoConnectON(driver, "testtemplate5"); // calling method to enter Field details to add
		// User template with auto connect ON
		log.info("General User Template Added (Auto Connect ON) - Test Case-41 Completed");
	}

	@Test
	public void test06_addUserTemplatePowerAutoConnectinOn() throws InterruptedException { // Add User Template - Power User Privilege (auto connect
		// ON)
		System.out
		.println("Test Case-42 Started - Adding User Template with Power User Privilege (auto connect ON)");
		methods.addTemplate(driver); // calling method to initiate Template Add Process

		Thread.sleep(2000);
		Users.templatePrivilegePower(driver).click();
		log.info("User Privilege - Power User Selected");

		methods.addTemplateAutoConnectON(driver, "testtemplate6"); // calling method to enter Field details to add
		// User template with auto connect ON
		log.info("Power User Template Added (Auto Connect ON - Test Case-42 Completed");
	}
	
	@Test
	public void test07_addGeneralRemoteUserTemplate() throws InterruptedException {
		methods.addTemplate(driver);
		Users.templatePrivilegeGeneral(driver).click();
		SeleniumActions.exectuteJavaScriptClick(driver, Users.getTemplateRemoteAccessBtn(driver));
		//Users.getTemplateRemoteAccessBtn(driver).click();
		methods.addTemplateAutoConnectOFF(driver, "remoteTemplateGeneral");
		//create user
		methods.addUserWithTemplate(driver, "templateRemote1", "remoteTemplateGeneral");
		Thread.sleep(2000);
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "templateRemote1");
		Assert.assertTrue(isRemoteUser, "User created from template was not remote user");	
	}
	
	@Test
	public void test08_addPowerRemoteUserTemplate() throws InterruptedException {
		methods.addTemplate(driver);
		Users.templatePrivilegePower(driver).click();
		SeleniumActions.exectuteJavaScriptClick(driver, Users.getTemplateRemoteAccessBtn(driver));
		//Users.getTemplateRemoteAccessBtn(driver).click();
		methods.addTemplateAutoConnectOFF(driver, "remoteTemplatePower");
		//create user
		methods.addUserWithTemplate(driver, "templateRemote2", "remoteTemplatePower");
		Thread.sleep(2000);
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "templateRemote2");
		Assert.assertTrue(isRemoteUser, "User created from template was not remote user");	
	}
	
	@Test
	public void test09_addAdminRemoteUserTemplate() throws InterruptedException {
		methods.addTemplate(driver);
		Users.templatePrivilegeAdmin(driver).click();
		SeleniumActions.exectuteJavaScriptClick(driver, Users.getTemplateRemoteAccessBtn(driver));
		//Users.getTemplateRemoteAccessBtn(driver).click();
		methods.addTemplateAutoConnectOFF(driver, "remoteTemplateAdmin");
		//create user
		methods.addUserWithTemplate(driver, "templateRemote3", "remoteTemplateAdmin");
		Thread.sleep(2000);
		boolean isRemoteUser = methods.isUserRemoteApp(driver, "templateRemote3");
		Assert.assertTrue(isRemoteUser, "User created from template was not remote user");	
	}
	
	
	
	@Test
	public void test10_editTemplatePrivilegeGeneral() throws InterruptedException {
		
		//create admin template
		methods.addTemplate(driver); // calling method to initiate Template Add Process
		Thread.sleep(2000);
		Users.templatePrivilegeAdmin(driver).click();
		methods.addTemplateAutoConnectOFF(driver, templateName); 
		
			//add user using template
		methods.addUserWithTemplateName(driver, username, templateName);
		USER_PRIVILEGE privilegeOrg = methods.getUserPrivilege(driver, username);
		Assert.assertTrue(privilegeOrg.equals(USER_PRIVILEGE.ADMIN), "User privilege was not admin: actual, " + privilegeOrg);
		methods.editUserTemplatePrivilege(driver, templateName, USER_PRIVILEGE.GENERAL);
		USER_PRIVILEGE privilege = methods.getUserPrivilege(driver, username);
		Assert.assertTrue(privilege.equals(USER_PRIVILEGE.GENERAL), "user privilege was not general, actual:" + privilege);
	}
	@Test
	public void test11_editTemplatePrivilegePower() throws InterruptedException {
		methods.editUserTemplatePrivilege(driver, templateName, USER_PRIVILEGE.POWER);
		USER_PRIVILEGE privilege = methods.getUserPrivilege(driver, username);
		Assert.assertTrue(privilege.equals(USER_PRIVILEGE.POWER), "user privilege was not POWER, actual:" + privilege);
	}
	@Test
	public void test12_editTemplatePrivilegeAdmin() throws InterruptedException {
		methods.editUserTemplatePrivilege(driver, templateName, USER_PRIVILEGE.ADMIN);
		USER_PRIVILEGE privilege = methods.getUserPrivilege(driver, username);
		Assert.assertTrue(privilege.equals(USER_PRIVILEGE.ADMIN), "user privilege was not admin, actual:" + privilege);
	}
	@Test
	public void test13_editTemplateAutoConnect() throws InterruptedException {
		String autoConnectName = "autoCon";
		methods.editUserTemplateAutoConnectOn(driver, templateName, autoConnectName);
		boolean isAutoConnectEnabled = methods.isAutoConnectEnabledUser(driver, username);
		Assert.assertTrue(isAutoConnectEnabled, "AutoConnect was not enabled");
		String autoName = methods.getUserAutoConnectName(driver, username);
		Assert.assertTrue(autoName.equals(autoConnectName), "auto connect name was not " + autoConnectName + ", actual:" + autoName);
	}
	@Test
	public void test14_editTemplateRemoteUser() throws InterruptedException {
		methods.editUserTemplateRemoteAccessOn(driver, templateName);
		boolean isRemote = methods.isUserRemoteApp(driver, username);
		Assert.assertTrue(isRemote, "User was not remote app user");
	}
	
	@Test
	public void test15_editUserTemplateName() throws InterruptedException {
		String newName = "editNameTest";
		String originalName = methods.getBasedOnTemplateName(driver, username);
		Assert.assertTrue(originalName.equals(templateName), "Original based on template name was not " + templateName + 
				". Actual:" + originalName);
		methods.editUserTemplateName(driver, templateName, newName);
		String originalName2 = methods.getBasedOnTemplateName(driver, username);
		Assert.assertTrue(originalName2.equals(newName), "Original based on template name was not " + newName + 
				". Actual:" + originalName2);
	}
	
	
	



}