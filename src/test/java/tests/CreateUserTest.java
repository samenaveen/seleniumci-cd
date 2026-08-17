package tests;

import java.util.Map;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.CreateUserPage;
import pages.LoginPage;
import util.ExcelUtil;

public class CreateUserTest extends BaseTest {

	@Test(dataProvider = "getRowData")
	public void verifyCreateUserTest(Map<String, String> testdata) throws Exception {
		
		LoginPage loginPage = new LoginPage(getDriver(), getExtentTest());
		launchUrl(getProperty("application_url"));
		loginPage.login(getProperty("username"), getProperty("password"));
		loginPage.isLoginSuccessful();
		
		CreateUserPage createUserPage = new CreateUserPage(getDriver(), getExtentTest());
		createUserPage.createUser(testdata);
		
		loginPage.logout();
		loginPage.isLogoutSuccessful();
	}
}
