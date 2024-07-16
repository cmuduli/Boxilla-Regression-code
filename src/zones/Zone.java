package zones;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.UsersMethods;
import methods.ZoneMethods;
import northbound.get.BoxillaHeaders;
import objects.Connections;
import objects.Users;

public class Zone extends StartupTestCase {

	final static Logger log = Logger.getLogger(Zone.class);
	private ZoneMethods zones = new ZoneMethods();
	private ConnectionsMethods connections = new ConnectionsMethods();
	private DevicesMethods devMethods = new DevicesMethods();
	private UsersMethods users = new UsersMethods();
	private DiscoveryMethods methods = new DiscoveryMethods();
	
	
	@Test
	public void test01_createZone() throws InterruptedException {
		String zoneName = "TestZoneA";
		String zoneDescription = "This is a zone used for testing";
		zones.addZone(driver, zoneName, zoneDescription);
		Thread.sleep(3000);
		String[] details = zones.getZoneDetails(driver, zoneName);
		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
		Assert.assertTrue(details[1].equals(zoneDescription), "Zone description from details did not equal " + zoneDescription + ". Actual:" + details[1]);
		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);		
	}
	
	@Test
	public void test02_editZone() throws InterruptedException
	{
		
		String zoneNewName = "downstairszoneA";
		String zoneNewDescription = "A description for " + zoneNewName;
		String originalZoneName="TestZoneA";
		String originalZoneDescription="This is a zone used for testing";
		zones.editZone(driver, originalZoneName, zoneNewName, zoneNewDescription);
		Thread.sleep(3000);
		String[] details = zones.getZoneDetails(driver, zoneNewName);
		
		Assert.assertTrue(details[0].equals(zoneNewName), "Zone name from details did not equal " + zoneNewName + ". Actual:" + details[0]);
		Assert.assertTrue(details[1].equals( zoneNewDescription), "Zone description from details did not equal " +  zoneNewDescription + ". Actual:" + details[1]);
		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);	
		//Revert back to the original Zone Name and Description 
		Thread.sleep(5000);
		zones.editZone(driver, zoneNewName, originalZoneName, zoneNewDescription);
		
	}
	@Test
	public void test03_deleteZone() throws InterruptedException {
		String zoneName = "TestZoneA";
		zones.deleteZone(driver, zoneName);
		Assert.assertFalse(zones.isZoneAvailable(driver, zoneName), "Zone " + zoneName + " is still available after being deleted");
	}
	
	@Test
	public void test04_maximumZones() throws InterruptedException {
		String zoneName = "testZone";
		char zoneAppend = 'A';
		
		for(int j =0; j < 10; j++) {
			String zoneName2 = zoneName + zoneAppend++;
			zones.addZone(driver, zoneName2, "This is a zone");
			//this test runs too fast going to slow it down a little
			String[] details = zones.getZoneDetails(driver, zoneName2);
			Assert.assertTrue(details[0].equals(zoneName2), "Zone name from details did not equal " + zoneName2 + ". Actual:" + details[0]);
			Assert.assertTrue(details[1].equals( "This is a zone"), "Zone description from details did not equal " +  "This is a zone" + ". Actual:" + details[1]);
			Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
			Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
			Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);		
		}
		String toastMessage =  zones.addZoneLimitReached(driver);
		Assert.assertTrue(toastMessage.equals("Zones limit reached.."), "Zone limit reached toast was mismatched: Actual:" + toastMessage);
		//Deleting all the Zones before starting the next test case
		log.info("Deleting all zones before starting testcases");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
		printSuitetDetails(true);
	}
	
