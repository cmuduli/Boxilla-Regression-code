package objects;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import systemAdmin.SystemAdministration;

public class Devices {

	private static WebElement element = null;
	//zeroU
	private static String zerouUpgradeTab = "//a[contains(text(),'EMDDV-T')]";
	private static String zeroUSearchBox = "//div[@id='emddv-tx-table_filter']//label//input";
	// dashboard Xpath
	
	public static String iconimageXpath="//img[@class='navbar-brand-name']";
	public static String lunchIconXpath="//span[@class='fa fa-external-link']";
	public static String notificationIcon="//a[@id='notifications']";
	public static String notificationtitlexpath="//span[@class='fa pficon-flag']";
	public static String helplogoXpath="//span[@class='fa pficon-help']";
	
	//emerald SE specific xpaths
	private static String emeraldSeReceiverTabXpath = "//a[contains(.,'EMDSE-R')]";
	private static String emeraldSeTransmitterTabXpath = "//a[contains(.,'EMDSE-T')]";
	private static String emeraldSeSearchBoxXpath = "//input[@aria-controls='emdse-tx-table']";//input[@aria-controls='emdse-rx-table']
	private static String emeraldSeSearchBoxRxXpath = "//input[@aria-controls='emdse-rx-table']";
	//emerald specific xpaths
	private static String emeraldUpgradeTabXpath = "//a[contains(.,'Emerald')]";
	private static String uploadEmeraldBuildBtn = "//input[@label='Emerald Transmitter Upgrade Files']";
	private static String submitEmeraldBuildBtn = "//input[@name='commit']";
	//xpaths for 1.2 API
	private static String videoSettingXpath = "//div[@data-bb-tab='video_table']";
	private static String miscSettingsXpath = "//div[@data-bb-tab='tab-misc-settings']";
	private static String globalSettingsXpath = "//div[@data-bb-title='control_tabs']";
	private static String systemPropertiesButtonXpath = "//span[contains(.,'System Properties')]";
	private static String removeTemplateButtonXpath = "//span[contains(.,'-')]";
	private static String editTemplateButtonXpath = "//span[contains(.,'Edit Template')]";
	private static String addTemplateButtonXpath = "//div[@id='addTemplateDevices']";
	private static String applianceTypeDropdownXpath = "//select[@id='add-template-appliance-type-dropdown']";
	private static String templateNameTextBoxXpath = "//input[@id='add-template-name-text-input']";
	public static String videoQualityDropdownXpath = "//select[@id='add-template-video-quality-dropdown']";
	private static String videoSourceDropdownXpath = "//select[@id='add-template-video-source-opt-dropdown']"; 
	private static String HIDConfigurationDropdownXpath = "//select[@id='add-template-hid-configuration-dropdown']";
//	private static String audioDropdownXpath = "//select[@class='audio form-control']";
	private static String audioDropdownXpath = "//select[@id='add-template-audio-source-dropdown']";
	private static String EdidSettingsDvi1DropdownXpath = "//select[@id='add-template-edid-settings-dvi-1-dropdown']";
	private static String EdidSettingsDvi2DropdownXpath = "//select[@id='add-template-edid-settings-dvi-2-dropdown']";
	private static String saveTemplateButtonTransmitterXpath = "//div[@id='add-template-modal']//button[@id='fav-btn-save']";
	private static String saveTemplateButtonReceiverXpath = "//div[@id='add-template-modal']//button[@id='fav-btn-save']";
	private static String powerModeDropdownXpath = "//div[@id='add-template-modal']//select[@id='power-mode-dropdown']";
	private static String httpEnabledDropdownXpath = "//div[@id='add-template-modal']//select[@id='http-enabled-dropdown']";
	private static String editRxTemplateNameDropDownXpath = "//div[@id='edit-appliance-modal']//select[@id='edit-template-dropdown']";
	private static String editTxTemplateNameDropDownXpath = "//div[@id='edit-appliance-modal']//select[@id='edit-template-dropdown']";//"//div[starts-with(@class,'form-group template_name_tx_div')]//following::select[starts-with(@class,'form-control template_name_tx')]";
	private static String templatenameDropdownXpath = "//select[@class='form-control edit_template_appliance_type']";//"//div[starts-with(@class,\"form-group template_name_tx_div\")]//following::select[starts-with(@class,\"form-control template_name_tx_\")]";//"//select[@class='form-control edit_template_appliance_type']";
	private static String rxTemplateNameDropdownXpath = "//div[starts-with(@id,'edit_rx_settings_modal')]//following::select[starts-with(@class,'form-control template_name_rx')]";
	private static String editTemplateVideoQualityDropDownxpath = "//select[@id='edit-template-video-quality-dropdown']";
	private static String editTemplateVideoSourceDropdownXpath = "//select[@id='edit-template-video-source-opt-dropdown']";
	private static String editTemplateHidConfigurationDropdownXpath = "//select[@id='edit-template-hid-configuration-dropdown']";
	private static String editTemplateAudioDropdownXpath = "//select[@class='form-control edit_template_audio']";
	private static String editTemplateEdidSettingsDvi1DropdownXpath = "//select[@id='edit-template-edid-settings-dvi-1-dropdown']";
	private static String editTemplateEdidSettingsDvi2DropdownXpath = "//select[@id='edit-template-edid-settings-dvi-2-dropdown']";
	private static String editTemplateTxSaveButtonXpath = "//div[@id='edit-template-modal']//button[@id='fav-btn-save']";
	private static String editTemplateMouseTimeoutDropdownXpath = "//select[@id='edit-template-mouse-keyboard-timeout-dropdown']";
	private static String saveSystemProperyToastXpath = "//div[@class='toast-title']";
	private static String mouseTimeoutDropdownXpath = "//select[@id='add-template-mouse-keyboard-timeout-dropdown']";
	private static String editTemplatePowerModeDropdownXpath = "//select[@class='form-control edit_template_pwr_mde']";
	private static String editTemplateHttpEnabledDropdownXpath = "//select[@class='form-control edit_template_http_enbld']";
	private static String editTemplateRxSaveBtnXpath = "//button[@class='btn btn-primary saveBtnRx_edit_template rx_template']";
//	private static String deleteTemplateBtnXpath = "//span[contains(.,'-')]";
	private static String deleteTemplateBtnXpath = "//div[@class='row bb-title-container']//div[@id='deleteTemplateDevices']";

	private static String deleteTemplateTemplateNameDropdownXpath = "//select[@id='delete-template-dropdown']";
	private static String deleteTemplateDeleteBtnXpath = "//button[contains(text(),'Delete')]";
	private static String editDeviceSettingsTxXpath = "//div[@class='dropdown dropdown-kebab-pf open']//following::a[@data-original-title='Edit Settings']";//"//a[@aria-describedby='tooltip60669']";
	private static String editDeviceSettingsRxXpath = "//div[@class='dropdown dropdown-kebab-pf open']//following::a[@data-original-title='Edit Settings']";
	
	//Global settings tab elements
	private static String gsHotKeyDropdownXpath = "//select[@id='hot_key']";
	private static String gsFunctionalHotKeyXpath = "//select[@id='func_hot_key']";
	private static String gsRDPConResolutionXpath = "//select[@id='rdp_resolution']";
	private static String gsConnectionInactivityTimerBtnXpth = "//button[@data-id='conn-range']";
	private static String gsConnectionInActivityRangeDropdownXpath = "//select[@id='connection_inactivity_timer']";
	private static String gsOSDInactivityTimerBtnXpath = "//button[@data-id='gui-range']";
	private static String gsOSDInavtivityRangeDropdownXpath = "//select[@id='gui_inactivity_timer']";
	private static String gsBrokerConnectionTypeDropdownXpath = "//select[@id='broker_connection_type']";
	private static String gsWebAccessAddressTextBox = "//input[@id='broker_web_address']";
	private static String gsConnectionBrokerNameTextBox = "//input[@id='broker_name_ip']";
	private static String gsDomainTextBox = "//input[@id='broker_domain']";
	private static String gsLoadBalanceInfoTextBox = "//input[@id='broker_load_balance_info']";
	private static String gsApplyBtn = "//input[@class='btn btn-primary']";
	
	//System Properties elements
	private static String spVideoQualityDropdownXpath = "//select[@id='video-quality-dropdown']";
	private static String spVideoSourceDropdownXpath = "//select[@id='video-source-opt-dropdown']";
	
	private static String spHidConfigurationDropdownXpath = "//select[@id='hid-configuration-dropdown']";
//	private static String spAudioDropdownXpath = "//select[@class='form-control audio_sys_set']";
	private static String spAudioDropdownXpath = "//select[@id='audio-source-dropdown']";
	
