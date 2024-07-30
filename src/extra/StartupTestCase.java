package extra;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.nio.file.Files;
import org.apache.log4j.Logger;
import org.eclipse.jetty.util.log.Log;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;

import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.http.Header;
import io.restassured.response.Response;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.HardwareMonitor;
import methods.SystemMethods;
import methods.UsersMethods;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import northbound.get.BoxillaHeaders;
import northbound.put.config.EditRestPasswordConfig;
import northbound.put.config.EditRestUsernameConfig;
import northbound.put.config.EditRestPasswordConfig.ChangePassword;
import northbound.put.config.EditRestUsernameConfig.Username;
import objects.Devices;
import objects.Landingpage;
import objects.Loginpage;
import objects.SystemAll;
import testNG.Utilities;

/**
 * Main test class that every testcase will inherit from.
 * This class will set up everything most test classes need. It will 
 * read the properties file and set up inherited instance variables.
 * 
 *  Also sets the webdriver version and browser to use in the test
 *  
 * @author Brendan O'Regan
 *
 */

public class StartupTestCase extends RESTStatistics{
	private String lexMac = "";
	private String lex1Mac = "";
	private String rex1Mac = "";
	private String rex2Mac = "";
	private String lexIp = "";
	private String rex1Ip = "";
	private String rex2Ip = "";
	
	public static String northboundVersion = "";
	private String boxillaBuild;
	public static boolean isEmerald = false;
	public static boolean isEmeraldSe  = false;
	public static boolean isZeroU = true;
	public static String deviceUserName, devicePassword;
	public WebDriver driver;
	public String boxillaManager;
	public String boxillaVersion;
	public String boxillaVersionNew;
	public Properties prop = new Properties();
	public Properties extraDevProp = new Properties();
	protected String userName;
	public  String password;
	private static long startTime;
	private static long splitTime;
	private static ArrayList<String> initialSmartCtl;
	private static HardwareMonitor ctl;
	private static int testCounter;
	public static String url;
	static Files hardwareFile;
	private static int waitTime;
	private int retry = 0;
	final static Logger log = Logger.getLogger(StartupTestCase.class);
	protected String port;
	protected String http;
	private String rest_version;
	private Header head;
	public String dbUser;
	public String dbName;
	public String dbPassword;
	private boolean shouldManageExtraDevices;
	protected AppliancePool devicePool = new AppliancePool();
	
	protected Device txSingle,txSingleExtra, rxSingle, txDual, rxDual, txEmerald, rxEmerald, shTx, dhTx, shRx, dhRx,shrx1,shrx2;
	public String[] deviceKeys ={"shtx","dhtx","shrx","dhrx","shrx1","shrx2"};
	
//	public String[] ConnectionsIP = {"10.211.130.107","10.211.133.18","10.211.130.90","10.211.133.9","10.211.133.15","10.211.128.234","10.211.128.232","10.211.129.235", "10.211.130.132", "10.211.131.34", "10.211.131.123", "10.211.133.29", "10.211.129.248", "10.211.132.47"};
	String browser = "firefox";
	//test properties
	protected String dellSwitchMac = "";
	protected String dellSwitchIp = "";
	protected String restuser = prop.getProperty("rest_user");
	protected String restPassword = prop.getProperty("rest_password");
	protected String txIp = prop.getProperty("txIP");
	protected String rxIp = prop.getProperty("rxIP");
//	protected String txIpExtra = prop.getProperty("txIPExtra");
	protected String txIpDual = prop.getProperty("txIPDual");
	protected String rxIpDual = prop.getProperty("rxIPDual");
	protected String txTemplateName = "TEST_TX";
	protected String rxTemplateName = "TEST_RX";
	public String singleTxName;
	public String dualTxName;
	public String singleRxName;
	public String dualRxName;
	public String sh;
	public String dh;
	protected String txEmeraldName = "Test_TX_Emerald";
	protected String rxEmeraldName = "Test_RX_Emerald";
	protected String boxillaManager2 = "";
	protected String virtualIp = "";
	public static String boxillaUsername = "";
	public static String boxillaPassword = "";
	public static String gifLocation = "C:\\Test_Workstation\\SeleniumAutomation\\test-output\\gifs";
	public String boxillaRestPassword;
	public String boxillaRestUser;
	public String boxillaRestPassword1;
	public String boxillaRestUser1;
	public static  boolean is4kCon = true;
	protected int compression = 10;

	

