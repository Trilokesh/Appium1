package com.appium.automation;

import java.util.ArrayList;

public class Logger {
	private static ArrayList<String> log = new ArrayList<String>();
	
	public static void log(String toLog){
		System.out.println(toLog);
		log.add(toLog);
	}
	
	public static void log (Object toLog){
		System.out.println(toLog.toString());
		log.add(toLog.toString());
	}

	public static void debug(String toLog) {
		if (PropertyReader.getPropertyBoolean("test.debug.mode", false)){
			if (toLog != null){
				log.add(toLog);
				System.out.println(toLog.toString());
			}
		}
	}
	
	public static String getLineFromEnd(int indexFromEnd){
		if (log.size()>indexFromEnd){
			return log.get(log.size()-indexFromEnd-1);
		}
		else
			return null;
	}

	public static void debug(Exception e) {
		if (PropertyReader.getPropertyBoolean("test.debug.mode", false)){
			System.out.println(e.getMessage());
			e.printStackTrace();
		}
	}
}
