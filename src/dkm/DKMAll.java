package dkm;

import java.io.IOException;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.DiscoveryMethods;
import methods.DkmMethods;
import objects.DkmElements;

public class DKMAll extends StartupTestCase2{

	private DkmMethods methods = new DkmMethods();
	final static Logger log = Logger.getLogger(DkmMethods.class);

//	public DKMAll() throws IOException {
//		super();
//	}
//
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
//	public void test01_AddDkmSwitch() throws InterruptedException { // Add Switch
//		log.info("Test Case-94 Started - DKM-Add Switch");
//		methods.navigateToDkmSwitches(driver);
//		methods.addDkmSwitch(driver, "10.211.128.123", "Test_Switch");
//		log.info("DKM - Switch added - Test Case-94 Completed");
//	}
//
//	@Test(groups = {"boxillaFunctional", "chrome"})
//	public void test02_editDkmSwitch() throws InterruptedException { // Edit Switch
//		log.info("Test Case-95 Started - Edit Switch");
//		methods.navigateToDkmSwitches(driver);
//		methods.editSwitch(driver, "10.211.128.123"); // changes switch port number to 16
//		log.info("Switch Edited Successfully : Test Case-95 Completed");
//	}
//
//	@Test(groups = {"boxillaFunctional", "chrome"})
//	public void test03_addConnection() throws InterruptedException { // Add Connection
//		log.info("Test Case-96 Started - DKM - Add Connection");
//		methods.navigateToDkmViewer(driver);
//		methods.detachConnctions(driver);
//		// Extracting current active connections
//		String currentActiveConnectionsCount = DkmElements.activeConnectionCoutner(driver).getText();
//		log.info(currentActiveConnectionsCount);
//		methods.addSource(driver, "VTX1");
//		methods.addDestination(driver, "VTX1", "VRX1");
//		// New counts should be different than old counts
//		String newActiveConnectionsCount = DkmElements.activeConnectionCoutner(driver).getText();
//		Assert.assertFalse(currentActiveConnectionsCount.equalsIgnoreCase(newActiveConnectionsCount));
//		methods.timer(driver);
//		log.info("DKM - New Connection added succesfully : Test Case-96 Completed");
//	}
//	
//	@Test
//	public void test04_detachConnection() throws InterruptedException {
//		methods.navigateToDkmViewer(driver);
//		methods.detachConnctions(driver);
//		//navigate back to dkm viewer and check connection is killed
//		methods.navigateToDkmViewer(driver);
//		String activeConnectionsCount = DkmElements.activeConnectionCoutner(driver).getText();
//		log.info("Connection count: " + activeConnectionsCount);
//		Assert.assertTrue(activeConnectionsCount.equals("0"), "Connections was not 0");
//	}
//
//	@Test(groups = {"boxillaFunctional"})
//	public void test04_addPreset() throws InterruptedException { // Add Preset
//		log.info("Test Case-97 Started - DKM - Add Preset");
//		methods.navigateToDkmViewer(driver);
//		methods.addPreset(driver, "VTX1", "VRX1", "Test_Preset");
//		log.info("DKM - Preset Added Successfully : Test Case-97 Completed");
//	}

//	@Test(groups = {"boxillaFunctional", "smoke"})
//	public void test05_addConnectionMatrix() throws InterruptedException { // Add Connection Matrix
//		log.info("Test Case-98 Started - Create Connection matrix");
//		methods.navigateToDkmViewer(driver);
//		//methods.detachConnctions(driver);
//		methods.timer(driver);
//		Assert.assertTrue(DkmElements.nonActiveSourceElements(driver).size() != 0,
//				"***** Non Active Source Element count is 0 *****");
//		methods.addSource(driver, "VTX1");
//		methods.addDestinationLoop(driver, "VTX1");
//		methods.addSource(driver, "VTX2");
//		methods.addDestinationLoop(driver, "VTX2");
//		methods.addSource(driver, "VTX3");
//		methods.addDestinationLoop(driver, "VTX3");
//		methods.addSource(driver, "VTX4");
//		methods.addDestinationLoop(driver, "VTX4");
//		methods.addSource(driver, "VTX5");
//		methods.addDestinationLoop(driver, "VTX5");
//		methods.assertElmentResize(driver); // Removing elements from VTX5 and checking if elements resizes
//		log.info("Connection matrix created - Test Case-98 completed");
//	}
//
//	@Test(groups = {"boxillaFunctional"})
	public void test06_deleteSwitch() throws InterruptedException { // Delete Switch
		log.info("Test Case-99 Started - DKM-Delete Switch");
		methods.navigateToDkmSwitches(driver);
		methods.deleteSwitch(driver, "10.211.128.123");
		log.info("Switch Deleted - Test Case-99 Completed");
	}
	
	@Test
	public void test() {
		DiscoveryMethods dis = new DiscoveryMethods();
		dis.newDicoverDevices(driver, true);
		//dis.searchAndEdit(driver, "11:11:11:11:11", "10.211.128.111");
		
	}
}

