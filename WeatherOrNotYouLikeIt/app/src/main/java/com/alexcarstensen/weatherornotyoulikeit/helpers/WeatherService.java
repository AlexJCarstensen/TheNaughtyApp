package com.alexcarstensen.weatherornotyoulikeit.helpers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.alexcarstensen.weatherornotyoulikeit.MainActivity;
import com.alexcarstensen.weatherornotyoulikeit.model.CityWeatherDetails;
import com.alexcarstensen.weatherornotyoulikeit.weatherItem;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.StringReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;



/**
 * Created by ajc on 22-09-2016.
 */

public class WeatherService extends Service {
    public static final String BROADCAST_WEATHERLIST_UPDATE = "weather_list_update";
    public static final String BROADCAST_FRESH_WEATHER_UPDATE = "fresh_weather_update";

    public static final String LOG_LINE = "------------------------------------->> ";
    private CityWeatherDetails freshWeatherDetails;
    private CityWeatherDetails weatherListDetails;
    private weatherItem freshWeather;
    private ArrayList<weatherItem> weatherList;
    private final WeatherBinder binder = new WeatherBinder();
    private final String AARHUS_ID = "2624652";


    private DatabaseHelper dbHelper = new DatabaseHelper(this);


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        Log.d("Bound Service", LOG_LINE + "onBind() called");

