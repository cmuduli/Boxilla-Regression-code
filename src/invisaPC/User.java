package invisaPC;
/**
 * Class that defines a user in boxilla
 * @author Boxilla
 *
 */
public class User {

	private String password = "";
	private String privilege = "";
	private String user_name = "";
	private String configuration = "";
	private String autoConnect = "";
	private String autoConnectName = "";
	private String createdAt = "";
	private String updatedAt = "";
	
	
	public String getUpdatedAt() {
		return this.updatedAt;
	}
	
	public void setUpdatedAt(String updatedAt) {
		if(updatedAt == null) {
			this.updatedAt = "";
		}else { 
			this.updatedAt = updatedAt;
		}
	}
	
	public String getCreatedAt() {
		return this.createdAt;
	}
	
	public void setCreatedAt(String createdAt) {
		if(createdAt == null) {
			this.createdAt = "";
		}else {
			this.createdAt = createdAt;
		}
	}
	
	public String getAutoConnectName() {
		return this.autoConnectName;
	}
	
	public void setAutoConnectName(String autoConnectName) {
		if(autoConnectName == null) {
			this.autoConnectName = "";
		}else {
			this.autoConnectName = autoConnectName;
		}
	}
	
	public String getAutoConnect() {
		return this.autoConnect;
	}
	
	public void setAutoConnect(String autoConnect) {
		if(autoConnect == null) {
			this.autoConnect = "";
		}else {
			this.autoConnect = autoConnect;
		}
	}
	
	public String getConfiguration() {
		return this.configuration;
	}
	
	public void setConfiguration(String configuration) {
		if(configuration == null) {
			this.configuration = "";
		}else {
			this.configuration = configuration;
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

	public String getPrivilege() {
		return privilege;
	}

	public void setPrivilege(String privilege) {
		if(privilege == null) {
			this.privilege = "";
		}else {
			this.privilege = privilege;
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
	
	


	public String toString() {
		String sep = System.lineSeparator();
		return "User_name:" + getUser_name() + sep + "password:" + getPassword() + 
				sep + "Privilege:" + getPrivilege() + sep  + "Auto_Connect_Name:" + getAutoConnectName() + 
				sep + "Auto Connect:" + getAutoConnect() + sep + "Created at:" + getCreatedAt() + sep + 
				"Updated at:" + getUpdatedAt() + sep + "Configuration:" + getConfiguration();
	}


	@Override
	public boolean equals(Object a) {
		if(a == null) {
			return false;
		}
		User newUser = (User) a;
		if(this.getUser_name().equals(newUser.getUser_name()) &&
				this.getAutoConnectName().equals(newUser.getAutoConnectName()) &&
				this.getAutoConnect().equals(newUser.getAutoConnect()) &&
				this.getConfiguration().equals(newUser.getConfiguration()) &&
				this.getCreatedAt().equals(newUser.getCreatedAt()) &&
				this.getUpdatedAt().equals(newUser.getUpdatedAt()) &&
				this.getPassword().equals(newUser.getPassword()) &&
				this.getPrivilege().equals(newUser.getPrivilege())) {
			return true;
		}
		return false;
	}

	@Override
	public int hashCode() {
		int result = 384;
		result = 37 * result + getUser_name().hashCode();
		result = 37 * result + getPassword().hashCode();
		result = 37 * result + getPrivilege().hashCode();
		return result;

	}


}
