package com.framework;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.openqa.selenium.By;
import org.testng.Assert;
import com.aventstack.extentreports.Status;

public class Common extends Base {
	final static String MAAHI_PROPERTYFILE_PATH = "./src/main/resources/maahi/config.properties";
	final static String MAAHIS_PROPERTYFILE_PATH = "./src/main/resources/maahis/config.properties";
	
	public static String getBaseURL() {
		String propertyEnv = System.getProperty("maahi.env");
		if(propertyEnv!=null) {
			return readPIRMProperty(propertyEnv+"_Env_BaseURL");
		}	
		return readPIRMProperty(readPIRMProperty("environment")+"_Env_BaseURL");
	}	
	
	public static String getBase2URL() {
		String propertyEnv = System.getProperty("maahi.env");
		if(propertyEnv!=null) {
			return readPIRMSProperty(propertyEnv+"_Env_BaseURL");
		}	
		return readPIRMSProperty(readPIRMSProperty("environment")+"_Env_BaseURL");
	}	
	
	public static String readSingleFileToByteArray(String filePath) {
		String byteConterted = null;
		try {
			File file =  new File(filePath);
			byte[] bytes = null;
			bytes = Files.readAllBytes(file.toPath());
			byteConterted = Base64.getEncoder().encodeToString(bytes);
		} catch (IOException e) {
			e.printStackTrace();
			logReport(Status.FAIL, e);
		}	
		return byteConterted;
	}
	
