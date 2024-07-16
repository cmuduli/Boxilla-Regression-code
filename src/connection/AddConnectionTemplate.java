package connection;

import java.io.IOException;

/**
 * Class with tests for connection templates in Boxilla
 */

import org.apache.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import extra.SeleniumActions;
import extra.StartupTestCase;
import extra.StartupTestCase2;
import methods.ConnectionsMethods;
import objects.Connections;
import objects.Connections.VIA;

/**
 * Class that contains tests for Connection templates in boxilla
 * @author Brendan O Regan
 *
 */

public class AddConnectionTemplate extends StartupTestCase {

	ConnectionsMethods methods = new ConnectionsMethods();
	final static Logger log = Logger.getLogger(AddConnectionTemplate.class);
	private String dataFileLocation = "C:\\Test_Workstation\\SeleniumAutomation\\templateData.txt";

	/**
	 * Adds private connection template, via TX, all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "smoke", "chrome", "quick"})
	public void test01_addTxConnectionTemplateAllEnabled() throws InterruptedException { // Add Connection Template - Via TX, Private, all enabled
		log.info("Test Case-03 Started - Adding Connection Template TX - Private All Options Enabled");
		methods.addConnectionTemplate(driver, "tx", "testtemplate1");
		methods.addTemplateChooseConnectionType(driver, "private");
		methods.addTemplateEnableExtendedDesktop(driver);
		methods.addTemplateEnableUSBRedirection(driver);
		methods.addTemplateEnableAudio(driver);
		methods.addTemplateEnablePersistenConnection(driver);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template TX - Private All Options Enabled - Test Case-03 Completed");
	}

	/**
	 * Add Connection Template - Via TX, Private, all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test02_addTxConnectionTemplateAllDisabled() throws InterruptedException { // Add Connection Template - Via TX, Private, all
		// disabled
		log.info("Test Case-04 Started - Adding Connection Template TX - Private All Options disabled");
		methods.addConnectionTemplate(driver, "tx", "testtemplate2");
		methods.addTemplateChooseConnectionType(driver, "private");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template TX - Private All Options disabled - Test Case-04 Completed");
	}

	/**
	 * Add Connection Template - Via TX, Shared, all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test03_addSharedTxConnectionTemplateAllEnabled() throws InterruptedException { // Add Connection Template - Via TX, Shared, all enabled
		log.info("Test Case-05 Started - Adding Connection Template TX - Shared All Options Enabled");
		methods.addConnectionTemplate(driver, "tx", "testtemplate3");
		methods.addTemplateChooseConnectionType(driver, "shared");
		methods.addTemplateEnableExtendedDesktop(driver);
		methods.addTemplateEnablePersistenConnection(driver);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template TX - Shared All Options Enabled - Test Case-05 Completed");
	}

	/**
	 * Add Connection Template - Via TX, Shared, all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test04_addSharedTxConnectionTemplateAllDisabled() throws InterruptedException { // Add Connection Template - Via TX, Shared, all disabled
		log.info("Test Case-06 Started - Adding Connection Template TX - Shared All Options disabled");
		methods.addConnectionTemplate(driver, "tx", "testtemplate4");
		methods.addTemplateChooseConnectionType(driver, "shared");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template TX - Shared All Options disabled - Test Case-06 Completed");
	}

	/**
	 * Add Connection Template - Via Broker, all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test05_addBrokerConnectionTemplateAllEnabled() throws InterruptedException { // Add Connection Template - Via Broker, all enabled
		log.info("Test Case-07 Started - Adding Connection Template Broker - All Options Enabled");
		methods.addConnectionTemplate(driver, "broker", "testtemplate5");
		methods.addTemplateDomainName(driver, "blackbox");
		methods.addTemplateLoadBalanceInfo(driver, "loadbalance");
		methods.addTemplateEnableExtendedDesktop(driver);
		methods.addTemplateEnableUSBRedirection(driver);
		methods.addTemplateEnableAudio(driver);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template Broker - All Options Enabled - Test Case-07 Completed");
	}

	/**
	 * Add Connection Template - Via Broker, all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test06_addBrokerConnectionTemplateAllDisabled() throws InterruptedException { // Add Connection Template - Via Broker, all disabled
		log.info("Test Case-08 Started - Adding Connection Template Broker - All Options Disabled");
		methods.addConnectionTemplate(driver, "broker", "testtemplate6");
		methods.addTemplateDomainName(driver, "blackbox");
		methods.addTemplateLoadBalanceInfo(driver, "loadbalance");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template Broker - All Options Disabled - Test Case-08 Completed");
	}

	/**
	 * Add Connection Template - Via VM, all options enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test07_addVmConnectionTemplateAllEnabled() throws InterruptedException { // Add Connection Template - Via VM, all enabled
		log.info("Test Case-09 Started - Adding Connection Template Via VM - All Options Enabled");
		methods.addConnectionTemplate(driver, "vm", "testtemplate7");
		methods.addTemplateDomainName(driver, "blackbox");
		methods.addTemplateEnableExtendedDesktop(driver);
		methods.addTemplateEnableUSBRedirection(driver);
		methods.addTemplateEnableAudio(driver);
		methods.addTemplateEnableNLA(driver);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template via VM - All Options Enabled - Test Case-09 Completed");
	}

	/**
	 * Add Connection Template - Via VM, all options disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test08_addVmConnectionTemplateAllDisabled() throws InterruptedException { // Add Connection Template - Via VM, all options disabled
		log.info("Test Case-10 Started - Adding Connection Template Via VM - All Options Disabled");
		methods.addConnectionTemplate(driver, "vm", "testtemplate8");
		methods.addTemplateDomainName(driver, "blackbox");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template via VM - All Options Disabled - Test Case-10 Completed");
	}

	/**
	 * Add Connection Template - Via VM Pool, all options Enabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test09_addVmPoolConnectionTemplateAllEnabled() throws InterruptedException { // Add Connection Template - Via VM Pool, all Enabled
		log.info("Test Case-11 Started - Adding Connection Template VM Pool- All Options Enabled");
		methods.addConnectionTemplate(driver, "vmpool", "testtemplate9");
		methods.addTemplateEnableExtendedDesktop(driver);
		methods.addTemplateEnableUSBRedirection(driver);
		methods.addTemplateEnableAudio(driver);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template VM Pool - All Options Enabled - Test Case-11 Completed");
	}

	/**
	 * Add Connection Template - Via VM Pool, all options Disabled
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test10_addVmPoolConnectionTemplateAllDisabled() throws InterruptedException { // Add Connection Template - Via VM Pool, all Disabled
		log.info("Test Case-12 Started - Adding Connection Template VM Pool- All Options Disabled");
		methods.addConnectionTemplate(driver, "vmpool", "testtemplate10");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		log.info("Added Connection Template VM Pool - All Options Disabled - Test Case-12 Completed");
	}

	@Test(groups = {"boxillaFunctional"})
	public void test11_addVmHorizonTemplate() throws InterruptedException {
		log.info("test11_addVmHorizonTemplate");
		methods.addConnectionTemplate(driver, "horizon", "horizonTemplate");
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		methods.timer(driver);
		String message = SeleniumActions.seleniumGetText(driver, Connections.templateToastMessage);
		log.info("MESSAGE:" + message);
		Assert.assertTrue(message.contains("Template successfully created"), "Toast message did not contain success");
	}

	/**
	 * Add connection via TX using template
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional", "chrome"})
	public void test12_addTxConnectionUsingTemplate() throws InterruptedException { // Create Connection - TX type using template
		log.info("Test Case-23 Started - Creating Connection Via TX using template");
		methods.addConnection(driver, "testconnection11", "yes");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateTX),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "testconnection11");
		log.info("Connection Via TX using template Created - Test Case-23 Completed");
	}

	/**
	 * Add connection via VM using template
	 * @throws InterruptedException
	 */
	@Test(groups = {"boxillaFunctional"})
	public void test13_addVmConnectionUsingTemplate() throws InterruptedException { // Create Connection - VM type using template
		log.info("Test Case-24 Started - Creating Connection Via VM using template");
		methods.addConnection(driver, "testconnection12", "yes");
		methods.connectionInfo(driver, "vm", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateVM),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "testconnection12");
		log.info("Connection Via VM using template Created - Test Case-24 Completed");
	}

	/**
	 * Add connection via VM pool using template
	 * @throws InterruptedException
	 */
	//@Test(groups = {"boxillaFunctional"})
	public void test14_addVmPoolConnectionUsingTemplate() throws InterruptedException { // Create Connection - VM Pool type using template
		log.info("Test Case-25 Started - Creating Connection Via VM Pool using template");
		methods.addConnection(driver, "testconnection13", "yes");
		methods.connectionInfo(driver, "vmpool", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateVMPool),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "testconnection13");
		log.info("Connection Via VM Pool using template Created - Test Case-25 Completed");
	}

	/**
	 * Add connection via broker using template
	 * @throws InterruptedException
	 */
	//@Test(groups = {"boxillaFunctional"})
	public void test15_addBrokerConnectionUsingTemplate() throws InterruptedException { // Create Connection - Broker type using template
		log.info("Test Case-26 Started - Creating Connection Via Broker using template");
		methods.addConnection(driver, "testconnection14", "yes");
		methods.connectionInfo(driver, "broker", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateBroker),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "testconnection14");
		log.info("Connection Via Broker using template Created - Test Case-26 Completed");
	}
	@Test(groups = {"boxillaFunctional"})
	public void test16_addVmHorizonViaTemplate() throws InterruptedException {
		methods.addConnection(driver, "horizonTemplateConnection", "yes");
		methods.connectionInfo(driver, "horizon", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateHorizon),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNext(driver);
		methods.saveConnection(driver, "horizonTemplateConnection");
	}


	//	@Test
	public void test17_editTemplateVia() throws InterruptedException {
		log.info("Create a TX template, assign it to a connection, then change to a VM");
		String templateName = "editTemplate";
		methods.addConnectionTemplate(driver, "tx", templateName);
		SeleniumActions.seleniumClick(driver, Connections.addTemplateSavebtn);
		//create connection to use this template
		methods.addConnection(driver, "testEditTemplate", "yes");
		methods.connectionInfo(driver, "tx", "user", "user", txIp);
		// Assert if Template Selection drop down available
		Assert.assertTrue(SeleniumActions.seleniumIsDisplayed(driver, Connections.connectionTemplateTX),
				"***** Template List not Displayed *****");
		methods.propertyInfoClickNextWithTemplate(driver, templateName, VIA.TX);
		String connectionDetails = methods.saveConnection(driver, "testEditTemplate");
		log.info("Asserting that the template was attached to the connection");
		Assert.assertTrue(connectionDetails.contains(templateName), "Connection did not contain template name. Actual: " + connectionDetails);
		Assert.assertTrue(connectionDetails.contains("ConnectViaTx"), "Connection via was not correct. Expected ConnectViaTx. Actual: " + connectionDetails);
		log.info("Template has been applied to connection. Editing template via and chekcing again");
		methods.editConnectionTemplateVia(driver, templateName, VIA.VM);
		methods.navigateToConnectionsManage(driver);
		//search for connection
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, "testEditTemplate");
		methods.timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains(templateName), "Connection did not contain template name. Actual: " + connectionTableText);
		Assert.assertTrue(connectionTableText.contains("VM"), "Connection via was not correct. Expected VM. Actual: " + connectionTableText);
	}
	
	//@Test
	public void test18_editTemplateName() throws InterruptedException {
		methods.editConnectionTemplateName(driver, "editTemplate", "editTemplateChanged");
		methods.navigateToConnectionsManage(driver);
		//search for connection
		SeleniumActions.seleniumSendKeys(driver, Connections.searchTextbox, "testEditTemplate");
		methods.timer(driver);
		String connectionTableText = SeleniumActions.seleniumGetText(driver, Connections.connectionTable);
		Assert.assertTrue(connectionTableText.contains("editTemplateChanged"), "Template name was not changed. Expceted editTemplateChanged_template, actual: " + connectionTableText);
	}

	@DataProvider(name = "DP1")
	public Object[][] data() throws IOException { 
		return readData(dataFileLocation);
	}

	@Test(dataProvider="DP1")
	public void createTemplate(String templateName, String via, String type, String isExtendedDesktop, 
			String isUSBR, String isAudio, String isPersistent, String isViewOnly) throws InterruptedException {
		methods.masterCreateTemplate(templateName, via, type, isExtendedDesktop, isUSBR, isAudio, isPersistent,
				isViewOnly, driver);

		methods.addConnection(driver, templateName + "_con", "yes");
		methods.connectionInfo(driver, via, "user", "user", txIp);
		methods.selectTemplateForConnection(driver, templateName, via, templateName + "_con");
	}	
}
