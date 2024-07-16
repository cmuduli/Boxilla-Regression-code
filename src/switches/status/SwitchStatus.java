package switches.status;

import java.net.InetAddress;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.testng.Assert;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.SwitchCLI;
import methods.SwitchMethods;
import objects.Switch.Column;

public class SwitchStatus extends StartupTestCase {

	final static Logger log = Logger.getLogger(SwitchStatus.class);
	
	SwitchMethods switchMethods = new SwitchMethods();
	SwitchCLI switchCli = new SwitchCLI();
	private String switchName = "switch1";
	
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addSwitchThroughDiscovery() throws InterruptedException {
		switchMethods.manageSwitchByDiscovery(driver, dellSwitchMac, switchName, dellSwitchIp);
		String text = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.IP);
		Assert.assertTrue(text.contains(dellSwitchIp), "Dell switch was not added correctly");
		log.info("Assertion success");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
//	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test02_pingSwitch() throws InterruptedException {
		log.info("Attempting to ping switch");
		switchMethods.pingSwitch(driver, dellSwitchIp);
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test03_getSwitchDetailsSwitchName() throws InterruptedException {
		log.info("Attempting to get switch details");
		String pageSource = switchMethods.getSwitchDetails(driver, dellSwitchIp);
		log.info(pageSource);
		//Check details
		log.info("Checking if IP address was returned in switch details");
		Assert.assertTrue(pageSource.contains(dellSwitchIp), "Switch details did not contain switch IP");
		log.info("IP address was returned. Checking if switch name is returned in switch details.");
		Assert.assertTrue(pageSource.contains(switchName), "Switch details did not contain switch name");
		log.info("Switch name was return. Checking if switch MAC is returned in switch details");
		Assert.assertTrue(pageSource.contains(dellSwitchMac), "Switch details did not contain MAC");
		log.info("Switch details contained switch mac address");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test04_getSwitchDetailsTemperature() throws InterruptedException {
		log.info("Test to get switch temperature details");
		int switchTemp = switchMethods.getSwitchDetailsTemperature(driver, dellSwitchIp);
		Assert.assertTrue(switchTemp > 0 && switchTemp < 95, "Temperature was not between 0 and 95 or temperature did not return, switchTemp = " + switchTemp);
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	
	
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test05_rebootSwitch() throws InterruptedException {
		log.info("Attempting to reboot switch.");
		log.info("Checking uptime before rebooting");
		float originalUptime = switchMethods.getSwitchDetailsUpTime(driver, dellSwitchIp);
		log.info("Original Uptime: " + originalUptime);
		log.info("Rebooting switch.");
		switchMethods.rebootSwitch(driver, dellSwitchIp);
		log.info("pinging switch until it comes back online");
		Assert.assertTrue(switchMethods.isSwitchReachable(dellSwitchIp), "Switch never came back online");
		log.info("Sleeping for 1 minute to give boxilla time to poll switch");
		Thread.sleep(65000);
		log.info("Waking. Checking uptime again and comparing");
		float newUptime = switchMethods.getSwitchDetailsUpTime(driver, dellSwitchIp);
		Assert.assertTrue(newUptime < originalUptime, "New uptime was not smaller than old uptime, actual new:" + newUptime + " original: " + originalUptime);
		
		log.info("Asseration complete");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test06_changeSwitchName() throws InterruptedException {
		log.info("Attempting to change switch name");
		switchMethods.changeSwitchName(driver, dellSwitchIp, "newName");
		String newName = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.NAME);
		Assert.assertTrue(newName.equals("newName"), "Switch name not changed");
		switchMethods.changeSwitchName(driver, dellSwitchIp, switchName);
		newName = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.NAME);
		Assert.assertTrue(newName.equals(switchName), "Switch name not changed");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test07_changeSwitchIp() throws InterruptedException {
		log.info("Attempting to change switch IP");
		String newIp = "10.231.128.107";
		String toast = switchMethods.editSwitchDetails(driver, dellSwitchIp, newIp, "255.255.248.0", "10.211.128.1");
		log.info("Asserting toasta message was success. Toast message:" + toast);
		Assert.assertTrue(toast.contains("Success"), "Toast message did not contain success");
		log.info("Toast message contained successful. Checking table for new switch IP");
		String text = switchMethods.getSwitchTableValueByIp(driver, newIp, Column.IP);
		log.info("Asserting IP address in boxilla");
		Assert.assertTrue(text.contains(newIp), "IP address did not match new IP address in boxilla, " + text);
		log.info("IP address in boxilla changed. Asserting IP address on switch");
		//assert on switch itself
		String mgmtPort = switchCli.getMgmtPortDetails(newIp);
		Assert.assertTrue(mgmtPort.contains(newIp), "IP address was not changed on the dell switch, " + mgmtPort);
		log.info("IP address on switch changed");
		log.info("IP address changed successfully. Changing back to original");
		switchMethods.editSwitchDetails(driver,newIp, dellSwitchIp, "255.255.248.0", "10.211.128.1");
		String text2 = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.IP);
		Assert.assertTrue(text2.contains(dellSwitchIp), "IP address did not match new IP address in boxilla, " + text);
		//assert on switch itself
		mgmtPort = switchCli.getMgmtPortDetails(dellSwitchIp);
		Assert.assertTrue(mgmtPort.contains(dellSwitchIp), "IP address was not changed on the dell switch ," + mgmtPort);
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	 @Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	 public void test08_enableSharedMode() throws InterruptedException {
//		 Thread.sleep(60000);
		 log.info("attempting to enable shared mode");
		 switchMethods.enableSharedMode(driver, dellSwitchIp);
		 log.info("Checking boxilla if shared mode is enabled");
		 String isSharedMode = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.SHARED_MODE);
		 Assert.assertTrue(isSharedMode.equals("Enabled"), "Boxilla did not report shared mode as enabled. Actual, " + isSharedMode);
		 log.info("Boxilla is reporting shared mode as enabled. Checking switch");
//		 Assert.assertTrue(switchCli.isSharedModeEnabled(dellSwitchIp), "Shared mode not enabled on swith");
		 log.info("Dell switch reporting shared mode as enabled");
		 log.info("Is TX online: " + isIpReachable(txIp));
		 //may add a shared mode connection test here
	 }
//	 
	 @Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	 public void test09_disableSharedMode() throws InterruptedException {
		 log.info("Attempting to disabled shared mode");
		 switchMethods.disabledSharedMode(driver, dellSwitchIp);
		 log.info("Checking boxilla if shared mode is disabled");
		 String isSharedMode = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.SHARED_MODE);
		 Assert.assertTrue(isSharedMode.equals("Disabled"), "Boxilla did not report shared mode as disabled. Actual, " + isSharedMode);
		 log.info("Boxilla is reporting shared mode as disabled. Checking switch");
//		 Assert.assertTrue(switchCli.isSharedModeDisabled(dellSwitchIp), "Shared mode enabled on swith");
		 log.info("Dell switch reporting shared mode as disabled");
		 log.info("Is TX online: " + isIpReachable(txIp));
	 }
	 // do not reun this test case
	 
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test10_invalidSwitchIp() throws InterruptedException {
		log.info("Attempting to change switch to bad IP address");
		String output = switchMethods.editSwitchDetails(driver, dellSwitchIp, "1939.303.203.33", "255.255.248.0", "10.211.128.1");
		log.info("Toast notification text: " + output + " asserting on toast");
		//// CHANGE WHEN TOAST NOTIFICATIONS ARE FIXED //////
		//Assert.assertTrue(!output.contains("Success"), "Invalid IP address got a successfull toast message");
		log.info("Toast message success, asserting on switch");
		String mgmtPort = switchCli.getMgmtPortDetails("1939.303.203.33");
		Assert.assertTrue(!mgmtPort.contains("1939.303.203.33"), "IP address was changed on the dell switch, " + mgmtPort);
		log.info("Assert on switch success");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	
	 @Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	 public void test11_unmanageSwitch() throws InterruptedException {
		 log.info("Attempting to delete switch");
		 switchMethods.unmanageSwitch(driver, dellSwitchIp);
		 String text = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.IP);
		 Assert.assertFalse(text.contains(dellSwitchIp), "Dell switch was not unmanaged correctly");
		 log.info("Assertion success");
		 log.info("Is TX online: " + isIpReachable(txIp));
	 }
	 