	private static String spEdidSettingsDvi1DropdownXpath = "//select[@id='edid-settings-dvi-1-dropdown']";
	private static String spEdidSettingsDvi2DropdownXpath = "//select[@id='edid-settings-dvi-2-dropdown']";
	private static String spPowerModeDropdownXpath = "//select[@id='power-mode-dropdown']";
	private static String spHttpEnabledDropdownXpath = "//select[@id='http-enabled-dropdown']";
//	private static String spSaveBtnXpath = "//div[@id='system-properties-modal']//button[@id='fav-btn-save']";
	private static String spSaveBtnXpath = "//div[@id='system-properties-modal']//button[@id='fav-btn-save']";

	private static String spMouseTimeoutXpath = "//select[@id='mouse-keyboard-timeout-dropdown']";
	private static String spEdidDvi1 =  "//select[@id='edid-settings-dvi-1-dropdown']";
	private static String spEdidDvi2 = "//select[@id='edid-settings-dvi-2-dropdown']";
	private static String spCancelBtnXpath = "//button[@class='btn btn-default closeBtn']";
	
	//edit transmitter settings
	private static String editTxSettingsSettingTypeDropdownXpath = "//select[@id='setting-type-dropdown']";//"//div[starts-with(@id,\"edit_tx_settings_modal_\")]//following::select[starts-with(@class,\"form-control setting_type_tx\")]";//select[@class='form-control setting_type_rx_1281']
	private static String editRxSettingsSettingTypeDropdownXpath = "//select[@id='setting-type-dropdown']";
	private static String editTxSaveBtnXpath = "//div[@id='edit-appliance-modal']//button[@id='fav-btn-save']";//"//div[starts-with(@id,\"edit_tx_settings_modal_\")]//following::button[starts-with(@class,\"btn btn-primary saveBtnTxSettings\")]";
	private static String editRxSaveBtnXpath = "//div[@id='edit-appliance-modal']//button[@id='fav-btn-save']";
	private static String editTxCancelXpath = "//div[@id='edit-appliance-modal']//button[@class='btn btn-default'][contains(text(),'Close')]";
	private static String editRxCancelXpath = "//div[@id='edit-appliance-modal']//button[@class='btn btn-default'][contains(text(),'Close')]";
	
	private static String editTemplateTemplateNameDropdownXpath = "//select[@id='edit-template-dropdown']";
	private static String uniqueVideoQualityDropdownXpath = "//select[@id='edit-template-video-quality-dropdown']";
	private static String uniqueVideoSourceDropdownXpath = "//select[@id='edit-template-video-source-opt-dropdown']";
	private static String uniqueHidDropdownXpath = "//select[@id='edit-template-hid-configuration-dropdown']";
	private static String uniqueAudioDropdownXpath = "//div[starts-with(@id,\"edit_tx_settings_modal_\")]//following::select[starts-with(@class,\"form-control audio\")]";
	private static String uniqueMouseTimeoutDropdownXpath = "//select[@id='edit-template-mouse-keyboard-timeout-dropdown']";
	private static String uniqueEdid1DropdownXpath = "//select[@id='edit-template-edid-settings-dvi-1-dropdown']";
	private static String uniqueEdid2DropdownXpath = "//select[@id='edit-template-edid-settings-dvi-2-dropdown']";
	private static String uniquePowerModeDropdownXpath = "//div[@id='edit-appliance-modal']//select[@id='power-mode-dropdown']";
	private static String uniqueHttpEnabledDropDownXpath = "//div[@id='edit-appliance-modal']//select[@id='http-enabled-dropdown']";
	private static String deleteTemplateCancelBtn = "//button[@class='btn btn-default closeBtn']";
	
	//bulk update
	private static String bulkUpdateBtnXpath = "//span[contains(.,'Bulk Update')]";
	private static String bulkUpdateApplianceTypeDropdownXpath = "//select[@id='appliance-type-dropdown']";
	private static String bulkUpdateSaveBtnXpath = "//div[@id='bulk-update-modal']//button[@id='fav-btn-save']";
	private static String bulkUpdateCancelBtnXpath = "//button[@class='btn btn-default closeBtn']";
	public static String bulkUpdateTemplateNameDropdownXpath = "//div[@id='bulk-update-modal']//select[@id='delete-template-dropdown']";
	private static String bulkUpdateSearchBoxXpath = "//form[@class='form-horizontal']//div[@class='dataTables_header']//input";
	private static String bulkUpdateDeviceCheckboxXpath = "//input[@type='checkbox']";
	
	//device upgrades
	private static String peUpgradeSearchBoxTransmitter = "//div[@id='emdpe-tx-table_filter']//label//input";
	private static String fourkUpgradeSearchBoxTransmitter = "//div[@id='emd4k-tx-table_filter']//label//input";
	private static String seUpgradeSearchBoxTransmitter = "//div[@id='emdse-tx-table_filter']//label//input";
	private static String peReceiverActiveReleaseTable = "//table[@id='emdpe-rx-table']";
	private static String fourkReceiverActiveReleaseTable = "//table[@id='emd4k-rx-table']";
	private static String seReceiverActiveReleaseTable = "//table[@id='emdse-rx-table']";
	private static String peTransmitterActiveReleaseTable = "//table[@id='emdpe-tx-table']";
	private static String fourkTransmitterActiveReleaseTable = "//table[@id='emd4k-tx-table']";
	private static String seTransmitterActiveReleaseTable = "//table[@id='emdse-tx-table']";
	private static String activateReleaseLink = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Activate')]";
	private static String peTransmitterTab = "//a[contains(text(),'EMDPE-T')]";
	private static String fourkTransmitterTab = "//a[contains(text(),'EMD4K-T')]";
	private static String seTransmitterTab = "//a[contains(text(),'EMDSE-T')]";
	private static String peReceiverTab = "//a[contains(text(),'EMDPE-R')]";

	private static String emeraldReleaseTabReceiverLink = "//a[@href='#emd4k-r']";
	private static String emeraldReleaseTabTransmitterLink = "//a[@href='#emd4k-t']";
	private static String emeraldTxSearchBox = "//input[@aria-controls='emd4k-tx-table']";
	public static String emeraldRxSearchBox = "//input[@aria-controls='emd4k-rx-table']";
	
	public static String applianceTable = "(//div[@class=\"\"])[1]";//"//div[@id='tab-video-settings']";
	public static String deviceStatusSearchBox = ".//input[@type='search']";
	public static String selectDevicesSearchBox = "//input[@aria-controls='DataTables_Table_0']";
	public static String dtxrSearchBox = "(.//input[@type='search'])[1]";
	public static String dtxtSearchBox = "(.//input[@type='search'])[2]";
	public static String breadCrumbBtn = ".//*[@id='dropdownKebab']";
	public static String ipAddress = "//table[@id='appliance_table']/tbody/tr[1]/td[3]";
	public static String breadCrumbBtnEmeraldRX = "//table[@id='emd4k-rx-table']/tbody/tr[1]/td[4]";
	public static String breadCrumbBtnEmeraldTX = "//table[@id='emd4k-tx-table']/tbody/tr[1]/td[4]";
	public static String breadCrumbBtnEmeraldSeTx = "//table[@id='emdse-tx-table']/tbody/tr[1]/td[4]";
	public static String breadCrumbBtnZeroU = "//table[@id='emddv-tx-table']//img";
	//public static String breadCrumbBtnEmeraldSeRx  = "//table[@id='emdse-tx-table']/tbody/tr[1]/td[4]";
	public static String breadCrumbBtnEmeraldSeRx  = "//table[@id='emdse-rx-table']//img";
	public static String breadCrumbBtnTX = "//table[@id='tx-table']/tbody/tr[1]/td[4]";
	public static String breadCrumbBtnRX = "//table[@id='rx-table']/tbody/tr[1]/td[4]";
	
	
	//public static String unManageTab = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'UnManage')]";
	public static String unManageTab = "//div[@id='tab-video-settings']//li[3]//a[1]";
	private static String fourkReceiverActiveDropdown = "//table[@id='emd4k-rx-table']//img";
	private static String peReceiverActiveDropdown = "//table[@id='emdpe-rx-table']//img";
	private static String seReceiverActiveDropdown = "//table[@id='emdse-rx-table']//img";
	private static String peTransmitterActiveDropdown = "//table[@id='emdpe-tx-table']//img";
	private static String fourkTransmitterActiveDropdown = "//table[@id='emd4k-tx-table']//img";
	private static String seTransmitterActiveDropdown = "//table[@id='emdse-tx-table']//img";
	public static String peUpgradeSearchBoxReceiver = "//div[@id='emdpe-rx-table_filter']//label//input";
	public static String fourkUpgradeSearchBoxReceiver = "//div[@id='emd4k-rx-table_filter']//label//input";
	public static String seUpgradeSearchBoxReceiver = "//div[@id='emdse-rx-table_filter']//label//input";
	public static String selectDevices = ".//div[@data-bb-tab='tab-upgrades']";
	public static String selectSetTimeOut="//a[@id='setTimeoutButton']";
	public static String selectTimeouttext="//input[@id='upgradeTimeout']";
	public static String TimeoutSavebtn="//button[@id='btn-timeout-save']";
	public static String timeoutsavebtn="//div[@class='toast-title']";
	public static String releasesTab = "//div[contains(text(), 'Releases')]";
	public static String upgradeBtn = ".//*[@id='upgrade_boards']";
	public static String uploadBtn = "//a[@href='/blackbox/upload']";
	public static String upgradeTable = ".//*[@data-header='Upgrade Table']";
	public static String rxTable  = ".//*[@id='rx-table']";
	public static String rxTableVersionNumber = "//table[@id='rx-table']/tbody/tr[1]/td[1]";
	public static String isActiveRX = "//table[@id='rx-table']/tbody/tr[1]/td[3]";
	public static String txTable = ".//*[@id='tx-table']";
	public static String txTableVersionNumber = "//table[@id='tx-table']/tbody/tr[1]/td[1]";
	public static String isActiveEmeraldRX = "//table[@id='emd4k-rx-table']/tbody/tr[1]/td[3]";
	public static String isActiveEmeraldTX = "//table[@id='emd4k-tx-table']/tbody/tr[1]/td[3]";
	
