package objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

/**
 * Holds all the objects for Boxilla - Connections page
 * 
 * @author Boxilla
 *
 */
public class Connections {

	public enum VIA {
		TX, VM, POOL, BROKER, HORIZON, PAIR
	}

	public enum Orientation {
		H12, H21, V12, V21
	}

	private static WebElement element = null;

	// viewer xpaths
	public static String getPropertyTemplate(VIA via) {
		switch (via) {
		case TX:
			return "//select[@id='property-templates-via-tx']";
		case VM:
			return "//select[@id='property-templates-vm']";
		case POOL:
			return "//select[@id='property-templates-vm-pool']";
		case BROKER:
			return "//select[@id='property-templates-broker']";
		case HORIZON:
			return "//select[@id='property-templates-vm-horizon-view']";
		case PAIR:
			return "//div[@id='complete']//label[6]";
		default:
			return null;
		}
	}

	// connetion options icons
	private static String iconExtDesktopDisabled = "//img[@src='/assets/icons/monitor_off.svg']";
	private static String iconExtDesktopEnabled = "//img[@src='/assets/icons/monitor_on.svg']";
	private static String iconAudioDisabled = "//img[@src='/assets/icons/audio_off.svg']";
	private static String iconAudioEnabled = "//img[@src='/assets/icons/audio_on.svg']";
	private static String iconUsbDisabled = "//img[@src='/assets/icons/usbr_off.svg']";
	private static String iconUsbEnabled = "//img[@src='/assets/icons/usbr_on.svg']";
	private static String iconPersistentDisabled = "//img[@src='/assets/icons/persistent_off.svg']";
	private static String iconPersistentEnabled = "//img[@src='/assets/icons/persistent_on.svg']";
	private static String iconViewOnlyDisabled = "//i[contains(@class,'fa fa-eye')]";
	private static String iconNlaEnabled = "//img[@src='/assets/icons/nla_on.svg']";
	private static String iconNlaDisabled = "//img[@src='/assets/icons/nla_off.svg']";
	private static String connectionNameFromConManageTable = "//tbody[@id='table-connection']//td[1]";
	private static String connectionTypeFromConManageTable = "//tbody[@id='table-connection']//td[3]";
	private static String connectionViaFromConManageTable = "//tbody[@id='table-connection']//td[4]";

	private static String editConnectionIp1 = "//input[@id='ip-address-1']";
	private static String editConnectionIp2 = "//input[@id='ip-address-2']";
	private static String editConnectionModalNextBtn = "//button[@class='btn btn-primary wizard-pf-next']";
	private static String connectionDropdownEdit = "//a[@class='connection-edit']";
	private static String connectionDropdown = "//button[@id='dropdownKebab']//img";
	private static String pairedTarget1 = "//input[@id='radio-target-1']";
	private static String pairedTarget2 = "//input[@id='radio-target-2']";
	private static String addConnectionModal = "//div[@id='complete']";
	private static String audioIp1 = "//input[@id='audio-ip-1']";
	private static String txPairButton = "//input[@id='radio-tx-pair']";
	private static String pairedIpAddress1 = "//input[@id='ip-address-1']";
	private static String pairedIpAddress2 = "//input[@id='ip-address-2']";
	private static String originalDisplay1Orientation = "//div[@class='portlet-header ui-widget-header ui-corner-all'][contains(text(),'DISPLAY 1')]";
	private static String originalTemplateDisplay1Orientation = "//div[@class='portlet-header'][contains(text(),'DISPLAY 1')]";
	private static String originalDisplay2Orientation = "//div[@id='2']//div[@class='portlet-header ui-widget-header ui-corner-all'][contains(text(),'DISPLAY 2')]";
	private static String originalTemplateDisplay2Orientation = "//div[@class='portlet-header'][contains(text(),'DISPLAY 2')]";
	private static String orientationRightColumn = "//div[@id='right_column']";
	private static String templateOrientationRightColumn = "//div[@id='property_right_column']";

	private static String templateTxPairButton = "//div[@id='property-form']//label[6]";
	private static String templatePairedTarget1 = "//input[@id='property-radio-target-1']";
	private static String templatePairedTarget2 = "//input[@id='property-radio-target-2']";
	private static String templateAudioIp1 = "//input[@id='property-audio-ip-1']";

	private static String competeConnectionModal = "//div[@id='complete']";
	private static String vmHorizonTemplateButton = "//input[@id='radio-property-vm-horizon-view']";
	private static String vmHorizonViewButton = "//input[@id='radio-vmhorizonview']";
	private static String managePresetsBtnXpath = "//button[@data-target='#managePresets']";
	private static String makeConnectionBtnXpath = "//button[contains(.,'Make Connection')]";
	private static String createCustomBtnXpath = "(.//div[@class='modal-footer']//button)[6]"; // button[contains(@id,'create-custom-preset')]";
	private static String createPresetSearchBoxXpath = "//div[@id='addPresetSources']//following::input[@id='source-name']";
	private static String searchSourcesXpath = "//div[@id='addSources']//following::input[@id='source-name']";
	private static String searchAvailablePresetsXpath = "//input[@id='preset-name']";
	private static String activateSourcesDestinationsXpath = "//div[@id='addSourceDestinations']//button[@id='activate-selected']";
	private static String activeSourcesSourceXpath = "//div[@id='addSource' or @id='create-connection-modal']//button[@type='button' and contains(., 'Activate Selected')]";
	private static String privateConnectionDropDownXpath = ".//input[@id='destSelect']";
	private static String privateConnectionsActivateBtnXpath = "//button[@class='btn btn-primary saveBtnDestinations']";
//	private static String sharedDestinationSearchBoxXpath = "//div[@id='addSourceDestinations']//following::input[@id='destination-name']";
	private static String sharedDestinationSearchBoxXpath = "//div[@id='add-shared-destination-modal']//input[@id='source-name']";

	//private static String activateSelectedDestinationXpath = "//div[@id='addSourceDestinations']//button[@id='activate-selected']";
	private static String activateSelectedDestinationXpath = "//div[@id='add-shared-destination-modal']//button[@type='button'][normalize-space()='Activate Selected']";

