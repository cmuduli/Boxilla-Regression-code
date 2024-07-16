package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Holds all the object models for boxilla System pages
 * @author Boxilla
 *
 */
public class SystemAll {
	
	
	//system info
	private static String systemInfoTab = "//div[@data-bb-title='System Info']";
	private static String exportButton = "//a[@class='btn btn-primary']";
	private static String currentVersionFromTable = "//div[@id='systeminfo']//tr//td[1]";
	private static String minorVersionFromTable = "//div[@id='systeminfo']//tr//td[3]";
	private static String serialNumberFromTable = "//div[@id='systeminfo']//tr//td[2]";
	//dual nic
	private static String editEth2Dropdown = "//div[@id='network']//tr[2]//td[6]//div[1]//button[1]//img[1]";
	private static String editEth2IpBtn = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Edit Network Port')]";
	private static String rotate = "//img[@class='rotate']";
	private static String eth1DropdownBtn = "//div[@id='network']//img";
	private static String editEth1Btn = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Edit Network Port')]";
	private static String enableEth2Btn = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Enable Ethernet Port 2')]";
	private static String eth2ApplyBtn = "//div[@id='network']//button[@id='btn-enable-eth2-apply']";
	private static String eth1ApplyBtn = "//div[@id='network']//button[@id='btn-edit-eth1-apply']";
	private static String eth1IpAddress = "//div[@id='network']//input[@id='edit-eth1-ip']";
	private static String eth2EditApplyBtn = "//div[@id='network']//button[@id='btn-edit-eth2-apply']";
	private static String eth2IpAddress = "//div[@id='network']//input[@id='enable-eth2-ip']";
	private static String eth2Netmask = "//div[@id='network']//input[@id='enable-eth2-netmask']";
	private static String eth2Gateway = "//div[@id='network']//input[@id='enable-eth2-gateway']";
	private static String editEth2Gateway = "//div[@id='network']//input[@id='edit-eth2-gateway']";
	private static String editEth2Netmask = "//div[@id='network']//input[@id='edit-eth2-netmask']";
	private static String editEth2Ip = "//div[@id='network']//input[@id='edit-eth2-ip']";
	//Active Directory
	public static String activeDirectoryTab = "//div[@data-bb-tab='ldap']";
	public static String activeDirectoryGlobalApplyButton = "//input[@name='apply_ad_settings']";
	public static String activeDirectorySupportButton = "//span[@id='ad-enabled-input']";
	public static String activeDirectoryIpTextBox = "//input[@id='ldap-ip-field']";//ip-field
	public static String activeDirectoryPortTextBox = "//input[@id='port-field']";
	public static String activeDirectoryDomainTextBox = "//input[@id='ad-domain-field']";
	public static String activeDirectoryUsernameTextBox = "//input[@id='ad-username-field']";
	public static String activeDirectoryPasswordTextBox = "//input[@id='ad-password-field']";
	public static String activeDirectoryToast = "//div[@class='toast-message']";
	public static String groupAssociationsSearchBox = "//input[@type='search']";
	public static String OUtableData = "//table[@id='ou-table']/tbody/tr[1]/td[1]";
	public static String connectionGroupTableData = "//table[@id='ou-table']/tbody/tr[1]/td[2]";
	private static String createOuButton = "//button[contains(text(),'Add AD')]";
	private static String createAddOuButton = "//button[@id='add-ou-modal-button']";
	private static String deleteOuLink = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Delete')]";
	
	private static String eth1IpFromTable = "//div[@id='network']//td[2]";
	private static String eth1Netmask = "//div[@id='network']//input[@id='edit-eth1-netmask']";
	private static String eth1Gateway = "//div[@id='network']//input[@id='edit-eth1-gateway']";

