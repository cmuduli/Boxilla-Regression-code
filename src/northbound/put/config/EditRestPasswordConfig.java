package northbound.put.config;

public class EditRestPasswordConfig {

	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/users/rest/password";
	}
	
	public class ChangePassword {
		public String username;
		public String new_password;
	}
}
