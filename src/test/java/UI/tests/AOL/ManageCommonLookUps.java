package UI.tests.AOL;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;
import TestBase.UI.GetDriverInstance;
import UtilClasses.UI.ApplicationLogin;
import UtilClasses.UI.CommonUtils;
import UtilClasses.UI.NavigationTaskFlows;
import pageDefinitions.UI.oracle.applcore.qa.Aolcore.LookupsWrapper;
import pageDefinitions.UI.oracle.applcore.qa.Aolcore.ManageDocumentSequence;

public class ManageCommonLookUps extends GetDriverInstance {

	private String id;
	private WebDriver isoDriver = null;
	private ApplicationLogin appLogin = null;
	private NavigationTaskFlows taskFlowsNavigation = null;
	private LookupsWrapper isoUtils = null;
	private JavascriptExecutor js = null;
	private ManageDocumentSequence docPage = null;
	

	@Parameters({ "user", "pwd" })
	@BeforeClass
	public void setUp(String user, String passWord) throws Exception {
		try {
			isoDriver =  getDriverInstanceObject();
			appLogin = new ApplicationLogin(isoDriver);
			taskFlowsNavigation = new NavigationTaskFlows(isoDriver);
			isoUtils = new LookupsWrapper(isoDriver);
			js = (JavascriptExecutor) isoDriver;
			docPage = new ManageDocumentSequence(isoDriver);
			id = CommonUtils.uniqueId();
			appLogin.login(user, passWord, isoDriver);
			CommonUtils.explicitWait(taskFlowsNavigation.getGlobalPageInstance().navigatorButton, "Click", "", isoDriver);
			CommonUtils.hold(4);
			
			if(ApplicationLogin.newsFeedEnabled) {
				taskFlowsNavigation.navigateToTask(appLogin.getGloablePageTemplateInstance().setupandmaintenance, isoDriver);
			}else {
				CommonUtils.explicitWait(docPage.setupAndMaintananceElement, "Click", "", isoDriver);
				docPage.setupAndMaintananceElement.click();
				CommonUtils.hold(4);
			}
			taskFlowsNavigation.navigateToAOLTaskFlows("Manage Common Lookups", isoDriver);
			CommonUtils.hold(4);
			CommonUtils.explicitWait(isoDriver.findElement(By.xpath("//h1[contains(text(),'Manage Common Lookups')]")),"Click", "",isoDriver);
		} catch (Exception e) {
			System.out.println("Exception in initializing objects in ManageCommonLookUps class:  " + e.getMessage());
			e.printStackTrace();
		}
	}

	@Test(description = "Create Common LookupType")
	public void testcase01() throws Exception {
		CommonUtils.hold(2);
		isoUtils.createLookupType("STD_" + id, "MEAN_" + id, "DESC_" + id, "Common",isoDriver);
		Assert.assertTrue(isoUtils.verifyStatus("STD_" + id, "", "MEAN_" + id, "DESC_" + id,isoDriver), "DONE");
	}

	@Test(description = "Create Common Lookup code ", dependsOnMethods = { "testcase01" })
	public void testcase02() throws Exception {
		CommonUtils.hold(2);
		isoUtils.createLookupCode("STD_" + id, "STD_CODE_" + id, "MAEN_CODE_" + id, "DESC_CODE_" + id, "Common",isoDriver);
		Assert.assertTrue(isoUtils.verifyStatus("STD_" + id, "STD_CODE_" + id, "MAEN_CODE_" + id, "DESC_CODE_" + id,isoDriver),"LookUpCode created");
	}

	@Test(description = "Update common Lookuptype", dependsOnMethods = { "testcase02" })
	public void testcase03() throws Exception {
		isoUtils.updateLookupType("STD_" + id, "Update"+id, "update"+id,isoDriver);
		Assert.assertTrue(isoUtils.verifyStatus("STD_" + id, "", "Update"+id, "update"+id,isoDriver), "Updated");
	}

	@Test(description = "Update Common lookup Code", dependsOnMethods = { "testcase03" })
	public void testcase04() throws Exception {
		isoUtils.updateLookupCode("STD_" + id, "STD_CODE_" + id, "CODE_M", "CODE_D", "Common",isoDriver);
		Assert.assertTrue(isoUtils.verifyStatus("STD_" + id, "STD_CODE_" + id, "CODE_M", "CODE_D",isoDriver));

	}

	@Test(description = "Delete Common LookupCode", dependsOnMethods = { "testcase04" })
	public void testcase05() throws Exception {
		isoUtils.deleteLookupCode("STD_" + id, "STD_CODE_" + id,isoDriver);
		Assert.assertTrue(isoUtils.verifyDeleteCode("STD_" + id, "STD_CODE_" + id,isoDriver));

	}

	@Test(description = "Delete Common Lookup Type", dependsOnMethods = { "testcase05" })
	public void testcase06() throws Exception {
		isoUtils.deleteLookupType("STD_" + id,isoDriver);
		Assert.assertTrue(isoUtils.verifyDeleteType("STD_" + id,isoDriver));
	}

	@AfterClass(alwaysRun = true)
	public void logOut() throws Exception {
		isoDriver.quit();
	}
}
