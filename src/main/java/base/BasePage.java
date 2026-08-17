package base;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.aventstack.extentreports.ExtentTest;

import reports.ExtentReportUtil;

public class BasePage extends AbstractBase {

	private WebDriver driver;
	private ExtentTest extentTest;
	private WebDriverWait explicitWait;
	protected static final Logger logger = LoggerFactory.getLogger(BasePage.class);

	public BasePage(WebDriver driver, ExtentTest extentTest) {
		this.driver = driver;
		this.extentTest = extentTest;
		PageFactory.initElements(driver, this);
		this.explicitWait = new WebDriverWait(driver, Duration.ofSeconds(10));
	}

	public WebDriver getDriver() {
		return driver;
	}

	protected ExtentTest getExtentTest() {
		return extentTest;
	}
	
	public WebDriverWait getExplicitWait() {
		return explicitWait;
	}

	protected void takeScreenshot(String details) throws Exception {
		String screenshotName = "Screenshot_" + System.currentTimeMillis();
		ExtentReportUtil.takeScreenshot(getDriver(), getExtentTest(), screenshotName, details);
	}
	
	
	/**
	 * Enters text into a web element located by the given locator.
	 * 
	 * @param locator The By locator of the web element.
	 * @param text    The text to enter into the web element.
	 */
	public void enterText(By locator, String text, long timeoutInSeconds) {
		try {
			WebElement element = getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.visibilityOfElementLocated(locator));
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			System.out.println("Exception occurred while entering text: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Enters text into a web element located by the given locator.
	 * 
	 * @param locator The By locator of the web element.
	 * @param text    The text to enter into the web element.
	 */
	public void enterText(WebElement element, String text, long timeoutInSeconds) {
		try {
			logger.debug("entering text in the element : {}  {}", text, element.toString());
			getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.visibilityOf(element));
			element.clear();
			element.sendKeys(text);
		} catch (Exception e) {
			logger.error("Exception occurred while entering text: {} " , e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Clicks on a web element located by the given locator.
	 * 
	 * @param locator The By locator of the web element.
	 */
	public void click(By locator, long timeoutInSeconds) {
		try {
			WebElement ele = getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.elementToBeClickable(locator));
			ele.click();
		} catch (Exception e) {
			logger.error("Exception occurred while clicking element: " + e.getMessage());
			throw e;
		}
	}
	
	/**
	 * Clicks on a web element located by the given locator.
	 * 
	 * @param locator The By locator of the web element.
	 */
	public void click(WebElement ele, long timeoutInSeconds) {
		try {
			getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.elementToBeClickable(ele));
			ele.click();
		} catch (Exception e) {
			logger.error("Exception occurred while clicking element: " + e.getMessage());
			throw e;
		}
	}
	
	
	public boolean isElementDisplayed(By locator, long timeoutInSeconds) {
		try {
			getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.visibilityOfElementLocated(locator));
			return true;
		} catch (Exception e) {
			logger.error("Exception occurred while checking element visibility: " + e.getMessage());
			return false;
		}
	}
	
	public boolean isElementDisplayed(WebElement ele, long timeoutInSeconds) {
		try {
			getExplicitWait().withTimeout(Duration.ofSeconds(timeoutInSeconds))
			.until(ExpectedConditions.visibilityOf(ele));
			return true;
		} catch (Exception e) {
			logger.error("Exception occurred while checking element visibility: " + e.getMessage());
			return false;
		}
	}
}
