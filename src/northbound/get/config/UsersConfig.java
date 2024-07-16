package northbound.get.config;

public class UsersConfig {

	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/users/kvm";
	}
	
	public class GetUser {
		public String[] usernames;
	}
	
	
	public String getUsersUsername(int position) {
		return "message.users[" + position + "].username";
	}
	public String getUsersPrivilege(int position) {
		return "message.users[" + position + "].privilege";
	}
	public String getUsersAutoConnect(int position) {
		return "message.users[" + position + "].auto_connect";
	}
	public String getUsersAutoConnectName(int position) {
		return "message.users[" + position + "].auto_connect_name";
	}
	public String getUsersRemoteAccess(int position) {
		return "message.users[" + position + "].remote_access";
	}
	public String getUsersConnectionsConnectionName(int position1, int position2) {
		return "message.users[" + position1 + "].connections[" + position2 + "].connection_name";
	}
	
	
	//converts text from data driven file to text the REST API understands
	public String[] convertToStandard(String privilege, String isAutoConnect, String autoConnectName, String isRemote) {
		String [] returnArray = new String[4];
		String priv = "";
		if(privilege.equals("admin")) {
			priv = "Administrator";
		}else if(privilege.equals("power")) {
			priv = "PowerUser";
		}else if(privilege.equals("general")) {
			priv = "User";
		}
		
		String autoCon = "";
		String autoConName = "";
		if(isAutoConnect.equals("true")) {
			autoCon = "Yes";
			autoConName = autoConnectName;
		}else {
			autoCon = "No";
			autoConName = null;
		}
		String remote = "";
		if(isRemote.equals("true")) {
			remote = "Yes";
		}else {
			remote = "No";
		}
		returnArray[0] = priv;
		returnArray[1] = autoCon;
		returnArray[2] = autoConName;
		returnArray[3] = remote;
		return returnArray;
	}
	
}
