package methods;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.json.simple.JSONObject;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.SkipException;
import org.testng.asserts.Assertion;

import extra.SeleniumActions;
import extra.Ssh;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import extra.RESTStatistics.REQUEST_TYPE;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import northbound.get.BoxillaHeaders;
import northbound.post.config.CreateKvmConnectionsConfig;
import northbound.post.config.CreateKvmConnectionsConfig.CreateConnection;
import objects.Cluster;
import objects.Connections;
import objects.Discovery;
import objects.Landingpage;
import objects.Loginpage;
import objects.SystemAll;
import objects.Users;

/**
 * Class contains all methods for interacting with boxilla System pages
 * @author Boxilla
 *
 */

public class SystemMethods extends StartupTestCase {
	UsersMethods userMethods = new UsersMethods();
	ConnectionsMethods connectionMethods = new ConnectionsMethods();
	private boolean ipFail = false;
	 private CreateKvmConnectionsConfig config=new CreateKvmConnectionsConfig();
	final static Logger log = Logger.getLogger(SystemMethods.class);
//	@Override
//	public void loadProperties() {
//		// TODO Auto-generated method stub
//		super.loadProperties();
//	}
	
	private String getUri() {
		return getHttp() + "://" + boxillaManager  + "/bxa-api/users/kvm";
	}
	class UserCreation {
		public String username;
		public String password;
		public String privilege;
		public String remote_access;
		public String auto_connect;
		public String auto_connect_name;
	}
	class connectionLicense
	{
		public String name;
		public String host;
		public String zone;
		public String group;
		public String connection_type;
		public String view_only;
		public String extended_desktop;
		public String usb_redirection;
		public String audio;
		public String persistent;
		public String cmode; 
			
	}
	   
  

   
	/**
	 * This will enable the northbound rest api through Boxilla UI.
	 * If it is already enabled we do nothing
	 * @param driver
	 * @throws InterruptedException
	 */
	public void enableNorthboundAPI(WebDriver driver) throws InterruptedException {
		navigateToSystemSettings(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.restApiTab);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SystemAll.restApiHeader)));

		boolean isOff = SeleniumActions.seleniumIsDisplayed(driver, SystemAll.restApiSwitchOff);
		log.info("Is off:"  + isOff);
		if(isOff) {
			log.info("Northbound API is disabled. Enabling");
			SeleniumActions.seleniumClick(driver, SystemAll.restApiSwitchOff);
		}else {
			log.info("Northbound API is already enabled. Doing nothing");
		}

	}
	/**
	 * Disables the Northbound REST API through Boxilla UI
	 * @param driver
	 * @throws InterruptedException
	 */
	public void disableNorthboundAPI(WebDriver driver) throws InterruptedException {
		navigateToSystemSettings(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.restApiTab);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SystemAll.restApiHeader)));
		boolean isOn = SeleniumActions.seleniumIsDisplayed(driver, SystemAll.restApiSwitchOn);
		if(isOn) {
			log.info("Northbound API is enabled. Disabling");
			SeleniumActions.seleniumClick(driver, SystemAll.restApiSwitchOn);
		}else {
			log.info("Northbound API is already disabled. Doing nothing");
		}

	}
	/**
	 * This will enable a second NIC on boxilla and enter network details. All through 
	 * Boxilla UI
	 * @param driver
	 * @param ip
	 * @param netmask
	 * @param gateway
	 * @throws InterruptedException
	 */
	public void addDualNic(WebDriver driver, String ip, String netmask, String gateway) throws InterruptedException {
		log.info("Setting Dual NIC network details");
		navigateToSystemSettings(driver);

		//check if dual NIC is already enabled which it prob will be as 
		//there is no way to disable it. Check for Ethernet Port 2 on the page.
		//if it exists then an edit dual nic should be called
		String page = driver.getPageSource();
		if(!SeleniumActions.seleniumIsDisplayed(driver, SystemAll.getEditEth2Dropdown())) {
			log.info("Dual NIC is not enabled already. Enabling");
			SeleniumActions.seleniumClick(driver, SystemAll.getEth1DropdownBtn());
			SeleniumActions.seleniumClick(driver, SystemAll.getEnableEth2Btn());
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getEth2ApplyBtn())));
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth2IpAddress());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth2IpAddress(), ip);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth2Netmask());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth2Netmask(), netmask);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth2Gateway());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth2Gateway(), gateway);
			SeleniumActions.seleniumClick(driver, SystemAll.getEth2ApplyBtn());
			//assert

			new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SystemAll.getRotate())));
			new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(SystemAll.getRotate())));
			String[] details = getDualNicDetails(driver);
			Assert.assertTrue(details[0].equals(ip), "Dual NIC IP was not " + ip + ", actual:" + details[0]);
			Assert.assertTrue(details[1].equals(netmask), "Dual NIC netmask was not " + netmask + ", actual:" + details[1]);
			Assert.assertTrue(details[2].equals(gateway), "Dual NIC gateway was not " + gateway + ", actual:" + details[2]);
		}else { 
			//edit Dual NIC ip
			log.info("Performing an edit instead");
			editDualNicNetworkDetails(driver, ip, netmask, gateway);
		}
	}
	/**
	 * Edit the network details of the second NIC in boxilla ui. Second NIC needs to be enabled 
	 * @param driver
	 * @param ip
	 * @param netmask
	 * @param gateway
	 * @throws InterruptedException
	 */
	public void editDualNicNetworkDetails(WebDriver driver, String ip, String netmask, String gateway) throws InterruptedException {
		log.info("Editing Dual NIC network details");
		navigateToSystemSettings(driver);
		String page = driver.getPageSource();
		if(page.contains("Ethernet Port 2")) { 
			log.info("Dual NIC is active. Attempting to edit");
			SeleniumActions.seleniumClick(driver, SystemAll.getEditEth2Dropdown());
			SeleniumActions.seleniumClick(driver, SystemAll.getEditEth2IpBtn());

			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getEth2EditApplyBtn())));
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEditEth2Ip());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEditEth2Ip(), ip);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEditEth2Netmask());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEditEth2Netmask(), netmask);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEditEth2Gateway());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEditEth2Gateway(), gateway);
			SeleniumActions.seleniumClick(driver, SystemAll.getEth2EditApplyBtn());

			new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SystemAll.getRotate())));
			new WebDriverWait(driver, 60).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(SystemAll.getRotate())));
			String[] details = getDualNicDetails(driver);
			Assert.assertTrue(details[0].equals(ip), "Dual NIC IP was not " + ip + ", actual:" + details[0]);
			Assert.assertTrue(details[1].equals(netmask), "Dual NIC netmask was not " + netmask + ", actual:" + details[1]);
			Assert.assertTrue(details[2].equals(gateway), "Dual NIC gateway was not " + gateway + ", actual:" + details[2]);

		}else {
			log.info("Dual NIC not active. Please active before editing...Doing nothing...");
		}
	}
	/**
	 * This will return an array containing the second nic on boxillas details.
	 * ip will be index 0, netmask 1, gateway 2
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String[] getDualNicDetails(WebDriver driver) throws InterruptedException {
		log.info("Getting Dual NIC network details");
		navigateToSystemSettings(driver);
		String[] details = new String[3];
		String ip = SeleniumActions.seleniumGetText(driver, SystemAll.getEth2IpFromTable());
		log.info("Eth 2 IP: " + ip);
		details[0] = ip;
		String netmask = SeleniumActions.seleniumGetText(driver, SystemAll.getEth2NetmaskFromTable());
		log.info("Eth 2 netmask: " + netmask);
		details[1] = netmask;
		String gateway = SeleniumActions.seleniumGetText(driver, SystemAll.getEth2GatewayFromTable());
		log.info("Eth 2 gateway:" + gateway);
		details[2] = gateway;
		return details;
	}

	/**
	 * Returns a string array current Active Directory settings
	 * taken from the Current AD Settings table
	 * 0 - IP Address
	 * 1 - Port
	 * 2 - Domain
	 * 3 - Username
	 * @param driver
	 * @return
	 */
	public String[] getCurrentADSettings(WebDriver driver) throws InterruptedException {
		navigateToActiveDirectory(driver);
		String [] settings = new String[4];
		settings[0] = SeleniumActions.seleniumGetText(driver, SystemAll.currentADSettingsIpAddress);
		settings[1] = SeleniumActions.seleniumGetText(driver, SystemAll.currentADSettingsPort);
		settings[2] = SeleniumActions.seleniumGetText(driver, SystemAll.currentADSettingsDomain);
		settings[3] = SeleniumActions.seleniumGetText(driver, SystemAll.currentADSettingsUsername);
		log.info("IP:" + settings[0]);
		log.info("Port:" + settings[1]);
		log.info("Domain:" + settings[2]);
		log.info("Username:" + settings[3]);
		return settings;

	}
	/**
	 * this will create an OU to use with an active directory user in boxilla. the contents of the 
	 * toast message will be returned so this can be used for positive and negative tests
	 * @param driver
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	public void createOU(WebDriver driver, String name) throws InterruptedException {
		navigateToActiveDirectory(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.getCreateOuButton());
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(SystemAll.getCreateOuModal())));
		SeleniumActions.seleniumSendKeys(driver, SystemAll.getOuNameTextBox(), name);
		SeleniumActions.seleniumClick(driver, SystemAll.getCreateAddOuButton());
//		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(By.xpath(SystemAll.activeDirectoryToast)));
//		String toast = SeleniumActions.seleniumGetText(driver, SystemAll.activeDirectoryToast);
//		log.info("Toast message from create OU:" + toast);
//		return toast;	
	}
	/**
	 * deletes an ou from boxilla. returns contents of the toast message
	 * @param driver
	 * @param name
	 * @return
	 * @throws InterruptedException
	 */
	public void deleteOU(WebDriver driver, String name) throws InterruptedException {
		log.info("Attempting to delete OU:" + name);
		clickOUTableDropdown(driver, name);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getDeleteOuLink())));
		SeleniumActions.seleniumClick(driver, SystemAll.getDeleteOuLink());
