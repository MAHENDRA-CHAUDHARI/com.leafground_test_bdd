package com.demoTour.Pages;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;
import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;
import com.framework.Base;

public class RegUserDetailsPage extends Base{

	public static By register_menu = By.linkText("REGISTER");
	public static By first_name_tf = By.xpath("//*[@name='firstName']");
	public static By last_name_tf = By.xpath("//*[@name='lastName']");
	public static By phone_number_tf = By.xpath("//*[@name='phone']");
	public static By email_id_tf = By.xpath("//*[@name='userName']");
	public static By address_tf = By.xpath("//*[@name='address1']");
	public static By city_name_tf = By.xpath("//*[@name='city']");
	public static By state_name_tf = By.xpath("//*[@name='state']");
	public static By postal_code_tf = By.xpath("//*[@name='postalCode']");
	public static By country_name_dropdown = By.xpath("//*[@name='country']");
	public static By userName_tf = By.xpath("//*[@name='email']");
	public static By passwsord_tf = By.xpath("//*[@name='password']");
	public static By confirm_Password_tf = By.xpath("//*[@name='confirmPassword']");
	public static By submit_btn = By.xpath("//*[@name='submit']");
	public static By cross_icon = By.xpath("//*[@class='cb-close']");
	public static By registerMenu = By.xpath("//*[@src='images/mast_register.gif']");
	public static By dear_user = By.xpath("((//*[@border='0'])[8]//tr[3]//font/b)[1]");
	public static By confirmation_msg = By.xpath("((//*[@border='0'])[8]//tr[3]//font)[2]");
	public static By sign_in_link = By.xpath("((//*[@border='0'])[8]//tr[3]//font)[2]/a");
	public static By verify_userName_note = By.xpath("((//*[@border='0'])[8]//tr[3]//font/b)[2]");
	
	public static void verifyRegisterMenuAvailability(String menuName) {
		String expectedMenu = getText(register_menu);
		if(menuName.equals(expectedMenu)) {
			logReport(Status.PASS, "Verify ["+expectedMenu+"] Menu", true);
		} else {
			logReport(Status.FAIL, "Register Menu not matched",true);
		}
	}
	public static void clickOnRegisterMenu() {
		if(isDisplayed(register_menu)) {
			click(register_menu);
			logReport(Status.PASS, "User successfully clicked on [Register] Menu", true);
		} else {
			logReport(Status.FAIL, "Unable to click on [Register] Menu",true);
		}
	}
	
	public static void enterText(By element, String value, String textFieldName) {
		waitUntilElementLocated(element, 10);
		if(isEnabled(element)) {
			clear(element);          hardPause(1);
			type(element, value);
			logReport(Status.PASS, "User enter the "+textFieldName+" values as: ["+value+"]", true);
		}else {
			logReport(Status.FAIL, "Unable to enter the values inside "+textFieldName+" textbox", true);
		}
	}
	
	public static void EnterFirstName(String value) {
		enterText(first_name_tf, value, "First Name");
	}
	public static void EnterLastName(String value) {
		enterText(last_name_tf, value, "Last Name");
	}
	public static void EnterPhoneNumber(String value) {
		enterText(phone_number_tf, value, "Phone Number");
	}
	public static void EnterEmailID(String value) {
		enterText(email_id_tf, value, "Email ID");
	}
	public static void EnterAddress(String value) {
		enterText(address_tf, value, "Address");
	}
	public static void EnterCity(String value) {
		enterText(city_name_tf, value, "City Name");
	}
	public static void EnterStateName(String value) {
		enterText(state_name_tf, value, "State Name");
	}
	public static void EnterPostlaCode(String value) {
		enterText(postal_code_tf, value, "Postal Code");
	}
	public static void EnterUserName(String value) {
		enterText(userName_tf, value, "Username");
	}
	public static void EnterPassword(String value) {
		enterText(passwsord_tf, value, "Password");
	}
	public static void EnterConfirmPassword(String value) {
		enterText(confirm_Password_tf, value, "Confirm Password");
	}
	
    public static String getSelectedOption(By locator) {
        WebElement element = driver.findElement(locator);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText().trim();
    }
	public static void  selectOptionFromDropdown(By element, String optionName, String optionType) {
		selectDroprownUsingValue(element, optionName);  hardPause(2);
		String getOptionName = getSelectedOption(element).trim();  
		verifyEquals(optionName, getOptionName);
		logReport(Status.PASS, "User successfully Select "+optionType+" as ["+getOptionName+"]", true);
	}
	public static void selectCountry(String countryName) {
		selectOptionFromDropdown(country_name_dropdown, countryName, "Country");  hardPause(2);
	}
	
	public static void userClickOnBtn(By element, String btnName) {
		if(isDisplayed(element)) {
			click(element);
			logReport(Status.PASS, "User clicked on the ["+btnName+"] button");
		}else {
			logReport(Status.FAIL, "Unable to click on ["+btnName+"] button", true);
		}
	}
	public static void userClickOnSubmitBtn() {
		userClickOnBtn(submit_btn, "Submit");
	}
	
