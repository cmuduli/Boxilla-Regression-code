package northbound.get.config;

public class PresetConnectionDetailsConfig {
	
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/presets/details";
	}
	
	public String getPresetName(int position) {
		return "message.presets[" + position + "].name";
 	}
	public String getPresetype(int position) {
		return "message.presets[" + position + "].type";
 	}
	
	public String getPresetConnection(int position,int position2) {
		return "message.data[" + position + "].source[" + position2 + "].connection_name";
	}
}