//	@Test
//	public void test04_editZone() throws InterruptedException {
//		Thread.sleep(61327);
//		String zoneName = "testingZoneA";
//		String zoneDescription = "A description for " + zoneName;
//		String zoneNewName = "downstairszoneA";
//		String zoneNewDescription = "A description for " + zoneNewName;
//		zones.addZone(driver, zoneName, zoneDescription);
//		String[] details = zones.getZoneDetails(driver, zoneName);
//		Assert.assertTrue(details[0].equals(zoneName), "Zone name from details did not equal " + zoneName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals( zoneDescription), "Zone description from details did not equal " +  zoneDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);	
//		
//		//edit zone
//		zones.editZone(driver, zoneName, zoneNewName, zoneNewDescription);
//		details = zones.getZoneDetails(driver, zoneNewName);
//		Assert.assertTrue(details[0].equals(zoneNewName), "Zone name from details did not equal " + zoneNewName + ". Actual:" + details[0]);
//		Assert.assertTrue(details[1].equals( zoneNewDescription), "Zone description from details did not equal " +  zoneNewDescription + ". Actual:" + details[1]);
//		Assert.assertTrue(details[2].equals("0"), "Zone users from details did not equal " + 0 + ". Actual:" + details[2]);
//		Assert.assertTrue(details[3].equals("0"), "Zone connections from details did not equal " + 0 + ". Actual:" + details[3]);
//		Assert.assertTrue(details[4].equals("0"), "Zone devices from details did not equal " + 0 + ". Actual:" + details[4]);
			
//	}
	
	@Test
	public void test05_addConnectionToZone() throws InterruptedException {
		String zoneName = "testZonecon";
		String connectionName = "zoneConnection1";
		zones.addZone(driver, zoneName, "this is a zone");
		connections.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false",txIp, driver);
		zones.addConnectionsToZone(driver, zoneName, connectionName);
		
	}
	
	@Test
	public void test06_removeConnectionFromZone() throws InterruptedException {
		String zoneName = "testZonecon";
		String connectionName = "zoneConnection1";
//		zones.addZone(driver, zoneName, "this is a zone");
//		connections.createMasterConnection(connectionName, "tx", "private", "false", "false", "false", "false", "false",txIp, driver);
//		zones.addConnectionsToZone(driver, zoneName, connectionName);
		
//		//connection remove
		zones.removeConnectionsFromZone(driver, zoneName, connectionName);
		
	}
	
	@Test
	public void test07_swapConnectionBetweenZones() throws InterruptedException {
//		Thread.sleep(69827);
		String zoneName1 = "testZoneA";
		String zoneName2 = "testZoneB";
		String connection = "zoneConnnection3";
		
		zones.addZone(driver, zoneName1, "this is a zone");
		connections.createMasterConnection(connection, "tx", "private", "false", "false", "false", "false", "false",txIp, driver);
		zones.addConnectionsToZone(driver, zoneName1, connection);
		zones.addZone(driver, zoneName2, "this is a zone");
		
		zones.swapConnectionFromZone(driver, zoneName1, zoneName2, connection);
		zones.getZoneDetails(driver, zoneName2);
		boolean connectionSwap = zones.isConnectionInActive(driver, connection);
		Assert.assertTrue(connectionSwap, "Connection:" + connection + " was not in active. Did not swap");
		
		//clean up
		connections.deleteConnection(driver, connection);
		zones.deleteZone(driver, zoneName1);
		zones.deleteZone(driver, zoneName2);
	}
	
	/*
	 * test case 08 and test case 10 both testcases covered in northboundpost.editZoneReciver testcases
	 * */
	
//	@Test((already commented)
	public void test08_addRemoveDeviceToZone() throws InterruptedException {
//		Thread.sleep(76938);
		String zoneName = "testZoneC";
		zones.addZone(driver, zoneName, "this is a zone");
		zones.addDevicesToZone(driver, zoneName, rxEmerald.getDeviceName());
		
		zones.removeDevicesFromZone(driver, zoneName, rxEmerald.getDeviceName());
		
		//clean up
		zones.deleteZone(driver, zoneName);	
	}
	
	
