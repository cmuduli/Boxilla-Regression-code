package northbound.put.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;

import extra.RESTStatistics;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.put.EditKvmConnectionsViaTx;
import northbound.put.config.EditKvmConnectionsConfig.EditConnection;

public class EditKvmConnectionsConfig extends RESTStatistics {

	final static Logger log = Logger.getLogger(EditKvmConnectionsConfig.class);
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/kvm";
	}
	
	public class EditConnection {
		public String name;
		public String host;
		public String group;
		public String connection_type;
		public String view_only;
		public String new_name;
		public String extended_desktop;
		public String usb_redirection;
		public String audio;
		public String persistent;
		public String password;
		public String port;
		public String domain;
		public String nla;
		public String username;
		public String protocol;
		public String host_2;
		public String pairing_type;
		public String orientation;
		public String audio_source;
		public String zone;
		public String cmode;
		
	}
	public void editVMHorizonConnection(String name, String connection_type, String host,
			String view_only, String username, String password, String protocol
			, String new_name, String zone, String boxillaIp, String restUser, String restPassword) {
		EditConnection edit = new EditConnection();
		edit.name = name;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.view_only = view_only;
		edit.username = username;
		edit.password = password;
		edit.protocol = protocol;
		edit.new_name = new_name;
		edit.group  = "VMHorizon";
		edit.zone = zone;
		edit(boxillaIp, restUser, restPassword, edit);
	}
	
	public void editVMConnection(String name, String extDesktop, String usb, String nla, String view, 
			String port, String domain, String host, String username, String password, String connection_type, 
			String audio, String new_name, String zone, String boxillaIp, String restUser, String restPassword) {
		EditConnection edit = new EditConnection();
		edit.name = name;
		edit.extended_desktop = extDesktop;
		edit.usb_redirection = usb;
		edit.nla = nla;
		edit.view_only = view;
		edit.port = port;
		edit.domain = domain;
		edit.host = host;
		edit.username = username;
		edit.password = password;
		edit.connection_type = connection_type;
		edit.audio = audio;
		edit.group = "VM";
		edit.new_name = new_name;
		edit.zone = zone;
		edit(boxillaIp, restUser, restPassword, edit);
	}

	public void editPairedConnection(String name, String connection_type, String host, String host2, String audio, 
			String audio_source, String orientation, String pairing_type, String persistent, String view_only,String new_name, String zone, String boxillaIp, String restUser, String restPassword) {
		log.info("Editing connection with name:" + name);
		EditConnection edit = new EditConnection();
		edit.name = name;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.host_2 = host2;
		edit.audio = audio;
		edit.audio_source = audio_source;
		edit.orientation = orientation;
		edit.pairing_type = pairing_type;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.new_name = new_name;
		edit.group = "TXPair";
		edit.zone = zone;
		
		edit(boxillaIp, restUser, restPassword, edit);
	} 
	
	public void editViaTxConnection(String name, String host, String connection_type, String view_only, String group,
			String ext_desk, String audio, String persistent, String usb, String newName, String cmode, String zone, String boxillaIp, String restUser, String restPassword) {
		log.info("Editing connection with name:" + name);
		EditConnection edit = new EditConnection();
		edit.name = name;
		edit.group = group;
		edit.connection_type = connection_type;
		edit.host = host;
		edit.audio = audio;
		edit.extended_desktop = ext_desk;
		edit.persistent = persistent;
		edit.view_only = view_only;
		edit.usb_redirection = usb;
		edit.new_name = newName;
		edit.zone = zone;
		edit.cmode = cmode;
		
		edit( boxillaIp, restUser, restPassword, edit);
	}
	public void editPoolConnection(String name, String extDesk, String usb, String view, String host,
			String connection_type, String audio, String new_name, String boxillaIp, String restUser, String restPassword) {
		log.info("Editing connection with name:" + name);
		EditConnection edit = new EditConnection();
		edit.name = name;
		edit.extended_desktop = extDesk;
		edit.usb_redirection = usb;
		edit.view_only = view;
		//edit.host = host;
		edit.connection_type = connection_type;
		edit.audio = audio;
		edit.new_name = new_name;
		edit.group = "VMPool";
		edit( boxillaIp, restUser, restPassword, edit);
	}

	private void edit(String boxillaIp, String restUser, String restPassword, EditConnection edit) {
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(edit)
		.put(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Updated properties for connection " + edit.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.PUT, response);
	}
	
	
}
