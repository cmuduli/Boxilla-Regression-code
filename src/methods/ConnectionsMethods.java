package methods;

import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.junit.rules.ExpectedException;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.SoftAssert;

import extra.SeleniumActions;
import extra.Ssh;
import extra.StartupTestCase;
import invisaPC.Rest.VideoFacts;
import objects.ActiveConnectionElements;
import objects.Connections;
import objects.Connections.VIA;
import objects.Discovery;
import objects.Landingpage;
import objects.Switch;
import objects.SystemAll;
import objects.Users;
import testNG.Utilities;

/**
 * Class contains apis to interact with connection related tasks in boxilla and
 * invisaPC
 * 
 * @author Boxilla
 *
 */
public class ConnectionsMethods {
	SoftAssert Assert = new SoftAssert();
	UsersMethods usermethods = new UsersMethods();
	final static Logger log = Logger.getLogger(ConnectionsMethods.class);

	/**
	 * This will log into Boxilla and create a bonded connection using the details
	 * provided
	 * 
	 * @param driver
	 * @param bondedConnectionName
	 *            - Name to give the bonded connection
	 * @param zone
	 *            - Zone to add the bonded connection to. Send "" if no zone
	 * @param connections
	 *            - An array of connections that will
	 * @param fromBondedConnection
	 *            - If you want to create the bonded connection from an existing one
	 *            pass the name here
	 * @throws InterruptedException
	 */
	public void createBondedConnection(WebDriver driver, String bondedConnectionName, String zone, String[] connections,
			String fromBondedConnection) throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnection);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(Connections.btnNext)));
		WebElement e = driver.findElement(By.xpath(Connections.getViaBonded()));
		SeleniumActions.exectuteJavaScriptClick(driver, e);
		SeleniumActions.seleniumSendKeys(driver, Connections.getCreateBondedConnectionNameTextBox(),
				bondedConnectionName);
		if (!zone.equals("")) {
			SeleniumActions.seleniumDropdown(driver, Connections.getCreateBondedConSelectZone(), zone);
		}
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		if (!fromBondedConnection.equals("")) {
			SeleniumActions.seleniumDropdown(driver, Connections.getCreateBondedConnectionExistingBondDropdown(),
					fromBondedConnection);
		} else {
			// add all connections
			for (int j =0 ; j < connections.length; j++) {
				SeleniumActions.seleniumClick(driver, Connections.getAddConnectionToBondButton());
				SeleniumActions.seleniumSendKeys(driver, Connections.getAddConnectionToBondTextBox(j), connections[j]);
			}
		}

		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		reviewBondedConSummary(driver, bondedConnectionName, connections);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.addSwitchSuccessToastXpath)));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Successfully created bonded connection."),
				"Error creating bonded connection group. Expected Succes, Actual:" + toast);

	}

	/**
	 * Checks that a bonded connection is running in the bonded connection viewer
	 * 
	 * @param driver
	 * @param bondedConnection
	 *            - list of connections in the bonded connection
	 * @param bondedReceivers
	 *            - List of the receivers in the bonded receiver group
	 * @throws InterruptedException
	 */
	public void checkExtendedBondedConnections(WebDriver driver, String[] bondedConnection, String[] bondedReceivers)
			throws InterruptedException {

		navigateToBondedConnectionViewer(driver);
		SeleniumActions.seleniumClick(driver, Connections.getExpandBondedConnectionsViewer());
		for (int j = 0; j < bondedConnection.length; j++) {
			Assert.assertTrue(
					SeleniumActions.seleniumIsDisplayed(driver,
							Connections.getExtendedBondedConnection(bondedConnection[j], bondedReceivers[j])),
					"Bonded connection running connection " + bondedConnection[j] + " and receiver "
							+ bondedReceivers[j] + " is not running");
		}
	}

	/**
	 * Kills the bonded connection from the bonded connection view
	 * 
	 * @param driver
	 * @param bondedCon
	 *            - The name of the bonded connection to kill
	 * @throws InterruptedException
	 */
	public void killBondedConnectionFromViewer(WebDriver driver, String bondedCon) throws InterruptedException {
		navigateToBondedConnectionViewer(driver);
//		SeleniumActions.seleniumClick(driver, Connections.getViewerToggleBondedBtnOff());
		SeleniumActions.seleniumClick(driver, Connections.getTerminateBondedConnection(bondedCon));
		Thread.sleep(61000);
	}

	/**
	 * Checks that a bonded connection is NOT running in the bonded connection
	 * viewer
	 * 
	 * @param driver
	 * @param bondedCon
	 *            - name of bonded connection to check
	 * @throws InterruptedException
	 */
	public void checkActiveBondedConnectionNotRunning(WebDriver driver, String bondedCon) throws InterruptedException {
		navigateToBondedConnectionViewer(driver);
		Assert.assertFalse(
				SeleniumActions.seleniumIsDisplayed(driver, Connections.getActiveBondedConnectionViewer(bondedCon)),
				"Bonded connection:" + bondedCon + " is still running");
	}

	/**
	 * Checks a bonded connection is running in Bonded connection viewer
	 * 
	 * @param driver
	 * @param bondedCon
	 *            - name of the bonded connection
	 * @param noOfPairs
	 *            - number of bonded connections running
	 * @throws InterruptedException
	 */
	public void checkActiveBondedConnection(WebDriver driver, String bondedCon, String noOfPairs)
			throws InterruptedException {
		navigateToBondedConnectionViewer(driver);
		Assert.assertTrue(
				SeleniumActions.seleniumIsDisplayed(driver, Connections.getActiveBondedConnectionViewer(bondedCon)),
				"Bonded connection:" + bondedCon + " is not running");
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.getActiveBondedPairViewer(noOfPairs)),
				"The bonded connection " + bondedCon
						+ " is not running or does not have the correct number of pairs. Expected pairs:" + noOfPairs);
	}

	/**
	 * Navigates from anywhere in Boxilla to the bonded connection viewer
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToBondedConnectionViewer(WebDriver driver) throws InterruptedException {
		navigateToConnectionViewer(driver);
		SeleniumActions.seleniumClick(driver, Connections.getViewerToggleBondedBtnOn());
	}

	/**
	 * Deletes a connection from a bonded connection group
	 * 
	 * @param driver
	 * @param groupName
	 *            - Bonded connection name
	 * @param index
	 *            - The index of the connection to be removed
	 * @param newConList
	 *            - The new updated connection list
	 * @throws InterruptedException
	 */
	public void deleteConnectionFromBond(WebDriver driver, String groupName, String index, String[] newConList)
			throws InterruptedException {
		Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
				"bonded connection does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionGroupDropdown());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditBondedConnectionGroupBtn())));
		SeleniumActions.seleniumClick(driver, Connections.getEditBondedConnectionGroupBtn());

		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		SeleniumActions.seleniumClick(driver, Connections.getDeleteConnectionFromBond(index));
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		reviewBondedConSummary(driver, groupName, newConList);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.addSwitchSuccessToastXpath)));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Successfully edited bonded connection."),
				"Error creating bonded connection group. Expected Succes, Actual:" + toast);

	}

	/**
	 * Edits a bonded connection by moving a connection in the bonded connection up
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the bonded connection
	 * @param startIndex
	 *            - The start index where the connection is
	 * @param endIndex
	 *            - The index where the connection will end up
	 * @param newGroup
	 *            - An array of the new connection configuration
	 * @throws InterruptedException
	 */
	public void editBondedConnectionMoveConnectionUp(WebDriver driver, String groupName, String startIndex,
			String endIndex, String[] newGroup) throws InterruptedException {
		Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
				"bonded connection does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionGroupDropdown());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditBondedConnectionGroupBtn())));
		SeleniumActions.seleniumClick(driver, Connections.getEditBondedConnectionGroupBtn());
		int startIndexInt = Integer.parseInt(startIndex);
		int endIndexInt = Integer.parseInt(endIndex);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		for (int j = endIndexInt; j > startIndexInt; j--) {
			SeleniumActions.seleniumClick(driver, Connections.getMoveConnectionUpBtn(Integer.toString(j)));
		}
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		reviewBondedConSummary(driver, groupName, newGroup);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.addSwitchSuccessToastXpath)));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Successfully edited bonded connection."),
				"Error creating bonded connection group. Expected Succes, Actual:" + toast);

	}

	/**
	 * Edits a bonded connection by moving a connection in the bonded connection
	 * down
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the bonded connection
	 * @param startIndex
	 *            - The start index where the connection is
	 * @param endIndex
	 *            - The index where the connection will end up
	 * @param newGroup
	 *            - An array of the new connection configuration
	 * @throws InterruptedException
	 */
	public void editBondedConnectionMoveConnectionDown(WebDriver driver, String groupName, String startIndex,
			String endIndex, String[] newGroup) throws InterruptedException {
		Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
				"bonded connection does not exist:" + groupName);
		SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionGroupDropdown());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditBondedConnectionGroupBtn())));
		SeleniumActions.seleniumClick(driver, Connections.getEditBondedConnectionGroupBtn());
		int startIndexInt = Integer.parseInt(startIndex);
		int endIndexInt = Integer.parseInt(endIndex);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		for (int j = startIndexInt; j < endIndexInt; j++) {
			SeleniumActions.seleniumClick(driver, Connections.getMoveConnectionUpBtn(Integer.toString(j)));
		}
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		reviewBondedConSummary(driver, groupName, newGroup);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.addSwitchSuccessToastXpath)));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Successfully edited bonded connection."),
				"Error creating bonded connection group. Expected Succes, Actual:" + toast);

	}

	/**
	 * Edits an already created bonded connection name and or connection list
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the bonded connection to edit
	 * @param groupNewName
	 *            - New name of the bonded connection
	 * @param connectionList
	 *            - Original list of connections in bonded connection
	 * @param newConnectionList
	 *            - new list of connections in bonded connection
	 * @throws InterruptedException
	 */
	public void editBondedConnection(WebDriver driver, String groupName, String groupNewName, String[] connectionList,
			String[] newConnectionList) throws InterruptedException {
		Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
				"bonded connection group " + groupName + " does not exist");
		SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionGroupDropdown());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditBondedConnectionGroupBtn())));
		SeleniumActions.seleniumClick(driver, Connections.getEditBondedConnectionGroupBtn());

		if (!groupNewName.equals("")) {
			SeleniumActions.seleniumSendKeysClear(driver, Connections.getCreateBondedConnectionNameTextBox());
			SeleniumActions.seleniumSendKeys(driver, Connections.getCreateBondedConnectionNameTextBox(), groupNewName);
			groupName = groupNewName;
		}
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		if (newConnectionList == null) {
			SeleniumActions.seleniumClick(driver, Connections.btnNext);
		} else {
			// delete all connections and apply the new ones
			for (int j = 0; j < connectionList.length; j++) {
				SeleniumActions.seleniumClick(driver, Connections.getDeleteConnectionFromBond("0"));
			}
			// add new connections
			for (int j = 0; j < newConnectionList.length; j++) {
				SeleniumActions.seleniumClick(driver, Connections.getAddConnectionToBondButton());
				SeleniumActions.seleniumSendKeys(driver, Connections.getAddConnectionToBondTextBox(j),
						newConnectionList[j]);
			}
			connectionList = newConnectionList;
			SeleniumActions.seleniumClick(driver, Connections.btnNext);
		}
		reviewBondedConSummary(driver, groupName, connectionList);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Switch.addSwitchSuccessToastXpath)));
		String toast = SeleniumActions.seleniumGetText(driver, Switch.addSwitchSuccessToastXpath);
		Assert.assertTrue(toast.equals("Successfully edited bonded connection."),
				"Error creating bonded connection group. Expected Succes, Actual:" + toast);

	}

	/**
	 * Gets the name of a connection group from the connection group table
	 * 
	 * @param driver
	 * @param groupName
	 *            - name to search for
	 * @param isOnPage
	 *            - set this to true if you are already on the page.
	 * @return - The name of the group from the table
	 * @throws InterruptedException
	 */
	public String getNameOfConnectionGorup(WebDriver driver, String groupName, boolean isOnPage)
			throws InterruptedException {

		if (!isOnPage) {
			Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
					"Bonded connection group does not exist");
		}
		String name = SeleniumActions.seleniumGetText(driver, Connections.getBondedConnectionNameFromTable());
		log.info("Bonded connection name = " + name);
		return name;
	}

	public String getNumberOfConnectionInBondedGroup(WebDriver driver, String groupName, boolean isOnPage)
			throws InterruptedException {
		if (!isOnPage) {
			Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
					"Bonded connection group does not exist");
		}
		String number = SeleniumActions.seleniumGetText(driver, Connections.getBondedConnectionCountFromTable());
		log.info("Number of connections in bonded group: " + number);
		return number;
	}

	/**
	 * This will check all the details of a bonded connection in the bonded
	 * connection table against what is passed in
	 * 
	 * @param driver
	 * @param groupName
	 *            - name of the bonded connection
	 * @param numberOfConnections
	 *            - the number of connnections in the bonded connection
	 * @param connections
	 *            - list of the connections in the bonded connection
	 * @param zone
	 *            - The zone (if any) the bonded connection is in. Set to "" if no
	 *            zone
	 * @throws InterruptedException
	 */
	public void checkAllBondedConnectionGroupDetails(WebDriver driver, String groupName, String numberOfConnections,
			String[] connections, String zone) throws InterruptedException {
		boolean isExist = doesBondedConnectionExist(driver, groupName, false);
		Assert.assertTrue(isExist, "Connection group:" + groupName + " does not exist");
		String name = getNameOfConnectionGorup(driver, groupName, true);
		log.info("Asserting name is correct");
		Assert.assertTrue(name.equals(groupName), "Name does not match");
		String noCon = getNumberOfConnectionInBondedGroup(driver, groupName, true);
		log.info("Asserting number of connections is correct");
		Assert.assertTrue(noCon.equals(numberOfConnections),
				"Number of connections did not match. expected:" + numberOfConnections + " actual:" + noCon);
		if (zone.equals("")) {
			zone = "-";
		}
		String zoneName = getBondedGroupZone(driver, groupName, true);
		Assert.assertTrue(zoneName.equals(zone), "Zone name did not match. Expected:" + zone + " , actual:" + zoneName);
		log.info("Asserting connections from bonded group");
		String[] cons = getConnectionsInBondedGroup(driver, groupName, true);
		for (int j = 0; j < cons.length; j++) {
			Assert.assertTrue(cons[j].equals(connections[j]),
					"Connections did not match. Expected:" + connections[j] + " ,actual:" + cons[j]);
		}
	}

	/**
	 * This will delete the passed in bonded connection group
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the bonded connection group to be deleted
	 * @throws InterruptedException
	 */
	public void deleteBondedConnectionGroup(WebDriver driver, String groupName) throws InterruptedException {
		if (doesBondedConnectionExist(driver, groupName, false)) {
			SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionGroupDropdown());
			new WebDriverWait(driver, 60).until(
					ExpectedConditions.elementToBeClickable(By.xpath(Connections.getDeleteBondedConnectionGroupBtn())));
			SeleniumActions.seleniumClick(driver, Connections.getDeleteBondedConnectionGroupBtn());
			Alert alert = driver.switchTo().alert();
			alert.accept();
			new WebDriverWait(driver, 60).until(
					ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(Connections.templateToastMessage)));
			String toast = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
			Assert.assertTrue(toast.contains("Success"),
					"Error in delete. Toast did not contain Success. Actual:" + toast);
		} else {
			throw new AssertionError("No such bonded connection group exists");
		}
	}

	/**
	 * Returns the zone the bonded connection group belongs to (if any)
	 * 
	 * @param driver
	 * @param groupName
	 *            - Name of the bonded connection group to check
	 * @param isOnPage
	 *            - Set to true if already on this page
	 * @return
	 * @throws InterruptedException
	 */
	public String getBondedGroupZone(WebDriver driver, String groupName, boolean isOnPage) throws InterruptedException {
		if (!isOnPage) {
			Assert.assertTrue(doesBondedConnectionExist(driver, groupName, false),
					"Bonded connection group does not exist");
		}
		String zone = SeleniumActions.seleniumGetText(driver, Connections.getBondedConnectionZoneFromTable());
		log.info("Bonded connection group zone: " + zone);
		return zone;
	}

	public String[] getConnectionsInBondedGroup(WebDriver driver, String groupName, boolean isOnPage)
			throws InterruptedException {
		if (!isOnPage) {
			doesBondedConnectionExist(driver, groupName, false);
		}
		String numberofConnections = getNumberOfConnectionInBondedGroup(driver, groupName, true);
		int number = Integer.parseInt(numberofConnections);
		SeleniumActions.seleniumClick(driver, Connections.getBondedConnectionDrillDown());
		new WebDriverWait(driver, 60).until(ExpectedConditions
				.visibilityOfElementLocated(By.xpath(Connections.getBondedConnectionDrillDownHeading())));
		String[] connections = new String[number];
		for (int j = 0; j < number; j++) {
			String con = SeleniumActions.seleniumGetText(driver, Connections.getConnectionFromBonded(groupName, j))
					.substring(1).trim();
			log.info("Connection:" + con + ".");

			connections[j] = con;
		}
		return connections;
	}

	/**
	 * This checks the review page when making a bonded connection for all the
	 * correct bonded connection details
	 * 
	 * @param driver
	 * @param bondedGroupName
	 *            - name of the bonded connection to check
	 * @param connections
	 *            - List of connections in the bonded connection
	 */
	private void reviewBondedConSummary(WebDriver driver, String bondedGroupName, String[] connections) {
		String nameText = SeleniumActions.seleniumGetText(driver, Connections.getBondedConSummaryName());
		Assert.assertTrue(bondedGroupName.equals(nameText),
				"Bonded connection name did not match name on summary. Expected: " + bondedGroupName + " ,Actual:"
						+ nameText);
		for (int j = 0; j < connections.length; j++) {
			Assert.assertTrue(
					SeleniumActions.seleniumIsDisplayed(driver,
							Connections.getBondedConSummaryConNames(connections[j])),
					"The connection " + connections[j] + "was not on the summary page");
		}
	}

	/**
	 * Navigates from anywhere in boxilla to the bonded connection table
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToBondedConnectionsTable(WebDriver driver) throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumClick(driver, Connections.getBondedTab());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getBondedConTableHead())));
	}

	/**
	 * Checks if a bonded connection exists on Boxilla
	 * 
	 * @param driver
	 * @param bondedConnectionName
	 *            - Name of the bonded connection to check
	 * @param isOnPage
	 *            - set to true if already on this page
	 * @return - Boolean. True if bonded connection exists, else false
	 * @throws InterruptedException
	 */
	public boolean doesBondedConnectionExist(WebDriver driver, String bondedConnectionName, boolean isOnPage)
			throws InterruptedException {
		if (!isOnPage) {
			navigateToBondedConnectionsTable(driver);
		}
		SeleniumActions.seleniumSendKeysClear(driver, Connections.getBondedConSearchBox());
		SeleniumActions.seleniumSendKeys(driver, Connections.getBondedConSearchBox(), bondedConnectionName);
		Thread.sleep(500);
		return SeleniumActions.seleniumIsDisplayed(driver, Connections.getBondedConnectionNameFromTable());
	}

	/**
	 * THis will get the value of an element on a page, then refresh the page after
	 * a certain length of time refresh the page and return the new value
	 * 
	 * @param originalValue
	 * @param xpath
	 * @param driver
	 * @return
	 */
	public String autoRefreshPoll(String originalValue, String xpath, WebDriver driver) {

		String newValue = "";
		int counter = 0;
		boolean timer = true;
		while (timer) {
			try {
				Thread.sleep(10000);
			} catch (Exception e) {

			}
			counter++;
			newValue = SeleniumActions.seleniumGetText(driver, xpath);
			log.info("Value:" + newValue);
			if (!newValue.equals(originalValue) || counter == 20) {
				timer = false;
				return newValue;
			}
		}
		return newValue;

	}

	/**
	 * This sets the display orientation for navair connections based on parameters
	 * passed in
	 * 
	 * @param driver
	 * @param orientation
	 *            - The orientation of the connection
	 * @param isTemplate
	 *            - Is the connection based on a template
	 */
	public void setDisplayOrientation(WebDriver driver, Connections.Orientation orientation, boolean isTemplate) {
		switch (orientation) {
		case H12:
			log.info("Setting Orientation to H12");
			if (!isTemplate) {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalDisplay2Orientation(),
						Connections.getOrientationRightColumn());
			} else {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalTemplateDisplay2Orientation(),
						Connections.getTemplateOrientationRightColumn());
			}
			break;
		case H21:
			log.info("Setting Orientation to H21");
			if (!isTemplate) {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalDisplay1Orientation(),
						Connections.getOrientationRightColumn());
			} else {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalTemplateDisplay1Orientation(),
						Connections.getTemplateOrientationRightColumn());
			}
			break;
		case V21:
			log.info("Setting Orientation to V21");
			if (!isTemplate) {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalDisplay1Orientation(),
						Connections.getOriginalDisplay2Orientation());
			} else {
				SeleniumActions.dragAndDrop(driver, Connections.getOriginalTemplateDisplay1Orientation(),
						Connections.getOriginalTemplateDisplay2Orientation());
			}
			break;
		case V12:
			log.info("V12 is default orientation. Doing nothing");
			break;
		}
	}

	/**
	 * This will edit a navair connections orientation
	 * 
	 * @param driver
	 * @param connectionToEdit
	 *            - Name of the connection to edit
	 * @param ore
	 *            - The new orientation to give the connection
	 * @throws InterruptedException
	 */
	public void editPairedConnectionOrientation(WebDriver driver, String connectionToEdit, Connections.Orientation ore)
			throws InterruptedException {
		navigateToConnectionsManage(driver);
		// find the connection to edit
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionToEdit);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionToEdit),
				"Connection table text did not contain: " + connectionToEdit + ", actual text: " + connectionTableText); // Asserting
																															// connection
																															// name
																															// present
																															// in
																															// connection
																															// table

		// click dropdown
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdown());
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdownEdit());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditModalNextBtn())));

		// navigate to private shared
		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		setDisplayOrientation(driver, ore, false);
		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		saveConnection(driver, connectionToEdit);

	}

	/**
	 * This will edit a navair paired connections connetion type to either shared or
	 * private
	 * 
	 * @param driver
	 * @param connectionToEdit
	 *            - Name of the connection to edit
	 * @param isShared
	 *            - true if shared connection
	 * @throws InterruptedException
	 */
	public void editPairedConnectionType(WebDriver driver, String connectionToEdit, boolean isShared)
			throws InterruptedException {
		navigateToConnectionsManage(driver);
		// find the connection to edit
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionToEdit);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionToEdit),
				"Connection table text did not contain: " + connectionToEdit + ", actual text: " + connectionTableText); // Asserting
																															// connection
																															// name
																															// present
																															// in
																															// connection
																															// table

		// click dropdown
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdown());
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdownEdit());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditModalNextBtn())));

		// navigate to private shared
		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		if (isShared) {
			chooseCoonectionType(driver, "shared");
		} else {
			chooseCoonectionType(driver, "private");
		}
		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		String completeText = SeleniumActions.seleniumGetText(driver, Connections.getCompeteConnectionModal());
		String clean = completeText.replaceAll("\r", "").replaceAll("\n", "");
		log.info(clean);

		// check shared
		if (isShared) {
			Assert.assertTrue(clean.contains("Connection TypeShared"), "Connection type was not shared");
		} else {
			Assert.assertTrue(clean.contains("Connection TypePrivate"), "Connection type was not private");
		}
		saveConnection(driver, connectionToEdit);
	}

	/**
	 * Edit navair paired connections IP address
	 * 
	 * @param driver
	 * @param connectionToEdit
	 *            - The name of the connection to edit
	 * @param ip1
	 *            - the IP address of the first IP to change to
	 * @param ip2
	 *            - the IP address of the second IP to change to
	 * @throws InterruptedException
	 */
	public void editPairedConnectionIps(WebDriver driver, String connectionToEdit, String ip1, String ip2)
			throws InterruptedException {
		navigateToConnectionsManage(driver);
		// find the connection to edit
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionToEdit);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionToEdit),
				"Connection table text did not contain: " + connectionToEdit + ", actual text: " + connectionTableText); // Asserting
																															// connection
																															// name
																															// present
																															// in
																															// connection
																															// table

		// click dropdown
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdown());
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdownEdit());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Connections.getEditModalNextBtn())));
		SeleniumActions.seleniumSendKeysClear(driver, Connections.getEditConnectionIp1());
		SeleniumActions.seleniumSendKeys(driver, Connections.getEditConnectionIp1(), ip1);
		SeleniumActions.seleniumSendKeysClear(driver, Connections.getEditConnectionIp2());
		SeleniumActions.seleniumSendKeys(driver, Connections.getEditConnectionIp2(), ip2);

		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		SeleniumActions.seleniumClick(driver, Connections.getEditModalNextBtn());
		String completeText = SeleniumActions.seleniumGetText(driver, Connections.getCompeteConnectionModal());
		String clean = completeText.replaceAll("\r", "").replaceAll("\n", "");
		log.info(clean);
		Assert.assertTrue(clean.contains(ip1), "review modal did not contain IP1");
		Assert.assertTrue(clean.contains(ip2), "review modal did not contain IP2");
		saveConnection(driver, connectionToEdit);

	}

	/**
	 * assumes cpage is connections manage and connection has been searched for
	 * 
	 * @param driver
	 * @param name
	 * @param type
	 * @param via
	 */
	public void checkConnectionDetailsConnectionManage(WebDriver driver, String name, String type, String via) {
		if (!name.equals("") && name != null) {
			log.info("Checking con name");
			String realName = SeleniumActions.seleniumGetText(driver,
					Connections.getConnectionNameFromConManageTable());
			Assert.assertTrue(realName.equals(name),
					"Connection name did not match. Expected:" + name + " , Actual:" + realName);
		}

		if (!type.equals("") && type != null) {
			log.info("Checking con type");
			String realType = SeleniumActions.seleniumGetText(driver,
					Connections.getConnectionTypeFromConManageTable());
			Assert.assertTrue(realType.equals(type),
					"Connection type did not match. Expected:" + type + ", Actual:" + realType);
		}

		if (!via.equals("") && via != null) {
			log.info("Checking con via");
			String realVia = SeleniumActions.seleniumGetText(driver, Connections.getConnectionViaFromConManageTable());
			Assert.assertTrue(realVia.equals(via),
					"Connection via did not match. Expected:" + via + " , Actual:" + realVia);
		}

	}

	// using Strings instead of booleans cause im too lazy to figure out how to use
	// dataprovider with bools
	/**
	 * This method will create a navair paired connections using the parameters
	 * provided. Can be used in a data driven test
	 * 
	 * @param driver
	 * @param connectionName
	 *            - name of the connection to create
	 * @param isTemplate
	 *            - true or false string is say if creating from a template
	 * @param templateName
	 *            - name of the template if used
	 * @param targets
	 *            - number of targets for the connection
	 * @param orientation
	 *            - orientation of the connection
	 * @param ip1
	 *            - IP address of source 1
	 * @param ip2
	 *            - IP address of source 2
	 * @param shared
	 *            - true or false string to say if connection is shared
	 * @param audio
	 *            - true or false string to say if audio is enabled
	 * @param audio2
	 *            - true or false string to say if audio 2 is enabled
	 * @param persistent
	 *            - true or false string to say if persistence is enabled
	 * @param viewOnly
	 *            - true or false string to say if viewOnly is enabled
	 * @throws InterruptedException
	 */
	public void createTxPairConnection(WebDriver driver, String connectionName, String isTemplate, String templateName,
			String targets, Connections.Orientation orientation, String ip1, String ip2, String shared, String audio,
			String audio2, String persistent, String viewOnly) throws InterruptedException {
		navigateToConnectionsManage(driver);
		log.info("Creating TX pair connection");
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnection);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getAddConnectionModal())));
		WebElement txPair = driver.findElement(By.xpath(Connections.getTxPairButton()));
		SeleniumActions.exectuteJavaScriptClick(driver, txPair);
		if (isTemplate.equals("true")) {

			WebElement useTemplate = driver.findElement(By.xpath(Connections.useTemplateYes()));
			SeleniumActions.exectuteJavaScriptClick(driver, useTemplate);
			SeleniumActions.exectuteJavaScriptClick(driver, txPair);
			SeleniumActions.seleniumSendKeys(driver, Connections.connectionName, connectionName);
			SeleniumActions.seleniumSendKeys(driver, Connections.getPairedIpAddress1(), ip1);
			SeleniumActions.seleniumSendKeys(driver, Connections.getPairedIpAddress2(), ip2);
			SeleniumActions.seleniumClick(driver, Connections.btnNext);
			SeleniumActions.seleniumDropdown(driver, Connections.getConnectionTemplatePair(), templateName);
			propertyInfoClickNext(driver);
			assertReviewPaired(driver, connectionName, targets, ip1, ip2, shared, audio, audio2, persistent, viewOnly);
			saveConnection(driver, connectionName);
			return;
		}
		if (targets.equals("1")) {
			WebElement target1 = driver.findElement(By.xpath(Connections.getPairedTarget1()));
			SeleniumActions.exectuteJavaScriptClick(driver, target1);
		} else {
			WebElement target2 = driver.findElement(By.xpath(Connections.getPairedTarget2()));
			SeleniumActions.exectuteJavaScriptClick(driver, target2);
		}
		SeleniumActions.seleniumSendKeys(driver, Connections.connectionName, connectionName);
		SeleniumActions.seleniumSendKeys(driver, Connections.getPairedIpAddress1(), ip1);
		SeleniumActions.seleniumSendKeys(driver, Connections.getPairedIpAddress2(), ip2);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.connectionTypePrivate)));

		// property info
		if (shared.equals("true"))
			chooseCoonectionType(driver, "shared");

		if (audio.equals("true")) {
			enableAudio(driver);
			if (audio2.equals("true")) {
				WebElement audioElement1 = driver.findElement(By.xpath(Connections.getAudioIp1()));
				SeleniumActions.exectuteJavaScriptClick(driver, audioElement1);
			}
		}

		if (persistent.equals("true"))
			enablePersistenConnection(driver);

		if (viewOnly.equals("true"))
			enableViewOnlyConnection(driver);

		if (targets.equals("2")) {
			setDisplayOrientation(driver, orientation, false);
		}

		propertyInfoClickNext(driver);
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getCompeteConnectionModal())));
		String completeText = SeleniumActions.seleniumGetText(driver, Connections.getCompeteConnectionModal());
		String clean = completeText.replaceAll("\r", "").replaceAll("\n", "");
		log.info(clean);

		assertReviewPaired(driver, connectionName, targets, ip1, ip2, shared, audio, audio2, persistent, viewOnly);
		saveConnection(driver, connectionName);

	}

	/**
	 * This will check connection details of a navair paired connection on the
	 * review screen when creating a connection
	 * 
	 * @param driver
	 * @param connectionName
	 * @param targets
	 * @param ip1
	 * @param ip2
	 * @param shared
	 * @param audio
	 * @param audio2
	 * @param persistent
	 * @param viewOnly
	 */
	private void assertReviewPaired(WebDriver driver, String connectionName, String targets, String ip1, String ip2,
			String shared, String audio, String audio2, String persistent, String viewOnly) {
		// assert the review modal

		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getCompeteConnectionModal())));
		String completeText = SeleniumActions.seleniumGetText(driver, Connections.getCompeteConnectionModal());
		String clean = completeText.replaceAll("\r", "").replaceAll("\n", "");
		log.info(clean);

		// shared private
		if (shared.equals("true")) {
			Assert.assertTrue(clean.contains("Connection TypeShared"), "Connection type was not shared");
		} else {
			Assert.assertTrue(clean.contains("Connection TypePrivate"), "Connection type was not private");
		}
		// name
		Assert.assertTrue(clean.contains(connectionName), "Connection name was not in review modal");
		// number of targets
		if (targets.equals("1")) {
			Assert.assertTrue(clean.contains("Targets1"), "Number of targets on review modal was not 1");
		} else {
			Assert.assertTrue(clean.contains("Targets2"), "Number of targets on review modal was not 2");
		}
		// ip addresses
		Assert.assertTrue(clean.contains(ip1), "review modal did not contain IP1");
		Assert.assertTrue(clean.contains(ip2), "review modal did not contain IP2");
		// AUDIO
		if (audio.equals("true")) {
			Assert.assertTrue(clean.contains("AudioYes"), "Review modal did not contain Audio Yes");
			if (audio2.equals("true")) {
				Assert.assertTrue(clean.contains("Audio: IP Address 2Yes"),
						"Review modal did not contain Audio IP 2 Yes");
			} else {
				Assert.assertTrue(clean.contains("Audio: IP Address 1Yes"),
						"Review modal did not contain Audio IP 1 Yes");
			}
		} else {
			Assert.assertTrue(clean.contains("AudioNo"), "Review modal did not contain Audio No");
		}
		// persistent
		if (persistent.equals("true")) {
			Assert.assertTrue(clean.contains("Persistent ConnectionYes"),
					"Review modal did not contain Persistent Connection Yes");
		} else {
			Assert.assertTrue(clean.contains("Persistent ConnectionNo"),
					"Review modal did not contain Persistent Connection No");
		}
		// viewonly
		if (viewOnly.equals("true")) {
			Assert.assertTrue(clean.contains("View OnlyYes"), "Review modal did not contain View Only Yes");
		} else {
			Assert.assertTrue(clean.contains("View OnlyNo"), "Review modal did not contain View Only No");
		}

	}

	/**
	 * This will SSH into an appliance and check if the connection process is
	 * running with the correct connection name. Throws an assertion error if
	 * connection is not running
	 * 
	 * @param username
	 *            - SSH user name for appliance
	 * @param password
	 *            - SSH password for the appliance
	 * @param ip
	 *            - IP address of the appliance
	 * @param connectionName
	 *            - Name of connection to check
	 */
	public void checkConnectionIsActiveSsh(String username, String password, String ip, String connectionName) {
		Ssh ssh = new Ssh(username, password, ip);
		ssh.loginToServer();
		String output = ssh.sendCommand("ps -ax");
		Assert.assertTrue(output.contains(connectionName), "Connection " + connectionName + " was not running");
		log.info("Connection " + connectionName + " is running");
	}

	/**
	 * This will return the connection time from the active connection config table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getTimeFromActiveConnectionConfigTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		SeleniumActions.seleniumClick(driver, ActiveConnectionElements.getConfigurationTableTab());
		String bw = SeleniumActions.seleniumGetText(driver, ActiveConnectionElements.getConfigTableConnectionTime());
		log.info("Time from Active Connection config table:" + bw);
		return bw;
	}

	/**
	 * This will return the network bandwidth from the active connection frame rate
	 * table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getNetworkBWFromActiveConnectionFrameTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		SeleniumActions.seleniumClick(driver, ActiveConnectionElements.getFrameRateTableTab());
		String bw = SeleniumActions.seleniumGetText(driver,
				ActiveConnectionElements.getFrameTableConnectionNetworkBW());
		log.info("Total connection network B/W from Active Connection frame table:" + bw);
		return bw;
	}

	/**
	 * This will return the network bandwidth from the active connection table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getNetworkBWFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		String bw = SeleniumActions.seleniumGetText(driver,
				ActiveConnectionElements.getConnectionTableConnectionNetworkBW());
		log.info("Total connection network B/W from Active Connection table:" + bw);
		return bw;
	}

	/**
	 * This will return the connection time from the active connection table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getConnectionTimeFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		String time = SeleniumActions.seleniumGetText(driver,
				ActiveConnectionElements.getConnectionTableConnectionTime());
		log.info("Connection time from Active Connection table:" + time);
		return time;
	}

	/**
	 * This will return all the details of an active connection from the connection
	 * connection table
	 * 
	 * @param driver
	 * @param rows
	 * @return
	 * @throws InterruptedException
	 */
	public String[] getActiveConnectionsDetails(WebDriver driver, int[][] rows) throws InterruptedException {
		String[] connections = new String[rows.length];
		navigateToActiveConnection(driver);
		for (int j = 0; j < rows.length; j++) {
			for (int k = 0; k < rows[k].length; k++) {
				connections[j] = SeleniumActions.seleniumGetText(driver,
						ActiveConnectionElements.getRowAndColumnFromActiveConnectionTable(rows[j][0], rows[j][1]));
				log.info("Row:" + rows[j][0]);
				log.info("Column:" + rows[j][1]);
				log.info("Return:" + connections[j]);
			}
		}
		return connections;

	}

	/**
	 * This will return a transmitter from an active connection table
	 * 
	 * @param driver
	 * @param txName
	 * @return
	 * @throws InterruptedException
	 */
	public String searchActiveConnectionForTransmitter(WebDriver driver, String txName) throws InterruptedException {
		navigateToActiveConnection(driver);
		SeleniumActions.seleniumSendKeysClear(driver, ActiveConnectionElements.getActiveConnectionSearchBox());
		SeleniumActions.seleniumSendKeys(driver, ActiveConnectionElements.getActiveConnectionSearchBox(), txName);
		String tx = "";
		tx = SeleniumActions.seleniumGetText(driver, ActiveConnectionElements.getTransmitterFromSearch(txName));
		return tx;
	}

	public String getTransmitterFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		// Thread.sleep(2000);
		Thread.sleep(5000);

		String tx = SeleniumActions.seleniumGetText(driver, ActiveConnectionElements.getConnectionTableTransmitter());
		log.info("TX from Active Connection table:" + tx);
		return tx;
	}

	/**
	 * This will return the user from the active connection table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getUserFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		Thread.sleep(5000);
		String user = SeleniumActions.seleniumGetText(driver, ActiveConnectionElements.getConnectionTableUser());
		log.info("User from Active Connection table:" + user);
		return user;
	}

	/**
	 * This will return the active connection name from the active connection table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getConnectionNameFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		Thread.sleep(2000);
		String name = SeleniumActions.seleniumGetText(driver,
				ActiveConnectionElements.getConnectionTableConnectionName());
		log.info("Connection name from Active Connection table:" + name);
		return name;
	}

	/**
	 * This will return the receiver from the active connection table
	 * 
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getReceiverFromActiveConnectionTable(WebDriver driver) throws InterruptedException {
		navigateToActiveConnection(driver);
		Thread.sleep(5000);
		String receiver = SeleniumActions.seleniumGetText(driver,
				ActiveConnectionElements.getConnectionTableReceiver());
		log.info("Receiver from Active Connection Table:" + receiver);
		return receiver;
	}

	/**
	 * This will edit a connection templates name
	 * 
	 * @param driver
	 * @param oldTemplateName
	 *            - Name of template to change
	 * @param newTemplateName
	 *            - Name of template to change too
	 * @throws InterruptedException
	 */
	public void editConnectionTemplateName(WebDriver driver, String oldTemplateName, String newTemplateName)
			throws InterruptedException {
		editConnectionTemplate(driver, oldTemplateName);
		String templateNameXpath = "//input[@id='property-name']";
		SeleniumActions.seleniumSendKeysClear(driver, templateNameXpath);
		SeleniumActions.seleniumSendKeys(driver, templateNameXpath, newTemplateName);
		SeleniumActions.seleniumClick(driver, "//button[@id='btn-property-save']");
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastMsg = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toastMsg.contains("Successfully"),
				"Toasta message did not contain success, actual:" + toastMsg);
	}

	/**
	 * This will edit a connection templates via (what type of connection)
	 * 
	 * @param driver
	 * @param oldTemplateName
	 * @param via
	 * @throws InterruptedException
	 */
	public void editConnectionTemplateVia(WebDriver driver, String oldTemplateName, Connections.VIA via)
			throws InterruptedException {
		editConnectionTemplate(driver, oldTemplateName);

		switch (via) {
		case VM:
			SeleniumActions.seleniumClick(driver, "//div[3]//div[1]//div[1]//label[2]");
			break;
		case TX:
			SeleniumActions.seleniumClick(driver, "//div[@id='property-form']//div[3]//div[1]//div[1]//label[1]");
			break;
		case POOL:
			SeleniumActions.seleniumClick(driver, "//div[@id='property-form']//label[3]");
			break;
		case BROKER:
			SeleniumActions.seleniumClick(driver, "//div[@id='property-form']//label[4]");
			break;
		case HORIZON:
			SeleniumActions.seleniumClick(driver, "//div[@id='property-form']//label[5]");
			break;
		}

		SeleniumActions.seleniumClick(driver, "//button[@id='btn-property-save']");
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toastMsg = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		log.info("Toast Message:" + toastMsg);
		Assert.assertTrue(toastMsg.contains("Successfully"),
				"Toasta message did not contain success, actual:" + toastMsg);
	}

	/**
	 * This will check the connection options of a viaTx connection
	 * 
	 * @param driver
	 * @param name
	 * @param via
	 * @param type
	 * @param extDesk
	 * @param audio
	 * @param usb
	 * @param persistent
	 * @param view
	 * @return
	 * @throws InterruptedException
	 */
	public boolean[] checkConnectionOptionsViaTx(WebDriver driver, String name, String via, String type, String extDesk,
			String audio, String usb, String persistent, String view) throws InterruptedException {
		log.info("Checking connection details in Boxilla UI");
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, name + " "); // add a space to only get one
																							// in the table
		isViewOnlyEnabled(driver, view);
		checkConnectionDetailsConnectionManage(driver, name, type, via);
		boolean[] options = new boolean[4];
		// //check options
		if (extDesk.equals("Yes")) {
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopEnabled())));
		} else {
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopDisabled())));
		}
		if (audio.equals("Yes")) {
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioEnabled())));
		} else {
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioDisabled())));
		}
		if (usb.equals("Yes")) {
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbEnabled())));
		} else {
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbDisabled())));
		}
		if (persistent.equals("Yes")) {
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconPersistentEnabled())));
		} else {
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconPersistentDisabled())));
		}
		return options;
	}

	/**
	 * Will check the icon on connection table to see if view only is enabled
	 * 
	 * @param driver
	 * @param view
	 */
	public void isViewOnlyEnabled(WebDriver driver, String view) {
		String att = SeleniumActions.getAttribute(driver, Connections.getIconViewOnlyDisabled(), "style");
		if (view.equals("Yes")) {
			Assert.assertTrue(att.contains("rgb(184, 233, 134)"), "View only was not enabled");
		} else {
			Assert.assertTrue(att.contains("rgb(206, 207, 200)"), "View only was not disabled");
		}
	}

	/**
	 * This will check if the connection options for a VM pool are correct in the
	 * connections table
	 * 
	 * @param driver
	 * @param name
	 * @param via
	 * @param type
	 * @param extDesk
	 * @param audio
	 * @param usb
	 * @param view
	 * @return
	 * @throws InterruptedException
	 */
	public boolean[] checkConnectionOptionsVMPool(WebDriver driver, String name, String via, String type,
			String extDesk, String audio, String usb, String view) throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, name + " "); // add a space to only get one
																							// in the table
		isViewOnlyEnabled(driver, view);
		checkConnectionDetailsConnectionManage(driver, name, type, via);
		boolean[] options = new boolean[4];
		// check options
		if (extDesk.equals("Yes")) {
			log.info("Checking ext desktop is enabled");
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopEnabled())));
		} else {
			log.info("Checking ext desktop is disabled");
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopDisabled())));
		}
		if (audio.equals("Yes")) {
			log.info("Checking audio is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioEnabled())));
		} else {
			log.info("Checking audio is disbled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioDisabled())));
		}
		if (usb.equals("Yes")) {
			log.info("Checking usb is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbEnabled())));
		} else {
			log.info("Checking usb is disbled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbDisabled())));
		}
		return options;
	}

	/**
	 * This will check the connection options for a VM connection are correct in the
	 * connections table
	 * 
	 * @param driver
	 * @param name
	 * @param via
	 * @param type
	 * @param extDesk
	 * @param audio
	 * @param usb
	 * @param nla
	 * @param view
	 * @return
	 * @throws InterruptedException
	 */
	public boolean[] checkConnectionOptionsVM(WebDriver driver, String name, String via, String type, String extDesk,
			String audio, String usb, String nla, String view) throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, name + " "); // add a space to only get one
																							// in the table
		isViewOnlyEnabled(driver, view);
		checkConnectionDetailsConnectionManage(driver, name, type, via);
		boolean[] options = new boolean[4];
		// check options
		if (extDesk.equals("Yes")) {
			log.info("Checking ext desktop is enabled");
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopEnabled())));
		} else {
			log.info("Checking ext desktop is disabled");
			new WebDriverWait(driver, 10).until(
					ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconExtDesktopDisabled())));
		}
		if (audio.equals("Yes")) {
			log.info("Checking audio is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioEnabled())));
		} else {
			log.info("Checking audio is disbled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconAudioDisabled())));
		}
		if (usb.equals("Yes")) {
			log.info("Checking usb is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbEnabled())));
		} else {
			log.info("Checking usb is disbled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconUsbDisabled())));
		}
		if (nla.equals("Yes")) {
			log.info("Checking nla is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconNlaEnabled())));
		} else {
			log.info("Checking nla is enabled");
			new WebDriverWait(driver, 10)
					.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.getIconNlaDisabled())));
		}
		return options;
	}

	private void editConnectionTemplate(WebDriver driver, String oldTemplateName) throws InterruptedException {
		navigateToConnectionsManage(driver);
		// click edit connection template
		String editTemplateButton = "//span[contains(text(),'Edit Connection Template')]";
		SeleniumActions.seleniumClick(driver, "//span[contains(text(),'Edit Connection Template')]");
		// check the pop up opened
		String saveTemplateButton = "//button[@id='btn-property-save']";
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(saveTemplateButton)));

		String chooseTemplateDropDown = "//select[@id='select-existing-template']";
		SeleniumActions.seleniumDropdown(driver, chooseTemplateDropDown, oldTemplateName);
	}

	/**
	 * This method will allow you to create a template for a connection with any
	 * option enabled or disabled
	 * 
	 * @param templateName
	 * @param via
	 * @param type
	 * @param isExtendedDesktop
	 * @param isUSBR
	 * @param isAudio
	 * @param isPersistent
	 * @param isViewOnly
	 * @param driver
	 * @throws InterruptedException
	 */
	public void masterCreateTemplate(String templateName, String via, String type, String isExtendedDesktop,
			String isUSBR, String isAudio, String isPersistent, String isViewOnly, WebDriver driver)
			throws InterruptedException {
		addConnectionTemplate(driver, via, templateName);
		addTemplateChooseConnectionType(driver, type);

		if (via.equals("broker")) {
			addTemplateDomainName(driver, "blackbox");
			addTemplateLoadBalanceInfo(driver, "loadbalance");
		}

		if (isExtendedDesktop.equals("true"))
			addTemplateEnableExtendedDesktop(driver);

		if (isUSBR.equals("true"))
			addTemplateEnableUSBRedirection(driver);

		if (isAudio.equals("true"))
			addTemplateEnableAudio(driver);

		if (isPersistent.equals("true") && !via.equals("vm")) {
			addTemplateEnablePersistenConnection(driver);
		} else if (isPersistent.equals("true") && via.equals("vm")) {
			addTemplateEnableNLA(driver);
		}

		if (isViewOnly.equals("true"))
			addTemplateViewOnlyConnection(driver);

		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
	}

	public void createPairedConnection(WebDriver driver, String connectionName, int numberOfSources, String ip1,
			String ip2) throws InterruptedException {
		navigateToConnectionsManage(driver);
		log.info("Creating paired connection with following details");
		log.info("Connection Name:" + connectionName);
		log.info("Number of sources:" + numberOfSources);
		log.info("Ip address 1:" + ip1 + " Ip address 2:" + ip2);
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnection);
	}

	/**
	 * This will create a connection of any type with any options enabled
	 * 
	 * @param connectionName
	 * @param connectVia
	 * @param type
	 * @param isExtended
	 * @param isUSBR
	 * @param isAudio
	 * @param isPersistent
	 * @param isViewOnly
	 * @param ip
	 * @param driver
	 * @param zone
	 * @param isLossless
	 * @throws InterruptedException
	 */
	@SuppressWarnings("null")
	public void createMasterConnection(String connectionName, String connectVia, String type, String isExtended,
			String isUSBR, String isAudio, String isPersistent, String isViewOnly, String ip, WebDriver driver,
			String zone, boolean isLossless) throws InterruptedException {
		addConnection(driver, connectionName, "no"); // connection name, user template
		if (zone != null) {
			if (!zone.equals(""))
				SeleniumActions.seleniumDropdown(driver, Connections.getZone(), zone);
		}
		if (isLossless) {
			SeleniumActions.seleniumClick(driver, Connections.getLosslessBtn());
		} else
			SeleniumActions.seleniumClick(driver, Connections.getOptimizedMode());

		connectionInfo(driver, connectVia, "user", "user", ip); // connection via, name, host ip
		if (connectVia.equals("tx")) {
			chooseCoonectionType(driver, type); // connection type
		} else if (connectVia.equals("broker")) {
			domainName(driver, "blackbox");
			loadBalanceInfo(driver, "loadbalance");
		}
		if (isExtended.equals("true"))
			enableExtendedDesktop(driver);
		if (isUSBR.equals("true"))
			enableUSBRedirection(driver);
		if (isAudio.equals("true"))
			enableAudio(driver);
		if (isPersistent.equals("true"))
			enablePersistenConnection(driver);
		if (isViewOnly.equals("true"))
			enableViewOnlyConnection(driver);

		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName, zone, isLossless); // Connection name to assert
	}

	/**
	 * This will create a connection of any type with any options enabled
	 * 
	 * @param connectionName
	 * @param connectVia
	 * @param type
	 * @param isExtended
	 * @param isUSBR
	 * @param isAudio
	 * @param isPersistent
	 * @param isViewOnly
	 * @param ip
	 * @param driver
	 * @param zone
	 * @param isLossless
	 * @throws InterruptedException
	 */
	public void createMasterConnection(String connectionName, String compressionMode, String connectVia, String type,
			String isExtended, String isUSBR, String isAudio, String isPersistent, String isViewOnly, String ip,
			WebDriver driver, String zone) throws InterruptedException {
		
		addConnection(driver, connectionName, "no"); // connection name, user template
		SeleniumActions.seleniumDropdown(driver, Connections.getZone(), zone);
		connectionInfo(driver, connectVia, "user", "user", ip); // connection via, name, host ip
		if (connectVia.equals("tx")) {
			chooseCoonectionType(driver, type); // connection type
		} else if (connectVia.equals("broker")) {
			domainName(driver, "blackbox");
			loadBalanceInfo(driver, "loadbalance");
		}
		if (isExtended.equals("true"))
			enableExtendedDesktop(driver);
		if (isUSBR.equals("true"))
			enableUSBRedirection(driver);
		if (isAudio.equals("true"))
			enableAudio(driver);
		if (isPersistent.equals("true"))
			enablePersistenConnection(driver);
		if (isViewOnly.equals("true"))
			enableViewOnlyConnection(driver);

		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName, zone); // Connection name to assert
	}
	
	public void createMasterConnectionZone(String connectionName, String connectionType, WebDriver driver, String txIp,String zone) throws InterruptedException
	{
		log.info("Creating connection : " + connectionName);
		addConnection(driver, connectionName, "no"); // connection name, user template
		SeleniumActions.seleniumDropdown(driver, Connections.getZone(), zone);
		connectionInfo(driver, "tx", "user", "", txIp); // connection via, name, host ip
		Thread.sleep(3000);
		chooseCoonectionType(driver, connectionType); // connection type
		enableExtendedDesktop(driver);
		enablePersistenConnection(driver);
		if (connectionType.equals("private")) {
			enableUSBRedirection(driver);
			enableAudio(driver);
		}
		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName);
		Thread.sleep(4000);
		
	}
	
    

	/**
	 * This will create a connection of any type with any options enabled
	 * 
	 * @param connectionName
	 * @param connectVia
	 * @param type
	 * @param isExtended
	 * @param isUSBR
	 * @param isAudio
	 * @param isPersistent
	 * @param isViewOnly
	 * @param ip
	 * @param driver
	 * @param zone
	 * @param isLossless
	 * @throws InterruptedException
	 */
	public void createMasterConnection(String connectionName, String connectVia, String type, String isExtended,
			String isUSBR, String isAudio, String isPersistent, String isViewOnly, String ip, WebDriver driver)
			throws InterruptedException {
		addConnection(driver, connectionName, "no"); // connection name, user template
		connectionInfo(driver, connectVia, "user", "user", ip); // connection via, name, host ip
		if (connectVia.equals("tx")) {
			chooseCoonectionType(driver, type); // connection type

		} else if (connectVia.equals("broker")) {
			domainName(driver, "blackbox");
			loadBalanceInfo(driver, "loadbalance");
		}
		// if(compressionmode.equalsIgnoreCase("Lossless")) {
		// SeleniumActions.seleniumClick(driver, Connections.getLosslessBtn());}
		// else SeleniumActions.seleniumClick(driver, Connections.getOptimizedMode());

		if (isExtended.equals("true"))
			enableExtendedDesktop(driver);
		if (isUSBR.equals("true"))
			enableUSBRedirection(driver);
		if (isAudio.equals("true"))
			enableAudio(driver);
		if (isPersistent.equals("true"))
			enablePersistenConnection(driver);
		if (isViewOnly.equals("true"))
			enableViewOnlyConnection(driver);

		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName); // Connection name to assert

	}

	/**
	 * Deletes a preset from connections > viewer page Navigates to connections >
	 * viewer and deletes the preset with the passed in name
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param name
	 *            name of preset to delete
	 * 
	 * @throws InterruptedException
	 */
	public void deletePreset(WebDriver driver, String name) throws InterruptedException {
		navigateToConnectionViewer(driver);
		Connections.managePresetsBtn(driver).click();
		timer(driver);
		Connections.searchAvailablePresets(driver).sendKeys(name);
		Connections.deletePresetBtn(driver, name).click();
		timer(driver);
	}

	/**
	 * Edits a preset name in connections > viewer
	 * 
	 * Navigates to connections > viewer and edits the name of a preset
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param oldName
	 *            original preset name
	 * @param newName
	 *            new preset name
	 * 
	 * @throws InterruptedException
	 */
	public void editPresetName(WebDriver driver, String oldName, String newName) throws InterruptedException {
		navigateToConnectionViewer(driver);
		Connections.managePresetsBtn(driver).click();
		timer(driver);
		Connections.searchAvailablePresets(driver).sendKeys(oldName);
		Connections.editPresetBtn(driver, oldName).click();
		timer(driver);
		Connections.editPresetEditSourcesNextBtn(driver).click();
		timer(driver);
		Connections.editPresetEditDestinationsNextBtn(driver).click();
		timer(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Connections.getCreatePresetNameTextBoxXpath());
		SeleniumActions.seleniumSendKeys(driver, Connections.getCreatePresetNameTextBoxXpath(), newName);
		Connections.createPresetCompleteBtn(driver).click();
	}

	/**
	 * Creates a new preset in connections > viewer
	 * 
	 * Navigates to connections > viewer and creates a new preset with passed in
	 * connection sources and destinations
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param sourceList
	 *            array containing the names of all the sources (connections) to add
	 *            to preset
	 * @param destinationList
	 *            array containing the names of all the destinations (receivers) to
	 *            add to the preset
	 * 
	 * @param presetName
	 *            name of the preset
	 * @param isPartial
	 *            boolean to indicate if the preset is partial or full. true for
	 *            partial
	 * 
	 * @throws InterruptedException
	 */
	public void createPreset(WebDriver driver, String[] sourceList, String[] destinationList, String presetName,
			boolean isPartial) throws InterruptedException {
		log.info("Attempting to create preset with name " + presetName);
		navigateToConnectionViewer(driver);
		timer(driver);
		Connections.managePresetsBtn(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h4[contains(.,'Manage Presets')]")));
		Connections.createCustomPresetBtn(driver).click();
		for (String s : sourceList) {
			log.info("Attempting to add source " + s);
			Connections.presetSourcesSearchBox(driver).sendKeys(s);
			SeleniumActions.seleniumClick(driver, Connections.firstItemInCreatePresetSourceList(driver, s));
			log.info("Successfully added source " + s);
			Connections.presetSourcesSearchBox(driver).clear();
		}
		Connections.presetSelectSourceNextBtn(driver).click();
		for (String d : destinationList) {
			log.info("Attempting to add destingation " + d);
			Connections.presetSelectDestinationSearchBox(driver).sendKeys(d);
			SeleniumActions.seleniumClick(driver, Connections.firstItemInCreatePresetDestinationList(driver, d));
			Connections.presetSelectDestinationSearchBox(driver).clear();
			log.info("Successfully added destination " + d);
		}
		Connections.presetDestinationNextBtn(driver).click();
		SeleniumActions.seleniumSendKeys(driver, Connections.getCreatePresetNameTextBoxXpath(), presetName);
		if (isPartial) {
			Connections.createPresetTypeDowndown(driver, "Partial");
		} else {
			Connections.createPresetTypeDowndown(driver, "Full");
		}
		Connections.createPresetCompleteBtn(driver).click();
		log.info("Successfully created preset with name " + presetName);

	}

	/**
	 * Breaks a connection in connections > viewer
	 * 
	 * Navigates to connections > viewer and clicks the x on the source with the
	 * passed in name which breaks the connections
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param name
	 *            name of the source (connection) to break
	 * 
	 * @throws InterruptedException
	 */
	public void breakConnection(WebDriver driver, String name) throws InterruptedException {
		log.info("Attempting to break connection with name " + name);
		navigateToConnectionViewer(driver);
		timer(driver);
		Connections.breakConnection(driver, name).click();
		timer(driver);
		try {
			boolean isBroken = Connections.singleSourceDestinationCheck(driver, name).isDisplayed();
			if (isBroken) {
				driver.navigate().refresh();
				timer(driver);
				Connections.breakConnection(driver, name).click();
				timer(driver);
			}

		} catch (Exception e) {
			log.info("connection broken");
		}
		log.info("Successfully broken connection " + name);
	}

	/**
	 * Saves the current connections in connections > viewer to a preset
	 * 
	 * Navigates to connections > viewer and saves the current connections as a
	 * preset with passed in name. Boolean indicates if the preset is partial or
	 * full
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param name
	 *            name of the preset to create
	 * @param isPartial
	 *            boolean to indicate if the preset is full or partial. True for
	 *            partial
	 * 
	 * @throws InterruptedException
	 */
	public void saveSnapshot(WebDriver driver, String name, boolean isPartial) throws InterruptedException {
		log.info("Saving snapshot " + name);
		navigateToConnectionViewer(driver);
		SeleniumActions.seleniumClick(driver, Connections.getSnapshotBtn());
		timer(driver);
		Connections.snapshotNameInput(driver).sendKeys(name);
		if (isPartial) {
			Connections.snapshotTypeDropdown(driver, "Partial");
		} else {
			Connections.snapshotTypeDropdown(driver, "Full");
		}
		timer(driver);
		Connections.saveSnapshotBtn(driver).click();
		timer(driver);

	}

	/**
	 * Navigates from anywhere in boxilla to connections > viewer
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void navigateToConnectionViewer(WebDriver driver) throws InterruptedException {
		log.info("Navigating to connections > viewer");
		int counter = 0;
		while (counter < 5) {
			try {
				driver.navigate().refresh();
				Thread.sleep(2000);
				SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
				log.info("Connections tab has been clicked. Checking for connections viewer link to be available");
				new WebDriverWait(driver, 60)
						.until(ExpectedConditions.elementToBeClickable(Landingpage.connectionsViewer(driver)));
				log.info("Connections viewer link is available");
				counter = 6;
			} catch (Exception e) {
				log.info(
						"Problem with clicking the connections tab button. Maybe tab did not expand. Printing stacktrace and retrying");
				e.printStackTrace();
				counter++;
			}
		}

		Landingpage.connectionsViewer(driver).click();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(Connections.makeConnectionBtn(driver)));
		log.info("Successfully navigated to connections viewer page");
	}

	/**
	 * Adds a private destination to a source in connections > viewer
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionName
	 *            name of the connection to add destination to
	 * @param destination
	 *            name of the destination to add
	 * 
	 * @throws InterruptedException
	 */
	public void addPrivateDestination(WebDriver driver, String connectionName, String destination)
			throws InterruptedException {
		log.info("Adding private destination. Connection: " + connectionName + " Destination: " + destination);
		navigateToConnectionViewer(driver);
		// sometime the button is highlighted but not clicked. Refresh the page and try
		// again
		int counter = 0;
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(By.xpath(Connections.destination(driver, connectionName))));
		while (counter < 5) {
			try {
				SeleniumActions.seleniumClick(driver, Connections.destination(driver, connectionName));
				counter = 6;
			} catch (Exception e) {
				log.info("Button sometimes gets stuck. Refreshing page and retrying");
				counter++;

			}
		}
		timer(driver);
		Connections.privateConnectionDropDown(driver, destination);
		Connections.privateConnectionsActivateBtn(driver).click();
	}

	/**
	 * Adds one or more shared destinations to a source in connections > viewer
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionName
	 *            name of the connection to add destination to
	 * @param destination
	 *            array of names of the destinations to add
	 * 
	 * @throws InterruptedException
	 */
	public void addSharedDestination(WebDriver driver, String connectionName, String[] destinations)
			throws InterruptedException {
		log.info("Adding shared destination. Connectoin: " + connectionName);
		navigateToConnectionViewer(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.destination(driver, connectionName));
		timer(driver);
		for (String s : destinations) {
			log.info("Destination : " + s);
			Connections.sharedDestinationSearchBox(driver).sendKeys(s);
			timer(driver);
			Connections.firstItemInDestination(driver, s).click();
			timer(driver);
			Connections.sharedDestinationSearchBox(driver).clear();
			timer(driver);
		}
		SeleniumActions.seleniumClick(driver, Connections.getActivateSelectedDestinationXpath());
		timer(driver);
	}

	/**
	 * Add one or more sources in connections > viewer
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connections
	 *            array holding the names of the connections to add
	 * 
	 * @throws InterruptedException
	 */
	public void addSources(WebDriver driver, String[] connections) throws InterruptedException {
		log.info("Adding Sources.");
		navigateToConnectionViewer(driver);
		Thread.sleep(10000);
//		driver.navigate().refresh();
		// for some reason sometimes these buttons get stuck and do not click in
		// selenium to refresh page and retry
		int counter = 0;
		while (counter < 5) {
			try {
				Connections.makeConnectionBtn(driver).click();
				new WebDriverWait(driver, 60)
						.until(ExpectedConditions.elementToBeClickable(Connections.activateSourcesSource(driver)));
				log.info("Make connections successfully clicked");
			} catch (Exception e) {
				log.info("make connection button did not click. Refreshing page and retrying");
				counter++;
			}
		}
		for (String s : connections) {
			log.info("Source Name: " + s);
			Connections.searchSources(driver).sendKeys(s);
			new WebDriverWait(driver, 60).until(
					ExpectedConditions.elementToBeClickable(By.xpath(Connections.firstItemInSourceList(driver, s))));
			SeleniumActions.seleniumClick(driver, Connections.firstItemInSourceList(driver, s));
			Connections.searchSources(driver).clear();
		}
		log.info("Clicking activate Sources");
		Connections.activateSourcesSource(driver).click();

	}

	/**
	 * Navigates to the connections > manage page from anywhere in boxilla
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void navigateToConnectionsManage(WebDriver driver) throws InterruptedException {
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
		log.info("Navigate to Connections > Manage : Connections Tab clicked");
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Landingpage.connectionsManage)));
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsManage);
		log.info("Navigate to Connections > Manage : Connections > Manage Tab clicked");
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(Connections.btnAddConnection))));
		log.info("Navigate to Connections > Manage : Title Assertion Executed - Success");
	}
	
	public void deleteNonUniqueConnection(WebDriver driver) throws InterruptedException 
	{
		navigateToConnectionsManage(driver);
		log.info("Navigate to the manage -> Connection page");
		List<WebElement> connectionName = driver.findElements(By.xpath("//table[@id='conntable']//tbody//td[1]"));
		 System.out.println(connectionName.size());
		for (int i = 1; i <= connectionName.size(); i++) {
			String conn=driver.findElement(By.xpath("(//table[@id='conntable']//tbody//td[1])[ "+ i +"]")).getText();
			driver.findElement(By.xpath("//input[@aria-controls='conntable']")).sendKeys(conn);
			driver.findElement(By.xpath("(//img[@src='/assets/icons/table-dropdown.svg'])[1]")).click();
			Thread.sleep(2000);
			driver.findElement(By.xpath("//div[@class='dropdown dropdown-kebab-pf open']//a[@class='connection-delete'][normalize-space()='Delete']")).click();
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(3000);
			driver.findElement(By.xpath("//input[@aria-controls='conntable']")).clear();
			driver.navigate().refresh();
		
		
	}}

	/**
	 * Creates a connection in connections > manage
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionName
	 *            name of the connection to create
	 * @param useTemplate
	 *            Indicates if connection should be created from a template. yes or
	 *            no
	 * 
	 * @throws InterruptedException
	 */
	public void addConnection(WebDriver driver, String connectionName, String useTemplate) throws InterruptedException {
		navigateToConnectionsManage(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnection);
		log.info("Adding Connection : Add Connection Button Clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Connections.useTemplateNo(driver)));
		if (useTemplate.equalsIgnoreCase("no")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.useTemplateNo(driver));
			log.info("Adding Connection : Use Template No Selected");
		} else if (useTemplate.equalsIgnoreCase("yes")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.useTemplateYes(driver));
			log.info("Adding Connection : Use Template Yes Selected");
		}
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.connectionName, connectionName);
		if (StartupTestCase.is4kCon) {
			log.info("This is a connection for 4k device so setting compression mode to optimized");
			// WebElement e = driver.findElement(By.xpath(Connections.getLosslessBtn()));
			WebElement e = driver.findElement(By.xpath(Connections.getOptimizedMode()));
			SeleniumActions.exectuteJavaScriptClick(driver, e);
		}
		log.info("Adding Connection : Connection Name Entered");
	}

	public void connectionInfo(WebDriver driver, String via, String username, String password, String hostIP)
			throws InterruptedException { // use via string from : tx , vm , vmpool, broker
		timer(driver);
		if (via.equalsIgnoreCase("TX")) {
			((JavascriptExecutor) driver).executeScript("arguments[0].checked = true;",
					SeleniumActions.getElement(driver, Connections.connectionViaTX));
			log.info("Connection Via " + via + " selected");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.hostName, hostIP);
			log.info("Adding Connection : Host Name Entered");
		} else if (via.equalsIgnoreCase("vm")) {
			SeleniumActions.seleniumClick(driver, Connections.connectionViaVM);
			log.info("Connection Via " + via + " selected");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.hostName, hostIP);
			log.info("Adding Connection : Host Name Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.usernameTextbox, username);
			log.info("Adding Connection : Username Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.passwordTextbox, password);
			log.info("Adding Connection : Password Entered");
		} else if (via.equalsIgnoreCase("vmpool")) {
			SeleniumActions.seleniumClick(driver, Connections.connectionViaVMPool);
			log.info("Connection Via " + via + " selected");
		} else if (via.equalsIgnoreCase("broker")) {
			SeleniumActions.seleniumClick(driver, Connections.connectionViaBroker);
			log.info("Connection Via " + via + " selected");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.hostName, hostIP);
			log.info("Adding Connection : Host Name Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.usernameTextbox, username);
			log.info("Adding Connection : Username Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.passwordTextbox, username);
			log.info("Adding Connection : Password Entered");
		} else if (via.equalsIgnoreCase("horizon")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.getVmHorizonViewBtn(driver));
			log.info("Connection Via " + via + " selected");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.hostName, hostIP);
			log.info("Adding Connection : Host Name Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.usernameTextbox, username);
			log.info("Adding Connection : Username Entered");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.passwordTextbox, username);
			log.info("Adding Connection : Password Entered");
		} else {
			log.info("Error in Selecting Connect Via");
			throw new SkipException("There is issue with the selecting connection");
		}
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		log.info("Adding Connection - Stage 1 Complete");
	}

	// Choose connection Private or Shared - Only for Connection via TX
	/**
	 * Chooses a connection, private or shared for TX connections
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionType
	 *            shared for shared connection, private for a private one
	 * 
	 * @throws InterruptedException
	 */
	public void chooseCoonectionType(WebDriver driver, String connectionType) throws InterruptedException {
		if (connectionType.equalsIgnoreCase("private")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.connectionTypePrivate);
			log.info("Adding Connection : Connection Type selected to Private");
		} else if (connectionType.equalsIgnoreCase("shared")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.connectionTypeShared);
			log.info("Adding Connection: Connection Type selected to Shared");
		}
	}

	// Enable Extended Desktop
	/**
	 * When creating a connection in connections > manage, sets extended desktop to
	 * on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void enableExtendedDesktop(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.extendedDesktop(driver));
		log.info("Adding Connection - Extended Desktop ON");

	}

	// Enable USB Redirection
	/**
	 * When creating a connection in connections > manage, sets USB redirection to
	 * on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void enableUSBRedirection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.usbRedirection(driver));
		log.info("Adding Connection - USB Redirection ON");
	}

	// Enable Audio
	/**
	 * When creating a connection in connections > manage, sets audio input to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void enableAudio(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.audio(driver));
		log.info("Adding Connection - Audio Input ON");
	}

	// Enable Persistent Connection
	/**
	 * Wehn creating a connection in connections > manage, sets persistent
	 * connection to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void enablePersistenConnection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.persistentConnection(driver));
		log.info("Adding Connection - Persistent Connection ON");
	}

	public void enableViewOnlyConnection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.viewOnlyConnection(driver));
		log.info("Add Connection - View Only Connection");
	}

	// Enable NLA
	/**
	 * When creating a connection via VM in connections > manage, sets NLA to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void enableNLA(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.NLA(driver));
		log.info("Adding Connection - NLA ON");
	}

	// Enter Domain name
	/**
	 * When creating a connection via VM in connections > manage, enters the domain
	 * name in the field
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param domainName
	 *            the name of the domain
	 * 
	 * @throws InterruptedException
	 */
	public void domainName(WebDriver driver, String domainName) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.domainTextbox, domainName);
		log.info("Adding Connection - Domain name Entered");
	}

	// Enter Load Balance Info
	/**
	 * When creating a connection via Broker in connections > manage, enters load
	 * balance info in the field
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param loadBalanceInfo
	 *            the load balance info
	 * 
	 * @throws InterruptedException
	 */
	public void loadBalanceInfo(WebDriver driver, String loadBalanceInfo) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.loadBalanceInfo, loadBalanceInfo);
		log.info("Adding Connection - Load Balance Info Entered");
	}

	// Click Next on Property information section : Stage 2 - Add Connections
	/**
	 * Clicks next when creating the initial connection
	 * 
	 * @param driver
	 * 
	 * @throws InterruptedException
	 */
	public void propertyInfoClickNext(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		log.info("Adding Connection - Stage 2 completed");
	}

	public void propertyInfoClickNextWithTemplate(WebDriver driver, String templateName, VIA via)
			throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumDropdown(driver, Connections.getPropertyTemplate(via), templateName);
		SeleniumActions.seleniumClick(driver, Connections.btnNext);
		// Connections.btnNext(driver).click();
		log.info("Adding Connection - Stage 2 completed");
	}

	/**
	 * Checks the connections > manage table for a specific connection. Throws an
	 * assert error if connection is not found
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionName
	 *            the name of the connection to search for
	 * 
	 * @throws InterruptedException
	 */
	public void checkConnectionExists(WebDriver driver, String connectionName) throws InterruptedException {
//		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsManage);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionName),
				"Connection table text did not contain: " + connectionName + ", actual text: " + connectionTableText); // Asserting
																														// connection
																														// name
																														// present
																														// in
																														// connection
																														// table
	}
	
	
	
