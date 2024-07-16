package northbound.get.config;

public class VersioningConfig {

	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp + "/bxa-api/version";
	}
	
	public String getBoxillaSoftwareVersion() {
		return "message.software_version";
	}
	public String getBoxillaSerialNumber() {
		return "message.serial_number";
	}
	public String getBoxillaModelNumber() {
		return "message.model_number";
	}
	public String getApiVersion() {
		return "message.api_version";
	}
	
}
