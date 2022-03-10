package android;

import com.browserstack.local.Local;
import java.net.URL; import java.util.*;
import io.appium.java_client.android.*;
import org.openqa.selenium.support.ui.*;
import org.openqa.selenium.remote.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.RemoteWebDriver;

public class BrowserStackSampleLocal {
	
  private static Local localInstance;
  public static String userName = "YOUR_USERNAME";
  public static String accessKey = "YOUR_ACCESS_KEY";


  public static void setupLocal() throws Exception {
    localInstance = new Local();
    Map<String, String> options = new HashMap<String, String>();
    options.put("key", accessKey);
    localInstance.start(options);
  }

  public static void tearDownLocal() throws Exception {
    localInstance.stop();
  }

	public static void main(String[] args) throws Exception {
      // Start the BrowserStack Local binary
      setupLocal();

      DesiredCapabilities capabilities = new DesiredCapabilities();

    	// Set your access credentials
    	capabilities.setCapability("browserstack.user", userName);
    	capabilities.setCapability("browserstack.key", accessKey);

    	// Set URL of the application under test
    	capabilities.setCapability("app", "bs://<app-id>");

    	// Specify device and os_version for testing
    	capabilities.setCapability("device", "Google Pixel 3");
    	capabilities.setCapability("os_version", "9.0");

      // Set the browserstack.local capability to true
      capabilities.setCapability("browserstack.local", true);

      // Set other BrowserStack capabilities
    	capabilities.setCapability("project", "First Java Project");
    	capabilities.setCapability("build", "browserstack-build-1");
    	capabilities.setCapability("name", "local_test");
       

   	  // Initialise the remote Webdriver using BrowserStack remote URL
    	// and desired capabilities defined above
        RemoteWebDriver driver = new RemoteWebDriver(new URL("http://hub.browserstack.com/wd/hub"), capabilities);

        // Test case for the BrowserStack sample Android Local app. 
        // If you have uploaded your app, update the test case here.   
        WebElement searchElement = new WebDriverWait(driver, 30).until(
            ExpectedConditions.elementToBeClickable(By.id("com.example.android.basicnetworking:id/test_action")));
        searchElement.click();
        WebElement insertTextElement = (WebElement) new WebDriverWait(driver, 30).until(
            ExpectedConditions.elementToBeClickable(By.className("android.widget.TextView")));

        WebElement testElement = null;
        List<WebElement> allTextViewElements = driver.findElements(By.className("android.widget.TextView"));
        Thread.sleep(10);
        for(WebElement textElement : allTextViewElements) {
          if(textElement.getText().contains("The active connection is")) {
            testElement = textElement;
          }
        }

        if(testElement == null) {
          throw new Error("Cannot find the needed TextView element from app");
        }
        String matchedString = testElement.getText();
        System.out.println(matchedString);
        assert(matchedString.contains("The active connection is wifi"));
        assert(matchedString.contains("Up and running"));

        // Invoke driver.quit() after the test is done to indicate that the test is completed.
        driver.quit();

        // Stop the BrowserStack Local binary
        tearDownLocal();

	}

}
