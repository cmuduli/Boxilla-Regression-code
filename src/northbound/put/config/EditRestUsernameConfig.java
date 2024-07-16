package northbound.put.config;

public class EditRestUsernameConfig {

	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/users/rest/username";
	}
	
	public class Username {
		public String username;
		public String new_username;
	}
	
}
