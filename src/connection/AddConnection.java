package connection;


import static io.restassured.RestAssured.basic;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import methods.ConnectionsMethods;
import objects.Connections;
import testNG.Utilities;

/**
 * Class with tests for various ways of adding connections in boxilla
 * @author Brendan O Regan
 *
 */

public class AddConnection extends StartupTestCase {
	private ConnectionsMethods methods = new ConnectionsMethods();
	final static Logger log = Logger.getLogger(AddConnection.class);
	private String dataFile = "C:\\Test_Workstation\\SeleniumAutomation\\dataFile.txt";

	/**
	 * Adds a private connection via TX, with all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addPrivateTxConnectionAllEnabled() throws InterruptedException { // Add Connection - Via TX , Private, all option enabled
		// options enabled
		log.info("Test Case-13 Started - Add Connection Via TX, Private, All option enabled");
		methods.addConnection(driver, "tx_testconnection1", "no"); // connection name, user template
		methods.connectionInfo(driver, "tx", "user","user", txIp); // connection via, name, host ip
		methods.chooseCoonectionType(driver, "private"); // connection type
		methods.enableExtendedDesktop(driver);
		methods.enableUSBRedirection(driver);
		methods.enableAudio(driver);
		methods.enablePersistenConnection(driver);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "tx_testconnection1"); // Connection name to assert
		log.debug("Connection Via TX Added - Test Case-13 Completed");
	}

	/**
	 * Adds a private connection via TX, with all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test02_addPrivateTxConnectionAllDisabled() throws InterruptedException { // Add Connection - Via TX, Private, All option disabled
		log.info("Test Case-14 Started - Add Connection via TX, Private, All option disabled");
		methods.addConnection(driver, "tx_testconnection2", "no");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		methods.chooseCoonectionType(driver, "private");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "tx_testconnection2");
		log.info("Connection Via TX Added - Test Case-14 Completed");
	}

	/**
	 * Adds shared connection via TX with all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test03_addSharedTxConnectionAllEnabled() throws InterruptedException { // Add Connection - Via TX , Shared, all option enabled
		log.info("Test Case-15 Started - Add Connection via TX, Shared, All option enabled");
		methods.addConnection(driver, "tx_testconnection3", "no");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		methods.chooseCoonectionType(driver, "shared");
		methods.enableExtendedDesktop(driver);
		methods.enablePersistenConnection(driver);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "tx_testconnection3");
		log.info("Connection Via TX Added - Test Case-15 Completed");
	}

	/**
	 * Adds shared connection via TX with all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test04_addSharedTxConnectionAllEnabled() throws InterruptedException { // Add Connection - Via TX, Shared, All option disabled
		log.info("Test Case-16 Started - Add Connection via TX, Shared, All option disabled");
		methods.addConnection(driver, "tx_testconnection4", "no");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		methods.chooseCoonectionType(driver, "shared");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "tx_testconnection4");
		log.info("Connection Via TX Added - Test Case-16 Completed");
	}

	/**
	 * Adds connection via VM with all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test05_addVmConnectionAllEnabled() throws InterruptedException { // Add Connection - Via VM, all option enabled
		log.info("Test Case-17 Started - Add Connection Via VM, All option enabled");
		methods.addConnection(driver, "vm_testconnection5", "no");
		methods.connectionInfo(driver, "vm", "user", "user", txIp);
		methods.domainName(driver, "blckbox");
		methods.enableExtendedDesktop(driver);
		methods.enableUSBRedirection(driver);
		methods.enableAudio(driver);
		methods.enableNLA(driver);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "vm_testconnection5");
		log.info("Connection Via VM Added - Test Case-17 Completed");
	}

	/**
	 * Adds connection via VM with all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test06_addVmConnectionAllDisabled() throws InterruptedException { // Add Connection - Via VM, all option disabled
		log.info("Test Case-18 - Add Connection Via VM, All option disabled");
		methods.addConnection(driver, "vm_testconnection6", "no");
		methods.connectionInfo(driver, "vm", "user", "user", txIp);
		methods.domainName(driver, "blackbox");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "vm_testconnection6");
		log.info("Connection Via VM Added - Test Case-18 Completed");
	}

	/**
	 * Add connection via VM pool with all options enabled
	 * @throws InterruptedException
	 */
	//	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test07_addVmPoolConnectionAllEnabled() throws InterruptedException { // Add Connection - Via VM pool, all option enabled
		log.info("Test Case-19 - Add Connection Via VM Pool, all option enabled");
		methods.addConnection(driver, "vmpool_testconnection7", "no");
		methods.connectionInfo(driver, "vmpool", "user", "user", txIp);
		methods.enableExtendedDesktop(driver);
		methods.enableUSBRedirection(driver);
		methods.enableAudio(driver);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "vmpool_testconnection7");
		log.info("Connection Via VM Pool added - Test Case-19 Completed");
	}

	/**
	 * Add connection via VM pool with all options disabled
	 * @throws InterruptedException
	 */
	//	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test08_addVmPoolConnectionAllDisabled() throws InterruptedException { // Add Connection - Via VM pool, all option disabled
		log.info("Test Case-20 - Add Connection Via VM Pool, all option disabled");
		methods.addConnection(driver, "vmpool_testconnection8", "no");
		methods.connectionInfo(driver, "vmpool", "user", "user", txIp);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "vmpool_testconnection8");
		log.info("Connection Via VM Pool added - Test Case-20 Completed");
	}

	/**
	 * Add connection via broker with all options enabled
	 * @throws InterruptedException
	 */
	//@Test(groups = {"boxillaFunctional"})
	public void test09_addBrokerConnectionAllEnabled() throws InterruptedException { // Add Connection - Via Broker, all option enabled
		log.info("Test Case-21 - Add Connection Via Broker, all option enabled");
		methods.addConnection(driver, "broker_testconnection9", "no");
		methods.connectionInfo(driver, "broker", "user", "user", txIp);
		methods.domainName(driver, "blackbox");
		methods.loadBalanceInfo(driver, "loadbalance");
		methods.enableExtendedDesktop(driver);
		methods.enableUSBRedirection(driver);
		methods.enableAudio(driver);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "broker_testconnection9");
		log.info("Connection Via Broker added - Test Case-21 Completed");
	}
	/**
	 * Add connection via broker with all options disabled
	 * @throws InterruptedException
	 */
	//@Test(groups = {"boxillaFunctional"})
	public void test10_addBrokerConnectionAllDisabled() throws InterruptedException { // Add Connection - Via Broker, all option disabled
		log.info("Test Case-22 - Add Connection Via Broker, all option disabled");
		methods.addConnection(driver, "broker_testconnection10", "no");
		methods.connectionInfo(driver, "broker", "user", "user", txIp);
		methods.domainName(driver, "blackbox");
		methods.loadBalanceInfo(driver, "loadbalance");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "broker_testconnection10");
		log.info("Connection Via Broker added - Test Case-22 Completed");
	}
	/**
	 * Test that create a horizon view connection 
	 * @throws InterruptedException
	 */
	//since boxilla 3196
	@Test(groups = {"boxillaFunctional"})
	public void test11_createHorizonViewConnection() throws InterruptedException { 
		log.info("test11_createHorizonViewConnection");
		methods.addConnection(driver, "horizonViewConnection", "no");
		methods.connectionInfo(driver, "horizon", "user", "user", txIp);
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "horizonViewConnection");
		log.info("test01_createHorizonViewConnection Completed");
	}


	/**
	 * Utility method used for data driven tests. This will read in a data file and
	 * the values will be used in any test with the DP1 value
	 * @return
	 * @throws IOException
	 */
	@DataProvider(name = "DP1")
	public Object[][] createData() throws IOException {
		return readData(dataFile);
	}

	//view only mode tests
	@Test(dataProvider = "DP1")
	public void test17_viewOnlyConnections(String connectionName, String connectVia, String type, String isExtended, 
			String isUSBR, String isAudio, String isPersistent, String isViewOnly) throws InterruptedException {
		methods.createMasterConnection(connectionName, connectVia, type, isExtended, isUSBR, isAudio, isPersistent, isViewOnly,
				txIp, driver);	
	}		
}

