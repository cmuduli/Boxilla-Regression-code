package methods;

import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;

import extra.SeleniumActions;
import extra.StartupTestCase;
import objects.Devices;
import objects.DkmElements;
import objects.Landingpage;
import objects.SystemAll;
import objects.Users;

/**
 * Class contains all methods for interacting with Boxilla DKM
 * @author Boxilla
 *
 */
public class DkmMethods {

	final static Logger log = Logger.getLogger(DkmMethods.class);
	/**
	 * Please do not use this method. It is bad practice to add hard coded waits. Use implicit 
	 * wait instead
	 * @param driver
	 * @throws InterruptedException
	 */
	@Deprecated
	public void timer(WebDriver driver) throws InterruptedException { // Method for thread sleep
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(StartupTestCase.getWaitTime(), TimeUnit.SECONDS);
	}

	/**
	 * From anywhere in boxilla, navigate to DKM > Switches
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToDkmSwitches(WebDriver driver) throws InterruptedException {
		timer(driver);
		Landingpage.dkmTab(driver).click();
		log.info("DKM tab clicked");
		timer(driver);
		Landingpage.switchesTab(driver).click();
		log.info("Switches tab clicked");
		timer(driver);
		String title = driver.getTitle();
		Assert.assertTrue(title.contains("Boxilla - DKM | Devices"),
				"Title did not contain: Boxilla - DKM | Devices, actual text: " + title);
	}

	/**
	 * Adds a new DKM switch
	 * @param driver
	 * @param switchIP IP address of the switch
	 * @param switchName name of the switch
	 * @throws InterruptedException
	 */
	public void addDkmSwitch(WebDriver driver, String switchIP, String switchName) throws InterruptedException {
		/* Add DKM switch, search using switch IP address.. if present skip the test, if not add the switch */
		timer(driver);
		DkmElements.searchBox(driver).clear();
		DkmElements.searchBox(driver).sendKeys(switchIP);
		log.info("Device IP entered in Search box");
		if (!DkmElements.switchesTable(driver).getText().contains(switchIP)) {
			timer(driver);
			DkmElements.addSwitchBtn(driver).click();
			log.info("Add Switch Button Clicked");
			timer(driver);
			DkmElements.switchName(driver).clear();
			DkmElements.switchName(driver).sendKeys(switchName);
			log.info("Switch Name Entered");
			timer(driver);
			DkmElements.switchIpAddress(driver).clear();
			DkmElements.switchIpAddress(driver).sendKeys(switchIP);
			log.info("Switch IP address entered");
			timer(driver);
			DkmElements.applyBtn(driver).click();
			log.info("Adding Switch - Apply button clicked");
			Alert alert = driver.switchTo().alert();
			alert.accept();
			log.info("Alert accepted : Are you sure you want to add this switch?");
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully Created Switch"),
					"Notification Message did not contain: Successfully Created Switch, actual text: " + notificationMessage);
			log.info("Success notification asserted.. Asserting switch in the Switches Table..");
			timer(driver);
			driver.navigate().refresh();
			DkmElements.searchBox(driver).sendKeys(switchIP);
			timer(driver);
			String switchesTable = DkmElements.switchesTable(driver).getText();
			Assert.assertTrue(switchesTable.contains(switchIP),
					"Switches table did not contain: " + switchIP + ", actual text: " + switchesTable);
			log.info("Assertion Complete.. Switch Added Successfully");

			//check if switch is online
			int timer = 0;
			int limit = 12;			//12 iterations of 5 seconds = 1 minute
			while(timer  <= limit) {
				log.info("Chceking if switch is online");
				if(DkmElements.switchesTable(driver).getText().contains("OnLine")) {
					log.info("Switch is online");
					break;
				}else if(timer < limit) {
					log.info("Switch is offline. Rechecking..");
					timer++;
					driver.navigate().refresh();
					Thread.sleep(5000);
				}else if (timer == limit) {
					throw new SkipException("Switch did not come online in" + limit + " seconds..Skipping");
				}
			}
			log.info("DKM-Switch Added - Test Case-94 Completed");
		} else if (DkmElements.switchesTable(driver).getText().contains(switchIP)) {
			log.info("Switch already added in table.. Deleting Switch");
			deleteSwitch(driver, switchIP);
			log.info("Switch Deleted.. Trying to add Switch again.. ");
			addDkmSwitch(driver, switchIP, switchName);
			timer(driver);
		}
	}

	/**
	 * Delete a DKM switch with the given ip address
	 * @param driver
	 * @param switchIP The IP address of the switch to delete
	 * @throws InterruptedException
	 */
	public void deleteSwitch(WebDriver driver, String switchIP) throws InterruptedException {
		/* Search using Switch IP address
		 * If switch present delete Switch else skip test case */
		timer(driver);
		navigateToDkmViewer(driver);
		detachConnctions(driver);
		timer(driver);
		navigateToDkmSwitches(driver);
		DkmElements.searchBox(driver).clear();
		DkmElements.searchBox(driver).sendKeys(switchIP);
		log.info("Device IP entered in Search box");
		timer(driver);
		if (DkmElements.switchesTable(driver).getText().contains(switchIP)) {
			DkmElements.switchKebab(driver).click();
			log.info("Switch Kebab Dropdown Clicked");
			timer(driver);
			DkmElements.deleteBtn(driver).click();
			log.info("Delete button clicked");
			Alert alert = driver.switchTo().alert();
			alert.accept();
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully deleted switch"),
					"Notification Message did not contain: Successfully deleted switch, actual text: " + notificationMessage);
			log.info("DKM-Switch Deleted");
		} else if (!DkmElements.switchesTable(driver).getText().contains(switchIP)) {
			log.info("Searched switch " + switchIP + " is not present in table");
			throw new SkipException("***** " + switchIP + " is not presentSkipping test *****");
		}
	}

	/**
	 * Remove all connections from DKM > viewer
	 * @param driver
	 * @throws InterruptedException
	 */
	public void detachConnctions(WebDriver driver) throws InterruptedException {
		// Get Detach counts
		timer(driver);
		int detachCount = driver.findElements(By.xpath(".//*[@class='active-source']//*[@class='detach']")).size();
		// detaching all connection if any connection present

		while (detachCount != 0) {
			log.info("There are " + detachCount + " connections to detach.. Detaching connection...");
			timer(driver);
			DkmElements.sourceDetachBtn(driver).click();
			timer(driver);
			driver.navigate().refresh();
			log.info("Page Refreshed");
			detachCount = driver.findElements(By.xpath(".//*[@class='active-source']//*[@class='detach']")).size();
		}
		timer(driver);
		int newDetachCount = driver.findElements(By.xpath(".//*[@class='active-source']//*[@class='detach']")).size();
		Assert.assertTrue(newDetachCount == 0, "***** Error in Detaching Connections *****");
	}

	/**
	 * Add a soruce in DKM > Viewer
	 * @param driver
	 * @param sourceName
	 * @throws InterruptedException
	 */
	public void addSource(WebDriver driver, String sourceName) throws InterruptedException {
		/* Add source and Assert if source is added in Active connection table */
		timer(driver);
		DkmElements.makeConnectionBtn(driver).click();
		log.info("Make Connection Button Clicked");
		timer(driver);
		DkmElements.searchSource(driver).sendKeys(sourceName);
		log.info(sourceName + " Entered in search box");
		timer(driver);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DkmElements.searchedElement(driver, sourceName));
		DkmElements.searchedElement(driver, sourceName).click();
		log.info(sourceName + " selected from searched result");
		timer(driver);
		DkmElements.activateSelectedBtnSource(driver).click();
		log.info("Activate Seletectecd button clicked");
		timer(driver);
		String activeConnectionTable = DkmElements.activeConnectionTable(driver).getText();
		Assert.assertTrue(activeConnectionTable.contains(sourceName),
				"Active Connection Table did not contain: " + sourceName + ", actual text: " + activeConnectionTable);
	}

	/**
	 * Add a destination to a source in DKM > viewer
	 * @param driver
	 * @param sourceName
	 * @param destinationName
	 * @throws InterruptedException
	 */
	public void addDestination(WebDriver driver, String sourceName, String destinationName)
			throws InterruptedException {
		/* Add Destination to the Active Source already added
		 * Pre-condition : Webdriver should be on DKM > Viewer page and 
		 * there should be active source with the name passed in argument */
		timer(driver);
		DkmElements.addDestination(driver, sourceName).click();
		log.info("Add Destination button clicked");
		timer(driver);
		DkmElements.searchDestination(driver).sendKeys(destinationName);
		log.info(destinationName + " entered in destination searched box");
		timer(driver);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", DkmElements.searchedElement(driver, destinationName));
		DkmElements.searchedElement(driver, destinationName).click();
		log.info(destinationName + " selected from searched result");
		timer(driver);
		DkmElements.activateSelectedBtnDestination(driver).click();
		log.info("Activate button clicked");
		timer(driver);
		assertUI(driver, sourceName);
	}

	/**
	 * Navigate from anywhere in boxilla to DKM > viewer
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToDkmViewer(WebDriver driver) throws InterruptedException {
		timer(driver);
		Landingpage.dkmTab(driver).click();
		log.info("DKM tab clicked");
		timer(driver);
		Landingpage.viewerBtn(driver).click();
		log.info("DKM > Viewer tab clicked");
	}

	/**
	 * Edit switch with given IP address
	 * @param driver
	 * @param switchIP
	 * @throws InterruptedException
	 */
	public void editSwitch(WebDriver driver, String switchIP) throws InterruptedException {
		timer(driver);
		DkmElements.searchBox(driver).sendKeys(switchIP);
		log.info("Device IP entered in Search box");
		if (DkmElements.switchesTable(driver).getText().contains(switchIP)) {
			timer(driver);
			DkmElements.switchKebab(driver).click();
			log.info("Switch Kebab menu clicked");
			timer(driver);
			DkmElements.editBtn(driver).click();
			log.info("Edit tab clicked");
			timer(driver);
			DkmElements.numOfPorts(driver).clear();
			DkmElements.numOfPorts(driver).sendKeys("16");
			log.info("Port number entered");
			timer(driver);
			DkmElements.editSaveApplyBtn(driver).click();
			Alert alert = driver.switchTo().alert();
			alert.accept();
			timer(driver);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully Edited Switch"),
					"Notification Message did not contain: Successfully Edited Switch, actual text: " + notificationMessage);
			timer(driver);
			driver.navigate().refresh();
			timer(driver);
			DkmElements.searchBox(driver).sendKeys(switchIP);
			timer(driver);
			String switchesTable = DkmElements.switchesTable(driver).getText();
			Assert.assertTrue(switchesTable.contains("16"),
					"Switches table did not contain: 16, actual text: " + switchesTable);
			log.info("Changes in Switch Asserted Successfully");
		} else {
			log.info("Searched Switch not found");
			throw new SkipException("***** Searched Switch not found *****");
		}
	}

	/**
	 * Add a DKM preset
	 * @param driver
	 * @param sourceName
	 * @param destinationName
	 * @param presetName
	 * @throws InterruptedException
	 */
	public void addPreset(WebDriver driver, String sourceName, String destinationName, String presetName)
			throws InterruptedException {
		/* Search in available presets using preset name - If preset name is available skip the test, if not available click create custom preset
		 * Search in Available Sources list using source name - select if available, skip test if not available
		 * Search in Available Destinations list using destination name, select if available, skip test if not available */
		timer(driver);
		DkmElements.managePresetsBtn(driver).click();
		log.info("Manage Presets button clicked");
		timer(driver);
		DkmElements.presetSearchbox(driver).sendKeys(presetName);
		log.info("Search existing presets to check if " + presetName + " is available..");
		timer(driver);
		if (!DkmElements.availablePresets(driver).getText().contains(presetName)) {
			DkmElements.createCustomPreset(driver).click();
			log.info("Create Custom button clicked");
			timer(driver);
			DkmElements.creatPresetSearchSource(driver).sendKeys(sourceName);
			log.info(sourceName + " entered in to search box");
			timer(driver);
			if (DkmElements.availableSources(driver).getText().contains(sourceName)) {
				DkmElements.createPresetSearchedElement(driver, sourceName).click();
				log.info(sourceName + " selected");
				timer(driver);
				DkmElements.nextBtn(driver).click();
				log.info("Clicked on next button - Navigated to Stage-2");
				timer(driver);
				DkmElements.createPresetSearchDestination(driver).sendKeys(destinationName);
				log.info(destinationName + " enterred in the search box");
				timer(driver);
				if (DkmElements.availableDestinations(driver).getText().contains(destinationName)) {
					DkmElements.createPresetSearchedElement(driver, destinationName).click();
					log.info(destinationName + " selected");
					timer(driver);
					DkmElements.nextBtn(driver).click();
					log.info("Clicked on next button - Navigated to Stage-3");
					timer(driver);
					DkmElements.presetName(driver).sendKeys(presetName);
					log.info("Preset name enterred in the text box");
					timer(driver);
					Select selectType = new Select(DkmElements.createPresetSelectType(driver));
					selectType.selectByValue("full");
					timer(driver);
					DkmElements.completeBtn(driver).click();// Finish flow here
					assertPreset(driver, presetName);
				} else {
					log.info(destinationName + " is not available in Available Destinations.. Skipping test");
					throw new SkipException(
							"***** " + destinationName + " is not present in Available Destinations List *****");
				}
			} else {
				log.info(sourceName + " is not available in Available Sources List.. Skipping test..");
				throw new SkipException("***** " + sourceName + " is not present in Available Sources List *****");
			}

		} else {
			log.info(presetName + " is already preset in Available Presets list.. Skipping test");
			throw new SkipException("*****" + presetName + " is present in preset list already*****");
		}
	}

	/**
	 * Checks if preset exists uning name of preset
	 * @param driver
	 * @param presetName
	 * @throws InterruptedException
	 */
	public void assertPreset(WebDriver driver, String presetName) throws InterruptedException {
		log.info("Asserting Preset..");
		timer(driver);
		DkmElements.managePresetsBtn(driver).click();
		log.info("Manage Presets button clicked");
		timer(driver);
		DkmElements.presetSearchbox(driver).sendKeys(presetName);
		log.info("Search existing presets to check if " + presetName + " is available..");
		timer(driver);
		String availablePresets = DkmElements.availablePresets(driver).getText();
		Assert.assertTrue(availablePresets.contains(presetName),
				"Available presets did not contain: " + presetName + ", actual text: " + availablePresets);
		log.info(presetName + " present in Preset list.. Assertion complete");
	}

	/**
	 * Adds 5 VTX to a single source 
	 * @param driver
	 * @param sourceName
	 * @throws InterruptedException
	 */
	public void addDestinationLoop(WebDriver driver, String sourceName) throws InterruptedException {
		log.info("Adding destination for " + sourceName);
		if (sourceName.equalsIgnoreCase("VTX1")) {
			int checkbox = 1;
			DkmElements.addDestination(driver, sourceName).click();
			log.info("***" + sourceName + " - Add Destination button clicked ***");
			timer(driver);
			DkmElements.destinationCheckbox(driver, checkbox).click();
			log.info("VRX" + checkbox + " Selected Available Destinations");
		} else if (sourceName.equalsIgnoreCase("VTX2")) {
			int checkbox = 2;
			DkmElements.addDestination(driver, sourceName).click();
			log.info("***" + sourceName + " - Add Destination button clicked ***");
			while (checkbox < 4) {
				timer(driver);
				DkmElements.destinationCheckbox(driver, checkbox).click();
				log.info("VRX" + checkbox + " Selected Available Destinations");
				checkbox++;
			}
			log.info("Total " + DkmElements.selectedElements(driver).size() + " destinations selected");
			// Assert if required destinations are selected
			Assert.assertTrue(DkmElements.selectedElements(driver).size() == 2, "***** < 2 elements selected *****");
		} else if (sourceName.equalsIgnoreCase("VTX3")) {
			int checkbox = 4;
			DkmElements.addDestination(driver, "VTX3").click();
			log.info("***" + sourceName + " - Add Destination button clicked ***");
			while (checkbox < 7) {
				timer(driver);
				DkmElements.destinationCheckbox(driver, checkbox).click();
				log.info("VRX" + checkbox + " Selected from Available Destinations");
				checkbox++;
			}
			log.info("Total " + DkmElements.selectedElements(driver).size() + " destinations selected");
			Assert.assertTrue(DkmElements.selectedElements(driver).size() == 3, "***** < 3 elements selected *****");
		} else if (sourceName.equalsIgnoreCase("VTX4")) {
			int checkbox = 7;
			// Scrolling by 250 pixel
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(0, 250);");
			DkmElements.addDestination(driver, "VTX4").click();
			log.info("***" + sourceName + " - Add Destination button clicked ***");
			while (checkbox < 11) {
				timer(driver);
				// Scroll inside web element vertically (e.g. 150 pixel)
				js.executeScript("arguments[0].scrollTop = arguments[1];", DkmElements.destinationContainer(driver),
						150);
				DkmElements.destinationCheckbox(driver, checkbox).click();
				log.info("VRX" + checkbox + " Selected from Available Destinations");
				checkbox++;
			}
			log.info("Total " + DkmElements.selectedElements(driver).size() + " destinations selected");
			Assert.assertTrue(DkmElements.selectedElements(driver).size() == 4, "***** < 4 elements selected *****");
		} else if (sourceName.equalsIgnoreCase("VTX5")) {
			int checkbox = 11;
			// Scrolling by 250 pixel
			JavascriptExecutor js = (JavascriptExecutor) driver;
			js.executeScript("scroll(0, 250);");
			DkmElements.addDestination(driver, "VTX5").click();
			log.info("***" + sourceName + " - Add Destination button clicked ***");
			while (checkbox < 16) {
				timer(driver);
				// Scroll inside web element vertically (e.g. 200 pixel)
				js.executeScript("arguments[0].scrollTop = arguments[1];", DkmElements.destinationContainer(driver),
						200);
				DkmElements.destinationCheckbox(driver, checkbox).click();
				log.info("VRX" + checkbox + " Selected from Available Destinations");
				checkbox++;
			}
			log.info("Total " + DkmElements.selectedElements(driver).size() + " destinations selected");
			Assert.assertTrue(DkmElements.selectedElements(driver).size() == 5, "***** < 5 elements selected *****");
		}
		timer(driver);
		SeleniumActions.seleniumClick(driver, DkmElements.getActiveSelectedBtnDestination());
		//DkmElements.activateSelectedBtnDestination(driver).click();
		log.info("Activate button clicked");
		//wait for DKM page to load
		boolean hasLoaded = false;
		while(!hasLoaded) {
			try {
				new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(DkmElements.managePresetsBtn(driver)));
				hasLoaded = true;
			}catch(Exception e) {
				log.info("Button click did not work. Trying again");
				SeleniumActions.seleniumClick(driver, DkmElements.getActiveSelectedBtnDestination());
			}
		}

		log.info("Asserting UI to make sure destinations tag size is correct");
		assertUI(driver, sourceName);
	}


	public void assertUI(WebDriver driver, String sourceName) {
		/* based on addDestinationLoop assert that correct class selected based on number of destinations selected*/
		if (sourceName.equalsIgnoreCase("VTX1")) {
			Assert.assertTrue(DkmElements.colmd12(driver, sourceName).size() == 1,
					"***** Destination elements count for " + sourceName + " is not equal to 1 *****");
			log.info("There is 1 destination element and class is col-md-12");
		} else if (sourceName.equalsIgnoreCase("VTX2")) {
			Assert.assertTrue(DkmElements.colmd6(driver, sourceName).size() == 2,
					"***** Destination elements count for " + sourceName + " is not equal to 2 *****");
			log.info("There are 2 destination elements and class is col-md-6");
		} else if (sourceName.equalsIgnoreCase("VTX3")) {
			Assert.assertTrue(DkmElements.colmd4(driver, sourceName).size() == 3,
					"***** Destination elements count for " + sourceName + " is not equal to 3 *****");
			log.info("There are 3 destination elements and class is col-md-4");
		} else if (sourceName.equalsIgnoreCase("VTX4") || sourceName.equalsIgnoreCase("VTX5")) {
			Assert.assertTrue(DkmElements.colmd3(driver, sourceName).size() == 4,
					"***** Destination elements count for " + sourceName + " is not equalt to 4 *****");
			log.info("There are 4 destination elements and class is col-md-3");
		}
	}

	public void assertElmentResize(WebDriver driver) throws InterruptedException {
		try {
			timer(driver);
			DkmElements.destinationDetach(driver, "VTX5").click();
			log.info("First Destination detached from VTX5");
			timer(driver);
			DkmElements.destinationDetach(driver, "VTX5").click();
			log.info("Second Destination detached from VTX5");
			timer(driver);
			DkmElements.destinationDetach(driver, "VTX5").click();
			log.info("Third Destination detached from VTX5");
			timer(driver);
			Assert.assertTrue(DkmElements.colmd6(driver, "VTX5").size() == 2,
					"***** Destination elements count for " + "VTX5" + " is not equal to 2 *****");
			log.info("New width for destination under VTX5 successfully asserted");
		} catch (Exception e) {
			log.info("Error in Element Resize test");
			throw e;
		}
	}

	/**
	 * Deletes a DKM preset
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deletePreset(WebDriver driver) throws InterruptedException { // delete Preset
		timer(driver);
		DkmElements.managePresetsBtn(driver).click();
		timer(driver);
		log.info("Managed Presets button clicked");
		log.info("Checking for manage presets pop up window");
		int count = 0;
		while(!DkmElements.managePresetHeading(driver).isDisplayed() && count < 2) {
			count++;
			DkmElements.managePresetsBtn(driver).click();
			timer(driver);
			log.info("Managed Presets button clicked:" + count);
			log.info("Checking for manage presets pop up window:" + count);
		}
		int presetCount = DkmElements.availablePresetsList(driver).size(); // Presets counts
		while (presetCount != 0) {
			log.info("There are " + presetCount + " Presets to delete..");
			timer(driver);
			DkmElements.availablePresetsList(driver).get(0).click();
			log.info("Preset delete..");
			timer(driver);
			DkmElements.managePresetsBtn(driver).click();
			log.info("Managed Presets button clicked");
			presetCount = DkmElements.availablePresetsList(driver).size(); // New preset counts
		}
		DkmElements.closeBtnManagePresetsModal(driver).click();
	}

	/**
	 * Clears all DKM connections, deletes presets and deletes and remkaes a switch
	 * @param driver
	 * @param switchIP
	 * @param switchName
	 * @throws InterruptedException
	 */
	public void resetAllDkm(WebDriver driver) throws InterruptedException {
		log.info("Deleting connections and presets");
		navigateToDkmViewer(driver);
		timer(driver);
		detachConnctions(driver);
		timer(driver);
		deletePreset(driver);
		log.info("Connections and Presets deleted");
	}
}
