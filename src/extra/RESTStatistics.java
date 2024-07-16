package extra;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.OptionalDouble;

import org.apache.log4j.Logger;
import org.testng.Reporter;
import org.testng.annotations.Test;

import io.restassured.response.Response;

public class RESTStatistics {
	
	private static String path = "C:\\rest_statistics\\serialized.txt";
    private static File statisticsFile;
	private static Map<String, List<Long>> TestResults = new HashMap<String, List<Long>>();
	final String ESCAPE_PROPERTY = "org.uncommons.reportng.escape-output";
	final static Logger log = Logger.getLogger(StartupTestCase.class);
	
	public enum REQUEST_TYPE{
		POST("POST"),
		PUT("PUT"),
		GET("GET"),
		DELETE("DELETE");
		
		private final String requestType;       

		private REQUEST_TYPE(String requestType) {
			this.requestType = requestType;
		}

		public boolean equalsName(String otherName) {
			return requestType.equals(otherName);
		}

		public String toString() {
			return this.requestType;
		}
	}
	
	public void SaveResponseStatistics(String url, REQUEST_TYPE requestType, Response response) {
		String key = String.format("[%s]:%s",requestType.toString(), url);
		if (TestResults.containsKey(key)) {
			TestResults.get(key).add(response.getTime());
		}
		else {
			ArrayList<Long> values = new ArrayList<>();
			values.add(response.getTime());
			TestResults.put(key, values);
		}	
	}
	
	protected void PrintRestStatistics() {
		
		log.info("Initiating statistics file.");
		InitStatisticsFile();
		log.info("File initiated.");
		log.info("Merging results from previous regression with current.");
		AppendResults(TestResults);
		
		log.info("Generating report.");
		
		System.setProperty(ESCAPE_PROPERTY, "false");
		String tdStyle = "background-color: #efefef; text-align: center;";
	    String apiTdStlye ="background-color: #efefef;";
		StringBuilder markup = new StringBuilder();
		markup.append("<div style=padding-top: 15px; padding: 10px;'>");
        markup.append("<h2 style='padding: 4px 0;'>NBR statistics</h2>");
		markup.append("<table style='width:100%;'>");
		markup.append("<tr style='background-color: #07a54a;color: white;'>");
		markup.append("<th>API Path</th>");
		markup.append("<th>Runs</th>");
		markup.append("<th>Min Response time</th>");
		markup.append("<th>Max response time</th>");
		markup.append("<th>Average Response</th>");
		markup.append("</tr>");
	
		
		TestResults.forEach((key, val) ->{
			String testName = key;
			ArrayList<Long> responseTimes = (ArrayList<Long>) val;
			OptionalDouble average = responseTimes.stream()
                         .mapToDouble(a -> a)
                         .average();
			Long min = Collections.min(responseTimes);
			Long max = Collections.max(responseTimes);
			markup.append("<tr>");
			markup.append(String.format("<td style='%s'>%s</td>", apiTdStlye, testName));
			markup.append(String.format("<td style='%s'>%d</td>", tdStyle, responseTimes.size()));
			markup.append(String.format("<td style='%s'>%dms</td>", tdStyle, min));
			markup.append(String.format("<td style='%s'>%dms</td>", tdStyle, max));
			markup.append(String.format("<td style='%s'>%sms</td>", tdStyle, average.isPresent() ? Double.toString(average.getAsDouble()) : ""));
			markup.append("</tr>");
		});
		markup.append("<table>");
		markup.append("<div/>");
		log.info("Report generation complete.");
		Reporter.log(markup.toString());
	}
	
	private void AppendResults(Map<String, List<Long>> newResults) {
		log.info("Extracting previous report data.");
		Map<String, List<Long>> extracted = serializeStatistics();
			if (extracted.size() > 0) 
			  mergeDictionaries(newResults, extracted);
			log.info("Deserializing merged statistics for next regression.");
			deserializeStatistics(newResults);
	}
	
	private void deserializeStatistics(Map<String, List<Long>> newResults) {
		try {
			FileOutputStream f = new FileOutputStream(statisticsFile);
            ObjectOutputStream o = new ObjectOutputStream(f);

            // Write objects to file
            o.writeObject(newResults);

            o.close();
            f.close();
		} catch (FileNotFoundException e) {
	      System.out.println("File not found");
	    } catch (IOException e) {
	      System.out.println("Error initializing stream");
	    }
	}
	
	private Map<String, List<Long>> serializeStatistics() {
		FileInputStream fi;
		Map<String, List<Long>> List =  new HashMap<String, List<Long>>();
		try {
			if (statisticsFile.length() > 0) {
				fi = new FileInputStream(statisticsFile);
		        ObjectInputStream oi = new ObjectInputStream(fi);
		        
		        List = (HashMap<String, List<Long>>)oi.readObject();

		        oi.close();
		        fi.close();
		        return List;
			}

		} 
		catch (FileNotFoundException e) {
			e.printStackTrace();
		} 
		catch (IOException e) {
			e.printStackTrace();
		} 
		catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return  List;
	}
	
	private static void mergeDictionaries(Map<String, List<Long>> newResults, Map<String, List<Long>> oldResults) {
		oldResults.forEach((key, val) -> {
			if (newResults.containsKey(key))
			  newResults.get(key).addAll(val);
			else
			  newResults.put(key, val);
		});
	}
	
	private static void InitStatisticsFile() {
		if (statisticsFile==null)
		  statisticsFile = GetOrCreateStatisticsFile(path);
	}
	
	private static File GetOrCreateStatisticsFile(String path) {
		File file = new File(path);
		if (!file.exists())
			try {
				file.createNewFile();
			} 
			catch (IOException e) {
				e.printStackTrace();
			}
		return file;
	}
}

