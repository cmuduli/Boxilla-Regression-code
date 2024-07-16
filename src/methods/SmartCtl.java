package methods;

import java.util.ArrayList;

import org.testng.annotations.Test;

import extra.Files;
import extra.Ssh;

public class SmartCtl {
	
	private Ssh shell;
	private Files fileOptions;
	private String user;
	private String password;
	private String ip;
	
	
	public SmartCtl(String user, String password, String ip) {
		this.user = user;
		this.password = password;
		this.ip = ip;
	}
	
	public void writeSmartCtlToFile() {
		//fileOptions.writeFile(getItemsFromSmartCtl());
	}
	
	/**
	 * will return an arraylist of all the items checked with smartctl
	 * @return
	 */
	public ArrayList<String> getItemsFromSmartCtl(){
		String [] parsedOutput = serverReturn();
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
	private String[] serverReturn() {
		System.out.println("Running smartCtl");
		shell = new Ssh(user, password, ip);
		shell.loginToServer();
		String serverReturn = shell.sendCommand("smartctl -a /dev/sda1");
		shell.disconnect();
		String [] parsedOutput = serverReturn.split("\\n");
		return parsedOutput;
	}
	
	private ArrayList<String> getItemsToCheck() {
		fileOptions = new Files();
		ArrayList<String> itemsToCheck = fileOptions.readFile("C:\\Test_Workstation\\SeleniumAutomation\\resources\\smartCtl.txt");
		return itemsToCheck;
	}

	public void compareValues(ArrayList<String> initialSmartCtl, ArrayList<String> newSmartCtlValues) {
		for(int j=0; j < initialSmartCtl.size(); j++) {
			if(!initialSmartCtl.get(j).equals(newSmartCtlValues.get(j))) {
				System.out.println();
				System.out.println("***********************************************************************************");
				System.out.println("Smart Control Value has changed");
				System.out.println("===================================================================================");
				System.out.println("Initial Value :" + initialSmartCtl.get(j));
				System.out.println("New Value     :" + newSmartCtlValues.get(j));
				System.out.println();
				System.out.println("************************************************************************************");
			}
		}
	}

}