	public static void closePopupWindow() {
		waitUntilElementClickable(cross_icon, 20);
		if(isDisplayed(cross_icon)) {
			click(cross_icon);
			logReport(Status.PASS, "User clicked on the [X] mark to close popup");
		}else {
			logReport(Status.FAIL, "Unable to click on [X] mark", true);
		}
	}
	 public static void handlePopupIfPresent() {
	        try {
	            List<WebElement> popupElements = driver.findElements(cross_icon);
	            if (!popupElements.isEmpty() && popupElements.get(0).isDisplayed()) {
	                WebDriverWait wait = new WebDriverWait(driver, 10);
	                WebElement closeBtn = wait.until(ExpectedConditions.elementToBeClickable(cross_icon));
	                closeBtn.click();
	                logReport(Status.PASS, "Popup closed automatically.");
	            }
	        } catch (Exception e) {
	            logReport(Status.INFO, "No popup appeared.");
	        }
	    }
	
	public static By getAccordionName(String accordionName) {
		return By.xpath("//font[@color='#000000' and normalize-space()='"+accordionName+"']");
	}
	public static By getPlaceholderName(String placeholdrName) {
		return By.xpath("//b[normalize-space()='"+placeholdrName+":']");
	}

	public static void verifyAccordionName(String[] accordionName) {
		for(int i=0; i<accordionName.length; i++) {
			String ActualAccordionName = getText(getAccordionName(accordionName[i])).trim();
			verifyEquals(ActualAccordionName, accordionName[i]);
			logReport(Status.PASS, "Verify Accordion Name as ["+ActualAccordionName+"]", true);
		}
	}
	public static void verifyPlaceholderName(String[] placeholdrName, String typeName) {
		for(int i=0; i<placeholdrName.length; i++) {
			String ActualAccordionName = getText(getPlaceholderName(placeholdrName[i])).trim().replace(":", "");
			verifyEquals(ActualAccordionName, placeholdrName[i]);
			logReport(Status.PASS, "Verify "+typeName+" Placeholder Name is ["+ActualAccordionName+"]", true);
		}
	}
	
	public static void verifyRegisterHeaderName(String MenuName) {
		WebElement  img = driver.findElement(registerMenu);
		String actualMenuName = img.getAttribute("src").trim();
		verifyEquals(MenuName, actualMenuName);
	}
	public static void verifyRegisterSalutationForUser(String firstName, String lastName) {
		String actual_salutation = "Dear "+firstName+" "+lastName+",";
		String expected_salutation = getText(dear_user);
		if(actual_salutation.equals(expected_salutation)) {
			logReport(Status.PASS, "Registration Salutation text Matched: ["+expected_salutation+"]", true);
		} else {
			logReport(Status.FAIL, "Registration Salutation text  not matched",true);
		}
	}
	
	public static void verifyRegistrationMessage(String givenMessage) {
		String textPart = getText(confirmation_msg);
		String linkText = getText(sign_in_link);
		String actualText = textPart.replace("  ", " " + linkText + " ");
		String expectedText = "Thank you for registering. You may now sign-in using the user name and password you've just entered.";
		verifyEqualIgnoringCase(actualText, expectedText);
		logReport(Status.PASS, "Verify confirmation Message:["+actualText+"]", true);
		logReport(Status.PASS, "User successfully verified Registration Message");
	}
	
	public static void verifyRegNotes(String notes, String userId) {
		String expected_note = notes+""+userId+".";
		String actual_note = getText(verify_userName_note).trim();
		verifyEquals(actual_note, expected_note);
		logReport(Status.PASS, "Verify Registration Notes is ["+actual_note+"]", true);
	}
	
	    public static String extractTextFromImageByXPath(WebDriver driver, String xpath) throws IOException, TesseractException, WebDriverException {
	        WebElement imageElement = driver.findElement(By.xpath(xpath));

	        File screenshot = imageElement.getScreenshotAs(OutputType.FILE);
	        BufferedImage elementScreenshot = ImageIO.read(screenshot);

	        File tempFile = new File("temp_element.png");
	        ImageIO.write(elementScreenshot, "png", tempFile);

	        // Perform OCR on the cropped image
	        ITesseract tesseract = new Tesseract();
	        tesseract.setDatapath(System.getProperty("user.dir") + "/src/main/resources/tessdata");
	        tesseract.setLanguage("eng");
	        return tesseract.doOCR(tempFile);
	    }
	
	public static void abc() {
		String xpath = "//img[@src='images/mast_register.gif']";
		try {
		    String extractedText = extractTextFromImageByXPath(driver, xpath);
		    System.out.println("OCR text: " + extractedText);
		} catch (IOException | TesseractException e) {
		    e.printStackTrace();
		}
	}
}
