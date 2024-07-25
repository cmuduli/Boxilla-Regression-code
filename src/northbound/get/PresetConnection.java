package northbound.get;

import org.apache.log4j.Logger;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import objects.Connections;

public class PresetConnection extends StartupTestCase{
	
	final static Logger log = Logger.getLogger(KvmActiveConnections.class);
	private ConnectionsMethods connections = new ConnectionsMethods();
	private String presetName = "firstPreset";
	private String privateConnectionName = "testPresetprivate";
	private String sharedConnectionName ="testPresetshared";
	
	@BeforeClass(alwaysRun = true)
	public void beforeclass() throws InterruptedException 
	{
		printSuitetDetails(false);
		getDevices();
		cleanUpLogin();
		connections.createTxConnection(privateConnectionName, "private", driver, txIp);
		connections.createTxConnection(sharedConnectionName, "shared", driver, txIp);
		String[] sourceList = { privateConnectionName };
		String[] destinationList = { singleRxName };
		connections.createPreset(driver, sourceList, destinationList, presetName, false);
		log.info("Asserting if preset has been created by checking if the preset button for " + presetName
				+ " is displayed");
		Assert.assertTrue(Connections.getPresetBtn(driver, presetName).isDisplayed(),
				"Button with preset name is not displayed, button name : " + presetName);

	}

	@Test
	public void firsttets() 
	{
		log.info("Beforeclass");
		
	}
	
	
}




