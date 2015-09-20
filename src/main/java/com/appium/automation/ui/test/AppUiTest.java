package com.appium.automation.ui.test;

import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.remote.MobileCapabilityType;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.testng.reporters.TestHTMLReporter;

import com.appium.automation.PropertyReader;

public class AppUiTest {
	public static Logger logger = Logger.getLogger(AppUiTest.class);

	protected static AppiumDriver driver = null;
	protected static String baseUrl = getBaseUrl();
	protected static String signOutURL = "";
	private static ArrayList<Object> cleanUpList = new ArrayList<Object>();
	
	protected AppUiTest test = this;

	// constructor
	public AppUiTest() {}

	public AppiumDriver getAppiumDriver() {
		return driver;
	}

	public void initialSetUp() throws MalformedURLException {
		// Logger.debug("Running tests in the following class: "
		// + this.toString());

		// create an instance of driver::this will be parameter based soon
		if (driver == null) {
			driver = createAppiumDriver(setDesiredCapabilities());
		}
	}

	private AppiumDriver createAppiumDriver(DesiredCapabilities capabilities) throws MalformedURLException {
		driver = new AndroidDriver(new URL(baseUrl+ "/wd/hub"), capabilities);
		return driver;
	}
	
	private DesiredCapabilities setDesiredCapabilities(){
		DesiredCapabilities capabilities = new DesiredCapabilities().android();
		capabilities.setCapability(MobileCapabilityType.AUTOMATION_NAME, PropertyReader.getPropertyValue("test.automationname"));
        capabilities.setCapability(MobileCapabilityType.PLATFORM_NAME, PropertyReader.getPropertyValue("test.platform"));
        capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, PropertyReader.getPropertyValue("test.devicename"));
        capabilities.setCapability(MobileCapabilityType.VERSION, PropertyReader.getPropertyValue("test.version"));
        if(!StringUtils.isBlank(PropertyReader.getPropertyValue("test.app"))){
        	capabilities.setCapability(MobileCapabilityType.APP, PropertyReader.getPropertyValue("testHomeDir") + "/" +
        			PropertyReader.getPropertyValue("test.app"));
        }
        capabilities.setCapability(MobileCapabilityType.APP_PACKAGE, PropertyReader.getPropertyValue("test.pkg"));
        capabilities.setCapability(MobileCapabilityType.APP_ACTIVITY, PropertyReader.getPropertyValue("test.activity"));
        
        return capabilities;
        
	}

	public void finalCleanUp() {
		if (driver != null) {
			driver.quit();
			driver = null;
		}
	}

	protected void setUp() {

	}

	protected void cleanUp() {
		
	}

	// ****************************************************************************************//
	// ***************************HELPER
	// FUNCTIONS*********************************************//
	// ****************************************************************************************//
	private static String getPlatform() {
		String browser = PropertyReader.getPropertyValue("test.platform");
		if (browser == null || browser.equals("${test.platform}")) {
			browser = "Android";
		}
		return browser;
	}

	private static String getBaseUrl() {
		String bUrl = PropertyReader.getPropertyValue("appium.url");
		//logger.info("Base url: " + ippUrl);
		return bUrl;
	}

	

}
