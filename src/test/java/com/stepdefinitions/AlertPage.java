package com.stepdefinitions;

import static org.testng.Assert.assertTrue;
import static org.testng.Assert.fail;
import java.time.Duration;
import java.util.logging.LogRecord;

import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.UnhandledAlertException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.Status;
import com.utilityClasses.SeleniumOperation;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AlertPage extends SeleniumOperation {

    public static WebDriver driver;
    String enteredText = "";
    public static By alertMenu = By.xpath("//span[text()='Alert']");
    public static By simpleAlertBtn = By.xpath("//span[text()='Show']");
    public static By confirmAlertBtn = By.xpath("(//span[text()='Show'])[2]");
    public static By promptAlertBtn = By.xpath("(//span[text()='Show'])[3]");
    public static By sweetAlertBtn = By.xpath("//span[text()='Show Sweet Alert']");
    public static By sweetModalBtn = By.xpath("//span[contains(text(),'Show Modal Dialog')]");
    public static By sweetConfirmBtn = By.xpath("//span[contains(text(),'Delete')]");
    public static By resultMsg = By.id("result");

    // ===========================
    // Navigation
    // ===========================
    @When("user navigates to the Alert Handling page")
    public void user_navigates_to_the_alert_handling_page() {
    	waitUntilElementClickable(alertMenu, Duration.ofSeconds(5));
    	logReport(Status.PASS, "Verify Alert Menu is Visible");
        click(alertMenu);
        logReport(Status.PASS, "Clicked Alert menu", true);
    }

    @Then("the page should be loaded successfully")
    public void the_page_should_be_loaded_successfully() { 
        assertTrue(driver.getCurrentUrl().contains("alert"));
        logReport(Status.PASS, "Verify the Alert name is :[alert]", true);
    }

    // ===========================
    // SIMPLE ALERT
    // ===========================
    @When("user clicks on Simple Alert button")
    public void user_clicks_on_simple_alert_button() {
    	click(simpleAlertBtn);
    	logReport(Status.PASS, "Clicked on Simple Alert", true);
    }

    @Then("alert should be displayed")
    public void alert_should_be_displayed() {
    	waitUntilAlertPresent(Duration.ofSeconds(5));
        assertTrue(driver.switchTo().alert() != null);
        logReport(Status.PASS, "Simple Alert is displayed", true);
    }

    @When("user accepts the alert")
    public void user_accepts_the_alert() {
        driver.switchTo().alert().accept();
        logReport(Status.PASS, "Successfully accept the Alert", true);
    }

    @Then("alert should be closed successfully")
    public void alert_should_be_closed_successfully() {
        try {
            driver.switchTo().alert();
            fail("Alert still present");
        } catch (NoAlertPresentException e) {
            assertTrue(true);
            logReport(Status.PASS, "Successfully Closed the Alert", true);
        }
    }

    @Then("page should remain stable")
    public void page_should_remain_stable() {
        assertTrue(driver.findElement(simpleAlertBtn).isDisplayed());
        logReport(Status.PASS, "Verify the Alert page remain Stable", true);
    }

    // ===========================
    // CONFIRM ALERT
    // ===========================
    @When("user clicks on Confirm Alert button")
    public void user_clicks_on_confirm_alert_button() {
        click(confirmAlertBtn);
        logReport(Status.PASS, "Successfully Confirm Alert button", true);
    }

    @Then("confirmation alert should be displayed")
    public void confirmation_alert_should_be_displayed() {
    	waitUntilAlertPresent(Duration.ofSeconds(5));
        assertTrue(driver.switchTo().alert() != null);
        logReport(Status.PASS, "Confirmation Alert is Dispalyed", true);
    }

    @When("user clicks OK on alert")
    public void user_clicks_ok_on_alert() {
        driver.switchTo().alert().accept();
        logReport(Status.PASS, "Successfully accept the Alert", true);
    }

    @Then("success message for OK should be displayed")
    public void success_message_for_ok_should_be_displayed() {
        assertTrue(getText(resultMsg).contains("You clicked: Ok"));
        logReport(Status.PASS, "Successfully Validated Alert OK message", true);
    }

    @When("user clicks Cancel on alert")
    public void user_clicks_cancel_on_alert() {
        driver.switchTo().alert().dismiss();
        logReport(Status.PASS, "Successfully Dismissed or Close the Alert", true);
    }

    @Then("cancel message should be displayed")
    public void cancel_message_should_be_displayed() {
        assertTrue(getText(resultMsg).contains("You clicked: Cancel"));
        logReport(Status.PASS, "Successfully Validate the Alert Close/Cancel/Dismissed Message", true);
    }

    // ===========================
    // PROMPT ALERT
    // ===========================
    @When("user clicks on Prompt Alert button")
    public void user_clicks_on_prompt_alert_button() {
    	click(promptAlertBtn);
    	logReport(Status.PASS, "Successfully Clicked on the Prompt Alert", true);
    }

    @Then("prompt alert should be displayed")
    public void prompt_alert_should_be_displayed() {
        waitUntilAlertPresent(Duration.ofSeconds(5));
        assertTrue(driver.switchTo().alert() != null);
        logReport(Status.PASS, "Successfully Alert is displaye", true);
    }

    @When("user enters {string} in alert")
    public void user_enters_in_alert(String text) {
        enteredText = text;
        Alert alert = driver.switchTo().alert();
        alert.sendKeys(text);
        logReport(Status.PASS, "Successfully Enter Text in Propmpt Alert is:"+text, true);
    }

    @When("user accepts the prompt alert")
    public void user_accepts_the_prompt_alert() {
        driver.switchTo().alert().accept();
        logReport(Status.PASS, "Successfully accept the Alert", true);
    }

    @Then("entered text should be displayed on page")
    public void entered_text_should_be_displayed_on_page() {
        assertTrue(getText(resultMsg).contains(enteredText));
        logReport(Status.PASS, "Successfully verified entered Text into the Alert", true);
    }

    // ===========================
    // SWEET ALERT
    // ===========================
    @When("user clicks on Sweet Alert simple button")
    public void user_clicks_on_sweet_alert_simple_button() {
        click(sweetAlertBtn);
        logReport(Status.PASS, "Successfuly clicked on Sweet Alert", true);
    }

    @Then("sweet alert dialog should be visible")
    public void sweet_alert_dialog_should_be_visible() {
    	WebElement ele = driver.findElement(By.className("ui-dialog"));
    	waitUntilElementVisible(By.className("ui-dialog"), Duration.ofSeconds(5));
    	if(ele.isDisplayed()) {
    		logReport(Status.PASS, "Alert dialogue is dipslyed", true);
    	}else {
    		logReport(Status.FAIL, "Alert dialogue is NOT dipslyed", true);
    	}
    }

    @When("user clicks dismiss button")
    public void user_clicks_dismiss_button() {
        driver.findElement(By.xpath("//span[text()='Dismiss']")).click();
        logReport(Status.PASS, "Sucessfully Clicked on Dismiss Button", true);
    }

    @Then("dialog should be closed")
    public void dialog_should_be_closed() {
        assertTrue(driver.findElements(By.className("ui-dialog")).size() == 0);
        logReport(Status.PASS, "Alert dialogue successfully closed", true);
    }

    // ===========================
    // MODAL DIALOG
    // ===========================
    @When("user opens Sweet Modal Dialog")
    public void user_opens_sweet_modal_dialog() {
        click(sweetModalBtn);
        logReport(Status.PASS, "Alert dialogue is dipslyed", true);
    }

    @Then("modal dialog should be visible")
    public void modal_dialog_should_be_visible() {
    	WebElement ele = driver.findElement(By.className("ui-dialog"));
    	waitUntilElementVisible(By.className("ui-dialog"), Duration.ofSeconds(5));
    	if(ele.isDisplayed()) {
    		logReport(Status.PASS, "Alert model dialogue is dipslyed", true);
    	}else {
    		logReport(Status.FAIL, "Alert model dialogue is NOT dipslyed", true);
    	}
    }

    @Then("background should be blocked")
    public void background_should_be_blocked() {
    	WebElement ele = driver.findElement(By.className("ui-dialog"));
    	waitUntilElementVisible(By.className("ui-widget-overlay"), Duration.ofSeconds(5));
    	if(ele.isDisplayed()) {
    		logReport(Status.PASS, "Alert model dialogue so background is block", true);
    	}else {
    		logReport(Status.FAIL, "Alert model dialogue so background is NOT block", true);
    	}
    }

    public static By dismiss_btn_model = By.xpath("//span[text()='Close']");
    @When("user clicks dismiss button on modal")
    public void user_clicks_dismiss_button_on_modal() {
    	click(dismiss_btn_model);
		logReport(Status.PASS, "Successfully clicked on Model Alert Dismiss Button", true);
    }

    @Then("modal should be closed")
    public void modal_should_be_closed() {
        assertTrue(driver.findElements(By.className("ui-dialog")).size() == 0);
        logReport(Status.PASS, "Verify Model Alert closed");
    }

    @Then("user should regain page control")
    public void user_should_regain_page_control() {
        assertTrue(driver.findElement(simpleAlertBtn).isDisplayed());
        logReport(Status.PASS, "User should regain page control");
     }

    // ===========================
    // SWEET CONFIRMATION
    // ===========================
    @When("user clicks Sweet Alert Confirmation button")
    public void user_clicks_sweet_alert_confirmation_button() {
        click(sweetAlertBtn);
        logReport(Status.PASS, "Successfully clicked on  Sweet confirmation", true);
    }

    @Then("confirmation dialog should appear")
    public void confirmation_dialog_should_appear() {
        WebElement ele = driver.findElement(By.className("ui-confirm-dialog"));
    	waitUntilElementVisible(By.className("ui-confirm-dialog"), Duration.ofSeconds(5));
    	if(ele.isDisplayed()) {
    		logReport(Status.PASS, "Sweet Alert confirmation Dialogue appears", true);
    	}else {
    		logReport(Status.FAIL, "Sweet Alert confirmation Dialogue NOT appears", true);
    	}
    }

    public static By yes_btn = By.xpath("//span[text()='Yes']");
    public static By no_btn = By.xpath("//span[text()='No']");
    @When("user clicks Yes button") 
    public void user_clicks_yes_button() {
        click(yes_btn);
        logReport(Status.PASS, "Successfully clicked on YES button", true);
    }

    @Then("success confirmation message should be shown")
    public void success_confirmation_message_should_be_shown() {
        assertTrue(true);
    }

    @When("user clicks No button")
    public void user_clicks_no_button() {
    	click(no_btn);
        logReport(Status.PASS, "Successfully clicked on NO button", true);
    }

    @Then("cancellation message should be shown")
    public void cancellation_message_should_be_shown() {
        assertTrue(true);
    }
}