//	public void deleteConnection(WebDriver driver,String connectionName) throws InterruptedException 
//	{
//		navigateToConnectionsManage(driver);
//		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
//		SystemAll.backupRestoreTab(driver).click();
//		
//		
//		
//		
//	}

	// Save (Add) Connection
	/**
	 * Saves a connection in connections > manage
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionName
	 *            the name of the connection to save
	 * 
	 * @throws InterruptedException
	 */
	public String saveConnection(WebDriver driver, String connectionName) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		log.info("Adding Connection - Save button Clicked.. Asserting connection");
		timer(driver);
		String toastMessage = Users.notificationMessage(driver).getText();
		SeleniumActions.takeScreenshot(driver);
		Assert.assertTrue( // Asserting toast message
				toastMessage.contains("Successfully created"),
				"Toast Message did not contain: Successfully created, actual text: " + toastMessage);
		SeleniumActions.refreshPage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionName),
				"Connection table text did not contain: " + connectionName + ", actual text: " + connectionTableText); // Asserting
																														// connection
																														// name
																														// present
																														// in
																														// connection
																														// table
		log.info("Adding Connection - Assertion Completed. Successfully Created Connection");
		return connectionTableText;
	}

	public String saveConnection(WebDriver driver, String connectionName, String zoneName) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		log.info("Adding Connection - Save button Clicked.. Asserting connection");
		timer(driver);
		String toastMessage = Users.notificationMessage(driver).getText();
		SeleniumActions.takeScreenshot(driver);
		Assert.assertTrue( // Asserting toast message
				toastMessage.contains("Successfully created"),
				"Toast Message did not contain: Successfully created, actual text: " + toastMessage);
		SeleniumActions.refreshPage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionName),
				"Connection table text did not contain: " + connectionName + ", actual text: " + connectionTableText); // Asserting
																														// connection
																														// name
																														// present
																														// in
																														// connection
																														// table

		if (zoneName != null || !zoneName.equals("")) {
			Assert.assertTrue(connectionTableText.contains(zoneName), "Zone name was not in table data");
		}
		log.info("Adding Connection - Assertion Completed. Successfully Created Connection");
		return connectionTableText;
	}

	/**
	 * Saves a connection after it has been made
	 * 
	 * @param driver
	 * @param connectionName
	 * @param zoneName
	 * @param isLossless
	 * @return
	 * @throws InterruptedException
	 */
	public String saveConnection(WebDriver driver, String connectionName, String zoneName, boolean isLossless)
			throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnSave);
		log.info("Adding Connection - Save button Clicked.. Asserting connection");
		timer(driver);
		String toastMessage = Users.notificationMessage(driver).getText();
		SeleniumActions.takeScreenshot(driver);
		Assert.assertTrue( // Asserting toast message
				toastMessage.contains("Successfully created"),
				"Toast Message did not contain: Successfully created, actual text: " + toastMessage);
		SeleniumActions.refreshPage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
		timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(connectionName),
				"Connection table text did not contain: " + connectionName + ", actual text: " + connectionTableText); // Asserting
																														// connection
																														// name
																														// present
																														// in
																														// connection
																														// table

		if (zoneName != null) {
			if (!zoneName.equals(""))
				Assert.assertTrue(connectionTableText.contains(zoneName), "Zone name was not in table data");
		}

		if (isLossless) {
			Assert.assertTrue(connectionTableText.contains("Lossless"), "Connection was not lossless");
		} else {
			Assert.assertTrue(connectionTableText.contains("Optimized"), "Connection was not optimized");
		}
		log.info("Adding Connection - Assertion Completed. Successfully Created Connection");
		return connectionTableText;
	}

	// Add Connection Template - Enable Extended Desktop
	/**
	 * When creating a connection template in connections > manage, sets extended
	 * desktop to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateEnableExtendedDesktop(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateExtendedDesktop(driver));
		log.info("Adding Connection - Extended Desktop ON");
	}

	// Add Connection Template - Enable USB Redirection
	/**
	 * When creating a connection template in connections > manage, sets USB
	 * redirection to on
	 * 
	 * @param driverwebdriver
	 *            to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateEnableUSBRedirection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateUsbRedirection(driver));
		log.info("Adding Connection - USB Redirection ON");
	}

	// Add Connection Template - Enable Audio
	/**
	 * When creating a connection template in connections > manage, sets Audio input
	 * to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateEnableAudio(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateAudio(driver));
		log.info("Adding Connection - Audio Input ON");
	}

	// Add Connection Template - Enable Persistent Connection
	/**
	 * When creating a connection template in connections > manage, sets Persistent
	 * connection to on
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateEnablePersistenConnection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplatePersistentConnection(driver));
		log.info("Adding Connection - Persistent Connection ON");
	}

	public void addTemplateViewOnlyConnection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateViewOnly(driver));
	}

	// Add Connection Template - Enable NLA
	/**
	 * When creating a connection template in connections > manage, sets NLA to on
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void addTemplateEnableNLA(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateNLA(driver));
		log.info("Adding Connection - NLA ON");
	}

	// using strings for dataprovider
	public void addPairConnectionTemplate(WebDriver driver, String templateName, String shared, String targets,
			Connections.Orientation orientation, String audio, String audio2, String persistent, String viewOnly)
			throws InterruptedException {
		navigateToConnectionsManage(driver);
		log.info("Creating paired connection template");
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnectionTemplate);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Connections.addTemplateSavebtn)));
		SeleniumActions.seleniumClick(driver, Connections.getTemplateTxPairButton());
		SeleniumActions.seleniumSendKeys(driver, Connections.templateName, templateName);
		if (targets.equals("1")) {
			log.info("Do nothing. 1 target already selected");
		} else {
			WebElement target2 = driver.findElement(By.xpath(Connections.getTemplatePairedTarget2()));
			SeleniumActions.exectuteJavaScriptClick(driver, target2);
			setDisplayOrientation(driver, orientation, true);
		}
		if (shared.equals("true"))
			SeleniumActions.seleniumClick(driver, Connections.addTemplateShared);

		if (audio.equals("true")) {
			addTemplateEnableAudio(driver);
			if (audio2.equals("true")) {
				WebElement ip1Audio = driver.findElement(By.xpath(Connections.getTemplateAudioIp1()));
				SeleniumActions.exectuteJavaScriptClick(driver, ip1Audio);
			}
		}
		if (persistent.equals("true"))
			addTemplateEnablePersistenConnection(driver);

		if (viewOnly.equals("true"))
			addTemplateViewOnlyConnection(driver);

		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toast = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toast.contains("Template successfully created"),
				"Toast message did not contain 'Template successfully created' , actual:" + toast);
	}

	// Add Connection Template - use via string from : tx , vm , vmpool, broker
	/**
	 * Creates a connection template in connections > manage
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param via
	 *            connection type. tx, vm, vmpool or broker
	 * @param templateName
	 *            name of the template
	 * 
	 * @throws InterruptedException
	 */
	public void addConnectionTemplate(WebDriver driver, String via, String templateName) throws InterruptedException {
		navigateToConnectionsManage(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnectionTemplate);
		log.info("Adding Connection Tempalte : Add Connection Template Button Clicked");
		timer(driver);
		if (via.equalsIgnoreCase("tx")) {
			new WebDriverWait(driver, 60).until(
					ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath(Connections.addTemplateTX))));
			WebElement e = driver.findElement(By.xpath(Connections.addTemplateTX));
			SeleniumActions.exectuteJavaScriptClick(driver, e);
			log.info("Connect via " + via + " selected");
		} else if (via.equalsIgnoreCase("broker")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateBroker(driver));
			log.info("Connect via " + via + " selected");
		} else if (via.equalsIgnoreCase("vm")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateVM(driver));
			log.info("Connect via " + via + " selected");
		} else if (via.equalsIgnoreCase("vmpool")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.addTemplateVMPool(driver));
			log.info("Connect via " + via + " selected");
		} else if (via.equalsIgnoreCase("horizon")) {
			SeleniumActions.exectuteJavaScriptClick(driver, Connections.getVmHorizonTemplate(driver));
		} else {
			log.info("Adding Template: Error in Selecting Connect Via");
			throw new SkipException("** Adding Connection Template : There is issue with the selecting connect via **");
		}
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.templateName, templateName);
		log.info("Adding Connection Template : Template Name entered");
	}

	// Add Template - Choose connection Private or Shared - Only for Connect via TX
	/**
	 * When creating a connection template in connections > manage, sets the
	 * connection to private or shared. TX only
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param connectionType
	 *            sets the connection type. shared or private
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateChooseConnectionType(WebDriver driver, String connectionType) throws InterruptedException {
		if (connectionType.equalsIgnoreCase("private")) {
			timer(driver);
			WebElement e = driver.findElement(By.xpath(Connections.addTemplatePrivate));
			SeleniumActions.exectuteJavaScriptClick(driver, e);
			log.info("Adding Connection : Connection Type selected to Private");
		} else if (connectionType.equalsIgnoreCase("shared")) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.addTemplateShared);
			log.info("Adding Connection: Connection Type selected to Shared");
		}
	}

	// Save (Add) Connection Template
	/**
	 * Saves a connection template
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * 
	 * @throws InterruptedException
	 */
	public void saveConnectionTemplate(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnAddConnectionTemplate);
		log.info("Adding Connection Template - Save Template button Clicked.. Asserting connection");
		timer(driver);
		String toastMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue( // Asserting toast message
				toastMessage.contains("Template successfully created."),
				"Toast message did not contain: Template successfully created, actual text: " + toastMessage);
		log.info("Adding Connection - Assertion Completed. Successfully Created Connection Template");
	}

	// Add Template - Enter Domain name
	/**
	 * When creating a connection template, enters the domain name
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param domainName
	 *            the name of the domain
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateDomainName(WebDriver driver, String domainName) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.addTemplateDomainTextbox, domainName);
		log.info("Adding Connection Template - Domain name Entered");
	}

	// Add Template - Enter Load Balance Info
	/**
	 * When adding a template, enters the load balancer info
	 * 
	 * @param driver
	 *            webdriver to drive the browser interaction
	 * @param loadBalanceInfo
	 *            load balancer information
	 * 
	 * @throws InterruptedException
	 */
	public void addTemplateLoadBalanceInfo(WebDriver driver, String loadBalanceInfo) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.addTemplateLoadBalanceInfo, loadBalanceInfo);
		log.info("Adding Connection Template - Load Balance Info Entered");
	}

	// Delete Connection Template Initiate
	/**
	 * Navigates to connections > manage and clicks the delete button
	 * 
	 * @param driver
	 * 
	 * @throws InterruptedException
	 */
	public void deleteTemplate(WebDriver driver) throws InterruptedException { // Delete User Template - Initiate
		navigateToConnectionsManage(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnDeleteConnectionTemplate);
		log.info("Delete Connection Template : Delete Connection Template Button Clicked");
	}

	/**
	 * Deletes selected template
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deleteSelectedTemplate(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.btnDeleteTemplates);
		String toastMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(toastMessage.contains("Successfully deleted"),
				"Toast message did not contain: Successfully deleted, actual text: " + toastMessage);
		log.info("Template Deleted Successfully");
	}

	/**
	 * Pauses execution. Should not use. Please use explicit wait instead
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	@Deprecated
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(StartupTestCase.getWaitTime(), TimeUnit.SECONDS);
	}

	/**
	 * Navigates to connections > groups
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToGroups(WebDriver driver) throws InterruptedException {
		// timer(driver);
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Landingpage.getConnectionGroupLink())));
		log.info("Connections Group - Clicked on Connections Tab");
		// timer(driver);
		Landingpage.groupsTab(driver).click();
		log.info("Connections Group - Clicked on Groups tab");
	}

	public void selectTemplateForConnection(WebDriver driver, String templateName, String type, String connectionName)
			throws InterruptedException {
		Select templateList = new Select(Connections.connectionTemplateDropdown(driver, type));
		templateList.selectByVisibleText(templateName);
		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName);
	}

	/**
	 * Adds a connection group to a group name
	 * 
	 * @param driver
	 * @param groupName
	 * @throws InterruptedException
	 */
	public void addConnectionGroup(WebDriver driver, String groupName) throws InterruptedException {
		navigateToGroups(driver);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.newGroupBtn);
		log.info("Add Connections Group - Clicked on add Group button");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.groupName, groupName);
		log.info("Add Connections Group- Group Name Entered");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.groupDescription, groupName + " description");
		log.info("Add Connections Group - Group Description added");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.groupAddBtn);
		log.info("Add Connections Group - Add button clicked");
		timer(driver);
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Successfully added group"),
				"Notification message did not contain: Successfully added group, actual text: " + notificationMessage);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupName);
		String connectionGroupTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionGroupTable);
		Assert.assertTrue(connectionGroupTableText.contains(groupName),
				"Connection group table did not contain: " + groupName + ", actual text: " + connectionGroupTableText);
		SeleniumActions.seleniumSendKeysClear(driver, Connections.searchTextbox);
		timer(driver);
		log.info("Add Connections Group - " + groupName + " added successfully");
	}
	public void dissolveConnectionGroup(WebDriver driver, String groupName) throws InterruptedException 
	{
		navigateToGroups(driver); // Navigate to Connections > Groups
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupName);
		log.info("Dissolve Group - Group name entered in searchbox");
		if (SeleniumActions.seleniumGetText(driver, Connections.connectionGroupTable).contains(groupName)) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
			log.info("Dissolve Group - Breadcrumb clicked from searched group");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.dissolveBtn);
			log.info("Dissolve Group - Clicked on Dissolve button");
			Alert alert = driver.switchTo().alert();
			alert.accept();
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully dissolved group"),
					"Notification message did not cotain: Successfully dissolved group, actual text: "
							+ notificationMessage);
			log.info("Dissolve Group - Notification message Asserted");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupName);
			log.info("Dissolve Group - Group name entered in search box to check if group dissolved successfully");
			// Assert if group name is disappeared from connection group table
			String connectionGroupTable = SeleniumActions.seleniumGetText(driver, Connections.connectionGroupTable);
			Assert.assertFalse(connectionGroupTable.contains(groupName),
					"Group table contained: " + groupName + ", actual text: " + connectionGroupTable);
			log.info("Dissolve Group - Group not present in the Group table");
		} else {
			log.info("Dissolve Group - Searched Group not found");
			throw new SkipException("***** Searched Group not found *****");
		}	
		
		
	}

	// Use connection name to filter connection to manage tx , vm, vmpool, broker
	/**
	 * Adds a connection to the passed in group
	 * 
	 * @param driver
	 * @param connectionName
	 * @param groupname
	 * @throws InterruptedException
	 */
	public void addConnectionToSelectedGroup(WebDriver driver, String connectionName, String groupname)
			throws InterruptedException {
		navigateToManageConnection(driver, groupname);
		SeleniumActions.seleniumSendKeys(driver, Connections.connectionListFilterBox, connectionName);
		timer(driver);
		Select options = new Select(SeleniumActions.getElement(driver, Connections.nonSelectedActiveConnectionList));
		int connectionListSize = options.getOptions().size();
		if (connectionListSize > 0) { // Add all Connection, if Connection available to add
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.moveAllBtn);
			log.info("Groups > Manage Connections - All Connection moved to Belongs to Group Box");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.saveBtnGroupConnections);
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully added connections to group"),
					"Notificaion messgae did not contain: Successfully added connections to group, actual text: "
							+ notificationMessage);
			log.info("Groups > Manage Connections - Connection Managed successfully");
		} else { // if no connection available, exit test and mark test case -failure
			throw new SkipException("****** Sufficient connection not available to add in to group ********");
		}
	}

	// Edit name of first group present in the table
	/**
	 * Edit group name in connetions > group
	 * 
	 * @param driver
	 * @param newName
	 * @throws InterruptedException
	 */
	public void editGroupName(WebDriver driver, String newName) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		log.info("Groups > Edit - Breadcrumb clicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.editGroup);
		log.info("Group > Edit - Edit tab clicked");
		timer(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Connections.groupName);
		log.info("Group > Edit - Cuurent name cleared");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.groupName, newName);
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.groupUpdateBtn);
		timer(driver);
		String notificationMessage = Users.notificationMessage(driver).getText();
		// Assert check - Successful name edit
		Assert.assertTrue(notificationMessage.contains("Successfully edited group"),
				"Notification message did not contain: Successfully edited group, actual text: " + notificationMessage);
		timer(driver);
	}

	// Delete first group present in the group table
	/**
	 * Delete group in connections > group
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deleteGroup(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		log.info("Groups > Delete - Breadcrumb clicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.groupDelete);
		log.info("Groups > Delete - Delete Button clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Successfully deleted group"),
				"Notification message did not contain: Successfully deleted group, actual text: "
						+ notificationMessage);
	}

	// This navigates to Manage Connections option under group
	/**
	 * Navigates to manage connections option under passed in groupname
	 * 
	 * @param driver
	 * @param groupname
	 * @throws InterruptedException
	 */
	public void navigateToManageConnection(WebDriver driver, String groupname) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupname);
		log.info("Groups > Manage Connections - Group name entered in filter box");
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		log.info("Groups > Manage Connections - Breadcrumb clicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.groupManageConnection);
		log.info("Groups > Manage Connections - Manage Connections clicked");
		timer(driver);
	}

	// Remove connection from single connection - First in the list
	public void removeSingleConnection(WebDriver driver) throws InterruptedException {
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		log.info("Remove Single Connection - Breadcrumb clicked in first element");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Connections.groupManageConnection);
		log.info("Remove Single Connection - Manage Connection tab clicked");
		timer(driver);
		Select options = new Select(SeleniumActions.getElement(driver, Connections.selectedActiveConnectionList));
		if (options.getOptions().size() > 0) {
			options.selectByIndex(0); // select first connection in the table
			log.info("Remove Single Connection - First connection from list is selected");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.removeSelectedBtn);
			log.info("Remove Single Connection - Remove single connection button clicked");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.saveBtnGroupConnections);
			log.info("Remove Single Connection - Save button clicked");
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully added connections to group"),
					"Notification message did not contain: Successfully added connections to group, actual text: "
							+ notificationMessage);
			log.info("Remove Single Connection - Assertion check complete");
		} else {
			log.info("Connection List is empty.. Skipping test case..");
			throw new SkipException("Skipping test case - There isn't any connection present in the list to add.");
		}
	}

	/**
	 * Deletes a connection from Boxilla and verifies connection was removed
	 * 
	 * @param driver
	 * @param name
	 *            - The name of the connection to delete
	 * @throws InterruptedException
	 */
	public void deleteConnection(WebDriver driver, String name) throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, name);
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		SeleniumActions.seleniumClick(driver, Connections.getDeleteConnection());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Connections.templateToastMessage)));
		String toast = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		Assert.assertTrue(toast.equals("Successfully deleted connection."),
				"toast message did not contain successfully deleted connection. Actual:" + toast);
	}

	/**
	 * Edits the zone the connection is part of
	 * 
	 * @param driver
	 * @param connectionName
	 *            - Name of connection to edit
	 * @param zoneName
	 *            - Name of zone to edit
	 * @throws InterruptedException
	 */
	public void editConnectionZone(WebDriver driver, String connectionName, String zoneName)
			throws InterruptedException {
		navigateToConnectionsManage(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, connectionName);
		SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
		SeleniumActions.seleniumClick(driver, Connections.getConnectionDropdownEdit());
		SeleniumActions.seleniumDropdown(driver, Connections.getZone(), zoneName);
		propertyInfoClickNext(driver);
		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName, zoneName);
	}

	/**
	 * Adds a connection group to a user
	 * 
	 * @param driver
	 * @param username
	 *            - The name of the user to add the group to
	 * @param groupname
	 *            - the name of the group to add to the user
	 * @throws InterruptedException
	 */
	public void addGroupToUser(WebDriver driver, String username, String groupname) throws InterruptedException {
		usermethods.navigateToUsersManage(driver); // navigate to Users > Manage
		timer(driver);
		Users.searchbox(driver).sendKeys(username);
		log.info("Dissolve Group - Username enteted in serach box");
		timer(driver);
		if (Users.usersTable(driver).getText().contains(username)) {
			Users.userBreadCrumb(driver, username).click();
			log.info("Dissolve Group - User breadcrumb clicked");
			timer(driver);
			Users.userManageGroupTab(driver, username).click();
			log.info("Dissolve Group - Manage Group tab clicked");
			timer(driver);
			Users.groupFilterBox(driver).sendKeys(groupname);
			log.info("Dissolve Group - Groupname entered in filterbox");
			if (Users.nonSelectedGroupList(driver).getText().contains(groupname)) {
				timer(driver);
				Users.moveAllConnection(driver).click();
				log.info("Dissolve Group - All searched group moved to Selected Group table");
				timer(driver);
				Users.btnSaveGroup(driver).click();
				log.info("Dissolve Group - Save button clicked");
				timer(driver);
				String notificationMesage = Users.notificationMessage(driver).getText();
				Assert.assertTrue(notificationMesage.contains("Successfully added user to group"),
						"Notification message did not contain: Successfully added user to group, actual text: "
								+ notificationMesage);
				log.info("Dissolve Group - Notification asserted.. Group Added Successfully");
			} else {
				log.info("Searched group not found in non-selected list");
				throw new SkipException("***** Seached group not found in the list *****");
			}
		} else {
			log.info("Searched user not found");
			throw new SkipException("***** Searched user not found *****");
		}
	}

	/**
	 * Dissolves a connection group
	 * 
	 * @param driver
	 * @param username
	 * @param groupname
	 * @throws InterruptedException
	 */
	public void dissolveGroup(WebDriver driver, String username, String groupname) throws InterruptedException {
		navigateToGroups(driver); // Navigate to Connections > Groups
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupname);
		log.info("Dissolve Group - Group name entered in searchbox");
		if (SeleniumActions.seleniumGetText(driver, Connections.connectionGroupTable).contains(groupname)) {
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.breadCrumb);
			log.info("Dissolve Group - Breadcrumb clicked from searched group");
			timer(driver);
			SeleniumActions.seleniumClick(driver, Connections.dissolveBtn);
			log.info("Dissolve Group - Clicked on Dissolve button");
			Alert alert = driver.switchTo().alert();
			alert.accept();
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully dissolved group"),
					"Notification message did not cotain: Successfully dissolved group, actual text: "
							+ notificationMessage);
			log.info("Dissolve Group - Notification message Asserted");
			timer(driver);
			SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, groupname);
			log.info("Dissolve Group - Group name entered in search box to check if group dissolved successfully");
			// Assert if group name is disappeared from connection group table
			String connectionGroupTable = SeleniumActions.seleniumGetText(driver, Connections.connectionGroupTable);
			Assert.assertFalse(connectionGroupTable.contains(groupname),
					"Group table contained: " + groupname + ", actual text: " + connectionGroupTable);
			log.info("Dissolve Group - Group not present in the Group table");
			usermethods.navigateToUsersManage(driver);
			timer(driver);
			Users.searchbox(driver).sendKeys(username);
			log.info("Dissolve Group - Username enteted in serach box");
			if (Users.usersTable(driver).getText().contains(username)) {
				log.info("Dissolve Group - Searched user found");
				timer(driver);
				Users.userBreadCrumb(driver, username).click();
				log.info("Dissolve Group - User breadcrumb clicked");
				timer(driver);
				Users.userManageConnectionTab(driver, username).click();
				log.info("Dissolve Group - Users > Manage Connection tab clicked");
				timer(driver);
				String selectedActiveConnectionList = Users.selectedActiveConnectionList(driver).getText();
				Assert.assertTrue(selectedActiveConnectionList.contains("tx"),
						"Selected Active Connection List did not contain: tx, actual text: "
								+ selectedActiveConnectionList);
				log.info("Dissolve Group - Connection assigned to user");
			} else {
				log.info("Dissolve Group - Searched user not found");
				throw new SkipException("***** Searched user not found *****");
			}
		} else {
			log.info("Dissolve Group - Searched Group not found");
			throw new SkipException("***** Searched Group not found *****");
		}
	}

	/**
	 * Navigate to the active connection page from anywhere in Boxilla
	 * 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToActiveConnection(WebDriver driver) throws InterruptedException {
		DashboardMethods dash = new DashboardMethods();
		dash.navigateToDashboard(driver);
		log.info("Navigating to Active Connections");
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(Landingpage.connectionsActive(driver)));
		Landingpage.connectionsActive(driver).click();
		Thread.sleep(5000);
		new WebDriverWait(driver, 60).until(ExpectedConditions
				.presenceOfElementLocated(By.xpath(ActiveConnectionElements.numberOfDevicesOnlineXpath)));
		log.info("Successfully navigated to Active Connections");

	}

	/**
	 * Create a basic TX connection
	 * 
	 * @param connectionName
	 * @param connectionType
	 * @param driver
	 * @param txIp
	 * @throws InterruptedException
	 */
	public void createTxConnection(String connectionName, String connectionType, WebDriver driver, String txIp)
			throws InterruptedException {
		log.info("Creating connection : " + connectionName);
		addConnection(driver, connectionName, "no"); // connection name, user template
		connectionInfo(driver, "tx", "user", "", txIp); // connection via, name, host ip
		Thread.sleep(3000);
		chooseCoonectionType(driver, connectionType); // connection type
		enableExtendedDesktop(driver);
		enablePersistenConnection(driver);
		if (connectionType.equals("private")) {
			enableUSBRedirection(driver);
			enableAudio(driver);
		}
		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName);
	}

	/**
	 * Create a basic VM connection
	 * 
	 * @param connectionName
	 * @param vmUserName
	 * @param vmPassword
	 * @param vmIp
	 * @param domainName
	 * @param driver
	 * @throws InterruptedException
	 */
	public void createVmRdpConnection(String connectionName, String vmUserName, String vmPassword, String vmIp,
			String domainName, WebDriver driver) throws InterruptedException {
		addConnection(driver, connectionName, "no");
		connectionInfo(driver, "vm", vmUserName, vmPassword, vmIp);
		domainName(driver, domainName);
		enableExtendedDesktop(driver);
		enableUSBRedirection(driver);
		enableAudio(driver);
		enableNLA(driver);
		propertyInfoClickNext(driver);
		saveConnection(driver, connectionName);
	}

	public void createRealConnection(String connectionName, String userName, String password, String ipAddress,
			String txIpaddress) {
		if (connectionName.equals(""))
			connectionName = "Test_TX_Registry";
		Ssh shell = new Ssh(userName, password, ipAddress);
		shell.loginToServer();
		String output = "";

		String e2 = shell.sendCommand("e2_read");
		log.info("E2_Read:" + e2);
		if (!e2.contains("Information")) {
			output = shell.sendCommand("/usr/bin/dfreerdp -C '" + connectionName
					+ "' -U 'admin' -u demo -p 'cloud' -g 1920x1080 --dfb:no-banner --dfb:mode=1920x1080 -a 32 -x l --rfx --ignore-certificate --no-tls --no-nla --composition --no-osb --no-bmp-cache --plugin rdpsnd --data alsa:hw:0,0 feedback:0 -- --plugin drdynvc --data rdpeusb -- --inactive 0 --hotkey 0 "
					+ txIpaddress + "&");
		} else {
			output = shell.sendCommand("PATH=/bin:/sbin:/usr/bin:/usr/sbin ; " + "/usr/bin/bbfreerdp -C '"
					+ connectionName
					+ "' -U 'admin' -u demo -p 'cloud' -g 1920x1080 --dfb:no-banner --dfb:mode=1920x1080 -a 32 -x l --rfx --ignore-certificate --no-tls --no-nla --composition --no-osb --no-bmp-cache --plugin rdpsnd --data alsa:hw:0,0 feedback:0 -- --plugin drdynvc --data rdpeusb -- --inactive 0 --hotkey 0 "
					+ txIpaddress + " > /usr/local/bbrdp.log 2>&1 &");
		}
		try {
			Thread.sleep(10000);
			shell.disconnect();
			log.info("Output from freedrdp script: " + output);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
