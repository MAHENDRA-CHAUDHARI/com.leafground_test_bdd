package com.framework;

public class Locator extends Base{
	private static LocatorBy locator = null;
	private static String value = null;
	
	public Locator(LocatorBy locator, String values) {
		Locator.locator = locator;
		Locator.value = values;
	}
	
	public LocatorBy getLocator() {
		return locator;
	}
	
	public String getValue() {
		return value;
	}
}
