package objects;

import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
/**
 * Holds all the object models for Boxilla - DKM pages
 * @author Boxilla
 *
 */
public class DkmElements {
	
	public static WebElement element = null;
	public static String deleteVcpuConfirm = "//button[@id='delSaveBtn']";
	
//	public static WebElement deleteVcpuConfirm(WebDriver driver) {
//		return driver.findElement(By.xpath("//button[@id='delSaveBtn']"));
//	}
	
	public static Select deleteCvpuSelect(WebDriver driver) {
		Select dropdown = new Select(driver.findElement(By.xpath("//select[@id='vCpu']")));
		return dropdown;
	}
	
	public static WebElement addVcpuBtn(WebDriver driver) {
		return driver.findElement(By.xpath("//span[contains(.,'Create vCPU')]"));
	}
	
	public static WebElement deleteVcpuBtn(WebDriver driver) {
		return driver.findElement(By.xpath("//span[contains(.,'Delete vCPU')]"));
	}
	
	public static WebElement vCpuNameTxtBox(WebDriver driver) {
		return driver.findElement(By.xpath("//input[@id='vcpuName']"));
	}
	
	public static WebElement saveVcpuBtn(WebDriver driver) {
		return driver.findElement(By.xpath("//button[contains(.,'Save')]"));
	}
	
	public static WebElement saveVcpuToastMessage(WebDriver driver) {
		return driver.findElement(By.xpath("//div[@class='toast-message']"));
	}

	public static WebElement addSwitchBtn(WebDriver driver) { // Add Switch Button
		return element = driver.findElement(By.linkText("Add Switch"));
	}

	public static WebElement portsTab(WebDriver driver) { // Ports Tab
		return element = driver.findElement(By.xpath(".//*[@data-bb-tab='devices_table']"));
	}

	public static WebElement switchesTable(WebDriver driver) { // Switches Table
		return element = driver.findElement(By.xpath(".//*[@id='switchTable']"));
	}

	public static WebElement switchName(WebDriver driver) { // Switch Name text box
		// return element = driver.findElement(By.xpath(".//*[@id='switchName']"));
		return element = driver.findElement(By.xpath(".//*[@id='nameVar']"));
	}

	public static WebElement switchIpAddress(WebDriver driver) { // IP address textbox
		return element = driver.findElement(By.xpath(".//*[@id='ipAddress']"));
	}

	public static WebElement applyBtn(WebDriver driver) { // Apply Button - Add DKM Switch
		return element = driver.findElement(By.xpath(".//*//button[@id='addSaveBtn' and contains(., 'Apply')]"));
	}

	public static WebElement editSaveApplyBtn(WebDriver driver) { // Apply Button - Switch Edit
		return element = driver.findElement(By.xpath(".//*[@id='editSaveBtn']"));
	}

	public static WebElement searchBox(WebDriver driver) { // Switch Search box
		return element = driver.findElement(By.xpath(".//input[@aria-controls='switchTable']"));
	}

	public static WebElement switchKebab(WebDriver driver) { // Kebab dropdown menu
		return element = driver.findElement(By.xpath("(//table[@id='switchTable']//*[@id='dropdownKebab'])"));
	}

	public static WebElement deleteBtn(WebDriver driver) { // Delete option in Kebab dropdown
		return element = driver.findElement(By.xpath("//a[@class='deleteSwitch']"));
	}

	public static WebElement editBtn(WebDriver driver) { // Edit tab
		return element = driver.findElement(By.linkText("Edit"));
	}

	public static WebElement numOfPorts(WebDriver driver) { // Number of Ports
		return element = driver.findElement(By.xpath(".//*[@id='numPortsEdit']"));
	}

	public static WebElement makeConnectionBtn(WebDriver driver) { // Make Connection Button
		return element = driver.findElement(By.xpath("//*[@data-target='#addSource']"));
	}

	public static WebElement nonActiveSource(WebDriver driver) { // Non active source list
		return element = driver.findElement(By.xpath(".//*[@id='non-active-source']"));
	}

