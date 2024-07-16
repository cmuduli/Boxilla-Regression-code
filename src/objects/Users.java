package objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;
/**
 * Holds all the object models for Boxilla - Users page
 * @author Boxilla
 *
 */
public class Users {
	
	public enum USER_PRIVILEGE {
		ADMIN, GENERAL, POWER
	}
	
	private static String zoneSelecter = "//select[@id='zone']";
	private static String usernameFieldAllUsers = "//td[@class='sorting_1']";
	private static String manageConnectionModelSearchBox = "//div[@id='connection-manage-form']//div[@class='box1 col-md-6']//input[@placeholder='Filter']";
	private static String newUserReviewRemoteAccess = "//p[@id='review-remote-access']";
	private static String newUserReviewAutoConnectName = "//p[@id='review-auto-connect-name']";
	private static String newUserReviewAutoConnect = "//p[@id='review-auto-connect']";
	private static String newUserReviewPrivilege = "//p[@id='review-privilege']";
	private static String newUserReviewName = "//p[@id='review-username']";
	private static String newUserSaveBtn = "//button[@class='btn btn-primary wizard-pf-save']";
	private static String newUserAutoConnectNameTB = "//input[@id='auto-connect-name']";
	private static String newUserAutoConnectBtn = "//input[@id='auto-connect']";
	private static String newUserPrivilegeGeneralBtn = "//input[@id='radio-normal']";
	private static String newUserPrivilegePowerBtn = "//input[@id='radio-power']";
	private static String newUserPrivilegeAdminBtn = "//input[@id='radio-admin']";
	private static String newUserPasswordTB = "//input[@id='password']";
	private static String newUserNextBtn = "//button[@class='btn btn-primary wizard-pf-next']";
	private static String enableActiveDirUserBtn = "//label[@id='ldap-enabled-label']";
	private static String newUserUserNameTB = "//input[@id='username']";
	private static String newUserConfirmPasswordTB = "//input[@id='confirm-password']";
	private static String addUserModel = "//div[@class='wizard-pf-main']";
	private static String userNameActiveUsersTable = "//div[@id='DataTables_Table_0_wrapper']//tr//td[1]";
	private static String currentConnectionActiveUsersTable = "//div[@id='DataTables_Table_0_wrapper']//tr//td[5]";
	private static String userBasedOnTemplate = "//table[@id='table-user']//tr//td[2]";
	private static String userAutoConnectNameFromTable = "//table[@id='table-user']//tr//td[5]";
	private static String remoteAcccessFromUserTable = "//table[@id='table-user']//tr//td[8]";
	private static String userAutoConnectFromTable = "//table[@id='table-user']//tr//td[4]";
	private static String userPrivilegeFromTable = "//table[@id='table-user']//tr//td[3]";
	private static WebElement element = null;
	private static String activeDirectoryDisableBtn = "//label[@id='ldap-disabled-label']";
	private static String editTemplateAutoConnectName = "//input[@id='property-auto-connect-name']";
	private static String editTemplateTemplateName = "//input[@id='property-name']";
	private static String chooseTemplateDropdown = "//select[@id='select-existing-template']";
	private static String editTemplateSaveBtn = "//button[@id='btn-save']";
	private static String editTemplate = "//span[contains(text(),'Edit Template')]" ;
	public static String toastXpath = "//div[@class='toast-message']";
	public static String passwordErrorMessageXpathId = "confirm-password-help-block";
	public static String usernameErrorMessageXpathId = "username-help-block";
	public static String favouritesPopup = "//h4[contains(.,'User Favorites')]";
	public static String favouritesSucessToast = "//div[@class='toast-title']";
	public static String favouritesPopupSave = "//button[@id='fav-btn-save']";
	public static String favouritesCloseBtn = "//div[@id='favorites-modal']//button[@class='btn btn-default'][contains(text(),'Close')]";
	public static String remoteAccess = "//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-off bootstrap-switch-mini bootstrap-switch-id-remote-access bootstrap-switch-animate']//span[@class='bootstrap-switch-label']";
	public static String remoteAccessStatus = "//table[@id='table-user']";
	public static String templateRemoteAccessBtn = "//input[@id='property-remote-access']";
	public static String editTemplateRemoteAccessBtn = "//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-off bootstrap-switch-mini bootstrap-switch-id-property-remote-access bootstrap-switch-animate']//span[@class='bootstrap-switch-handle-off bootstrap-switch-default'][contains(text(),'OFF')]";
	public static String toggleActiveDirectoryNo = "//label[@id='ldap-disabled-label']";
	public static String adStatusConnected = "//i[@class='fa fa-check']";
	public static String adStatusNotConnected = "//i[@class='fa fa-times']";
	private static String editTemplateUserPrivilegeAdmin = "//div[@id='property-form']//div[@class='col-sm-8']//label[1]";
	private static String editTemplateUserPrivilegeGeneral = "//div[@id='property-form']//label[2]";
	private static String editTemplateUserPrivilegePower = "//div[@id='property-form']//label[3]";
	private static String activeUsersHeading = "//h1[@class='bb-table-title']";
	private static String manageConnectionsMoveConnectionBtn = "//div[@id='connection-manage-form']//button[@class='btn move btn-default']";
	private static String manageConnectionsSaveBtn = "//button[@id='btn-connections-save']";
	private static String getFavourite="//input[@id='hotkey-1-dropdown']";
	
	
	public static String getFavouriteDropdown(int number) {
		return "//select[@id='hotkey-" + number + "-dropdown']";
	}
	public static WebElement getfavouritehotkey(WebDriver driver,int number) 
	{
//		return driver.findElement(By.xpath(getFavourite));
		return driver.findElement(By.xpath("//input[@id='hotkey-" + number +"-dropdown']"));
	}
	public static String getZoneSelecter() {
		return zoneSelecter;
	}
	public static String getRemoteAcccessFromUserTable() {
		return remoteAcccessFromUserTable;
	}
	public static String getUsernameFieldAllUsers() {
		return usernameFieldAllUsers;
	}
	public static String getManageConnectionsSaveBtn() {
		return manageConnectionsSaveBtn;
	}
	public static String getManageConnectionsMoveConnectionBtn() {
		return manageConnectionsMoveConnectionBtn;
	}
	public static String getConnectionInList( ) {
		return "//select[@id='bootstrap-duallistbox-nonselected-list_']";
	}
	public static String getManageConnectionModelSearchBox() {
		return manageConnectionModelSearchBox;
	}
	public static String getNewUserReviewRemoteAccess() {
		return newUserReviewRemoteAccess;
	}
	public static String getNewUserReviewAutoConnectName() {
		return newUserReviewAutoConnectName;
	}
	public static String getNewUserReviewAutoConnect() {
		return newUserReviewAutoConnect;
	}
	public static String getNewUserReviewPrivilege() {
		return newUserReviewPrivilege;
	}
	public static String getNewUserReviewName() {
		return newUserReviewName;
	}
	public static String getNewUserSaveBtn() {
		return newUserSaveBtn;
	}
	public static String getNewUserAutoConnectNameTB() {
		return newUserAutoConnectNameTB;
	}
	public static String getNewUserAutoConnectBtn() {
		return newUserAutoConnectBtn;
	}
	public static String getNewUserPrivilegePowerBtn() {
		return newUserPrivilegePowerBtn;
	}
	public static String getNewUserPrivilegeGeneralBtn() {
		return newUserPrivilegeGeneralBtn;
	}
	public static String getNewUserPrivilegeAdminBtn() {
		return newUserPrivilegeAdminBtn;
	}
	public static String getNewUserNextBtn() {
		return newUserNextBtn;
	}
	public static String getNewUserConfirmPasswordTB() {
		return newUserConfirmPasswordTB;
	}
	public static String getNewUserPasswordTB() {
		return newUserPasswordTB;
	}
	public static String getEnableActiveDirUserBtn() {
		return enableActiveDirUserBtn;
	}
	public static String getNewUserUserNameTB() {
		return newUserUserNameTB;
	}
	public static String getAddUserModel() {
		return addUserModel;
	}
	public static String getUserNameActiveUsersTable() {
		return userNameActiveUsersTable;
	}
	public static String getActiveUsersHeading() {
		return activeUsersHeading;
	}
	public static String getCurrentConnectionActiveUsersTable() {
		return currentConnectionActiveUsersTable;
	}
	