	private static String snapshotBtnXpath = "//button[@id='save-snapshot']";
	private static String snapshotNameInputXpath = "//input[@id='name']";
	private static String snapshotTypeDropdownXpath = "//select[@id='type']";
//	private static String saveSnapshotBtnXpath = "//button[@class='btn btn-primary save-preset-snapshot']";
	private static String saveSnapshotBtnXpath = "//button[@id='fav-btn-save']";
	// private static String presetSourcesSearchBoxXpath =
	// "//div[@id='addPresetSources']//following::input[@id='source-name']";
	private static String presetSourcesSearchBoxXpath = "//div[@id='editPreset']//input[@id='source-name']";

	// private static String presetSelectSourceNextBtnXpath = "//button[@class='btn
	// btn-primary wizard-pf-next']";
	private static String presetSelectSourceNextBtnXpath = "//button[normalize-space()='Next']";

	// private static String presetSelectDestinationSearchBoxXpath =
	// "//div[@id='destination-container-skeleton']//following::input[@id='destination-name']";#
	private static String presetSelectDestinationSearchBoxXpath = "//div[@class='col-md-6 col-sm-12 search-filter']//div//input[@id='destination-name']";

	private static String presetDestinationNextBtnXpath = "//button[normalize-space()='Next']";
	private static String createPresetNameTextBoxXpath = "//form[@id='preset-details']//following::input[@id='name']";
	private static String createPresetTypeDowndownXpath = "//select[@id='type']";
	// private static String createPresetCompleteBtnXpath = "//button[@class='btn
	// btn-primary wizard-pf-finish']";
	private static String createPresetCompleteBtnXpath = "//button[normalize-space()='Complete']";
	private static String nonActiveSourceXpath = "//div[@id='non-active-source']";
	private static String nonActiveDestinationXpath = "//div[@id='non-active-destination']";
	//private static String editPresetEditSourcesNextBtnXpath = "//button[@class='btn btn-primary wizard-pf-next']";
	private static String editPresetEditSourcesNextBtnXpath = "//button[normalize-space()='Next']";

	//private static String editPresetEditDestinationsNextBtnXpath = "//button[@class='btn btn-primary wizard-pf-next']";
	private static String editPresetEditDestinationsNextBtnXpath = "//button[normalize-space()='Next']";

	private static String editPresetUpdateBtnXpath = "//button[@class='btn btn-primary wizard-pf-finish']";
	private static String createPresetCloseModalXpath = "//div[@id='managePresets']//following::button[@class='close']";
	public static String connectionName = ".//*[@id='connection-name']";
	public static String btnAddConnection = ".//*[@id='new-connection']";
	public static String btnAddConnectionTemplate = ".//*[@id='new-property']";
	public static String templateName = ".//*[@id='property-name']";
	public static String hostName = ".//*[@id='host']";
	public static String connectionViaTX = "((.//div[@class='btn-group'])[1]/label)[1]";
	public static String connectionViaVM = "((.//div[@class='btn-group'])[1]/label)[2]";
	public static String connectionViaVMPool = "((.//div[@class='btn-group'])[1]/label)[3]";
	public static String connectionViaBroker = "((.//div[@class='btn-group'])[1]/label)[4]";
	public static String connectionViaPair = "//div[@id='complete']//label[6]";
	public static String btnCancel = "//button[@type='button' and contains(., 'Cancel')]";
	public static String btnNext = "//button[@type='button' and contains(., 'Next')]";
	public static String btnSave = "//button[@class='btn btn-primary wizard-pf-save']";
//	public static String connectionTypePrivate = ".//*[@id='input-c-type']/div/div/label[1]";
	public static String connectionTypePrivate = "(//label[@class='btn btn-primary active'][normalize-space()='Private'])[1]";
	public static String connectionTypeShared = ".//*[@id='input-c-type']/div/div/label[2]";
	public static String connectionTable = ".//*[@id='conntable']";
	public static String searchTextbox = ".//*[@type='search']";
	public static String usernameTextbox = ".//*[@id='username']";
	public static String passwordTextbox = ".//*[@id='password']";
	public static String domainTextbox = ".//*[@id='domain']";
	public static String loadBalanceInfo = ".//*[@id='load-balance-info']";
	public static String addTemplateTX = "//input[@id='radio-property-via-tx']";
	public static String addTemplateVM = "//input[@id='radio-property-vm']";
	public static String addTemplateVMPool = "//input[@id='radio-property-vm-pool']";
	public static String addTemplateBroker = "//input[@id='radio-property-broker']";
	public static String addTemplatePrivate = ".//*[@id='property-c-type']/div/div/label[1]";
	public static String addTemplateShared = ".//*[@id='property-c-type']/div/div/label[2]";
	public static String addTemplateSavebtn = ".//*[@id='btn-property-save']";
	public static String addTemplateDomainTextbox = ".//*[@id='property-domain']";
	public static String addTemplateLoadBalanceInfo = ".//*[@id='property-load-balance-info']";
	public static String btnDeleteConnectionTemplate = ".//*[@id='delete-property']";
	public static String nonSelectedList = ".//*[@id='bootstrap-duallistbox-nonselected-list_']";
	public static String moveSelectedBtn = "(//button[@title='Move selected'])[1]";
	public static String moveAllBtn = "(//button[@title='Move all'])[1]";
	public static String removeSelectedBtn = "(//button[@title='Remove selected'])[1]";
	public static String removeAllBtn = "(//button[@title='Remove all'])[1]";
	public static String btnDeleteTemplates = ".//*[@id='btn-delete-connection-properties']";
	public static String connectionTemplateTX = ".//*[@id='property-templates-via-tx']";
	public static String connectionTemplatePair = "//select[@id='property-templates-tx-pair']";
	public static String connectionTemplateHorizon = "//select[@id='property-templates-vm-horizon-view']";
	public static String connectionTemplateBroker = ".//*[@id='property-templates-broker']";
	public static String connectionTemplateVM = ".//*[@id='property-templates-vm']";
	public static String connectionTemplateVMPool = ".//*[@id='property-templates-vm-pool']";
	public static String totalAvailableConnection = ".//*[@id='conntable_info']/b[3]";
	public static String totalAvailableConnection2 = ".//*[@id='conntable_info']/b[1]";
	public static String newGroupBtn = ".//*[@id='new-group']";
	public static String groupName = ".//*[@id='group-name']";
	public static String groupDescription = ".//*[@id='group-description']";
	public static String groupAddBtn = ".//*[@id='new-group-save']";
	public static String connectionGroupTable = ".//*[@data-header='All Connections']";
//	public static String totalAvailableGroups = ".//*[@id='DataTables_Table_0_info']/b[3]";
	public static String totalAvailableGroups = ".//*[@id='connection-groups-table_info']/b[3]";
//	public static String totalAvailableGroups2 = ".//*[@id='DataTables_Table_0_info']/b[1]";
	public static String totalAvailableGroups2 = ".//*[@id='connection-groups-table_info']/b[1]";
	public static String breadCrumb = "(.//*[@id='dropdownKebab'])[1]";
	public static String breadCrumbGroup2 = "(.//*[@id='dropdownKebab'])[2]";
	public static String groupManageConnection = ".//*[@class='manage-group']";
	public static String editGroup = ".//*[@class='edit-group']";
	public static String saveBtnGroupConnections = ".//*[@id='btn-save-group-connections']";
	public static String connectionListFilterBox = "(.//*[@class='filter form-control'])[1]";
	public static String belongsToGroupFilterBox = "(.//*[@class='filter form-control'])[2]";
	public static String nonSelectedActiveConnectionList = "(.//*[@id='bootstrap-duallistbox-nonselected-list_'])";
	public static String selectedActiveConnectionList = "(.//*[@id='bootstrap-duallistbox-selected-list_'])";
	public static String groupUpdateBtn = ".//*[@id='group-update']";
	public static String groupDelete = ".//*[@class='dropdown dropdown-kebab-pf open']//*[@class='delete-group']";
	public static String dissolveBtn = ".//*[@class='dropdown dropdown-kebab-pf open']//*[@class='dissolve-group']";
	public static String templateToastMessage = "//div[@class='toast-message']";
	private static String zone = "//select[@id='zones-dropdown']";
	private static String deleteConnection = "//a[@class='connection-delete']";
	private static String losslessBtn = "//input[@id='radio-lossless']";
	private static String optimizedBtn = "//input[@id='radio-optimized']";
	// bonded connections
	private static String bondedTab = "//div[@id='bonded-connections-tab-button']";
	private static String viaBonded = "//input[@id='radio-bonded']";
	private static String createBondedConnectionNameTextBox = "//input[@id='connection-name']";
	private static String createBondedConnectionExistingBondDropdown = "//select[@id='existing-bonded-connections']";
	private static String addConnectionToBondButton = "//button[@id='add-bonded-pair-btn']";
	private static String createBondedConSelectZone = "//select[@id='zones-dropdown']";
	private static String bondedConSummaryName = "//p[@id='reviewBondedName']";
	private static String bondedConTableHead = "//th[contains(text(),'Bonded connections')]";
	private static String bondedConSearchBox = "//input[@aria-controls='bondedtable']";
	private static String bondedConnectionNameFromTable = "//tbody[@id='table-bonded-connections']//td[2]";
	private static String bondedConnectionCountFromTable = "//tbody[@id='table-bonded-connections']//td[3]";
	private static String bondedConnectionZoneFromTable = "//tbody[@id='table-bonded-connections']//td[4]";
	private static String bondedConnectionDrillDown = "//tbody[@id='table-bonded-connections']//td[1]";
	private static String bondedConnectionDrillDownHeading = "//h3[contains(text(),'Bonded connections')]";
	private static String bondedConnectionGroupDropdown = "//tbody[@id='table-bonded-connections']//td[5]//button";
	private static String editBondedConnectionGroupBtn = "//a[@class='bonded-connection-edit']";
	private static String deleteBondedConnectionGroupBtn = "//a[@class='bonded-connection-delete']";
	private static String viewerToggleBondedBtnOn = "//span[contains(text(),'OFF')]";
	private static String viewerToggleBondedBtnOff = "//span[contains(text(),'ON')]";
	private static String activeBondedConnectionsHeading = "//span[contains(text(),'Bonded Active Connections')]";
	private static String expandBondedConnectionsViewer = "//span[contains(text(),'Expand')]";
    private static String searchSources="//div[@class='col-md-3']//input[@id='source-name']";
    private static String searchDestinations="//div[@class='col-md-3']//input[@id='destination-name']";
	public static String getTerminateBondedConnection(String connectionName) {
		return "//span[contains(text(),'" + connectionName + "')]//following::span[@class='fa fa-times']";
	}

