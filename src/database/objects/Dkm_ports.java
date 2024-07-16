package database.objects;

public class Dkm_ports {

	private String id = "";
	private String switch_name = "";
	private String port_type = "";
	private String port_id = "";
	private String port_name = "";
	private String created_at = "";
	private String updated_at = "";
	private String hex = "";
	private String connection = "";
	private String receiver_ip = "";
	private String rcpu = "";
	private String real_virtual = "";
	
	
	@Override
	public int hashCode() {
		int result = 939;
		result = 22 * result + this.id.hashCode();
		result = 22 * result + this.switch_name.hashCode();
		result = 22 * result + this.port_type.hashCode();
		result = 22 * result + this.port_id.hashCode();
		result = 22 * result + this.hex.hashCode();
		
		return result;
	}
	
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;

		Dkm_ports ports = (Dkm_ports) obj;
		
		if(this.id.equals(ports.getId()) && this.switch_name.equals(ports.getSwitch_name()) &&
				this.port_type.equals(ports.getPort_type()) && this.port_id.equals(ports.getPort_id()) &&
				this.port_name.equals(ports.getPort_name()) && this.created_at.equals(ports.getCreated_at()) 
				&& this.updated_at.equals(ports.getUpdated_at()) && this.hex.equals(ports.getHex()) &&
				this.connection.equals(ports.getConnection()) && this.receiver_ip.equals(ports.getReceiver_ip())
				&& this.rcpu.equals(ports.getRcpu()) && this.real_virtual.equals(ports.real_virtual)) {
			return true;
		}
		return false;
	}
	
	public String toString() {
		String sep = System.lineSeparator();
		return "id:" + this.id + sep + "switch_name:" + this.switch_name + 
				sep + "port_type:" + this.port_type + sep + "port_id:" + this.port_id
				+ sep + "port_name:" + this.port_name + sep + "created_at:" + this.created_at + 
				sep + "updated_at:" + this.updated_at + sep + "hex:" + this.hex + sep +
				"connection:" + this.connection + sep + "receiver_ip:" + this.receiver_ip + 
				sep + "rcpu:" + this.rcpu + sep + "real_virtual:" + this.real_virtual;
	}
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		if(id == null) {
			this.id = "";
		}else {
			this.id = id;
		}	
	}
	
	public String getSwitch_name() {
		return switch_name;
	}
	
	public void setSwitch_name(String switch_name) {
		if(switch_name == null) {
			this.switch_name = "";
		}else {
			this.switch_name = switch_name;
		}
	}
	
	public String getPort_type() {
		return port_type;
	}
	
	public void setPort_type(String port_type) {
		if(port_type == null) {
			this.port_type = "";
		}else {
			this.port_type = port_type;
		}
	}
	
	public String getPort_id() {
		return port_id;
	}
	
	public void setPort_id(String port_id) {
		if(port_id == null) {
			this.port_id = "";
		}else {
			this.port_id = port_id;
		}
	}
	
	public String getPort_name() {
		return port_name;
	}
	
	public void setPort_name(String port_name) {
		if(port_name == null) {
			this.port_name = "";
		}else {
			this.port_name = port_name;
		}
	}
	
	public String getCreated_at() {
		return created_at;
	}
	
	public void setCreated_at(String created_at) {
		if(created_at == null) {
			this.created_at = "";
		}else {
			this.created_at = created_at;
		}
	}
	
	public String getUpdated_at() {
		return updated_at;
	}
	
	public void setUpdated_at(String updated_at) {
		if(updated_at == null) {
			this.updated_at = "";
		}else {
			this.updated_at = updated_at;
		}
	}
	
	public String getHex() {
		return hex;
	}
	
	public void setHex(String hex) {
		if(hex == null) {
			this.hex = "";
		}else {
			this.hex = hex;
		}
	}
	
	public String getConnection() {
		return connection;
	}
	
	public void setConnection(String connection) {
		if(connection == null) {
			this.connection = "";
		}else {
			this.connection = connection;
		}
	}
	
	public String getReceiver_ip() {
		return receiver_ip;
	}
	
	public void setReceiver_ip(String receiver_ip) {
		if(receiver_ip == null) {
			this.receiver_ip = "";
		}else {
			this.receiver_ip = receiver_ip;
		}
	}
	
	public String getRcpu() {
		return rcpu;
	}
	
	public void setRcpu(String rcpu) {
		if(rcpu == null) {
			this.rcpu = "";
		}else {
			this.rcpu = rcpu;
		}
	}
	
	public String getReal_virtual() {
		return real_virtual;
	}
	
	public void setReal_virtual(String real_virtual) {
		if(real_virtual == null) {
			this.real_virtual = "";
		}else {
			this.real_virtual = real_virtual;
		}
	}
}