	public static String getUserBasedOnTemplate() {
		return userBasedOnTemplate;
	}
	
	public static String getUserAutoConnectNameFromTable() {
		return userAutoConnectNameFromTable;
	}
	public static String getUserAutoConnectFromTable() {
		return userAutoConnectFromTable;
	}
	public static String getUserPrivilegeFromTable() {
		return userPrivilegeFromTable;
	}
	
	public static String getActiveDirectoryDisableBtn() {
		return activeDirectoryDisableBtn;
	}
	public static String getEditTemplateAutoConnectName() {
		return editTemplateAutoConnectName;
	}
	
	public static String getEditTemplateUserPrivilegePower() {
		return editTemplateUserPrivilegePower;
	}
	public static String getEditTemplateUserPrivilegeGeneral( ) {
		return editTemplateUserPrivilegeGeneral;
	}
	public static String getEditTemplateUserPrivilegeAdmin() {
		return editTemplateUserPrivilegeAdmin;
	}
	
	public static String getEditTemplateTemplateName() {
		return editTemplateTemplateName;
	}
	public static String getChooseTemplateDropdown() {
		return chooseTemplateDropdown;
	}
	public static WebElement getToggleActiveDirectory(WebDriver driver) {
		return driver.findElement(By.xpath(toggleActiveDirectoryNo));
	}
	public static String getEditTemplateSaveBtn() {
		return editTemplateSaveBtn;
	}
	public static String getEditTemplate() {
		return editTemplate;
	}
	public static WebElement getTemplateRemoteAccessBtn(WebDriver driver) {
		return driver.findElement(By.xpath(templateRemoteAccessBtn));
	}
	