        return binder;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        Log.d("Service", LOG_LINE + "onCreate() called");
       GetWeatherDataForActivity();





    }

    private void GetWeatherDataForActivity()
    {
        GetNewestListFromDb();
        GetFreshWeather();
    }

    public void GetFreshWeather()
    {
        //TODO: GetFreshWeather from web helper
        WebServiceHelper helper = new WebServiceHelper();

        helper.GetFreshWeather(AARHUS_ID, this);
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Service", LOG_LINE + "onStartCommand() called");

        WebServiceHelper helper = new WebServiceHelper();

        helper.GetWeatherForDb(AARHUS_ID, this);

        return START_NOT_STICKY;
    }

    public void GetNewestListFromDb()
    {


        AsyncTask<Object, String, String> task = new AsyncTask<Object, String, String>() {
            @Override
            protected String doInBackground(Object[] params) {


                weatherList = dbHelper.getWeatherList();

                String s = "something";
                return s;
            }


            @Override
            protected void onPostExecute(String stringResult) {
                super.onPostExecute(stringResult);

                Log.d("Service", LOG_LINE + "onPostExecute() called");

                ListWeatherUpdateReady();

            }
        };

        task.execute();


    }

    private void FreshWeatherUpdateReady()
    {
        Log.d("Service", LOG_LINE + "FreshUpdate() called");

        // TODO: Broadcast so activity knows there is an update

        Intent broadcastFreshIntent = new Intent();
        broadcastFreshIntent.setAction(BROADCAST_FRESH_WEATHER_UPDATE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastFreshIntent);
    }

    private void ListWeatherUpdateReady()
    {
        Log.d("Service", LOG_LINE + "ListUpdate() called");

        Intent broadcastListIntent = new Intent();
        broadcastListIntent.setAction(BROADCAST_WEATHERLIST_UPDATE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastListIntent);
    }

    public weatherItem GetNewWeather()
    {
        return freshWeather;
    }
    public ArrayList<weatherItem> GetNewWeatherList() {return weatherList;}



    public class WeatherBinder extends Binder
    {
        public WeatherService getService()
        {Log.d("Binder", LOG_LINE + "getService() Called");

            return WeatherService.this;
        }
    }

    class WebServiceHelper {

        private static final String WEATHER_API_KEY = "ff9b1bee11bd3e40dcd579e8390ae8de";

        private static final String WEATHER_API_CALL_HEAD = "http://api.openweathermap.org/data/2.5/weather?id=";

        private static final String WEATHER_API_CALL_TAIL = "&APPID=" + WEATHER_API_KEY;

        //Volley queue
        RequestQueue queue;
        String txtResponse = "";


        public void GetFreshWeather(String cityId, Context context) {

            Log.d("Weather Helper#1", LOG_LINE + "GetWeather() called");

            if (queue == null) {
                queue = Volley.newRequestQueue(context);
            }

            Log.d("Weather Helper#2", LOG_LINE + "Queue created");

            String url = WEATHER_API_CALL_HEAD + cityId + WEATHER_API_CALL_TAIL;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("Weather Helper#3", LOG_LINE + "Response: " + response);

                            txtResponse = response;

                            try
                            {
                                freshWeatherDetails = StringToWeatherDetails(txtResponse);

                                freshWeather = WeatherDetailsToWeather(freshWeatherDetails);

                                FreshWeatherUpdateReady();

                            }
                            catch (Exception e)
                            {
                                Log.d("Service", LOG_LINE + "Error in response");
                                queue = null;
                            }


                            queue = null;

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    txtResponse = error.getMessage();
                    Log.d("Weather Helper#4", LOG_LINE + txtResponse);
                    queue = null;
                }
            });

            queue.add(stringRequest);





        }

        public void GetWeatherForDb(String cityId, final Context context)
        {

            Log.d("Weather Helper#1", LOG_LINE + "GetWeather() called");

            if (queue == null) {
                queue = Volley.newRequestQueue(context);
            }

            Log.d("Weather Helper#2", LOG_LINE + "Queue created");

            String url = WEATHER_API_CALL_HEAD + cityId + WEATHER_API_CALL_TAIL;

            StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            Log.d("Weather Helper#10", LOG_LINE + "Response: " + response);

                            txtResponse = response;

                            try
                            {
                                weatherListDetails = StringToWeatherDetails(txtResponse);

                                weatherItem tempItem = WeatherDetailsToWeather(weatherListDetails);

                                SQLiteDatabase sb = dbHelper.getWritableDatabase();

                                dbHelper.addWeather(tempItem);

                                weatherList = dbHelper.getWeatherList();

                                ListWeatherUpdateReady();
                                Log.d("Weather Helper#11", LOG_LINE + "Database written to: " + response);
                            }
                            catch(Exception e)
                            {
                                Log.d("Service", LOG_LINE + "Error in response");
                                queue = null;
                            }


                            queue = null;

                        }
                    }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {


                    txtResponse = error.getMessage();
                    Log.d("Weather Helper#4", LOG_LINE + txtResponse);
                    queue = null;
                }
            });

            queue.add(stringRequest);


        }

        private CityWeatherDetails StringToWeatherDetails(String stringWeather) {
            Log.d("Weather Helper#5", LOG_LINE + "StringToWeather() called");
            Gson gsonBuilder = new Gson();
            JsonReader reader = new JsonReader(new StringReader(stringWeather));
            reader.setLenient(true);
            CityWeatherDetails tempWeather = gsonBuilder.fromJson(reader, CityWeatherDetails.class);

            if(tempWeather.getWeather()!= null && tempWeather.getWeather()[0] != null) {
                Log.d("Weather item", LOG_LINE + "Weather status: " + tempWeather.getWeather()[0].getDescription());
            }
            return tempWeather;

        }

        private weatherItem WeatherDetailsToWeather(CityWeatherDetails cityWeather)
        {


            String weatherStatus = cityWeather.getWeather()[0].getDescription();
            String time = new SimpleDateFormat("HH:mm").format(Calendar.getInstance().getTime());
            String date = new SimpleDateFormat("yyyy-MM-dd").format(Calendar.getInstance().getTime());
            Double celciusTemperature = TempeatureHelper.KelvinToCelcius(cityWeather.getMain().getTemp());
            String icon = cityWeather.getWeather()[0].getIcon();

            Log.d("Temp", LOG_LINE + celciusTemperature.toString());

            //TODO: Why the f*** Is the temperature so high?!?

            String tempearture = String.format("%.1f" ,celciusTemperature);

            //String tempearture = celciusTemperature.toString();

            Log.d("tempString", LOG_LINE + tempearture);

            weatherItem tempItem = new weatherItem(weatherStatus, date, tempearture, time, 1, icon); //TODO: What is response code?

            return tempItem;
        }




    }
}



