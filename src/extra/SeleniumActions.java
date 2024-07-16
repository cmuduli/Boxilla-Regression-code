package extra;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import testNG.Utilities;
/**
 * This class contains all the selenium events that a test will 
 * use when writing methods for test cases. 
 * @author Boxilla
 *
 */
public class SeleniumActions {
	
	final static Logger log = Logger.getLogger(SeleniumActions.class);
	static int screenShotcounter = 0;
	static ArrayList<String> screenshotList = new ArrayList<String>();
	
	private static List<WebElement> getCorrectElement(WebDriver driver, String location) {
		List<WebElement> e = null;
			e = driver.findElements(By.xpath(location));
			if(e.size() == 0) {
				e = driver.findElements(By.linkText(location));
				return e;
			}
			return e;
	}
	//DONT NOW USE. CAUSES ISSUES WITH BOXILLA
	private static void highlightElement(WebDriver driver, WebElement ele) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].setAttribute('style', 'border: 4px solid red;');", ele);
	}
	public static void takeScreenshot(WebDriver driver) {
		boolean run = false;
		if(run) {
			screenShotcounter++;
			String fileName = "./test-output/Screenshots/" + "giffy" + screenShotcounter + ".png";
			screenshotList.add(fileName);
			Utilities.captureScreenShot(driver, "giffy" + screenShotcounter, "test");
		}
	}
	
	public static List<WebElement> getListOfElements(WebDriver driver, String xpath) {
		List<WebElement> e = null;
		e = driver.findElements(By.xpath(xpath));
		return e;
	}
	/**
	 * Executes a javascript click on a webelemt
	 * @param driver - Webdriver needed to interact with the browser
	 * @param ele - The element to javascript click on
	 */
	public static void exectuteJavaScriptClick(WebDriver driver, WebElement ele) {
		log.info("Clicking using javascript");
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("arguments[0].click()", ele);
		takeScreenshot(driver);
		
	}
	
	/**
	 * Uses the webdriver to refresh the current page
	 * @param driver - WebDriver needed to interact with the browser
	 * @throws InterruptedException
	 */
	public static void refreshPage(WebDriver driver) throws InterruptedException {
		Thread.sleep(1000);
		driver.navigate().refresh();
		Thread.sleep(1000);
		takeScreenshot(driver);
		
	}
	/**
	 * Method that will get the text of a drop down element and return it 
	 * as a string
	 * @param driver - Webdriver needed to interact with the browser
	 * @param xpath - The xpath of the element to get the text from
	 * @return returns a string of text
	 */
	public static String seleniumDropdownGetText(WebDriver driver, String xpath) {
		Select select = new Select(driver.findElement(By.xpath(xpath)));
		WebElement option = select.getFirstSelectedOption();
		String optionText = option.getText();
		takeScreenshot(driver);
		return optionText;
		
	}
	/**
	 * Selects a value from a drop down list
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - Xpath of the dropdown element
	 * @param value - The text of the item to select from the dropdown
	 */
	public static void seleniumDropdown(WebDriver driver, String xpath, String value) {
		List<WebElement> elements = getCorrectElement(driver, xpath);
		
		for(WebElement e : elements) {
			try {
				if(e.isDisplayed()) {
					log.info("Selecting " + value + " from " + xpath);
					Select select = new Select(e);
					select.selectByVisibleText(value);
					takeScreenshot(driver);
					break;
				}
			}catch(Exception e1){
				System.out.println("Not the correct element");
			}
		}
	}
	/**
	 * Returns true if the element is displayed in the browser
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - Xpath of the element to be checked 
	 * @return true if displayed otherwise false
	 */
	public static boolean seleniumIsDisplayed(WebDriver driver, String xpath) {
		log.info("Checking if " + xpath + " is displayed");
		try {
			WebElement e = driver.findElement(By.xpath(xpath));
			e.isDisplayed();
			takeScreenshot(driver);
			return true;
		}catch(Exception e1) {
			return false;
		}

		
	}
	/**
	 * Returns true if the element is enabled
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - Xpath of the element to be checked
	 * @return true is element is enable else false
	 */
	public static boolean seleniumIsEnabled(WebDriver driver, String xpath) {
		log.info("Checking if " + xpath + " is enabled");
		try {
			WebElement e = driver.findElement(By.xpath(xpath));
			takeScreenshot(driver);
			return e.isEnabled();
		}catch(Exception e) {
			takeScreenshot(driver);
			return false;
		}
	}
	
	/**
	 * Get the value of an elements attribute
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - The xpath of the element
	 * @param attribute - The attribute to get the value from 
	 * @return the value of the attribute
	 */
	public static String getAttribute(WebDriver driver, String xpath, String attribute) {
		log.info("Getting attribute:" + attribute + " from element " + xpath);
		WebElement e = driver.findElement(By.xpath(xpath));
		String attReturn = e.getAttribute(attribute);
		log.info(attribute + ":" + attReturn);
		return attReturn;
	}
	
	/**
	 * Will click the element
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - xpath of the element to be clicked
	 */
	public static void seleniumClick(WebDriver driver, String xpath) {
		List<WebElement> elements = getCorrectElement(driver, xpath);
		int counter = 0;
		for(WebElement e : elements) {
			while(counter < 21) {
			try {
				log.info("Clicking " + xpath);
				//((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", e);
				e.click();
				takeScreenshot(driver);
				log.info("Clicked successfully");
				counter = 21;
			}catch(Exception ex) {
				log.info("Unable to click. May cause a test failure if no success message:" + xpath);
				//ex.printStackTrace();
				counter ++;
				if(counter > 20) {
					throw new AssertionError("Unable to click " + xpath);
				}
			}
			}
		}
	}
	/**
	 * Get the text of an element
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - The xpath of the element to get the text from
	 * @return String containing the text from the element
	 */
	public static String seleniumGetText(WebDriver driver, String xpath) {
		List<WebElement> elements = getCorrectElement(driver, xpath);
		String text = "";
		for(WebElement e : elements) {
			try {
				log.info("Getting text from  " + xpath);
				text = e.getText();
				takeScreenshot(driver);
			}catch(Exception ex) {

			}
		}
		return text;
	}
	
	/**
	 * Get text will not always work. This method can be used instead
	 * @param driver
	 * @param xpath
	 * @return
	 */
	public static String seleniumGetInnerText(WebDriver driver, String xpath) {
		WebElement e = driver.findElement(By.xpath(xpath));
		String text = e.getAttribute("innerText");
		if(text.equals("") || text == null) 
			throw new AssertionError("Text was empty or null: ");
		log.info("Inner text:" + text);
		return text;
	}
	/**
	 * Sends the text to the text box
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - Xpath of the text box
	 * @param text - text to send to the text box
	 */
	public static void seleniumSendKeys(WebDriver driver, String xpath, String text) {
		List<WebElement> elements = getCorrectElement(driver, xpath);
		for(WebElement e : elements) {
			try {
				//e.clear();
				e.sendKeys(text);
				log.info("Sending " + text + " to " + xpath);
				takeScreenshot(driver);
				break;
			}catch(Exception ex) {

			}
		}
	}
	/**
	 * Will clear any text from a textbox
	 * @param driver - WebDriver needed to interact with the browser
	 * @param xpath - The xpath of the text box
	 */
	public static void seleniumSendKeysClear(WebDriver driver, String xpath) {
		List<WebElement> elements = driver.findElements(By.xpath(xpath));
		for(WebElement e : elements) {
			try {
				e.clear();
				takeScreenshot(driver);
				break;
			}catch(Exception ex) {

			}
		}
	}
	/**
	 * Will simulate a drag and drop from a source element to a destination
	 * @param driver - WebDriver needed to interact with the browser
	 * @param source - Source element 
	 * @param destination - Destination element
	 */
	public static void dragAndDrop(WebDriver driver, WebElement source, WebElement destination) {
		Actions builder = new Actions(driver);

		Action dragAndDrop = builder.clickAndHold(source)
		   .moveToElement(destination)
		   .release(destination)
		   .build();

		dragAndDrop.perform();
	}
	/**
	 * Will simulate a drag and drop from a source element to a destination
	 * @param driver - WebDriver needed to interact with the browser
	 * @param source - Source element 
	 * @param destination - Destination element
	 */
	public static void dragAndDrop(WebDriver driver, String source, String destination) {
		List<WebElement> sourceElements = driver.findElements(By.xpath(source));
		List<WebElement> destinationElements = driver.findElements(By.xpath(destination));
		WebElement sourceEle = null;
		WebElement desEle = null;
		for(WebElement e : sourceElements) {
			if(e.isDisplayed()) {
				sourceEle = e;
			}
		}
		for(WebElement d : destinationElements) {
			if(d.isDisplayed()) {
				desEle = d;
			}
		}
		
		Actions builder = new Actions(driver);

		Action dragAndDrop = builder.clickAndHold(sourceEle)
		   .moveToElement(desEle)
		   .release(desEle)
		   .build();

		dragAndDrop.perform();
		try {
			Thread.sleep(1000);
		}catch(Exception e) {
			
		}
	}
	
	public static WebElement getElement(WebDriver driver, String xpath) {
		return driver.findElement(By.xpath(xpath));
	}
	
//	public static void seleniumClick(WebDriver driver, WebElement element) {
//			log.info("Clicking " + element.);
//			element.click();
//			
//	}

}
