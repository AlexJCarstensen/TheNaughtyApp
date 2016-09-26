package com.alexcarstensen.weatherornotyoulikeit.helpers;

/**
 * Created by ajc on 22-09-2016.
 */

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;
import android.widget.Toast;

import com.alexcarstensen.weatherornotyoulikeit.MainActivity;
import com.alexcarstensen.weatherornotyoulikeit.weatherItem;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpStack;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class WebServiceHelper {

    private static final String WEATHER_API_KEY = "ff9b1bee11bd3e40dcd579e8390ae8de";

    private static final String WEATHER_API_CALL_HEAD = "http://api.openweathermap.org/data/2.5/forecast/city?id=";

    private static final String WEATHER_API_CALL_TAIL = "&APPID=" + WEATHER_API_KEY;

    //Volley queue
    RequestQueue queue;
    String txtResponse = "";


public void GetWeather(String cityId, Context context)
{

    if(queue == null)
    {
        queue = Volley.newRequestQueue(context);
    }

    String url = WEATHER_API_CALL_HEAD + cityId + WEATHER_API_CALL_TAIL;

    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {

                    txtResponse = response;


                }
            }, new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            txtResponse = "failure";
        }
    });



    //TODO: Check if phone has permission to internet, else ask for permission

    //TODO: Check for internet connection, in activity


}

    private weatherItem StringToWeatherItem(String stringWeather)
    {
        return null;

    }



}
