package util;

import java.io.File;
import java.io.IOException;

import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

	public static String takeScreenshot(WebDriver driver, String screenshotName) throws IOException {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File file = ts.getScreenshotAs(OutputType.FILE);
		String path = System.getProperty("user.dir") + "/reports/screenshots/" + screenshotName + ".png";
	    try {
			FileUtils.copyFile(file, new File(path));
		} catch (IOException e) {
			throw e;
		}
	    return path;
	}
}