//		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(SystemAll.activeDirectoryToast)));
//		String toast = SeleniumActions.seleniumGetText(driver, SystemAll.activeDirectoryToast);
//		log.info("Toast:" + toast);
//		return toast;
	}
	/**
	 * this will navigate and search the AD group associations table. it will not process the data
	 * @param driver
	 * @param search
	 * @throws InterruptedException
	 */
	public void searchActiveDirectoryGroupAssociations(WebDriver driver, String search) throws InterruptedException {
		navigateToActiveDirectory(driver);
//		SeleniumActions.seleniumSendKeys(driver, SystemAll.groupAssociationsSearchBox, search);
		SystemAll.searchOU(driver).sendKeys(search);
	}
	/**
	 * This will return data from the group associations table depending on parameters passed in.
	 *  data type is either "ou" for OU table data or "cg" for connection group table data
	 * @param driver
	 * @param search
	 * @param dataType
	 * @return
	 * @throws InterruptedException
	 */
	public String getGroupAssociationsTableData(WebDriver driver, String search, String dataType) throws InterruptedException {
		searchActiveDirectoryGroupAssociations(driver, search);
		if(dataType.equals("ou")) {
			return SeleniumActions.seleniumGetText(driver, SystemAll.OUtableData);
		}else if(dataType.equals("cg")) {
			return SeleniumActions.seleniumGetText(driver, SystemAll.connectionGroupTableData);
		}else {
			log.info("no table column for " + dataType);
			return null;
		}
	}

	/**
	 * This will navigate to the OU table in boxilla and click the dropdown menu button
	 * @param driver
	 * @param search
	 * @throws InterruptedException
	 */
	public void clickOUTableDropdown(WebDriver driver, String search) throws InterruptedException {
		log.info("Attempting to click OU table drop down");
		navigateToActiveDirectory(driver);
		SystemAll.searchOU(driver).clear();
		SystemAll.searchOU(driver).sendKeys(search);
		WebElement e = driver.findElement(By.xpath(SystemAll.getDeleteOuDropdown(search)));
		log.info("IS Displayed:" + e.isDisplayed());
		log.info("IS enabled:" + e.isEnabled());
		log.info("IS selected:" + e.isSelected());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(e));
		int counter = 0;
		while(counter < 10) {

			try {
				e.click();
				new WebDriverWait(driver, 5).until(ExpectedConditions.elementToBeClickable(SystemAll.getLinkToGroup(driver)));
				counter = 10;

			}catch(Exception e1)
			{
				log.info("button didnt click");
				counter++;
			}
		}
		log.info("Dropdown clicked");
	}
	/**
	 * This will link an ou with a connection group 
	 * @param driver
	 * @param search
	 * @param connectionGroupName
	 * @throws InterruptedException
	 */
	public void linkOUToGroup(WebDriver driver, String search, String connectionGroupName) throws InterruptedException { 
		clickOUTableDropdown(driver, search);
		SeleniumActions.seleniumClick(driver, SystemAll.linkToGroup);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.getLinkToGroupCancelButton(driver)));
		log.info("Choose connection group pop up open");
		log.info("Selecting:" + connectionGroupName );
		SeleniumActions.seleniumClick(driver, SystemAll.getConnectionGroupListElement(connectionGroupName));
		String toastText = SeleniumActions.seleniumGetText(driver, SystemAll.activeDirectoryToast);
		Assert.assertTrue(toastText.contains("Success"), "Group was not successfully added to OU, actual toast text:" + toastText);

	}
	/**
	 * this will unlink an ou from a connection group
	 * @param driver
	 * @param search
	 * @throws InterruptedException
	 */
	public void unlinkOUToGroup(WebDriver driver, String search) throws InterruptedException {
		clickOUTableDropdown(driver, search);
		SeleniumActions.seleniumClick(driver, SystemAll.unlinkGroupButton);
		String toastText = SeleniumActions.seleniumGetText(driver, SystemAll.activeDirectoryToast);
		Assert.assertTrue(toastText.contains("Success"), "Group was not unlinked, actual toast text:" + toastText);
	}
	/**
	 * this will navigate to the active directory settings page and configure an 
	 * active directory server in boxilla
	 * @param ip
	 * @param port
	 * @param domain
	 * @param username
	 * @param password
	 * @param driver
	 * @throws InterruptedException
	 */
	public void enterActiveDirectorySettings(String ip, String port, String domain, 
			String username, String password, WebDriver driver) throws InterruptedException {
		log.info("Attemping to enter active directory settings");
		log.info("IP:" + ip);
		log.info("Port:" + port);
		log.info("Domain:" + domain);
		log.info("Username:" + username);
		log.info("Password:" + password);

		navigateToActiveDirectory(driver);
		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.activeDirectoryIpTextBox);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.activeDirectoryIpTextBox, ip);

		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.activeDirectoryPortTextBox);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.activeDirectoryPortTextBox, port);

		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.activeDirectoryDomainTextBox);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.activeDirectoryDomainTextBox, domain);

		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.activeDirectoryUsernameTextBox);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.activeDirectoryUsernameTextBox, username);

		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.activeDirectoryPasswordTextBox);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.activeDirectoryPasswordTextBox, password);
		log.info("Details entered. Clicking apply");
		SeleniumActions.seleniumClick(driver, SystemAll.activeDirectoryGlobalApplyButton);
		Thread.sleep(5000);

	}
	/**
	 * this will disable AD support in boxilla
	 * @param driver
	 * @throws InterruptedException
	 */
	public void turnOffActiveDirectorySupport(WebDriver driver) throws InterruptedException {
		log.info("Attemping to turn active directory support off");
		navigateToActiveDirectory(driver);
		if(SystemAll.getActiveDirectoryGlobalApplyButton(driver).isEnabled()) {
			log.info("Active directory support is on. Turning off");
			SeleniumActions.seleniumClick(driver, SystemAll.activeDirectorySupportButton);
			boolean isEnabled = SystemAll.getActiveDirectoryGlobalApplyButton(driver).isEnabled();
			Assert.assertFalse(isEnabled, "Active directory support was not turned off");
			log.info("Active directory support has been turned off");
		}else {
			log.info("Active directory is already off");
		}

	}
	/**
	 * this will enable AD support in boxilla
	 * @param driver
	 * @throws InterruptedException
	 */
	public void turnOnActiveDirectorySupport(WebDriver driver) throws InterruptedException {
		navigateToActiveDirectory(driver);
		//if active directory is off the apply button is not visable
		if(SystemAll.getActiveDirectoryGlobalApplyButton(driver).isEnabled()) {
			log.info("Active directory is already on");
		}else {
			log.info("Active directory is not on. Turning on");
			SeleniumActions.seleniumClick(driver, SystemAll.activeDirectorySupportButton);
			boolean isEnabled = SystemAll.getActiveDirectoryGlobalApplyButton(driver).isEnabled();
			Assert.assertTrue(isEnabled, "Active Directory is not enabled");
			log.info("Active directory has been turned on");
		}


	}

	/**
	 * this will navigate to the active directory page in boxilla 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToActiveDirectory(WebDriver driver) throws InterruptedException {
		log.info("Attemping to navigate to System > Active Directory");
		navigateToSystemSettings(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.activeDirectoryTab);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(SystemAll.getActiveDirectorySupportButton(driver)));
		log.info("Successfully navigated to active directory");
	}
	public boolean getIpFail() {
		return ipFail;
	}
	public void setIpFail(boolean ipFail) {
		this.ipFail = ipFail;
	}
	/**
	 * do not use hard coded timers. please use implicit waits instead
	 * @param driver
	 * @throws InterruptedException
	 */
	@Deprecated
	public void timer(WebDriver driver) throws InterruptedException {
		Thread.sleep(2000);
		driver.manage().timeouts().implicitlyWait(StartupTestCase.getWaitTime(), TimeUnit.SECONDS);
	}

	/* Navigate to System > Administrator page and assert page title */
	/**
	 * Navigates to boxilla System Admin page and asserts the page title
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToSystemAdmin(WebDriver driver) throws InterruptedException {
		timer(driver);
		Landingpage.systemTab(driver).click();
		log.info("System Dropdown clicked");
		timer(driver);

		Landingpage.systemAdmin(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.upgradeTab(driver)));
	}

	/* Upload most recent version from shared directory and Activate version */
	/**
	 * Uploads most recent version of boxilla build and activates
	 * @param driver
	 * @param versionNumber
	 * @param boxillaIp
	 * @throws InterruptedException
	 */
	public void systemUpgrade(WebDriver driver, String versionNumber, String boxillaIp) throws InterruptedException {
		navigateToSystemAdmin(driver);
		versionUpload(driver, versionNumber); // calling method to upload version
		activateVersion(driver, versionNumber, boxillaIp); // calling method to activate version
	}
	
	public void systemUpgrade1(WebDriver driver, String versionNumber, String boxillaIp) throws InterruptedException {
		navigateToSystemAdmin(driver);
		activateVersion(driver, versionNumber, boxillaIp); // calling method to activate version
	}

	/* System Upgrade and downgrade .. Search using version1 -> Activate if Inactive, else if Activate version2  */
	public void systemUpgradeSoak(WebDriver driver, String version1, String version2, String boxillaIp) throws InterruptedException {
		String shortVersion1 = version1.substring(0, version1.length() - 4);
		String shortVersion2 = version2.substring(0, version2.length() - 4);

		if (driver.getPageSource().contains("/system/home")) {
			timer(driver);
			driver.findElement(By.xpath("//*[@data-original-title='System']")).click();
			log.info("System Tab Clicked");
		} else {
			navigateToSystemAdmin(driver);
		}

		timer(driver);
		SystemAll.searchboxUpgrade(driver).sendKeys(shortVersion1);
		log.info(shortVersion1 + " entered in search box to activate version");
		if (SystemAll.activeState(driver).getText().contains("Inactive")) {
			timer(driver);
			log.info(shortVersion1 + " is Inactive.. Trying to activate it..");
			activateVersion(driver, version1, boxillaIp);
		} else if (SystemAll.activeState(driver).getText().contains("Active")) {
			timer(driver);
			log.info(shortVersion1 + " is active.. Trying to activate " + shortVersion2);
			activateVersion(driver, version2, boxillaIp);
		} else {
			log.info("Version State is neither Active nor Inactive");
			throw new SkipException("***** Version State is neither Active nor Inactive *****");
		}
	}

	/* Activate Selected version. Search using passed version and activate.. Assert version once activated */
	/**
	 * Activates the selected version of boxilla software
	 * @param driver
	 * @param versionNumber
	 * @param boxillaIp
	 * @throws InterruptedException
	 */
	public void activateVersion(WebDriver driver, String versionNumber, String boxillaIp) throws InterruptedException {
		String shortVersion = versionNumber.substring(0, versionNumber.length() - 4);
		timer(driver);
		SystemAll.searchboxUpgrade(driver).clear();
		timer(driver);
		SystemAll.searchboxUpgrade(driver).sendKeys(shortVersion);
		log.info("Version number entered in search box to activate version");
		timer(driver);
		//		String currentVersionTable = SystemAll.currentVersionTable(driver).getText();

		SystemAll.btnVersionBreadCrumb(driver, shortVersion).click();
		log.info("Version - BreadCrum Button Clicked");
		timer(driver);
		SystemAll.versionActivate(driver, shortVersion).click();
		Thread.sleep(2000);


		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Starting Upgrade....");
		timer(driver);
		int i = 0;
		// Wait until Upgrade process completes - Loop keeps asserting message inside
		// spinner tag
		try {
			while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
				Thread.sleep(20000);
				log.info(i + " Upgrading in Progress....");
				i++;
			}
			Thread.sleep(5000);
			log.info("Upgrade complete.. Asserting version..");
			timer(driver);

			//test failure point. iF upgrade goes wrong
			//we will log into the boxilla server and restart the services so
			//the regression may continue on
			Landingpage.dashboard(driver).click(); // Navigated to Dashboard for smooth process
		} catch (Exception e) {
			sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, "service moy restart");			//restart services if upgrade fails
			Thread.sleep(60000); 				//wait for boxilla to come back up
			Assert.fail("The upgrade failed and recovered. Marking test as fail");			
		}
		timer(driver);
		if (driver.getPageSource().contains("/system/home")) {
			timer(driver);
			driver.findElement(By.xpath("//*[@data-original-title='System']")).click();
			log.info("System Tab Clicked");
		} else {
			navigateToSystemAdmin(driver);
		}
		timer(driver);
		SystemAll.systemInfo(driver).click();
		log.info("System Info Tab clicked");
		timer(driver);
		log.info(SystemAll.systemInfoTable(driver).getText());
		String systemInfoTable = SystemAll.systemInfoTable(driver).getText();
		String check = shortVersion.split("\\.")[3];
		Assert.assertTrue(systemInfoTable.contains(check),
				"System info table did not contain: " + check + ", actual text: " + systemInfoTable);
	}

	/**
	 * this will return the minor version number of the currently 
	 * installed boxilla software taken from teh boxilla ui
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getBoxillaMinorVersion(WebDriver driver) throws InterruptedException {
		log.info("Attempting to get boxilla minor version from table");
		navigateToSystemInfo(driver);
		String version = SeleniumActions.seleniumGetText(driver, SystemAll.getMinorVersionFromTable());
		return version;
	}
	/**
	 * this will return boxillas serial number taken from the boxilla ui
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getBoxillaSerialNumber(WebDriver driver) throws InterruptedException {
		log.info("Attempting to get boxilla serial number from table");
		navigateToSystemInfo(driver);
		String serial = SeleniumActions.seleniumGetText(driver, SystemAll.getSerialNumberFromTable());
		return serial;
	}
	/**
	 * this will return the major version number of the current boxilla. taken from boxilla ui
	 * @param driver
	 * @return
	 * @throws InterruptedException
	 */
	public String getBoxillaMajorVersion(WebDriver driver) throws InterruptedException {
		log.info("Attempting to get boxilla major version from table");
		navigateToSystemInfo(driver);
		String version = SeleniumActions.seleniumGetText(driver, SystemAll.getCurrentVersionFromTable());
		return version;
	}

	/**
	 * this will navigate to the system information page on boxilla from any other page
	 * @param driver
	 * @throws InterruptedException
	 */
	private void navigateToSystemInfo(WebDriver driver) throws InterruptedException {
		log.info("Navigating to system info");
		navigateToSystemAdmin(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.getSystemInfoTab());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getExportButton())));
	}

	/* Upload passed boxilla version from shared directory */
	/**
	 * Uploads the passed in version of boxilla software from the shared drive
	 * . Deprecated - invalid file path
	 * @param driver
	 * @param version
	 * @throws InterruptedException
	 */
	
	public void versionUpload(WebDriver driver, String version) throws InterruptedException {
		String shortVersion = version.substring(0, version.length() - 4);	//remove extension
		System.out.println(shortVersion);
		timer(driver);
		SystemAll.upgradeTab(driver).click();
		log.info("Upgrade tab is clicked");
		String title = driver.getTitle();
		timer(driver);
		try {
			String fileName = "C:\\Selenium\\boxilla\\"+version;
			log.info("File to upload:" + fileName);
//			SystemAll.licenseUploadElement(driver)
//			.sendKeys(fileName);
//			SystemAll.uploadElement(driver).click();
			SystemAll.uploadElement(driver).sendKeys(fileName);//\\\\10.10.10.61\\share\\api\\
		}catch(Exception e) {
			e.printStackTrace();
		}
		log.info("Upload file selected");
		timer(driver);
//		SystemAll.btnSubmit(driver).click();
		log.info("File Submitted");
		Thread.sleep(20000);
		SystemAll.searchboxUpgrade(driver).sendKeys(shortVersion);
		log.info("Successfully Redirected.. Version number entered in the search box");
		timer(driver);
		String currentVersionTable = SystemAll.currentVersionTable(driver).getText();
		System.out.println(currentVersionTable);
		Assert.assertTrue(currentVersionTable.contains(shortVersion),
				"Current Version Table did not contain: " + shortVersion + ", actual text: " + currentVersionTable);
		log.info("Version upload assertion completed");
		timer(driver);
		SystemAll.searchboxUpgrade(driver).clear(); // Clear search box
	}

	/** using yesterday's date, custom backup file name & timestamp of custom backup.
	 * Checks if Nightly backup from previous date is present. Check if custom back up is present and uploads if not
	 * Activate custom backup and assert custom User and Connection before Resetting database
	 * Custom backup file name and timestamp are stored in excel file*/
	public void dbRestore(WebDriver driver, String yesterdayDate, String customBackup, String timeStamp)
			throws InterruptedException {
		navigateToSystemAdmin(driver);
		timer(driver);
		SystemAll.backupRestoreTab(driver).click();
		log.info("Backup and Restore Tab Clicked");
		timer(driver);

		SystemAll.searchboxBackupTable(driver).sendKeys(yesterdayDate);
		log.info("Yesterday's date entered in search box");
		timer(driver);
		log.info("Backup found from yesterday");
		uploadCustomBackup(driver, customBackup, timeStamp); // Upload custom backup
		activateBackup(driver, timeStamp); // Activate Uploaded backup
		assertConnection(driver, "backupcheck"); // Assert custom connection present in custom backup
		assertUser(driver, "backupcheck"); // Assert custom user present in custom backup
		dbReset(driver);
	}

	/**
	 * Upload backup database
	 * @param driver
	 * @param fileName name of the database file to upload
	 * @param timeStamp timestamp on the database file
	 * @param fileLocation the directory location of the file
	 * @throws InterruptedException
	 */
	public void uploadBackup(WebDriver driver, String fileName, String timeStamp, String fileLocation) throws InterruptedException {
		timer(driver);
		SystemAll.searchboxBackupTable(driver).clear();
		timer(driver);
		log.info("Uploading custom-made backup");
		SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
		timer(driver);
		if (!(SystemAll.backupTable(driver).getText().contains(timeStamp))) {
			SystemAll.uploadBtn(driver).click();
			log.info("Upload button clicked");
			timer(driver);
			String backupfile = fileLocation + "\\" + fileName;
			SystemAll.backupUploadElement(driver).sendKeys(backupfile);
			log.info("Version selected to upload");
			timer(driver);
			SystemAll.btnSubmit(driver).click();
			log.info("Backup file uploaded");
			timer(driver);
			SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
			log.info("Asserting Upload.. Filtered using name of uploaded file");
			timer(driver);
			log.info("Backup uploaded successfuly.");
		} else {
			log.info("Custom-made Backup is present on Boxilla");
		}
	}

	/** pre-condition: navigate to Backup/Restore tab in System > Administration 
	 * Search using passed custom backup, if back up not found > Upload the backup and activate
	 * if back up found > activate  */
	public void uploadCustomBackup(WebDriver driver, String customBackup, String timeStamp)
			throws InterruptedException {
		timer(driver);
		SystemAll.searchboxBackupTable(driver).clear();
		timer(driver);
		log.info("Uploading custom-made backup");
		SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
		timer(driver);
		if (!(SystemAll.backupTable(driver).getText().contains(timeStamp))) {
			SystemAll.uploadBtn(driver).click();
			log.info("Upload button clicked");
			timer(driver);
			String backupfile = "C:\\Selenium\\Database_Backup\\" + customBackup + ".bbx";
			SystemAll.backupUploadElement(driver).sendKeys(backupfile);
			log.info("Version selected to upload");
			timer(driver);
			SystemAll.btnSubmit(driver).click();
			log.info("Backup file uploaded");
			timer(driver);
			SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
			log.info("Asserting Upload.. Filtered using name of uploaded file");
			timer(driver);
			log.info("Backup uploaded successfuly.");
		} else {
			log.info("Custom-made Backup is present on Boxilla");
		}

	}

	/** Pre condition: Database needs to be uploaded prior to activate
	 * Search using timestamp, activate first searched result and assert notification on completion */
	public void activateBackup(WebDriver driver, String timeStamp) throws InterruptedException {
		timer(driver);
		SystemAll.searchboxBackupTable(driver).clear();
		timer(driver);
		log.info("Uploading custom-made backup");
		SystemAll.searchboxBackupTable(driver).sendKeys(timeStamp);
		timer(driver);
		SystemAll.breadCrumb(driver).click(); // bread crumb clicked
		log.info("Breadcrumb clicked");
		timer(driver);
		SystemAll.restoreBtn(driver).click();
		log.info("Database restore button clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		int counter = 0;
		while (Landingpage.spinner(driver).isDisplayed() && counter < 60) {
			log.info((counter + 1) + ". Database restore in Progress...");
			Thread.sleep(5000);
			counter++;
		}
//		timer(driver);
//		String notoificationMessage = Users.notificationMessage(driver).getText();
//		Assert.assertTrue(notoificationMessage.contains("Successfully restored backup"),
//				"Notification Message did not contain: Successfully restored backup, actual text: " + notoificationMessage);
//		log.info("Database Restore: Notification Message successfully asserted.");
	}

	/** Search passed connection name and fails if not present in the list */
	public void assertConnection(WebDriver driver, String connectionName) throws InterruptedException {
		log.info("Asserting Connection..");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsTab);
		log.info("Connection Tab Clicked");
		timer(driver);
		SeleniumActions.seleniumClick(driver, Landingpage.connectionsManage);
		log.info("Connections > Manage tab clicked");
		timer(driver);
		String connectionTable = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTable.contains(connectionName),
				"Connection Table did not contain: " + connectionName + ", actual text: " + connectionTable);
		log.info("Custom connection asserted.. ");
	}

	/** Search passed user name and fails if not present in the list */
	public void assertUser(WebDriver driver, String userName) throws InterruptedException {
		log.info("Asserting User..");
		timer(driver);
		Landingpage.usersTab(driver).click();
		log.info("Users tab clicked");
		timer(driver);
		Landingpage.usersManageTab(driver).click();
		log.info("Users > Manage tab clicked");
		timer(driver);
		Assert.assertTrue(Users.usersTable(driver).getText().contains(userName), "***** Custom User not found *****");
		log.info("Custom user asserted.. ");
	}

	/** Database Reset */
	public void dbReset(WebDriver driver) throws InterruptedException {
		timer(driver);
		log.info("Resetting database..");
		navigateToSystemAdmin(driver);
		timer(driver);
		SystemAll.backupRestoreTab(driver).click();
		log.info("Backup and Restore Tab Clicked");
		timer(driver);
		SystemAll.resetdbBtn(driver).click();
		log.info("Reset DB button clicked");
		Alert alert2 = driver.switchTo().alert();
		alert2.accept();
		timer(driver);
		int i = 0;
		while (Landingpage.spinner(driver).isDisplayed() && i < 60) {
			log.info((i + 1) + ". Resetting in Progress...");
			Thread.sleep(5000);
			i++;
		}
		timer(driver);
		
		String notificationMessage = Users.notificationMessage(driver).getText();
//		Assert.assertTrue(notificationMessage.contains("Successfully restored Database"),
//				"Notification Message did not contain: Successfully restored Database, actual text: " + notificationMessage);
		Assert.assertTrue(notificationMessage.contains("DB reset successful"),
				"Notification Message did not contain: DB reset successful, actual text: " + notificationMessage);
	}

	/** Navigate to System > Settings and assert page title*/
	public void navigateToSystemSettings(WebDriver driver) throws InterruptedException {
		timer(driver);
		Landingpage.systemTab(driver).click();
		log.info("System Dropdown clicked");
		timer(driver);
		Landingpage.systemSettings(driver).click();
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.networkTab(driver)));
	}

	/**
	 * Change IP address of Boxilla unit.. Netmask and Gateway is hard-coded currently which can be grabbed from excel file if required
	 * @param driver
	 * @param newIP
	 * @param originalBoxilla
	 * @param recoveryIp
	 * @throws InterruptedException
	 */
	public void changeNetwork(WebDriver driver, String newIP, String originalBoxilla, String recoveryIp) throws InterruptedException {
		String netmask = "";
		//split ip address to find out what subnet it is on
		String[] splitIp = newIP.split("\\.");
		if(splitIp[2].equals("11")) {
			netmask = "255.255.0.0";
		}else {
			netmask = "255.255.248.0";
		}

		timer(driver);
		SystemAll.networkTab(driver).click();
		log.info("Network tab clicked");
		timer(driver);
		String currentIp = SeleniumActions.seleniumGetText(driver, SystemAll.getEth1IpFromTable());
		log.info("Current IP:" + currentIp);
		if (currentIp.contains(newIP)) {
			log.info("Current IP address is same as new IP address.. Skipping test case");
			new SkipException("***** Same IP address *****");
		} else {
			SeleniumActions.seleniumClick(driver, SystemAll.getEth1DropdownBtn());
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getEditEth1Btn())));
			SeleniumActions.seleniumClick(driver, SystemAll.getEditEth1Btn());
			new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(By.xpath(SystemAll.getEth1ApplyBtn())));
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth1IpAddress());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth1IpAddress(), newIP);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth1Netmask());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth1Netmask(), netmask);
			SeleniumActions.seleniumSendKeysClear(driver, SystemAll.getEth1Gateway());
			SeleniumActions.seleniumSendKeys(driver, SystemAll.getEth1Gateway(), "10.211.128.1");
			SeleniumActions.seleniumClick(driver, SystemAll.getEth1ApplyBtn());

			int i = 0;
			while (Landingpage.spinner(driver).isDisplayed() && i < 25) {
				log.info((i + 1) + ". Changing IP address...");
				Thread.sleep(3000);
				i++;
			}
			log.info("Waiting for two minute before navigating to new IP address");
			Thread.sleep(120000);
			try {
				driver.navigate().to("https://" + newIP + "/");
			}catch(Exception e) {
				e.printStackTrace();
				ipFail = true;
				//if this page does not appear. We need to handle it by
				//restarting the boxilla through SSH
				log.info("boxilla did not come back up. Rebooting");
				//ping the ipAddresses first
				boolean isBoxillaUp = pingIpAddress(originalBoxilla);		
				if(isBoxillaUp) {
					sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, originalBoxilla, "reboot");
					Thread.sleep(120000); 			//make sure box is up
					driver.navigate().to("https://" + newIP + "/");
				}else {
					if(originalBoxilla.equals(newIP)) {
						sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, recoveryIp, "reboot");
						Thread.sleep(120000); 			//make sure box is up
						driver.navigate().to("https://" + originalBoxilla + "/");
					}else {
						sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, newIP, "reboot");
						Thread.sleep(120000); 			//make sure box is up

						//
						driver.navigate().to("https://" + originalBoxilla + "/");
					}
				}
			}
			timer(driver);
			String title = driver.getTitle();
			if (title.equalsIgnoreCase("Boxilla - Dashboard")) {
				log.info("Navigated to Dashboard");
			} else if (title.equalsIgnoreCase("Certificate Error: Navigation Blocked")) {
				driver.navigate().to("javascript:document.getElementById('overridelink').click()");
				log.info("Navigated to login screen..Logging In");
				timer(driver);
				Loginpage.username(driver).sendKeys("admin");
				log.info("Username entered");
				timer(driver);
				Loginpage.password(driver).sendKeys("admin");
				log.info("Password entered");
				timer(driver);
				Loginpage.loginbtn(driver).click();
				log.info("Clicked on Login button");
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Landingpage.dashboardLinkText)));
			} else if (title.equalsIgnoreCase("Login")) {
				log.info("Navigated to login screen..Logging In");
				Loginpage.username(driver).sendKeys("admin");
				log.info("Username entered");
				timer(driver);
				Loginpage.password(driver).sendKeys("admin");
				log.info("Password entered");
				timer(driver);
				Loginpage.loginbtn(driver).click();
				log.info("Clicked on Login button");
				new WebDriverWait(driver, 120).until(ExpectedConditions.presenceOfElementLocated(By.linkText(Landingpage.dashboardLinkText)));
			}
		}
	}

	/**
	 * this will delete all remote app licenses that exist in boxilla
	 * @param driver
	 */
	public void deleteAllAppLicenses(WebDriver driver) {
		navigateToAppLicense(driver);
		try {
			while(SystemAll.getDeleteAppLicenseButton(driver).isDisplayed()) {
				SystemAll.getDeleteAppLicenseButton(driver).click();
				Alert alert = driver.switchTo().alert();
				alert.accept();	
				driver.navigate().refresh();
			}
		}catch(Exception e) {
			log.info("No licenses to delete");
		}

	}

	/**
	 * navigates to the remote app license page in boxilla from any other page
	 * @param driver
	 */
	public void navigateToAppLicense(WebDriver driver) {
		log.info("Attempting navigate to app license");
		SeleniumActions.seleniumClick(driver, SystemAll.licenseTab);
		log.info("License tab clicked");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.getAppLicenseLink(driver)));
		SeleniumActions.seleniumClick(driver, SystemAll.appLicenseLink);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.getAddAppLicenseBtn(driver)));
		log.info("Successfully navigated to app license");
	}

	/**
	 * Uploads a valid remote app license to boxilla from the passed in 
	 * file location
	 * @param driver
	 * @param fileLocation
	 */
	public void uploadAppLicense(WebDriver driver, String fileLocation) {
		navigateToAppLicense(driver);
		SeleniumActions.seleniumClick(driver, SystemAll.addAppLicenseBtn);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.chooseFileAppLicense, fileLocation);
		SeleniumActions.seleniumClick(driver, SystemAll.appLicesneSubmitBtn);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.getAddAppLicenseBtn(driver)));
		String numberOfLicenses = SeleniumActions.seleniumGetText(driver, SystemAll.appLicenseNumber);
		log.info("App license number: " + numberOfLicenses);
	}

	/**
	 * this returns the current number of remote app licenses in boxilla
	 * @param driver
	 * @return
	 */
	public int getNumberOfAppLicenses(WebDriver driver) {
		navigateToAppLicense(driver);
		try {
			if(SeleniumActions.seleniumIsDisplayed(driver, SystemAll.appLicenseNumber)) {
				String numberOfLicenses = SeleniumActions.seleniumGetText(driver, SystemAll.appLicenseNumber);
				String[] licenseSplit = numberOfLicenses.split("\\s+");
				int number = Integer.parseInt(licenseSplit[4]);
				return number;
			}
		}catch(Exception e) {
			return 0;
		}
		return 0;
	}

	/**
	 * Navigate to System > License
	 * @param driver
	 * @throws InterruptedException
	 */
	public void navigateToLicense(WebDriver driver) throws InterruptedException {
		log.info("Attempting navigate to BOXILLA license");
		SeleniumActions.seleniumClick(driver, SystemAll.licenseTab);
		log.info("License tab clicked");

		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.getBoxillaLicenseLink(driver)));
		SeleniumActions.seleniumClick(driver, SystemAll.boxillaLicenseLink);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(SystemAll.addLicenseBtn(driver)));
		log.info("successfully navigated to boxilla license page");
	}

	/**
	 * Adds an unlimited license to boxilla. 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void addUnlimitedLicense(WebDriver driver, String addUnlimited) throws InterruptedException {
		navigateToLicense(driver); // calling method to navigate to System > License
		timer(driver);
		SystemAll.addLicenseBtn(driver).click();
		timer(driver);
		SystemAll.licenseUploadElement(driver)
		.sendKeys("C:\\Selenium\\Licenses\\"+addUnlimited+"\\licenseKey_Unlimitedusers.lic");
		timer(driver);
		SystemAll.btnSubmit(driver).click();
		timer(driver);
	}

	/**
	 * Method to add license - uses current limit and new license limit 
	 * @param driver
	 * @param currentLimit
	 * @param addLicense
	 * @throws InterruptedException
	 */
	public void addLicense(WebDriver driver, String currentLimit, String addLicense, String ip) throws InterruptedException {
		navigateToLicense(driver); // calling method to navigate to System > License
		timer(driver);
		// Extracting current limit from the information table
		String currentLicenseLimit = SystemAll.currentLicenseLimt(driver).getText();
		log.info("Current license limit in information table is : " + currentLicenseLimit);
		// if extracted value matches the input value
		if (currentLicenseLimit.equalsIgnoreCase(currentLimit)) {
			SystemAll.addLicenseBtn(driver).click();
			log.info("Add License Button Clicked");
			timer(driver);
			String fileName = "C:\\Selenium\\Licenses\\" + ip + "\\licenseKey_" + addLicense + "users.lic";
			log.info("File to upload:" + fileName);
			SystemAll.licenseUploadElement(driver)
			.sendKeys(fileName);
			log.info("License file selected to upload");
			timer(driver);
			SystemAll.btnSubmit(driver).click();
			log.info("Clicked on Submit button");
			timer(driver);
			driver.manage().timeouts().implicitlyWait(1, TimeUnit.MINUTES);

			// Get new license limit based on the license added
			int oldLicenseValue = Integer.parseInt(currentLicenseLimit); // Current limit String value
			int licenseAdded = Integer.parseInt(addLicense);
			int newLicenseLimitInt = oldLicenseValue + licenseAdded;
			String newLicenseLimit = Integer.toString(newLicenseLimitInt);
			if (addLicense.equalsIgnoreCase("Unlimited")) {
				newLicenseLimit ="Unlimited";
			}//else newLicenseLimit = oldLicenseValue + licenseAdded;
				
			
			log.info("New License Value should be " + newLicenseLimit);
			timer(driver);
			String currentLicenseLimitValidation = SystemAll.currentLicenseLimt(driver).getText();
			Assert.assertTrue(currentLicenseLimitValidation.contentEquals(newLicenseLimit),
					"Current License Limit does not equal: " + newLicenseLimit + ", actual text: " + currentLicenseLimitValidation);
		} // If extracted current limit value doesn't match with input value
		else {
			log.info("Current User/Connection/Device limit is different than " + currentLimit);
			throw new SkipException(
					"***** Skipping test case - Add License - Because current limit doesn't match with input *****");
		}
		timer(driver);
		log.info("License Added and Asserted..");
	}

	/**
	 * Adds a boxilla system user
	 * @param driver
	 * @param userName
	 * @throws InterruptedException
	 */
	public void addBoxillaUser(WebDriver driver, String userName) throws InterruptedException {
		navigateToSystemAdmin(driver);

		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users Tab");
		timer(driver);
		SystemAll.newUser(driver).click();
		log.info("Boxilla user add: Clicked on New User Button");
		timer(driver);
		SystemAll.boxillaUserName(driver).sendKeys(userName);
		log.info("Boxilla user add: Username enetered");
		timer(driver);
		SystemAll.boxillaFirstName(driver).sendKeys("firstname");
		log.info("Boxilla user add: Firstname entered");
		timer(driver);
		SystemAll.boxillaSurname(driver).sendKeys("surname");
		log.info("Boxilla user add: Surname entered");
		timer(driver);
		SystemAll.boxillaEmailAdd(driver).sendKeys("test@blackbox.com");
		log.info("Boxilla user add: Email address added");
		timer(driver);
		Select select = new Select(SystemAll.boxillaAuthorizedBy(driver));
		select.selectByValue("1");
		log.info("Boxilla user add: Clicked on Authorized by drop down and selected INTERNAL");
		timer(driver);
		if (SystemAll.boxillaPassword(driver).isDisplayed()) {
			SystemAll.boxillaPassword(driver).sendKeys("!Password");
			log.info("Boxilla user add: Password entered");
			timer(driver);
			SystemAll.boxillaPasswordVerify(driver).sendKeys("!Password");
			log.info("Boxilla user add: Password re-entered");
			timer(driver);
			SystemAll.btnSubmit(driver).click();
			log.info("Boxilla user adding : Asserting if Boxilla user is added successfully.");
		} else {
			Assert.fail("***** Password textbox disabled *****");
		}
		timer(driver);

		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users tab");
		timer(driver);
		SystemAll.searchboxBoxillaUsers(driver).sendKeys(userName);
		String boxillaUsersTable = SystemAll.boxillaUsersTable(driver).getText();
		Assert.assertTrue(boxillaUsersTable.contains(userName),
				"Boxilla users table did not contain: " + userName + ", actual text: " + boxillaUsersTable);
		log.info("Boxilla user add: User is present in user table.. Assertion Completed");

	}

	/**
	 * Deletes a boxilla user
	 * @param driver
	 * @param userName
	 * @throws InterruptedException
	 */
	public void deleteBoxillaUser(WebDriver driver, String userName) throws InterruptedException {
		log.info("Deleting added Boxilla user");
		navigateToSystemAdmin(driver);
		SystemAll.boxillaUsersTab(driver).click();
		log.info("Boxilla user add: Clicked on Boxilla Users Tab");
		timer(driver);
		SystemAll.searchboxBoxillaUsers(driver).sendKeys(userName);
		log.info("Delete Boxilla User - Username Entered in Search box");
		timer(driver);
		SystemAll.boxillaUserBreadCrumb(driver, userName).click();
		log.info("Delete Boxilla User - User bread crumb clicked");
		timer(driver);
		SystemAll.boxillaUserDelete(driver).click();
		log.info("Delete Boxilla User - Delete button clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
		SystemAll.searchboxBoxillaUsers(driver).clear();
		SystemAll.searchboxBoxillaUsers(driver).sendKeys(userName);
		log.info("Delete Boxilla User - Username Entered in Search box");
		String boxillaUsersTable = SystemAll.boxillaUsersTable(driver).getText();
		Assert.assertFalse(boxillaUsersTable.contains(userName),
				"Boxilla Users table contained: " + userName + ", actual text: " + boxillaUsersTable);
		log.info("Boxilla User deleted successfully");
	}

	/**
	 * Deletes all licenses if the delete button is available for them
	 * @param driver
	 * @throws InterruptedException
	 */
	public void deletLicense(WebDriver driver) throws InterruptedException {
		log.info("Deleting license");
		navigateToLicense(driver);
		timer(driver);

		log.info("Checking if additional licenses available");
		List<WebElement> deleteBtn = driver.findElements(By.xpath(".//*[@data-original-title='Delete license']"));
		log.info("Total Delete Elements found : " + deleteBtn.size());
		int i = 0;
		while (i < deleteBtn.size()) {
			timer(driver);
			SystemAll.deleteLicenseBtn(driver).click();
			Alert alert = driver.switchTo().alert();
			alert.accept();
			Thread.sleep(5000);
			String notificationMessage = Users.notificationMessage(driver).getText();
			Assert.assertTrue(notificationMessage.contains("Successfully deleted license"),
					"Notification Message did not contain: Successfully deleted license, actual text: " + notificationMessage );
			timer(driver);
			i++;
		}
		log.info("Total " + i + " License deleted");
	}

	/**
	 * 	 Select Name of metric you want to change from: audioBW , droppedFrame,
	 * 	framesPerSecond, RTT, totalBW, usbBW, userLatency, videoBW
	 * @param driver
	 * @param thresholdName
	 * @param warningThreshold
	 * @param critcalThreshold
	 * @param maxThreshold
	 * @throws InterruptedException
	 */
	public void changeThresholdsValue(WebDriver driver, String thresholdName, String warningThreshold,
			String critcalThreshold, String maxThreshold) throws InterruptedException {
		navigateToSystemSettings(driver);
		String id = "";
		switch (thresholdName) {
		case "Audio BW" :
			id = "5";
			break;
		case "Dropped Frames" :
			id = "7";
			break;
		case "Frames Per Second" :
			id = "6";
			break;
		case "RTT" :
			id = "2";
			break;
		case "Total BW" :
			id = "8";
			break;
		case "USB BW" :
			id = "4";
			break;
		case "User Latency" :
			id = "1";
			break;
		case "Video BW" :
			id = "3";
			break;
		}
		timer(driver);
		SystemAll.thresholdsTab(driver).click();
		log.info("Changing Thresholds - Clicked on Thresholds Tab");
		timer(driver);
		SeleniumActions.seleniumSendKeys(driver, SystemAll.thresholdsSearchBox, thresholdName);
		timer(driver);
		SystemAll.editBtnThresholds(driver, thresholdName).click();
		log.info("Changing Thresholds - Edit button clicked");
		timer(driver);

		//change warning thresholds
		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.get2kWarningTextBox(id));
		SeleniumActions.seleniumSendKeys(driver, SystemAll.get2kWarningTextBox(id), warningThreshold);

		//change critical thresholds
		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.get2kCriticalTextBox(id));
		SeleniumActions.seleniumSendKeys(driver, SystemAll.get2kCriticalTextBox(id), critcalThreshold);

		//change max threshold
		SeleniumActions.seleniumSendKeysClear(driver, SystemAll.get2kMaxTextBox(id));
		SeleniumActions.seleniumSendKeys(driver, SystemAll.get2kMaxTextBox(id), maxThreshold);

		SystemAll.saveBtnThreshold(driver, thresholdName).click();
		log.info("Changing Thresholds - Save button Clicked.. Asserting changes..");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		timer(driver);
		String notificationMessage = Users.notificationMessage(driver).getText();
		Assert.assertTrue(notificationMessage.contains("Successfully updated threshold"),
				"Notification Message did not contain: Successfully updated threshold, actual text: " + notificationMessage);
		timer(driver);
		driver.navigate().refresh();
		timer(driver);
		SystemAll.thresholdsTab(driver).click();
		log.info("Changing Thresholds - Navigated to Threshold Tab");
		String thresholdRow = SystemAll.thresholdRow(driver, thresholdName).getText();
		Assert.assertTrue(SystemAll.thresholdRow(driver, thresholdName).getText().contains(maxThreshold),
				"ThresholdRow did not contain: " + maxThreshold + ", actual text: " + thresholdRow);
		log.info("Changing Thresholds - Assertion completed successfully");
	}

	/**
	 * Add maximum Users based on License limit
	 * @param driver
	 * @param boxillaIp
	 * @throws InterruptedException
	 */
	public void usersLicenseLimitTest(WebDriver driver, String boxillaIp) throws InterruptedException {
		navigateToLicense(driver);
		timer(driver);
		int currentLicenseLimit = Integer.parseInt(SystemAll.currentLicenseLimt(driver).getText());
		log.info("Current License Limit is : " + currentLicenseLimit);
		userMethods.navigateToUsersManage(driver);
		timer(driver);
		int currentUsers = Integer.parseInt(Users.totalAvailableUser(driver).getText());
		log.info("Current Active Users are: " + currentUsers);
		if (currentUsers < currentLicenseLimit) {
			int requiredUsers = currentLicenseLimit - currentUsers;
			log.info("Adding required " + requiredUsers + " Users..");
			log.info("Creating user");
			for (int i = 0; i <requiredUsers; i++) {			
		        UserCreation user = new UserCreation();
				user.username = "test_user_"+i;
				user.password = "test";
				user.privilege = "Administrator";
				user.remote_access = "Yes";
				user.auto_connect = "No";
				//user.auto_connect_name = "";
				 RestAssured.useRelaxedHTTPSValidation();
				Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.body(user)
				.post("https://"+boxillaManager +"/bxa-api/users/kvm")
				.then().assertThat().statusCode(201)
				.body("message", equalTo("Created user " + user.username + ".")).extract().response();
				SaveResponseStatistics(getUri(), REQUEST_TYPE.POST, response);
			}
		
//			executeLicensetestRakeTask(driver, "BXAMGR_USERS", requiredUsers, boxillaIp);
			// BXAMGR_USERS is argument passed in SSH command
		} else {
			log.info(
					"Users count is equal or higher than license limit.. Asserting if Add User button is disabled");
			timer(driver);
			Assert.assertFalse(Users.addUser(driver).isDisplayed(), "***** Add User button is Enabled *****");
			log.info("Add User Button is Disabled.. Successfully added maximum user");
		}
		log.info("User adding completed.. Asserting if Add User button is disabled..");
		driver.navigate().refresh();
		timer(driver);
		Assert.assertFalse(Users.addUser(driver).isDisplayed(), "***** Add User button is Enabled *****");
		log.info("Add User Button is Disabled.. Successfully added maximum user");
		log.info("Clearing all the users");		
		clearentries();
		log.info("Entries cleared.. Asserting if Add User button is Enabled..");
		driver.navigate().refresh();
		timer(driver);
		Assert.assertTrue(Users.addUser(driver).isDisplayed(), "***** Add User Button is still disabled *****");
		log.info("Asserted.. Add User button enabled again.");
	}

	/* Add maximum Connection based on License limit */
	public void connectionsLicenseLimitTest(WebDriver driver, String boxillaIp) throws InterruptedException {
		
		navigateToLicense(driver);
		timer(driver);
		int currentLicenseLimit = Integer.parseInt(SystemAll.currentLicenseLimt(driver).getText());
		log.info("Current License Limit is : " + currentLicenseLimit);
		connectionMethods.navigateToConnectionsManage(driver);
		timer(driver);
		// Extract total available connections
		int currentConnections = 0;
		if (SeleniumActions.seleniumGetText(driver, Connections.totalAvailableConnection2).contains("1")) {
			// if one or more connection present
			currentConnections = Integer.parseInt(SeleniumActions.seleniumGetText(driver, Connections.totalAvailableConnection));
		} else if (SeleniumActions.seleniumGetText(driver, Connections.totalAvailableConnection).contains("0")) {
			// if no connection present
			currentConnections = Integer.parseInt(SeleniumActions.seleniumGetText(driver, Connections.totalAvailableConnection2));
		} else {
			throw new SkipException("*** Error in Exracting numbers of current available connections");
		}
		log.info("Current Active Connections are: " + currentConnections);
		if (currentConnections < currentLicenseLimit) {
			int requiredConnection = currentLicenseLimit - currentConnections;
			log.info("Adding required " + requiredConnection + " Connections..");
			log.info("Adding Required Connection");
			for (int j = 0; j < requiredConnection; j++) 
			{
				CreateKvmConnectionsConfig.CreateConnection con = config.new CreateConnection();
				con.name="ConnectionTest_"+j;
//				con.host="10.211.130.86";		
				con.zone= "";
				con.host= txIp;
				con.group= "ConnectViaTx";
				con.connection_type= "Private";
				con.view_only= "No";
				con.extended_desktop= "No";
				con.usb_redirection= "No";
				con.audio= "No";
				con.persistent= "No";
				con.cmode= "0";
				 RestAssured.useRelaxedHTTPSValidation();
					Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
					.when().contentType(ContentType.JSON)
					.body(con)
					.post(config.getUri(boxillaManager))
					.then().assertThat().statusCode(201)
					.body("message", equalTo("Created " + con.group +" connection "+ con.name + ".")).extract().response();
					SaveResponseStatistics(config.getUri(boxillaManager), REQUEST_TYPE.POST, response);
				

			}
			
			//executeLicensetestRakeTask(driver, "BXAMGR_CONNECTIONS", requiredConnection, boxillaIp);
	       Thread.sleep(3000);
			// BXAMGR_CONNECTIONS is argument passed in SSH command
		} else {
			log.info(
					"Connections count is equal or higher than license limit.. Asserting if Add Connection button is disabled");
			timer(driver);
			Assert.assertTrue(SeleniumActions.getElement(driver, Connections.btnAddConnection).isDisplayed(),
					"***** Add Connection button is Enabled *****");
			log.info("Add Connection Button is Disabled.. Successfully added maximum connection");
		}
		log.info("License rake task completed.. Asserting if Add Connection button is disabled..");
		driver.navigate().refresh();
		timer(driver);
		Assert.assertTrue(SeleniumActions.getElement(driver, Connections.btnAddConnection).isDisplayed(),
				"***** Add Connection button is Enabled *****");
		log.info("Add Connection Button is Disabled.. Successfully added maximum Connections");

		deleteAllConnection();
		Thread.sleep(3000);
		log.info("Entries cleared.. Asserting if Add Connection button is Enabled..");
		driver.navigate().refresh();
		timer(driver);
		Assert.assertTrue(SeleniumActions.getElement(driver, Connections.btnAddConnection).isDisplayed(),
				"***** Add Connection Button is still disabled *****");
		log.info("Asserted.. Add Connection button enabled again.");
	}

	/**
	 * Starts a rake task on boxilla to create a number of boxilla users or connections
	 * DatabaseTable value should be either BXAMGR_USERS or BXAMGR_CONNECTIONS
	 * @param driver
	 * @param databaseTable
	 * @param requiredUsers
	 * @param boxillaIp
	 */
	private void executeLicensetestRakeTask(WebDriver driver, String databaseTable, int requiredUsers, String boxillaIp) {
		try {
			if (requiredUsers <= 325) {

				//stop script
				String stopOutput = sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, "initctl stop cloudium-example-licensetest");				
				Thread.sleep(15000);
				log.info("Rake task stopped");
				// Converting Decimal to Hex to pass with SSH command
				String Hex = Integer.toHexString(requiredUsers);
				// Create command to execute bat file
				String command = "initctl start cloudium-example-licensetest " + databaseTable + "=" + Hex;
				String startOutput = sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, command);
				if(startOutput.contains("start/running")) {					
					log.info("Started rake task : cloudium-example-licensetest.. Waiting for 1 minutes");
					Thread.sleep(60000);
				}
				//stop again
				sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, "initctl stop cloudium-example-licensetest");
				Thread.sleep(15000);
				log.info("Rake task stopped");
			} else {
				log.info("License limit is higher than 300.. Skipping test");
				throw new SkipException("***** Required users is higher than 300 *****");
			}
		} catch (Exception e) {
			log.info(e);
			throw new SkipException("***** Couln't find license limit ******");
		}
	}

	/* Clear database entries created by rake task */
	/**
	 * Clears the database enteries created by the rake task
	 * @param driver
	 * @param boxillaIp
	 * @throws InterruptedException
	 */
	private void clearentries() throws InterruptedException {
//		System.out
//		.println("Clear entries created using license rake task.. Waiting for 1 minute before refreshing page");
//		sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, "initctl start cloudium-example-licensetest");
//		Thread.sleep(60000);
//		sendCommandToBoxilla(StartupTestCase.boxillaUsername, StartupTestCase.boxillaPassword, boxillaIp, "initctl stop cloudium-example-licensetest");
//		Thread.sleep(15000);
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.delete("https://" + boxillaManager  + "/bxa-api/users/kvm/all")
				.then().assertThat().statusCode(200)
				.body("message", equalTo("Successfully deleted all users.")).extract().response();
	}
	private void deleteAllConnection()
	{
		Response response = given().auth().preemptive().basic(boxillaRestUser, boxillaRestPassword).headers(BoxillaHeaders.getBoxillaHeaders())
				.when().contentType(ContentType.JSON)
				.delete("https://" + boxillaManager  + "/bxa-api/connections/kvm/all")
				.then().assertThat().statusCode(200)
				.body("message", equalTo("Successfully deleted all connections.")).extract().response();
	}
	

	/* Change System Clock */
	/**
	 * Changes system clock
	 * @param driver
	 * @throws InterruptedException
	 */
	public void changeClock(WebDriver driver) throws InterruptedException {
		navigateToSystemSettings(driver);
		timer(driver);
		SystemAll.clockTab(driver).click();
		log.info("Changing Clock - Cicked on clock tab");
		timer(driver);
		String currentDate = SystemAll.clockText(driver).getText();
		SystemAll.calendarBtn(driver).click();
		log.info("Changing Clock - Calendar button clicked");
		timer(driver);
		((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", SystemAll.clockIcon(driver));
		SystemAll.clockIcon(driver).click();
		log.info("Changing Clock - Clock icon clicked");
		timer(driver);
		SystemAll.hourUpIcon(driver).click();
		log.info("Changing Clock - Hour up icon clicked");
		timer(driver);
		SystemAll.dateSubmit(driver).click();
		log.info("Changing Clock - Submit button clicked");
		Thread.sleep(10000);
		navigateToSystemSettings(driver);
		timer(driver);
		SystemAll.clockTab(driver).click();
		log.info("Changing Clock - Cicked on clock tab");
		timer(driver);
		Assert.assertFalse(currentDate.equalsIgnoreCase(SystemAll.clockText(driver).getText()),
				"***** Clock change Assertion failed *****");
		log.info("Changing Clock - Clock change assertion completed");
	}

	/**
	 * Reboots boxilla from boxilla 
	 * @param driver
	 * @throws InterruptedException
	 */
	public void boxillaReboot(WebDriver driver) throws InterruptedException {
		navigateToSystemAdmin(driver);
		timer(driver);
		SystemAll.rebootBtn(driver).click();
		log.info("Reboot button clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Rebooting Boxilla... Please wait one minute then refresh.");
		new WebDriverWait(driver,200).until(ExpectedConditions.presenceOfElementLocated(By.xpath(Loginpage.userNameXpath)));
		int retry = 0;

		while(retry < 2) {
			try {
				Loginpage.username(driver).sendKeys("admin");
				log.info("Username entered");
				timer(driver);
				Loginpage.password(driver).sendKeys("admin");
				log.info("Password entered");
				timer(driver);
				Loginpage.loginbtn(driver).click();
				log.info("Clicked on Login Button");
				timer(driver);
				Assert.assertTrue(Landingpage.dashboard(driver).isDisplayed(), "Landing page did not load ");
				retry = 2;
			}
			catch(Exception | AssertionError e) {
				e.printStackTrace();
				log.info("Error loggin back in. Will wait one minute and retry once:" + retry);
				Thread.sleep(60000);
				try {
					driver.get("https://10.10.10.147/users/login");
				}catch(Exception f) {
					f.printStackTrace();

				}
				timer(driver);
				retry ++ ;
			}
		}

	}

	/**
	 * Reboot boxilla and assert that day is not present in uptime
	 * @param driver
	 * @throws InterruptedException
	 */
	public void boxillaRebootAndAssert(WebDriver driver) throws InterruptedException {
		boxillaReboot(driver);
		navigateToSystemAdmin(driver);
		timer(driver);
		SystemAll.systemInfo(driver).click();
		log.info("System Info Tab clicked");
		timer(driver);
		log.info(SystemAll.systemInfoTable(driver).getText());
		String systemInfoTable = SystemAll.systemInfoTable(driver).getText();
		Assert.assertFalse(systemInfoTable.contains("hour"),
				"System info table contained: hor, actual text: " +  systemInfoTable);
		log.info("Reboot assertion completed");
	}

	/**
	 * used to ping an ipaddress. returns true is destination is pings back
	 * @param ipAddress
	 * @return
	 */
	public boolean pingIpAddress(String ipAddress) {
		Runtime runtime = Runtime.getRuntime();
		String cmds = "ping " + ipAddress;

		Process proc;

		try {
			proc = runtime.exec(cmds);
			proc.getOutputStream().close();
			InputStream inputStream = proc.getInputStream();
			InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
			BufferedReader reader = new BufferedReader(inputStreamReader);

			String line;

			while((line = reader.readLine()) != null) {
				if(line.contains("Reply from " + ipAddress + ":")) {
					log.info("Pinging " + ipAddress + " is successful");
					return true;
				}
			}
		}catch (IOException e) {
			log.info("Pinging " + ipAddress + " is unsuccessful");
			e.printStackTrace();
		}
		return false;
	}

	/**
	 * Sends a command over SSH to Boxilla
	 * @param user login user name of boxilla
	 * @param password login password for boxilla 
	 * @param ipAddress IP address of the boxilla machine
	 * @param command Command to run on boxilla
	 * @return
	 */
	public String sendCommandToBoxilla(String user, String password, String ipAddress, String command) {
		Ssh shell = new Ssh(user, password, ipAddress);
		shell.loginToServer();
		String output = shell.sendCommand(command);
		shell.disconnect();
		return output;
	}

	/**
	 * Logs into boxilla through SSH and restarts the moy service
	 * @param user
	 * @param password
	 * @param ipAddress
	 * @throws InterruptedException
	 */
	public void restartMoyService(String user, String password, String ipAddress) throws InterruptedException{
		String output = sendCommandToBoxilla(user, password,
				ipAddress, "service moy restart");

		if(!output.contains("moy stop/waiting")) {
			Assert.fail("moy did not stop...Failing test");
		}
		log.info("Service restarted...Sleeping...");
		//sleep and wait for service to restart
		Thread.sleep(30000);
	}
	/**
	 * Backs up the boxilla database
	 * @param driver
	 * @throws InterruptedException
	 */
	public void backupDatabase(WebDriver driver) throws InterruptedException {
		navigateToSystemAdmin(driver);
		SystemAll.backupRestoreTab(driver).click();
		log.info("Backup and Restore Tab Clicked");
		SystemAll.backupBtn(driver).click();
		log.info("BackUp button clicked");
		Alert alert = driver.switchTo().alert();
		alert.accept();
		int i = 0;
		while (Landingpage.spinner(driver).isDisplayed() && i < 50) {
			log.info((i + 1) + ". Backup File is Downloading...");
			Thread.sleep(5000);
			i++;
		}
	}
	public  void checkBackup(WebDriver driver, String backuptimestamp) throws InterruptedException {
		timer(driver);
		navigateToSystemAdmin(driver);
		timer(driver);
		SystemAll.backupRestoreTab(driver).click();
		log.info("Backup and Restore Tab Clicked");
		SystemAll.searchboxBackupTable(driver).clear();
		log.info("Clear the BackUp SearchBox");
		timer(driver);
		SystemAll.searchboxBackupTable(driver).sendKeys(backuptimestamp);
		Thread.sleep(3000);
		log.info("Searching the BackupFile");
		 
		
	}
	
}
