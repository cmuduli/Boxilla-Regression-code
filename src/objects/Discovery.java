package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Holds all the object models for Boxills - Discovery page
 * @author Boxilla
 *
 */
public class Discovery {
	private static WebElement element = null;

	private static String zoneDropdown = "//select[@id='zones-dropdown']";
	private static String discoverButton = "//span[@class='bb-title-btn-text'][contains(text(),'Discover')]";
	public static String discoverBtnXpath = "Discover";
	public static String automaticDiscoveryTab = ".//div[@data-bb-tab='tab-discovery']";
	public static String addManuallyTab = ".//div[@data-bb-tab='tab-manage-device']";
	public static String searchBox = ".//input[@type='search']";
	public static String connectionTableBody = ".//table/tbody";
	public static String searchedConnection = "//table/tbody/tr[1]";
	public static String applianceEdit = ".//*[@data-original-title='Edit Managed Appliance Settings']";
	public static String applianceManage  = ".//*[@data-original-title='Manage Discovered Appliance']";
	public static String ipAddress = ".//*[@id='kvm_discovery_ip']";
	public static String gateway = ".//*[@id='kvm_discovery_gateway']";
	public static String netmask = ".//*[@id='kvm_discovery_netmask']";
	public static String applyBtn = ".//input[@value='Apply']";
	public static String breadCrumbBtn = "//*[@id='dropdownKebab']";
	public static String managedName = ".//*[@id='kvm_appliance_name']";
	public static String manageApplyBtn = ".//*[@id='discovery-manage']";
	public static String manualSearchIPaddBox = ".//*[@id='ip-address']";
	public static String hostName = ".//*[@id='hostname']";
	public static String getInfoBtn = ".//*[@id='btn-get-info']";
	public static String deviceInfo = ".//*[@id='info']";
	public static String manageDevice = ".//*[@id='btn-manage']";
	public static String ipOfManagedDevice = "//table/tbody/tr[1]/td[2]";
	public static String deviceState = "//table/tbody/tr[1]/td[6]";
	public static String manageSwitchBtnXpath = "//a[@data-original-title='Manage Discovered Appliance']";
	private static String discoverSearchBox = "//div[@id='discovery-table_filter']//label//input";
	private static String discoveryTableRow = "//tr[@id='Appliance']";
	private static String editDeivceApplyBtn = "//input[@name='commit']";
	public static String pingBtnXpath=".//*[contains(text(),'Ping')]";
	public static String deviceIpSearchXpath=".//*[@id='ip']";
	public static String pingBtn=".//*[@class='btn btn-primary btn-ping']";
	
	
	public static String getPingoptnBtn() 
	{
		return pingBtn;
	}
	
	public static String getPingBtn() 
	{
		return pingBtnXpath;
	}
	public static String getDeviceIpSearchBtn() 
	{
		return deviceIpSearchXpath;
	}
	
	public static String getEditDeivceApplyBtn() {
		return editDeivceApplyBtn;
	}
	public static String getDiscoveryTableRow() {
		return discoveryTableRow;
	}
	public static String getDiscoverSearchBox() {
		return discoverSearchBox;
	}
	public static String getDiscoverButton() {
		return discoverButton;
	}
	public static String getZoneDropdown() {
		return zoneDropdown;
	}
	public static String getManageSwitchBtnXpath() {
		return manageSwitchBtnXpath;
	}
	public static WebElement getManageSwitchBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getManageSwitchBtnXpath()));
	}
	public static WebElement discoverBtn(WebDriver driver) { // Discover Button
		element = driver.findElement(By.linkText(discoverBtnXpath));
		return element;
	}

//	public static WebElement automaticDiscoveryTab(WebDriver driver) { // Automatic Discovery Tab
//		element = driver.findElement(By.xpath(".//div[@data-bb-tab='tab-discovery']"));
//		return element;
//	}

