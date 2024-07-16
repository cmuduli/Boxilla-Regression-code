package connection;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.eclipse.jetty.websocket.common.ConnectionState;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import objects.Connections;
import objects.Landingpage;
import testNG.Utilities;

/**
 * This class contains test cases for creating and launching Navair connections
 * @author Boxilla
 *
 */
public class Navair extends StartupTestCase {

	final static Logger log = Logger.getLogger(Navair.class);

	private static int templateConnectionCounter1 = 1;
	private static int templateConnectionCounter = 1;
	private ConnectionsMethods methods = new ConnectionsMethods();
	private DevicesMethods devices = new DevicesMethods();
	private String connectionDataFileLocation = "C:\\Test_Workstation\\SeleniumAutomation\\navairConnections.txt";
	private String templateDataFileLocation = "C:\\Test_Workstation\\SeleniumAutomation\\navairTemplates.txt";
	private static boolean isXmlNew = false;
	private static boolean isXmlNewTemplate = false;
	String sharedCon = "pairedSharedCon1";


	/**
	 * Data reader that will read in data from a text file and 
	 * can then pass those values to a test
	 * @return
	 * @throws IOException
	 */
	@DataProvider(name = "DP2")
	public Object[][] createTemplateData() throws IOException {
		return readData(templateDataFileLocation);
	}

	/**
	 * Data driven test that will create all variations of navair connections in Boxilla
	 * @param name
	 * @param isTemplate
	 * @param templateName
	 * @param targets
	 * @param orientation
	 * @param ip1
	 * @param ip2
	 * @param shared
	 * @param audio
	 * @param audio2
	 * @param persistent
	 * @param viewOnly
	 * @throws InterruptedException
	 */
	@Test(dataProvider="DP1")
	public void test01_createNavairConnection(String name, String isTemplate, String templateName, String targets, String orientation, 
			String ip1, String ip2, String shared, String audio, String audio2, String persistent, String viewOnly) throws InterruptedException {
		Connections.Orientation ore = getOrientation(orientation);
		methods.createTxPairConnection(driver, name, isTemplate, templateName, targets, ore, txIp,
				txIpDual, shared, audio, audio2, persistent, viewOnly);	
	}

	/**
	 * Data driven test that will check the xml file on the appliance for 
	 * Navair connections and settings
	 * @param name
	 * @param isTemplate
	 * @param templateName
	 * @param targets
	 * @param orientation
	 * @param ip1
	 * @param ip2
	 * @param shared
	 * @param audio
	 * @param audio2
	 * @param persistent
	 * @param viewOnly
	 * @throws InterruptedException
	 */
	@Test(dataProvider="DP1")
	public void test02_checkXmlForConnections(String name, String isTemplate, String templateName, String targets, String orientation, 
			String ip1, String ip2, String shared, String audio, String audio2, String persistent, String viewOnly) throws InterruptedException {
		//pull down xml
		getNewXml();
		String xml = getXml();
		String connection = checkConnectionInXml(name, targets, orientation, txIp, txIpDual, shared, audio, audio2, persistent, viewOnly, false);
		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
	}