	/**
	 * Sets up most commonly used properties from properties file
	 */
	public StartupTestCase() {
		log.info("In StartUp constructor");
		loadProperties("test.properties");
		
		is4kCon = Boolean.parseBoolean(prop.getProperty("is4kCon"));
		System.out.print("is4kCon:" + is4kCon);
		northboundVersion = prop.getProperty("northboundVersion");
		lexMac = prop.getProperty("lexMac");
		lex1Mac = prop.getProperty("lex1Mac");
		rex1Mac = prop.getProperty("rex1Mac");
		rex2Mac = prop.getProperty("rex2Mac");
		compression = Integer.parseInt(prop.getProperty("compression"));
		lexIp = prop.getProperty("lexIp");
		
		rex1Ip = prop.getProperty("rex1Ip");
		rex2Ip = prop.getProperty("rex2Ip");
				
		
		virtualIp = prop.getProperty("vip");
		virtualIp = virtualIp.trim();
		boxillaManager2 = prop.getProperty("boxillaManager2");
		boxillaVersion = prop.getProperty("boxillaVersion");
		boxillaVersionNew = prop.getProperty("boxillaVersionNew");
		dellSwitchIp = prop.getProperty("dellswitchip");
		dellSwitchMac = prop.getProperty("dellswitchmac");
		boxillaManager = prop.getProperty("boxillaManager");
		userName = prop.getProperty("userName");
		password = prop.getProperty("password");
		waitTime = Integer.parseInt(prop.getProperty("waitTime"));
		url = "https://" + boxillaManager + "/";
		port = prop.getProperty("devicePort");
		http = prop.getProperty("http");
		rest_version = prop.getProperty("rest_version");
		head = new Header("api-version", rest_version);
		dbUser = prop.getProperty("dbUser");
		dbName = prop.getProperty("dbName");
		dbPassword = prop.getProperty("dbPassword");
		deviceUserName = prop.getProperty("deviceUserName");
		devicePassword = prop.getProperty("devicePassword");
		boxillaUsername = prop.getProperty("boxillaUserName");
		boxillaPassword = prop.getProperty("boxillaPassword");
		boxillaRestPassword = prop.getProperty("newRestPassword");
		boxillaRestUser = prop.getProperty("newRestUser");
		boxillaRestPassword1 = prop.getProperty("boxillaRestPassword");
		boxillaRestUser1 = prop.getProperty("boxillaRestUsername");
		String temp =  prop.getProperty("shouldManageExtraDevices");
		shouldManageExtraDevices = temp.equalsIgnoreCase("true") ? true : false;
		txIp=prop.getProperty("txIP");
		restuser = prop.getProperty("rest_user");
		restPassword = prop.getProperty("rest_password");
		
		
		//required for some of the suites, at least bonding
		if(shouldManageExtraDevices) {
			loadExtraDeviceProperties();
			try {
			String commonNetmask = extraDevProp.getProperty("commonNetmask");
			String commonGateway = extraDevProp.getProperty("commonGateway");
			String [] shTxProp = extraDevProp.getProperty("shtx").split(",");
			String [] shRxProp = extraDevProp.getProperty("shrx").split(",");
			String [] dhTxProp = extraDevProp.getProperty("dhtx").split(",");
			String [] dhRxProp = extraDevProp.getProperty("dhrx").split(",");
			String [] frRxProp = extraDevProp.getProperty("shrx1").split(",");
			String [] frRx1Prop = extraDevProp.getProperty("shrx2").split(",");

		
				
//				Device[] devices = new Device[deviceKeys.length];
//
//				// Iterate through the device keys array
//				for (int i = 0; i < deviceKeys.length; i++) {
//				    // Retrieve property values from extraDevProp
//				    String[] deviceProps = extraDevProp.getProperty(deviceKeys[i]).split(",");
//				    // Create a new Device instance and add it to the devices array
//				    devices[i] = new Device(deviceProps[0], deviceProps[1], commonGateway, commonNetmask, true, deviceProps[2]);
//				   
//				}
				
				shTx = new Device(shTxProp[0], shTxProp[1], commonGateway, commonNetmask, true, shTxProp[2]);
				shRx = new Device(shRxProp[0], shRxProp[1], commonGateway, commonNetmask, true, shRxProp[2]);
				dhTx = new Device(dhTxProp[0], dhTxProp[1], commonGateway, commonNetmask, true, dhTxProp[2]);
				dhRx = new Device(dhRxProp[0], dhRxProp[1], commonGateway, commonNetmask, true, dhRxProp[2]);
				shrx1 = new Device(frRxProp[0], frRxProp[1], commonGateway, commonNetmask, true, frRxProp[2]);
				shrx2 = new Device(frRx1Prop[0], frRx1Prop[1], commonGateway, commonNetmask, true, frRx1Prop[2]);
			}catch(Exception ex) {
				log.info("Failed to instantiate extra devices. Exception "+ex.getMessage());
				Exception some = ex;
			}

		}
			
		log.info("This is 4.0");
	}

	public String getLexMac() {
		return lexMac;
	}
	
	public String getLex1Mac() {
		return lex1Mac;
	}
	public String getRex1Mac() {
		return rex1Mac;
	}
	public String getRex2Mac() {
		return rex2Mac;
	}
	
	public String getLexIp() {
		return lexIp;
	}
	public void setLexIp(String lexIp) {
		this.lexIp = lexIp;
	}
	public String getRex1Ip() {
		return rex1Ip;
	}
	public String getRex2Ip() {
		return rex2Ip;
	}
	public void setRex1Ip(String rex1Ip) {
		this.rex1Ip = rex1Ip;
	}
	public void setRex2Ip(String rex2Ip) {
		this.rex2Ip = rex2Ip;
	}

	/**
	 * 
	 * @return boxilla database 
 name
	 */
	public String getDbUser() {
		return dbUser;
	}
	/**
	 * 
	 * @return boxilla database name
	 */
	public String getDbName() {
		return dbName;
	}
	/**
	 * 
	 * @return boxilla database password
	 */
	public String getDbPassword() { 
		return dbPassword;
	}
	
	/**
	 * 
	 * @return REST APIs header object
	 */
	public Header getHead() {
		return head;
	}
	/**
	 * 
	 * @return REST APIs rest version
	 */
	public String getRest_version() {
		return rest_version;
	}
	/**
	 * 
	 * @return REST APIs port used
	 */
	public String getPort() {
		return port;
	}
	/**
	 * 
	 * @return REST APIs http. http or
	 *  */
	public String getHttp() {
		return http;
	}
	
