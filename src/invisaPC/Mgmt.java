package invisaPC;

/**
 * Class that defines a management object
 * @author Boxilla
 *
 */

public class Mgmt {

	private String name = "";
	private String MAC = "";
	private String ipAddress = "";
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getMAC() {
		return MAC;
	}
	public void setMAC(String mAC) {
		MAC = mAC;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;
		
		Mgmt mgmt = (Mgmt) obj;
		
		if(this.getName().equals(mgmt.getName()) && this.getMAC().equals(mgmt.getMAC()) &&
				this.getIpAddress().equals(mgmt.getIpAddress())) {
			return true;
		}
		return false;
	}
	
	@Override
	public int hashCode() {
		int result = 483;
		result = 11 * result + this.getName().hashCode();
		result = 11 * result + this.getIpAddress().hashCode();
		result = 11 * result + this.getMAC().hashCode();
		
		return result;
	}
	
}
