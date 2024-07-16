package methods;

import org.apache.log4j.Logger;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;

import extra.SeleniumActions;
import invisaPC.Rest.Alerts;
import objects.Cluster;
import objects.Cluster.CLUSTER_INFO_TABLE_COLUMNS;
import objects.Cluster.CLUSTER_NODE_COLUMNS;
import objects.Landingpage;
import objects.Switch;
/**
 * This class contains all the methods needed to interact with 
 * the cluster page in Boxilla
 * @author Boxilla
 *
 */
public class ClusterMethods {

	final static Logger log = Logger.getLogger(ClusterMethods.class);

	/**
	 * This will navigate to the cluster page from anywhere in Boxilla
	 * @param driver
	 */
	public void navigateToCluster(WebDriver driver) {
		log.info("Attempting to navigate to cluster");
		SeleniumActions.seleniumClick(driver, Landingpage.clusterTabXpath);
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOfElementLocated(By.xpath(Cluster.getNodeInfoTabXpath())));
		log.info("Successfully navigated to cluster");
	}
	/**
	 * This will prepare a boxilla to be a standby boxilla in a cluster.
	 * @param driver
	 * @param masterBoxillaIp - The IP address of the master boxilla
	 * @param nodeId - The node ID to use for the standby boxilla
	 * @param nodeName - The name of the node for the standby Boxilla 
	 * @throws InterruptedException 
	 */
	public void prepareStandByBoxilla(WebDriver driver, String masterBoxillaIp, String nodeId, String nodeName) throws InterruptedException {
		log.info("Attempting to prepare active standby");
		navigateToCluster(driver);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getPrepareStandByBtnXpath(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getPrepareStandByBtnXpath());
		log.info("Waiting for prepare active standby pop up to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getStandByPrepareStandbyBtn(driver)));
		log.info("Pop up appeared. Entering details");
		//clear textboxes
		SeleniumActions.seleniumSendKeysClear(driver, Cluster.getStandyMasterBoxillaIpTextBoxXpath());
		SeleniumActions.seleniumSendKeysClear(driver, Cluster.getStandByNodeIdTextBoxXpath());
		SeleniumActions.seleniumSendKeysClear(driver, Cluster.getStandByNodeNameTextBoxXpath());

		SeleniumActions.seleniumSendKeys(driver, Cluster.getStandyMasterBoxillaIpTextBoxXpath(), masterBoxillaIp);
		SeleniumActions.seleniumSendKeys(driver, Cluster.getStandByNodeIdTextBoxXpath(), nodeId);
		SeleniumActions.seleniumSendKeys(driver, Cluster.getStandByNodeNameTextBoxXpath(), nodeName);
		SeleniumActions.seleniumClick(driver, Cluster.getStandByPrepareStandbyBtnXpath());
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
//		Thread.sleep(8000);
//		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
//		log.info("Toast message: " + toast);
//		Assert.assertTrue(toast.contains("StandBy Boxilla successfully prepared."), "Toast did not contain success");
	}
	/**
	 * This will prepare a boxilla to be a master Boxilla in a cluster
	 * @param driver
	 * @param clusterId - The ID to use for the cluster
	 * @param virtualIp - The virtual IP to use for the cluster
	 * @param nodeId - The node ID to use for the cluster
	 * @param nodeName - The node name to use for the cluster
	 */
	public void prepareMasterBoxilla(WebDriver driver, String clusterId, String virtualIp, String nodeId, String nodeName) {
		log.info("Attempting to prepare master boxilla");
		navigateToCluster(driver);
		SeleniumActions.seleniumClick(driver, Cluster.getPrepareMasterBtnXpath());
		log.info("Waiting for pop up to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getMasterPrepPrepareMasterBtn(driver)));
		log.info("Pop up appeared. Entering master details");
		SeleniumActions.seleniumSendKeys(driver, Cluster.getMasterPrepClusterIdTextBoxXpath(), clusterId);
		SeleniumActions.seleniumSendKeys(driver, Cluster.getMasterPrepVirtualIpTextBoxXpath(), virtualIp);
		SeleniumActions.seleniumSendKeys(driver, Cluster.getMasterPrepNodeIdTextBoxXpath(), nodeId);
		SeleniumActions.seleniumSendKeys(driver, Cluster.getMasterPrepNodeNameTextBoxXpath(), nodeName);
		SeleniumActions.seleniumClick(driver, Cluster.getMasterPrepPrepareMasterBtnXpath());
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
		log.info("Toast message: " + toast);
		Assert.assertTrue(toast.contains("Master Boxilla successfully prepared."), "Toast did not contain success");
		log.info("Cluster dissolved successfully");
	}
	/**
	 * This will dissolve a Boxilla cluster
	 * @param driver
	 */
	public void dissolveCluster(WebDriver driver) {
		log.info("Attempting to dissolve cluster");
		navigateToCluster(driver);
		SeleniumActions.seleniumClick(driver, Cluster.getDissolveCluserBtnXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner has disappeared. Getting toast notification");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Cluster.getToastNotification(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
		log.info("Toast message: " + toast);
		Assert.assertTrue(toast.contains("Boxilla cluster has been successfully dissolve"), "Toast did not contain success");	
	}
	/**
	 * This will search for a specific node
	 * @param driver
	 * @param search
	 */
	public void searchNodeInfo(WebDriver driver, String search) {
		log.info("Attempting to search node info");
		navigateToCluster(driver);
		SeleniumActions.seleniumSendKeysClear(driver, Cluster.getNodeInfoSearchBoxXpath());
		SeleniumActions.seleniumSendKeys(driver, Cluster.getNodeInfoSearchBoxXpath(), search);
	}
	/**
	 * This will search cluster info table
	 * @param driver
	 * @param search
	 */
	public void searchClusterInfoTable(WebDriver driver, String search) {
		log.info("Attempting to search cluster info table");
		navigateToCluster(driver);
		SeleniumActions.seleniumClick(driver, Cluster.getClusterInfoTabXpath());
		SeleniumActions.seleniumSendKeysClear(driver, Cluster.getClusterInfoSearchBoxXpath());
		SeleniumActions.seleniumSendKeys(driver, Cluster.getClusterInfoSearchBoxXpath(), search);
	}
	/**
	 * This will return a string of text from a column in the cluster 
	 * info table
	 * @param driver
	 * @param search - String to search for 
	 * @param columns - Column to get the text from
	 * @return - Text from column
	 * @throws InterruptedException 
	 */
	public String getClusterInfoTableColumn(WebDriver driver, String search, CLUSTER_INFO_TABLE_COLUMNS columns) throws InterruptedException {
		searchClusterInfoTable(driver, search);
		int columnIndex = 1;

		switch(columns) {
		case CLUSTER_ID:
			log.info("Getting cluster ID from table");
			columnIndex = 1;
			break;
		case VIRTUAL_IP :
			log.info("Getting virtual IP from table");
			columnIndex = 2;
			break;
		}
		Thread.sleep(3000);
		String textFromTable = Cluster.getClusterInfoTableColumn(driver, columnIndex).getText();
		Thread.sleep(3000);
		log.info("value from table : " + textFromTable);
		return textFromTable;	
	}
	/**
	 * This method will get test from the cluster table depending on which 
	 *  column is passed
	 * @param driver
	 * @param search
	 * @param column
	 * @return
	 * @throws InterruptedException 
	 */
	public String getNodeInfoTableColumn(WebDriver driver,String search, CLUSTER_NODE_COLUMNS column) throws InterruptedException {
		searchNodeInfo(driver, search);
		int columnIndex = 1;

		switch(column) {
		case IP_ADDRESS :
			log.info("Getting IP Address from table");
			columnIndex = 1;
			break;
		case MAC :
			log.info("Getting MAC from table");
			columnIndex = 2;
			break;
		case HOST :
			log.info("Getting Host from table");
			columnIndex = 5;
			break;
		case SOFTWARE_VERSION :
			log.info("Getting software version from table");
			columnIndex = 6;
			break;
		case LIMIT:
			log.info("Getting limit from table");
			columnIndex = 7;
			break;
		case STATE:
			log.info("Getting state from table");
//			columnIndex = 9;
			columnIndex = 8;
			break;

			//when boxilla is a master
		case NODE_ID :
			log.info("Getting Node ID from table");
			columnIndex = 3;
			break;
		case NODE_NAME :
			log.info("Getting Node Name from table");
			columnIndex = 4;
			break;
		}
		Thread.sleep(3000);
		String textFromTable = Cluster.getNodeInfoTableColumn(driver, columnIndex).getText();
		log.info("value from table : " + textFromTable);
		return textFromTable;	
	}
	public void makeMasterStandAlone(WebDriver driver) {

	}
	/**
	 * This will log into a boxilla and if it is a master boxilla
	 *  it will make the boxilla stand alone
	 * @param driver
	 * @param boxillaIp - The IP address of the boxilla to make stand alone
	 */
	public void makeStandbyStandAlone(WebDriver driver, String boxillaIp) {
		log.info("Attemping to make standby boxilla with IP " + boxillaIp + " standalone");
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getMakeStandbyStandaloneDropdownBtnXpath(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getMakeStandbyStandaloneDropdownBtnXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
		log.info("Toast message: " + toast);
		Assert.assertTrue(toast.contains("Standalone Boxilla successfully prepared"), "Toast did not contain 'Standalone Boxilla successfully prepared', actual " + toast);
		log.info("Standby successfully ");	
	}
	/**
	 * This will log into a failed boxilla that is now back online 
	 * and change its status from failed to standby
	 * @param driver
	 * @param boxillaIp  -The IP address of the failed Boxilla
	 */
	public void prepareStandbyFailedBoxilla(WebDriver driver, String boxillaIp) {
		log.info("Attemping to make a failed boxilla with IP " + boxillaIp + " standby");
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath()); 
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getPrepareStandbyFailedBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getPrepareStandbyFailedBtnXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
		log.info("Toast message: " + toast);
		Assert.assertTrue(toast.contains("Standby Boxilla successfully prepared"), "Toast message did not contain 'Standby Boxilla successfully prepared', actual, " + toast);
		log.info("Failed boxilla is now standby again");
	}

	/**
	 * This will log into a standby Boxilla and detach it from the cluster
	 * @param driver
	 * @param boxillaIp - The IP address of the Boxilla to detach
	 */
	public void detachStandBy(WebDriver driver, String boxillaIp) {
		log.info("Attemping to detach standby boxilla with IP " + boxillaIp);
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getDetachStandbyDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getDetachStandbyDropdownBtnXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner has disappeared.");
		log.info("Successfully detached standby");
	}
	
	public void switchoverToActive(WebDriver driver, String boxillaIp)
	{
		log.info("Attemping to Switchover standby boxilla with IP " + boxillaIp);
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getDetachStandbyDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getswitchoverToActiveDropdownBtnXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner has disappeared.");
		log.info("Successfully Switch over StandBy to Active");
		
	}
	
	/**
	 * This will log into a boxilla that has failed and detach it from the cluster
	 * @param driver
	 * @param boxillaIp - The IP address of the boxilla to detach
	 */
	public void detachFailedBoxilla(WebDriver driver, String boxillaIp) {
		log.info("Attemping to detach failed boxilla with IP " + boxillaIp);
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getDetachFailedBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.detachFailedBtnXpath);
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner has disappeared.");
		log.info("Boxilla with IP " + boxillaIp + " detached from cluster");
	}

	/**
	 * This will switch over boxillas in a cluster
	 * @param driver
	 * @throws InterruptedException 
	 */
	public void switchoverBoxilla(WebDriver driver) throws InterruptedException {
		log.info("Attempting to switchover boxilla");
		navigateToCluster(driver);
		Thread.sleep(10000);
		SeleniumActions.seleniumClick(driver, Cluster.getSwitchoverBtnpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getSwitchoverFormBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getSwitchoverFormBtnXpath());

		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Switchover complete");


	}
	/**
	 * This will change a Boxilla that has failed in a cluster and 
	 *  make it a stand alone Boxilla
	 * @param driver
	 * @param boxillaIp - The IP address of the failed Boxilla
	 */
	public void makeFailedBoxillaStandalone(WebDriver driver, String boxillaIp) {
		log.info("Attemping to make failed boxilla with IP " + boxillaIp + " a standalone boxilla");
		searchNodeInfo(driver, boxillaIp);
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getNodeDropdownBtn(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getNodeDropdownBtnXpath());
		new WebDriverWait(driver, 60).until(ExpectedConditions.elementToBeClickable(Cluster.getMakeFailedStandAlone(driver)));
		SeleniumActions.seleniumClick(driver, Cluster.getMakeFailedStandAloneXpath());
		Alert alert = driver.switchTo().alert();
		alert.accept();
		log.info("Waiting for spinner to appear");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner appeared. Waiting for it to disappear");
		new WebDriverWait(driver,180).until(ExpectedConditions.invisibilityOf(Switch.getSpinner(driver)));
		log.info("Spinner has disappeared. Getting toast notification");
		new WebDriverWait(driver, 60).until(ExpectedConditions.visibilityOf(Cluster.getToastNotification(driver)));
		log.info("Toast message appeared. Getting text and asserting ");
		String toast = SeleniumActions.seleniumGetText(driver, Cluster.getToastNotificationXpath());
		log.info("Toast message: " + toast);
		Assert.assertTrue(toast.contains("Standalone Boxilla successfully prepared"),
				"toast did not contain 'Standalone Boxilla successfully prepared', actual ," + toast);
		log.info("Failed boxilla has been made standalone");
	}
}
