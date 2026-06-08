package com.demoTour.Pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;
import com.framework.Base;

public class FlightReservationPage extends Base {
	
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
	
	public static void verifyFlightMenuAvailability(String menuName) {
		String expectedMenu = getText(flight_menu);
		if(menuName.equals(expectedMenu)) {
			logReport(Status.PASS, "Verify ["+expectedMenu+"] Menu", true);
		} else {
			logReport(Status.FAIL, "Flight Menu not matched",true);
		}
	}
	public static void clickOnFlightMenu() {
		if(isDisplayed(flight_menu)) {
			click(flight_menu);
			logReport(Status.PASS, "User successfully clicked on [Flight] Menu", true);
		} else {
			logReport(Status.FAIL, "Unable to click on [Flight] Menu",true);
		}
	}
	
	public static void RadioButtonCheck() {
        try {
            WebElement oneWayRadio = driver.findElement(oneWay_trip);
            WebElement roundTripRadio = driver.findElement(round_trip);
            if(oneWayRadio.isSelected()) {
            	logReport(Status.PASS, "One-Way trip is selected by default.", true);
            } else if(roundTripRadio.isSelected()) {
            	logReport(Status.PASS, "Round-Trip is selected by default.", true);
            } else {
            	logReport(Status.FAIL,"No radio button is selected by default.", true);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }   
	}
	
	public static void selectOneWayTrip() {
		if (!isSelected(oneWay_trip)) {
            click(oneWay_trip);
            logReport(Status.PASS,"One-Way trip selected.", true);
        } else {
        	logReport(Status.FAIL,"One-Way trip is already selected.", true);
        }
	}
	
	public static void selectPassenger(String numberOfPassenger) {
		RegUserDetailsPage.selectOptionFromDropdown(passengers, numberOfPassenger, "Passengers");
	}
	public static void selectDepartingFrom(String departFromLocation) {
		RegUserDetailsPage.selectOptionFromDropdown(departingFrom, departFromLocation, "Departing From");
	}
	public static void selectDepartingDay(String departDay) {
		RegUserDetailsPage.selectOptionFromDropdown(departingDay, departDay, "Depart On Day");
	}
	public static void selectArriveIn(String arriveLocation) {
		RegUserDetailsPage.selectOptionFromDropdown(arrivingIn, arriveLocation, "Arrive In");
	}
	public static void selectReturnDay(String returnDay) {
		RegUserDetailsPage.selectOptionFromDropdown(returningDay, returnDay, "Return Day");
	}
	
	public static void  selectOptionFromDropdown(By element, String optionName, String optionType) {
		selectDroprownUsingVisibleText(element, optionName);  hardPause(2);
		String getOptionName = RegUserDetailsPage.getSelectedOption(element).trim();  
		verifyEquals(optionName, getOptionName);
		logReport(Status.PASS, "User successfully Select "+optionType+" as ["+getOptionName+"]", true);
	}
	public static void selectAirlinePrefernces(String airlineName) {
		selectOptionFromDropdown(airlinePreference, airlineName, "Airline Name");
	}
	public static void selectDepartingMonth(String departMonth) {
		selectOptionFromDropdown(departingMonth, departMonth, "Depart On Month");
	}
	public static void selectReturnMonth(String returnMonth) {
		selectOptionFromDropdown(returningMonth, returnMonth, "Return Month");
	}
	
	public static void userClickOnContinueBtn() {
		RegUserDetailsPage.userClickOnBtn(continue_btn, "Continue");
	}
	
	public static void checkServiceClassSelected() {
	    try {
	        WebElement economy = driver.findElement(economyClass);
	        WebElement business = driver.findElement(businessClass);
	        WebElement first = driver.findElement(firstClass);

	        if (economy.isSelected()) {
	            logReport(Status.PASS, "Economy class is selected.", true);
	        } else if (business.isSelected()) {
	            logReport(Status.PASS, "Business class is selected.", true);
	        } else if (first.isSelected()) {
	            logReport(Status.PASS, "First class is selected.", true);
	        } else {
	            logReport(Status.FAIL, "No service class is selected.", true);
	        }
	    } catch (Exception e) {
	        e.printStackTrace();
	    }
	}
	
	public static void selectServiceClass(String classType) {
		classType = classType.trim().toLowerCase();
		By locator = null;
		  if (classType.contains("economy")) {
		      locator = economyClass;
		  }else if (classType.contains("business")) {
		      locator = businessClass;
		  }else if (classType.contains("first")) {
		      locator = firstClass;
		  } else {
		      logReport(Status.FAIL, "Invalid class provided: " + classType);
		      return;
		  }

		 WebElement element = driver.findElement(locator);
		  if (element.isSelected()) {
		   	logReport(Status.PASS, classType + " already selected");
		  } else {
		      element.click();
		      logReport(Status.PASS, classType + " selected successfully");
		  }
	}
	
	public static void verifyNoFlightMsg(String ActualMsg) {
		String expected_msg = getText(no_flight_no_seat_msg).trim();
		verifyEquals(ActualMsg, expected_msg);
		logReport(Status.PASS, "Verify Flight Not Available message ["+expected_msg+"]", true);
	}
	
	public static void verifyInstructionMsg(String Actual_instruction) {
		String expected_instruction = getText(flight_book_instruction).trim();
		verifyEquals(Actual_instruction, expected_instruction);
		logReport(Status.PASS, "Verify Instruction message ["+expected_instruction+"]", true);
	}
}
