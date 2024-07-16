package activeDirectory;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import objects.SystemAll;
import objects.Users;

/**
 * This class contains tests for Active Directory uses in Boxilla
 * @author Boxilla
 *
 */
public class ActiveDirectory extends StartupTestCase {

	final static Logger log = Logger.getLogger(ActiveDirectory.class);

	private SystemMethods sysMethods = new SystemMethods();
	private UsersMethods userMethods = new UsersMethods();
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private DevicesMethods devMethods = new DevicesMethods();
	private String eth2Ip = "198.222.22.20";
	private String eth2Netmask = "255.255.248.0";
	private String eth2Gateway = "198.222.22.1";
	private String adIp = "10.211.129.213";
	private String adPort = "389";
	private String adDomain = "limerick.lab.autotest.com";
	private String adUsername = "Administrator";
//	private String adPassword = "Blackbox2023!!";   //needs to be moved to the properties file
	private String adPassword = "Blackbox@123";

	//	@Test
	public void test01_addEth2Ip() throws InterruptedException {
		sysMethods.addDualNic(driver, eth2Ip, eth2Netmask, eth2Gateway);
	}

	//	@Test 
	public void test02_editEth2Ip() throws InterruptedException {
		sysMethods.editDualNicNetworkDetails(driver, "10.111.11.11", "255.255.255.0", "10.111.11.1");
		log.info("Test passed. Setting network back to good netework");
		sysMethods.editDualNicNetworkDetails(driver, eth2Ip, eth2Netmask, eth2Gateway);
	}


	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	/**
	 * Test to enable Active Directory support in Boxilla
	 * @throws InterruptedException
	 */
	@Test
	public void test03_enableActiveDirectory() throws InterruptedException {
		sysMethods.turnOnActiveDirectorySupport(driver);
	}

	/**
	 * Test to configure an Active Directory server in Boxilla
	 * After server is configured the values in the UI are checked
	 * @throws InterruptedException
	 */
	@Test
	public void test04_configureActiveDirectory() throws InterruptedException {
		sysMethods.enterActiveDirectorySettings(adIp, adPort, adDomain, adUsername, adPassword, driver);

		//get the settings and assert
		String[] settings = sysMethods.getCurrentADSettings(driver);
		log.info("Checking Active Diretory IP");
		Assert.assertTrue(settings[0].equals(adIp), "Active directory IP was not set. Excepted: " + adIp + " actual:" + settings[0]);
		log.info("Checking Active directory port");
		Assert.assertTrue(settings[1].equals(adPort), "Port was not set. Excepted:" + adPort + " actual:" + settings[1]);
		log.info("Checking active directory domain");
		Assert.assertTrue(settings[2].equals(adDomain), "Domain was not set. Expected:" + adDomain + " actual:" + settings[2]);
		log.info("Checking active directory username");
		Assert.assertTrue(settings[3].equals(adUsername), "Username was not set. Excpeted:" + adUsername + " actual:" + settings[3]);
	}

	/**
	 * Test to create an Active Directory user in Boxilla. 
	 * The user should already be created manually on the active directory server
	 * @throws InterruptedException
	 * @throws AWTException 
	 */
	/*
	 * Test to take the Boxilla backup and make some changes into the functionality
	 **/
	 
//	@Test
	public void test_boxillabackup_ADUser() throws InterruptedException, AWTException 
	{
		
		//create a AD user 
		String username = "Chiranjeeb";
//		userMethods.addUserAD(driver, username);
		//create the boxilla backup
		log.info("Test Preparation - Upload Custom Database Backup");
		sysMethods.backupDatabase(driver);
		Robot reobt=new Robot();

		reobt.keyPress(KeyEvent.VK_ENTER);
		reobt.keyRelease(KeyEvent.VK_ENTER);
		System.out.println("Enter key pressed");
		reobt.keyPress(KeyEvent.VK_ENTER);
		reobt.keyRelease(KeyEvent.VK_ENTER);
		
		
		
		
	}
	@Test
	public void test05_createActiveDirectoryUser() throws InterruptedException {
		String username = "bren";
		userMethods.addUserAD(driver, username);
		//wait for AD to sync
		Thread.sleep(60000);
		String status = userMethods.getActiveUserStatus(driver, username);
		log.info("Checking username");
		Assert.assertTrue(status.contains(username), "Status did not contain username. Excepted:" + username + " Actual:" + status );
		log.info("Checking Authorized by");
		Assert.assertTrue(status.contains("Active Directory"), "Authorized by did not contain Active Directory. Actual:" + status);
		log.info("Checking Domain");
		Assert.assertTrue(status.contains(adDomain),"Status did not contain domain. Expected:" + adDomain + " actual:" + status);
		log.info("Checking AD status");
		Assert.assertTrue(status.contains("OU Status"), "Status did not contain OD Status. Expected: ADtrue, actual: " + status );
	}