	public static WebElement connectionFavouriteDropDown(WebDriver driver, int number) {
		return driver.findElement(By.xpath("//select[@id='hotkey-" + number + "-dropdown']"));
	}
	
	public static WebElement userRemoteAccessStatus(WebDriver driver) {
		return driver.findElement(By.xpath(remoteAccessStatus));
		
		
	}
	
	//connection favourites
	public static void selectConnectionFavouriteDropDown(WebDriver driver, int number, String favName) {
		List<WebElement> elements = driver.findElements(By.xpath("//select[@id='hotkey-" + number + "-dropdown']"));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					
						select.selectByVisibleText(favName);
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
//	public static WebElement favouritesPopUp(WebDriver driver) {
//		return driver.findElement(By.xpath("//h4[contains(.,'User Favourites')]"));
//	}
	
	public static WebElement manageFavouritesDropdownLink(WebDriver driver, String username) {
		return driver.findElement(By.xpath("//td[contains(text(),'" + username + "')]//following::a[contains(text(),'Favorites')]"));
	}
	public static WebElement userNameErrorMessage (WebDriver driver) {
		return driver.findElement(By.id(usernameErrorMessageXpathId));
	}
	public static WebElement passwordErrorMessage(WebDriver driver) {
		return driver.findElement(By.id(passwordErrorMessageXpathId));
	}
 	
	public static WebElement errorToast(WebDriver driver) {
		return driver.findElement(By.xpath("//div[@class='toast-title']"));
	}

	public static WebElement addUser(WebDriver driver) { // add user button
		element = driver.findElement(By.id("new-user"));
		return element;
	}

	public static WebElement addTemplate(WebDriver driver) { // add template button
		element = driver.findElement(By.xpath(".//*[@id='new-property']"));
		return element;
	}

	public static WebElement deleteTemplate(WebDriver driver) { // delete template button
		element = driver.findElement(By.xpath(".//*[@id='delete-property']"));
		return element;
	}

	public static WebElement useTemplateNo(WebDriver driver) { // radio button No - Use template
		element = driver.findElement(By.xpath("//input[@id='radio-unique']"));
		return element;
	}

	public static WebElement useTemplateYes(WebDriver driver) { // radio button Yes - Use template
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath("(.//*[@class='btn-group'])[2]/label[2]"));
		} else {
			return element = driver.findElement(By.id("radio-template"));
		}
	}

	public static WebElement username(WebDriver driver) { // Username textbox
		element = driver.findElement(By.xpath(".//*[@id='username']"));
		return element;
	}

	public static WebElement password(WebDriver driver) { // Password textbox
		element = driver.findElement(By.xpath(".//*[@id='password']"));
		return element;
	}

	public static WebElement confirmPassword(WebDriver driver) { // Confirm password textbox
		element = driver.findElement(By.xpath(".//*[@id='confirm-password']"));
		return element;
	}

	public static WebElement btnCancel(WebDriver driver) { // Cancel Button
		element = driver.findElement(By.xpath(".//*[@id='complete']/div/div/div[3]/button[1]"));
		// xpath : //button[@type='button' and contains(., 'Cancel')]
		return element;
	}

	public static WebElement btnBack(WebDriver driver) { // Back Button
		element = driver.findElement(By.xpath(".//*[@id='complete']/div/div/div[3]/button[2]"));
		// xpath: //button[@type='button' and contains(., 'Back')]
		return element;
	}

	public static WebElement btnNext(WebDriver driver) { // Next Button
		element = driver.findElement(By.xpath(".//*[@id='complete']/div/div/div[3]/button[3]"));
		// xpath: //button[@type='button' and contains(., 'Next')]
		return element;
	}

	public static WebElement btnSave(WebDriver driver) { // Save Button
		element = driver.findElement(By.xpath(".//*[@id='complete']/div/div/div[3]/button[4]"));
		return element;
	}

	public static WebElement userPrivilegeAdmin(WebDriver driver) { // Admin user privilege button
		element = driver.findElement(By.xpath(".//*[@id='privilege-input']/div/label[1]"));
		return element;
	}

	public static WebElement userPrivilegeGeneral(WebDriver driver) { // General user privilege button
		element = driver.findElement(By.xpath(".//*[@id='privilege-input']/div/label[2]"));
		return element;
	}

	public static WebElement userPrivilegePower(WebDriver driver) { // Power user privilege button
		element = driver.findElement(By.xpath(".//*[@id='privilege-input']/div/label[3]"));
		return element;
	}

	public static WebElement autoConnectSwitch(WebDriver driver) { // Auto Connect Switch - Add User Process
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='auto-connect-input']/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='auto-connect']"));
		}
	}

	public static WebElement autoConnectName(WebDriver driver) { // Auto Connect Name - Add User Process
		element = driver.findElement(By.xpath(".//*[@id='auto-connect-name']"));
		return element;
	}

	public static WebElement templateName(WebDriver driver) { // Template Name
		element = driver.findElement(By.xpath(".//*[@id='property-name']"));
		return element;
	}

	public static WebElement templatePrivilegeAdmin(WebDriver driver) { // Template User Privilege - Administrator
		element = driver.findElement(By.xpath(".//*[@id='input-p-type']/div/div/label[1]"));
		// element =
		// driver.findElement(By.xpath("//input[@id='radio-property-admin']"));
		return element;
	}

	public static WebElement templatePrivilegeGeneral(WebDriver driver) { // Template User Privilege - General User
		element = driver.findElement(By.xpath(".//*[@id='input-p-type']/div/div/label[2]"));
		return element;
	}

	public static WebElement templatePrivilegePower(WebDriver driver) { // Template User Privilege - Power User
		element = driver.findElement(By.xpath(".//*[@id='input-p-type']/div/div/label[3]"));
		return element;
	}

	public static WebElement templateAutoConnectSwitch(WebDriver driver) { // Auto Connect Switch - Add User Template
		if (driver instanceof ChromeDriver) {
			return element = driver
					.findElement(By.xpath(".//*[@id='property-form']/div/div/div[2]/form/div[3]/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-auto-connect']"));
		}
	}

	public static WebElement templateAutoConnectName(WebDriver driver) { // Auto Connect Name - Add User Template
		element = driver.findElement(By.xpath(".//*[@id='property-auto-connect-name']"));
		return element;
	}

	public static WebElement btnSaveTemplate(WebDriver driver) { // Save Template Button
		element = driver.findElement(By.xpath(".//*[@id='btn-save']"));
		return element;
	}

	public static WebElement notificationContainer(WebDriver driver) { // Notification container - Green flash
																		// notification on right corner
		element = driver.findElement(By.xpath("//div[@id='toast-container']"));
		return element;
	}

	public static WebElement notificationMessage(WebDriver driver) { // Notification message - Toast Message
		element = driver.findElement(By.xpath(toastXpath));
		
		return element;
	}

	public static WebElement nonSelectedList(WebDriver driver) { // Delete Template -> Delete User Template ->
																	// Non-Selected List box
		element = driver.findElement(By.xpath(".//*[@id='bootstrap-duallistbox-nonselected-list_']"));
		return element;
	}

	/* ******************* Elements for Moving Selected Items - User Template List*********************  */
	public static WebElement moveSelectedTemplate(WebDriver driver) { // Move Selected Items form the list
		element = driver.findElement(By.xpath("(//button[@title='Move selected'])[1]"));
		return element;
	}

	public static WebElement moveAllTemplate(WebDriver driver) { // Move All Items form the list
		element = driver.findElement(By.xpath("(//button[@title='Move all'])[1]"));
		return element;
	}

	public static WebElement removeSelectedTemplate(WebDriver driver) { // Remove Selected Items form the list
		element = driver.findElement(By.xpath("(//button[@title='Remove selected'])[1]"));
		return element;
	}

	public static WebElement removeAllTemplate(WebDriver driver) { // Remove All Items form the list
		element = driver.findElement(By.xpath("(//button[@title='Remove all'])[1]"));
		return element;
	}
	/* ********************************************* List End *****************************************  */

	public static WebElement btnDeleteTemplate(WebDriver driver) { // User Template Delete Button
		element = driver.findElement(By.xpath(".//*[@id='btn-delete-user-properties']"));
		return element;
	}

	public static WebElement userBreadCrumb(WebDriver driver, String name) { // User Breadcrum button in Users Table
		//element = driver
		//		.findElement(By.xpath("//tr/td[normalize-space(text())=\"" + name + "\"]/..//*[@id='dropdownKebab']"));
		element=driver.findElement(By.xpath(".//tr/td/div[@class=\"dropdown dropdown-kebab-pf\"]/button[@id=\"dropdownKebab\"]"));
		return element;
	}

	public static WebElement userEditTab(WebDriver driver, String name) {
		element = driver
				.findElement(By.xpath("//tr/td[normalize-space(text())=\"" + name + "\"]/..//*[@class='user-edit']"));
		return element;
	}

	public static WebElement userManageConnectionTab(WebDriver driver, String name) {
		element = driver.findElement(
				By.xpath("//tr/td[normalize-space(text())=\"" + name.trim() + "\"]/..//*[@class='user-manage-connections']"));
		return element;
	}

	public static WebElement userManageGroupTab(WebDriver driver, String name) {
		element = driver.findElement(
				By.xpath("//tr/td[normalize-space(text())=\"" + name + "\"]/..//*[@class='user-manage-group']"));
		return element;
	}

	public static WebElement userDeleteTab(WebDriver driver, String name) {
		element = driver
				.findElement(By.xpath("//tr/td[normalize-space(text())=\"" + name + "\"]/..//*[@class='user-delete']"));
		return element;
	}

	// Manage Connections - non selected connection list
	public static WebElement nonSelectedActiveConnectionList(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//*[@id='bootstrap-duallistbox-nonselected-list_'])[2]"));
		return element;
	}

	// Manage Connections - selected connection list
	public static WebElement selectedActiveConnectionList(WebDriver driver) {
		element = driver.findElement(By.xpath("(.//*[@id='bootstrap-duallistbox-selected-list_'])[2]"));
		return element;
	}

	/* ******************* Elements for Moving Selected Items - Manage Connection List*********************  */
	public static WebElement moveSelectedConenction(WebDriver driver) { // Move Selected Connection form the list
		element = driver.findElement(By.xpath("(//button[@title='Move selected'])[2]"));
		return element;
	}

	public static WebElement moveAllConnection(WebDriver driver) { // Move All Connection form the list
		element = driver.findElement(By.xpath("(//button[@title='Move all'])[2]"));
		return element;
	}

	public static WebElement removeSelectedConnection(WebDriver driver) { // Remove Selected Connection form the list
		element = driver.findElement(By.xpath("(//button[@title='Remove selected'])[2]"));
		return element;
	}

	public static WebElement removeAllConnection(WebDriver driver) { // Remove All Connection form the list
		element = driver.findElement(By.xpath("(//button[@title='Remove all'])[2]"));
		return element;
	}
	/* ********************************************* List End *****************************************  */

	public static WebElement btnSaveConnection(WebDriver driver) { // Save Connection - Adding Connection to User
		element = driver.findElement(By.xpath("//button[@id='btn-connections-save']"));
		return element;
	}

	public static String templates() {
		return ".//*[@id='property-templates']";
	}
	
	public static WebElement templates(WebDriver driver) { // Templates Dropdown - Create User with Template
		element = driver.findElement(By.xpath(templates()));
		return element;
	}

	public static WebElement usersTable(WebDriver driver) { // Users Table
		element = driver.findElement(By.xpath(".//*[@id='table-user']"));
		return element;
	}

	public static WebElement activeUseTemplate(WebDriver driver) { // Use template Yes / No (Returns element which is
																	// active
		// element = driver.findElement(By.xpath(".//*[@class='btn btn-primary
		// active']/input[@id='radio-unique']"));
		element = driver.findElement(By.xpath("(.//*[@class='btn btn-primary active'])[2]/input"));
		return element;
	}

	public static WebElement userForm(WebDriver driver) { // User form
		element = driver.findElement(By.xpath(".//*[@id='complete']"));
		return element;
	}

	public static WebElement userTemplateForm(WebDriver driver) { // User Template form
		element = driver.findElement(By.xpath(".//*[@id='property-form']"));
		return element;
	}

	public static WebElement searchbox(WebDriver driver) { // Filter search box
		return element = driver.findElement(By.xpath("//input[@type='search']"));
	}

	// Available users count when more than one users available
	public static WebElement totalAvailableUser(WebDriver driver) {
		return element = driver.findElement(By.xpath(".//*[@id='table-user_info']/b[3]"));
	}

	public static WebElement groupFilterBox(WebDriver driver) { // Group filter box
		return element = driver.findElement(By.xpath("(.//*[@class='filter form-control'])[3]"));
	}

	public static WebElement btnSaveGroup(WebDriver driver) { // Save user group button
		return element = driver.findElement(By.xpath(".//*[@id='btn-save-user-group-connections']"));
	}

	public static WebElement nonSelectedGroupList(WebDriver driver) { // Non Selected Groups
		return element = driver.findElement(
				By.xpath(".//*[@id='manage-user-group']//*[@id='bootstrap-duallistbox-nonselected-list_']"));
	}
}
