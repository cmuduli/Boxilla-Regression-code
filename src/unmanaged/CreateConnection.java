package unmanaged;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.ScpTo;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import invisaPC.Connection;
import invisaPC.User;
import invisaPC.XMLParserSAX;
import methods.PeripheralsMethods;
import objects.Peripherals;
import testNG.Utilities;

/**
 * Creates connections on an unmanaged boxilla and using the CloudData xml file to assert the
 * connnection was created
 * @author Boxilla
 *
 */
public class CreateConnection extends StartupTestCase2{

	
	private PeripheralsMethods per = new PeripheralsMethods();
	
	@Test
	public void test() throws InterruptedException {
		for(int x =1; x < 2; x++) {
			//per.navigateToPeripherals(driver);
			//per.discover(driver);
	
				String names = per.getDiscovertedDeviceDetails(driver, "10.211.129.183", Peripherals.DISCOVERY.STATE);
				
			//Assert.assertTrue(s.contains("10.211.128.72"));
		}
	}
	
//	final static Logger log = Logger.getLogger(CreateConnection.class);
//	@BeforeClass
//	public void beforeClass() {
//		
//		Ssh shell = new Ssh("root", "barrow1admin_12", "10.211.129.86");
//		shell.loginToServer();
//		shell.sendCommand("mkdir /usr/automationScripts");
//		//Copy script to device
//		ScpTo copy = new ScpTo();
//		copy.scpTo("emeraldScripts/click_connection.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "click_connection.sh");
//		copy.scpTo("emeraldScripts/emeraldAutomation.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "emeraldAutomation.sh");
//		copy.scpTo("emeraldScripts/login.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "login.sh");
//		copy.scpTo("emeraldScripts/tabText.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "tabText.sh");
//		copy.scpTo("emeraldScripts/write.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "write.sh");
//		
//		
//		//fix the file for unix and make it runnable
//		shell.sendCommand("dos2unix /usr/automationScripts/*");
//		shell.sendCommand("chmod 777 /usr/automationScripts/*");
//		
//		//shell.sendCommand("/usr/emeraldAutomation.sh");
//		shell.disconnect();
//	}
//	@Test
//	public void test01() {
//		System.out.println("test");
////		Ssh shell = new Ssh("root", "barrow1admin_12", "10.211.129.86");
////		shell.loginToServer();
////		String output = shell.sendCommand("/usr/emeraldAutomation.sh \"\\x05\" \"\\x05\" \"\\x05\" \"\\x05\" \"\\x05\"");
////		//System.out.println(output);
////		shell.disconnect();
//		//write the output of the xml to a local file
////		String fileName = "C:\\temp\\test01.xml";
////		Utilities.writeStringToFile(output, fileName);
////		Connection testConnection = new Connection();
////		testConnection.setConnectionType("Private");
////		testConnection.setViaTx("true");
////		testConnection.setName("test1");
////		testConnection.setIp_address("10.211.129.86");
////		XMLParserSAX sax = new XMLParserSAX();
////		ArrayList<Connection> connections = sax.go(fileName, "Connection");
////		for(Connection c : connections) {
////			if(!c.getName().contains("Default")) {
////				boolean check = testConnection.getName().equals(c.getName()) &&
////						testConnection.getViaTx().equals(c.getViaTx()) &&
////						testConnection.getIp_address().equals(c.getIp_address());
////				Assert.assertTrue(check, "Connection details did not match");
////			}
////		}
//	}
//	@Override
//	@BeforeMethod(alwaysRun = true)
//	@Parameters({ "browser" })
//	public void login(String browser, Method method) {
//		
//	}
//	
//	/**
//	 * Tests in the class do not use the browser so 
//	 * this superclass method gets overridden and logout removed.
//	 * Also no screen shot is taken on fail
//	 */
//	@Override
//	@AfterMethod(alwaysRun = true)
//	public void logout(ITestResult result) {
//		log.info("********* @ After Method Started ************");
//		// Taking screen shot on failure
//		//String url = "https://" + boxillaManager + "/";
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		
//		//print result
//		if(ITestResult.FAILURE == result.getStatus())
//			log.info(result.getName() + " :FAIL");
//		
//		if(ITestResult.SKIP == result.getStatus())
//			log.info(result.getName() + " :SKIP");
//		
//		if(ITestResult.SUCCESS == result.getStatus())
//			log.info(result.getName() + " :PASS");
//		
//		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
//			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
//			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
//		}
//	}
//	
////	public static void main(String[]args) throws InterruptedException {
////		Ssh shell = new Ssh("root", "barrow1admin_12", "10.211.129.86");
////		shell.loginToServer();
////		shell.sendCommand("mkdir /usr/automationScripts");
////		//Copy script to device
////		ScpTo copy = new ScpTo();
////		copy.scpTo("emeraldScripts/click_connection.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "click_connection.sh");
////		copy.scpTo("emeraldScripts/emeraldAutomation.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "emeraldAutomation.sh");
////		copy.scpTo("emeraldScripts/login.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "login.sh");
////		copy.scpTo("emeraldScripts/tabText.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "tabText.sh");
////		copy.scpTo("emeraldScripts/write.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "write.sh");
////		copy.scpTo("emeraldScripts/tab.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "tab.sh");
////		copy.scpTo("emeraldScripts/enter.sh", "root", "10.211.129.86", "barrow1admin_12", "/usr/automationScripts/", "enter.sh");
////		
////		//fix the file for unix and make it runnable
////		shell.sendCommand("dos2unix /usr/automationScripts/*");
////		shell.sendCommand("chmod 777 /usr/automationScripts/*");
////		
////		//shell.sendCommand("/usr/emeraldAutomation.sh");
////		
////		Properties keyProperties = new Properties();
////		try {
////			InputStream in = new FileInputStream("char.properties");
////			keyProperties.load(in);
////			in.close();
////		} catch (IOException e) {
////			log.info("Properties file failed to load");
////		}
////		//System.out.println(keyProperties.getProperty("A"));
////		String location = "/usr/automationScripts/";
////		//properties lookup 
////		String output = shell.sendCommand(location + "login.sh" + convertToInput("admin", keyProperties));
////		Thread.sleep(1000);
////		shell.sendCommand(location + "click_connection.sh");
////		String connectionName = "brendantest";
////		
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");		
////		Thread.sleep(200);
////		shell.sendCommand(location + "tabText.sh" + convertToInput(connectionName, keyProperties));
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tabText.sh" + convertToInput("10.211.129.86", keyProperties));
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "enter.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "enter.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "tab.sh");
////		Thread.sleep(200);
////		shell.sendCommand(location + "enter.sh");
////		
////		shell.disconnect();
////
////		
////	}
//	public static String convertToInput(String text, Properties keyProperties) {
//		String values = " ";
//		char[] testChar = text.toCharArray();
//		for(char a : testChar) {
//			values  = values + keyProperties.getProperty(Character.toString(a)) + " ";
//		}
//		return values;
//	}
}