	public static String isActiveEmeraldSeTx = "//table[@id='emdse-tx-table']/tbody/tr[1]/td[3]";
	public static String isActiveEmeraldSeRx = "//table[@id='emdse-rx-table']/tbody/tr[1]/td[3]";
	private static String deviceVersion = "//table[@id='DataTables_Table_0']//td[5]";
	public static String isActiveZeroUTx = "//table[@id='emddv-tx-table']/tbody/tr[1]/td[3]";
	public static String isActiveZeroURx = "//table[@id='emddv-rx-table']/tbody/tr[1]/td[3]";
	private static String selectAllDevicesUpgrade = "//input[@id='check_all']";
	public static String isActiveTX = "//table[@id='tx-table']/tbody/tr[1]/td[3]";
	public static String rxTab = ".//a[@href='#dtx-r']";
	public static String txTab = ".//a[@href='#dtx-t']";
	public static String searchboxRX = ".//*[@id='rx-table_filter']/label/input";
	public static String searchboxTX = ".//*[@id='tx-table_filter']/label/input";
	public static String uploadElement = ".//*[@id='kvm_upgrade_filename']";
	public static String applyBtn = "//div[@id='editManagedDevIP']";
	public static String deviceSettingsApply = "//input[@name='commit']";
	public static String deleteVersion = ".//*[@data-original-title='Remove Release Image']";
	public static String activateVersionEmeraldRX = ".//table[@id='emd4k-rx-table']//*[@data-original-title='Activate Release Image']";
	public static String activateVersionEmeraldTX = ".//table[@id='emd4k-tx-table']//*[@data-original-title='Activate Release Image']";
	
	public static String activateVersionEmeraldSeTX = ".//table[@id='emdse-tx-table']//*[@data-original-title='Activate Release Image']";
	public static String activateVersionEmeraldSeRX = ".//table[@id='emdse-rx-table']//*[@data-original-title='Activate Release Image']";
	public static String activateVersionZeroU = "//div[@class='dropdown dropdown-kebab-pf open']//a[contains(text(),'Activate')]";
	public static String activateVersionTX = ".//table[@id='tx-table']//*[@data-original-title='Activate Release Image']";
	public static String activateVersionRX = ".//table[@id='rx-table']//*[@data-original-title='Activate Release Image']";
	public static String firmwareVersionTX = "//td[@class='tx-version']";
	public static String firmwareVersionRX = "//td[@class='rx-version']";
	public static String state = "//table/tbody/tr[1]/td[6]";
	public static String searchedDeviceCheckbox = "//table/tbody/tr[1]/td[1]/input";
	public static String allCheckbox = ".//*[@id='check_all']";
	public static String connectionInactivityTimerBtn = ".//*[@data-select='connection_inactivity_timer']";
	public static String osdInactivityTimerBtn = ".//*[@data-select='gui_inactivity_timer']";
	public static String connectionInactivityRange = ".//*[@id='connection_inactivity_timer']";
	public static String OSDInactivityRange = ".//*[@id='gui_inactivity_timer']";
	public static String details = ".//*[@data-original-title='Retrieve Appliance Details']";
	public static String detailsclose="//div[@id='appliance-details-modal']//button[@type='button'][normalize-space()='Close']";
	public static String ping = ".//*[@data-original-title='Ping Appliance']";
	public static String forcelogout=".//*[@data-original-title='Force Logout']";
	public static String changeDeviceZoneSave="//div[@id='change-appliance-zone-modal']//button[@id='fav-btn-save']";
	public static String changeDeviceZone=".//*[@data-original-title='Change Device Zone']";
	public static String changeDeviceZoneDropDown="//select[@id='zone-list']";
	public static String deviceDetailsNameHeader = "(.//*[@class='card-pf-title text-center'])[1]";
	public static String divTitle = "(.//*[@class='bb-title'])";
	public static String edit = ".//*[@data-original-title='Edit Network Settings']";
	public static String chanageDeviceName = ".//*[@data-original-title='Change Device Name']";
	public static String restoreDevice = ".//*[@data-original-title='Restore Device Configuration To Factory Default Settings']";
	public static String rebootDevice = ".//*[@data-original-title='Reboot Device']";
	public static String editDeviceIPAddTextbox = ".//*[@id='kvm_appliance_ip']";
	public static String editDeviceGatewayTextbox = ".//*[@id='kvm_appliance_gateway']";
	public static String currentIP = ".//*[@id='appliance_table']/tbody/tr/td[2]";
	public static String hostnameTextbox = "//input[@id='edit-appliance-name-text-input']";
	public static String hostnameApplyBtn = "//div[@id='change-appliance-name-modal']//button[@id='fav-btn-save']";
//	public static String deviceUptime = "(.//*[@class='card-pf-body'])[2]/p[5]"; 
	public static String deviceUptime = "((//div[@class='card-container'])[2]//p[@class='card-pf-info text-center'])[5]"; 
	public static String httpEnabledStatus = "//div[@id='DataTables_Table_0_wrapper']//td[10]";
//	private static String deviceDetailsBackBtn = "//button[contains(text(),'Back')]";
	private static String deviceDetailsBackBtn = "//div[@id='appliance-details-modal']//button[@type='button'][normalize-space()='Close']";

	private static String functionalHotkeyDropdown = "//select[@id='func_hot_key']";
	private static String RDP_ConnectionResolution = "//select[@id='rdp_resolution']";
	private static String hotkeyDropdown = "//select[@id='hot_key']";
	
	//multicast
	public static String multiCastIpTextBox = "//input[@id='kvm_appliance_multicast_ip']";
	public static String multiCastPortTextBox = "//input[@id='kvm_appliance_multicast_master_port']";
	
	public static String upgradeTableEntire = "//table[@id='DataTables_Table_0']//tbody";
	//BONDED GROUPS
	private static String createBondedReceiverGroup = "//span[contains(text(),'Bonded Receiver Group')]";
	private static String addReceiverButton = "//button[@id='add-bonded-pair-btn']";
	private static String addReceiverButtonEdit = "//div[@id='edit-zone-list']//following::button[@id='add-bonded-pair-btn']";
	private static String receiverGroupName = "//input[@id='name-field']";
	private static String addReceiverGroupZoneList = "//select[@id='zone-list']";
	private static String addReceiverGroupSaveButton = "//button[@id='fav-btn-save']";
	private static String bondedGroupSearch = "//input[@type='search']";
	private static String bondedGroupExpand = "//span[contains(text(),'Expand')]";
	private static String bondedGroupTableReceiverCount = "//span[contains(text(),'Expand')]//following::td[2]";
	private static String bondedGroupTableZone = "//span[contains(text(),'Expand')]//following::td[3]";
	private static String bondedGroupTableStatus = "//span[contains(text(),'Expand')]//following::td[4]";
	private static String bondedReceiversHeading = "//h3[contains(text(),'Bonded Receivers')]";
	private static String bondedGroupBurgerButton = "//button[@id='dropdownKebab']";
	private static String editBondedReceiverGroup = "//a[@title='Edit']";
	private static String deleteBondedReceiverGroup = "//a[@title='Delete']";
	private static String deleteBondedGroupToast = "//div[@id='toast-container']";
	private static String editBondedGroupSaveBtn = "//div[@id='edit-zone-list']//following::button[@id='fav-btn-save']";
	
