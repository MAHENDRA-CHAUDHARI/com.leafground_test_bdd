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
	    generateReport();
	}
	
//	@Before
//    public void before(Scenario scenario) {
//		TestCaseStart(scenario.getName(),"Scenario Execution");
//        logReport(Status.INFO,"===== START: " + scenario.getName() + " =====",false);
//    }
	
//	@Before
//	public void before(Scenario scenario) {
//	    String category = "Unassigned";
//	    for (String tag : scenario.getSourceTagNames()) {
//	        if (tag.startsWith("@Cat")) {
//	            category = tag.replace("@", "");
//	            break;
//	        }
//	    }
//	    TestCaseStart(scenario.getName(), category);
//	    logReport(Status.INFO,"===== START: " + scenario.getName() +" | CATEGORY: " + category + " =====",false);
//	}
	@Before
	public void before(Scenario scenario) {

	    String executionTag = "NA";
	    String category = "Unassigned";

	    for (String tag : scenario.getSourceTagNames()) {
	        String cleanTag = tag.replace("@", "");

	        if (cleanTag.equalsIgnoreCase("Smoke") ||
	            cleanTag.equalsIgnoreCase("Regression") ||
	            cleanTag.equalsIgnoreCase("Sanity")) {
	            executionTag = cleanTag;
	        } else if (!cleanTag.equalsIgnoreCase("Test")) {
	            category = cleanTag;
	        }
	    }
	    startScenarioTest(scenario.getName(), category);
	    logReport(Status.INFO,"START: " + scenario.getName() +
	            " | EXECUTION: " + executionTag +" | CATEGORY: " + category,false);
	}
	
	@After
    public void after(Scenario scenario) {
        if (scenario.isFailed()) {
            logReport(Status.FAIL,"Scenario Failed: " + scenario.getName(),true);
        } else {
            logReport(Status.PASS,"Scenario Passed: " + scenario.getName());
        }
        logReport(Status.INFO,"===== END: " + scenario.getName() + " =====");
        TestCaseEnd();
    }
	
	 @AfterAll
	    public static void tearDown() {
	        if (getExtent() != null) {
	            getExtent().flush();
	        }
	    }
	 
	/*
    @Before
    public void before(Scenario scenario) throws UnknownHostException {
    	generateReport();
    	getExtent();
        TestCaseStart("Test Cases Started","Executing Scenario");
        logReport(Status.INFO, "===== START: " + scenario.getName() + " =====", false);
    }

    @After
    public void after(Scenario scenario) {
    	logReport(Status.INFO,"===== END: " + scenario.getName() + " =====");
        TestCaseEnd();
        TestSuiteEnd();
    }
    */
}