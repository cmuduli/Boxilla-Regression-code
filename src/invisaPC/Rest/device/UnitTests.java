package invisaPC.Rest.device;

import static io.restassured.RestAssured.basic;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.log4j.Logger;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.beust.testng.TestNG;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import methods.DevicesMethods;
import methods.DiscoveryMethods;

public class UnitTests extends StartupTestCase {

	
	final static Logger log = Logger.getLogger(UnitTests.class);
	DiscoveryMethods discoveryMethods = new DiscoveryMethods();
	DevicesMethods deviceMethods = new DevicesMethods();
	
	//test properties
	private String restuser = "rest_user";
	private String restPassword = "18_22_33_AA";
	private String txIp = prop.getProperty("txIP2");
	private String rxIp = prop.getProperty("rxIP2");
	
	private String singleHeadTx = "10.211.128.155";
	private String dualHeadTx = "";
	private String rx = "";
	
	//system properties
	String videoQuality = "2";				///return from rest : 1
	String videoSource = "DVI optimised";	///return from rest : 1
	String hidConfig = "Basic";				///return from rest : 1
	String audio = "USB";					//return from rest : 2
	String edidDvi1 = "1920x1080";			///return from rest : 0
	String edidDvi2 = "1920x1200";			///return from rest : 1
	String mouseTimeout = "3";				///return from rest : 3
	boolean isManual = false;				//sets Power Mode to Auto
	boolean isHttpEnabled = true;			//sets HTTP Enabled to Enabled
	private String port;
	private String http;
	
	public void go() {
		
		port = getPort();
		http = getHttp();
		System.out.println(http + port);
		try {
//			
//			System.out.println("Single head IP:" + single);
//			System.out.println("Dual head IP:" + dual);
//			System.out.println("RX IP:" + rx);
			this.singleHeadTx = "10.211.128.155";
			this.dualHeadTx = "10.10.11.245";
			this.rx = "10.10.10.156";
			//cleanUpLogin();
			//deviceManageTestPrepDualHead();
			//deviceManageTestPrepSingleHead();
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			//RestAssured.baseURI = "http://" + txIp + ":7778/control/configuration/tx_settings";
			
		}catch(Exception | AssertionError e) {
			e.printStackTrace();
			//cleanUpLogout();
		}
		//cleanUpLogout();
		
		int videoQuality = given().when().get("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").andReturn().getStatusCode();
		System.out.println(videoQuality);
	}
	
	@BeforeClass
	//@Parameters({"single", "dual", "rx"})
	public void beforeClass() {
		port = getPort();
		http = getHttp();
		System.out.println(http + port);
		try {
//			
//			System.out.println("Single head IP:" + single);
//			System.out.println("Dual head IP:" + dual);
//			System.out.println("RX IP:" + rx);
			this.singleHeadTx = "10.211.128.155";
			this.dualHeadTx = "10.10.11.245";
			this.rx = "10.10.10.156";
			//cleanUpLogin();
			//deviceManageTestPrepDualHead();
			//deviceManageTestPrepSingleHead();
			RestAssured.authentication = basic(restuser, restPassword);
			RestAssured.useRelaxedHTTPSValidation();
			
			//RestAssured.baseURI = "http://" + txIp + ":7778/control/configuration/tx_settings";
			
		}catch(Exception | AssertionError e) {
			e.printStackTrace();
			//cleanUpLogout();
		}
		//cleanUpLogout();
	}
	
	@Override
	@BeforeMethod
	@Parameters({ "browser" })
	public void login(String browser, Method method) {
		
	}
	
//	@Test
//	public void test() {
//		
//		class Con{
//			public String action = "make_connection";
//			public String user = "admin";
//			public String transmitter = "10.10.10.155";
//		}
//		String x = given()
//		.body("{\r\n" + 
//				"   “action”: “make_connection”,\r\n" + 
//				"   “user”: “admin”,   \r\n" + 
//				"   “transmitter”: “10.10.10.155”   \r\n" + 
//				"}\r\n" + 
//				"")
//		.when().contentType(ContentType.JSON).put("http://10.10.10.156:7778/corrib/control/connections").andReturn().statusLine();
//		
//		System.out.println(x);	
//	}
	
