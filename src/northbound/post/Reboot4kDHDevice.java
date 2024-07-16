package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.apache.log4j.Logger;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import device.DeviceDiscovery;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.SystemMethods;
import methods.DevicesMethods.Reboot;
import northbound.get.BoxillaHeaders;
import testNG.Utilities;

public class Reboot4kDHDevice extends StartupTestCase2 {
	
	DevicesMethods deviceMethods = new DevicesMethods();
	SystemMethods system = new SystemMethods();
	private DiscoveryMethods methods = new DiscoveryMethods();
	final static Logger log = Logger.getLogger(DeviceDiscovery.class);
//	private String dualdevice = rxDual.getDeviceName();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() throws InterruptedException  {
		printSuitetDetails(false);
		getDevices();

		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		cleanUpLogin();
	}

	public class Reboot {
		String[] device_names;
	}
	//it will manage the 4K Dual Device

	@Test
	public void managedevice() throws InterruptedException {
		log.info("Adding the 4K DH device....");
		methods.discoverDevices(driver);
		methods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"), rxIpDual, rxDual.getGateway(),
				rxDual.getNetmask());
		methods.manageApplianceAutomatic(driver, rxDual.getDeviceName(), rxDual.getMac(), prop.getProperty("ipCheck"));
		log.info("4K Device managed Successfully");
		Thread.sleep(20000);
		system.pingIpAddress(rxIpDual);

	}
	//it will reboot the 4k device

	@Test
	public void rebootdualrx() throws InterruptedException {
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		String[] devices = { rxDual.getDeviceName() };
		Reboot reboot = new Reboot();
		reboot.device_names = devices;

		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword)
				.headers(BoxillaHeaders.getBoxillaHeaders()).when().contentType(ContentType.JSON).body(reboot)
				.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot").then().assertThat().statusCode(200)
				.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();

		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST,
				response);
		log.info("Pinging the Device");
		Thread.sleep(20000);
		system.pingIpAddress(rxIpDual);
	}

	@AfterClass
	public void aferclass() throws InterruptedException 
	{
		cleanUpLogin();
		log.info("Unmanage device ");
		deviceMethods.unManageDevice(driver, rxIpDual);
		cleanUpLogout();
		
	}
		
	}


