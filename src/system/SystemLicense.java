package system;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.ScpTo;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import soak.ServiceRestart;
import testNG.Utilities;

public class SystemLicense extends StartupTestCase {

	private SystemMethods methods = new SystemMethods();
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private UsersMethods userMethods = new UsersMethods();
	final static Logger log = Logger.getLogger(SystemLicense.class);

	/**
	 * The file to create users and connections in boxilla DB is no longer included in boxilla upgrades
	 * After an iso install the file will be gone so we scp it from the automation env
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		try {	
			String fileName = "C:\\Test_Workstation\\SeleniumAutomation\\resources\\cloudium-example-licensetest.conf";
			ScpTo copy = new ScpTo();
			copy.scpTo(fileName, boxillaUsername, boxillaManager, boxillaPassword, "/etc/init/", "cloudium-example-licensetest.conf");
		}catch(Exception | AssertionError e) {
			e.printStackTrace();
		}
	}

	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test01_upload50Licenses() throws InterruptedException { // Upload 50 License
		log.info("Test Case-73 - 50 License Upload");
		methods.deletLicense(driver);
		methods.addLicense(driver, "25", "50", boxillaManager);
		log.info("50 License Uploaded - Test Case-73 Completed");
	}

	@Test
	public void test02_fiftyUserLimitCheck() throws InterruptedException { // 50 license - Users Limit Check
		log.info("Test Case-74 - 50 License - User Limit Check");
		methods.usersLicenseLimitTest(driver, boxillaManager);
		log.info("50 License - User Limit Check - Test Case-74 Completed");
	}

	@Test
	public void test03_fiftyConnectionLimitCheck() throws InterruptedException { // 50 license - Connection Limit Check
		log.info("Test Case-75 - 50 License - Connections Limit Check");
		methods.connectionsLicenseLimitTest(driver, boxillaManager);
		log.info("50 License - Connections Limit Check - Test Case-75 Completed");
	}

	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test04_upload100Licenses() throws InterruptedException { // Upload 100 License
		log.info("Test Case-76 - 100 License Upload");
		methods.deletLicense(driver);
		methods.addLicense(driver, "25", "100", boxillaManager);
		log.info("100 License Uploaded - Test Case-76 Completed");
	}

	@Test
	public void test05_100_usersLimitCheck() throws InterruptedException { // 100 license - Users Limit Check
		log.info("Test Case-77 - 100 License - User Limit Check");
		methods.usersLicenseLimitTest(driver, boxillaManager);
		log.info("100 License - User Limit Check - Test Case-77 Completed");
	}

	@Test
	public void test06_100_connectionLimitCheck() throws InterruptedException { // 100 license - Connection Limit Check
		log.info("Test Case-78 - 100 License - Connections Limit Check");
		methods.connectionsLicenseLimitTest(driver, boxillaManager);
		log.info("100 License - Connections Limit Check - Test Case-78 Completed");
	}

	@Test(retryAnalyzer = testNG.RetryAnalyzer.class)
	public void test07_upload300License() throws InterruptedException { // Upload 300 License
		log.info("Test Case-79 - 300 License Upload");
		methods.deletLicense(driver);
		methods.addLicense(driver, "25", "300", boxillaManager);
		log.info("300 License Uploaded - Test Case-79 Completed");
	}

	@Test
	public void test08_300_userLimitCheck() throws InterruptedException { // 300 license - Users Limit Check
		log.info("Test Case-80 - 300 License - User Limit Check");
		methods.usersLicenseLimitTest(driver, boxillaManager);
		log.info("300 License - User Limit Check - Test Case-80 Completed");
	}

	@Test(dependsOnMethods = { "test07_upload300License" })
	public void test09_300_connectionLimitCheck() throws InterruptedException { // 300 license - Connection Limit Check
		log.info("Test Case-81 - 300 License - Connections Limit Check");
		methods.connectionsLicenseLimitTest(driver, boxillaManager);
		log.info("300 License - Connections Limit Check - Test Case-81 Completed");
	}

	@Test
	public void test10_addBoxillaUser() throws InterruptedException { // Add Boxilla User
		log.info("Test Case-82 - Add Boxilla User");
		methods.addBoxillaUser(driver, "boxillauser1");
		methods.deleteBoxillaUser(driver, "boxillauser1"); // Delete user as database reset doesn't delete it
		log.info("Boxilla user added - Test Case-82 Completed");
	}
	
}
