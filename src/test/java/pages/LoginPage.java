package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.testng.Assert;

import com.aventstack.extentreports.ExtentTest;

import base.BasePage;
import reports.ExtentReportUtil;
import util.GenericUtil;
import util.ScreenshotUtil;

public class LoginPage extends BasePage{

	public LoginPage(WebDriver driver, ExtentTest extentTest) {
		super(driver, extentTest);
	}
	
	@FindBy(name="username")
	private WebElement usernameTxtBx;
	
	@FindBy(name="password")
	private WebElement passwordTxtBx;
	
	@FindBy(className="orangehrm-login-button")
	private WebElement loginBtn;
	
	@FindBy(xpath="//h6[text()='Dashboard']")
	private WebElement dashboardHeader;
	
	//locators
//	By usernameTxtBx = By.name("username");
//	By passwordTxtBx = By.name("password");
//	By loginBtn = By.className("orangehrm-login-button");
//	By dashboardHeader = By.xpath("//h6[text()='Dashboard']");
	
	//methods
	
	public void login(String username, String password) {
		enterText(usernameTxtBx, username, 10);
		//File file = usernameTxtBx.getScreenshotAs(OutputType.FILE);
		enterText(passwordTxtBx, password, 10);
		getExtentTest().info("Entered username and password");
		click(loginBtn, 5);
		getExtentTest().info("Clicked on Login button");
		logger.info("Login method executed with username: {}", username);
	}
	
	public void logout() {
		click(By.className("oxd-userdropdown"), 5);
		click(By.linkText("Logout"), 5);
		logger.info("Logout method executed");
	}
	
	public void isLoginSuccessful() throws Exception {
		Assert.assertTrue(isElementDisplayed(dashboardHeader, 15), "Verify login is successful");
		getExtentTest().pass("Login successful, Dashboard is displayed");
		takeScreenshot("Screenshot after successful login");
	}
	
	public void isLogoutSuccessful() throws Exception {
		Assert.assertTrue(isElementDisplayed(usernameTxtBx, 15), "Verify logout is successful");
		takeScreenshot("LogoutSuccess");
	}
}
