package methods;

import java.io.File;
import java.util.ArrayList;
import org.testng.annotations.Test;
import extra.Files;
import extra.Ssh;


/**
 * This class contains methods to monitor the boxilla hardware. It uses 
 * the smartctl command for Harddrive and CPU temperture reporting
 * and the top command for CPU and memory usage
 * @author Brendan O'Regan
 *
 */

public class HardwareMonitor {

	private Ssh shell;
	private Files fileOptions;
	private String user;
	private String password;
	private String ip;


	public HardwareMonitor(String user, String password, String ip) {
		this.user = user;
		this.password = password;
		this.ip = ip;
	}

	public void writeSmartCtlToFile() {
		File file = new File("/SeleniumAutomation/resources/smartCtl.txt");
		fileOptions.writeFile(getItemsFromSmartCtl(), file);
	}

	/**
	 * will return an arraylist of all the items checked with smartctl
	 * @return
	 */
	public ArrayList<String> getItemsFromSmartCtl(){
		String [] parsedOutput = serverReturn("smartctl -a /dev/sda1");
		ArrayList<String> checkedItems = new ArrayList<String>();
		ArrayList<String> itemsToCheck = getItemsToCheck();
		for(String s : parsedOutput ) {
			for(String line : itemsToCheck) {
				if(s.contains(line)) {
					checkedItems.add(s);
				}
			}

		}

		return checkedItems;
	}



	/**
	 * Will parse the output of an entire line and return the raw value
	 * @return
	 */
	private String getRawValue(String line) {
		line = line.replace(" ","");
		String[] parsed = line.split("-");
		return parsed[1];
	}

	/**
	 * Will log into the given server and run the smartCtl command 
	 * @param user
	 * @param password
	 * @param ip
	 * @return
	 */
	private String[] serverReturn(String command) {
		System.out.println("Running smartCtl");
		shell = new Ssh(user, password, ip);
		shell.loginToServer();
		String serverReturn = shell.sendCommand(command);
		shell.disconnect();
		String [] parsedOutput = serverReturn.split("\\n");
		return parsedOutput;
	}

	private ArrayList<String> getItemsToCheck() {
		fileOptions = new Files();
		ArrayList<String> itemsToCheck = fileOptions.readFile("C:\\Test_Workstation\\SeleniumAutomation\\resources\\smartCtl.txt");
		return itemsToCheck;
	}

	public ArrayList<String> compareValues(ArrayList<String> initialSmartCtl, ArrayList<String> newSmartCtlValues) {
		ArrayList<String> output = new ArrayList<String>();
		for(int j=0; j < initialSmartCtl.size(); j++) {
			if(!initialSmartCtl.get(j).equals(newSmartCtlValues.get(j))) {
				output.add(System.lineSeparator());
				output.add(System.lineSeparator());
				output.add("***********************************************************************************");
				output.add("Smart Control Value has changed");
				output.add("===================================================================================");
				output.add("Initial Value :" + initialSmartCtl.get(j));
				output.add("New Value     :" + newSmartCtlValues.get(j));
				output.add(System.lineSeparator());
				output.add("************************************************************************************");
			}
		}
		return output;
	}

	public ArrayList<String> getTopOutput(String sortedBy, String className) {
		ArrayList<String> returnOutput = new ArrayList<String>();
		String[] parsed = serverReturn("top -b -n1 -o %" + sortedBy);
		boolean isOutput = false;
		String memoryDetails = "";
		int counter = 0;
		String[] topThree = new String[5];
		for(String s : parsed) {
			System.out.println("line: " + s);
			if(s.contains("KiB Mem")) {
				memoryDetails = s;
			}
			if(isOutput == true && counter < 5) {
				topThree[counter] = s;
				counter++;
			}
			if(s.contains("PID"))
				isOutput = true;
		}
		returnOutput.add("Hardware report after class " + className);
		returnOutput.add("====================================================================================");
		returnOutput.add(System.lineSeparator());
		returnOutput.add("Memory details" );
		returnOutput.add(memoryDetails);		
		returnOutput.add(System.lineSeparator());
		returnOutput.add("Top 5 processes sorted by " + sortedBy);
		returnOutput.add("====================================================================================");
		for(String s1 : topThree) {
			returnOutput.add(s1);
		}
		returnOutput.add("************************************************************************************");
		returnOutput.add(System.lineSeparator());
		return returnOutput;
	}

	public static void main(String args[]) {
		HardwareMonitor mon = new HardwareMonitor("root", "barrow1admin_12", "10.10.10.182");
		mon.getTopOutput("MEM", "TEST");
	}


	public ArrayList<String> hardwareReportStartText(String date, String time) {
		ArrayList<String> output = new ArrayList<String>();
		output.add("Hardware report for regression run on " + date + " " + time);
		output.add(System.lineSeparator());
		return output;
	}

}