	public static WebElement nonActiveDestination(WebDriver driver) { // Non active destination list
		return element = driver.findElement(By.xpath(".//*[@id='non-active-destination']"));
	}

	public static java.util.List<WebElement> nonActiveSourceElements(WebDriver driver) {
		return driver.findElements(By.xpath(".//*[@id='non-active-source']//*[@class='non-active-element']"));
	}

	public static WebElement searchSource(WebDriver driver) { // Source search box
		return element = driver.findElement(By.xpath("//*[@id='addSources']//*[@name='source']"));
	}
	
	public static WebElement searchPresetSource(WebDriver driver) {
		return driver.findElement(By.id("//*[@id='source-name']//*[@name='source']"));
	}

	public static WebElement searchDestination(WebDriver driver) { // Destination search box
		return element = driver.findElement(By.xpath("//*[@id='addDestination']//*[@name='destination']"));
	}

	public static WebElement destinationCheckbox(WebDriver driver, int index) {
		// if (driver instanceof ChromeDriver) {
		return element = driver.findElement(
				By.xpath("(//*[@id='addDestination']//*[@class='destination-grid-item'])[" + index + "]/td[1]"));
		// } else {
		// return element = driver
		// .findElement(By.xpath("(.//*[@id='addSourceDestinations']//*[@type='checkbox'])["
		// + index + "]"));
		// }
	}

	public static WebElement sourceTable(WebDriver driver) { // Source Table
		return element = driver.findElement(
				By.xpath("//*[@id='addSource']//*[@class='source-element-container element-grid-container']"));
	}

	public static WebElement searchedElement(WebDriver driver, String name) {
		// Searched Source or Destination element
		//return element = driver.findElement(By.xpath("//td[contains(text(),\'" + name + "\')]"));
		//td[./text()='VTX1']
		return element = driver.findElement(By.xpath("//td[./text()='" + name + "']"));
	}
	
	public static WebElement presetSourceListItem(WebDriver driver,String name) {
		System.out.println("//div[contains(@id,'addPresetSources')]//child::td[text() = '" + name + "']");
		return driver.findElement(By.xpath("//div[contains(@id,'addPresetSources')]//child::td[text() = '" + name + "']"));
	}

	public static WebElement activateSelectedBtnSource(WebDriver driver) { // Activate Selected Button Source
		return element = driver.findElement(By.xpath("//*[@id='addSource']//*[@id='activate-selected']"));
	}

	public static String getActiveSelectedBtnDestination() {
		return "//*[@id='addDestination']//*[@id='activate-selected']";
	}
	public static WebElement activateSelectedBtnDestination(WebDriver driver) { // Activate Selected Button Destination
		return element = driver.findElement(By.xpath("//*[@id='addDestination']//*[@id='activate-selected']"));
	}

	/*	public static WebElement activeElementContainer(WebDriver driver) {
			// Active element container to assert if source added successfully
			return element = driver.findElement(By.xpath("//*[@class='active-element-container']"));
		}*/

	public static WebElement addDestination(WebDriver driver, String sourceName) {
		// Add Destination tab - relevant to source name passed as argument
		return element = driver.findElement(By.xpath(
				"//span[contains(text(),\'" + sourceName + "\')]/../../../..//*[@data-target='#addDestination']"));
	}

	public static WebElement activeConnectionCoutner(WebDriver driver) { // Active Connection Counter element
		return element = driver.findElement(By.xpath("(.//*[@class='card-pf-aggregate-status-count'])[2]"));
	}

	public static WebElement activeConnectionTable(WebDriver driver) { // Active Connection Table
		return element = driver.findElement(By.xpath(".//*[@class='active-element-container']"));
	}

	public static WebElement sourceDetachBtn(WebDriver driver) { // Source Detach Button
		return element = driver.findElement(By.xpath(".//*[@class='active-source']//*[@class='detach']"));
	}

