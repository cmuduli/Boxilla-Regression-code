package methods;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.Device;
import extra.SeleniumActions;
import extra.StartupTestCase2;
import northbound.delete.TerminateActiveConnection;
import objects.Connections;
import objects.Discovery;
import objects.Switch;
import objects.Users;
import objects.Zones;

/**
 * This class contains all the methods for interacting with the zone pages in Boxilla
 * @author Boxilla
 *
 */
public class ZoneMethods {


	final static Logger log = Logger.getLogger(ZoneMethods.class);

	/**
	 * Navigates to the zone page from anywhere in Boxilla
	 * @param driver
	 */
	public void navigateToZones(WebDriver driver) {
		log.info("Navigating to zones");
		SeleniumActions.seleniumClick(driver, Zones.getZoneDashboardLink());
		new WebDriverWait(driver, 120).until(ExpectedConditions.elementToBeClickable(By.xpath(Zones.getAddZoneButton())));
		log.info("Successfully navigated to Zones");
	}

	/**
	 * Creates a zone in Boxilla and asserts zone is available
	 * @param driver
	 * @param name
	 * @param description
	 * @throws InterruptedException 
	 */
	public void addZone(WebDriver driver, String name, String description) throws InterruptedException {
		log.info("Adding new zone with name:" + name);
		navigateToZones(driver);
		SeleniumActions.seleniumClick(driver, Zones.getAddZoneButton());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getAddZoneModal())));
		SeleniumActions.seleniumSendKeysClear(driver, Zones.getAddZoneZoneNameTB());
		Thread.sleep(2000);
		SeleniumActions.seleniumSendKeys(driver, Zones.getAddZoneZoneNameTB(), name);
		
		SeleniumActions.seleniumSendKeysClear(driver, Zones.getAddZoneZoneDescriptionTB());
		
		SeleniumActions.seleniumSendKeys(driver, Zones.getAddZoneZoneDescriptionTB(), description);
		Thread.sleep(3000);
		SeleniumActions.seleniumClick(driver, Zones.getAddZoneSaveButton());
		Thread.sleep(3000);
		boolean available = isZoneAvailable(driver, name);
		Assert.assertTrue(available, "Zone with name:" + name + "was not created");
	}

	/**
	 * Returns the given zones ID from Boxilla UI
	 * @param driver
	 * @param zoneName
	 * @return
	 */
	public String getZoneId(WebDriver driver, String zoneName) {
		String  id = SeleniumActions.seleniumGetText(driver, Zones.getAvailableZone(zoneName));
		//need to parse the return
		char first = id.charAt(0);
		id = Character.toString(first);
		log.info("Zone ID for zone:" + zoneName + " is ID:" + id);
		return id;
	}

	/**
	 * Returns true if zone is available in Boxilla else false
	 * @param driver
	 * @param zoneName
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isZoneAvailable(WebDriver driver, String zoneName) throws InterruptedException {
		navigateToZones(driver);
		Thread.sleep(3000);
		boolean isAvailable = SeleniumActions.seleniumIsDisplayed(driver, Zones.getAvailableZone(zoneName));
		log.info("Zone is available:" + isAvailable);
		return isAvailable;
	}

	/**
	 * Edit a zones name and description
	 * @param driver
	 * @param name
	 * @param newName
	 * @param newDescription
	 * @throws InterruptedException 
	 */
	public void editZone(WebDriver driver, String name, String newName, String newDescription) throws InterruptedException {
		navigateToZones(driver);
		Thread.sleep(3000);
		if(isZoneAvailable(driver, name)) {
			SeleniumActions.seleniumClick(driver, Zones.getZoneEditButton(name));
			SeleniumActions.seleniumSendKeysClear(driver, Zones.getEditNameTextBox());
			SeleniumActions.seleniumSendKeys(driver, Zones.getEditNameTextBox(), newName);
			SeleniumActions.seleniumSendKeysClear(driver, Zones.getEditDescriptionTextBox());
			SeleniumActions.seleniumSendKeys(driver, Zones.getEditDescriptionTextBox(), newDescription);
			SeleniumActions.seleniumClick(driver, Zones.getEditZoneApplyButton());	
			Thread.sleep(5000);
		}else {
			throw new AssertionError("Zone " + name + " is not available");
		}

	}

	public void editZoneFromDescription(WebDriver driver, String name, String newName, String newDescription) throws InterruptedException, AssertionError {
		navigateToZones(driver);
		if(isZoneAvailable(driver, name)) {
			SeleniumActions.seleniumClick(driver, Zones.getAvailableZone(name));
			SeleniumActions.seleniumClick(driver, Zones.getEditZoneFromDescriptionButton());
			SeleniumActions.seleniumSendKeysClear(driver, Zones.getEditNameTextBox());
			SeleniumActions.seleniumSendKeys(driver, Zones.getEditNameTextBox(), newName);
			SeleniumActions.seleniumSendKeysClear(driver, Zones.getEditDescriptionTextBox());
			SeleniumActions.seleniumSendKeys(driver, Zones.getEditDescriptionTextBox(), newDescription);
			SeleniumActions.seleniumClick(driver, Zones.getEditZoneApplyButton());	
		}else {
			throw new AssertionError("Zone " + name + " is not available");
		}
	}

	public void clickZoneSelectionTowerConnection(WebDriver driver, String zoneName) {
		String id = getZoneId(driver, zoneName);
		SeleniumActions.seleniumClick(driver, Zones.getConnectionTowerZone(id));
	}

	public void clickZoneSelectionTowerDevice(WebDriver driver, String zoneName) {
		String id = getZoneId(driver, zoneName);
		SeleniumActions.seleniumClick(driver, Zones.getDeviceTowerZone(id));
	}

	/**
	 * Deletes a zone from Boxilla
	 * @param driver
	 * @param name
	 * @throws InterruptedException 
	 */
	public void deleteZone(WebDriver driver, String name) throws InterruptedException {
		navigateToZones(driver);
		if(isZoneAvailable(driver, name)) {
			SeleniumActions.seleniumClick(driver, Zones.getZoneDeleteButton(name));
		}else {
			throw new AssertionError("Zone " + name + " is not available");
		}
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(3000);
//		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
//		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		boolean isDeleted = isZoneAvailable(driver, name);
		Assert.assertFalse(isDeleted, "Zone was not deleted");
		log.info("Zone " + name + " is deleted");
	}

	/**
	 * Attempts to delete a zone with devices or connections attached. Asserts error toast
	 * @param driver
	 * @param name
	 * @throws AssertionError 
	 * @throws InterruptedException 
	 */
	public void deleteZoneUnable(WebDriver driver, String name) throws InterruptedException, AssertionError {
		navigateToZones(driver);
		if(isZoneAvailable(driver, name)) {
			SeleniumActions.seleniumClick(driver, Zones.getZoneDeleteButton(name));
		}else {
			throw new AssertionError("Zone " + name + " is not available");
		}
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toast = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toast.equals("Zone has devices or connections assigned. Removal forbidden."));

	}

	/**
	 * Attempt to add a zone when the maximum allowed zones are already created. Returns toast notification
	 * @param driver
	 * @return
	 */
	public String addZoneLimitReached(WebDriver driver) {
		navigateToZones(driver);
		SeleniumActions.seleniumClick(driver, Zones.getAddZoneButton());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toast = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		return toast;
	}

	/**
	 * Returns an array containing the details of a given zone.
	 * Index is
	 * 0 - zone name
	 * 1 - zone description
	 * 2 - zone users
	 * 3 - zone connections
	 * 4 - zone devices
	 * @param driver
	 * @param name
	 * @return
	 * @throws InterruptedException 
	 */
	public String[] getZoneDetails(WebDriver driver, String name) throws InterruptedException {
		boolean isAvailable = isZoneAvailable(driver, name);
		if(isAvailable) {
			String[] details = new String[5];
			SeleniumActions.seleniumClick(driver, Zones.getAvailableZone(name));
			Thread.sleep(3000);
			details[0] = SeleniumActions.seleniumGetText(driver, Zones.getDetailsZoneName());
			details[1] = SeleniumActions.seleniumGetText(driver, Zones.getDetailsZoneDescription());
			//users does not come out clean so we need to parse
			String users = SeleniumActions.seleniumGetText(driver, Zones.getDetailsZoneUsers());
			details[2] = users.substring(users.length() - 1);
			//connections does not come out clean so we need to parse
			String connections = SeleniumActions.seleniumGetText(driver, Zones.getDetailsZoneConnections());
			details[3] = connections.substring(connections.length() -1);
			//devices does not come out clean so need to parse
			String devices = SeleniumActions.seleniumGetText(driver, Zones.getDetailsZoneDevices());
			details[4] = devices.substring(devices.length() - 1);
			log.info("zone name:" + details[0]);
			log.info("zone description:" + details[1]);
			log.info("Zone users:" + details[2]);
			log.info("Zone connections:" + details[3]);
			log.info("Zone devices:" + details[4]);
			return details;
		}else {
			throw new AssertionError("Zone " + name + " is not available");
		}
	}

	/**
	 * Add a number of devices to a zone
	 * @param driver
	 * @param zoneName
	 * @param devices
	 * @throws InterruptedException 
	 */
	public void addDevicesToZone(WebDriver driver, String zoneName, String... devices) throws InterruptedException {
		String [] details = getZoneDetails(driver, zoneName);
		Assert.assertTrue(details[0].equals(zoneName), "Zone details did not match");

		for(String s : devices) {
			SeleniumActions.seleniumClick(driver, Zones.getAvailableDevices(s));
			Assert.assertTrue(isDeviceInActive(driver, s), "Device is not active");
		}
		log.info("Device has been added to active list.. Saving");
		SeleniumActions.seleniumClick(driver, Zones.getDevicesApplyButton());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Devices reassigned."), "Toast message was not Devices reassigned.  Actual:" + toastText);
		for(String s : devices) {
			Assert.assertTrue(isDeviceInActive(driver, s), "Device is not active");
		}
	}

	/**
	 * Remove a number of devices from a zone
	 * @param driver
	 * @param zoneName
	 * @param devices
	 * @throws InterruptedException 
	 */
	public void removeDevicesFromZone(WebDriver driver, String zoneName, String... devices) throws InterruptedException {
		String [] details = getZoneDetails(driver, zoneName);
		Assert.assertTrue(details[0].equals(zoneName), "Zone details did not match");

		for(String s : devices) {
			SeleniumActions.seleniumClick(driver, Zones.getActiveDevices(s));
//			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getAvailableDevices(s))));
		}
		log.info("Device(s) have been added to the available list");
		SeleniumActions.seleniumClick(driver, Zones.getDevicesApplyButton());
		Thread.sleep(3000);
//		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Devices reassigned."), "Toast message was not Devices reassigned.  Actual:" + toastText);
		for(String s : devices) {
			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getAvailableDevices(s))));
		}
	}

	/**
	 * Swap a number of devices from one zone to another
	 * @param driver
	 * @param origZone - Name of zone to remove device from
	 * @param newZone - Name of zone to add device to
	 * @param deviceName
	 * @throws InterruptedException 
	 */
	public void swapDeviceFromZone(WebDriver driver, String origZone, String newZone, String... deviceName) throws InterruptedException {
		getZoneDetails(driver, newZone);
		clickZoneSelectionTowerDevice(driver, origZone);
		for(String s : deviceName) {
			SeleniumActions.seleniumClick(driver, Zones.getAvailableDevices(s));
			Assert.assertTrue(isDeviceInActive(driver, s), "Device is not active");
		}
		log.info("Device has been added to active list.. Saving");
		SeleniumActions.seleniumClick(driver, Zones.getDevicesApplyButton());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Devices reassigned."), "Toast message was not Devices reassigned.  Actual:" + toastText);
	}

	/**
	 * Swap a nuimber of connection from one zone to another
	 * @param driver
	 * @param origZone - The name of the zone to remove the connection from
	 * @param newZone - The name of the zone to add the connection to
	 * @param connectionName
	 * @throws InterruptedException 
	 */
	public void swapConnectionFromZone(WebDriver driver, String origZone, String newZone, String... connectionName) throws InterruptedException {
		getZoneDetails(driver, newZone);
		clickZoneSelectionTowerConnection(driver, origZone);
		for(String s : connectionName) {
			SeleniumActions.seleniumClick(driver, Zones.getAvailableConnection(s));
			Assert.assertTrue(isConnectionInActive(driver, s ), "Connection was not active" );
		}
		SeleniumActions.seleniumClick(driver, Zones.getConnectionsApplyButton());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Connections reassigned."), "Toast message was not Connections reassigned.  Actual:" + toastText);
	}

	/**
	 * Adds connections to a zone
	 * @param driver
	 * @param zoneName
	 * @param connectionName
	 * @throws InterruptedException 
	 */
	public void addConnectionsToZone(WebDriver driver, String zoneName, String... connectionName) throws InterruptedException {
		String [] details = getZoneDetails(driver, zoneName);
		Assert.assertTrue(details[0].equals(zoneName), "Zone details did not match");

		for(String s : connectionName) {
			SeleniumActions.seleniumClick(driver, Zones.getAvailableConnection(s));
			Assert.assertTrue(isConnectionInActive(driver, s ), "Connection was not active" );
		}
		log.info("Connection has been added to active list. Saving");
		SeleniumActions.seleniumClick(driver, Zones.getConnectionsApplyButton());
//		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		Thread.sleep(3000);
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Connections reassigned."), "Toast message was not Connections reassigned.  Actual:" + toastText);
		for(String s : connectionName) {
			Assert.assertTrue(isConnectionInActive(driver, s ), "Connection was not active" );	
		}
	}

	/**
	 * Returns true if a connection in a zone is in the active table on the zone page
	 * @param driver
	 * @param name
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isConnectionInActive(WebDriver driver, String name) throws InterruptedException {
		boolean isAvail = SeleniumActions.seleniumIsDisplayed(driver, Zones.getAvailableConnection(name));
		System.out.println(isAvail);
		boolean isActive = SeleniumActions.seleniumIsDisplayed(driver, Zones.getActiveConnection(name));
		System.out.println(isActive);
//		Thread.sleep(2000);
		if(isAvail && isActive) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if a device in a zone is in the active table on the zone page
	 * @param driver
	 * @param name
	 * @return
	 */
	public boolean isDeviceInActive(WebDriver driver, String name) {
		boolean isAvail = SeleniumActions.seleniumIsDisplayed(driver, Zones.getAvailableDevices(name));
		boolean isActive = SeleniumActions.seleniumIsDisplayed(driver, Zones.getActiveDevices(name));
		if(isAvail && isActive) {
			return true;
		}
		return false;
	}

	/**
	 * Returns true if a given device is assigned to a zone, else false
	 * @param zoneName
	 * @param deviceName
	 * @param driver
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isDeviceAssignedToZone(String zoneName, String deviceName, WebDriver driver) throws InterruptedException {
		getZoneDetails(driver, zoneName);
		return isDeviceInActive(driver, deviceName);
	}

	/**
	 * Returns true if a connection is assigned to a zone, else false
	 * @param zoneName
	 * @param connectionName
	 * @param driver
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isConnectionAssignedToZone(String zoneName, String connectionName, WebDriver driver) throws InterruptedException {
		getZoneDetails(driver, zoneName);
		return isConnectionInActive(driver, connectionName);


	}

	/**
	 * Removes connections from a zone
	 * @param driver
	 * @param zoneName
	 * @param connectionName
	 * @throws InterruptedException 
	 */
	public void removeConnectionsFromZone(WebDriver driver, String zoneName, String... connectionName) throws InterruptedException {
		String [] details = getZoneDetails(driver, zoneName);
		Assert.assertTrue(details[0].equals(zoneName), "Zone details did not match");
		for(String s : connectionName) {
			SeleniumActions.seleniumClick(driver, Zones.getActiveConnection(s));
			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getAvailableConnection(s))));
		}
		log.info("Connection has been added to available list. Saving");
		SeleniumActions.seleniumClick(driver, Zones.getConnectionsApplyButton());
		Thread.sleep(2000);
