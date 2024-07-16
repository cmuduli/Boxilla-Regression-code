package northbound.post.config;

import static io.restassured.RestAssured.given;

import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.get.config.ConnectionStatusConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;

import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;

import extra.RESTStatistics;

public class CreateKvmConnectionsConfig extends RESTStatistics {

	final static Logger log = Logger.getLogger(CreateKvmConnectionsConfig.class);
	
	public class CreateConnection {
		public String name;
		public String host;
		public String group;
		public String view_only;
		public String connection_type;
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
		public String cmode;
		public String zone;
		
	}
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/connections/kvm";
	}
	
	public void createVMHorizonConnection(String name, String connection_type, String host, String view_only, String username, String password, String protocol
			, String zone, String boxillaIp, String restUser, String restPassword) {
		CreateConnection con = new CreateConnection();
		con.name = name;
		con.group  = "VMHorizon";
		con.connection_type = connection_type;
		con.host = host;
		con.view_only = view_only;
		con.username = username;
		con.password = password;
		con.protocol = protocol;
		con.zone = zone;
		
		log.info("Creating connection ");
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created VMHorizon connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);
	}
	public void createPoolConnection(String name, String extDesk, String usb, String view, String host,
			String connection_type, String audio, String boxillaIp, String restUser, String restPassword) {
		CreateConnection con = new CreateConnection ();
		con.name = name;
		con.extended_desktop = extDesk;
		con.usb_redirection = usb;
		con.view_only = view;
		
		con.group = "VMPool";
		con.connection_type = connection_type;
		con.audio = audio;
		
		
		log.info("Creating VMPool connection");
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);

	}
	public void createVmConnection(String name, String extDesktop, String usb, String nla, String view, 
			String port, String domain, String host, String username, String password, String connection_type, 
			String audio, String zone, String boxillaIp, String restUser, String restPassword) {
		
		CreateConnection con = new CreateConnection ();
		con.name = name;
		con.extended_desktop = extDesktop;
		con.usb_redirection = usb;
		con.nla = nla;
		con.view_only = view;
		con.port = port;
		con.domain = domain;
		con.host = host;
		con.username = username;
		con.password = password;
		con.group = "VM";
		con.connection_type = connection_type;
		con.audio = audio;
		con.zone = zone;
		
		log.info("Creating VM connection");
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created " + con.group + " connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);

	}
	
	public void createPairConnection(String name, String connection_type, String host, String host2, String audio, 
			String audio_source, String orientation, String pairing_type, String persistent, String view_only, String zone, String boxillaIp, String restUser, String restPassword) {
		log.info("Creating TX PAired connection with name:" + name);
		CreateConnection con = new CreateConnection();
		con.name = name;
		con.connection_type = connection_type;
		con.group = "TXPair";
		con.host = host;
		con.host_2 = host2;
		con.audio = audio;
		con.audio_source = audio_source;
		con.orientation = orientation;
		con.pairing_type = pairing_type;
		con.persistent = persistent;
		con.view_only = view_only;
		con.zone = zone;
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created TXPair connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);
	}
	
	public void createViaTxConnection(String name, String host, String connection_type, String view_only, String group,
			String ext_desk, String audio, String persistent, String usb, String cmode, String zone, String boxillaIp, String restUser, String restPassword) {
		log.info("Creating TX connection with name:" + name);
		CreateConnection con = new CreateConnection();
		con.name = name;
		con.host = host;
		con.connection_type = connection_type;
		con.view_only = view_only;
		con.group = group;
		con.extended_desktop = ext_desk; 
		con.audio = audio;
		con.persistent = persistent;
		con.usb_redirection = usb;
		con.cmode = cmode;
		con.zone = zone;
		System.out.println("current user name is"   + restUser + "and password is " + restPassword );
		
		Response response = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(con)
		.post(getUri(boxillaIp))
		.then().assertThat().statusCode(201)
		.body("message", equalTo("Created ConnectViaTx connection " + con.name + ".")).extract().response();
		
		SaveResponseStatistics(getUri(boxillaIp), REQUEST_TYPE.POST, response);


	}
	
	
}
