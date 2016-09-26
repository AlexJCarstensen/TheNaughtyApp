package com.alexcarstensen.weatherornotyoulikeit.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by ajc on 22-09-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " FLOAT";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TEMPERATURE + FLOAT_TYPE + COMMA_SEP +
                    FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + " )";

    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + FeedReaderContract.FeedEntry.TABLE_NAME;

    // If you change the database schema, you must increment the database version.
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "WeatherDB.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_ENTRIES);
        onCreate(db);
    }

    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        onUpgrade(db, oldVersion, newVersion);
    }

    public void addWeather(WeatherDAO weather){
        ContentValues values = new ContentValues();
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_DESCRIPTION, weather.getDescription());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TEMPERATURE, weather.getTemperature());
        values.put(FeedReaderContract.FeedEntry.COLUMN_NAME_TIMESTAMP, weather.getTimestamp().toString());
    }



}
