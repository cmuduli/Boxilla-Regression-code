package methods;

import java.util.ArrayList;

import org.eclipse.jetty.util.log.Log;

import extra.Ssh;
/**
 * This class contains methods to log into a dell switch and run commands
 * @author Boxilla
 *
 */
public class SwitchCLI {

	private Ssh shell;

	public String getRunningConfiguration(String ip) {
		shell = new Ssh("admin", "admin", ip);
		shell.loginToServer();
		return shell.sendCommand("show running-configuration");
	}

	public void exitSwitch() {
		shell.disconnect();
	}

	public String getMgmtPortDetails() {
		shell.sendCommand("interface mgmt 1/1/1");
		return shell.sendCommand("show configuration");
	}
	private ArrayList<String> getEthPortDetails(String ip) {
		ArrayList<String> portList = new ArrayList<String>();

		String output = getRunningConfiguration(ip);
		String[] parsedOutput = output.split("!");
		for(String s : parsedOutput) {
			if(s.contains("ethernet1/1/") && !s.contains("igmp")) {
				portList.add(s);
			}
		}
		exitSwitch();
		return portList;
	}

	public String getEthPortDetails (String ip, String portNumber) {
		String output = getRunningConfiguration(ip);
		String[] parsedOutput = output.split("!");
		for(String s : parsedOutput) {
			if(s.contains("ethernet1/1/" + portNumber) && s.contains("flowcontrol")) {
				System.out.println(s);
				exitSwitch();
				return s;
			}
		}
		exitSwitch();
		return "";
	}
	public String getMgmtPortDetails (String ip) {
		String output = getRunningConfiguration(ip);
		String[] parsedOutput = output.split("!");
		for(String s : parsedOutput) {
			if(s.contains("mgmt1/1/1")) {
				exitSwitch();
				return s;
			}
		}
		exitSwitch();
		return "";
	}
	public String getVlanDetails (String ip, String vlanNumber) {
		String output = getRunningConfiguration(ip);
		String[] parsedOutput = output.split("!");
		for(String s : parsedOutput) {
			if(s.contains("interface vlan" + vlanNumber)) {
				exitSwitch();
				return s;
			}
		}
		exitSwitch();
		return "";
	}

	public boolean isSharedModeEnabled(String ip) {
		String vlanDetails = getVlanDetails(ip, "1003");
		System.out.println(vlanDetails);
		String output = getRunningConfiguration(ip);
		System.out.println(output);
		if(vlanDetails.contains("ip igmp snooping fast-leave") && 
				output.contains("ip igmp snooping enable")) {
			System.out.println("ip igmp snooping fast-leave  and ip igmp snooping enable set OK. Checking ports");
			ArrayList<String> ports = getEthPortDetails(ip);
			for(String s : ports) {
				if(!s.contains("switchport access vlan 1003")) {
					System.out.println("Port is not part of vlan 1003. Shared mode not enabled." + s);
					return false;
				}	else {
					System.out.println("Port is part of vlan 1003. Checking next port");
				}
			}
			System.out.println("All ports are part of vlan 1003");
			exitSwitch();
			return true;
		}
		exitSwitch();
		return false;
	}
	public boolean isSharedModeDisabled(String ip) {
		String vlanDetails = getVlanDetails(ip, "1");
		System.out.println(vlanDetails);
		String output = getRunningConfiguration(ip);
		System.out.println(output);
		if(!vlanDetails.contains("ip igmp snooping fast-leave") && 
				!output.contains("ip igmp snooping enable")) {
			System.out.println("ip igmp snooping fast-leave  and ip igmp snooping enable not set. Checking ports");
			ArrayList<String> ports = getEthPortDetails(ip);
			for(String s : ports) {
				if(s.contains("switchport access vlan 10")) {
					System.out.println("Port is not part of vlan 1." + s);
					return false;
				}	else {
					System.out.println("Port is part of vlan 1. Checking next port");
				}
			}
			System.out.println("No ports are part of vlan 1003");
			exitSwitch();
			return true;
		}
		exitSwitch();
		return false;

	}


}
