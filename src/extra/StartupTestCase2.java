package extra;

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
import java.util.List;
import java.util.Properties;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Parameters;
import java.nio.file.Files;
import connection.AddConnection;
import io.restassured.http.Header;
import io.restassured.http.Headers;
import methods.DevicesMethods;
import methods.DiscoveryMethods;
import methods.HardwareMonitor;
import methods.SystemMethods;
import net.jsourcerer.webdriver.jserrorcollector.JavaScriptError;
import objects.Landingpage;
import objects.Loginpage;
import testNG.Utilities;

/**
 * Identical to StartupTestCase except the beforeMethod. Use this class when running tests locally
 * @author Brendan O'Regan
 *
 */

public class StartupTestCase2 extends RESTStatistics {

	private String lexMac = "";
	private String lex1Mac = "";
	private String rex1Mac = "";
	private String rex2Mac = "";
	private String lexIp = "";
	private String rex1Ip = "";
	private String rex2Ip = "";
	
	private static boolean is4kCon = false;
	public static boolean isManage = false;
	public static boolean isUnmanage = false;
	public static boolean isEmerald = false;
	protected String deviceUserName, devicePassword;
	public WebDriver driver;
	public String boxillaManager;
	public String boxillaVersion;
	public String boxillaVersionNew;
	public Properties prop = new Properties();
	protected String userName;
	public  String password;
	private static long startTime;
	private static long splitTime;
	private static ArrayList<String> initialSmartCtl;
	private static HardwareMonitor ctl;
	private static int testCounter;
	public static String url;
	static Files hardwareFile;
	final static Logger log = Logger.getLogger(StartupTestCase2.class);
	private static int waitTime;
	public String port;
	public String https;
	protected String http;
	public String rest_version;
	public String boxillaRestVersion;
	public String boxillaPort = prop.getProperty("boxillaPort");
	public Header head;
	public Header boxillaHead;
	public Header boxillaHead2;
	public String dbUser;
	public String dbName;
	public String dbPassword;
//	private boolean shouldManageExtraDevices;
	protected boolean shouldManageExtraDevices;
	public AppliancePool devicePool = new AppliancePool();
	public Device txSingle,txSingleExtra,rxSingle, txDual, rxDual, txEmerald, rxEmerald, shTx, dhTx, shRx, dhRx;
	
