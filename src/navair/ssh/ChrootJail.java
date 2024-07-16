package navair.ssh;

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;


import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;

public class ChrootJail extends StartupTestCase {
	private Ssh ssh;
	private String username = "commander";
	private String password = "Rv6#ACEmh";
	
	final static Logger log = Logger.getLogger(ChrootJail.class);

	@Test
	public void test01_checkPwdCommand() {
		ssh = new Ssh(username, password, boxillaManager);
		ssh.loginToServer();
		String homeDir = "/home/commander";
		String outputHomeDir = ssh.sendCommand("pwd");
		log.info("Home directory:" + outputHomeDir  + ".");
		ssh.disconnect();
		Assert.assertTrue(outputHomeDir.contains(homeDir), "Home directory was not " + homeDir + ", actual, " + outputHomeDir);
	}
	
	@Test
	public void test02_checkLsCommand() {
		ssh = new Ssh(username, password, boxillaManager);
		ssh.loginToServer();
		String lsExpectedOutput = "commander";
		String output = ssh.sendCommand("ls ../");
		ssh.disconnect();
		Assert.assertTrue(output.contains(lsExpectedOutput), "ls command did not return " + lsExpectedOutput + ", actual," + output);
	}
	
	@Test
	public void test03_checkNoFileCreationTouch() {
		ssh = new Ssh(username, password, boxillaManager);
		ssh.loginToServer();
		ssh.sendCommand("touch newFile");
		String output = ssh.sendCommand("ls");
		ssh.disconnect();
		Assert.assertTrue(!output.contains("newFile"), "output from vi command returned fileName newFile, actual, " + output);
	}
		

	@Test
	public void test04_checkNoCdToRoot() {
		ssh = new Ssh(username, password, boxillaManager);
		ssh.loginToServer();
		String cdExpected = "/home/commander";
		ssh.sendCommand("cd /usr/local/");
		String output = ssh.sendCommand("pwd");
		ssh.disconnect();
		Assert.assertTrue(output.contains(cdExpected), "output from cd /root/ command did not return " + cdExpected + ", actual, " + output);
	}
	
	@Test
	public void test05_checkNoPermissionForFile() {
		ssh = new Ssh(username, password, boxillaManager);
		ssh.loginToServer();
		ssh.sendCommand("echo test > newFile");
		String output = ssh.sendCommand("ls");
		ssh.disconnect();
		Assert.assertTrue(!output.contains("newFile"), "output contained newFile, actual:" + output);
	}
	
	
	
}
