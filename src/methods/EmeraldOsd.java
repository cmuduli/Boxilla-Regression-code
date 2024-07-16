package methods;

import java.awt.image.BufferedImage;
import java.awt.image.DataBuffer;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Properties;

import javax.imageio.ImageIO;

import org.apache.log4j.Logger;
import org.testng.Assert;

import extra.ScpTo;
import extra.Ssh;
import invisaPC.Connection;
import invisaPC.XMLParserSAX;
import testNG.Utilities;
import unmanaged.CreateConnection;

/**
 * Class to automate the emerald OSD
 * @author Boxilla
 *
 */
/**
 * This was a class create to explore the automation of the appliance OSD. Not needed for normal regression
 * @author Boxilla
 *
 */
public class EmeraldOsd {

	final static Logger log = Logger.getLogger(EmeraldOsd.class);

	private String scriptLocation = "/usr/automationScripts/";
	private String ipAddress;
	private String password;
	private String username;
	private Ssh shell;
	private Properties keyProperties = new Properties();

	public EmeraldOsd(String ipAddress, String username, String password) {
		loadProperties();
		this.ipAddress = ipAddress;
		this.username = username;
		this.password = password;
		shell = new Ssh(username, password, ipAddress);
		copyScripts();

	}

	private void loadProperties() {
		try {
			InputStream in = new FileInputStream("char.properties");
			keyProperties.load(in);
			in.close();
		} catch (IOException e) {
			log.info("Properties file failed to load");
		}
	}

