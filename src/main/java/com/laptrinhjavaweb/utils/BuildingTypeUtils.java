package com.laptrinhjavaweb.utils;

import com.laptrinhjavaweb.enums.BuildingTypesEnum;

import java.util.ArrayList;
import java.util.List;



public class BuildingTypeUtils {
	public static String convertBuildingType(String value) {	
		if(value != null) {
			List<String> newTypes = new ArrayList<>();
			//Map<String, String> mapTypes = getBuildingType();	
			for (String str : value.split(",")) {
				newTypes.add(BuildingTypesEnum.valueOf(str).getBuildingTypeValue());
			}
			return String.join(", ", newTypes);
		}
		return null;	
	}
}