	//END
	//logger
	
	final static Logger log = Logger.getLogger(Devices.class);

	public static String getFourkTransmitterActiveReleaseTable() {
		return fourkTransmitterActiveReleaseTable;
	}
	public static String getFourkTransmitterActiveDropdown() {
		return fourkTransmitterActiveDropdown;
	}
	public static String getFourkUpgradeSearchBoxTransmitter() {
		return fourkUpgradeSearchBoxTransmitter;
	}
	public static String getFourkTransmitterTab() {
		return fourkTransmitterTab;
	}
	public static String getFourkReceiverActiveReleaseTable() {
		return fourkReceiverActiveReleaseTable;
	}
	public static String getFourkReceiverActiveDropdown() {
		return fourkReceiverActiveDropdown;
	}
	public static String getFourkUpgradeSearchBoxReceiver() {
		return fourkUpgradeSearchBoxReceiver;
	}
	public static String getSeTransmitterActiveReleaseTable() {
		return seTransmitterActiveReleaseTable;
	}
	public static String getSeTransmitterActiveDropdown() {
		return seTransmitterActiveDropdown;
	}
	public static String getSeUpgradeSearchBoxTransmitter() {
		return seUpgradeSearchBoxTransmitter;
	}
	public static String getSeReceiverActiveReleaseTable() {
		return seReceiverActiveReleaseTable;
	}
	public static String getSeReceiverActiveDropdown() {
		return seReceiverActiveDropdown;
	}
	public static String getSeUpgradeSearchBoxReceiver() {
		return seUpgradeSearchBoxReceiver;
	}
	public static String getEditBondedReceiverMoveDown(int position) {
		return "//div[@id='edit-zone-list']//following::button[@id='shift-down-" + position + "']";
	}
	public static String getEditBondedReceiverMoveUp(int position) {
		return "//button[@id='shift-up-" + position + "']";
	}
	public static String getEditBondedGroupSaveBtn() {
		return editBondedGroupSaveBtn;
	}
	public static String getAddReceiverButtonEdit() {
		return addReceiverButtonEdit;
	}
	public static WebElement getchangeDeviceZoneDropDown(WebDriver driver) 
	{
		return driver.findElement(By.xpath(changeDeviceZoneDropDown));
	}
	public static String getDeleteReceiverFromGroupButton(int number) {
		return "//button[@id='delete-bonded-pair-" + number + "']" ;
	}
	
	public static String getDeleteBondedGroupToast() {
		return deleteBondedGroupToast;
	}
	public static String getDeleteBondedReceiverGroup() {
		return deleteBondedReceiverGroup;
	}
	public static String getEditBondedReceiverGroup() {
		return editBondedReceiverGroup;
	}
	public static String getBondedGroupBurgerButton() {
		return bondedGroupBurgerButton;
	}
	public static String getReceiverInGroup(int number, String groupName) {
		return "//li[@id='" + number + "-" + groupName + "']";
	}
	
	public static String getBondedReceiversHeading() {
		return bondedReceiversHeading;
	}
	public static String getBondedGroupTableStatus() {
		return bondedGroupTableStatus;
	}
	public static String getBondedGroupTableZone() {
		return bondedGroupTableZone;
	}
	public static String getBondedGroupTableReceiverCount() {
		return bondedGroupTableReceiverCount;
	}
	public static String getBondedGroupExpand() {
		return bondedGroupExpand;
	}
	public static String getGroupNameFromTable(String name) {
		return "//td[contains(text(),'" + name + "')]";
	}
	
	public static String getBondedGroupSearch() {
		return bondedGroupSearch;
	}
	public static String getBondedReceiverDropdown(String number) {
		return "//input[@id='receiver-input-" + number + "']";
	}
	public static String getAddReceiverGroupSaveButton () {
		return addReceiverGroupSaveButton;
	}
	public static String getAddReceiverGroupZoneList() {
		return addReceiverGroupZoneList;
	}
	public static String getReceiverGroupName() {
		return receiverGroupName;
	}
	public static String getAddReceiverButton() {
		return addReceiverButton;
	}
	public static String getCreateBondedReceiverGroup ( ) {
		return createBondedReceiverGroup;
	}
	
	public static String getPeReceiverActiveDropdown() {
		return peReceiverActiveDropdown;
	}
	public static String getPeTransmitterActiveReleaseTable() {
		return peTransmitterActiveReleaseTable;
	}
	public static String getDeviceVersion() {
		return deviceVersion;
	}
	public static String getSelectAllDevicesUpgrade() {
		return selectAllDevicesUpgrade;
	}
	public static String getPeReceiverActiveReleaseTable() {
		return peReceiverActiveReleaseTable;
	}
	
	public static String getPeTransmitterActiveDropdown() {
		return peTransmitterActiveDropdown;
	}
	public static String getActivateReleaseLinkPeReceiver() {
		return activateReleaseLink;
	}
	public static String getPeUpgradeSearchBoxReceiver() {
		return peUpgradeSearchBoxReceiver;
	}
	
	public static String getPeUpgradeSearchBoxTransmitter() {
		return peUpgradeSearchBoxTransmitter;
	}
	
	public static String getPeTransmitterTab() {
		return peTransmitterTab;
	}
	public static String getPeReceiverTab() {
		return peReceiverTab;
	}
	public static String getSeReceiverTab() {
		return "//a[contains(text(),'EMDSE-R')]";
	}
	public static String getSeTransmitterTab() {
		return "//a[contains(text(),'EMDSE-T')]";
	}
	public static String get4kReceiverTab() {
		return "//a[contains(text(),'EMD4K-R')]";
	}
	public static String get4KTransmitterTab() {
		return "//a[contains(text(),'EMD4K-T')]";
	}

	public static String getUpgradeTableEntire() {
		return upgradeTableEntire;
	}
	public static String getHotkeyDropdown() {
		return hotkeyDropdown;
	}
	
	public static String getRDP_ConnectionResolution() {
		return RDP_ConnectionResolution;
	}
	
	public static String getFunctionalHotkeyDropdown() {
		return functionalHotkeyDropdown;
	}
	
	public static String getDeviceDetailsBackBtn() {
		return deviceDetailsBackBtn;
	}
	

