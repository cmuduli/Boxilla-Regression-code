package extra;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;

import org.apache.log4j.Logger;

import methods.DkmMethods;

/**
 * Utility class for dealing with files
 * @author Brendan O Regan
 *
 */

public class Files {
	
	private String fileName;
	private File file;
	private FileReader fileReader;
	private BufferedReader bufferedReader;
	private StringBuffer stringBuffer;
	final static Logger log = Logger.getLogger(Files.class);
	
	/**
	 * Read a file and return its contents line by line in arraylist
	 * @param fileName location and name of the file to read
	 * @return
	 */
	public ArrayList<String> readFile(String fileName) {
		log.info("Reading file " + fileName);
		this.fileName = fileName;
		
		file = new File(fileName);
		FileReader fileReader = null;
		try {
			fileReader = new FileReader(file);
		} catch (FileNotFoundException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		 bufferedReader = new BufferedReader(fileReader);
		 stringBuffer = new StringBuffer();
		 String line; 
		 ArrayList<String> lines = new ArrayList<String>();
			try {
				while ((line = bufferedReader.readLine()) != null) {
					lines.add(line);
				}
			} catch (IOException e) {
				log.info("Error Reading file " + fileName);
				e.printStackTrace();
			}
		 return lines;
		
	}
	
	/**
	 * Make a directory for hardware reports
	 */
	public void mkdir() {
		String directoryLocation = System.getProperty("user.home") + "//hardwareReports";
		File dir = new File(System.getProperty(directoryLocation));
		if(!dir.exists()) {
			log.info("Creating " + directoryLocation);
			dir.mkdir();
		}
	}
	
	/**
	 * Create a file
	 * @param name location and name of the file to create
	 * @return boolean. True if file created successfully, else false
	 */
	public boolean createFile(String name) {
	//	mkdir();
		file = new File(name);
		try {
			return file.createNewFile();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			log.info("Error creating file");
		}
		return false;
	}
	
	/**
	 * Takes an arraylist of lines and writes them to another file
	 * @param lines arraylist containing the lines
	 * @param file File object to write to
	 */
	public void writeFile(ArrayList<String> lines, File file) {
		
		
		try {
			Path path = Paths.get(file.getAbsolutePath());
			java.nio.file.Files.write(path, lines, Charset.forName("UTF-8"),  StandardOpenOption.APPEND);
		} catch (Exception e) {
			log.info("Could not write to file");
			e.printStackTrace();
		}
		
	}
	
	public File getFile() {
		return file;
	}
	

}
