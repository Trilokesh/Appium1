/**
 * @tbarua
 */
package com.appium.automation.ui;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map.Entry;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.apache.log4j.Logger;
import org.openqa.selenium.Keys;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.appium.automation.AppiumWaitHandler;
import com.appium.automation.PropertyReader;
import com.appium.automation.ui.AppViewElement.ElementAttribute;
import com.appium.automation.ui.test.AppUiTest;


/**
 * @author tbarua
 * 
 */
public abstract class AppView {
	private static Logger logger = Logger.getLogger(AppView.class);
	/**
	 * Default wait times, not to be used directly. Use getters instead to enable page implementations overrides 
	 */
	public static final int WAIT_TIME = PropertyReader.getPropertyInt("test.wait.time", 30000);
	public static final int WAIT_INCR = PropertyReader.getPropertyInt("test.wait.incr", 500);
	
	protected HashMap<String, AppViewElement> pageElements = null;
	protected AppUiTest test = null;
	String elementfile = null;
	
	private static long actionDelayInMiliSec = setActionDelay(PropertyReader.getPropertyValue("test.action.delay"));
	
	/**
	 * Constructs this page and stores reference to the test that is using it
	 */
	public AppView(AppUiTest test) {
		this.test = test;
	}

	private static long setActionDelay(String propertyValue) {
		if (propertyValue != null){
			return Long.parseLong(propertyValue);
		}
		return 0;
	}

	public void initViewElements(String areaName) {
		// Read the base/master index file to get path of elements xml file.
		InputStream is2 = null;

		// read the element xml file for the desired area and populate the page
		// we will grab the xml file from the class's directory named
		// areaName.xml
		elementfile = areaName + ".xml";
		is2 = this.getClass().getResourceAsStream(elementfile);
		try {
			logger.info("Retrieving elements from : " + elementfile
					+ " For area : " + areaName);
			if (is2 == null){
				throw new RuntimeException ("View element file: " + elementfile + " could not be read.");
			}
			if (pageElements == null)
				pageElements = new HashMap<String, AppViewElement>();
			pageElements.putAll(getWebElementsFromXML(is2, areaName));
		} catch (Exception ex) {
			throw new RuntimeException("Unable to load the xml for area: "
					+ areaName, ex);
		} finally {
			try {
				if (is2 != null)
					is2.close();
			} catch (IOException e) {

			}
		}
	}
	
	/**
	 * Only use for dynamic elements that do not fit into page elements xml
	 * 
	 * @param element
	 */
	protected void addElementToPageElements(AppViewElement element){
		if (pageElements.containsKey(element.getElementKey()))
			throw new RuntimeException("Unable to add element " + element + " because an element with the same KEY already exists.");
		pageElements.put(element.getElementKey(), element);
	}

	/**
	 * This method reads an xml files with Web Element Meta data and returns a
	 * HashMap containing information stored in all the elements.
	 * 
	 * @param XML_FileName
	 * @param elementHashMap
	 * @return
	 * @throws ParserConfigurationException
	 * @throws IOException
	 * @throws SAXException
	 */
	public static HashMap<String, AppViewElement> getWebElementsFromXML(
			String filename, String areaSeeked)
			throws ParserConfigurationException, SAXException, IOException {
		HashMap<String, AppViewElement> retVal = null;
		FileInputStream fis = new FileInputStream(filename);
		retVal = getWebElementsFromXML(fis, areaSeeked);
		fis.close();
		return retVal;
	}

