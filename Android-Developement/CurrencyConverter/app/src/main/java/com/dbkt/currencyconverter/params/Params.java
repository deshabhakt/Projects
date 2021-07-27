package com.dbkt.currencyconverter.params;

public class Params {
     public static final int DB_VERSION = 1;
     public static final String DB_NAME = "Currency_Data";
     public static final String TABLE_NAME = "Currency_Table";
     public static boolean flag_toUpdateOrNot = false;

     public static int db_timestamp = 0;

    // column names of our table
    public static final String KEY_ID = "id";
    public static final String KEY_SHORT_NAME = "Short_Name";
    public static final String KEY_LONG_NAME = "Long_Name";
    public static final String KEY_RATES = "Rates";
    public static final String KEY_FLAG_LOCATION = "Flag_Location";
    public static final String KEY_BASE_CURRENCY = "Base_currency";
    public static final String KEY_TIMESTAMP = "TIMESTAMP";

}
