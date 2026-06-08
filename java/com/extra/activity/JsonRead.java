package com.extra.activity;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class JsonRead {

	public static void main(String[] args) throws IOException,FileNotFoundException, org.json.simple.parser.ParseException, ParseException {
		JSONParser parser = new JSONParser();
		Object obj = parser.parse(new FileReader("./Reports/status.json"));
		JSONObject jsonObject = (JSONObject) obj;
		
		int total_count = Integer.parseInt(jsonObject.get("TOTAL").toString());
		int total_pass = Integer.parseInt(jsonObject.get("PASS").toString());
		int skips = Integer.parseInt(jsonObject.get("SKIP").toString());
		int total_fail = Integer.parseInt(jsonObject.get("FAIL").toString());
		
		Float pass_p = Float.parseFloat(String.format("%.2f", (float)total_pass/total_count*100));
		
		Float fail_p =  Float.parseFloat(String.format("%.2f", (float)total_fail/total_count*100));
		
		System.out.format("+-------+-------+------+--------------+--------------+-------+%n");
		System.out.format("|  PASS +  FAIL + SKIP + PASS_PERCENT + PASS_PERCENT + TOTAL |%n");
		System.out.format("+-------+-------+------+--------------+--------------+-------+%n");
		String leftAlignment = "| %-4s | %-4s | %-4s | %-12s | %-13s | %-6s |%n";
		
		for(int i=0; i<1; i++) {
			System.out.format(leftAlignment, total_pass, total_fail, skips, pass_p, fail_p, total_count);
		}
		System.out.format("+-------+-------+------+--------------+--------------+-------+%n");
	}
}
