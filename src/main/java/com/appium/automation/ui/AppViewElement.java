/**
 * 
 */
package com.appium.automation.ui;

import java.util.Hashtable;
import java.util.Map.Entry;
import java.util.Set;

/**
 * @author tbarua
 *
 */
public class AppViewElement {
	public enum ElementAttribute{FINDBY, ID, TAG, NAME, LINK, TEXT, XPATH, CLASS, CSS, SCREENSHOT, PLACEHOLDER, LABEL, PARTIAL_TEXT, CONTAINER, ROW, COLUMN};
	public enum ElementLocatorType{ID, TAG, NAME, LINK, TEXT, XPATH, CLASS, CSS, SCREENSHOT, PLACEHOLDER, LABEL, PARTIAL_TEXT, CONTAINER};

	String elementKey;
	Hashtable<ElementAttribute, String> elementAttributes = new Hashtable<AppViewElement.ElementAttribute, String>();
	
	public AppViewElement(String elementKey){
		this.elementKey = elementKey;
	}
	
	public AppViewElement(AppViewElement element){
		this.elementKey = element.getElementKey();
		for (Entry<ElementAttribute, String> entry : element.getAttributes()){
			setAttribute(entry.getKey(), entry.getValue());
		}		
	}
	
	public AppViewElement(String elementKey, ElementLocatorType findBy, String locator, String containerKey){
		this(elementKey);
		setAttribute(ElementAttribute.FINDBY, findBy.toString());
		setAttribute(ElementAttribute.valueOf(findBy.toString()), locator);
		if (containerKey!=null)
			setAttribute(ElementAttribute.CONTAINER, containerKey);
	}

	public AppViewElement(String elementKey, ElementLocatorType findBy, String locator) {
		this(elementKey, findBy, locator, null);
	}

	public AppViewElement(ElementLocatorType findBy, String locator) {
		this ("DYNAMIC_ELEMENT", findBy, locator);
	}

	/**
	 * @return
	 */
	public Set<Entry<ElementAttribute, String>> getAttributes() {
		Hashtable<ElementAttribute, String> attributeTable = new Hashtable<ElementAttribute, String>();
		for (Entry<ElementAttribute, String> entry : elementAttributes.entrySet()){
			attributeTable.put(entry.getKey(), entry.getValue());
		}
		return attributeTable.entrySet();
	}

	/**
	 * @return
	 */
	public String getElementKey() {
		return elementKey;
	}

	public void setAttribute(ElementAttribute attribute, String value){
		elementAttributes.put(attribute, value);
	}
	
	public String getAttribute(ElementAttribute attribute){
		if (elementAttributes.containsKey(attribute)){
			return elementAttributes.get(attribute);
		}
		else{
			return "";
		}
	}
	
	@Override
	public String toString() {
		StringBuilder strBuilder = new StringBuilder();
		strBuilder.append("AppElement \"" + getElementKey() + "\" [");
		for (ElementAttribute attr : ElementAttribute.values()){
			if (getAttribute(attr) != null && !"".equals(getAttribute(attr))){
				strBuilder.append(attr+"=");
				strBuilder.append(getAttribute(attr)+", ");
			}
		}
		return strBuilder.substring(0, strBuilder.length()-2) + "]";
	}
	
	public ElementLocatorType getElementLocatorType(){
		return ElementLocatorType.valueOf(getAttribute(ElementAttribute.FINDBY).toUpperCase());
	}
	
	public String getElementFindBy(){
		return getAttribute(AppViewElement.ElementAttribute.valueOf(getElementLocatorType().toString()));
	}
}
