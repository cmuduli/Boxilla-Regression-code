package northbound.delete.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import extra.RESTStatistics;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;

public class DeleteZoneConfig extends RESTStatistics {

	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/zones";
	}
	
	public class DeleteZoneObject {
		public String[] zone_names;
	}
	
	public void deleteZone(String[] zone_names, String boxillaManager, String boxillaRestUser, String boxillaRestPassword) {
		DeleteZoneObject delete = new DeleteZoneObject();
		delete.zone_names = zone_names;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(delete)
		.delete(getUri(boxillaManager))
		.then().assertThat().statusCode(200).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.DELETE, response);
		//.body("message", equalTo("Successfully created zone " + name + "."));
	}
	
}
