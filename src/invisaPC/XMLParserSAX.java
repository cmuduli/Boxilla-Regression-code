package invisaPC;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.apache.log4j.Logger;
import org.xml.sax.SAXException;



public class XMLParserSAX {

	final static Logger log = Logger.getLogger(XMLParserSAX.class);
	
    public ArrayList go(String fileName, String objectType) {
    SAXParserFactory saxParserFactory = SAXParserFactory.newInstance();
    try {
    	log.info("Parsing xml...");
        SAXParser saxParser = saxParserFactory.newSAXParser();
        MyUserHandler handler = new MyUserHandler();
        saxParser.parse(new File(fileName), handler);
       
        if(objectType.equals("User"))
        	return handler.getListOfUser();
        
        if(objectType.equals("Connection"))
        	return handler.getListOfConnections();
        
        if(objectType.equals("RX"))
        	return handler.getListOfRX();
        
        if(objectType.endsWith("Mgmt"))
        	return handler.getListOfMgmt();
        
        return null;
        
    } catch (ParserConfigurationException | SAXException | IOException e) {
        e.printStackTrace();
        return null;
    }
    }

}