package northbound.get;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.Test;

import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.ConnectionsMethods;
import northbound.get.config.PresetConnectionDetailsConfig;

public class PresetConnectionDetails extends StartupTestCase2 {
	
	
	

	final static Logger log = Logger.getLogger(KvmActiveConnections.class);
	 private PresetConnectionDetailsConfig config=new PresetConnectionDetailsConfig();
	private ConnectionsMethods connections = new ConnectionsMethods();
	
       private String presetType= "Full"; 
	
       private String presetName = "firstPreset";
       private String privateConnectionName = "testPresetprivate";
	
	
	
	@Test
	public void test() 
	{
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		Response respons = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON)
				.get(config.getUri(boxillaManager)).then().assertThat().statusCode(200)
				.body(config.getPresetName(0), equalTo(presetName))
				.body(config.getPresetype(0), equalTo(presetType))
				.body(config.getPresetConnection(0,0), equalTo(privateConnectionName)).extract().response();
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.GET, respons);
		
	}
	
	
	

}