	//test properties
	public String boxillaRestPassword;
	public String boxillaRestUser;
	public String boxillaRestPassword1;
	public String boxillaRestUser1;
	protected String restuser;
	protected String restPassword;
	public String txIp;
	public String txIpExtra;
	public String sh;
	public String dh;
	public String rxIp;
	public String txIpDual;
	public String rxIpDual;
	public String txTemplateName = "TEST_TX";
	public String rxTemplateName = "TEST_RX";
	public String singleTxName;
	public String dualTxName;
	public String c;
	public String singleRxName;
	public String dualRxName;
	protected String txEmeraldName = "Test_TX_Emerald";
	protected String rxEmeraldName = "Test_RX_Emerald";
	public String deviceOldVersion = prop.getProperty("oldVersion");
	public String deviceMiddleVersion = prop.getProperty("middleVersion");
	public String deviceNewVersion = prop.getProperty("newVersion");
	public Properties extraDevProp = new Properties();
	protected String dellSwitchIp = "";
	protected String dellSwitchMac = "";
	protected String boxillaManager2 = "";
	protected String virtualIp = "";
	public String boxillaUsername = "";
	public String boxillaPassword = "";
	public static String gifLocation = "C:\\Test_Workstation\\SeleniumAutomation\\test-output\\gifs";
	public List<Header> listOfHeaders = new ArrayList<Header>();
	public Headers boxillaHeaders;
	protected int compression = 10;
	public StartupTestCase2() {
//		StartupTestCase t = new StartupTestCase();
		loadProperties("test.properties");
		
		lexMac = prop.getProperty("lexMac");
		lex1Mac = prop.getProperty("lex1Mac");
		rex1Mac = prop.getProperty("rex1Mac");
		rex2Mac = prop.getProperty("rex2Mac");
		compression = Integer.parseInt(prop.getProperty("compression"));
		lexIp = prop.getProperty("lexIp");
		rex1Ip = prop.getProperty("rex1Ip");
		rex2Ip = prop.getProperty("rex2Ip");
		
		virtualIp = prop.getProperty("vip");
		boxillaManager2 = prop.getProperty("boxillaManager2");
		boxillaVersion = prop.getProperty("boxillaVersion");
		boxillaVersionNew = prop.getProperty("boxillaVersionNew");
		dellSwitchMac = prop.getProperty("dellswitchmac");
		dellSwitchIp = prop.getProperty("dellswitchip");
		boxillaManager = prop.getProperty("boxillaManager");
		userName = prop.getProperty("userName");
		password = prop.getProperty("password");
		
		url = "https://" + boxillaManager + "/";
		ctl = new HardwareMonitor(prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"), boxillaManager);
		waitTime = Integer.parseInt(prop.getProperty("waitTime"));
		boxillaPort = prop.getProperty("boxillaPort");
		port = prop.getProperty("devicePort");
		https = prop.getProperty("http");
		boxillaRestVersion = prop.getProperty("boxillaRestVersion");
		rest_version = prop.getProperty("rest_version");
		boxillaHead = new Header("api-version", boxillaRestVersion);
		boxillaHead2 = new Header("Accept", "application/json");
		Header boxillaHead3 = new Header("Content-Type", "application/json");
		//listOfHeaders.add(boxillaHead);
		listOfHeaders.add(boxillaHead2);
		listOfHeaders.add(boxillaHead3);
		boxillaHeaders = new Headers(listOfHeaders);
		head = new Header("api-version", rest_version);
		dbUser = prop.getProperty("dbUser");
		dbName = prop.getProperty("dbName");
		dbPassword = prop.getProperty("dbPassword");
		deviceOldVersion = prop.getProperty("oldVersion");
		deviceMiddleVersion = prop.getProperty("middleVersion");
		deviceNewVersion = prop.getProperty("newVersion");
		deviceUserName = prop.getProperty("deviceUserName");
		devicePassword = prop.getProperty("devicePassword");
		boxillaUsername = prop.getProperty("boxillaUserName");
		boxillaPassword = prop.getProperty("boxillaPassword");
		boxillaRestPassword = prop.getProperty("boxillaRestPassword");
		boxillaRestUser = prop.getProperty("boxillaRestUsername");
		String temp =  prop.getProperty("shouldManageExtraDevices");
		shouldManageExtraDevices = temp.equalsIgnoreCase("true") ? true : false;
		txIp=prop.getProperty("txIP");
//		txIpExtra=prop.getProperty("txIPExtra");
		restuser = prop.getProperty("rest_user");
		restPassword = prop.getProperty("rest_password");
		if(shouldManageExtraDevices) 
		{
			loadExtraDeviceProperties();
			String commonNetmask = extraDevProp.getProperty("commonNetmask");
			String commonGateway = extraDevProp.getProperty("commonGateway");
			String [] shTxProp = extraDevProp.getProperty("shtx").split(",");
			String [] shRxProp = extraDevProp.getProperty("shrx").split(",");
			String [] dhTxProp = extraDevProp.getProperty("dhtx").split(",");
			String [] dhRxProp = extraDevProp.getProperty("dhrx").split(",");
//			String [] frRxProp = extraDevProp.getProperty("4krx").split(",");
//			String [] frRx1Prop = extraDevProp.getProperty("4krx1").split(",");

			try {
			    shTx = new Device(shTxProp[0], shTxProp[1], commonGateway, commonNetmask, true, shTxProp[2]);
				shRx = new Device(shRxProp[0], shRxProp[1], commonGateway, commonNetmask, true, shRxProp[2]);
				dhTx = new Device(dhTxProp[0], dhTxProp[1], commonGateway, commonNetmask, true, dhTxProp[2]);
				dhRx = new Device(dhRxProp[0], dhRxProp[1], commonGateway, commonNetmask, true, dhRxProp[2]);
//				fourKRx = new Device(dhRxProp[0], dhRxProp[1], commonGateway, commonNetmask, true, dhRxProp[2]);
//				fourKRx1 = new Device(dhRxProp[0], dhRxProp[1], commonGateway, commonNetmask, true, dhRxProp[2]);
			}catch(Exception ex) {
				log.info("Failed to instantiate extra devices. Excepption "+ex.getMessage());
				Exception some = ex;
				
			}
			
			
			}}
	
				
	
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
	
	public String getDbUser() {
		return dbUser;
	}
	public String getDbName() {
		return dbName;
	}
	public String getDbPassword() { 
		return dbPassword;
	}
	public Header getHead() {
		return head;
	}
	public String getRest_version() {
		return rest_version;
	}
	
	public String getPort() {
		return port;
	}
	public String getHttp() {
		return https;
	}
	
	@BeforeSuite(alwaysRun = true)
	//@Parameters({ "runFullSetUp" })
	public void beforeSuite() throws InterruptedException {
		
		System.out.println("before suite");
		SystemMethods sys = new SystemMethods();;
		getDevices();
		
		//create a directory for failed test gifs
		
		File dir = new File(gifLocation);
		if(!dir.exists()) {
			dir.mkdir();
		}else {
			File[] files = dir.listFiles();
			for(File f: files) {
				f.delete();
			}
		}
		
		if(isManage) {
			try {	
				cleanUpLogin();
				sys.dbReset(driver);
				//deviceManageTestPrep();
			}catch(Exception e) {
				e.printStackTrace();
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
			}
			
			//print appliance versions before suite kicks off
			getApplianceVersion(txIp);
			getApplianceVersion(rxIp);
			getApplianceVersion(txIpDual);
			getApplianceVersion(rxIpDual);
			
			//print appliance versions before suite kicks off
			getApplianceVersion(txIp);
			getApplianceVersion(rxIp);
			getApplianceVersion(txIpDual);
			getApplianceVersion(rxIpDual);
		}
		startTime = System.currentTimeMillis();
		
		
//		hardwareFile.writeFile(ctl.hardwareReportStartText(getDateAndTime(false), getDateAndTime(true)), hardwareFile.getFile());
		
		
		
		startTime = System.currentTimeMillis();
		
	}
	
//	public void collectLogs(ITestResult result) {
//		try {
//			Utilities.captureDeviceLog(rxIp, result.getName());
//		}catch(Exception e) {
//			log.info("Error capturing device log." + rxIp);
//		}
//		try {
//			Utilities.captureDeviceLog(txIp, result.getName());
//		}catch(Exception e) {
//			log.info("Error capturing device log." + txIp);
//		}
//		try {
//			Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
//					 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
//			}catch(Exception e) {
//				System.out.println("Error when trying to capture log file. Catching error and continuing");
//				e.printStackTrace();
//			}
//	}
	
	@AfterSuite(alwaysRun = true)
	public void afterSuite() {
		long totalTime = System.currentTimeMillis();
		System.out.println("After suite");
//		ArrayList<String> newSmartCtlValues = ctl.getItemsFromSmartCtl();
//		hardwareFile.writeFile(ctl.compareValues(initialSmartCtl, newSmartCtlValues), hardwareFile.getFile());
//		
		String runFullSetUp = "false";
		
		if(isUnmanage) {
			DevicesMethods deviceMethods = new DevicesMethods();
			try {
				cleanUpLogin();
				deviceMethods.unManageDevice(driver, txIp);
				deviceMethods.unManageDevice(driver, rxIp);
				deviceMethods.unManageDevice(driver, txIpDual);
				deviceMethods.unManageDevice(driver, rxIpDual);
			}catch(Exception | AssertionError e) {
				Utilities.captureScreenShot(driver, this.getClass().getName() + "_afterClass", "After Class");
				e.printStackTrace();
				cleanUpLogout();
			}
			cleanUpLogout();
			printSuitetDetails(true);
	
		}
		
		
		
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
			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			driver.get(url);
//			
			try {
				
				Loginpage.username(driver).sendKeys(userName);

			
				Loginpage.password(driver).sendKeys(password);

				
				Loginpage.loginbtn(driver).click();
				
				
				
			} catch (Exception e) {
				System.out.println("Error in Clean up login method");
				e.printStackTrace();
//				driver.quit();
				driver.close();
			}
	}
	// This is overloaded method for cluster
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
//					driver.quit();
					driver.close();
				}
		}
	
	public void waitForBoxillaPoll() {
		try {
			log.info("Waiting for boxillas next poll before continuing");
			Thread.sleep(65000);
		}catch(Exception e) {
			
		}
	}
	/**
	 * Seperate logout method that is used for cleaning up tests
	 */
	public void cleanUpLogout() {
		try {
			Thread.sleep(1000);
			driver.get(url);
			Thread.sleep(2000);
			Landingpage.logoutDropdown(driver).click();
			Thread.sleep(3000);
			Actions act=new Actions(driver);
			act.moveToElement(Landingpage.logoutbtn(driver)).click().build().perform();
			Thread.sleep(3000);
//			driver.findElement(By.xpath("(//a[normalize-space()='Log out'])[1]")).click();
//			Landingpage.logoutbtn(driver).click();
			Thread.sleep(3000);
			System.out.println("Logout From Boxilla");
//			driver.quit();
			driver.close();
			
		} catch (Exception e) {
			// TODO: handle exception
//			driver.quit();
			driver.close();
			System.out.println("Error for logging out");
		}
	}
	
	@BeforeMethod(alwaysRun = true)
	//@Parameters({ "browser" })
	public void login(Method method) throws InterruptedException {
		url = "https://" + boxillaManager + "/";
	//	url = "https://10.211.129.178/";
		splitTime = System.currentTimeMillis();
		// Select driver based on the Browser parameter selected
		//String url = "https://" + boxillaManager + "/";
		//if (browser.equalsIgnoreCase("firefox")) {
			/* *************************** Firefox Driver ********************************* */
			System.setProperty("webdriver.gecko.driver", "C:\\Selenium\\Webdrivers\\geckodriver.exe");
			DesiredCapabilities caps = new DesiredCapabilities();
			caps.setCapability("acceptInsecureCerts", true); // Accepting insecure content
			driver = new FirefoxDriver(caps);
			driver.manage().window().maximize();
			driver.manage().timeouts().implicitlyWait(10,TimeUnit.SECONDS);
			driver.get(url);
//		} else if (browser.equalsIgnoreCase("chrome")) {
//			/* **************************** Chrome Driver ********************************* */
//			System.setProperty("webdriver.chrome.driver", "C:\\Selenium\\Webdrivers\\chromedriver.exe");
//			driver = new ChromeDriver();
//			driver.manage().window().maximize();
//			driver.get(url);
//		} else if (browser.equalsIgnoreCase("ie")) {
//			// ******************************* IE Driver
//			// ************************************
//			System.setProperty("webdriver.ie.driver", "C:\\Selenium\\Webdrivers\\IEDriverServer.exe");
//			driver = new InternetExplorerDriver();
//			driver.manage().window().maximize();
//			driver.get(url);
//			driver.navigate().to("javascript:document.getElementById('overridelink').click()");
//		}

		try {
		
			printTestDetails("STARTING ", method.getName(), "");
			Loginpage.username(driver).sendKeys(userName);

		
			Loginpage.password(driver).sendKeys(password);

			
			Loginpage.loginbtn(driver).click();
			
			//set timeout for webdriver
			int timeout = Integer.parseInt(prop.getProperty("waitTime"));
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Landingpage.dashboard(driver)));
		} catch (Exception e) {
			log.info("Error in Login method");
//			driver.quit();
			driver.close();
		}

	}

