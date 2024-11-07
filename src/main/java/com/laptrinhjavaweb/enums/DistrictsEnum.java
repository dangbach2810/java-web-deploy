package com.laptrinhjavaweb.enums;

public enum DistrictsEnum {
	
	QUAN_1("Quận 1"),  
    QUAN_2("Quận 2"),  
    QUAN_3("Quận 3"),
    QUAN_4("Quận 4"),
    QUAN_5("Quận 5");
    private final String districtValue;

    DistrictsEnum(String districtValue) {
        this.districtValue = districtValue;
    }

	public String getDistrictValue() {
		return districtValue;
	}
}
