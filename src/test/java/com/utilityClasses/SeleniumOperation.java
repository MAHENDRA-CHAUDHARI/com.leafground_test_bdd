package com.utilityClasses;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;
import java.util.Hashtable;
import java.util.NoSuchElementException;
import java.util.Properties;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import com.aventstack.extentreports.Status;
import org.junit.Assert;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class SeleniumOperation {
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
		HTMLReportGenerator.logReport(Status.PASS, "User successfully Select "+optionType+" as ["+getOptionName+"]", true);
	}
	
	public static void closeBrowserWindow() throws InterruptedException 	{
		Thread.sleep(5000);
		driver.close();
	}
}