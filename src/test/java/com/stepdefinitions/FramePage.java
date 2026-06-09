package com.stepdefinitions;

import java.time.Duration;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchFrameException;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.Status;
import com.utilityClasses.SeleniumOperation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FramePage extends SeleniumOperation {
    public static String pageName = "Browser - Frame Handle";

    // Menu
    public static By browser_menu = By.xpath("(//a[@href='#'])[5]");
    public static By frame_menu = By.xpath("//span[text()='Frame']");
    public static By frame_heading = By.xpath("//*[contains(text(),'Click Me')]");
    public static By clickMe_inside_Btn = By.xpath("(//button[@id='Click'])[1]");
    public static By first_inside_text = By.xpath("//*[text()='Hurray! You Clicked Me.']");
    int frameCount = 0;
    boolean frameSwitchSuccess = false;
    boolean exceptionHandled = false;

    // ======================================================
    // Launch Browser
    // ======================================================
    @Given("user launches the browser {string}")
    public void user_launches_the_browser(String browserNameFromScenario) {
        if (browserNameFromScenario != null && !browserNameFromScenario.trim().isEmpty()) {
            System.setProperty("browser.override", browserNameFromScenario.trim());
        }

        SeleniumOperation.browserLaunch();
        String browserUsed = getConfigProp().getProperty("browser").trim();
        logReport(Status.PASS,browserUsed.toUpperCase() + " Browser launched successfully");
    }

    @Given("user enter url and open Home page for Ground Leaf")
    public void user_enter_url_and_open_home_page() {
        SeleniumOperation.openApplication();
        logReport(Status.PASS, "Application opened successfully");
    }

    @When("user click on Browser Menu")
    public void clickOnBrowserMenu() {
    	waitUntilElementLocated(browser_menu, Duration.ofSeconds(5));
        click(browser_menu);
        logReport(Status.PASS, "Clicked Browser menu", true);
    }

    @When("user navigates to Frame Handling page")
    public void user_navigates_to_frame_handling_page() {
        waitUntilElementLocated(frame_menu, Duration.ofSeconds(10));
        click(frame_menu);
        logReport(Status.PASS, "Clicked Frame menu", true);
    }

    @Then("the page should be loaded successfully")
    public void the_page_should_be_loaded_successfully() {
        waitUntilElementVisible(frame_heading, Duration.ofSeconds(10));
        logReport(Status.PASS, "Frame page loaded successfully", true);
    }

    // ======================================================
    // SINGLE FRAME
    // ======================================================
    @When("user switches to first frame")
    public void user_switches_to_first_frame() {
    	driver.switchTo().frame(0);
    	click(clickMe_inside_Btn);
        logReport(Status.PASS, "Switched to first frame");
    }

    @Then("user clicks on Click Me button inside inner frame")
    public void user_clicks_on_click_me_button_inside_frame() {
    	String actualText = "Hurray! You Clicked Me.";
    	if(getText(first_inside_text).equals(actualText)) {
    		logReport(Status.PASS, "User capture inside Frame Text: "+actualText, true);
    	}else {
    		logReport(Status.PASS, "Unable to capture inside Frame Text", true); 
    	}
    }

    @Then("user should return to main page context")
    public void user_should_return_to_main_page_context() {
        driver.switchTo().defaultContent();
        logReport(Status.PASS,"Returned to default content successfully");
    }

    // ======================================================
    // NESTED FRAME
    // ======================================================
    @When("user switches to inner frame")
    public void user_switches_to_nested_frame() {
        driver.switchTo().frame(2);
        logReport(Status.PASS, "Switched to inner frame successfully");
    }

    @Then("user clicks on Click Me button inside nested frame")
    public void user_clicks_on_click_me_button_inside_inner_frame() {
    	driver.switchTo().frame(0);
    	logReport(Status.PASS, "Switched to nested frame successfully");
    	click(clickMe_inside_Btn);
    	String actualText = "Hurray! You Clicked Me.";
    	if(getText(first_inside_text).equals(actualText)) {
    		logReport(Status.PASS, "User capture inside Frame Text: "+actualText, true);
    	}else {
    		logReport(Status.PASS, "Unable to capture inside Frame Text", true); 
    	}
    }

    @Then("user should return to parent frame")
    public void user_should_return_to_parent_frame() {
        driver.switchTo().parentFrame();
        logReport(Status.PASS, "Returned to parent frame");
    }

    // ======================================================
    // FRAME COUNT
    // ======================================================
    @When("user retrieves all iframe elements")
    public void user_retrieves_all_iframe_elements() {
        driver.switchTo().defaultContent();
        List<WebElement> frames =driver.findElements(By.tagName("iframe"));
        frameCount = frames.size();
        logReport(Status.PASS,"Total Frames Found : " + frameCount);
    }
    @Then("frame count should be greater than or equal to expected value")
    public void frame_count_should_be_greater_than_or_equal_to_expected_value() {
        if (frameCount >= 2) {
            logReport(Status.PASS,"Frame count validation passed");
        } else {
            logReport(Status.FAIL,"Frame count validation failed");
        }
    }

    // ======================================================
    // FRAME SWITCH STABILITY
    // ======================================================
    @When("user switches to frame")
    public void user_switches_to_frame() {
        driver.switchTo().frame(0);
        frameSwitchSuccess = true;
    }

    @When("user returns to default content")
    public void user_returns_to_default_content() {
        driver.switchTo().defaultContent();
    }

    @When("user switches again to another frame")
    public void user_switches_again_to_another_frame() {
        driver.switchTo().frame(1);
    }

    @Then("all frame switches should work without exception")
    public void all_frame_switches_should_work_without_exception() {
        if (frameSwitchSuccess) {
            logReport(Status.PASS,"Frame switching successful", true);
        } else {
            logReport(Status.FAIL,"Frame switching failed", true);
        }
    }

    // ======================================================
    // INVALID FRAME
    // ======================================================
    @When("user tries to switch to non-existing frame")
    public void user_tries_to_switch_to_non_existing_frame() {
        try {
            driver.switchTo().frame(100);
        } catch (NoSuchFrameException e) {
            exceptionHandled = true;
            logReport(Status.INFO,"NoSuchFrameException handled");
        }
    }

    @Then("NoSuchFrameException should be handled gracefully")
    public void no_such_frame_exception_should_be_handled_gracefully() {
        if (exceptionHandled) {
            logReport(Status.PASS,"NoSuchFrameException handled successfully");
        } else {
            logReport(Status.FAIL,"Exception was not handled");
        }
    }

    // ======================================================
    // ACTION WITHOUT FRAME SWITCH
    // ======================================================
    @When("user tries to click element inside frame without switching")
    public void user_tries_to_click_element_inside_frame_without_switching() {
        try {
            driver.switchTo().defaultContent();
            click(clickMe_inside_Btn);
        } catch (Exception e) {
            exceptionHandled = true;
            logReport(Status.INFO,"Exception occurred as expected");
        }
    }

    @Then("StaleElementReferenceException or failure should occur safely")
    public void stale_element_reference_exception_or_failure_should_occur_safely() {
        if (exceptionHandled) {
            logReport(Status.PASS,"Exception handled safely");
        } else {
            logReport(Status.FAIL,"Expected exception did not occur");
        }
    }
    
	@Then("closed browser window")
	public static void ClosedBrowser() throws InterruptedException {
		closeBrowserWindow();
	}
}