	/**
	 * Data driven test that will create variations of connection templates
	 * for navair connections
	 * @param templateName
	 * @param shared
	 * @param targets
	 * @param orientation
	 * @param audio
	 * @param audio2
	 * @param persistent
	 * @param viewOnly
	 * @throws InterruptedException
	 */
//	@Test(dataProvider="DP2")
//	public void test03_createNavairTemplates(String templateName, String shared, String targets, String orientation, String audio, 
//			String audio2, String persistent, String viewOnly) throws InterruptedException {
//		Connections.Orientation ore = getOrientation(orientation);
//		methods.addPairConnectionTemplate(driver, templateName, shared, targets, ore, audio, audio2, persistent, viewOnly);
//	}
//
//	/**
//	 * Data driven test that will create navair connections from templates
//	 * @param templateName
//	 * @param shared
//	 * @param targets
//	 * @param orientation
//	 * @param audio
//	 * @param audio2
//	 * @param persistent
//	 * @param viewOnly
//	 * @throws InterruptedException
//	 */
//	@Test(dataProvider="DP2")
//	public void test04_createNavairConnectionFromTemplate(String templateName, String shared, String targets, String orientation, String audio, 
//			String audio2, String persistent, String viewOnly) throws InterruptedException {
//		Connections.Orientation ore = getOrientation(orientation);
//		methods.createTxPairConnection(driver, "navConFromTem" + templateConnectionCounter, "true", templateName, targets, ore, txIp,
//				txIpDual, shared, audio, audio2, persistent, viewOnly);
//		templateConnectionCounter++;
//	}
//
//	/**
//	 * Datadriven test that will check the appliance xml for navair connections
//	 *  that were created by from templates
//	 * @param templateName
//	 * @param shared
//	 * @param targets
//	 * @param orientation
//	 * @param audio
//	 * @param audio2
//	 * @param persistent
//	 * @param viewOnly
//	 * @throws InterruptedException
//	 */
//	@Test(dataProvider="DP2")
//	public void test05_checkXmlForConnectionsFromTemplates(String templateName, String shared, String targets, String orientation, String audio, 
//			String audio2, String persistent, String viewOnly) throws InterruptedException {
//		getNewXmlTemplate();
//		String xml = getXml();
//		String connection = checkConnectionInXml("navConFromTem" + templateConnectionCounter1, targets, orientation, txIp, txIpDual, shared, 
//				audio, audio2, persistent, viewOnly, true);
//		templateConnectionCounter1++;
//		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
//
//	}
//
//	/**
//	 * Test that launches a navair private connection with 1 target
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test06_pairedPrivate1Target() throws InterruptedException {
//		String conName = "navPaired9";
//		String[] connectionSources = {conName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addPrivateDestination(driver, conName, singleRxName);
//		log.info("Private destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//		boolean isConnection = Connections.singleSourceDestinationCheck(driver, conName, singleRxName).isDisplayed();
//		Assert.assertTrue(isConnection, "Connection is not displayed on the connection viewer");
//		String name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(txDual.getDeviceName()), "TX name in active connections did not match");
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(txSingle.getDeviceName()), "TX name in active connections did not match");
//		log.info("Connection running. Breaking");
//		methods.breakConnection(driver, conName);
//		name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//	}
//
//	/**
//	 * Test that launches a private navair connection with 2 targets
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test07_pairedPrivateTarget2() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		String conName = "navPaired1";
//		String[] connectionSources = {conName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addPrivateDestination(driver, conName, singleRxName);
//		log.info("Private destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//		boolean isConnection = Connections.singleSourceDestinationCheck(driver, conName, singleRxName).isDisplayed();
//		Assert.assertTrue(isConnection, "Connection is not displayed on the connection viewer");
//		String name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(txDual.getDeviceName()), "TX name in active connections did not match");
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(txSingle.getDeviceName()), "TX name in active connections did not match");
//		log.info("Connection running. Breaking");
//		methods.breakConnection(driver, conName);
//		name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//	}
//
//	/**
//	 * Test that launches a private navair connection with audio from the first 
//	 * connection
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test08_pairedPrivateAudio1() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		String conName = "navParied2";
//		String[] connectionSources = {conName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addPrivateDestination(driver, conName, singleRxName);
//		log.info("Private destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//		boolean isConnection = Connections.singleSourceDestinationCheck(driver, conName, singleRxName).isDisplayed();
//		Assert.assertTrue(isConnection, "Connection is not displayed on the connection viewer");
//		String name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(txDual.getDeviceName()), "TX name in active connections did not match");
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(txSingle.getDeviceName()), "TX name in active connections did not match");
//		log.info("Connection running. Breaking");
//		methods.breakConnection(driver, conName);
//		name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//	}
//
//	/**
//	 * Test that launches a private navair connection 
//	 * with audio from the second connection
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test09_pairedPrivateAudio2() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		String conName = "navPaired10";
//		String[] connectionSources = {conName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addPrivateDestination(driver, conName, singleRxName);
//		log.info("Private destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//		boolean isConnection = Connections.singleSourceDestinationCheck(driver, conName, singleRxName).isDisplayed();
//		Assert.assertTrue(isConnection, "Connection is not displayed on the connection viewer");
//		String name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(txDual.getDeviceName()), "TX name in active connections did not match");
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(txSingle.getDeviceName()), "TX name in active connections did not match");
//		log.info("Connection running. Breaking");
//		methods.breakConnection(driver, conName);
//		name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//	}
//
//	/**
//	 * Test that launches a shared navair connection
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test10_pairedShared() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		methods.createTxPairConnection(driver, sharedCon, "false", "", "2", Connections.Orientation.H12, txIp,
//				txIpDual, "true", "true", "true", "false", "false");	
//		devices.recreateCloudData(rxIp, null);
//		devices.rebootDeviceSSH(rxIp,deviceUserName, devicePassword, 0);
//		devices.recreateCloudData(rxIpDual, null);
//		devices.rebootDeviceSSH(rxIpDual,deviceUserName, devicePassword, 0);
//		String[] connectionSources = {sharedCon};
//		String[] destinations = {singleRxName, dualRxName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addSharedDestination(driver, sharedCon, destinations);
//		log.info("Shared destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//
//		boolean source = Connections.matrixItem(driver, sharedCon).isDisplayed();
//		boolean destination1 = Connections.matrixItem(driver, singleRxName).isDisplayed();
//		boolean destination2 = Connections.matrixItem(driver, dualRxName).isDisplayed();
//		boolean check = source && destination1 && destination2;
//		log.info("Asserting if shared connection has been established");
//		Assert.assertTrue(check, "Source and all destinations were not displayed");
//		Thread.sleep(60000);
//		getActionConDetails();
//		//break connection
//		methods.breakConnection(driver, sharedCon);
//		String name = methods.searchActiveConnectionForTransmitter(driver, rxDual.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//		name = methods.searchActiveConnectionForTransmitter(driver, rxSingle.getDeviceName());
//		Assert.assertTrue(name.equals(""), "Connection name was still in active connections table after breaking. " + name);
//	}
//
//	/**
//	 * Test that launches a navair connection that 
//	 * was created from a template
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test11_pairedConnectionFromTemplate() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		//create template and connection
//		String templateName = "test11_pairedConnectionFromTemplate";
//		String conName = "test11PairedCon";
//		methods.addPairConnectionTemplate(driver, templateName, "false", "1", Connections.Orientation.H12, "true",
//				"false", "false", "false");
//		methods.createTxPairConnection(driver, conName, "true", templateName, "1", null, txIp,
//				txIpDual, "false", "true", "false", "false", "false");
//
//		//pull down the xml
//		devices.recreateCloudData(rxIp, null);
//		devices.rebootDeviceSSH(rxIp,deviceUserName, devicePassword, 0);
//
//		//create the connection
//		String[] connectionSources = {conName};
//		log.info("Attempting to add soruces");
//		methods.addSources(driver, connectionSources);
//		log.info("Sources added. Trying to add private destination");
//		methods.addPrivateDestination(driver, conName, singleRxName);
//		log.info("Private destination added. Sleeping and refreshing page");
//		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
//		driver.navigate().refresh();
//		Thread.sleep(5000);
//		log.info("Checking if connection is active by asserting on the connections > viewer UI");
//		boolean isConnection = Connections.singleSourceDestinationCheck(driver, conName, singleRxName).isDisplayed();
//		Assert.assertTrue(isConnection, "Connection is not displayed on the connection viewer");
//
//		//assert active connection table
//		String name = methods.searchActiveConnectionForTransmitter(driver, txDual.getDeviceName());
//		Assert.assertTrue(name.equals(txDual.getDeviceName()), "TX name in active connections did not match");
//		name = methods.searchActiveConnectionForTransmitter(driver, txSingle.getDeviceName());
//		Assert.assertTrue(name.equals(txSingle.getDeviceName()), "TX name in active connections did not match");
//	}
//
//	/**
//	 * Test that edits the IP address of a navair connection
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test12_editConnectionIp() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		//create initial connection
//		String conName = "test12_editConnectionIp";
//		methods.createTxPairConnection(driver, conName, "false", "", "1", Connections.Orientation.H12, txIp,
//				txIpDual, "false", "false", "false", "false", "false");
//
//		devices.recreateCloudData(rxIp);
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
//		String xml = getXml();
//		String connection = checkConnectionInXml(conName, "", "H12", txIp, txIpDual, "false", "false", "false", "false", "false", false);
//		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
//		log.info("Original connection created and in xml OK. Edit IP addresses and rechecking");
//		//edit connection in boxilla
//		String ip1 = "1.1.1.1";
//		String ip2 = "2.2.2.2";
//		methods.editPairedConnectionIps(driver, conName, ip1, ip2);
//
//		devices.recreateCloudData(rxIp);
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
//		xml = getXml();
//		connection = checkConnectionInXml(conName, "", "H12", ip1, ip2, "false", "false", "false", "false", "false", false);
//		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
//	}
//
//	/**
//	 * Test that edits a navair connection type from  private to shared and
//	 * checks the connection in the appliance xml file
//	 * @throws InterruptedException
//	 */
//	@Test
//	public void test13_editConnectionType() throws InterruptedException {
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 0);
//		devices.rebootDeviceSSH(rxIpDual, deviceUserName, devicePassword, 0);
//		//create initial connection
//		String conName = "test13_editConnectionType";
//		methods.createTxPairConnection(driver, conName, "false", "", "1", Connections.Orientation.H12, txIp,
//				txIpDual, "false", "false", "false", "false", "false");
//
//		devices.recreateCloudData(rxIp);
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
//		String xml = getXml();
//		String connection = checkConnectionInXml(conName, "", "H12", txIp, txIpDual, "false", "false", "false", "false", "false", false);
//		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
//		log.info("Original connection created and in xml OK. Edit connection type and rechecking");
//		//edit connection type and recheck
//		methods.editPairedConnectionType(driver,conName, true);
//		devices.recreateCloudData(rxIp);
//		devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
//		xml = getXml();
//		connection = checkConnectionInXml(conName, "", "H12", txIp, txIpDual, "true", "false", "false", "false", "false", false);
//		Assert.assertTrue(xml.contains(connection), "Xml file does not contain connection");
//	}

	private void getActionConDetails() throws InterruptedException {
		int[][] conName = {{1,1}, {2,1}, {3,1}, {4,1}}; // connection name for each for connections
		int[][] receiver = {{1,2}, {2,2}, {3,2}, {4,2}};
		int[][] type = {{1,4}, {2,4}, {3,4}, {4,4}};
		int[][] transmitter = {{1,5}, {2,5}, {3,5}, {4,5}};
		String[] names = methods.getActiveConnectionsDetails(driver, conName);
		log.info("Asserting connection names");
		Assert.assertTrue(names[0].equals(sharedCon), "name of connection did not equal:");
		Assert.assertTrue(names[1].equals(sharedCon), "name of connection did not equal:");
		Assert.assertTrue(names[2].equals(sharedCon), "name of connection did not equal:");
		Assert.assertTrue(names[3].equals(sharedCon), "name of connection did not equal:");
		String[] receiverName = methods.getActiveConnectionsDetails(driver, receiver);
		log.info("Asserting connection receiver");
		Assert.assertTrue(receiverName[0].contains(singleRxName) || receiverName[0].contains(dualRxName), "name of receiver did not equal:" 
				+ singleRxName + " or " + dualRxName + " . Actual:" + receiverName[0]);

		Assert.assertTrue(receiverName[1].contains(singleRxName) || receiverName[1].contains(dualRxName), "name of receiver did not equal:"
				+ singleRxName + " or " + dualRxName + " . Actual:" + receiverName[1]);

		Assert.assertTrue(receiverName[2].contains(singleRxName) || receiverName[2].contains(dualRxName), "name of receiver did not equal:"
				+ singleRxName + " or " + dualRxName + " . Actual:" + receiverName[2]);

		Assert.assertTrue(receiverName[3].contains(singleRxName) || receiverName[3].contains(dualRxName), "name of receiver did not equal:"
				+ singleRxName + " or " + dualRxName + " . Actual:" + receiverName[3]);
		String[] conType = methods.getActiveConnectionsDetails(driver, type);
		log.info("Asserting connection type");
		Assert.assertTrue(conType[0].equals("Shared"), "Connection type was not Shared. Actual:" + conType[0]);
		Assert.assertTrue(conType[1].equals("Shared"), "Connection type was not Shared. Actual:" + conType[1]);
		Assert.assertTrue(conType[2].equals("Shared"), "Connection type was not Shared. Actual:" + conType[2]);
		Assert.assertTrue(conType[3].equals("Shared"), "Connection type was not Shared. Actual:" + conType[3]);
	}

	@DataProvider(name = "DP1")
	public Object[][] createData() throws IOException {
		return readData(connectionDataFileLocation);
	}


	private void getNewXml() throws InterruptedException {
		if(!isXmlNew) {
			devices.recreateCloudData(rxIp);
			devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
			isXmlNew = true;
		}
	}
	private void getNewXmlTemplate() throws InterruptedException {
		if(!isXmlNewTemplate) {
			devices.recreateCloudData(rxIp);
			devices.rebootDeviceSSH(rxIp, deviceUserName, devicePassword, 10);
			isXmlNewTemplate = true;
		}
	}

	/**
	 * Check the xml file returned from the appliance for all 
	 * navair connectino details
	 * @param name
	 * @param targets
	 * @param orientation
	 * @param ip1
	 * @param ip2
	 * @param shared
	 * @param audio
	 * @param audio2
	 * @param persistent
	 * @param viewOnly
	 * @param isConnectionTemplate
	 * @return
	 */
	private String checkConnectionInXml(String name, String targets, String orientation, String ip1, String ip2, String shared, String audio, 
			String audio2, String persistent, String viewOnly, boolean isConnectionTemplate) {

		//awful code. Converts the values to ones in the xml
		if(shared.equals("true")) {
			shared = "Shared";
		}else {
			shared = "Private";
		}

		if(targets.equals("2")) {
			targets = "TX_Pair_2";
		}else {
			targets = "TX_Pair_1";
			orientation = "";
		}

		if(audio.equals("false")) {
			audio2 = "";
		}else {
			if(audio2.equals("true")) {
				audio2 = "2";
			}else {
				audio2 = "1";
			}
		}
		if(audio.equals("true")) {
			audio = "Yes";
		}else {
			audio="";
		}
		if(viewOnly.equals("true")) {
			viewOnly = "Yes";
		}else {
			viewOnly = "";
		}

		String con = "";
		if(!isConnectionTemplate) {
			con = "<Connection ExtDesk='' Audio='" + audio + "' Name='" + name + "' Connection_type='" + shared + "' "
					+ "USB_Redirection='' viaTX='false' Colour_depth='32' IP_address='" + ip1 + "' IP_address_2='" + ip2 + "' "
					+ "Orientation='" + orientation + "' Audio_Source='" + audio2 + "' type='" + targets + "' Persistent='"+ persistent + "' PreEmption_mode='false'"
					+ " Horizon='false' ViewOnly='" + viewOnly + "'/>";
			log.info("Created Connection String:" + con);
			return con;
		}else {
			con = "<Connection ExtDesk='' Audio='" + audio + "' Name='" + name + "' Connection_type='" + shared + "' "
					+ "USB_Redirection='' viaTX='false' Colour_depth='32' IP_address='" + ip1 + "' IP_address_2='" + ip2 + "' "
					+ "Orientation='" + orientation + "' Audio_Source='" + audio2 + "' type='" + targets + "' User_Name='' Password=''" + " Persistent='"+ persistent + "' PreEmption_mode='false'"
					+ " Horizon='false' ViewOnly='" + viewOnly + "'/>";
			log.info("Created Connection String:" + con);
			return con;
		}
	}

	/**
	 * SSHs on to an appliance and get the xml file
	 * @return
	 */
	private String getXml() {
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		String xml = ssh.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");
		ssh.disconnect();
		return xml;
	}
	private void removeXml() {
		Ssh ssh = new Ssh(deviceUserName, devicePassword, rxIp);
		ssh.loginToServer();
		ssh.sendCommand("rm /usr/local/gui_files/CloudDataA.xml");
		ssh.disconnect();
	}

	public Connections.Orientation getOrientation(String orientation) {
		if(orientation.equals("V12")) {
			return Connections.Orientation.V12;
		}else if(orientation.equals("V21")) {
			return Connections.Orientation.V21;
		}else if(orientation.equals("H12")) {
			return Connections.Orientation.H12;
		}else if(orientation.equals("H21")) {
			return Connections.Orientation.H21;
		}
		return null;
	}

}