	/**
	 * Test to create an Active Directory user in Boxilla. This user
	 * will not exist on the active directory server
	 * @throws InterruptedException
	 */
	@Test
	public void test06_createActiveDirectoryUserNoRecord() throws InterruptedException {
		String username = "userNoRecord";
		userMethods.addUserAD(driver, username);
		//wait for AD to sync
		Thread.sleep(60000);
		String status = userMethods.getActiveUserStatus(driver, username);
		log.info("Checking username");
		Assert.assertTrue(status.contains(username), "Status did not contain username. Excepted:" + username + " Actual:" + status );
		log.info("Checking Authorized by");
		Assert.assertTrue(status.contains("Active Directory"), "Authorized by did not contain Active Directory. Actual:" + status);
		log.info("Checking Domain");
		Assert.assertTrue(status.contains(adDomain),"Status did not contain domain. Expected:" + adDomain + " actual:" + status);
		log.info("Checking AD status");
		Assert.assertTrue(status.contains("OU Status"), "Status did not contain AD Status. Expected: ADtrue, actual: " + status );
	}

	/**
	 * Test to create a non active directory user in Boxilla and confirm 
	 * in the UI that the user is not an AD user
	 * @throws InterruptedException
	 */
	@Test
	public void test07_createNonActiveDirectoryUser() throws InterruptedException {
		String username = "nonActive";
		userMethods.addUser(driver, username);
		Users.userPrivilegeAdmin(driver).click();
		log.info("User Privilege - Administrator selected");
		userMethods.addUserNoTemplateAutoConnectOFF(driver, username);
		String status = userMethods.getActiveUserStatus(driver, username);
		log.info("Checking Authroized by. Should be local");
		Assert.assertTrue(status.contains("Local"), "Authorized by was not Local, actual:" + status);
		log.info("Checking AD status");
		Assert.assertTrue(status.contains("ADnone"), "AD Status was not correct, actual:" + status);
	}

	/**
	 * Test to create an active directory user in Boxilla and verify the 
	 * users OUs are added to boxilla
	 * @throws InterruptedException
	 */
	@Test
	public void test08_checkOrganisationalUnit() throws InterruptedException {
		log.info("Add user attached to OU US > Tech Support");
		String username = "danial";
		userMethods.addUserAD(driver, username);
		//wait for AD to sync
		Thread.sleep(60000);
		String status = userMethods.getActiveUserStatus(driver, username);
		log.info(status);
		log.info("Checking AD status");
		Assert.assertTrue(status.contains("OU Status"), "Status did not contain AD Status. Expected: ADtrue, actual: " + status );
		log.info("Checking if OUs are added to boxilla");
		String ou1 = sysMethods.getGroupAssociationsTableData(driver, "US", "ou");
		String ou2 = sysMethods.getGroupAssociationsTableData(driver, "Tech Support", "ou");
		Assert.assertTrue(ou1.contains("US"), "US organisational unit was not in boxilla, actual:" + ou1);
		Assert.assertTrue(ou2.contains("Tech Support"), "Tech Support organisational unit was not in boxilla, actual:" + ou2);
	}
//Test should not expect the US to be removed after deleting the user-Already_commnt
//	@Test
	public void test09_deleteUserCheckOU() throws InterruptedException {
		userMethods.navigateToUsersManage(driver);
		userMethods.deleteUser("danial", driver);
//		log.info("Waiting for AD to sync");
//		Thread.sleep(60000);
//		String ou1 = sysMethods.getGroupAssociationsTableData(driver, "US", "ou");
//		String ou2 = sysMethods.getGroupAssociationsTableData(driver, "Tech Support", "ou");
//		Assert.assertTrue(!ou1.contains("US"), "US organisational unit was in boxilla, actual:" + ou1);
//		Assert.assertTrue(!ou2.contains("Tech Support"), "Tech Support organisational unit was in boxilla, actual:" + ou2);
	}

