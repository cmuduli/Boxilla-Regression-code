package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Holds all the object models for the main dashboard in boxilla
 * @author Boxilla
 *
 */
public class Landingpage {

	private static String peripheralsLink = "//span[contains(text(),'Peripherals')]";
	//dashboard 
	private static String dashboardBandwith = "//span[@id='bandwidth-text']"; 
	private static String dashboardHeading = "//h1[@class='bb-title']";
	private static String networkBwTableConName = "//div[@id='tab-network-bandwidth']//div[@class='col-md-6']//div[@class='bble']//div[@id='DataTables_Table_0_wrapper']//div//td[1]";
	private static String userResponseTableConName = "//div[@id='tab-user-response']//td[0]";
	private static String droppedFramesTableConName = "//div[@id='tab-dropped-frames']//td[0]";
	private static String roundtripTimeTableConName = "//div[@id='tab-roundtrip-time']//td[0]";
	private static String userResponseTimeTab = "//div[contains(text(),'User Response Time')]";
	private static String droppedFramesTab = "//div[contains(text(),'Dropped Frames')]";
	private static String roundtripTimeTab = "//div[contains(text(),'Roundtrip Time')]";
	private static String logoutBtn = "//a[@id='menu_item_logout']";
	private static String logoutDropdownXpath = ".//*[@id='dropdownMenu2']";
	private static WebElement element = null;
	public static String dashboardLinkText = "Dashboard";
	private static String connectionsViewerXpath = "//div[@id='Connections-dropdown']//span[text() = 'Viewer']";
	public static String connectionsTab = "Connections";
	public static String connectionsManage = "//a[@href='/connections/manage']";
	private static String discoveryTabLink = "//span[contains(text(),'Discovery')]";
	private static String activeConnectionsToggleBondedViewOn = "//span[contains(text(),'OFF')]";
	private static String activeConnectionsToggleBondedViewOff = "//span[contains(text(),'ON')]";
	private static String bondedConnectionsTableExpand = "//span[contains(text(),'Bonded Active Connections')]//following::td[1]";
	private static String bondedConnectionsTableConName = "//span[contains(text(),'Bonded Active Connections')]//following::td[2]";
	private static String bondedConnectionsTableUsername = "//span[contains(text(),'Bonded Active Connections')]//following::td[3]";
	
	//cluster
	public static String clusterTabXpath = "//span[contains(.,'Cluster')]";
	//switches
	public static String switchesTab = "//a[@data-target='Switches-dropdown']";
	public static String switchesStatusLink = "//span[contains(.,'Status')]";
	public static String switchesUpgradeLink = "//div[@id='Switches-dropdown']//span[contains(.,'Upgrades')]";
	//device groups
	private static String devicesGroupLink = "//a[@href='/devices/groups']";
	private static String devicesLink = "//span[contains(text(),'Devices')]";
	private static String connectionGroupLink = "//a[@href='/connections/groups']";
	
	public static String getConnectionGroupLink() {
		return connectionGroupLink;
	}
	public static String getTransmitterFromBond(int index) {
		return "//tbody/tr[@id='tab-network-bandwidth0']/td[4]/p[" + index + "]";
	}
	public static String getReceiverFromBond(int index) {
		return "//tbody/tr[@id='tab-network-bandwidth0']/td[3]/p[" + index  + "]";
	}
	public static String getUsernameFromBond(int index) {
		return "//tbody/tr[@id='tab-network-bandwidth0']/td[2]/p[" + index + "]";
	}
	public static String getConnectionNameFromBond(int index) {
		return "//tbody/tr[@id='tab-network-bandwidth0']/td[1]/div[" + index + "]/div[1]/p[1]";
	}
	public static String getBondedConnectionsTableUsername() {
		return bondedConnectionsTableUsername;
	}
	public static String getBondedConnectionsTableConName() {
		return bondedConnectionsTableConName;
	}
	
	public static String getBondedConnectionsTableExpand() {
		return bondedConnectionsTableExpand;
	}
	public static String getActiveConnectionsToggleBondedViewOff() {
		return activeConnectionsToggleBondedViewOff;
	}
	public static String getActiveConnectionsToggleBondedViewOn() {
		return activeConnectionsToggleBondedViewOn;
	}
	public static String getDevicesLink() {
		return devicesLink;
	}
	public static String getDevicesGroupLink() {
		return devicesGroupLink;
	}
 	public static String getDiscoveryTabLink() {
		return discoveryTabLink;
	}
	public static String getPeripheralsLink() {
		return peripheralsLink;
	}
	
	public static String getLogoutBtn() {
		return logoutBtn;
	}
	public static String getLogoutDropdownXpath() {
		return logoutDropdownXpath;
	}
	public static String getRoundtripTimeTableConName() {
		return roundtripTimeTableConName;
	}
	public static String getDroppedFramesTableConName() {
		return droppedFramesTableConName;
	}
	public static String getRoundtripTimeTab() {
		return roundtripTimeTab;
	}
	
	public static String getDroppedFramesTab() {
		return droppedFramesTab;
	}
	
	public static String getUserResponseTimeTab() {
		return userResponseTimeTab;
	}
	public static String getUserResponseTableConName() {
		return userResponseTableConName;
	}
	public static String getNetworkBwTableConName() {
		return networkBwTableConName;
	}
	
