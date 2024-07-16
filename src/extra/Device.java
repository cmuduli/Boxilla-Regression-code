package extra;
/**
 * Class to represent a TX or RX device. Dual or single head.
 * Used by the appliance pool
 * @author Brendan O'Regan
 *
 */
 public class Device {
	
	 /**
	  * Creates a Device object
	  * 
	  * @param ipAddress the IP address of the device
	  * @param mac MAC address of the device
	  * @param gateway Gateway IP address of the device
	  * @param netmask Netmask IP address of the device
	  */

	
	public Device(String ipAddress, String mac, String gateway, String netmask, boolean isEmerald, String deviceName) {
		this.ipAddress = ipAddress;
		this.mac = mac;
		this.gateway = gateway;
		this.netmask = netmask;
		this.isEmerald = isEmerald;
		this.deviceName = deviceName;
	}
	
	private String ipAddress, gateway, netmask, mac;
	private boolean isDual;
	private boolean isReceiver;
	private boolean isEmerald;
	private String deviceName;
	
	
	public String getDeviceName() {
		return deviceName;
	}
	public void setDeviceName(String deviceName) { 
		this.deviceName = deviceName;
	}
	
	public void setEmerald(boolean isEmerald) {
		this.isEmerald = isEmerald;
	}
	public boolean getEmerald() {
		return isEmerald;
	}
	
	public String getMac() {
		return this.mac;
	}
	public void setMac(String mac) {
		this.mac = mac;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public String getGateway() {
		return gateway;
	}
	public void setGateway(String gateway) {
		this.gateway = gateway;
	}
	public String getNetmask() {
		return netmask;
	}
	public void setNetmask(String netmask) {
		this.netmask = netmask;
	}
	public boolean isDual() {
		return isDual;
	}
	/**
	 * Used to indicate if the device is dual or single head
	 * 
	 * @param type set this to single for single head and dual for dual head
	 */
	public void setDual(String type) {
		if(type.equals("single")) {
			isDual = false;
		}else if (type.equals("dual")){
			isDual = true;
		}
	}
	public boolean isReceiver() {
		return isReceiver;
	}
	/**
	 * Used to indicate if device is receiver or transmitter
	 * @param type set to tx for transmitter or rx for receiver
	 */
	public void setReceiver(String type) {
		if(type.equals("tx")) {
			isReceiver = false;
		}else if(type.equals("rx")) {
			isReceiver = true;
		}
	}
	
	/**
	 * Overriding objects toString method. Prints all device details
	 */
	public String toString() {
		return "IP=" + getIpAddress() + " MAC=" + getMac() +  " Gateway=" + getGateway() + " Netmask=" + getNetmask()
		 + " Is Receiver=" + isReceiver() + " Is Dual=" + isDual() + " Is Emerald=" + getEmerald() + " Name=" + getDeviceName() ;
	}
	
}