	private static String eth2IpFromTable = "//div[@id='network']//td[2]";//"//table[@data-header='Network Settings Table']";///tbody/tr[1]/td[1]";
	private static String eth2NetmaskFromTable = "//div[@id='network']//td[3]";
	private static String eth2GatewayFromTable = "//div[@id='network']//td[4]";
	public static String OUTableDropdown = "//button[@id='dropdownKebab']";
	public static String linkToGroup = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Link To Group')]";
	public static String linkToGroupCancelButton = "//button[@class='btn btn-default'][contains(text(),'Cancel')]";
	public static String unlinkGroupButton = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Unlink')]";
	public static String currentADSettingsIpAddress = "//td[@id='ldap-ip-td']";
	public static String currentADSettingsPort = "//td[@id='ldap-port-td']";
	public static String currentADSettingsDomain = "//td[@id='ldap-domain-td']";
	public static String currentADSettingsUsername = "//td[@id='ldap-admin-td']";
	private static String createOuModal = "//div[@id='add-ou-modal']//div[@class='modal-content']";
	private static String ouNameTextBox = "//input[@id='ou-name-field']";
	private static WebElement element = null;
	public static String licenseTab = "//span[@class='list-group-item-value dropdown-btn'][contains(text(),'License')]";
	public static String appLicenseLink = "//span[contains(.,'App License')]";
	public static String addAppLicenseBtn = "//span[contains(.,'Add License')]";
	public static String boxillaLicenseLink = "//span[contains(text(),'Boxilla License')]";
	public static String chooseFileAppLicense = "//input[@id='kvm_rap_license_filename']";
	public static String appLicesneSubmitBtn = "//input[@value='Submit']";
	public static String appLicenseNumber = "//td[@align='center']";
	public static String thresholdsSearchBox = "//input[@type='search']";
	public static String deleteAppLicenseButton = "//a[@class='delete-rap-license']";
	
	//clock
	public static String clocktab= "//div[contains(text(),'Clock')]";
	public static String timeZoneSearchbox= "//input[@id='time-zone-picker']";
	public static String timeZoneSubmitbtn= "//button[@id='time-zone-submit']";

	//rest
	public static String restApiTab = "//div[contains(text(),'REST Api')]";
	public static String restApiSwitchOff = "//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-off bootstrap-switch-mini bootstrap-switch-id-rest-api-toggle-switch bootstrap-switch-animate']//span[@class='bootstrap-switch-handle-off bootstrap-switch-default']";
	public static String restApiSwitchOn = "//div[@class='bootstrap-switch bootstrap-switch-wrapper bootstrap-switch-on bootstrap-switch-mini bootstrap-switch-id-rest-api-toggle-switch bootstrap-switch-animate']//span[@class='bootstrap-switch-handle-on bootstrap-switch-primary']";
	public static String restApiHeader = "//h2[contains(text(),'REST API State')]";
	public static String SearchOU="//input[@aria-controls='ou-table']";
	
	
	
	public static String getDeleteOuDropdown(String ouName) {
		return "//tr[contains(@id,'" + ouName + "')]//img";
	}
	
	public static WebElement searchOU(WebDriver driver) {
		return driver.findElement(By.xpath(SearchOU));
	}
	public static String getDeleteOuLink() {
		return deleteOuLink;
	}
	public static String getCreateAddOuButton() {
		return createAddOuButton;
	}
	public static String getOuNameTextBox() {
		return ouNameTextBox;
	}
	public static String getCreateOuModal() {
		return createOuModal;
	}
	public static String getCreateOuButton() {
		return createOuButton;
	}
	public static String getRestApiHeader() {
		return restApiHeader;
	}
	public static String getRestApiSwitchOff() {
		return restApiSwitchOff;
	}
	public static String getRestApiTab() {
		return restApiTab;
	}
	public static String getSerialNumberFromTable() {
		return serialNumberFromTable;
	}
	public static String getMinorVersionFromTable() {
		return minorVersionFromTable;
	}
	public static String getCurrentVersionFromTable() {
		return currentVersionFromTable;
	}
	public static String getExportButton() {
		return exportButton;
	}
	public static String getSystemInfoTab() {
		return systemInfoTab;
	}
	public static String getEth1Gateway() {
		return eth1Gateway;
	}
	public static String getEth1Netmask() {
		return eth1Netmask;
	}
	public static String getEth1IpAddress() {
		return eth1IpAddress;
	}
	public static String getEth1ApplyBtn() {
		return eth1ApplyBtn;
	}
	public static String getEth1IpFromTable() {
		return eth1IpFromTable;
	} 

