package icron;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.PeripheralsMethods;
import objects.ActiveConnectionElements;
import objects.Peripherals;
import testNG.Utilities;

/**
 * This class contains test cases for discovering ICRON devices in Boxilla
 * @author Boxilla
 *
 */
public class Discovery extends StartupTestCase {

	final static Logger log = Logger.getLogger(Discovery.class);
	PeripheralsMethods perMethods = new PeripheralsMethods();
	Peripherals perimethods=new Peripherals();
	
	/**
	 * Test that will discover LEX icron devices by their MAC addresses
	 * @throws InterruptedException 
	 */
	
	@Test
	public void test00_checkDeviceOnline() throws InterruptedException 
	{
		
		perMethods.discover(driver);
		Thread.sleep(3000);
		String text=SeleniumActions.seleniumGetText(driver, perimethods.getDeviceOnlineCount());
		log.info("Device  count is "+ text);
		Assert.assertTrue(text.contains("0"), 
				"The number of devices online did not equal 0, actual text: " + text);
		
	}
	@Test
	public void test01_discoverLexDevicesByMac () throws InterruptedException {
		String text = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.MAC);
		Assert.assertTrue(text.equals(getLexMac()), "MAC addresses did not match. Expected:" + getLexMac() + " , actual:" + text);
		text = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.TYPE);
		Assert.assertTrue(text.equals("Local"), "Extender Type did not match. Expected: Local , actual:" + text);
		
		text = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.STATE);
		Assert.assertTrue(text.equals("Unbonded"), "Extender state did not match. Expected: Unbonded , actual:" + text);
	}
	/**
	 * Test that will discover REX icron devices by their MAC addresses
	 * @throws InterruptedException 
	 */
	@Test
	public void test02_discoverRex1ByMac () throws InterruptedException {
		
		//rex1
		String text = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.MAC);
		Assert.assertTrue(text.equals(getRex1Mac()), "MAC addresses did not match. Expected:" + getLexMac() + " , actual:" + text);
		
		text = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.TYPE);
		Assert.assertTrue(text.equals("Remote"), "Extender type did not match. Expected Remote , actual:" + text);
		
		text = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.STATE);
		Assert.assertTrue(text.equals("Unbonded"), "Extender state did not match. Expected: Unbonded , actual:" + text);		
	}
	/**
	 * Test that will discover REX icron devices by their MAC addresses
	 * @throws InterruptedException 
	 */
	@Test
	public void test03_discoverRex2ByMac () throws InterruptedException {
		
		//rex2
		String text = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.MAC);
		Assert.assertTrue(text.equals(getRex2Mac()), "MAC addresses did not match. Expected:" + getLexMac() + " , actual:" + text);
		
		text = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.TYPE);
		Assert.assertTrue(text.equals("Remote"), "Extender type did not match. Expected Remote , actual:" + text);
		
		text = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.STATE);
		Assert.assertTrue(text.equals("Unbonded"), "Extender state did not match. Expected: Unbonded , actual:" + text);		
	}
	/**
	 * Test that will change the IP address of a LEX icron device
	 * @throws InterruptedException 
	 */
	@Test
	public void test04_changeLexIp() throws InterruptedException {
		String newIp = Ssh.getFreeIp();
		System.out.println(newIp);
		String oldIp = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.IP);
		log.info("The Current IP of The LEX is:" + oldIp);
		perMethods.editDeviceNetworkDiscover(driver, getLexMac(), newIp, "10.231.128.1", "255.255.248.0");
		String search = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.IP);
		Assert.assertTrue(search.equals(newIp), "Device IP was not changed. Expected " + newIp + " , actual:" + search);
		Thread.sleep(4000);
		log.info("Changing LEX IP to old IP");
		perMethods.editDeviceNetworkDiscover(driver, getLexMac(), oldIp, "10.231.128.1", "255.255.248.0");
		setLexIp(newIp);
	}
	/**
	 * Test that will change the IP address of a REX icron device
	 * @throws InterruptedException 
	 */
	@Test
	public void test05_changeRex1Ip() throws InterruptedException {
		String newIp = Ssh.getFreeIp();
		log.info(newIp);
		String oldIp = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.IP);
		log.info("The Current IP of The Rex1 is:" + oldIp);
		perMethods.editDeviceNetworkDiscover(driver, getRex1Mac(), newIp, "10.231.128.1", "255.255.248.0");
		String search = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.IP);
		Assert.assertTrue(search.equals(newIp), "Device IP was not changed. Expected " + newIp + " , actual:" + search);
		Thread.sleep(4000);
		log.info("Changing Rex1 IP to old IP");
		perMethods.editDeviceNetworkDiscover(driver, getRex1Mac(), oldIp, "10.231.128.1", "255.255.248.0");
		setRex1Ip(newIp);
	}
	/**
	 * Test that will change the IP address of a REX icron device
	 * @throws InterruptedException 
	 */
	@Test
	public void test06_changeRex2Ip() throws InterruptedException {
		String newIp = Ssh.getFreeIp();
		String oldIp = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.IP);
		log.info("The Current IP of The Rex2 is:" + oldIp);
		perMethods.editDeviceNetworkDiscover(driver, getRex2Mac(), newIp, "10.231.128.1", "255.255.248.0");
		String search = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.IP);
		Assert.assertTrue(search.equals(newIp), "Device IP was not changed. Expected " + newIp + " , actual:" + search);
		Thread.sleep(4000);
		log.info("Changing Rex2 IP to old IP");
		perMethods.editDeviceNetworkDiscover(driver, getRex2Mac(), oldIp, "10.231.128.1", "255.255.248.0");
		setRex2Ip(newIp);
	}
	
	/**
	 * Test that will bond a LEX icron device to a transmitter
	 * @throws InterruptedException 
	 */
	@Test 
	public void test07_bondLexToTX() throws InterruptedException {
		perMethods.editBonding(driver, getLexMac(), "LEX1", txEmeraldName);
		boolean isBonded = perMethods.isBonded(driver, getLexMac());
		Assert.assertTrue(isBonded == true, "Device was not bonded");
	}
	/**
	 * Test that will bond a REX icron device to a receiver
	 * @throws InterruptedException 
	 */
	@Test
	public void test08_bondRex1ToRX() throws InterruptedException {
		perMethods.editBonding(driver, getRex1Mac(), "REX1", rxEmeraldName );
		boolean isBonded = perMethods.isBonded(driver, getRex1Mac());
		Assert.assertTrue(isBonded == true, "Device was not bonded");
	}
	/**
	 * Test that will bond a REX icron device to a receiver
	 * @throws InterruptedException 
	 */
	@Test
	public void test09_bondRex2ToRX() throws InterruptedException {
		perMethods.editBonding(driver, getRex2Mac(), "REX2", dualRxName);
		boolean isBonded = perMethods.isBonded(driver, getRex2Mac());
		Assert.assertTrue(isBonded == true, "Device was not bonded");
	}
	@Test
	public void test10_bondedDeviceOnline() 
	{
		
		perMethods.navigateToSettingsTab(driver);
		String text=SeleniumActions.seleniumGetText(driver, perimethods.getDeviceOnlineCount());
		log.info("The number of device online is "+ text);
		Assert.assertTrue(text.contains("3"), 
				"The number of devices online did not equal 3, actual text: " + text);
	}
	
	/**
	 * This method will run once, after all the tests in this class finish. This will
	 * unbond any icron devices from KVM devices
	 */
	@AfterClass
	public void afterClass() {
		
		try {
			cleanUpLogin();
			perMethods.settingsUnbondDevice(driver, getLexMac());
			String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.STATE);
			Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			
			perMethods.settingsUnbondDevice(driver, getRex1Mac());
			 state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.STATE);
			Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			
			perMethods.settingsUnbondDevice(driver, getRex2Mac());
			 state = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.STATE);
			Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			
			cleanUpLogout();
			super.afterClass();
			
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		
		
		
	}
	
}