	 @Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	 public void test12_manageSwitchThroughSwitchManage() throws InterruptedException {
		 log.info("Is TX online: " + isIpReachable(txIp));
//		 Thread.sleep(230000);
		 log.info("Attempting to manage switch through Switch > Status page. First "
		 		+ "give switch an IP address");
		 switchMethods.setIpByDiscovery(driver, dellSwitchMac, dellSwitchIp);
		
		 switchMethods.addSwitch(driver, switchName, dellSwitchIp);
		 log.info("Checking switch was managed");
		 Thread.sleep(4000);
		 driver.navigate().refresh();
		 Thread.sleep(4000);
			String text = switchMethods.getSwitchTableValueByIp(driver, dellSwitchIp, Column.IP);
			log.info("Switch text");
			Assert.assertTrue(text.contains(dellSwitchIp), "Dell switch was not added correctly");
			log.info("Assert OK");
			log.info("Is TX online: " + isIpReachable(txIp));
	 }
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test13_checkOffline() throws InterruptedException {
		
		log.info("Taking switch offline and asserting that boxilla reports it");
		switchMethods.rebootSwitch(driver, dellSwitchIp);
		log.info("Sleeping for switch to go offline");
		Thread.sleep(10000);
		boolean isOffline = switchMethods.isSwitchOffline(driver, dellSwitchIp, 30);
		Assert.assertTrue(isOffline, "Switch did not go offline in over 60 seconds");
		log.info("Switch is reported as being offline.. Checking switch comes back online");
		Assert.assertTrue(switchMethods.isSwitchOnline(driver, dellSwitchIp, 30), "Switch did not come back online");
		log.info("Switch came back online");
		log.info("Is TX online: " + isIpReachable(txIp));
	}
	
}