	public static String getEditEth2Ip() {
		return editEth2Ip;
	}
	public static String getEditEth2Netmask() {
		return editEth2Netmask;
	}
	public static String getEditEth2Gateway() {
		return editEth2Gateway;
	}
	public static String getEth2EditApplyBtn() {
		return eth2EditApplyBtn;
	}
	public static String getEditEth2IpBtn() {
		return editEth2IpBtn;
	}
	public static String getEditEth2Dropdown() {
		return editEth2Dropdown;
	}
	public static String getEth2NetmaskFromTable() {
		return eth2NetmaskFromTable;
	}
	public static String getEth2GatewayFromTable() {
		return eth2GatewayFromTable;
	}

	
	public static String getEth2IpFromTable() {
		return eth2IpFromTable;
	}
	public static String getRotate() {
		return rotate;
	}
	public static String getEth2Gateway() {
		return eth2Gateway;
	}
	public static String getEth2Netmask() {
		return eth2Netmask;
	}
	public static String getEth2IpAddress() {
		return eth2IpAddress;
	}
	public static String getEth2ApplyBtn() {
		return eth2ApplyBtn;
	}
	
	public static String getEditEth1Btn() {
		return editEth1Btn;
	}
	
	public static String getEnableEth2Btn() {
		return enableEth2Btn;
	}
	
	public static String getEth1DropdownBtn() {
		return eth1DropdownBtn;
	}
	public static String getConnectionGroupListElement(String connectionGroupName) {
		return "//td[@id='name_" + connectionGroupName + "']";
	}
	public static WebElement getLinkToGroupCancelButton(WebDriver driver) {
		return driver.findElement(By.xpath(linkToGroupCancelButton));
	}
	public static WebElement getLinkToGroup(WebDriver driver) {
		return driver.findElement(By.xpath(linkToGroup));
	}
	public static WebElement getActiveDirectoryToast(WebDriver driver) {
		return driver.findElement(By.xpath(activeDirectoryToast));
	}
	
	public static WebElement getActiveDirectorySupportButton(WebDriver driver) {
		return driver.findElement(By.xpath(activeDirectorySupportButton));
	}
	public static WebElement getActiveDirectoryGlobalApplyButton(WebDriver driver) {
		return driver.findElement(By.xpath(activeDirectoryGlobalApplyButton));
	}
	public static WebElement getDeleteAppLicenseButton(WebDriver driver) {
		return driver.findElement(By.xpath(deleteAppLicenseButton));
	}
	public static String get2kWarningTextBox(String id) {
		return "//input[@name='kvm_thresholds[warning_threshold]']";
	}
	public static String get4kWarningTextBox(String id) {
		return "//input[@name='kvm_thresholds[warning_threshold_4k]']";
	}
	public static String get2kCriticalTextBox(String id) {
		return "//input[@name='kvm_thresholds[critical_threshold]']";
	}
	public static String get4kCriticalTextBox(String id) {
		return "//input[@name='kvm_thresholds[critical_threshold_4k]']";
	}
	public static String get2kMaxTextBox(String id) {
		return "//input[@name='kvm_thresholds[max]']";
	}
	public static String get4kMaxTextBox(String id) {
		return "//input[@name='kvm_thresholds[max_4k]']";
	}
	public static WebElement getBoxillaLicenseLink(WebDriver driver) {
		element = driver.findElement(By.xpath(boxillaLicenseLink));
		return element;
	}
	
	public static WebElement getAddAppLicenseBtn(WebDriver driver) {
		element = driver.findElement(By.xpath(addAppLicenseBtn));
		return element;
	}
	
	public static WebElement getAppLicenseLink(WebDriver driver) {
		element = driver.findElement(By.xpath(appLicenseLink));
		return element;
	}

	public static WebElement upgradeTab(WebDriver driver) { // Upgrade Tab - System > Administration > Upgrade
		element = driver.findElement(By.xpath("//div[@data-bb-title='Upgrade']"));
		return element;
	}

