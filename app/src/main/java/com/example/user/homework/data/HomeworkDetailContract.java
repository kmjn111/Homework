package com.example.user.homework.data;


import android.provider.BaseColumns;

public final class HomeworkDetailContract {
    public static final String DB_NAME="homeworkDetail.db";
    public static final int DATABASE_VERSION = 1;
    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA_SEP = ",";
    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private HomeworkDetailContract() {}

    /* Inner class that defines the table contents */
    public static class HomeworkDetail implements BaseColumns {
        public static final String TABLE_NAME="CompanySub";
        public static final String KEY_PARENT = "parent";
        public static final String KEY_TITLE = "title";
        public static final String KEY_PRICE = "price";
        public static final String KEY_IMAGE = "image";
        public static final String KEY_EXPLAIN = "explain";

        public static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + " (" +
                _ID + " INTEGER PRIMARY KEY" + COMMA_SEP +
                KEY_PARENT + TEXT_TYPE + COMMA_SEP +
                KEY_IMAGE + TEXT_TYPE + COMMA_SEP +
                KEY_TITLE + TEXT_TYPE + COMMA_SEP +
                KEY_PRICE + TEXT_TYPE + COMMA_SEP +
                KEY_EXPLAIN + TEXT_TYPE +  " )";
        public static final String DELETE_TABLE = "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
