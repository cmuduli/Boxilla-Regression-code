package methods;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import javax.sound.midi.MidiDevice.Info;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import com.gargoylesoftware.htmlunit.javascript.host.Map;

import extra.SeleniumActions;
import extra.StartupTestCase;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import objects.Devices;
import objects.Discovery;
import objects.Landingpage;
import objects.Switch;
import objects.Users;

/**
 * Class contains methods for interacting with boxilla discovery
 * @author Boxilla
 *
 */
public class DiscoveryMethods  {

	final static Logger log = Logger.getLogger(DiscoveryMethods.class);

	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(StartupTestCase.getWaitTime(), TimeUnit.SECONDS);
	}

	/**
	 * Uses boxilla to discover all devices
	 * @param driver
	 * @throws InterruptedException
	 */
	public void discoverDevices(WebDriver driver) throws InterruptedException { // Discover device on the network
//		timer(driver);
		Landingpage.discoveryTab(driver).click();
		log.info("Discovery tab is Clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Discovery.discoverBtn(driver)));
		discover(driver);
		timer(driver);
	}

	/**
	 * Manages an appliance through boxilla using the MAC address
	 * Must have run discovery before calling this method
	 * 
	 * @param driver
	 * @param name Name of device after manage
	 * @param macAdd The MAC address of the device to manage
	 * @param ipCheck The IP address to give to the device
	 * @throws InterruptedException
	 */
	public void manageApplianceAutomatic(WebDriver driver, String name, String macAdd, String ipCheck)
			throws InterruptedException { // Manage Appliance
		timer(driver);
		if (SeleniumActions.seleniumGetText(driver, Discovery.connectionTableBody).contains(macAdd)) {
			log.info("Device found with given MAC Address..");
			timer(driver);
			String connectionStatus = SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection);
			if(connectionStatus.contains("UnManaged") || connectionStatus.contains("Orphaned") &&
					connectionStatus.contains(ipCheck)) {
				SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
				log.info("BreadCrumb Clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.applianceManage);
				log.info("Manage Button Clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Discovery.managedName, name);
				log.info("Managed Name Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.manageApplyBtn);
				log.info("Apply button clicked to manage the appliance");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				log.info("Managing Device...");
				new WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Devices.getsystemPropertiesButtonXpath())));
				deviceStatusCheck(driver, name); // Calling method to navigate to Devices > Status and check if
				// appliance is added
				log.info("Device asserted in Devices > Status");
			} else { // actions if device is not UnManaged
				log.info("Device satae is not UnManaged - Can not Manage this device");
				throw new SkipException("***** Device state is not UnManaged - Can not Manage this device");
			}
		} else {
			log.info("Device not found using given MAC Address");
			throw new SkipException("***** Device not found using given MAC Address *****");
		}
	}


	/**
	 * Manage appliance automatically through Boxilla. 
	 * @param driver
	 * @param name
	 * @param macAdd
	 * @param ipCheck
	 * @param zone
	 * @throws InterruptedException
	 */
	public void manageApplianceAutomatic(WebDriver driver, String name, String macAdd, String ipCheck, String zone)
			throws InterruptedException { // Manage Appliance
		timer(driver);
		if (SeleniumActions.seleniumGetText(driver, Discovery.connectionTableBody).contains(macAdd)) {
			log.info("Device found with given MAC Address..");
			timer(driver);
			String connectionStatus = SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection);
			if(connectionStatus.contains("UnManaged") || connectionStatus.contains("Orphaned") &&
					connectionStatus.contains(ipCheck)) {
				SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
				log.info("BreadCrumb Clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.applianceManage);
				log.info("Manage Button Clicked");
				timer(driver);
				SeleniumActions.seleniumDropdown(driver, Discovery.getZoneDropdown(), zone);
				SeleniumActions.seleniumSendKeys(driver, Discovery.managedName, name);
				log.info("Managed Name Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.manageApplyBtn);
				log.info("Apply button clicked to manage the appliance");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				log.info("Managing Device...");
				new WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Devices.getsystemPropertiesButtonXpath())));
				deviceStatusCheck(driver, name); // Calling method to navigate to Devices > Status and check if
				// appliance is added
				log.info("Device asserted in Devices > Status");
			} else { // actions if device is not UnManaged
				log.info("Device satae is not UnManaged - Can not Manage this device");
				throw new SkipException("***** Device state is not UnManaged - Can not Manage this device");
			}
		} else {
			log.info("Device not found using given MAC Address");
			throw new SkipException("***** Device not found using given MAC Address *****");
		}
	}

	/**
	 * Manages a device through boxilla manually 
	 * @param driver
	 * @param macAdd
	 * @param hostName
	 * @param ipAdd
	 * @throws InterruptedException
	 */
	public void addDeviceManually(WebDriver driver, String macAdd, String hostName, String ipAdd)
			throws InterruptedException {
		timer(driver);
		if (SeleniumActions.seleniumGetText(driver, Discovery.connectionTableBody).contains(macAdd)) {
			log.info("Device found with given MAC Address..");
			timer(driver);
			String connectionStatus = SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection);
			if(connectionStatus.contains("UnManaged") && connectionStatus.contains(ipAdd)) {
				timer(driver);
				Landingpage.discoveryTab(driver).click();
				log.info("Landing Page > Discovery tab clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.addManuallyTab);
				log.info("Add Manually Tab Clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Discovery.manualSearchIPaddBox, ipAdd);
				log.info("IP Address Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.getInfoBtn);
				log.info("Get Information Button Clicked");
				timer(driver);
				int i = 0;
				// Waiting to retrive device information
				while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
					log.info((i + 1) + ". Retrieving Device information...");
					Thread.sleep(2000);
					i++;
				}
				timer(driver);
				if (notificationExists(driver)) { // calling method to check if notification exists
					log.info(Users.notificationMessage(driver).getText());
					throw new SkipException("***** Skipping the test case - , Device can not be managed *****");
				} else {
					// assert if device information is retrieved
					Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Discovery.deviceInfo),
							"***** Device Information not available *****");
					String deviceInfo = SeleniumActions.seleniumGetText(driver, Discovery.deviceInfo);
					Assert.assertTrue(deviceInfo.contains(ipAdd),
							"Device info did not contain: " + ipAdd + ", actual text: " + deviceInfo);
					timer(driver);
					SeleniumActions.seleniumSendKeys(driver, Discovery.hostName, hostName);
					timer(driver);
					SeleniumActions.seleniumClick(driver, Discovery.manageDevice);
					log.info("Mange Device Button Clicked");
					new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
//					i = 0;
//					while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
//						log.info((i + 1) + ". Managing Device...");
//						Thread.sleep(2000);
//						i++;
//					}
					String notificationMessage = Users.notificationMessage(driver).getText();
					Assert.assertTrue(notificationMessage.contains("Success"),
							"Notification message did not contain: Success, actual text: " + notificationMessage);
					log.info("Success - Notification message varified");
					deviceStatusCheck(driver, ipAdd);
				}
			} else if ((SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains("Orphaned"))
					&& (SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(ipAdd))) {
				SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
				log.info("BreadCrumb Clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.applianceManage);
				log.info("Manage Button Clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Discovery.managedName, hostName);
				log.info("Managed Name Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.manageApplyBtn);
				log.info("Apply button clicked to manage the appliance");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				log.info("Managing Device...");
				timer(driver);
				int i = 0;
				while (Landingpage.spinner(driver).isDisplayed()) {
					log.info((i + 1) + ". Managing in Progress...");
					Thread.sleep(5000);
					i++;
				}
				Thread.sleep(1000);
				deviceStatusCheck(driver, hostName); // Calling method to navigate to Devices > Status and check if
				// appliance is added
				log.info("Device asserted in Devices > Status");
			} else {
				log.info("Device can not be managed as device state is already Managed");
				throw new SkipException("*****  Device is already managed *****");
			}
		}
	}

	/**
	 * Manages a device through boxilla manually  and assigns it to a zone
	 * @param driver
	 * @param macAdd
	 * @param hostName
	 * @param ipAdd
	 * @throws InterruptedException
	 */
	public void addDeviceManually(WebDriver driver, String macAdd, String hostName, String ipAdd, String zone)
			throws InterruptedException {
		timer(driver);
		String x = SeleniumActions.seleniumGetText(driver, Discovery.connectionTableBody);
		log.info("X:" + x);
		if (SeleniumActions.seleniumGetText(driver, Discovery.connectionTableBody).contains(macAdd)) {
			log.info("Device found with given MAC Address..");
			timer(driver);
			String connectionStatus = SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection);
			if(connectionStatus.contains("UnManaged") && connectionStatus.contains(ipAdd)) {
				timer(driver);
				Landingpage.discoveryTab(driver).click();
				log.info("Landing Page > Discovery tab clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.addManuallyTab);
				log.info("Add Manually Tab Clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Discovery.manualSearchIPaddBox, ipAdd);
				log.info("IP Address Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.getInfoBtn);
				log.info("Get Information Button Clicked");
				timer(driver);
				int i = 0;
				// Waiting to retrive device information
				while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
					log.info((i + 1) + ". Retrieving Device information...");
					Thread.sleep(2000);
					i++;
				}
				timer(driver);
				if (notificationExists(driver)) { // calling method to check if notification exists
					log.info(Users.notificationMessage(driver).getText());
					throw new SkipException("***** Skipping the test case - , Device can not be managed *****");
				} else {
					// assert if device information is retrieved
					Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Discovery.deviceInfo),
							"***** Device Information not available *****");
					String deviceInfo = SeleniumActions.seleniumGetText(driver, Discovery.deviceInfo);
					Assert.assertTrue(deviceInfo.contains(ipAdd),
							"Device info did not contain: " + ipAdd + ", actual text: " + deviceInfo);
					timer(driver);
					SeleniumActions.seleniumSendKeys(driver, Discovery.hostName, hostName);
					SeleniumActions.seleniumDropdown(driver, Discovery.getZoneDropdown(), zone);
					timer(driver);
					SeleniumActions.seleniumClick(driver, Discovery.manageDevice);
					log.info("Mange Device Button Clicked");
					i = 0;
					while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
						log.info((i + 1) + ". Managing Device...");
						Thread.sleep(2000);
						i++;
					}
					driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
					String notificationMessage = Users.notificationMessage(driver).getText();
					Assert.assertTrue(notificationMessage.contains("Success"),
							"Notification message did not contain: Success, actual text: " + notificationMessage);
					log.info("Success - Notification message varified");
					deviceStatusCheck(driver, ipAdd);
				}
			} else if ((SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains("Orphaned"))
					&& (SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(ipAdd))) {
				SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
				log.info("BreadCrumb Clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.applianceManage);
				log.info("Manage Button Clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Discovery.managedName, hostName);
				log.info("Managed Name Entered");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Discovery.manageApplyBtn);
				log.info("Apply button clicked to manage the appliance");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				log.info("Managing Device...");
				timer(driver);
				int i = 0;
				while (Landingpage.spinner(driver).isDisplayed()) {
					log.info((i + 1) + ". Managing in Progress...");
					Thread.sleep(5000);
					i++;
				}
				Thread.sleep(1000);
				deviceStatusCheck(driver, hostName); // Calling method to navigate to Devices > Status and check if
				// appliance is added
				log.info("Device asserted in Devices > Status");
			} else {
				log.info("Device can not be managed as device state is already Managed");
				throw new SkipException("*****  Device is already managed *****");
			}
		}
	}

	// method to check State and IP address of device and change it when require
	/**
	 * Checks the device state using MAC address
	 * If the device is managed, it is unmanaged and rediscovered then remanaged with the 
	 * new IP address. 	If the device is orphaned it is remanaged and if it is unmanaged
	 * it is managed with the IP address passed in
	 * @param driver
	 * @param macAdd
	 * @param ipCheck
	 * @param newIP
	 * @param gateway
	 * @param netmask
	 * @throws InterruptedException
	 */
	public void stateAndIPcheck(WebDriver driver, String macAdd, String ipCheck, String newIP, String gateway,
			String netmask) throws InterruptedException {
		boolean isFound = false;
		int retry = 0;
		log.info("Checking Device Satate and IP Address.. Changing if required");
		timer(driver);
		Landingpage.discoveryTab(driver).click();
		log.info("Discovry Tab Clicked");
		while(!isFound && retry < 10) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Discovery.automaticDiscoveryTab);
			timer(driver);
			SeleniumActions.seleniumSendKeysClear(driver, Discovery.searchBox);
			SeleniumActions.seleniumSendKeys(driver, Discovery.searchBox, macAdd);
			log.info("Device searched using MAC address");
			timer(driver);
			log.info(SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection));
			if (SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(macAdd)) {
				isFound = true;
				timer(driver);
				log.info("Device found with given MAC Address.. Checking State...");
				String state = SeleniumActions.seleniumGetText(driver, Discovery.deviceState);
				if (state.contentEquals("Managed")) {
					timer(driver);
					log.info("Device is Managed.. Grabbing IP address to unmange");
					timer(driver);
					String ipAddress = SeleniumActions.seleniumGetText(driver, Discovery.ipOfManagedDevice);
					timer(driver);
					Landingpage.devicesTab(driver).click();
					log.info("Unamanging device : Devices Tab clicked");
					timer(driver);
					Landingpage.devicesStatus(driver).click();
					log.info("Unmanaging device : Status tab clicked");
					new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Devices.bulkUpdateBtn(driver)));
					timer(driver);
					SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
					log.info("Unmanaging device : Searched using IP address");
					timer(driver);
					SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtn);
					log.info("Unmanaging device : BreadCrumb Clicked");
					timer(driver);
					SeleniumActions.seleniumClick(driver, Devices.unManageTab);
					log.info("Unmanaging device : UnManage button clicked");
					Thread.sleep(1000);
					try {
						Alert alert = driver.switchTo().alert();
						alert.accept();
					} catch (Exception e) {
						log.info("Unmanaging device : No alerts");
					}
					timer(driver);
					int j = 0;
					while (Landingpage.spinner(driver).isDisplayed() && j < 50) {
						log.info((j + 1) + ". UnManaging Device...");
						Thread.sleep(2000);
						j++;
					}
					//if spinner is still displayed after 100 seconds. fail the test
					Assert.assertFalse(Landingpage.spinner(driver).isDisplayed(), "Device is not unmanaged after 100 seconds. Failing..");
					log.info("UnManaged Device Successfully.. Re Discovering the Device...");
					timer(driver);
					stateAndIPcheck(driver, macAdd, ipCheck, newIP, gateway, netmask);
				} else if (SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains("Orphaned")) {
					timer(driver);
					log.info("Device is Oraphaned.. Managing device..");
				} else if (SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains("UnManaged")) {
					if (!(SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(ipCheck))) {
						log.info("The Device is on different Network.. Trying to change IP address");
						timer(driver);
						SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
						log.info("BreadCrumb Clicked");
						timer(driver);
						SeleniumActions.seleniumClick(driver, Discovery.applianceEdit);
						log.info("Edit Button Clicked");
						timer(driver);
						SeleniumActions.seleniumSendKeysClear(driver, Discovery.ipAddress);
						SeleniumActions.seleniumSendKeys(driver, Discovery.ipAddress, newIP);
						log.info("New IP Address Entered");
						timer(driver);
						SeleniumActions.seleniumSendKeysClear(driver, Discovery.gateway);
						SeleniumActions.seleniumSendKeys(driver, Discovery.gateway, gateway);
						log.info("New Gateway Enetered");
						timer(driver);
						SeleniumActions.seleniumSendKeysClear(driver, Discovery.netmask);
						SeleniumActions.seleniumSendKeys(driver, Discovery.netmask, netmask);
						log.info("New Netmask Entered");
						timer(driver);
						SeleniumActions.seleniumClick(driver, Discovery.applyBtn);
						log.info("Appliance Details Edited");
						new WebDriverWait(driver, 60).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Discovery.discoverBtnXpath)));
						timer(driver);
						discover(driver);
						timer(driver);
						SeleniumActions.seleniumSendKeys(driver, Discovery.searchBox, newIP);
						int counter = 0;
						while ((!SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(newIP)) && counter < 2) {
							discover(driver);
							counter++;
						}
						if ((counter == 2) && (!SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection).contains(newIP))) {
							log.info("Executing State and IP check method one more time..");
							stateAndIPcheck(driver, macAdd, ipCheck, newIP, gateway, netmask);
						}
						String searchedConnection = SeleniumActions.seleniumGetText(driver, Discovery.searchedConnection);
						Assert.assertTrue(searchedConnection.contains(newIP),
								"Searched connection did not contain: " + newIP + ", actual text: " + searchedConnection);
						log.info("IP address change Asserted");
					} else { // send Skip exception is device is on the same network
						log.info("Device is on same network as Boxilla unit");
					}
				} else {
					log.info("Device state is neither Managed nor UnManaged");
					throw new SkipException("***** Device state is neither Manage or UnManaged *****");
				}
			} else {
				log.info("Device not found");
				log.info("waiting and retrying...");
				Thread.sleep(4000);
				discoverDevices(driver);

				retry++;
				if(retry >= 10)
					throw new SkipException("***** Device not found *****");
			}
		}
	}

	/**
	 * Checks if a notification is displayed on screen
	 * @param driver
	 * @return true if notification is displayed. Else false
	 */
	private boolean notificationExists(WebDriver driver) {
		try {
			Users.notificationContainer(driver);
		} catch (NoSuchElementException e) {
			return false;
		}
		return true;
	}

	/**
	 * Checks if device has been successfully managed. 
	 * Throws an assert error if device is not managed
	 * @param driver
	 * @param name
	 * @throws InterruptedException
	 */
	public void deviceStatusCheck(WebDriver driver, String name) throws InterruptedException {
		log.info("Asserting if Device is successfully managed");
		timer(driver);
		Landingpage.devicesTab(driver).click();
		timer(driver);
		Landingpage.devicesStatus(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Devices.bulkUpdateBtn(driver)));
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, name);
		log.info("Device name entered in search box");
		timer(driver);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains(name),
				"Device appliance table did not contain: " + name + ", actual text: " + deviceApplianceTable);
	}

	/**
	 * Returns the device video table text
	 * @param driver
	 * @param deviceIp
	 * @param isMisc
	 * @return
	 * @throws InterruptedException
	 */
	public String getDeviceVideoTableText(WebDriver driver, String deviceIp, boolean isMisc) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		timer(driver);
		Landingpage.devicesStatus(driver).click();
		timer(driver);
		if(isMisc == true) {
			Devices.MiscSettingsTab(driver).click();
		}
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceIp);
		log.info("Device name entered in search box");
		timer(driver);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		return deviceApplianceTable;
	}

	/**
	 * Clicks the discovery button in device > discovery and waits until the spinner is complete 
	 * @param driver
	 * @throws InterruptedException
	 */
	private void discover(WebDriver driver) throws InterruptedException { // clicking discovry button
		timer(driver);
		SeleniumActions.seleniumClick(driver, Discovery.discoverBtnXpath);
		log.info("Disovery button is Clicked to start discovering devices");
		log.info("Discovery in Progress...");
		timer(driver);
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Discovery complete");
	}

	/**
	 * Navigate to the device discovery page from any where in Boxilla
	 * @param driver
	 */
	public void navigateToDiscovery(WebDriver driver) {
		log.info("Navigating to discovery");
		SeleniumActions.seleniumClick(driver, Landingpage.getDiscoveryTabLink());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Discovery.discoverBtn(driver)));
		log.info("On discovery page");
	}

	/**
	 * CLick the discovery button and waits for the devices to return
	 * @param driver
	 * @param navigateTo - set to true if already on the page
	 */
	public void newDicoverDevices(WebDriver driver, boolean navigateTo) {
		//sometimes we just want to rerun the actual discovery
		if(navigateTo) {
			navigateToDiscovery(driver);
		}
		SeleniumActions.seleniumClick(driver, Discovery.getDiscoverButton());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		log.info("Devices discovered");
	}

	/**
	 * This will search the all discovered devices for the appliance MAC address.
	 * If found the devices network details will be changed to the passed in values.
	 * If the device does not show up in discovery it can retry x number of times 
	 * @param driver
	 * @param mac - MAC address of the device to be found
	 * @param ip - IP address to change on the found device
	 * @param gateway - gateway for the found device
	 * @param netmask - netmask for the found device
	 * @param retry - number of times to attempt discovery if the device does not show up
	 */
	public void searchAndEdit(WebDriver driver, String mac, String ip, String gateway, String netmask, int retry) {
		SeleniumActions.seleniumSendKeys(driver, Discovery.getDiscoverSearchBox(), mac);
		String tableText = SeleniumActions.seleniumGetText(driver, Discovery.getDiscoveryTableRow());
		log.info("table text:" + tableText);
		if(!tableText.equals("")) {
			if(tableText.contains(mac)) {
				log.info("Found correct device:" + tableText);
				log.info("Checking device status");
				if(tableText.contains("Orphaned") || tableText.contains("UnManaged")) {
					log.info("Device is either orphaned or unmanaged. Checking IP address");
					if(!tableText.contains(ip)) {
						log.info("IP Address differs. Changing");
						if(!changeIPDiscovery(driver, ip, gateway, netmask)) {
							searchAndEdit(driver, mac, ip, gateway, netmask, retry);
						}
					}else {
						log.info("IP address is the same. Moving on to manage");
					}
				}else {
					log.info("Device is managed already");
					throw new AssertionError("Device already managed");
				}
			}
		}else {
			log.info("Device not found. Retrying");
			if(retry > 1) {
				retry --;
				newDicoverDevices(driver, false);
				searchAndEdit(driver, mac, ip, gateway, netmask, retry);
			}else {
				throw new AssertionError("Device could not be found by mac address");
			}	
		}
	}

	/**
	 * This will run a full discover / edit / manage on a device
	 * @param driver
	 * @param mac
	 * @param ip
	 * @param gateway
	 * @param netmask
	 * @param deviceName
	 * @param retry
	 */
	public void addDeviceToBoxilla(WebDriver driver,String mac, String ip,
			String gateway, String netmask, String deviceName, int retry) {
		newDicoverDevices(driver, true);
		searchAndEdit(driver, mac, ip, gateway, netmask, retry);
		manageDevice(driver, ip, deviceName);
	}
	
	
	
	
	public void addDeviceToBoxilla1(WebDriver driver,String mac, String ip,
			String gateway, String netmask,String deviceName,int retry,String boxillaIp,String restUser,String restPassword ) {
		newDicoverDevices(driver, true);
		searchAndEdit(driver, mac, ip, gateway, netmask, retry);
//		manageDevice(driver, ip, deviceName);
//		managedeviceIPRest(boxillaIp, restUser, restPassword, ip, deviceName);
		manageDevice1(driver,boxillaIp, restUser, restPassword, ip, deviceName);
		
		
	}

	/**
	 * THis will change a devices network details from the device discovery page. Must already be on the 
	 *  discovery page
	 * @param driver
	 * @param ip
	 * @param gateway
	 * @param netmask
	 * @return
	 */
	public boolean changeIPDiscovery(WebDriver driver, String ip, String gateway, String netmask) {
		SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Discovery.applianceEdit)));
		SeleniumActions.seleniumClick(driver, Discovery.applianceEdit);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Discovery.getEditDeivceApplyBtn())));
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.ipAddress);
		SeleniumActions.seleniumSendKeys(driver, Discovery.ipAddress, ip);
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.gateway);
		SeleniumActions.seleniumSendKeys(driver, Discovery.gateway, gateway);
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.netmask);
		SeleniumActions.seleniumSendKeys(driver, Discovery.netmask, netmask);
		SeleniumActions.seleniumClick(driver, Discovery.getEditDeivceApplyBtn());
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.switchPingToastXpath)));
		String toastText = SeleniumActions.seleniumGetText(driver, Switch.switchPingToastXpath);
		if(toastText.contains("Success")) {
			log.info("IP changed successfully");
			return true;
		}else {
			return false;
		}
	}
	
	
	
	/*
	 * This will manage the devices through API
	 * */
	public String getUri(String boxillaIp) 
	{
		return "https://" + boxillaIp +"/bxa-api/automation/devices/manage_devices";
	}
	
	public class manageDeviceApi
	{
		public String ip;
		public String hostname;
		
	}
	
	public void manageDevice1(WebDriver driver,String boxillaIp,String restUser,String restPassword,String Ip,String deviceName) 
	{
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		manageDeviceApi device=new manageDeviceApi();
		device.ip=Ip;
		device.hostname=deviceName;
		HashMap<String,Object>managedevice=new HashMap<>();
		managedevice.put("devices",Arrays.asList(device));
      given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders()).when()
		.contentType(ContentType.JSON).body(managedevice).post(getUri(boxillaIp)).then().assertThat()
		.statusCode(200).body("status", equalTo("success"));
      log.info("Device managed. Searching managed devices table for device to confirm");
      Landingpage.devicesTab(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.devicesStatus(driver)));
		Landingpage.devicesStatus(driver).click();
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceName);
		log.info("Device name entered in search box");
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertTrue(deviceApplianceTable.contains(deviceName),
				"Device appliance table did not contain: " + deviceName + ", actual text: " + deviceApplianceTable);

		
	}
	
	
	/*
	 * This test will unmanaged the device through API
	 * */
	
	public String getUri1(String boxillaIp) 
	{
		return "https://" + boxillaIp +"/bxa-api/automation/devices/unmanage_devices";
	}
	
    public class unManagedeviceApi
    {
    	public String[] macs;
    }
    
    public void unmanageDevice1(WebDriver driver,String boxillaIp,String restUser,String restPassword,String macs,String ipAddress ) throws InterruptedException, IOException 
    {
//    	Thread.sleep(10000);
    	RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		unManagedeviceApi unmanage=new unManagedeviceApi();
		unmanage.macs=new String[1];
		unmanage.macs[0]=macs;
		given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders()).when()
		.contentType(ContentType.JSON).body(unmanage).post(getUri1(boxillaIp)).then().assertThat()
		.statusCode(200).body("status", equalTo("success"));
		 log.info("Device unmanaged. Searching unmanaged devices table for device to confirm");
		 Landingpage.devicesTab(driver).click();
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.devicesStatus(driver)));
			Landingpage.devicesStatus(driver).click();
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
			log.info("UnManage Device - Device name entered in search box");
			timer(driver);
			String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
			Assert.assertFalse(deviceApplianceTable.contains(ipAddress),
					"Device appliance table did not contain: " + ipAddress + ", actual text: " + ipAddress);
		 
		
    }

	/**
	 * This will search for a device with the IP passed in and manage the device 
	 * in Boxilla, giving the device the passed in name
	 * @param driver
	 * @param ip - IP of the device to manage
	 * @param deviceName - Name to give the device when managed
	 */
	public void manageDevice(WebDriver driver, String ip, String deviceName) {
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.getDiscoverSearchBox());
		SeleniumActions.seleniumSendKeys(driver, Discovery.getDiscoverSearchBox(), ip);
		String tableText = SeleniumActions.seleniumGetText(driver, Discovery.getDiscoveryTableRow());
		if(tableText.contains(ip)) {
			log.info("Found device with IP:" +ip);
			SeleniumActions.seleniumClick(driver, Discovery.breadCrumbBtn);
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Discovery.applianceManage)));
			SeleniumActions.seleniumClick(driver, Discovery.applianceManage);
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Discovery.manageApplyBtn)));
			SeleniumActions.seleniumSendKeysClear(driver, Discovery.managedName);
			SeleniumActions.seleniumSendKeys(driver, Discovery.managedName, deviceName);
			SeleniumActions.seleniumClick(driver, Discovery.manageApplyBtn);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
			new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getsystemPropertiesButtonXpath())));
			log.info("Device managed. Searching managed devices table for device to confirm");
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceName);
			log.info("Device name entered in search box");
			String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
			Assert.assertTrue(deviceApplianceTable.contains(deviceName),
					"Device appliance table did not contain: " + deviceName + ", actual text: " + deviceApplianceTable);


		}	
		
	}
	public void pingDeviceIP(WebDriver driver,String ip) throws InterruptedException 
	{
		timer(driver);
		SeleniumActions.seleniumClick(driver, Discovery.pingBtnXpath);
		log.info("Ping Button clicked ");
		SeleniumActions.seleniumSendKeysClear(driver, Discovery.deviceIpSearchXpath);
		log.info("Clear the Device search field");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Discovery.deviceIpSearchXpath, ip);
		log.info("Send the Device IP");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Discovery.pingBtn);
		log.info("Device IP Pinged and waiting to dissapear the spinning Button ");
		int i = 0;
		// Waiting to retrive device information
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			log.info((i + 1) + ". Retrieving Device information...");
			Thread.sleep(2000);
			i++;
		}
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Success"),
				"Notification message did not contain: Success, actual text: " + notificationMessage);
		log.info("Device IP Pined Successfully");
		
		
	}


}