	public static WebElement backupRestoreTab(WebDriver driver) { // Backup Restore Tab
		element = driver.findElement(By.xpath("//div[@data-bb-title='System Backups']"));
		return element;
	}
	

	public static WebElement backupBtn(WebDriver driver) { // Backup Button
		return element = driver.findElement(By.xpath(".//*[@class='bb-title-btn backups-backup']"));
	}

	public static WebElement uploadBtn(WebDriver driver) { // Upload Button
		return element = driver.findElement(By.xpath(".//*[@class='bb-title-btn backups-upload']"));
	}

	public static WebElement resetdbBtn(WebDriver driver) { // Reset DB Button
		return element = driver.findElement(By.xpath(".//*[@class='bb-title-btn backups-reset']"));
	}

	public static WebElement uploadElement(WebDriver driver) { // Upload element System > Admin > Upgrade > file upload
//		element = driver.findElement(By.xpath(".//*[@accept='.bbx']"));
		element = driver.findElement(By.xpath(".//*[@type='file']"));
		return element;
	}

	public static WebElement btnSubmit(WebDriver driver) { // Submit button -- File upload

		element = driver.findElement(By.xpath(".//input[@type='submit']"));
		return element;
	}

	public static WebElement btnCancel(WebDriver driver) { // Cancel button -- File upload
		element = driver.findElement(By.xpath(".//a[@href='/system/home']"));
		return element;
	}

	public static WebElement btnVersionBreadCrumb(WebDriver driver, String name) { // Breadcrumb upgrade table
//		return element = driver.findElement(By.xpath(
//				"//tr/th[normalize-space(text())=\"" + name + "\"]/..//*[@class='btn btn-link dropdown-toggle']"));
		return element = driver.findElement(By.xpath("//td//td[@class='bb-table-dropdown-td']//img"));
	}

	/*public static WebElement btnVersionBreadCrumb(WebDriver driver) { // Breadcrumb upgrade table
		return element = driver.findElement(By.xpath(".//*[@id='upgradetable']//*[@id='dropdownKebab']"));
	}*/

	public static WebElement versionActivate(WebDriver driver, String name) { // Active version button
//		element = driver.findElement(
//				By.xpath("//tr/th[normalize-space(text())=\"" + name + "\"]/..//*[@class='upgrade-activate']"));
		element = driver.findElement(By.xpath("(//a[normalize-space()='Activate'])[1]"));
		return element;
	}

	/*public static WebElement versionActivate(WebDriver driver) { // Active version button
		return element = driver.findElement(By.xpath("//*[@class='upgrade-activate']"));
	}*/

	public static WebElement systemInfo(WebDriver driver) { // System Info - System > Admin > SystemInfo
		element = driver.findElement(By.xpath("//div[@data-bb-title='System Info']"));
		return element;
	}
	public static WebElement certificates(WebDriver driver) 
	{
		element = driver.findElement(By.xpath(".//*[contains(text(),'Certificates')]"));
		return element;
	}
	
	public static String getSysteminfoTableColumn(String index) {
		return "//div[@id='systeminfo']//tr//td[" + index + "]";
	}
	public static WebElement resetcert(WebDriver driver) 
	{
		return element=driver.findElement(By.xpath(".//*[contains(text(),'Reset Certs to Default')]"));
	}

	public static WebElement systemInfoTable(WebDriver driver) { // System Info Table - System > Admin > SystemInfo
		element = driver.findElement(By.xpath(".//*[@id='systeminfo']"));
		return element;
	}

	public static WebElement currentVersionTable(WebDriver driver) { // Current Version Table
//		element = driver.findElement(By.xpath("(.//*[@id='new_kvm_upgrade'])[1]"));
		element = driver.findElement(By.xpath("(.//*[@class='datatable react-data-table line-chart-pf table table-striped table-bordered']//tbody//tr//td[1])[1]"));
		return element;
	}

	public static WebElement searchboxUpgrade(WebDriver driver) { // Search box under System > Administration > Upgrade
		element = driver.findElement(By.xpath("(.//*[@type='search'])[2]"));
		return element;
	}

