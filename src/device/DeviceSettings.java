package device;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
import methods.DevicesMethods.HOTKEY;
import methods.DevicesMethods.RESOLUTION;

/**
 * Class that contains tests for Device settings
 * @author Brendan O Regan
 *
 */
public class DeviceSettings extends StartupTestCase {

	DevicesMethods methods = new DevicesMethods();
	final static Logger log = Logger.getLogger(DeviceSettings.class);

	public DeviceSettings() throws IOException {
		super();
	}
	/**
	 * Changes connection timer then checks the appliance xml file
	 * for the new value
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald", "chrome", "quick"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_changeConnectionTimer() throws InterruptedException { // Change Connection & OSD Inactivity Timer
		log.info("Test Case-65 - Change Devices Settings");
		String value = methods.changeInactivityTimer(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String output = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + output);
		String stringToCheck = "<Connection_inactivity_Timer Value='" + value + "'/>";
		log.info("Value "+ stringToCheck);
		Assert.assertTrue(output.contains(stringToCheck), "Inactiviy timer was not in xml file. Expected " + value + 
				" actual, " + output);

		log.info("Change Devices Settings - Test Case-65 Completed");
	}

	/**
	 * Test that changes the OSD timer in Boxilla and checks the appliance xml for 
	 * the new value
	 * @throws InterruptedException
	 */
	@Test(groups= {"boxillaFunctional", "smoke" ,"emerald", "chrome", "quick"},retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test02_changeOSDTimer() throws InterruptedException { // Change Connection & OSD Inactivity Timer
		log.info("Test Case-65 - Change Devices Settings");
		String value = methods.changeOSDInactivityTimer(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String output = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + output);
		String stringToCheck = "<GUI_Inactivity_Timer Value='" + value + "'/>";
		log.info("Inactive Timer value= "+ stringToCheck );
		Assert.assertTrue(output.contains(stringToCheck), "OSD timer was not in xml file. Expected " + value + 
				" actual, " + output);	
		log.info("Change Devices Settings - Test Case-65 Completed");
	}

	/**
	 * Test that disables the connection timer in Boxilla then 
	 * checks the xml for the updated value
	 * @throws InterruptedException
	 */
	@Test
	public void test03_disableConnectionTimer() throws InterruptedException {
		methods.disableInactivityTimer(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String output = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + output);
		String stringToCheck = "<Connection_inactivity_Timer Value='0'/>";
		Assert.assertTrue(output.contains(stringToCheck), "Inactiviy timer was not in xml file. Expected 0, actual, " + output);
	}

	/**
	 * Test that disables the OSD timer in Boxilla 
	 * and checks the appliance xml file for new value
	 * @throws InterruptedException
	 */
	@Test
	public void test04_disableOSDTimer() throws InterruptedException {
		methods.disableOSDTimer(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String output = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + output);
		String stringToCheck = "<GUI_Inactivity_Timer Value='0'/>";
		Assert.assertTrue(output.contains(stringToCheck), "OSD timer was not in xml file. Expected " + "0" + 
				" actual, " + output);	
	}

	/**
	 * Test that enables the functional hotkey in Boxilla and checks
	 * the appliance xml for the new value
	 * @throws InterruptedException
	 */
	@Test
	public void test05_enableFunctionalHotkey() throws InterruptedException {
		methods.enableFunctionalHotkey(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Functional_HotKey key='Enable'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Functional hotkey was not enabled xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Disables the functional hotkey in Boxilla and then 
	 * checks the appliance xml for the new value
	 * @throws InterruptedException
	 */
	@Test
	public void test06_disableFunctionalHotkey() throws InterruptedException {
		methods.disableFunctionalHotkey(driver);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Functional_HotKey key='Disable'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Functional hotkey was not disabled xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}
	/**
	 * Test which changes the RDP resolution to 1920x1080
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test07_changeRDP_ConnectionResolution1920x1080() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R1920X1080);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='1920x1080'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 1920x1080 in xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}
	/**
	 * Test which changes the RDP resolution to 1920x1200
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test08_changeRDP_ConnectionResolution1920x1200() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R1920X1200);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='1920x1200'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 1920x1200 in xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Test which changes the RDP resolution to 800x600
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test09_changeRDP_ConnectionResolution800x600() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R800X600);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='800x600'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 800x600 xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Test which changes the RDP resolution to 640x480
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test10_changeRDP_ConnectionResolution640x480() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R640X480);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='640x480'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 640x480 xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Test which changes the RDP resolution to 1024x768
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test11_changeRDP_ConnectionResolution1024x768() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R1024X768);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='1024x768'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 1024x768 xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Test which changes the RDP resolution to 1280x1024
	 *  in Boxilla, then checks the appliance xml file for 
	 *  this value
	 * @throws InterruptedException
	 */
	@Test
	public void test12_changeRDP_ConnectionResolution1280x1024() throws InterruptedException {
		methods.setRDP_ConnectionResolution(driver, RESOLUTION.R1280X1024);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Screen_Resolution Default='1280x1024'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Resolution was not set to 1280x1024 xml file. Expected " + "0" + 
				" actual, " + xmlFile);
	}

	/**
	 * Test that will change the hotkey to Print Scrn on Boxilla. Appliance
	 *  xml file is then checked for this value
	 * @throws InterruptedException
	 */
	@Test
	public void test13_changeHotkeyPrintScrn() throws InterruptedException {
		methods.setHotkey(driver, HOTKEY.PRINTSCRN);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Hot_Key hotkey=''/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Hotkey was not set to PrintScrn in xml file. Expected " + stringToCheck + 
				" actual, " + xmlFile);
	}

	/**
	 * Test that will change the hotkey to Alt on Boxilla. Appliance
	 *  xml file is then checked for this value
	 * @throws InterruptedException
	 */
	@Test
	public void test14_changeHotkeyAlt() throws InterruptedException {
		methods.setHotkey(driver, HOTKEY.ALT_ALT);
		methods.recreateCloudData(rxIp, txIp);
		methods.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xmlFile = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		log.info("XML CONTENTS:" + xmlFile);
		String stringToCheck = "<Hot_Key hotkey='2'/>";
		Assert.assertTrue(xmlFile.contains(stringToCheck), "Hotkey was not set to Alt-Alt in xml file. Expected " + stringToCheck + 
				" actual, " + xmlFile);
	}
}