	public static String getExtendedBondedConnection(String conName, String destination) {
		return "//div[@data-original-title='" + conName
				+ "']//following::div[@class='active-destination']//following::span[contains(text(),'" + destination
				+ "')]";
	}
	
	public static WebElement getsearchSources(WebDriver driver) 
	{
		return driver.findElement(By.xpath(searchSources));
	}
	public static WebElement getsearchDestinations(WebDriver driver) 
	{
		return driver.findElement(By.xpath(searchDestinations));
	}

	public static String getExpandBondedConnectionsViewer() {
		return expandBondedConnectionsViewer;
	}

	public static String getActiveBondedConnectionsHeading() {
		return activeBondedConnectionsHeading;
	}

	public static String getActiveBondedPairViewer(String numberOfPairs) {
		return "//span[contains(text(),'Pairs: " + numberOfPairs + "')]";
	}

	public static String getActiveBondedConnectionViewer(String connectionName) {
		return "//span[contains(text(),'" + connectionName + "')]";
	}

	public static String getViewerToggleBondedBtnOff() {
		return viewerToggleBondedBtnOff;
	}

	public static String getViewerToggleBondedBtnOn() {
		return viewerToggleBondedBtnOn;
	}

	public static String getMoveConnectionUpBtn(String index) {
		return "//button[@id='shift-up-" + index + "']";
	}

	public static String getMoveConnectionDownBtn(String index) {
		return "//button[@id='shift-down-" + index + "']";
	}

	public static String getDeleteConnectionFromBond(String index) {
		return "//button[@id='delete-bonded-pair-" + index + "']";
	}

	public static String getDeleteBondedConnectionGroupBtn() {
		return deleteBondedConnectionGroupBtn;
	}

	public static String getEditBondedConnectionGroupBtn() {
		return editBondedConnectionGroupBtn;
	}

	public static String getBondedConnectionGroupDropdown() {
		return bondedConnectionGroupDropdown;
	}

	public static String getBondedConnectionDrillDownHeading() {
		return bondedConnectionDrillDownHeading;
	}

