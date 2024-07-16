package invisaPC;

/**
 * Abstract class that defines a device object
 * @author Boxilla
 *
 */

public abstract class Device {

	private String default_gateway = "";
	private String dns_address = "";
	private String sw_ver = "";
	private String dns2_address = "";
	private String serial_num = "";
	private String model_num = "";
	private String name = "";
	private String mac = "";
	private String network_mask = "";
	private String ip_address = "";

	//database properties
	String createdAt = "";
	String updatedAt = "";
	String fqdn = "";
	String broadcast = "";
	String os = "";
	String arch = "";
	String spn = "";
	String upgradeState = "";
	String upgradeStatus = "";
	String location = "";
	String assetNumber = "";
	String zoneId = "";
	String propertiesState = "";
	String jwt = "";

	public String getDefault_gateway() {
		return default_gateway;
	}

	public String getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(String createdAt) {
		if(createdAt == null) {
			this.createdAt = "";
		}
		this.createdAt = createdAt;
	}

	public String getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(String updatedAt) {
		if(updatedAt == null) {
			this.updatedAt = "";
		}else {
			this.updatedAt = updatedAt;
		}
	}

	public String getFqdn() {
		return fqdn;
	}
	public void setFqdn(String fqdn) {
		if(fqdn == null) {
			this.fqdn = "";
		}else {
			this.fqdn = fqdn;
		}
	}

	public String getBroadcast() {
		return broadcast;
	}

	public void setBroadcast(String broadcast) {
		if(broadcast == null) {
			this.broadcast = "";
		}else {
			this.broadcast = broadcast;
		}
	}

	public String getOs() {
		return os;
	}

	public void setOs(String os) {
		if(os == null) {
			this.os = "";
		}else {
			this.os = os;
		}
	}

	public String getArch() {
		return arch;
	}

	public void setArch(String arch) {
		if(arch == null) {
			this.arch = "";
		}else {
			this.arch = arch;
		}
	}

	public String getSpn() {
		return spn;
	}

	public void setSpn(String spn) {
		if(spn == null) {
			this.spn = "";
		}else {
			this.spn = spn;
		}
	}

	public String getUpgradeState() {
		return upgradeState;
	}

	public void setUpgradeState(String upgradeState) {
		if(upgradeState == null) {
			this.upgradeState = "";
		}else {
			this.upgradeState = upgradeState;
		}
	}

	public String getUpgradeStatus() {
		return upgradeStatus;
	}

	public void setUpgradeStatus(String upgradeStatus) {
		if(upgradeStatus == null) {
			this.upgradeStatus = "";
		}else {
			this.upgradeStatus = upgradeStatus;
		}
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		if(location == null) {
			this.location = "";
		}else {
			this.location = location;
		}
	}

	public String getAssetNumber() {
		return assetNumber;
	}

	public void setAssetNumber(String assetNumber) {
		if(assetNumber == null) {
			this.assetNumber = "";
		}else {
			this.assetNumber = assetNumber;
		}
	}

	public String getZoneId() {
		return zoneId;
	}

	public void setZoneId(String zoneId) {
		if(zoneId == null) {
			this.zoneId = "";
		}else {
			this.zoneId = zoneId;
		}
	}

	public String getPropertiesState() {
		return propertiesState;
	}

	public void setPropertiesState(String propertiesState) {
		if(propertiesState == null) {
			this.propertiesState = "";
		}else {
			this.propertiesState = propertiesState;
		}
	}

	public String getJwt() {
		return jwt;
	}

	public void setJwt(String jwt) {
		if(jwt == null) {
			this.jwt = "";
		}else {
			this.jwt = jwt;
		}
	}
	public void setDefault_gateway(String default_gateway) {
		if(default_gateway == null) {
			this.default_gateway = "";
		}else {
			this.default_gateway = default_gateway;
		}
	}

	public String getDns_address() {
		return dns_address;
	}

	public void setDns_address(String dns_address) {
		if(dns_address == null) {
			this.dns_address = "";
		}else {
			this.dns_address = dns_address;
		}
	}

	public String getSw_ver() {
		return sw_ver;
	}

	public void setSw_ver(String sw_ver) {
		if(sw_ver == null) {
			this.sw_ver = "";
		}else {
			this.sw_ver = sw_ver;
		}
	}

	public String getDns2_address() {
		return dns2_address;
	}

	public void setDns2_address(String dns2_address) {
		if(dns2_address == null) {
			this.dns2_address = "";
		}else {
			this.dns2_address = dns2_address;
		}
	}

	public String getSerial_num() {
		return serial_num;
	}

	public void setSerial_num(String serial_num) {
		if(serial_num == null) {
			this.serial_num = "";
		}else {
			this.serial_num = serial_num;
		}
	}

	public String getModel_num() {
		return model_num;
	}

	public void setModel_num(String model_num) {
		if(model_num == null) {
			this.model_num = "";
		}else {
			this.model_num = model_num;
		}
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		if(name == null) {
			this.name = "";
		}else {
			this.name = name;
		}
	}

	public String getMac() {
		return mac;
	}

	public void setMac(String mac) {
		if(mac == null) {
			this.mac = "";
		}else {
			this.mac = mac.toLowerCase();
		}
	}

	public String getNetwork_mask() {
		return network_mask;
	}

	public void setNetwork_mask(String network_mask) {
		if(network_mask == null) {
			this.network_mask = "";
		}else {
			this.network_mask = network_mask;
		}
	}

	public String getIp_address() {
		return ip_address;
	}

	public void setIp_address(String ip_address) {
		if(ip_address == null) {
			this.ip_address = "";
		}else {
			this.ip_address = ip_address;
		}
	}

	@Override
	public boolean equals(Object obj) {
		if(obj == null) 
			return false;

		Device device = (Device) obj;

		if(this.dns_address.equals(device.getDns_address()) && this.default_gateway.equals(device.getDefault_gateway())
				&& this.sw_ver.equals(device.getSw_ver()) && this.dns2_address.equals(device.getDns2_address()) &&
				this.serial_num.equals(device.getSerial_num()) && this.name.equals(device.getName()) &&
				this.mac.equals(device.getMac()) && this.network_mask.equals(device.getNetwork_mask()) &&
				this.ip_address.equals(device.getIp_address()) && this.createdAt.equals(device.getCreatedAt())
				&& this.updatedAt.equals(device.getUpdatedAt()) && this.fqdn.equals(device.getFqdn()) &&
				this.broadcast.equals(device.getBroadcast()) && this.os.equals(device.getOs()) && 
				this.arch.equals(device.getArch()) && this.spn.equals(device.getSpn()) && 
				this.upgradeState.equals(device.getUpgradeState()) && this.upgradeStatus.equals(device.getUpgradeStatus())
				&& this.location.equals(device.getLocation()) && this.assetNumber.equals(device.getAssetNumber())
				&& this.zoneId.equals(device.getZoneId()) && this.propertiesState.equals(device.getPropertiesState()) 
				&& this.jwt.equals(device.getJwt())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 239;
		result = 55 * result + this.default_gateway.hashCode();
		result = 55 * result + this.getName().hashCode();
		result = 55 * result + this.getMac().hashCode();
		result = 55 * result + this.getIp_address().hashCode();
		result = 55 * result + this.getDns2_address().hashCode();
		result = 55 * result + this.getArch().hashCode();

		return result;

	}


}