	public static String getManagePresetsBtn() {
		return ".//*[@data-target='#managePresets']";
	}
	public static WebElement managePresetsBtn(WebDriver driver) { // Manage Presets button
		return element = driver.findElement(By.xpath(".//*[@data-target='#managePresets']"));
	}

	public static WebElement createCustomPreset(WebDriver driver) { // Create Custom Preset Button
		return element = driver.findElement(By.xpath(".//*[@id='create-custom-preset']"));
	}

	public static WebElement presetSearchbox(WebDriver driver) { // Preset search box
		return element = driver.findElement(By.xpath(".//*[@id='preset-name']"));
	}

	public static WebElement availablePresets(WebDriver driver) { // Available Presets table
		return element = driver.findElement(By.xpath(".//*[@class='element-grid-container']"));
	}

	public static WebElement creatPresetSearchSource(WebDriver driver) { // Search Source - Create Preset
		return element = driver.findElement(By.xpath("//*[@id='createPreset']//*[@name='source']"));
	}

	public static WebElement availableSources(WebDriver driver) { // Available Sources List
		return element = driver.findElement(
				By.xpath(".//*[@id='addPresetSources']//*[@class='source-element-container element-grid-container']"));
	}

	public static WebElement nextBtn(WebDriver driver) { // Next button - Create Preset
		return element = driver.findElement(By.xpath(".//*[@class='btn btn-primary wizard-pf-next']"));
	}

	public static WebElement createPresetSearchDestination(WebDriver driver) { // Search destination - Create Preset
		return element = driver.findElement(By.xpath("//*[@id='createPreset']//*[@name='destination']"));
	}

	public static WebElement availableDestinations(WebDriver driver) { // Avaialable Destination List
		return element = driver.findElement(
				By.xpath(".//*[@id='createPreset']//*[@class='destination-element-container element-grid-container']"));
	}

	public static WebElement destinationContainer(WebDriver driver) {
		return element = driver.findElement(By
				.xpath(".//*[@id='addDestination']//*[@class='destination-element-container element-grid-container']"));
	}

	public static WebElement presetName(WebDriver driver) { // Create Preset - Preset name
		return element = driver.findElement(By.xpath("//*[@id='createPreset']//*[@name='name']"));
	}

	public static WebElement createPresetSearchedElement(WebDriver driver, String name) { // Create Preset Searched
																							// element
		// Searched Source or Destination element
		return element = driver
				.findElement(By.xpath("//*[@id='createPreset']//*//td[contains(text(),\'" + name + "\')]"));
	}

	public static WebElement createPresetSelectType(WebDriver driver) { // Create Preset - Select Type
		return element = driver.findElement(By.xpath("//*[@id='createPreset']//*[@id='type']"));
	}

	public static WebElement completeBtn(WebDriver driver) { // Create Preset - Complete button
		return element = driver.findElement(By.xpath(".//*[@class='btn btn-primary wizard-pf-finish']"));
	}

	/* Destination Elements - Used to check the counts of destination added
	 * application code is designed to select element class based on number of destination added 
	 * classes will be selected from col-md-3 , col-md-4, col-md-6 and col-md-12*/
	public static List<WebElement> colmd3(WebDriver driver, String source) {
		return driver.findElements(By.xpath("//span[contains(text(),\'" + source
				+ "\')]/../../..//*[@class='source-destinations']//*[@class='col-md-3 col-sm-12 destination-element']"));
	}

	public static java.util.List<WebElement> colmd4(WebDriver driver, String source) {
		return driver.findElements(By.xpath("//span[contains(text(),\'" + source
				+ "\')]/../../..//*[@class='source-destinations']//*[@class='col-md-4 col-sm-12 destination-element']"));
	}

	public static java.util.List<WebElement> colmd6(WebDriver driver, String source) {
		return driver.findElements(By.xpath("//span[contains(text(),\'" + source
				+ "\')]/../../..//*[@class='source-destinations']//*[@class='col-md-6 col-sm-12 destination-element']"));
	}