	/**
	 * Runs once before regression. Sets the device properties from device.properties.
	 * Logs into boxilla and manages any devices that are defined in device.properties. 
	 * 
	 * @param runFullSetUp taken from the textng.xml and if true will populate the boxilla 
	 * database with simulated users, connections and devices
	 * @throws InterruptedException 
	 */
	@BeforeSuite(alwaysRun = true)
	@Parameters({ "emerald", "emeraldse" })
	public void beforeSuite(String emerald, String emeraldse) throws InterruptedException {
		log.info("In before Suite");
		getBoxillaVersion();
		log.info("Got boxilla version");
		if(emerald.equals("true")) {
			log.info("********** Build is Emerald 4k ****************");
			isEmerald = true;
		}else {
			isEmerald = false;
		}
		if(emeraldse.equals("true")) {
			isEmeraldSe = true;
			log.info("********** Build is Emerald SE ****************");
		}else {
			isEmeraldSe = false;
		}
		System.out.println("Emerald is " + emerald);
		System.out.println("************************************************************************************");

		System.out.println("*****************	BEFORE CLASS            ********************************");
		System.out.println("************************************************************************************");
		loadProperties("test.properties");
		getDevices();
		
		//create a directory for failed test gifs
//		File dir = new File(gifLocation);
//		if(!dir.exists()) {
//			dir.mkdir();
//		}else {
//			File[] files = dir.listFiles();
//			for(File f: files) {
//				f.delete();
//			}
//		}
//
		try {	
			log.info("Attempting to reset DB and manage devices");
			cleanUpLogin();
			log.info("Logged in");
			SystemMethods sys = new SystemMethods();
			sys.dbReset(driver);
			sys.enableNorthboundAPI(driver);
			changeRestLogin();
			log.info("Rechromeset DB");
			//addLicense();
			deviceManageTestPrep2();
			log.info("Managed devices");
		}catch(Exception e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_beforeClass", "Before Class");
			e.printStackTrace();
			cleanUpLogout();
		}
		cleanUpLogout();

		
		if (shouldManageExtraDevices) {
			getApplianceVersion(shRx.getIpAddress());
			getApplianceVersion(shTx.getIpAddress());
			getApplianceVersion(dhRx.getIpAddress());
			getApplianceVersion(dhTx.getIpAddress());
			getApplianceVersion(shrx1.getIpAddress());
			getApplianceVersion(shrx2.getIpAddress());
		}
		
		//print appliance versions before suite kicks off
		getApplianceVersion(txIp);
		getApplianceVersion(rxIp);
		getApplianceVersion(txIpDual);
		getApplianceVersion(rxIpDual);
		


		startTime = System.currentTimeMillis();
		Thread.sleep(120000);
	}
	/**
	 * This will change Boxillas Northbound REST API 
	 *  login details from default to whatever is set in the 
	 *  properties file 
	 * @throws InterruptedException
	 */
	private void changeRestLogin() throws InterruptedException {
		
		try {
		Thread.sleep(1000);
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		EditRestUsernameConfig config = new EditRestUsernameConfig();
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = prop.getProperty("boxillaRestUsername");
		username.new_username = prop.getProperty("newRestUser");
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(username.username, prop.getProperty("boxillaRestPassword")).headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
	
		
		 EditRestPasswordConfig passConfig = new EditRestPasswordConfig();
		 String newPassword = prop.getProperty("newRestPassword");
			EditRestPasswordConfig.ChangePassword pass = passConfig.new ChangePassword();
			pass.username = boxillaRestUser;
			pass.new_password = newPassword;
			
			log.info("Editing password to " + pass.new_password);
			response = given().auth().preemptive().basic(boxillaRestUser, prop.getProperty("boxillaRestPassword")).headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(pass)
			.put(passConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated REST password.")).extract().response();
			
			SaveResponseStatistics(passConfig.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
		}catch(Exception e) {
			log.info("Error changing rest ");
		}
			
	}
	/**
	 * This will add a 300 license to Boxilla
	 */
	public void addLicense() {
		try {
		SystemMethods system = new SystemMethods();
		system.navigateToLicense(driver); 
		SystemAll.addLicenseBtn(driver).click();
		log.info("Add License Button Clicked");
		system.timer(driver);
		// String licenseValue = "50";
		String fileName = "C:\\Selenium\\Licenses\\" + boxillaManager + "\\licenseKey_" + "300" + "users.lic";
		log.info("license file:" + fileName);
		SystemAll.licenseUploadElement(driver)
				.sendKeys(fileName);
		log.info("License file selected to upload");
		system.timer(driver);
		SystemAll.btnSubmit(driver).click();
		log.info("Clicked on Submit button");
		log.info("Adding 300 limit license for testing");
		Thread.sleep(30000);
		} catch (InterruptedException e) {
			log.info("Issue with uploading license.. Continuining tests");
		}
	
	}
	
	/**
	 * Runs once after regression has finished. Will use boxilla and unmanage any devices
	 * defined in device.properties. Will also set the Northbound REST API login 
	 * back to default
	 * @throws InterruptedException 
	 * @throws IOException 
	 */
	@AfterSuite(alwaysRun = true)
	public void afterSuite() throws InterruptedException, IOException {
		
		ResetRestLogin();
		resetUnmangeApi(boxillaManager);
		long totalTime = System.currentTimeMillis();
		System.out.println("After suite");		
		String runFullSetUp = "false";
		
//		boxillaRestUsername=REST_BbAdminUser
//				boxillaRestPassword=Boxill@2020
//				newRestUser=automationUser
//				newRestPassword=automationPass
		
//		try {
//		EditRestUsernameConfig config = new EditRestUsernameConfig();
//		EditRestUsernameConfig.Username username = config.new Username();
//		username.username = "automationUser";
//		username.new_username = "REST_BbAdminUser";
//		log.info("Editing username to " + username.new_username);
//		given().auth().preemptive().basic(username.username, "automationPass").headers(BoxillaHeaders.getBoxillaHeaders())
//		.when().contentType(ContentType.JSON)
//		.body(username)
//		.put(config.getUri(boxillaManager))
//		.then().assertThat().statusCode(200)
//		.body("message", equalTo("Successfully updated REST username."));
//	
//		
//		 EditRestPasswordConfig passConfig = new EditRestPasswordConfig();
//		 String newPassword = "Boxill@2020";
//			EditRestPasswordConfig.ChangePassword pass = passConfig.new ChangePassword();
//			pass.username = "REST_BbAdminUser";
//			pass.new_password = newPassword;
//			
//			log.info("Editing password to " + pass.new_password);
//			given().auth().preemptive().basic("REST_BbAdminUser", "automationPass").headers(BoxillaHeaders.getBoxillaHeaders())
//			.when().contentType(ContentType.JSON)
//			.body(pass)
//			.put(passConfig.getUri(boxillaManager))
//			.then().assertThat().statusCode(200)
//			.body("message", equalTo("Successfully updated REST password."));
//		}catch(Exception e) {
//			log.info("Error changing rest user / password ");
//		}
		
		
		DevicesMethods deviceMethods = new DevicesMethods();
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();
		try {
			cleanUpLogin();
			
			deviceMethods.unManageDevice(driver, txIp);
//			discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txEmerald.getMac(),txIp);
			deviceMethods.unManageDevice(driver, rxIp);
//			discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,rxEmerald.getMac(),rxIp);
			deviceMethods.unManageDevice(driver, txIpDual);
//			discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txDual.getMac(),txIpDual);
			deviceMethods.unManageDevice(driver, rxIpDual);
//			discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,rxDual.getMac(),rxIpDual);
			
			if(shouldManageExtraDevices) {
				deviceMethods.unManageDevice(driver, shTx.getIpAddress());
//				discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txEmerald.getMac(),shTx.getIpAddress());
				deviceMethods.unManageDevice(driver, shRx.getIpAddress());
//				discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txEmerald.getMac(),shRx.getIpAddress());
				deviceMethods.unManageDevice(driver, dhTx.getIpAddress());
//				discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txEmerald.getMac(),dhTx.getIpAddress());
				deviceMethods.unManageDevice(driver, dhRx.getIpAddress());
//				discoveryMethods.unmanageDevice1(driver,boxillaManager, boxillaRestUser1, boxillaRestPassword1,txEmerald.getMac(),dhRx.getIpAddress());
			}
		}catch(Exception | AssertionError e) {
			Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
			log.info("Error unmanaging devices.");
			cleanUpLogout();
		}
		cleanUpLogout();

		PrintRestStatistics();
		printSuitetDetails(true);
	
		
		
		
		
		if(runFullSetUp.equals("true")) {
			try {
				tearDownForSoak();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}

	
	/**
	 * Login method used for setup work in test classes. Will open a firefox browser 
	 * and log into boxilla. The instance of webdriver can then be used to perform any 
	 * test class specific setup.
	 */
	public void cleanUpLogin() {
		try {
		String url = "https://" + boxillaManager + "/";
			System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content
//		System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Webdrivers\\chromedriver.exe");
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
//		driver.get(url);
//			
//		System.setProperty("webdriver.ie.driver", "C:\\Selenium\\Webdrivers\\IEDriverServer.exe");
//		driver = new InternetExplorerDriver();
//		driver.manage().window().maximize();
//		driver.get(url);
//		driver.navigate().to("javascript:document.getElementById('overridelink').click()");
			
//			FirefoxProfile firefoxProfile = new FirefoxProfile();
//			firefoxProfile.setPreference("browser.download.folderList",2);
//			firefoxProfile.setPreference("browser.tabs.remote.autostart.2", false);
//			firefoxProfile.setPreference("browser.download.manager.showWhenStarting",false);
//			firefoxProfile.setPreference("browser.helperApps.alwaysAsk.force", false);
//			firefoxProfile.setPreference("browser.helperApps.neverAsk.saveToDisk","application/octet-stream");
//			
//			firefoxProfile.setPreference("browser.download.dir","C:\\temp");
//			caps.setCapability(FirefoxDriver.PROFILE, firefoxProfile);
		if(browser.equals("firefox")) {
			driver = new FirefoxDriver(caps);
//			driver.manage().window().maximize();
			driver.manage().window().maximize();
			driver.get(url);
			}else if(browser.equals("chrome")) {
				System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Webdrivers\\chromedriver.exe");
				driver = new ChromeDriver();
				
				driver.manage().window().maximize();
				driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
				driver.get(url);
			}else if(browser.equals("edge")) {
				driver = new EdgeDriver();
				driver.manage().window().maximize();
				driver.get(url);
				WebElement advnacedButton = driver.findElement(By.id("details-button"));
				advnacedButton.click();
				//Thread.sleep(1000);
				WebElement proceed = driver.findElement(By.xpath("//a[contains(.,'Continue to')]"));
				proceed.click();
			}
			try {
				Thread.sleep(2000);
				Loginpage.username(driver).sendKeys(userName);

				Thread.sleep(2000);
				Loginpage.password(driver).sendKeys(password);

				Thread.sleep(2000);
				Loginpage.loginbtn(driver).click();
				
				
				driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
			} catch (Exception e) {
				System.out.println("Error in Clean up login method");
//				driver.quit();
				driver.close();
			}
		}
		catch (Exception e) {
			System.out.println("Error in login method");
//			driver.quit();
			driver.close();
		}
	}
	public void cleanUpLogin(String boxilla2) {
		String url = "https://" + boxilla2 + "/";
		System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
		DesiredCapabilities caps = new DesiredCapabilities();
		caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content

			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.get(url);
			
			try {
				
				Thread.sleep(2000);
				Loginpage.username(driver).sendKeys(userName);

				Thread.sleep(2000);
				Loginpage.password(driver).sendKeys(password);

				Thread.sleep(2000);
				Loginpage.loginbtn(driver).click();
				
				
				driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
				
			} catch (Exception e) {
				System.out.println("Error in Clean up login method");
				e.printStackTrace();
//				driver.quit();
			}
	}
	/**
	 * Logout method that should be used after used  cleanUpLogin() to logout of boxilla
	 */
	public void cleanUpLogout() {
		try {
			Thread.sleep(1000);
			driver.get(url);
			Thread.sleep(2000);
			Landingpage.logoutDropdown(driver).click();
			Thread.sleep(2000);
			Actions act=new Actions(driver);
			act.moveToElement(Landingpage.logoutbtn(driver)).click().build().perform();
//			Landingpage.logoutbtn(driver).click();
			Thread.sleep(2000);
//			driver.quit();
			driver.close();
			
		
			
		} catch (Exception e) {
			// TODO: handle exception
//			driver.quit();
			driver.close();
		}
	}
	
	/**
	 * Will run before each test case unless overridden. 
	 * Opens a browser and logs into boxilla. Browser depends on what is defined in the testng.xml
	 * 
	 * @param browser - passed in from testng.xml
	 * @param method - this is injected into the method by testng. Used to access the test name
	 * @throws InterruptedException
	 * 
	 */
	@BeforeMethod(alwaysRun = true)
	@Parameters({ "browser" })
	public void login(String browser, Method method) throws InterruptedException {
		log.info("Starting login method");
		url = "https://" + boxillaManager + "/";
		splitTime = System.currentTimeMillis();
		// Select driver based on the Browser parameter selected
		//String url = "https://" + boxillaManager + "/";
		try {
		if (browser.equalsIgnoreCase("firefox")) {
			log.info("driver is firefox");
			/* *************************** Firefox Driver ********************************* */
			System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content
			
			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.get(url);
		} else if (browser.equalsIgnoreCase("chrome")) {
			/* **************************** Chrome Driver ********************************* */
			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Webdrivers\\chromedriver.exe");
			driver = new ChromeDriver();
			driver.manage().window().maximize();
			driver.get(url);
		} else if (browser.equalsIgnoreCase("ie")) {
			// ******************************* IE Driver
			// ************************************
			System.setProperty("webdriver.ie.driver", "C:\\Selenium\\Webdrivers\\IEDriverServer.exe");
			driver = new InternetExplorerDriver();
			driver.manage().window().maximize();
			driver.get(url);
			driver.navigate().to("javascript:document.getElementById('overridelink').click()");
		} else if(browser.equals("edge")) {
			driver = new EdgeDriver();
			driver.manage().window().maximize();
			driver.get(url);
			WebElement advnacedButton = driver.findElement(By.id("details-button"));
			advnacedButton.click();
			//Thread.sleep(1000);
			WebElement proceed = driver.findElement(By.xpath("//a[contains(.,'Continue to')]"));
			proceed.click();
		}
		}catch(Exception e) {
			e.printStackTrace();
			log.info("Error starting webdriver");
			if(retry < 1) {
				Utilities.captureScreenShot(driver, "LOGIN ERROR"+retry, "lOGIN ERROR"+retry);
				log.info("Retrying");
				retry++;
//				driver.quit();
				driver.close();
				login(browser, method);
		}
		}
		try {
		
			printTestDetails("STARTING ", method.getName(), "");
			Loginpage.username(driver).sendKeys(userName);

			Loginpage.password(driver).sendKeys(password);

			Loginpage.loginbtn(driver).click();
			
			//set timeout for webdriver
			int timeout = Integer.parseInt(prop.getProperty("waitTime"));
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.dashboard(driver)));
			Assert.assertTrue(Landingpage.dashboard(driver).isDisplayed(), "Dashboard is not displayed. Error logging in");
		} catch (Exception | AssertionError e) {
			log.info("Error in Login method.");
//			driver.quit();
			driver.close();
			if(retry < 1) {
				log.info("Retrying");
				Utilities.captureScreenShot(driver, "LOGIN ERROR", "lOGIN ERROR_RETRY");
				retry++;
				login(browser, method);
			}else {
				Assert.assertTrue(false, "Dashboard is not displayed. Error logging in");
				Utilities.captureScreenShot(driver, "LOGIN ERROR", "lOGIN ERROR");
			}
		}

	}
	
//	public void clearGif() {
//		log.info("Clearing gif screenshots");
//		SeleniumActions.screenshotList.clear();
//		SeleniumActions.screenShotcounter = 0;
//		String dir = "./test-output/Screenshots/";
//		File folder = new File(dir);
//		File[] files = folder.listFiles();
//		for(File f : files) {
//			if(f.getAbsolutePath().contains("giffy")) {
//				f.delete();
//			}
//		}
//	}

	/**
	 * Runs after each test unless overridden. Gets the results of each test and logs it. Also captures a screen shot 
	 * if a test has failed. Logs out of boxilla and closes the webdriver
	 * @param result - injected into method by testng. Used to get the test result.
	 * @throws InterruptedException
	 */
	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) throws InterruptedException {
		log.info("In log out method");
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		String results = "";
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			results = "FAIL";
		
		if(ITestResult.SKIP == result.getStatus())
			results = "SKIP";
		
		if(ITestResult.SUCCESS == result.getStatus())
			results = "PASS";
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			Throwable failReason = result.getThrowable();
			log.info("FAIL REASON:" + failReason.toString());
			String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			Utilities.captureScreenShot(driver, screenShotName, result.getName());
			try {
				String gifName = "";
				if(ITestResult.SKIP == result.getStatus()) {
					gifName = result.getName() + "_skip";
				}else {
					gifName = result.getName() + "_fail";
				}
				 List<JavaScriptError> jsErrors = JavaScriptError.readErrors(driver); 
				 log.info("************* JAVA SCRIPT ERRORS **************");
				for(JavaScriptError e : jsErrors) {
					log.info(e.getErrorMessage() + "Line Number:" + e.getLineNumber());
					
				}
				log.debug("*************** END JAVA SRIPT ERRORS *************");
				
//				GifSequenceWriter.createGif(SeleniumActions.screenshotList, gifName);
//				clearGif();
//			Utilities.captureLog(boxillaManager, boxillaUsername, boxillaPassword,
//					 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
			}catch(Exception e) {
				System.out.println("Error when trying to capture log file. Catching error and continuing");
				e.printStackTrace();
			}
			
			//collectLogs(result);
		}
		try {

			driver.get(url);

			Landingpage.logoutDropdown(driver).click();

			Landingpage.logoutbtn(driver).click();
//			driver.quit();
			driver.close();
			long endTime = System.currentTimeMillis();
			long duration = endTime - startTime;
			//System.out.println("Regression running for : " + getTimeFromMilliSeconds(duration));
			long singleTestTime = endTime - splitTime;
			System.out.println(result.getName() + " took : " + getTimeFromMilliSeconds(singleTestTime));
			
		} catch (Exception e) {
			// TODO: handle exception
//			driver.quit();
			driver.close();
		}
		printTestDetails("FINISHING", result.getName(), results);
		System.out.println("Tests Completed:" + ++testCounter);
	}