	public static WebElement searchboxBackupTable(WebDriver driver) { // Search box under System > Administration >
																		// Upgrade
		return element = driver.findElement(By.xpath(".//*[@id='backup-table_filter']/label/input"));
	}

	public static WebElement backupUploadElement(WebDriver driver) { // Browse button / Database backup upload element
		return element = driver.findElement(By.xpath("//input[@id='upload_file']"));
	}

	public static WebElement backupTable(WebDriver driver) { // Backup table
		return element = driver.findElement(By.xpath(".//*[@id='backup-table']"));
	}
	public static WebElement backupTime(WebDriver driver) {
		return element= driver.findElement(By.xpath("(//tbody//tr//td[2])[1]"));
	}

	public static WebElement breadCrumb(WebDriver driver) {
		return element = driver.findElement(By.xpath(".//*[@id='dropdownKebab']"));
	}
	public static WebElement deleteBtn(WebDriver driver) 
	{
		return element=driver.findElement(By.xpath(".//*[@data-confirm='Delete Backup boxilla ?']"));
				
	}
	public static WebElement editbtnBoxillaUser(WebDriver driver) 
	{
		return element=driver.findElement(By.xpath(".//*[@class='user-edit']"));
	}

	public static WebElement restoreBtn(WebDriver driver) {
		return element = driver.findElement(By.linkText("Restore"));
		// .//*[@data-original-title='Restore from System Backup File']
	}

	public static WebElement networkTab(WebDriver driver) { // Network Tab - System > Settings > Network
		element = driver.findElement(By.xpath("//div[@data-bb-tab='network']"));
		return element;
	}

	public static WebElement thresholdsTab(WebDriver driver) { // Thresholds Tab - System > Settings > Thresholds
		return element = driver.findElement(By.xpath("//div[@data-bb-tab='thresholds']"));
	}

	public static WebElement ipAddTextbox(WebDriver driver) { // IP address text box
		return element = driver.findElement(By.xpath("(.//*[@id='ip'])[1]"));
	}

	public static WebElement netmaskTextbox(WebDriver driver) { // Netmask text box
		return element = driver.findElement(By.xpath("(.//*[@id='netmask'])[1]"));
	}

	public static WebElement gatewayTextbox(WebDriver driver) { // Gateway text box
		return element = driver.findElement(By.xpath("(.//*[@id='gateway'])[1]"));
	}

	public static WebElement applyBtn(WebDriver driver) { // Apply button
		return element = driver.findElement(By.xpath("(.//*[@id='changeip'])[1]"));
	}

	public static WebElement currentNetworkSettings(WebDriver driver) { // Current network setting table
		return element = driver.findElement(By.xpath(".//*[@class='table table-bordered']"));
	}

	public static WebElement clockTab(WebDriver driver) { // Clock tab - System > Settings > Clock
		return element = driver.findElement(By.xpath("//div[@data-bb-tab='changedate']"));
	}
	
	public static WebElement ActiveDirectoryTab(WebDriver driver) { 
		return element = driver.findElement(By.xpath(activeDirectoryTab));
	}

	public static WebElement addLicenseBtn(WebDriver driver) { // Add License Button
		return element = driver.findElement(By.xpath(".//a[@href='/blackbox/system/addlicense']"));
	}

	public static WebElement licenseUploadElement(WebDriver driver) { // Browse button / License upload element
		return element = driver.findElement(By.xpath(".//*[@id='kvm_upgrade_filename']"));
	}

	public static WebElement currentLicenseLimt(WebDriver driver) { // Current limit of licenses
		return element = driver.findElement(By.xpath(".//*[@id='tr_info']/td/b"));
	}

