package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.anyOf;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import net.bytebuddy.matcher.EqualityMatcher;
import northbound.get.config.presetConnectionConfig;
import objects.Connections;

public class PresetConnection extends StartupTestCase2{
	
	final static Logger log = Logger.getLogger(KvmActiveConnections.class);
	private ConnectionsMethods connections = new ConnectionsMethods();
	private presetConnectionConfig config=new presetConnectionConfig();
	private String presetName = "firstPreset";
	private String privateConnectionName = "testPresetprivate";
	private String sharedConnectionName ="sharedTest";
	
//	@BeforeClass(alwaysRun = true)
	public void beforeclass() throws InterruptedException 
	{
		printSuitetDetails(false);
		getDevices();
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		cleanUpLogin();
		connections.createTxConnection(privateConnectionName, "private", driver, txIp);
		connections.createTxConnection(sharedConnectionName, "shared", driver, txIp);
		

	}

	@Test(priority = 2)
	public void test01_CreatePresetConnection_Private() throws InterruptedException 
	
	{
		
		String[] sourceList = { privateConnectionName };
		String[] destinationList = { singleRxName };
		connections.createPreset(driver, sourceList, destinationList, presetName, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		 Response respons=given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager))
				.then().assertThat().statusCode(200)
				.body(config.getPresetConnectionConnectionName(0), equalTo(presetName)).extract().response();
				SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
				
		
		
	}
	
	@Test(priority = 1)
	public void test02_emptyPresetConnection() 
	{
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();	
		 Response respons=given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager))
				.then().assertThat().statusCode(200)
				.body(config.getPresetConnectionConnectionName(0), equalTo(null)).extract().response();
				SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
		
		
	}
	
	@Test
	public void test03_CreatePresetconnection_Shared() throws InterruptedException 
	{
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		String[] connectionSources = { sharedConnectionName };
		String[] destinations = { singleRxName, dualRxName };
		
		connections.createPreset(driver, connectionSources, destinations, presetName, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);
		
		Response respons=given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager))
				.then().assertThat().statusCode(200)
				.body(config.getPresetConnectionConnectionName(0), equalTo(presetName))
				.body(config.getPresetConnectionConnectionName(0), equalTo(presetName)).extract().response();
				SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
		
		
		
	}
	
	
}