	/**
	 * Test to link a connection group to an AD users
	 * OU 
	 * @throws InterruptedException
	 */
	@Test
	public void test10_linkGroupToOU() throws InterruptedException {
		String connectionName = "adTest10";
		conMethods.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", 
				"false", "false", txIp, driver);
		conMethods.navigateToGroups(driver);
		conMethods.addConnectionGroup(driver, "testGroup");
		conMethods.addConnectionToSelectedGroup(driver, connectionName, "testGroup");
		sysMethods.linkOUToGroup(driver, "QA", "testGroup");
		String group = sysMethods.getGroupAssociationsTableData(driver, "QA", "cg");
		Assert.assertTrue(group.equals("testGroup"), "testGroup was no added as a connection group");	
	}

	/**
	 * Test to unlink a connection group from an AD users OUs
	 * @throws InterruptedException
	 */
	@Test
	public void test11_unlinkGroupFromOU() throws InterruptedException {
		sysMethods.navigateToActiveDirectory(driver);
		sysMethods.unlinkOUToGroup(driver, "QA");
		String group = sysMethods.getGroupAssociationsTableData(driver, "QA", "cg");
		Assert.assertTrue(group.equals("-"), "Group was not removed: expected: - , actual:" + group);

	}
	/**
	 * Test to create an OU in Boxilla. 
	 * This OU is then verified in the Boxilla UI
	 * @throws InterruptedException
	 */
	@Test
	public void test12_createOu() throws InterruptedException {
		String name = "test12_ou";
		 sysMethods.createOU(driver, name);
//		Assert.assertTrue(toast.contains("OU Succesfully added"), "OU was not succesfully added. Toast message:" + toast);
		//check table for new OU
		String table = sysMethods.getGroupAssociationsTableData(driver, name, "ou");
		Assert.assertTrue(table.equals(name), "OU was not in the OU table");

		//clean up
		sysMethods.deleteOU(driver, name);
	}

	/**
	 * Test to delete an OU from Boxilla and verify 
	 * the OU has been removed
	 * @throws InterruptedException
	 */
	@Test
	public void test13_deleteOu() throws InterruptedException {
		String name = "test13_ou";
		 sysMethods.createOU(driver, name);
//		Assert.assertTrue(toast.contains("OU Succesfully added"), "OU was not succesfully added. Toast message:" + toast);
		//check table for new OU
		String table = sysMethods.getGroupAssociationsTableData(driver, name, "ou");
		Assert.assertTrue(table.equals(name), "OU was not in the OU table");

		//clean up
		sysMethods.deleteOU(driver, name);
		String table2 = sysMethods.getGroupAssociationsTableData(driver, name, "ou");
		log.info("table2:" + table2);
		Assert.assertTrue(table2.equals("No results matched the search criteria."), "OU was in the OU table");
	}

	/**
	 * Test to add a connection to the OU 'undefined'
	 * This will then log the AD user into an appliance 
	 * and check the xml file for the connection
	 * @throws InterruptedException
	 * already commented
	 */
