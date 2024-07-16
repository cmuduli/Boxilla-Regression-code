package bonding;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.filter.log.RequestLoggingFilter;
import io.restassured.filter.log.ResponseLoggingFilter;
import io.restassured.http.ContentType;
import methods.ConnectionsMethods;
import northbound.get.BoxillaHeaders;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateZoneConfig;

/**
 * This class contains all the test cases for testing the 
 * creation / editing / deletion of Bonded connections in 
 * Boxilla
 * @author Boxilla
 *
 */
public class BondedConnection extends StartupTestCase {


	private CreateKvmConnectionsConfig createCon = new CreateKvmConnectionsConfig();
	private String txConnectionName = "createBonded";
	private String txConnectionShreadName="createBondedShared";
	private String vmConnectionName = "createBondedVM";
	final static Logger log = Logger.getLogger(BondedConnection.class);
	private ConnectionsMethods connections = new ConnectionsMethods();
	
	
	
	  

	/**
	 * This set up method will run once before any test cases in this class run.
	 * It will set up different kind of connections in Boxilla to be used
	 * in the test cases
	 */
	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());
		//creating connections to use in tests
		createCon.createViaTxConnection(txConnectionName + "1", txIp, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionName + "2", txIpDual, "Private", "Yes", "ConnectViaTx", "Yes", "Yes",
				"Yes", "Yes", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);
		
		createCon.createViaTxConnection(txConnectionName + "3", sh, "Private", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);
		
		createCon.createViaTxConnection(txConnectionName + "4", dh, "Private", "Yes", "ConnectViaTx", "Yes", "Yes",
				"Yes", "Yes", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionShreadName + "1", txIp, "Shared", "Yes", "ConnectViaTx", "Yes", "Yes",
				"Yes", "No", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionShreadName + "2", txIp, "Shared", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionShreadName + "3", txIp, "Shared", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager, boxillaRestUser, boxillaRestPassword);
//
//		createCon.createViaTxConnection(txConnectionName + "6", sh, "Private", "No", "ConnectViaTx", "No", "No",
//				"No", "No", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

//		createCon.createViaTxConnection(txConnectionName + "7", dh, "Private", "Yes", "ConnectViaTx", "Yes", "Yes",
//				"Yes", "Yes", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		createCon.createViaTxConnection(txConnectionShreadName + "4", txIp, "Shared", "No", "ConnectViaTx", "No", "No",
				"No", "No", "10", "", boxillaManager,  boxillaRestUser, boxillaRestPassword);

		//create VM connections
		createCon.createVmConnection(vmConnectionName + "1", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "2", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "3", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "4", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "5", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "6", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "7", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);

		createCon.createVmConnection(vmConnectionName + "8", "No", "No", "Yes", "No", "3898", "blackbox.com", "1.1.1.1", "test", "test", "Private", "No",
				"", boxillaManager, boxillaRestUser, boxillaRestPassword);
	}
	/**
	 * This test will set up a bonded connection with 4 viaTX(private connection)
	 * connections in Boxilla and verify
	 * the bonded connection has been set up correctly
	 * @throws InterruptedException
	 */
	@Test
	public void test01_bondedConnection4viaTx() throws InterruptedException {
		log.info("This test will create a bonded connection group with 8 via txConnections");
		String[] connectionsForBond = new String[4];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test01Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test01Bond", "4", connectionsForBond, "");
	}
	
	@Test
	public void test02_bondedSharedConnection4viaTx() throws InterruptedException {
		log.info("This test will create a bonded Shared connection group with 8 via txConnections");
		String[] connectionsForBond = new String[4];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionShreadName + j;
		}
		connections.createBondedConnection(driver, "test02Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test02Bond", "4", connectionsForBond, "");
	}
	
	
	/**
	 * This test will set up a bonded connection with 8 VM
	 * connections in Boxilla and verify
	 * the bonded connection has been set up correctly
	 * @throws InterruptedException
	 */
