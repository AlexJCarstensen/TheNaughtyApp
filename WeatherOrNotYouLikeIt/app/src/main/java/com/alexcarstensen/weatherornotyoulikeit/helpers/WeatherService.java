package com.alexcarstensen.weatherornotyoulikeit.helpers;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
import java.util.Calendar;
import java.util.Date;



/**
 * Created by ajc on 22-09-2016.
 */

public class WeatherService extends Service {
    public static final String BROADCAST_WEATHER_UPDATE = "weather_update";

    public static final String LOG_LINE = "------------------------------------->> ";
    private CityWeatherDetails weatherDetails;
    private weatherItem weather;
    private final WeatherBinder binder = new WeatherBinder();
    private final String AARHUS_ID = "2624652";

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



        //TODO: Every half hour, update weather info, but first see if it works


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId)
    {
        Log.d("Service", LOG_LINE + "onStartCommand() called");

        WebServiceHelper helper = new WebServiceHelper();

        //debugging
        helper.GetWeather(AARHUS_ID, this);

        return START_NOT_STICKY;
    }

    private void WeatherUpdateReady()
    {
        Log.d("Service", LOG_LINE + "Update() called");

        // TODO: Broadcast so activity knows there is an update

        Intent broadcastIntent = new Intent();
        broadcastIntent.setAction(BROADCAST_WEATHER_UPDATE);

        LocalBroadcastManager.getInstance(this).sendBroadcast(broadcastIntent);
    }

    public weatherItem GetNewWeather()
    {
        return weather;
    }



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


        public void GetWeather(String cityId, Context context) {

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

                            weatherDetails = StringToWeatherDetails(txtResponse);

                            weather = WeatherDetailsToWeather(weatherDetails);

                            WeatherUpdateReady();

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
            String date = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
            Double celciusTemperature = TempeatureHelper.KelvinToCelcius(cityWeather.getMain().getTemp());

            Log.d("Temp", LOG_LINE + celciusTemperature.toString());

            //TODO: Why the f*** Is the temperature so high?!?

            String tempearture = String.format("%.1f" ,celciusTemperature);

            //String tempearture = celciusTemperature.toString();

            Log.d("tempString", LOG_LINE + tempearture);

            weatherItem tempItem = new weatherItem(weatherStatus, date, tempearture, time, 1,"dummy"); //TODO: What is response code?

            return tempItem;
        }


    }
}



