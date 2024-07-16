package objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

public class Peripherals {

	public enum SETTINGS {
		EXT_NAME, TYPE, MAC, STATE, IP, BONDED_DEVICE, BONDED_DEVICE_IP
	}
	public enum DISCOVERY {
		TYPE, MAC, IP, STATE
	}
	//discovery tab
	private String discoverButton = "//div[@id='discoverUsbExtenders']";
	private String toastMessage = "//div[@class='toast-message']";
	private String discoveryTab = "//div[@data-bb-tab='discovery_table']";
	private String discoveryTable = "//div[@id='discovery_table']";
	private String discoveryTableSearchBox = "//div[@id='discovery_table']//div[@class='dataTables_header']//input";
	private String discoveryTableInfo = "//div[@id='discovery_table']//div[@class='dataTables_header']//b[2]";
	private String discoveryDeviceDropdown = "//button[@id='dropdownKebab']//img";
	private String discoveryEditNetworkLink = "//a[text()='Edit Network']";
	private String discoveryEditNetworkModal = "//div[@id='edit-icron-usb-extender-network-modal']";
	private String discoveryEditNetworkIpAddressTextBox = "//input[@id='ip-address-text-field']";
	private String discoveryEditNetworkGatewayTextBox = "//input[@id='extender-gateway-text-input']";
	private String discoveryEditNetworkNetmaskTextBox = "//input[@id='netmask-text-input']";
	private String discoveryEditNetworkSaveBtn = "//div[@id='edit-icron-usb-extender-network-modal']//button[@id='fav-btn-save']";
	private String editNetworkSpinner = "//div[@class='spin-wrapper']//img[@class='rotate']";
	private String searchedDeviceIp  = "//div[@id='discovery_table']//tr//td[3]";
	private String searchedDeviceBondedStatus  = "//div[@id='discovery_table']//tr//td[8]";
	private String editBondingLink = "//a[text()='Edit Bonding']";
	private String editBondingModel = "//div[@id='edit-icron-usb-extender-bonding-modal']";
	private String editBondingExtName = "//div[@id='edit-icron-usb-extender-bonding-modal']//input[contains(@class,'form-control')]";
//	private String editBondingDestinations = "//div[@id='edit-icron-usb-extender-bonding-modal']//select[@id='edit-destination-dropdown']";
	private String editBondingDestinations = "//div[@id='edit-icron-usb-extender-bonding-modal']//input[@id='edit-destination-dropdown']";

	private String editBondingSaveBtn = "//div[@id='edit-icron-usb-extender-bonding-modal']//button[@id='fav-btn-save']";
//	private String discoveryDevicedetails="//table/tbody/tr[1]";
	private String discoveryDevicedetails="(//table/tbody/tr[1])[1]";
	
	
	//settings tab
	private String settingsTab = "//div[@data-bb-tab='settings_table']";
	private String settingsTable = "//div[@id='settings_table']";
	private String settingsSearchbox = "//div[@id='settings_table']//div[@class='dataTables_header']//input";
	private String settingsTableInfo = "//div[@id='settings_table']//div[@class='dataTables_header']//b[2]";
	private String settingsDropdown = "//div[@id='settings_table']//div[@id='DataTables_Table_0_wrapper']//div//img";
	private String settingsDropdownDetails = "//a[text()='Details']";
	private String usbDetails = "//div[@class='dtails-container']";
	private String usbDetailsCloseBtn = "//div[@id='extender-details-modal']//button[@class='btn btn-default'][contains(text(),'Close')]";
	private String settingsDropdownPing = "//a[text()='Ping']";
	private String settingsDropdownEditNetwork = "//div[@class='cards-pf']//li[3]//a[1]";
	private String settingsEditNetworkModal = "//div[@id='edit-icron-usb-extender-network-modal']";
	private String settingsEditNetworkIp = "//input[@id='ip-address-text-field']";
	private String settingsEditNetworkSave = "//div[@id='edit-icron-usb-extender-network-modal']//button[@id='fav-btn-save']";
	private String settingsChangeExtendername = "//a[text()='Change Extender Name']";
	private String editExtenderNameModal = "//div[@id='edit-extender-name-modal']";
	private String extenderNameTextbox = "//input[@id='edit-extender-name-text-input']";
	private String extenderNameSaveBtn = "//div[@id='edit-extender-name-modal']//button[@id='fav-btn-save']";
	private String settingsEditBonding = "//div[@class='cards-pf']//li[5]//a[1]";
	private String settingsEditBondingModal = "//div[@id='edit-icron-usb-extender-bonding-modal']";
	private String settingsEditBondingExtenderName = "//div[@id='edit-icron-usb-extender-bonding-modal']//input[contains(@class,'form-control')]";
	private String settingsEditBondingDestinations = "//div[@id='edit-icron-usb-extender-bonding-modal']//select[@id='edit-destination-dropdown']";
	private String settingsEditBondingSaveBtn = "//div[@id='edit-icron-usb-extender-bonding-modal']//button[@id='fav-btn-save']";
	private String settingsUnbondLink = "//a[text()='Unbond']";
	private String settingsDropdownReset="//a[normalize-space()='Reset']";
	private String settingsState="(//table//tbody//tr[1]//td[4])[2]";
	
	//extender connections
	private String extenderConnectionsTab = "//div[@data-bb-tab='extender_connections']";
	private String icronConnectionSource = "//div[@class='active-source icron']//span";
	private String icronConnectionDestination = "//div[@class='active-destination icron']//span";
	private String extenderConnectionsTable = "//div[@id='extender_connections']";
	private String activeConnectionCount = "//span[@class='card-pf-aggregate-status-count']//following::span[@class='card-pf-aggregate-status-count']";
	
