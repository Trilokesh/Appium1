package com.getsocial.calcapp.automation.ui.test;


import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import com.getsocial.calcapp.automation.ui.CalcAppView;

public class TestDivision extends TestBase {
	
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
	public void testDivision1(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("8", "2");
		calcApp.clickDivision();
		
		Assert.assertEquals(calcApp.getResult(), "4");
	}
	
	@Test
	public void testDivision2(){
		CalcAppView calcApp = new CalcAppView(this);
		calcApp.populateArgs("1000", "60");
		calcApp.clickDivision();
		
		Assert.assertEquals(calcApp.getResult(), "16");
	}
}
