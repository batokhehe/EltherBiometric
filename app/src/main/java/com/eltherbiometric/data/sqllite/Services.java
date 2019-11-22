package com.eltherbiometric.data.sqllite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Services extends DBHandler {
    public Services(Context context) {
        super(context);
    }

    public void Save(String nik, String name) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        try {
            contentValues.put(User.COLUMN_NIK, nik);
            contentValues.put(User.COLUMN_NAME, name);
            db.insert(User.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public void Presence(String nik, String method) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        DateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        String date_ = dateFormat.format(date);
        String time_ = timeFormat.format(date);

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        db.beginTransaction();
        try {
            contentValues.put(Presence.COLUMN_NIK, nik);
            contentValues.put(Presence.COLUMN_METHOD, method);
            contentValues.put(Presence.COLUMN_DATE, date_);
            contentValues.put(Presence.COLUMN_TIME, time_);
            db.insert(Presence.TABLE_NAME, null, contentValues);
            db.setTransactionSuccessful();
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            db.endTransaction();
            db.close();
        }
    }

    public List<com.eltherbiometric.data.model.Presence> AllPresence() {
        List<com.eltherbiometric.data.model.Presence> Datas = new ArrayList<>();

        // Define a projection that specifies which columns from the database
        // you will actually use after this query.
        String[] projection = {
                Presence.COLUMN_NIK,
                Presence.COLUMN_DATE,
                Presence.COLUMN_TIME,
                Presence.COLUMN_METHOD
        };

        String sortOrder = null;

        SQLiteDatabase db = this.getWritableDatabase();

        Cursor cursor = db.query(
                Presence.TABLE_NAME,   // The table to query
                projection,             // The array of columns to return (pass null to get All)
                null,              // The columns for the WHERE clause
                null,          // The values for the WHERE clause
                null,                   // don't group the rows
                null,                   // don't filter by row groups
                sortOrder               // The sort order
        );

        // looping through All rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                com.eltherbiometric.data.model.Presence presence = new com.eltherbiometric.data.model.Presence();
                presence.setNik(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_NIK)));
                presence.setDate(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_DATE)));
                presence.setTime(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_TIME)));
                presence.setMethod(cursor.getString(cursor.getColumnIndex(Presence.COLUMN_METHOD)));

                Datas.add(presence);
            } while (cursor.moveToNext());
        }
        cursor.close();
        // close db connection
        db.close();
        // return settings list
        return Datas;
    }
}
