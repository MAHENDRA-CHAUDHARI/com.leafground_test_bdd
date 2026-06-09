package com.testrunner;

import org.junit.runner.RunWith;
import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;

@RunWith(Cucumber.class)
@CucumberOptions
		(
				features="src/test/resources/BUSINESS_LOGIC/CUCUMBER_SUITES/BrowserMenu/FrameHandle.feature",
		        tags="@Regression",
//		        tags="@SmokeTest or @RegresTest",
		        glue={"com.stepdefinitions","com.utilityClasses"},
		        monochrome=true,
		        plugin={"pretty"},
		        dryRun = false
		)

public class RunTest
{
               
}