//	@Test
	public void test14_addConnectionToUndefined() throws InterruptedException {
		String connectionName = "test142_con";
		String adUserName = "adTest";
		String adPassword = "Blackbox11!!";
		String groupName = "OU Undefined";

		conMethods.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", 
				"false", "false", txIp, driver);
		conMethods.navigateToGroups(driver);
		conMethods.addConnectionToSelectedGroup(driver, connectionName, groupName);

		Login login = new Login();
		login.username = adUserName;
		login.password = adPassword;
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + adUserName);
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + adUserName + " has successfully logged in to the target receivers."));

		//wait for file to copy then get it
		Thread.sleep(3000);
		String xml = devMethods.getCloudGuiXml(rxIp);
		boolean exists = devMethods.checkAdUserConnection(xml, adUserName, connectionName);
		Assert.assertTrue(exists, "Connection was not part of AD user");
	}


	/**
	 * This test will create an OU and add a connection 
	 * group to this OU. User is then logged into an appliance 
	 * and the xml file is checked for the connection  
	 * @throws InterruptedException
	 */
	
	@Test
	public void test15_createOuAddConGroup() throws InterruptedException {
		String ouName = "AD_Test";
		String connectionName = "test15_con";
		String adUserName = "adTest";
		String adPassword = "Blackbox11!!";
		String groupName = "test14Group";

		conMethods.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", 
				"false", "false", txIp, driver);
		conMethods.addConnectionGroup(driver, groupName);
		conMethods.addConnectionToSelectedGroup(driver, connectionName, groupName);
		sysMethods.createOU(driver, ouName);
		sysMethods.linkOUToGroup(driver, ouName, groupName);

		//log in with user and check connections
		Login login = new Login();
		login.username = adUserName;
		login.password = adPassword;
		login.rx_list = new String[1];
		login.rx_list[0] = rxEmerald.getDeviceName();
		login.forced = "Yes";
		log.info("Logging in user:" + adUserName);
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(login)
		.post("https://" + boxillaManager + "/bxa-api/users/kvm/login")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("User " + adUserName + " has successfully logged in to the target receivers."));

		//wait for file to copy then get it
		Thread.sleep(3000);
		String xml = devMethods.getCloudGuiXml(rxIp);
		boolean exists = devMethods.checkAdUserConnection(xml, adUserName, connectionName);
		Assert.assertTrue(exists, "Connection was not part of AD user");
	    conMethods.dissolveConnectionGroup(driver, groupName);
	}
	
	@Test
	public void test16_dissolveOUConnectionGroup() throws InterruptedException 
	{
		String ouName = "AD_Test_dessolve";
		String connectionName = "test15_con";
		String groupName = "test15Group";

		conMethods.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", 
				"false", "false", txIp, driver);
		conMethods.addConnectionGroup(driver, groupName);
		conMethods.addConnectionToSelectedGroup(driver, connectionName, groupName);
		sysMethods.createOU(driver, ouName);
		sysMethods.linkOUToGroup(driver, ouName, groupName);
		String table = sysMethods.getGroupAssociationsTableData(driver, ouName, "ou");
		Assert.assertTrue(table.equals(ouName), "OU was not in the OU table");
		 conMethods.dissolveConnectionGroup(driver, groupName);
		
		
	}
	@Test
	public void test17_dissolveConnectionGroupADuser() throws InterruptedException 
	{
		String username = "chiran";
		String groupName="test16Group";
		conMethods.addConnectionGroup(driver, groupName);
		userMethods.addUserAD(driver, username);
		conMethods.addGroupToUser(driver, username, groupName);
		conMethods.dissolveConnectionGroup(driver, groupName);
		
	}
	// already commented

	//@Test
	public void test16_createDelete20Ous() throws InterruptedException {

		for(int j=0; j<20; j++) {
			String name = "test16_ou_" + j;
			 sysMethods.createOU(driver, name);
//			Assert.assertTrue(toast.contains("OU Succesfully added"), "OU was not succesfully added. Toast message:" + toast);
			//check table for new OU
			String table = sysMethods.getGroupAssociationsTableData(driver, name, "ou");
			Assert.assertTrue(table.equals(name), "OU was not in the OU table");
			log.info("Test iteration create:" + (j + 1));
		}
		for(int j=0; j<20; j++) {
			//clean up
			String name = "test16_ou_" + j;
			sysMethods.deleteOU(driver, name);
			String table2 = sysMethods.getGroupAssociationsTableData(driver, name, "ou");
			log.info("table2:" + table2);
			Assert.assertTrue(table2.equals("No results matched the search criteria."), "OU was in the OU table");
			log.info("Test iteration delete:" + (j + 1));
		}
	}

	public class Login {
		public String username;
		public String password;
		public String[] rx_list;
		public String forced;
	}
}
