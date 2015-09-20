package com.appium.automation.ui;

import java.util.List;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.appium.automation.AppiumWaitHandler;
import com.appium.automation.ui.AppViewElement.ElementAttribute;
import com.appium.automation.ui.test.AppUiTest;
/**
 * 
 * @author tbarua
 *
 */
public class AppPage extends AppiumAbstractPage {
	
	public static Logger logger = Logger.getLogger(AppPage.class);
		
	public AppPage(AppUiTest test){
		super(test);
	}

	/*
	 * This method checks visible existence of an element -Whose Generic KEY is
	 * passed
	 */
	protected boolean isElementVisible(String elementKey) {
		logger.info("Looking for element = " + elementKey);
		// We need to create a clone of the element retrieved from the hashmap
		// as we cant make the hash map dirty.
		AppViewElement anElement = new AppViewElement(
				getAppViewElement(elementKey));
		try {
			WebElement elementOfInterest = getWebElementFromAppPageElement(anElement);
			// Send a click, to place pointer on the element.
			return elementOfInterest.isDisplayed();
		} catch (TimeoutException ex) {
			logger.error("Exception while waiting on element : ");
			ex.printStackTrace();
			return false;
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return false;
		}
	}

	protected String getElementXPath(WebElement element) {
		return (String) ((JavascriptExecutor) driver)
				.executeScript(
						"gPt=function(c){if(c.id!==''){return'id(\"'+c.id+'\")'}if(c===document.body){return c.tagName}var a=0;var e=c.parentNode.childNodes;for(var b=0;b<e.length;b++){var d=e[b];if(d===c){return gPt(c.parentNode)+'/'+c.tagName+'['+(a+1)+']'}if(d.nodeType===1&&d.tagName===c.tagName){a++}}};return gPt(arguments[0]).toLowerCase();",
						element);
	}


	/*
	 * This method rips out the embedded WebElement inside the AppPageElement by
	 * using meta information inside the AppPageElement.
	 */
	protected WebElement getWebElementFromAppPageElement(
			AppViewElement anAppPageElement) {
		WebElement elementOfInterest = null;
		String parentKey = anAppPageElement.getAttribute(ElementAttribute.CONTAINER);
		
		//if searching using parent element, start from parent element
		if (parentKey!=null && !"".equals(parentKey)){
			WebElement parentElement = getWebElementFromAppPageElement(parentKey);
			try {
				String locator = anAppPageElement.getElementFindBy();
				
				switch (anAppPageElement.getElementLocatorType()){
				case ID:
					elementOfInterest = parentElement.findElement(By.id(locator));
					break;
				case XPATH:
					elementOfInterest = parentElement.findElement(By.xpath(locator));
					break;
				case NAME:
					elementOfInterest = parentElement.findElement(By.name(locator));
					break;
				case TAG:
					elementOfInterest = parentElement.findElement(By.tagName(locator));
					break;
				case TEXT:
					elementOfInterest = parentElement.findElement(By.partialLinkText(locator));
					break;
				case LINK:
					elementOfInterest = parentElement.findElement(By.linkText(locator));
					break;
				case CSS:
					elementOfInterest = parentElement.findElement(By.cssSelector(locator));
					break;
				case CLASS:
					elementOfInterest = parentElement.findElement(By.className(locator));
					break;
				}
								
			} catch (Exception ex) {
				// Package up the causing exception into a NoSuchElementException.
				throw new NoSuchElementException("Unable to find element: "
						+ anAppPageElement, ex);
			}
		}
		else{
		//if no parent element specified, search from the top of the driver tree
			try {
				String locator = anAppPageElement.getElementFindBy();
				
				switch (anAppPageElement.getElementLocatorType()){
				case ID:
					elementOfInterest = ((WebDriver) driver).findElement(By.id(locator));
					break;
				case XPATH:
					elementOfInterest = ((WebDriver) driver).findElement(By.xpath(locator));
					break;
				case NAME:
					elementOfInterest = ((WebDriver) driver).findElement(By.name(locator));
					break;
				case TAG:
					elementOfInterest = ((WebDriver) driver).findElement(By.tagName(locator));
					break;
				case TEXT:
					elementOfInterest = ((WebDriver) driver).findElement(By.partialLinkText(locator));
					break;
				case LINK:
					elementOfInterest = ((WebDriver) driver).findElement(By.linkText(locator));
					break;
				case CSS:
					elementOfInterest = ((WebDriver) driver).findElement(By.cssSelector(locator));
					break;
				case CLASS:
					elementOfInterest = ((WebDriver) driver).findElement(By.className(locator));
					break;
				}
								
			} catch (Exception ex) {
				// Package up the causing exception into a NoSuchElementException.
				throw new NoSuchElementException("Unable to find element: "
						+ anAppPageElement, ex);
			}
		}
		// If we can't find the element, attempt to fail fast by throwing an
		// exception.
		if (elementOfInterest == null) {
			throw new NoSuchElementException("Unable to find element: "
					+ anAppPageElement);
		}

		return elementOfInterest;
	}

