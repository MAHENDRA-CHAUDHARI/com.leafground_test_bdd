package com.framework;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.Random;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import org.json.simple.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.ElementNotInteractableException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.Point;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.io.FileHandler;
import org.openqa.selenium.support.Color;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import javax.imageio.ImageIO;
import org.testng.Assert;
import org.testng.ITestResult;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.MediaEntityBuilder;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.Markup;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import io.appium.java_client.windows.WindowsDriver;

public class Base {
	public static WebDriver driver;
	public static WindowsDriver<WebElement> winDriver;
	public static Properties prop;
	static ExtentReports extent= null;
	static ExtentTest test = null;
	public static String reportPath = null;
	private static int passCount = 0;
	private static int failCount = 0;
	private static int skipCount = 0;
	private static String startTime = "";
	private static String browserTested = "";
	private static boolean failureTest = false;

	@BeforeSuite
	public void generateReport() {
		String actualSuite="";
		String suiteName =System.getProperty("maahi.testSuite");
		if(suiteName!=null) {
			actualSuite = "_"+Paths.get(suiteName).getFileName().toString().split("\\.")[0];
		}
		
		String envValue = System.getProperty("maahi.env");
		if(envValue!=null) {
			if(envValue.equalsIgnoreCase("test")) {
				System.setProperty("maahi.env", "test");
			}
		}
		
		String timeValue = "";
		if((suiteName==null)&&(envValue==null)) {
			timeValue = "_"+DateTimeFormatter.ofPattern("dd_MM_yyyy").format(LocalDateTime.now());
		}
		
		reportPath = System.getProperty("user.dir")+"/Reports/ExecutionReport"+timeValue;
		new File (reportPath).mkdir();
		new File (reportPath+"/Screenshots").mkdir();
		new File (reportPath+"/files").mkdir();
		
		extent = new ExtentReports();
		ExtentSparkReporter spark = new ExtentSparkReporter(reportPath + "/Execution Report.html");
		extent.attachReporter(spark);
		spark.config().setDocumentTitle("Automation Execution Report");
		spark.config().setReportName("Functional Suite Report");
		spark.config().setTheme(Theme.DARK);
		spark.config().setCss(".dark .detail-head h4 {color:#fff}");
		spark.config().setCss("h3 {font-size: 16px}");
		spark.config().setTimelineEnabled(true);
		
		extent.setSystemInfo(fetchProperty("environmentName"), fetchProperty("testEnv"));
		extent.setSystemInfo(fetchProperty("testType"), fetchProperty("testName"));
		extent.setSystemInfo(fetchProperty("buildVersionName"), fetchProperty("buildVersionNumber"));
		extent.setSystemInfo(fetchProperty("releaseVersionName"), fetchProperty("releaseVersionNumber"));
		extent.setSystemInfo(fetchProperty("qa"), fetchProperty("qaName"));
		startTime = DateTimeFormatter.ofPattern("MMM d,yyyy h:mm:ss a").format(LocalDateTime.now());
		extent.getTestSubject();
		
	}
	
	public static String fetchProperty(String name) {
		String propertyValue =  getConfigProp().getProperty(name);
		return propertyValue;
	}
	