	public static String getConnectionFromBonded(String groupName, int number) {
		return "//li[@id='" + number + "-" + groupName + "']";
	}

	public static String getBondedConnectionDrillDown() {
		return bondedConnectionDrillDown;
	}

	public static String getBondedConnectionZoneFromTable() {
		return bondedConnectionZoneFromTable;
	}

	public static String getBondedConnectionCountFromTable() {
		return bondedConnectionCountFromTable;
	}

	public static String getBondedConnectionNameFromTable() {
		return bondedConnectionNameFromTable;
	}

	public static String getBondedConSearchBox() {
		return bondedConSearchBox;
	}

	public static String getBondedConTableHead() {
		return bondedConTableHead;
	}

	public static String getBondedConSummaryConNames(String conName) {
		return "//ul[@id='secondary-receivers-review']//following::li[contains(text(),'" + conName + "')]";
	}

	public static String getBondedConSummaryName() {
		return bondedConSummaryName;
	}

	public static String getAddConnectionToBondTextBox(int number) {
		return "//input[@id='connection-input-" + number + "']";
	}

	public static String getCreateBondedConSelectZone() {
		return createBondedConSelectZone;
	}

	public static String getAddConnectionToBondButton() {
		return addConnectionToBondButton;
	}

	public static String getCreateBondedConnectionExistingBondDropdown() {
		return createBondedConnectionExistingBondDropdown;
	}

	public static String getCreateBondedConnectionNameTextBox() {
		return createBondedConnectionNameTextBox;
	}

	public static String getViaBonded() {
		return viaBonded;
	}

	public static String getBondedTab() {
		return bondedTab;
	}

	public static String getLosslessBtn() {
		return losslessBtn;
	}

	public static String getDeleteConnection() {
		return deleteConnection;
	}

	public static String getOptimizedMode() {
		return optimizedBtn;
	}

	public static String getZone() {
		return zone;
	}

	public static String getConnectionViaFromConManageTable() {
		return connectionViaFromConManageTable;
	}

	public static String getConnectionTypeFromConManageTable() {
		return connectionTypeFromConManageTable;
	}

	public static String getConnectionNameFromConManageTable() {
		return connectionNameFromConManageTable;
	}

	public static String getIconNlaDisabled() {
		return iconNlaDisabled;
	}

	public static String getIconNlaEnabled() {
		return iconNlaEnabled;
	}

	public static String getIconPersistentEnabled() {
		return iconPersistentEnabled;
	}

	public static String getIconUsbEnabled() {
		return iconUsbEnabled;
	}

	public static String getIconAudioEnabled() {
		return iconAudioEnabled;
	}

	public static String getIconExtDesktopEnabled() {
		return iconExtDesktopEnabled;
	}

	public static String getIconViewOnlyDisabled() {
		return iconViewOnlyDisabled;
	}

	public static String getIconPersistentDisabled() {
		return iconPersistentDisabled;
	}

	public static String getIconUsbDisabled() {
		return iconUsbDisabled;
	}

	public static String getIconAudioDisabled() {
		return iconAudioDisabled;
	}

	public static String getIconExtDesktopDisabled() {
		return iconExtDesktopDisabled;
	}

	public static String getEditConnectionIp1() {
		return editConnectionIp1;
	}

	public static String getEditConnectionIp2() {
		return editConnectionIp2;
	}

	public static String getEditModalNextBtn() {
		return editConnectionModalNextBtn;
	}

	public static String getConnectionDropdownEdit() {
		return connectionDropdownEdit;
	}

	public static String getConnectionDropdown() {
		return connectionDropdown;
	}

	public static String getTemplateOrientationRightColumn() {
		return templateOrientationRightColumn;
	}

	public static String getOriginalTemplateDisplay2Orientation() {
		return originalTemplateDisplay2Orientation;
	}

	public static String getOriginalTemplateDisplay1Orientation() {
		return originalTemplateDisplay1Orientation;
	}

	public static String getOrientationRightColumn() {
		return orientationRightColumn;
	}

	public static String getOriginalDisplay1Orientation() {
		return originalDisplay1Orientation;
	}

	public static String getOriginalDisplay2Orientation() {
		return originalDisplay2Orientation;
	}

	public static String getConnectionTemplatePair() {
		return connectionTemplatePair;
	}

	public static String getTemplateAudioIp1() {
		return templateAudioIp1;
	}

	public static String getTemplatePairedTarget2() {
		return templatePairedTarget2;
	}

	public static String getTemplatePairedTarget1() {
		return templatePairedTarget1;
	}

	public static String getTemplateTxPairButton() {
		return templateTxPairButton;
	}

	public static String getCompeteConnectionModal() {
		return competeConnectionModal;
	}

	public static String getAudioIp1() {
		return audioIp1;
	}

	public static String getConnectionViaPair() {
		return connectionViaPair;
	}

	public static String getAddConnectionModal() {
		return addConnectionModal;
	}

	public static String getPairedTarget1() {
		return pairedTarget1;
	}

	public static String getPairedTarget2() {
		return pairedTarget2;
	}

	public static String getTxPairButton() {
		return txPairButton;
	}

	public static String getPairedIpAddress1() {
		return pairedIpAddress1;
	}

	public static String getPairedIpAddress2() {
		return pairedIpAddress2;
	}

	public static WebElement getVmHorizonTemplate(WebDriver driver) {
		return driver.findElement(By.xpath(getVmHorizonTemplate()));
	}

	public static String getVmHorizonTemplate() {
		return vmHorizonTemplateButton;
	}

	public static String getVmHorizonViewBtn() {
		return vmHorizonViewButton;
	}

	public static WebElement getVmHorizonViewBtn(WebDriver driver) {
		return driver.findElement(By.xpath(getVmHorizonViewBtn()));
	}

	public static WebElement connectionTemplateDropdown(WebDriver driver, String type) {
		if (type.equals("tx")) {
			return driver.findElement(By.xpath("//select[@id='property-templates-via-tx']"));
		} else if (type.equals("vm")) {
			return driver.findElement(By.xpath("//select[@id='property-templates-vm']"));
		} else if (type.equals("broker")) {
			return driver.findElement(By.xpath("//select[@id='property-templates-broker']"));
		} else if (type.equals("vmpool")) {
			return driver.findElement(By.xpath("//select[@id='property-templates-vm-pool']"));
		} else if (type.equals("horizon")) {
			return driver.findElement(By.xpath("//select[@id='property-templates-vm-horizon-view']"));
		} else {
			return null;
		}
	}