	/**
	 * @param anAppPageElement
	 * @return
	 */
	private WebElement getParentElement(AppViewElement anAppPageElement) {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * This method rips out the embedded WebElement inside the AppPageElement by
	 * using meta information inside the AppPageElement.
	 */
	protected List<WebElement> getWebElementsFromAppPageElement(
			AppViewElement anAppPageElement) {
		List<WebElement> elementsOfInterest = null;
		
		String parentKey = anAppPageElement.getAttribute(ElementAttribute.CONTAINER);
		
		//if searching using parent element, start from parent element
		if (parentKey!=null && !"".equals(parentKey)){
			WebElement parentElement = getWebElementFromAppPageElement(parentKey);
			try {
				String locator = anAppPageElement.getElementFindBy();
				
				switch (anAppPageElement.getElementLocatorType()){
				case ID:
					elementsOfInterest = parentElement.findElements(By.id(locator));
					break;
				case XPATH:
					elementsOfInterest = parentElement.findElements(By.xpath(locator));
					break;
				case NAME:
					elementsOfInterest = parentElement.findElements(By.name(locator));
					break;
				case TAG:
					elementsOfInterest = parentElement.findElements(By.tagName(locator));
					break;
				case TEXT:
					elementsOfInterest = parentElement.findElements(By.partialLinkText(locator));
					break;
				case LINK:
					elementsOfInterest = parentElement.findElements(By.linkText(locator));
					break;
				case CSS:
					elementsOfInterest = parentElement.findElements(By.cssSelector(locator));
					break;
				case CLASS:
					elementsOfInterest = parentElement.findElements(By.className(locator));
					break;
				}
								
			} catch (Exception ex) {
				// Package up the causing exception into a NoSuchElementException.
				throw new NoSuchElementException("Unable to find element: "
						+ anAppPageElement, ex);
			}
		}
		
		try {
			String locator = anAppPageElement.getAttribute(AppViewElement.ElementAttribute.valueOf(anAppPageElement.getElementLocatorType().toString()));
			
			switch (anAppPageElement.getElementLocatorType()){
			case ID:
				elementsOfInterest = ((WebDriver) driver).findElements(By.id(locator));
				break;
			case XPATH:
				elementsOfInterest = ((WebDriver) driver).findElements(By.xpath(locator));
				break;
			case NAME:
				elementsOfInterest = ((WebDriver) driver).findElements(By.name(locator));
				break;
			case TAG:
				elementsOfInterest = ((WebDriver) driver).findElements(By.tagName(locator));
				break;
			case TEXT:
				elementsOfInterest = ((WebDriver) driver).findElements(By.partialLinkText(locator));
				break;
			case LINK:
				elementsOfInterest = ((WebDriver) driver).findElements(By.linkText(locator));
				break;
			case CSS:
				elementsOfInterest = ((WebDriver) driver).findElements(By.cssSelector(locator));
				break;
			case CLASS:
				elementsOfInterest = ((WebDriver) driver).findElements(By.className(locator));
				break;
			}
		} catch (Exception ex) {
			// Package up the causing exception into a NoSuchElementException.
			throw new NoSuchElementException("Unable to find element: "
					+ anAppPageElement, ex);
		}

		// If we can't find the element, attempt to fail fast by throwing an
		// exception.
		if (elementsOfInterest == null) {
			throw new NoSuchElementException("Unable to find element: "
					+ anAppPageElement);
		}

		return elementsOfInterest;
	}

	protected WebElement getWebElementFromAppPageElement(String elementKey) {
		AppViewElement element = new AppViewElement(getAppViewElement(elementKey));
		return getWebElementFromAppPageElement(element);
	}

	protected List<WebElement> getWebElementsFromAppPageElement(String elementKey) {
		AppViewElement element = new AppViewElement(getAppViewElement(elementKey));
		return getWebElementsFromAppPageElement(element);
	}

	protected String getElementsText(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getText();
	}

	protected String getClass(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getAttribute("class");
	}

	protected String getHref(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getAttribute("href");
	}

	protected String getTagName(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getTagName();
	}

	protected String getId(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getAttribute("id");
	}

	protected String getCSSAttribute(String elementKey, String cssAttribute) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getCssValue(cssAttribute);
	}

