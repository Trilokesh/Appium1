package com.appium.automation.ui;

import java.util.Calendar;

import org.apache.log4j.Logger;

import com.appium.automation.AppiumWaitHandler;

public class AppUiActionFactory {
	
	private static long lastActionTime;
	private static Logger logger = Logger.getLogger(AppUiActionFactory.class);
	
	/**
	 * @param handler callback test function to check condition to stop waiting
	 * @return true if success before timeout
	 */
	public static boolean waitOnWaitHandler(AppView view, long timeout, String timeOutMessage, AppiumWaitHandler handler){
		long waitIncr = view.getDefaultWaitIncr();
		return waitOnWaitHandler(waitIncr, timeout, timeOutMessage, handler);
	}
	
	/**
	 * @param handler callback test function to check condition to stop waiting
	 * @return true if success before timeout
	 */
	public static boolean waitOnWaitHandler(long waitIncr, long timeout, String timeOutMessage, AppiumWaitHandler handler){
		long startTime = Calendar.getInstance().getTimeInMillis();
		while (Calendar.getInstance().getTimeInMillis() < startTime + timeout){
			if (handler.testIfWaitFinished(Calendar.getInstance().getTimeInMillis() - startTime)){
				return true;
			}
			try {
				logger.info("Sleeping for "+waitIncr+" ms.");
				Thread.sleep(waitIncr);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		if (timeOutMessage != null)
			logger.info(timeOutMessage+", timeout = " + timeout/1000 + " seconds");
		return false;
	}
	
	public static boolean waitOnWaitHandler(AppView view, AppiumWaitHandler handler){
		return waitOnWaitHandler(view, view.getDefaultWait(), null, handler);
	}
	
	public static boolean waitOnWaitHandler(AppView view, String timeoutMessage, AppiumWaitHandler handler){
		return waitOnWaitHandler(view, view.getDefaultWait(), timeoutMessage, handler);
	}
}
