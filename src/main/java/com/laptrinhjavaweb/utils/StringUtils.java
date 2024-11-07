package com.laptrinhjavaweb.utils;

public class StringUtils {
	public static boolean isNullOrEmpty(String value) {
		if(value != null && value != "") {
			return false;//co data
		}
		return true;//ko co data
	}
	public static boolean isNullOrEmpty(Object value) {
		if(value != null && value != "") {
			return false;//co data
		}
		return true;//ko co data
	}
	
}

