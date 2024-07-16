package bonding;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;

import java.awt.List;
import java.util.ArrayList;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import methods.DevicesMethods;
import methods.ZoneMethods;
import northbound.post.config.CreateZoneConfig;
import northbound.post.config.EditZoneReceiversConfig;
import objects.Devices;
/**
 * This class contains tests for creation / editing / deletion 
 * of bonded receiver groups
 * @author Boxilla
 *
 */
public class BondedReceivers extends StartupTestCase {

	final static Logger log = Logger.getLogger(BondedReceivers.class);

	private DevicesMethods deviceMethods = new DevicesMethods();
	private EditZoneReceiversConfig editReceiver = new EditZoneReceiversConfig();
	private ZoneMethods zones = new ZoneMethods();
	private String shRxIp = "10.231.128.87";
	private String dhRxIp = "10.231.128.141";
	private ArrayList<String> groupNames = new ArrayList<String>();

	/**
	 * This set up will run once before any tests in this class run. 
	 * It set up the Northbound REST API
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
	}
	/**
	 * Returns the URL for the Northbound REST API for 
	 * GET Bonded Group
	 * @param ip - The IP address of Boxilla
	 * @return - URL for GET bonded group
	 */
	public String getUrl(String ip) {
		return getHttp() + "://" + ip + getPort() + "/control/configuration/bonded_group";
	}
	/**
	 * Runs a REST API call to the Northbound GET Bonded Group
	 * @param receiverIp - The IP address of the receiver
	 * @param group - The name of the bonded group
	 */
	public void getBondedGroup(String receiverIp, String group) {
		RestAssured.authentication = basic(restuser, restPassword);			//REST authentication
		RestAssured.useRelaxedHTTPSValidation();
		given().header(getHead())
		.when()
		.get(getUrl(receiverIp))
		.then().assertThat().statusCode(200);
	}

