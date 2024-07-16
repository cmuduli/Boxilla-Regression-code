package methods;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.testng.Assert.ARRAY_MISMATCH_TEMPLATE;

import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.Scanner;
import java.util.Timer;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import device.DeviceDiscovery;
import extra.ScpTo;
import extra.SeleniumActions;
import extra.Ssh;
import extra.StartupTestCase;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.post.RebootDevice.Reboot;
import objects.ActiveConnectionElements;
import objects.Connections;
import objects.Devices;
import objects.Landingpage;
import objects.Switch;
import objects.Users;
import testNG.Utilities;

/**
 * Class contains methods for interacting with boxilla devices pages
 * 
 * @author Boxilla
 *
 */
public class DevicesMethods extends StartupTestCase {

	private Object object;

	/**
	 * Enum represending the 2k resolutions available
	 * 
	 * @author Boxilla
	 *
	 */
	public enum RESOLUTION {
		R1920X1080, R1920X1200, R800X600, R640X480, R1024X768, R1280X1024
	}

	/**
	 * Enum representing the hotkeys that are available to set
	 * 
	 * @author Boxilla
	 *
	 */
	public enum HOTKEY {
		PRINTSCRN, ALT_ALT, CTRL_CTRL, SHIFT_SHIFT, MOUSE
	}

	final static Logger log = Logger.getLogger(DevicesMethods.class);