	//getters for xpaths
	public static String getZerouUpgradeTabXpath() {
		return zerouUpgradeTab;
	}
	public static String getZeroUSearchBox() {
		return zeroUSearchBox;
	}
	
	
	public static String getEmeraldSeSearchBoxRxXpath() {
		return emeraldSeSearchBoxRxXpath;
	}
	public static String getEmeraldSeReceiverTabXpath() {
		return emeraldSeReceiverTabXpath;
	}
	public static String getEmeraldSeSearchBoxXpath() {
		return emeraldSeSearchBoxXpath;
	}
	public static String getEmeraldSeTransmitterTabXpath() {
		return emeraldSeTransmitterTabXpath;
	}
	public static WebElement getZerouUpgradeTab(WebDriver driver) {
		return driver.findElement(By.xpath(getZerouUpgradeTabXpath()));
	}
	public static WebElement getIsActiveEmeraldSeTx(WebDriver driver) {
		return driver.findElement(By.xpath(isActiveEmeraldSeTx));
	}
	public static WebElement getIsActiveEmeraldSeRx(WebDriver driver) {
		return driver.findElement(By.xpath(isActiveEmeraldSeRx));
	}
	public static WebElement getEmeraldSeSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(getEmeraldSeSearchBoxXpath()));
	}
	public static WebElement getEmeraldSeTransmitterTab(WebDriver driver) {
		return driver.findElement(By.xpath(getEmeraldSeTransmitterTabXpath()));
	}
	public static WebElement getEmeraldSeReceiverTab(WebDriver driver) {
		return driver.findElement(By.xpath(getEmeraldSeReceiverTabXpath()));
	}
	public static WebElement getDeviceTableSetting(WebDriver driver, String columnNumber) {
		return driver.findElement(By.xpath("//table[@id='appliance_table']//following::td[" + columnNumber + "]"));
	}
	public static WebElement getDeviceMiscTableSetting(WebDriver driver, String columnNumber) {
		return driver.findElement(By.xpath("//table[@id='appliance_table_misc']//following::td[" + columnNumber + "]"));
	}
	public static String getEditTxSaveBtnXpath () {
		return editTxSaveBtnXpath;
	}
	
	public static String getMiscSettingsXpath() {
		return miscSettingsXpath;
	}
	
	public static String getBulkUpdateDeviceCheckboxXpath() {
		return bulkUpdateDeviceCheckboxXpath;
	}
	
	public static String getBulkUpdateSaveBtnXpath() {
		return bulkUpdateSaveBtnXpath;
	}
	public static String getBulkUpdateCancelBtnXpath() {
		return bulkUpdateCancelBtnXpath;
	}
	
	public static WebElement httpEnabledElement(WebDriver driver ) {
		return driver.findElement(By.xpath(httpEnabledStatus));
	}
	
	
	public static String getEditRxCancelXpath() {
		return editRxCancelXpath;
	}
	
	public static String getdeleteTemplateCancelBtn () {
		return deleteTemplateCancelBtn;
	}
	
	public static String getEditTxCancelXpath() {
		return editTxCancelXpath;
	}
	
	public static String getEditRxSaveBtnXpath() {
		return editRxSaveBtnXpath;
	}
	
	public static String getSpCancelBtnXpath() {
		return spCancelBtnXpath;
	}
	public static String getEditTxSettingsSettingTypeDropdownXpath() {
		return editTxSettingsSettingTypeDropdownXpath;
	}
	public static String getEditRxSettingsSettingTypeDropdownXpath() {
		return editRxSettingsSettingTypeDropdownXpath;
	}
	
	public static String getsystemPropertiesButtonXpath() {
		return systemPropertiesButtonXpath;
	}
	public static String getSaveSystemProperyToastXpath() {
		return saveSystemProperyToastXpath;
	}
	public static String getTemplateNameDropdownXpath() {
		return templatenameDropdownXpath;
	}
	
	public static WebElement searchBoxEmeraldRx(WebDriver driver) {
		return driver.findElement(By.xpath(emeraldRxSearchBox));
	}
	
	public static WebElement searchBoxEmeraldTx(WebDriver driver) {
		return driver.findElement(By.xpath(emeraldTxSearchBox));
	}
	
	public static WebElement editRxSaveBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editRxSaveBtnXpath));
	}
	
	public static WebElement editDeviceSettingsRx(WebDriver driver) {
		return driver.findElement(By.xpath(editDeviceSettingsRxXpath));
	}
	
	public static WebElement editTxCancel(WebDriver driver) {
		return driver.findElement(By.xpath(editTxCancelXpath));
	}
	
	public static WebElement editTxSaveBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editTxSaveBtnXpath));
	}
	
	public static WebElement editDeviceSettingsTx(WebDriver driver) {
		return driver.findElement(By.xpath(editDeviceSettingsTxXpath));
	}
	
	public static WebElement bulkUpdateSearchBox(WebDriver driver) {
		return driver.findElement(By.xpath(bulkUpdateSearchBoxXpath));
	}
	
	public static WebElement spCancelBtn(WebDriver driver) {
		return driver.findElement(By.xpath(spCancelBtnXpath));
	}
	public static WebElement getEmeraldSeSearchBoxRx(WebDriver driver) {
		return driver.findElement(By.xpath(getEmeraldSeSearchBoxRxXpath()));
	}
	public static WebElement deleteTemplate(WebDriver driver) {
		return driver.findElement(By.xpath(deleteTemplateBtnXpath));
	}
	
	public static WebElement editTemplateRxSaveBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editTemplateRxSaveBtnXpath));
	}
	
	public static WebElement deleteTemplateDeleteBtn(WebDriver driver) {
		return driver.findElement(By.xpath(deleteTemplateDeleteBtnXpath));
	}
	
