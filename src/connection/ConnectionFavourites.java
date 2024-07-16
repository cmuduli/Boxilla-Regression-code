package connection;


import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.UsersMethods;
import objects.Connections;
import objects.Users;
import testNG.Utilities;
/**
 * This class contains tests for the Boxilla / Appliance feature
 * Favourite Connections
 * @author Boxilla
 *
 */
public class ConnectionFavourites extends StartupTestCase {

	final static Logger log = Logger.getLogger(ConnectionFavourites.class);
	private ConnectionsMethods conMethods = new ConnectionsMethods();
	private UsersMethods userMethods = new UsersMethods();
	private DevicesMethods deviceMethods = new DevicesMethods();
	private String user1 = "conFavUser1";
	private String user2 = "conFavUser2";
	private String user3 = "conFavUser3";
	private String connectionName = "favCon";

	/**
	 * Set up that will run once before any tests in this class run.
	 * It will create 10 via TX connections, users and assigns the connections
	 * to these users for use in the test cases
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		getDevices();
		printSuitetDetails(false);
		try {
			cleanUpLogin();
			log.info("Creating 10 connections to use for favourites");
			for(int j=0; j < 10; j++) {
				conMethods.createTxConnection(connectionName + j, "private", driver, txIp);
			}
			log.info("Creating users to assign favourites to");
			userMethods.addUser(driver, user1, "admin", "admin");
			Users.userPrivilegeAdmin(driver).click();
			userMethods.addUserNoTemplateAutoConnectOFF(driver, user1);
			userMethods.addUser(driver, user2, "admin", "admin");
			Users.userPrivilegeAdmin(driver).click();
			userMethods.addUserNoTemplateAutoConnectOFF(driver, user2);
			userMethods.addUser(driver, user3, "admin", "admin");
			Users.userPrivilegeAdmin(driver).click();
			userMethods.addUserNoTemplateAutoConnectOFF(driver, user3);

			log.info("Assigning connections to users");
			userMethods.addAllConnectionsToUser(driver, user1);
			userMethods.addAllConnectionsToUser(driver, user2);

		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();
	}

	/**
	 * Test that will add a connection to user favourite slot 0. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addFavouriteToSlot0() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 0, connectionName + "0");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 0);
		Assert.assertTrue(text.equals(connectionName + "0"), "Favourite 0 did not equal favCon0, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "0", "0");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 1. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test02_addFavouriteToSlot1() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 1, connectionName + "1");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 1);
		Assert.assertTrue(text.equals(connectionName + "1"), "Favourite 1 did not equal favCon1, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "1", "1");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 2. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test03_addFavouriteToSlot2() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 2, connectionName + "2");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 2);
		Assert.assertTrue(text.equals(connectionName + "2"), "Favourite 2 did not equal favCon2, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "2", "2");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 3. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test04_addFavouriteToSlot3() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 3, connectionName + "3");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 3);
		Assert.assertTrue(text.equals(connectionName + "3"), "Favourite 3 did not equal favCon3, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "3", "3");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 4. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test05_addFavouriteToSlot4() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 4, connectionName + "4");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 4);
		Assert.assertTrue(text.equals(connectionName + "4"), "Favourite 4 did not equal favCon4, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "4", "4");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 5. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test06_addFavouriteToSlot5() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 5, connectionName + "5");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 5);
		Assert.assertTrue(text.equals(connectionName + "5"), "Favourite 5 did not equal favCon5, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "5", "5");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 6. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test07_addFavouriteToSlot6() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 6, connectionName + "6");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 6);
		Assert.assertTrue(text.equals(connectionName + "6"), "Favourite 6 did not equal favCon6, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "6", "6");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}
	/**
	 * Test that will add a connection to user favourite slot 7. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test08_addFavouriteToSlot7() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 7, connectionName + "7");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 7);
		Assert.assertTrue(text.equals(connectionName + "7"), "Favourite 7 did not equal favCon7, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "7", "7");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	/**
	 * Test that will add a connection to user favourite slot 8. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test09_addFavouriteToSlot8() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 8, connectionName + "8");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 8);
		Assert.assertTrue(text.equals(connectionName + "8"), "Favourite 8 did not equal favCon8, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "8", "8");
		Assert.assertTrue(isXml, "Favourite was not set in xml");

	}

	/**
	 * Test that will add a connection to user favourite slot 9. Verifies 
	 * in Boxilla UI that the connection has been assigned, then verifies 
	 * the connection favourite in the xml file on the appliance
	 * @throws InterruptedException
	 */
	@Test
	public void test10_addFavouriteToSlot9() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 9, connectionName + "9");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 9);
		Assert.assertTrue(text.equals(connectionName + "9"), "Favourite 9 did not equal favCon9, actual " + text );
		boolean isXml = deviceMethods.checkConnectionFavouritesXml(rxIp, user1, connectionName + "9", "9");
		Assert.assertTrue(isXml, "Favourite was not set in xml");
	}

	//@Test
	public void test11_setDuplicateFavourite() throws InterruptedException {
		log.info("*** EXPECTED TO FAIL. REQUIRMENTS NOT MATCHING WITH S/W ***");
		userMethods.selectUserFavourite(driver, user1, 0, connectionName + "0");
		String message = userMethods.selectUserFavouriteNoAssert(driver, user1, 9, connectionName + "0");
		log.info("Checking error message");
		Assert.assertTrue(message.contains("used more than once"), "Did not get correct toast message error, actual " + message);
		log.info("Checking dropdown");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 9);
		Assert.assertTrue(text.equals(connectionName + "0"), "Duplicate favourite was added");
	}
	/**
	 * This test will unallocate a connection from the user favourite slot 0 
	 * and verify in the UI the connection has been removed. 
	 * @throws InterruptedException
	 */
	@Test
	public void test12_setUnallocated() throws InterruptedException {
		userMethods.selectUserFavourite(driver, user1, 0, connectionName + "0");
		log.info("Checking if favourite was set");
		String text = userMethods.getCurrentConnectionFavourite(driver, user1, 0);
		Assert.assertTrue(text.equals(connectionName + "0"), "Connection favourite was not set");
		SeleniumActions.seleniumClick(driver, Users.favouritesCloseBtn);
		userMethods.selectUserFavourite(driver, user1, 0, "unallocated");
		String unAllocatedText = userMethods.getCurrentConnectionFavourite(driver, user1, 0);
		Assert.assertTrue(unAllocatedText.equals("unallocated"), "Connection favourite was not unset");
	}
	/**
	 * Test that tries to allocate a favourite connection to a user
	 * who has no connections assigned
	 * @throws InterruptedException
	 */
	//@Test
	public void test13_setFavouriteNoConnectionsAssigned() throws InterruptedException {
		userMethods.navigateToManageFavouritesNoCheck(driver, user3);
		userMethods.timer(driver);
		String message = Users.notificationMessage(driver).getText();
		System.out.println(message);
		Assert.assertTrue(message.contains("No Connections Found"), "User was allowed add favourites");
	}

	/**
	 * Test that sets two user favourites
	 * @throws InterruptedException
	 */
	@Test
	public void test14_setTwoFavourites() throws InterruptedException  {
		userMethods.selectUserFavourite(driver, user2, 0, connectionName + "0");
		userMethods.selectUserFavourite(driver, user2, 4, connectionName + "4");
		String text = userMethods.getCurrentConnectionFavourite(driver, user2, 0);
		String text2 = userMethods.getCurrentConnectionFavourite(driver, user2, 4);
		Assert.assertTrue(text.equals(connectionName + "0") &&
				(text2.equals(connectionName + "4")), "Favourite 8 did not equal favCon8, actual " + text );
	}

	/**
	 * Test that sets user favourites on a power user
	 * @throws InterruptedException
	 */
	@Test 
	public void test15_setFavouritePowerUser() throws InterruptedException {
		log.info("adding power user");
		String username = "powerUserFav";
		userMethods.addUser(driver, username, "admin", "admin");
		Users.userPrivilegePower(driver).click();
		userMethods.addUserNoTemplateAutoConnectOFF(driver, username);
		log.info("adding connections to user");
		userMethods.addAllConnectionsToUser(driver, username);

		log.info("Setting power user favourite");
		userMethods.selectUserFavourite(driver, username, 0, connectionName + "0");
		String text = userMethods.getCurrentConnectionFavourite(driver, username, 0);
		Assert.assertTrue(text.equals(connectionName + "0"), "Favourite 0 did not equal favCon0, actual " + text );
	}

	/**
	 * Test that sets user favourites on a general user
	 * @throws InterruptedException
	 */
	@Test
	public void test16_setFavouriteGeneralUser() throws InterruptedException {
		log.info("adding general user");
		String username = "generalUserFav";
		userMethods.addUser(driver, username, "admin", "admin");
		Users.userPrivilegeGeneral(driver).click();
		userMethods.addUserNoTemplateAutoConnectOFF(driver, username);
		log.info("adding connections to user");
		userMethods.addAllConnectionsToUser(driver, username);

		log.info("Setting power user favourite");
		userMethods.selectUserFavourite(driver, username, 0, connectionName + "0");
		String text = userMethods.getCurrentConnectionFavourite(driver, username, 0);
		Assert.assertTrue(text.equals(connectionName + "0"), "Favourite 0 did not equal favCon0, actual " + text );
	}

	/**
	 * Test that sets user favourites on an admin user
	 * and sets the favourite to autoconnect
	 * @throws InterruptedException
	 */
	@Test
	public void test17_setFavouriteAdminUserAutoConnect() throws InterruptedException {
		log.info("Adding admin user with autoconnect");
		String conName = "test17setFavAdmin";
		conMethods.createTxConnection(conName, "private", driver, txIp);
		String username = "adminAutoConnectUserFav";
		userMethods.addUser(driver, username, "admin", "admin");
		Users.userPrivilegeAdmin(driver).click();
		userMethods.addUserNoTemplateAutoConnectON(driver, conName);
		log.info("adding connections to user");
		userMethods.addAllConnectionsToUser(driver, username);
		log.info("Setting admin user with autoconnect favourite");
		userMethods.selectUserFavourite(driver, username, 0, connectionName + "0");
		String text = userMethods.getCurrentConnectionFavourite(driver, username, 0);
		Assert.assertTrue(text.equals(connectionName + "0"), "Favourite 0 did not equal favCon0, actual " + text );
	}
}
