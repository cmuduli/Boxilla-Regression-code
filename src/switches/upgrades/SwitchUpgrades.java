package switches.upgrades;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.SwitchMethods;
import switches.status.SwitchStatus;
import testNG.Utilities;

public class SwitchUpgrades extends StartupTestCase {

	final static Logger log = Logger.getLogger(SwitchUpgrades.class);
	
	private SwitchMethods switches = new SwitchMethods();
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_uploadDellSwitchVersion() throws InterruptedException {
		log.info("Attempting to upload dell software");
		switches.uploadSwitchFile(driver, "C:\\Test_Workstation\\SeleniumAutomation\\resources\\switch-10.4.2.0.bbx", "10.4.2.0", true);
		log.info("Dell software uploaded successfully");
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test02_activateDellSwitchVersion() throws InterruptedException {
		log.info("Attempting to activate release version");
		switches.activateSwitchReleaseVersion(driver, "10.4.2.0");
		log.info("Asserting version mis match");
		Assert.assertTrue(switches.isVersionMismatch(driver, dellSwitchIp), "Boxilla was not reporting a version mistatch");
		log.info("Assertion successful");
	}
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test03_upgradeDellSwitch() throws InterruptedException
	{
		log.info("Attepting to upgrade the switch");
		switches.upgradeSwitch(driver, dellSwitchIp, "10.4.2.0");
	}
	
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
//	public void test03_checkVersionMismatch() throws InterruptedException {
//		log.info("Attempting to check version mismatch");
////		switches.uploadSwitchFile(driver, "C:\\Test_Workstation\\SeleniumAutomation\\resources\\switch-10.4.1.2.bbx", "10.4.1.2", true);
////		switches.activateSwitchReleaseVersion(driver, "10.4.1.2");
//		log.info("Asserting version mis match");
//		Assert.assertTrue(switches.isVersionMismatch(driver, dellSwitchIp), "Boxilla was not reporting a version mistatch");
//		log.info("Assertion successful");
//	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test04_deleteDellSwitchRelease() throws InterruptedException {
		log.info("Attempting to delete dell switch release");
		switches.deleteDellSwitchVersion(driver, "10.4.2.0");
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test05_uploadInvalidDellUpgrade() throws InterruptedException {
		log.info("Attempting to upload invalid dell software");
		switches.uploadSwitchFile(driver, "C:\\Test_Workstation\\SeleniumAutomation\\resources\\TX_DTX_V4.2.1_r4864.clu", "10.4.2.0", false);
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test06_upgradeToCurrentLevel() throws InterruptedException {
		log.info("Attempting to active software already installed on switch");
		switches.uploadSwitchFile(driver, "C:\\Test_Workstation\\SeleniumAutomation\\resources\\switch-10.5.3.0.bbx", "10.5.3.0", true);
		switches.activateSwitchReleaseVersion(driver, "10.5.3.0");
		String state = switches.getSwitchState(driver, dellSwitchIp);
		log.info("Switch state = " + state);
		Assert.assertTrue(state.equals("Idle"), "Switch state was not idle after uploading the same version, actual:" + state);	
	}
//	@Override
//	@AfterClass(alwaysRun=true)
//	public void afterClass() throws InterruptedException {
//		log.info("**** Starting test teardown for " + this.getClass().getSimpleName() + " ****");
//		try {
//			cleanUpLogin();
//			switches.unmanageSwitch(driver, dellSwitchIp);
//			cleanUpLogout();
//			super.afterClass();
//			printSuitetDetails(true);
//		}catch(Exception | AssertionError e) {
//			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
//			e.printStackTrace();
//			cleanUpLogout();
//		}
//	}
}
