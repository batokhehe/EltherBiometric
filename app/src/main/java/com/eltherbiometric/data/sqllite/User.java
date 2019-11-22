package com.eltherbiometric.data.sqllite;

public class User {
    public static final String TABLE_NAME = "user";
    public static final String COLUMN_NIK = "nik";
    public static final String COLUMN_NAME = "name";

    public static String createTable(){
        String[] fields = new String[]{
                TABLE_NAME,
                COLUMN_NIK,
                COLUMN_NAME,
        };
        return String.format("CREATE TABLE %s (%s TEXT PRIMARY KEY, " +
                        "%s TEXT NULL);",
                fields);
    }

    public static String dropTable(){
        return "DROP TABLE IF EXISTS " + TABLE_NAME;
    }
}