	@Override
	@AfterMethod
	public void logout(ITestResult result) {
		log.info("********* @ After Method Started ************");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		try {
			Thread.sleep(2000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			log.info(result.getName() + " :FAIL");
		
		if(ITestResult.SKIP == result.getStatus())
			log.info(result.getName() + " :SKIP");
		
		if(ITestResult.SUCCESS == result.getStatus())
			log.info(result.getName() + " :PASS");
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			//String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			//Utilities.captureScreenShot(driver, screenShotName, result.getName());
		}
	}
	
//	@Test
//	public void test01() {
//		//RestAssured.baseURI = "http://10.10.10.155:7778";
//		ArrayList<String> listOfProperties = new ArrayList<String>();
//		listOfProperties.add("hid");
//		listOfProperties.add("video_source_optimisation");
//		listOfProperties.add("mouse_keyboard_timeout");
//		listOfProperties.add("head_1_edid");
//		listOfProperties.add("video_quality");
//		
//		
//		String x = given()
//		.body("{\"hid\":1,\"video_source_optimisation\":2,\"mouse_keyboard_timeout\":4,\"head_1_edid\":4, \"video_quality\":4}")
//		.when().contentType(ContentType.JSON).put("https://10.10.10.155:8888/control/configuration/tx_settings").andReturn().statusLine();
//		
//		System.out.println(x);	
//	}
	
	@Test
	public void testAllCombinationsSingleheadTX() throws InterruptedException {
	
		if(!singleHeadTx.equals("skip")) {
		System.out.println("Test Starting - testAllCombinationsSingleheadTX" );
		int pass = 0;
		int fail = 0;
		//video quality
		for(int videoQ = 0; videoQ < 5; videoQ++) {
			//videoSource
			for(int videoSou=0; videoSou < 5; videoSou++) {
				//hid
				for(int hid = 0; hid < 2; hid++) {
					//edid1
					for(int edid = 0; edid < 5; edid++) {
						System.out.println("Quality:" + videoQ);
						System.out.println("videoSou:" + videoSou);
						System.out.println("hid:" + hid);
						System.out.println("edid:" + edid);
						System.out.println("*******************************");
						sendPutTXSingle(setSingleTx(videoQ,videoSou, hid,  edid));
						
						Thread.sleep(90000);
						
						int videoQuality = given().when().get("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("video_quality");
						int videoSource = given().when().get("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("video_source_optimisation");
						int hidReturn = given().when().get("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("hid");
						int edidReturn = given().when().get("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("head_1_edid");
						
						if(videoQuality == videoQ && videoSource == videoSou
								&& hidReturn == hid && edidReturn == edid) {
							System.out.println("ALL VALUES RETURNED OK - TEST PASSED");
							pass++;
							System.out.println("***********************************");
							System.out.println("Pass:" + pass + "  Fail:" + fail);
							System.out.println("***********************************");
						}else {
							System.out.println("VALUES DID NOT MATCH - TEST FAILED");
							System.out.println("Video Quality: Expected: " + videoQ + ", actual:" + videoQuality);
							System.out.println("Video Source: Expected: " + videoSou + ", actual:" + videoSource);
							System.out.println("HID: Expected: " + hid + ", actual:" + hidReturn);
							System.out.println("EDID: Expected: " + edid + ", actual:" + edidReturn);
							fail++;
							System.out.println("***********************************");
							System.out.println("Pass:" + pass + "  Fail:" + fail);
							System.out.println("***********************************");
						}
						
					}
				}
			}
		}
		System.out.println("**************** Results ******************");
		System.out.println("PASS:" + pass);
		System.out.println("FAIL" + fail);
		System.out.println("*******************************************");
		}
	}
	
	
//	@Test
//	public void testAllCombinationsDualHeadRX() throws InterruptedException {
//		
//		if(!rx.equals("skip")) {
//		System.out.println("Test Starting - testAllCombinationsDualHeadRX" );
//		int pass = 0; 
//		int fail = 0;
//		
//		//httpEnabled
//		for(int http=0; http < 2; http++) {
//			for(int power=0; power < 2; power++) {
//				System.out.println("HTTP Enabled:" + http);
//				System.out.println("Power Mode:" + power);
//				sendPutRX(setRX(power, http));
//				Thread.sleep(70000);
//				
//				int powerReturn = given().when().get("https://" + rx + ":8888/control/configuration/rx_settings").jsonPath().get("power_mode");
//				int httpReturn = given().when().get("https://" + rx + ":8888/control/configuration/rx_settings").jsonPath().get("http_enable");
//				
//				if(powerReturn == power && httpReturn == http) {
//					System.out.println("ALL VALUES RETURNED OK - TEST PASSED");
//					pass++;
//					System.out.println("***********************************");
//					System.out.println("Pass:" + pass + "  Fail:" + fail);
//					System.out.println("***********************************");
//				}else {
//					System.out.println("VALEUS DID NOT MATCH - TEST FAILED");
//					System.out.println("HTTP Enabled: Expected: " + http + ", actual : " + httpReturn);
//					System.out.println("Power Mode: Expected: " + power + ", actual : " + powerReturn);
//					fail++;
//					System.out.println("***********************************");
//					System.out.println("Pass:" + pass + "  Fail:" + fail);
//					System.out.println("***********************************");
//				}
//				
//				
//			}
//		}
//		
//		System.out.println("**************** Results ******************");
//		System.out.println("PASS:" + pass);
//		System.out.println("FAIL" + fail);
//		System.out.println("*******************************************");
//		}
//	}
	
	
//	@Test
//	public void testAllCombinationsSingleHeadRX() throws InterruptedException {
//		System.out.println("Test Starting - testAllCombinationsSingleHeadRX" );
//		int pass = 0; 
//		int fail = 0;
//		
//		//httpEnabled
//		for(int http=0; http < 2; http++) {
//			for(int power=0; power < 2; power++) {
//				System.out.println("HTTP Enabled:" + http);
//				System.out.println("Power Mode:" + power);
//				sendPutRXSingle(setRX(power, http));
//				Thread.sleep(70000);
//				
//				int powerReturn = given().when().get("https://10.10.10.156:8888/control/configuration/rx_settings").jsonPath().get("power_mode");
//				int httpReturn = given().when().get("https://10.10.10.156:8888/control/configuration/rx_settings").jsonPath().get("http_enable");
//				
//				if(powerReturn == power && httpReturn == http) {
//					System.out.println("ALL VALUES RETURNED OK - TEST PASSED");
//					pass++;
//					System.out.println("***********************************");
//					System.out.println("Pass:" + pass + "  Fail:" + fail);
//					System.out.println("***********************************");
//				}else {
//					System.out.println("VALEUS DID NOT MATCH - TEST FAILED");
//					System.out.println("HTTP Enabled: Expected: " + http + ", actual : " + httpReturn);
//					System.out.println("Power Mode: Expected: " + power + ", actual : " + powerReturn);
//					fail++;
//					System.out.println("***********************************");
//					System.out.println("Pass:" + pass + "  Fail:" + fail);
//					System.out.println("***********************************");
//				}
//				
//				
//			}
//		}
//		
//		System.out.println("**************** Results ******************");
//		System.out.println("PASS:" + pass);
//		System.out.println("FAIL" + fail);
//		System.out.println("*******************************************");
//	}
//	
//	@Test
//	public void testAllCombinationsDualheadTX() throws InterruptedException {
//		if(!dualHeadTx.equals("skip")) {
//		System.out.println("Test Starting - testAllCombinationsDualheadTX" );
//		int pass = 0;
//		int fail = 0;
//		//video quality
//		for(int videoQ = 0; videoQ < 5; videoQ++) {
//			//videoSource
//			for(int hid=0; hid < 3; hid++) {
//				//hid
//				for(int edid1 = 0; edid1 < 5; edid1++) {
//					//edid1
//					for(int edid2 = 0; edid2 < 5; edid2++) {
//						System.out.println("Quality:" + videoQ);
//						System.out.println("hid:" + hid);
//						System.out.println("edid:" + edid1);
//						System.out.println("edid:" + edid2);
//						System.out.println("*******************************");
//						sendPutTX(setTX(videoQ,hid,0,edid1, edid2));
//						
//						Thread.sleep(90000);
//						
//						int videoQuality = given().when().get("https://" + dualHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("video_quality");
//						int hidReturn = given().when().get("https://" + dualHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("hid");
//						int edidReturn = given().when().get("https://" + dualHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("head_1_edid");
//						int edidReturn2 = given().when().get("https://" + dualHeadTx + ":8888/control/configuration/tx_settings").jsonPath().get("head_2_edid");
//						
//						if(videoQuality == videoQ && hidReturn == hid && edidReturn == edid1 && edidReturn2 == edid2) {
//							System.out.println("ALL VALUES RETURNED OK - TEST PASSED");
//							pass++;
//							System.out.println("***********************************");
//							System.out.println("Pass:" + pass + "  Fail:" + fail);
//						}else {
//							System.out.println("VALUES DID NOT MATCH- TEST FAILED");
//							System.out.println("Video Quality: Expected: " + videoQ + ", actual:" + videoQuality );
//							System.out.println("HID: Expected: " + hid + ", actual:" + hidReturn);
//							System.out.println("EDID: Expected: " + edid1 + ", actual:" + edidReturn);
//							System.out.println("EDID2: Expected: " + edid2 + ", actual:" + edidReturn2);
//							fail++;
//							System.out.println("***********************************");
//							System.out.println("Pass:" + pass + "  Fail:" + fail);
//						}
//						
//					}
//				}
//			}
//		}
//		System.out.println("**************** Results ******************");
//		System.out.println("PASS:" + pass);
//		System.out.println("FAIL" + fail);
//		System.out.println("*******************************************");
//		
//		}
//		
//	}
	
	
//	@Test
//	public void test01_videoQualityAllValues() throws InterruptedException {
//		log.info("**** Starting test01_videoQualityAllValues ****");
//		for(int j=0; j<5; j++) {
//			log.info("Testing video quality with value:" + j);
//			sendPutTX(setTX(j,0,0,0,0));
//			Thread.sleep(60000);
//			
//			
//			int value = given().when().get("http://" + txIp + ":7778/control/configuration/tx_settings").jsonPath().get("video_quality");
//			System.out.println("Value:" + value);
//			given()
//			.when()
//			.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("video_quality", equalTo(j));	
//		}
//	}
	
//	@Test
//	public void test02_hidAllVlaues() throws InterruptedException {
//		log.info("**** Starting test02_hidAllVlaues ****");
//		for(int j=0; j < 3; j++) {
//			log.info("Testing hid with value:" + j);
//			sendPut(setTX(0,j,0,0,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("hid", equalTo(j));	
//			log.info("hid " + j + " OK!");
//		}
//	}
	
//	@Test
//	public void test03_edid1AllVlaues() throws InterruptedException {
//		log.info("**** Starting test03_edid1AllVlaues ****");
//		for(int j=0; j < 6; j++) {
//			log.info("Testing head_1_edid with value:" + j);
//			sendPut(setTX(0,0,0,j,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("head_1_edid", equalTo(j));	
//			log.info("head_1_edid " + j + " OK!");
//		}
//	}
	
//	@Test
//	public void test04_edid2AllVlaues() throws InterruptedException {
//		log.info("**** Starting test04_edid2AllVlaues ****");
//		for(int j=0; j < 6; j++) {
//			log.info("Testing head_2_edid with value:" + j);
//			sendPut(setTX(0,0,0,0,j));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("head_2_edid", equalTo(j));	
//			log.info("head_2_edid " + j + " OK!");
//		}
//	}
	
//	@Test
//	public void test05() throws InterruptedException {
//		//EDID LOOP
//		for(int j=0; j < 5; j++) {
//			//video quality loop
//			for(int k=0; k < 5; k++) {
//				System.out.println("Video Quality:" + k);
//				System.out.println("edid:" + j);
//				sendPut(setTX(k,0,0,j,0));
//				Thread.sleep(60000);
//				
//				given()
//				.when()
//				.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//				.then()
//				.body("video_quality", equalTo(k -1));	
//				log.info("video_quality" + k + " OK!");
//				
//				given()
//				.when()
//				.get("http://" + txIp + ":7778/control/configuration/tx_settings")
//				.then()
//				.body("head_1_edid", equalTo(j));	
//				log.info("head_1_edid " + j + " OK!");
//			}
//		}
//	}
	
//	@Test
//	public void test06_powerModeAllValues() throws InterruptedException {
//		log.info("**** Starting test06_powerModeAllValues ****");
//		for(int j=0 ; j < 2; j++) {
//			sendPutRX(setRx(j,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + rxIp + ":7778/control/configuration/rx_settings")
//			.then()
//			.body("power_mode", equalTo(j));	
//			log.info("power_mode " + j + " OK!");
//			
//		}
//	}
//	
//	@Test
//	public void test07_httpEnableAllValues() throws InterruptedException {
//		log.info("**** Starting test07_httpEnableAllValues ****");
//		for(int j=0 ; j < 2; j++) {
//			sendPutRX(setRx(0,j));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + rxIp + ":7778/control/configuration/rx_settings")
//			.then()
//			.body("http_enable", equalTo(j));	
//			log.info("http_enable " + j + " OK!");
//		}
//	}
	
	//single head
//	@Test
//	public void test08_singleHheadVideoQualityAllValues() throws InterruptedException {
//		log.info("**** Starting test08_singleHheadVideoQualityAllValues ****");
//		for(int j=0; j<5; j++) {
//			log.info("Testing video quality with value:" + j);
//			sendPutTXSingle(setSingleTx(j,0,0,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + prop.getProperty("txIp2") + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("video_quality", equalTo(j));	
//		}
//	}
	
//	@Test
//	public void test09_singleHheadVideoSourceAllValues() throws InterruptedException {
//		log.info("**** Starting test09_singleHheadVideoSourceAllValues ****");
//		for(int j=0; j<5; j++) {
//			log.info("Testing video source with value:" + j);
//			sendPutTXSingle(setSingleTx(0,j,0,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + prop.getProperty("txIp2") + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("video_source_optimisation", equalTo(j));	
//			log.info("video_source_optimisation " + j + " OK!");
//		}
//	}
	
//	@Test
//	public void test10_singleHheadHidAllValues() throws InterruptedException {
//		log.info("**** Starting test10_singleHheadHidAllValues ****");
//		for(int j=0; j<3; j++) {
//			log.info("Testing hid with value:" + j);
//			sendPutTXSingle(setSingleTx(0,0,j,0));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + prop.getProperty("txIp2") + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("hid", equalTo(j));	
//			log.info("hid " + j + " OK!");
//		}
//	}
	
//	@Test
//	public void test10_singleHheadEdid1AllValues() throws InterruptedException {
//		log.info("**** Starting test10_singleHheadEdid1AllValues ****");
//		for(int j=0; j < 6; j++) {
//			log.info("Testing EDID1 with value:" + j);
//			sendPutTXSingle(setSingleTx(0,0,0,j));
//			Thread.sleep(60000);
//			given()
//			.when()
//			.get("http://" + prop.getProperty("txIp2") + ":7778/control/configuration/tx_settings")
//			.then()
//			.body("head_1_edid", equalTo(j));	
//			log.info("head_1_edid " + j + " OK!");
//		}
//	}
	
	public TX_Single_Properties setSingleTx(int videoQuality, int videoSource, int hid, int edid1) {
		TX_Single_Properties tx = new TX_Single_Properties();
		tx.setVideo_quality(videoQuality);
		tx.setVideo_source_optimisation(videoSource);
		tx.setHid(hid);
		tx.setHead_1_edid(edid1);
		
		return tx;
	}
	
	public TX_Properties setTX(int videoQuality, int hid, int mouse, int edid1, int edid2) {
		TX_Properties tx = new TX_Properties();
		tx.setVideo_quality(videoQuality);
		tx.setHid(hid);
		tx.setMouse_keyboard_timeout(mouse);
		tx.setHead_1_edid(edid1);
		tx.setHead_2_edid(edid2);
		
		return tx;
	}
	
	public RX_Properties setRX(int powerMode, int httpEnable) {
		RX_Properties rx = new RX_Properties();
		rx.setPower_mode(powerMode);
		rx.setHttp_enable(httpEnable);
		
		return rx;
	}
	
	public void sendPutTX(Properties prop) {
		Header head = new Header("version", "1.0.4");
		int status = given().header(head).body(prop)
		.when()
		.contentType(ContentType.JSON)
		.put("https://" + dualHeadTx + ":8888/control/configuration/tx_settings").andReturn().statusCode();
		
		System.out.println("PUT STATUS CODE:" + status);
	}
	
	public void sendPutTXSingle(Properties prop) {
		Header head = new Header("version", "1.0.4");
		int status = given().header(head).body(prop)
		.when()
		.contentType(ContentType.JSON)
		.put("https://" + singleHeadTx + ":8888/control/configuration/tx_settings").andReturn().statusCode();
	
		System.out.println("PUT STATUS CODE:" + status);
	}
	
	public void sendPutRX(Properties prop) {
		int status = given().body(prop)
		.when()
		.contentType(ContentType.JSON)
		.put("https://" + rx  + ":8888/control/configuration/rx_settings").andReturn().statusCode();
		
		System.out.println("PUT STATUS CODE:" + status);
	}
	
	public void sendPutRXSingle(Properties prop) {
		int status = given().body(prop)
		.when()
		.contentType(ContentType.JSON)
		.put("https://10.10.10.156:8888/control/configuration/rx_settings").andReturn().statusCode();
		
		System.out.println("PUT STATUS CODE:" + status);
	}
	
	
	
	public void deviceManageTestPrepSingleHead() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("rxMac"), prop.getProperty("ipCheck"), prop.getProperty("rxIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		//discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", prop.getProperty("rxMac"), prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("txMac"), prop.getProperty("ipCheck"), prop.getProperty("txIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		//discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", prop.getProperty("txMac"), prop.getProperty("ipCheck"));
		
		log.info("IP Addresses given to both RX and TX devices");
	}
	
	
	public void deviceManageTestPrepDualHead() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("rxMac"), prop.getProperty("ipCheck"), prop.getProperty("rxIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		//discoveryMethods.manageApplianceAutomatic(driver, "Test_RX", prop.getProperty("rxMac"), prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, prop.getProperty("txMac"), prop.getProperty("ipCheck"), prop.getProperty("txIP"), prop.getProperty("gateway"), prop.getProperty("netmask"));
		//discoveryMethods.manageApplianceAutomatic(driver, "Test_TX", prop.getProperty("txMac"), prop.getProperty("ipCheck"));
		
		log.info("IP Addresses given to both RX and TX devices");
	}
	
	public static void main(String args[]) {
		  List<String> file = new ArrayList<String>();
		    file.add("unit.xml");
		    TestNG testNG = new TestNG();
		    testNG.setTestSuites(file);
		    testNG.run();
//		UnitTests u = new UnitTests();
//		u.go();
		
		
	}
	
}