	protected String getSrc(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getAttribute("src");
	}

	protected String getName(String elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getCssValue("name");
	}


	protected boolean waitIfAppPageElementVisible(String key, int delay) {
		if (delay < 1) {
			delay = 5000;
		}
		try {
			WebElement element = getWebElementFromAppPageElement(key);
			while (element != null && element.isDisplayed()) {
				logger.info(key + " is visible, waiting...");
				wait(delay);
				element = getWebElementFromAppPageElement(key);
			}
			return true;
		} catch (org.openqa.selenium.NoSuchElementException e2) {
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return false;
	}

	public boolean waitForAsyncContent(AppViewElement appElement) {

		wait(appElement);
		WebElement element = getWebElementFromAppPageElement(appElement);

		// Up to 10 times
		for (int i = 0; i < 10; i++) {
			// Check whether our element is visible yet
			if (element.isDisplayed()) {
				return true;
			}
			wait(1000);
		}
		return false;
	}
	

	
	
	//Utility functions to use only locally

	/**
	 * Waits until Selenium 2.0 can find an element with the specified name attribute.
	 * 
	 * @param name the 'name' attribute value
	 * @param maxMilliseconds how long we should wait
	 * @param frame the frame the element is located on
	 * @param ignoreRendering do we care if the element is visible or not
	 * @return true if Driver is able to find a web element with the given name attribute value
	 */
	private boolean waitOnName(final String name, long maxMilliseconds, final String frame, final boolean ignoreRendering) {

		return AppUiActionFactory.waitOnWaitHandler(this, maxMilliseconds, "Waiting for object name: " + name + " timed out", new AppiumWaitHandler() {
			
			public boolean testIfWaitFinished(long milisecondsPassed) {
				WebElement identifier = null;

				try {
					if (frame == null)
						identifier = driver.findElement(By.name(name));
					else {
						driver.switchTo().defaultContent();
						identifier = driver.switchTo().frame(frame).findElement(By.name(name));
					}
				} catch (Exception e) {			
					identifier = null;
				}

				if (identifier != null && (ignoreRendering || identifier.isDisplayed()) ) {
					logger.info("   Found name in: " + milisecondsPassed/1000 + " sec");
					return true;
				}
				return false;
			}
		});
	}

	/**
	 * Waits until Selenium 2.0  can find an element with the specified name attribute.
	 * @param name
	 * @return
	 */
	private boolean waitOnName(String name) {
		return waitOnName(name, getDefaultWait(), null, false);
	}
	
	/**
	 * Waits until Selenium 2.0  can find the specified header text. The header
	 * title is the common ui header text that can be present on many pages. 
	 * 
	 * @param id
	 * @param maxMilliseconds
	 * @param frame
	 * @param ignoreRendering
	 * @return true if Selenium 2.0 is able to find an element with the given id
	 */
	private boolean waitOnId(final String id, long maxMilliseconds, final String frame, final boolean ignoreRendering) {
		return AppUiActionFactory.waitOnWaitHandler(this, maxMilliseconds, "Waiting for id: " + id + " timed out", new AppiumWaitHandler() {
			public boolean testIfWaitFinished(long milisecondsPassed) {
				WebElement identifier = null;
				try {
					if (frame == null)
						identifier = driver.findElement(By.id(id));
					else {
						driver.switchTo().defaultContent();
						identifier = driver.switchTo().frame(frame).findElement(By.id(id));
					}
				} catch (Exception e) {	
					identifier = null;
				}
				if (identifier != null && (ignoreRendering || (identifier).isDisplayed()) ) {
					logger.info("   Found id in: " + milisecondsPassed/1000 + " sec");
					return true;
				}
				return false;
			}
		}
		);
	}

	private boolean waitOnId(String id) {
		return waitOnId(id, getDefaultWait(), null, false);
	}
	
	private boolean waitOnId(String path, long timeOut) {		
		if(timeOut<=0){
			return waitOnId(path, getDefaultWait(), null, false);
		}
		return waitOnId(path, timeOut, null, false);
	}
	
	/**
	 * Waits until Selenium 2.0  can find the element specified by xpath.
	 *
	 * @param path
	 * @param maxMilliseconds
	 * @param frame
	 * @param ignoreRendering
	 * @return true if Selenium 2.0 is able to find an element with the given id
	 */
	private boolean waitOnXPath(final String path, long maxMilliseconds, final String frame, final boolean ignoreRendering) {

		return AppUiActionFactory.waitOnWaitHandler(this, maxMilliseconds, "Waiting for xpath: " + path + " timed out", new AppiumWaitHandler() {
			
			public boolean testIfWaitFinished(long milisecondsPassed) {
				WebElement identifier = null;

				try {
					if (frame == null)
						identifier = driver.findElement(By.xpath(path));
					else {
						driver.switchTo().defaultContent();
						identifier = driver.switchTo().frame(frame).findElement(By.xpath(path));
					}
				} catch (Exception e) {
					identifier = null;
				}

				if (identifier != null && (ignoreRendering || identifier.isDisplayed()) ) {
					logger.info("   Found XPath in: " + milisecondsPassed/1000 + " sec");
					return true;
				}
				return false;
			}
		});			
	}

	private boolean waitOnXPath(String path) {
		return waitOnXPath(path, getDefaultWait(), null, false);
	}
	
	private boolean waitOnXPath(String path, long timeOut) {		
		if(timeOut<=0){
			return waitOnXPath(path, getDefaultWait(), null, false);
		}
		return waitOnXPath(path, timeOut, null, false);
	}

	@Override
	protected boolean _clickImpl(AppViewElement element) {
		WebElement elementOfInterest = getWebElementFromAppPageElement(element);
		if (elementOfInterest.isDisplayed() && elementOfInterest.isEnabled()) {
			try {
				elementOfInterest.click();
				return true;
			} catch (org.openqa.selenium.WebDriverException ex) {
				return false;
			}
		}
		return false;
	}

	@Override
	protected void _waitUntilAppReady() {
		// TODO Auto-generated method stub
		
	}

	@Override
	protected boolean _typeImpl(AppViewElement element, String text, boolean changeFocus) {
		try {
			WebElement elementOfInterest = getWebElementFromAppPageElement(element);
			if (changeFocus){
				// Send a click, to place pointer on the element.
				elementOfInterest.click();
			}
			// Send keystrokes to the element now.
			elementOfInterest.sendKeys(text);
			if (changeFocus){
				// Send a click, to place pointer on the element.
				elementOfInterest.sendKeys("\t");
				return true;
			}
			else{
				return true;
			}
		} catch (TimeoutException ex) {
			logger.error("Exception while searching for element : ");
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			logger.error(ex.getMessage());
			ex.printStackTrace();
		}
		return false;
	}

	@Override
	protected boolean _clearImpl(AppViewElement element) {
		WebElement webElement = getWebElementFromAppPageElement(element);
		webElement.clear();
		return true;
	}

	@Override
	protected boolean _selectImpl(AppViewElement element, String value) {
		boolean found = false;
		try {
			WebElement elementOfInterest = null;
			elementOfInterest = getWebElementFromAppPageElement(element);
			elementOfInterest.click();		
			List<WebElement> allOptions = elementOfInterest.findElements(By.tagName("option"));
			for (WebElement option : allOptions) {
				if (option.getText().equals(value)){
					found = true;
					option.click();
				}
			}

		} catch (TimeoutException ex) {
			logger.error("Exception while selecting an element : ");
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return found;
		}
		return found;
	}
	
	@Override
	protected boolean _selectImpl(AppViewElement element1, AppViewElement element2, String value) {
		boolean found = false;
		try {
			WebElement elementOfInterest = null;
			//Thread.sleep(5000);
			//elementOfInterest = driver.
				//	findElement(By.xpath("//div[@id='uniqName_18_1']"));
			elementOfInterest = getWebElementFromAppPageElement(element1);
			elementOfInterest.click();
			System.out.println("######################### Waiting ...");
			Thread.sleep(2000);
			List<WebElement> drpListItems = getWebElementsFromAppPageElement(element2);
			//List<WebElement> drpListItems = driver.findElements(By.className("dijitMenuItem"));
			for(WebElement temp : drpListItems){  //now use special for loop and go through the each element and click on the particular element based on the reqiuirement
				String val = temp.getAttribute("aria-label");
				//System.out.println("OPTION::" + val + ", EXP::" + value);
				if(!StringUtils.isEmpty(val)){
					if(val.trim().equals(value)){
					    temp.click();
					    found=true;
					    break;
					}
				}
			}
			
		} catch (TimeoutException ex) {
			logger.error("Exception while selecting an element : ");
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return found;
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return found;
	}
	
	public void dojoclick(){
		
		 //click on the Adult Drop Down Button
	 //WebDriverWait waitForDrpDown = new WebDriverWait(driver, 5);
	 //waitForDrpDown.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(".//*[@id='ns_7_CO19VHUC6N5PC0ACV6A7BG2I12_fmAdults_menu']/tbody")));
	 //after clicking the button it will wait for the drop down to be visible,if it is not visible in 5 Seconds, then it will throw an error
		List<WebElement> drpListItems = driver.findElements(By.className("dijitMenuItemLabel"));
	 //now get all the element which is under 'dijitMenuItemLabel', there will be 6 elements in the List varibale drpListItems,
	 //you can check that by using drpListItems.size()
	for(WebElement temp : drpListItems){  //now use special for loop and go through the each element and click on the particular element based on the reqiuirement
	if(temp.getText().trim().equals("5 Adults")){
	    temp.click();
	    break;
	}
	}
 }

	@Override
	protected boolean _selectImpl(AppViewElement element, int index) {
		boolean found = false;
		try {
			WebElement elementOfInterest = null;
			if (index > 0) {
				elementOfInterest = getWebElementsFromAppPageElement(element)
						.get(index);
			} else {
				elementOfInterest = getWebElementFromAppPageElement(element);
			}
			List<WebElement> allOptions = elementOfInterest.findElements(By.tagName("option"));
			if (index >= allOptions.size())
				throw new IllegalStateException("Select index is out of range");
			allOptions.get(index).click();
			found = true;
		} catch (TimeoutException ex) {
			logger.error("Exception while selecting an element : ");
			ex.printStackTrace();
		} catch (NullPointerException ex) {
			ex.printStackTrace();
			return found;
		}
		return found;
	}

	@Override
	protected boolean _waitImpl(AppViewElement anAppPageElement, long timeout) {
		try {
			String locator = anAppPageElement.getAttribute(AppViewElement.ElementAttribute.valueOf(anAppPageElement.getElementLocatorType().toString()));
			
			switch (anAppPageElement.getElementLocatorType()){
			case ID:
				return waitOnId(locator, timeout);
			case XPATH:
				return waitOnXPath(locator, timeout);
			case NAME:
				return waitOnName(locator);
			case TAG:
				return waitOnXPath("//" + locator);
			case TEXT:
				return waitOnXPath("//*[text()='" + locator + "']");
			case LINK:
				return waitOnXPath("//a[text()='" + locator + "']");
			case CSS:
				throw new Exception("Wait on CSS Element - not implemented");
			case CLASS:
				return waitOnXPath("//[@class='" + locator + "']");
			}
		} catch (Exception ex) {
			// Package up the causing exception into a NoSuchElementException.
			throw new NoSuchElementException("Unable to find element: "
					+ anAppPageElement, ex);
		}
		return false;
	}

	@Override
	protected String _getPropertyImpl(AppViewElement elementKey, String propertyName) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getAttribute(propertyName);
	}
	

	@Override
	protected String _getPropertyImpl(AppViewElement elementKey) {
		WebElement element = getWebElementFromAppPageElement(elementKey);
		return element.getText();
	}

	@Override
	protected boolean _waitOnPropertyValueImpl(AppViewElement el, String propertyName, String value, long timeout) {
		throw new IllegalStateException("Not implemented");
	}

	@Override
	protected boolean _pressImpl(AppViewElement element, Keys keyboardInput) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see AppView#_doubleClickImpl(AppViewElement)
	 */
	@Override
	protected boolean _doubleClickImpl(AppViewElement element) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see AppView#_pressImpl(AppViewElement, java.lang.String)
	 */
	@Override
	protected boolean _pressImpl(AppViewElement element, String charSequence) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean checkForUnexpectedPopUps(String cause) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected boolean _rightClickImpl(AppViewElement element) {
		// TODO Auto-generated method stub
		return false;
	}

}
