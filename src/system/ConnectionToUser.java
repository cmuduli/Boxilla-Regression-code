package system;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.BeforeClass;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import methods.SystemMethods;
import methods.UsersMethods;
import northbound.get.BoxillaHeaders;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;

public class ConnectionToUser extends StartupTestCase {
	
	private SystemMethods methods = new SystemMethods();
	private ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private CreateKvmConnectionsConfig config = new CreateKvmConnectionsConfig();
	private UsersMethods userMethods = new UsersMethods();
	final static Logger log = Logger.getLogger(SystemLicense.class);

	@BeforeClass(alwaysRun = true)
	public void beforeclass() throws InterruptedException {
		log.info("Before Starting the test cases delete all the connection");
		deleteAllConnection();

	}

	
	@Test
	public void test1_addTwoconnectionToUser() throws InterruptedException {
		log.info("It will add Two Connection to the User");
		connectionsMax(driver, boxillaManager, 2,"ConnectionTest_");
		userMethods.addAllConnectionsToUser(driver, "admin");
		deleteAllConnection();
	}
	
	@Test
	public void test2_add_Hundred_connectionToUser() throws InterruptedException {
		log.info("It will add Hundred Connection to the User");
		connectionsMax(driver, boxillaManager, 100,"ConnectionTestuser_");
		userMethods.addAllConnectionsToUser(driver, "admin");
		deleteAllConnection();
	}
	@Test
	public void test3_add_Eight_Hundred_connectionToUser() throws InterruptedException {
		log.info("It will add EightHundredTen Connection to the User");
		connectionsMax(driver, boxillaManager, 810,"ConnectionTestuserAdmin_");
		driver.navigate().refresh();
		Thread.sleep(6000);
		driver.navigate().refresh();
		userMethods.addAllConnectionsToUser(driver, "admin");
		deleteAllConnection();
	}

	
	
	
	
	public void connectionsMax(WebDriver driver, String boxillaIp, int connectionNumber,String connectionName) {
		
		for (int i = 0; i < connectionNumber; i++) {
			CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
			
			con.name = connectionName +i;
			con.zone = "";
			con.host = txIp;
			con.group = "ConnectViaTx";
			con.connection_type = "Private";
			con.view_only = "No";
			con.extended_desktop = "No";
			con.usb_redirection = "No";
			con.audio = "No";
			con.persistent = "No";
			con.cmode = "10";
			RestAssured.useRelaxedHTTPSValidation();
			RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
			Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
					.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(con)
					.post(config.getUri(boxillaIp)).then().assertThat().statusCode(201)
					.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract()
					.response();
			SaveResponseStatistics(config.getUri(boxillaIp), REQUEST_TYPE.POST, response);
         
		}
		
	}

	
	public void deleteAllConnection() 
    {
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		// RestAssured.filters(new RequestLoggingFilter(), new ResponseLoggingFilter());

		log.info("Deleting all connections");
		given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.delete("https://" + boxillaManager + "/bxa-api/connections/kvm/all").then().assertThat()
				.statusCode(200).body("message", equalTo("Successfully deleted all connections."));
    }

}
