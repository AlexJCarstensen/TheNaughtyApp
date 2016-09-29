package com.alexcarstensen.weatherornotyoulikeit;


import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;


import com.alexcarstensen.weatherornotyoulikeit.helpers.WeatherService;

import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    private WeatherService weatherService;
    private weatherItem weather;
    private ServiceConnection weatherServiceConnection;
    // For debugging
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //Registering receiver
        IntentFilter weatherFilter = new IntentFilter();
        weatherFilter.addAction(WeatherService.BROADCAST_WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onWeatherUpdate, weatherFilter);

//        setupConnectionToWeatherService();
//        Intent bindIntent = new Intent(MainActivity.this, WeatherService.class);
//        bindService(bindIntent,weatherServiceConnection, Context.BIND_AUTO_CREATE);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, R.string.txtUpdateWeather, Snackbar.LENGTH_LONG)
                 //       .setAction("Action", null).show();

                //For debugging
                /*weatherItem wObj = new weatherItem("Weather #" + (i+1), "Date #" + (i+1),"Temp #" + (i+1),"Time #" + (i+1),0);
                FragmentManager fm = getSupportFragmentManager();
                content_history_weather_fragment fragment = (content_history_weather_fragment) fm.findFragmentById(R.id.fragment_history_weather);
                fragment.setWeatherObject(wObj);
                i++;
                */

                // For web debugging
//                if(weatherService != null) {
//                    weather = weatherService.GetNewWeather();
                    weather = new weatherItem("Cloudy", "20160930","13.1","12:45:12",0);

                    if(weather !=null) {
                        FragmentManager fragMan = getSupportFragmentManager();
                        content_weather_fragment fragment = (content_weather_fragment) fragMan.findFragmentById(R.id.fragment_weather);
                        fragment.setCurrentWeather(weather);
                        Toast.makeText(getApplicationContext(), R.string.txtUpdateWeather, Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        Toast.makeText(getApplicationContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
                    }
//                }
//                else
//                {
//                    Toast.makeText(getApplicationContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
//                }

            }
        });



    }

    private void setupConnectionToWeatherService()
    {
        weatherServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                weatherService = ((WeatherService.WeatherBinder)service).getService();
                Log.d("Connection", "Connection established");
            }

            @Override
            public void onServiceDisconnected(ComponentName name) {
                weatherService = null;
                Log.d("Connection", "Connection established");
            }
        };
    }

    private BroadcastReceiver onWeatherUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", WeatherService.LOG_LINE + "onWeatherUpdate() called");
            if(weatherService != null) {
                weather = weatherService.GetNewWeather();
            }

            Log.d("weather item", WeatherService.LOG_LINE + weather.getWeatherStatus());



            //Checking if it works when it receives the update
            //TODO: Jeppe check if this is right!
            FragmentManager fm = getSupportFragmentManager();
            content_history_weather_fragment fragment = (content_history_weather_fragment) fm.findFragmentById(R.id.fragment_history_weather);
            fragment.setWeatherObject(weather);
            i++;
        }
    };


//    protected void OnDestroy(){
//        super.onDestroy();
//        unbindService(weatherServiceConnection);
//
//    }
}
