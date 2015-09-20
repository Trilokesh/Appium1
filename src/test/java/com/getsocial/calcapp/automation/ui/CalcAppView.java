package com.getsocial.calcapp.automation.ui;

import com.appium.automation.ui.AppPage;
import com.appium.automation.ui.test.AppUiTest;

public class CalcAppView extends AppPage {

	public CalcAppView(AppUiTest test) {
		super(test);
		initViewElements("calcapp");
		if (!waitForApp()){
			throw new IllegalStateException("Calc App is not visible");		
		}	
	}

	public boolean waitForApp() {
		return wait("APP_TEXT_VIEW", 10000);
	}
	
	public boolean populateArgs(String arg1, String arg2){
		if(enterArg1(arg1)){
			if(enterArg2(arg2)){
				return true;
			}
		}
		return false;
	}
	
	public boolean enterArg1(String arg1){
		return type("ARG1_TEXT_BOX", arg1);
	}
	
	public boolean enterArg2(String arg2){
		return type("ARG2_TEXT_BOX", arg2);
	}
	
	public boolean clickSubtract(){
		return click("SUBTRACT_BUTTON");
	}
	
	public boolean clickDivision(){
		return click("DIVISION_BUTTON");
	}
	
	public void calcSync(){
		wait(2000);
	}
	
	public String getResult(){
		System.out.println("RESULT1::................................." + getProperty("RESULT_TEXT_VIEW", "text"));
		return getProperty("RESULT_TEXT_VIEW", "text");
	}
	
	public boolean checkFailureMessage(){
		if(wait("FAILURE_MSG", 5000)){
			return true;
		}
		return false;
	}
	
	public boolean handleExceptionWindow(){
		return click("OK_BUTTON");
	}
}
