package com.alexcarstensen.weatherornotyoulikeit.helpers;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.alexcarstensen.weatherornotyoulikeit.model.Weather;

import java.security.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.security.Timestamp;


/**
 * Created by ajc on 22-09-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static FeedReaderContract.FeedEntry FeedEntry;

    private static final String TEXT_TYPE = " TEXT";
    private static final String FLOAT_TYPE = " FLOAT";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_NAME_ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_NAME_DESCRIPTION + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TEMPERATURE + FLOAT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_NAME_TIMESTAMP + TEXT_TYPE + " )";

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
        Log.d("Database","Database created");
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
        values.put(FeedEntry.COLUMN_NAME_DESCRIPTION, weather.getDescription());
        values.put(FeedEntry.COLUMN_NAME_TEMPERATURE, weather.getTemperature());
        values.put(FeedEntry.COLUMN_NAME_TIMESTAMP, weather.getTimestamp());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(FeedEntry.TABLE_NAME, null, values);
        db.close();
    }

    public WeatherDAO findWeather(String WeatherDescription){
        String query = "SELECT * FROM " + FeedEntry.TABLE_NAME +
                        " WHERE " + FeedEntry.COLUMN_NAME_DESCRIPTION +
                        " =  \"" + WeatherDescription + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        WeatherDAO weather = new WeatherDAO();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            weather.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_ID))));
            weather.setDescription(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_DESCRIPTION)));
            weather.setTemperature(Float.parseFloat(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_TEMPERATURE))));
            weather.setTimestamp(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_NAME_TIMESTAMP)));
            cursor.close();
        } else {
            weather = null;
        }
        db.close();
        return weather;
    }

}
