package icron;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.PeripheralsMethods;
import objects.Peripherals;
import objects.Peripherals.DISCOVERY;
import objects.Peripherals.SETTINGS;
import testNG.Utilities;
/**
 * This class contains tests for checking and editing the 
 * settings of ICRON devices
 * @author Boxilla
 *
 */
public class Settings extends StartupTestCase{
	
	final static Logger log = Logger.getLogger(Settings.class);
	
	private PeripheralsMethods perMethods = new PeripheralsMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	String lexName = "LEX1_BONDED";
	String rexName = "REX1_BONDED";
	
	/**
	 * Test that will bond a LEX to a transmitter and then check 
	 *  the details in Boxilla UI
	 * @throws InterruptedException 
	 */
	@Test
	
	public void test01_checkBondedLexDetails() throws InterruptedException {
		perMethods.editBonding(driver, getLexMac(), "LEX1_BONDED", singleTxName);
		
		String name = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.EXT_NAME);
		String type = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.TYPE);
		String state = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.STATE);
		String ip = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.IP);
		String deviceName = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE);
		String deviceIp = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE_IP);
		
		log.info("Checking name");
		Assert.assertTrue(name.equals(lexName), "Extender name did not match. Expected:" + lexName + " , actual:" + name);
		log.info("Checking type");
		Assert.assertTrue(type.equals("Local"), "Extender type did not match. Expected: Local , actual:" + type);
		log.info("Checking state");
		Assert.assertTrue(state.equals("Online"), "Extender state did not match. Expected: Online , actual:" + state);
		log.info("Checking IP address");
		Assert.assertTrue(ip.equals(getLexIp()), "Extender IP did not match. Expected:" + getLexIp() + " , actual:" + ip);
		log.info("Checking bonded device name");
		Assert.assertTrue(deviceName.equals(singleTxName), "Bonded device name did not match. Expected:" + singleTxName + " , actual:" + deviceName);
		log.info("Checking bonded device IP");
		Assert.assertTrue(deviceIp.equals(txIp), "Bonded device IP did not match. Expected:" + txIp + " , actual:" + deviceIp);
	}
	/**
	 * Test that bonds a REX to a receiver and checks
	 *  the details in the Boxilla UI
	 * @throws InterruptedException 
	 */
	@Test
	public void test02_checkBondedRexDetails() throws InterruptedException {
		perMethods.editBonding(driver, getRex1Mac(), rexName, singleRxName);
		
		String name = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.EXT_NAME);
		String type = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.TYPE);
		String state = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.STATE);
		String ip = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.IP);
		String deviceName = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE);
		String deviceIp = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE_IP);
		
		log.info("Checking name");
		Assert.assertTrue(name.equals(rexName), "Extender name did not match. Expected:" + rexName + " , actual:" + name);
		log.info("Checking type");
		Assert.assertTrue(type.equals("Remote"), "Extender type did not match. Expected: Remote , actual:" + type);
		log.info("Checking state");
		Assert.assertTrue(state.equals("Online"), "Extender state did not match. Expected: Online , actual:" + state);
		log.info("Checking IP address");
		Assert.assertTrue(ip.equals(getRex1Ip()), "Extender IP did not match. Expected:" + getRex1Ip() + " , actual:" + ip);
		log.info("Checking bonded device name");
		Assert.assertTrue(deviceName.equals(singleRxName), "Bonded device name did not match. Expected:" + singleRxName + " , actual:" + deviceName);
		log.info("Checking bonded device IP");
		Assert.assertTrue(deviceIp.equals(rxIp), "Bonded device IP did not match. Expected:" + rxIp + " , actual:" + deviceIp);
	}
	
	/**
	 * Test that checks the details of an LEX device
	 */
	@Test
	public void test03_checkLexExtenderDetails() {
		String details = perMethods.getDeviceDetails(driver, getLexMac());
		log.info("Checking Name");
		Assert.assertTrue(details.contains(lexName), "Extender details did not contain lex details");
		log.info("Checking MAC");
		Assert.assertTrue(details.contains(getLexMac()), "Extender details did not contain mac");
		log.info("Checking IP");
		Assert.assertTrue(details.contains(getLexIp()), "Extender details did not contain IP address");
	}
	/**
	 * Test that checks the details of a REX device
	 */
	@Test
	public void test04_checkRexExtenderDetails() {
		String details = perMethods.getDeviceDetails(driver, getRex1Mac());
		log.info("Checking Name");
		Assert.assertTrue(details.contains(rexName), "Extender details did not contain rex details");
		log.info("Checking MAC");
		Assert.assertTrue(details.contains(getRex1Mac()), "Extender details did not contain mac");
		log.info("Checking IP");
		Assert.assertTrue(details.contains(getRex1Ip()), "Extender details did not contain IP address");
	}
	
	/**
	 * Test that pings a LEX device from Boxilla
	 */
	@Test
	public void test05_pingLex() {
		perMethods.pingDevice(driver, getLexMac());
	}
	/**
	 * Test that pings a REX device from Boxilla
	 */
	@Test
	public void test06_pingRex() 
	{
		perMethods.pingDevice(driver, getRex1Mac());
	}
	/**
	 * Test that reset a LEX device from Boxilla
	 * @throws InterruptedException 
	 */
	@Test
	public void  test07_resetLex() throws InterruptedException 
	{
		perMethods.resetDevice(driver, getLexMac());
	}
	/**
	 * Test that reset a REX device from Boxilla
	 * @throws InterruptedException 
	 */
	
	@Test
	public void  test08_resetRex() throws InterruptedException 
	{
		perMethods.resetDevice(driver, getRex1Mac());
	}
	
	/**
	 * Test that edits the bonding details of an LEX device
	 */
	@Test
	public void test09_editLexBonding() {
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(singleTxName));
		
		perMethods.settingsEditBonding(driver, getLexMac(), lexName, dualTxName);
		bondedDevice = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualTxName), "Bonded device name did not match. Expected:" + dualTxName + " , actual:" + bondedDevice);
		String bondedDeviceIp = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE_IP);
		Assert.assertTrue(bondedDeviceIp.equals(txIpDual), "Bonded device IP did not match. Expected:" + txIpDual + " , actual:" + bondedDeviceIp);
	}
	/**
	 * Test that edits the bonding details of a REX device
	 */
	@Test
	public void test10_editRexBonding() {
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(singleRxName));
		
		perMethods.settingsEditBonding(driver, getRex1Mac(), rexName, dualRxName);
		bondedDevice = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualRxName), "Bonded device name did not match. Expected:" + dualRxName + " , actual:" + bondedDevice);
		String bondedDeviceIp = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE_IP);
		Assert.assertTrue(bondedDeviceIp.equals(rxIpDual), "Bonded device IP did not match. Expected:" + rxIpDual + " , actual:" + bondedDeviceIp);
	}
	
	/**
	 * Test that unbonds a LEX from a transmitter
	 * @throws InterruptedException 
	 */
	@Test
	public void test11_unbondLex() throws InterruptedException {
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualTxName));
		perMethods.settingsUnbondDevice(driver, getLexMac());
		
		String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Unbonded"), "State did not match. Expected Unbonded , actual:" + state);
	}
	/**
	 * Test that unbonds a REX from a receiver
	 * @throws InterruptedException 
	 */
	@Test
	public void test12_unbondRex() throws InterruptedException {
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualRxName));
		perMethods.settingsUnbondDevice(driver, getRex1Mac());
		
		String state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Unbonded"), "State did not match. Expected Unbonded , actual:" + state);
	}
	/**
	 * Test that changes a LEX device name
	 * @throws InterruptedException 
	 */
	@Test
	public void test13_changeExtenderNameLex() throws InterruptedException {
		String newLexName = "Test_Local";
		perMethods.editBonding(driver, getLexMac(), lexName, singleTxName);
		String name = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.EXT_NAME);
		log.info("Checking name");
		Assert.assertTrue(name.equals(lexName), "Extender name did not match. Expected:" + lexName + " , actual:" + name);
		perMethods.changeExtenderName(driver, getLexMac(), newLexName);
		String newName = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.EXT_NAME);
		log.info("Checking new name");
		Assert.assertTrue(newName.equals(newLexName), "Extender name did not match. Expected:" + newLexName + " , actual:" + name);
	}
	/**
	 * Test that changes a REX devices name
	 * @throws InterruptedException 
	 */
	@Test
	public void test14_changeExtenderNameRex() throws InterruptedException {
		String newRexName = "Test_Remote";
		perMethods.editBonding(driver, getRex1Mac(), rexName, singleRxName);
		String name = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.EXT_NAME);
		log.info("Checking name");
		Assert.assertTrue(name.equals(rexName), "Extender name did not match. Expected:" + rexName + " , actual:" + name);
		perMethods.changeExtenderName(driver, getRex1Mac(), newRexName);
		String newName = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.EXT_NAME);
		log.info("Checking new name");
		Assert.assertTrue(newName.equals(newRexName), "Extender name did not match. Expected:" + newRexName + " , actual:" + newName);
	}
	
	@Test
	public void test15_unmanageBondedDeviceTX() throws InterruptedException {
		String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Bonded"), "State did not match. Expected Bonded , actual:" + state);
		deviceMethods.unManageDevice(driver, txIp);
		 state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), DISCOVERY.STATE);
		 Assert.assertTrue(state.equals("Unbonded"), "State did not match. Expected Unbonded , actual:" + state);	
	}
	@Test
	public void test16_unmanageBondedDeviceRX() throws InterruptedException {
		String state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Bonded"), "State did not match. Expected Bonded , actual:" + state);
		deviceMethods.unManageDevice(driver, rxIp);
		 state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), DISCOVERY.STATE);
		 Assert.assertTrue(state.equals("Unbonded"), "State did not match. Expected Unbonded , actual:" + state);	
	}
	
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		
		printSuitetDetails(false);
		try {
			log.info("Starting test setup.... getting default icron devices IP addresses");
			cleanUpLogin();
			
			String lex = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.IP);
			setLexIp(lex);
			
			String rex1 = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.IP);
			setRex1Ip(rex1);
			
			String rex2 = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.IP);
			setRex2Ip(rex2);
			
			cleanUpLogout();
			}
		catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}

	@AfterClass(alwaysRun = true)
	public void afterClass() {
		try {
			cleanUpLogin();
			
			if(!isEmerald) {
				//RX
				discoveryMethods.discoverDevices(driver);
				discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"),
						rxIp, rxSingle.getGateway(),rxSingle.getNetmask());
				discoveryMethods.manageApplianceAutomatic(driver, rxSingle.getDeviceName(), rxSingle.getMac(),
						prop.getProperty("ipCheck"));
				
				//TX
				discoveryMethods.discoverDevices(driver);
				discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"),
						txIp, txSingle.getGateway(), txSingle.getNetmask());
				discoveryMethods.manageApplianceAutomatic(driver, txSingle.getDeviceName(), txSingle.getMac(), 
						prop.getProperty("ipCheck"));
				}
			
			if(isEmerald) {
				System.out.println("Emerald devices are being managed");
				System.out.println(txEmerald.getMac() + prop.getProperty("ipCheck") + txEmerald.getIpAddress()
				+ txEmerald.getGateway() + txEmerald.getNetmask());
				//emerald
				discoveryMethods.discoverDevices(driver);
				discoveryMethods.stateAndIPcheck(driver, txEmerald.getMac(), prop.getProperty("ipCheck"),
						txEmerald.getIpAddress(),txEmerald.getGateway(), txEmerald.getNetmask());
				discoveryMethods.manageApplianceAutomatic(driver, txEmerald.getDeviceName(), txEmerald.getMac(),
						prop.getProperty("ipCheck"));
				
				discoveryMethods.discoverDevices(driver);
				discoveryMethods.stateAndIPcheck(driver, rxEmerald.getMac(), prop.getProperty("ipCheck"),
						rxEmerald.getIpAddress(),rxEmerald.getGateway(), rxEmerald.getNetmask());
				discoveryMethods.manageApplianceAutomatic(driver, rxEmerald.getDeviceName(), rxEmerald.getMac(),
						prop.getProperty("ipCheck"));
				}
			
			super.afterClass();
			
			
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}
	
}
