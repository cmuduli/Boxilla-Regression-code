package northbound.get.config;

public class presetConnectionConfig 
{
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/presets";
	}
	
	
	public String getPresetConnectionConnectionName(int position) {
		return "message.presets[" + position + "]";
 	}
	
	

}
