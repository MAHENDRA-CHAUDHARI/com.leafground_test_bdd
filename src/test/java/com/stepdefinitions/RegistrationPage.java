package com.stepdefinitions;

import java.util.Hashtable;
import java.util.Objects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.aventstack.extentreports.Status;
import com.utilityClasses.HTMLReportGenerator;
import com.utilityClasses.SeleniumOperation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class RegistrationPage extends SeleniumOperation{
	public static WebDriver driver;
	public static String PageName = "REGISTER";
	
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
	
	@Given("user Launch browser {string}")
	public void user_launch_browser(String browserNameFromScenario) {
		HTMLReportGenerator.startTest("Verify Lanch the Browser", PageName);
	    if(browserNameFromScenario != null && !browserNameFromScenario.trim().isEmpty()) {
	        System.setProperty("browser.override", browserNameFromScenario.trim());
	    }
	    SeleniumOperation.browserLaunch();
	    String browserUsed = getConfigProp().getProperty("browser").trim();
	    HTMLReportGenerator.logReport(Status.PASS, ""+browserUsed.toUpperCase()+ " Browser Launch Successfully");
	}

	@Given ("user enter url and open Home page")
	public void user_enter_url_and_open_home_page() {
		HTMLReportGenerator.startTest("Verify Open Application", PageName);
		SeleniumOperation.openApplication();
	    HTMLReportGenerator.logReport(Status.PASS,"Open URL Successfully");
	}
	
	@When("user click on Register Menu")
	public void user_click_on_Register_Menu() throws InterruptedException {
		HTMLReportGenerator.startTest("Verify user click on Register Manu", PageName);
		click(register_menu);
		HTMLReportGenerator.logReport(Status.PASS, "User successfully click on [Register] Menu", true);
     }
	
	@When("user enter First Name {string}")
	public void user_enter_first_name_first_name(String firstName) {
		HTMLReportGenerator.startTest("Verify user enter the First Name", PageName);
		Object[] input3=new Object[2];
        input3[0]= first_name_tf;
        input3[1] = firstName; 
        Hashtable<String, Object> firstName_type= sendKey(input3); 
        HTMLReportGenerator.logReport(Status.PASS, "User Enter First Name as["+firstName+"]", true);
	}

	@When("user enter Last Name {string}")
	public void user_enter_last_name_last_name(String lastNme) {
		HTMLReportGenerator.startTest("Verify enter the Last Name of user", PageName);
		Object[] input4=new Object[2];
		input4[0]=last_name_tf; 
		input4[1] = lastNme;
        Hashtable<String, Object> lastName_type=sendKey(input4);
        HTMLReportGenerator.logReport(Status.PASS, "user enter Last Name: ["+lastNme+"]", true);
	}
	
	@When("user enter Phone Number {string}")
	public void user_enter_phone_number(String phoneNumber) {
		HTMLReportGenerator.startTest("Verify enter the Phone Number", PageName);
		Object[] input4=new Object[2];
		input4[0]=phone_number_tf; 
		input4[1] = phoneNumber;
        Hashtable<String, Object> phoneNumber_type=sendKey(input4);
        HTMLReportGenerator.logReport(Status.PASS, "user enter Phone Number:["+phoneNumber+"]", true);
	}
	
	@When("user enter Email ID {string}")
	public void user_enter_email_id_email_id(String emailId) {
		HTMLReportGenerator.startTest("Verify user enter the Email ID", PageName);
		Object[] input5=new Object[2];
		input5[0]=email_id_tf; 
		input5[1] = emailId;
        Hashtable<String, Object> email_id=sendKey(input5);
        HTMLReportGenerator.logReport(Status.PASS, "user enter Email ID:["+emailId+"]", true);
	}
	
	@When("user enter address {string}")
	public void user_enter_address_address(String addressName) {
		HTMLReportGenerator.startTest("Verify user enter the Address", PageName);
		Object[] input6=new Object[2];
		input6[0]=address_tf; 
		input6[1] = addressName;
        Hashtable<String, Object> address_type=sendKey(input6);
        HTMLReportGenerator.logReport(Status.PASS, "user enter address:["+addressName+"]", true);
	}
	
	@When("user enter select city name {string}")
	public void user_enter_select_city_name_city_name(String cityName) {
		HTMLReportGenerator.startTest("Verify user enter the City", PageName);
		Object[] input7=new Object[2];
		input7[0]=city_name_tf; 
		input7[1] = cityName;
        Hashtable<String, Object> cityName_type=sendKey(input7);
        HTMLReportGenerator.logReport(Status.PASS, "user enter select city name:["+cityName+"]", true);
	}
	
	@When("user enter state name {string}")
	public void user_enter_state_name_state_name(String stateName) {
		HTMLReportGenerator.startTest("Verify user enter the State Name", PageName);
		Object[] input8=new Object[2];
		input8[0]=state_name_tf; 
		input8[1] = stateName;
        Hashtable<String, Object> stateName_type=sendKey(input8);
        HTMLReportGenerator.logReport(Status.PASS, "user enter state name:["+stateName+"]", true);
	}
	
	@When("user enter Postal Code {string}")
	public void user_enter_postal_code_postal_code(String postCode) {
		HTMLReportGenerator.startTest("Verify user enter the Postla Code", PageName);
		Object[] input9=new Object[2];
		input9[0]=postal_code_tf; 
		input9[1] = postCode;
        Hashtable<String, Object> postCode_type=sendKey(input9);
        HTMLReportGenerator.logReport(Status.PASS, "user enter Postal Code:["+postCode+"]", true);
	}
	
	@When("user select country {string}")
	public void user_select_country_country(String Country) {
		HTMLReportGenerator.startTest("Verify user enter the Country", PageName);
		Object[] input10=new Object[2];
		input10[0]=country_name_dropdown; 
		input10[1] = Country;
        Hashtable<String, Object> selectCountry=dropdown(input10);
        HTMLReportGenerator.logReport(Status.PASS, "user select country: ["+Country+"]", true);
	}
	
	@When("user enter username {string}")
	public void user_enter_username_user_name(String userName) {
		HTMLReportGenerator.startTest("Verify user enter the Username", PageName);
		Object[] input11=new Object[2];
		input11[0]=userName_tf; 
		input11[1] = userName;
        Hashtable<String, Object> userName_type=sendKey(input11);
        HTMLReportGenerator.logReport(Status.PASS, "user enter username:["+userName+"]", true);
	}
	
	@When("user enter password {string}")
	public void user_enter_password_password(String password) {
		HTMLReportGenerator.startTest("Verify user enter the Password", PageName);
		Object[] input12=new Object[2];
		input12[0]=passwsord_tf; 
		input12[1] = password;
        Hashtable<String, Object> password_type=sendKey(input12);
        HTMLReportGenerator.logReport(Status.PASS, "User enter password:["+password+"]", true);
	}
	
	@When("user enter confirmation password {string}")
	public void user_enter_confirmation_password_confirm_password(String confirmPW) {
		HTMLReportGenerator.startTest("Verify user enter the Confirm Password", PageName);
		if(Objects.nonNull(confirm_Password_tf)) {
			 HTMLReportGenerator.logReport(Status.PASS, "Password Text field is available", true);
			 type(confirm_Password_tf, confirmPW);
			 HTMLReportGenerator.logReport(Status.PASS, "User enter Password successfully", true);
		} else {
			HTMLReportGenerator.logReport(Status.FAIL, "Unable to enter Password");
		}
	}
	
	@When("prompt popup is open then close it for further activity")
	public void prompt_popup_is_open_then_close_it_for_further_activity() {
		click(cross_icon);
        HTMLReportGenerator.logReport(Status.PASS, "Prompt popup is open then close it for further activity");
	}
	
	@Then("user click on Submit button")
	public void user_click_on_Login_button() throws InterruptedException {
		HTMLReportGenerator.startTest("Verify user clicked on Submit Button", PageName);
		userClickOnBtn(submit_btn, "Submit");
//		click(submit_btn);
//        HTMLReportGenerator.logReport(Status.PASS, "User click on [Submit] button");
	}
		
	@Then("verify complete registration salutation for {string} and {string}")
	public void verify_complete_registration_salutation_for_user(String firstName, String lastName) {
		HTMLReportGenerator.startTest("Verify user Salutation Message with FirstName and LastName", PageName);
		String actual_salutation = "Dear "+firstName+" "+lastName+",";
		String expected_salutation = getText(dear_user);
		if(actual_salutation.equals(expected_salutation)) {
			HTMLReportGenerator.logReport(Status.PASS, "Registration Salutation text Matched: ["+expected_salutation+"]", true);
		} else {
			HTMLReportGenerator.logReport(Status.FAIL, "Registration Salutation text  not matched",true);
		}
	}
	
	@Then("user can see the registration confirmation message")
	public void user_can_see_the_registration_confirmation_message_confirm_msg() {
		HTMLReportGenerator.startTest("Verify Registration confirmation message", PageName);
		String textPart = getText(confirmation_msg);
		String linkText = getText(sign_in_link);
		String actualText = textPart.replace("  ", " " + linkText + " ");
		String expectedText = "Thank you for registering. You may now sign-in using the user name and password you've just entered.";
		verifyEqualIgnoringCase(actualText, expectedText);
		HTMLReportGenerator.logReport(Status.PASS, "Verify confirmation Message:["+actualText+"]", true);
		HTMLReportGenerator.logReport(Status.PASS, "User successfully verified Registration Message");
	}
	
	@Then("user can validate the user details {string}")
	public void user_can_validate_the_user_details(String userId) {
		HTMLReportGenerator.startTest("Verify userID greeting Notes", PageName);
		String notes = "Note: Your user name is ";
		String expected_note = notes+""+userId+".";
		String actual_note = getText(verify_userName_note).trim();
		verifyEquals(actual_note, expected_note);
		HTMLReportGenerator.logReport(Status.PASS, "Verify Registration Notes is ["+actual_note+"]", true);
	}
	
	@Then("successfully registration done then closed browser")
	public void closed_browser() throws InterruptedException {
		HTMLReportGenerator.startTest("Successfully closed the browser", PageName);
		closeBrowserWindow();
		HTMLReportGenerator.logReport(Status.PASS, "Successfully closed the Broswer");
	}
	
	public static void userClickOnBtn(By element, String btnName) {
		if(isDisplayed(element)) {
			click(element);
			HTMLReportGenerator.logReport(Status.PASS, "User clicked on the ["+btnName+"] button");
		}else {
			HTMLReportGenerator.logReport(Status.FAIL, "Unable to click on ["+btnName+"] button", true);
		}
	}
}
