package stubs;

import extra.StartupTestCase;
import methods.ConnectionsMethods;
import methods.DiscoveryMethods;
import methods.SystemMethods;
import methods.UsersMethods;

public class ResetDatabase {
	
	public static void main(String[] args) throws InterruptedException {
		if(args[0].equalsIgnoreCase("resetdb"))
			restoreDatabase();
		
		if(args[0].equalsIgnoreCase("connectionAll")) {
			if(args[1] == null || args[2] == null) {
				System.out.println("remoteappfail");
				return;
			}
			createConnectionAllEnabled(args[1], args[2]);
		}
		
		if(args[0].equalsIgnoreCase("createuser")) {
			if(args[1] == null || args[2] == null) {
				System.out.println("remoteappfail");
			}else {
				createRemoteAppUser(args[1], args[2]);
			}
		}
		
		
		if(args[0].equalsIgnoreCase("manageDevice")) {
			if(args[1] == null || args[2] == null || args[3] == null) {
				System.out.println("remoteappfail");
				return;
			}else {
				manageDevice(args[1], args[2], args[3]);
			}
		}
	
	}
	
	public static void manageDevice(String ipaddress, String mac, String deviceName) {
		StartupTestCase start  = null;
		try {
			start = new StartupTestCase();
			start.cleanUpLogin();
			DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
			discoveryMethods.discoverDevices(start.driver);
			discoveryMethods.stateAndIPcheck(start.driver, mac, "10.211.128",
				ipaddress, "10.211.128.1", "255.255.248.0");
			discoveryMethods.manageApplianceAutomatic(start.driver, deviceName, mac,
				"10.211.128");
			start.cleanUpLogout();
			System.out.println("remoteapppass");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("remoteappfail");
		}
	}
	
	public static void createRemoteAppUser(String name, String type) {
		//password will be admin by default
		StartupTestCase start  = null;
		try {
			 start = new StartupTestCase();
			start.cleanUpLogin();
			UsersMethods user = new UsersMethods();
			user.addRemoteAppUser(start.driver,type,  name);
			start.cleanUpLogout();
			System.out.println("remoteapppass");
		}catch(Exception e) {
			e.printStackTrace();
			start.cleanUpLogout();
			System.out.println("remoteappfail");
		}
	}
	public static void createConnectionNoneEnabled(String name, String ip) {
		StartupTestCase start = null;
		try {
			 start = new StartupTestCase();
			start.cleanUpLogin();
			ConnectionsMethods methods = new ConnectionsMethods();
			
			methods.addConnection(start.driver, name, "no"); // connection name, user template
			methods.connectionInfo(start.driver, "tx", "","", ip); // connection via, name, host ip
			methods.chooseCoonectionType(start.driver, "private"); // connection type
			methods.enableExtendedDesktop(start.driver);
			methods.enableUSBRedirection(start.driver);
			methods.enableAudio(start.driver);
			methods.enablePersistenConnection(start.driver);
			methods.propertyInfoClickNext(start.driver);
			methods.saveConnection(start.driver, name); // Connection name to assert
			start.cleanUpLogout();
			System.out.println("remoteapppass");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("remoteappfail");
			start.cleanUpLogout();
		}
	}
	
	public static void createConnectionAllEnabled(String name, String ip) {
		StartupTestCase start = null;
		try {
			 start = new StartupTestCase();
			start.cleanUpLogin();
			ConnectionsMethods methods = new ConnectionsMethods();
			
			methods.addConnection(start.driver, name, "no"); // connection name, user template
			methods.connectionInfo(start.driver, "tx", "","", ip); // connection via, name, host ip
			methods.chooseCoonectionType(start.driver, "private"); // connection type
			methods.enableExtendedDesktop(start.driver);
			methods.enableUSBRedirection(start.driver);
			methods.enableAudio(start.driver);
			methods.enablePersistenConnection(start.driver);
			methods.propertyInfoClickNext(start.driver);
			methods.saveConnection(start.driver, name); // Connection name to assert
			start.cleanUpLogout();
			System.out.println("remoteapppass");
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("remoteappfail");
			start.cleanUpLogout();
		}
	}

	public static void restoreDatabase() {
		try {
		StartupTestCase start = new StartupTestCase();
		start.cleanUpLogin();
		SystemMethods system = new SystemMethods();
		system.dbReset(start.driver);
		start.cleanUpLogout();
		}catch(Exception e) {
			e.printStackTrace();
			System.out.println("remoteappfail");
		}
		System.out.println("remoteapppass");
	}

}
