package com.laptrinhjavaweb.enums;

public enum SpecialSearchParamEnum {
    STAFF_ID("staffId"),
    RENT_PRICE_FROM("rentPriceFrom"),
    RENT_PRICE_TO("rentPriceTo"),
    RENT_AREA_FROM("rentAreaFrom"),
    RENT_AREA_TO("rentAreaTo"),
    BUILDING_TYPES("buildingTypes");

    private final String value;

    SpecialSearchParamEnum(String value){this.value = value;}

    public String getValue(){return value;}
}
