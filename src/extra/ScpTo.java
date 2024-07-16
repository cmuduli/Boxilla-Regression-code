package extra;
import com.jcraft.jsch.*;

import invisaPC.Rest.ActiveConnections;

import java.awt.*;
import javax.swing.*;

import org.apache.log4j.Logger;

import java.io.*;

/**
 * This class will scp from a local machine to a remote machine. It does not use 
 * passkeys, it uses passcodes. 
 * 
 * Code samplet taken from  <a href=https://gist.githubusercontent.com/ymnk/2318108/raw/82819389a225265c2aa4ca11afc0b35e938607fe/ScpTo.java>here</a>
 * @author Brendan O'Regan
 *
 */

public class ScpTo{

	final static Logger log = Logger.getLogger(ScpTo.class);

	/**
	 * Scp a file  from current machine to remote machine
	 * 
	 * @param localFileName locay file name to copy
	 * @param desUser destination machine user login
	 * @param desIp destination machine IP address
	 * @param desPassword destination machine login password
	 * @param desLocation location on destination machine to copy the file to
	 * @param desFileName name to copy the file to on the destination machine
	 */
	public  void scpTo(String localFileName, String desUser, String desIp, String desPassword, String desLocation, String desFileName) {
		log.info("Attempting to scp local file: " + localFileName );
		log.info("To IP" + desIp);
		log.info("To location:" + desLocation);
		log.info("Remote file name:" + desFileName);

		FileInputStream fis=null;
		try{
			String lfile=localFileName;
			String user=desUser;
			String host=desIp;
			String rfile=desLocation + desFileName;

			JSch jsch=new JSch();
			Session session=jsch.getSession(user, host, 22);
			session.setPassword(desPassword);
			java.util.Properties config = new java.util.Properties(); 
			config.put("StrictHostKeyChecking", "no");
			session.setConfig(config);
			session.connect();

			boolean ptimestamp = false;

			// exec 'scp -t rfile' remotely
			String command="scp " + (ptimestamp ? "-p" :"") +" -t "+rfile;
			Channel channel=session.openChannel("exec");
			((ChannelExec)channel).setCommand(command);

			// get I/O streams for remote scp
			OutputStream out=channel.getOutputStream();
			InputStream in=channel.getInputStream();

			channel.connect();

			if(checkAck(in)!=0){
				System.exit(0);
			}

			File _lfile = new File(lfile);

			if(ptimestamp){
				command="T "+(_lfile.lastModified()/1000)+" 0";
				// The access time should be sent here,
				// but it is not accessible with JavaAPI ;-<
				command+=(" "+(_lfile.lastModified()/1000)+" 0\n"); 
				out.write(command.getBytes()); out.flush();
				if(checkAck(in)!=0){
					System.exit(0);
				}
			}

			// send "C0644 filesize filename", where filename should not include '/'
			long filesize=_lfile.length();
			command="C0644 "+filesize+" ";
			if(lfile.lastIndexOf('/')>0){
				command+=lfile.substring(lfile.lastIndexOf('/')+1);
			}
			else{
				command+=lfile;
			}
			command+="\n";
			out.write(command.getBytes()); out.flush();
			if(checkAck(in)!=0){
				System.exit(0);
			}

			// send a content of lfile
			fis=new FileInputStream(lfile);
			byte[] buf=new byte[1024];
			while(true){
				int len=fis.read(buf, 0, buf.length);
				if(len<=0) break;
				out.write(buf, 0, len); //out.flush();
			}
			fis.close();
			fis=null;
			// send '\0'
			buf[0]=0; out.write(buf, 0, 1); out.flush();
			if(checkAck(in)!=0){
				System.exit(0);
			}
			out.close();

			channel.disconnect();
			session.disconnect();

		}
		catch(Exception e){
			log.info("Error SCP file across");
			try{if(fis!=null)fis.close();}catch(Exception ee){}
		}
		log.info("File successfully send");
	}

	/**
	 * Used to check if the scp was successful
	 * @param in
	 * @return
	 * @throws IOException
	 */
	static int checkAck(InputStream in) throws IOException{
		int b=in.read();
		// b may be 0 for success,
		//          1 for error,
		//          2 for fatal error,
		//          -1
		if(b==0) return b;
		if(b==-1) return b;

		if(b==1 || b==2){
			StringBuffer sb=new StringBuffer();
			int c;
			do {
				c=in.read();
				sb.append((char)c);
			}
			while(c!='\n');
			if(b==1){ // error
				System.out.print(sb.toString());
			}
			if(b==2){ // fatal error
				System.out.print(sb.toString());
			}
		}
		return b;
	}


}