	private void copyScripts() {
		shell.loginToServer();
		shell.sendCommand("rm -r " + scriptLocation);
		shell.sendCommand("mkdir " + scriptLocation);
		ArrayList<String> files = getScripts();
		for(String s : files) {
			ScpTo copy = new ScpTo();
			copy.scpTo("emeraldScripts/" + s, username, ipAddress, password, scriptLocation, s);
		}
		shell.sendCommand("dos2unix " + scriptLocation + "*");
		shell.sendCommand("chmod 777 " + scriptLocation  + "*");
		shell.disconnect();
	}
	private ArrayList<String> getScripts() {
		ArrayList<String> files = new ArrayList<String>();
		File folder = new File("emeraldScripts");
		File[] listOfFiles = folder.listFiles();
		for(File f : listOfFiles) {
			files.add(f.getName());
		}
		return files;
	}
	private void navigateToItemInList(int numberInList) {
		shell.loginToServer();
		String output = shell.sendCommand(scriptLocation + "login.sh" + convertToInput("admin"));
		for(int j=0; j < 5; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		for(int j=0; j < numberInList; j++ ) {
			output = shell.sendCommand(scriptLocation + "down.sh");
		}
	}

	public void removeConnection(int numberInList) {
		String output = "";
		navigateToItemInList(numberInList);
		for(int j=0; j < 5; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();
	}

	public void editConnection(int numberInList,String connectionType, String fieldToEdit, String value) {
		String output = "";
		navigateToItemInList(numberInList);
		for(int j=0; j<4; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		switch(connectionType) {
		case "tx" :
			editTxConnectionDetails(fieldToEdit, value);
			break;
		case "horizon":
			editHorizonConnectionDetails(fieldToEdit, value);
			break;
		case "direct" :
			editDirectConnectionDetails(fieldToEdit, value);
			break;

		}

		shell.disconnect();
	}

	public void connectToConnection(int numberInList) {
		String output;
		navigateToItemInList(numberInList);
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();

	}

	public void editDirectConnectionDetails(String fieldToEdit, String value) {
		String output;
		switch (fieldToEdit) {
		case ("name") :
			output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<7; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case ("ip") :
			output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<6; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case ("port") :
			output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<5; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case("username") :
			for(int j=0; j < 4; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<4; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case("password") :
			for(int j=0; j < 5; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<3; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case("domain") :
			for(int j=0; j < 6; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
		for(int j=0; j<2; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;
		case("audio") :
			for(int j=0; j < 7; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		break;

		}

		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();

	}

	public static void main(String[] args) throws InterruptedException {
		EmeraldOsd emerald = new EmeraldOsd("10.211.129.86", "root", "barrow1admin_12");
		//	emerald.copyScripts();
		//emerald.createConnection("connection344", "10.211.129.66","tx", "test", "test","4888", "blackbox.com", true, true, true);
		//emerald.editConnection(1, "direct", "ip", "10.211.129.5");
		//Connection c = emerald.getConnectionFromXml("connection344");
		//System.out.println(c.toString());
		//	Assert.assertTrue(c.getName().equals("connection343"), "Connection name did not match");
		//emerald.connectToConnection(1);
		//System.out.println(emerald.checkForConnection("10.211.129.87"));
		emerald.changeOsdResolution("1280x720");
		System.out.println(emerald.checkEmeraldOsdResolution("10.211.129.87"));
	}

	public void gracefullyKillConnection(String rxIpAddress) {
		Ssh kill = new Ssh("root", "barrow1admin_12", rxIpAddress);
		kill.loginToServer();
		kill.sendCommand("killall -SIGUSR1 connection_control");
		kill.disconnect();
	}

	public String checkEmeraldOsdResolution(String ip) {
		String output;
		ScpTo copy = new ScpTo();
		copy.scpTo("emeraldScripts/getres.sh", "root", ip, "barrow1admin_12", "/usr/", "getres.sh");
		Ssh resShell = new Ssh("root", "barrow1admin_12", ip);
		resShell.loginToServer();
		resShell.sendCommand("dos2unix /usr/getres.sh");
		resShell.sendCommand("chmod 777 /usr/getres.sh");
		output = resShell.sendCommand("/usr/getres.sh");
		output = resShell.sendCommand("cat /usr/text.txt");
		resShell.disconnect();

		//parse output to get resolution only
		String resolution[] = output.split("\\s+");
		output = resolution[1];
		return output;
	}
	public void takeScreenShot(String name, String ip) {
		Ssh screenShotShell = new Ssh("root", "barrow1admin_12", ip);
		screenShotShell.loginToServer();
		screenShotShell.sendCommand("/dev/fbgrab " + name + ".png");
		screenShotShell.disconnect();
	}

	public float compareImage(File fileA, File fileB) {

		float percentage = 0;
		try {
			// take buffer data from both image files //
			BufferedImage biA = ImageIO.read(fileA);
			DataBuffer dbA = biA.getData().getDataBuffer();
			int sizeA = dbA.getSize();
			BufferedImage biB = ImageIO.read(fileB);
			DataBuffer dbB = biB.getData().getDataBuffer();
			int sizeB = dbB.getSize();
			int count = 0;
			// compare data-buffer objects //
			if (sizeA == sizeB) {

				for (int i = 0; i < sizeA; i++) {

					if (dbA.getElem(i) == dbB.getElem(i)) {
						count = count + 1;
					}

				}
				percentage = (count * 100) / sizeA;
			} else {
				System.out.println("Both the images are not of same size");
			}

		} catch (Exception e) {
			System.out.println("Failed to compare image files ...");
		}
		return percentage;
	}

	public boolean checkForConnection(String ip) {
		Ssh connectShell = new Ssh("root", "barrow1admin_12", ip);
		connectShell.loginToServer();
		String out = connectShell.sendCommand("ps -ax | grep free");
		String stringToCheck = "/usr/bin/bbfreerdp";
		connectShell.disconnect();
		if(out.contains(stringToCheck)) {
			return true;
		}
		return false;
	}

	public Connection getConnectionFromXml(String conName) {
		Ssh connectionShell = new Ssh("root", "barrow1admin_12", "10.211.129.87");
		connectionShell.loginToServer();
		String output = connectionShell.sendCommand("cat /usr/local/gui_files/CloudDataA.xml");

		//write the output of the xml to a local file
		String fileName = "C:\\temp\\test01.xml";
		Utilities.writeStringToFile(output, fileName);

		XMLParserSAX sax = new XMLParserSAX();
		ArrayList<Connection> connections = sax.go(fileName, "Connection");
		connectionShell.disconnect();
		Connection returnConnection;
		for(Connection c : connections) {
			if(c.getName().equals(conName)) {
				return c;
			}
		}
		return null;
	}

	public void changeOsdResolution(String resolution) throws InterruptedException {
		String output;
		tabTo("control");

		for(int j=0; j<5; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}

		output = shell.sendCommand(scriptLocation + "enter.sh");

		for(int j=0; j<3; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}

		for(int j=0; j<5;j++) {
			output = shell.sendCommand(scriptLocation + "up.sh");
		}
		//case statement start
		switch(resolution) {
		case "1280x720" :
			output = shell.sendCommand(scriptLocation + "down.sh");
			break;
		case "1280x1024" :
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			break;
		case "1920x1080" :
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			break;
		case "auto" :
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
			break;
		}
		//case statement end
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		Thread.sleep(3000);
		for(int j=0; j<5; j++) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
		}
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();

	}

	public void editHorizonConnectionDetails(String fieldToEdit, String value) {
		String output;
		switch (fieldToEdit) {
		case "name" :
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			for(int j=0; j<4; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;	
		case "ip" :
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			for(int j=0; j<3; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		case "username" :
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			for(int j=0; j<2; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		case "password" :
			for(int j=0; j<4; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;

		}
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();
	}

	public void editTxConnectionDetails(String fieldToEdit, String value) {
		String output;
		switch(fieldToEdit) {
		case "name" :
			output = shell.sendCommand(scriptLocation + "tab.sh");		
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			for(int j=0; j<4; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		case "ip" :
			output = shell.sendCommand(scriptLocation + "tab.sh");	
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(value));
			for(int j=0; j<3; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		case "USBR":
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "enter.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		case "persistent" :
			for(int j=0; j<4; j++) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
			}
			output = shell.sendCommand(scriptLocation + "enter.sh");
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "enter.sh");
			break;
		}
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();


	}
	public void tabTo(String tab) {
		Ssh rxSsh = new Ssh("root", "barrow1admin_12", "10.211.129.87");
		rxSsh.loginToServer();
		rxSsh.sendCommand("/sbin/reboot");
		rxSsh.disconnect();
		try {
			Thread.sleep(50000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		shell.loginToServer();
		String output = shell.sendCommand(scriptLocation + "login.sh" + convertToInput("admin"));
		output = shell.sendCommand(scriptLocation + "tab.sh");
		switch (tab) {
		case "control" :
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			break;
		case "users" :
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			break;
		case "information" :
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			output = shell.sendCommand(scriptLocation + "cycleTabs.sh");
			break;


		}
	}

	public void createConnection(String connectionName, String ipAddress, String connectionType,
			String vmUsername, String vmPassword, String port,String domain, boolean isEnableUsb, boolean isPersistent,
			boolean isAudioEnable) {
		shell.loginToServer();
		String output = shell.sendCommand(scriptLocation + "login.sh" + convertToInput("admin"));
		output = shell.sendCommand(scriptLocation + "click_connection.sh");
		connectionDetails(connectionName,ipAddress, connectionType, vmUsername, vmPassword,port, domain, isEnableUsb, isPersistent, isAudioEnable);

	}

	private void connectionDetails(String connectionName, String connectionIp, String connectionType, String connectionUsername, String connectionPassword,
			String port, String domain, boolean isEnableUsb, boolean isPersistent, boolean isEnableAudio) {
		String output;
		if(connectionType.equals("horizon")) {
			output = shell.sendCommand(scriptLocation + "down.sh");
		}
		if(connectionType.equals("direct")) {
			output = shell.sendCommand(scriptLocation + "down.sh");
			output = shell.sendCommand(scriptLocation + "down.sh");
		}
		output = shell.sendCommand(scriptLocation + "tab.sh");		
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(connectionName));
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(connectionIp));


		if(connectionType.equals("direct")) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(port));
		}

		if(connectionType.equals("horizon") || connectionType.equals("direct")) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(connectionUsername));
			output = shell.sendCommand(scriptLocation + "tab.sh");
			output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(connectionPassword));
			if(connectionType.equals("direct")) {
				output = shell.sendCommand(scriptLocation + "tab.sh");
				output = shell.sendCommand(scriptLocation + "tabText.sh" + convertToInput(domain));
			}
		}

		if(connectionType.equals("direct")) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
			if(isEnableAudio) {
				output = shell.sendCommand(scriptLocation + "enter.sh");
			}
		}

		if(connectionType.equals("tx")) {
			output = shell.sendCommand(scriptLocation + "tab.sh");
			if(isEnableUsb) {
				output = shell.sendCommand(scriptLocation + "enter.sh");
			}
			output = shell.sendCommand(scriptLocation + "tab.sh");
			if(isPersistent) {
				output = shell.sendCommand(scriptLocation + "enter.sh");
			}
		}
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		output = shell.sendCommand(scriptLocation + "tab.sh");
		output = shell.sendCommand(scriptLocation + "enter.sh");
		shell.disconnect();
	}
	public String convertToInput(String text) {
		String values = " ";
		char[] testChar = text.toCharArray();
		for(char a : testChar) {
			values  = values + keyProperties.getProperty(Character.toString(a)) + " ";
		}
		return values;
	}
}
