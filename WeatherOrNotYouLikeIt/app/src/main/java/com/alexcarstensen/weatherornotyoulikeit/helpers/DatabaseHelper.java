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
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import com.alexcarstensen.weatherornotyoulikeit.weatherItem;

/**
 * Created by ajc on 22-09-2016.
 */

public class DatabaseHelper extends SQLiteOpenHelper{

    private static FeedReaderContract.FeedEntry FeedEntry;

    private static final String TEXT_TYPE = " TEXT";
    private static final String INTERGER_TYPE = " INTERGER";

    private static final String COMMA_SEP = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + FeedReaderContract.FeedEntry.TABLE_NAME + " (" +
                    FeedEntry.COLUMN_ID + " INTEGER PRIMARY KEY," +
                    FeedEntry.COLUMN_WEATHERSTATUS + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_DATE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_TEMPERATURE + TEXT_TYPE + COMMA_SEP +
                    FeedEntry.COLUMN_RESULTCODE + INTERGER_TYPE + " )";

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

    public void addWeather(weatherItem _weatherItem){
        ContentValues values = new ContentValues();
        values.put(FeedEntry.COLUMN_WEATHERSTATUS, _weatherItem.getWeatherStatus());
        values.put(FeedEntry.COLUMN_DATE, _weatherItem.getDate()+" "+_weatherItem.getTime());
        values.put(FeedEntry.COLUMN_TEMPERATURE, _weatherItem.getTemperature());
        values.put(FeedEntry.COLUMN_RESULTCODE, _weatherItem.getResultCode());

        SQLiteDatabase db = this.getWritableDatabase();

        db.insert(FeedEntry.TABLE_NAME, null, values);
        db.close();
    }

    public static int getDatabaseVersion() {
        return DATABASE_VERSION;
    }


    public weatherItem getWeather(String WeatherStatus){
        String query = "SELECT * FROM " + FeedEntry.TABLE_NAME +
                        " WHERE " + FeedEntry.COLUMN_WEATHERSTATUS +
                        " =  \"" + WeatherStatus + "\"";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        weatherItem weather = new weatherItem();

        if (cursor.moveToFirst()) {
            cursor.moveToFirst();

            weather.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_ID))));
            weather.setWeatherStatus(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_WEATHERSTATUS)));
            weather.setDate(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_DATE)));
            weather.setTemperature(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_TEMPERATURE)));
            weather.setResultCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_RESULTCODE))));

            cursor.close();
        } else {
            weather = null;
        }
        db.close();
        return weather;
    }

    public List<weatherItem> getWeatherList(){

        Calendar calendar = Calendar.getInstance();

        String query = "SELECT * FROM " + FeedEntry.TABLE_NAME +
                        " WHERE " + FeedEntry.COLUMN_DATE + " > datetime('now','-1day')"+
                        " ORDER BY date("+ FeedEntry.COLUMN_DATE +")";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        List<weatherItem> WeatherItemList = new ArrayList<weatherItem>();

        if (cursor.moveToFirst()) {
            while (cursor.isAfterLast() == false) {
                weatherItem weather = new weatherItem();
                String datetime = cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_DATE));
                String[] times = datetime.split(" ", 2);

                weather.setID(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_ID))));
                weather.setWeatherStatus(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_WEATHERSTATUS)));
                weather.setDate(times[0]);
                weather.setTime(times[1]);
                weather.setTemperature(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_TEMPERATURE)));
                weather.setResultCode(Integer.parseInt(cursor.getString(cursor.getColumnIndex(FeedEntry.COLUMN_RESULTCODE))));

                WeatherItemList.add(weather);
                cursor.moveToNext();
            }
            cursor.close();
        }else {
            WeatherItemList = null;
        }
        db.close();
        return WeatherItemList;
    }

}
