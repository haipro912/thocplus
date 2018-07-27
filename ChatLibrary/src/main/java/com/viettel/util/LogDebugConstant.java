package com.viettel.util;

/**
 * Created by thanhnt72 on 4/4/2018.
 */

public class LogDebugConstant {
    public static final String TABLE = "LOG_DEBUG";
    public static final String ID = "id";
    public static final String TIMESTAMP = "timestamp";
    public static final String CONTENT = "content";

    public static final String CREATE_STATEMENT = "CREATE TABLE IF NOT EXISTS " + TABLE +
            " (" + ID + " INTEGER PRIMARY KEY, " +
            TIMESTAMP + " TEXT, " +
            CONTENT + " TEXT)";
    public static final String DROP_STATEMENT = "DROP TABLE IF EXISTS " + TABLE;
    public static final String SELECT_ALL_STATEMENT = "SELECT * FROM " + TABLE;
    public static final String DELETE_ALL_STATEMENT = "DELETE FROM " + TABLE;
}