//		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastText = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastText.equals("Connections reassigned."), "Toast message was not Connections reassigned.  Actual:" + toastText);
		for(String s : connectionName) {
			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getAvailableConnection(s))));
		}
	}

	/**
	 * Returns true if user favourites are in a zone
	 * @param driver
	 * @param zoneName
	 * @param username
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isUserFavouritesInZone(WebDriver driver, String zoneName, String username) throws InterruptedException {
		getZoneDetails(driver, zoneName);
		boolean isAvail = SeleniumActions.seleniumIsDisplayed(driver, Zones.getUserNameFromZoneFav(username));
		return isAvail;
	}

	/**
	 * Navigates to zone user favourites
	 * @param driver
	 * @param zoneName
	 * @param username
	 * @throws InterruptedException 
	 */
	public void navigateToUserFavourites(WebDriver driver, String zoneName, String username) throws InterruptedException {
		boolean isAvailable = isUserFavouritesInZone(driver, zoneName, username);

		if(isAvailable) {
			SeleniumActions.seleniumClick(driver, Zones.getUserFavouriteButton(username));
			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Zones.getFavouriteModal())));

		}
	}
	/**
	 * Sets user favourites from the zone page
	 * @param driver
	 * @param zoneName
	 * @param username
	 * @param connectionName
	 * @param slot
	 * @throws InterruptedException 
	 */
	public void setUserFavourite(WebDriver driver, String zoneName, String username, String connectionName, int slot) throws InterruptedException {
		navigateToUserFavourites( driver,  zoneName,  username);
		Users.getfavouritehotkey(driver,slot).clear();
		Users.getfavouritehotkey(driver,slot).sendKeys(connectionName);
//		SeleniumActions.seleniumDropdown(driver, Users.getFavouriteDropdown(slot), connectionName);
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Zones.getFavouriteModal())));
	}

	/**
	 * Gets all user favourties for a given user and zone
	 * @param driver
	 * @param zoneName
	 * @param username
	 * @return
	 * @throws InterruptedException 
	 */
	public String[] getUserFavourites(WebDriver driver, String zoneName, String username) throws InterruptedException {
		navigateToUserFavourites( driver,  zoneName,  username);
		String[] connectionList = new String[10];
		for(int j=0; j < connectionList.length; j++) {
			WebElement e = driver.findElement(By.xpath(Users.getFavouriteDropdown(j)));
			Select select = new Select(e);
			WebElement option = select.getFirstSelectedOption();
			connectionList[j] =option.getText();
			log.info("Slot:" + j +  ", Connection:" + connectionList[j]);
		}
		SeleniumActions.seleniumClick(driver, Zones.getFavouriteModalCloseButton());
		return connectionList;
	}
	public String[] getUserFavourites1(WebDriver driver, String zoneName,int favName, String username) throws InterruptedException {
		navigateToUserFavourites( driver,  zoneName,  username);
		Thread.sleep(4000);
		String[] connectionList = new String[10];
		for(int j=0; j < connectionList.length; j++) {
			connectionList[j] =Users.getfavouritehotkey(driver,favName).getAttribute("value");
			log.info("Slot:" + j +  ", Connection:" + connectionList[j]);
		}
		SeleniumActions.seleniumClick(driver, Zones.getFavouriteModalCloseButton());
		return connectionList;
	}
	
	public void unallocateConnection(WebDriver driver, String zoneName, String username, int slot) throws InterruptedException {
		navigateToUserFavourites( driver,  zoneName,  username);
	    Users.getfavouritehotkey(driver, slot).clear();
//		SeleniumActions.seleniumDropdown(driver, Users.getFavouriteDropdown(slot), "unallocated");
		SeleniumActions.seleniumClick(driver, Users.favouritesPopupSave);
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Zones.getFavouriteModal())));
	}




}
