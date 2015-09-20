package com.appium.automation.ui;

import io.appium.java_client.AppiumDriver;

import java.util.List;

import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import com.appium.automation.ui.test.AppUiTest;

/**
 * This class provides functionality common to all web pages, no matter what implementation (html, flex or whatever else)
 * 
 *
 */
public abstract class AppiumAbstractPage extends AppView {
	
	private static Logger logger = Logger.getLogger(AppiumAbstractPage.class);

	protected AppiumDriver driver = null;
	
	public AppiumAbstractPage(AppUiTest test) {
		super(test);
		driver = test.getAppiumDriver();
		if (driver == null){
			logger.info("Attempting to use page object when web driver is null.");
		}
	}

	public void switchToAppFrame(){
		switchToFrame("default,appIframe");
}

/**
 * @param iFramePath path to go get to desired iFrame comma separated
 */
public boolean switchToFrame(String iFramePath) {
	logger.info("Switching to frame: " + iFramePath);
	String [] iFramePathArray = iFramePath.split(",");
	try{
		for (String curIframeName : iFramePathArray){
			if (curIframeName.equals("default")){
				driver.switchTo().defaultContent();
			}
			else{
				driver.switchTo().frame(curIframeName);
//				FirefoxDriver oldDriver = (FirefoxDriver) driver;
//				oldDriver.get(oldDriver.getCurrentUrl());
//				FirefoxDriver newDriver = (FirefoxDriver) driver.switchTo().frame(curIframeName);
//				driver = newDriver;
			}
		}
	}
	catch (NoSuchFrameException e) {
		logger.info(e.getMessage());
		e.printStackTrace();
		return false;
	}	
	return true;
}

/**
 * @param refFrameName if null, search will take a place from the default frame
 * 
 */
public void logAllFrames(String refFramePath){
	if (refFramePath == null){
		refFramePath = "default";
	}
	switchToFrame(refFramePath);
	List<WebElement> frames = null;
	frames = driver.findElements(By.tagName("IFRAME"));
	if (frames == null){
		//no more children, print my path
		logger.info(refFramePath);
	}
	else{
		//step through the children and do a recursive call
		for (WebElement frame : frames){
			refFramePath += "," + frame.getAttribute("id");
			logAllFrames(refFramePath);
		}
	}
}
}