	/**
	 * Navigate from anywhere to the Bonded devices groups page
	 * 
	 * @param driver
	 */
	public void navigateToDevicesGroups(WebDriver driver) {
		log.info("Navigating to devices groups");
		SeleniumActions.seleniumClick(driver, Landingpage.getDevicesLink());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Landingpage.getDevicesGroupLink())));
		SeleniumActions.seleniumClick(driver, Landingpage.getDevicesGroupLink());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getCreateBondedReceiverGroup())));
		log.info("Successfully navigated to bonded receiver group page");
	}

	/**
	 * Check all the details of a bonded device group from the bonded device table
	 * 
	 * @param driver
	 * @param groupName
	 *            - name of device group to check
	 * @param noOfReceiver
	 *            - The amount of receivers in the group
	 * @param receivers
	 *            - List of all the receivers in the group
	 * @param zoneName
	 *            - Name of the zone the group is in (if any)
	 * @throws InterruptedException
	 */
	public void checkAllBondedReceiverGroupDetails(WebDriver driver, String groupName, String noOfReceiver,
			String[] receivers, String zoneName) throws InterruptedException {
		log.info("Checking if gorup exist");
		Assert.assertTrue(doesGroupExist(driver, groupName, false), "Bonded group does not exist");
		log.info("Checking number of receivers");
		Assert.assertTrue(noOfReceiver.equals(getNumberOfReceiversInGroup(driver, groupName, true)),
				"Number of receiver in group does not match");
		log.info("Checking zone name");
		// if bonded group has no zone it will appear in the table as a -
		if (zoneName.equals("")) {
			zoneName = "-";
		}
		Assert.assertTrue(zoneName.equals(getZoneForReceiverGroup(driver, groupName, true)),
				"Zone name did not match.");
		log.info("Checking all receivers in group match");
		String[] receiversFromGroup = getReceiverInGroup(driver, groupName, true);
		for (int j = 0; j < receiversFromGroup.length; j++) {
			log.info("RECEV:" + receiversFromGroup[j] + "!");
			String lines[] = receiversFromGroup[j].split("\\r?\\n");
			Assert.assertTrue(lines[0].equals(receivers[j]),
					"Receivers did not match. Expected:" + receivers[j] + " , Actual:" + lines[0]);
		}

	}

	/**
	 * Edits a bonded receiver groups name and list of receivers
	 * 
	 * @param driver
	 * @param groupName
	 *            - name of the group to edit
	 * @param newGroupName
	 *            - new name to give to group
	 * @param originalReceivers
	 *            - list of receivers that were originally in the group
	 * @param newReceivers
	 *            - list of updated receivers to give to the group
	 * @throws InterruptedException
	 */
	public void editBondedReceiverGroup(WebDriver driver, String groupName, String newGroupName,
			String[] originalReceivers, String[] newReceivers) throws InterruptedException {
		Assert.assertTrue(doesGroupExist(driver, groupName, false), "Group does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Devices.getBondedGroupBurgerButton());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getEditBondedReceiverGroup())));
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedReceiverGroup());
		if (!newGroupName.equals("")) {
			SeleniumActions.seleniumSendKeysClear(driver, Devices.getReceiverGroupName());
			SeleniumActions.seleniumSendKeys(driver, Devices.getReceiverGroupName(), newGroupName);
		}
		for (int j = originalReceivers.length; j > 1; j--) {
			SeleniumActions.seleniumClick(driver, Devices.getDeleteReceiverFromGroupButton(j - 1));
		}
		SeleniumActions.seleniumSendKeysClear(driver, Devices.getBondedReceiverDropdown("0"));
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedReceiverDropdown("0"), newReceivers[0]);
		for (int j = 1; j < newReceivers.length; j++) {
			SeleniumActions.seleniumClick(driver, Devices.getAddReceiverButtonEdit());
			SeleniumActions.seleniumSendKeys(driver, Devices.getBondedReceiverDropdown(Integer.toString(j)),
					newReceivers[j]);
		}
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedGroupSaveBtn());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String toast = SeleniumActions.seleniumGetText(driver, Devices.getSaveSystemProperyToastXpath());
		String toastDetails = SeleniumActions.seleniumGetInnerText(driver, Devices.getSaveSystemProperyToastXpath());
		Assert.assertTrue(toast.contains("Success"),
				"Error creating bonded receiver group. Expected Success, actual:" + toast + " " + toastDetails);
	}

	/**
	 * Edits the index of the bonded receivers in the group by moving one receiver
	 * up. Using the index of the receiver in the group
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the group to edit
	 * @param receivers
	 *            - List of receivers in the group
	 * @param startIndex
	 *            - The start index of the receiver you want to move
	 * @param endIndex
	 *            - The end index of the receiver you want to move
	 * @throws InterruptedException
	 */
	public void moveBondedReceiverUp(WebDriver driver, String groupName, String[] receivers, int startIndex,
			int endIndex) throws InterruptedException {
		Assert.assertTrue(doesGroupExist(driver, groupName, false),
				"Bonded receiver group does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Devices.getBondedGroupBurgerButton());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getEditBondedReceiverGroup())));
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedReceiverGroup());
		
		for (int j = endIndex; j > startIndex; j--) {
			SeleniumActions.seleniumClick(driver, Devices.getEditBondedReceiverMoveUp((j)));
		}
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedGroupSaveBtn());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String toast = SeleniumActions.seleniumGetText(driver, Devices.getSaveSystemProperyToastXpath());
		System.out.println(toast);
		String toastDetails = SeleniumActions.seleniumGetInnerText(driver, Devices.getSaveSystemProperyToastXpath());
		Assert.assertTrue(toast.contains("Success"),
				"Error creating bonded receiver group. Expected Success, actual:" + toast + " " + toastDetails);

	}

	/**
	 * Edits the index of the bonded receivers in the group by moving one receiver
	 * down. Using the index of the receiver in the group
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the group to edit
	 * @param receivers
	 *            - List of receivers in the group
	 * @param startIndex
	 *            - The start index of the receiver you want to move
	 * @param endIndex
	 *            - The end index of the receiver you want to move
	 * @throws InterruptedException
	 */
	public void moveBondedReceiverDown(WebDriver driver, String groupName, String[] receivers, int startIndex,
			int endIndex) throws InterruptedException {
		Assert.assertTrue(doesGroupExist(driver, groupName, false),
				"Bonded receiver group does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Devices.getBondedGroupBurgerButton());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getEditBondedReceiverGroup())));
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedReceiverGroup());
		for (int j = startIndex; j < endIndex; j++) {
			SeleniumActions.seleniumClick(driver, Devices.getEditBondedReceiverMoveDown((j)));
		}
		SeleniumActions.seleniumClick(driver, Devices.getEditBondedGroupSaveBtn());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String toast = SeleniumActions.seleniumGetText(driver, Devices.getSaveSystemProperyToastXpath());
		String toastDetails = SeleniumActions.seleniumGetInnerText(driver, Devices.getSaveSystemProperyToastXpath());
		Assert.assertTrue(toast.contains("Success"),
				"Error creating bonded receiver group. Expected Success, actual:" + toast + " " + toastDetails);

	}

	/**
	 * Deletes a bonded receiver group and checks the group is no longer in Boxilla
	 * 
	 * @param driver
	 * @param groupName
	 * @throws InterruptedException
	 */
	public void deleteGroup(WebDriver driver, String groupName) throws InterruptedException {
		log.info("Attempting to delete bonded receiver group:" + groupName);
		Assert.assertTrue(doesGroupExist(driver, groupName, false), "Group does not exist. Cannot delete");
		SeleniumActions.seleniumClick(driver, Devices.getBondedGroupBurgerButton());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getDeleteBondedReceiverGroup())));
		SeleniumActions.seleniumClick(driver, Devices.getDeleteBondedReceiverGroup());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getDeleteBondedGroupToast())));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Receiver group has been removed."),
				"Unable to delete bonded group. Toast message was not success. Actual:" + toast);
		Assert.assertFalse(doesGroupExist(driver, groupName, false), "Group was not deleted");
		;

	}

	/**
	 * Returns true if the receiver group passed in exists in the receiver group
	 * table
	 * 
	 * @param driver
	 * @param groupName
	 *            - name of the group to check
	 * @param isOnPage
	 *            - set to true if already on this page
	 * @return returns true if group exists, else false
	 * @throws InterruptedException
	 */
	public boolean doesGroupExist(WebDriver driver, String groupName, boolean isOnPage) throws InterruptedException {
		if (!isOnPage)
			navigateToDevicesGroups(driver);

		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedGroupSearch(), groupName);
		Thread.sleep(1000);
		return SeleniumActions.seleniumIsDisplayed(driver, Devices.getGroupNameFromTable(groupName));
	}

	/**
	 * This will return a list of all the receivers in a bonded group
	 * 
	 * @param driver
	 * @param groupName
	 *            - The name of the group to get the receivers from
	 * @param isOnPage
	 *            - set to true if already on the receiver group table page
	 * @return Array of recievers in the group
	 * @throws InterruptedException
	 */
	public String[] getReceiverInGroup(WebDriver driver, String groupName, boolean isOnPage)
			throws InterruptedException {
		if (!isOnPage)
			navigateToDevicesGroups(driver);

		int numberOfReceivers = Integer.parseInt(getNumberOfReceiversInGroup(driver, groupName, isOnPage));
		String[] receivers = new String[numberOfReceivers];
		SeleniumActions.seleniumSendKeysClear(driver, Devices.getBondedGroupSearch());
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedGroupSearch(), groupName);
		SeleniumActions.seleniumClick(driver, Devices.getBondedGroupExpand());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getBondedReceiversHeading())));
		Thread.sleep(3000);
		for (int j = 0; j < numberOfReceivers; j++) {
			receivers[j] = SeleniumActions.seleniumGetText(driver, Devices.getReceiverInGroup(j, groupName))
					.substring(1).trim();
		}
		for (String s : receivers) {
			log.info("Reciever:" + s + ".");
		}
		return receivers;
	}

	/**
	 * Returns the status of the bonded receiver group
	 * 
	 * @param driver
	 * @param groupName
	 *            - The name of the group to get the status from
	 * @param isOnPage
	 *            - Set to true if already on the bonded receiver group page
	 * @return String representing the receivers group status
	 */
	public String getReceiverGroupStatus(WebDriver driver, String groupName, boolean isOnPage) {
		if (!isOnPage)
			navigateToDevicesGroups(driver);

		SeleniumActions.seleniumSendKeysClear(driver, Devices.getBondedGroupSearch());
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedGroupSearch(), groupName);
		String status = SeleniumActions.seleniumGetText(driver, Devices.getBondedGroupTableStatus());
		log.info("Bonded group status:" + status);
		return status;

	}

	/**
	 * Returns the zone the receiver group belongs to
	 * 
	 * @param driver
	 * @param groupName
	 *            - The name of the group to get the zone from
	 * @param isOnPage
	 *            - Set to true if already on the receiver group page
	 * @return String with the receiver groups zone name
	 */
	public String getZoneForReceiverGroup(WebDriver driver, String groupName, boolean isOnPage) {
		if (!isOnPage)
			navigateToDevicesGroups(driver);

		SeleniumActions.seleniumSendKeysClear(driver, Devices.getBondedGroupSearch());
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedGroupSearch(), groupName);
		String zone = SeleniumActions.seleniumGetText(driver, Devices.getBondedGroupTableZone());
		log.info("Receiver group zone:" + zone);
		return zone;
	}

	/**
	 * Returns the number of receivers in a bonded receiver group
	 * 
	 * @param driver
	 * @param groupName
	 *            - The name of the receiver group to get the number of receivers
	 *            from
	 * @param isOnPage
	 *            - Set to true if already on the bonded receiver group page
	 * @return
	 */
	public String getNumberOfReceiversInGroup(WebDriver driver, String groupName, boolean isOnPage) {
		if (!isOnPage)
			navigateToDevicesGroups(driver);

		SeleniumActions.seleniumSendKeysClear(driver, Devices.getBondedGroupSearch());
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedGroupSearch(), groupName);
		String number = SeleniumActions.seleniumGetText(driver, Devices.getBondedGroupTableReceiverCount());
		log.info("Number of receivers in group:" + number);
		return number;
	}

	/**
	 * Creates a bonded receiver group
	 * 
	 * @param driver
	 * @param name
	 *            - The name to give the receiver group to create
	 * @param zone
	 *            - The name of the zone (if any) the reciever group belongs to. Set
	 *            to "" for no group
	 * @param receivers
	 *            - list of the receiver to add to the group
	 * @throws InterruptedException
	 */
	public void createBondedGroup(WebDriver driver, String name, String zone, String[] receivers)
			throws InterruptedException {
		log.info("Creating bonded receiver group");
		navigateToDevicesGroups(driver);
		SeleniumActions.seleniumClick(driver, Devices.getCreateBondedReceiverGroup());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getAddReceiverGroupSaveButton())));
		log.info("Create bonded receiver group pop up showing");

		if (zone != "") {
			SeleniumActions.seleniumDropdown(driver, Devices.getAddReceiverGroupZoneList(), zone);
		}

		// add first receiver
		SeleniumActions.seleniumSendKeys(driver, Devices.getBondedReceiverDropdown("0"), receivers[0]);
		for (int j = 1; j < receivers.length; j++) {
			SeleniumActions.seleniumClick(driver, Devices.getAddReceiverButton());

			SeleniumActions.seleniumSendKeys(driver, Devices.getBondedReceiverDropdown(Integer.toString(j)),
					receivers[j]);
		}
		SeleniumActions.seleniumSendKeys(driver, Devices.getReceiverGroupName(), name);
		SeleniumActions.seleniumClick(driver, Devices.getAddReceiverGroupSaveButton());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String toast = SeleniumActions.seleniumGetText(driver, Devices.getSaveSystemProperyToastXpath());
		String toastDetails = SeleniumActions.seleniumGetInnerText(driver, Devices.getSaveSystemProperyToastXpath());
		Assert.assertTrue(toast.contains("Success"),
				"Error creating bonded receiver group. Expected Success, actual:" + toast + " " + toastDetails);
	}

	/**
	 * Will return the currect resolution the transmitter is displaying by logging
	 * into the appliance through SSH and getting the resolution from the command
	 * line
	 * 
	 * @param transmitterIp
	 *            - The ip of the transmitter to check resolution on
	 * @return String that contains the resolution
	 */
	public String getTranmitterResolution(String transmitterIp) {
		int counter = 0;
		String res = "";
		Ssh txDevice = new Ssh("root", "barrow1admin_12", transmitterIp);
		while (counter < 5) {
			try {
				txDevice.loginToServer();
				res = txDevice.sendCommand("capture_layer_cli -d1");
				counter = 5;
			} catch (Exception e) {
				log.info("Could not log into server. Trying again");
				counter++;
			}
		}

		log.info(res);
		String[] resSplit = res.split("Detected resolution is ");

		String[] resSplit2 = resSplit[1].split("\\s+");
		res = resSplit2[0];
		txDevice.disconnect();
		return res;
	}

	/**
	 * Sets the resolution on a source PC
	 * 
	 * @param sourceIp
	 * @param user
	 * @param pass
	 * @param resolution
	 */
	public void setSourceResolution(String sourceIp, String user, String pass, String resolution) {
		int counter = 0;
		Ssh sourcePc = new Ssh(user, pass, sourceIp);

		while (counter < 5) {
			try {
				sourcePc.loginToServer();
				sourcePc.sendCommand("echo " + resolution + " >> autoLogfile.log");
				sourcePc.sendCommand("xrandr -d :0 -s " + resolution);

				counter = 5;
			} catch (Exception e) {
				log.info("Error logging in. Retry");
				counter++;
			}
		}
		sourcePc.disconnect();

	}

	// convert TX settings to deivce rest api values. Incomplete
	/**
	 * Converts tx settings to device REST API values
	 * 
	 * @param videoQ
	 * @param videoOpt
	 * @param hid
	 * @param mouseT
	 * @param edid1
	 * @return
	 */
	public int[] convertTXSingle(String videoQ, String videoOpt, String hid, String mouseT, String edid1) {
		int[] values = new int[5];
		switch (videoQ) {
		case "Default":
			values[0] = 2;
			break;
		case "Best Quality":
			values[0] = 0;
			break;
		case "Best Compression":
			values[0] = 4;
			break;
		case "2":
			values[0] = 1;
			break;
		case "4":
			values[0] = 3;
			break;
		}

		switch (videoOpt) {
		case "Off":
			values[1] = 0;
			break;
		case "DVI Optimised":
			values[1] = 1;
			break;
		case "VGA - High Performance":
			values[1] = 2;
			break;
		case "VGA - Optimised":
			values[1] = 3;
			break;
		case "VGA - Low Bandwidth":
			values[1] = 4;
			break;
		}

		switch (hid) {
		case "Default":
			values[2] = 0;
			break;
		case "Basic":
			values[2] = 1;
			break;
		case "MAC":
			values[2] = 2;
			break;
		case "Absolute":
			values[2] = 3;
			break;
		}

		switch (mouseT) {
		case "0":
			values[3] = 0;
			break;
		case "1":
			values[3] = 1;
			break;
		case "2":
			values[3] = 2;
			break;
		case "3":
			values[3] = 3;
			break;
		case "4":
			values[3] = 4;
			break;
		}
		return values;
	}

	/**
	 * Will ssh into boxilla and run 3 curl commands that replicate the logging in
	 * of an AD user on the device matching the mac passed in
	 * 
	 * @param username
	 * @param password
	 * @param mac
	 * @param boxillaIP
	 * @return
	 * @throws InterruptedException
	 */
	public String logInADUser(String username, String password, String mac, String boxillaIP, String deviceIp)
			throws InterruptedException {
		log.info("Using curl to log AD user into device and return the datatbase");
		Ssh ssh = new Ssh(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIP);
		ssh.loginToServer();

		// curl commands to run on boxilla
		String loginCurl = "curl -i -v -X POST -H \"Content-Type: application/json\" -H \"Accept: application/json\" --data '{\"username\":\""
				+ username + "\", \"password\":\"" + password + "\", \"mac\":\"" + mac + "\"}' -u admin:admin https://"
				+ boxillaIP + "/api/ad/v1/user/login";
		String statusCurl = "curl -i -v -X GET -H \"Content-Type: application/json\" -H \"Accept: application/json\" --data '{\"username\":\""
				+ username + "\", \"mac\":\"" + mac + "\"}' -u admin:admin https://" + boxillaIP
				+ "/api/ad/v1/user/auth_status";
		String completeCurl = "curl -i -v -X POST -H \"Content-Type: application/json\" -H \"Accept: application/json\" --data '{\"username\":\""
				+ username + "\", \"mac\":\"" + mac + "\"}' -u admin:admin https://" + boxillaIP
				+ "/api/ad/v1/user/auth_complete";

		log.info("loginCurl:" + loginCurl);
		log.info("statusCurl:" + statusCurl);
		log.info("completeCurl:" + completeCurl);

		ssh.sendCommand(loginCurl);
		Thread.sleep(2000);
		ssh.sendCommand(statusCurl);
		Thread.sleep(2000);
		ssh.sendCommand(completeCurl);
		ssh.disconnect();

		log.info("Login complete. Getting CloudData");
		Ssh sshDevice = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, deviceIp);
		sshDevice.loginToServer();
		String cloudData = sshDevice.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		return cloudData;
	}

	/**
	 * Will set the muilticast port Ip address on a 4K device
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - The IP address of the appliance to set the multicast ip on
	 * @param multicastIp
	 *            - The multicast IP address to set
	 * @throws InterruptedException
	 */
	public void setMulticastPortandIp(WebDriver driver, String deviceIp, String multicastIp)
			throws InterruptedException {
		navigateToOptions(driver, deviceIp);
		SeleniumActions.seleniumClick(driver, Devices.edit);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.applyBtn)));

		SeleniumActions.seleniumSendKeysClear(driver, Devices.multiCastIpTextBox);
		SeleniumActions.seleniumSendKeys(driver, Devices.multiCastIpTextBox, multicastIp);

	}

	/**
	 * Retuns the value of the specified column. Options are Video Quality, Video
	 * Source, EDID1, EDID2, HID, Mouse Timeout.
	 * 
	 * @param driver
	 * @param ipAddress
	 * @param setting
	 * @return
	 * @throws InterruptedException
	 */
	public String getSettingDataFromTable(WebDriver driver, String ipAddress, String setting)
			throws InterruptedException {
		navigateToDeviceStatus(driver);
		String value = "";
		if (setting.equals("HID") || setting.equals("Mouse Timeout")) {
			SeleniumActions.seleniumClick(driver, Devices.getMiscSettingsXpath());
			timer(driver);
		}
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		switch (setting) {
		case "Video Quality":
			value = Devices.getDeviceTableSetting(driver, "7").getText();
			break;
		case "Video Source":
			value = Devices.getDeviceTableSetting(driver, "8").getText();
			break;
		case "EDID1":
			value = Devices.getDeviceTableSetting(driver, "9").getText();
			break;
		case "EDID2":
			value = Devices.getDeviceTableSetting(driver, "10").getText();
			break;
		case "HID":
			value = Devices.getDeviceMiscTableSetting(driver, "7").getText();
			break;
		case "Mouse Timeout":
			value = Devices.getDeviceMiscTableSetting(driver, "8").getText();
			break;
		}

		return value;
	}

	/**
	 * This will log into a machine and copy a bash script over which will take the
	 * interface down and bring it back up a set time later
	 * 
	 * @param ip
	 *            - Ip address of the machine to bring the interfaces down and up on
	 * @param username
	 *            - User name to log into the machine
	 * @param password
	 *            - Password to log into the machine
	 * @param networkInterface
	 *            - The name of the network interface to bring down/up
	 * @param sleepTime
	 *            - The time in seconds to leave the interface down for
	 * @throws InterruptedException
	 */
	public void interfacesDownUp(String ip, String username, String password, String networkInterface, String sleepTime)
			throws InterruptedException {

		log.info(
				"Attempting to bring " + networkInterface + " down for " + sleepTime + " seconds and bring it back up");
		ScpTo scp = new ScpTo();
		scp.scpTo("interfaceDownUp.sh", username, ip, password, "/usr/local/", "interfaceDownUp.sh");
		Ssh shell = new Ssh(username, password, ip);
		shell.loginToServer();
		shell.sendCommand("chmod 777 /usr/local/interfaceDownUp.sh");
		shell.sendCommand("dos2unix /usr/local/interfaceDownUp.sh");
		shell.sendCommandNoReturn("nohup /usr/local/interfaceDownUp.sh " + networkInterface + " " + sleepTime + " &");
		shell.disconnect();
		log.info("script finished");
	}

	/**
	 * This will log into a machine and remove the interfaceDownUp bash file
	 * 
	 * @param ip
	 * @param password
	 * @param username
	 */
	public void removeInterfaceFile(String ip, String password, String username) {
		log.info("attempting to remove /usr/local/interfaceDownUp.sh");
		Ssh ssh = new Ssh(username, password, ip);
		ssh.loginToServer();
		ssh.sendCommand("rm /usr/local/interfaceDownUp.sh");
		ssh.disconnect();
		log.info("/usr/local/interfaceDownUp.sh removed");
	}

	/**
	 * Navigates to the emerald upgrade page from anywhere in Boxilla
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToEmeraldUpgrade(WebDriver driver) throws InterruptedException {
		log.info("Navigating to upgrade emerald device");
		Landingpage.devicesTab(driver).click();
		timer(driver);
		Landingpage.devicesUpgrades(driver).click();
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		Devices.emeraldTab(driver).click();
		log.info("Successfully navigated to upgrade emerald device");
	}

	/**
	 * Reboots device through SSH
	 * 
	 * SSH's into a device and runs the reboot command. Pings the device to check
	 * when the device is online again and then checks that the webservice on the
	 * device is running
	 * 
	 * @param ip
	 *            The IP address of the device
	 * @param userName
	 *            Login username for the device
	 * @param password
	 *            Login password for the device
	 * 
	 * @throws InterruptedException
	 */
	public void rebootDeviceSSH(String ip, String userName, String password, long millSeconds)
			throws InterruptedException {
		Ssh shell = new Ssh(userName, password, ip);
		int retry = 0;
		while (retry < 5) {
			try {
				shell.loginToServer();
				retry = 6;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("SSH login failed. Retrying");
				Thread.sleep(5000);
				retry++;
			}
		}
		String command = "reboot";
		if (StartupTestCase.isEmerald)
			command = "/sbin/reboot";
		shell.sendCommand(command);
		log.info("Waiting while device reboots");
		shell.disconnect();

		checkReboot(ip, userName, password);
	}

	/**
	 * Logs into an appliance using SSH to check if the appliance has rebooted by
	 * pinging the device ip address
	 * 
	 * @param ip
	 *            - IP of the device to check
	 * @param userName
	 *            - Username used to log into the device through SSH
	 * @param password
	 *            - Password used to log into the device through SSH
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void checkReboot(String ip, String userName, String password) throws InterruptedException, AssertionError {
		Ssh shell = new Ssh(userName, password, ip);
		// first ping to make sure device has gone off line
		SystemMethods sysMethods = new SystemMethods();
		int deviceOffline = 0;
		while (sysMethods.pingIpAddress(ip) && deviceOffline < 50) {
			log.info("device has not gone offline yet.. rechceking:" + deviceOffline);
			deviceOffline++;
			Thread.sleep(2000);
		}
		if (!sysMethods.pingIpAddress(ip)) {
			log.info("device has gone offline");
		}

		int counter = 0;
		boolean isUp = false;
		while (!sysMethods.pingIpAddress(ip) && counter < 10) {
			log.info("device not up yet. Checking again");
			counter++;
		}
		if (counter >= 10) {
			log.info("DEVICE WAS NOT PINGABLE. MAY CAUSE TEST ERRORS PLEASE CHECK");
		}
		counter = 0;
		Ssh shell2 = null;
		if (sysMethods.pingIpAddress(ip)) {
			log.info("Device is pingable. Checking for webservice");
			Thread.sleep(5000);
			boolean isLoggedIn = false;
			int loginCounter = 0;
			while (!isLoggedIn && loginCounter < 5) {
				shell2 = new Ssh(userName, password, ip);
				isLoggedIn = shell2.loginToServer();
				loginCounter++;
				Thread.sleep(2000);
			}
			boolean isWebService = false;
			while (!isWebService && counter < 30) {
				String output = shell2.sendCommand("ps -ax");
				if (output.contains("/usr/bin/webservice")) {
					log.info("WebSerivce is running. Device full rebooted");
					isWebService = true;
				} else {
					counter++;
					log.info("WebService is not running. checking again..");
					Thread.sleep(1000);
				}
			}
		}
		if (counter > 60) {
			log.info("ERROR... WEBSERVICE DID NOT START. MAY CAUSE TEST FAILS. PLEASE CHECK");
		}
		shell2.disconnect();
	}

	/**
	 * Pushes the appliance database (CloudData.xml) from boxilla to appliances
	 * 
	 * @throws InterruptedException
	 */
	public void recreateCloudData(String rxIp, String txIp) throws InterruptedException {
		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, rxIp);
		shell.loginToServer();
		String command = "";
		String check = "cat /opt/blackbox/startgui.sh";
		check = shell.sendCommand(check);
		log.info("Check:" + check);
		if (!check.contains("source /opt/blackbox/script_output.sh")) {
			command = "/opt/cloudium/startgui.sh&";
		} else {
			command = "/opt/blackbox/startgui.sh&";
		}
		String output = shell.sendCommand(command);
		log.info("Output from startgui: " + output);
		Thread.sleep(10000);

		shell.disconnect();

	}

	public void recreateCloudData(String rxIp, String txIp, boolean force) throws InterruptedException {
		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, rxIp);
		shell.loginToServer();
		String command = "";
		String check = "cat /opt/blackbox/startgui.sh";

		command = "/opt/blackbox/startgui.sh&";

		String output = shell.sendCommand(command);
		log.info("Output from startgui: " + output);
		Thread.sleep(10000);

		shell.disconnect();
	}

	/**
	 * Will log into a device using SSH and get the device type which is returned
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - The IP of the device to get the type
	 * @return String containing the device type
	 */
	public String getDeviceTypeForUpgrade(WebDriver driver, String deviceIp) {
		navigateToDeviceStatus(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceIp);
		String out = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		if (out.contains("EMD2000SE-R") || out.contains("EMD2002SE-R")) {
			log.info("Device is a Single or Dual head SE Receiver");
			return "SE-R";
		} else if (out.contains("EMD200DV-T") || out.contains("EMD200DV-T")) {
			log.info("Device is a ZeroU Transmitter");
			return "ZEROU";
		} else if (out.contains("DTX1002-T") || out.contains("DTX1000-T")) { /// need to add other types of corrib TX
																				/// devices
			log.info("Device is a DTX Transmitter");
			return "DTX-T";
		} else if (out.contains("EMD2002PE-R") || out.contains("EMD2000PE-R")) {
			log.info("Device is a PE receiver");
			return "PE-R";
		} else if (out.contains("EMD2000SE-T") || out.contains("EMD2002SE-T")) {
			log.info("Device is a SE Transmitter");
			return "SE-T";
		} else if (out.contains("EMD2002PE-T") || out.contains("EMD2000PE-T")) {
			log.info("Deivce is PE transmitter");
			return "PE-T";
		} else if (out.contains("DTX1000-R") || out.contains("DTX1002-R")) {
			log.info("DEvice is a DTX Receiver");
			return "DTX-R";
		} else if (out.contains("EMD4000T")) {
			log.info("Device is a 4K Transmitter");
			return "4K-T";
		} else if (out.contains("EMD4000R")) {
			log.info("Device is 4K Receiver");
			return "4K-R";
		} else {
			log.info("Device type is not recoginsied");
			throw new AssertionError("Device type is not recoginsied:" + out);
		}
	}

	/**
	 * Pushes the appliance database (CloudData.xml) from boxilla to appliances
	 * 
	 * @throws InterruptedException
	 */
	public String recreateCloudData(String rxIp) throws InterruptedException {
		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, rxIp);
		shell.loginToServer();

		String check = shell.sendCommand("cat /opt/blackbox/startgui.sh");
		log.info("Check:" + check);

		Thread t1 = new Thread(new Runnable() {
			public void run() {
				String command = "";
				if (!check.contains("function")) {
					command = "/opt/cloudium/startgui.sh&";
				} else {
					command = "/opt/blackbox/startgui.sh&";
				}
				String output = shell.sendCommand(command);
			}
		});
		t1.start();

		Thread.sleep(10000);
		shell.disconnect();
		shell.loginToServer();
		String out = shell.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		shell.disconnect();
		return out;

	}

	@Deprecated
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
	}

	/**
	 * Noavigates to the device bulk update modal. Waits until the save button is
	 * present
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToBulkUpdate(WebDriver driver) throws InterruptedException {
		log.info("Navigating to Bulk Update Settings Modal..");
		navigateToDeviceStatus(driver);
		int counter = 0;
		while (counter < 6) {
			try {
				Devices.bulkUpdateBtn(driver).click();
				new WebDriverWait(driver, 10)
						.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getBulkUpdateSaveBtnXpath())));
				log.info("Bulk update model appeared");
				counter = 6;
			} catch (Exception e) {
				log.info("*****************************");
				log.info("Bulk update modal did not appear. Retrying:" + counter++);
			}

		}

	}

	/**
	 * This will check if a certain devices status has changed to configured. If the
	 * devices status is not configured it will wait for 60 seconds and check again
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - The ip address of the device to check
	 * @throws InterruptedException
	 */
	public void checkStatusConfigured(WebDriver driver, String deviceIp) throws InterruptedException {
		log.info("Attempting to check device status Configured for device " + deviceIp);
		navigateToDeviceStatus(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceIp);
		String deviceDetails = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		log.info("Table details:" + deviceDetails);
		if (!deviceDetails.contains("Configured") && !deviceDetails.contains("configured")) {
			log.info("Device was not configured. Waiting one minute and trying again");
			Thread.sleep(60000);
			driver.navigate().refresh();
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceIp);
			deviceDetails = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		}

		Assert.assertTrue(deviceDetails.contains("Configured") || deviceDetails.contains("configured"),
				"Device table did not contain Configured, actual: " + deviceDetails);
		log.info("Device is configured");
	}

	/**
	 * Will check if HTTP is enabled on a receiver device. Will return true if HTTP
	 * is enabled
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - IP address of the receiver to check
	 * @return boolean true if HTTP is enabled, else false
	 * @throws InterruptedException
	 */
	public boolean checkHttpEnabled(WebDriver driver, String deviceIp) throws InterruptedException {
		log.info("Checking if http is enabled for device  " + deviceIp);
		navigateToDeviceStatus(driver);
		SeleniumActions.seleniumClick(driver, Devices.getMiscSettingsXpath());
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, deviceIp);
		String status = SeleniumActions.seleniumGetText(driver, Devices.httpEnabledStatus);
		log.info("status is " + status);
		if (status.contains("Enabled")) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * This will change a receivers HTTP to enabled through Boxilla
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - IP address of the receiver to change HTTP
	 * @throws InterruptedException
	 */
	public void changedHttpToEnabled(WebDriver driver, String deviceIp) throws InterruptedException {
		try {
			log.info("Attempting to change http to enabled for device " + deviceIp);
			checkStatusConfigured(driver, deviceIp);
			boolean isEnabled = checkHttpEnabled(driver, deviceIp);
			if (!isEnabled) {
				log.info("HTTP is not enabled. Enabling");
				setUniquePropertyRx(driver, deviceIp, "HTTP Enabled", true);
				log.info("HTTP has been enabled. Pausing for device to configure");
				Thread.sleep(60000);
			} else {
				log.info("HTTP Enabled is already enabled.");
			}
		} catch (Exception e) {
			log.info("Error changing HTTP to enabled");
		}
	}

	/**
	 * Method to select devices for bulk update.
	 * 
	 * Will navigate to the bulk update modal. Select the passed in device type and
	 * template name. The name of each device to be updated should be added to an
	 * array and passed in.
	 * 
	 * @param driver
	 * @param deviceType
	 * @param templateName
	 * @param deviceList
	 * @throws InterruptedException
	 */
	public void bulkUpdate(WebDriver driver, String deviceType, String templateName, String[] deviceList)
			throws InterruptedException {
		log.info("Attempting bulk update");
		navigateToBulkUpdate(driver);
		Devices.bulkUpdateApplianceTypeDropdown(driver, deviceType);
		SeleniumActions.seleniumDropdown(driver, Devices.bulkUpdateTemplateNameDropdownXpath, templateName);
		for (String s : deviceList) {
			log.info("Selecting device: " + s);
			timer(driver);
			Devices.bulkUpdateSearchBox(driver).sendKeys(s);
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.getBulkUpdateDeviceCheckboxXpath());
			Devices.bulkUpdateSearchBox(driver).clear();
		}
		Devices.bulkUpdateSearchBox(driver).clear();
