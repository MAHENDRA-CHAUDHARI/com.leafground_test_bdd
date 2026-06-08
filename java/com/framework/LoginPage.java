package com.framework;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.testng.Assert;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.markuputils.ExtentColor;
import com.aventstack.extentreports.markuputils.MarkupHelper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;

public class LoginPage extends Base{

	public static List<String> loginApplication() {
		navigateURL (Common.getPIRMLoginURL());
		setImplicitWait(5);
		
		if (isDisplayed(By.xpath("//button[@id='details-button']"))) {
			click(By.xpath("//button[@id='details-button']"));
			click(By.xpath("//a[@id='proceed-link']"));
		}
		Common.resetImplicitWait();
		addTokenToCookie();
		List<String> loginDetails = new ArrayList<String>();
		loginDetails.add(Common.getDetailsFromToken(Common.readPIRMProperty("token2"), "email")+" ["+Common.getDetailsFromToken(Common.readPIRMProperty("token2"), "ein")+"]");
		loginDetails.addAll(Common.getRolesFromToken (Common.readPIRMProperty("token2")));
		logReport(Status. INFO, MarkupHelper.createLabel("Login Details", ExtentColor.GREY));
		logReport(Status. INFO, MarkupHelper.createOrderedList(loginDetails));
		return loginDetails;
	}
	
	public static List<String> loginApplication(String tokenNumber) {
		navigateURL (Common.getPIRMLoginURL());
		setImplicitWait(5);
		if (isDisplayed(By.xpath("//button[@id='details-button']"))) {
			click(By.xpath("//button[@id='details-button']"));
			click(By.xpath("//a[@id='proceed-link']"));
		}
		Common.resetImplicitWait(); 
		addTokenToCookie (tokenNumber);
		return logTokenDetails(tokenNumber);
	}

	public static void addTokenToCookie( ) {
		String regenURL = getRegenerateUrl();
		HashMap <String, List<String>> tokens = getTokenDetails();
		for(Map.Entry<String, List<String>> entry:tokens.entrySet()) {
			if (CheckTokenexpire (entry.getKey())) {
				logReport(Status.INFO, "Token is about to Expire in 40 mins. Regenerating new token for token["+entry.getValue()+"]");
						String newTokenGen = regenerateToken (entry.getKey(), regenURL);
				if (newTokenGen==null) {
					Assert.fail("Unable to generate new token ");
				}
				for(int i=0;i<entry.getValue().size();i++) {
					Common.updatePIRMProperty(entry.getValue().get(i), newTokenGen);
					if(entry.getValue().get(i).equals("token2")) {
						driver.manage().addCookie(new Cookie("IDToken", newTokenGen));
					}
				}
			} else {
				driver.manage().addCookie(new Cookie("IDToken", Common.readPIRMProperty("token2")));
			}
		}
	}

	public static void addTokenToCookie(String token) {
		String initToken = Common.readPIRMProperty(token);
		if(CheckTokenexpire (initToken)) {
			logReport(Status.INFO, "Token is about to Expire in 45 mins. Regenerating new token for token ["+token+"]");
			String regenURL = getRegenerateUrl();
			String newTokenGen = regenerateToken (initToken, regenURL);
		if(newTokenGen==null) {
			Assert.fail("Unable to generate new token ");
		}		
		Common.updatePIRMProperty (token, newTokenGen);
		driver.manage().addCookie(new Cookie("IDToken", newTokenGen));
		} else {
			driver.manage().addCookie(new Cookie("IDToken", initToken)); 
		}
	}

	public static String getValidToken(String tokenID) {
		String initToken = Common.readPIRMProperty (tokenID);
		if (CheckTokenexpire (initToken)) {
		logReport(Status.INFO, "Token is about to Expire in 45 mins. Regenerating new token for token ["+tokenID+"]");
		String regenURL = getRegenerateUrl();
		String newTokenGen = regenerateToken(initToken, regenURL);
		if(newTokenGen==null) {
			Assert.fail("Unable to generate new token ");
		} Common.updatePIRMProperty (tokenID, newTokenGen);
			hardPause(1);
			return newTokenGen;
		} else {
			hardPause(1);
			return initToken;
		}
	}
	
