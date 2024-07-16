package methods;

import java.util.List;
import java.util.PropertyResourceBundle;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import extra.SeleniumActions;
import invisaPC.Rest.ActiveConnections;
import objects.Devices;
import objects.Landingpage;
import objects.Peripherals;

public class PeripheralsMethods {

	final static Logger log = Logger.getLogger(PeripheralsMethods.class);
	
	private Landingpage landingObjects = new Landingpage();
	private Peripherals perObjects = new Peripherals();
	
	public String getActiveConnectionsCount(WebDriver driver) {
		navigateToPeripherals(driver);
		log.info("Attempting to get active connections count");
		String count = SeleniumActions.seleniumGetInnerText(driver, perObjects.getActiveConnectionCount());
		log.info("Active Connections Count:" + count);
		return count;
	}
	
	public void navigateToExtenderConnections(WebDriver driver) {
		log.info("Navigating to peripherals > extender connections tab");
		navigateToPeripherals(driver);
		SeleniumActions.seleniumClick(driver, perObjects.getExtenderConnectionsTab());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getExtenderConnectionsTable())));
		log.info("Navigated to extender connection tab");
	}
	public String getIcronPrivateDestination(WebDriver driver) {
		navigateToExtenderConnections(driver);
		
		String destination = "";
		try {
			destination = SeleniumActions.seleniumGetInnerText(driver, perObjects.getIcronConnectionDestination());
		}catch(NoSuchElementException e) {
			log.info("No destination available");
		}
		log.info("Icron Destination: " + destination );
		return destination;
	}
	
	public String getIcronSharedDestinations(WebDriver driver) {
		navigateToExtenderConnections(driver);
		String names = "";
		try {
			List<WebElement> destinations = SeleniumActions.getListOfElements(driver, perObjects.getIcronConnectionDestination());
			int size = destinations.size();
			for(int j=0; j < size; j++) {
				names = names +  destinations.get(j).getAttribute("innerText");
			}
		}catch(NoSuchElementException e) {
			log.info("No destinations");
		}
		log.info("Destinations:" + names);
		return names;
	}
	
	public boolean isIcronExtenderInConnection(WebDriver driver, String sourceName) {
		navigateToExtenderConnections(driver);	
		boolean isConnected =  SeleniumActions.seleniumIsDisplayed(driver, perObjects.getMultipleSources(sourceName));
		log.info(sourceName + " is connected:" + isConnected);
		return isConnected;
	}
	
	public String getIcronSourceName(WebDriver driver) {
		navigateToExtenderConnections(driver);
		
		String icronSource = "";
		
		try {
			icronSource = SeleniumActions.seleniumGetInnerText(driver, perObjects.getIcronConnectionSource());
		}catch(NoSuchElementException e) {
			log.info("No source available");
		}
		//String icronSource = SeleniumActions.seleniumGetText(driver, perObjects.getIcronConnectionSource());
		log.info("Icron Source:" + icronSource);
		return icronSource;
	}
	
	public void settingsUnbondDevice(WebDriver driver, String deviceSearch) {
		searchSettingsTable(driver, deviceSearch);
		log.info("Unbonding device");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsUnbondLink())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsUnbondLink());
		String toast = getToastMessage(driver);
		//only 1 L in successfully in boxila. Update when bug 2071 is fixed
		Assert.assertTrue(toast.contains("Successfully unbonded device."), "Toast message did not contain successfully. Actual:" + toast);
		log.info("Device unbonded");
	}
	
	public void settingsEditBonding(WebDriver driver, String deviceSearch, String extenderName, String destination) {
		searchSettingsTable(driver, deviceSearch);
		log.info("Editing bonding from settings");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsEditBonding())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsEditBonding());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsEditBondingModal())));
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getSettingsEditBondingExtenderName());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getSettingsEditBondingExtenderName(), destination);
//		SeleniumActions.seleniumSendKeys(driver, perObjects.getSettingsEditBondingDestinations(), destination);
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsEditBondingSaveBtn());
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("successfully"), "Toast did not contain successfully. Actual:" + toast);
		log.info("Bonding edited");
		
	}
	
	public void changeExtenderName(WebDriver driver, String searchDevice, String newName) {
		searchSettingsTable(driver, searchDevice);
		log.info("editing extender name to:" + newName);
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsChangeExtendername())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsChangeExtendername());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getEditExtenderNameModal())));
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getExtenderNameTextbox());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getExtenderNameTextbox(), newName);
		SeleniumActions.seleniumClick(driver, perObjects.getExtenderNameSaveBtn());
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("success"), "Toast message did not contain success. Actual: " + toast);
		log.info("Extender name changed");	
	}
	
	public void editDeviceIpSettings(WebDriver driver, String deviceSearch, String newIp) {
		searchSettingsTable(driver, deviceSearch);
		log.info("Editing device IP");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsDropdownEditNetwork())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdownEditNetwork());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsEditNetworkModal())));
		log.info("Edit network settings modal opened. Changing IP");
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getSettingsEditNetworkIp());
		SeleniumActions.seleniumSendKeys(driver,  perObjects.getSettingsEditNetworkIp(), newIp);
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsEditNetworkSave());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getEditNetworkSpinner())));
		log.info("Spinner found. Waiting for spinner to disappear and checking toast message");
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(perObjects.getEditNetworkSpinner())));
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("successfully"), "Toast message was not a success, actual:" + toast);	
		log.info("Device IP changed");
	}
	
	public String getDeviceDetails(WebDriver driver, String deviceSearch) {
		searchSettingsTable(driver, deviceSearch);
		log.info("Getting searched device details");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsDropdownDetails())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdownDetails());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getUsbDetails())));
		String text = SeleniumActions.seleniumGetText(driver, perObjects.getUsbDetails());
		log.info("Device Detail text:" + text);
		SeleniumActions.seleniumClick(driver, perObjects.getUsbDetailsCloseBtn());
		log.info("Device details returned");
		return text;	
	}
	
	public void pingDevice(WebDriver driver, String deviceSearch) {
		searchSettingsTable(driver, deviceSearch);
		log.info("pinging device");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsDropdownPing())));
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdownPing());
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("Ping OK"), "Toast message did not include Ping OK, actual:" + toast);
		log.info("device pinged");
		
	}
	public void resetDevice(WebDriver driver,String deviceSearch) throws InterruptedException 
	{
		boolean isState = false;
		int retry = 0;
		searchSettingsTable(driver, deviceSearch);
		log.info("reset device");
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsDropdown());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsDropdownPing())));
		SeleniumActions.seleniumClick(driver,perObjects.getSettingsDropdownReset());
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("Successfully reset extender:"), "Toast message did not include Sucessfully reset extender, actual:" + toast);
		log.info("device reset");
		String deviceState=SeleniumActions.seleniumGetText(driver,perObjects.getSettingsState());