	/**
	 * Will load the property file into memory for use in test cases
	 */
	public void loadProperties(String propertyFile) {
		log.info("Loading properties");
		try {
			InputStream in = new FileInputStream(propertyFile);
			prop.load(in);
			in.close();
			log.info("Properties loaded successfully");
		} catch (IOException e) {
			System.out.println("Properties file failed to load");
		}
	}
	
	public void loadExtraDeviceProperties() {
		log.info("Loading extra device properties.");
		try {
			InputStream in = new FileInputStream("deviceExtra.properties");
			extraDevProp.load(in);
			in.close();
			log.info("Properties loaded successfully");
		} catch (IOException e) {
			System.out.println("Properties file failed to load");
		}
	}

	/**
	 * 
	 * @return the property file object
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * sets login user name for boxilla
	 * 
	 * @param userName - the user name to log into boxilla with
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * returns login username for boxilla
	 * @return boxilla user name for login
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * sets login password for boxilla
	 * 
	 * @param password boxilla password that matches username
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 *
	 * @return login password for boxilla
	 */
	public String getPassword() {
		return password;
	}

	/**
	 * Utility method to return time in mm:ss from milliseconds
	 * 
	 * @param time the time in milliseconds
	 * @return String of time in mm:ss format
	 */
	public String getTimeFromMilliSeconds(long time) {
		return new SimpleDateFormat("mm:ss").format(new Date(time));
		
	}
	
