package com.laptrinhjavaweb.constant;

public class SystemConstant {
    /*Spring security 4: ROLE_ADMIN, Spring security 3 not required*/
    public static final String ADMIN_ROLE = "ROLE_ADMIN";
    /*Spring security 4: ROLE_ADMIN, Spring security 3 not required*/
    public static final String STAFF_ROLE = "ROLE_STAFF";
    public static final String MANAGER_ROLE = "ROLE_MANAGER";
    public static final String HOME = "/trang-chu";
    public static final String ADMIN_HOME = "/admin/home";
    public static final String MODEL = "model";
    public static final String INSERT_SUCCESS = "insert_success";
    public static final String UPDATE_SUCCESS = "update_success";
    public static final String DELETE_SUCCESS = "delete_success";
    public static final String ASSIGN_SUCCESS = "assign_success";
    public static final String ERROR_SYSTEM = "error_system";
    public static final String ALERT = "alert";
    public static final String MESSAGE_RESPONSE = "messageResponse";
    public static final String PASSWORD_DEFAULT = "123456";
    public static final String CHANGE_PASSWORD_FAIL = "change_password_fail";
    public static final String ONE_EQUAL_ONE = "where 1 = 1";
    public static final String GROUP_BY_BUILDING_ID = " group by b.id ";
    public static final String STAFF_MAP = "staffMap";
    public static final String DISTRICT_MAP = "districtMap";
    public static final String TRANSACTION_MAP = "transactionMap";
    public static final String BUILDING_TYPE_MAP = "buildingTypeMap";
    public static final String MESSAGE = "message";
    public static final String SUCCESS = "success";
    public static final String DANGER = "danger";
    public static final String BUILDING_ID = "buildingId";
    public static final int ACTIVE_STATUS = 1;

    public static final String BUILDING_ALIAS = "b.";
    public static final String EQUAL_OPERATOR = " = ";
    public static final String GREATER_THAN_OPERATOR = " >= ";
    public static final String LESS_THAN_OPERATOR = " <= ";
    public static final String RENT_AREA_FROM_SEARCH_PARAM = "rentAreaFrom";
    public static final String RENT_AREA_TO_SEARCH_PARAM = "rentAreaTo";
    public static final String RENT_PRICE_FROM_SEARCH_PARAM = "rentPriceFrom";
    public static final String RENT_PRICE_TO_SEARCH_PARAM = "rentPriceTo";
    public static final String STAFF_SEARCH_PARAM = "staffId";
    public static final String BUILDING_TYPE_SEARCH_PARAM = "buildingTypes";
    public static final String CUSTOMER_ALIAS = "c.";


    public static final String TRANSACTIONS = "transactions";
}