//		Assert.assertTrue(deviceState.contains("Offline"),"Device state did not contain offline, actual device state is:" + deviceState);
		Thread.sleep(5000);
		while (!isState && retry < 5) 
		{
			log.info("Checking for device state ");
           if (deviceState.contains("Online"))
           {
        	   isState=true;
        	   log.info("Device State is online");
        	   break;
			
		} else {
			log.info("Device State is "+ deviceState );
			log.info("waiting and retrying...");
			Thread.sleep(5000);
			driver.navigate().refresh();
			retry++;
			if(retry >=5)
				throw new SkipException("***** Device State not found Online *****");

			

		}			
		}
		
	}
	public String[] getAllIcronIp(WebDriver driver, String[] macAddresses) throws InterruptedException {
		String[] ipaddresses = new String[3];
		
		for(int j=0; j < 3; j++) {
		ipaddresses[j] = getDiscovertedDeviceDetails(driver, macAddresses[j], Peripherals.DISCOVERY.IP);
		log.info("IP Address for device with MAC " + macAddresses[j] + " :" + ipaddresses[j]);
		}
		
		return ipaddresses;
	}
	public String getDiscovertedDeviceDetails(WebDriver driver, String deviceSearch, Peripherals.DISCOVERY column) throws InterruptedException {
		searchDiscoveryTable(driver, deviceSearch);
		String text = "";
		
		switch(column) {
			case TYPE :
				log.info("Getting extender type from discovery table");
				text = SeleniumActions.seleniumGetInnerText(driver, perObjects.getDiscoveryTableColumn("1"));
				break;
			case MAC : 
				log.info("Getting extender MAC from discovery table");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getDiscoveryTableColumn("2"));
				break;
			case IP :
				log.info("Getting extender device IP from discovery table");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getDiscoveryTableColumn("3"));
				break;
			case STATE :
				log.info("Getting extender state from discovery table");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getDiscoveryTableColumn("8"));
				break;
		}
		log.info("Returned value:" + text);
		return text;
	}
	
	public String getBondedDeviceDetails(WebDriver driver, String deviceSearch, Peripherals.SETTINGS column) {
		searchSettingsTable(driver, deviceSearch);
		String text = "";
		switch(column) {
			case EXT_NAME : 			//get text not working for extendder name. Get inner text instead
				log.info("Getting extender name");
				text = SeleniumActions.seleniumGetInnerText(driver, perObjects.getSettingsTableColumn("1"));
				break;
			case TYPE:
				log.info("Getting extender type");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("2"));
				break;
				
			case MAC:
				log.info("Getting extender MAC");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("3"));
				break;
				
			case STATE:
				log.info("Getting extender state");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("4"));
				break;
				
			case IP :
				log.info("Getting extender IP");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("6"));
				break;
				
			case BONDED_DEVICE :
				log.info("Getting extender bonded device name");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("7"));
				break;
				
			case BONDED_DEVICE_IP:
				log.info("Getting bonded devices IP address");
				text = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableColumn("8"));
				break;
		}
		log.info("returned value:" + text);
		return text;
	}
	
	
	
	public void navigateToSettingsTab(WebDriver driver) {
		log.info("Navigating to Peripherals > settings tab");
		navigateToPeripherals(driver);
		SeleniumActions.seleniumClick(driver, perObjects.getSettingsTab());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getSettingsTable())));
		log.info("Successfully navigated to settings tab");
	}
	
	/**
	 * Searches the discovery table for device using deviceDetails. 
	 * Checks the table state column. If Bonded returns true. If unbonded
	 * returns false. If null or empty throws an error
	 * @param driver
	 * @param deviceDetails
	 * @return
	 * @throws InterruptedException 
	 */
	public boolean isBonded(WebDriver driver, String deviceDetails) throws InterruptedException {
		searchDiscoveryTable(driver,  deviceDetails);
		log.info("Getting device bonded status from discovery table with device details:" + deviceDetails);
		String bonded = SeleniumActions.seleniumGetText(driver, perObjects.getSearchedDeviceBondedStatus());
		if(bonded == null || (!bonded.equals("Unbonded") && !bonded.equals("Bonded"))) {
			throw new AssertionError("Unknown bonded state");
		}else {
			log.info("Device state is:" + bonded);
			if(bonded.equals("Unbonded" )) {
				return false;
			}else if(bonded.equals("Bonded")) {
				return true;
			}
		}
		log.info("Could not get device state. Returning unbonded");
		return false;
	}
	
	public String getSearchedDeviceIp(WebDriver driver, String deviceDetails) throws InterruptedException {
		searchDiscoveryTable(driver,  deviceDetails);
		log.info("Getting device IP from discovery table with device details:" + deviceDetails);
		String deviceIp = SeleniumActions.seleniumGetText(driver, perObjects.getSearchedDeviceIp());
		if(deviceIp == null || deviceIp.equals("")) {
			throw new AssertionError("Device IP was null or empty string");
		}else {
			log.info("Device IP: " + deviceIp);
			return deviceIp;
		}
		
	}
	public void searchSettingsTable(WebDriver driver, String searchDetails) {
		navigateToSettingsTab(driver);
		log.info("Searching settings table for:" + searchDetails);
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getSettingsSearchbox());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getSettingsSearchbox(), searchDetails);
		String amountOfSearchResults = SeleniumActions.seleniumGetText(driver, perObjects.getSettingsTableInfo());
		int numberOfResults = Integer.parseInt(amountOfSearchResults);
		log.info("Number of results returned:" + numberOfResults);
		Assert.assertTrue(numberOfResults==1, "Number of search results did not equal 1, actual:" + numberOfResults);
	}
	
	public void searchDiscoveryTable(WebDriver driver, String deviceDetails) throws InterruptedException {
		
		boolean isFound = false;
		int retry = 0;
		while (!isFound && retry < 5)
		{
			discover(driver);
			log.info("Searching discovery table");
			SeleniumActions.seleniumClick(driver, perObjects.getDiscoveryTab());
			new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getDiscoveryTable())));
			Thread.sleep(3000);
			log.info("Page is on the discovery tab. Searching for " + deviceDetails);
			SeleniumActions.seleniumSendKeysClear(driver, perObjects.getDiscoveryTableSearchBox());
			SeleniumActions.seleniumSendKeys(driver, perObjects.getDiscoveryTableSearchBox(), deviceDetails);
			log.info("Device searched using MAC address");
			if ( SeleniumActions.seleniumGetText(driver, perObjects.getdiscoveryDevicedetails()).contains(deviceDetails)) {
				isFound = true;
				log.info("Device found with given MAC Address.. Checking State...");
				String amountOfSearchResults = SeleniumActions.seleniumGetText(driver, perObjects.getDiscoveryTableInfo());
				int numberOfResults = Integer.parseInt(amountOfSearchResults);
				log.info("Number of results returned:" + numberOfResults);
				Assert.assertTrue(numberOfResults==1, "Number of search results did not equal 1, actual:" + numberOfResults);
				break;
			}
			
			else {
				log.info("Device not found");
				log.info("waiting and retrying...");
				Thread.sleep(5000);
				discover(driver);
				retry++;
				if(retry >=5)
					throw new SkipException("***** Device not found *****");

			}
			
		}		
	}
	
	
	
	public void editBonding(WebDriver driver, String deviceDetails, String extName, String destination) throws InterruptedException {
		searchDiscoveryTable(driver, deviceDetails);
		log.info("Editing bonding from discovery page");
		SeleniumActions.seleniumClick(driver, perObjects.getDiscoveryDeviceDropdown());
		SeleniumActions.seleniumClick(driver, perObjects.getEditBondingLink());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getEditBondingModel())));
		log.info("Modal is visible. Entering details");
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getEditBondingExtName());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getEditBondingExtName(), extName);
		SeleniumActions.seleniumSendKeys(driver, perObjects.getEditBondingDestinations(), destination);
		log.info("Details entered. Clicking save and getting toast message");
		SeleniumActions.seleniumClick(driver, perObjects.getEditBondingSaveBtn());
		Thread.sleep(3000);
		String toast = getToastMessage(driver);
		Assert.assertTrue(toast.contains("successfully bonded"), "Toast was not successful, actual:" + toast);
		
		
	}
	
	public void editDeviceNetworkDiscover(WebDriver driver, String deviceDetails, String newIP, String newGateway, String newNetmask) throws InterruptedException {
		searchDiscoveryTable(driver, deviceDetails);
		log.info("Editing Icron device network on discover table");
		SeleniumActions.seleniumClick(driver, perObjects.getDiscoveryDeviceDropdown());
		SeleniumActions.seleniumClick(driver, perObjects.getDiscoveryEditNetworkLink());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getDiscoveryEditNetworkModal())));
		
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getDiscoveryEditNetworkIpAddressTextBox());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getDiscoveryEditNetworkIpAddressTextBox(), newIP);
		
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getDiscoveryEditNetworkGatewayTextBox());
		SeleniumActions.seleniumSendKeys(driver, perObjects.getDiscoveryEditNetworkGatewayTextBox(), newGateway);
		
		SeleniumActions.seleniumSendKeysClear(driver, perObjects.getDiscoveryEditNetworkNetmaskTextBox());
		SeleniumActions.seleniumSendKeys(driver,perObjects.getDiscoveryEditNetworkNetmaskTextBox(), newNetmask);
		
		log.info("Network details updated. Saving and getting toast message details");
		SeleniumActions.seleniumClick(driver, perObjects.getDiscoveryEditNetworkSaveBtn());
		checkSpinner(driver);
		String toastMessage = getToastMessage(driver);
		Assert.assertTrue(toastMessage.contains(newIP) && toastMessage.contains("successfully"), "Toast message"
				+ "did not contain expected. Actual, " + toastMessage);
		
		
	}
	
	private void checkSpinner(WebDriver driver) {
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getEditNetworkSpinner())));
		log.info("Spinner found..waiting for spinner to disappear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(perObjects.getEditNetworkSpinner())));
		log.info("spinner has disappeared");
	}
	/**
	 * Navigates to peripherals and clicks the discover button.
	 * The toast message is checked for a successsgul discovery
	 * @param driver
	 */
	public void discover(WebDriver driver) {
		navigateToPeripherals( driver);
		log.info("discovering peripherals");
		SeleniumActions.seleniumClick(driver, perObjects.getDiscoverButton());
		String toastMessage = getToastMessage(driver);
		Assert.assertTrue(toastMessage.contains("Discovery completed"), "Toast message did not contain Discovery completed, actual: " + toastMessage);
	}

	private String getToastMessage(WebDriver driver) {
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(perObjects.getToastMessage())));
		log.info("Toast message found");
		String toastMessage = SeleniumActions.seleniumGetText(driver, perObjects.getToastMessage());
		log.info("Toast message output: " + toastMessage);
		return toastMessage;
	}
	
	/**
	 * Navigates from anywhere in boxilla to the peripherals page.
	 * Checks this page is loaded by looking for the discover button
	 * @param driver
	 */
	public void navigateToPeripherals(WebDriver driver) {
		log.info("Navigating to Peripherals page");
		SeleniumActions.seleniumClick(driver, landingObjects.getPeripheralsLink());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(perObjects.getDiscoverButton())));
		log.info("Discovery button is in clickable state. Successfully navigated to Peripherals page");
	}
}