	public static Properties getConfigProp() {
		if(prop == null) {
			try {
				prop = new Properties();
				FileInputStream ip = new FileInputStream("./src/main/resources/config.properties");
				prop.load(ip);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return prop;
	}
	
	@SuppressWarnings("unchecked")
	private static void updateStatusCount(boolean pass, boolean fail) {
		if(pass) {
			passCount=passCount+1;
		} else if (fail) {
			failCount =failCount+1;
		} else {
			skipCount=skipCount+1;
		}
		
		int total_count = passCount+failCount+skipCount;
		Float pass_p = Float.parseFloat(String.format("%.2f", (float)passCount/total_count*100));
		Float fail_p = Float.parseFloat(String.format("%.2f", (float)failCount/total_count*100));
		String browserName = getConfigProp().getProperty("browser");
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
	
	@AfterSuite
	public void transferReportToServer() {
		String buildID = "1";
		String suiteName = "maahi.testSuite";
		String propertyEnv = "maahi.env";
		if(propertyEnv!=null && buildID!=null && suiteName!=null) {
//			Jcraft.transferReportToServer(reportPath);
		}
	}
	
	public static void startTest(String testCaseName) {
		failureTest = false;
		test = extent.createTest(testCaseName);
	}
	
	public static void startTest(String testCaseName, String category) {
		failureTest = false;
		test = extent.createTest(testCaseName).assignCategory(category).assignAuthor(Thread.currentThread().getStackTrace()[2].getClassName());
	}
	
	public static void endTest(ITestResult result) {
		if (result.getStatus() == ITestResult. FAILURE) {
			updateStatusCount(false, true);
			try {
				test.fail("Test Failed. Capturing Screenshot", 
						MediaEntityBuilder.createScreenCaptureFromPath (takeScreenshot()).build());
				logReport (Status. FAIL, result.getThrowable());
			} catch (Exception e) {
				logReport (Status. FAIL, result.getThrowable());
			}
	}
		
	if (result.getStatus() == ITestResult.SKIP) {
		updateStatusCount(false, false);
		try {
			test.fail("Test Skipped. Capturing Screenshot",
					MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshot()).build());
			logReport (Status.SKIP, result.getThrowable());
		} catch (Exception e) {
			logReport (Status.SKIP, result.getThrowable());
		}
	}
	
	if (result.getStatus() == ITestResult.SUCCESS) {
		if (failureTest) {
			updateStatusCount(false, true);
		} else {
			updateStatusCount(true, false);
		} 
			logReport(Status. PASS, "Test Completed");
		}
		extent.flush();
	}
	
	public static String takeScreenshotWindow() {
		String screenShotName = UUID.randomUUID().toString();
		String path = reportPath + "/screenshots/" + screenShotName + ".jpeg";
		String retPath = "./screenshots/" + screenShotName + ".jpeg";
		try {
			File f = ((TakesScreenshot) winDriver).getScreenshotAs (OutputType. FILE);
			try {
				FileHandler.copy(f, new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		return retPath;
	}
	
	public static void endTestWindow (ITestResult result) {
		if (result.getStatus() == ITestResult. FAILURE) {
			updateStatusCount(false, true);
			try {
				test.fail("Test Failed. Capturing Screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath (takeScreenshotWindow()).build());
				logReport(Status.FAIL, result.getThrowable());
			}catch (Exception e) {
				logReport (Status.FAIL, result.getThrowable());
			}
		}
		
		if (result.getStatus() == ITestResult.SKIP) {
			updateStatusCount(false, false);
			try {
				test.fail("Test Skipped. Capturing Screenshot",
						MediaEntityBuilder.createScreenCaptureFromPath (takeScreenshotWindow()).build());
				logReport(Status. SKIP, result.getThrowable());
			} catch (Exception e) {
				logReport(Status. SKIP, result.getThrowable());
			}
		}
		if (result.getStatus() == ITestResult.SUCCESS) {
			updateStatusCount(true, false);
			logReport(Status.PASS, "Test Completed");
		}
		extent.flush();
	}
	
	public static void closeAutocadApplication() {
		if(winDriver!=null) {
			winDriver.quit();
		}
		Common.taskkill("acad.exe", 10);
	}
	
	public static void closeGivenApplication(String appExe) {
		if(winDriver!=null)  {
			winDriver.quit();
		}
		Common.taskkill(appExe, 10);
	}

	public static void logReport (Status status, String message, boolean screenshot) {
		if (screenshot) {
			test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath (takeScreenshot()).build());
		} else {
			test.log(status, message);
		}
		logStatus (status, message);
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

	public static String takeScreenshot() {
		String screenShotName = UUID.randomUUID().toString();
		String path = reportPath + "/screenshots/" + screenShotName + ".jpeg";
		String retPath = "./screenshots/" + screenShotName + ".jpeg";
		try {
			File f = ((TakesScreenshot) driver).getScreenshotAs (OutputType.FILE);
			try {
				FileHandler.copy(f, new File(path));
			} catch (IOException e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		return retPath;
	}
	
	public static void openDriver() {
		String browserName = getConfigProp().getProperty("browser");
		if (browserName.equals("chrome")) {
			System.setProperty("webdriver.chrome.driver", "drivers/chromedriver.exe");
			driver = new ChromeDriver();
			if(!browserTested.contains("Chrome")) {
				if (browserTested.equals("")) {
					browserTested = "Chrome";
				} else {
					browserTested = browserTested+", Chrome";
				}
			}
		} else if (browserName.equals("edge")) {		        
			System.setProperty("webdriver.edge.driver", "drivers/msedgedriver.exe");
			driver = new EdgeDriver();
			if(!browserTested.contains("Edge")) {
				if (browserTested.equals("")) {
					browserTested = "Edge";
				}else{
					browserTested = browserTested+", Edge";
				}
			}
		}
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(Long.valueOf(getConfigProp().getProperty("implicitlyWait")), TimeUnit.SECONDS);
	}
	
	public static void logStatus (Status status, Object message) {
		if (status == Status. PASS || status == Status. INFO) {
			System.out.println("["+Common.getTodaysDate("yyyy-MM-dd HH:mm:ss")+"] "+message);
		} else {
			System.err.println("["+Common.getTodaysDate("yyyy-MM-dd HH:mm:ss")+"] "+message);
		}
		if(status==Status.FAIL) {
			failureTest=true;
		}
	}
	
	public static WebElement locateAppElement (LocatorBy locatorBy, String locator) {
		switch (locatorBy) {
		
		case ACCESSIBILITY_ID:
			return winDriver.findElementByAccessibilityId(locator);
			
		case CLASS_NAME:
			return winDriver.findElementByClassName(locator);
		
		case CSS_SELECTOR:
			return winDriver.findElementByCssSelector(locator);
	
		case CUSTOM:
			return winDriver.findElementByCustom (locator);
	
		case ID:
			return winDriver.findElementById(locator);
	
		case IMAGE:
			return winDriver.findElementByImage (locator);
	
		case LINK_TEXT:
			return winDriver.findElementByLinkText(locator);
		
		case NAME:
			return winDriver.findElementByName (locator);
		
		case PARTIAL_LINK_TEXT:
			return winDriver.findElementByPartialLinkText (locator);
		
		case TAG_NAME:
			return winDriver.findElementByTagName(locator);
		
		case WINDOWS_UI_AUTOMATION:
			return winDriver.findElementByWindowsUIAutomation (locator);
		
		case XPATH:
			return winDriver.findElementByXPath (locator);
		}
		return null;
	}
	
	public static String captureElement(LocatorBy locatorBy, String locator) {
		String screenShotName = UUID.randomUUID().toString();
		String path = reportPath + "/screenshots/" + screenShotName + ".jpg";
		String retPath = "./screenshots/" + screenShotName + ".jpg";
		Point location = locateAppElement (locatorBy, locator).getLocation();
		Dimension size = locateAppElement(locatorBy, locator).getSize();
		File scrFile=((TakesScreenshot) winDriver).getScreenshotAs (OutputType.FILE);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(scrFile);
		} catch (IOException e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		
		BufferedImage croppedImage = bufferedImage.getSubimage (location.x, location.y, size.width, size.height);
		File pathFile = new File(path);
		try {
			ImageIO.write(croppedImage, "jpg", pathFile);
		} catch (IOException e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		return retPath;
	}
	
	public static String cropFromElement (LocatorBy locatorBy, String locator, int topLeftX, int topLeftY,int bottomRightX, int bottomRightY) {
		String screenShotName = UUID.randomUUID().toString();
		String path = reportPath + "/screenshots/" + screenShotName + ".jpg";
		String retPath = "./screenshots/" + screenShotName + ".jpg";
		Point location = locateAppElement(locatorBy, locator).getLocation(); Dimension size = locateAppElement(locatorBy, locator).getSize();
		File scrFile=((TakesScreenshot) winDriver).getScreenshotAs (OutputType.FILE);
		BufferedImage bufferedImage = null;
		try {
			bufferedImage = ImageIO.read(scrFile);
		} catch (IOException e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		
		BufferedImage locatorImage = bufferedImage.getSubimage(location.x, location.y, size.width, size.height);
		BufferedImage croppedImage = locatorImage.getSubimage(topLeftX, topLeftY, (bottomRightX-topLeftX), (bottomRightY-topLeftY));
		File pathFile = new File(path);
		try {
			ImageIO.write(croppedImage, "jpg", pathFile);
		} catch (IOException e) {
			test.log(Status.SKIP, "Can not take screenshot: " + e);
		}
		return retPath;
	}
	
	public static boolean switchToGivenAppWindow(String windowNameToSwitch, int maxWaitTimeSeconds) {
		for(int ite=0; ite<maxWaitTimeSeconds; ite++) {
			for(String window: winDriver.getWindowHandles()) {
				winDriver.switchTo().window(window);
				if(winDriver.getTitle().startsWith(windowNameToSwitch)) {
					logReportForWindow(Status.PASS, "Switched to window ["+windowNameToSwitch+"]", true);
					return true;
				}
			}
			hardPause(1);
		} 
		logReportForWindow(Status.SKIP, "Unable to Switched to window ["+windowNameToSwitch+"]", true);
		return false;
	}	
	
	public static void logReportForWindow (Status status, String message, boolean screenshot) {
		if (screenshot) {
			test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(takeScreenshotWindow()).build());
		} else {
			test.log(status, message);
		}
		logStatus (status, message);
	}
	
	public static void logReportForWindow (Status status, String message) {
		test.log(status, message);
		logStatus(status, message);
	}
	
	public static void logReportForWindow (Status status, String message, LocatorBy locatorBy, String locator) {
		test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(captureElement(locatorBy, locator)).build());
		logStatus (status, message);
	}
	
	protected void logReportForWindowForArray (Status info, String string, boolean screenshot) {
		logStatus (info, string);
	}
	
	public static void logReportForWindow (Status status, String message, LocatorBy locatorBy, String locator, int topLeftX, int topLeftY, int bottomRightX, int bottomRightY) {
		test.log(status, message, MediaEntityBuilder.createScreenCaptureFromPath(cropFromElement(locatorBy, locator,topLeftX,topLeftY,bottomRightX, bottomRightY)).build());
		logStatus (status, message);
	}
	
	public static void setImplicitWait(int timeout) {
		driver.manage().timeouts().implicitlyWait(timeout, TimeUnit.SECONDS);
	}
	
	public static void waitUntilElementVisible(By element, int waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.visibilityOfElementLocated (element));
	}
	
	public static void waitUntilElementSuppress (By element, int waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.invisibilityOfElementLocated(element)); 
	}
	
	public static void waitUntilElementLocated(By element, int waitTime) {
		new WebDriverWait(driver, waitTime).until(ExpectedConditions.presenceOfAllElementsLocatedBy(element));
	}
	
	public static void waitUntilElementClickable (By element, int waitTime) {
		new WebDriverWait(driver, waitTime).until (ExpectedConditions.elementToBeClickable (element));
	}
	
	public static void navigateURL (String url) {
		driver.get(url);
		logReport(Status. PASS, "Navigated to URL [" + url + "]");
	}
	public static void refreshPage() {
		driver.navigate().refresh(); hardPause(10);
		logReport (Status. PASS, "Refreshed Page");
	}
	
	public static void type (By element, CharSequence... value) {
		driver.findElement(element).sendKeys(value);
	}
	
	public static String generateString(int length) {
		String uuid = UUID.randomUUID().toString(); 
		uuid = uuid.substring(0, Math.min(uuid.length(), length));
		return uuid;
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
	
	public static void navigateBack() {
		driver.navigate().back();
	}
	
	public static void scrollToElement(By element) {
		try {
			((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);",
					driver.findElement(element));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static void zoom_In_Out_Size() {
		JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("document.body.style.zoom='0.70'");
	}
	
	public static void click(By element) {
		try {
			driver.findElement(element).click();
		} catch (ElementNotInteractableException e) {
			scrollToElement(element);
			driver.findElement (element).click();
		}
	}
	
	public static boolean isDisplayed(By element){
		try {
			return driver.findElement(element).isDisplayed();
		}catch (Exception e) {
			return false;
		}	
	}		
	
	public static boolean isSelected(By element){
		try {
			return driver.findElement(element).isSelected();
		}catch (Exception e) {
			return false;
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
		} catch (NoSuchElementException e) {
			return false;
		}
	}
	
	public static int getElementsCount (By element) { 
		return driver.findElements(element).size();
	}
	
	public static Actions getActionClass() {
		return new Actions (driver);
	}
	
	public static List<WebElement> getListOfWebElements (By elements) { 
		return driver.findElements (elements);
	}
	
	public static enum LocatorBy {
		ACCESSIBILITY_ID, CLASS_NAME, CSS_SELECTOR, CUSTOM, 
		ID, IMAGE, LINK_TEXT, NAME, PARTIAL_LINK_TEXT,
		TAG_NAME, WINDOWS_UI_AUTOMATION, XPATH
	}
	
	//UI color code verification code Snippest
	public static String btnColour (By tagName, String cssvalueName) {
		WebElement tag = driver.findElement(tagName); //
		String rgbyColor = tag.getCssValue(cssvalueName); // background-color
		String hexColour = Color.fromString(rgbyColor).asHex(); //Actual Color
		logReport(Status.PASS, "RGBA Color: ["+rgbyColor+"] is match with Hex code: ["+hexColour+"]");
		return hexColour;
	}
	public static String hiddenText(String a, String b, String c) {
		String actualText= ((JavascriptExecutor) driver).executeScript("return document.getElementBy"+a+" ('"+b+"'). "+c+"").toString();
		setImplicitWait(2);
		return actualText;
	}
	
	public static void popultatedDropDown (By element1, By element2, String dropdownText) {
		waitUntilElementLocated(element1, 2);
		click(element1); waitUntilElementLocated (element2, 2); hardPause(3);
		List <WebElement> dropdowns=driver.findElements(element2);
		for(int i=0; i<dropdowns.size(); i++) {
			WebElement dropdownsElements = dropdowns.get(i);
			String DDElementsValue=dropdownsElements.getText();
			if(DDElementsValue.contentEquals (dropdownText)) {
				dropdownsElements.click();
				break;
			}
		}
	}
	
	public static void checkStringBetween (By element, String mystring, int minstring, int maxstring) {
		type(element, mystring);
			if(mystring.length()>= minstring || mystring.length() <= maxstring) {
			} else {
				Assert.assertEquals(true, false);
			}
	}
	public static void stringValidation (By element, String mystring) {
		clear(element); type(element, mystring);
		String regex = "[a-zA-z0-9]";
		if(mystring.matches("[" +regex+ "]")) {
			System.out.println("Valid input data");
		} else {
			System.out.println("Special Character enter");
		}	
	}
	
	public static void getWindowHandles() {
		String parent_window = driver.getWindowHandle();
		System.out.println("parent_window: "+parent_window);
		Set<String> win = driver.getWindowHandles();
		Iterator<String> window = win.iterator();
		while(window.hasNext()) {
			String child_window= window.next();
			System.out.println("child_window: "+child_window);
			if(!parent_window.equals(child_window)) {
				driver.switchTo().window(child_window);
			}
		}
	}
	
	public static String getCurrentDate() {
		DateFormat dateFormat = new SimpleDateFormat("dd/MM/YYYY");
		Date date =new Date();
		String currentDate =dateFormat.format(date);
		return currentDate;
	}
	
	public static void partialMatch (By element1, By element2, By element3, String dropdownText) {
		driver.findElement(element1).click(); hardPause (2); clear(element2); hardPause (2);
		type (element2, dropdownText); hardPause (3);
		List <WebElement> dropdowns=driver.findElements(element3);
		List<String> matchedText = new ArrayList<>();
		for(int i=0; i<dropdowns.size(); i++) {
			matchedText.add(dropdowns.get(i).getText());   }
		List<String> matchElement = matchedText;
		logReport (Status. PASS, "Tota Element Present in DropDown: "+dropdowns.size());
		logReport(Status. PASS, "Entered Text is: ["+dropdownText+"] Matched element are: "+matchElement);
	}
	
	public static void ActionMoment (By element) {
		Actions a = new Actions (driver);
		WebElement ActionElement=driver.findElement(element);
		a.moveToElement(ActionElement);
		a.sendKeys (Keys.ARROW_DOWN);
		a.sendKeys (Keys.ENTER).build().perform();
	}
	
	public static void popultatVal (By element1, By element2, int j) {
		driver.findElement(element1).click();		hardPause (2);
		List <WebElement> dropdowns=driver.findElements (element2);
		for(int i=0; i<dropdowns.size(); i++) {
		WebElement dropdownsElements =dropdowns.get(i);
			if(j<dropdowns.size()) {
				dropdownsElements.click();
			}
		}
	}
	
	public static void doubleClickAction (By element) {
		Actions actions = new Actions (driver);
		WebElement elementLocator = driver.findElement(element);
		actions.doubleClick(elementLocator).perform();
	}
	
	public static void DropDownElementCount(By element1, By element2, String dropdownText) {
		waitUntilElementLocated (element1, 2);
		click(element1); waitUntilElementLocated (element2, 2);
		List <WebElement> dropdowns=driver.findElements (element2);
		logReport (Status.INFO, "Total Element Present in DropDown: "+dropdowns.size());
	}	
	
//Supplementary Method: Custom Date selector-Only for PrimeNG
	public static void selectCustomDate (By openCalander, By currentYearElement, By monthElement, By datelist, String year, String month, String day) {
		selectDroprownUsingVisibleText(currentYearElement, year);
		selectDroprownUsingVisibleText(monthElement, month);
		for (WebElement day_element : getListOfWebElements(datelist)) {
			String selectDay = day_element.getText();
			if (selectDay.equals(day)) {
				day_element.click();
				break;
			}
			String expectedDate = getAttributeValue(openCalander, "value");
			DateValidation (day, month, year, expectedDate);
		}
	}
	
//Date Validator
	public static void DateValidation (String Day, String Month, String Year, String expectedDate) {
		int d = Integer.parseInt(Day); int m = monthFormatter(Month);
		String day = (d < 10 ? "0": "") + d;
		String month = (m < 10? "0": "") + m;
		String actualDate = day+"/"+month+"/"+Year;
		logReport (Status.PASS, "Selected Date is: ["+day+ "/" +month+ "/" +Year+ "]");
			if(expectedDate.equalsIgnoreCase (actualDate)) {
				logReport(Status.PASS, "Expected: ["+expectedDate+"] & Actual: ["+actualDate+"] date matched");
			} else {
				logReport(Status.FAIL, "Expected and Actual date not matched");
		}
	}
	
	// Month Formatter
	public static int monthFormatter(String month) {
		String[] monthList = new String[] { "Select Months", "January", "February", "March", "April", "May", "June", "July", "August", "Septeber", "October", "November", "December"};
		int monthNumber = 0;
		for (int i = 0; i < monthList.length; i++) {
			if (month.equals(monthList[i])) {
				monthNumber = i;
				break;
			}
		} return monthNumber; 
	}
	
	public static void validateRequiredField_Error(By element1, By element2, String error_1) {
		clear(element1);		click(element1);
		getActionClass().sendKeys (driver.findElement(element1), Keys. TAB).build().perform(); 
		if (getText(element2).equals(error_1))
			logReport(Status.PASS, "verify mandatory field: ["+getText(element2)+"]", true);
		else
			logReport(Status. FAIL, "mandatory field not verified", true);
	}
	
	//Write and Read Text file
	public static void writeTextFile(By object) throws IOException {
		String textLine = getText(object);
		File file = new File("./src/main/resources/maahi_data/myData.txt");
		FileWriter wr =new FileWriter(file);
		BufferedWriter writer = new BufferedWriter (wr);
		writer.write(textLine);
		writer.close();
	}
	
	public static String readTextFile_PIRM() {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader("./src/main/resources/maahi_data/myData.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader txtReader = new BufferedReader (fileReader);
		String sCurrentLine = null;
		try {
			sCurrentLine = txtReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sCurrentLine;
	}	
		
	public static String readTextFile() {
		FileReader fileReader = null;
		try {
			fileReader = new FileReader("./src/main/resources/maahi_data/copyData.txt");
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		
		BufferedReader txtReader = new BufferedReader (fileReader);
		String sCurrentLine = null;
		try {
			sCurrentLine = txtReader.readLine();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return sCurrentLine;
	}
	
	//Store data from excel into copyData.txt-----> copy and paste respective location
	public static void writeTextintoNotepad (String textLine) throws IOException {
		File file = new File("./src/main/resources/maahi_data/copyData.txt");
		FileWriter wr =new FileWriter(file);
		BufferedWriter writer = new BufferedWriter(wr);
		writer.write(textLine);
		writer.close();
	}
	public static void zoomOut() throws AWTException {
		setImplicitWait(5);
		JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("document.body.style.zoom='67%'");
	}
	
	public static void zoomIn() throws AWTException {
		for(int i=0; i<3; i++){
			Robot robot = new Robot();
			robot.keyPress (KeyEvent. VK_CONTROL);
			robot.keyPress (KeyEvent. VK_ADD);
			robot.keyRelease (KeyEvent.VK_CONTROL);
			robot.keyRelease (KeyEvent. VK_ADD);
		}	
	}
		
	public static void verifyDownloadedFile() {
		File folder = new File(System.getProperty("user.home") +"\\Downloads");
		File [] listOfFiles = folder.listFiles();
		boolean found = false;
		String strPattern = "^[a-zA-Z0-9._-]+\\.(csv|xls)$";
		for (File listOfFile:listOfFiles) {
			if (listOfFile.isFile()) {
				String fileName = listOfFile.getName();
				logReport (Status.PASS, "Downloaded File is: ["+fileName+"]");
				if (fileName.matches(strPattern)) {
					found = true;
					Assert.assertTrue(found, "File is download");
				}
				listOfFile.deleteOnExit();
			logReport (Status. PASS, "Downloaded File: ["+listOfFile+"] is verified");
			}
		}
	}
	
	public static String randomTextasPerLimit(int j) {
		int k = j;
		String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
		StringBuilder sb = new StringBuilder();
		Random random = new Random();
		
		for (int i = 0; i < k; i++) {
			int index = random.nextInt(alphabet.length());
			char randomChar = alphabet.charAt(index);
			sb.append(randomChar);
		}
		String randomString = sb.toString();
		return randomString;
	}
	
	public static void getSessionID() {
		String id = driver.getWindowHandle();
		System.out.println("Session ID: "+id);
	}
	
	public static void PageLoadTimes() {
		driver.manage().timeouts().pageLoadTimeout(10,  TimeUnit.SECONDS);
	}
}
