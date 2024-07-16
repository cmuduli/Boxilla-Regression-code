package extra;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
/**
 * This class is used for all SSH related functions. Using this class you can SSH into 
 * a machine and run commands which return the output
 * @author Brendan O'Regan
 *
 */
public class Ssh {
	
	private JSch shell = new JSch();
	private String userName;
	private String password;
	private String ipAddress;
	private Session session;
	final static Logger log = Logger.getLogger(Ssh.class);

	/**
	 * Creates an SSH object which can be used to login to a machine and run commands
	 * @param userName login username of machine to connect to
	 * @param password login password of machine to connect to
	 * @param ipAddress IP address of machine to connect to
	 */
	public Ssh(String userName, String password, String ipAddress) {
		this.userName = userName;
		this.password = password;
		this.ipAddress = ipAddress;
	}
	
	/**
	 * Logs into the server with the details provided in
	 * the constructor
	 */
	public boolean  loginToServer() {
		try {
			log.info("Trying to connect to " + ipAddress);
			log.info("Using login details  " + userName + "/" + password);
			
			
			session = shell.getSession(userName, ipAddress, 22);
			session.setPassword(password);
			
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect(30000);
			log.info("Successfully connected to " + ipAddress);
			return true;
		} catch (JSchException e) {
			log.info("Error connecting to " + ipAddress);
			e.printStackTrace();
			return false;
		} 
	}
	
	/**
	 * Send a command to the connect machine and return no output
	 * @param command command to run on connected machine
	 */
	public void sendCommandNoReturn(String command) {
		log.info("Sending command " + command);
		 StringBuilder out = new StringBuilder();

	     try
	     {
	        Channel channel = session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        InputStream commandOutput = channel.getInputStream();
	        channel.connect();
	        commandOutput.read();
	        channel.disconnect();
	     }	     catch(IOException ioX)
	     {
	    	 log.info("Error sending command");
	     }
	     catch(JSchException jschX)
	     {
	    	 log.info("Error sending command");
	     }
	     session.disconnect();
	}
	
	
	
	
	/**
	 * Sends the passed in command and executes it.
	 * Returns the output of the command into a string
	 * @param command command to send to the connected machine
	 * @return
	 */
	public String sendCommand(String command) {
		log.info("Sending command " + command);
		 StringBuilder out = new StringBuilder();

	     try
	     {
	        Channel channel = session.openChannel("exec");
	        ((ChannelExec)channel).setCommand(command);
	        InputStream commandOutput = channel.getInputStream();
	        channel.connect();
	        int readByte = commandOutput.read();

	        while(readByte != 0xffffffff)
	        {
	           out.append((char)readByte);
	           readByte = commandOutput.read();
	        }

	        channel.disconnect();
	     }
	     catch(IOException ioX)
	     {
	    	 log.info("Error sending command");
	     }
	     catch(JSchException jschX)
	     {
	    	 log.info("Error sending command");
	     }

	     return out.toString();
	  }
	public String sendSH(String command) throws IOException, InterruptedException 
	{try {
        // Specify the path to your .sh script
        String scriptPath = command;

        // Build the command
        ProcessBuilder processBuilder = new ProcessBuilder("bash", scriptPath);

        // Redirect error stream to output stream
        processBuilder.redirectErrorStream(true);

        // Start the process
        Process process = processBuilder.start();

        // Read the output
        InputStream inputStream = process.getInputStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));

        String line;
        while ((line = reader.readLine()) != null) {
            System.out.println(line);
        }

        // Wait for the process to finish
        int exitCode = process.waitFor();
        System.out.println("Script execution finished with exit code: " + exitCode);

        // Close the streams
        reader.close();
        inputStream.close();
    } catch (IOException | InterruptedException e) {
        e.printStackTrace();
    }
	return command;
    }

	
	
	
//	public String sendhttpCommand(String command) {
//		log.info("Sending command " + command);
//		 StringBuilder out = new StringBuilder();
//
//	     try
//	     {
//	        Channel channel = session.openChannel("exec");
//	        ((ChannelExec)channel).setCommand(command);
//	        InputStream commandOutput = channel.getInputStream();
//	        channel.connect();
//	        String readByte = Integer.toString(commandOutput.read());
//	        int chiran=Integer.parseInt(readByte,16);
//
//	        //if(readByte == 0xffffffff)
//	        
//	           out.append((char)chiran);
////	           readByte = commandOutput.read();
//	           
//	       
//
//	        channel.disconnect();
//	     }
//	     catch(IOException ioX)
//	     {
//	    	 log.info("Error sending command");
//	     }
//	     catch(JSchException jschX)
//	     {
//	    	 log.info("Error sending command");
//	     }
//
//	     return out.toString();
//	  }
//	
	/**
	 * Disconnects from the connected machine.
	 * This must be run when finsihed with the machine
	 */
	public void disconnect() {
		log.info("Disconnecting from " + ipAddress);
		session.disconnect();
	}
	
//	public static void main(String [] args) {
//		Connection c = null;
//		Statement stmt = null;
//		try { 
//			Class.forName("org.postgresql.Driver");
//			c = DriverManager.getConnection("jdbc:postgresql://10.211.129.2:5432/foreman", "postgres", "foreman");
//			
//			System.out.println("Connection established");
//			stmt = c.createStatement();
//			ResultSet rs = stmt.executeQuery("select * from features;");
//			while(rs.next()) {
//				System.out.println(rs.getString("name"));
//			}
//			rs.close();
//			stmt.close();
//			c.close();
//			
//			
//		}catch(Exception e ) {
//			e.printStackTrace();
//		}
//		
//	}
	public static void main(String args[]) {
		Ssh.getFreeIp();
	}
	
	public static String getFreeIp() {	
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader("ipaddresses.txt"));
			String line = reader.readLine();
			while(line != null) {
				log.info("IP Address:" + line);
				
				//check if address is alive
				try {
					InetAddress pingIp = InetAddress.getByName(line);
					if(pingIp.isReachable(2000)) {
						log.info("IP address " + line + " is alive");
					}else {
						log.info("Ip address " + line + " is not reachable. returning");
						return line;
					}
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				line = reader.readLine();
				
			}
		}catch(IOException e) {
			e.printStackTrace();
		}
		
		
		throw new AssertionError("No IPs available");
		
	}
}
