package com.framework;
import java.util.concurrent.atomic.AtomicInteger;
import org.testng.IRetryAnalyzer;
import org.testng.ITestResult;

public class RetryAnalyzer implements IRetryAnalyzer {
	private final AtomicInteger retryCount = new AtomicInteger(0);
    private static final int MAX_RETRY_COUNT = 1;

    @Override
    public boolean retry(ITestResult result) {
        if (retryCount.getAndIncrement() < MAX_RETRY_COUNT) {
            result.setAttribute("RETRY", true);
            return true;  
        }
        return false; 
    }
}