	public static WebElement createPresetCloseModal(WebDriver driver) {
		return driver.findElement(By.xpath(createPresetCloseModalXpath));
	}

	// location is the location in the list. 1 = first and so on
	public static WebElement managePresetsListItem(WebDriver driver, String location) {
		return driver.findElement(By.xpath("//div[@id='" + location + "']"));
	}

	public static WebElement numberOfOnlineReceivers(WebDriver driver, String number) {
		return driver.findElement(By.xpath("//p[normalize-space() = 'Online: " + number + "']"));
	}

	public static WebElement numberOfActiveConnections(WebDriver driver, String number) {
		return driver.findElement(By.xpath("//p[normalize-space() = 'Active Connections: " + number + "']"));
	}

	public static WebElement numberOfInActiveReceiver(WebDriver driver, String number) {
		return driver.findElement(By.xpath("//p[text() = 'Inactive Receivers: " + number + "']"));
	}

	public static WebElement sourceContextMenuAudio(WebDriver driver, boolean isOn) {
		if (isOn) {
			return driver.findElement(By.xpath("//td[text()='Audio']//following::span[@class='fa fa-toggle-on']"));
		} else {
			return driver.findElement(By.xpath("//td[text()='Audio']//following::span[@class='fa fa-toggle-off']"));
		}
	}

	public static WebElement sourceContextMenuUsb(WebDriver driver, boolean isOn) {
		if (isOn) {
			return driver.findElement(By.xpath("//td[text()='vUSB']//following::span[@class='fa fa-toggle-off']"));
		} else {
			return driver.findElement(By.xpath("//td[text()='vUSB']//following::span[@class='fa fa-toggle-off']"));
		}
	}

	public static WebElement saveSnapshotBtn(WebDriver driver) {
		return driver.findElement(By.xpath(saveSnapshotBtnXpath));
	}

