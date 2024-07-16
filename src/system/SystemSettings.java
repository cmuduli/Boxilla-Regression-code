package system;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.SystemMethods;
import testNG.Utilities;

public class SystemSettings extends StartupTestCase2 {

	private String extraIP;
	private SystemMethods methods = new SystemMethods();
	final static Logger log = Logger.getLogger(SystemSettings.class);

	public SystemSettings() throws IOException {
		extraIP = prop.getProperty("extraIP2");
	}

	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_changeAudioBwLimit() throws InterruptedException { // Changing Audio BW
		methods.enableNorthboundAPI(driver);
	}

//	@Test
//	public void test02_changeDroppedFramesLimit() throws InterruptedException { // Changing Dropped Frames
//		log.info("Test Case-84 - Changing Dropped Frames Thresholds Limit");
//		methods.changeThresholdsValue(driver, "Dropped Frames", "25", "30", "65");
//		log.info("Dropped Frames changed - Test Case-84 Completed");
//	}
//
//	@Test
//	public void test03_changeFpsLimit() throws InterruptedException { // Changing Frames per second
//		log.info("Test Case-85 - Changing Frames Per Second Thresholds Limit");
//		methods.changeThresholdsValue(driver, "Frames Per Second", "55", "30", "65");
//		log.info("Dropped Frames changed - Test Case-85 Completed");
//	}
//
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
//	public void test04_changeRttLimit() throws InterruptedException { // Changing RTT
//		log.info("Test Case-86 - Changing RTT Thresholds Limit");
//		methods.changeThresholdsValue(driver, "RTT", "3", "6", "11");
//		log.info("RTT changed - Test Case-86 Completed");
//	}
//
//	@Test
//	public void test05_changeTotalBwLimits() throws InterruptedException { // Changing Total BW
//		log.info("Test Case-87 - Changing Total BW Thresholds Limit");
//		methods.changeThresholdsValue(driver, "Total BW", "55", "105", "205");
//		log.info("Total BW changed - Test Case-87 Completed");
//	}
//
//	@Test
//	public void test06_changeUsbBwLimits() throws InterruptedException { // Changing USB BW
//		log.info("Test Case-88 - Changing USB BW Thresholds Limit");
//		methods.changeThresholdsValue(driver, "USB BW", "2", "2.5", "3.5");
//		log.info("USB BW changed - Test Case-88 Completed");
//	}
//
//	@Test
//	public void test07_changeUserLatencyLimits() throws InterruptedException { // Changing User Latency
//		log.info("Test Case-89 - Changing User Latency Thresholds Limit");
//		methods.changeThresholdsValue(driver, "User Latency", "20", "25", "35");
//		log.info("User Latency changed - Test Case-89 Completed");
//	}
//
//	@Test
//	public void test08_changeVideoBwLimits() throws InterruptedException { // Changing Video BW
//		log.info("Test Case-90 - Changing Video BW Thresholds Limit");
//		methods.changeThresholdsValue(driver, "Video BW", "55", "105", "205");
//		log.info("Video BW Changed - Test Case-90 Completed");
//	}
//
//	@Test
//	public void test09_changeClockSettings() throws InterruptedException { // Changing Clock
//		log.info("Test Case-91 - Change clock setting");
//		methods.changeClock(driver);
//		log.info("Clock setting changed - Test Case-91 Completed");
//	}
//
//	@Test
//	public void test10_changeBoxillaIp() throws InterruptedException { // Change Boxilla IP
//		log.info("Test Case-92  - Change IP address of Boxilla unit");
//		methods.navigateToSystemSettings(driver);
//		// Change IP address to extra IP stored in excel
//		methods.changeNetwork(driver, extraIP, boxillaManager, extraIP);
//		log.info("Changing IP address back to old IP");
//		methods.navigateToSystemSettings(driver);
//		// Change IP address back to old IP
//		methods.changeNetwork(driver, boxillaManager, boxillaManager, extraIP);
//		log.info("Changing IP address of Boxilla - Test Case-92 Completed");
//		if(methods.getIpFail() == true) {
//			methods.setIpFail(false);
//			Assert.fail("ip change failed");
//		}
//	}
//
//	@Test(dependsOnMethods = { "test10_changeBoxillaIp" })
//	public void test11_restartBoxilla() throws InterruptedException { // Boxilla restart
//		log.info("Test Case-93 : Reboot Boxilla Unit");
//		methods.boxillaRebootAndAssert(driver);
//		log.info("Reboot Boxilla Unit : Test Case-93 Completed");
//	}
}
