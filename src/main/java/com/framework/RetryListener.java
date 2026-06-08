package com.framework;

import java.util.Iterator;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class RetryListener implements ITestListener{
	
    @Override
    public void onTestStart(ITestResult result) {
//        System.out.println("Test Failed: " + result.getName());
    }
    
    @Override
    public void onTestFailure(ITestResult result) {
//        System.out.println("Test Failed: " + result.getName());
    }

    @Override
    public void onTestSuccess(ITestResult result) {
//        System.out.println("Test Passed: " + result.getName());
    }

    @Override
    public void onTestSkipped(ITestResult result) {
//        System.out.println("Test Skipped: " + result.getName());
    }
    
    @Override
    public void onFinish(ITestContext context) {
    	removeDuplicates(context.getFailedTests().getAllResults().iterator(), context);
    	removeDuplicates(context.getSkippedTests().getAllResults().iterator(), context);
    }
    
    private void removeDuplicates(Iterator<ITestResult> iterator, ITestContext context) {
    	 while (iterator.hasNext()) {
    	        ITestResult result = iterator.next();
    	        ITestNGMethod method = result.getMethod();
    	        if (context.getPassedTests().getResults(method).size() > 0) {
    	            iterator.remove();
    	        }
    	 }
    }
}
