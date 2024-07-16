package northbound.get.config;

public class KvmActiveConnectionsConfig {

	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/kvm/active";
	}
	
	public String getActiveConnectionConnectionName(int position) {
		return "message.active_connections[" + position + "].connection_name";
 	}
	
	public String getActiveConnectionReceiverName(int position) {
		return "message.active_connections[" + position + "].receiver_name";
	}
	public String getActiveConnectionHostType(int position) {
		return "message.active_connections[" + position + "].host.type";
	}
	public String getActiveConnectionHostValue(int position) {
		return "message.active_connections[" + position + "].host.value";
	}
	public String getActiveConnectionActiveUser(int position) {
		return "message.active_connections[" + position + "].active_user";
	}
	public String getActiveConnectionType(int position) {
		return "message.active_connections[" + position + "].type";
	}
	public String getActiveConnection(int position) {
		return "message.active_connections[" + position + "]";
	}
	
}
