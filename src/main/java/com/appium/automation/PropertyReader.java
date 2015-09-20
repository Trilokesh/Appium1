package com.appium.automation;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.Map.Entry;

import org.apache.commons.lang3.StringUtils;
import org.junit.Assert;
//import org.apache.log4j.Logger;


public class PropertyReader {

	private static Properties configProperties;
	private static String testHome;
	private static File testHomeDir = null;
	private static String ippUrl = null, appDbid = null, logDir = null, doPOST = null, testuserUsername = null,
		testuserPassword = null, testArtifactsDir = null;
	//public static Logger logger = Logger.getLogger(PropertyReader.class);
	/**
	 * Read configuration properties from config.properties file and initialize
	 * them
	 */
	static {
		Properties tempProps = new Properties();
		InputStream input = null;

		try {
			configProperties = 	System.getProperties();
			
			testHome = System.getProperty("user.dir");
			if (testHome != null) 
				testHomeDir = new File(testHome);
			else
				testHomeDir = new File(".");
			
			input = new FileInputStream(new File(testHomeDir, "src/test/resources/config.properties"));
			tempProps.load(input);
			for (Entry<Object, Object> prop : tempProps.entrySet()){
				if (configProperties.getProperty((String)prop.getKey()) == null){
					String value = (String)prop.getValue();
					configProperties.setProperty((String)prop.getKey(), (String)prop.getValue());
				}
			}
			configProperties.setProperty("testHomeDir", testHome);
			
			boolean replaced = false;
			do{
				//this time around no replacements made yet
				replaced = false;
				for (Entry<Object, Object> property  : tempProps.entrySet()){
					if (substituteProperty((String)property.getKey()))
						replaced = true;
				}
			}while(replaced);
			
		} catch (Exception err) {
			err.printStackTrace();
			throw new RuntimeException(
					"Failed to load config.properties file. "
							+ "Make sure the file exists in {TEST_HOME}/src/test/resources dir (" + testHomeDir.getAbsolutePath() + ")",
					err);
		} finally {
			if (input != null) {
				try {
					input.close();
				} catch (IOException e) {
					// ignore
				}
			}
		}
	}

	private PropertyReader() {
	}

	
	/**
	 * This function looks through properties to see if their values contain already defined properties. If so, it replaces ${prop} with already set property value
	 * 
	 * @param value
	 * @return true if replacement was made
	 */
	public static boolean substituteProperty(String property) {
		String value = System.getProperties().getProperty(property);
		String tempString = value;
		while (tempString.contains("${")){
			tempString = tempString.substring(tempString.indexOf("${")+2);
			String propertyName = tempString.substring(0, tempString.indexOf("}"));
			//if there is a value for replacement and not substituting itself
			if (System.getProperties().getProperty(propertyName) != null && !property.equals(propertyName)){
				value = value.replace("${"+propertyName+"}", System.getProperties().getProperty(propertyName));
				System.getProperties().setProperty(property, value);
				return true;
			}
		}
		return false;
	}

	/**
	 * Get all properties retrieved from config.properties file and command line
	 * 
	 * @return configProperties All config properties
	 */
	public static Properties getProperties() {
		return configProperties;
	}

	public static String getPropertyValue(String key) {
		return configProperties.getProperty(key);
	}
	
	public static int getPropertyInt(String key, int defVal) {
		String temp = getPropertyValue(key);
		int retVal = defVal;
		if (temp == null)
			return retVal;
		try{
			retVal = Integer.parseInt(temp);
			return retVal;
		}catch (Exception e) {
			e.printStackTrace();
			return retVal;
		}
	}
	
	public static boolean getPropertyBoolean(String key, boolean defVal) {
		String temp = getPropertyValue(key);
		boolean retVal = defVal;
		if (temp == null)
			return retVal;
		try{
			retVal = Boolean.parseBoolean(temp);
			return retVal;
		}catch (Exception e) {
			e.printStackTrace();
			return retVal;
		}
	}
	
	public static String getProperty(String key, String defVal){
		String retVal = getPropertyValue(key);
		if (retVal == null)
			retVal = defVal;
		return retVal;
	}

	/**
	 * Get a property from the config file, or return the default if the
	 * property is not found
	 * 
	 * @param key
	 *            The lookup key for the configuration file
	 * @param defVal
	 *            the default value if the entry is not found
	 * 
	 * @return The found value or the default
	 */
	public static String getProperty(String key, String defVal,
			Properties confPro) throws Exception {
		String retVal = null;
		if (null != configProperties) {
			retVal = (String) configProperties.get(key);
			// logger.info("KEY: " + key + " VALUE: " + retVal);
			if (retVal == null) {
				/*
				 * logger.info("Property [" + key +
				 * "] not found - using default: [" + defVal + "]");
				 */
				retVal = defVal;
			}
		}
		return retVal;
	}

	/**
	 * Get the property from the config file, or throw an exception if the
	 * property is not found
	 * 
	 * @param key
	 *            The lookup key for the configuration file
	 * 
	 * @return The found value for the key
	 */
	public static String getProperty(String key, Properties confPro)
			throws Exception {
		// init();
		Assert.assertNotNull("No properties file loaded", configProperties);

		String retVal = (String) confPro.get(key);
		// logger.info("KEY: " + key + " VALUE: " + retVal);
		if (retVal == null) {
			throw new java.lang.Exception("Property " + key + " not found");
		}
		return retVal;
	}

	/**
	 * Get the boolean property from the config file, or throw an exception if
	 * the property is not found
	 * 
	 * @param key
	 *            The lookup key for the configuration file
	 * 
	 * @return The found value for the key
	 */
	public static boolean getPropertyBoolean(String key, Properties confPro)
			throws Exception {
		Assert.assertNotNull("No properties file loaded",confPro);

		String retVal = (String) confPro.get(key);
		// logger.info("KEY: " + key + " VALUE: " + retVal);
		if (retVal == null) {
			throw new java.lang.Exception("Property " + key + " not found");
		}
		if (retVal.equals("true"))
			return true;
		else
			return false;
	}
}

