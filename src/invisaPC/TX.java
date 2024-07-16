package invisaPC;
/**
 * Class that defines a trasnmitter device
 * @author Boxilla
 *
 */
public class TX extends Device {
	
	
	public String toString() {
		String sep = System.lineSeparator();
		return "TX Default Gateway:" + getDefault_gateway() + sep + "TX DNS Address:" + getDns_address() + sep
				+ "TX SW Version:" + getSw_ver() + sep + "TX DNS2 Address:" + getDns2_address() + sep 
				+ "TX Serial Num:" + getSerial_num() + sep + "TX Model Num:" + getModel_num() + sep
				+ "Name:" + getName() + sep + "TX MAC:" + getMac() + sep + "TX Network_Mask:" + getNetwork_mask() + sep
				+ "IP Address:" + getIp_address();
	}

}
