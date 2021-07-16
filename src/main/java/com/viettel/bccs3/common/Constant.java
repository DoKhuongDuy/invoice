package com.viettel.bccs3.common;

public class Constant {

    private Constant() {
    }

    public static final String EVERY_TEN_SECONDS = "*/10 * * * * *";
    public static final String EVERY_2MINUTE = "0 */2 * * * *";
    public static final String EVERY_LAST_DAY = "59 59 23 * * *";

    public static final Long CONFIG_RETRY = 5L;
    public static final String NOT_CREATED_INVOICE = "NOT_CREATED_INVOICE";
    public static final String INVOICE_NOT_EXIST = "INVOICE_NOT_EXIST";
    public static final String CREATE_INVOICE_AFTER_DATE = "CREATE_INVOICE_AFTER_DATE";
    public static final String SALE_TRANS_TYPE_NOT_INVOICE = "SALE_TRANS_TYPE_NOT_INVOICE";
    public static final String SALE_TRANS = "SALE_TRANS";
    public static final String SALE_TRANS_GENERAL = "SALE_TRANS_GENERAL";
    public static final String NO_SHOP_INVOICE_LIST = "NO_SHOP_INVOICE_LIST";
    public static final String NOT_FOUND_SALE_TRANS = "NOT_FOUND_SALE_TRANS";
    public static final String NOT_FOUND_SALE_TRANS_DETAIL = "NOT_FOUND_SALE_TRANS_DETAIL";
    public static final String NOT_FOUND_SALE_TRANS_GENERAL = "NOT_FOUND_SALE_TRANS_GENERAL";
    public static final String NOT_FOUND_ORIGINAL_SALE_TRANS = "NOT_FOUND_ORIGINAL_SALE_TRANS";
    public static final String NOT_FOUND_ORIGINAL_SALE_TRANS_GENERAL = "NOT_FOUND_ORIGINAL_SALE_TRANS_GENERAL";
    public static final String FROM_TRANS_ID_IS_NULL = "FROM_TRANS_ID_IS_NULL";
    public static final String TOTAL_TRANS_NOT_MAP = "TOTAL_TRANS_NOT_MAP";
    public static final String INVALID_INVOICE_STATUS = "INVALID_INVOICE_STATUS";
    public static final String TRANS_TYPE_NOT_INVOICE = "TRANS_TYPE_NOT_INVOICE";
    public static final String FORBIDDEN_INVOICE_FOR_OUTSIDE_COMPANY = "FORBIDDEN_INVOICE_FOR_OUTSIDE_COMPANY";
    public static final String TRANS_PAYMENT_INVALID = "TRANS_PAYMENT_INVALID";
    public static final String MERGE_INVOICE_LIST_CONTAINS_ADJUST_INVOICE = "MERGE_INVOICE_LIST_CONTAINS_ADJUST_INVOICE";
    public static final String MERGE_INVOICE_LIST_DATE_ERROR = "MERGE_INVOICE_LIST_DATE_ERROR";
    public static final String MERGE_INVOICE_LIST_RECEIVER_ERROR = "MERGE_INVOICE_LIST_RECEIVER_ERROR";
    public static final String MERGE_INVOICE_LIST_CUSTOMER_ID_ERROR = "MERGE_INVOICE_LIST_CUSTOMER_ID_ERROR";
    public static final String MERGE_INVOICE_LIST_CUSTOMER_NAME_ERROR = "MERGE_INVOICE_LIST_CUSTOMER_NAME_ERROR";
    public static final String MERGE_INVOICE_LIST_TIN_ERROR = "MERGE_INVOICE_LIST_TIN_ERROR";
    public static final String MERGE_INVOICE_LIST_CURRENCY_ERROR = "MERGE_INVOICE_LIST_CURRENCY_ERROR";
    public static final String NO_INVOICE_LIST = "NO_INVOICE_LIST";
    public static final String INVOICE_LIST_IS_BEING_PROCESSED = "INVOICE_LIST_IS_BEING_PROCESSED";
    public static final String NOT_UPDATE_INVOICE_LIST = "NOT_UPDATE_INVOICE_LIST";
    public static final String INVOICE_LENGTH_NOT_EXIST = "INVOICE_LENGTH_NOT_EXIST";

}
