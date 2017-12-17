package com.example.user.homework.data;


import android.provider.BaseColumns;

public final class HomeworkContract {
    public static final String DB_NAME="homework.db";
    public static final int DATABASE_VERSION = 2;
    private static final String TEXT_TYPE = " TEXT";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HomeworkContract() {}

    /* Inner class that defines the table contents */
    public static class Homework implements BaseColumns {
        public static final String TABLE_NAME="Company";
        public static final String KEY_NAME = "Name";
        public static final String KEY_ADDR = "Addr";
        public static final String KEY_PHONE = "Phone";
        public static final String KEY_IMAGE = "Image";
        public static final String KEY_TIME = "Optime";
        public static final String KEY_LATI = "Lati";
        public static final String KEY_LONG = "Long";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_NAME + TEXT_TYPE + COMMA_SEP +
                KEY_ADDR + TEXT_TYPE + COMMA_SEP +
                KEY_PHONE + TEXT_TYPE + COMMA_SEP +
                KEY_IMAGE + TEXT_TYPE + COMMA_SEP +
                KEY_TIME + TEXT_TYPE +  COMMA_SEP +
                KEY_LATI + REAL_TYPE + COMMA_SEP +
                KEY_LONG + REAL_TYPE +" )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
