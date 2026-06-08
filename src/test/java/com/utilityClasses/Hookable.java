package com.utilityClasses;

import java.net.UnknownHostException;
import com.aventstack.extentreports.Status;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.cucumber.java.Scenario;

public class Hookable extends SeleniumOperation{

	@BeforeAll
    public static void setup() throws UnknownHostException {
        HTMLReportGenerator.generateReport();
    }
	
	@Before
    public void before(Scenario scenario) {
        HTMLReportGenerator.TestCaseStart("Test Case", scenario.getName());
        HTMLReportGenerator.logReport(Status.INFO,"===== START: " + scenario.getName() + " =====",false);
    }
	
	@After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            HTMLReportGenerator.logReport(Status.FAIL,"Scenario Failed: " + scenario.getName(),true);
        } else {
            HTMLReportGenerator.logReport(Status.PASS,"Scenario Passed: " + scenario.getName());
        }
        HTMLReportGenerator.logReport(Status.INFO,"===== END: " + scenario.getName() + " =====");
        HTMLReportGenerator.TestCaseEnd();
    }
	
	 @AfterAll
	    public static void tearDown() {
	        if (HTMLReportGenerator.getExtent() != null) {
	            HTMLReportGenerator.getExtent().flush();
	        }
	    }
	 
	/*
    @Before
    public void before(Scenario scenario) throws UnknownHostException {
    	HTMLReportGenerator.generateReport();
    	HTMLReportGenerator.getExtent();
        HTMLReportGenerator.TestCaseStart("Test Cases Started","Executing Scenario");
        HTMLReportGenerator.logReport(Status.INFO, "===== START: " + scenario.getName() + " =====", false);
    }

    @After
    public void after(Scenario scenario) {
    	HTMLReportGenerator.logReport(Status.INFO,"===== END: " + scenario.getName() + " =====");
        HTMLReportGenerator.TestCaseEnd();
        HTMLReportGenerator.TestSuiteEnd();
    }
    */
}