	/**
	 * Utility method to return the current date and time in either 
	 * yyyyMMdd or hhmmss format
	 * 
	 * @param time if true returns current date in hhmmss else yyyyMMdd
	 * @return current date and time in yyyyMMdd or hhmmss format
	 */
	public String getDateAndTime(boolean time) {
		DateFormat dateFormat = null;
		if(!time) {
			dateFormat = new SimpleDateFormat("yyyyMMdd");
		}else
		{
			dateFormat = new SimpleDateFormat("hhmmss");
		}
		Date date = new Date();
		return (dateFormat.format(date)); //2016/11/16 12:08:43
	}
	

	/**
	 * Will load a number of users, connections and devices into 
	 * the boxilla database so that tests may be run with different numbers
	 * 
	 * @param number the amount of users and connections to create
	 * @throws InterruptedException
	 */
	private void setUpForSoak(String number) throws InterruptedException {
		int amount = Integer.parseInt(number);
		number = Integer.toHexString(amount);
		Ssh shell = new Ssh(boxillaUsername, boxillaPassword, boxillaManager );
		shell.loginToServer();
		String output = shell.sendCommand("initctl start cloudium-example-licensetest BXAMGR_USERS=" + number + " BXAMGR_CONNECTIONS=" + 
		number + " BXAMGR_DEVICES=" + number);		//create 2000 
		System.out.println("Output: " + output);
		Thread.sleep(240000);				//wait for script to complete
		shell.disconnect();
		
		shell.loginToServer();
		String stopOutput = shell.sendCommand("initctl stop cloudium-example-licensetest");					//stop the service
		System.out.println("Output: " + stopOutput);
		shell.disconnect();
		System.out.println("database created...");
		Thread.sleep(60000);
	}
	
