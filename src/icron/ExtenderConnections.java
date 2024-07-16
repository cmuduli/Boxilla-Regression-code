package icron;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.PeripheralsMethods;
import objects.ActiveConnectionElements;
import objects.Connections;
import objects.Peripherals;
import objects.Peripherals.SETTINGS;
import testNG.Utilities;

/**
 * This class contains tests that will launch connections with ICRON devices
 * attached
 * 
 * @author Boxilla
 *
 */
public class ExtenderConnections extends StartupTestCase{

	final static Logger log = Logger.getLogger(ExtenderConnections.class);
	private PeripheralsMethods perMethods = new PeripheralsMethods();
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods device = new DevicesMethods();

	private String presetName2 = "secondPreset";
	private String presetName = "firstPreset";
	private String privateConnectionName = "icronPrivate1";
	private String sharedConnectionName = "icronShared";
	private String privateConnection2Name = "icronPrivate2";
	private String privateConnection3Name = "icronPrivate3";
	private String lexName = "LEX1";
	private String lex2Name = "LEX2";
	private String rex1Name = "REX1";
	private String rex2Name = "REX2";
	
	SoftAssert Assert1=new SoftAssert();

	/**
	 * Launch a private connection with ICRON devices attached
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test01_privateConnection() throws InterruptedException {
		bondLexToSingleTx();
		bondRex1ToSingleRx();

		// create connection
		createConnection(privateConnectionName, singleRxName);
		String icronDestination = perMethods.getIcronPrivateDestination(driver);
		Assert.assertTrue(icronDestination.equals(rex1Name),
				"Icron destination did not match. Expected:" + rex1Name + " , Actual:" + icronDestination);
		String icronSource = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , actual:" + icronSource);
		String text = ActiveConnectionElements.noOfActiveConnections(driver).getText();
		log.info("Number of connections are " + text);
		Assert.assertTrue(text.contains("1"), "The number of active connections did not equal 1, actual text: " + text);
	}

	/**
	 * Test that will break a connection that is running and has ICRON devices
	 * attached
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test02_breakPrivateConnection() throws InterruptedException {
		// first check that connection is running
		String icronDestination = perMethods.getIcronPrivateDestination(driver);
		Assert.assertTrue(icronDestination.equals(rex1Name),
				"Icron destination did not match. Expected:" + rex1Name + " , Actual:" + icronDestination);
		String icronSource = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , actual:" + icronSource);

		connections.breakConnection(driver, privateConnectionName);
		Thread.sleep(60000);
		String icronDestination2 = perMethods.getIcronPrivateDestination(driver);
		Assert.assertTrue(icronDestination2.equals(""),
				"Icron destination did not match. Expected empty. Actual:" + icronDestination2);
		String icronSource2 = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource2.equals(""),
				"Icron source did not match. Expected empty. Actual:" + icronSource2);
		// unBond the LEX device
		log.info(" LEX Device UnBonding Started");
		Thread.sleep(3000);
		perMethods.settingsUnbondDevice(driver, getLexMac());
		String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
		//UnBound the REX1 Device
		log.info("REX1 Device unBonding Started");
		Thread.sleep(3000);
		perMethods.settingsUnbondDevice(driver, getRex1Mac());
		 state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.STATE);
		Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
		

	}

	/**
	 * Test that will launch a shared connection with ICRON devices attached
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test03_createSharedConnection() throws InterruptedException {
		bondLexToSingleTx();
		bondRex1ToSingleRx();
		bondRex2ToDualRx();
		createSharedConnection();
		String icronSource = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , Actual:" + icronSource);
		String destinations = perMethods.getIcronSharedDestinations(driver);
		Assert.assertTrue(destinations.contains(rex1Name),
				"Icron destination 1 did not match. Expected:" + rex1Name + " , Actual:" + destinations);
//		 Assert.assertTrue(destinations.contains(rex2Name), "Icron destination 2 did not match. Expected:" + rex2Name + " , Actual:" + destinations);
		String text = ActiveConnectionElements.noOfActiveConnections(driver).getText();
		log.info("Number of connections are " + text);
		Assert.assertTrue(text.contains("1"), "The number of active connections did not equal 1, actual text: " + text);
	}

	/**
	 * Test that will break a shared connection that is running with Icron devices
	 * attached
	 * 
	 * @throws InterruptedException
	 */
	@Test
	public void test04_breakSharedConnection() throws InterruptedException {
		// check shared connections are running
		String icronSource = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , Actual:" + icronSource);
		String destinations = perMethods.getIcronSharedDestinations(driver);
		Assert.assertTrue(destinations.contains(rex1Name),
				"Icron destination 1 did not match. Expected:" + rex1Name + " , Actual:" + destinations);
//		 Assert.assertTrue(destinations.contains(rex2Name), "Icron destination 2 did not match. Expected:" + rex2Name + " , Actual:" + destinations);

		// break connection
		connections.breakConnection(driver, sharedConnectionName);
		Thread.sleep(60000);
		String icronSource2 = perMethods.getIcronSourceName(driver);
		Assert.assertTrue(icronSource2.equals(""),
				"Icron source did not match. Expecting empty , Actual:" + icronSource2);

		String destinations2 = perMethods.getIcronSharedDestinations(driver);
		Assert.assertTrue(destinations2.equals(""),
				"Icron destinations did not match. Expecting empty , Actual:" + destinations2);
		// unBond the LEX device
				log.info(" LEX Device UnBonding Started");
				Thread.sleep(3000);
				perMethods.settingsUnbondDevice(driver, getLexMac());
				String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.STATE);
				Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
				//UnBound the REX1 Device
				log.info("REX1 Device unBonding Started");
				Thread.sleep(3000);
				perMethods.settingsUnbondDevice(driver, getRex1Mac());
				 state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.STATE);
				Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
				log.info("REX2 Device UnBonding Started ");
				Thread.sleep(2000);
				perMethods.settingsUnbondDevice(driver, getRex2Mac());
				 state = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.STATE);
				 Assert.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
	}

	/**
	 * Test that will launch two private connections with ICRON devices attached
	 * 
	 * @throws InterruptedException
	 */
