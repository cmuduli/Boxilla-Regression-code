package invisaPC;

/**
 * Class that defines an receiver device
 * @author Boxilla
 *
 */

public class RX extends Device {
	
	
	public String toString() {
		String sep = System.lineSeparator();
		return "RX Default Gateway:" + getDefault_gateway() + sep + "RX DNS Address:" + getDns_address() + sep
				+ "RX SW Version:" + getSw_ver() + sep + "RX DNS2 Address:" + getDns2_address() + sep 
				+ "RX Serial Num:" + getSerial_num() + sep + "RX Model Num:" + getModel_num() + sep
				+ "Name:" + getName() + sep + "RX MAC:" + getMac() + sep + "RX Network_Mask:" + getNetwork_mask() + sep
				+ "IP Address:" + getIp_address() + sep + "Created At:" + getCreatedAt() + sep + "Updated at:" + getUpdatedAt();
	}

}