	public static String getDashboardHeading() {
		return dashboardHeading;
	}
	public static String getDashboardBandwith() {
		return dashboardBandwith;
	}
	public static WebElement devicesStatus(WebDriver driver) { // Devices -> Status tab
		element = driver.findElement(By.xpath("//div[@id='Devices-dropdown']//span[@class='list-group-item-value'][contains(text(),'Settings')]"));
		return element;
	}
	
	public static WebElement dellSwitchesTab(WebDriver driver) {
		return driver.findElement(By.xpath(switchesTab));
	}
	public static String getClusterTabXpath() {
		return clusterTabXpath;
	}
	public static WebElement getClusterTab(WebDriver driver) {
		return driver.findElement(By.xpath(getClusterTabXpath()));
	}
	public static WebElement connectionsViewer(WebDriver driver) {
		return driver.findElement(By.xpath(connectionsViewerXpath));
	}
	public static WebElement usersTab(WebDriver driver) { // Users Tab on landing tab
		element = driver.findElement(By.linkText("Users"));
		return element;
	}

	public static WebElement usersManageTab(WebDriver driver) { // Users -> Manage tab
		// element =
		// driver.findElement(By.xpath(".//*[@id='Users-dropdown']/ul/li[1]/a"));
		element = driver.findElement(By.xpath("//a[@href='/users/manage']"));
		return element;
	}

	public static WebElement usersActiveTab(WebDriver driver) { // Users -> Active tab
		element = driver.findElement(By.linkText("Active"));
		return element;
	}

	public static WebElement logoutDropdown(WebDriver driver) { // Drop Down menu on User Icon on top right corner
		element = driver.findElement(By.xpath(".//*[@id='dropdownMenu2']"));
		return element;
	}

	public static WebElement logoutbtn(WebDriver driver) { // Log Out Button
		element = driver.findElement(By.linkText("Log out"));
		return element;
	}

	public static WebElement dashboard(WebDriver driver) { // Brand Icon to Navigate to Home Page
		element = driver.findElement(By.linkText(dashboardLinkText));
		return element;
	}

//	public static WebElement connectionsTab(WebDriver driver) { // Connection Tab on landing tab
//		element = driver.findElement(By.linkText("Connections"));
//		return element;
//	}

//	public static WebElement connectionsManage(WebDriver driver) { // Connection -> Manage tab
//		element = driver.findElement(By.xpath("//a[@href='/connections/manage']"));
//		return element;
//	}
	
	//connection -> active
	public static WebElement connectionsActive(WebDriver driver) {		
		return driver.findElement(By.xpath("//a[@href='/connections/active']"));
	}

	public static WebElement systemTab(WebDriver driver) { // System Tab on landing tab
		element = driver.findElement(By.linkText("System"));
		return element;
	}

	public static WebElement systemAdmin(WebDriver driver) { // System -> Administration
		element = driver.findElement(By.xpath("//span[contains(text(),'Administration')]"));
		return element;
	}

	public static WebElement systemSettings(WebDriver driver) { // System -> Settings
		element = driver.findElement(By.xpath("//div[@id='System-dropdown']//span[@class='list-group-item-value'][contains(text(),'Settings')]"));
		return element;
	}

	public static WebElement systemLicense(WebDriver driver) {
		return element = driver.findElement(By.xpath("//a[@href='/blackbox_invisa_pc_plugin/system/license']"));
	}

	public static WebElement spinner(WebDriver driver) { // Spinner Element
		element = driver.findElement(By.xpath(".//*[@id='spinner']"));
		return element;
	}

	public static WebElement title(WebDriver driver) { // Title Element
		element = driver.findElement(By.xpath("//title"));
		return element;
	}

	public static WebElement discoveryTab(WebDriver driver) { // Discovery Tab
		element = driver.findElement(By.linkText("Discovery"));
		return element;
	}

	public static WebElement devicesTab(WebDriver driver) { // Devices Tab on landing tab
		element = driver.findElement(By.linkText("Devices"));
		return element;
	}



	public static WebElement devicesUpgrades(WebDriver driver) { // Devices -> Upgrades tab
		return element = driver.findElement(By.xpath("//a[@href='/devices/upgrades']"));
	}

	public static WebElement devicesSettings(WebDriver driver) { // Devices -> Settings tab
		return element = driver.findElement(By.xpath("//a[@href='/devices/settings']"));
	}

	public static WebElement groupsTab(WebDriver driver) { // Connections -> Groups tab
		return element = driver.findElement(By.xpath("//a[@href='/connections/groups']"));
	}

	public static WebElement dkmTab(WebDriver driver) { // DKM Tab on landing tab
		return element = driver.findElement(By.linkText("DKM"));
	}

	public static WebElement switchesTab(WebDriver driver) { // DKM > Switches tab
		return element = driver.findElement(By.xpath("//a[@href='/dkm/home']"));
	}

	public static WebElement viewerBtn(WebDriver driver) { // DKM > Viewer tab
		return element = driver.findElement(By.xpath("//a[@href='/dkm/viewer']"));
	}

	public static WebElement dkmConnectionsTab(WebDriver driver) { // DKM > Connections
		return element = driver.findElement(By.xpath("//a[@href='/dkm/active']"));
	}
}
