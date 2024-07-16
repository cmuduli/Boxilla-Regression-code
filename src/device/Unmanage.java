package device;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
/**
 * This class contains test cases for unmanaging devices from Boxilla
 * @author Boxilla
 *
 */
public class Unmanage extends StartupTestCase2 {

	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	private DevicesMethods device = new DevicesMethods();
	final static Logger log = Logger.getLogger(Unmanage.class);
	private String deviceName = "";
	private String deviceMac = "";

	/**
	 * Test that will unmanage a device from Boxilla 
	 * and check device has been removed from Boxilla
	 * @throws InterruptedException
	 * @throws IOException 
	 */
	@Test
	public void test01_unmanageManage() throws InterruptedException, IOException {
		
		log.info("Unmanaging device");
//		resetUnmangeApi(boxillaManager);
		device.unManageDevice(driver, rxIp);
//		discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser, boxillaRestPassword,deviceMac,rxIp);
		log.info("Device unmanaged. Waiting 60 seconds for device to reboot then trying to manage again");
		Thread.sleep(5000);
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, deviceMac, prop.getProperty("ipCheck"),
				rxIp, rxSingle.getGateway(),rxSingle.getNetmask());
		log.info("managing device");
//		discoveryMethods.manageDevice1(driver,boxillaManager, boxillaRestUser, boxillaRestPassword, rxIp, deviceName);
		discoveryMethods.manageApplianceAutomatic(driver, deviceName, deviceMac,
				prop.getProperty("ipCheck"));
		log.info("Device re-managed successfully");

	}

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {

		if(StartupTestCase.isEmerald) {
			deviceName = rxEmerald.getDeviceName();
			deviceMac = rxEmerald.getMac();
		}else {
			deviceName = rxSingle.getDeviceName();
			deviceMac = rxSingle.getMac();
		}

	}

}
