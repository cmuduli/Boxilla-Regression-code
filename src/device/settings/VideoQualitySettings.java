package device.settings;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import extra.StartupTestCase2;
import methods.DevicesMethods;

public class VideoQualitySettings extends StartupTestCase2 {
	
	final static Logger log = Logger.getLogger(VideoQualitySettings.class);
	
	private DevicesMethods deviceMethods = new DevicesMethods();
	
	@Test
	public void test01_checkBest_Quality () throws InterruptedException {
		deviceMethods.setUniquePropertyTx(driver, txIp, "Video Quality", "Best Quality");
		String output = deviceMethods.getSettingDataFromTable(driver, txIp, "Video Quality");
		log.info("Best quality value: " + output);
		Assert.assertTrue(output.equals("Best Quality"), "Value did not equal best quality, actual " + output);
	}

}
