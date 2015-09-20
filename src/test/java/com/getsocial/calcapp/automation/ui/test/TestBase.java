package com.getsocial.calcapp.automation.ui.test;



import java.net.MalformedURLException;

import org.testng.annotations.AfterClass;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import com.appium.automation.ui.test.AppUiTest;


public class TestBase extends AppUiTest {
	
	protected boolean appCrashed=false;
	
	@BeforeSuite
	public void initialSetup() throws MalformedURLException{
		logger.info("******************************  BEFORE SUITE **********************");
		super.initialSetUp();
	}
	
	@BeforeClass //(groups={"testsetup"})
	public void testSetUp(){
		logger.info("******************************  BEFORE CLASS **********************");
	}
	
	/**
	 * 
	 */
	@BeforeMethod //(groups={"testsetup"})
	public void methodSetUp(){
		
	}
	
	/**
	 * @throws InterruptedException 
	 * 
	 */
	@AfterMethod //(groups={"testcleanup"})
	public void methodCleanup() throws InterruptedException{
		if(appCrashed){
			driver.resetApp();
		}
		super.cleanUp();
	}

	/* (non-Javadoc)
	 * @see
	 */
	@AfterClass //(groups={"testcleanup"})
	public void cleanUp(){
		logger.info("******************************  AFTER CLASS **********************");
		
	}
	
	@AfterSuite
	public void finalCleanUp(){
		logger.info("******************************  AFTER SUITE **********************");
		super.finalCleanUp();
	}
	
}
