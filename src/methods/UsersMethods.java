package methods;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import extra.SeleniumActions;
import extra.StartupTestCase;
import objects.Landingpage;
import objects.Users;
import objects.Users.USER_PRIVILEGE;
/**
 * Class contains all the methods to interact with boxilla users pages
 * @author Boxilla
 *
 */
public class UsersMethods {

	final static Logger log = Logger.getLogger(UsersMethods.class);

	/**
	 * returns a string array that contains a users details from the users table
	 * array index:
	 * 0 - username
	 * 1 - privilage
	 * 2 - autoconnect
	 * 3 - autoconnect name
	 * 4 - remote access
	 * @param driver
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	public String[] getUserDetails(WebDriver driver, String name) throws InterruptedException {
		String[] values = new String[5];
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(name);
		values[0] = SeleniumActions.seleniumGetText(driver, Users.getUsernameFieldAllUsers());
		log.info("username:" + values[0]+ ".");
		values[1] = SeleniumActions.seleniumGetText(driver, Users.getUserPrivilegeFromTable());
		log.info("privilege:" + values[1]+ ".");
		values[2] = SeleniumActions.seleniumGetText(driver, Users.getUserAutoConnectFromTable());
		log.info("autoconnect:" + values[2]+ ".");
		values[3] = SeleniumActions.seleniumGetText(driver, Users.getUserAutoConnectNameFromTable());
		log.info("autoconnect name:" + values[3]+ ".");
		values[4] = SeleniumActions.seleniumGetText(driver, Users.getRemoteAcccessFromUserTable());
		log.info("remote access:" + values[4] + ".");
		return values;
	}
	/**
	 * do not use hard coded waits. please use implicit waits instead
	 * @param driver
	 * @throws InterruptedException
	 */
	@Deprecated
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(30, TimeUnit.SECONDS);
	}

	/**
	 * Returns the current users username from the active users table
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getActiveUsersCurrentUser(WebDriver driver) throws InterruptedException {
		navigateToActiveUsers(driver);
		String currentConnection = SeleniumActions.seleniumGetText(driver, Users.getUserNameActiveUsersTable());
		log.info("Current user from active users table:" + currentConnection);
		return currentConnection;	
	}

	/**
	 * returns the active users connection name
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getActiveUsersCurrentConnection(WebDriver driver) throws InterruptedException {
		navigateToActiveUsers(driver);
		String currentConnection = SeleniumActions.seleniumGetText(driver, Users.getCurrentConnectionActiveUsersTable());
		log.info("Current active connection from active users table:" + currentConnection);
		return currentConnection;	
	}

	/**
	 * navigates to the active users table from any where in boxilla
	 * @param driver
	 */
	public void navigateToActiveUsers(WebDriver driver) {
		Landingpage.usersTab(driver).click();
		Landingpage.usersActiveTab(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users.getActiveUsersHeading())));
	}

	/**
	 * returns true if the user has auto connection enabled else false
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isAutoConnectEnabledUser(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		String output = SeleniumActions.seleniumGetText(driver, Users.getUserAutoConnectFromTable());
		log.info("AutoConnect:" + output);
		if(output.contains("No")) {
			return false;
		}else {
			return true;
		}
	}

	/**
	 * get a users based on template name from users table
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public String getBasedOnTemplateName(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		String output = SeleniumActions.seleniumGetText(driver, Users.getUserBasedOnTemplate());
		log.info("Based on Template:" + output);
		return output;
	}

	/**
	 * returns a users auto connect name from the users table
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public String getUserAutoConnectName(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		String output = SeleniumActions.seleniumGetText(driver, Users.getUserAutoConnectNameFromTable());
		log.info("AutoConnect name:" + output);
		return output;
	}

	/**
	 * returns a users privilege from the users table. privilege is an enum with the values
	 *  - POWER, ADMIN, GENERAL
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public USER_PRIVILEGE getUserPrivilege(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		Thread.sleep(4000);
		Users.searchbox(driver).sendKeys(username);
		String privilege = SeleniumActions.seleniumGetText(driver, Users.getUserPrivilegeFromTable());
		log.info("PRIVILAGE:" + privilege);

		if(privilege.equals("PowerUser")) {
			return USER_PRIVILEGE.POWER;
		}else if(privilege.equals("Administrator")) {
			return USER_PRIVILEGE.ADMIN;
		}else if(privilege.equals("User")) {
			return USER_PRIVILEGE.GENERAL;
		}

		return null;
	}

	/**
	 * creates a user from a template
	 * @param driver
	 * @param username
	 * @param templateName
	 * @throws InterruptedException
	 */
	public void addUserWithTemplateName(WebDriver driver, String username, String templateName) throws InterruptedException {
		addUserWithTemplate(driver, username);
		Users.templates(driver).click();
		SeleniumActions.seleniumDropdown(driver, Users.templates(), templateName);
		Users.btnNext(driver).click();
		log.info("Add user using Template - Stage 2 completed");
		Thread.sleep(2000);
		Users.btnSave(driver).click(); // Save user
	}
	/**
	 * navigates to edit user template from anywhere in boxilla
	 * @param driver
	 * @param templateName
	 * @throws InterruptedException
	 */
	public void navigateToEditUserTemplate(WebDriver driver, String templateName) throws InterruptedException {
		navigateToUsersManage(driver);
		SeleniumActions.seleniumClick(driver, Users.getEditTemplate());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Users.getEditTemplateSaveBtn())));
		SeleniumActions.seleniumDropdown(driver, Users.getChooseTemplateDropdown(), templateName);	
	}

	/**
	 * edit an existing user templates name
	 * @param driver
	 * @param templateName
	 * @param newTemplateName
	 * @throws InterruptedException
	 */
	public void editUserTemplateName(WebDriver driver, String templateName, String newTemplateName) throws InterruptedException {
		navigateToEditUserTemplate(driver, templateName);
		SeleniumActions.seleniumSendKeysClear(driver, Users.getEditTemplateTemplateName());
		SeleniumActions.seleniumSendKeys(driver, Users.getEditTemplateTemplateName(), newTemplateName);
		SeleniumActions.seleniumClick(driver, Users.getEditTemplateSaveBtn());
		timer(driver); 
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Users.addUser(driver)));

	}

	/**
	 * edits a template by turning remote access on in the template
	 * @param driver
	 * @param templateName
	 * @throws InterruptedException
	 */
	public void editUserTemplateRemoteAccessOn(WebDriver driver, String templateName) throws InterruptedException {
		navigateToEditUserTemplate(driver, templateName);

		SeleniumActions.seleniumClick(driver, Users.editTemplateRemoteAccessBtn);
		SeleniumActions.seleniumClick(driver, Users.getEditTemplateSaveBtn());
		timer(driver);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Users.addUser(driver)));
	}

	/**
	 * edits a user template bu turning auto connection on
	 * @param driver
	 * @param templateName
	 * @param autoConnectName
	 * @throws InterruptedException
	 */
	public void editUserTemplateAutoConnectOn(WebDriver driver, String templateName, String autoConnectName) throws InterruptedException {
		navigateToEditUserTemplate(driver, templateName);
		SeleniumActions.exectuteJavaScriptClick(driver, Users.templateAutoConnectSwitch(driver));
		SeleniumActions.seleniumSendKeys(driver, Users.getEditTemplateAutoConnectName(), autoConnectName);
		SeleniumActions.seleniumClick(driver, Users.getEditTemplateSaveBtn());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Users.addUser(driver)));
	}

	/**
	 * edit a user tempate by changing the user privilege
	 * @param driver
	 * @param templateName
	 * @param privilege
	 * @throws InterruptedException
	 */
	public void editUserTemplatePrivilege(WebDriver driver, String templateName, USER_PRIVILEGE privilege) throws InterruptedException {
		navigateToEditUserTemplate(driver, templateName);
		switch(privilege) {
		case ADMIN:
			SeleniumActions.seleniumClick(driver, Users.getEditTemplateUserPrivilegeAdmin());
			break;
		case GENERAL: 
			SeleniumActions.seleniumClick(driver, Users.getEditTemplateUserPrivilegeGeneral());
			break;
		case POWER:
			SeleniumActions.seleniumClick(driver, Users.getEditTemplateUserPrivilegePower());
			break;
		}
		SeleniumActions.seleniumClick(driver, Users.getEditTemplateSaveBtn());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Users.addUser(driver)));

	}

	/**
	 * returns true if the user is a remote app user else false. 
	 * @param driver
	 * @param userName
	 * @return
	 * @throws InterruptedException
	 */
	public boolean isUserRemoteApp(WebDriver driver, String userName) throws InterruptedException {
		Landingpage.usersTab(driver).click();
		timer(driver);
		Landingpage.usersManageTab(driver).click();
		timer(driver);
		Users.searchbox(driver).sendKeys(userName);
		log.info("Username entered in search box");
		timer(driver);
		String status =Users.userRemoteAccessStatus(driver).getText();
		log.info("STATUS: " + status);
		if(status.contains(userName)) {
			String[] statusSplit = status.split("\\s+");
			String remoteAccessStatus = statusSplit[statusSplit.length -2];
			log.info("REAL STATUS: " + remoteAccessStatus);
			if(remoteAccessStatus.equals("Yes")) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Adds connections to the user 
	 * @param driver
	 * @param username - name of user to add connections to 
	 * @param connection - the name of the connection
	 * @throws InterruptedException
	 */
	public void addConnectionToUser(WebDriver driver, String username, String... connection) throws InterruptedException {
		manageUser(driver, username);
		Users.userManageConnectionTab(driver, username).click();
		for(String s : connection) {
			SeleniumActions.seleniumSendKeysClear(driver, Users.getManageConnectionModelSearchBox());
			SeleniumActions.seleniumDropdown(driver, Users.getConnectionInList(), s);
		}
		SeleniumActions.seleniumClick(driver, Users.getManageConnectionsMoveConnectionBtn());
		SeleniumActions.seleniumClick(driver, Users.getManageConnectionsSaveBtn());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users.toastXpath)));
		String message = SeleniumActions.seleniumGetText(driver, Users.toastXpath);
		Assert.assertTrue(message.contains("Successfully edited user connections"), "Could not set user connections, toast message was " + message);
	}

	/**
	 * Add every connection in Boxilla to a used
	 * @param driver
	 * @param username
	 * @throws InterruptedException
	 */
	public void addAllConnectionsToUser(WebDriver driver, String username) throws InterruptedException {
		manageUser(driver, username);
		Users.userManageConnectionTab(driver, username).click();
		Users.moveAllConnection(driver).click();
		saveConnection(driver);
	}

	/**
	 * Returns the name of the connection assigned to the passed in favourite slot
	 * @param driver
	 * @param username - name of the user to check
	 * @param connectionFavNumber - the number of the slot to get the connection from
	 * @return String - name of the connection in the slot 
	 * @throws InterruptedException
	 */
	public String getCurrentConnectionFavourite(WebDriver driver, String username, int connectionFavNumber) throws InterruptedException {
		if(connectionFavNumber == 0) {
			connectionFavNumber = 10;
		}
		log.info("Getting name of connection in favourite " + connectionFavNumber + " for user " + username);
		navigateToManageFavourites(driver, username);
		log.info("On favourites pop up");
		timer(driver);
		log.info("Getting text from select:" + Users.connectionFavouriteDropDown(driver, connectionFavNumber));
		Select select  = new Select (Users.connectionFavouriteDropDown(driver, connectionFavNumber));
		WebElement option = select.getFirstSelectedOption();
		String text = option.getText();
		log.info("Favourite name: " + text);
		SeleniumActions.seleniumClick(driver, Users.favouritesCloseBtn);
		return text;	
	}

	/**
	 * Returns an array of all favourite connection for a give user
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public String[] getAllCurrentConnectionFavourites(WebDriver driver, String username) throws InterruptedException {
		String[] favs = new String[10];
		log.info("Getting names of all connection favourites for user " + username);
		navigateToManageFavourites(driver, username);
		log.info("On favourites pop up");
		timer(driver);
		int counter = 0;
		for(int j=0; j > -1; j--) {
			if(j==0) {
				counter = 10;
			}else {
				counter = j;
			}
			Select select  = new Select (Users.connectionFavouriteDropDown(driver, counter));
			WebElement option = select.getFirstSelectedOption();

			favs[j] = option.getText();
			log.info("Favourite name: " + favs[j]);
		}		
		SeleniumActions.seleniumClick(driver, Users.favouritesCloseBtn);
		return favs;

	}

	/**
	 * Returns the name of the connection that is assigned to a given user, in a given favourite slot and belonging 
	 * to a give zone
	 * @param driver
	 * @param username
	 * @param connectionFavNumber
	 * @param zoneName
	 * @return
	 * @throws InterruptedException
	 */
	public String getCurrentConnectionFavourite(WebDriver driver, String username,int  connectionFavNumber, String zoneName) throws InterruptedException {
		log.info("Getting name of connection in favourite " +  connectionFavNumber + " for user " + username);
		navigateToManageFavourites(driver, username);
		SeleniumActions.seleniumDropdown(driver, Users.getZoneSelecter(), zoneName);
//		String text=Users.getfavouritehotkey(driver).getAttribute("value");
		Select select  = new Select (Users.connectionFavouriteDropDown(driver, connectionFavNumber));
		WebElement option = select.getFirstSelectedOption();
		String text = option.getText();
		log.info("Favourite name: " + text);
		SeleniumActions.seleniumClick(driver, Users.favouritesCloseBtn);
		return text;	
	}
	public String getCurrentConnectionFavourite1(WebDriver driver, String username,int favconnName, String zoneName) throws InterruptedException {
		log.info("Getting name of connection in favourite " + favconnName + " for user " + username);
		navigateToManageFavourites(driver, username);
		SeleniumActions.seleniumDropdown(driver, Users.getZoneSelecter(), zoneName);
		String text=Users.getfavouritehotkey(driver,favconnName).getAttribute("value");
//		Select select  = new Select (Users.connectionFavouriteDropDown(driver, connectionFavNumber));
//		WebElement option = select.getFirstSelectedOption();
//		String text = option.getText();
		log.info("Favourite name: " + text);
		SeleniumActions.seleniumClick(driver, Users.favouritesCloseBtn);
		return text;
	}
	/**
	 * Takes in an array of connections that will be assigned to a given 
	 * user as connection favourites based on their index in the array
	 * @param driver
	 * @param username
	 * @param listOfFavourites
	 * @throws InterruptedException
	 */
	public void selectAllUserFavourites(WebDriver driver, String username, String[] listOfFavourites) throws InterruptedException {
		log.info("Setting all 10 connection favourites for user " + username);
		int counter = 0;
		navigateToManageFavourites(driver, username);
		for(String s : listOfFavourites) {
			Users.selectConnectionFavouriteDropDown(driver, counter, s);
			counter++;
		}
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		timer(driver);
		String message = SeleniumActions.seleniumGetText(driver, Users.toastXpath);
		System.out.println(message);
		Assert.assertTrue(message.equals("Successfully Saved Favorites"), "Could not save favourites, toast message was " + message);
	}

	public void selectUserFavourite(WebDriver driver, String username, int favNumber, String favConnection, String zoneName) throws InterruptedException {
		log.info("Setting favourite " + favNumber + " for user " + username + " to " + favConnection);
		navigateToManageFavourites(driver, username);
		SeleniumActions.seleniumDropdown(driver, Users.getZoneSelecter(), zoneName);
		Users.getfavouritehotkey(driver,favNumber).sendKeys(favConnection);
//		SeleniumActions.seleniumDropdown(driver, Users.getFavouriteDropdown(favNumber), favConnection);
		//	Users.selectConnectionFavouriteDropDown(driver, favNumber, favConnection);
		log.info("Selected favourite connection, attempting to save..");
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		//		timer(driver);
		//		String message = SeleniumActions.seleniumGetText(driver, Users.toastXpath);
		//		System.out.println(message);
		//		Assert.assertTrue(message.equals("Successfully Saved Favorites"), "Could not save favourites, toast message was " + message);
	}

	/**
	 * Set a connection to a user favourite slot
	 * @param driver
	 * @param username
	 * @param favNumber
	 * @param favConnection
	 * @throws InterruptedException
	 */
	public void selectUserFavourite(WebDriver driver, String username, int favNumber, String favConnection) throws InterruptedException {

		if(favNumber == 0) {			//favourites used to go from 0 - 9 but this was changed to 1 - 10
			favNumber = 10;
		}
		log.info("Setting favourite " + favNumber + " for user " + username + " to " + favConnection);
		navigateToManageFavourites(driver, username);
		timer(driver);
		Users.selectConnectionFavouriteDropDown(driver, favNumber, favConnection);
		timer(driver);
		log.info("Selected favourite connection, attempting to save..");
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users.toastXpath)));
		String message = SeleniumActions.seleniumGetText(driver, Users.toastXpath);
		System.out.println(message);
		Assert.assertTrue(message.equals("Favorites saved."), "Could not save favourites, toast message was " + message);
	}
	/**
	 * Sets a connection to a user favourite slot. Does not run an asseration on the toast messaage. returns instead
	 * @param driver
	 * @param username
	 * @param favNumber
	 * @param favConnection
	 * @return
	 * @throws InterruptedException
	 */
	public String selectUserFavouriteNoAssert(WebDriver driver, String username, int favNumber, String favConnection) throws InterruptedException {
		log.info("Setting favourite " + favNumber + " for user " + username + " to " + favConnection);
		navigateToManageFavourites(driver, username);
		Users.selectConnectionFavouriteDropDown(driver, favNumber, favConnection);
		log.info("Selected favourite connection, attempting to save..");
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		timer(driver);
		String message = SeleniumActions.seleniumGetText(driver, Users.toastXpath);
		return message;
	}

	/**
	 * Navigate to user favourites with no asseration
	 * @param driver
	 * @param username
	 * @throws InterruptedException
	 */
	public void navigateToManageFavouritesNoCheck(WebDriver driver, String username) throws InterruptedException {
		log.info("Navigating to manage favourite connections for user " + username);
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		timer(driver);
		Users.userBreadCrumb(driver, username).click();
		timer(driver);
		Users.manageFavouritesDropdownLink(driver, username).click();
	}
	/**
	 * navigate to manage user favourites
	 * @param driver
	 * @param username
	 * @throws InterruptedException
	 */
	public void navigateToManageFavourites(WebDriver driver, String username) throws InterruptedException {
		log.info("Navigating to manage favourite connections for user " + username);
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		timer(driver);
		Users.userBreadCrumb(driver, username).click();
		timer(driver);
		Users.manageFavouritesDropdownLink(driver, username).click();
		Thread.sleep(3000);
//		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(driver.findElement(By.xpath(Users.favouritesPopup))));
		log.info("Successfully navigated to favourite connections for user " + username);
	}

	/**
	 * Deletes a user using boxilla
	 * @param user
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deleteUser(String user, WebDriver driver) throws InterruptedException {
		log.info("deleting User: " + user);
		navigateToUsersManage(driver);
		Thread.sleep(3000);
		Users.searchbox(driver).clear();
		Users.searchbox(driver).sendKeys(user);
		Users.userBreadCrumb(driver, user).click();
		timer(driver);
		new WebDriverWait(driver,20).until(ExpectedConditions.elementToBeClickable(Users.userDeleteTab(driver, user))).click();
//		Users.userDeleteTab(driver, user).click();
		timer(driver);
		Alert alert = driver.switchTo().alert();
		alert.accept();

		new WebDriverWait(driver, 20).until(ExpectedConditions.presenceOfElementLocated(By.xpath("//div[@class='toast-message']")));
		log.info("User deleted successfully");
	}

	/**
	 * Add a user to boxilla who is authorized to use the remote app
	 * @param driver
	 * @param name
	 * @param privilege admin, power or general
	 * @param password
	 * @throws InterruptedException
	 */
	public void addRemoteAppUser(WebDriver driver, String name, String privilege, String...password) throws InterruptedException {
		addUser(driver, name, password);
		String userPrivilege = "";
		Thread.sleep(2000);
		if(privilege.equals("admin")) {
			userPrivilege = "Administrator";
			Users.userPrivilegeAdmin(driver).click();
		}else if(privilege.equals("power")) {
			userPrivilege = "PowerUser";
			Users.userPrivilegePower(driver).click();
		}else if(privilege.equals("general")) {
			userPrivilege = "User";
			Users.userPrivilegeGeneral(driver).click();
		}
		log.info("User Privilege - Administrator selected");
		SeleniumActions.seleniumClick(driver, Users.remoteAccess);
		Thread.sleep(2000);
		Users.btnNext(driver).click();
		log.info("Add user - Stage 2 completed");
		Thread.sleep(2000);
		Users.btnSave(driver).click();
		log.info("User " + name + " Added");
		timer(driver);
		driver.navigate().refresh(); // refreshing page to make sure the element is loaded
		log.info("Page refreshed");
		timer(driver);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in search box");
		timer(driver);
		String usersTable = Users.usersTable(driver).getText();
		Assert.assertTrue(usersTable.contains(name), 
				"User table does not contain: " + name + ", actual text: " + usersTable);
		Assert.assertTrue(usersTable.contains(userPrivilege), 
				"User table does not contain: " + userPrivilege + ", actual text: " + usersTable);
	}
	/**
	 * Creates a new user through boxilla
	 * @param driver
	 * @param name
	 * @param password
	 * @throws InterruptedException
	 */
	public void addUser(WebDriver driver, String name, String...password) throws InterruptedException { // Add User Initiate - Click on Add
		// User Btn
		navigateToUsersManage(driver);
		timer(driver);
		Users.addUser(driver).click();
		log.info("Add User Button Clicked");
		timer(driver);
		Assert.assertTrue(Users.userForm(driver).isDisplayed(), "***** Add User Form is not displayed *****");
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", Users.useTemplateNo(driver));
		log.info("Use Template - No selected");

		//ONLY FOR 3.2 ACTIVE DIRECTORY
		SeleniumActions.seleniumClick(driver, Users.toggleActiveDirectoryNo);
		timer(driver);
		Users.username(driver).sendKeys(name);
		log.info("New username entered");

		String defaultPassword1 = "admin";
		String defaultPassword2 = "admin";

		if(password.length > 0) {
			defaultPassword1 = password[0];
			defaultPassword2 = password[1];
		}

		timer(driver);
		Users.password(driver).sendKeys(defaultPassword1);
		log.info("New Password entered");

		timer(driver);
		Users.confirmPassword(driver).sendKeys(defaultPassword2);
		log.info("Password re-entered");

		timer(driver);
		Users.btnNext(driver).click();
		log.info("Add user - Stage 1 completed");
	}

	/**
	 * Adds an active directory user to Boxilla
	 * @param driver
	 * @param username
	 * @throws InterruptedException
	 */
	public void addUserAD(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		timer(driver);
		Users.addUser(driver).click();
		log.info("Add User Button Clicked");
		timer(driver);
		Assert.assertTrue(Users.userForm(driver).isDisplayed(), "***** Add User Form is not displayed *****");
		Thread.sleep(2000);
		((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;", Users.useTemplateNo(driver));
		log.info("Use Template - No selected");
		timer(driver);
		Users.username(driver).sendKeys(username);
		log.info("New username entered");
		Users.btnNext(driver).click();
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");
		addUserNoTemplateAutoConnectOFF(driver, username);
	}
	
	

	/**
	 * Checks and returns an active directory users AD status. Return values are 
	 * ADtrue, ADfalse or ADnone
	 * @param driver
	 * @param username
	 * @return
	 * @throws InterruptedException
	 */
	public String getActiveUserStatus(WebDriver driver, String username) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys(username);
		log.info("Username entered in search box");
		timer(driver);
		String usersTable = Users.usersTable(driver).getText();
		log.info("User INFO:" + usersTable);
		boolean isConnected;
		boolean isDisconnected;
		try {
			isConnected = SeleniumActions.seleniumIsDisplayed(driver, Users.adStatusConnected);
			System.out.println("isconnected is "+isConnected);
		}catch(Exception e) {
			log.info("User is not connected");
			isConnected = false;
		}

		try {
			isDisconnected = SeleniumActions.seleniumIsDisplayed(driver, Users.adStatusNotConnected);
			System.out.println("isDisconnected is "+isDisconnected);
		}catch(Exception e) {
			log.info("User is not disconnected");
			isDisconnected = false;
		}

		if(isConnected) {
			usersTable = usersTable + " ADtrue";
		}else if(isDisconnected){
			usersTable = usersTable + " ADfalse";
		}else {
			usersTable = usersTable + " ADnone";
		}
		System.out.println(usersTable);
		return usersTable;
	}

	/**
	 * Checks a specific user exists in boxilla
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void checkUserExists(WebDriver driver, String name) throws InterruptedException {
		Landingpage.usersTab(driver).click();
		timer(driver);
		Landingpage.usersManageTab(driver).click();
		timer(driver);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in search box");
		timer(driver);
		String usersTable = Users.usersTable(driver).getText();
		Assert.assertTrue(usersTable.contains(name), 
				"User table does not contain: " + name + ", actual text: " + usersTable);	
	}

	/**
	 * Creates a new user, not using a template with auto connect off
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void addUserNoTemplateAutoConnectOFF(WebDriver driver, String name) throws InterruptedException { // Save
		// User
		Thread.sleep(2000);
		Users.btnNext(driver).click();
		log.info("Add user - Stage 2 completed");

		Thread.sleep(2000);
		Users.btnSave(driver).click();
		log.info("User " + name + " Added");
		timer(driver);
		driver.navigate().refresh(); // refreshing page to make sure the element is loaded
		log.info("Page refreshed");
		timer(driver);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in search box");
		timer(driver);
		String usersTable = Users.usersTable(driver).getText();
		Assert.assertTrue(usersTable.contains(name), 
				"User table does not contain: " + name + ", actual text: " + usersTable);
	}

	/**
	 * Creates a new user, not using a template with auto connect on
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void addUserNoTemplateAutoConnectON(WebDriver driver, String name) throws InterruptedException {
		Thread.sleep(2000);
		SeleniumActions.exectuteJavaScriptClick(driver, Users.autoConnectSwitch(driver));
		log.info("Auto Connect is On");
		Thread.sleep(2000);
		Users.autoConnectName(driver).sendKeys(name);
		log.info("Auto connect name is added");
		Thread.sleep(2000);
		Users.btnNext(driver).click();
		log.info("Add user - Stage 2 completed");
		Thread.sleep(2000);
		Users.btnSave(driver).click();
		log.info("User Added");
		timer(driver);
		driver.navigate().refresh(); // refreshing page to make sure the element is loaded
		log.info("Page refreshed");
		timer(driver);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in search box");
		timer(driver);
		String userTable = Users.usersTable(driver).getText();
		Assert.assertTrue(userTable.contains(name), 
				"User table does not contain: " + name + ", actual text: " + userTable);
	}


	public void manageUser(WebDriver driver, String name) throws InterruptedException {
		navigateToUsersManage(driver);
		timer(driver);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in the search box");
		timer(driver);
		if (Users.usersTable(driver).getText().contains(name)) {
			Users.userBreadCrumb(driver, name).click();
			log.info("User breadcrum clicked");
		} else {
			log.info("Searched user: " + name + " is not present..");
			throw new SkipException("*****Searched user " + name + "is not present****");
		}
	}

	/**
	 * Adds a connection to user
	 * @param driver
	 * @throws InterruptedException
	 */
	public void saveConnection(WebDriver driver) throws InterruptedException { // Add Connection to User - Save
		// Connection
		Thread.sleep(2000);
		Users.btnSaveConnection(driver).click();
		;
		log.info("Save Connection Button Clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Users.notificationMessage(driver)));

		String toastMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(toastMessage.contains("Successfully edited user connections"),
				"Notification message did not contain: Successfully edited user connections, actual text: " + toastMessage);
		log.info("Connection Added Successfully");
	}

	/**
	 * Adds a user template
	 * @param driver
	 * @throws InterruptedException
	 */
	public void addTemplate(WebDriver driver) throws InterruptedException {
		navigateToUsersManage(driver);
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Users.addTemplate(driver).click();
		Thread.sleep(3000);
		log.info("Add User Template Button Clicked");

		timer(driver);
		Assert.assertTrue(Users.userTemplateForm(driver).isDisplayed(),
				"***** User Template Form is not displayed *****");
	}

	/**
	 * Adds a user template with auto connect on
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void addTemplateAutoConnectON(WebDriver driver, String name) throws InterruptedException {
		Thread.sleep(2000);
		Users.templateName(driver).sendKeys(name);
		log.info("Template Name Entered");

		Thread.sleep(2000);
		SeleniumActions.exectuteJavaScriptClick(driver, Users.templateAutoConnectSwitch(driver));
		log.info("Auto Connect is ON");
		Thread.sleep(2000);
		Users.templateAutoConnectName(driver).sendKeys(name);
		log.info("Template Auto Connect Name Entered");
		Thread.sleep(2000);
		Users.btnSaveTemplate(driver).click();
		Thread.sleep(2000);
		Users.notificationContainer(driver).getAttribute("innerHTML")
		.contains("Successfully createddddd user property");
	}

	/**
	 * Adds a user template with autoconnect off
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void addTemplateAutoConnectOFF(WebDriver driver, String name) throws InterruptedException {
		Thread.sleep(4000);
		Users.templateName(driver).sendKeys(name);
		log.info("Template Name Entered");

		Thread.sleep(3000);
		Users.btnSaveTemplate(driver).click();

		Thread.sleep(2000);
		String toastMessage = Users.notificationMessage(driver).getText();
		// Assert Flash Notification for Successful action.. Green notification on the
		// right corner of window
		Assert.assertTrue(toastMessage.contains("Successfully created user property"),
				"Notification Message did not contain Successfully created user property, actual text: " + toastMessage);
		log.info("Template Added Successfully");
	}

	/**
	 * Deletes a user template
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deleteTemplate(WebDriver driver) throws InterruptedException { // Delete User Template - Initiate
		navigateToUsersManage(driver);
		timer(driver);
		Users.deleteTemplate(driver).click();
		log.info("Delete User Templates Tab Clicked");
		timer(driver);
		Users.nonSelectedList(driver).isDisplayed(); // Assertion Check for Template List Box
		log.info("Delete User Template Button Clicked");
	}

	/**
	 * Deletes a specific user template
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deleteSelectedTemplate(WebDriver driver) throws InterruptedException { // Delete Selected User Templates
		Thread.sleep(2000);
		Users.btnDeleteTemplate(driver).click();
		// Assert Flash Notification for Successful action.. Green notification on the
		// right corner of window
		driver.manage().timeouts().implicitlyWait(3, TimeUnit.SECONDS);
		String toastMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(toastMessage.contains("Successfully deleted"),
				"Notification Message did not contain Successfully deleted, actual text: " + toastMessage);
		log.info("Template Deleted Successfully");
	}

	public void addUserWithTemplate(WebDriver driver, String name, String templateName) throws InterruptedException {
		addUserWithTemplate(driver, name);
		// Getting list of available templates to use
		Select templatesList = new Select(Users.templates(driver));
		if (templatesList.getOptions().size() < 1) {
			throw new SkipException("***** No Templates Present *****");
		} else {
			templatesList.selectByVisibleText(templateName);
			Thread.sleep(2000);
			Users.templates(driver).click();
			log.info("Template Selected from Dropdown menu");
			Thread.sleep(2000);
			Users.btnNext(driver).click();
			log.info("Add user using Template - Stage 2 completed");
			Thread.sleep(2000);
			Users.btnSave(driver).click(); // Save user
			log.info("User Added Using Template - Test Case-49 Completed");
		}
	}
	/**
	 * Adds a user using a template
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void addUserWithTemplate(WebDriver driver, String name) throws InterruptedException {
		// Add User Initiate - Click on Add User btn
		navigateToUsersManage(driver);
		Thread.sleep(2000);
		Users.addUser(driver).click();
		log.info("Add User Button Clicked");
		Thread.sleep(5000);
		SeleniumActions.exectuteJavaScriptClick(driver, Users.useTemplateYes(driver));
		log.info("Use Template - Yes selected");
		boolean isEnabled = Users.password(driver).isEnabled();
		System.out.println("IS ENABLED:" + isEnabled);
		log.info("Disabled. Password field disabled. AD must be enabled. Disabling");
		SeleniumActions.seleniumClick(driver, Users.getActiveDirectoryDisableBtn());
		Thread.sleep(2000);
		Users.username(driver).sendKeys(name);
		log.info("New username entered");
		Thread.sleep(2000);
		Users.password(driver).sendKeys(name);
		log.info("New Password entered");
		Thread.sleep(2000);
		Users.confirmPassword(driver).sendKeys(name);
		log.info("Password re-entered");
		Thread.sleep(2000);
		Users.btnNext(driver).click();
		log.info("Add user - Stage 1 completed");
	}

	/**
	 * Edit users username
	 * @param driver
	 * @param oldUsername
	 * @param newUsername
	 * @throws InterruptedException
	 */
	public void userEditName(WebDriver driver, String oldUsername, String newUsername) throws InterruptedException {
		Thread.sleep(2000);
		Users.userEditTab(driver, oldUsername).click();
		log.info("User Edit Tab Clicked");
		Thread.sleep(2000);
		Users.username(driver).clear();
		Thread.sleep(2000);
		Users.username(driver).sendKeys(newUsername);
		Thread.sleep(2000);
		log.info("New Username entered");
		Users.btnNext(driver).click();
		Thread.sleep(2000);
		Users.btnNext(driver).click();
		Thread.sleep(2000);
		Users.btnSave(driver).click();
		log.info("New Username saved");
		Thread.sleep(2000);
		driver.navigate().refresh(); // refreshing page to make sure the element is loaded
		timer(driver);
		Users.searchbox(driver).sendKeys(newUsername);
		log.info("Username entered in search box");
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		String usersTable = Users.usersTable(driver).getText();
		Assert.assertTrue(usersTable.contains(newUsername), 
				"User table did not contain: " + newUsername + ", actual text: " + usersTable);
	}

	public void editUser(WebDriver driver, String userName, String connectionName) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.searchbox(driver).sendKeys("admin");
		Users.userBreadCrumb(driver, "admin").click();
		Users.userEditTab(driver, userName).click();
		Users.btnNext(driver).click();
		Users.autoConnectSwitch(driver).click();
		Users.autoConnectName(driver).sendKeys(connectionName);
		Users.btnNext(driver).click();
		Users.btnSave(driver).click();	
	}

	/**
	 * Navigate to user > manage
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToUsersManage(WebDriver driver) throws InterruptedException { // Navigate to Users manage
		timer(driver);
		Landingpage.usersTab(driver).click();
		log.info("Users Tab clicked");
		timer(driver);
		Landingpage.usersManageTab(driver).click();
		log.info("Users > Manage Tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Users.addUser(driver)));
	}
	/**
	 * This will create any type of user with any option enabled or disabled. Can be used for data driven tests
	 * @param driver
	 * @param name
	 * @param password
	 * @param isTemplate
	 * @param isActiveDir
	 * @param privilege
	 * @param isAutoConnect
	 * @param autoConnectName
	 * @param remoteAccess
	 * @throws InterruptedException
	 */
	public void masterCreateUser(WebDriver driver, String name, String password, String isTemplate, String isActiveDir, 
			String privilege, String isAutoConnect, String autoConnectName, String remoteAccess ) throws InterruptedException {
		navigateToUsersManage(driver);
		Users.addUser(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Users.getAddUserModel())));
		SeleniumActions.seleniumSendKeys(driver, Users.getNewUserUserNameTB(), name);
		if(isActiveDir.equals("true")) {
			SeleniumActions.seleniumClick(driver, Users.getEnableActiveDirUserBtn());
		}else {
			SeleniumActions.seleniumClick(driver, Users.getActiveDirectoryDisableBtn());
			SeleniumActions.seleniumSendKeys(driver, Users.getNewUserPasswordTB(), password);
			SeleniumActions.seleniumSendKeys(driver, Users.getNewUserConfirmPasswordTB(), password);
		}

		SeleniumActions.seleniumClick(driver, Users.getNewUserNextBtn());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Users.getNewUserPrivilegeAdminBtn())));

		if(privilege.equals("admin")) {
			WebElement admin = driver.findElement(By.xpath(Users.getNewUserPrivilegeAdminBtn()));
			SeleniumActions.exectuteJavaScriptClick(driver, admin);
		}else if(privilege.equals("general")) {
			WebElement general = driver.findElement(By.xpath(Users.getNewUserPrivilegeGeneralBtn()));
			SeleniumActions.exectuteJavaScriptClick(driver, general);
		}else if(privilege.equals("power")) {
			WebElement power = driver.findElement(By.xpath(Users.getNewUserPrivilegePowerBtn()));
			SeleniumActions.exectuteJavaScriptClick(driver, power);
		}

		//autoconnect
		if(isAutoConnect.equals("true")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Users.autoConnectSwitch(driver));
			SeleniumActions.seleniumSendKeys(driver, Users.getNewUserAutoConnectNameTB(), autoConnectName);
		}

		//remote access
		if(remoteAccess.equals("true")) {
			SeleniumActions.seleniumClick(driver, Users.remoteAccess);
		}
		SeleniumActions.seleniumClick(driver, Users.getNewUserNextBtn());
		assertUserReview(driver, name, privilege, isAutoConnect, autoConnectName, remoteAccess);



	}

	/**
	 * Checks the user creation review modal for correctness
	 * @param driver
	 * @param name
	 * @param privilege
	 * @param isAutoConnect
	 * @param autoConnectName
	 * @param remoteAccess
	 * @throws InterruptedException 
	 */
	private void assertUserReview(WebDriver driver, String name, String privilege, String isAutoConnect,
			String autoConnectName, String remoteAccess) throws InterruptedException {
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Users.getNewUserSaveBtn())));

		String reviewName = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewName());
		Assert.assertTrue(reviewName.equals(name), "Review name did not equal expected name. Expected:" + name + " , actual:" + reviewName);

		String reviewPrivilege = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewPrivilege());
		if(privilege.equals("admin")) {
			Assert.assertTrue(reviewPrivilege.equals("Administrator"), "Review privilege did not equal Administrator, actual:" + reviewPrivilege);
		}else if(privilege.equals("general")){
			Assert.assertTrue(reviewPrivilege.equals("User"), "Review privilege did not equal User, actual:" + reviewPrivilege);
		}else if(privilege.equals("power")) {
			Assert.assertTrue(reviewPrivilege.equals("PowerUser"), "Review privilege did not equal PowerUser, actual:" + reviewPrivilege);
		}

		if(isAutoConnect.equals("true")) {
			String reviewAutoConnect = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewAutoConnect());
			String reviewAutoConnectName = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewAutoConnectName());
			Assert.assertTrue(reviewAutoConnect.equals("Yes"), " Review Auto connect did not equal Yes. Actual:" + reviewAutoConnect);
			Assert.assertTrue(reviewAutoConnectName.equals(autoConnectName), "Review auto connect name did not equal " + 
					autoConnectName + " , actual:" + reviewAutoConnectName);
		}else {
			String reviewAutoConnect = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewAutoConnect());
			Assert.assertTrue(reviewAutoConnect.equals("No"), " Review Auto connect did not equal No. Actual:" + reviewAutoConnect);
		}

		if(remoteAccess.equals("true")) {
			String reviewRemoteAccess = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewRemoteAccess());
			Assert.assertTrue(reviewRemoteAccess.equals("Yes"), "Review remote access did not equal Yes. Actual:" + reviewRemoteAccess);
		}else {
			String reviewRemoteAccess = SeleniumActions.seleniumGetInnerText(driver, Users.getNewUserReviewRemoteAccess());
			Assert.assertTrue(reviewRemoteAccess.equals("No"), "Review remote access did not equal No. Actual:" + reviewRemoteAccess);
		}

		//save and check table
		saveUser(driver, name);
	}

	/**
	 * Save the creation of a use and asserts user has been created in the users 
	 * manage table
	 * @param driver
	 * @param name
	 * @throws InterruptedException 
	 */
	private void saveUser(WebDriver driver, String name) throws InterruptedException {
		Users.btnSave(driver).click();
		log.info("User " + name + " Added");
		driver.navigate().refresh(); // refreshing page to make sure the element is loaded
		log.info("Page refreshed");
		Thread.sleep(3000);
		Users.searchbox(driver).sendKeys(name);
		log.info("Username entered in search box");
		String usersTable = Users.usersTable(driver).getText();
		Assert.assertTrue(usersTable.contains(name), 
				"User table does not contain: " + name + ", actual text: " + usersTable);
	}

}