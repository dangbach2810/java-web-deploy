package com.laptrinhjavaweb.utils;

public class NumberUtils {
	public static boolean isInteger(String value) {
		if(value == null) {
			return false;
		}
		try {
			Integer result = Integer.parseInt(value);
		} catch(NumberFormatException nfe){
			return false;
		}
		return true;
	}
}
