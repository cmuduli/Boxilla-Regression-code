package northbound.post;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import methods.DevicesMethods;
import northbound.get.BoxillaHeaders;
import objects.Devices;

public class RebootDevice extends StartupTestCase {
	
	DevicesMethods deviceMethods = new DevicesMethods();

	@BeforeClass(alwaysRun = true)
	public void beforeClass() {
		printSuitetDetails(false);
		getDevices();
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
	}
	
	public class Reboot {
		String [] device_names;
	}
	
		
//	@Test
	public void test01_rebootAll() throws InterruptedException {
//		Thread.sleep(100000); 
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleTxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualTxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxOldUptime = deviceMethods.uptime(driver);
		
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot-all")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("All KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot-all", REQUEST_TYPE.POST, response);
		
		Thread.sleep(60000);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleTxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualTxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxNewUptime = deviceMethods.uptime(driver);
		
		Assert.assertTrue(singleTxNewUptime < singleTxOldUptime, "Single TX was not rebooted");
		Assert.assertTrue(dualTxNewUptime < dualTxOldUptime, "Dual TX was not rebooted");
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "Single RX was not rebooted");
		Assert.assertTrue(dualRxNewUptime < dualRxOldUptime, "Dual RX was not rebooted");
	}
	
	@Test
	public void test02_rebootSingleDeviceRX() throws InterruptedException {
		Thread.sleep(80000);  
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		String[] devices = {rxEmerald.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);

		Thread.sleep(60000);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		Thread.sleep(3000);
		float singleRxNewUptime = deviceMethods.uptime(driver);
		
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "RX device was not rebooted");
		
	}
	
	@Test
	public void test03_rebootMultipleDeviceRX() throws InterruptedException {
		Thread.sleep(80000);
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxOldUptime = deviceMethods.uptime(driver);
		
		String[] devices = {rxEmerald.getDeviceName(), rxDual.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);

		Thread.sleep(60000);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxNewUptime = deviceMethods.uptime(driver);
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "RX single device was not rebooted");
		Assert.assertTrue(dualRxNewUptime < dualRxOldUptime, "RX dual device was not rebooted");
		
	}
	@Test
	public void test04_rebootSingleDeviceTX() throws InterruptedException {
		Thread.sleep(80000);
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		String[] devices = {txEmerald.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);
       
		Thread.sleep(60000);
		
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleRxNewUptime = deviceMethods.uptime(driver);
		
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "TX device was not rebooted");
		
	}
	
	@Test
	public void test05_rebootMultipleDeviceTX() throws InterruptedException {
		Thread.sleep(80000);
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualRxOldUptime = deviceMethods.uptime(driver);
		
		String[] devices = {txEmerald.getDeviceName(), txDual.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);
		
		Thread.sleep(60000);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		driver.navigate().refresh();
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleRxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualRxNewUptime = deviceMethods.uptime(driver);
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "TX single device was not rebooted");
		Assert.assertTrue(dualRxNewUptime < dualRxOldUptime, "TX dual device was not rebooted");
		
	}
	
	@Test
	public void test06_rebootRxTx() throws InterruptedException {
		Thread.sleep(80000);
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleTxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualTxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxOldUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxOldUptime = deviceMethods.uptime(driver);
		
		String[] devices = {rxEmerald.getDeviceName(), rxDual.getDeviceName(), 
				txEmerald.getDeviceName(), txDual.getDeviceName()};
		Reboot reboot = new Reboot();
		reboot.device_names = devices;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(200)
		.body("message", equalTo("The selected KVM devices are successfully rebooted.")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);
		
		Thread.sleep(60000);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, txIp, txEmerald.getMac(), txEmerald.getDeviceName());
		float singleTxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		
		deviceMethods.retrieveDetails(driver, txIpDual, txDual.getMac(), txDual.getDeviceName());
		float dualTxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIp, rxEmerald.getMac(), rxEmerald.getDeviceName());
		float singleRxNewUptime = deviceMethods.uptime(driver);
		SeleniumActions.seleniumClick(driver,Devices.detailsclose);
		Thread.sleep(5000);
		deviceMethods.retrieveDetails(driver, rxIpDual, rxDual.getMac(), rxDual.getDeviceName());
		float dualRxNewUptime = deviceMethods.uptime(driver);
		
		Assert.assertTrue(singleTxNewUptime < singleTxOldUptime, "Single TX was not rebooted");
		Assert.assertTrue(dualTxNewUptime < dualTxOldUptime, "Dual TX was not rebooted");
		Assert.assertTrue(singleRxNewUptime < singleRxOldUptime, "Single RX was not rebooted");
		Assert.assertTrue(dualRxNewUptime < dualRxOldUptime, "Dual RX was not rebooted");
	}
	
	@Test
	public void test07_invalidDevice() {
		String[] device = {"invalid"};
		Reboot reboot = new Reboot();
		reboot.device_names = device;
		
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(reboot)
		.post("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot")
		.then().assertThat().statusCode(400)
		.body("message", equalTo("The following device does not exist: [\"invalid\"].")).extract().response();
		
		SaveResponseStatistics("https://" + boxillaManager + "/bxa-api/devices/kvm/reboot", REQUEST_TYPE.POST, response);

		
	}
}