	/**
	 * will delete all the users, connections and devices created by setUpForSoak
	 * 
	 * @throws InterruptedException
	 */
	private void tearDownForSoak() throws InterruptedException {
		Ssh shell = new Ssh(boxillaUsername, boxillaPassword, boxillaManager );
		shell.loginToServer();
		String output = shell.sendCommand("initctl start cloudium-example-licensetest");		//delete everything
		System.out.println("Output: " + output);
		shell.disconnect();
		Thread.sleep(240000);				//wait for script to complete
		
		shell.loginToServer();
		String stopOutput = shell.sendCommand("initctl stop cloudium-example-licensetest");					//stop the service 
		System.out.println("Output: " + stopOutput);
		shell.disconnect(); 
	}
	
	/**
	 * 
	 * @return the selenuim wait time
	 */
	public static int getWaitTime() {
		return waitTime;
	}
	
	/**
	 * Utility method to print out the test suite details. Prints for beginning and end of test
	 * @param end if true print out is for the end of test, else for the beginning
	 */
	public void printSuitetDetails(boolean end) {
		String text = "";
		if(end) {
			text = "FINISHING";
		}else {
			text = "STARTING";
		}
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
		log.info("***************************************************************************************");
		log.info("*                                                                                     *");
		log.info("                         " + text + " SUITE " + this.getClass().getSimpleName());
		log.info("*                                                                                     *");
		log.info("***************************************************************************************");
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
	}

	/**
	 * Utility method used to bring the test details
	 * @param end 
	 * @param testName
	 * @param result
	 */
	public void printTestDetails(String end, String testName, String result) {
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
		log.info("***************************************************************************************");
		log.info("                         " + end + " TEST " + testName + ":" + result);
		log.info("***************************************************************************************");
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
	}
	
	/**
	 * Runs before each test class. Prints details of the class to sets appliance details
	 * 
	 * @throws InterruptedException
	 */
	@BeforeClass
	public void beforeClass() throws InterruptedException {
		printSuitetDetails(false);
		getDevices();
	}

	
	public void collectLogs(ITestResult result) {
		try {
			Utilities.captureDeviceLog(rxIp, result.getName());
		}catch(Exception e) {
			log.info("Error capturing device log." + rxIp);
		}
		try {
			Utilities.captureDeviceLog(txIp, result.getName());
		}catch(Exception e) {
			log.info("Error capturing device log." + txIp);
		}
		try {
			//Utilities.captureLog(boxillaManager, boxillaUsername, boxillaPassword,
			//		 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
			}catch(Exception e) {
				System.out.println("Error when trying to capture log file. Catching error and continuing");
				e.printStackTrace();
			}
	}

	/**
	 * Runs after each test class. Prints details of the class and the sleeps so
	 * the appliances are ready for the next test class
	 * 
	 * @throws InterruptedException
	 */
	@AfterClass
	public void afterClass() throws InterruptedException {
		printSuitetDetails(true);
		try {
		rebootDevices(rxIp, txIp);
		}catch(Exception e) {
			e.printStackTrace();
			log.info("Error rebooting. Continuing...");
		}
		
		
//		Database db = new Database();
//		db.connectToDatabase("10.211.129.3", "postgres", "foreman", "foreman");
//		HashMap map = db.getAllTableCount(db.getAllTableNames());
//		
//		Database db2 = new Database();
//		db2.connectToDatabase("10.211.128.147", "postgres", "foreman", "foreman");
//		HashMap map2 = db2.getAllTableCount(db2.getAllTableNames());
//		
//		System.out.println("DATABASE MATCH:" + map.equals(map2));
//		
		
	}
/**
 * This will change the Northbound REST API login back to default values	
 */
private void ResetRestLogin() {
		
		RestAssured.useRelaxedHTTPSValidation();
		RestAssured.enableLoggingOfRequestAndResponseIfValidationFails();
		
		EditRestUsernameConfig config = new EditRestUsernameConfig();
		EditRestUsernameConfig.Username username = config.new Username();
		username.username = "automationUser";
		username.new_username = "REST_BbAdminUser";
		log.info("Editing username to " + username.new_username);
		Response response = given().auth().preemptive().basic(username.username, "automationPass").headers(BoxillaHeaders.getBoxillaHeaders())
		.when().contentType(ContentType.JSON)
		.body(username)
		.put(config.getUri(boxillaManager))
		.then().assertThat().statusCode(200)
		.body("message", equalTo("Successfully updated REST username.")).extract().response();
		
		SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.PUT, response);

	
		
		 EditRestPasswordConfig passConfig = new EditRestPasswordConfig();
		 String newPassword = prop.getProperty("newRestPassword");
			EditRestPasswordConfig.ChangePassword pass = passConfig.new ChangePassword();
			pass.username = "REST_BbAdminUser";
			pass.new_password = "Boxill@2020";
			
			//log.info("Editing password to " + pass.new_password);
			response = given().auth().preemptive().basic("REST_BbAdminUser", "automationPass").headers(BoxillaHeaders.getBoxillaHeaders())
			.when().contentType(ContentType.JSON)
			.body(pass)
			.put(passConfig.getUri(boxillaManager))
			.then().assertThat().statusCode(200)
			.body("message", equalTo("Successfully updated REST password.")).extract().response();
			