//	@Test(already commented)
	public void test10_swapDevicesBetweenZones() throws InterruptedException {
//		Thread.sleep(46939);
		String zoneName1 = "testZoneD";
		String zoneName2 = "testZoneE";
		
		zones.addZone(driver, zoneName1, "this is a zone");
		zones.addZone(driver, zoneName2, "this is also a zone");
		zones.addDevicesToZone(driver, zoneName1, rxEmerald.getDeviceName());
		
		zones.swapDeviceFromZone(driver, zoneName1, zoneName2, rxEmerald.getDeviceName());
		
		zones.getZoneDetails(driver, zoneName2);
		boolean hasSwapped = zones.isDeviceInActive(driver, rxEmerald.getDeviceName());
		Assert.assertTrue(hasSwapped, "Device did not swap to other zone");
//		
//		//clean up
		zones.removeDevicesFromZone(driver, zoneName2, rxEmerald.getDeviceName());
		zones.deleteZone(driver, zoneName1);
		zones.deleteZone(driver, zoneName2);
		
	}
	
	@Test
	public void test11_createTxConnectionWithZone() throws InterruptedException {
		Thread.sleep(3000);
		String zoneName = "testZoneF";
		String connectionName = "zoneConA";
		
		zones.addZone(driver, zoneName, "this is a zone");
//		connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "true", "true", txIp,
//				driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		zones.getZoneDetails(driver, zoneName);
		boolean isActive = zones.isConnectionInActive(driver, connectionName);
		Assert.assertTrue(isActive, "Connection was not active on zone");
		
		connections.deleteConnection(driver, connectionName);
		zones.deleteZone(driver, zoneName);
		
		
	}
	
	@Test
	public void test12_manageDeviceAutoWithZone() throws InterruptedException {
//		Thread.sleep(55848);
		String zoneName = "testZoneG";
		zones.addZone(driver, zoneName, "This is a zone");
		
		devMethods.unManageDevice(driver, rxIpDual);
		log.info("Test Case-61 Started - Adding Appliance using Automatic Discovery");
		methods.discoverDevices(driver);
		methods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(),rxDual.getNetmask());
		methods.manageApplianceAutomatic(driver, rxDual.getDeviceName(), rxDual.getMac(),
				prop.getProperty("ipCheck"), zoneName);
		
		zones.getZoneDetails(driver, zoneName);
		boolean isActiveDevice = zones.isDeviceInActive(driver, rxDual.getDeviceName());
		Assert.assertTrue(isActiveDevice, "Device is not part of the zone");
		
		//clean up
		zones.removeDevicesFromZone(driver, zoneName, rxDual.getDeviceName());
		zones.deleteZone(driver, zoneName);
	}
	
//	@Test(Already added )
	public void test13_addFavouriteFromUserPage() throws InterruptedException {
//		Thread.sleep(72535);
		String zoneName = "testZoneH";
		String connectionName = "zoneConnectionX";
		String user = "zoneUserA";
		zones.addZone(driver, zoneName, "this is a zone");
//		connections.createMasterConnection(connectionName, "TX", "private", "true", "true", "true", "true", "true", txIp,
//				user, driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		users.masterCreateUser(driver, user, "test", "false", "false", "admin", "false", "", "false");
		users.addConnectionToUser(driver, user, connectionName);
		users.selectUserFavourite(driver, user, 1, connectionName, zoneName);
		
		//assert on user > connection fav that connection favourite has been added
		String conFav = users.getCurrentConnectionFavourite1(driver, user,1,zoneName);
		Assert.assertTrue(conFav.equals(connectionName), "Connection name on user > favourite did not match. Expected:" + connectionName + " , Actual:" + conFav );
		//assert on zones > favourites
		String[] favs = zones.getUserFavourites1(driver, zoneName,1,user);
		Assert.assertTrue(favs[0].equals(connectionName), "Connection name on zone > user > favourite did not match. Expected:" + connectionName + " , Actual:" + favs[1]);
		connections.deleteConnection(driver, connectionName);
		zones.deleteZone(driver, zoneName);
	}