//	@Test
	public void test03_bondedConnection8VM() throws InterruptedException {
		log.info("This test will create a bonded connection group with 8 via txConnections");
		String[] connectionsForBond = new String[8];
		for(int j=1; j < 9; j++) {
			connectionsForBond[j-1] = vmConnectionName + j;
		}
		connections.createBondedConnection(driver, "test03Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test03Bond", "8", connectionsForBond, "");
	}
	/**
	 * This test will set up a bonded connection using 
	 * an existing bonded connection as a template
	 * @throws InterruptedException
	 */
	@Test
	public void test04_bondedConnectionFromExisting() throws InterruptedException  {
		log.info("This test will create a bonded connection group with 8 connections from another bonded group");
		String[] connectionsForBond = new String[4];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test03Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test03Bond", "4", connectionsForBond, "");

		log.info("Bonded connection created. Creating another from this bonded group");
		connections.createBondedConnection(driver, "test03FromBond", "", connectionsForBond, "test03Bond");
		connections.checkAllBondedConnectionGroupDetails(driver, "test03FromBond", "4", connectionsForBond, "");
	}
	/**
	 * This test will setup and then delete a bonded connection
	 * @throws InterruptedException
	 */
	@Test
	public void test05_deleteBondedConnectionGroup() throws InterruptedException {
		log.info("This test will create a bonded connection group and then delete that group");
		String[] connectionsForBond = new String[4];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test04Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test04Bond", "4", connectionsForBond, "");
		connections.deleteBondedConnectionGroup(driver, "test04Bond");
		Assert.assertFalse(connections.doesBondedConnectionExist(driver, "test04Bond", false), "Bonded connection group was not deleted");
	}
	/**
	 * This test will edit an existing bonded connections
	 * name
	 * @throws InterruptedException
	 */
	@Test
	public void test06_editBondedConnectionGroupName() throws InterruptedException {
		String[] connectionsForBond = new String[4];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test05Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test05Bond", "4", connectionsForBond, "");

		//edit
		connections.editBondedConnection(driver, "test05Bond", "test05newBond", connectionsForBond, null);
		connections.checkAllBondedConnectionGroupDetails(driver, "test05newBond", "4", connectionsForBond, "");	
	}
	/**
	 * This test will edit an existing bonded connection
	 * connection list
	 * @throws InterruptedException
	 */