	public static HashMap<String, AppViewElement> getWebElementsFromXML(
			InputStream is, String areaSeeked)
			throws ParserConfigurationException, SAXException, IOException {
		HashMap<String, AppViewElement> elementHashMap = new HashMap<String, AppViewElement>();

		// 1. Open the XML File.
		DocumentBuilderFactory docFactory = DocumentBuilderFactory
				.newInstance();
		DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
		Document doc = docBuilder.parse(is);

		// 2. Read contents from the XML File One Element at a time and Populate
		// the HashMap.
		Node rootNode = doc.getDocumentElement();
		NodeList listOfElements = rootNode.getChildNodes(); // Get all the Child
															// nodes of the root
															// node.

		if (rootNode.hasAttributes()) { // The root node contains the area name
										// of IPP as attribute value.
			NamedNodeMap attributesOfRoot = rootNode.getAttributes();

			// If the elements values seeked by the calling method are not of
			// the same area as in the XML file return null.
			if (!attributesOfRoot.getNamedItem("area").getNodeValue()
					.equalsIgnoreCase(areaSeeked))
				return null;
		}

		/* Getting values out of each element from the XML file. */
		for (int i = 0; i < listOfElements.getLength(); i++) {
			AppViewElement currentElement;
			HashMap<String, String> tempElementsAttributes = new HashMap<String, String>();
			Node currentNode = listOfElements.item(i);
			// data values to extract from the XML File.

			/* Getting values out of Tags */
			if (currentNode.hasChildNodes()) {
				NodeList elementFields = currentNode.getChildNodes();
				for (int k = 0; k < elementFields.getLength(); k++) {
					String nodeName = elementFields.item(k).getNodeName();
					String nodeValue = "";

					if (elementFields.item(k).hasChildNodes()) {
						if (elementFields.item(k).getFirstChild()
								.getNodeValue() != null) {
							nodeValue = elementFields.item(k).getFirstChild()
									.getNodeValue();
						}
					} else if (!elementFields.item(k).getNodeName()
							.equalsIgnoreCase("#text")) {
						nodeValue = "";
					}
					tempElementsAttributes.put(nodeName.toUpperCase(),
							nodeValue);
				}
			}

			/* Getting values out of attributes */
			if (currentNode.hasAttributes()) {
				NamedNodeMap elementAttributesMap = currentNode.getAttributes();
				for (int iter = 0; iter < elementAttributesMap.getLength(); iter++) {
					Node curNode = elementAttributesMap.item(iter);
					tempElementsAttributes.put(curNode.getNodeName()
							.toUpperCase(), curNode.getNodeValue());
				}
			}

			if (!tempElementsAttributes.isEmpty()) {
				String key = tempElementsAttributes.get("KEY");
				if (key != null && !key.equals("")) {
					tempElementsAttributes.remove("KEY");
					currentElement = new AppViewElement(key);
					for (ElementAttribute elementAttr : ElementAttribute
							.values()) {
						if (tempElementsAttributes.containsKey(elementAttr
								.toString())) {
							currentElement.setAttribute(elementAttr,
									tempElementsAttributes.get(elementAttr
											.toString()));
							tempElementsAttributes.remove(elementAttr
									.toString());
						}
					}
					if (tempElementsAttributes.isEmpty()) {
						elementHashMap.put(currentElement.getElementKey(),
								currentElement);
					} else { // The node is not a valid / known node in the
								// framework. print the message.
						for (Entry<String, String> entry : tempElementsAttributes
								.entrySet()) {
							logger.info("\n Invalid NODE in the XML File : "
											+ entry.getKey()
											+ " : "
											+ entry.getValue());
						}
					}
				} else {
					logger.info("\nInvalid element in the XML File: no key name for element");
				}
			}
		}
		return elementHashMap;
	}

	/**
	 * Returns a copy of the abstract UI element identified by the elementKey
	 * @param elementKey the "key" argument for every element in the page object's xml file
	 * @return a copy of the abstract UI element identified by the elementKey
	 */
	protected AppViewElement getAppViewElement(String elementKey) {
		AppViewElement el = pageElements.get(elementKey);
		if (el == null) {
			throw new IllegalStateException("Element: " + elementKey
					+ " not found in page elements descriptor file: "+elementfile);
		}
		return new AppViewElement(el);
	}
	
