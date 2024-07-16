package extra;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.log4j.Logger;

import database.objects.EmeraldRxUpgrades;
import database.objects.EmeraldTxUpgrades;
import database.objects.Kvm_Upgrades;
import database.objects.RxUpgrades;
import database.objects.TxUpgrades;
import invisaPC.RX;
import invisaPC.TX;

/**
 * Class to interact with Boxillas postgres database. 
 * @author Boxilla
 *
 */

public class Database {

	final static Logger log = Logger.getLogger(Database.class);

	Connection connection = null;
	Statement stmt = null;
	boolean isConnected = false;
	ResultSet results;
	ResultSet results2;

	/**
	 * Connect to a portgres database using the creditionals provided
	 * @param ip
	 * @param username
	 * @param password
	 * @param dbName
	 */
	public void connectToDatabase(String ip, String username, String password, String dbName) {

		try { 
			Class.forName("org.postgresql.Driver");
			connection = DriverManager.getConnection("jdbc:postgresql://" + ip + ":5432/" + dbName, username, password);
			log.info("Connected to " + dbName + " on IP " + ip );
			isConnected = true;
		}catch(Exception e) {
			log.info("Error connecting to database");
			isConnected = false;
			e.printStackTrace();
		}
	}

	public HashMap<String, Integer> getAllTableCount(ArrayList<String> names) {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		for(String s : names) {
			int x =getNumberOfRows("select * from " + s + ";");
			map.put(s, x);
		}

		for(String key : map.keySet()) {
			System.out.println(key + ":" + map.get(key));
		}

		return map;
	}

