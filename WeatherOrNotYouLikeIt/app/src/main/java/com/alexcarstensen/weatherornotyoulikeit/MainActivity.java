package com.alexcarstensen.weatherornotyoulikeit;


import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
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

    //Alarm
    private AlarmManager alarmMng;
    private PendingIntent alarmIntent;


    private WeatherService weatherService;
    private weatherItem weather;
    private weatherItem listWeather;
    private ServiceConnection weatherServiceConnection;
    private FragmentManager _fm;
    private content_history_weather_fragment _fragment;

    private boolean isBound = false;
    // For debugging
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fm = getSupportFragmentManager();
        _fragment = (content_history_weather_fragment) _fm.findFragmentById(R.id.fragment_history_weather);

        //Registering receiver
        IntentFilter weatherFilter = new IntentFilter();
        weatherFilter.addAction(WeatherService.BROADCAST_FRESH_WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onFreshWeatherUpdate, weatherFilter);

        setupConnectionToWeatherService();
        Intent bindIntent = new Intent(MainActivity.this, WeatherService.class);
        isBound =getApplicationContext().bindService(bindIntent,weatherServiceConnection, Context.BIND_AUTO_CREATE);

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
                        content_weather_fragment fragmentCurrentWeather = (content_weather_fragment) fragMan.findFragmentById(R.id.fragment_weather);
                        fragmentCurrentWeather.setCurrentWeather(weather);
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




        //Setting up Alarm to make the service run every 30 min.
        alarmMng = (AlarmManager)MainActivity.this.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
        alarmMng.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        alarmMng.setRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, SystemClock.elapsedRealtime() + 1000, SystemClock.elapsedRealtime() + 1000, alarmIntent);

    }


    @Override
    public void onDestroy(){
        super.onDestroy();
        if(isBound)
            getApplicationContext().unbindService(weatherServiceConnection);
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

    private BroadcastReceiver onFreshWeatherUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", WeatherService.LOG_LINE + "onFreshnWeatherUpdate() called");
            if(weatherService != null) {
                weather = weatherService.GetNewWeather();
                //Checking if it works when it receives the update

                if(weather != null) {

                    _fragment.setWeatherObject(weather);
                }
            }
            Log.d("weather item", WeatherService.LOG_LINE + weather.getWeatherStatus());

        }
    };

    private BroadcastReceiver onListWeatherUpdate = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("MainActivity", WeatherService.LOG_LINE + "onFreshWeatherUpdate() called");

            if(weatherService != null)
            {
                listWeather = weatherService.GetNewListWeather();


                //TODO: Update the list!
            }
        }
    };

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        unbindService(weatherServiceConnection);
//
//    }


}