	public static WebElement deleteLicense50(WebDriver driver) { // License delete button > for license50
		return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"50\"]/../td/a"));
	}

	// Upgrade Tab - System > Administration > Boxilla Users
	public static WebElement boxillaUsersTab(WebDriver driver) {
		return element = driver.findElement(By.xpath("//div[@data-bb-title='Users']"));
	}

	public static WebElement newUser(WebDriver driver) { // New User button
		return element = driver.findElement(By.xpath(".//a[@href='/users/new']"));
	}

	public static WebElement boxillaUserName(WebDriver driver) { // Boxilla Username Textbox
		return element = driver.findElement(By.xpath(".//*[@id='user_login']"));
	}

	public static WebElement boxillaFirstName(WebDriver driver) { // Boxilla Firstname
		return element = driver.findElement(By.xpath(".//*[@id='user_firstname']"));
	}

	public static WebElement boxillaSurname(WebDriver driver) { // Boxilla Surname
		return element = driver.findElement(By.xpath(".//*[@id='user_lastname']"));
	}

	public static WebElement boxillaEmailAdd(WebDriver driver) { // Boxilla Email Address
		return element = driver.findElement(By.xpath(".//*[@id='user_mail']"));
	}

	public static WebElement boxillaAuthorizedBy(WebDriver driver) { // Authorized by dropbox
		return element = driver.findElement(By.xpath(".//*[@id='user_auth_source_id']"));
	}

	public static WebElement boxillaPassword(WebDriver driver) { // Boxilla user passowrd
		return element = driver.findElement(By.xpath(".//*[@id='user_password']"));
	}
	public static WebElement checkErrormessage(WebDriver driver) 
	{
		return element=driver.findElement(By.xpath("//span[contains(text(),'Your password cannot contain your username')]"));
	}
	public static WebElement checksequencesErrormessage(WebDriver driver) 
	{
		return element=driver.findElement(By.xpath(".//*[contains(text(),'Your password contains sequences')]"));
	}

	public static WebElement boxillaPasswordVerify(WebDriver driver) { // Boxilla user password verify
		return element = driver.findElement(By.xpath(".//*[@id='user_password_confirmation']"));
	}

	public static WebElement searchboxBoxillaUsers(WebDriver driver) { // Search box - Boxilla Users
		return element = driver.findElement(By.xpath(".//*[@id='table-user_filter']/label/input"));
	}

	public static WebElement deleteLicenseBtn(WebDriver driver) { // Delete License button
		return element = driver.findElement(By.xpath(".//*[@data-original-title='Delete license']"));
	}

	public static WebElement audioBWEdit(WebDriver driver) { // Edit button Audio BW
		return element = driver
				.findElement(By.xpath("//tr/td[normalize-space(text())=\"Audio BW\"]/../td[6]/center/button"));
	}

	public static WebElement editBtnThresholds(WebDriver driver, String thresholdName) { // Edit button
		return element = driver.findElement(By.xpath("//button[contains(text(),'Edit')]"));
//		if (thresholdName.equalsIgnoreCase("Audio BW")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"Audio BW\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
//			return element = driver.findElement(
//					By.xpath("//tr/td[normalize-space(text())=\"Dropped Frames\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
//			return element = driver.findElement(
//					By.xpath("//tr/td[normalize-space(text())=\"Frames Per Second\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("RTT")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"RTT\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("Total BW")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"Total BW\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"USB BW\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"User Latency\"]/../td[6]/center/button"));
//		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
//			return element = driver
//					.findElement(By.xpath("//tr/td[normalize-space(text())=\"Video BW\"]/../td[6]/center/button"));
//		} else {
//			return null;
//		}
	}

	public static WebElement thresholdRow(WebDriver driver, String thresholdName) { // Edit button
		if (thresholdName.equalsIgnoreCase("Audio BW")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"Audio BW\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"Dropped Frames\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"Frames Per Second\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("RTT")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"RTT\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("Total BW")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"Total BW\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"USB BW\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"User Latency\"]/.."));
		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
			return element = driver.findElement(By.xpath("//tr/td[normalize-space(text())=\"Video BW\"]/.."));
		} else {
			return null;
		}
	}

	public static WebElement warningThreshold(WebDriver driver, String thresholdName) { // Warning Threshold
		if (thresholdName.equalsIgnoreCase("Audio BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[1]"));
		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[2]"));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[3]"));
		} else if (thresholdName.equalsIgnoreCase("RTT")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[4]"));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[5]"));
		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[6]"));
		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[7]"));
		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[warning_threshold]'])[8]"));
		} else {
			return null;
		}
	}

	public static WebElement criticalThreshold(WebDriver driver, String thresholdName) { // Critical Threshold
		if (thresholdName.equalsIgnoreCase("Audio BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[1]"));
		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[2]"));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[3]"));
		} else if (thresholdName.equalsIgnoreCase("RTT")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[4]"));
		} else if (thresholdName.equalsIgnoreCase("Total BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[5]"));
		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[6]"));
		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[7]"));
		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[critical_threshold]'])[8]"));
		} else {
			return null;
		}
	}

	public static WebElement maxThreshold(WebDriver driver, String thresholdName) { // Max Threshold
		if (thresholdName.equalsIgnoreCase("Audio BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[1]"));
		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[2]"));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[3]"));
		} else if (thresholdName.equalsIgnoreCase("RTT")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[4]"));
		} else if (thresholdName.equalsIgnoreCase("Total BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[5]"));
		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[6]"));
		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[7]"));
		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
			return element = driver.findElement(By.xpath("(.//*[@name='kvm_thresholds[max]'])[8]"));
		} else {
			return null;
		}
	}

	public static WebElement saveBtnThreshold(WebDriver driver, String thresholdName) { // Save Threshold
		if (thresholdName.equalsIgnoreCase("Audio BW")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='Audio BW']"));
		} else if (thresholdName.equalsIgnoreCase("Dropped Frames")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='Dropped Frames']"));
		} else if (thresholdName.equalsIgnoreCase("Frames Per Second")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='Frames Per Second']"));
		} else if (thresholdName.equalsIgnoreCase("RTT")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='RTT']"));
		} else if (thresholdName.equalsIgnoreCase("Total BW")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='Total BW']"));
		} else if (thresholdName.equalsIgnoreCase("USB BW")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='USB BW']"));
		} else if (thresholdName.equalsIgnoreCase("User Latency")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='User Latency']"));
		} else if (thresholdName.equalsIgnoreCase("Video BW")) {
			return element = driver.findElement(By.xpath(".//*[@data-what-id='Video BW']"));
		} else {
			return null;
		}
	}

	public static WebElement dateTimeField(WebDriver driver) { // Date Time Picker
		return element = driver.findElement(By.xpath(".//*[@id='datetime']"));
	}

	public static WebElement calendarBtn(WebDriver driver) { // calendar button
		return element = driver.findElement(By.xpath(".//*[@class='input-group-addon']"));
	}

	public static WebElement dateSubmit(WebDriver driver) { // date submit button
		return element = driver.findElement(By.xpath(".//*[@type='submit']"));
	}

	public static WebElement clockText(WebDriver driver) { // clock setting text
		return element = driver.findElement(By.xpath(".//div[@class='form-group']/h4"));
	}

	public static WebElement clockIcon(WebDriver driver) { // Clock icon
		return element = driver.findElement(By.xpath(".//*[@class='glyphicon glyphicon-time']"));
	}

	public static WebElement hourUpIcon(WebDriver driver) { // hour up icon
		return element = driver.findElement(By.xpath(".//*[@class='glyphicon glyphicon-chevron-up']"));
	}

	public static WebElement boxillaUsersTable(WebDriver driver) { // Boxilla user table
		return element = driver.findElement(By.xpath(".//*[@id='table-user']"));
	}

	public static WebElement boxillaUserBreadCrumb(WebDriver driver, String name) { // boxilla user breadcrumb
		return element = driver.findElement(By.xpath(
				"//tr/td[normalize-space(text())=\"" + name + "\"]/..//*[@class='btn btn-link dropdown-toggle']"));
	}

	public static WebElement boxillaUserDelete(WebDriver driver) { // Boxilla user delete option
		return element = driver.findElement(By.xpath("//*[@class='user-delete']"));
	}

	public static WebElement activeState(WebDriver driver) { // Version State Active or Inactive
		return element = driver.findElement(By.xpath(".//*[@id='upgradetable']/tbody/tr/th[3]"));
	}

	public static WebElement rebootBtn(WebDriver driver) { // reboot button
		return element = driver.findElement(By.xpath(".//*[@id='boxilla-reboot']"));
	}
}