//		Devices.bulkUpdateSearchBox(driver).sendKeys(Keys.RETURN);
		// save
		SeleniumActions.seleniumClick(driver, Devices.getBulkUpdateSaveBtnXpath());
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			log.info("No alert popup");
		}
//		timer(driver);
//		Thread.sleep(4000);
//		String message = Devices.getDeviceToastMessage(driver).getText();
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Devices.getDeviceToastMessage(driver)));
//		System.out.println("Bulk update toast message " + message);
		// Assert.assertTrue(message.contains("Success"),"Toast did not contain success,
		// actual " + message);
		log.info("Bulk update initiated");

	}

	/**
	 * Sets a single unique property for a receiver device
	 * 
	 * @param driver
	 * @param ipAddress
	 * @param property
	 * @param value
	 * @throws InterruptedException
	 */
	public void setUniquePropertyRx(WebDriver driver, String ipAddress, String property, boolean value)
			throws InterruptedException {
		log.info("Attempting to set unique property " + property + " for RX device: " + ipAddress);
		navigateToEditSettingsRx(driver, ipAddress);
		Devices.editRxSettingsSettingDropdown(driver, "Unique");
		timer(driver);

		switch (property) {
		case "Power Mode":
			Devices.uniquePowerModeDropdown(driver, value);
			break;
		case "HTTP Enabled":
			Devices.uniqueHttpEnabledDropdown(driver, value);
			break;
		}
		saveEditRxSettings(driver); // done
		log.info("Unique property set");

	}

	/**
	 * Sets a single unique property for a transmitter device
	 * 
	 * @param driver
	 * @param ipAddress
	 * @param property
	 * @param value
	 * @throws InterruptedException
	 */
	public void setUniquePropertyTx(WebDriver driver, String ipAddress, String property, String value)
			throws InterruptedException {
		log.info("Attempting to set unique property " + property + " for TX device: " + ipAddress);
		navigateToEditSettingsTx(driver, ipAddress);
		Devices.editTxSettingsSettingDropdown(driver, "Unique");
		timer(driver);

		switch (property) {
		case "Video Quality":
			Devices.uniqueVideoQualityDropdown(driver, value);
			break;
		case "Video Source":
			Devices.uniqueVideoSourceDropdown(driver, value);
			break;
		case "HID":
			Devices.uniqueHidDropdown(driver, value);
			break;
		case "Audio":
			Devices.uniqueAudioDropdown(driver, value);
			break;
		case "Mouse Timeout":
			Devices.uniqueMouseTimeoutDropdown(driver, value);
			break;
		case "EDID1":
			Devices.uniqueEdid1Dropdown(driver, value);
			break;
		case "EDID2":
			Devices.uniqueEdid2Dropdown(driver, value);
			break;
		}
		saveEditTxSettings(driver); /// done
		log.info("Unique property set");
	}

	/**
	 * Brings you to the device status page from anywhere in Boxilla
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToDeviceStatus(WebDriver driver) {
		log.info("Attempting to navigate to device status");
		SeleniumActions.seleniumClick(driver, "//span[contains(.,'Devices')]");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.devicesStatus(driver)));
		Landingpage.devicesStatus(driver).click();
		// check if page appeared
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath(Devices.getsystemPropertiesButtonXpath())));
		log.info("Successfully navigated to device status");
	}

	/**
	 * Edit the template for a receiver. Template name is passed along the two
	 * booleans for dropdowns with 2 options each
	 * 
	 * @param driver
	 * @param templateName
	 * @param isManual
	 * @param isEnabled
	 * @throws InterruptedException
	 */
	public void editTemplateReceiver(WebDriver driver, String templateName, boolean isManual, boolean isEnabled)
			throws InterruptedException {
		log.info("Attempting to edit receiver template " + templateName);
		navigateToEditTemplate(driver);
		Devices.templateNameDropdown(driver, templateName);
		Devices.editTemplatePowerModeDropdown(driver, false);
		Devices.editTemplateHttpEnabledDropdown(driver, true);
		timer(driver);
		Devices.editTemplateRxSaveBtn(driver).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		// assert the template saved
		timer(driver);
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Success"), "The toast message did not contain success, actual " + message);
		log.info("Successsfully edited RX template");
	}

	/**
	 * Navigates to the delete template modal
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToDeleteTemplate(WebDriver driver) throws InterruptedException {
		log.info("Attempting to navigate to delete template");
		navigateToDeviceStatus(driver);
		WebElement delete=Devices.deleteTemplate(driver);
		Actions action=new Actions(driver);
		action.moveToElement(delete).build().perform();
		action.click().build().perform();
//		new WebDriverWait(driver, 60)
//				.until(ExpectedConditions.elementToBeClickable(Devices.deleteTemplateDeleteBtn(driver)));
		log.info("Successfully navigated to delete template");

	}

	/**
	 * deletes the template with the passed in name
	 * 
	 * @param driver
	 * @param templateName
	 * @throws InterruptedException
	 */
	public void deleteTemplate(WebDriver driver, String templateName) throws InterruptedException {
		log.info("Attempting to delete template " + templateName);
		driver.navigate().refresh();
		navigateToDeleteTemplate(driver);
		Devices.deleteTemplateTemplateNameDropdown(driver, templateName);
		timer(driver);
		Devices.deleteTemplateDeleteBtn(driver).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);

		// assert toast message
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Success"), "Toas message did not contain Success, actual " + message);
		timer(driver);
		log.info("Successfully deleted template " + templateName);
	}

	/**
	 * Edit a template for a TX device. Template name is passed and any drop down
	 * values to be changed
	 * 
	 * @param driver
	 * @param templateName
	 * @param videoQuality
	 * @param videoSource
	 * @param hidConfig
	 * @param audio
	 * @param mouseTimeout
	 * @param edidDvi1
	 * @param edidDvi2
	 * @throws InterruptedException
	 */
	public void editTemplateTransmitter(WebDriver driver, String templateName, String videoQuality, String videoSource,
			String hidConfig, String audio, String mouseTimeout, String edidDvi1, String edidDvi2)
			throws InterruptedException {
		log.info("Attempting to edit TX template " + templateName);
		navigateToEditTemplate(driver);
		Devices.editTemplateTemplateNameDropdown(driver, templateName);
		Devices.editTemplateVideoQualityDropDown(driver, videoQuality);
		Devices.editTemplateVideoSourceDropdown(driver, videoSource);
		Devices.editTemplateHidConfigurationDropdown(driver, hidConfig);
		Devices.editTemplateMouseTimeoutDropdown(driver, mouseTimeout);
		Devices.editTemplateEdidSettingsDvi1Dropdown(driver, edidDvi1);
		Devices.editTemplateEdidSettingsDvi2Dropdown(driver, edidDvi2);
		Devices.editTemplateTxSaveBtn(driver).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
		// assert the toast message
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Success"), "The toast message did not contain success, actual " + message);
		log.info("Successfully edited " + templateName);

	}

	/**
	 * Navigates to the device system properties
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToSystemProperties(WebDriver driver) throws InterruptedException {
		log.info("Attempting to navigate to System properties");
		navigateToDeviceStatus(driver);
		int counter = 0;
		while (counter < 5) {
			try {
				Devices.systemPropertiesBtn(driver).click();
				// check it modal appeared
				new WebDriverWait(driver, 10).until(ExpectedConditions.elementToBeClickable(Devices.spSaveBtn(driver)));
				log.info("Navigated to System properties");
				counter = 6;
			} catch (Exception e) {
				log.info("*******************************************************************************");
				log.info("System properties modal did not open retrying:" + counter++);
			}
		}
	}

	/**
	 * Navigate to the device template edit page
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToEditTemplate(WebDriver driver) throws InterruptedException {
		log.info("Attempting to navigate to System properties");
		navigateToDeviceStatus(driver);

		Devices.editTemplateBtn(driver).click();
		log.info("Successfully navigated to System properties");
	}

	/**
	 * Adds a template for a receiver. Pass in the template name and the options.
	 * Receiver only has 2 options with 2 optioons in each dropdown so a boolean is
	 * used
	 * 
	 * @param driver
	 * @param templateName
	 * @param powerMode
	 * @param isHttpEnabled
	 * @throws InterruptedException
	 */
	public void addTemplateReceiver(WebDriver driver, String templateName, boolean powerMode, boolean isHttpEnabled)
			throws InterruptedException {
		log.info("Attemprting to add receiver template: " + templateName);
		navigateToDevicePropertyCreateTemplate(driver);
		Devices.applianceTypeDropdown(driver, true);
		Devices.templateNameTextBox(driver).sendKeys(templateName);
		Devices.PowerModeDropdown(driver, powerMode);
		Devices.HttpEnabledDropdown(driver, isHttpEnabled);
		timer(driver);
		Devices.saveTemplateRxBtn(driver).click();
		Thread.sleep(4000);
		timer(driver);

		// assert template is created
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Success"), "Toast message did not contain success, actual " + message);
		log.info("Successfully added template for receiver");

	}

	/**
	 * Adds a template for a TX device. Pass in the template name and values from
	 * the drop down. Clicks save and asserts the toast message displays success
	 * 
	 * @param driver
	 * @param templateName
	 * @param videoQuality
	 * @param videoSource
	 * @param hidConfig
	 * @param audio
	 * @param mouseTimeout
	 * @param edidDvi1
	 * @param edidDvi2
	 * @throws InterruptedException
	 */
	public void addTemplateTransmitter(WebDriver driver, String templateName, String videoQuality, String videoSource,
			String hidConfig, String audio, String mouseTimeout, String edidDvi1, String edidDvi2)
			throws InterruptedException {
		log.info("Attempting to add a template for transmitter: " + templateName);
		navigateToDevicePropertyCreateTemplate(driver);
		Devices.applianceTypeDropdown(driver, false);
		Devices.templateNameTextBox(driver).sendKeys(templateName);
		SeleniumActions.seleniumDropdown(driver, Devices.videoQualityDropdownXpath, videoQuality);
		Devices.videoSourceDropdown(driver, videoSource);
		Devices.HIDConfigurationDropdown(driver, hidConfig);
		Devices.mouseTimeoutDropdown(driver, mouseTimeout);
		Devices.EdidSettingsDvi1Dropdown(driver, edidDvi1);
		Devices.EdidSettingsDvi2Dropdown(driver, edidDvi2);
		Devices.audioDropdown(driver, audio);
		Thread.sleep(3000);
		Devices.saveTemplateTxBtn(driver).click();
		Thread.sleep(4000);

		// assert template is created
		String message = Devices.getDeviceToastMessage(driver).getText();
		Assert.assertTrue(message.contains("Success"), "Toast message did not contain success, actual " + message);
		timer(driver);
		log.info("Successfully added template for transmitter");
	}

	/**
	 * Navigate to device add template
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToDevicePropertyCreateTemplate(WebDriver driver) throws InterruptedException {
		log.info("Attempting to navigate to device create template");
		navigateToDeviceStatus(driver);
		int counter = 0;
		while (counter < 5) { // sometimes the modal wont open with webdriver so we retry
			try {
				new WebDriverWait(driver, 60)
						.until(ExpectedConditions.elementToBeClickable(Devices.addTemplateBtn(driver)));
				Devices.addTemplateBtn(driver).click();
				new WebDriverWait(driver, 60)
						.until(ExpectedConditions.elementToBeClickable(Devices.saveTemplateTxBtn(driver)));
				counter = 6;
			} catch (Exception e) {
				log.info("Issue with templte modal opening. Retrying");
				counter++;
				driver.navigate().refresh();
			}
		}
		log.info("Successfully navigated to device create template");
	}

	/**
	 * Sets all the device system properties, Most take in the text that is in the
	 * dropdown menu, dropdowns with 2 options take a boolean
	 * 
	 * @param driver
	 * @param videoQuality
	 * @param videoSource
	 * @param hidConfig
	 * @param audio
	 * @param mouseTimeout
	 * @param edidDvi1
	 * @param edidDvi2
	 * @param isPowerModeManual
	 * @param isHttpEnabled
	 * @throws InterruptedException
	 */
	public void setSystemProperties(WebDriver driver, String videoQuality, String videoSource, String hidConfig,
			String audio, String mouseTimeout, String edidDvi1, String edidDvi2, boolean isPowerModeManual,
			boolean isHttpEnabled) throws InterruptedException {
		log.info("Attempting to set device system properties");
		navigateToSystemProperties(driver);
		log.info("Setting the following properties, Video Quality:" + videoQuality + " Video Source:" + videoSource
				+ " HID:" + hidConfig + " Audio:" + audio + " Mouse Timeout:" + mouseTimeout + " EDID DVI1: " + edidDvi1
				+ " EDID DVI1:" + edidDvi1 + " Power Mode Manual:" + isPowerModeManual + " Is HTTP Enabled:"
				+ isHttpEnabled);

		// set properties
		timer(driver);
		Devices.spVideoQualityDropdown(driver, videoQuality);
		timer(driver);
		Devices.spVideoSourceDropdown(driver, videoSource);

		Devices.spHidConfigurationDropdown(driver, hidConfig);

		Devices.spMouseTimeoutDropdown(driver, mouseTimeout);

		Devices.spEdidDvi1Dropdown(driver, edidDvi1);

		Devices.spEdidDvi2Dropdown(driver, edidDvi2);
		Devices.audioSourceDropdown(driver, audio);

		Devices.spPowerModeDropdown(driver, isPowerModeManual);

		Devices.spHttpEnabledDropdown(driver, isHttpEnabled);
		Thread.sleep(2000);
		saveSystemProperty(driver);
		timer(driver);
		log.info("Successfully set device system properties");

	}

	/**
	 * hits the save button on the system property modal and asserts the toast
	 * message
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void saveSystemProperty(WebDriver driver) throws InterruptedException {
		log.info("Attempting to save device system properties");
		timer(driver);
		Devices.spSaveBtn(driver).click();
		try {
			Alert alert = driver.switchTo().alert();
			alert.accept();
		} catch (Exception e) {
			log.info("No TX settings changed so no alert pop up");
		}
		// timer(driver);
		// assert if successful
//		Thread.sleep(3000);
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String message = Devices.getDeviceToastMessage(driver).getText();
		log.info("Pop up message: " + message);
		timer(driver);
		if (message.equals("Error"))
			SeleniumActions.seleniumClick(driver, Devices.getSpCancelBtnXpath());
		timer(driver);
		log.info("Successfully saved device system properties");
	}

	/**
	 * Saves a system property for a receiver device
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void saveSystemPropertyRX(WebDriver driver) throws InterruptedException {
		log.info("Attempting to save device system properties");
		timer(driver);
		Devices.spSaveBtn(driver).click();
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
		// assert if successful
		WebDriverWait wait = new WebDriverWait(driver, 30);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String message = Devices.getDeviceToastMessage(driver).getText();
		log.info("Pop up message: " + message);
		timer(driver);
		if (message.equals("Error"))
			SeleniumActions.seleniumClick(driver, Devices.getSpCancelBtnXpath());
		timer(driver);
		log.info("Successfully saved device system properties");
	}

	/**
	 * Saves RX settings after edit
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void saveEditRxSettings(WebDriver driver) throws InterruptedException {
		log.info("Attempting to save RX device settings");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.getEditRxSaveBtnXpath());
		// timer(driver);
		Thread.sleep(4000);
		// new WebDriverWait(driver,
		// 60).until(ExpectedConditions.visibilityOf(Devices.getDeviceToastMessage(driver)));
		String message = Devices.getDeviceToastMessage(driver).getText();
		log.info("Pop up message: " + message);
		timer(driver);
		if (message.equals("Error"))
			SeleniumActions.seleniumClick(driver, Devices.getEditRxCancelXpath());
		timer(driver);
		log.info("Successfully saved RX device settings");
	}

	/**
	 * Saves TX settings after edit
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void saveEditTxSettings(WebDriver driver) throws InterruptedException {
		log.info("Attempting to save transmitter properties");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.getEditTxSaveBtnXpath());
		// assert if successful
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
//		Thread.sleep(3000);

		new WebDriverWait(driver,
		 60).until(ExpectedConditions.visibilityOf(Devices.getDeviceToastMessage(driver)));
		String message = Devices.getDeviceToastMessage(driver).getText();
		log.info("Pop up message: " + message);
		timer(driver);
		if (message.equals("Error")) {
			SeleniumActions.seleniumClick(driver, Devices.getEditTxCancelXpath());
			throw new AssertionError("Unable to save TX Settings. Toast error");
		}

		timer(driver);
		log.info("Successfully saved transmitter properties");
	}

	/**
	 * Used to change a single receiver system property Values for property are -
	 * Power Mode, HTTP Enabled. Setting is a boolean. For Power Mode true == Manual
	 * Fro HTTP Enabled true == Enabled
	 * 
	 * @param driver
	 * @param property
	 * @param setting
	 * @throws InterruptedException
	 */
	public void setSingleSystemPropertyReceiver(WebDriver driver, String property, boolean setting)
			throws InterruptedException {
		log.info("Attempting to set single RX device system property : " + property);
		navigateToSystemProperties(driver);
		timer(driver);
		switch (property) {
		case "Power Mode":
			Devices.spPowerModeDropdown(driver, setting);
			saveSystemPropertyRX(driver);
			break;

		case "HTTP Enabled":
			Devices.spHttpEnabledDropdown(driver, setting);
			saveSystemPropertyRX(driver);
			break;
		}
		log.info("Successfully set sing RX device system property");
	}

	/**
	 * Used to change a single transmitter system property. Values for property are
	 * - Video Quality, Video Source, HID Config, Audio, Mouse Timeout, EDID DVI1,
	 * EDID DVI2 Setting is the value of the drop down
	 * 
	 * @param driver
	 * @param property
	 * @param setting
	 * @throws InterruptedException
	 */
	public void setSingleSystemPropertyTransmitter(WebDriver driver, String property, String setting)
			throws InterruptedException {
		log.info("Attempting to changing single TX system property: " + property + " to " + setting);
		navigateToSystemProperties(driver);
		timer(driver);
		switch (property) {

		case "Video Quality":
			Devices.spVideoQualityDropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "Video Source":
			Devices.spVideoSourceDropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "HID Config":
			Devices.spHidConfigurationDropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "Audio":
			Devices.spAudioDropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "Mouse Timeout":
			Devices.spMouseTimeoutDropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "EDID DVI1":
			Devices.spEdidDvi1Dropdown(driver, setting);
			saveSystemProperty(driver);
			break;

		case "EDID DVI2":
			Devices.spEdidDvi2Dropdown(driver, setting);
			saveSystemProperty(driver);
			break;
		}
		log.info("Successfully set single TX device system property");
	}

	/**
	 * Navigate from anywhere in boxilla to the device upgrade page
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToUpgrade(WebDriver driver) throws InterruptedException {
		timer(driver);
		Landingpage.devicesTab(driver).click();
		log.info("Upgrading device : Devices dropdown clicked");
		timer(driver);
		int x = 0;
		while (x < 5) {
			try {
				Landingpage.devicesUpgrades(driver).click();
				x = 5;
			} catch (Exception e) {
				log.info("try again");
				x++;
			}
		}
		log.info("Upgrading device : Upgrades tab clicked");
		timer(driver);
		timer(driver);
		timer(driver);
		timer(driver);
		timer(driver);
		String title = driver.getTitle();
		Assert.assertTrue(title.contentEquals("Boxilla - Devices | Upgrades"),
				"Title did not equal: Boxilla - Devices | Upgrades, actual text: " + title);
		log.info("Upgrading device : Page title asserted");
	}

	/**
	 * Upload a version of software for devices
	 * 
	 * @param driver
	 * @param appliance
	 * @param versionNumber
	 * @throws InterruptedException
	 */
	public void uploadVersion(WebDriver driver, String appliance, String versionNumber) throws InterruptedException {
		// if appliance is tx
		if (appliance.equalsIgnoreCase("tx")) {
			log.info("Uploading Version : Started");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Uploading Version : Releases tab clicked");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.txTab);
			log.info("Uploading Version : DTX-T clciked ");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Devices.searchboxTX, versionNumber);
			log.info("Uploading Version : Version number entered in search box");
			log.info("Uploading Version : Checking if Version is Uploaded");
			timer(driver);
			// Upload check
			if (SeleniumActions.seleniumGetText(driver, Devices.txTableVersionNumber).equalsIgnoreCase(versionNumber)) {
				log.info("Uploading Version : Version already uploaded.. ");
				timer(driver);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxTX);
				log.info("Uploading Version : Search box cleared");
			} else {
				log.info("Uploading Version : Version not found.. Uploading..");
				timer(driver);
				log.info("Uploading Version : Started");
				SeleniumActions.seleniumClick(driver, Devices.uploadBtn);
				log.info("Uploading Version : Upload button clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Devices.uploadElement,
						"C:\\Selenium\\Corrib_Version\\" + "TX" + "_DTX_" + versionNumber + ".clu");
				log.info("Uploading Version : Version Selected to upload");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.applyBtn);
				log.info("Uploading Version : Apply button clicked");
				Thread.sleep(20000);
				String title = driver.getTitle();
				Assert.assertTrue(title.contentEquals("Boxilla - Devices | Upgrades"),
						"Title did not contain: Boxilla - Devices | Upgrades, actual text: " + title);
				log.info("Uploading Version : Version uploaded successfully.. Researching version");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.releasesTab);
				log.info("Uploading Version : Releases tab clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.txTab);
				log.info("Uploading Version : DTX-T clciked ");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Devices.searchboxTX, versionNumber);
				log.info("Uploading Version : Version number entered in search box");
				timer(driver);
				String txTable = SeleniumActions.seleniumGetText(driver, Devices.txTable);
				Assert.assertTrue(txTable.contains(versionNumber),
						"TX table did not contain: " + versionNumber + ", actual text:" + txTable);
				timer(driver);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxTX);
				// Calling devices upgrade method to redirect to the Device > Upgrades
				log.info("Uploading Version : Found searched version.. Checking State");
			}
		} else if (appliance.equalsIgnoreCase("rx")) {
			log.info("Uploading Version : Started");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Uploading Version : Releases tab clicked");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.rxTab);
			log.info("Uploading Version : DTX-R clciked ");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Devices.searchboxRX, versionNumber);
			log.info("Uploading Version : Version number entered in search box");
			log.info("Uploading Version : Checking if Version is Uploaded");
			timer(driver);
			// Upload check
			if (SeleniumActions.seleniumGetText(driver, Devices.rxTableVersionNumber).equalsIgnoreCase(versionNumber)) {
				log.info("Uploading Version : Version already uploaded.. ");
				timer(driver);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxRX);
				log.info("Uploading Version : Search box cleared");
			} else {
				log.info("Uploading Version : Version not found.. Uploading..");
				timer(driver);
				log.info("Uploading Version : Started");
				SeleniumActions.seleniumClick(driver, Devices.uploadBtn);
				log.info("Uploading Version : Upload button clicked");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Devices.uploadElement,
						"C:\\Selenium\\Corrib_Version\\" + "RX" + "_DTX_" + versionNumber + ".clu");
				log.info("Uploading Version : Version Selected to upload");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.applyBtn);
				log.info("Uploading Version : Apply button clicked");
				driver.manage().timeouts().implicitlyWait(60, TimeUnit.SECONDS);
				String title = driver.getTitle();
				Assert.assertTrue(title.contentEquals("Boxilla - Devices | Upgrades"),
						"Title did not equal: Boxilla - Devices | Upgrades, actual text: " + title);
				log.info("Uploading Version : Version uploaded successfully.. Researching version");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.releasesTab);
				log.info("Uploading Version : Releases tab clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.rxTab);
				log.info("Uploading Version : DTX-R clciked ");
				timer(driver);
				SeleniumActions.seleniumSendKeys(driver, Devices.searchboxRX, versionNumber);
				log.info("Uploading Version : Version number entered in search box");
				timer(driver);
				String rxTable = SeleniumActions.seleniumGetText(driver, Devices.rxTable);
				Assert.assertTrue(rxTable.contains(versionNumber),
						"Title did not contain: " + versionNumber + ", actual text: " + rxTable);
				timer(driver);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxRX);
				// Calling devices upgrade method to redirect to the Device > Upgrades
				log.info("Uploading Version : Found searched version.. Checking State");
			}
		} else {
			new SkipException("***** Could not recognize appliance type");
		}

		log.info("Upgrading device : Version Upload Completed");
	}

	/**
	 * Will navigate to upgrades and active and active the passed in versionNumber
	 * 
	 * @param driver
	 * @param appliance
	 * @param versionNumber
	 * @throws InterruptedException
	 */
	public void activateVersionEmerald(WebDriver driver, String appliance, String versionNumber)
			throws InterruptedException {
		if (appliance.equalsIgnoreCase("tx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			Devices.emeraldReleaseTabTransmitter(driver).click();
			log.info("Activating version : Clicked on Emerald TX tab");
			timer(driver);
			Devices.searchBoxEmeraldTx(driver).clear();
			Devices.searchBoxEmeraldTx(driver).sendKeys(versionNumber);
			log.info("Activating Version : Version number entered in the search box");

			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveEmeraldTX).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnEmeraldTX);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionEmeraldTX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else if (appliance.equalsIgnoreCase("rx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			Devices.emeraldReleaseTabReceiver(driver).click();
			log.info("Activating version : Clicked on Emerald TX tab");
			timer(driver);
			SeleniumActions.seleniumSendKeysClear(driver, Devices.emeraldRxSearchBox);
			SeleniumActions.seleniumSendKeys(driver, Devices.emeraldRxSearchBox, versionNumber);
			log.info("Activating Version : Version number entered in the search box");
			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveEmeraldRX).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnEmeraldRX);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionEmeraldRX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else {
			new SkipException("***** Could not recognize appliance type *****");
		}

	}

	/**
	 * Will navigate to the devices upgrade page, click the zeroU upgrade tab and
	 * select the version number from the table and activate it
	 * 
	 * @param driver
	 * @param appliance
	 *            - rx or tx
	 * @param versionNumber
	 *            - The full appliance upgrade string
	 * @throws InterruptedException
	 */
	public void activateVersionZeroU(WebDriver driver, String appliance, String versionNumber)
			throws InterruptedException {
		if (appliance.equalsIgnoreCase("tx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.getZerouUpgradeTabXpath());
			log.info("Activating version : Clicked on ZeroU TX tab");
			timer(driver);
			SeleniumActions.seleniumSendKeysClear(driver, Devices.getZeroUSearchBox());
			SeleniumActions.seleniumSendKeys(driver, Devices.getZeroUSearchBox(), versionNumber);
			log.info("Activating Version : Version number entered in the search box");

			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveZeroUTx).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnZeroU);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionZeroU);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else if (appliance.equalsIgnoreCase("rx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			Devices.getEmeraldSeReceiverTab(driver).click();
			log.info("Activating version : Clicked on Emerald TX tab");
			timer(driver);
			Devices.getEmeraldSeSearchBoxRx(driver).clear();
			Devices.getEmeraldSeSearchBoxRx(driver).sendKeys(versionNumber);
			log.info("Activating Version : Version number entered in the search box");
			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveEmeraldSeRx).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnEmeraldSeRx);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionEmeraldSeRX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else {
			new SkipException("***** Could not recognize appliance type *****");
		}

	}

	/**
	 * This will navigate to device upgrade and select the emerald SE tab for either
	 * RX or TX. It will then search the table and activate the release version
	 * 
	 * @param driver
	 * @param appliance
	 *            - rx or tx
	 * @param versionNumber
	 *            - The full device upgrade string
	 * @throws InterruptedException
	 */
	public void activateVersionEmeraldSe(WebDriver driver, String appliance, String versionNumber)
			throws InterruptedException {
		if (appliance.equalsIgnoreCase("tx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			Devices.getEmeraldSeTransmitterTab(driver).click();
			log.info("Activating version : Clicked on Emerald SE TX tab");
			timer(driver);
			Devices.getEmeraldSeSearchBox(driver).clear();
			Devices.getEmeraldSeSearchBox(driver).sendKeys(versionNumber);
			log.info("Activating Version : Version number entered in the search box");

			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveEmeraldSeTx).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnEmeraldSeTx);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionEmeraldSeTX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else if (appliance.equalsIgnoreCase("rx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			Devices.getEmeraldSeReceiverTab(driver).click();
			log.info("Activating version : Clicked on Emerald TX tab");
			timer(driver);
			Devices.getEmeraldSeSearchBoxRx(driver).clear();
			Devices.getEmeraldSeSearchBoxRx(driver).sendKeys(versionNumber);
			log.info("Activating Version : Version number entered in the search box");
			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveEmeraldSeRx).contains("No")) {
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnEmeraldSeRx);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionEmeraldSeRX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else {
			new SkipException("***** Could not recognize appliance type *****");
		}

	}

	/**
	 * Activate a specific version of software for the devices
	 * 
	 * @param driver
	 * @param appliance
	 * @param versionNumber
	 * @throws InterruptedException
	 */
	public void activateVersion(WebDriver driver, String appliance, String versionNumber) throws InterruptedException {
		// Activate Version
		if (appliance.equalsIgnoreCase("tx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.txTab);
			log.info("Activating version : Clicked on TX tab");
			timer(driver);
			SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxTX);
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Devices.searchboxTX, versionNumber);
			log.info("Activating Version : Version number entered in the search box");

			// Activate Version if it is not activated
			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveTX).contains("No")) {
				timer(driver);
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnTX);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionTX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}
			} else {
				log.info("Upgrading device : Version is active");
			}
		} else if (appliance.equalsIgnoreCase("rx")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.releasesTab);
			log.info("Activating version : Clicked on Release tab");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.rxTab);
			timer(driver);
			SeleniumActions.seleniumSendKeysClear(driver, Devices.searchboxRX);
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Devices.searchboxRX, versionNumber);
			log.info("Activating Version : Version number entered in the search box");

			// Activate Version if it is not activated
			if (SeleniumActions.seleniumGetText(driver, Devices.isActiveRX).contains("No")) {
				timer(driver);
				log.info("Upgrading device : Version is not active.. Activating version");
				SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtnRX);
				log.info("Upgrading device : Breadcrumb button clicked");
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.activateVersionRX);
				log.info("Upgrading device : Acivate button clicked");
				Alert alert = driver.switchTo().alert();
				alert.accept();
				int i = 0;
				int counter = 0;
				while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
					log.info((i + 1) + ". Activation in Progress...");
					Thread.sleep(5000);
					i++;
				}

			} else {
				log.info("Upgrading device : Version is active");
			}
		} else {
			new SkipException("***** Could not recognize appliance type *****");
		}
	}

	/**
	 * Upgrade a specific device
	 * 
	 * @param driver
	 * @param applianceIP
	 * @throws InterruptedException
	 */
	private void completeUpgrade(WebDriver driver, String applianceIP) throws InterruptedException {
		log.info("Upgarding device : Firmware Version doesn't match with active version - Upgrade required");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.allCheckbox);
		log.info("Upgrading device : All Devices checkbox unclicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.searchedDeviceCheckbox);
		log.info("Upgrading device : Searched device checkbox clicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.upgradeBtn);
		log.info("Upgrading device : Upgrade Button Clicked");
		Alert alert2 = driver.switchTo().alert();
		alert2.accept();
		Assert.assertTrue(Users.notificationMessage(driver).getText().contains("Upgrades have started."),
				"***** Error in starting Upgrade *****");
		Thread.sleep(30000); // wait for 30 seconds to clear notification message
		log.info("Upgrading device : Upgarde started waiting to complete");
		int counter = 0;
		while (!(SeleniumActions.seleniumGetText(driver, Devices.upgradeTable).contains("No Upgrade Required"))
				&& (counter < 10)) {
			log.info((counter + 1) + ". Device Upgrade in progress...");
			Thread.sleep(30000);
			driver.navigate().refresh();
			SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
			counter++;
		}

		log.info("Upgrade Device : Upgrade Completed.. Asserting..");
		timer(driver);
		driver.navigate().refresh();
		SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
		log.info("Upgrading Device : Appliance IP entered in search box");
		timer(driver);
		String deviceState = SeleniumActions.seleniumGetText(driver, Devices.state);
		Assert.assertTrue(deviceState.contains("No Upgrade Required"),
				"Device state did not contain: No Upgrade Required, actual text: " + deviceState);
		log.info("Upgrading Device : Upgrade Asserted");

	}

	/**
	 * Gets the device ID from Boxilla UI
	 * 
	 * @param driver
	 * @param ipaddress
	 * @throws InterruptedException
	 */
	public void getDeviceId(WebDriver driver, String ipaddress) throws InterruptedException {
		timer(driver);
		Landingpage.devicesTab(driver).click();
		log.info("Devices > Status > Options - Clicked on Devices tab");
		timer(driver);
		Landingpage.devicesStatus(driver).click();
		log.info("Devices > Status > Options - Clicked on Status tab");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, Devices.deviceStatusSearchBox);
		log.info("Devices > Status > Options - Device Model entered in Search box");

		List<WebElement> list = driver
				.findElements(By.xpath("//table[@id='appliance_table']//following::tr[@class='success odd']"));
		for (WebElement e : list) {
			if (e.isDisplayed()) {
				log.debug(e.getAttribute("data-id"));
			}
		}

	}

	/**
	 * Checks if a specific device is online in Boxilla UI
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void checkDeviceOnline(WebDriver driver, String ipAddress) throws InterruptedException {
		log.info("Attempting to check if device with IP address " + ipAddress + " is online");
		timer(driver);
		Landingpage.devicesTab(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.devicesStatus(driver)));
		Landingpage.devicesStatus(driver).click();
		log.info("Devices > Status > Options - Clicked on Status tab");
		timer(driver);
//		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		// check if device is online
		int timer = 0;
		int limit =40; // 40 iterations of 5 seconds = 3.3 minute
		while (timer <= limit) {
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
			log.info("Checking if device is online");
			String isOnline = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
			log.info("Is Online:" + isOnline);
			if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("OnLine") && SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("Configured")) {
				log.info("Device is online and Configured");
				break;
			} else if (timer < limit) {
				timer++;
				log.info("Device is offline. Rechecking " + timer);
				driver.navigate().refresh();
				Thread.sleep(20000);
				
			} else if (timer == limit) {
				Assert.assertTrue(1 == 0, "Device is not online");
			}
		}
		log.info("Successfully checked if device is online and Configured");
	}

	/**
	 * Checks if a specific device is offline in Boxilla UI
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void checkDeviceOffline(WebDriver driver, String ipAddress) throws InterruptedException {
		log.info("Attempting to check if device with IP address " + ipAddress + " is offline");
		timer(driver);
		Landingpage.devicesTab(driver).click();
		timer(driver);
		Landingpage.devicesStatus(driver).click();
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		// check if device is online
		int timer = 0;
		int limit = 12; // 12 iterations of 5 seconds = 1 minute
		while (timer <= limit) {
			log.info("Checking if device is offline");
			if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("OffLine")) {
				log.info("Device is OffLine");
				break;
			} else if (timer < limit) {
				timer++;
				log.info("Device is online. Rechecking " + timer);
				driver.navigate().refresh();
				Thread.sleep(5000);
			} else if (timer == limit) {
				Assert.assertTrue(1 == 0, "Device is online");
			}
		}
		log.info("Successfully checked if device is offline");
	}

	/**
	 * If device is online clicks the device options dropdown
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void navigateToOptions(WebDriver driver, String ipAddress) throws InterruptedException {
		checkDeviceOnline(driver, ipAddress);
		timer(driver);
		if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains(ipAddress)) {
			SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtn);
			log.info("Devices > Status > Options - Clicked on breadcrumb");
		} else {
			log.info("Devices > Status > Options - Searched device not found");
			throw new SkipException("***** Searched device - " + ipAddress + " not found *****");
		}

	}

	// Retrieve device details using device Name
	/**
	 * Retrive device details from Boxilla UI
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void retrieveDetails(WebDriver driver, String ipAddress, String mac, String deviceName)
			throws InterruptedException {
		// driver.navigate().refresh();
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.details);
		log.info("Retreieve Device Details - Details tab clicked");
//		Thread.sleep(20000);
//		 new WebDriverWait(driver,60).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		 new WebDriverWait(driver,
		 60).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		// new WebDriverWait(driver,
		// 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getDeviceDetailsBackBtn())));
		log.info("Getting page text and checking for ip, mac and device name");
		String pageText = driver.getPageSource();
//		System.out.println(pageText);
		Assert.assertTrue(pageText.contains(ipAddress),
				"Page did not contain IP address:" + ipAddress + " actual:" + pageText);
		log.info("IP Address asserted");
		Assert.assertTrue(pageText.contains(mac), "Page did not contain mac address:" + mac + " actual:" + pageText);
		log.info("mac address asserted");
		Assert.assertTrue(pageText.contains(deviceName),
				"Page did not contain device name:" + deviceName + " actual:" + pageText);
		log.info("Device name asserted");

	}

	// Ping device - Using device IP Address to filter
	/**
	 * Ping a specific device through boxilla
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void pingDevice(WebDriver driver, String ipAddress) throws InterruptedException {
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.ping);
		log.info("Ping device - Ping tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Successfully Pinged"),
				"Notification Message did not contain: Successfully Pinged, actual text: " + notificationMessage);
		log.info("Ping device - Notification message asserted successfully");
	}
	
	public void forceLogout(WebDriver driver,String ipAddress) throws InterruptedException
	{
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.forcelogout);
		log.info("Ping device - forcelogout tab clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(3000);
		String notificationMessage = Users.notificationMessage(driver).getText();//Force Logout successful
		Assert.assertTrue(notificationMessage.contains("Force Logout successful"),
				"Notification Message did not contain: Force Logout successful, actual text: " + notificationMessage);
		log.info("Forclogout device - Notification message asserted successfully");
	}
	public void changeDeviceZone(WebDriver driver,String ipAddress) throws InterruptedException 
	{
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.changeDeviceZone);
//	    WebElement ele=driver.findElement(By.xpath("//select[@id='zone-list']"));
	    Select select=new Select(Devices.getchangeDeviceZoneDropDown(driver));
	    select.selectByIndex(0);
	    SeleniumActions.seleniumClick(driver, Devices.changeDeviceZoneSave);
	    Thread.sleep(2000);
	    String message=Devices.getDeviceToastMessage(driver).getText();
		System.out.println(" message is "+message);
//		Assert.assertTrue(message.contains("Error"), "Toast message did not contain Error, actual " + message);
//		log.info("The System does not allow to unmanage the devices which is in Active connection. "
//				+ "Test pass");
		
	    
		
		
		
	}
	

	/**
	 * Finds the correct TX device, clicks the breadcrumb button and then selects
	 * edit settings which brings up an edit transmitter modal
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void navigateToEditSettingsTx(WebDriver driver, String ipAddress) throws InterruptedException {
		log.info("Attempting to navigate to edit TX device settings");
		navigateToOptions(driver, ipAddress);
		timer(driver);
		Devices.editDeviceSettingsTx(driver).click();
//		timer(driver);
		new WebDriverWait(driver, 60).until(ExpectedConditions
				.presenceOfElementLocated(By.xpath(Devices.getEditTxSettingsSettingTypeDropdownXpath())));
		log.info("Successfully navigated to edit TX device settings");

	}

	/**
	 * Opens the edit settings modal for receiver device
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void navigateToEditSettingsRx(WebDriver driver, String ipAddress) throws InterruptedException {
		log.info("Attempting to navigate to edit RX device settings");
		navigateToOptions(driver, ipAddress);
		timer(driver);
		Devices.editDeviceSettingsRx(driver).click();
		timer(driver);
		new WebDriverWait(driver, 60).until(ExpectedConditions
				.presenceOfElementLocated(By.xpath(Devices.getEditRxSettingsSettingTypeDropdownXpath())));
		log.info("Successfully navigated to edit RX device settings");
	}

	/**
	 * Sets specific transmitter device to system properties
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void setTxToSystemProperties(WebDriver driver, String ipAddress) throws InterruptedException {
		navigateToEditSettingsTx(driver, ipAddress);
		Devices.editTxSettingsSettingDropdown(driver, "System");
		timer(driver);
		saveEditTxSettings(driver);
	}

	/**
	 * Sets specific transmitter device to template properties
	 * 
	 * @param driver
	 * @param ipAddress
	 *            IP address of the transmitter
	 * @param templateName
	 *            name of the template to set transmitter to
	 * @throws InterruptedException
	 */
	public void setTxToTemplateProperties(WebDriver driver, String ipAddress, String templateName)
			throws InterruptedException {
		navigateToEditSettingsTx(driver, ipAddress);
		Devices.editTxSettingsSettingDropdown(driver, "Template");
		timer(driver);
		Devices.EditTxTemplateNameDropdown(driver, templateName);
		timer(driver);
		saveEditTxSettings(driver);
	}

	/**
	 * Sets specific receiver device to system properties
	 * 
	 * @param driver
	 * @param ipAddress
	 *            IP address of the receiver
	 * @throws InterruptedException
	 */
	public void setRxToSystemProperty(WebDriver driver, String ipAddress) throws InterruptedException {
		navigateToEditSettingsRx(driver, ipAddress);
		Devices.editRxSettingsSettingDropdown(driver, "System");
		timer(driver);
		saveEditRxSettings(driver);
	}

	/**
	 * Sets specific receiver device to template properties
	 * 
	 * @param driver
	 * @param ipAddress
	 *            IP address of the receiver
	 * @param templateName
	 *            name of the template to set receiver
	 * @throws InterruptedException
	 */
	public void setRxToTemplateProperties(WebDriver driver, String ipAddress, String templateName)
			throws InterruptedException {
		navigateToEditSettingsRx(driver, ipAddress);
		Devices.editRxSettingsSettingDropdown(driver, "Template");
		timer(driver);
		Devices.editRxTemplateNameDropdown(driver, templateName);
		timer(driver);
		saveEditRxSettings(driver);
	}
	
	
	
	
	
    
    
	/**
	 * Unmanage a device with the given IP address
	 * 
	 * @param driver
	 * @param ipAddress
	 *            IP address of the device to unmanage
	 * @throws InterruptedException
	 */
	
	
	public void unManageDevice(WebDriver driver, String ipAddress) throws InterruptedException {
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.unManageTab);
		log.info("UnManage Device -  Clicked on Unmanage Tab");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		int counter = 0;
		log.info("Waiting for spinner to appear.");
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		log.info("The spinner has appeared, waiting for spinner to disappear.");
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(Switch.spinnerXpath)));
		Thread.sleep(3000);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		log.info("UnManage Device - Device name entered in search box");
		timer(driver);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertFalse(deviceApplianceTable.contains(ipAddress),
				"Device appliance table did not contain: " + ipAddress + ", actual text: " + ipAddress);
	}

	/**
	 * Change the devices IP to a new one through Boxilla UI
	 * 
	 * @param driver
	 * @param currentIP
	 *            Current IP address of the device
	 * @param newIP
	 *            IP address to change device to
	 * @throws InterruptedException
	 */
	public void editDevice(WebDriver driver, String currentIP, String newIP, boolean isChangeBack)
			throws InterruptedException {
		navigateToOptions(driver, currentIP);
		editIPAdd(driver, currentIP, newIP);
		log.info("Edit Device - IP address changed to " + newIP + ". Changing back to " + currentIP);
		int counter = 0;
		log.info("waiting for boxilla to pick up device online / offline");
		Thread.sleep(30000);
		driver.navigate().refresh();
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, newIP);
		while (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("OffLine") && counter < 5) {
			driver.navigate().refresh();
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, newIP);
			log.info((counter + 1) + ". Device state is offline.. Refreshing page");
			Thread.sleep(4000);
			counter++;

		}
		if (isChangeBack) {
			timer(driver);
			log.info("Edit Device - new IP address entered in search box");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Devices.breadCrumbBtn);
			log.info("Edit Device - Clicked on breadcrumb");
			editIPAdd(driver, newIP, currentIP); // New IP became currentIP and previous IP will be new IP
		}
	}
	
	public void editDevice1(WebDriver driver, String currentIP, String newIP)
			throws InterruptedException {
		navigateToOptions(driver, currentIP);
		editIPAdd(driver, currentIP, newIP);
		log.info("Edit Device - IP address changed to " + newIP + ". Changing back to " + currentIP);
		int counter = 0;
		log.info("waiting for boxilla to pick up device online / offline");
		driver.navigate().refresh();
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, newIP);
		while (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains("OffLine") && counter < 5) {
			driver.navigate().refresh();
			SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, newIP);
			log.info((counter + 1) + ". Device state is offline.. Refreshing page");
			Thread.sleep(4000);
			counter++;
		}
	}

	/**
	 * Soak specific method to change the device IP address
	 * 
	 * @param driver
	 * @param deviceName
	 * @param currentIP
	 * @param newIP
	 * @throws InterruptedException
	 */
	public void deviceIPchangeSoak(WebDriver driver, String deviceName, String currentIP, String newIP)
			throws InterruptedException {
		/*
		 * device IP change method to use in soak test.. Flow: Search device using
		 * device name , extract existing IP address, compares it with IP passed in
		 * parameters and change IP address accordingly
		 */
		navigateToOptions(driver, deviceName); // Searching using device name instead of IP address
		timer(driver);
		log.info(SeleniumActions.seleniumGetText(driver, Devices.applianceTable));
		if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains(currentIP)) {
			editIPAdd(driver, currentIP, newIP); // Changing IP address from Current IP to new IP
		} else if (SeleniumActions.seleniumGetText(driver, Devices.applianceTable).contains(newIP)) {
			editIPAdd(driver, newIP, currentIP);
		} else {
			log.info("IP address doesn't match with any of addresses passed with method.. Skipping test");
			throw new SkipException("***** IP address error.. Skipping test.. *****");
		}
	}

	/**
	 * This will change a device IP address through Boxilla
	 * 
	 * @param driver
	 * @param currentIP
	 *            - current IP address of device
	 * @param newIP
	 *            - IP address to change the device to
	 * @throws InterruptedException
	 */
	private void editIPAdd(WebDriver driver, String currentIP, String newIP) throws InterruptedException {
		log.info("Changing IP address form " + currentIP + " to " + newIP);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.edit);
		log.info("Edit Device - Edit tab clicked");
		timer(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Devices.editDeviceIPAddTextbox);
		log.info("Edit Device - " + currentIP + " cleared from textbox");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.editDeviceIPAddTextbox, newIP);
		log.info("Edit Device - " + newIP + " entered");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.applyBtn);
		log.info("Edit Device - Apply button clicked");
		timer(driver);
		new WebDriverWait(driver,120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		Thread.sleep(5000);
		log.info("Appliance IP Change Sucessfully");
		navigateToDeviceStatus(driver);
		driver.navigate().refresh();
		Thread.sleep(5000);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, newIP);
		String deviceApplicaneTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		log.info("Edit Device - New IP address entered in to search box");
		Assert.assertTrue(deviceApplicaneTable.contains(newIP),
				"Device applicane table did not contain: " + newIP + ", actual text: " + deviceApplicaneTable);
		log.info("Edit Device - Device Edit asserted");
		timer(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Devices.deviceStatusSearchBox);
	}

	/**
	 * Edit the device name through Boxilla
	 * 
	 * @param driver
	 * @param ipAddress
	 *            IP address of the device
	 * @param newName
	 *            New name to change the device to
	 * @throws InterruptedException
	 */
	public void changeDeviceName(WebDriver driver, String ipAddress, String newName) throws InterruptedException {
		navigateToOptions(driver, ipAddress);
		log.info("Changing name to:" + newName);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.chanageDeviceName);
		log.info("Change Device Name - Change Device Name button clicked");
		timer(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Devices.hostnameTextbox);
		timer(driver);
		// driver.findElement(By.xpath("//input[@id='edit-appliance-name-text-input']")).clear();
		SeleniumActions.seleniumSendKeys(driver, Devices.hostnameTextbox, newName);
		log.info("Change Device Name - New name entered in Hostname textbox");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.hostnameApplyBtn);
		// Thread.sleep(3000);
		log.info("Change Device Name - Apply Button Clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(5000);
		
		SeleniumActions.seleniumSendKeysClear(driver, Devices.deviceStatusSearchBox);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		log.info("Change Device Name - IP address entered into search box");
		timer(driver);
		String deviceApplicaneTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertTrue(deviceApplicaneTable.contains(newName),
				"Device appliance table did not contain: " + newName + ", actual text: " + deviceApplicaneTable);
		log.info("Change Device Name - Assertion Completed");
	}

	/**
	 * Check through boxilla, how long a device has been running for
	 * 
	 * @param driver
	 * @return uptime - length of time device has been online for
	 */
	public float uptime(WebDriver driver) {
		String value = SeleniumActions.seleniumGetText(driver, Devices.deviceUptime);
		System.out.println(value);
		// Extract everyting after '</strong> ' text
		String extracted = value.substring(value.lastIndexOf(' ') + 10);
		System.out.println(extracted);
		float uptime = Float.valueOf(extracted);
		log.info("Device Uptime:" + uptime);
		return uptime;
	}

	/**
	 * Reboot a device through Boxilla
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void rebootDevice(WebDriver driver, String ipAddress) throws InterruptedException {
		timer(driver);
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.rebootDevice);
		log.info("Reboot Device - Reboot tab clicked.. Waiting for 2 minutes before asserting uptime");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		Thread.sleep(60000);
	}

	/**
	 * Restore a device through Boxilla.
	 * 
	 * @param driver
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	@Deprecated
	public void restoreDevice(WebDriver driver, String ipAddress) throws InterruptedException {
		navigateToOptions(driver, ipAddress);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.restoreDevice);
		log.info("Restore Device - Restore tab clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		int counter = 0;
		while ((Landingpage.spinner(driver).isDisplayed()) && (counter < 10)) {
			log.info((counter + 1) + ". Restoring device...");
			Thread.sleep(2000);
			counter++;
		}
		log.info("Restore Device - Restore Completed.. Asserting.. ");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Devices.deviceStatusSearchBox, ipAddress);
		log.info("Restore Device - Device IP entered in to search box");
		timer(driver);
		String deviceApplianceTable = SeleniumActions.seleniumGetText(driver, Devices.applianceTable);
		Assert.assertFalse(deviceApplianceTable.contains(ipAddress),
				"Device appliance table contained: " + ipAddress + ", actual text: " + deviceApplianceTable);
		log.info("Restore Device - Device not found.. Restore complete");
	}

	/**
	 * Change connection inactivity time on a device through Boxilla
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public String changeOSDInactivityTimer(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		log.info("Devices Settings - Clicked on Devices tab");
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		log.info("Devices Settings - Clicked on Devices > Settings tab");
		Thread.sleep(2000);
		String button2Text = SeleniumActions.seleniumGetText(driver, Devices.osdInactivityTimerBtn);
		int min = 2;
		int max = 60;
		Random number = new Random();
		int generatedNumber = number.nextInt((max - min) + 1) + min;
		String numberString = Integer.toString(generatedNumber);
		log.info("Setting value to:" + generatedNumber);

		if (button2Text.contains("Disable")) {
			log.info("OSD InActivity Timer is Enabled.. changing timeout value");
			// Scoll to the element as it was failing in Chrome without scrolling
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					SeleniumActions.getElement(driver, Devices.osdInactivityTimerBtn));
			SeleniumActions.seleniumDropdown(driver, Devices.OSDInactivityRange, numberString);

		} else {
			log.info("OSD InActivity Timer is Disabled.. Enabling..");
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					SeleniumActions.getElement(driver, Devices.osdInactivityTimerBtn));
			SeleniumActions.seleniumClick(driver, Devices.osdInactivityTimerBtn);
			SeleniumActions.seleniumDropdown(driver, Devices.OSDInactivityRange, numberString);
			log.info("Devices Settings - OSD Inactivity Timer Enabled");
		}

		Thread.sleep(2000);
		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");
		return numberString;
	}

	/**
	 * Disables the functional hotkey through Boxilla
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void disableFunctionalHotkey(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.deviceSettingsApply)));
		String selectOption = SeleniumActions.seleniumDropdownGetText(driver, Devices.getFunctionalHotkeyDropdown());

		if (selectOption.equals("Disable")) {
			log.info("Functional Hotkey already disabled. Doing nothing");
		} else {
			log.info("Functional hotkey is not disabled. Disabling");
			SeleniumActions.seleniumDropdown(driver, Devices.getFunctionalHotkeyDropdown(), "Disable");
			SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Device Settings - Apply Button clicked and Alert accepted");

		}
	}

	/**
	 * Sets the appliance hotkey in Boxilla UI. Hot key is an enum called HOTKEY and
	 * the values are PRINTSCRN, ALT_ALT, CTRL_CTRL, SHIFT_SHIFT, MOUSE
	 * 
	 * @param driver
	 * @param key
	 * @throws InterruptedException
	 */
	public void setHotkey(WebDriver driver, HOTKEY key) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.deviceSettingsApply)));
		switch (key) {
		case PRINTSCRN:
			SeleniumActions.seleniumDropdown(driver, Devices.getHotkeyDropdown(), "PrintScrn");
			break;
		case ALT_ALT:
			SeleniumActions.seleniumDropdown(driver, Devices.getHotkeyDropdown(), "Alt-Alt");
			break;
		case CTRL_CTRL:
			SeleniumActions.seleniumDropdown(driver, Devices.getHotkeyDropdown(), "Ctrl-Crtl");
			break;
		case SHIFT_SHIFT:
			SeleniumActions.seleniumDropdown(driver, Devices.getHotkeyDropdown(), "Shift-Shift");
			break;
		case MOUSE:
			SeleniumActions.seleniumDropdown(driver, Devices.getHotkeyDropdown(), "Mouse-Left+Right");
			break;
		}
		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");
	}

	/**
	 * Sets the appliance RDP connection resolution through Boxilla UI. Uses an enum
	 * called RESOLUTION to select the resolution. Values are R1920X1080, R1024X768,
	 * R1920X1200, R1280X1024, R640X480, R800X600
	 * 
	 * @param driver
	 * @param res
	 * @throws InterruptedException
	 */
	public void setRDP_ConnectionResolution(WebDriver driver, RESOLUTION res) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.deviceSettingsApply)));
		switch (res) {
		case R1920X1080:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "1920x1080");
			break;
		case R1024X768:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "1024x768");
			break;
		case R1920X1200:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "1920x1200");
			break;
		case R1280X1024:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "1280x1024");
			break;
		case R640X480:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "640x480");
			break;
		case R800X600:
			SeleniumActions.seleniumDropdown(driver, Devices.getRDP_ConnectionResolution(), "800x600");
			break;
		}

		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");

	}

	/**
	 * Will enable the appliance functional hotkey through Boxilla UI
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void enableFunctionalHotkey(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.deviceSettingsApply)));
		String selectOption = SeleniumActions.seleniumDropdownGetText(driver, Devices.getFunctionalHotkeyDropdown());

		if (selectOption.equals("Enable")) {
			log.info("Functional Hotkey already enabled. Doing nothing");
		} else {
			log.info("Functional hotkey is not enabled. Enabling");
			SeleniumActions.seleniumDropdown(driver, Devices.getFunctionalHotkeyDropdown(), "Enable");
			SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
			;
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Device Settings - Apply Button clicked and Alert accepted");

		}
	}

	/**
	 * Disable the appliance OSD timer through Boxilla UI
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void disableOSDTimer(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		log.info("Devices Settings - Clicked on Devices tab");
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		log.info("Devices Settings - Clicked on Devices > Settings tab");
		Thread.sleep(2000);
		// get text from button to determine if timer is enabled or not
		String buttonText = SeleniumActions.seleniumGetText(driver, Devices.osdInactivityTimerBtn);
		if (buttonText.contains("Disable")) {
			log.info("Timer enabled. Disabling");
			SeleniumActions.seleniumClick(driver, Devices.osdInactivityTimerBtn);
		} else {
			log.info("Timer already disabled. Doing nothing");
		}
		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");

	}

	/**
	 * Disable the appliance inactivity time through Boxilla UI
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void disableInactivityTimer(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		log.info("Devices Settings - Clicked on Devices tab");
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		log.info("Devices Settings - Clicked on Devices > Settings tab");
		Thread.sleep(2000);
		// get text from button to determine if timer is enabled or not
		String buttonText = SeleniumActions.seleniumGetText(driver, Devices.connectionInactivityTimerBtn);
		if (buttonText.contains("Disable")) {
			log.info("Timer enabled. Disabling");
			SeleniumActions.seleniumClick(driver, Devices.connectionInactivityTimerBtn);
		} else {
			log.info("Timer already disabled. Doing nothing");
		}
		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");

	}

	/**
	 * Change appliance inactivity timer through Boxilla UI to a random number
	 * between 1 - 60 and return this number
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String changeInactivityTimer(WebDriver driver) throws InterruptedException {
		Landingpage.devicesTab(driver).click();
		log.info("Devices Settings - Clicked on Devices tab");
		Thread.sleep(2000);
		Landingpage.devicesSettings(driver).click();
		log.info("Devices Settings - Clicked on Devices > Settings tab");
		Thread.sleep(2000);
		// get text from button to determine if timer is enabled or not
		String buttonText = SeleniumActions.seleniumGetText(driver, Devices.connectionInactivityTimerBtn);
		// generate random number to change the timer to
		int min = 2;
		int max = 60;
		Random number = new Random();
		int generatedNumber = number.nextInt((max - min) + 1) + min;
		String numberString = Integer.toString(generatedNumber);
		log.info("Setting value to:" + generatedNumber);
		if (buttonText.contains("Disable")) {
			log.info("Connection Inactivity Timer is Enabled.. changing timer value");
			SeleniumActions.seleniumDropdown(driver, Devices.connectionInactivityRange, numberString);
		} else {
			log.info("Connection Inactivity Timer is Disabled.. Enabling..");
			SeleniumActions.seleniumClick(driver, Devices.connectionInactivityTimerBtn);
			SeleniumActions.seleniumDropdown(driver, Devices.connectionInactivityRange, numberString);
		}
		SeleniumActions.seleniumClick(driver, Devices.deviceSettingsApply);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Device Settings - Apply Button clicked and Alert accepted");
		return numberString;
	}

	/**
	 * This method will upgrade all devices managed by Boxilla and assert in the
	 * boxilla UI that the devices upgraded successfully
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @param deviceType
	 * @param deviceIps
	 * @throws InterruptedException
	 */
	public void newUpgrade(WebDriver driver, String upgradeVersion, String deviceType, String[] deviceIps)
			throws InterruptedException {
		log.info("All devices must be on the same version and of the same type");
		log.info("Starting upgrade");
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.getSelectAllDevicesUpgrade());
		String upgradeStyleBefore = getUpgradeButtonStyle(driver);
		SeleniumActions.seleniumClick(driver, Devices.upgradeBtn);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		int counter = 0;
		String upgradeStyleAfter = "";
		boolean isDisabled = true;
		while (isDisabled && counter < 80) {
			Thread.sleep(10000);
			upgradeStyleAfter = getUpgradeButtonStyle(driver);
			log.info("Upgrade enabled:" + upgradeStyleBefore);
			log.info("Upgrade disabled:" + upgradeStyleAfter);
			if (!upgradeStyleAfter.contains("disabled")) {
				log.info("MATCH");
				isDisabled = false;
			}
			counter++;
		}

		if (counter > 40) {
			log.info("Upgrade took longer than 12 minutes. Failing"); // if upgrading lots of devices
			throw new AssertionError("Upgrade took longer than 12 minutes. Failing"); // this will need to be increased
		}

		String version1 = getDeviceVersionFromUi(driver, deviceIps[0]);
		String version2 = getDeviceVersionFromUi(driver, deviceIps[1]);
		String version3 = getDeviceVersionFromUi(driver, deviceIps[2]);
		String version4 = getDeviceVersionFromUi(driver, deviceIps[3]);

		Assert.assertTrue(version1.equals(upgradeVersion));
		Assert.assertTrue(version2.equals(upgradeVersion));
		Assert.assertTrue(version3.equals(upgradeVersion));
		Assert.assertTrue(version4.equals(upgradeVersion));

	}

	/**
	 * Returns the class attribute for the Upgrade device button. This will let you
	 * know if the button is greyed out and not clickable
	 * 
	 * @param driver
	 * @return
	 */
	public String getUpgradeButtonStyle(WebDriver driver) {
		String att = SeleniumActions.getAttribute(driver, Devices.upgradeBtn, "class");
		log.info("class:" + att);
		return att;
	}

	/**
	 * Returns the current version of software an appliance is on through Boxilla UI
	 * 
	 * @param driver
	 * @param deviceIp
	 *            - IP address of the device to check version
	 * @return the current version of software on this appliance
	 * @throws InterruptedException
	 */
	public String getDeviceVersionFromUi(WebDriver driver, String deviceIp) throws InterruptedException {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Devices.selectDevicesSearchBox);
		SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, deviceIp);
		String version = SeleniumActions.seleniumGetText(driver, Devices.getDeviceVersion());
		log.info("Device version from table:" + version);
		return version;
	}

	/**
	 * This will activate a specific release for a PE receiver in Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 *            - version to activate
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void activePEReceiverRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating PE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getPeReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getPeUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getPeUpgradeSearchBoxReceiver(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getPeReceiverActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating PE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getPeReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getPeUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getPeUpgradeSearchBoxReceiver(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getPeReceiverActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * This will activate a specific release for 4K receiver through Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void active4kReceiverRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating 4k Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.get4kReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getFourkUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getFourkUpgradeSearchBoxReceiver(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getFourkReceiverActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating 4k Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.get4kReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getFourkUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getFourkUpgradeSearchBoxReceiver(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getFourkReceiverActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * This will activate a specific for SE receiver through Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void activeSEReceiverRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating SE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getSeReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSeUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getSeUpgradeSearchBoxReceiver(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getSeReceiverActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating SE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getSeReceiverTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSeUpgradeSearchBoxReceiver())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getSeUpgradeSearchBoxReceiver(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getSeReceiverActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * This will activate a specific release for a PE transmitter through Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void activePETransmitterRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating PE Transmitter software");
		SeleniumActions.seleniumClick(driver, Devices.getPeTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getPeUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getPeUpgradeSearchBoxTransmitter(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getPeTransmitterActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating PE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getPeTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getPeUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getPeUpgradeSearchBoxTransmitter(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getPeTransmitterActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * This will activate a specific 4k transmitter release through Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void active4kTransmitterRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating 4k Transmitter software");
		SeleniumActions.seleniumClick(driver, Devices.getFourkTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getFourkUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getFourkUpgradeSearchBoxTransmitter(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getFourkTransmitterActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating 4k Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getFourkTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getFourkUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getFourkUpgradeSearchBoxTransmitter(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getFourkTransmitterActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * This will activate a specific SE transmitter release through Boxilla UI
	 * 
	 * @param driver
	 * @param upgradeVersion
	 * @throws InterruptedException
	 * @throws AssertionError
	 */
	public void activeSETransmitterRelease(WebDriver driver, String upgradeVersion)
			throws InterruptedException, AssertionError {
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating SE Transmitter software");
		SeleniumActions.seleniumClick(driver, Devices.getSeTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSeUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getSeUpgradeSearchBoxTransmitter(), upgradeVersion);
		SeleniumActions.seleniumClick(driver, Devices.getSeTransmitterActiveDropdown());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getActivateReleaseLinkPeReceiver())));
		SeleniumActions.seleniumClick(driver, Devices.getActivateReleaseLinkPeReceiver());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 120).until(ExpectedConditions.visibilityOf(Landingpage.spinner(driver)));
		new WebDriverWait(driver, 120).until(ExpectedConditions.invisibilityOf(Landingpage.spinner(driver)));
		log.info("Version activated. checking ");
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		log.info("Activating SE Receiver software");
		SeleniumActions.seleniumClick(driver, Devices.getSeTransmitterTab());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSeUpgradeSearchBoxTransmitter())));
		SeleniumActions.seleniumSendKeys(driver, Devices.getSeUpgradeSearchBoxTransmitter(), upgradeVersion);
		String isActivated = SeleniumActions.seleniumGetText(driver, Devices.getSeTransmitterActiveReleaseTable());
		log.info("Is Activeated:" + isActivated);
		if (isActivated.contains("Yes")) {
			log.info("Release is activated");
		} else {
			throw new AssertionError("The release was not marked as activated by boxilla");
		}
	}

	/**
	 * Used to test timings. Assumptions: upgrade version already exists on boxilla.
	 * Emerald devices
	 * 
	 * @param upgradeVersion
	 * @throws InterruptedException
	 */
	public void upgradeAll(WebDriver driver, String upgradeVersion) throws InterruptedException {
		log.info("Upgrading all devices");
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver, Devices.releasesTab);
		timer(driver);
		log.info("TX ACTIVATED");
		activateVersion(driver, "rx", upgradeVersion);
		log.info("RX ACTIVATED");
		log.info("Starting upgrade");
		timer(driver);
		timer(driver);
		timer(driver);
		navigateToUpgrade(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Devices.upgradeBtn);
		Alert alert3 = driver.switchTo().alert();
		alert3.accept();
	}

	/**
	 * Returns all text from the device table.
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getAllDeviceTable(WebDriver driver) throws InterruptedException {
		timer(driver);
		driver.navigate().refresh();
		Thread.sleep(7000);
		SeleniumActions.seleniumClick(driver, Devices.selectDevices);
		log.info("Upgrading device : Select Devices tab clicked.");
		timer(driver);

		// check entire table for versions
		String upgradeTable = SeleniumActions.seleniumGetText(driver, Devices.getUpgradeTableEntire());
		log.info("upgrade table:" + upgradeTable);
		return upgradeTable;

	}
	
	
	public void setTimeOut(WebDriver driver,String time) throws InterruptedException 
	{
		navigateToUpgrade(driver);
		SeleniumActions.seleniumClick(driver,Devices.selectSetTimeOut);
		log.info("Set Time out Button clicked successfully");
		Thread.sleep(3000);
		SeleniumActions.seleniumSendKeysClear(driver, Devices.selectTimeouttext);
		SeleniumActions.seleniumSendKeys(driver, Devices.selectTimeouttext, time);
		SeleniumActions.seleniumClick(driver, Devices.TimeoutSavebtn);
		Thread.sleep(3000);
		 String toastmessage=SeleniumActions.seleniumGetText(driver, Devices.timeoutsavebtn);
	     Assert.assertTrue(toastmessage.contains("success"),"Toast message is not success Actual is:" + toastmessage);
	     log.info("Device Upgrade Timeout updated Sucessfully");
	    
		
	}
	 public void timeoutRange(WebDriver driver,String minimumtime,String maximumtime) throws InterruptedException 
	 {
		 navigateToUpgrade(driver);
			SeleniumActions.seleniumClick(driver,Devices.selectSetTimeOut);
			log.info("Set Time out Button clicked successfully");
			Thread.sleep(3000); 
			SeleniumActions.seleniumSendKeysClear(driver, Devices.selectTimeouttext);
			SeleniumActions.seleniumSendKeys(driver, Devices.selectTimeouttext, minimumtime);
			SeleniumActions.seleniumClick(driver, Devices.TimeoutSavebtn);
			Thread.sleep(3000);
			String toastmessage=SeleniumActions.seleniumGetText(driver, Devices.timeoutsavebtn);
			Assert.assertFalse(toastmessage.contains("success"),"System allowed to save upgrdae timeout with mimimum value actualy value is "+ toastmessage);
			SeleniumActions.seleniumSendKeysClear(driver, Devices.selectTimeouttext);
			SeleniumActions.seleniumSendKeys(driver, Devices.selectTimeouttext, maximumtime);
			String toastmessagemax=SeleniumActions.seleniumGetText(driver, Devices.timeoutsavebtn);
			Assert.assertFalse(toastmessagemax.contains("success"),"System allowed to save upgrdae timeout with mimimum value actualy value is "+ toastmessage);
	 }

	/**
	 * Upgrade a device
	 * 
	 * @param driver
	 * @param appliance
	 * @param applianceIP
	 * 
	 * @param newVersion
	 * @param oldVersion
	 * @throws InterruptedException
	 */
	public void upgradeDevice(WebDriver driver, String appliance, String applianceIP, String newVersion,
			String oldVersion) throws InterruptedException {

		timer(driver);
		driver.navigate().refresh();
		Thread.sleep(7000);
		SeleniumActions.seleniumClick(driver, Devices.selectDevices);
		log.info("Upgrading device : Select Devices tab clicked.");
		timer(driver);
		log.info("Upgrading device : Checking Firmware version of " + appliance + " device");
		SeleniumActions.seleniumSendKeysClear(driver, Devices.selectDevicesSearchBox);
		SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
		log.info("Upgrading device : Device searched using IP address");
		if (SeleniumActions.seleniumGetText(driver, Devices.state).contains("No Upgrade Required")
				|| SeleniumActions.seleniumGetText(driver, Devices.state).contains("Idle")) {
			timer(driver);
			String firmware = Devices.firmwareVersion(driver, appliance).getText();
			timer(driver);
			log.info("Firmware from boxilla:" + firmware);
			log.info("Firmware new:" + newVersion);
			if (firmware.equalsIgnoreCase(newVersion)) {
				log.info("Upgraing device : current version is " + newVersion + " Trying to activate " + oldVersion);
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.releasesTab);
				log.info("Upgrading device : Releases tab clicked");
				timer(driver);
				Devices.deviceReleaseTab(driver, appliance).click();
				log.info("Upgrading device : Device releases tab clciked ");

				if (StartupTestCase.isEmerald) {
					log.debug("****** USING EMERALD ************");
					activateVersionEmerald(driver, appliance, oldVersion);
				} else if (StartupTestCase.isEmeraldSe) {
					log.debug("****** USING EMERALD SE ************");
					activateVersionEmeraldSe(driver, appliance, oldVersion); // Activating v4.1
				} else if (StartupTestCase.isZeroU) {
					log.info("***** USING ZERO U *******************");
					activateVersionZeroU(driver, appliance, oldVersion);
				} else {
					log.debug("****** NOT USING EMERALD ************");
					activateVersion(driver, appliance, oldVersion); // Activating v4.1
				}
				log.debug("Upgrading device : Version " + oldVersion + " Activated.. Re checking Device State");
				// Re checking appliance version and state
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.selectDevices);
				log.info("Upgrading device : Select Devices tab clicked.");
				timer(driver);
				driver.navigate().refresh(); // refresh the page or else the xpath for the search box will have changed
				SeleniumActions.seleniumSendKeysClear(driver, Devices.selectDevicesSearchBox);
				SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
				log.info("Upgrading device : Device searched using IP address");
				timer(driver);
				String deviceUpgradeTable = SeleniumActions.seleniumGetText(driver, Devices.upgradeTable);
				log.info("Text from upgrade table:" + deviceUpgradeTable);
				Assert.assertTrue(deviceUpgradeTable.contains("Mis-match to Active Version"),
						"Device upgrade table did not contain: Mis-match to Active Version, actual text: "
								+ deviceUpgradeTable);
				log.info("Upgrading device : Device state is Mis-match.. Starting upgrade");
				timer(driver);
				completeUpgrade(driver, applianceIP);
			} else if (firmware.equalsIgnoreCase(oldVersion)) {

				log.info("Upgraing device : current version is " + oldVersion + " Trying to activate " + newVersion);
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.releasesTab);
				log.info("Upgrading device : Releases tab clicked");
				timer(driver);
				Devices.deviceReleaseTab(driver, appliance).click();
				log.info("Upgrading device : Device releases tab clciked ");

				if (StartupTestCase.isEmerald) {

					log.debug("****** USING EMERALD ************");
					activateVersionEmerald(driver, appliance, newVersion);
				} else if (StartupTestCase.isEmeraldSe) {
					activateVersionEmeraldSe(driver, appliance, newVersion);
				} else if (StartupTestCase.isZeroU) {
					activateVersionZeroU(driver, appliance, newVersion);
				} else {
					log.debug("****** NOT USING EMERALD ************");
					activateVersion(driver, appliance, newVersion); // Activating v4.1
				}
				log.debug("Upgrading device : Version " + newVersion + " Activated.. Re checking Device State");
				// Re checking appliance version and state
				timer(driver);
				SeleniumActions.seleniumClick(driver, Devices.selectDevices);
				log.info("Upgrading device : Select Devices tab clicked.");
				timer(driver);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.selectDevicesSearchBox);
				SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
				log.info("Upgrading device : Device searched using IP address");
				timer(driver);
				String deviceUpgradeTable = SeleniumActions.seleniumGetText(driver, Devices.upgradeTable);
				Assert.assertTrue(deviceUpgradeTable.contains("Mis-match to Active Version"),
						"Device upgrade table did not contain: Mis-match to Active Version, actual text: "
								+ deviceUpgradeTable);
				log.info("Upgrading device : Device state is Mis-match.. Starting upgrade");
				timer(driver);
				completeUpgrade(driver, applianceIP);
			} else {
				log.info("Version is neither " + newVersion + " nor " + oldVersion + ".. Activating " + newVersion);
				if (StartupTestCase.isEmerald) {

					log.debug("****** USING EMERALD ************");
					activateVersionEmerald(driver, appliance, newVersion);
				} else if (StartupTestCase.isEmeraldSe) {
					activateVersionEmeraldSe(driver, appliance, newVersion);
				} else if (StartupTestCase.isZeroU) {
					activateVersionZeroU(driver, appliance, newVersion);
				} else {
					log.debug("****** NOT USING EMERALD ************");
					activateVersion(driver, appliance, newVersion); // Activating v4.1
				}
				timer(driver);
				driver.navigate().refresh();
				Thread.sleep(7000);
				SeleniumActions.seleniumSendKeysClear(driver, Devices.selectDevicesSearchBox);
				SeleniumActions.seleniumSendKeys(driver, Devices.selectDevicesSearchBox, applianceIP);
				log.info("Upgrading device : Device searched using IP address");
				log.info("Upgrading device : Device searched using IP address");
				String deviceUpgradeTable2 = SeleniumActions.seleniumGetText(driver, Devices.upgradeTable);
				Assert.assertTrue(deviceUpgradeTable2.contains("Mis-match to Active Version"),
						"Device upgrade table did not contain: Mis-match to Active Version, actual text: "
								+ deviceUpgradeTable2);
				completeUpgrade(driver, applianceIP);
			}
		} else if (SeleniumActions.seleniumGetText(driver, Devices.upgradeTable)
				.contains("Mis-match to Active Version")) {
			completeUpgrade(driver, applianceIP);
		}
	}

	/**
	 * This will return the output of running the e2Read command on an appliance by
	 * logging into the appliance through SSH and running the command
	 * 
	 * @param deviceIp
	 *            -Ip address of the device to log into
	 * @param userName
	 *            - user name for logging into an appliance through SSH
	 * @param password
	 *            - password for logging into an appliance through SSU
	 * @return String - the output of e2Read command
	 */
	public String getE2Read(String deviceIp, String userName, String password) {
		Ssh ssh = new Ssh(userName, password, deviceIp);
		ssh.loginToServer();
		String e2 = ssh.sendCommand("e2_read");
		log.info("E2_Read:" + e2);
		if (!e2.contains("Information")) {
			e2 = ssh.sendCommand("/opt/cloudium/hw_scripts/factory/e2_read.elf");
			log.info("E2_Read:" + e2);
		}
		ssh.disconnect();
		return e2;
	}
	
	
	// it will reboot the dualrx device
	public class Reboot {
		String [] device_names;
	}
	public void rebootdualrx()
	{
		String[] devices = {rxDual.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);
	}
	

	/**
	 * Returns the device software version by logging into the device through SSH
	 * and getting the current software version
	 * 
	 * @param deviceIp
	 * @param userName
	 * @param password
	 * @return
	 */
	public String getDeviceSwVersion(String deviceIp, String userName, String password) {
		Ssh ssh = new Ssh(userName, password, deviceIp);
		ssh.loginToServer();
		String version = ssh.sendCommand("cat /VERSION");
		version = version.replace("\n", ""); // replace line breals
		ssh.disconnect();
		return version;
	}

	/**
	 * Returns the appliance serial number by logging into the device using SSH
	 * 
	 * @param ipAddress
	 * @param username
	 * @param password
	 * @return
	 */
	public String getSerialNumber(String ipAddress, String username, String password) {
		String out = getE2Read(ipAddress, username, password);
		String[] split = out.split("SEBB");
		String[] split2 = split[1].split("\\s+");
		log.info("Serial Number:" + "SEBB" + split2[0]);
		return "SEBB" + split2[0];
	}

	/**
	 * Returns the device MPN number by logging into the device through SSH
	 * 
	 * @param ip
	 * @param username
	 * @param password
	 * @return
	 */
	public String getMpn(String ip, String username, String password) {
		String out = getE2Read(ip, username, password);
		String[] split = out.split("300-");
		String[] split2 = split[1].split("\\s+");
		log.info("MPN:300-" + split2[0]);
		return "300-" + split2[0];
	}

	/**
	 * Used to scan the appliance xml file to check if a user has a connection
	 * assigned
	 * 
	 * @param xml
	 *            a string representing an xml file
	 * @param username
	 *            - name of user to check
	 * @param conName
	 *            - name of connection to look for in user
	 * @return boolean - true if user has connection else false
	 */
	public boolean checkAdUserConnection(String xml, String username, String conName) {
		Scanner scan = new Scanner(xml);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.contains(username)) {
				while (scan.hasNextLine()) {
					line = scan.nextLine();
					if (line.contains(conName)) {
						return true;
					}
				}
			}
		}
		return false;
	}

	/**
	 * Will return the device xml file as a string by logging into a device using
	 * SSH
	 * 
	 * @param deviceIp
	 * @return
	 * @throws InterruptedException
	 */
	public String getCloudGuiXml(String deviceIp) throws InterruptedException {
		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, deviceIp);
		int retry = 0;
		while (retry < 5) {
			try {
				shell.loginToServer();
				retry = 6;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("SSH login failed. Retrying");
				Thread.sleep(5000);
				retry++;
			}
		}
		String xml = shell.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML:" + xml);
		return xml;
	}

	/**
	 * This will check the appliance xml file to check if a user has a connection
	 * assigned to a favourite slot. Will return true if the user has the connection
	 * favourite else false
	 * 
	 * @param deviceIp
	 *            - IP address of the device
	 * @param username
	 *            - name of the user to check for connection favourite
	 * @param connectionName
	 *            - name of the connection to check for
	 * @param favNumber
	 *            - the slot number the connection was added to
	 * @return
	 * @throws InterruptedException
	 */
	public boolean checkConnectionFavouritesXml(String deviceIp, String username, String connectionName,
			String favNumber) throws InterruptedException {
		recreateCloudData(deviceIp);
		rebootDeviceSSH(deviceIp, StartupTestCase.deviceUserName, StartupTestCase.devicePassword, 0);
		// get xml
		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, deviceIp);
		int retry = 0;
		while (retry < 5) {
			try {
				shell.loginToServer();
				retry = 6;
			} catch (Exception e) {
				e.printStackTrace();
				log.info("SSH login failed. Retrying");
				Thread.sleep(5000);
				retry++;
			}
		}
		String xml = shell.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");

		Scanner scan = new Scanner(xml);
		while (scan.hasNextLine()) {
			String line = scan.nextLine();
			if (line.contains(username)) {
				while (scan.hasNextLine()) {
					line = scan.nextLine();
					while (line.contains("Associated_connections")) {
						if (line.contains(connectionName) && line.contains("Favorite='" + favNumber + "'")) {
							log.debug("MATCH!");
							return true;
						} else {
							line = scan.nextLine();
						}
					}
				}
			}
		}
		return false;
	}

}