			SaveResponseStatistics(passConfig.getUri(boxillaManager), REQUEST_TYPE.PUT, response);
			
	}
	/**
	 * This method will discover / edit / manage all the devices in the 
	 * device.properties file for use in regression 
	 */
	public void deviceManageTestPrep2() {
		log.info("Attempting to manage devices for regression");
		
		driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
		
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();	
		
		if(shouldManageExtraDevices) {
			if (shTx!=null)
				discoveryMethods.addDeviceToBoxilla(driver, shTx.getMac(), shTx.getIpAddress(),
						shTx.getGateway(), shTx.getNetmask(), shTx.getDeviceName(), 10);
			if (shRx!=null)
				discoveryMethods.addDeviceToBoxilla(driver, shRx.getMac(), shRx.getIpAddress(),
						shRx.getGateway(), shRx.getNetmask(), shRx.getDeviceName(), 10);
			if (dhTx!=null)
				discoveryMethods.addDeviceToBoxilla(driver, dhTx.getMac(), dhTx.getIpAddress(),
						dhTx.getGateway(), dhTx.getNetmask(), dhTx.getDeviceName(), 10);
			if (dhRx!=null)
				discoveryMethods.addDeviceToBoxilla(driver, dhRx.getMac(), dhRx.getIpAddress(),
						dhRx.getGateway(), dhRx.getNetmask(), dhRx.getDeviceName(), 10);
			if (shrx1!=null)
				discoveryMethods.addDeviceToBoxilla(driver, shrx1.getMac(), shrx1.getIpAddress(),
						shrx1.getGateway(), shrx1.getNetmask(), shrx1.getDeviceName(), 10);
			if (shrx2!=null)
				discoveryMethods.addDeviceToBoxilla(driver, shrx2.getMac(), shrx2.getIpAddress(),
						shrx2.getGateway(), shrx2.getNetmask(), shrx2.getDeviceName(), 10);
		}
		
		
//		discoveryMethods.addDeviceToBoxilla(driver, txSingleExtra.getMac(), txSingleExtra.getIpAddress(),
//				"10.231.128.1", "255.255.248.0", txSingleExtra.getDeviceName(), 10);
		
		discoveryMethods.addDeviceToBoxilla(driver, rxEmerald.getMac(), rxEmerald.getIpAddress(),
				"10.231.128.1", "255.255.248.0", rxEmerald.getDeviceName(), 10);
		
//		discoveryMethods.addDeviceToBoxilla1(driver, rxEmerald.getMac(), rxEmerald.getIpAddress(),
//				"10.231.128.1", "255.255.248.0", rxEmerald.getDeviceName(), 10,boxillaManager,boxillaRestUser,boxillaRestPassword);
//		
		discoveryMethods.addDeviceToBoxilla(driver, txEmerald.getMac(), txEmerald.getIpAddress(),
				"10.231.128.1", "255.255.248.0", txEmerald.getDeviceName(), 10);
//		discoveryMethods.addDeviceToBoxilla1(driver, txEmerald.getMac(), txEmerald.getIpAddress(),
//				"10.231.128.1", "255.255.248.0", txEmerald.getDeviceName(), 10,boxillaManager,boxillaRestUser,boxillaRestPassword);
		
		discoveryMethods.addDeviceToBoxilla(driver, rxDual.getMac(), rxDual.getIpAddress(),
				"10.231.128.1", "255.255.248.0", rxDual.getDeviceName(), 10);
//		discoveryMethods.addDeviceToBoxilla1(driver, rxDual.getMac(), rxDual.getIpAddress(),
//				"10.231.128.1", "255.255.248.0", rxDual.getDeviceName(), 10,boxillaManager,boxillaRestUser,boxillaRestPassword);
		
		discoveryMethods.addDeviceToBoxilla(driver, txDual.getMac(), txDual.getIpAddress(),
				"10.231.128.1", "255.255.248.0", txDual.getDeviceName(), 10);
//		discoveryMethods.addDeviceToBoxilla1(driver, txDual.getMac(), txDual.getIpAddress(),
//				"10.231.128.1", "255.255.248.0", txDual.getDeviceName(), 10,boxillaManager,boxillaRestUser,boxillaRestPassword);

		log.info("Appliances managed successfully");
		driver.manage().timeouts().implicitlyWait(waitTime, TimeUnit.SECONDS);
	}

	/**
	 * Utility method to manage devices in boxilla. used in beforeSuite
	 * 
	 * @throws InterruptedException
	 */
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
		
		if(!isEmerald) {
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxSingle.getMac(), prop.getProperty("ipCheck"),
				rxIp, rxSingle.getGateway(),rxSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxSingle.getDeviceName(), rxSingle.getMac(),
				prop.getProperty("ipCheck"));
		
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txSingle.getMac(), prop.getProperty("ipCheck"),
				txIp, txSingle.getGateway(), txSingle.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txSingle.getDeviceName(), txSingle.getMac(), 
				prop.getProperty("ipCheck"));
		}
		//RX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxDual.getMac(), prop.getProperty("ipCheck"),
				rxIpDual, rxDual.getGateway(), rxDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxDual.getDeviceName(),rxDual.getMac(),
				prop.getProperty("ipCheck"));
				
		//TX
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txDual.getMac(), prop.getProperty("ipCheck"),
				txIpDual,txDual.getGateway(), txDual.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txDual.getDeviceName(), txDual.getMac(),
				prop.getProperty("ipCheck"));
		
		
		if(isEmerald) {
			
	
			
		System.out.println("Emerald devices are being managed");
		System.out.println(txEmerald.getMac() + prop.getProperty("ipCheck") + txEmerald.getIpAddress()
		+ txEmerald.getGateway() + txEmerald.getNetmask());
		//emerald
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, txEmerald.getMac(), prop.getProperty("ipCheck"),
				txEmerald.getIpAddress(),txEmerald.getGateway(), txEmerald.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, txEmerald.getDeviceName(), txEmerald.getMac(),
				prop.getProperty("ipCheck"));
		
		discoveryMethods.discoverDevices(driver);
		discoveryMethods.stateAndIPcheck(driver, rxEmerald.getMac(), prop.getProperty("ipCheck"),
				rxEmerald.getIpAddress(),rxEmerald.getGateway(), rxEmerald.getNetmask());
		discoveryMethods.manageApplianceAutomatic(driver, rxEmerald.getDeviceName(), rxEmerald.getMac(),
				prop.getProperty("ipCheck"));
		}
				
		
		log.info("Appliances Managed Successfully");
	}
	
	public void waitForBoxillaPoll() {
		try {
			log.info("Waiting for boxillas next poll before continuing");
			Thread.sleep(65000);
		}catch(Exception e) {
			
		}
	}
	public void getDevices() {
		 restuser = prop.getProperty("rest_user");
		 restPassword = prop.getProperty("rest_password");
		if(isEmerald) {
			System.out.println("************* EMERALD BUILD **************");
		}else {
			log.info("**************** InvisaPC Build ****************");
		}
		devicePool.getAllDevices();
		txSingle = devicePool.getTxSingle();
//		txSingleExtra=devicePool.getTxSingleExtra();
		rxSingle = devicePool.getRxSingle();
		txDual = devicePool.getTxDual();
		rxDual = devicePool.getRxDual();
		txEmerald = devicePool.getTxEmerald();
		rxEmerald = devicePool.getRxEmerald();
		dualTxName = txDual.getDeviceName();
		dualRxName = rxDual.getDeviceName();
		
		
		if(!isEmerald) {
			txIp = txSingle.getIpAddress();
//			txIpExtra=txSingleExtra.getIpAddress();
			rxIp = rxSingle.getIpAddress();
			singleTxName = txSingle.getDeviceName();
			singleRxName = rxSingle.getDeviceName();
//			sh=shTx.getIpAddress();
//			dh=dhTx.getIpAddress();
			
			
		}else {
			rxIp = rxEmerald.getIpAddress();
			txIp = txEmerald.getIpAddress();
//			txIpExtra=txSingleExtra.getIpAddress();
			singleTxName = txEmerald.getDeviceName();
			singleRxName = rxEmerald.getDeviceName();
			txSingle = txEmerald;
			rxSingle = rxEmerald;
		}
		txIpDual = txDual.getIpAddress();
		rxIpDual = rxDual.getIpAddress();
		
//		sh=shTx.getIpAddress();
//		dh=dhTx.getIpAddress();
		
		System.out.println("txSingle:" + txSingle.toString());
		System.out.println("rxSingle:" + rxSingle.toString());
		System.out.println("txDual:" + txDual.toString());
		System.out.println("rxDual:" + rxDual.toString());
	}
	/**
	 * THis will SSH into Boxilla and print out the current software version
	 */
	private void getBoxillaVersion() {
		Ssh ssh = new Ssh(boxillaUsername, boxillaPassword, boxillaManager);
		ssh.loginToServer();
		boxillaBuild = ssh.sendCommand("cat /usr/share/moy/db/svnNo");
		ssh.disconnect();
		log.info("BOXILLA BUILD: " + boxillaBuild);
	}
	/**
	 * This will SSH into an appliance and print out the currect software version
	 * @param deviceIp - The IP address of the appliance to get the version from
	 */
	private void getApplianceVersion(String deviceIp) {
		Ssh ssh = new Ssh(deviceUserName, devicePassword, deviceIp);
		ssh.loginToServer();
		String applianceBuild = ssh.sendCommand("cat /VERSION");
		log.info("removing all logs before starting tests");
		ssh.sendCommand("rm /usr/local/syslog.log*");
		ssh.disconnect();
		log.info("Appliance build: " + applianceBuild);
	}
	public void resetUnmangeApi(String boxillaIp) throws InterruptedException, IOException 
	{
		Ssh ssh = new Ssh(deviceUserName, devicePassword, boxillaIp);
		ssh.loginToServer();
		Thread.sleep(4000);
		ssh.sendCommand("cd /root/automation/");
		Thread.sleep(4000);
		String comand=ssh.sendCommand("./automationInstall.sh");
		log.info("Store command"+ comand);
		Thread.sleep(4000);
		log.info("Api Reset Successfully");
	
	}
	
	public String getBuild(String build) {
		String[] splitString = build.split("_");
		String x = splitString[0].replace(".","");
		String y = x.replace("V","");
		System.out.println(y);
		return y;
	}
	
	/**
	 * THis will reboot all appliances that are managed by boxilla
	 * @param ipaddress
	 * @throws InterruptedException
	 */
	protected void rebootDevices(String ...ipaddress) throws InterruptedException {
		log.info("Rebooting devices for next test");
		for(String ip : ipaddress) {
			Ssh ssh = new Ssh(deviceUserName, devicePassword, ip);
			ssh.loginToServer();
			String command = "reboot";
			if(isEmerald)
				command = "/sbin/reboot";
			ssh.sendCommand(command);
			ssh.disconnect();
		}
		Thread.sleep(90000);
		
	}
	/**
	 * This will ping an ip address and return true if it 
	 * is alive
	 * @param ip - IP address to test
	 * @return true if IP is reachable else false
	 */
	public static boolean isIpReachable(String ip) {
		boolean reachable = false;
		InetAddress address;
		try {
			address = InetAddress.getByName(ip);
			reachable = address.isReachable(1000);
		}catch (UnknownHostException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();
		}
		return reachable;
	}

	/**
	 * Reads a csv file line by line and creates a multidimentional object 
	 * array. Passed to the data provider to run data driven tests
	 * @param dataLocation
	 * @return
	 * @throws IOException
	 */
	public Object[][] readData(String dataLocation) throws IOException {
		//get total number of lines in file first
		Path path = Paths.get(dataLocation);
		long numberOfLine = Files.lines(path).count();
		int intNumber = Math.toIntExact(numberOfLine);
		Object[][] obj = null;
		BufferedReader reader;
		try {
			reader = new BufferedReader(new FileReader(dataLocation));
			String line = reader.readLine();
			Object[] obj4 = line.split(",");
			obj = new Object[intNumber][obj4.length];
			int counter = 0;
			while(line !=null) {
				for(int j=0; j < obj4.length; j++) {
					obj[counter][j] = obj4[j];
				}
				counter++;
				line = reader.readLine();
				if(line != null)
					obj4 = line.split(",");
			}
		
		}catch(Exception e) {
			e.printStackTrace();
		}
		return obj;
	}
	
}