	public static WebElement editPresetUpdateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editPresetUpdateBtnXpath));
	}

	public static WebElement editPresetEditDestinationsNextBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editPresetEditDestinationsNextBtnXpath));
	}

	public static WebElement editPresetEditSourcesNextBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editPresetEditSourcesNextBtnXpath));
	}

	public static WebElement deletePresetBtn(WebDriver driver, String name) {
		return driver.findElement(
				By.xpath("//div[@data-preset-name='" + name + "']//span[@class='fa fa-times delete-preset']"));
	}

	public static WebElement editPresetBtn(WebDriver driver, String name) {
		return driver.findElement(
				By.xpath("//div[@data-preset-name='" + name + "']//following::span[@data-target='#editPreset']"));
	}

	public static WebElement matrixItem(WebDriver driver, String name) {
		return driver.findElement(By.xpath("//span[contains(.,'" + name + "')]"));
	}

	public static WebElement nonActiveSource(WebDriver driver) {
		return driver.findElement(By.xpath(nonActiveSourceXpath));
	}

	public static WebElement nonActiveDestination(WebDriver driver) {
		return driver.findElement(By.xpath(nonActiveDestinationXpath));
	}

	public static WebElement singleSourceDestinationCheck(WebDriver driver, String source) {
		return driver.findElement(By.xpath("//span[text() = '" + source + "']"));
	}

	public static WebElement singleSourceDestinationCheck(WebDriver driver, String source, String destination) {
		return driver.findElement(
				By.xpath("//span[text() = '" + source + "']//following::span[text() = '" + destination + "']"));
	}

	public static WebElement getPresetBtn(WebDriver driver, String presetName) {
		return driver.findElement(By.xpath("//button[@data-attr-id='"+presetName+"']"));
//		return driver.findElement(By.xpath("//button[@class='btn btn-default btn-lg preset-activation-button'][contains(text(),'" + presetName + "')]"));
	}

	public static String getPresetButtonXpath(String name) {
		return "//button[@data-attr-id='" + name + "']";
	}

	public static String getCreatePresetNameTextBoxXpath() {
		return createPresetNameTextBoxXpath;
	}

	public static WebElement createPresetCompleteBtn(WebDriver driver) {
		return driver.findElement(By.xpath(createPresetCompleteBtnXpath));
	}

	public static void createPresetTypeDowndown(WebDriver driver, String value) {
		List<WebElement> elements = driver.findElements(By.xpath(createPresetTypeDowndownXpath));

		for (WebElement e : elements) {
			try {
				Select select = new Select(e);
				select.selectByVisibleText(value);
				break;
			} catch (Exception e1) {
				System.out.println("Not the correct element");
			}
		}
	}

	public static WebElement presetDestinationNextBtn(WebDriver driver) {
		return driver.findElement(By.xpath(presetDestinationNextBtnXpath));
	}

	public static String firstItemInCreatePresetDestinationList(WebDriver driver, String name) {
		//return "//table[contains(@id,'destination-items-container')]//td[contains(text(), '" + name + "')]";
		// return "//td[@class='element-title'][contains(text(),'" + name + "')]";
		return "//div[@class='destination-element-container element-grid-container']//td[@class='element-title'][contains(text(),'" + name + "')]";
		
	}

	public static WebElement presetSelectDestinationSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(presetSelectDestinationSearchBoxXpath));
	}

	public static WebElement presetSourcesSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(presetSourcesSearchBoxXpath));
	}

	public static WebElement presetSelectSourceNextBtn(WebDriver driver) {
		return driver.findElement(By.xpath(presetSelectSourceNextBtnXpath));
	}

	public static String firstItemInCreatePresetSourceList(WebDriver driver, String name) {
		//return "//div[@id='addPresetSources']//td[@class='element-title'][contains(text(),'" + name + "')]";
		return "//div[@class='source-element-container element-grid-container']//td[@class='element-title'][contains(text(),'" + name + "')]";
	}

	public static WebElement managePresetsBtn(WebDriver driver) {
		return driver.findElement(By.xpath(managePresetsBtnXpath));
	}

	public static WebElement searchAvailablePresets(WebDriver driver) {
		return driver.findElement(By.xpath(searchAvailablePresetsXpath));
	}

	public static WebElement createCustomPresetBtn(WebDriver driver) {
		return driver.findElement(By.xpath(createCustomBtnXpath));
	}

	// this element is the x from the source or destination passed in
	public static WebElement breakConnection(WebDriver driver, String connectionName) {
		// return driver.findElement(By.xpath("(//span[text() = '\" + connectionName +
		// \"']//following::span[@class='fa fa-times'])[2]"));
		return driver.findElement(By.xpath("(.//span[@class='detach'])[1]"));
	}

	public static void snapshotTypeDropdown(WebDriver driver, String value) {
		Select select = new Select(driver.findElement(By.xpath(snapshotTypeDropdownXpath)));
		select.selectByVisibleText(value);
	}

	public static WebElement snapshotNameInput(WebDriver driver) {
		return driver.findElement(By.xpath(snapshotNameInputXpath));
	}

	public static String getSnapshotBtn() {
		return snapshotBtnXpath;
	}

	public static String getActivateSelectedDestinationXpath() {
		return activateSelectedDestinationXpath;
	}

	public static WebElement firstItemInDestination(WebDriver driver, String destinationName) {
		return driver.findElement(By.xpath("//td[contains(.,'" + destinationName + "')]"));
	}

	public static WebElement sharedDestinationSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(sharedDestinationSearchBoxXpath));
	}

	public static WebElement privateConnectionsActivateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(privateConnectionsActivateBtnXpath));
	}

	public static void privateConnectionDropDown(WebDriver driver, String value) {
		driver.findElement(By.xpath(privateConnectionDropDownXpath)).click();
		driver.findElement(By.xpath(privateConnectionDropDownXpath)).sendKeys(value);
		// Select select = new
		// Select(driver.findElement(By.xpath(privateConnectionDropDownXpath)));
		// select.selectByVisibleText(value);
	}

	public static String destination(WebDriver driver, String connectionName) {
		// return "//span[text() ='" + connectionName +
		// "']//following::div[@class='source-destinations']//following::span[@class='fa
		// fa-plus']";
		return "//div[@class='active-source-outer' and contains(.,'" + connectionName
				+ "')]//span[@class='fa fa-plus']";
	}

	public static String firstItemInSourceList(WebDriver driver, String connectionName) {
		return "//td[text()='" + connectionName + "']";
	}

	public static WebElement activateSourcesDestination(WebDriver driver) {
		return driver.findElement(By.xpath(activateSourcesDestinationsXpath));
	}

	public static WebElement activateSourcesSource(WebDriver driver) {
		return driver.findElement(By.xpath(activeSourcesSourceXpath));
	}

	public static WebElement searchSources(WebDriver driver) {
		return driver.findElement(By.xpath(searchSourcesXpath));
	}

	public static WebElement makeConnectionBtn(WebDriver driver) {
		return driver.findElement(By.xpath(makeConnectionBtnXpath));
	}

	// public static WebElement btnAddConnection(WebDriver driver) { // Add
	// Connection Button
	// element = driver.findElement(By.xpath(".//*[@id='new-connection']"));
	// return element;
	// }JH

	// public static WebElement btnAddConnectionTemplate(WebDriver driver) { // Add
	// Connection Template Button
	// element = driver.findElement(By.xpath(".//*[@id='new-property']"));
	// return element;aa
	// }

	// public static WebElement templateName(WebDriver driver) { // Connection
	// Template Name
	// element = driver.findElement(By.xpath(".//*[@id='property-name']"));
	// return element;
	// }

	// public static WebElement connectionName(WebDriver driver) { // Connection
	// Name Text box
	// element = driver.findElement(By.xpath(".//*[@id='connection-name']"));
	// return element;dd
	// }
	//
	// public static WebElement hostName(WebDriver driver) { // Hostname Text box
	// element = driver.findElement(By.xpath(".//*[@id='host']"));
	// return element;
	// }

	// public static WebElement connectionViaTX(WebDriver driver) { // Connection
	// Via TX
	// element =
	// driver.findElement(By.xpath("((.//div[@class='btn-group'])[1]/label)[1]"));
	// return element;
	// }
	//
	// public static WebElement connectionViaVM(WebDriver driver) { // Connection
	// Via VM
	// element =
	// driver.findElement(By.xpath("((.//div[@class='btn-group'])[1]/label)[2]"));
	// return element;
	// }
	//
	// public static WebElement connectionViaVMPool(WebDriver driver) { //
	// Connection Via VM Pool
	// element =
	// driver.findElement(By.xpath("((.//div[@class='btn-group'])[1]/label)[3]"));
	// return element;
	// }

	// public static WebElement connectionViaBroker(WebDriver driver) { //
	// Connection Via Broker
	// element =
	// driver.findElement(By.xpath("((.//div[@class='btn-group'])[1]/label)[4]"));
	// return element;
	// }

	public static String useTemplateNo() {
		return ".//*[@id='radio-unique']";
	}

	public static String useTemplateYes() {
		return ".//*[@id='radio-template']";
	}

	public static WebElement useTemplateNo(WebDriver driver) { // Use Template No button

		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath("(.//*[@class='btn-group'])[2]/label[1]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='radio-unique']"));
		}
	}

	public static WebElement useTemplateYes(WebDriver driver) { // Use Template Yes button
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath("(.//*[@class='btn-group'])[2]/label[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='radio-template']"));
		}
	}

	// public static WebElement btnCancel(WebDriver driver) { // Cancel Button
	// element = driver.findElement(By.xpath("//button[@type='button' and
	// contains(., 'Cancel')]"));
	// return element;
	// }

	public static WebElement btnBack(WebDriver driver) { // Back Button
		element = driver.findElement(By.xpath("//button[@type='button' and contains(., 'Back')]"));
		return element;
	}

	// public static WebElement btnNext(WebDriver driver) { // Next Button
	// element = driver.findElement(By.xpath("//button[@type='button' and
	// contains(., 'Next')]"));
	// return element;
	// }

	// public static WebElement btnSave(WebDriver driver) { // Save Button
	// element = driver.findElement(By.xpath("//button[@class='btn btn-primary
	// wizard-pf-save']"));
	// return element;
	// }

	// public static WebElement connectionTypePrivate(WebDriver driver) { //
	// Connection Type - Private
	// element =
	// driver.findElement(By.xpath(".//*[@id='input-c-type']/div/div/label[1]"));
	// return element;
	// }

	// public static WebElement connectionTypeShared(WebDriver driver) { //
	// Connection Type - Shared
	// // element = driver.findElement(By.id("radio-shared"));
	// element =
	// driver.findElement(By.xpath(".//*[@id='input-c-type']/div/div/label[2]"));
	// return element;
	// }

	public static String extendedDesktop() {
		return "//input[@id='extended-desktop']";
	}

	public static WebElement extendedDesktop(WebDriver driver) { // Extended Desktop Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='input-ext-desktop']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='extended-desktop']"));
		}
	}

	public static WebElement usbRedirection(WebDriver driver) { // USB Redirection Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='input-usb-r']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='usb-redirection']"));
		}
	}

	public static WebElement audio(WebDriver driver) { // Audio Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='input-audio']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='audio']"));
		}
	}

	public static WebElement persistentConnection(WebDriver driver) { // Persistent Connection Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='input-persist']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='persistent-connection']"));
		}
	}

	public static WebElement viewOnlyConnection(WebDriver driver) { // Persistent Connection Input
		return element = driver.findElement(By.xpath(".//*[@id='view-only']"));
	}

	public static WebElement NLA(WebDriver driver) { // NLA Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='input-nla']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='nla']"));
		}
	}

	// public static WebElement connectionTable(WebDriver driver) { // Connection
	// Table
	// element = driver.findElement(By.xpath(".//*[@id='conntable']"));
	// return element;
	// }
	//
	// public static WebElement searchTextbox(WebDriver driver) { // Search Textbox
	// element = driver.findElement(By.xpath(".//*[@type='search']"));
	// return element;
	// }

	// public static WebElement usernameTextbox(WebDriver driver) { // Username
	// Textbox
	// element = driver.findElement(By.xpath(".//*[@id='username']"));
	// return element;
	// }

	// public static WebElement passwordTextbox(WebDriver driver) { // Password
	// Textbox
	// element = driver.findElement(By.xpath(".//*[@id='password']"));
	// return element;
	// }

	// public static WebElement domainTextbox(WebDriver driver) { // Domain Textbox
	// element = driver.findElement(By.xpath(".//*[@id='domain']"));
	// return element;
	// }

	// public static WebElement loadBalanceInfo(WebDriver driver) { // Load Balance
	// Info Textbox
	// element = driver.findElement(By.xpath(".//*[@id='load-balance-info']"));
	// return element;
	// }

	// ******************* Add Connection Template ***************************
	// public static WebElement addTemplateTX(WebDriver driver) { // Add Template -
	// Connection Via TX
	// element =
	// driver.findElement(By.xpath("((.//div[@class='btn-group'])[4]/label)[1]"));
	// return element;
	// }

	public static WebElement addTemplateVM(WebDriver driver) { // Add Template - Connection Via VM
		element = driver.findElement(By.xpath(addTemplateVM));
		return element;
	}

	public static WebElement addTemplateVMPool(WebDriver driver) { // Add Template - Connection Via VM Pool
		element = driver.findElement(By.xpath(addTemplateVMPool));
		return element;
	}

	//
	public static WebElement addTemplateBroker(WebDriver driver) { // Add Template - Connection Via Broker
		element = driver.findElement(By.xpath(addTemplateBroker));
		return element;
	}
	//
	// public static WebElement addTemplatePrivate(WebDriver driver) { // Add
	// Template Connection Type - Private
	// element =
	// driver.findElement(By.xpath(".//*[@id='property-c-type']/div/div/label[1]"));
	// return element;
	// }
	//
	// public static WebElement addTemplateShared(WebDriver driver) { // Add
	// Template Connection Type - Shared
	// // element = driver.findElement(By.id("radio-shared"));
	// element =
	// driver.findElement(By.xpath(".//*[@id='property-c-type']/div/div/label[2]"));
	// return element;
	// }

	public static WebElement addTemplateExtendedDesktop(WebDriver driver) { // Add Template - Extended Desktop Input
		if (driver instanceof ChromeDriver) {
			return element = driver
					.findElement(By.xpath(".//*[@id='property-ext-desktop-wrapper']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-ext-desktop']"));
		}
	}

	public static WebElement addTemplateUsbRedirection(WebDriver driver) { // Add Template - USB Redirection Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='property-usbr-wrapper']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-usbr']"));
		}
	}

	public static WebElement addTemplateAudio(WebDriver driver) { // Add Template - Audio Input
		if (driver instanceof ChromeDriver) {
			return element = driver.findElement(By.xpath(".//*[@id='property-audio-wrapper']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-audio']"));
		}
	}

	public static WebElement addTemplateViewOnly(WebDriver driver) {
		return driver.findElement(By.xpath(".//*[@id='property-view-only']"));
	}

	public static WebElement addTemplatePersistentConnection(WebDriver driver) { // Add Template - Persistent Connection
		// Input
		if (driver instanceof ChromeDriver) {
			return element = driver
					.findElement(By.xpath(".//*[@id='property-persistent-wrapper']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-persistent']"));
		}
	}

	public static WebElement addTemplateNLA(WebDriver driver) { // Add Template - NLA Input
		if (driver instanceof ChromeDriver) {
			return driver.findElement(By.xpath(".//*[@id='property-nla-wrapper']/div/div/div/span[2]"));
		} else {
			return element = driver.findElement(By.xpath(".//*[@id='property-nla']"));
		}
	}

	// public static WebElement addTemplateSavebtn(WebDriver driver) { // Save
	// Template Button
	// element = driver.findElement(By.xpath(".//*[@id='btn-property-save']"));
	// return element;
	// }

	// public static WebElement addTemplateDomainTextbox(WebDriver driver) { // Add
	// Template - Domain Textbox
	// element = driver.findElement(By.xpath(".//*[@id='property-domain']"));
	// return element;
	// }

	// public static WebElement addTemplateLoadBalanceInfo(WebDriver driver) { //
	// Add Template - Load Balance Info Textbox
	// element =
	// driver.findElement(By.xpath(".//*[@id='property-load-balance-info']"));
	// return element;
	// }

	// public static WebElement btnDeleteConnectionTemplate(WebDriver driver) { //
	// Delete Connection Template Button
	// element = driver.findElement(By.xpath(".//*[@id='delete-property']"));
	// return element;
	// }

	// public static WebElement nonSelectedList(WebDriver driver) { // Delete
	// Connection Template -> Non-Selected List box
	// element =
	// driver.findElement(By.xpath(".//*[@id='bootstrap-duallistbox-nonselected-list_']"));
	// return element;
	// }

	/*
	 * ******************* Elements for Moving Selected Items - Connection Template
	 * List*********************
	 */
	// public static WebElement moveSelectedBtn(WebDriver driver) { // Move Selected
	// Items form the list
	// element = driver.findElement(By.xpath("(//button[@title='Move
	// selected'])[1]"));
	// return element;
	// }

	// public static WebElement moveAllBtn(WebDriver driver) { // Move All Items
	// form the list
	// element = driver.findElement(By.xpath("(//button[@title='Move all'])[1]"));
	// return element;
	// }

	// public static WebElement removeSelectedBtn(WebDriver driver) { // Remove
	// Selected Items form the list
	// element = driver.findElement(By.xpath("(//button[@title='Remove
	// selected'])[1]"));
	// return element;
	// }

	// public static WebElement removeAllBtn(WebDriver driver) { // Remove All Items
	// form the list
	// element = driver.findElement(By.xpath("(//button[@title='Remove all'])[1]"));
	// return element;
	// }

	// public static WebElement btnDeleteTemplates(WebDriver driver) { // Remove All
	// Items form the list
	// element =
	// driver.findElement(By.xpath(".//*[@id='btn-delete-connection-properties']"));
	// return element;
	// }

	// public static WebElement connectionTemplateTX(WebDriver driver) { //
	// Connection Template TX
	// return element =
	// driver.findElement(By.xpath(".//*[@id='property-templates-via-tx']"));
	// }

	// public static WebElement connectionTemplateBroker(WebDriver driver) { //
	// Connection Template Broker
	// return element =
	// driver.findElement(By.xpath(".//*[@id='property-templates-broker']"));
	// }

	// public static WebElement connectionTemplateVM(WebDriver driver) { //
	// Connection Template VM
	// return element =
	// driver.findElement(By.xpath(".//*[@id='property-templates-vm']"));
	// }

	// public static WebElement connectionTemplateVMPool(WebDriver driver) { //
	// Connection Template VM Pool
	// return element =
	// driver.findElement(By.xpath(".//*[@id='property-templates-vm-pool']"));
	// }

	// Returns number of total available connection when multiple connection
	// avaialble
	// public static WebElement totalAvailableConnection(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath(".//*[@id='conntable_info']/b[3]"));
	// }

	// Returns number of total available connection when no connection avaialble
	// public static WebElement totalAvailableConnection2(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath(".//*[@id='conntable_info']/b[1]"));
	// }

	// public static WebElement newGroupBtn(WebDriver driver) { // New Group Button
	// return element = driver.findElement(By.xpath(".//*[@id='new-group']"));
	// }

	// public static WebElement groupName(WebDriver driver) { // Group name textbox
	// return element = driver.findElement(By.xpath(".//*[@id='group-name']"));
	// }
	//
	// public static WebElement groupDescription(WebDriver driver) { // Group
	// description text box
	// return element =
	// driver.findElement(By.xpath(".//*[@id='group-description']"));
	// }

	// public static WebElement groupAddBtn(WebDriver driver) { // Add button - new
	// group
	// return element = driver.findElement(By.xpath(".//*[@id='new-group-save']"));
	// }

	// public static WebElement connectionGroupTable(WebDriver driver) { // All
	// Connection Groups name
	// return element = driver.findElement(By.xpath(".//*[@data-header='All
	// Connections']"));
	// }

	// return number of total available groups when one or more group present
	// public static WebElement totalAvailableGroups(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath(".//*[@id='DataTables_Table_0_info']/b[3]"));
	// }

	// return number of total available groups when no group present
	// public static WebElement totalAvailableGroups2(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath(".//*[@id='DataTables_Table_0_info']/b[1]"));
	// }

	// public static WebElement breadCrumb(WebDriver driver) { // breadcrumb from
	// first row
	// return element =
	// driver.findElement(By.xpath("(.//*[@id='dropdownKebab'])[1]"));
	// }

	// public static WebElement breadCrumbGroup2(WebDriver driver) { // breadcrumb
	// from first row
	// return element =
	// driver.findElement(By.xpath("(.//*[@id='dropdownKebab'])[2]"));
	// }

	// public static WebElement groupManageConnection(WebDriver driver) { // Groups
	// > Manage Tab
	// return element = driver.findElement(By.xpath(".//*[@class='manage-group']"));
	// }

	// public static WebElement editGroup(WebDriver driver) { // Groups > Edit Tab
	// return element = driver.findElement(By.xpath(".//*[@class='edit-group']"));
	// }

	// public static WebElement saveBtnGroupConnections(WebDriver driver) { // Group
	// Connection > Save button
	// return element =
	// driver.findElement(By.xpath(".//*[@id='btn-save-group-connections']"));
	// }

	// public static WebElement connectionListFilterBox(WebDriver driver) { //
	// Connection list filter box
	// return element = driver.findElement(By.xpath("(.//*[@class='filter
	// form-control'])[1]"));
	// }

	// public static WebElement belongsToGroupFilterBox(WebDriver driver) { //
	// Belongs to Group filter box
	// return element = driver.findElement(By.xpath("(.//*[@class='filter
	// form-control'])[2]"));
	// }

	// Manage Connections - non selected connection list
	// public static WebElement nonSelectedActiveConnectionList(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath("(.//*[@id='bootstrap-duallistbox-nonselected-list_'])"));
	// }

	// Manage Connections - selected connection list
	// public static WebElement selectedActiveConnectionList(WebDriver driver) {
	// return element =
	// driver.findElement(By.xpath("(.//*[@id='bootstrap-duallistbox-selected-list_'])"));
	// }

	// public static WebElement groupUpdateBtn(WebDriver driver) { // Edit Group
	// return element = driver.findElement(By.xpath(".//*[@id='group-update']"));
	// }

	// public static WebElement groupDelete(WebDriver driver) { // Delete Group
	// return element = driver
	// .findElement(By.xpath(".//*[@class='dropdown dropdown-kebab-pf
	// open']//*[@class='delete-group']"));
	// }

	// public static WebElement dissolveBtn(WebDriver driver) { // Dissolve button
	// return element = driver
	// .findElement(By.xpath(".//*[@class='dropdown dropdown-kebab-pf
	// open']//*[@class='dissolve-group']"));
	// }
}
