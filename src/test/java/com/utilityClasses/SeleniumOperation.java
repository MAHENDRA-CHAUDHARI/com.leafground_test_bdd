package com.utilityClasses;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;

import org.junit.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.simple.JSONObject;

public class SeleniumOperation {
	public static String reportPath = null;
	private static String startTime = "";
    private static ExtentReports extent = null;
    private static ExtentTest test = null;
    private static boolean isTestFailed = false;
    private static boolean failureTest = false;
	private static int passCount = 0;
	private static int failCount = 0;
	private static int skipCount = 0;
	public static Hashtable<String,Object> outputParameters=new Hashtable<String,Object>();
    public  static WebDriver driver;
    public static Properties prop;
    public static String fetchProperty(String name) {
		String propertyValue =  getConfigProp().getProperty(name);
		return propertyValue;
	}
    
    public static Properties getConfigProp() {
    	if(prop == null) {
    		try {
    			prop = new Properties();
    			FileInputStream ip = new FileInputStream("./src/test/resources/propertyFiles/config.properties");
    			prop.load(ip);
    		}catch (FileNotFoundException e) {
				e.printStackTrace();
			}catch (IOException e) {
				e.printStackTrace();
			}
    	} 
    	return prop;
    }
    
  	//open browser
	public static void browserLaunch() {
  		try {
  		  String browserName = getConfigProp().getProperty("browser");	
  		  String path = getConfigProp().getProperty("browserPath");  		                 
  
			switch (browserName.toLowerCase()) {
			case "chrome":
				System.setProperty("webdriver.chrome.driver", path.trim());
				driver = new ChromeDriver();
				break;

			case "ff":
			case "firefox":
				System.setProperty("webdriver.gecko.driver", path.trim());
				driver = new FirefoxDriver();
				break;

			case "edge":
				System.setProperty("webdriver.edge.driver", path.trim());
				driver = new EdgeDriver();
				break;

			default:
				throw new Exception("Unsupported browser: " + browserName);
			}

			driver.manage().window().maximize();
			driver.manage().deleteAllCookies();
			driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(30));

			outputParameters.put("STATUS", "PASS");
			outputParameters.put("MESSAGE", "ACTION :Open Browser, Browser Used: " + browserName);

  		    } catch(Exception e) {
  		        outputParameters.put("STATUS", "FAIL");
  		        outputParameters.put("MESSAGE", "ACTION :Open Browser failed | Exception: " + e.getMessage());
  		        e.printStackTrace();
  		    }
  		}
  	  	
  	//openApplication
	public static void openApplication() {
	    try {
	        String url = getConfigProp().getProperty("base_url").trim();
	        driver.get(url);
	        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(20));
	        outputParameters.put("STATUS", "PASS");
	        outputParameters.put("MESSAGE", "ACTION: Open Application, URL: " + url);

	    } catch (Exception e) {
	        outputParameters.put("STATUS", "FAIL");
	        outputParameters.put("MESSAGE", "ACTION: Open Application failed | Exception: " + e.getMessage());
	        e.printStackTrace();
	    }
	}
  	
  	//sendKey 
  	public static Hashtable<String,Object> sendKey(Object[] inputParameters) {
  		try {
  			String value1=(String)inputParameters[1];
  			By locator = (By) inputParameters[0]; 
  	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
  	       
  	        element.sendKeys(value1);
  	        
	  		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	  		outputParameters.put("STATUS", "PASS");
	    	outputParameters.put("MESSAGE", "ACTION :SendKey, Input Given :"+inputParameters[1].toString());
  		}catch(Exception e) {
	  		outputParameters.put("STATUS", "FAIL");
	  	    outputParameters.put("MESSAGE", "ACTION :SendKey, Input Given :"+inputParameters[1].toString());
  		} 	
  		return outputParameters;
  	}
  	
  	//drop down
  	public static Hashtable<String,Object> dropdown(Object[] inputParameters){
  		try {
  			By locator = (By) inputParameters[0]; 
  			String value=(String)inputParameters[1];
  			
  	        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
  	        WebElement element = wait.until(ExpectedConditions.elementToBeClickable(locator));
	  		
	  		Select sel=new Select(element);
	  		sel.selectByVisibleText(value);
	  		driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(60));
	  		
	  		outputParameters.put("STATUS", "PASS");
	    	outputParameters.put("MESSAGE", "ACTION :DropDown, Input Given :"+inputParameters[1].toString());
  		
  		}catch(Exception e) {
	  		outputParameters.put("STATUS", "FAIL");
	  	    outputParameters.put("MESSAGE", "ACTION :DropDown, Input Given :"+inputParameters[1].toString());
  		}
  		return outputParameters; 		
  	}

 //Click 	
  	public static void click(By element) {
  		try {
  			driver.findElement(element).click();
  		}catch (ElementClickInterceptedException e) {
			e.printStackTrace();
			driver.findElement(element).click();
		}
  	}
  	
  	public static String getText(By element) {
		return driver.findElement(element).getText();
	}
	
	public static String getTitle() {
		return driver.getTitle();
	}
	
	public static void verifyEquals(Object actual, Object expected) {
		Assert.assertEquals(actual, expected);
	}	
	
	public static void isEqualIgnoringCase(String actual, String expected) {
	}	
	public static void verifyEqualIgnoringCase(String actual, String expected) {
		isEqualIgnoringCase(actual, expected);
	}
	
	public static void selectDroprownUsingIndex (By element, int index) {
		Select select = new Select(driver.findElement(element));
		select.selectByIndex (index);
	}
	public static void selectDroprownUsingVisibleText(By element, String visibleText) {
		Select select = new Select(driver.findElement(element));
		select.selectByVisibleText(visibleText);
	}
	public static void selectDroprownUsingValue(By element, String valueText) {
		Select select = new Select(driver.findElement(element));
		select.selectByValue(valueText);
	}
	
	public static String getAttributeValue(By element, String attribute) { 
		return driver.findElement(element).getAttribute(attribute);
	}
	
	public static boolean isEnabled(By element) {
		try {
			return driver.findElement(element).isEnabled();
		}catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static boolean isDisplayed(By element) {
		try {
			return driver.findElement(element).isDisplayed();
		}catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static boolean isSelected(By element) {
		try {
			return driver.findElement(element).isSelected();
		}catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static void type(By element, String values) {
		driver.findElement(element).sendKeys(values);
	}
	
	public static void clear (By element) { driver.findElement(element).clear();
		driver.findElement(element).clear();
	}
	
	public static void hardPause(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	//Mouse Hover
	public static Hashtable<String,Object> mouseOver(Object[]inputParameters){
		try	{
			String value=(String)inputParameters[0];
			By locator = (By) inputParameters[1];
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			
			Actions act=new Actions(driver);
			act.moveToElement(element).build().perform();
			log.info("Successfully moved to given element: {}", locator.toString());
			Thread.sleep(1000);
			
			outputParameters.put("STATUS", "PASS");
	    	outputParameters.put("MESSAGE", "ACTION :MouseHover, Input Given :"+inputParameters[0].toString());
		} catch(Exception e) {
			outputParameters.put("STATUS", "FAIL");
		    outputParameters.put("MESSAGE", "ACTION :MouseHover, Input Given :"+inputParameters[0].toString());
		}
		return outputParameters;
	}
	
	//Validation
	private static final Logger log = LogManager.getLogger(SeleniumOperation.class);
	public static Hashtable<String, Object> validation(Object[] inputParameters) {
	    try {
	        String given = (String) inputParameters[0];
	        By locator = (By) inputParameters[1];
			WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
			WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(locator));
			
	        String actualText = element.getText().trim();

	        if (actualText.equalsIgnoreCase(given.trim())) {
	            log.info("Test Case PASS | Expected: {} | Actual: {}", given, actualText);
	            outputParameters.put("STATUS", "PASS");
	            outputParameters.put("MESSAGE",
	                    "Validation successful. Expected: " + given + " | Actual: " + actualText);
	        } else {
	            log.error("Test Case FAIL | Expected: {} | Actual: {}", given, actualText);
	            outputParameters.put("STATUS", "FAIL");
	            outputParameters.put("MESSAGE",
	                    "Validation failed. Expected: " + given + " | Actual: " + actualText);
	        }
	    } catch (Exception e) {
	        log.error("Test Case FAIL due to exception", e);	        outputParameters.put("STATUS", "FAIL");
	        outputParameters.put("MESSAGE",
	                "Validation failed due to exception: " + e.getMessage());
	    } 
	    return outputParameters;
	}

    public static String getSelectedOption(By locator) {
        WebElement element = driver.findElement(locator);
        Select select = new Select(element);
        return select.getFirstSelectedOption().getText().trim();
    }
	public static void  selectOptionFromDropdown(By element, String optionName, String optionType) {
		selectDroprownUsingVisibleText(element, optionName);  hardPause(2);
		String getOptionName = getSelectedOption(element).trim();  
		verifyEquals(optionName, getOptionName);
		logReport(Status.PASS, "User successfully Select "+optionType+" as ["+getOptionName+"]", true);
	}
	
	public static void closeBrowserWindow() throws InterruptedException 	{
		Thread.sleep(5000);
		driver.close();
	}
	
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
	
//	public static void startTest(String testCaseName, String category) {
//		failureTest = false;
//		test = extent.createTest(testCaseName).assignCategory(category).assignAuthor(Thread.currentThread().getStackTrace()[2].getClassName());
//	}
//	public static void startScenarioTest(String scenarioName, String category) {
//	    failureTest = false;
//	    test = extent.createTest(scenarioName); 
//	    test.assignCategory(category); 
//	    test.assignAuthor(Thread.currentThread() .getStackTrace()[2].getClassName());
//	}
	public static void startScenarioTest(String scenarioName, String category) {
	    failureTest = false;
	    test = extent.createTest(scenarioName);
	    test.assignCategory(category); 
	    test.assignAuthor(
	        Thread.currentThread()
	              .getStackTrace()[2]
	              .getClassName());
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
	public static void setImplicitWait(int timeout) {
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}
	
	public static void waitUntilElementVisible(By element, Duration waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.visibilityOfElementLocated (element));
	}
	
	public static void waitUntilElementSuppress (By element, Duration waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.invisibilityOfElementLocated(element)); 
	}
	
	public static void waitUntilElementLocated(By element, Duration waitTime) {
		new WebDriverWait(driver, waitTime).until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
	}
	
	public static void waitUntilElementClickable (By element, Duration waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.elementToBeClickable (element));
	}
	
	public static void waitUntilAlertPresent(Duration waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.alertIsPresent());
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