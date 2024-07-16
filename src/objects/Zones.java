package objects;

public class Zones {

	private static String addZoneButton = "//button[contains(text(),'Add zone')]";
	private static String addZoneModal = "//div[@id='add-zone-modal']";
	private static String addZoneZoneNameTB = "//input[@id='zone-name']";
	private static String addZoneZoneDescriptionTB = "//div[@class='col-sm-8']//textarea";
	private static String addZoneSaveButton = "//button[contains(text(),'Save')]";
	private static String zoneDashboardLink = "//li[@id='menu-Zones']//a";
	private static String detailsHeader = "//span[contains(text(),'Details')]//following::span[1]";
	private static String detailsZoneName = "//h3[contains(text(),'Name')]//following::p[1]";
	private static String detailsZoneDescription = "//h3[contains(text(),'Description')]//following::p[1]";
	private static String detailsZoneUsers = "//h4[contains(text(),'Users')]";
	private static String detailsZoneConnections = "//h4[contains(text(),'Connections:')]";
	private static String detailsZoneDevices = "//h4[contains(text(),'Devices:')]";
	private static String availableConnectionsSearch = "//div[@class='connections-body']//div[@class='col-md-5']//input[@placeholder='search...']";
	private static String connectionsApplyButton = "//div[@class='connections-body']//div//button[@class='btn btn-primary'][contains(text(),'Apply')]";
	private static String devicesApplyButton = "(//button[@class='btn btn-primary'][normalize-space()='Apply'])[3]";
	private static String editNameTextBox = "//input[@name='name']";
	private static String editDescriptionTextBox = "//textarea[@name='description']";
	private static String editZoneApplyButton = "//div[@class='details-body']//button[@class='btn btn-primary'][contains(text(),'Apply')]";
	private static String editZoneFromDescriptionButton = "//span[contains(text(),'Edit')]";
	private static String favouriteModal = "//div[@id='favorites-modal']";
	private static String favouriteModalCloseButton = "//div[@id='favorites-modal']//button[@class='btn btn-default'][contains(text(),'Close')]";
	
	public static String getFavouriteModalCloseButton() {
		return favouriteModalCloseButton;
	}
	public static String getFavouriteModal() {
		return favouriteModal;
	}
	public static String getUserNameFromZoneFav(String username) {
		return "//div[contains(text(),'" + username + "')]";
	}
	public static String getUserFavouriteButton(String username) {
		return "//button[@class='btn btn-default']//span[contains(text(),'Favorites')]";
	}
	public static String getEditZoneFromDescriptionButton() {
		return editZoneFromDescriptionButton;
	}
	public static String getEditZoneApplyButton() {
		return editZoneApplyButton;
	}
	public static String getEditNameTextBox() {
		return editNameTextBox;
	}
	public static String getEditDescriptionTextBox() {
		return editDescriptionTextBox;
	}
	public static String getDevicesApplyButton() {
		return devicesApplyButton;
	}
	
	public static String getConnectionsApplyButton() {
		return connectionsApplyButton;
	}
	public static String getActiveDevices(String name) {
		return "//div[@class='zone-devices']//following::h4[contains(text(),'Selected')]//following::div[@id='device-item-" + name + "']";
	}
	public static String getAvailableDevices(String name) {
		return "//div[@class='zone-devices']//following::h4[contains(text(),'Selected')]//following::div[@id='device-item-" + name + "']";
	}
	public static String getActiveConnection(String name) {
		return "//h4[contains(text(), 'Selected')]//following::div[@id='connection-item-" + name + "']";
	}
	public static String getAvailableConnection(String name) {
//		return "//h4[contains(text(), 'Available')]//following::div[@id='connection-item-" + name + "']";
		return "//h4[contains(text(), 'Selected')]//following::div[@id='connection-item-" + name + "']";
	}
	public static String getAvailableConnectionsSearch() {
		return availableConnectionsSearch;
	}
	public static String getDetailsZoneDevices() {
		return detailsZoneDevices;
	}
	public static String getDetailsZoneConnections() {
		return detailsZoneConnections;
	}
	public static String getDetailsZoneUsers() {
		return detailsZoneUsers;
	}
	public static String getDetailsZoneName() {
		return detailsZoneName;
	}
	
	public static String getZoneDashboardLink() {
		return zoneDashboardLink;
	}
	public static String getAddZoneButton() {
		return addZoneButton;
	}
	public static String getAddZoneModal() {
		return addZoneModal;
	}
	public static String getAddZoneZoneNameTB() {
		return addZoneZoneNameTB;
	}
	public static String getAddZoneZoneDescriptionTB() {
		return addZoneZoneDescriptionTB;
	}
	public static String getAddZoneSaveButton() {
		return addZoneSaveButton;
	}
	
	public static String getAvailableZone(String zoneName) {
		return "//div[@id='zone-" + zoneName + "']";
		
	}
	public static String getZoneEditButton(String zoneName) {
		return "//div[@class='selector-container']//following-sibling::span[contains(text(),'" + zoneName + "')]//following::button[1]";
	}
	
	public static String getZoneDeleteButton(String zoneName) {
		return "//div[@class='selector-container']//following-sibling::span[contains(text(),'" + zoneName + "')]//following::button[2]";
	}
	public static String getDetailsHeader() {
		return detailsHeader;
	}
	public static String getDetailsZoneDescription() {
		return detailsZoneDescription;
	}
	
	public static String getConnectionTowerZone(String id) {
		return "//div[@class='connections-body']//div[@class='tower-filter-item'][contains(text(),'" + id + "')]";
	}
	public static String getDeviceTowerZone(String id) {
		return "//div[@class='zone-devices']//div[@class='tower-filter-item'][contains(text(),'" + id + "')]";
	}


	
}
