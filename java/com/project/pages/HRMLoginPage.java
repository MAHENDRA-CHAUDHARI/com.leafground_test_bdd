package com.project.pages;
import org.openqa.selenium.By;
import com.aventstack.extentreports.Status;
import com.framework.Base;

public class HRMLoginPage extends Base{

	public static By username_tf = By.name("username");
	public static By password_tf = By.name("password");
	public static By login_btn = By.xpath("//*[@type='submit']");
	
	
	public static void entreUserName(String user_name) {
		if(isDisplayed(username_tf)) {
			type(username_tf, user_name);
			logReport(Status.PASS, "User entered the Username as ["+user_name+"]", true);
		} else {
			logReport(Status.FAIL, "User unable to entered the Username", true);
		}
	}
	
	public static void entrePassword(String password) {
		if(isDisplayed(password_tf)) {
			type(password_tf, password);
			logReport(Status.PASS, "User entered the Password as ["+password+"]", true);
		} else {
			logReport(Status.FAIL, "User unable to entered the Password", true);
		}
	}
	
	public static void clickOnLoginBtn() {
		if(isEnabled(login_btn)) {
			click(login_btn);
			logReport(Status.PASS, "User successfully clicked on [Login] button", true);
		} else {
			logReport(Status.FAIL, "User unable to clicked on Login Button", true);
		}
	}
	
	
}
