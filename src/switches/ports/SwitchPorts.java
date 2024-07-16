package switches.ports;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.SwitchCLI;
import methods.SwitchMethods;
import objects.Switch.PORT_COLUMN;
import switches.status.SwitchStatus;
import testNG.Utilities;

public class SwitchPorts extends StartupTestCase {
	
	final static Logger log = Logger.getLogger(SwitchPorts.class);
	
	private SwitchMethods switches = new SwitchMethods();
	private SwitchCLI cli = new SwitchCLI();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private String switchName = "switch1";
	private DevicesMethods deviceMethods = new DevicesMethods();
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_disablePort() throws InterruptedException {
		log.info("Attempting to disable port");
		switches.portDown(driver, dellSwitchIp, switchName, "20", true);
		log.info("Asserting port disabled on switch");
		String portInfo = cli.getEthPortDetails(dellSwitchIp, "20");
		Assert.assertFalse(portInfo.contains("no shutdown"), "Port was not disabled on switch side, port details:" + portInfo);
		log.info("Assert successful");
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test02_enablePort() throws InterruptedException {
		log.info("Attempting to disable port");
		switches.portDown(driver, dellSwitchIp, switchName, "20", false);
		log.info("Asserting port disabled on switch");
		String portInfo = cli.getEthPortDetails(dellSwitchIp, "20");
		Assert.assertTrue(portInfo.contains("no shutdown"), "Port was not enabled on switch side, port details:" + portInfo);
		log.info("Assert successful");
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test03_enableMRouter() throws InterruptedException {
		log.info("Attempting to enable MRouter");
		switches.changeMrouter(driver, dellSwitchIp, switchName, "20", true);
		log.info("Asserting mrouter port on switch");
		String mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
		Assert.assertTrue(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/20"), "mrouting was not enabled on switch. Details:" + mrouterPort);
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test04_disableMRouter() throws InterruptedException {
		log.info("Attempting to disable mrouter");
		switches.changeMrouter(driver, dellSwitchIp, switchName, "20", false);
		String mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
		Assert.assertFalse(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/20"), "mrouting was not disabled on switch. Details:" + mrouterPort);
	}
	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test05_changeMRouterPort() throws InterruptedException {
		log.info("Attempting to change mrouter");
		log.info("Setting port 20 as mrouter");
		switches.changeMrouter(driver, dellSwitchIp, switchName, "20", true);
		String mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
		Assert.assertTrue(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/20"), "mrouting was not enabled on switch. Details:" + mrouterPort);
		log.info("Changing to port 21");
		switches.changeMrouter(driver, dellSwitchIp, switchName, "21", true);
		mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
		Assert.assertTrue(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/21"), "mrouting was not enabled on switch. Details:" + mrouterPort);
		
	}
//	
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test06_portDetailsBandwidthIn() throws InterruptedException {
		log.info("Checking port 1 and 2 have bandwidth in");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.BANDWIDTH_IN);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "2", PORT_COLUMN.BANDWIDTH_IN);
		log.info("return from bandwidth in: " + output + " " + output2);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "bandwidth in was 0");
		log.info("Port 1 assertion OK..Asserting port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "bandwidth in was 0");
		log.info("Port 2 ok");
//		
	}
	@Test(groups = {"noSE"})
	public void test07_portDetailsBandwidthOut() throws InterruptedException {
		log.info("Checking port 1 and 2 have bandwidth out");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.BANDWIDTH_OUT);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.BANDWIDTH_OUT);
		log.info("return from bandwidth in: " + output);
		log.info("return from bandwidth in: " + output2);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "bandwidth in was 0");
		log.info("Port 1 ok. checking port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "bandwidth in was 0");
		log.info("Port 2 OK");
	}
//	
	@Test(groups = {"noSE"})
	public void test08_portDetailsPacketsIn() throws InterruptedException {
		log.info("Checking port 1 and 2 have packets in");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.PACKETS_IN);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.PACKETS_IN);
		log.info("return from packets in: " + output);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "packets in was 0");
		log.info("Port 1 ok. checking port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "packets in was 0");
		log.info("Port 2 OK");
	}
//	
	@Test(groups = {"noSE"})
	public void test09_portDetailsPacketsOut() throws InterruptedException {
		log.info("Checking port 1 and 2 have packets out");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.PACKETS_OUT);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.PACKETS_OUT);
		log.info("return from packets out: " + output);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "packets out was 0");
		log.info("Port 1 ok. checking port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "packets out was 0");
		log.info("Port 2 OK");
	}
//	
	@Test(groups = {"noSE"})
	public void test10_portDetailsLineUsageIn() throws InterruptedException {
		log.info("Checking port 1 and 2 have line usage in");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.LINE_USAGE_IN);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.LINE_USAGE_IN);
		log.info("return from line usage in: " + output);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "line usage in was 0");
		log.info("Port 1 ok. checking port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "line usage in was 0");
		log.info("Port 2 OK");
	}