//	public static void bulkUpdateTemplateNameDropdown(WebDriver driver, String value) {
//		log.info("Template name selecting: " + value );
//		Select select = new Select(driver.findElement(By.xpath(bulkUpdateTemplateNameDropdownXpath)));
//		select.selectByVisibleText(value);
//	}
	
	public static void bulkUpdateApplianceTypeDropdown(WebDriver driver, String value) {
		log.info("Appliance type selecting: " + value );
		Select select = new Select(driver.findElement(By.xpath(bulkUpdateApplianceTypeDropdownXpath)));
		select.selectByVisibleText(value);
	}
	
	public static void uniqueHttpEnabledDropdown(WebDriver driver, boolean isEnabled) {
		log.info("Edit RX settings, Http Enabled. setting type dropdown selecting: " + isEnabled);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueHttpEnabledDropDownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					if(isEnabled) {
						select.selectByVisibleText("Enabled");
					}else {
						select.selectByVisibleText("Disabled");
					}
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static WebElement emeraldReleaseTabReceiver(WebDriver driver) {
		return driver.findElement(By.xpath(emeraldReleaseTabReceiverLink));
	}
	
	public static WebElement emeraldReleaseTabTransmitter(WebDriver driver) {
		return driver.findElement(By.xpath(emeraldReleaseTabTransmitterLink));
	}
	
	public static WebElement emeraldTab(WebDriver driver) {
		return driver.findElement(By.xpath(emeraldUpgradeTabXpath));
	}
	
	public static WebElement bulkUpdateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(bulkUpdateBtnXpath));
	}
	
	public static void uniquePowerModeDropdown(WebDriver driver, boolean isManual) {
		log.info("Edit TX settings, unique mouse timeout. setting type dropdown selecting: " + isManual);
		List<WebElement> elements = driver.findElements(By.xpath(uniquePowerModeDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					if(isManual) {
						select.selectByVisibleText("Manual");
					}else {
						select.selectByVisibleText("Auto");
					}
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static void uniqueEdid1Dropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique mouse timeout. setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueEdid1DropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static void uniqueEdid2Dropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique mouse timeout. setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueEdid2DropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	
	public static void uniqueMouseTimeoutDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique mouse timeout. setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueMouseTimeoutDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static void uniqueAudioDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique audio. setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueAudioDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				Select select = new Select(e);
				select.selectByVisibleText(text);
				break;
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
		
	public static void uniqueHidDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique HID setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueHidDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
				Select select = new Select(e);
				select.selectByVisibleText(text);
				break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static void uniqueVideoSourceDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique video source setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueVideoSourceDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	
	public static void uniqueVideoQualityDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings, unique video quality setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(uniqueVideoQualityDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
		
	//	Select select = new Select(driver.findElement(By.xpath(editTxSettingsSettingTypeDropdownXpath)));
	//	select.selectByVisibleText(text);
	}
	
	public static void editTxSettingsSettingDropdown(WebDriver driver, String text) {
		log.info("Edit TX settings setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(editTxSettingsSettingTypeDropdownXpath));
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
		
	//	Select select = new Select(driver.findElement(By.xpath(editTxSettingsSettingTypeDropdownXpath)));
	//	select.selectByVisibleText(text);
	}
	
	public static void editTemplateTemplateNameDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateTemplateNameDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editRxSettingsSettingDropdown(WebDriver driver, String text) {
		log.info("Edit RX settings setting type dropdown selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(editRxSettingsSettingTypeDropdownXpath));
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception ex) {
				System.out.println("Not the correct element");
			}
		}
		
	}
	
	public static void deleteTemplateTemplateNameDropdown(WebDriver driver, String text) {
		log.info("Delete Template, template name dropdown selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(deleteTemplateTemplateNameDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplateMouseTimeoutDropdown(WebDriver driver, String text) {
		log.info("Edit Template Mouse Keyboard Timeout selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateMouseTimeoutDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplatePowerModeDropdown(WebDriver driver, boolean isManual) {
		Select select = new Select(driver.findElement(By.xpath(editTemplatePowerModeDropdownXpath)));
		if(isManual) {
			log.info("Edit Template Power Mode dropdown selecting: Manual" );
			select.selectByVisibleText("Manual");
		}else {
			log.info("Edit Template Power Mode dropdown selecting: Auto" );
			select.selectByVisibleText("Auto");
		}
	}
	
	public static void editTemplateHttpEnabledDropdown(WebDriver driver, boolean isEnabled) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateHttpEnabledDropdownXpath)));
		if(isEnabled) {
			log.info("Edit Template HTTP Enabled Dropdown selecting: Enabled");
			select.selectByVisibleText("Enabled");
		}else {
			log.info("Edit Template HTTP Enabled Dropdown selecting: Disabled");
			select.selectByVisibleText("Disabled");
		}
	}
	
	public static WebElement getDeviceToastMessage(WebDriver driver) {
		return driver.findElement(By.xpath(getSaveSystemProperyToastXpath()));
	}
	
	public static void mouseTimeoutDropdown(WebDriver driver, String text) {
		log.info("Mouse Timeout selecting: " + text );
		Select select = new Select(driver.findElement(By.xpath(mouseTimeoutDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void spHttpEnabledDropdown(WebDriver driver, boolean isEnabled) {
		Select select = new Select(driver.findElement(By.xpath(spHttpEnabledDropdownXpath)));
		
		if(isEnabled) {
			log.info("SP HTTP Enabled selecting: Enabled");
			select.selectByVisibleText("Enabled");
		}else {
			log.info("SP HTTP Enabled selecting: Disabled");
			select.selectByVisibleText("Disabled");
		}
	}
	
	//elements for 1.2 API. Most dropdowns elemetns have two methods. 
	//One to select by index and one by text
	public static WebElement spSaveBtn(WebDriver driver) {
		return driver.findElement(By.xpath(spSaveBtnXpath));
	}
	public static void spPowerModeDropdown(WebDriver driver, boolean isManual) {
		Select select = new Select(driver.findElement(By.xpath(spPowerModeDropdownXpath)));
		if(isManual) {
			log.info("SP Power Mode selecting: Manual");
			select.selectByVisibleText("Manual");
		}else {
			select.selectByVisibleText("Auto");
			log.info("SP Power Mode selecting: Auto");
		}
	}
	
	public static void spEdidDvi1Dropdown(WebDriver driver, String text) {
		log.info("SP EDID DVI 1 selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spEdidDvi1)));
		select.selectByVisibleText(text);
	}
	
	public static void spEdidDvi2Dropdown(WebDriver driver, String text) {
		log.info("SP EDID DVI 2 selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spEdidDvi2)));
		select.selectByVisibleText(text);
	}
	
	public static void spMouseTimeoutDropdown(WebDriver driver, String text) {
		log.info("SP Mouse Keyboard Timeout selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spMouseTimeoutXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void spAudioDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(spAudioDropdownXpath)));
		select.selectByIndex(index);
	}
	public static void spAudioDropdown(WebDriver driver, String text) {
		log.info("SP Audio selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spAudioDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void spVideoSourceDropdown(WebDriver driver, String text) {
		log.info("SP Video Source selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spVideoSourceDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void audioSourceDropdown(WebDriver driver,String text)
	{
		log.info("Audio Source selecting: " + text);
		Select select=new Select(driver.findElement(By.xpath(spAudioDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void spVideoSourceDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(spVideoSourceDropdownXpath)));
		select.selectByIndex(index);
	}
	public static void spHidConfigurationDropdown(WebDriver driver, String text) {
		log.info("SP HID Config selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spHidConfigurationDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void spHidConfigurationDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(spHidConfigurationDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void spVideoQualityDropdown(WebDriver driver, String text) {
		log.info("spVideo Quality selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(spVideoQualityDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void spVideoQualityDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(spVideoQualityDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static WebElement gsApplyBtn(WebDriver driver) {
		return driver.findElement(By.xpath(gsApplyBtn));
	}
	
	public static WebElement gsDomainTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(gsDomainTextBox));
	}
	
	public static WebElement gsLoadBalanceInfoTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(gsLoadBalanceInfoTextBox));
	}
	
	public static WebElement gsWebAccessAddressTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(gsConnectionBrokerNameTextBox));
	}
	
	public static WebElement gsgsConnectionBrokerNameTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(gsWebAccessAddressTextBox));
	}
	
	public static void gsBrokerConnectionTypeDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(gsBrokerConnectionTypeDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void gsBrokerConnectionTypeDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsBrokerConnectionTypeDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void gsOSDInavtivityRangeDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsOSDInavtivityRangeDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void gsOSDInavtivityRangeDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(gsOSDInavtivityRangeDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static WebElement gsOSDInactivityTimerBtn(WebDriver driver) {
		return driver.findElement(By.xpath(gsOSDInactivityTimerBtnXpath));
	}
	public static WebElement gsConnectionInactivityTimerBtn(WebDriver driver) {
		return driver.findElement(By.xpath(gsConnectionInactivityTimerBtnXpth));
	}
	
	public static void gsConnectionInActivityRangeDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsConnectionInActivityRangeDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void gsConnectionInActivityRangeDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(gsConnectionInActivityRangeDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void gsRDPConResolutionDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsRDPConResolutionXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void gsRDPConResolutionDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(gsRDPConResolutionXpath)));
		select.selectByIndex(index);
	}
	
	public static void gsHotKeyDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsHotKeyDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void gsHotKeyDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(gsHotKeyDropdownXpath)));
		select.selectByIndex(index);
	}
	public static void gsFunctionalHotKeyDropdown(WebDriver driver, String text) {
		Select select = new Select(driver.findElement(By.xpath(gsFunctionalHotKeyXpath)));
		select.selectByVisibleText(text);
	}
	
	
	public static WebElement editTemplateTxSaveBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editTemplateTxSaveButtonXpath));
	
	}
	
	public static void editTemplateEdidSettingsDvi2Dropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateEdidSettingsDvi2DropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void editTemplateEdidSettingsDvi2Dropdown(WebDriver driver, String text) {
		log.info("Edit Template EDID DVI2 dropdown selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateEdidSettingsDvi2DropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplateEdidSettingsDvi1Dropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateEdidSettingsDvi1DropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void editTemplateEdidSettingsDvi1Dropdown(WebDriver driver, String text) {
		log.info("Edit Template EDID DVI1 dropdown selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateEdidSettingsDvi1DropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplateAudioDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateAudioDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void editTemplateAudioDropdown(WebDriver driver, String text) {
		log.info("Edit Template Audio dropdown selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateAudioDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplateHidConfigurationDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateHidConfigurationDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void editTemplateHidConfigurationDropdown(WebDriver driver, String text) {
		log.info("Edit Template HID Configuration Dropdown selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateHidConfigurationDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void editTemplateVideoSourceDropdown(WebDriver driver, String text) {
		log.info("Edit Template Video Source Dropdown selecting: " + text);
		
		Select select = new Select(driver.findElement(By.xpath(editTemplateVideoSourceDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static void editTemplateVideoSourceDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateVideoSourceDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void editTemplateVideoQualityDropDown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(editTemplateVideoQualityDropDownxpath)));
		select.selectByIndex(index);
	}
	public static void editTemplateVideoQualityDropDown(WebDriver driver, String text) {
		log.info("Edit template Video Quality selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(editTemplateVideoQualityDropDownxpath)));
		select.selectByVisibleText(text);
	}
	

	public static void editRxTemplateNameDropdown(WebDriver driver, String text) {
		log.info("Template  selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(editRxTemplateNameDropDownXpath));
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Not the correct element");
			}
		}
		
	}
	
	public static void rxTemplateNameDropdown(WebDriver driver, String text) {
		log.info("Template  selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(rxTemplateNameDropdownXpath));
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Not the correct element");
			}
		}
		
	}
	
	public static void templateNameDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(templatenameDropdownXpath)));
		select.selectByIndex(index);
	}
	
	
	public static void EditTxTemplateNameDropdown(WebDriver driver, String text) {
		log.info("Template  selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(editTxTemplateNameDropDownXpath));
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					//select.selectByValue(text + "_template");
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Not the correct element");
			}
		}
		
	}
	
	public static void templateNameDropdown(WebDriver driver, String text) {
		log.info("Template  selecting: " + text);
		List<WebElement> elements = driver.findElements(By.xpath(templatenameDropdownXpath));
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					Select select = new Select(e);
					//select.selectByValue(text + "_template");
					select.selectByVisibleText(text);
					break;
				}
			}catch(Exception ex) {
				ex.printStackTrace();
				System.out.println("Not the correct element");
			}
		}
		
	}
	
