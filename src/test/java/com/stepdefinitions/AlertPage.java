package com.stepdefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.utilityClasses.SeleniumOperation;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class AlertPage extends SeleniumOperation{
	public static WebDriver driver;

	public static By alert_menu = By.xpath("//span[text()='Alert']");
	
	
	@When("user navigates to the Alert Handling page")
	public void user_navigates_to_the_alert_handling_page() {
	   
	    
	}

	@When("user clicks on Simple Alert button")
	public void user_clicks_on_simple_alert_button() {
	   
	    
	}

	@Then("alert should be displayed")
	public void alert_should_be_displayed() {
	   
	    
	}

	@When("user accepts the alert")
	public void user_accepts_the_alert() {
	   
	    
	}

	@Then("alert should be closed successfully")
	public void alert_should_be_closed_successfully() {
	   
	    
	}

	@Then("page should remain stable")
	public void page_should_remain_stable() {
	   
	    
	}

	@When("user clicks on Confirm Alert button")
	public void user_clicks_on_confirm_alert_button() {
	   
	    
	}

	@Then("confirmation alert should be displayed")
	public void confirmation_alert_should_be_displayed() {
	   
	    
	}

	@When("user clicks OK on alert")
	public void user_clicks_ok_on_alert() {
	   
	    
	}

	@Then("success message for OK should be displayed")
	public void success_message_for_ok_should_be_displayed() {
	   
	    
	}

	@When("user clicks Cancel on alert")
	public void user_clicks_cancel_on_alert() {
	   
	    
	}

	@Then("cancel message should be displayed")
	public void cancel_message_should_be_displayed() {
	   
	    
	}

	@When("user clicks on Prompt Alert button")
	public void user_clicks_on_prompt_alert_button() {
	   
	    
	}

	@Then("prompt alert should be displayed")
	public void prompt_alert_should_be_displayed() {
	   
	    
	}

	@When("user enters {string} in alert")
	public void user_enters_in_alert(String string) {
	   
	    
	}

	@When("user accepts the prompt alert")
	public void user_accepts_the_prompt_alert() {
	   
	    
	}

	@Then("entered text should be displayed on page")
	public void entered_text_should_be_displayed_on_page() {
	   
	    
	}

	@When("user clicks on Sweet Alert simple button")
	public void user_clicks_on_sweet_alert_simple_button() {
	   
	    
	}

	@Then("sweet alert dialog should be visible")
	public void sweet_alert_dialog_should_be_visible() {
	   
	    
	}

	@When("user clicks dismiss button")
	public void user_clicks_dismiss_button() {
	   
	    
	}

	@Then("dialog should be closed")
	public void dialog_should_be_closed() {
	   
	    
	}

	@When("user opens Sweet Modal Dialog")
	public void user_opens_sweet_modal_dialog() {
	   
	    
	}

	@Then("modal dialog should be visible")
	public void modal_dialog_should_be_visible() {
	   
	    
	}

	@Then("background should be blocked")
	public void background_should_be_blocked() {
	   
	    
	}

	@When("user clicks dismiss button on modal")
	public void user_clicks_dismiss_button_on_modal() {
	   
	    
	}

	@Then("modal should be closed")
	public void modal_should_be_closed() {
	   
	    
	}

	@Then("user should regain page control")
	public void user_should_regain_page_control() {
	   
	    
	}

	@When("user clicks Sweet Alert Confirmation button")
	public void user_clicks_sweet_alert_confirmation_button() {
	   
	    
	}

	@Then("confirmation dialog should appear")
	public void confirmation_dialog_should_appear() {
	   
	    
	}

	@When("user clicks Yes button")
	public void user_clicks_yes_button() {
	   
	    
	}

	@Then("success confirmation message should be shown")
	public void success_confirmation_message_should_be_shown() {
	   
	    
	}

	@When("user clicks No button")
	public void user_clicks_no_button() {
	   
	    
	}

	@Then("cancellation message should be shown")
	public void cancellation_message_should_be_shown() {
	   
	    
	}

	@When("user opens Sweet Alert with minimize\\/maximize option")
	public void user_opens_sweet_alert_with_minimize_maximize_option() {
	   
	    
	}

	@When("user minimizes alert")
	public void user_minimizes_alert() {
	   
	    
	}

	@Then("alert should collapse")
	public void alert_should_collapse() {
	   
	    
	}

	@When("user maximizes alert")
	public void user_maximizes_alert() {
	   
	    
	}

	@Then("alert should expand again")
	public void alert_should_expand_again() {
	   
	    
	}

	@When("user triggers simple alert")
	public void user_triggers_simple_alert() {
	   
	    
	}

	@Then("alert should be present")
	public void alert_should_be_present() {
	   
	    
	}

	@Then("system should wait for alert handling")
	public void system_should_wait_for_alert_handling() {
	   
	    
	}

	@When("user does not interact with alert for {int} seconds")
	public void user_does_not_interact_with_alert_for_seconds(Integer int1) {
	   
	    
	}

	@Then("alert should still be active or handled safely")
	public void alert_should_still_be_active_or_handled_safely() {
	   
	    
	}

	@When("user triggers alert")
	public void user_triggers_alert() {
	   
	    
	}

	@When("user tries to interact with page elements")
	public void user_tries_to_interact_with_page_elements() {
	   
	    
	}

	@Then("interaction should be blocked until alert is handled")
	public void interaction_should_be_blocked_until_alert_is_handled() {
	   
	    
	}

	@When("no alert is present")
	public void no_alert_is_present() {
	   
	    
	}

	@When("user tries to accept alert")
	public void user_tries_to_accept_alert() {
	   
	    
	}

	@Then("NoAlertPresentException should be handled gracefully")
	public void no_alert_present_exception_should_be_handled_gracefully() {
	   
	    
	}
}