//	 @Test
	public void test05_twoPrivateConnections() throws InterruptedException {
		bondLexToSingleTx();
		bondRex1ToSingleRx();
		bondRex2ToDualRx();
		createConnection(privateConnectionName, singleRxName);
		createConnection(privateConnection2Name, dualRxName);

		boolean isLexConnect = perMethods.isIcronExtenderInConnection(driver, lexName);
		boolean isLex2Connect = perMethods.isIcronExtenderInConnection(driver, lex2Name);
		boolean isRex1Connect = perMethods.isIcronExtenderInConnection(driver, rex1Name);
		boolean isRex2Connect = perMethods.isIcronExtenderInConnection(driver, rex2Name);

		Assert.assertTrue(isLexConnect == true, "LEX1 is not in connection");
		Assert.assertTrue(isLex2Connect == true, "LEX2 is not in connection");
		Assert.assertTrue(isRex1Connect == true, "REX1 is not in connection");
		Assert.assertTrue(isRex2Connect == true, "REX2 is not in connection");

		// break connections
		connections.breakConnection(driver, privateConnectionName);
		connections.breakConnection(driver, privateConnection2Name);
		Thread.sleep(60000);
	}

	/**
	 * Test that will check how many ICRON devices are in connection
	 * 
	 * @throws InterruptedException
	 */
	// @Test
	public void test06_checkExtenderActiveConnectionsValue() throws InterruptedException {
		String count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("0"), "Initial active connections count was not 0. Actual:" + count);
		createConnection(privateConnectionName, singleRxName);
		count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("1"), " active connections count was not 1. Actual:" + count);
		createConnection(privateConnection2Name, dualRxName);
		count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("2"), " active connections count was not 2. Actual:" + count);
		connections.breakConnection(driver, privateConnectionName);
		count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("1"), " active connections count was not 1. Actual:" + count);
		connections.breakConnection(driver, privateConnection2Name);
		count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("0"), "Initial active connections count was not 0. Actual:" + count);
	}

