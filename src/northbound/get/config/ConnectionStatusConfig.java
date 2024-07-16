package northbound.get.config;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;

import extra.RESTStatistics;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig.GetConnectionStatus;

public class ConnectionStatusConfig extends RESTStatistics{
	
	final static Logger log = Logger.getLogger(ConnectionStatusConfig.class);
	
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/kvm";
	}
	
	public class GetConnectionStatus {
		public String[] connection_names;
	}
	
	public String getConnectionName(int position) {
		return "message.connections[" + position + "].name";
	}
	
	public String getCmode(int position) {
		return "message.connections[" + position + "].cmode";
	}
	
	public String getZone(int position) {
		return "message.connections[" + position + "].zone";
	}
	public String getConnectionHost(int position) {
		return "message.connections[" + position + "].host";
	}
	public String getConnectionConnectionType(int position) {
		return "message.connections[" + position + "].connection_type";
	}
	public String getConnectionViewOnly(int position) {
		return "message.connections[" + position + "].view_only";
	}
	public String getConnectionGroup(int position) {
		return "message.connections[" + position + "].group";
	}
	public String getConnectionExtDesk(int position) {
		return "message.connections[" + position + "].extended_desktop";
	}
	public String getConnectionAudio(int position) {
		return "message.connections[" + position + "].audio";
	}
	public String getConnectionPersistent(int position) {
		return "message.connections[" + position + "].persistent";
	}
	public String getConnectionUsb(int position) {
		return "message.connections[" + position + "].usb_redirection";
	}
	
	public void checkPairedConnection(String name, String connection_type, String host, String host2, String audio, 
			Integer audio_source, String orientation, String pairing_type, String persistent, String view_only,  String zone, String boxillaIp, String restUser, String restPassword ) {
		ConnectionStatusConfig conStatusConfig = new ConnectionStatusConfig();
		
		if(zone.equals("")) {
			zone = null;
		}
		
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(conStatusConfig.getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body(conStatusConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(host))
		.body("message.connections[0].connection_type", equalTo(connection_type))
		.body("message.connections[0].view_only", equalTo(view_only))
		.body("message.connections[0].group", equalTo("TXPair"))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].audio_source", equalTo(audio_source))
		.body("message.connections[0].host_2", equalTo(host2))
		.body("message.connections[0].pairing_type", equalTo(pairing_type))
		.body("message.connections[0].persistent", equalTo(persistent))
		.body("message.connections[0].config", equalTo("Unique"))
		.body("message.connections[0].zone", equalTo(zone)).extract().response();
		
		
		SaveResponseStatistics(conStatusConfig.getUri(boxillaIp), REQUEST_TYPE.GET, response);

	}
	
	public void checkViaTxConnection(String name, String host, String connection_type, String view_only, String group,
			String ext_desk, String audio, String persistent, String usb, String cmode, String zone, String boxillaIp, String restUser, String restPassword) {
		ConnectionStatusConfig conStatusConfig = new ConnectionStatusConfig();
		if(cmode.equals("10")) {
			cmode = "Optimized";
		}else if(cmode.equals("0")) {
			cmode = "Lossless";
		}
		
		if(zone.equals("")) {
			zone = null;
		}
		
		log.info("Checking connection " + name + " in REST");
		String json = "{ \"connection_names\": [\"" + name + "\"]}";
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(json)
		.when().contentType(ContentType.JSON)
		.get(conStatusConfig.getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body(conStatusConfig.getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(host))
		.body("message.connections[0].connection_type", equalTo(connection_type))
		.body("message.connections[0].view_only", equalTo(view_only))
		.body("message.connections[0].group", equalTo("ConnectViaTx"))
		.body("message.connections[0].extended_desktop", equalTo(ext_desk))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].persistent", equalTo(persistent))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].config", equalTo("Unique"))
		.body("message.connections[0].compression", equalTo(cmode)) 
		.body("message.connections[0].zone", equalTo(zone)).extract().response();
		
		SaveResponseStatistics(conStatusConfig.getUri(boxillaIp), REQUEST_TYPE.GET, response);
		
	}
	public void checkPoolConnections(String name, String extDesk, String usb, String view, String host,
			String connection_type, String audio, String zone, String boxillaIp, String restUser, String restPassword) {
		
		GetConnectionStatus conStatus = new GetConnectionStatus();
		conStatus.connection_names = new String[1];
		conStatus.connection_names[0] = name;
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(conStatus)
		.when().contentType(ContentType.JSON)
		.get(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body(getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(""))
		.body("message.connections[0].connection_type", equalTo(connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo("VMPool"))
		.body("message.connections[0].extended_desktop", equalTo(extDesk))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].config", equalTo("Unique")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.GET, response);
		//.body("message.connections[0].zone", equalTo(zone));
		
	}
	public void checkVMConnection(String name, String extDesktop, String usb, String nla, String view, 
			String port, String domain, String host, String username, String password, String connection_type, 
			String audio, String zone, String boxillaIp, String restUser, String restPassword) {
		
		GetConnectionStatus conStatus = new GetConnectionStatus();
		conStatus.connection_names = new String[1];
		conStatus.connection_names[0] = name;
		
		if(zone.equals("")) {
			zone = null;
		}
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(conStatus)
		.when().contentType(ContentType.JSON)
		.get(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body(getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(host))
		.body("message.connections[0].connection_type", equalTo(connection_type))
		.body("message.connections[0].view_only", equalTo(view))
		.body("message.connections[0].group", equalTo("VM"))
		.body("message.connections[0].extended_desktop", equalTo(extDesktop))
		.body("message.connections[0].audio", equalTo(audio))
		.body("message.connections[0].usb_redirection", equalTo(usb))
		.body("message.connections[0].port", equalTo(port))
		.body("message.connections[0].domain", equalTo(domain))
		.body("message.connections[0].nla", equalTo(nla))
		.body("message.connections[0].config", equalTo("Unique"))
		.body("message.connections[0].zone", equalTo(zone)).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.GET, response);
		
	}
	
	public void checkHorizonConnections(String name, String connection_type, String host, String view_only, String username, String password, String protocol
			, String zone, String boxillaIp, String restUser, String restPassword) {
		GetConnectionStatus conStatus = new GetConnectionStatus();
		conStatus.connection_names = new String[1];
		conStatus.connection_names[0] = name;
		if(zone.equals("")) {
			zone = null;
		}
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.body(conStatus)
		.when().contentType(ContentType.JSON)
		.get(getUri(boxillaIp))
		.then().assertThat().statusCode(200)
		.body(getConnectionName(0), equalTo(name))
		.body("message.connections[0].host", equalTo(host))
		.body("message.connections[0].connection_type", equalTo(connection_type))
		.body("message.connections[0].view_only", equalTo(view_only))
		.body("message.connections[0].group", equalTo("VMHorizon"))
		.body("message.connections[0].protocol", equalTo(protocol))
		.body("message.connections[0].username", equalTo(username))
		.body("message.connections[0].zone", equalTo(zone)).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.GET, response);

	}
 
}
