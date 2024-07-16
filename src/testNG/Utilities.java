package testNG;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;

import org.apache.commons.io.FileUtils;
import org.apache.log4j.Logger;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Action;
import org.openqa.selenium.interactions.Actions;
import org.testng.SkipException;

import extra.Ssh;
import extra.StartupTestCase;
import junit.framework.Assert;
import systemAdmin.SystemAdministration;

public class Utilities {

	Properties prop = new Properties();
	final static Logger log = Logger.getLogger(Utilities.class);

	/**
	 * Loads the properties file. Will throw a SkipException if file 
	 * is not found
	 */
	public void loadProperties() {
		try {
			FileInputStream input = new FileInputStream("test.properties");
			prop.load(input);
		} catch (IOException e) {
			log.info("Properties file not found");
			throw new SkipException("Skipped - Missing properties file");
		}
	}

	/**
	 * returns the properties file
	 * @return
	 */
	public Properties getProperties() {
		return prop;
	}
	public static String getCellData(int rowNum, int colNum) throws IOException {
		FileInputStream file = new FileInputStream("C:\\Selenium\\testdata.xlsx");

		@SuppressWarnings("resource")
		XSSFWorkbook wb = new XSSFWorkbook(file);
		XSSFSheet ws = wb.getSheet("sheet1");
		XSSFRow row;
		XSSFCell cell;

		try {
			row = ws.getRow(rowNum);
			cell = row.getCell(colNum);
			String CellValue = cell.getStringCellValue();

			return CellValue;
		} catch (Exception e) {
			return null;
		}
	}

	public static void captureScreenShot(WebDriver driver, String screenshotName, String testName) {
		try {
			TakesScreenshot ts = (TakesScreenshot) driver; // Create reference of take TakesScreenshot
			File source = ts.getScreenshotAs(OutputType.FILE); // Call method to capture screenshot
			FileUtils.copyFile(source, new File("./test-output/Screenshots/" + screenshotName + ".png"));
			log.info("Screenshot taken for test:" + testName);
		} catch (Exception e) {
			log.info("Exception while taking screenshot : " + e.getMessage());
		}
	}

	public static String getDateTimeStamp() {
		// creates a date time stamp that is Windows OS filename compatible
		return new SimpleDateFormat("dd HH.mm.ss").format(Calendar.getInstance().getTime());
	}

	public static boolean writeStringToFile(String data, String fileName) {
		try{
			PrintWriter out = new PrintWriter(fileName);
			out.println(data);
			out.close();
			return true;
		}catch(Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static void captureLog2(String boxillaIp, String userName, String password, String fileName) {
		log.info("Test failed so trying to capture snapshot of log file");
		
		Ssh shell = new Ssh(userName, password, boxillaIp);
		shell.loginToServer();
		String output = shell.sendCommand("tail -200 /var/log/upstart/moy.log");
		shell.disconnect();
		writeStringToFile(output, fileName);
		log.info("Log captured and saved to " + fileName);

	}

	public static void captureDeviceLog(String deviceIp, String fileName) {
//		log.info("Test has failed so capturing device log");
//		Ssh shell = new Ssh(StartupTestCase.deviceUserName, StartupTestCase.devicePassword, deviceIp);
//		shell.loginToServer();
//		String logText = shell.sendCommand("cat /usr/local/syslog.log");
//		shell.disconnect();
//		String device = deviceIp.replaceAll("\\.", "");
//		File file  =new File("./test-output/logs");
//		boolean exists = file.exists();
//		if(!exists) {
//			file.mkdir();
//		}
//		writeStringToFile(logText, file.toString() + "/LOG_" + device + "_" + fileName + "_" + getDateTimeStamp());
//		log.info("Device log from " + deviceIp + " saved");
		
	}


//	public static void seleniumClick(WebDriver driver, String xpath) {
//		List<WebElement> elements = driver.findElements(By.xpath(xpath));
//		for(WebElement e : elements) {
//			try {
//				e.click();
//				System.out.println("Clicking " + xpath);
//				break;
//			}catch(Exception ex) {
//
//			}
//		}
//	}
//	public static void seleniumSendKeys(WebDriver driver, String xpath, String text) {
//		List<WebElement> elements = driver.findElements(By.xpath(xpath));
//		for(WebElement e : elements) {
//			try {
//				e.sendKeys(text);
//				System.out.println("Sending " + text + " to " + xpath);
//				break;
//			}catch(Exception ex) {
//
//			}
//		}
//	}
//	public static void seleniumSendKeysClear(WebDriver driver, String xpath) {
//		List<WebElement> elements = driver.findElements(By.xpath(xpath));
//		for(WebElement e : elements) {
//			try {
//				e.clear();
//				break;
//			}catch(Exception ex) {
//
//			}
//		}
//	}
//	public static void dragAndDrop(WebDriver driver, WebElement source, WebElement destination) {
//		Actions builder = new Actions(driver);
//
//		Action dragAndDrop = builder.clickAndHold(source)
//		   .moveToElement(destination)
//		   .release(destination)
//		   .build();
//
//		dragAndDrop.perform();
//	}
	
//	public static void dragAndDrop(WebDriver driver, String source, String destination) {
//		List<WebElement> sourceElements = driver.findElements(By.xpath(source));
//		List<WebElement> destinationElements = driver.findElements(By.xpath(destination));
//		WebElement sourceEle = null;
//		WebElement desEle = null;
//		for(WebElement e : sourceElements) {
//			if(e.isDisplayed()) {
//				sourceEle = e;
//			}
//		}
//		for(WebElement d : destinationElements) {
//			if(d.isDisplayed()) {
//				desEle = d;
//			}
//		}
//		
//		Actions builder = new Actions(driver);
//
//		Action dragAndDrop = builder.clickAndHold(sourceEle)
//		   .moveToElement(desEle)
//		   .release(desEle)
//		   .build();
//
//		dragAndDrop.perform();
//	}
	
}