	public static String replaceValuesFromFile(String inputFilePath, HashMap<String, String> valuesToReplace) {
		Path path = Paths.get(inputFilePath);
		StringBuilder sb = new StringBuilder();
		try {
			BufferedReader reader= Files.newBufferedReader (path);
			try {
				String currentLine = "";
				while ((currentLine = reader.readLine()) != null) {
					{
						for (Entry<String, String> values: valuesToReplace.entrySet()) {
							currentLine = currentLine.replace(values.getKey(), values.getValue());
						}
						sb.append(currentLine);
					}
				}
				reader.close();
			} catch (IOException e) {
				e.printStackTrace();
				logReport(Status.FAIL, e.getLocalizedMessage());
			}
		} catch (IOException e) {
			e.printStackTrace();
			logReport(Status. FAIL, e.getLocalizedMessage());
		} return sb.toString();
	}
	private static String readGivenEngProperty(String engagement, String key) {
		try {
			Properties prop = new Properties();
			FileInputStream ip = new FileInputStream (engagement);
			prop.load(ip);
			return prop.getProperty(key);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String updateGivenEngProperty (String engagement, String key, String value) {
		try {
			FileInputStream in = new FileInputStream (engagement);
			Properties props = new Properties();
			props.load(in);
			in.close();
			FileOutputStream out = new FileOutputStream (engagement);
			props.setProperty(key, value);
			props.store(out, null);
			out.close();
	} catch (FileNotFoundException e) {
		e.printStackTrace();
	} catch (IOException e) {
		e.printStackTrace();
	}
		return null;
 }
	public static String getUniqueFileName (String extention) {
		return "TestFile" + DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS").format(LocalDateTime.now()) + "."
				+ extention;
	}
	
	public static String readPIRMProperty (String key) {
		return readGivenEngProperty (MAAHI_PROPERTYFILE_PATH, key);
	}
	
	public static String updatePIRMProperty (String key, String value) {
		return updateGivenEngProperty (MAAHI_PROPERTYFILE_PATH, key, value);
	} 
	
	public static String readPIRMSProperty (String key) {
		return readGivenEngProperty (MAAHIS_PROPERTYFILE_PATH, key);
	}
	
	public static String updatePIRMSProperty (String key, String value) { 
		return updateGivenEngProperty (MAAHIS_PROPERTYFILE_PATH, key, value);
	}
	
	public static String[] convertStrToArray (String str) {
		String[] tempArray = null;
		if (Objects.nonNull(str)) {
			tempArray = str.split("\n");
		}
		return tempArray;
	}
	
	public static int getRandomInteger(int maximum, int minimum) {
		return ((int) (Math.random() * (maximum-minimum))) + minimum;
	}
	
	public static boolean verifyTableExist(By element) {
		return isDisplayed(element);
	}
	
	public static void copyAndPasteFile(String src, String dest) {
		try {
			com.google.common.io.Files.copy(new File(src), new File(dest));
		} catch (IOException e) { 
			test.log(Status.SKIP, "Unable to copy file: " + e);
		}
	}
	public static boolean checkRunningProcess (String task) { 
		String filenameFilter = "/nh/fi \"Imagename eq "+task+"\"";
		String tasksCmd = System.getenv("windir") +"/system32/tasklist.exe "+filenameFilter;
		Process p= null;
		try {
			p = Runtime.getRuntime().exec(tasksCmd);
		} catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		ArrayList<String> procs = new ArrayList<String>();
		String line = null;
		try {
			while ((line = input.readLine()) != null)
				procs.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		try {
			input.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return procs.stream().filter(row -> row.indexOf(task) > -1).count() > 0;
	}
	
	public static void taskkill (String task, int maxWaitTime) {
		boolean processFound = false;
		for(int ite=0; ite<maxWaitTime; ite++){
		String filenameFilter = "/nh/fi \"Imagename eq "+task+"\"";
		String tasksCmd = System.getenv("windir") +"/system32/tasklist.exe "+filenameFilter;
		Process p= null;
		try {
			p = Runtime.getRuntime().exec(tasksCmd);
		}catch (IOException e) {
			e.printStackTrace();
		}
		BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
		ArrayList<String> procs = new ArrayList<String>();
		String line = null;
		try {
			while ((line = input.readLine()) != null)
				procs.add(line);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			input.close();
		}catch (IOException e) {
			e.printStackTrace();
		}
		processFound = procs.stream().filter(row -> row.indexOf(task) > -1).count() > 0;
		if(!processFound) {
			break;
		}
		hardPause(1);
	}
		if (processFound) {
		try {
			Runtime.getRuntime().exec("taskkill /f /im "+task);
		}catch (IOException e) {
			e.printStackTrace();
		}
	 }
 }
	public static String getTodaysDate (String format) {
		Date now = new Date(); //get current date
		Calendar calendar = Calendar.getInstance(); //get Java Calendar instance
		calendar.setTime(now); //set Calendar time to now
		SimpleDateFormat sdf= new SimpleDateFormat(format); //create a formatter for date
		return (sdf.format(now)); //format date as string
	}
	
	public static String getDownloadedFile(String basePath, String timeAfter, int waitSeconds, String fileExtension, String filecontains) {
		boolean fileFound = false;
		String filePath = "";
		
		Calendar waitTime = Calendar.getInstance();
		waitTime.add(Calendar. SECOND, waitSeconds);
		for(;true;) {
			Calendar nowTime = Calendar.getInstance();
			if(waitTime.after(nowTime)) {
				
			} else {
				break;
			}
			
			File folder = new File(basePath);
			File[] listOfFiles=folder.listFiles();
			
			for(File file: listOfFiles) {
				SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
				try {
					Date fileDate = sdf.parse(sdf.format(file.lastModified()));
					Date dateFrom= sdf.parse(timeAfter);
					if(file.getName().endsWith(fileExtension) && fileDate.after(dateFrom) && file.getName().contains(filecontains)) {
						fileFound = true;
						filePath = file.getPath();
						break;
					} 
				} catch (Exception e) {}
		} if(fileFound) {
			break;
		}
		}
		return filePath;
	}
	
	public static String[][] readDataFromSCVFile(String csvFilePath) throws IOException	{
		String thisLine;
		FileInputStream fis = new FileInputStream(csvFilePath);
		DataInputStream myInput = new DataInputStream(fis);
		
		List<String[]> lines = new 
		ArrayList<String[]>();
		while ((thisLine = myInput.readLine()) != null) {
			lines.add(thisLine.split(","));
		}
		String[][] array = new String[lines.size()][0];
		lines.toArray(array);
		return array;
	}
	
	public static String getExtensionOfFile(String fileNameWithExt) {
		int indx = fileNameWithExt.lastIndexOf(".");
		if(indx>=0) {
			return fileNameWithExt.substring(indx+1, fileNameWithExt.length());
		}
		return null;
	}
	
	public static String getFileNameOfFile(String fileNameWithExt) {
		int indx = fileNameWithExt.lastIndexOf(".");
		if(indx>=0) {
		return fileNameWithExt.substring(0, indx);
		}
		return null;
	}
	
	public static Object getDetailsFromToken (String token, String key) {
		String[] split_string = token.split("\\.");
		JSONObject payload = null;
		try {
			JSONParser parser = new JSONParser();
			JSONObject json = (JSONObject) parser.parse(new String(Base64.getUrlDecoder().decode(split_string[1])));
			payload = new JSONObject(json);
		}catch (Exception e) {
			Assert.fail("Invalid Token token. Please enter valid token details.");
		}
		return (Object) payload.get(key);
	}
	
	public static void resetImplicitWait() {
		driver.manage().timeouts().implicitlyWait(Long.valueOf(getConfigProp().getProperty("implicitlyWait")), TimeUnit.SECONDS);
	}
	
	public static List<String> getRolesFromToken(String token) {
		List<String> userRole = new ArrayList<String>();
		JSONObject payload = null;
		JSONArray jsonArray = new JSONArray();
		try {
			JSONParser parser = new JSONParser();   // String[] split_string = token.split([file://.]\\.);
			String[] split_string = token.split("\\.");
			JSONObject json = (JSONObject) parser.parse(new 
					String(Base64.getUrlDecoder().decode(split_string[1])));
			payload = new JSONObject(json);
			jsonArray = (JSONArray) payload.get("roles");
		}catch (Exception e) {
			Assert.fail("Invalid Token. Please enter valid token details.");
		}
		for (int i = 0; i < jsonArray.size(); i++) {
			JSONObject explrObject = (JSONObject) jsonArray.get(i);
			userRole.add((String) explrObject.get("displayName"));
		}
		return userRole;
	}
	
	public static String getPIRMLoginURL() {
		String propertyEnv = System.getProperty("pirm.env");
		if(propertyEnv!=null) {
			return readPIRMProperty (propertyEnv + "_portal_LoginUR1");
		}
			return readPIRMProperty(readPIRMProperty("environment") + "_portal_LoginURI");
	}
}