//	
	@Test(groups = {"noSE"})
	public void test11_portDetailsLineUsageOut() throws InterruptedException {
		log.info("Checking port 1 and 2 have line usage out");
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.LINE_USAGE_OUT);
		String output2 = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.LINE_USAGE_OUT);
		log.info("return from line usage out: " + output);
		log.info("Asserting port 1");
		Assert.assertFalse(output.equals("0") || output.equals("0.0"), "line usage out was 0");
		log.info("Port 1 ok. checking port 2");
		Assert.assertFalse(output2.equals("0") || output2.equals("0.0"), "line usage out was 0");
		log.info("Port 2 OK");
	}
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test12_checkPortChart() throws InterruptedException {
		log.info("Checking if port chart appears on screen");
		String output = switches.getChartText(driver, dellSwitchIp, switchName, "20");
		Assert.assertTrue(output.contains("1/1/20"), "Chart did not appear or had the wrong heading. Actual, " + output);
	}
//	@Test(groups = {"noSE"})
	public void test13_bandwidthInNoConnection() throws InterruptedException {
		log.info("Killing active connection by restarting receiver");
		deviceMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 3000);
		log.info("Sleeping for boxilla to run a new poll");
		Thread.sleep(30000);
		String output = switches.getPortSwitchTableData(driver, dellSwitchIp, switchName, "1", PORT_COLUMN.BANDWIDTH_IN);
		Assert.assertTrue(output.equals("0") || output.equals("0.0"), "bandwidth in was not 0");
	}
//	
//	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test14_checkSettingsPersistAfterReboot() throws InterruptedException {
		log.info("Enabling shared mode, disabling port 20, enabling mrouter on port 19 ");
		log.info("and rebooting switch and asserting the values are still set");
		 Assert.assertTrue(cli.isSharedModeEnabled(dellSwitchIp), "Shared mode not enabled on swith");
		 log.info("Shared mode is enabled on switch");
		 switches.changeMrouter(driver, dellSwitchIp, switchName, "20", true);
		 log.info("Assert on switch that mrouter is active on port 20");
		String mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
			Assert.assertTrue(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/20"), "mrouting was not enabled on switch. Details:" + mrouterPort);
			log.info("Mrouter is active on port 20. Attempting to disable port 19");
			switches.portDown(driver, dellSwitchIp, switchName, "19", true);
			log.info("Asserting if port is disabled on switch");
			String portInfo = cli.getEthPortDetails(dellSwitchIp, "19");
			Assert.assertFalse(portInfo.contains("no shutdown"), "Port was not disabled on switch side, port details:" + portInfo);
			log.info("Port was disabled. Rebooting switch and reasserting on switch once it comes back online");
			switches.rebootSwitch(driver, dellSwitchIp);
			log.info("pinging switch until it comes back online");
			Assert.assertTrue(switches.isSwitchReachable(dellSwitchIp), "Switch never came back online");
			
			log.info("Attemping asserts after reboot. Checking shared mode");
			 Assert.assertTrue(cli.isSharedModeEnabled(dellSwitchIp), "Shared mode not enabled on swith");
			 log.info("Shared mode still enabled. CHecking mrouter");
			 mrouterPort = cli.getVlanDetails(dellSwitchIp, "1003");
				Assert.assertTrue(mrouterPort.contains("ip igmp snooping mrouter interface ethernet1/1/20"), "mrouting was not enabled on switch. Details:" + mrouterPort);
			 log.info("mRouter still enabled. Checking port down");
			  portInfo = cli.getEthPortDetails(dellSwitchIp, "19");
				Assert.assertFalse(portInfo.contains("no shutdown"), "Port was not disabled on switch side, port details:" + portInfo);
			log.info("All asserts OK after reboot. Test passed");
	}
//	
//	@Override
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			cleanUpLogin();
			switches.isenableSharedMode(driver, dellSwitchIp);
			log.info("Starting test setup....");
			RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
			RestAssured.useRelaxedHTTPSValidation();
			log.info("Attempting to make a real connection");
			//conMethods.createRealConnection("", deviceUserName, devicePassword, rxIp, txIp) ;
			log.info("Sleeping while test attached");
			//Thread.sleep(2000);
//			log.info("Checking if active connection is ip");
//			given().header(getHead())
//			.when()
//			.get(getHttp() + "://" + txIp + getPort() + "/statistics/active_connections")
//			.then()
//			.body("kvm_active_connections.rx_hostname[0]", equalTo(rxIp));
//			
//			log.info("Active connection is running. Starting tests");
			cleanUpLogout();
			
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
	}

}
