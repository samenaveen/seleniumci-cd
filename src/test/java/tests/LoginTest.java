package tests;

import java.util.Map;

import org.testng.annotations.Test;

import base.BaseTest;
import pages.LoginPage;

public class LoginTest extends BaseTest {

	@Test
	public void verifyLoginTest() throws Exception {

		//List<Map<String, String>> sheetData = ExcelUtil.getSheetData("Login");

		//for (Map<String, String> data : sheetData) {
			LoginPage loginPage = new LoginPage(getDriver(), getExtentTest());
			launchUrl(getProperty("application_url"));
			loginPage.login(getProperty("username"), getProperty("password"));
			loginPage.isLoginSuccessful();
			loginPage.logout();
			loginPage.isLogoutSuccessful();
		//}
	}

	@Test(dataProvider = "getSheetData")
	public void verifyLoginTest1(Map<String, String> data) {

		//List<Map<String, String>> sheetData = ExcelUtil.getSheetData("Login");

//		LoginPage loginPage = new LoginPage(getDriver());
//		launchUrl(getProperty("application_url"));
//		loginPage.login(data.get("UserName"), data.get("Password"));
//		loginPage.isLoginSuccessful();
//		loginPage.logout();
//		loginPage.isLogoutSuccessful();
		
		System.out.println(data);
	}
}
