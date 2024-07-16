package northbound.post.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.util.Map;

import org.apache.log4j.Logger;

import extra.RESTStatistics;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;

public class EditUserFavouritesConfig extends RESTStatistics {

	final static Logger log = Logger.getLogger(CreateZoneConfig.class);
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/users/kvm/favs";
	}
	
	
	public class EditUserFavouriteObject {
		public String username;
		public String scope;
		public Map<String, Object> settings;
	}
	
	public void editUserFavourite(String username, String scope, Map<String,Object> settings, String boxillaManager, String boxillaRestUser, 
			String boxillaRestPassword) {
		
		EditUserFavouriteObject obj = new EditUserFavouriteObject();
		obj.username = username;
		obj.scope = scope;
		obj.settings = settings;
		
		String slot = "";
		if(!scope.equals("")) {
			slot = scope;
		}else {
			slot = "global";
		}
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(obj)
		.post(getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated favorite settings for user " + username + " to " + slot + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaManager), REQUEST_TYPE.POST, response);
		
	}
	
	
}