//	@Test
	public void test06_editBondedConnectionConnectionList() throws InterruptedException {
		String[] connectionsForBond = new String[8];
		String[] newConnectionForBond = new String[8];
		for(int j=1; j < 9; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test06Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test06Bond", "8", connectionsForBond, "");

		for(int j=1; j < 9; j++) {
			newConnectionForBond[j-1] = vmConnectionName + j;
		}
		connections.editBondedConnection(driver, "test06Bond", "", connectionsForBond, newConnectionForBond);
		connections.checkAllBondedConnectionGroupDetails(driver, "test06Bond", "8", newConnectionForBond, "");
	}

	/**
	 * This test will edit an existing bonded connections connection and 
	 * have less connections than previous
	 * @throws InterruptedException
	 */
//	@Test
	public void test07_editBondedConnectionConnectionListLessConnections() throws InterruptedException {
		String[] connectionsForBond = new String[8];
		String[] newConnectionForBond = new String[4];
		for(int j=1; j < 9; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test07Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test07Bond", "8", connectionsForBond, "");

		for(int j=1; j < 5; j++) {
			newConnectionForBond[j-1] = vmConnectionName + j;
		}
		connections.editBondedConnection(driver, "test07Bond", "", connectionsForBond, newConnectionForBond);
		connections.checkAllBondedConnectionGroupDetails(driver, "test07Bond", "4", newConnectionForBond, "");
	}

	/**
	 * This test will edit a bonded connections connection list 
	 * and increase the number of connections
	 * @throws InterruptedException
	 */
//	@Test
	public void test08_editBondedConnectionConnectionListMoreConnections() throws InterruptedException {
		String[] connectionsForBond = new String[4];
		String[] newConnectionForBond = new String[8];
		for(int j=1; j < 5; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		connections.createBondedConnection(driver, "test08Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test08Bond", "4", connectionsForBond, "");

		for(int j=1; j < 9; j++) {
			newConnectionForBond[j-1] = vmConnectionName + j;
		}
		connections.editBondedConnection(driver, "test08Bond", "", connectionsForBond, newConnectionForBond);
		connections.checkAllBondedConnectionGroupDetails(driver, "test08Bond", "8", newConnectionForBond, "");
	}

	/**
	 * This test will edit an existing bonded connection by 
	 * deleting one of the connection in the bonded connection
	 * @throws InterruptedException
	 */
//	@Test
	public void test09_editBondedConnectionDeleteConnection() throws InterruptedException {
		String[] connectionsForBond = new String[8];
		for(int j=1; j < 9; j++) {
			connectionsForBond[j-1] = txConnectionName + j;
		}
		String[] connectionListWithDeleted = new String[7];
		connectionListWithDeleted[0] = txConnectionName + 1;
		connectionListWithDeleted[1] = txConnectionName + 2;
		connectionListWithDeleted[2] = txConnectionName + 3;
		connectionListWithDeleted[3] = txConnectionName + 5;
		connectionListWithDeleted[4] = txConnectionName + 6;
		connectionListWithDeleted[5] = txConnectionName + 7;
		connectionListWithDeleted[6] = txConnectionName + 8;

		//create initial bonded connection
		connections.createBondedConnection(driver, "test09Bond", "", connectionsForBond, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test09Bond", "8", connectionsForBond, "");
		connections.deleteConnectionFromBond(driver, "test09Bond", "3", connectionListWithDeleted);
		connections.checkAllBondedConnectionGroupDetails(driver, "test09Bond", "7", connectionListWithDeleted, "");
	}
	/**
	 * This test will edit the order of connections in the 
	 * bonded connection using the Up arrow
	 * @throws InterruptedException
	 */
//	@Test
	public void test10_EditBondedConnectionMoveConnectionUp() throws InterruptedException {
		String[] connectionList = new String[8];
		connectionList[0] = txConnectionName + 1;
		connectionList[1] = txConnectionName + 2;
		connectionList[2] = txConnectionName + 3;
		connectionList[3] = txConnectionName + 4;
		connectionList[4] = txConnectionName + 5;
		connectionList[5] = txConnectionName + 6;
		connectionList[6] = txConnectionName + 7;
		connectionList[7] = txConnectionName + 8;

		String[] connectionListMoved = new String[8];
		connectionListMoved[0] = txConnectionName + 1;
		connectionListMoved[1] = txConnectionName + 2;
		connectionListMoved[2] = txConnectionName + 3;
		connectionListMoved[3] = txConnectionName + 4;
		connectionListMoved[4] = txConnectionName + 5;
		connectionListMoved[5] = txConnectionName + 8;
		connectionListMoved[6] = txConnectionName + 6;
		connectionListMoved[7] = txConnectionName + 7;

		connections.createBondedConnection(driver, "test10Bond", "", connectionList, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test10Bond", "8", connectionList, "");

		connections.editBondedConnectionMoveConnectionUp(driver, "test10Bond", "5", "7", connectionListMoved);
		connections.checkAllBondedConnectionGroupDetails(driver, "test10Bond", "8", connectionListMoved, "");
	}

	/**
	 * This test will edit the order of connections in the 
	 * bonded connection using the Down arrow
	 * @throws InterruptedException
	 */
	//Already commented
	//@Test
	public void test11_EditBondedConnectionMoveConnectionDown() throws InterruptedException {
		String[] connectionList = new String[8];
		connectionList[0] = txConnectionName + 1;
		connectionList[1] = txConnectionName + 2;
		connectionList[2] = txConnectionName + 3;
		connectionList[3] = txConnectionName + 4;
		connectionList[4] = txConnectionName + 5;
		connectionList[5] = txConnectionName + 6;
		connectionList[6] = txConnectionName + 7;
		connectionList[7] = txConnectionName + 8;

		String[] connectionListMoved = new String[8];
		connectionListMoved[0] = txConnectionName + 2;
		connectionListMoved[1] = txConnectionName + 3;
		connectionListMoved[2] = txConnectionName + 4;
		connectionListMoved[3] = txConnectionName + 5;
		connectionListMoved[4] = txConnectionName + 1;
		connectionListMoved[5] = txConnectionName + 6;
		connectionListMoved[6] = txConnectionName + 7;
		connectionListMoved[7] = txConnectionName + 8;

		connections.createBondedConnection(driver, "test11Bond", "", connectionList, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test11Bond", "8", connectionList, "");

		connections.editBondedConnectionMoveConnectionDown(driver, "test11Bond", "1", "5", connectionListMoved);
		connections.checkAllBondedConnectionGroupDetails(driver, "test11Bond", "8", connectionListMoved, "");
	}

	/**
	 * This test will create a zone and then a bonded connection 
	 * which will be added to this zone
	 * @throws InterruptedException
	 */
//	@Test
	public void test12_bondedConnectionInZone() throws InterruptedException {
		String conName = "test12BondCon";
		String zoneName = "test12BondZone";
		CreateZoneConfig zoneConfig = new CreateZoneConfig();
		//create zone
		zoneConfig.createZone(zoneName, "Zone for bond", boxillaManager, boxillaRestUser, boxillaRestPassword);
		//create connections
		for(int j=0; j < 5; j++) {
			createCon.createViaTxConnection(conName + j, txIp, "Private", "No", "ConnectViaTx", "No", "No",
					"No", "No", "10", zoneName, boxillaManager, boxillaRestUser, boxillaRestPassword);
		}
		//create bonded connection group
		String[] connectionList = new String[5];
		connectionList[0] = conName + 0;
		connectionList[1] = conName + 1;
		connectionList[2] = conName + 2;
		connectionList[3] = conName + 3;
		connectionList[4] = conName + 4;

		connections.createBondedConnection(driver, "test12Bond", zoneName, connectionList, "");
		connections.checkAllBondedConnectionGroupDetails(driver, "test12Bond", "5", connectionList, zoneName);


		//delete all connections
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));

		//delete all zones
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/zones/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all zones."));
	}
	
	@AfterClass
	public void afterClass() {
		//delete all connections
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully deleted all connections."));
	}
}