//	
	@Test
	public void test14_addFavouriteFromZonePage() throws InterruptedException {
//		Thread.sleep(43940);
		String zoneName = "testZoneH";
		String user = "zoneUserA";
		String connectionName = "zoneConnectionY";
		zones.addZone(driver, zoneName, "this is a zone");
////		connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "true", "true", txIp,
////				driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		users.masterCreateUser(driver, user, "test", "false", "false", "admin", "false", "", "false");
		users.addConnectionToUser(driver, user, connectionName);
		users.selectUserFavourite(driver, user, 1, connectionName, zoneName);
		zones.setUserFavourite(driver, zoneName, user, connectionName, 1);
//		String[] favs = zones.getUserFavourites(driver, zoneName, user);
		String[] favs = zones.getUserFavourites1(driver, zoneName,1, user);
		Assert.assertTrue(favs[0].equals(connectionName), "Connection name was not set");
	}
	
	@Test
	public void test15_editUserFavourite() throws InterruptedException {
//		Thread.sleep(65343);
		String zoneName = "testZoneH";
		String user = "zoneUserA";
		String connectionName = "zoneConnectionY";
		String newConnection = "zoneConnectionZ";
//		connections.createMasterConnection(newConnection, "tx", "private", "true", "true", "true", "true", "true", txIp,
//				driver, zoneName);
		connections.createMasterConnectionZone(newConnection, "private", driver,txIp, zoneName);
		users.addConnectionToUser(driver, user, newConnection);
//		String[] favs = zones.getUserFavourites(driver, zoneName, user);
		String[] favs = zones.getUserFavourites1(driver, zoneName,1, user);
		Assert.assertTrue(favs[2].equals(connectionName), "Connection name was not set");
		zones.setUserFavourite(driver, zoneName, user, newConnection, 2);
//		String[] favs2 = zones.getUserFavourites(driver, zoneName, user);
		String[] favs2 = zones.getUserFavourites1(driver, zoneName,2, user);
		Assert.assertTrue(favs2[2].equals(newConnection), "Connection name was not set");	
	}
	
	@Test
	public void test16_removeUserFavourite() throws InterruptedException {
//		Thread.sleep(65322);
		String zoneName = "testZoneH";
		String user = "zoneUserA";
//		String[]favs = zones.getUserFavourites(driver, zoneName, user);
		String[]favs = zones.getUserFavourites1(driver, zoneName,2, user);
		Assert.assertTrue(favs[2].equals("zoneConnectionZ"));
		zones.unallocateConnection(driver, zoneName, user, 2);
		String favs2 = Users.getfavouritehotkey(driver,2).getAttribute("placeholder");
		System.out.println(favs2);
//		
		Assert.assertTrue(favs2.equals("unallocated"), "Connection was not removed. Expected unallocated, actual:" + favs2);
		
////		//clean up
//		connections.deleteConnection(driver, "zoneConnectionX");
		connections.deleteConnection(driver, "zoneConnectionY");
		connections.deleteConnection(driver, "zoneConnectionZ");
		users.deleteUser(user, driver);
		zones.deleteZone(driver, zoneName);
		
	}
	
	@Test
	public void test18_deleteZoneConnectionAttached() throws InterruptedException {
//		Thread.sleep(17237);
		String zoneName = "testZoneI";
		String connectionName = "zoneConnectionB";
		zones.addZone(driver, zoneName, "This is a zone");
//		connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "true", "true", txIp,
//				driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
//		
		zones.deleteZoneUnable(driver, zoneName);
//		
//		//clean up
		zones.removeConnectionsFromZone(driver, zoneName, connectionName);
		zones.deleteZone(driver, zoneName);
		connections.deleteConnection(driver, connectionName);
	}
	
	@Test
	public void test19_editConnectionToZone() throws InterruptedException {
//		Thread.sleep(37272);
		String zoneName = "testZoneJ";
		String connectionName = "zoneConnectionC";
		zones.addZone(driver, zoneName, "this is a zone");
//		connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "true", "true", txIp,
//				driver);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		connections.editConnectionZone(driver, connectionName, zoneName);
		zones.getZoneDetails(driver, zoneName);
		boolean isActive = zones.isConnectionInActive(driver, connectionName);
		Assert.assertTrue(isActive, "Connection was not in active");
//		
//		//clean up
		connections.deleteConnection(driver, connectionName);
		zones.deleteZone(driver, zoneName);
	}
	
	@Test
	public void test20_launchConnnectionWithZone() throws InterruptedException {
//		Thread.sleep(95457);
		String zoneName = "testZoneK";
		String connectionName = "zoneConnectionD";
		zones.addZone(driver, zoneName, "this is a zone");
//		connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "false", "true", txIp,
//				driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		zones.addDevicesToZone(driver, zoneName, rxEmerald.getDeviceName());
		
		devMethods.recreateCloudData(rxIp, rxIp, true);
		devMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 0);
		Thread.sleep(20000);
		driver.navigate().refresh();
//		//launch connection
		String[] connectionSources = {connectionName};
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, connectionName, rxEmerald.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, rxEmerald.getDeviceName()).isDisplayed();
//		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + rxEmerald.getDeviceName());
		Assert.assertTrue(isConnection, "Connection between " + connectionName + " and " + rxEmerald.getDeviceName() + " is not displayed");
