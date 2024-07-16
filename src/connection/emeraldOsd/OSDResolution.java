package connection.emeraldOsd;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import methods.DevicesMethods;
import methods.EmeraldOsd;

public class OSDResolution extends StartupTestCase2 {

	final static Logger log = Logger.getLogger(OSDResolution.class);
	private EmeraldOsd emerald;
	
	
	
	@Test
	public void test01_changeRes800x600() throws InterruptedException {
		String resolution = "800x600";
		emerald.changeOsdResolution(resolution);
		String realResolution = emerald.checkEmeraldOsdResolution(rxIp);
		Assert.assertTrue(resolution.equals(realResolution), "Resolutions did not match");
	}
	
	@Test
	public void test02_changeRes1280x720() throws InterruptedException {
		String resolution = "1280x720";
		emerald.changeOsdResolution(resolution);
		String realResolution = emerald.checkEmeraldOsdResolution(rxIp);
		Assert.assertTrue(resolution.equals(realResolution), "Resolutions did not match");
	}
	
	@Test
	public void test03_changeRes1280x1024() throws InterruptedException {
		String resolution = "1280x1024";
		emerald.changeOsdResolution(resolution);
		String realResolution = emerald.checkEmeraldOsdResolution(rxIp);
		Assert.assertTrue(resolution.equals(realResolution), "Resolutions did not match");
	}
	
	@Test
	public void test04_changeRes1920x1080() throws InterruptedException {
		String resolution = "1920x1080";
		emerald.changeOsdResolution(resolution);
		String realResolution = emerald.checkEmeraldOsdResolution(rxIp);
		Assert.assertTrue(resolution.equals(realResolution), "Resolutions did not match");
	}
	
	@Test
	public void test05_changeResAuto() throws InterruptedException {
		String resolution = "3840x2160";		//auto uses resolution of connection
		String connectionName = "restest";
		emerald.changeOsdResolution("auto");
		emerald.createConnection(connectionName, txIp, "tx", "", "", "", "", false, false, false);
		emerald.connectToConnection(1);
		Thread.sleep(5000);
		emerald.gracefullyKillConnection(rxIp);
		Thread.sleep(5000);
		String realResolution = emerald.checkEmeraldOsdResolution(rxIp);
		Assert.assertTrue(resolution.equals(realResolution), "Resolutions did not match");
		
		//clean up 
		emerald.removeConnection(1);
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		emerald = new EmeraldOsd(txIp, "root", "barrow1admin_12");
		
	}
	
	/**
	 * We dont need to log into boxilla after each method in this suite
	 * so override with an empty method
	 */
	@Override
	@BeforeMethod(alwaysRun = true)
	//@Parameters({ "browser" })
	public void login(Method method) {
	
	}
	
	@Override
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			log.info(result.getName() + " :FAIL");
		
		if(ITestResult.SKIP == result.getStatus())
			log.info(result.getName() + " :SKIP");
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	@Override
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		printSuitetDetails(true);
//		//reboot the device
//	  DevicesMethods device = new DevicesMethods();
//	  device.rebootDeviceSSH(txIp, "root", "barrow1admin_12");
//	  device.rebootDeviceSSH(rxIp, "root", "barrow1admin_12");
	}
}
