package northbound.get.config;

public class ApplianceStatusConfig {

	public class GetStats {
		public String[] device_names;
	}
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/devices/kvm/status";
	}
	
	public String getDeviceZone(int position) {
		return "message.devices[" + position + "].zone";
	}
	public String getDeviceDeviceType(int position) {
		return "message.devices[" + position + "].device_type";
	}
	public String getDeviceLoggedInUserUsername(int position) {
		return "message.devices[" + position + "].logged_in_user.username";
	}
	public String getDeviceLoggedInUserType(int position) {
		return "message.devices[" + position + "].logged_in_user.type";
	}
	public String getDeviceActiveConnectionName(int position1, int position2) {
		return "message.devices[" + position1 + "].active_connections[" + position2 + "].active_connection_name";
	}
	public String getDeviceActiveConnectionType(int position1, int position2) {
		return "message.devices[" + position1 + "].active_connections[" + position2 + "].active_connection_type";
	}
	public String getDeviceActiveConnectinoGroup(int position1, int position2) {
		return "message.devices[" + position1 + "].active_connections[" + position2 + "].active_connection_group";
	}
	public String getDeviceState(int position) {
		return "message.devices[" + position + "].state";
	}
	public String getDeviceStatus(int position) {
		return "message.devices[" + position + "].status";
	}
	public String getDeivceDeviceName(int position) {
		return "message.devices[" + position + "].device_name";
	}
	public String getDeviceMac(int position) {
		return "message.devices[" + position + "].mac";
	}
	public String getDeviceIp(int position) {
		return "message.devices[" + position + "].ip";
	}
}
