package northbound.get.config;

public class GetIndividualZoneConfig {

	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/zones";
	}
	
	public class GetZone {
		public String name;
	}
	
	public String getZoneName(int position) {
		return "message.zones_info[" + position + "].name";
	}
	public String getZoneDescription(int position) {
		return "message.zones_info[" + position + "].description"; 
	}
	
	
	
}
 