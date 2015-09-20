package com.getsocial.calcapp.automation.ui.test;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.getsocial.calcapp.automation.ui.CalcAppView;

public class TestSubtract extends TestBase {
	
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
	public void testSubtract1(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("8", "2");
		calcApp.clickSubtract();
		
		Assert.assertEquals(calcApp.getResult(), "6");
	}
	
	@Test
	public void testSubtract2(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("0", "2");
		calcApp.clickSubtract();
		
		Assert.assertEquals(calcApp.getResult(), "-2");
	}
	
	@Test
	public void testSubtract3(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("0", "0");
		calcApp.clickSubtract();
		
		Assert.assertEquals(calcApp.getResult(), "0");
	}
}
