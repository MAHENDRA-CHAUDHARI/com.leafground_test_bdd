package com.stepdefinitions;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import com.utilityClasses.SeleniumOperation;

import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class WindowHandlePage extends SeleniumOperation{
	public static WebDriver driver;

	public static By window_menu = By.xpath("//span[text()='Window']");
	
	
	@When("user navigates to the Window Handling page")
	public void user_navigates_to_the_window_handling_page() {
	   
	    
	}

	@Then("page title should contain {string}")
	public void page_title_should_contain(String string) {
	   
	    
	}

	@Then("main window button should be visible")
	public void main_window_button_should_be_visible() {
	   
	    
	}

	@When("user clicks on Open Home Page in New Window button")
	public void user_clicks_on_open_home_page_in_new_window_button() {
	   
	    
	}

	@Then("new window should be opened")
	public void new_window_should_be_opened() {
	   
	    
	}

	@Then("total window count should be {int}")
	public void total_window_count_should_be(Integer int1) {
	   
	    
	}

	@When("user stores all window handles")
	public void user_stores_all_window_handles() {
	   
	    
	}

	@Then("parent and child window handles should be captured")
	public void parent_and_child_window_handles_should_be_captured() {
	   
	    
	}

	@When("user switches to child window")
	public void user_switches_to_child_window() {
	   
	    
	}

	@Then("child window should be active")
	public void child_window_should_be_active() {
	   
	    
	}

	@Then("URL should contain {string}")
	public void url_should_contain(String string) {
	   
	    
	}

	@Then("child page title should be verified")
	public void child_page_title_should_be_verified() {
	   
	    
	}

	@When("user switches back to parent window")
	public void user_switches_back_to_parent_window() {
	   
	    
	}

	@Then("parent window should be active")
	public void parent_window_should_be_active() {
	   
	    
	}

	@Then("main page should be displayed correctly")
	public void main_page_should_be_displayed_correctly() {
	   
	    
	}

	@When("user opens multiple windows \\(if available)")
	public void user_opens_multiple_windows_if_available() {
	   
	    
	}

	@Then("more than one child window should be opened")
	public void more_than_one_child_window_should_be_opened() {
	   
	    
	}

	@Then("all window handles should be stored")
	public void all_window_handles_should_be_stored() {
	   
	    
	}

	@When("user switches through all open windows")
	public void user_switches_through_all_open_windows() {
	   
	    
	}

	@Then("each window title or URL should be validated")
	public void each_window_title_or_url_should_be_validated() {
	   
	    
	}

	@When("user closes child window")
	public void user_closes_child_window() {
	   
	    
	}

	@Then("only parent window should remain open")
	public void only_parent_window_should_remain_open() {
	   
	    
	}

	@Then("parent window should still be active")
	public void parent_window_should_still_be_active() {
	   
	    
	}

	@Then("window count should reflect only {int} open window")
	public void window_count_should_reflect_only_open_window(Integer int1) {
	   
	    
	}

	@When("user tries to switch to a closed window handle")
	public void user_tries_to_switch_to_a_closed_window_handle() {
	   
	    
	}

	@Then("NoSuchWindowException should be handled")
	public void no_such_window_exception_should_be_handled() {
	   
	    
	}

	@When("user opens child window and refreshes page")
	public void user_opens_child_window_and_refreshes_page() {
	   
	    
	}

	@Then("page should reload successfully")
	public void page_should_reload_successfully() {
	   
	    
	}

	@Then("session should remain active")
	public void session_should_remain_active() {
	   
	    
	}

	@When("user closes all browser windows")
	public void user_closes_all_browser_windows() {
	   
	    
	}

	@Then("browser session should end cleanly")
	public void browser_session_should_end_cleanly() {
	   
	    
	}
}
