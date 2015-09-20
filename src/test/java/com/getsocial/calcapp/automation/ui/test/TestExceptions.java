package com.getsocial.calcapp.automation.ui.test;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.getsocial.calcapp.automation.ui.CalcAppView;

public class TestExceptions extends TestBase {
	
	/*@DataProvider(name="dataSetForBillTest")
	public Object[][] createDatSet(){
		TxnBeans txnBean = new TxnBeans();
		txnBean.setAmount("15.00");
		txnBean.setVendorName("vendors"+getRandomNumber(3));
		//txnBean.setItemName("Sales Tax Payable");
		txnBean.setItemName("Advertising");
		txnBean.setItemDescription("Sales Item");
		
		return new Object[][] {
				{txnBean}
		};
	}*/
	
	@Test
	public void testException1(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.clickSubtract();
		try{
			calcApp.getResult();
		}catch(Exception e){
			appCrashed=true;
			if(calcApp.checkFailureMessage()){
				calcApp.handleExceptionWindow();
				Assert.assertFalse(calcApp.checkFailureMessage(), "Calc App Crashed");
			}
			else{
				Assert.assertTrue(calcApp.waitForApp(), "Calc App Not Crashed");
			}
		}
	}
	
	@Test
	public void testException2(){
		driver.resetApp();
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.enterArg2("2");
		calcApp.clickSubtract();
		try{
			calcApp.getResult();
		}catch(Exception e){
			appCrashed=true;
			if(calcApp.checkFailureMessage()){
				calcApp.handleExceptionWindow();
				Assert.assertFalse(calcApp.checkFailureMessage(), "Calc App Crashed");
			}
			else{
				Assert.assertTrue(calcApp.waitForApp(), "Calc App Not Crashed");
			}
		}
	}
	
	@Test
	public void testException3(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.enterArg1("5");
		calcApp.clickSubtract();
		try{
			calcApp.getResult();
		}catch(Exception e){
			appCrashed=true;
			if(calcApp.checkFailureMessage()){
				calcApp.handleExceptionWindow();
				Assert.assertFalse(calcApp.checkFailureMessage(), "Calc App Crashed");
			}
			else{
				Assert.assertTrue(calcApp.waitForApp(), "Calc App Not Crashed");
			}
		}
	}
	
	@Test
	public void testException4(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("8", "0");
		calcApp.clickDivision();
		try{
			calcApp.getResult();
		}catch(Exception e){
			appCrashed=true;
			if(calcApp.checkFailureMessage()){
				calcApp.handleExceptionWindow();
				Assert.assertFalse(calcApp.checkFailureMessage(), "Calc App Crashed");
			}
			else{
				Assert.assertTrue(calcApp.waitForApp(), "Calc App Not Crashed");
			}
		}
	}
}
