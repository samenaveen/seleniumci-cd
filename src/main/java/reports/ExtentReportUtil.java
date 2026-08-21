package reports;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;

public class ExtentReportUtil {

    private static ExtentReports extentReports;
    private static final String REPORT_PATH = "test-output/ExtentReport.html";

    public static ExtentReports setupExtentReport() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(REPORT_PATH);
        extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("OS", System.getProperty("os.name"));
        extentReports.setSystemInfo("User", System.getProperty("user.name"));
        return extentReports;
    }

    public static void takeScreenshot(WebDriver driver, ExtentTest extentTest, String screenshotName, String details) throws Exception {
        try {
            TakesScreenshot takesScreenshot = (TakesScreenshot) driver;
            String base64Screenshot = takesScreenshot.getScreenshotAs(OutputType.BASE64);
            extentTest.addScreenCaptureFromBase64String(base64Screenshot, screenshotName + " - " + details);
        } catch (Exception e) {
            extentTest.fail("Failed to take screenshot: " + e.getMessage());
            throw e;
        }
    }
}