	/**
	 * Test that creates a bonded receiver group and verifies group is
	 * created successfully
	 * @throws InterruptedException
	 */
	@Test
	public void test01_createReceiverGroup() throws InterruptedException {
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName()};
		deviceMethods.createBondedGroup(driver, "test01RecGroup", "", receivers);
		removeGroupAfterClass("test01RecGroup");
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, "test01RecGroup", "2",
				receivers, "");
		getBondedGroup(rxSingle.getIpAddress(), "test01RecGroup");
		getBondedGroup(rxDual.getIpAddress(), "test01RecGroup");
	}

	/**
	 * Test that deletes a bonded receiver group and verifies the group 
	 * has been deleted from Boxilla
	 * @throws InterruptedException
	 */
	@Test
	public void test02_deleteReceiverGroup() throws InterruptedException {
		deviceMethods.deleteGroup(driver, "test01RecGroup");
		getBondedGroup(rxSingle.getIpAddress(), "");
		getBondedGroup(rxDual.getIpAddress(), "");
	}
	/**
	 * Test that edits the receivers in a bonded reciever group
	 * @throws InterruptedException
	 */
	@Test
	public void test03_editReceiverGroup() throws InterruptedException {
		String groupName = "test03RecGroup";
		String newGroupName = groupName + "New";
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX"};
		String[] newReceiver = {"SE_SH_RX", rxSingle.getDeviceName()};
		deviceMethods.createBondedGroup(driver, groupName, "", receivers);
		
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "4", receivers, "");

		getBondedGroup(rxSingle.getIpAddress(), groupName);
		getBondedGroup(rxDual.getIpAddress(), groupName);
		getBondedGroup(shRxIp, "");
		getBondedGroup(dhRxIp, "");

		deviceMethods.editBondedReceiverGroup(driver, groupName, newGroupName, receivers, newReceiver);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, newGroupName, "2", newReceiver, "");
		removeGroupAfterClass(newGroupName);
		
		getBondedGroup(rxSingle.getIpAddress(), "");
		getBondedGroup(rxDual.getIpAddress(), "");
		getBondedGroup(shRxIp, newGroupName);
		getBondedGroup(dhRxIp, newGroupName);
		deviceMethods.deleteGroup(driver, newGroupName);
		getBondedGroup(rxSingle.getIpAddress(), "");
		getBondedGroup(rxDual.getIpAddress(), "");
		getBondedGroup(shRxIp, "");
		getBondedGroup(dhRxIp, "");
	}

	
	
	/**
	 * Test that will add 6 Reciver to the Bonded group
	 * \
	 * @throws InterruptedException
	 */
	
	@Test 
	public void test04_createSixBondedReciverGroup() throws InterruptedException
	{
		String groupName = "test04RecGroup";
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX","4K_SH_RX","4KR_SH_RX"};
		deviceMethods.createBondedGroup(driver, groupName, "", receivers);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "6", receivers, "");
		deviceMethods.deleteGroup(driver, groupName);
		
	}
	/**
	 * Test that edits the index of the receivers in the receiver group
	 * by moving a receiver up the group
	 * @throws InterruptedException
	 */
	
	@Test
	public void test05_moveReceiverUp() throws InterruptedException {
		String groupName = "test05RecGroup";
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX"};
		String[] receiversNew = {"SE_DH_RX", rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX"};
		deviceMethods.createBondedGroup(driver, groupName, "", receivers);
		removeGroupAfterClass(groupName);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "4", receivers, "");

		getBondedGroup(rxSingle.getIpAddress(), groupName);
		getBondedGroup(rxDual.getIpAddress(), groupName);
		getBondedGroup(shRxIp, groupName);
		getBondedGroup(dhRxIp, groupName);

		deviceMethods.moveBondedReceiverUp(driver, groupName, receiversNew, 0, 3);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "4", receiversNew, "");
		getBondedGroup(rxSingle.getIpAddress(), groupName);
		getBondedGroup(rxDual.getIpAddress(), groupName);
		getBondedGroup(shRxIp, groupName);
		getBondedGroup(dhRxIp, groupName);
		deviceMethods.deleteGroup(driver, groupName);
	}

	/**
	 * Test that edits the index of the receivers in the receiver group
	 * by moving a receiver down the group
	 * @throws InterruptedException
	 */
	@Test
	public void test06_moveReceiverDown() throws InterruptedException {
		String groupName = "test06RecGroup";
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX"}; 
		String[] receiversNew = { rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX", rxSingle.getDeviceName()};
		deviceMethods.createBondedGroup(driver, groupName, "", receivers);
		removeGroupAfterClass(groupName);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "4", receivers, "");

		getBondedGroup(rxSingle.getIpAddress(), groupName);
		getBondedGroup(rxDual.getIpAddress(), groupName);
		getBondedGroup(shRxIp, groupName);
		getBondedGroup(dhRxIp, groupName);
		deviceMethods.moveBondedReceiverDown(driver, groupName, receiversNew, 0, 3);
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, groupName, "4", receiversNew, "");
		getBondedGroup(rxSingle.getIpAddress(), groupName);
		getBondedGroup(rxDual.getIpAddress(), groupName);
		getBondedGroup(shRxIp, groupName);
		getBondedGroup(dhRxIp, groupName);
		deviceMethods.deleteGroup(driver, groupName);

	}

	/**
	 * Test that will create a zone then a bonded receiver group and 
	 * adds the receiver group to the zone
	 * @throws InterruptedException
	 */
	@Test
	public void test07_createBondedReceiverGroupZones() throws InterruptedException {
		String zoneName = "test07BondZone";
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		String[] receivers = {rxSingle.getDeviceName(), rxDual.getDeviceName(), "SE_SH_RX", "SE_DH_RX"};
		String[] noReceivers = {};
		//create zone
		log.info("CREATING ZONE");
		zoneConfig.createZone(zoneName, "Zone for bond", boxillaManager, boxillaRestUser, boxillaRestPassword);
		log.info("Zone created");
		log.info("ADDING RECEIVERS TO ZONE");
		editReceiver.editReceivers(zoneName, receivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		log.info("CREATING BONDED GROUP");
		deviceMethods.createBondedGroup(driver, "test06RecBond", zoneName, receivers);
		removeGroupAfterClass("test06RecBond");
		deviceMethods.checkAllBondedReceiverGroupDetails(driver, "test06RecBond", "4", receivers, zoneName);
		getBondedGroup(rxSingle.getIpAddress(), "test06RecBond");
		getBondedGroup(rxDual.getIpAddress(), "test06RecBond");
		getBondedGroup(shRxIp, "test06RecBond");
		getBondedGroup(dhRxIp, "test06RecBond");
		log.info("REMOVING RECEIVERS FROM ZONE");
		editReceiver.editReceivers(zoneName, noReceivers, boxillaManager, boxillaRestUser, boxillaRestPassword);
		deviceMethods.deleteGroup(driver, "test06RecBond");
		zones.deleteZone(driver, zoneName);
	}
	
	
	@Test
	public void test08_CreateMultipleGroupWithSameReciver() throws InterruptedException 
	{
		String groupName = "test08RecGroup";
		String groupName1="test09RecGroup";
		String[] receiver= {rxSingle.getDeviceName()};
		// It will create one bonded Group with single Receiver
		deviceMethods.createBondedGroup(driver, groupName, "", receiver);
		
		//
		deviceMethods.navigateToDevicesGroups(driver);
		SeleniumActions.seleniumClick(driver, Devices.getCreateBondedReceiverGroup());
		new WebDriverWait(driver, 60)
				.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getAddReceiverGroupSaveButton())));
		log.info("Create bonded receiver group pop up showing");
		new WebDriverWait(driver, 60)
		.until(ExpectedConditions.elementToBeClickable(By.xpath(Devices.getAddReceiverGroupSaveButton())));
        log.info("Create bonded receiver group pop up showing");
        SeleniumActions.seleniumSendKeys(driver, Devices.getBondedReceiverDropdown("0"), receiver[0]);
        SeleniumActions.seleniumSendKeys(driver, Devices.getReceiverGroupName(), groupName1);
		SeleniumActions.seleniumClick(driver, Devices.getAddReceiverGroupSaveButton());
		new WebDriverWait(driver, 60).until(
				ExpectedConditions.visibilityOfElementLocated(By.xpath(Devices.getSaveSystemProperyToastXpath())));
		String toast = SeleniumActions.seleniumGetText(driver, Devices.getSaveSystemProperyToastXpath());
		String toastDetails = SeleniumActions.seleniumGetInnerText(driver, Devices.getSaveSystemProperyToastXpath());
		Assert.assertTrue(toast.contains("error")," Successfully group is created with the same reciver name. Actual : "+ toast +" "+ toastDetails);
	}
	
	private void removeGroupAfterClass(String groupName) {
		if (!groupNames.contains(groupName)) {
		  groupNames.add(groupName);
		}
	}
	
	@AfterClass
	public void afterClass() {
	  log.info("BondedReceivers suite complete. Removing groups.");
	  cleanUpLogin();
	  deviceMethods.navigateToDevicesGroups(driver);

	  groupNames.forEach(group -> {
		  try {
				if(deviceMethods.doesGroupExist(driver, group, true)) {
					deviceMethods.deleteGroup(driver, group);
				}
			} 
	 		catch (InterruptedException e) {
				log.warn("Failed to remove receiver group: "+group+". \n Error: "+e.getMessage());
	 		}
	  });
	  cleanUpLogout();
	}
}
