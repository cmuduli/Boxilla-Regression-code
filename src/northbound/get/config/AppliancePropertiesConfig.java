package northbound.get.config;

import static io.restassured.RestAssured.given;

import org.apache.log4j.Logger;
import org.testng.Assert;

import extra.StartupTestCase;
import io.restassured.http.ContentType;
import northbound.get.BoxillaHeaders;


public class AppliancePropertiesConfig {
	
	final static Logger log = Logger.getLogger(AppliancePropertiesConfig.class);
	private String parameterReturn = "message.properties";
	
	public String getDeviceName(int position) {
		return parameterReturn + "[" + position + "].device_name";
	}
	public String getDeviceVideoQuality(int position) {
		return parameterReturn + "[" + position + "].video_quality"; 
	}
	public String getDeviceVideoSourceOptimization(int position) {
		return parameterReturn + "[" + position + "].video_source_optimization";
	}
	
	public String getDeviceHidConfigurations(int position) {
		return parameterReturn + "[" + position + "].hid_configurations";
	}
	public String getDeviceEdidSettingsDvi1(int position) {
		return parameterReturn + "[" + position + "].edid_settings_dvi1";
	}
	
	public String getDeviceEdidSettingsDvi2(int position) {
		return parameterReturn + "[" + position + "].edid_settings_dvi2";
	}
	public String getDeviceMouseKeyTimeout(int position) {
		return parameterReturn + "[" + position + "].mouse_keyboard_timeout";
	}
	public String getDeviceHttpEnabled(int position) {
		return parameterReturn + "[" + position + "].http_enabled";
	}
	public String getDeviceZone(int position) {
		return parameterReturn + "[" + position + "].zone";
	}
	
	public String getUri(String boxillaIp) {
		return "https://" + boxillaIp  + "/bxa-api/devices/kvm/properties";
	}
	
	
	//will check for up to 500 seconds for device state to be configured
		public void checkDeviceState(String deviceName, String boxillaIp, String restUser, String restPassword) throws InterruptedException {
			boolean isWaiting = false;
			boolean isConfiguring = false;
			boolean isConfigured = false;
			int counter = 1;
			
			GetProperties getProp = new GetProperties();
			getProp.device_names = new String[1];
			getProp.device_names[0] = deviceName;
			log.info("Checking for configured");
			String body = "";
			while(!isConfigured && counter < 800) {
				try {
				 body = given().auth().preemptive().basic(restUser, restPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.body(getProp)
				.when().contentType(ContentType.JSON)
				.get(getUri(boxillaIp)).asString();
				}catch(Exception e) {
					e.printStackTrace();
				}
				//log.info("Body:" + body);
				
				if(!isWaiting) {
					if(body.contains("waiting")) {
						isWaiting = true;
						log.info(deviceName + " is waiting");
					}else {
						if(counter == 800) {
							throw new AssertionError("Device did not go to configured after 800 tries");
						}
						log.info(deviceName + " is not waiting. Retrying:" + counter);
						Thread.sleep(1000);
						counter++; 
					}
				}
				if(!isConfiguring) {
					if(body.contains("configuring")) {
						isConfiguring = true;
						log.info(deviceName + " is configuring");
					}else {
						if(counter == 800) {
							throw new AssertionError("Device did not go to configuring after 500 tries");
						}
						log.info(deviceName + " is not configuring. Retrying:" + counter);
						Thread.sleep(1000);
						counter++; 
					} 
				
				}
				
				if(body.contains("configured")) {
					isConfigured = true;
					log.info(deviceName + " is configured");
				}else {
					if(counter == 800) {
						throw new AssertionError("Device did not go to configured after 500 tries");
					}
					log.info(deviceName + " is not configured. Retrying:" + counter);
					Thread.sleep(1000);
					counter++; 
				}
			}
			Assert.assertTrue(isWaiting && isConfiguring && isConfigured, "Device did not go through waiting/configuring/configured. " 
			+ isWaiting + " " + isConfiguring + " " + isConfigured);
		}
	
	public class GetProperties {
		public String[] device_names;
	}

	
	
	
}