	public static boolean CheckTokenexpire(String token) {
		ZoneId istZone = ZoneId.of("Asia/Kolkata");
		long exp = (long) Common.getDetailsFromToken(token, "exp");
		Instant instant = Instant.ofEpochSecond(exp);
		LocalDateTime expireDateTime = LocalDateTime.ofInstant(instant, istZone);
		Instant currentInstant = Instant.now();
		LocalDateTime expectedWorkTime = LocalDateTime.ofInstant (currentInstant, istZone).plusMinutes (35);
		if(expireDateTime.isBefore(expectedWorkTime)) {
			return true;
		} else {
			return false;
		}
	}
	
	private static String getRegenerateUrl() {
		String regenURL =null;
		if(Common.readPIRMProperty("environment").equals("test")) {
			regenURL="https://webPageURL-cstest.nat.bt.com/auth/v1/regenerateUserToken";
		}else if(Common.readPIRMProperty("environment").equals("modern")) {
			regenURL = "https://webPageURL-cstest.nat.bt.com/auth/v1/regenerateUserToken";
		}else if(Common.readPIRMProperty("environment").equals("preprod")) {
			regenURL = "https://webPageURL-preprod.nat.bt.com/auth/v1/regenerateUserToken";
		}else if(Common.readPIRMProperty("environment").equals("dev")) {
			regenURL = "https://webPageURL-develop.nat.bt.com/auth/v1/regenerateUserToken";
		}else if (Common.readPIRMProperty("environment").equals("uat")) {
			regenURL = "https://webPageURL-uatest.nat.bt.com/auth/v1/regenerateUserToken";
		}else if(Common.readPIRMProperty("environment").equals("live")) {
			regenURL = "https://webPageURL-live.nat.bt.com/auth/v1/regenerateUserToken";
		}else{
			Assert.fail("Invalid Environment. Please enter valid Environment Details in config properties.");
		}
		return regenURL;
	}
	
	public static HashMap <String, List<String>> getTokenDetails(){
		HashMap <String, List<String>> tokens = new HashMap<String, List<String>>();
		int ite=1;
		do {
			List<String> tokenId = new ArrayList<String>();
			if(tokens.containsKey(Common.readPIRMProperty("token"+ite))) {
				tokenId.addAll(tokens.get(Common.readPIRMProperty("token"+ite)));
				tokenId.add("token"+ite);
				tokens.put(Common.readPIRMProperty("token"+ite), tokenId);
			}else {
				tokenId.add("token"+ite);
				tokens.put(Common.readPIRMProperty("token"+ite), tokenId);
			}
			ite++;
		} while (Common.readPIRMProperty("token"+ite)!=null);
		return tokens;
	}

	public static String regenerateToken(String token, String url) {
		String newToken = "";
		Response response = RestAssured.given().auth().oauth2 (token).relaxedHTTPSValidation().contentType(ContentType.JSON).get(url);
		JsonPath jsonPathEvaluator = response.jsonPath();
		newToken = jsonPathEvaluator.get("data. IDToken");
		System.out.println("new token:: "+newToken);
		return newToken;
	}
	
	public static List<String> logTokenDetails (String tokenNumber) {
		List<String> loginDetails = new ArrayList<String>();
		loginDetails.add(Common.getDetailsFromToken (Common.readPIRMProperty(tokenNumber), "email")+" ["+Common.getDetailsFromToken(Common.readPIRMProperty(tokenNumber), "ein")+"]");
		loginDetails.addAll(Common.getRolesFromToken (Common.readPIRMProperty(tokenNumber))); 
//		logReport(Status.INFO, MarkupHelper.createLabel("Login Details", ExtentColor.GREY)); ));
//		logReport(Status.INFO, MarkupHelper.createOrderedList (loginDetails));
		return loginDetails;
	}

}