//	public void clearGif() {
//		String dir = "./test-output/Screenshots/";
//		File folder = new File(dir);
//		File[] files = folder.listFiles();
//		for(File f : files) {
//			if(f.getAbsolutePath().contains("giffy")) {
//				f.delete();
//			}
//		}
//		SeleniumActions.screenshotList.clear();
//	}

	@AfterMethod(alwaysRun = true)
	public void logout(ITestResult result) throws Exception {
		// Taking screen shot on failure
		//String url = "https://" + boxillaManager + "/";
		String results = "";
		//print result
		if(ITestResult.FAILURE == result.getStatus())
			results = "FAIL";
		
		if(ITestResult.SKIP == result.getStatus())
			results = "SKIP";
		
		if(ITestResult.SUCCESS == result.getStatus()) {
			results = "PASS";
			//clearGif();
		
		}
		
		if (ITestResult.FAILURE == result.getStatus() || ITestResult.SKIP == result.getStatus()) {
			Throwable failReason = result.getThrowable();
			log.info("FAIL REASON:" + failReason.toString());
			String screenShotName = result.getName() + Utilities.getDateTimeStamp();
			Utilities.captureScreenShot(driver, screenShotName, result.getName());
			 List<JavaScriptError> jsErrors = JavaScriptError.readErrors(driver); 
			 log.info("************* JAVA SCRIPT ERRORS **************");
			for(JavaScriptError e : jsErrors) {
				log.info(e.getErrorMessage() + "Line Number:" + e.getLineNumber());
				
			}
			log.debug("*************** END JAVA SRIPT ERRORS *************");
			//GifSequenceWriter.createGif(SeleniumActions.screenshotList, result.getName());
			//clearGif();
			//Utilities.captureLog(boxillaManager, prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"),
				//	 "./test-output/Screenshots/LOG_" + result.getName() + Utilities.getDateTimeStamp() + ".txt");
			
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
			//log.info("Regression running for : " + getTimeFromMilliSeconds(duration));
			long singleTestTime = endTime - splitTime;
			log.info(result.getName() + " took : " + getTimeFromMilliSeconds(singleTestTime));
			
		} catch (Exception e) {
			// TODO: handle exception
//			driver.quit();
			driver.close();
		}
		printTestDetails("FINISHING", result.getName(), results);
		log.info("Tests Completed:" + ++testCounter);
	}

	/**
	 * Will load the property file into memory for use in test cases
	 */
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

	
	public void  loadProperties(String propertyfile) {
		try {
			InputStream in = new FileInputStream(propertyfile);
			prop.load(in);
			in.close();
		} catch (IOException e) {
			log.info("Properties file failed to load");
		}
	}

	/**
	 * returns the property file object
	 * @return
	 */
	public Properties getProp() {
		return prop;
	}

	/**
	 * sets login user name for boxilla
	 * @param userName
	 */
	public void setUserName(String userName) {
		this.userName = userName;
	}

	/**
	 * returns login username for boxilla
	 * @return
	 */
	public String getUserName() {
		return userName;
	}

	/**
	 * sets login password for boxilla
	 * @param password
	 */
	public void setPassword(String password) {
		this.password = password;
	}

	/**
	 * returns login password for boxilla
	 * @return
	 */
	public String getPassword() {
		return password;
	}

	public String getTimeFromMilliSeconds(long time) {
		return new SimpleDateFormat("mm:ss").format(new Date(time));
		
	}
	
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
	 * @throws InterruptedException 
	 */
	private void setUpForSoak(String number) throws InterruptedException {
		int amount = Integer.parseInt(number);
		number = Integer.toHexString(amount);
		Ssh shell = new Ssh(prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"), boxillaManager );
		shell.loginToServer();
		String output = shell.sendCommand("initctl start cloudium-example-licensetest BXAMGR_USERS=" + number + " BXAMGR_CONNECTIONS=" + 
		number + " BXAMGR_DEVICES=" + number);		//create 2000 
		log.info("Output: " + output);
		Thread.sleep(240000);				//wait for script to complete
		shell.disconnect();
		
		shell.loginToServer();
		String stopOutput = shell.sendCommand("initctl stop cloudium-example-licensetest");					//stop the service
		log.info("Output: " + stopOutput);
		shell.disconnect();
		log.info("database created...");
		Thread.sleep(60000);
	}
	
	/**
	 * will delete all the users, connections and devices created by setUpForSoak
	 * @throws InterruptedException
	 */
	private void tearDownForSoak() throws InterruptedException {
		Ssh shell = new Ssh(prop.getProperty("boxillaUserName"), prop.getProperty("boxillaPassword"), boxillaManager );
		shell.loginToServer();
		String output = shell.sendCommand("initctl start cloudium-example-licensetest");		//delete everything
		log.info("Output: " + output);
		shell.disconnect();
		Thread.sleep(240000);				//wait for script to complete
		
		shell.loginToServer();
		String stopOutput = shell.sendCommand("initctl stop cloudium-example-licensetest");					//stop the service 
		log.info("Output: " + stopOutput);
		shell.disconnect(); 
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

	public void printTestDetails(String end, String testName, String result) {
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
		log.info("***************************************************************************************");
		log.info("                         " + end + " TEST " + testName + ":" + result);
		log.info("***************************************************************************************");
		log.info(System.getProperty("line.separator"));
		log.info(System.getProperty("line.separator"));
	}
	
	@BeforeClass(alwaysRun=true)
	public void beforeClass() throws InterruptedException {
		getDevices();
		printSuitetDetails(false);
	}
	@AfterClass(alwaysRun=true)
	public void afterClass() throws InterruptedException {
		printSuitetDetails(true);
	}
	
	public void deviceManageTestPrep() throws InterruptedException {
		log.info("Test Preparation Manage Device");
		DiscoveryMethods discoveryMethods = new DiscoveryMethods();		
		

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
	public void getDevices() {
		
		 restuser = prop.getProperty("rest_user");
		 restPassword = prop.getProperty("rest_password");
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
//		sh=shTx.getIpAddress();
//		dh=dhTx.getIpAddress();
		
		if(!isEmerald) {
			txIp = txSingle.getIpAddress();
			rxIp = rxSingle.getIpAddress();
//			txIpExtra=txSingleExtra.getIpAddress();
			singleTxName = txSingle.getDeviceName();
			singleRxName = rxSingle.getDeviceName();
//			dualTxName = txDual.getDeviceName();
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
	
	private void getApplianceVersion(String deviceIp) {
		Ssh ssh = new Ssh(deviceUserName, devicePassword, deviceIp);
		ssh.loginToServer();
		String applianceBuild = ssh.sendCommand("cat /VERSION");
		ssh.disconnect();
		log.info("Appliance build: " + applianceBuild);
	}
	
	public boolean isIpReachable(String ip) {
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
