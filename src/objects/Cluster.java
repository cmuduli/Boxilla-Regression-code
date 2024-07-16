package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Cluster {

	
	//table columns
	public enum CLUSTER_NODE_COLUMNS {
		IP_ADDRESS, MAC, HOST, SOFTWARE_VERSION, LIMIT, STATE,
		NODE_ID, NODE_NAME
	}
	public enum CLUSTER_INFO_TABLE_COLUMNS {
		CLUSTER_ID, VIRTUAL_IP
	}
	//toast
	private static String toastNotificationXpath = "//div[@class='toast-message']";
	//main page
	private static String switchoverBtnXpath = "//span[contains(.,'Switchover')]";
	private static String prepareStandByBtnXpath = "//span[contains(.,'Prepare Standby')]";
	private static String prepareMasterBtnXpath = "//span[contains(.,'Prepare Master')]";
	private static String nodeInfoTabXpath = "//div[@data-bb-title='Node Info']";
	private static String clusterInfoTabXpath = "//div[@data-bb-title='Cluster Info']";
	private static String attachBtnXpath = "//span[contains(.,'Attach')]";
	private static String dissolveCluserBtnXpath = "//span[contains(.,'Dissolve Cluster')]";
	private static String clusterHeadingXpath = "//h1[contains(.,'Cluster Admin')]";
	private static String nodeInfoSearchBoxXpath = "//div[@id='node-info-table_filter']//label//input";
	private static String clusterInfoSearchBoxXpath = "//input[contains(@aria-controls,'DataTables_Table_1')]";
	private static String makeMasterStandaloneBtnXpath = "//span[contains(.,'Make Master Standalone')]";
	private static String nodeDropdownBtnXpath = "//button[@id='dropdownKebab']";
	private static String detachStandbyDropdownBtnXpath = "//a[contains(.,'Detach')]";
	private static String switchoverToActiveDropdownBtnXpath="//a[contains(.,'Switchover to Active')]";
	private static String makeStandbyStandaloneDropdownBtnXpath = "//a[contains(.,'Make Standalone')]";
	private static String prepareStandbyFailedBtnXpath = "//a[contains(.,'Prepare Standby')]";
	public static String detachFailedBtnXpath = "//a[contains(.,'Detach')]";
	private static String makeFailedStandAloneXpath  = "//a[contains(.,'Standalone')]";
	private static String switchoverBtnpath = "//div[@id='switchover']";
	private static String switchoverFormBtnXpath = "//button[@id='form-switchover-btn']";
	
	//standby preperation
	private static String standByMasterBoxillaIpTextBoxXpath = "//input[@id='preparestandby-mip']";
	private static String standByNodeIdTextBoxXpath = "//input[@id='preparestandby-node-id']";
	private static String standByNodeNameTextBoxXpath = "//input[@id='preparestandby-node-name']";
	private static String standbyPrepareStandbyBtnXpath = "//button[@id='btn-prepare-standby']";
	
	//master preperation
	private static String masterPrepClusterIdTextBoxXpath = "//input[@id='preparemaster-cluster-id']";
	private static String masterPrepVirtualIpTextBoxXpath = "//input[@id='preparemaster-vip']";
	private static String masterPrepNodeIdTextBoxXpath = "//input[@id='preparemaster-node-id']";
	private static String masterPrepNodeNameTextBoxXpath = "//input[@id='preparemaster-node-name']";
	private static String masterPrepPrepareMasterBtnXpath = "//button[@id='btn-prepare-master']";
	
	//attach
	private static String attachClusterIdTextBoxXpath = "//input[@id='attach-cluster-id']";
	private static String attachVirtualIpTextBoxXpath = "//input[@id='attach-vip']";
	
	
	//tosat
	public static String getToastNotificationXpath() {
		return toastNotificationXpath;
	}
	//attach
	public static String getAttachVirtualIpTextBoxXpath() {
		return attachVirtualIpTextBoxXpath;
	}
	public static String getAttachClusterIdTextBoxXpath() {
		return attachClusterIdTextBoxXpath;
	}
	//master preperation
	public static String getMasterPrepPrepareMasterBtnXpath() {
		return masterPrepPrepareMasterBtnXpath;
	}
	public static String getMasterPrepNodeNameTextBoxXpath() {
		return masterPrepNodeNameTextBoxXpath;
	}
	public static String getMasterPrepNodeIdTextBoxXpath() {
		return masterPrepNodeIdTextBoxXpath;
	}
	public static String getMasterPrepVirtualIpTextBoxXpath() {
		return masterPrepVirtualIpTextBoxXpath;
	}
	public static String getMasterPrepClusterIdTextBoxXpath() {
		return masterPrepClusterIdTextBoxXpath;
	}
	
	//standby prep
	public static String getPrepareStandbyFailedBtnXpath() {
		return prepareStandbyFailedBtnXpath;
	}
	public static String getStandByPrepareStandbyBtnXpath() {
		return standbyPrepareStandbyBtnXpath;
	}
	public static String getStandByNodeNameTextBoxXpath() {
		return standByNodeNameTextBoxXpath;
	}
	public static String getStandByNodeIdTextBoxXpath() {
		return standByNodeIdTextBoxXpath;
	}
	public static String getStandyMasterBoxillaIpTextBoxXpath() {
		return standByMasterBoxillaIpTextBoxXpath;
	}
	
	//main page
	public static String getSwitchoverFormBtnXpath() {
		return switchoverFormBtnXpath;
	}
	public static String getSwitchoverBtnpath() {
		return switchoverBtnpath;
	}
	public static String getMakeFailedStandAloneXpath() {
		return makeFailedStandAloneXpath;
	}
	public static String getDetachFailedBtnXpath() {
		return detachFailedBtnXpath;
	}
	public static String getMakeStandbyStandaloneDropdownBtnXpath() {
		return makeStandbyStandaloneDropdownBtnXpath;
	}
	public static String getDetachStandbyDropdownBtnXpath() {
		return detachStandbyDropdownBtnXpath;
	}
	
	public static String getswitchoverToActiveDropdownBtnXpath()
	{
		return switchoverToActiveDropdownBtnXpath;
	}
	
	public static String getNodeDropdownBtnXpath() {
		return nodeDropdownBtnXpath;
	}
	public static String getMakeMasterStandaloneBtnXpath() {
		return makeMasterStandaloneBtnXpath;
	}
	public static String getClusterInfoSearchBoxXpath() {
		return clusterInfoSearchBoxXpath;
	}
	public static String getNodeInfoSearchBoxXpath() {
		return nodeInfoSearchBoxXpath;
	}
	public static String getClusterHeadingXpath() {
		return clusterHeadingXpath;
	}
	public static String getDissolveCluserBtnXpath() {
		return dissolveCluserBtnXpath;
	}
	public static String getAttachBtnXpath() {
		return attachBtnXpath;
	}
	public static String getClusterInfoTabXpath() {
		return clusterInfoTabXpath;
	}
	public static String getNodeInfoTabXpath() {
		return nodeInfoTabXpath;
	}
	public static String getPrepareMasterBtnXpath() {
		return prepareMasterBtnXpath;
	}
	public static String getPrepareStandByBtnXpath() {
		return prepareStandByBtnXpath;
	}
	////////////////////////////////////////////
	///////////   ELEMENTS    /////////////////
	///////////////////////////////////////////
	
	
	//toast
	public static WebElement getToastNotification(WebDriver driver) {
		return driver.findElement(By.xpath(getToastNotificationXpath()));
	}
	//attach
	public static WebElement attachClusterIdTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getAttachClusterIdTextBoxXpath()));
	}
	
	
	//master prep
	public static WebElement getMasterPrepPrepareMasterBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getMasterPrepPrepareMasterBtnXpath()));
	}
	public static WebElement getMasterPrepNodeNameTextBoxXpath(WebDriver driver) {
		return driver.findElement(By.xpath(getMasterPrepNodeNameTextBoxXpath()));
	}
	public static WebElement getMasterPrepNodeIdTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getMasterPrepNodeIdTextBoxXpath()));
	}
	public static WebElement getMasterPrepClusterIdTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getMasterPrepClusterIdTextBoxXpath()));
	}
	public static WebElement getMasterPrepVirtualIpTextBoxXpath(WebDriver driver) {
		return driver.findElement(By.xpath(getMasterPrepVirtualIpTextBoxXpath()));
	}
	
	//standby prep
	public static WebElement getStandByPrepareStandbyBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getStandByPrepareStandbyBtnXpath()));
	}
	public static WebElement getStandByNodeNameTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getStandByNodeNameTextBoxXpath()));
	}
	public static WebElement getStandByNodeIdTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getStandByNodeIdTextBoxXpath()));
	}
	public static WebElement getStandyMasterBoxillaIpTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(getStandyMasterBoxillaIpTextBoxXpath()));
	}
	
	//main page
	public static WebElement getSwitchoverFormBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchoverFormBtnXpath()));
	}
	public static WebElement getSwitchoverBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchoverBtnpath()));
	}
	public static WebElement getMakeFailedStandAlone(WebDriver driver) {
		return driver.findElement(By.xpath(getMakeFailedStandAloneXpath()));
	}
	public static WebElement getDetachFailedBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getDetachFailedBtnXpath()));
	}
	public static WebElement getPrepareStandbyFailedBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getPrepareStandbyFailedBtnXpath()));
	}
	public static WebElement getMakeStandbyStandaloneDropdownBtnXpath(WebDriver driver) {
		return driver.findElement(By.xpath(getMakeStandbyStandaloneDropdownBtnXpath()));
	}
	public static WebElement getDetachStandbyDropdownBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getDetachStandbyDropdownBtnXpath()));
	}
	public static WebElement getNodeDropdownBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getNodeDropdownBtnXpath()));
	}
	public static WebElement getMakeMasterStandaloneBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getMakeMasterStandaloneBtnXpath()));
	}
	public static WebElement getClusterInfoSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(getClusterInfoSearchBoxXpath()));
	}
	public static WebElement getNodeInfoSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(getNodeInfoSearchBoxXpath()));
	}
	public static WebElement getClusterHeading(WebDriver driver) {
		return driver.findElement(By.xpath(getClusterHeadingXpath()));
	}
	public static WebElement getDissolveCluserBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getDissolveCluserBtnXpath()));
	}
	public static WebElement getAttachBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getAttachBtnXpath()));
	}
	public static WebElement getClusterInfoTab(WebDriver driver) {
		return driver.findElement(By.xpath(getClusterInfoTabXpath()));
	}
	public static WebElement getNodeInfoTab(WebDriver driver) {
		return driver.findElement(By.xpath(getNodeInfoTabXpath()));
	}
	public static WebElement getPrepareMasterBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getPrepareMasterBtnXpath()));
	}
	public static WebElement getPrepareStandByBtnXpath(WebDriver driver) {
		return driver.findElement(By.xpath(getPrepareStandByBtnXpath()));
	}
	public static WebElement getNodeInfoTableColumn(WebDriver driver, int columnIndex) {
		return driver.findElement(By.xpath("(//tr[1]//following::td)[" + columnIndex + "]"));
	}
	public static String getNodeInfoTableColumnXpath(int columnIndex) {
		return "//tr[1]//following::td[" + columnIndex + "]";
	}
	public static String getClusterInfoTableColumnXpath(int columnIndex) {
		return "//tr[@id='Clusterinfo']//following::td[" + columnIndex + "]";
	}
	public static WebElement getClusterInfoTableColumn(WebDriver driver, int columnIndex) {
		return driver.findElement(By.xpath("//tr[@id='cluster-info-row']//td[" + columnIndex + "]"));
		//return driver.findElement(By.xpath("//tr[@id='Clusterinfo']//following::td[" + columnIndex + "]"));
		
	}
	
}
