package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Switch {

	//status
	public static String switchNameThXpath = "//th[contains(.,'Switch Name')]";
	public static String addSwitchBtnXpath = "//span[contains(.,'Add Switches')]";
	public static String addSwitchSaveBtnXpath = "//div[@id='add-switch-modal']//button[@id='fav-btn-save']";
	public static String addSwitchNameTextXpath = "//div[@id='add-switch-modal']//input[@id='switch-name-input']";
	public static String addSwitchIpTextXpath = "//input[@id='switch-ip-input']";
	public static String addSwitchSuccessToastXpath = "//div[@class='toast-message']";
	public static String switchSearchBoxXpath = "//input[@type='search']";
	public static String switchTableFirstEntryIpAddressXpath = "//tr[1]//following::td[4]";
	public static String switchDropdownXpath = "//button[@id='dropdownKebab']";
	public static String switchDetailXpath = "//a[contains(.,'Details')]";
	public static String switchPingToastXpath = "//div[@class='toast-message']";
	public static String spinnerXpath = "//img[@class='rotate']";
	public static String switchPingXpath = "//a[@class='btn-ping']";
	public static String switchChangeNameXpath = "//a[normalize-space()='Change Name']";
	public static String switchNameTextboxXpath = "//div[@id='change-switch-name-modal']//input[@id='switch-name-input']";
	public static String switchChangeNameApplyBtnXpath = "//div[@id='change-switch-name-modal']//button[@id='fav-btn-save']";
	public static String switchEditNetworkXpath = "//a[contains(.,'Edit Network')]";
	public static String switchRebootXpath = "//a[normalize-space()='Reboot']";
	public static String switchUnmanageXpath = "//a[contains(.,'Unmanage')]";
	public static String switchEnableSharedModeBtnXpath = "//a[@data-original-title='Enable Shared Mode']";
	public static String switchDisableSharedModeBtnXpath = "//a[contains(.,'Disable Shared Mode')]";

	//edit network
	public static String switchEditIpTextboxXpath = "//input[@id='ip']";
	public static String switchEditNetmaskXpath = "//input[@id='netmask']";
	public static String switchEditNetworkApplyBtnXpath = "//div[@id='changeSwitchip']";
	public static String ipChangeErrorToastXpath = "//div[@class='toast-message']";
	
	//switch details
	public static String switchDetailsBackBtnXpath = "//button[contains(.,'Back')]";
	public static String switchDetailsUptimeXpath = "//p[contains(.,'Up Time')]";
	public static String switchDetailsTemperatureXpath = "//p[contains(.,'Temperature')]";
	public static String switchDetailsMacXpath = "//strong[contains(.,'MAC')]";
	
	//port details
	public static String portDetailsBackBtnXpath = "//button[contains(.,'Back')]";
	public static String portDetailsSearchTextboxXpath = "//input[@type='search']";
	public static String portDropdownBtnXpath = "//button[contains(@id,'dropdownKebab')]";
	public static String portDisableBtnXpath = "//a[contains(.,'Disable port')]";
	public static String portEnableBtnXpath = "//a[@data-original-title='Enable port']";
	public static String portDisabledImgXpath = " //img[@title='Port is disabled.']";
	public static String portIsDownImgXpath = "//img[@title='Port is down.']";
	public static String enableMRouterBtnXpth = "//a[@title='Enable MRouter']";
	public static String disableMRouterBtnXpath = "//a[@title='Disable MRouter']";
	public static String portChartImgXpath = "//img[@class='fa-chart-line']";
//	public static String chartxpath = "(//h4[@class='modal-title'])[1]";
	//h4[normalize-space()='switch1 | ethernet1/1/20']
	public static String chartxpath = "//h4[normalize-space()='switch1 | ethernet1/1/20']";
	
	//upgrades
	public static String uploadBtnXpath = "//span[contains(.,'Upload')]";
	public static String releasesTabXpath = "//div[@data-bb-tab='tab-releases']";
	public static String selectSwitchesTabXpath = "//div[@data-bb-tab='tab-switch-upgrades']";
	public static String upgradeSearchTextboxXpath = "//input[@type='search']";
	public static String upgradeFileBrowseBtnXpath = "//input[@type='file']";
	public static String upgradeFileSubmitBtnXpath = "//input[@type='submit']";
	public static String upgradeFileSuccessImgXpath = "//image[@src='/assests/success.png']";
	public static String upgradeReleaseTabXpath = "//div[@data-bb-tab='tab-switch-releases']";
	public static String upgradeReleaseTableHeadingXpath = "//th[contains(.,'Filename')]";
	public static String upgradeSwitchSearchTextboxXpath = "//input[@type='search']";
	public static String switchUpgradeStateXpath = "//table[@id='DataTables_Table_0']";
	public static String switchUpgradeCheckBoxXpath = "//input[@data-id='checkAll']";
	public static String switchUpgradeBtnXpath = "//a[@id='upgrade_switches']";
	public static String releaseDropdownBtnXpth  = "//button[@id='dropdownKebab']";
	public static String activateReleaseBtnXpath = "//a[contains(.,'Activate')]";
	public static String deleteReleaseBtnXpath = "//a[contains(.,'Delete')]";
	public static String invalidFileBackBtnXpath = "//button[contains(.,'Back')]";
	
	
	public enum Column {
		NAME, STATUS, MODEL, IP, PORTS_ONLINE, BANDWIDTH_IN, 
		BANDWIDTH_OUT, ALERTS, SHARED_MODE
	}
	public enum PORT_COLUMN {
		NAME, STATUS, BANDWIDTH_IN, BANDWIDTH_OUT, PACKETS_IN, PACKETS_OUT,
		LINE_USAGE_IN, LINE_USAGE_OUT, ERRORS,  MROUTER, MEDIA, CHART
	}
	public static String getChartxpath() {
		return chartxpath;
	}
	public static String getPortChartImgXpath() {
		return portChartImgXpath;
	}
	public static String getInvalidFileBackBtnXpath() {
		return invalidFileBackBtnXpath;
	}
	public static String getAddSwitchBtnXpath() {
		return addSwitchBtnXpath;
	}
	public static String getDisableMRouterBtnXpath() {
		return disableMRouterBtnXpath;
	}
	public static String getEnableMRouterBtnXpth() {
		return enableMRouterBtnXpth;
	}
	public static String getIpChangeErrorToastXpath() {
		return ipChangeErrorToastXpath;
	}
	public static String getSwitchDetailsMacXpath() {
		return switchDetailsMacXpath;
	}
	public static String getSwitchDetailsTemperatureXpath() {
		return switchDetailsTemperatureXpath;
	}
	public static String getDeleteReleaseBtnXpath() {
		return deleteReleaseBtnXpath;
	}
	public static String getActivateReleaseBtnXpath() {
		return activateReleaseBtnXpath;
	}
	public static String getReleaseDropdownBtnXpth() {
		return releaseDropdownBtnXpth;
	}
	public static String getSwitchDisableSharedModeBtnXpath() {
		return switchDisableSharedModeBtnXpath;
	}
	public static String getSwitchEnableSharedModeBtnXpath() {
		return switchEnableSharedModeBtnXpath;
	}
	public static String switchDetailsUptimeXpath() {
		return switchDetailsUptimeXpath;
	}
	public static String getSwitchUpgradeBtnXpath() {
		return switchUpgradeBtnXpath;
	}
	public static String getSwitchUpgradeCheckBoxXpath() {
		return switchUpgradeCheckBoxXpath;
	}
	public static String getSwitchUpgradeStateXpath() {
		return switchUpgradeStateXpath;
	}
	public static String getUpgradeSwitchSearchTextboxXpath() {
		return upgradeSwitchSearchTextboxXpath;
	}
	public static String getUpgradeActiveXpath(String rowNumber) {
		return "//tr[" + rowNumber + "]//following::td[" + "3" + "]";
	}
	public static String getUpgradeShortReleaseVersionXpath(String rowNumber) {
		return "//tr[" + rowNumber + "]//following::td[" + "2" + "]";
	}
	public static String getUpgradeReleaseVersionXpath(String rowNumber) {
		return "//tr[" + rowNumber + "]//following::td[" + "1" + "]";
	}
	public static String getUpgradeReleaseTableHeadingXpath() {
		return upgradeReleaseTableHeadingXpath;
	}
	public static String getUpgradeReleaseTabXpath() {
		return upgradeReleaseTabXpath;
	}
	public static String getUpgradeFileSuccessImgXpath() {
		return upgradeFileSuccessImgXpath;
	}
	public static String getUpgradeFileSubmitBtnXpath() {
		return upgradeFileSubmitBtnXpath;
	}
	
	public static String getUpgradeFileBrowseBtnXpath() {
		return upgradeFileBrowseBtnXpath;
	}
	public static String getUpgradeSearchTextboxXpath() {
		return upgradeSearchTextboxXpath;
	}
	public static String getSelectSwitchesTabXpath() {
		return selectSwitchesTabXpath;
	}
	public static String getReleasesTabXpath() {
		return releasesTabXpath;
	}
	public static String getPortDisabledImgXpath() {
		return portDisabledImgXpath;
	}
	public static String getPortEnableBtnXpath() {
		return portEnableBtnXpath;
	}
	public static String getPortDisableBtnXpath() {
		return portDisableBtnXpath;
	}
	public static String getPortDropdownBtnXpath() {
		return portDropdownBtnXpath;
	}
	public static String getPortIsDownImgXpath () {
		return portIsDownImgXpath;
	}
	public static String getPortDetailsSearchTextboxXpath() {
		return portDetailsSearchTextboxXpath;
	}
	
	public static String getPortDetailsBackBtnXpath() {
		return portDetailsBackBtnXpath;
	}
	
	public static String getSwitchNameLinkXpath(String switchName) {
		return "//a[contains(.,'" + switchName + "')]";
	}
	
	public static String getSwitchUnmanageXpath() {
		return switchUnmanageXpath;
	}
	public static String getSwitchRebootXpath() {
		return switchRebootXpath;
	}
	public static String getSwitchEditNetworkApplyBtnXpath() {
		return switchEditNetworkApplyBtnXpath;
	}
	public static String getSwitchEditNetmaskXpath() {
		return switchEditNetmaskXpath;
	}
	public static String getSwitchEditIpTextboxXpath() {
		return switchEditIpTextboxXpath;
	}
	public static String getSwitchEditNetworkXpath() {
		return switchEditNetworkXpath;
	}
	public static String getswitchChangeNameApplyBtnXpath() {
		return switchChangeNameApplyBtnXpath;
	}
	public static String getSwitchNameTextboxXpath() {
		return switchNameTextboxXpath;
	}
	public static String getSwitchChangeNameXpath() {
		return switchChangeNameXpath;
	}
	public static String getSwitchPingXpath() {
		return switchPingXpath;
	}
	public static String getSwitchDetailsBackBtnXpath() {
		return switchDetailsBackBtnXpath;
	}
	public static String getSwitchDetailXpath () {
		return switchDetailXpath;
	}
	
	public static String getSwitchDropdownXpath () {
		return switchDropdownXpath;
	}
	
	//elements
	public static WebElement getChart(WebDriver driver) {
		return driver.findElement(By.xpath(getChartxpath()));
	}
	public static WebElement getPortChartImg(WebDriver driver) {
		return driver.findElement(By.xpath(getPortChartImgXpath()));
	}
	public static WebElement getInvalidFileBackBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getInvalidFileBackBtnXpath()));
	}
	public static WebElement getAddSwitchBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getAddSwitchBtnXpath()));
	}
	public static WebElement getDisableMRouterBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getDisableMRouterBtnXpath()));
	}
	public static WebElement getEnableMRouterBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getEnableMRouterBtnXpth()));
	}
	public static WebElement getIpChangeErrorToast(WebDriver driver) {
		return driver.findElement(By.xpath(getIpChangeErrorToastXpath()));
	}
	public static WebElement getSwitchDetailsMac(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDetailsMacXpath()));
	}
	public static WebElement getSwitchDetailsTemperature(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDetailsTemperatureXpath()));
	}
	public static WebElement getDeleteReleaseBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getDeleteReleaseBtnXpath()));
	}
	public static WebElement getActivateReleaseBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getActivateReleaseBtnXpath()));
	}
	public static WebElement getReleaseDropdownBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getReleaseDropdownBtnXpth()));
	}
	public static WebElement getSwitchDisableSharedModeBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDisableSharedModeBtnXpath()));
	}
	public static WebElement getSwitchEnableSharedModeBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchEnableSharedModeBtnXpath()));
	}
	public static WebElement switchDetailsUptime(WebDriver driver) {
		return driver.findElement(By.xpath(switchDetailsUptimeXpath()));
	}
	public static WebElement getSwitchUpgradeBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchUpgradeBtnXpath()));
	}
	public static WebElement getSwitchUpgradeCheckBox(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchUpgradeCheckBoxXpath()));
	}
	public static WebElement getSwitchUpgradeState(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchUpgradeStateXpath()));
	}
	public static WebElement getUpgradeSwitchSearchTextbox(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeSwitchSearchTextboxXpath()));
	}
	public static WebElement getUpgradeActive(WebDriver driver, String rowNumber) {
		return driver.findElement(By.xpath(getUpgradeActiveXpath(rowNumber)));
	}
	public static WebElement getUpgradeShortReleaseVersion(WebDriver driver, String rowNumber) {
		return driver.findElement(By.xpath(getUpgradeShortReleaseVersionXpath(rowNumber)));
	}
	public static WebElement getUpgradeReleaseVersion(WebDriver driver, String rowNumber) {
		return driver.findElement(By.xpath(getUpgradeReleaseVersionXpath(rowNumber)));
	}
	public static WebElement getUpgradeReleaseTableHeading(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeReleaseTableHeadingXpath()));
	}
	public static WebElement getUpgradeReleaseTab(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeReleaseTabXpath()));
	}
	public static WebElement getUpgradeFileSuccessImg(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeFileSuccessImgXpath()));
	}
	public static WebElement getUpgradeFileSubmitBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeFileSubmitBtnXpath()));
	}
	public static WebElement getUpgradeFileBrowseBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeFileBrowseBtnXpath()));
	}
	public static WebElement getUpgradeSearchTextbox(WebDriver driver) {
		return driver.findElement(By.xpath(getUpgradeSearchTextboxXpath()));
	}
	public static WebElement getSelectSwitchesTab(WebDriver driver) {
		return driver.findElement(By.xpath(getSelectSwitchesTabXpath()));
	}
	public static WebElement getReleasesTab(WebDriver driver) {
		return driver.findElement(By.xpath(getReleasesTabXpath()));
	}
	public static WebElement getPortIsDownImg(WebDriver driver) {
		return driver.findElement(By.xpath(getPortIsDownImgXpath()));
	}
	public static WebElement getPortDisabledImg(WebDriver driver) {
		return driver.findElement(By.xpath(getPortDisabledImgXpath()));
	}
	public static WebElement getPortEnableBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getPortEnableBtnXpath()));
	}
	public static WebElement getPortDisableBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getPortDisableBtnXpath()));
	}
	public static WebElement getPortDropdownBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getPortDropdownBtnXpath()));
	}
	public static WebElement getPortDetailsSearchTextbox(WebDriver driver) {
		return driver.findElement(By.xpath(getPortDetailsSearchTextboxXpath()));
	}
	public static WebElement getPortDetailsBackBtn(WebDriver driver) { 
		return driver.findElement(By.xpath(getPortDetailsBackBtnXpath()));
	}
	public static WebElement getSwitchNameLink(WebDriver driver, String switchName) {
		return driver.findElement(By.xpath(getSwitchNameLinkXpath(switchName)));
	}
	public static WebElement getSwitchUnmanage(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchUnmanageXpath()));
	}
	public static WebElement getSwitchReboot(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchRebootXpath()));
	}
	public static WebElement getSwitchEditNetworkApplyBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchEditNetworkApplyBtnXpath()));
	}
	public static WebElement getSwitchEditNetmask(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchEditNetmaskXpath()));
	}
	public static WebElement getSwitchEditIpTextbox(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchEditIpTextboxXpath()));
	}
	public static WebElement getSwitchEditNetwork(WebDriver driver) {
		return driver.findElement(By.xpath(switchEditNetworkXpath));
	}
	
	public static WebElement getSwitchChangeNameApplyBtn(WebDriver driver) {
		return driver.findElement(By.xpath(switchChangeNameApplyBtnXpath));
	}
	public static WebElement getSwitchNameTextbox(WebDriver driver) {
		return driver.findElement(By.xpath(switchNameTextboxXpath));
	}
	public static WebElement getSwitchChangeName(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchChangeNameXpath()));
	}
	public static WebElement getToast(WebDriver driver) {
		return driver.findElement(By.xpath(switchPingToastXpath));
	}
	public static WebElement getSpinner(WebDriver driver) {
		return driver.findElement(By.xpath(spinnerXpath));
	}
	public static WebElement getSwitchPing(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchPingXpath()));
	}
	public static WebElement getSwitchDetailBackBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDetailsBackBtnXpath()));
	}
	public static WebElement getSwitchDetail(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDetailXpath()));
	}
	
	public static WebElement getSwitchDropdown(WebDriver driver) {
		return driver.findElement(By.xpath(getSwitchDropdownXpath()));
	}
	
	public static String getSwitchTableFirstEntryIpAddressXpath(String columnIndex) {
		return "//tr[1]//following::td[" + columnIndex + "]"; 
	}
	
	public static WebElement getSwitchTableColumn(WebDriver driver, String column) {
		return driver.findElement(By.xpath(getSwitchTableFirstEntryIpAddressXpath(column)));
	}
	
	public static String getPortTableFirstEntryIpAddressXpath(String columnIndex) {
		return "//tr[1]//following::td[" + columnIndex + "]"; 
	}
	
	public static WebElement getPortTableColumn(WebDriver driver, String column) {
		return driver.findElement(By.xpath(getPortTableFirstEntryIpAddressXpath(column)));
	}
	
}
