package com.stepdefinitions;

import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import com.aventstack.extentreports.Status;
import com.utilityClasses.HTMLReportGenerator;
import com.utilityClasses.SeleniumOperation;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;

public class FlightBookingSteps extends SeleniumOperation{
	public static WebDriver driver;
	public static String pageName = "Flight Booking";
	
	public static By flight_menu = By.linkText("Flights");
	public static By oneWay_trip = By.xpath("//input[@name='tripType' and @value='oneway']");
	public static By round_trip = By.xpath("//input[@name='tripType' and @value='roundtrip']");
	public static By passengers = By.xpath("//select[@name='passCount']");
	public static By departingFrom = By.xpath("//select[@name='fromPort']");
	public static By departingMonth = By.xpath("//select[@name='fromMonth']");
	public static By departingDay = By.xpath("//select[@name='fromDay']");
	public static By arrivingIn = By.xpath("//select[@name='toPort']");
	public static By returningMonth = By.xpath("//select[@name='toMonth']");
	public static By returningDay = By.xpath("//select[@name='toDay']");
	public static By economyClass = By.xpath("//input[@name='servClass' and @value='Coach']");
	public static By businessClass = By.xpath("//input[@name='servClass' and @value='Business']");
	public static By firstClass = By.xpath("//input[@name='servClass' and @value='First']");
	public static By airlinePreference = By.xpath("//select[@name='airline']");
	public static By continue_btn = By.xpath("//input[@name='findFlights']");
	public static By no_flight_no_seat_msg = By.xpath("(//font[contains(normalize-space(),'No Seats')])[2]");
	public static By flight_book_instruction = By.xpath("(//font[contains(normalize-space(),'Use our Flight Finder to search for the')])");
	
	@Given("user Launching browser {string}")
	public void user_launch_browser(String browserNameFromScenario) {
		HTMLReportGenerator.startTest("Verify Lanch the Browser", pageName);
	    if(browserNameFromScenario != null && !browserNameFromScenario.trim().isEmpty()) {
	        System.setProperty("browser.override", browserNameFromScenario.trim());
	    }
	    SeleniumOperation.browserLaunch();
	    String browserUsed = getConfigProp().getProperty("browser").trim();
	    HTMLReportGenerator.logReport(Status.PASS, ""+browserUsed.toUpperCase()+ " Browser Launch Successfully");
	}

	@Given ("user enter url and open Home page for Flight Book")
	public void user_enter_url_and_open_home_page() {
		HTMLReportGenerator.startTest("Verify Open Application", pageName);
		SeleniumOperation.openApplication();
	    HTMLReportGenerator.logReport(Status.PASS,"Open URL Successfully");
	}
	
	@When("verify Flight Menu availability {string}")
	public static void verify_Flight_Menu_availability(String menuName) {
		HTMLReportGenerator.startTest("Verify Flight Menu availability", pageName);
		String expectedMenu = getText(flight_menu);
		if(menuName.equals(expectedMenu)) {
			HTMLReportGenerator.logReport(Status.PASS, "Verify ["+expectedMenu+"] Menu", true);
		} else {
			HTMLReportGenerator.logReport(Status.FAIL, "Flight Menu not matched",true);
		}
	}
	
	@Then("user click on Fligt Menu")
	public void user_click_on_fligt_menu() {
		HTMLReportGenerator.startTest("Verify user click on Flight Manu", pageName);
		click(flight_menu);
		HTMLReportGenerator.logReport(Status.PASS, "User successfully click on ["+pageName+"] Menu", true);
	}

	@Given("user selects {string} trip type")
	public void user_selects(String string) {
		HTMLReportGenerator.startTest("Verify and Select Trip Type", pageName);
		if (!isSelected(oneWay_trip)) {
            click(oneWay_trip);
            HTMLReportGenerator.logReport(Status.PASS,"One-Way trip selected.", true);
        } else {
        	HTMLReportGenerator.logReport(Status.FAIL,"One-Way trip is already selected.", true);
        }
		RadioButtonCheck();
	}
	
	@Given("user selects departure city as {string}")
	public void user_selects_departure_city_as(String departFromLocation) {
		HTMLReportGenerator.startTest("User Select Departure City", pageName);
		selectOptionFromDropdown(departingFrom, departFromLocation, "Departing From");
	}
	
	@Given("user selects destination city as {string}")
	public void user_selects_destination_city_as(String arriveLocation) {
		HTMLReportGenerator.startTest("User select Destination City", pageName);
		selectOptionFromDropdown(arrivingIn, arriveLocation, "Arrive In");
	}
	
	@Given("user selects departure date as {string}")
	public void user_selects_departure_date_as(String departDay) {
		HTMLReportGenerator.startTest("User select departure Day and Month", pageName);
		selectOptionFromDropdown(departingDay, departDay, "Depart On Day");
		String departMonth = "June";
		selectOptionFromDropdown(departingMonth, departMonth, "Depart On Month");
		click(RegistrationPage.cross_icon);
	}
	
	@Given("user selects return date as {string}")
	public void user_selects_return_date_as(String returnDay) {
		HTMLReportGenerator.startTest("User select Return Day and Month", pageName);
		selectOptionFromDropdown(returningDay, returnDay, "Return Day");
		String returnMonth = "July";
		selectOptionFromDropdown(returningMonth, returnMonth, "Return Month");
	}

