package tests;

import java.net.MalformedURLException;
import java.net.URI;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.testng.annotations.Test;

public class GridTest {

	@Test
	public void verifyGridTest() throws Exception {
		ChromeOptions options = new ChromeOptions();
		WebDriver driver = new RemoteWebDriver(
				URI.create("http://localhost:4444/wd/hub").toURL(),
				options);
		driver.get("https://www.google.com");
		
		Thread.sleep(30000); // Just to observe the browser opening
		
	}
}
