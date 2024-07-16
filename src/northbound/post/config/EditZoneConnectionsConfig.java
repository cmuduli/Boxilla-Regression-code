package northbound.post.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import extra.RESTStatistics;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;

public class EditZoneConnectionsConfig extends RESTStatistics {

	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/zones/connections/kvm";
	}
	
	public class EditConnections {
		public String zone_name;
		public String[] connection_names;
	}
	
	public void editZoneConnections(String zoneName, String[] connectionNames, String boxillaIp, String restUser, String restPassword) {
		EditConnections editCon = new EditConnections();
		editCon.zone_name = zoneName;
		editCon.connection_names = connectionNames;
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(editCon)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body("message", equalTo(successMessage(connectionNames, zoneName))).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);

	}
	
	/**
	 * This method will generate the correct success message no matter how many connections are added to the zone
	 * @param connections
	 * @param zoneName
	 * @return
	 */
	public String successMessage(String[] connections, String zoneName) {
		String response = "";
		if(connections.length > 1) {
			response = "Successfully assigned connections [";
		}else {
			response = "Successfully assigned connection [";
		}
		for(String s : connections) {
			response = response + "\"" + s + "\", "; 
		}
		
		if(connections.length > 0) {
			int spaceIndex = response.lastIndexOf(" ");
			response = new StringBuilder(response).replace(spaceIndex, spaceIndex+1, "").toString();
		}
		
		response = response + "] for zone " + zoneName + ".";
		int index = response.lastIndexOf(",");
		if(index >=0) {
			response = new StringBuilder(response).replace(index, index + 1,"" ).toString();
		}
		return response;
	}

	
	
	
}
