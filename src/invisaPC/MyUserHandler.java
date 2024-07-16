package invisaPC;
import java.util.ArrayList;
import java.util.List;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;


public class MyUserHandler extends DefaultHandler {

    private List<Connection> empList = null;
    private Connection emp = null;


    //getter method for employee list
    public List<Connection> getEmpList() {
        return empList;
    }

    boolean bConnection = false;
    boolean bAudio = false;
    boolean bPersistent = false;
    boolean bUserName = false;
    boolean bColourDepth = false;
    String audio = "";
    String persistent = "";
    private ArrayList<Connection> listOfConnections = new ArrayList<Connection>();
    private ArrayList<User> listOfUser = new ArrayList<User>();
    private ArrayList<Device> listOfRX = new ArrayList<Device>();
    private ArrayList<Mgmt> listOfMgmt = new ArrayList<Mgmt>();

    
    public ArrayList<Mgmt> getListOfMgmt(){
    	return listOfMgmt;
    }
    
    public ArrayList<Device> getListOfRX(){
    	return listOfRX;
    }
    public ArrayList<Connection> getListOfConnections(){
    	return listOfConnections;
    }
    public ArrayList<User> getListOfUser(){
    	return listOfUser;
    }
    
    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes)
            throws SAXException {

    	
    	if(qName.equalsIgnoreCase("Connection")) {
    		Connection con = new Connection();
   		 con.setAudio(attributes.getValue("Audio"));
   		 con.setPersistent(persistent = attributes.getValue("Persistent"));
   		 con.setBroker(attributes.getValue("Broker"));
   		 con.setName(attributes.getValue("Name"));
   		 con.setExtDesk(attributes.getValue("ExtDesk"));
   		 con.setDomain(attributes.getValue("Domain"));
   		 con.setPassword(attributes.getValue("Password"));
   		 con.setPreEmption_mode(attributes.getValue("PreEmption_mode"));
   		 con.setConnectionType(attributes.getValue("Connection_type"));
   		 con.setUsb_redirection(attributes.getValue("USB_Redirection"));
   		 
   		con.setViaTX(attributes.getValue("viaTX"));
   		con.setColour_depth(attributes.getValue("Colour_depth"));
   		con.setUser_name(attributes.getValue("User_Name"));
   		con.setIp_address(attributes.getValue("IP_address"));
   		con.setPort(attributes.getValue("Port"));
   		con.setHorizon(attributes.getValue("Horizon"));
   		// System.out.println(con.toString());
   		// System.out.println();
    		 
    		// System.out.println(con.toString());
    		 //System.out.println();
    		 listOfConnections.add(con);
    	}
    	if(qName.equalsIgnoreCase("User")) {
    		User user = new User();
    		user.setUser_name(attributes.getValue("User_Name"));
    		user.setPassword(attributes.getValue("Password"));
    		user.setPrivilege(attributes.getValue("Privilege"));

    		
    		
    		//System.out.println(user.toString());
    		//System.out.println();
    		
    		listOfUser.add(user);
    	}
    	if(qName.equalsIgnoreCase("RX")) {
    		Device device = new RX();
    		device.setDefault_gateway(attributes.getValue("Default_Gateway"));
    		device.setDns_address(attributes.getValue("DNS_Address"));
    		device.setSw_ver(attributes.getValue("SW_ver"));
    		device.setDns2_address(attributes.getValue("DNS_2_Address"));
    		device.setSerial_num(attributes.getValue("Serial_num"));
    		device.setModel_num(attributes.getValue("Model_num"));
    		device.setName(attributes.getValue("Name"));
    		device.setMac(attributes.getValue("MAC"));
    		device.setNetwork_mask(attributes.getValue("Network_Mask"));
    		device.setIp_address(attributes.getValue("IP_address"));
    		
    		listOfRX.add(device);
    	}
    	
    	if(qName.equalsIgnoreCase("Mgmt")) {
    		Mgmt mgmt = new Mgmt();
    		mgmt.setName(attributes.getValue("Name"));
    		mgmt.setMAC(attributes.getValue("MAC"));
    		mgmt.setIpAddress(attributes.getValue("IP_address"));
    		
    		listOfMgmt.add(mgmt);
    	}
    	
//        if (qName.equalsIgnoreCase("Employee")) {
//            //create a new Employee and put it in Map
//            String id = attributes.getValue("id");
//            //initialize Employee object and set id attribute
//            emp = new Connection();
//            emp.setId(Integer.parseInt(id));
//            //initialize list
//            if (empList == null)
//                empList = new ArrayList<>();
//        } else if (qName.equalsIgnoreCase("name")) {
//            //set boolean values for fields, will be used in setting Employee variables
//            bName = true;
//        } else if (qName.equalsIgnoreCase("age")) {
//            bAge = true;
//        } else if (qName.equalsIgnoreCase("gender")) {
//            bGender = true;
//        } else if (qName.equalsIgnoreCase("role")) {
//            bRole = true;
//        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (qName.equalsIgnoreCase("Employee")) {
            //add Employee object to list
          //  empList.add(emp);
        }
    }
    public void printDetails() {
    	System.out.println("Audio:" + audio);
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {

//        if (bAge) {
//            //age element, set Employee age
//            emp.setAge(Integer.parseInt(new String(ch, start, length)));
//            bAge = false;
//        } else if (bName) {
//            emp.setName(new String(ch, start, length));
//            bName = false;
//        } else if (bRole) {
//            emp.setRole(new String(ch, start, length));
//            bRole = false;
//        } else if (bGender) {
//            emp.setGender(new String(ch, start, length));
//            bGender = false;
//        }
    }
    

}