//		
//		
//		//clean up
//		driver.navigate().refresh();
		log.info("Attempting to break connection in connection > viewer");
		connections.breakConnection(driver, connectionName);
        
		connections.deleteConnection(driver, connectionName);
		zones.removeDevicesFromZone(driver, zoneName, rxEmerald.getDeviceName());
		zones.deleteZone(driver, zoneName);
		
	}
	
	@Test
	public void test21_launchConnectionDeviceNotInZone() throws InterruptedException {
//		Thread.sleep(98545);
		String zoneName = "testZoneL";
		String connectionName = "zoneConnectionE";
		zones.addZone(driver, zoneName, "this is a zone");
		try {
//			connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "false", "true", txIp,
//					driver, zoneName);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		
		devMethods.recreateCloudData(rxIp, rxIp, true); 
		devMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 0);
		Thread.sleep(20000);
		driver.navigate().refresh();
//		//launch connection
		String[] connectionSources = {connectionName};
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, connectionName, rxEmerald.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, rxEmerald.getDeviceName()).isDisplayed();
//		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + rxEmerald.getDeviceName());
		Assert.assertTrue(isConnection, "Connection between " + connectionName + " and " + rxEmerald.getDeviceName() + " is not displayed");
//		
//		
//		//clean up
//		devMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 0);
//		driver.navigate().refresh();
		log.info("Attempting to break connection in connection > viewer");
		connections.breakConnection(driver, connectionName);
		connections.deleteConnection(driver, connectionName);
		zones.deleteZone(driver, zoneName);
	}
	
	@Test
	public void test22_launchConnectionConnectionNotInZone() throws InterruptedException {
//		Thread.sleep(85451);
		String zoneName = "testZoneM";
		String connectionName = "zoneConnectionF";
		zones.addZone(driver, zoneName, "this is a zone");
		try {
//			connections.createMasterConnection(connectionName, "tx", "private", "true", "true", "true", "false", "true", txIp,
//					driver);
		connections.createMasterConnectionZone(connectionName, "private", driver,txIp, zoneName);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		zones.addDevicesToZone(driver, zoneName, rxEmerald.getDeviceName());
		devMethods.recreateCloudData(rxIp, rxIp, true);
		devMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 0);
		Thread.sleep(20000);
		driver.navigate().refresh();
//		//launch connection
		String[] connectionSources = {connectionName};
		log.info("Attempting to add soruces");
		connections.addSources(driver, connectionSources);
		log.info("Sources added. Trying to add private destination");
		connections.addPrivateDestination(driver, connectionName, rxEmerald.getDeviceName());
		log.info("Private destination added. Sleeping and refreshing page");
		Thread.sleep(65000);				//wait for boxilla to poll again and make sure the connection is actually up and running
		driver.navigate().refresh();
		Thread.sleep(5000);
		log.info("Checking if connection is active by asserting on the connections > viewer UI");
		boolean isConnection = Connections.singleSourceDestinationCheck(driver, connectionName, rxEmerald.getDeviceName()).isDisplayed();
//		//Assert connection has been created
		log.info("Asserting if connections has been created between " + connectionName + " and " + rxEmerald.getDeviceName());
		Assert.assertTrue(isConnection, "Connection between " + connectionName + " and " + rxEmerald.getDeviceName() + " is not displayed");
//		
//		
//		//clean up
//		devMethods.rebootDeviceSSH(rxIp, "root", "barrow1admin_12", 0);
//		driver.navigate().refresh();
		log.info("Attempting to break connection in connection > viewer");
		connections.breakConnection(driver, connectionName);
		connections.deleteConnection(driver, connectionName);
		zones.removeDevicesFromZone(driver, zoneName, rxEmerald.getDeviceName());
		zones.deleteZone(driver, zoneName);
	}
	
	@AfterClass
	public void afterClass() throws InterruptedException {
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
		
		
		log.info("Deleting all zones before starting testcases");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
		printSuitetDetails(true);
//		try {
//		rebootDevices(rxIp, txIp);
//		}catch(Exception e) {
//			e.printStackTrace();
//			log.info("Error rebooting. Continuing...");
//		}
//	
	
	}
	
	
	@BeforeClass(alwaysRun = true)
	public void beforeclass() throws InterruptedException {
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		//RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		
		log.info("Deleting all connections");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
		
		
		log.info("Deleting all zones before starting testcases");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
//		super.login(browser, method);
	}

}
