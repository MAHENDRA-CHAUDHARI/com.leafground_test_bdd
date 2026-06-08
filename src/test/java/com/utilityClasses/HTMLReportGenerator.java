package com.utilityClasses;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.UUID;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.io.FileHandler;
import com.aventstack.extentreports.*;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.json.simple.JSONObject;

public class HTMLReportGenerator extends SeleniumOperation{
//	public static WebDriver driver;
	public static String reportPath = null;
	private static String startTime = "";
    private static ExtentReports extent = null;
    private static ExtentTest test = null;
    private static boolean isTestFailed = false;
    private static boolean failureTest = false;
	private static int passCount = 0;
	private static int failCount = 0;
	private static int skipCount = 0;

    public static void generateReport() throws UnknownHostException {
    	String  timeValue = "_"+DateTimeFormatter.ofPattern("dd_MM_yyyy").format(LocalDateTime.now());
        if (extent == null) {
        	reportPath = System.getProperty("user.dir")+"/Reports/ExecutionReport"+timeValue;
        	new File (reportPath).mkdir();
        	new File (reportPath+"/Screenshots").mkdir();
        	new File (reportPath+"/files").mkdir();
            ExtentSparkReporter spark = new ExtentSparkReporter(reportPath+"/Execution Report.html");
            spark.config().setDocumentTitle("Automation Execution Report");
    		spark.config().setReportName("Functional Suite Report");
    		spark.config().setTheme(Theme.DARK);
    		spark.config().setCss(".dark .detail-head h4 {color:#fff}");
    		spark.config().setCss("h3 {font-size: 16px}");
    		spark.config().setTimelineEnabled(true);
    		
            extent = new ExtentReports();
            extent.attachReporter(spark);

            extent.setSystemInfo("Host Name", InetAddress.getLocalHost().getHostName());
            extent.setSystemInfo("Environment", "TEST");
            extent.setSystemInfo("User Name", "QA");
        }
    }

    public static ExtentReports getExtent() {
        return extent;
    }

	public static void startTest(String testCaseName) {
		failureTest = false;
		test = extent.createTest(testCaseName);
	}
	
	public static void startTest(String testCaseName, String category) {
		failureTest = false;
		test = extent.createTest(testCaseName).assignCategory(category).assignAuthor(Thread.currentThread().getStackTrace()[2].getClassName());
	}
    public static void TestCaseStart(String TestName, String Description) {
        if (extent == null) {
            throw new RuntimeException("ExtentReports not initialized!");
        }
        test = extent.createTest(TestName, Description);
        isTestFailed = false;
    }

    public enum STATUS {
    	PASS, FAIL, SKIP, ERROR, INFO
    }
    public static void TestCaseEnd() {
        if (test == null) return;
        try {
            test.info("--------------------------------------------------");
            if (isTestFailed) {
                test.fail("FINAL STATUS: TEST CASE FAILED");
            } else {
                test.pass("FINAL STATUS: TEST CASE PASSED");
            }
            test.info("Execution End Time: " + getTodaysDate("yyyy-MM-dd HH:mm:ss"));
            test.info("--------------------------------------------------");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void TestSuiteEnd() {
        if (extent != null) {
            extent.flush();
        }
    }
    
	public static String getTodaysDate (String format) {
		Date now = new Date();
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(now);
		SimpleDateFormat sdf= new SimpleDateFormat(format);
		return (sdf.format(now));
	}
	
	public static void logStatus (Status status, Object message) {
		if (status == Status. PASS || status == Status. INFO) {
			System.out.println("["+getTodaysDate("yyyy-MM-dd HH:mm:ss")+"] "+message);
		} else {
			System.err.println("["+getTodaysDate("yyyy-MM-dd HH:mm:ss")+"] "+message);
		}
		if(status==Status.FAIL) {
			failureTest=true;
		}
	}
	
	public static void logReport (Status status, String message) {
		test.log(status, message);
		logStatus (status, message);
	}

	public static void logReport (Status status, Throwable t) {
		test.log(status, t);
		logStatus (status, t);
	}

	public static void logReport (Status status, Markup markup) {
		test.log(status, markup);
		logStatus (status, markup);
	}
	
    public static void logReport (Status status, String message, boolean screenshot) {
		if (screenshot) {
			test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath (takeScreenshot()).build());
		} else {
			test.log(status, message);
		}
		logStatus (status, message);
	}
	
    public static String takeScreenshot() {
    	String screenShotName = UUID.randomUUID().toString();
    	String path = reportPath + "/Screenshots/" + screenShotName + ".jpeg";
    	String retPath = "./Screenshots/" + screenShotName + ".jpeg";
    	try {
    		File src = (File) ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
    		try {
    			FileHandler.copy(src, new File(path));
    		} catch (IOException e) {
				e.printStackTrace();
			}
    	} catch (Exception e) {
			test.log(Status.SKIP, "Can not take screenshot: "+e);
		}
    	return retPath;
    }

    @SuppressWarnings("unchecked")
	public static void updateStatusCount(boolean pass, boolean fail) {
    	if(pass) {
    		passCount = passCount+1;
    	} else if(fail) {
    		failCount = failCount+1;
    	}else {
    		skipCount = skipCount+1;
    	}
    	
    	int total_count = passCount+failCount+skipCount;
    	Float pass_p = Float.parseFloat(String.format("%.2f", (float)passCount/total_count*100));
    	Float fail_p = Float.parseFloat(String.format("%.2f", (float)failCount/total_count*100));
    	String browserName = SeleniumOperation.getConfigProp().getProperty("browser");
    	JSONObject jsonObject = new JSONObject();
    	jsonObject.put("BROWSER", browserName);
		jsonObject.put("TOTAL", total_count);
		jsonObject.put("PASS", passCount);
		jsonObject.put("FAIL", failCount);
		jsonObject.put("SKIP", skipCount);
		jsonObject.put("PASS PERCENTAGE", pass_p);
		jsonObject.put("FAIL PERCENTAGE", fail_p);
		jsonObject.put("StartTime", startTime);
		jsonObject.put("EndTime", DateTimeFormatter.ofPattern("MMM d, yyyy h:mm:ss a").format(LocalDateTime.now()));
		
		try {
			FileWriter file = new FileWriter(reportPath+"/status.json");
			file.write(jsonObject.toJSONString());
			file.close();
		}catch (Exception e) {
			e.printStackTrace();
		}
    }
    
}