	@Given("user selects number of passengers as {string}")
	public void user_selects_number_of_passengers_as(String numberOfPassenger) {
		HTMLReportGenerator.startTest("User select numbet of Passenger", pageName);
		selectOptionFromDropdown(passengers, numberOfPassenger, "Passengers");
	}
	
	@Given("user selects service class as {string}")
	public void user_selects_service_class_as(String classType) {
		HTMLReportGenerator.startTest("User select Service Class Type", pageName);
		classType = classType.trim().toLowerCase();
		By locator = null;
		  if (classType.contains("economy")) {
		      locator = economyClass;
		  }else if (classType.contains("business")) {
		      locator = businessClass;
		  }else if (classType.contains("first")) {
		      locator = firstClass;
		  } else {
		      HTMLReportGenerator.logReport(Status.FAIL, "Invalid class provided: " + classType);
		      return;
		  }

		  if (isSelected(locator)) {
		   	HTMLReportGenerator.logReport(Status.PASS, classType + " already selected");
		  } else {
		      click(locator);
		      HTMLReportGenerator.logReport(Status.PASS, classType + " selected successfully");
		  }
	}
	
	@Then("verify selected default service class type")
	public static void verify_selected_service_class_type() {
		HTMLReportGenerator.startTest("User selected Service Class Type", pageName);
	    try {
	        WebElement economy = driver.findElement(economyClass);
	        WebElement business = driver.findElement(businessClass);
	        WebElement first = driver.findElement(firstClass);

	        if (economy.isSelected()) {
	            HTMLReportGenerator.logReport(Status.PASS, "Economy class is selected.", true);
	        } else if (business.isSelected()) {
	            HTMLReportGenerator.logReport(Status.PASS, "Business class is selected.", true);
	        } else if (first.isSelected()) {
	            HTMLReportGenerator.logReport(Status.PASS, "First class is selected.", true);
	        } else {
	            HTMLReportGenerator.logReport(Status.FAIL, "No service class is selected.", true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	@Given("user selects airline as {string}")
	public void user_selects_airline_as(String airlineName) {
		HTMLReportGenerator.startTest("User select Airline Preference", pageName);
		selectOptionFromDropdown(airlinePreference, airlineName, "Airline Name");
	}
	
	@When("user clicks on {string} button")
	public void user_clicks_on_continue_btn(String btn) {
		HTMLReportGenerator.startTest("Successfully clicked on Continue button", pageName);
		RegistrationPage.userClickOnBtn(continue_btn, "Continue");
	}
	
	@Then("user should see expected result {string}")
	public void user_should_see(String ActualMsg) {
		HTMLReportGenerator.startTest("Verify Expected Result after flight booking", pageName);
		String expected_msg = getText(no_flight_no_seat_msg).trim();
		verifyEquals(ActualMsg, expected_msg);
		HTMLReportGenerator.logReport(Status.PASS, "Verify Flight Not Available message ["+expected_msg+"]", true);
	}
	
	@Then("system should display error {string}")
	public void system_should_display_error(String ActualMsg) {
		HTMLReportGenerator.startTest("Verify No seat available or No flight available message", pageName);
		String expected_msg = getText(no_flight_no_seat_msg).trim();
		verifyEquals(ActualMsg, expected_msg);
		HTMLReportGenerator.logReport(Status.PASS, "Verify Flight Not Available message ["+expected_msg+"]", true);
	}
	
	@When("Verify Instruction message after Landing on Filght Menu {string}")
	public static void verify_nstruction_message(String Actual_instruction) {
		HTMLReportGenerator.startTest("Verify Fligt Booking & Finder Instruction", pageName);
		String expected_instruction = getText(flight_book_instruction).trim();
		verifyEquals(Actual_instruction, expected_instruction);
		HTMLReportGenerator.logReport(Status.PASS, "Verify Instruction message ["+expected_instruction+"]", true);
	}
	
	public static void RadioButtonCheck() {
        try {
            WebElement oneWayRadio = driver.findElement(oneWay_trip);
            WebElement roundTripRadio = driver.findElement(round_trip);
            if(oneWayRadio.isSelected()) {
            	HTMLReportGenerator.logReport(Status.PASS, "One-Way trip is selected by default.", true);
            } else if(roundTripRadio.isSelected()) {
            	HTMLReportGenerator.logReport(Status.PASS, "Round-Trip is selected by default.", true);
            } else {
            	HTMLReportGenerator.logReport(Status.FAIL,"No radio button is selected by default.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   
	}
	
	@Then("close browser window")
	public static void ClosedBrowser() throws InterruptedException {
		HTMLReportGenerator.startTest("Successfully closed Browser", pageName);
		closeBrowserWindow();
	}
	
	public static void handlePopupIfPresent() {
        try {
            List<WebElement> popupElements = driver.findElements(RegistrationPage.cross_icon);
            if (!popupElements.isEmpty() && popupElements.get(0).isDisplayed()) {
                popupElements.get(0).click();
                HTMLReportGenerator.logReport(Status.PASS, "Popup closed automatically.");
            }
        } catch (Exception e) {
        	HTMLReportGenerator.logReport(Status.INFO, "No popup appeared.");
        }
 }
}