	public static List<WebElement> colmd12(WebDriver driver, String source) {
		return driver.findElements(By.xpath("//span[contains(text(),\'" + source
				+ "\')]/../../..//*[@class='source-destinations']//*[@class='col-md-12 col-sm-12 destination-element']"));
	}

	public static List<WebElement> selectedElements(WebDriver driver) { // Selected elements
		return driver.findElements(By.xpath("//*[@class='selected-element']"));
	}

	public static WebElement destinationDetach(WebDriver driver, String sourceName) { // Detach destination
		return element = driver.findElement(By.xpath("//span[contains(text(),\'" + sourceName
				+ "\')]/../../..//*[@class='source-destinations']//*[@class='detach']"));
	}
	
	public static WebElement createPresetSearchBoxSource(WebDriver driver) {
		return driver.findElement(By.id("source-name"));
	}

	public static WebElement presetSourceCheckbox(WebDriver driver, int index) { // Preset sources checkbox
		return driver
				.findElement(By.xpath("//*[@id='createPreset']//*[@class='source-grid-item'][" + index + "]/td[1]"));
	}

	public static List<WebElement> selectedPresetSourceList(WebDriver driver) {
		return driver.findElements(By.xpath("//*[@id='createPreset']//*[@id='wizard-selected-preset-sources']//li"));
	}
	// Selected preset source list element
	/*public static WebElement selectedPresetSourceList(WebDriver driver, int index) {
		return driver.findElement(
				By.xpath("//*[@id='createPreset']//*[@id='wizard-selected-preset-sources']//li[" + index + "]"));
	}*/

	// Preset destination checkbox
	public static WebElement presetDestinationCheckbox(WebDriver driver, int destinationIndex, int sourceIndex) {
		return driver.findElement(By.xpath("(//*[@id='createPreset']//*[@class='destination-grid-item']["
				+ destinationIndex + "]/td[1])[" + sourceIndex + "]"));
	}
	
	public static WebElement selectPresetDestination(WebDriver driver, String name){
		System.out.println("//*[@id='createPreset']//*[@class='destination-grid-item']//child::td[text() = '"
				+ name + "']");
		List<WebElement> list = driver.findElements(By.xpath("//*[@id='createPreset']//*[@class='destination-grid-item']//child::td[text() = '"
				+ name + "']"));
		
		for(WebElement e : list) {
			try {
				((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
				e.click();
				return e;
			}catch(Exception ex) {
				ex.printStackTrace();
			}
		}
		return null;
		
	}

	// Selected Destination elements - Add Preset
	public static List<WebElement> presetSelectedDestinationContainer(WebDriver driver) {
		return driver.findElements(By.xpath(
				"//*[@id='createPreset']//*[@class='destination-element-container selected-element-container']"));
	}

	public static List<WebElement> availablePresetsList(WebDriver driver) { // Delete Preset from Available preset list
		return driver.findElements(By.xpath("//*[@id='managePresets']//*[@class='fa fa-times delete-preset']"));
	}

	public static WebElement dkmConnectionRow(WebDriver driver, String sourceName) {
		/* Assert DKM Connection using table row and source name to find the row */
		return driver.findElement(
				By.xpath("//*[@id='connection-table']//*[td//text()[contains(., \'" + sourceName + "\')]]"));
	}

	public static WebElement presetButton(WebDriver driver) { // There will be only one preset button after each test
		return driver.findElement(By.xpath("//*[@class='preset-btn-group btn-group']//button"));
	}

	public static WebElement closeBtnManagePresetsModal(WebDriver driver) { // Manage Presets modal - Close Button
		return driver.findElement(By.xpath("//*[@id='managePresets']//*[@class='close']"));
	}
	public static WebElement managePresetHeading(WebDriver driver) {
		return driver.findElement(By.xpath("//h4[contains(.,'Manage Presets')]"));
	}
}
