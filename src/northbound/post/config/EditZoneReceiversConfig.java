package northbound.post.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import extra.RESTStatistics;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;

public class EditZoneReceiversConfig extends RESTStatistics {

	public String getUri(String boxillaIp)  {
		 return "https://" + boxillaIp  + "/bxa-api/zones/devices/kvm/rx";
	}
	
	public class EditZoneReceiversObject {
		public String zone_name;
		public String[] receiver_names;
	}
	
	public void editReceivers(String zone_name, String[] receiver_names, String boxillaIp, String restUser, String restPassword) {
		
		EditZoneReceiversObject edit = new EditZoneReceiversObject();
		edit.zone_name = zone_name;
		edit.receiver_names = receiver_names;
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body("message", equalTo(successMessage(receiver_names, zone_name))).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);

		
	}
	
	public void removeAllReceivers(String zone_name, String boxillaIp, String restUser, String restPassword) {
		String[] receiver_names = {};
		EditZoneReceiversObject edit = new EditZoneReceiversObject();
		edit.zone_name = zone_name;
		edit.receiver_names = receiver_names;
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body("message", equalTo(successMessage(receiver_names, zone_name))).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);
	}
	
	
	public String successMessage(String[] connections, String zoneName) {
		String response = "";
		if(connections.length > 1) {
			response = "Successfully assigned receivers [";
		}else {
			response = "Successfully assigned receiver [";
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