	public ArrayList<String> getAllTableNames() {
		ArrayList<String> list = new ArrayList<String>();
		boolean startOfData = false;
		try {
			DatabaseMetaData md = connection.getMetaData();
			ResultSet rs = md.getTables("public", null, "%", null);
			while(rs.next()) {
				if(rs.getString(3).equals("architectures")) {
					startOfData = true;
				}
				if(rs.getString(3).equals("pg_toast_11113999")) {
					startOfData = false;
				}
				if(startOfData) {
					list.add(rs.getString(3));
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		String fullList = "";
		for(String s : list) {
			log.info(s);
			fullList = fullList + ":" + s;
		}
		return list;

	}

	public int getNumberOfRows(String statement) {
		ResultSet set = sendStatement(statement);
		int counter = 0;
		try {
			while(set.next()) {
				counter++;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		log.info("Number of rows for statement " + statement + ":" + counter);
		return counter;
	}

	/**
	 * Sent a statement and return the ResultSet
	 * @param statement
	 * @return
	 */
	public ResultSet sendStatement(String statement) {
		try {
			stmt = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
			return stmt.executeQuery(statement);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * Tidy up the connections. Close the statement, result set and connection objects
	 */
	public void closeDatabase() {
		try {
			stmt.close();
			results.close();
			results2.close();
			connection.close();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public invisaPC.User findUserByName(String name) {
		String stringToSend = "select * from kvm_users where username = '" + name + "';";
		System.out.println(stringToSend);
		results = sendStatement(stringToSend);
		results2 = sendStatement("select * from kvm_user_properties where property_name = '" + name + "_unique';");
		try {
			while(results.next()) {
				System.out.println(results.getString("username"));
				if(results.getString("username").equals(name)) {
					return createUserFromDb(results, results2);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * Select a connection from the database by name and creates a connection object if 
	 * connection exists. If connection does not exists returns null
	 * @param name
	 * @return
	 */
	public invisaPC.Connection findConnectionByName(String name) {
		String stringToSend = "select * from kvm_connections where name = '" + name + "';";
		System.out.println(stringToSend);
		results = sendStatement(stringToSend);
		results2 = sendStatement("select * from kvm_connection_properties where property_name = '" + name + "_unique';");

		try {
			while(results.next()) {
				System.out.println(results.getString("name"));
				if(results.getString("name").equals(name)) {
					return createConnectionFromDb(results, results2);
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	public invisaPC.Device findDeviceByName(String name) {
		String stringToSend = "select * from kvm_appliances where hostname = '" + name + "';";
		results = sendStatement(stringToSend);
		results2 = sendStatement(stringToSend);

		try {
			while(results.next()) {
				if(results.getString("hostname").equals(name)) {
					return createDeviceFromDb(results);
				}

			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}





	/**
	 * Finds an upgrade file based on the version. Because files
	 * can have the same version number you need to specify the type of 
	 * device. Options are, Emerald TX - eTX, Emerald RX - eRX, InvisaPC RX - RX, 
	 * InvisaPC TX - TX.
	 * @param version
	 * @param type
	 * @return
	 */
	public Kvm_Upgrades findUpgradeByVersion(String version, String type) {
		String lsm_class = "";
		Kvm_Upgrades upgrades = null;
		switch(type) {
		case "eTX" :
			lsm_class = "Transmitter-EMD";
			upgrades = new EmeraldTxUpgrades();
			break;
		case "eRX" :
			lsm_class = "Receiver-EMD";
			upgrades = new EmeraldRxUpgrades();
			break;
		case "RX" :
			lsm_class = "Receiver";
			upgrades = new RxUpgrades();
			break;
		case "TX" :
			lsm_class = "Transmitter";
			upgrades = new TxUpgrades();
			break;
		}
		String stringToSend = "select * from kvm_upgrades where lsm_version = '" + version + "' and lsm_class = '" + lsm_class + "';";
		System.out.println(stringToSend);
		results = sendStatement(stringToSend);
		results2 = sendStatement(stringToSend);
		try {
			while(results.next()) {
				if(results.getString("lsm_version").equals(version) && results.getString("lsm_class").equals(lsm_class)) {
					upgrades = createUpgradesFromDb(results, upgrades);
					return upgrades;
				}
			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;


	}

	public invisaPC.Device findDeviceByIp(String ip) {
		log.info(ip);
		String stringToSend = "select * from kvm_appliances where ip = '" + ip + "';";
		results = sendStatement(stringToSend);
		results2 = sendStatement(stringToSend);

		try {
			while(results.next()) {
				if(results.getString("ip").equals(ip)) {
					return createDeviceFromDb(results);
				}

			}
		}catch(SQLException e) {
			e.printStackTrace();
		}
		return null;
	}
	public Kvm_Upgrades createUpgradesFromDb(ResultSet upgradeSet, Kvm_Upgrades realUpgrade) {
		Kvm_Upgrades upgrades = realUpgrade;
		if(upgradeSet != null) {
			try {
				upgradeSet.beforeFirst();
				while(upgradeSet.next()) {
					upgrades.setActive_image(upgradeSet.getString("active_image"));
					upgrades.setCreated_at(upgradeSet.getString("created_at"));
					upgrades.setFilename(upgradeSet.getString("filename"));
					upgrades.setId(upgradeSet.getString("id"));
					upgrades.setLsm_brand(upgradeSet.getString("lsm_brand"));
					upgrades.setLsm_builddate(upgradeSet.getString("lsm_builddate"));
					upgrades.setLsm_class(upgradeSet.getString("lsm_class"));
					upgrades.setLsm_compatibility(upgradeSet.getString("lsm_compatibility"));
					upgrades.setLsm_type(upgradeSet.getString("lsm_type"));
					upgrades.setLsm_version(upgradeSet.getString("lsm_version"));
					upgrades.setUpdated_at(upgradeSet.getString("updated_at"));
					return upgrades;
				}
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	public invisaPC.Device createDeviceFromDb(ResultSet deviceSet) {
		invisaPC.Device device = null;
		if(deviceSet != null) {
			try {
				String spn = deviceSet.getString("spn");
				if(spn.endsWith("R")) {
					device = new RX();
				}else {
					device = new TX();
				}

				deviceSet.beforeFirst();
				while(deviceSet.next()) {
					device.setArch(deviceSet.getString("arch"));
					device.setAssetNumber(deviceSet.getString("asset_number"));
					device.setBroadcast(deviceSet.getString("broadcast"));
					device.setCreatedAt(deviceSet.getString("created_at"));
					device.setDefault_gateway(deviceSet.getString("gateway"));
					device.setDns2_address(deviceSet.getString("dns2"));
					device.setDns_address(deviceSet.getString("dns1"));
					device.setFqdn(deviceSet.getString("fqdn"));
					device.setIp_address(deviceSet.getString("ip"));
					device.setJwt(deviceSet.getString("jwt"));
					device.setLocation(deviceSet.getString("location"));
					device.setMac(deviceSet.getString("mac"));
					device.setModel_num(deviceSet.getString("model"));
					device.setName(deviceSet.getString("hostname"));
					device.setNetwork_mask(deviceSet.getString("netmask"));
					device.setOs(deviceSet.getString("os"));
					device.setPropertiesState(deviceSet.getString("properties_state"));
					device.setSerial_num(deviceSet.getString("serialno"));
					device.setSpn(deviceSet.getString("spn"));
					device.setSw_ver(deviceSet.getString("swversion"));
					device.setUpdatedAt(deviceSet.getString("updated_at"));
					device.setUpgradeState(deviceSet.getString("upgrade_state"));
					device.setUpgradeStatus(deviceSet.getString("upgrade_status"));
					device.setZoneId(deviceSet.getString("kvm_zone_id"));
					return device;
				}

			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return null;
	}


	public invisaPC.User createUserFromDb(ResultSet userSet, ResultSet userPropertiesSet) {
		if(userSet !=null && userPropertiesSet != null) {
			invisaPC.User user = new invisaPC.User();
			try {
				userSet.beforeFirst();
				while(userSet.next()) {
					user.setUser_name(userSet.getString("username"));
					user.setPassword(userSet.getString("password"));
					user.setCreatedAt(userSet.getString("created_at"));
					user.setUpdatedAt(userSet.getString("updated_at"));
				}

				while(userPropertiesSet.next()) {
					user.setConfiguration(userPropertiesSet.getString("configuration"));
					user.setPrivilege(userPropertiesSet.getString("privilege"));
					user.setAutoConnect(userPropertiesSet.getString("auto_connect"));
					user.setAutoConnectName(userPropertiesSet.getString("auto_connect_name"));
				}
				return user;
			}catch(SQLException e) {
				e.printStackTrace();
			}
		}
		return null;
	}

	/**
	 * Creates a connection object based on info from database tables
	 * @param connectionSet
	 * @param connectionPropertiesSet
	 * @return
	 */
	public invisaPC.Connection createConnectionFromDb(ResultSet connectionSet, ResultSet connectionPropertiesSet)  {
		if(connectionSet != null || connectionPropertiesSet != null) {
			invisaPC.Connection con = new invisaPC.Connection();
			try {
				connectionSet.beforeFirst();
				while(connectionSet.next()) {
					con.setName(connectionSet.getString("name"));
					con.setIp_address(connectionSet.getString("host"));
					con.setUser_name(connectionSet.getString("username"));
					con.setPassword(connectionSet.getString("password"));
				}
				while(connectionPropertiesSet.next()) {
					con.setPort(connectionPropertiesSet.getString("port"));
					con.setDomain(connectionPropertiesSet.getString("domain"));
					con.setAudio(connectionPropertiesSet.getString("audio"));
					con.setConnectionType(connectionPropertiesSet.getString("connection_type"));
					con.setExtDesk(connectionPropertiesSet.getString("ext_desktop"));
					con.setUsb_redirection(connectionPropertiesSet.getString("usb_redirection"));
					con.setColour_depth(connectionPropertiesSet.getString("color_depth"));
					con.setPersistent(connectionPropertiesSet.getString("persistent"));
					con.setPreEmption_mode(connectionPropertiesSet.getString("preempt"));
					String conType = connectionPropertiesSet.getString("group");
					switch(conType) {
					case "ConnectViaTx" :
						con.setViaTx("true");
						break;
					case "Broker" :
						con.setBroker("Yes");
						break;
					case "VMHorizon" :
						con.setHorizon("true");
						break;
					}
				}
				return con;
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				return con;
			}
		}
		return null;
	}
	public boolean compareDatabases(String masterIp, String slaveIp) {
		Database db = new Database();
		db.connectToDatabase(masterIp, "postgres", "foreman", "foreman");
		HashMap map = db.getAllTableCount(db.getAllTableNames());

		Database db2 = new Database();
		db2.connectToDatabase(slaveIp, "postgres", "foreman", "foreman");
		HashMap map2 = db2.getAllTableCount(db2.getAllTableNames());

		return map.equals(map2);
	}

	public static void main(String[]args) {
		Database db = new Database();
		db.connectToDatabase("10.211.128.45", "postgres", "foreman", "foreman");
		HashMap map = db.getAllTableCount(db.getAllTableNames());

		Database db2 = new Database();
		db2.connectToDatabase("10.211.128.46", "postgres", "foreman", "foreman");
		HashMap map2 = db2.getAllTableCount(db2.getAllTableNames());

		System.out.println(map.equals(map2));
		//		Database db2 = new Database();
		//		db2.connectToDatabase("10.211.128.46","postgres", "foreman", "foreman");
		//		invisaPC.User user2 = db2.findUserByName("cluster");
		//		db2.closeDatabase();
		//		
		//		if(user1.equals(user2)) {
		//			System.out.println("Equal");
		//		}else {
		//			System.out.println("Not Equal");
		//		}

		//		Kvm_Upgrades upgrade = db.findUpgradeByVersion("V1.0.1_r2625", "eRX");
		//		db.closeDatabase();
		//		System.out.println(upgrade.toString());
		//		System.out.println();
		//		System.out.println();
		//		System.out.println();

		//		Database db2 = new Database();
		//		db2.connectToDatabase("10.211.129.3", "postgres", "foreman", "foreman");
		//		invisaPC.Device device2 = db2.find("10.211.128.156");
		//		db2.closeDatabase();
		//		
		//		System.out.println(device2.toString());
		//		
		//		if(device2.equals(device)) {
		//			System.out.println("Match");
		//		}

		//		Database db2 = new Database();
		//		db2.connectToDatabase("10.211.128.147", "postgres", "foreman", "foreman");
		//		invisaPC.Connection con = db2.findConnectionByName("brendan");
		//		db2.closeDatabase();
		//		if(connection.equals(con)) {
		//			System.out.println("Match");
		//		}else {
		//			System.out.println("No Match");
		//		}
		//System.out.println(connection.toString());

	}
}
