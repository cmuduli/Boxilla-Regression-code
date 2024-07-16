package invisaPC;

/**
 * Class that defines a boxilla connection. 
 * Used to easily create a object to test against boxilla connections
 * @author Boxilla
 *
 */
public class Connection {

	private String extDesk = "";
	private String load_bal = "";
	private String domain = "";
	private String audio = "";
	private String persistent = "";
	private String broker = "";
	private String password = "";
	private String name = "";
	private String connectionType = "";
	private String usb_redirection = "";
	private String viaTX = "";
	private String user_name = "";
	private String colour_depth = "";
	private String ip_address = "";
	private String viaTxPool = "";
	private String port = "";
	private String PreEmption_mode = "";
	private String horizon = "";

	public void setHorizon(String horizon) {
		if(horizon == null) {
			this.horizon = "";
		}else {
			this.horizon = horizon;
		}
	}
	public String getHorizon() {
		return this.horizon;
	}

	public void setViaTx(String viaTX) {
		if(viaTX == null) {
			this.viaTX = "";
		}else {
			this.viaTX = viaTX;
		}
	}

	public String getViaTx() {
		return viaTX;
	}

	public void setPreEmption_mode(String PreEmption_mode) {
		if(PreEmption_mode == null) {
			this.PreEmption_mode = "";
		}else {
			this.PreEmption_mode = PreEmption_mode;
		}
	}

	public String getPreEmption_mode() {
		return PreEmption_mode;
	}

	public String getPersistent() {
		return persistent;
	}
	
	public String getExtDesk() {
		return extDesk;
	}
	
	public void setExtDesk(String extDesk) {
		if(extDesk == null) {
			this.extDesk = "";
		}else {
			this.extDesk = extDesk;
		}
	}
	
	public String getLoad_bal() {
		return load_bal;
	}
	
	public void setLoad_bal(String load_bal) {
		if(load_bal == null) {
			this.load_bal = "";
		}else {
			this.load_bal = load_bal;
		}
	}
	
	public String getDomain() {
		return domain;
	}
	
	public void setDomain(String domain) {
		if(domain == null) {
			this.domain = "";
		}else {
			this.domain = domain;
		}
	}
	
	public String getAudio() {
		return audio;
	}
	
	public void setAudio(String audio) {
		if(audio == null) {
			this.audio = "";
		}else {
			this.audio = audio;
		}
	}
	
	public String isPersistent() {
		return persistent;
	}
	
	public void setPersistent(String persistent) {
		if(persistent == null) {
			this.persistent = "";
		}else {
			this.persistent = persistent;
		}
	}
	
	public String getBroker() {
		return broker;
	}
	
	public void setBroker(String broker) {
		if(broker == null) {
			this.broker = "";
		}else {
			this.broker = broker;
		}
	}
	
	public String getPassword() {
		return password;
	}
	
	public void setPassword(String password) {
		if(password == null) {
			this.password = "";
		}else {
			this.password = password;
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
	
	public String getConnectionType() {
		return connectionType;
	}
	
	public void setConnectionType(String connectionType) {
		if(connectionType == null) {
			this.connectionType = "";
		}else {
			this.connectionType = connectionType;
		}
	}
	
	public String getUsb_redirection() {
		return usb_redirection;
	}
	
	public void setUsb_redirection(String usb_redirection) {
		if(usb_redirection == null) {
			this.usb_redirection = "";
		}else {
			this.usb_redirection = usb_redirection;
		}
	}
	
	public String isViaTX() {
		return viaTX;
	}
	
	public void setViaTX(String viaTX) {
		if(viaTX == null) {
			viaTX = "";
		}else {
			this.viaTX = viaTX;
		}
	}

	public String getUser_name() {
		return user_name;
	}

	public void setUser_name(String user_name) {
		if(user_name == null) {
			this.user_name = "";
		}else {
			this.user_name = user_name;
		}
	}

	public String getColour_depth() {
		return colour_depth;
	}

	public void setColour_depth(String colour_depth) {
		if(colour_depth == null) {
			this.colour_depth = "";
		}else {
			this.colour_depth = colour_depth;
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

	public String isViaTxPool() {
		return viaTxPool;
	}

	public void setViaTxPool(String viaTxPool) {
		if(viaTxPool == null) {
			this.viaTxPool = "";
		}else {
			this.viaTxPool = viaTxPool;
		}
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		if(port == null) {
			this.port = "";
		}else {
			this.port = port;
		}
	}

	/**
	 * Overriding objects toString method so easily print the connection details
	 */
	@Override
	public String toString() {
		String sep = System.lineSeparator();
		return "ExtDesk:" + getExtDesk() + sep + "Domain:" + getDomain() + sep
				+  "Audio:" + getAudio() + sep + "Persistent:" + getPersistent() + sep
				+ "Password:" + getPassword() + sep + "PreEmption_Mode:" + getPreEmption_mode() + sep
				+ "Name:" + getName() + sep + "Connection_type:" + getConnectionType() + sep
				+ "USB_Redirection:" + getUsb_redirection() + sep + "viaTx:" + getViaTx() + sep 
				+ "Colour_depth:" + getColour_depth() + sep + "User_Name:" + getUser_name() + sep
				+ "IP_address:" + getIp_address() + sep + "Port:" + getPort() + sep + "Horizon:" + getHorizon() + 
				sep + "Broker:" + getBroker();
	}
	/**
	 * Overriding object equals method to compare two connection objects. 
	 */
	@Override
	public boolean equals(Object obj) {
		if(obj == null)
			return false;

		Connection con = (Connection) obj;

		if(this.getName().equals(con.getName())  && 
				this.domain.equals(con.getDomain()) && this.port.equals(con.getPort()) && this.password.equals(con.getPassword()) &&
				this.PreEmption_mode.equals(con.getPreEmption_mode()) && this.colour_depth.equals(con.getColour_depth()) 
				&& this.user_name.equals(con.getUser_name()) && this.broker.equals(con.getBroker()) 
				&& this.getIp_address().equals(con.getIp_address()) && this.viaTX.equals(con.getViaTx()) && this.getAudio().equals(con.getAudio())
				&& this.getExtDesk().equals(con.getExtDesk()) && this.getConnectionType().equals(con.getConnectionType()) &&
				this.getPersistent().equals(con.getPersistent()) && this.getUsb_redirection().equals(con.getUsb_redirection())) {
			return true;
		}
		return false;
	}
	/**
	 * Overriding object hashcode to compare Connection objects
	 */
	@Override
	public int hashCode() {
		int result = 92;
		result = 12 * result + getIp_address().hashCode();
		result = 12 * result + getExtDesk().hashCode();
		result = 12 * result + getName().hashCode();
		result = 12 * result + getPersistent().hashCode();
		result = 12 * result + getConnectionType().hashCode();
		result = 12 * result + getUsb_redirection().hashCode();

		return result;
	}


}
