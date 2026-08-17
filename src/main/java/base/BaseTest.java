package base;

import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.testng.ITestContext;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.DataProvider;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;

import constants.Constants;
import reports.ExtentReportUtil;
import util.ExcelUtil;
import util.PropertyUtil;

public class BaseTest extends AbstractBase {

	
	ThreadLocal<WebDriver> threadLocalDriver = new ThreadLocal<>();
	ThreadLocal<ExtentTest> threadLocalTest = new ThreadLocal<>();
	protected static final Logger logger = LoggerFactory.getLogger(BaseTest.class);
	private static ExtentReports extentReport;

	@BeforeSuite
	public void beforeSuite() throws Exception {
		PropertyUtil.loadProperties();
		ExcelUtil.readExcel();
		extentReport = ExtentReportUtil.setupExtentReport();
	}

	@BeforeMethod
	public void createDriver(ITestContext context, Method method) throws MalformedURLException {
		WebDriver driver = null;
		Map<String, String> parameters = context.getCurrentXmlTest().getLocalParameters();
		String browser = parameters != null ? parameters.get("browser") : null;
		
		// Provide default browser if parameter is not set (e.g., when running from Eclipse)
		if (browser == null || browser.trim().isEmpty()) {
			browser = "chrome";
			logger.warn("Browser parameter not set in TestNG suite. Using default: chrome");
		}

		ExtentTest test = extentReport.createTest(method.getName());
		test.assignCategory(context.getCurrentXmlTest().getName());
		test.assignDevice(browser);
		threadLocalTest.set(test);

		boolean headless = Boolean.parseBoolean(getProperty("headless"));
		logger.info("Browser selected: " + browser + ", Headless: " + headless);

		String executionType = getProperty("executiontype");

		if ("local".equalsIgnoreCase(executionType)) {
			if ("chrome".equalsIgnoreCase(browser)) {
				ChromeOptions options = new ChromeOptions();
				if (headless) {
					options.addArguments("--headless=new");
				}
				driver = new ChromeDriver(options);
			} else if ("firefox".equalsIgnoreCase(browser)) {
				FirefoxOptions options = new FirefoxOptions();
				if (headless) {
					options.addArguments("--headless");
				}
				driver = new FirefoxDriver(options);
			} else if ("edge".equalsIgnoreCase(browser)) {
				EdgeOptions options = new EdgeOptions();
				if (headless) {
					options.addArguments("--headless=new");
				}
				driver = new EdgeDriver(options);
			} else if ("safari".equalsIgnoreCase(browser)) {
				driver = new SafariDriver();
			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
		} else if ("grid".equalsIgnoreCase(executionType)) {
			URL url = URI.create(getProperty("remoteurl")).toURL();
			if ("chrome".equalsIgnoreCase(browser)) {
				ChromeOptions options = new ChromeOptions();
				if (headless) {
					options.addArguments("--headless=new");
				}
				driver = new RemoteWebDriver(url, options);
			} else if ("firefox".equalsIgnoreCase(browser)) {
				FirefoxOptions options = new FirefoxOptions();
				if (headless) {
					options.addArguments("--headless");
				}
				driver = new RemoteWebDriver(url, options);
			} else if ("edge".equalsIgnoreCase(browser)) {
				EdgeOptions options = new EdgeOptions();
				if (headless) {
					options.addArguments("--headless=new");
				}
				driver = new RemoteWebDriver(url, options);
			} else if ("safari".equalsIgnoreCase(browser)) {
				SafariOptions options = new SafariOptions();
				if (headless) {
					logger.warn("Headless mode is not supported for Safari. Ignoring headless option.");
				}
				driver = new RemoteWebDriver(url, options);
			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
		} else if ("saucelabs".equalsIgnoreCase(executionType)) {
			URL url = URI.create(getProperty("saucelabsurl")).toURL();
			if ("chrome".equalsIgnoreCase(browser)) {
				ChromeOptions browserOptions = new ChromeOptions();
				if (headless) {
					browserOptions.addArguments("--headless=new");
				}
				browserOptions.setPlatformName("Windows 11");
				browserOptions.setBrowserVersion("latest");
				Map<String, Object> sauceOptions = getSauceOptions(method);
				browserOptions.setCapability("sauce:options", sauceOptions);
				driver = new RemoteWebDriver(url, browserOptions);
			} else if ("firefox".equalsIgnoreCase(browser)) {
				FirefoxOptions browserOptions = new FirefoxOptions();
				if (headless) {
					browserOptions.addArguments("--headless");
				}
				browserOptions.setPlatformName("Windows 11");
				browserOptions.setBrowserVersion("latest");
				Map<String, Object> sauceOptions = getSauceOptions(method);
				browserOptions.setCapability("sauce:options", sauceOptions);
				driver = new RemoteWebDriver(url, browserOptions);
			} else if ("edge".equalsIgnoreCase(browser)) {
				EdgeOptions browserOptions = new EdgeOptions();
				if (headless) {
					browserOptions.addArguments("--headless=new");
				}
				browserOptions.setPlatformName("Windows 11");
				browserOptions.setBrowserVersion("latest");
				Map<String, Object> sauceOptions = getSauceOptions(method);
				browserOptions.setCapability("sauce:options", sauceOptions);
				driver = new RemoteWebDriver(url, browserOptions);
			} else if ("safari".equalsIgnoreCase(browser)) {
				SafariOptions browserOptions = new SafariOptions();
				browserOptions.setPlatformName("Windows 11");
				browserOptions.setBrowserVersion("latest");
				Map<String, Object> sauceOptions = getSauceOptions(method);
				browserOptions.setCapability("sauce:options", sauceOptions);
				driver = new RemoteWebDriver(url, browserOptions);

			} else {
				throw new IllegalArgumentException("Unsupported browser: " + browser);
			}
		}

		else {
			throw new IllegalArgumentException("Unsupported execution type: " + executionType);
		}

		threadLocalDriver.set(driver);

		getDriver().manage().window().maximize();
		getDriver().manage().timeouts()
				.pageLoadTimeout(Duration.ofSeconds(Long.parseLong(getProperty("pageloadtimeout"))));
		getDriver().manage().timeouts().scriptTimeout(Duration.ofSeconds(Long.parseLong(getProperty("scripttimeout"))));

	}

	private Map<String, Object> getSauceOptions(Method method) {
		Map<String, Object> sauceOptions = new HashMap<>();
		sauceOptions.put("username", "oauth-venkatgopireddytech-d0cfe");
		sauceOptions.put("accessKey", "1170a5d7-2d06-4fb2-9acc-e0162c9a436f");
		sauceOptions.put("build", "Build-1");
		sauceOptions.put("name", method.getName());
		return sauceOptions;
	}

	public WebDriver getDriver() {
		return threadLocalDriver.get();
	}

	public ExtentTest getExtentTest() {
		return threadLocalTest.get();
	}

	@AfterMethod
	public void quitDriver(ITestResult result) throws Exception {

		String jobStatus = Constants.PASSED;
		if (result.getStatus() == ITestResult.FAILURE) {
			String screenshotName = "Screenshot_" + System.currentTimeMillis();
			ExtentReportUtil.takeScreenshot(getDriver(), getExtentTest(), screenshotName,
					result.getThrowable().toString());
			getExtentTest().fail("Test failed");
			jobStatus = "failed";
		} else if (result.getStatus() == ITestResult.SUCCESS) {
			getExtentTest().pass("Test passed");
		} else if (result.getStatus() == ITestResult.SKIP) {
			getExtentTest().skip("Test skipped: " + result.getThrowable());
			jobStatus = "skipped";
		}

		if (Constants.SAUCELABS.equalsIgnoreCase(getProperty("executiontype"))) {
			String sessionId = ((RemoteWebDriver) getDriver()).getSessionId().toString();
			String videoUrl = getProperty("saucelabsvideourl") + sessionId;

			// Attach the video URL to the Extent Report
			getExtentTest().info("Sauce Labs Video: <a href='" + videoUrl + "' target='_blank'>Watch Video</a>");

			JavascriptExecutor jsExecutor = (JavascriptExecutor) getDriver();
			jsExecutor.executeScript("sauce:job-result=" + jobStatus);
		}

		if (getDriver() != null) {
			getDriver().quit();
		}
	}

	@AfterSuite
	public void flushReport() {
		if (extentReport != null) {
			extentReport.flush();
		}
	}

	protected void launchUrl(String url) {
		logger.info("Launching URL: " + url);
		getDriver().get(url);
	}

	@DataProvider(name = "getRowData")
	public Object[][] getRowData(Method method) {
		Map<String, String> rowdata = ExcelUtil.getRowdata(method.getName());
//		Object[][] data = new Object[1][1];
//		data[0][0] = rowdata;
//		return data;

		return new Object[][] { { rowdata } };
	}

	@DataProvider(name = "getSheetData")
	public Object[][] getSheetData(Method method) {
		String sheetName = method.getName();
		List<Map<String, String>> sheetData = ExcelUtil.getSheetData(sheetName);

		Object[][] data = new Object[sheetData.size()][1];

		for (int i = 0; i < sheetData.size(); i++) {
			data[i][0] = sheetData.get(i);
		}
		return data;
	}

}