//	public static void templateNameDropdown(WebDriver driver, String text) {
//		log.info("Template Name selecting: " + text);
//		Select select = new Select(driver.findElement(By.xpath(templatenameDropdownXpath)));
//		select.selectByVisibleText(text + "_template");
//	}
	public static void HttpEnabledDropdown(WebDriver driver, boolean isEnabled) {
		Select select = new Select(driver.findElement(By.xpath(httpEnabledDropdownXpath)));
		
		if(isEnabled) {
			log.info("HTTP Enabled selecting: Enabled");
			select.selectByVisibleText("Enabled");
		}else {
			log.info("HTTP Enabled selecting: Disabled");
			select.selectByVisibleText("Disabled");
		}
	}
	public static void PowerModeDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(powerModeDropdownXpath)));
		select.selectByIndex(index);
	}
	public static void PowerModeDropdown(WebDriver driver, boolean isManual) {

		Select select = new Select(driver.findElement(By.xpath(powerModeDropdownXpath)));
		if(isManual) {
			log.info("Power Mode selecting: Manual");
			select.selectByVisibleText("Manual");
		}else {
			log.info("Power Mode selecting: Auto");
			select.selectByVisibleText("Auto");
		}
	}
	
	public static WebElement saveTemplateRxBtn(WebDriver driver) {
		return driver.findElement(By.xpath(saveTemplateButtonReceiverXpath));
	}
	
	public static WebElement saveTemplateTxBtn(WebDriver driver) {
		return driver.findElement(By.xpath(saveTemplateButtonTransmitterXpath));
	}
	
	public static void EdidSettingsDvi2Dropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(EdidSettingsDvi2DropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void EdidSettingsDvi2Dropdown(WebDriver driver, String text) {
		log.info("EDID DVI 2 selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(EdidSettingsDvi2DropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void EdidSettingsDvi1Dropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(EdidSettingsDvi1DropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void EdidSettingsDvi1Dropdown(WebDriver driver, String text) {
		log.info("EDID DVI 1 selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(EdidSettingsDvi1DropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void audioDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(audioDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void audioDropdown(WebDriver driver, String text) {
		log.info("Audio selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(audioDropdownXpath)));
		select.selectByVisibleText(text);
	}
	 
	public static void HIDConfigurationDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(HIDConfigurationDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void HIDConfigurationDropdown(WebDriver driver, String text) {
		log.info("HID Config selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(HIDConfigurationDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void videoSourceDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(videoSourceDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void videoSourceDropdown(WebDriver driver, String text) {
		log.info("Video Source selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(videoSourceDropdownXpath)));
		select.selectByVisibleText(text);
	}
	
	public static void applianceTypeDropdown(WebDriver driver, boolean isReceiver) {
		Select select = new Select(driver.findElement(By.xpath(applianceTypeDropdownXpath)));
		
		if(isReceiver) {
			log.info("Appliance type selecting: Receiver");
			select.selectByVisibleText("Receiver");
		}else {
			select.selectByVisibleText("Transmitter");
			log.info("Appliance type selecting: Transmitter");
		}
	}
	
	public static WebElement templateNameTextBox(WebDriver driver) {
		return driver.findElement(By.xpath(templateNameTextBoxXpath));
	}
	
	public static void videoQualityDropdown(WebDriver driver, int index) {
		Select select = new Select(driver.findElement(By.xpath(videoQualityDropdownXpath)));
		select.selectByIndex(index);
	}
	
	public static void videoQualityDropdown(WebDriver driver, String text) {
		log.info("Video Quality selecting: " + text);
		Select select = new Select(driver.findElement(By.xpath(videoQualityDropdownXpath)));
		select.selectByVisibleText(text);
	}
	public static WebElement addTemplateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(addTemplateButtonXpath));
	}
	public static WebElement editTemplateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(editTemplateButtonXpath));
	}
	public static WebElement removeTemplateBtn(WebDriver driver) {
		return driver.findElement(By.xpath(removeTemplateButtonXpath));
	}
	public static WebElement videoSettingsTab(WebDriver driver) {
		return driver.findElement(By.xpath(videoSettingXpath));
	}
	
	public static WebElement MiscSettingsTab(WebDriver driver) {
		return driver.findElement(By.xpath(miscSettingsXpath));
	}
	public static WebElement globalSettingsTab(WebDriver driver) {
		return driver.findElement(By.xpath(globalSettingsXpath));
	}
	public static WebElement systemPropertiesBtn(WebDriver driver) {
		return driver.findElement(By.xpath(systemPropertiesButtonXpath));
	}
	//end elements for 1.2 API

//	public static WebElement applianceTable(WebDriver driver) { // Appliance Table
//		element = driver.findElement(By.xpath(".//*[@id='appliance_table']"));
//		return element;
//	}

//	public static WebElement deviceStatusSearchBox(WebDriver driver) { // Search box under Device > Status
//		element = driver.findElement(By.xpath(".//input[@type='search']"));
//		return element;
//	}

//	public static WebElement selectDevicesSearchBox(WebDriver driver) { // Search box
//		//element = driver.findElement(By.xpath("(.//input[@type='search'])[3]")); 
//		
//		element = driver.findElement(By.xpath("//input[@aria-controls='DataTables_Table_0']"));
//		return element;
//	}
//
//	public static WebElement dtxrSearchBox(WebDriver driver) { // DTX-R Search box
//		element = driver.findElement(By.xpath("(.//input[@type='search'])[1]"));
//		return element;
//	}
//
//	public static WebElement dtxtSearchBox(WebDriver driver) { // DTX-T Search box
//		element = driver.findElement(By.xpath("(.//input[@type='search'])[2]"));
//		return element;
//	}
//
//	public static WebElement breadCrumbBtn(WebDriver driver) { // breadcrumb button - Devices > Status
//		element = driver.findElement(By.xpath(".//*[@id='dropdownKebab']"));
//		return element;
//	}
//
//	public static WebElement ipAddress(WebDriver driver) { // IP address extract from 2nd colum in table
//		return element = driver.findElement(By.xpath("//table/tbody/tr[1]/td[3]"));
//	}
//	
//	public static WebElement breadCrumbBtnEmeraldRX(WebDriver driver) { // breadcrumb button TX
//		element = driver.findElement(By.xpath("//table[@id='emd4k-rx-table']/tbody/tr[1]/td[4]"));
//		return element;
//	}
	
//	public static WebElement breadCrumbBtnEmeraldTX(WebDriver driver) { // breadcrumb button TX
//		element = driver.findElement(By.xpath("//table[@id='emd4k-tx-table']/tbody/tr[1]/td[4]"));
//		return element;
//	}
//
//	public static WebElement breadCrumbBtnTX(WebDriver driver) { // breadcrumb button TX
//		element = driver.findElement(By.xpath("//table[@id='tx-table']/tbody/tr[1]/td[4]"));
//		return element;
//	}
//
//	public static WebElement breadCrumbBtnRX(WebDriver driver) { // breadcrumb button RX
//		element = driver.findElement(By.xpath("//table[@id='rx-table']/tbody/tr[1]/td[4]"));
//		return element;
//	}
//
//	public static WebElement unManageTab(WebDriver driver) { // UnManage Button - BreadCrumb > UnManage
//		element = driver.findElement(By.xpath(".//a[@class='unmanage']"));
//		return element;
//	}
//
//	public static WebElement selectDevices(WebDriver driver) { // Select Devices Tab
//		return element = driver.findElement(By.xpath(".//div[@data-bb-tab='tab-upgrades']"));
//	}
//
//	public static WebElement releasesTab(WebDriver driver) { // Select Releases Tab
//		return element = driver.findElement(By.xpath("//div[contains(text(), 'Releases')]"));
//		// return element =
//		// driver.findElement(By.xpath(".//div[@data-bb-tab='tab-releases']"));
//	}
//
//	public static WebElement upgradeBtn(WebDriver driver) { // Upgrade Button
//		return element = driver.findElement(By.xpath(".//*[@id='upgrade_boards']"));
//	}
//
//	public static WebElement uploadBtn(WebDriver driver) { // Upload Button
//		return element = driver.findElement(By.xpath("//a[@href='/blackbox/upload']"));
//	}
//
//	public static WebElement upgradeTable(WebDriver driver) { // Device table under upgrade section
//		return element = driver.findElement(By.xpath(".//*[@data-header='Upgrade Table']"));
//	}
//
//	public static WebElement rxTable(WebDriver driver) { // RX Table under Upgrade > Releases
//		return element = driver.findElement(By.xpath(".//*[@id='rx-table']"));
//	}
//
//	public static WebElement rxTableVersionNumber(WebDriver driver) { // Version number in TX table
//		return element = driver.findElement(By.xpath("//table[@id='rx-table']/tbody/tr[1]/td[1]"));
//	}
//
//	public static WebElement isActiveRX(WebDriver driver) { // Checking if version is active
//		return element = driver.findElement(By.xpath("//table[@id='rx-table']/tbody/tr[1]/td[3]"));
//	}
//
//	public static WebElement txTable(WebDriver driver) { // TX Table under Upgrade > Releases
//		return element = driver.findElement(By.xpath(".//*[@id='tx-table']"));
//	}
//
//	public static WebElement txTableVersionNumber(WebDriver driver) { // Version number in TX table
//		return element = driver.findElement(By.xpath("//table[@id='tx-table']/tbody/tr[1]/td[1]"));
//	}
//
//	public static WebElement isActiveEmeraldRX(WebDriver driver) { // Checking if version is active
//		return element = driver.findElement(By.xpath("//table[@id='emd4k-rx-table']/tbody/tr[1]/td[3]"));
//	}
//	
//	public static WebElement isActiveEmeraldTX(WebDriver driver) { // Checking if version is active
//		return element = driver.findElement(By.xpath("//table[@id='emd4k-tx-table']/tbody/tr[1]/td[3]"));
//	}
//	
//	public static WebElement isActiveTX(WebDriver driver) { // Checking if version is active
//		return element = driver.findElement(By.xpath("//table[@id='tx-table']/tbody/tr[1]/td[3]"));
//	}
//
//	public static WebElement rxTab(WebDriver driver) { // RX Tab under Upgrade > Releases
//		return element = driver.findElement(By.xpath(".//a[@href='#dtx-r']"));
//	}

//	public static WebElement txTab(WebDriver driver) { // TX Tab under Upgrade > Releases
//		return element = driver.findElement(By.xpath(".//a[@href='#dtx-t']"));
//	}

	// Device release tab based on rx or tx device
	public static WebElement deviceReleaseTab(WebDriver driver, String appliance) {
		if (appliance.equalsIgnoreCase("tx")) {
			return element = driver.findElement(By.xpath(".//a[@href='#dtx-t']"));
		} else if (appliance.equalsIgnoreCase("rx")) {
			return element = driver.findElement(By.xpath(".//a[@href='#dtx-r']"));
		} else {
			return null;
		}
	}

//	public static WebElement searchboxRX(WebDriver driver) { // RX Table search box
//		return element = driver.findElement(By.xpath(".//*[@id='rx-table_filter']/label/input"));
//	}
//
//	public static WebElement searchboxTX(WebDriver driver) { // TX Table search box
//		return element = driver.findElement(By.xpath(".//*[@id='tx-table_filter']/label/input"));
//	}
//
//	public static WebElement uploadElement(WebDriver driver) { // Browse button / upload element
//		return element = driver.findElement(By.xpath(".//*[@id='kvm_upgrade_filename']"));
//	}
//
//	public static WebElement applyBtn(WebDriver driver) { // Apply Button
//		return element = driver.findElement(By.xpath(".//*[@value='Apply']"));
//	}
//
//	public static WebElement deleteVersion(WebDriver driver) { // Delete Version
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Remove Release Image']"));
//	}
//
//	public static WebElement activateVersionEmeraldRX(WebDriver driver) { // Activate Version Button TX
//		return element = driver
//				.findElement(By.xpath(".//table[@id='emd4k-rx-table']//*[@data-original-title='Activate Release Image']"));
//	}
//	
//	public static WebElement activateVersionEmeraldTX(WebDriver driver) { // Activate Version Button TX
//		return element = driver
//				.findElement(By.xpath(".//table[@id='emd4k-tx-table']//*[@data-original-title='Activate Release Image']"));
//	}
//	
//	public static WebElement activateVersionTX(WebDriver driver) { // Activate Version Button TX
//		return element = driver
//				.findElement(By.xpath(".//table[@id='tx-table']//*[@data-original-title='Activate Release Image']"));
//	}
//
//	public static WebElement activateVersionRX(WebDriver driver) { // Activate Version Button RX
//		return element = driver
//				.findElement(By.xpath(".//table[@id='rx-table']//*[@data-original-title='Activate Release Image']"));
//	}

	public static WebElement firmwareVersion(WebDriver driver, String appliance) { // Firmware version tx
		if (appliance.equalsIgnoreCase("tx")) {
			return element = driver.findElement(By.xpath("//td[@class='tx-version']"));
		} else if (appliance.equalsIgnoreCase("rx")) {
			return element = driver.findElement(By.xpath("//td[@class='rx-version']"));
		} else {
			return null;
		}
	}

//	public static WebElement firmwareVersionTX(WebDriver driver) { // Firmware version rx
//		return element = driver.findElement(By.xpath("//td[@class='tx-version']"));
//	}
//
//	public static WebElement firmwareVersionRX(WebDriver driver) { // Firmware version rx
//		return element = driver.findElement(By.xpath("//td[@class='rx-version']"));
//	}
//
//	public static WebElement state(WebDriver driver) { // State of device
//		return element = driver.findElement(By.xpath("//table/tbody/tr[1]/td[6]"));
//	}
//
//	public static WebElement searchedDeviceCheckbox(WebDriver driver) { // Checkbox for searched device
//		return element = driver.findElement(By.xpath("//table/tbody/tr[1]/td[1]/input"));
//	}
//
//	public static WebElement allCheckbox(WebDriver driver) { // Add Device checkbox
//		return element = driver.findElement(By.xpath(".//*[@id='check_all']"));
//	}

//	// Connection Inactivity Timer - Enable button
//	public static WebElement connectionInactivityTimerBtn(WebDriver driver) {
//		return element = driver.findElement(By.xpath(".//*[@data-select='connection_inactivity_timer']"));
//	}

//	// OSD Inactivity Timer - Enable button
//	public static WebElement osdInactivityTimerBtn(WebDriver driver) {
//		return element = driver.findElement(By.xpath(".//*[@data-select='gui_inactivity_timer']"));
//	}
//
//	// Connection InActivity Range (Minutes) drop down box
//	public static WebElement connectionInactivityRange(WebDriver driver) {
//		return element = driver.findElement(By.xpath(".//*[@id='connection_inactivity_timer']"));
//	}
//
//	// OSD InActivity Range (Minutes) drop down box
//	public static WebElement OSDInactivityRange(WebDriver driver) {
//		return element = driver.findElement(By.xpath(".//*[@id='gui_inactivity_timer']"));
//	}
//
//	public static WebElement details(WebDriver driver) { // Device Status > Managed Device > Details Tab
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Retrieve Appliance Details']"));
//	}
//
//	public static WebElement ping(WebDriver driver) { // Device Status > Managed Device > ping tab
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Ping Appliance']"));
//	}
//
//	public static WebElement deviceDetailsNameHeader(WebDriver driver) { // Header containing device name
//		return element = driver.findElement(By.xpath("(.//*[@class='card-pf-title text-center'])[1]"));
//	}
//
//	public static WebElement divTitle(WebDriver driver) { // Device Details division title
//		return element = driver.findElement(By.xpath("(.//*[@class='bb-title'])"));
//	}
//
//	public static WebElement edit(WebDriver driver) { // Edit device
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Edit Network Settings']"));
//	}
//
//	public static WebElement chanageDeviceName(WebDriver driver) { // Change device name
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Change Device Name']"));
//	}
//
//	public static WebElement restoreDevice(WebDriver driver) { // Restore Device
//		return element = driver.findElement(
//				By.xpath(".//*[@data-original-title='Restore Device Configuration To Factory Default Settings']"));
//	}
//
//	public static WebElement rebootDevice(WebDriver driver) { // Reboot Device
//		return element = driver.findElement(By.xpath(".//*[@data-original-title='Reboot Device']"));
//	}
//
//	public static WebElement editDeviceIPAddTextbox(WebDriver driver) { // Edit device - IP address textbox
//		return element = driver.findElement(By.xpath(".//*[@id='kvm_appliance_ip']"));
//	}
//
//	public static WebElement editDeviceGatewayTextbox(WebDriver driver) { // Edit device - Gateway textbox
//		return element = driver.findElement(By.xpath(".//*[@id='kvm_appliance_gateway']"));
//	}
//
//	public static WebElement currentIP(WebDriver driver) { // Element to extract current IP address
//		return element = driver.findElement(By.xpath(".//*[@id='appliance_table']/tbody/tr/td[2]"));
//	}
//
//	public static WebElement hostnameTextbox(WebDriver driver) { // Hostname text box
//		return element = driver.findElement(By.xpath(".//*[@name='kvm_hostname[nameVar]']"));
//	}
//
//	public static WebElement hostnameApplyBtn(WebDriver driver) { // hostname save button
//		return element = driver.findElement(By.xpath(".//*[@id='saveBtn']"));
//	}
//
//	public static WebElement deviceUptime(WebDriver driver) { // Device up time
//		return element = driver.findElement(By.xpath("(.//*[@class='card-pf-body'])[2]/p[5]"));
//	}
}