//	 @Test
	public void test07_switchConnection() throws InterruptedException {
		// check no connections are active
		String count = perMethods.getActiveConnectionsCount(driver);
		Assert.assertTrue(count.equals("0"), "Initial active connections count was not 0. Actual:" + count);
		bondLexToSingleTx();
		bondRex1ToSingleRx();
		bondRex2ToDualRx();
		// create first preset
		String[] sourceList = { privateConnectionName };
		String[] destinationList = { singleRxName };
		connections.createPreset(driver, sourceList, destinationList, presetName, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert1.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);
           driver.navigate().refresh();
		// create second preset
		String[] sourceList2 = { privateConnectionName };
		String[] destinationList2 = { dualRxName };
		connections.createPreset(driver, sourceList2, destinationList2, presetName2, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName2
				+ " is displayed");
		Assert1.assertTrue(Connections.getPresetBtn(driver, presetName2).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName2);
		driver.navigate().refresh();
		Thread.sleep(2000);
		device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
		applyPreset(presetName, singleRxName, privateConnectionName);

		// check icron
		String icronDestination = perMethods.getIcronPrivateDestination(driver);
		Assert1.assertTrue(icronDestination.equals(rex1Name),
				"Icron destination did not match. Expected:" + rex1Name + " , Actual:" + icronDestination);
		String icronSource = perMethods.getIcronSourceName(driver);
		Assert1.assertTrue(icronSource.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , actual:" + icronSource);
		device.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
		// apply second preset
		applyPreset(presetName2, dualRxName, privateConnectionName);
		// check icron
		String icronDestination2 = perMethods.getIcronPrivateDestination(driver);
		Assert1.assertTrue(icronDestination2.equals(rex2Name),
				"Icron destination did not match. Expected:" + rex2Name + " , Actual:" + icronDestination2);
		String icronSource2 = perMethods.getIcronSourceName(driver);
		Assert1.assertTrue(icronSource2.equals(lexName),
				"Icron source did not match. Expected:" + lexName + " , actual:" + icronSource2);
		//break connection
		connections.breakConnection(driver, privateConnectionName);
		String icronSource3 = perMethods.getIcronSourceName(driver);
		Assert1.assertTrue(icronSource3.equals(""),
				"Icron source did not match. Expecting empty , Actual:" + icronSource3);
		Assert1.assertAll();
	}
     
	private void applyPreset(String name, String sourceName, String connection) throws InterruptedException {
		connections.navigateToConnectionViewer(driver);
		log.info("Clicking preset button: " + name);
		Connections.getPresetBtn(driver, name).click();
		log.info("sleeping and refreshing page");
		Thread.sleep(65000);
		connections.navigateToConnectionViewer(driver);
		log.info("Waking and chekcing if connection elements are displayed");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connection, sourceName).isDisplayed();
		// Assert connection has been created
		log.info("Asserting if preset has been applied by looking for the source and destination in the viewer");
		Assert1.assertTrue(isConnection,
				"Connection between " + connection + " and " + sourceName + " is not displayed");
	}

	private void createSharedConnection() throws InterruptedException {
		String[] connectionSources = { sharedConnectionName };
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		String[] destinations = { singleRxName, dualRxName };
		connections.addSharedDestination(driver, sharedConnectionName, destinations);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		connections.navigateToConnectionViewer(driver);
		boolean source = Connections.matrixItem(driver, sharedConnectionName).isDisplayed();
		boolean destination1 = Connections.matrixItem(driver, singleRxName).isDisplayed();
		boolean destination2 = Connections.matrixItem(driver, dualRxName).isDisplayed();
		boolean check = source && destination1 && destination2;
		log.info("Asserting if shared connection has been established");
		Assert.assertTrue(check, "Source and all destinations were not displayed");
	}

	private void createConnection(String connectionName, String sourceName) throws InterruptedException {
		String[] connectionSources = { connectionName };
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, connectionName, sourceName);
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000); // wait for boxilla to poll again and make sure the connection is actually up
								// and running
		connections.navigateToConnectionViewer(driver);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, sourceName)
				.isDisplayed();
		// Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + sourceName);
		Assert.assertTrue(isConnection,
				"Connection between " + connectionName + " and " + sourceName + " is not displayed");
	}

	private void bondRex1ToSingleRx() throws InterruptedException {
		perMethods.editBonding(driver, getRex1Mac(), rex1Name, singleRxName);
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getRex1Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(singleRxName),
				"Bonded device does not match. Expected:" + singleRxName + " , Actual:" + bondedDevice);
	}

	private void bondRex2ToDualRx() throws InterruptedException {
		perMethods.editBonding(driver, getRex2Mac(), rex2Name, dualRxName);
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getRex2Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualRxName),
				"Bonded device does not match. Expected:" + singleRxName + " , Actual:" + bondedDevice);
	}

	private void bondLexToSingleTx() throws InterruptedException {
		perMethods.editBonding(driver, getLexMac(), lexName, singleTxName);
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getLexMac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(singleTxName),
				"Bonded device does not match. Expected:" + singleTxName + " , Actual:" + bondedDevice);
	}

	private void bondLexToDualTx() throws InterruptedException {
		perMethods.editBonding(driver, getLex1Mac(), lex2Name, dualTxName);
		String bondedDevice = perMethods.getBondedDeviceDetails(driver, getLex1Mac(), SETTINGS.BONDED_DEVICE);
		Assert.assertTrue(bondedDevice.equals(dualTxName),
				"Bonded device does not match. Expected:" + dualTxName + " , Actual:" + bondedDevice);
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

			connections.createTxConnection(privateConnectionName, "private", driver, txIp);
			connections.createTxConnection(sharedConnectionName, "shared", driver, txIp);
			connections.createTxConnection(privateConnection2Name, "private", driver, txIpDual);
			device.recreateCloudData(rxIp, rxIpDual);
			device.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
			log.info("Sleep while devices reboot...");
			device.recreateCloudData(rxIpDual, rxIpDual);
			device.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
			Thread.sleep(10000);

			cleanUpLogout();
		} catch (Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		
	}
	@AfterClass
	public void afterClass() {
		
		try {
			cleanUpLogin();
			perMethods.settingsUnbondDevice(driver, getLexMac());
			String state = perMethods.getDiscovertedDeviceDetails(driver, getLexMac(), Peripherals.DISCOVERY.STATE);
			Assert1.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			
			perMethods.settingsUnbondDevice(driver, getRex1Mac());
			 state = perMethods.getDiscovertedDeviceDetails(driver, getRex1Mac(), Peripherals.DISCOVERY.STATE);
			 Assert1.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			
			perMethods.settingsUnbondDevice(driver, getRex2Mac());
			 state = perMethods.getDiscovertedDeviceDetails(driver, getRex2Mac(), Peripherals.DISCOVERY.STATE);
			 Assert1.assertTrue(state.equals("Unbonded"), "Device state was bonded. Should have been unbonded");
			 Assert1.assertAll();
			cleanUpLogout();
			super.afterClass();
			
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			e.printStackTrace();
			cleanUpLogout();
		}

}}