//	public static WebElement addManuallyTab(WebDriver driver) { // Add Manually Tab
//		element = driver.findElement(By.xpath(".//div[@data-bb-tab='tab-manage-device']"));
//		return element;
//	}

//	public static WebElement searchBox(WebDriver driver) { // Search box
//		element = driver.findElement(By.xpath(".//input[@type='search']"));
//		return element;
//	}
//
//	public static WebElement connectionTableBody(WebDriver driver) { // Connection Table
//		element = driver.findElement(By.xpath(".//table/tbody"));
//		return element;
//	}
//
//	public static WebElement searchedConnection(WebDriver driver) { // First row from searched results
//		element = driver.findElement(By.xpath("//table/tbody/tr[1]"));
//		return element;
//	}
//
	public static WebElement applianceEdit(WebDriver driver) { // Breadcrumb > Edit appliance
		element = driver.findElement(By.xpath(".//*[@data-original-title='Edit Managed Appliance Settings']"));
		return element;
	}
//
//	public static WebElement applianceManage(WebDriver driver) { // Breadcrumb > Manage appliance
//		element = driver.findElement(By.xpath(".//*[@data-original-title='Manage Discovered Appliance']"));
//		return element;
//	}
//
	public static WebElement ipAddress(WebDriver driver) { // KVM Discovery IP address
		element = driver.findElement(By.xpath(".//*[@id='kvm_discovery_ip']"));
		return element;
	}
//
//	public static WebElement gateway(WebDriver driver) { // KVM Gateway address
//		element = driver.findElement(By.xpath(".//*[@id='kvm_discovery_gateway']"));
//		return element;
//	}
//
//	public static WebElement netmask(WebDriver driver) { // KVM subnetmask address
//		element = driver.findElement(By.xpath(".//*[@id='kvm_discovery_netmask']"));
//		return element;
//	}
//
//	public static WebElement applyBtn(WebDriver driver) { // Apply Button to complte editing appliance details and save
//		element = driver.findElement(By.xpath(".//input[@value='Apply']"));
//		return element;
//	}
//
//	public static WebElement breadCrumbBtn(WebDriver driver) { // breadcrumb button
//		element = driver.findElement(By.xpath("//*[@id='dropdownKebab']"));
//		return element;
//	}
//
//	public static WebElement managedName(WebDriver driver) { // Managed Name Text Box
//		element = driver.findElement(By.xpath(".//*[@id='kvm_appliance_name']"));
//		return element;
//	}
//
	public static WebElement manageApplyBtn(WebDriver driver) { // Manage device - Apply Btn
		element = driver.findElement(By.xpath(".//*[@id='discovery-manage']"));
		return element;
	}
//
//	public static WebElement manualSearchIPaddBox(WebDriver driver) { // Manual search option > IP address text box
//		element = driver.findElement(By.xpath(".//*[@id='ip-address']"));
//		return element;
//	}
//
//	public static WebElement hostName(WebDriver driver) { // Manually add device HostName
//		element = driver.findElement(By.xpath(".//*[@id='hostname']"));
//		return element;
//	}
//
//	public static WebElement getInfoBtn(WebDriver driver) { // Get Information button
//		element = driver.findElement(By.xpath(".//*[@id='btn-get-info']"));
//		return element;
//	}
//
//	public static WebElement deviceInfo(WebDriver driver) { // Appliance (Device) Information
//		return element = driver.findElement(By.xpath(".//*[@id='info']"));
//	}
//
	public static WebElement manageDevice(WebDriver driver) { // Manage Device
		return element = driver.findElement(By.xpath(".//*[@id='btn-manage']"));
	}
//
//	public static WebElement ipOfManagedDevice(WebDriver driver) { // Grab IP Address
//		return element = driver.findElement(By.xpath("//table/tbody/tr[1]/td[2]"));
//	}
//
//	public static WebElement deviceState(WebDriver driver) { // Grab Device state
//		return element = driver.findElement(By.xpath("//table/tbody/tr[1]/td[6]"));
//	}
}
