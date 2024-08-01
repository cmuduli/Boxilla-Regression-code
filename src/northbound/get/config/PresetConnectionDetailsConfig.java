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
	
	public String getPresetConnectionName(int position,int position1 ) {
		 return "message.presets[" + position + "].data[" + position1 + "].source.connection_name";
	}
	public String getPresetReciverName(int position,int position1,int position2) {
		 return "message.presets[" + position + "].data[" + position1 + "].destinations[" + position2 + "]";
	}
	
	
}
