package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Arrays;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import northbound.get.config.PresetConnectionDetailsConfig;
import objects.Connections;
import testNG.Utilities;

public class PresetConnectionDetails extends StartupTestCase2 {

	final static Logger log = Logger.getLogger(KvmActiveConnections.class);

	private PresetConnectionDetailsConfig config = new PresetConnectionDetailsConfig();
	private ConnectionsMethods connections = new ConnectionsMethods();

	private String[] presetType = { "Full", "Partial" };

	private String presetName = "firstPreset";
	private String privateConnectionName = "testPresetprivateDetails";
	private String sharedConnectionName = "sharedTestDetails";

//	@BeforeClass(alwaysRun = true)
	public void beforeclass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		try {
			cleanUpLogin();
			connections.createTxConnection(privateConnectionName, "private", driver, txIp);
			connections.createTxConnection(sharedConnectionName, "shared", driver, txIp);
		} catch (Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}

		cleanUpLogout();

	}

	@Test
	public void test01_detailsOfFullPrivateConnections() throws InterruptedException {

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		String[] connectionDestinations = { rxEmerald.getDeviceName() };
		String[] sourceList = { privateConnectionName };
//		String[] destinationList = { singleRxName };rxEmeraldName
		String[] destinationList = { rxEmeraldName };
		connections.createPreset(driver, sourceList, destinationList, presetName, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);

		Response respons = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager)).then().assertThat().statusCode(200)
				.body(config.getPresetName(0), equalTo(presetName)).body(config.getPresetype(0), equalTo(presetType[0]))
				.body(config.getPresetConnectionName(0, 0), equalTo(privateConnectionName))
				.body(config.getPresetReciverName(0, 0, 0), equalTo(connectionDestinations[0])).extract().response();
		// to log the Response
		log.info("Response Body: " + respons.getBody().asString());
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
		connections.deletePreset(driver, presetName);

	}

	@Test
	public void test02_detailsOfPartialSharedConnections() throws InterruptedException {

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();

		String[] connectionDestinations = { rxEmerald.getDeviceName() };
		String[] sourceList = { privateConnectionName };
//		String[] destinationList = { singleRxName };
		String[] destinationList = { rxEmeraldName };
		connections.createPreset(driver, sourceList, destinationList, presetName, true);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);

		Response respons = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager)).then().assertThat().statusCode(200)
				.body(config.getPresetName(0), equalTo(presetName)).body(config.getPresetype(0), equalTo(presetType[1]))
				.body(config.getPresetConnectionName(0, 0), equalTo(privateConnectionName))
				.body(config.getPresetReciverName(0, 0, 0), equalTo(connectionDestinations[0])).extract().response();
		// to log the Response
		log.info("Response Body: " + respons.getBody().asString());
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
		connections.deletePreset(driver, presetName);

	}

}
