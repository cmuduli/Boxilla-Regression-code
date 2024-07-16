package objects;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
/**
 * Hold all the page objects for Boxilla - active connection page
 * @author Boxilla
 *
 */
public class ActiveConnectionElements {
	
	private static WebElement element = null;
	
	//xpaths and object identifiers
	
	public static String numberOfDevicesOnlineXpath = "//span[contains(text(),'Devices On-Line')]/preceding-sibling::span[@id='stat-t-1']";
	public static String numberOfActiveConnectionsXpath = "//span[contains(text(),'Active Connections')]/preceding-sibling::span[@id='stat-t-1']"; 
	public static String activeConnectionsTableXpath = "//table[@id='DataTables_Table_0']";
	private static String connectionTableConnectionName = "//div[@id='performance_table']//tr//td[1]";
	//private static String connectionTableReceiver = "//div[@id='performance_table']//tr//td[2]";
	private static String connectionTableReceiver = "//div[@id='performance_table']//tr//td[3]";

//	private static String connectionTableUser = "//div[@id='performance_table']//tr//td[3]";
	private static String connectionTableUser = "//div[@id='performance_table']//tr//td[4]";

//	private static String connectionTableTransmitter = "//div[@id='performance_table']//tr//td[5]";
	private static String connectionTableTransmitter = "//div[@id='performance_table']//tr//td[6]";

	private static String connectionTableTransmitter2 = "//div[@id='active_table']//tr//tr//td[5]";
	private static String connectionTableConnectionTime = "//div[@id='active_table']//tr//td[6]";
	private static String connectionTableConnectionNetworkBW = "//div[@id='active_table']//tr//td[7]";
	private static String frameTableConnectionNetworkBW = "//div[@id='frame_table']//tr//td[7]";
	private static String configTableConnectionTime = "//div[@id='config_table']//tr//td[6]";
	private static String frameRateTableTab = "//div[contains(text(),'Frame Rate')]";
	private static String configurationTableTab = "//div[contains(text(),'Configuration')]";
	private static String activeConnectionSearchBox = "//div[@id='active_table']//div[@class='dataTables_header']//input";
	
	
	/**
	 * tr1 = first row
	 * td1 = first column
	 * @param row
	 * @param column
	 * @return
	 */
	public static String getRowAndColumnFromActiveConnectionTable(int row, int column) {
		return "//div[@id='active_table']//tr[" + row + "]//td[" + column + "]";
	}
	public static String getTransmitterFromSearch(String name) {
		return "//div[@id='active_table']//div[@id='DataTables_Table_0_wrapper']//div//td[contains(text(),'" + name + "')]";
	}
	
	public static String getActiveConnectionSearchBox() {
		return activeConnectionSearchBox;
	}
	public static String getConnectionTableTransmitter2() {
		return connectionTableTransmitter2;
	}
	public static String getConfigTableConnectionTime() {
		return configTableConnectionTime;
	}
	public static String getConfigurationTableTab() {
		return configurationTableTab;
	}
	public static String getFrameRateTableTab() {
		return frameRateTableTab;
	}
	public static String getFrameTableConnectionNetworkBW() {
		return frameTableConnectionNetworkBW;
	}
	
	public static String getConnectionTableConnectionNetworkBW() {
		return connectionTableConnectionNetworkBW;
	}
	public static String getConnectionTableConnectionTime() {
		return connectionTableConnectionTime;
	}
	public static String getConnectionTableTransmitter() {
		return connectionTableTransmitter;
	}
	
	public static String getConnectionTableUser() {
		return connectionTableUser;
	}
	
	public static String getConnectionTableReceiver() {
		return connectionTableReceiver;
	}
	public static String getConnectionTableConnectionName() {
		return connectionTableConnectionName;
	}
	/**
	 * Returns WebElement for the stat number of devices online
	 * @param driver
	 * @return
	 */
	public static WebElement noOfDevicesOnline(WebDriver driver) {
		return driver.findElement(By.xpath(numberOfDevicesOnlineXpath));
	}
	
	public static WebElement noOfActiveConnections(WebDriver driver) {
		return driver.findElement(By.xpath(numberOfActiveConnectionsXpath));
	}
	
	public static WebElement activeConnectionRow(WebDriver driver, int row) {
		WebElement table =  driver.findElement(By.xpath(activeConnectionsTableXpath));
		List<WebElement> tableRows = table.findElements(By.tagName("tr"));
		return tableRows.get(row);
	}
	
	
	
	
	

}