	/**
	 * Returns a copy of the abstract UI element identified by the elementKey if it exists. If it does not, returns null
	 * @param elementKey the "key" argument for every element in the page object's xml file
	 * @return a copy of the abstract UI element identified by the elementKey
	 */
	protected AppViewElement getAppViewElementIfExists(String elementKey) {
		if (!pageElements.containsKey(elementKey))
			return null;
		return getAppViewElement(elementKey);
	}
	
	
	/**
	 * Clicks on UI element identified by the elementKey, left mouse click if available
	 * @param elementKey the "key" argument for every element in the page object's xml file
	 * @return true if successful
	 */
	protected boolean click(String elementKey){
		return click(getAppViewElement(elementKey));
	}
	/**
	 * Clicks on this UI Element, left mouse click if available
	 * 
	 * @param element to click on
	 * @return true if successful
	 */
	protected boolean click(AppViewElement element){
		logger.info("...Clicking on element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _clickImpl(element);
	}
	
	/**
	 * @param element
	 * @return
	 */
	protected abstract boolean _clickImpl(AppViewElement element);

	/**
	 * Double Clicks on UI element identified by the elementKey, left mouse click if available
	 * @param elementKey the "key" argument for every element in the page object's xml file
	 * @return true if successful
	 */
	protected boolean doubleClick(String elementKey){
		return doubleClick(getAppViewElement(elementKey));
	}
	/**
	 * Double Clicks on this UI Element, left mouse click if available
	 * 
	 * @param element to click on
	 * @return true if successful
	 */
	protected boolean doubleClick(AppViewElement element){
		logger.info("...Double Clicking on element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _doubleClickImpl(element);
	}
	
	/**
	 * @param element
	 * @return
	 */
	protected abstract boolean _doubleClickImpl(AppViewElement element);

	
	
	
	/**
	 * Right clicks on UI element identified by the elementKey
	 * @param elementKey the "key" argument for every element in the page object's xml file
	 * @return true if successful
	 */
	protected boolean rightClick(String elementKey){
		return rightClick(getAppViewElement(elementKey));
	}
	/**
	 * Clicks on this UI Element, left mouse click if available
	 * 
	 * @param element to click on
	 * @return true if successful
	 */
	protected boolean rightClick(AppViewElement element){
		logger.info("...Right clicking on element..."+element+"\n");
		waitUntilOkToPerformAction();
		return _clickImpl(element);
	}
	
	/**
	 * @param element
	 * @return
	 */
	protected abstract boolean _rightClickImpl(AppViewElement element);

	/**
	 * Types text into the UI element. Does not clear the UI Element's contents. Use clear if needed 
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param text
	 * @return true if successful
	 */
	protected boolean type(String elementKey, String text){
		return type(getAppViewElement(elementKey), text);
	}
	
	/**
	 * Types text into the UI element. Does not clear the UI Element's contents. Use clear if needed 
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param text
	 * @return true if successful
	 */
	protected boolean type(String elementKey, String text, boolean setAndChangeFocus){
		return type(getAppViewElement(elementKey), text, setAndChangeFocus);
	}
	
	/**
	 * Types text into the UI element. Does not clear the UI Element's contents. Use clear if needed
	 * @param element 
	 * @param text
	 * @return true if successful
	 */
	protected boolean type(AppViewElement element, String text){
		return type(element, text, false);
	}
	
	/**
	 * Types text into the UI element. Does not clear the UI Element's contents. Use clear if needed
	 * @param element 
	 * @param text
	 * @return true if successful
	 */
	protected boolean type(AppViewElement element, String text, boolean setAndChangeFocust){
		logger.info("...Typing \""+text+"\" into element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _typeImpl(element, text, setAndChangeFocust);
	}
	
	protected abstract boolean _typeImpl(AppViewElement element, String text, boolean setAndChangeFocus);

	/**
	 * Presses key or key combination over the element
	 * @param element
	 * @param keyboardInput
	 * @return
	 */
	protected boolean press(AppViewElement element, Keys keyboardInput){
		logger.info("...Pressing \""+keyboardInput.toString()+"\" over element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _pressImpl(element, keyboardInput);
	}
	
	/**
	 * Presses key or key combination over the element
	 * @param elementKey
	 * @param keyboardInput
	 * @return
	 */
	protected boolean press(String elementKey, Keys keyboardInput){
		return (press(getAppViewElement(elementKey), keyboardInput));
	}
	
	protected abstract boolean _pressImpl(AppViewElement element, Keys keyboardInput);
	
	/**
	 * Presses key or key combination over the element
	 * @param element
	 * @param keyboardInput
	 * @return
	 */
	protected boolean press(AppViewElement element, String charSequence){
		logger.info("...Pressing \""+charSequence.toString()+"\" over element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _pressImpl(element, charSequence);
	}
	
	/**
	 * Presses key or key combination over the element
	 * @param elementKey
	 * @param keyboardInput
	 * @return
	 */
	protected boolean press(String elementKey, String charSequence){
		return (press(getAppViewElement(elementKey), charSequence));
	}
	
	protected abstract boolean _pressImpl(AppViewElement element, String charSequence);
	
	
	/**
	 * Clears contents of the UI Elements
	 * 
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @return true if successful
	 */
	protected boolean clear(String elementKey){
		return clear(getAppViewElement(elementKey));
	}
	/**
	 * Clears contents of the UI Elements
	 * @param element 
	 * @return true if successful
	 */
	protected boolean clear(AppViewElement element){
		logger.info("...Clearing element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _clearImpl(element);
	}
	
	protected abstract boolean _clearImpl(AppViewElement element);
	
	/**
	 * Selects item in the UI Element by value
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param value Value to select
	 * @return true if successful
	 */
	protected boolean select(String elementKey, String value){
		return select(getAppViewElement(elementKey), value);
	}
	
	/**
	 * Selects item in the UI Element by value
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param value Value to select
	 * @return true if successful
	 */
	protected boolean select(String elementKey1, String elementKey2, String value){
		return select(getAppViewElement(elementKey1), getAppViewElement(elementKey2), value);
	}
	/**
	 * Selects item in the UI Element by value
	 * @param element
	 * @param value Value to select
	 * @return true if successful
	 */
	protected boolean select(AppViewElement element, String value){
		logger.info("...Selecting item by value: "+value + " in element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _selectImpl(element, value);
	}
	
	protected abstract boolean _selectImpl(AppViewElement element, String value);

	/**
	 * Selects item in the UI Element by value
	 * @param element
	 * @param value Value to select
	 * @return true if successful
	 */
	protected boolean select(AppViewElement element1, AppViewElement element2, String value){
		logger.info("...Selecting item by value: "+value + " in element...\n"+element1+"\n");
		waitUntilOkToPerformAction();
		return _selectImpl(element1, element2, value);
	}
	
	/**
	 * Selects item in the UI Element by index
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param index
	 * @return true if successful
	 */
	protected boolean select(String elementKey, int index){
		return select(getAppViewElement(elementKey), index);
	}
	/**
	 * Selects item in the UI Element by index
	 * @param element
	 * @param index
	 * @return true if successful
	 */
	protected boolean select(AppViewElement element, int index){
		logger.info("...Selecting item by index: "+index + " in element...\n"+element+"\n");
		waitUntilOkToPerformAction();
		return _selectImpl(element, index);
	}
	
	/**
	 * @param element
	 * @param index
	 * @return
	 */
	protected abstract boolean _selectImpl(AppViewElement element, int index);
	
	/**
	 * @param el
	 * @param propertyString
	 * @return
	 */
	protected boolean getBooleanProperty(AppViewElement el, String propertyString){
		boolean result = false;
		String temp = getProperty(el, propertyString);
		try{
			result = Boolean.parseBoolean(temp);
		}catch (Exception e) {
			throw new IllegalStateException("Unable to parse string: \""+temp +"\" into a boolean.", e);
		}
		return result;
	}
	
	
	/**
	 * @param elementKey
	 * @param propertyString
	 * @return
	 */
	protected boolean getBooleanProperty(String elementKey, String propertyString){
		return getBooleanProperty(getAppViewElement(elementKey), propertyString);
	}
	
	/**
	 * @param elementKey
	 * @param propertyName
	 * @return
	 */
	protected String getProperty(String elementKey, String propertyName){
		return getProperty(getAppViewElement(elementKey), propertyName);
	}
	
	/**
	 * @param el
	 * @param propertyName
	 * @return
	 */
	protected String getProperty(AppViewElement element, String propertyName){
		logger.info("...Getting property: "+propertyName+ " from element...\n"+element);
		_waitUntilAppReady();
		String result = _getPropertyImpl(element, propertyName);
		logger.info("...Got value: \""+result+"\"");
		return result;
	}
	
	protected String getProperty(String elementKey){
		return _getPropertyImpl(getAppViewElement(elementKey));
	}
	
	protected abstract String _getPropertyImpl(AppViewElement AppViewElement);
	
	/**
	 * @param el
	 * @param propertyName
	 * @return
	 */
	protected abstract String _getPropertyImpl(AppViewElement element, String propertyName);
	
	/**
	 * @param elementKey
	 * @param propertyName
	 * @param value
	 * @return
	 */
	protected boolean waitOnPropertyValue(String elementKey, String propertyName, String value){
		return waitOnPropertyValue(getAppViewElement(elementKey), propertyName, value);
	}
	
	/**
	 * @param elementKey
	 * @param propertyName
	 * @param value
	 * @param timeout
	 * @return
	 */
	protected boolean waitOnPropertyValue(String elementKey, String propertyName, String value, long timeout){
		return waitOnPropertyValue(getAppViewElement(elementKey), propertyName, value, timeout);
	}
	
	/**
	 * @param el
	 * @param propertyName
	 * @param value
	 * @return
	 */
	protected boolean waitOnPropertyValue(AppViewElement el, String propertyName, String value){
		return waitOnPropertyValue(el, propertyName, value, getDefaultWait());
	}
	
	/**
	 * @param el
	 * @param propertyName
	 * @param value
	 * @param timeout
	 * @return
	 */
	protected boolean waitOnPropertyValue(AppViewElement el, String propertyName, String value, long timeout){
		_waitUntilAppReady();
		return _waitOnPropertyValueImpl(el, propertyName, value, timeout);
	}
	
	/**
	 * @param el
	 * @param propertyName
	 * @param value
	 * @param timeout
	 * @return
	 */
	protected abstract boolean _waitOnPropertyValueImpl(AppViewElement el, String propertyName, String value, long timeout);

	/**
	 * Waits for the element to become visible and enabled
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @param timeout
	 * @return true if successful
	 */
	protected boolean wait(String elementKey, long timeout){
		return wait(getAppViewElement(elementKey), timeout);
	}
	/**
	 * Waits for the element to become visible and enabled
	 * @param elementKey elementKey the "key" argument for every element in the page object's xml file
	 * @return true if successful
	 */
	protected boolean wait(String elementKey){
		return wait(elementKey, getDefaultWait());
	}
	
	/**
	 * Waits for the element to become visible and enabled
	 * @param element
	 * @param timeout
	 * @return true if successful
	 */
	protected boolean wait(AppViewElement element, long timeout){
		logger.info("...Waiting on element...\n"+element+"\n");
		_waitUntilAppReady();
		return _waitImpl(element, timeout);
	}
	/**
	 * @param element
	 * @param timeout
	 * @return
	 */
	protected abstract boolean _waitImpl(AppViewElement element, long timeout);

	/**
	 * Waits for the element to become visible and enabled
	 * @param element
	 * @return true if successful
	 */
	protected boolean wait(AppViewElement element){
		return wait(element, getDefaultWait());
	}
	
	/**
	 * Sleeps the specified amount of milliseconds
	 * @param milliseconds
	 */
	public void wait(int milliseconds) {
		Calendar clock = Calendar.getInstance();
		long endTime = clock.getTimeInMillis() + milliseconds;
		while (clock.getTimeInMillis() < endTime) {
			try {
				Thread.sleep(getDefaultWaitIncr());
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			clock = Calendar.getInstance();
		}
	}
	/**
	 * Sleeps the specified amount of milliseconds
	 * @param milliseconds
	 */
	public void waitDefault(){
		wait((int)getDefaultWait());
	}
	
	/**
	 * @param secondsPassed
	 * @return
	 */
	protected int waitMore(int secondsPassed) {
		secondsPassed += WAIT_INCR;
		wait(WAIT_INCR);
		return secondsPassed;
	}

	/**
	 * @param cause
	 * @return
	 */
	public abstract boolean checkForUnexpectedPopUps(String cause);
	
	/**
	 * Waits for action timer and calls local checks
	 * @return true when it's ok to perform action
	 */

	protected void waitUntilOkToPerformAction() {
		AppiumWaitHandler waitHandler = new AppiumWaitHandler() {

			public boolean testIfWaitFinished(long milisecondsPassed) {
				return (milisecondsPassed >= actionDelayInMiliSec);
			}
		};
		AppUiActionFactory.waitOnWaitHandler(this, waitHandler);
		_waitUntilAppReady();
	}
	
	/**
	 * 
	 */
	protected abstract void _waitUntilAppReady();

	/**
	 * @return
	 */
	public long getDefaultWait(){
		return WAIT_TIME;
	}
	
	/**
	 * @return
	 */
	public long getDefaultWaitIncr(){
		return WAIT_INCR;
	}

	public static long getActionDelayInMiliSec() {
		return actionDelayInMiliSec;
	}

	protected boolean _selectImpl(AppViewElement element1,
			AppViewElement element2, String value) {
		// TODO Auto-generated method stub
		return false;
	}
}
