package pages;

import java.util.Map;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

import com.aventstack.extentreports.ExtentTest;

import base.BasePage;

public class CreateUserPage extends BasePage{

	public CreateUserPage(WebDriver driver, ExtentTest extentTest) {
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
	
	
	public void createUser(Map<String, String> userDetails) {
		String userRole = userDetails.get("UserRole");
	}
	
	
}