	// On-line Devices 
    private  String deviceOnlineCheck="(//span[@id='stat-t-1'])[1]";
    
       

    
    
	


	//extender connections
	public String getActiveConnectionCount() {
		return activeConnectionCount;
	}
	public String getMultipleSources(String icronSource) {
		return "//span[contains(text(),'" + icronSource + "')]";
	}
	
	public String getIcronConnectionDestination () {
		return icronConnectionDestination;
	}
	public String getExtenderConnectionsTable () {
		return extenderConnectionsTable;
	}
	public String getExtenderConnectionsTab() {
		return extenderConnectionsTab;
	}
	public String getIcronConnectionSource () {
		return icronConnectionSource;
	}
	
	
	//settings tab
	public String getSettingsUnbondLink () {
		return settingsUnbondLink;
	}
	public String getSettingsEditBondingSaveBtn () {
		return settingsEditBondingSaveBtn;
	}
	public String getSettingsEditBondingDestinations () {
		return settingsEditBondingDestinations;
	}
	public String getSettingsEditBondingExtenderName () {
		return settingsEditBondingExtenderName;
	}
	public String getSettingsEditBondingModal () {
		return settingsEditBondingModal;
	}
	public String getSettingsEditBonding() {
		return settingsEditBonding;
	}
	public String getExtenderNameSaveBtn () {
		return extenderNameSaveBtn;
	}
	public String getExtenderNameTextbox () {
		return extenderNameTextbox;
	}
	public String getEditExtenderNameModal() {
		return editExtenderNameModal;
	}
	public String getSettingsChangeExtendername() {
		return settingsChangeExtendername;
	}
	public String getSettingsEditNetworkSave() {
		return settingsEditNetworkSave;
	}
	public String getSettingsEditNetworkIp() {
		return settingsEditNetworkIp;
	}
	public String getSettingsEditNetworkModal() {
		return settingsEditNetworkModal;
	}
	public String getSettingsDropdownEditNetwork() {
		return settingsDropdownEditNetwork;
	}
	public String getSettingsDropdownPing() {
		return settingsDropdownPing;
	}
	public String getSettingsDropdownReset() 
	{
		return settingsDropdownReset;
	}
	public String getSettingsState() 
	{
		return settingsState;
	}
	public String getUsbDetailsCloseBtn() {
		return usbDetailsCloseBtn;
	}
	public String getUsbDetails() {
		return usbDetails;
	}
	public String getSettingsDropdownDetails() {
		return settingsDropdownDetails;
	}
	public String getSettingsDropdown() {
		return settingsDropdown;
	}
	public String getSettingsTableColumn(String index) {
		return "//div[@id='settings_table']//tr//td[" + index + "]";
	}
	public String getSettingsTableInfo() {
		return settingsTableInfo;
	}
	public String getSettingsSearchbox() {
		return settingsSearchbox;
	}
	public String getSettingsTab() {
		return settingsTab;
	}
	public String getSettingsTable() {
		return settingsTable;
	}
	
	
	//discovery tab
	public String getEditBondingExtName() {
		return editBondingExtName;
	}
	public String getDiscoveryTableColumn(String index) {
		return "//div[@id='discovery_table']//tr//td[" + index + "]";
	}
	
	public String getEditBondingSaveBtn() {
		return editBondingSaveBtn;
	}
	public String getEditBondingDestinations() {
		return editBondingDestinations;
	}
//	public String getEditBondingExtName() {
//		return editBondingExtName;
//	}
	public String getEditBondingModel() {
		return editBondingModel;
	}
	public String getEditBondingLink() {
		return editBondingLink;
	}
	public String getSearchedDeviceBondedStatus() {
		return searchedDeviceBondedStatus;
	}
	public String getSearchedDeviceIp() {
		return searchedDeviceIp;
	}
	public String getEditNetworkSpinner() {
		return editNetworkSpinner;
	}
	public String getDiscoveryEditNetworkSaveBtn() {
		return discoveryEditNetworkSaveBtn;
	}
	public String getDiscoveryEditNetworkNetmaskTextBox() {
		return discoveryEditNetworkNetmaskTextBox;
	}
	public String getDiscoveryEditNetworkGatewayTextBox() {
		return discoveryEditNetworkGatewayTextBox;
	}
	public String getDiscoveryEditNetworkIpAddressTextBox() {
		return discoveryEditNetworkIpAddressTextBox;
	}
	public String getDiscoveryEditNetworkModal() {
		return discoveryEditNetworkModal;
	}
	public String getDiscoveryEditNetworkLink() {
		return discoveryEditNetworkLink;
	}
	public String getDiscoveryDeviceDropdown() {
		return discoveryDeviceDropdown;
	}
	public String getDiscoveryTableInfo() {
		return discoveryTableInfo;
	}
	public String getDiscoveryTableSearchBox() {
		return discoveryTableSearchBox;
	}
	public String getDiscoveryTable() {
		return discoveryTable;
	}
	public String getDiscoveryTab() {
		return discoveryTab;
	}
	public String getToastMessage() {
		return toastMessage;
	}
	public String getDiscoverButton() {
		return discoverButton;
	}
	public String getDeviceOnlineCount() 
	{
		return deviceOnlineCheck;
	}
	public String getdiscoveryDevicedetails() 
	{
		return discoveryDevicedetails;
	}
	
}
