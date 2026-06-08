package com.project.pages;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.framework.Base;

public class GooglePage extends Base{

	public static By google_serach_btn = By.xpath("(//*[@value='Google Search'])[2]");
	public static By Iam_feeling_lucky_btn = By.xpath("(//*[@name='btnI'])[2]");
	public static By google_logo = By.xpath("//*[@class='lnXdpd']");
	public static By xpath_4 = By.xpath("abc");
	public static By xpath_5 = By.xpath("abc");
	public static By xpath_6 = By.xpath("abc");
	public static By xpath_7 = By.xpath("abc");
	public static By xpath_8 = By.xpath("abc");
	public static By xpath_9 = By.xpath("abc");
	public static By xpath_10 = By.xpath("abc");
	public static By xpath_11 = By.xpath("abc");
	public static By xpath_12 = By.xpath("abc");
	public static By xpath_13 = By.xpath("abc");
	public static By xpath_14 = By.xpath("abc");
	public static By xpath_15 = By.xpath("abc");
	
	public static void verifyPageDisplayedOrNot() {
		if(isDisplayed(google_logo)) {
			logReport(Status.PASS, "Verify User landed on Page ["+getTitle()+"] is Displayed", true);
		} else {
			logReport(Status.FAIL, "Page Title is Disable", true);
		}
	}
	
	public static void verifyGoogleSearchBtn() {
		String actual = getAttributeValue(google_serach_btn, "value");
		if(actual.equals("Google Search")) {
			logReport(Status.PASS, "Verify ["+actual+"] Button title is matched", true);
		} else {
			logReport(Status.FAIL, "Button Title is not matched", true);
		}
	}
	
	public static void verifyIamFeelLuckyBtn() {
		String actual = getAttributeValue(Iam_feeling_lucky_btn, "value");
		if(actual.equals("I'm Feeling Lucky")) {
			logReport(Status.PASS, "Verify ["+actual+"] Button title is matche", true);
		} else {
			logReport(Status.FAIL, "Button Title is not matchede", true);
		}
	}
	
	public static void verifyPageTitle(String pageTitle) {
		String actual = getTitle();
//		String actual = getText(xpath_1).replaceAll("[^0-9A-Za-a]", " ").trim();
		if(pageTitle.equals(actual)) {
			logReport(Status.PASS, "Verify Page Title is ["+actual+"]", true);
		} else {
			logReport(Status.FAIL, "Page Title is not matched", true);
		}
	}
	
	public static void selectElementType(String elementName) {
		click(xpath_4);    hardPause(1);
		for(WebElement list_element:getListOfWebElements(xpath_4)) {
			String selectTypeName = list_element.getText();
			if(selectTypeName.equals(elementName)) {
				list_element.click();
				break;
			}   hardPause(1);
		}
		logReport(Status.PASS, "Verify Page Title is ["+getText(xpath_6)+"]", true);
	}
	
	public static void verifyListOfElement(String[] elementList) {
		click(xpath_6);
		List<WebElement> listOf_Elements = driver.findElements(xpath_4);
		for(int i=0; i<listOf_Elements.size(); i++) {
			verifyEquals(elementList[i], listOf_Elements.get(i).getText());
			logReport(Status.PASS, "Verify list of elements are:["+listOf_Elements.get(i).getText()+"]", true);			
		}
	}
	
	
}
