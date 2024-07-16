package northbound.get.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.log4j.Logger;

import methods.DevicesMethods;
import northbound.get.Appliances;

public class ApplianceConfig {

	private Map<String, String> deviceCodes = new HashMap<String, String> ();
	final static Logger log = Logger.getLogger(ApplianceConfig.class);
	
	
	public ApplianceConfig() {
		//set device details
		deviceCodes.put("300-067-001", "EMD2000PE-T-P");
		deviceCodes.put("300-069-001", "EMD2002PE-T-P");
		deviceCodes.put("300-015-001", "DTX1000-T");
		deviceCodes.put("300-013-001", "DTX1000-R");
		deviceCodes.put("300-070-001", "EMD200DV-T");
		deviceCodes.put("300-046-001", "EMD2000SE-R");
		deviceCodes.put("300-071-001", "EMD200EDV-T");
		deviceCodes.put("300-060-001", "EMD2002SE-R");
		deviceCodes.put("300-060-002", "EMD2002SE-R");
		deviceCodes.put("300-061-002", "EMD2002SE-T");
		deviceCodes.put("300-047-001", "EMD2000SE-T");
		deviceCodes.put("300-066-001", "EMD2000PE-R");
		deviceCodes.put("300-068-001", "EMD2002PE-R");
		deviceCodes.put("300-073-001", "EMD200DP-T-N");
		deviceCodes.put("300-041-003", "EMD4000T");
		deviceCodes.put("300-041-001", "EMD4000T");
		deviceCodes.put("300-046-002", "EMD2000SE-R");
		deviceCodes.put("300-072-001", "EMD200DP-T");
		deviceCodes.put("300-040-001", "EMD4000R");
		
		deviceCodes.put("300-040-003", "EMD4000R");
		deviceCodes.put("300-069-002", "EMD2002PE-T-P");
		deviceCodes.put("300-083-001", "EMD2002PE-R-P");
		//For new devices
		deviceCodes.put("300-110-001", "EMD2000SE-T-R2");
		deviceCodes.put("300-112-001", "EMD2000PE-T-R2");
		deviceCodes.put("300-113-001", "EMD4000T");
		deviceCodes.put("300-040-005", "EMD4000R");
		deviceCodes.put("300-041-005", "EMD4000T");
		
		
		
	}
	
	public String getDeviceDeviceName(int position) {
		return "message.devices[" + position + "].name";
	}
	public String getDeviceIp(int position) {
		return "message.devices[" + position + "].ip";
	}
	public String getDeviceMac(int position) {
		return "message.devices[" + position + "].mac";
	}
	public String getDeivceSerialNumber(int position) {
		return "message.devices[" + position + "].serialno";
	}
	public String getDeviceSoftwareVersion(int position) {
		return "message.devices[" + position + "].swversion";
	}
	public String getDeivceModel(int position) {
		return "message.devices[" + position + "].model";
	}

	
	public String convertMpn(String mpn) {
		String convert = deviceCodes.get(mpn);
		log.info("MPN:" + convert);
		return convert;
	}
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/devices/kvm";
	}
	public class GetAppliance {
		public String device_type;
		public String[] device_names;
	}
	
	public String[] setApplianceDetails(String ip, String deviceUserName, String devicePassword) {
		DevicesMethods devices = new DevicesMethods();
		String[] details = new String[3]; 
		details[0] = devices.getSerialNumber(ip, deviceUserName, devicePassword);
		String mpn = "";
		System.out.println(mpn);
		details[1] = devices.getDeviceSwVersion(ip, deviceUserName, devicePassword);
		 mpn = devices.getMpn(ip, deviceUserName, devicePassword);
		 details[2] = convertMpn(mpn);
		
		return details;
	}
}
