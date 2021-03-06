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
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;


import com.alexcarstensen.weatherornotyoulikeit.helpers.WeatherService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;


public class MainActivity extends AppCompatActivity {

    //For fragment use
    public static String PACKAGE_NAME;


    //Alarm
    private AlarmManager alarmMng;
    private PendingIntent alarmIntent;
    private final int ALARM_UNIQUE_ID = 0;


    private WeatherService weatherService;
    private weatherItem weather;
    private ArrayList<weatherItem> weatherList;
    private ServiceConnection weatherServiceConnection;
    private FragmentManager _fm;
    private content_history_weather_fragment _fragmentHistoryWeather;
    private content_weather_fragment _fragmentWeather;


    private boolean isBound = false;

    // For debugging
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        PACKAGE_NAME = getApplicationContext().getPackageName();

        _fm = getSupportFragmentManager();
        _fragmentHistoryWeather = (content_history_weather_fragment) _fm.findFragmentById(R.id.fragment_history_weather);
        _fragmentWeather = (content_weather_fragment) _fm.findFragmentById(R.id.fragment_weather);


        //Registering receiver
        IntentFilter weatherFilter = new IntentFilter();
        weatherFilter.addAction(WeatherService.BROADCAST_FRESH_WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onWeatherUpdate, weatherFilter);

        IntentFilter weatherListFilter = new IntentFilter();
        weatherListFilter.addAction(WeatherService.BROADCAST_WEATHERLIST_UPDATE);
        LocalBroadcastManager.getInstance(MainActivity.this).registerReceiver(onListWeatherUpdate, weatherListFilter);


        setupConnectionToWeatherService();
        Intent bindIntent = new Intent(MainActivity.this, WeatherService.class);
        isBound = getApplicationContext().bindService(bindIntent, weatherServiceConnection, Context.BIND_AUTO_CREATE);

//        DatabaseHelper databaseHelper = new DatabaseHelper(this);
//        SQLiteDatabase sb = databaseHelper.getWritableDatabase();
//        weatherItem wd0 = new weatherItem("RAIN", "2016-09-28", "13.4", "23:31", 23,"er");
////
//        weatherItem wd1 = new weatherItem("SKY", "2016-09-25", "13.4", "11:31", 24,"er");
////
//        weatherItem wd2 = new weatherItem("SUN", "2016-09-29", "13.4", "12:31", 26,"er");
////
//        weatherItem wd3 = new weatherItem("MOON", "2016-09-29", "13.4", "13:31", 27,"er");
////
//        databaseHelper.addWeather(wd0);
//        databaseHelper.addWeather(wd1);
//        databaseHelper.addWeather(wd2);
//        databaseHelper.addWeather(wd3);


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
                weatherService.GetFreshWeather();
//                    weather = new weatherItem("Cloudy", "20160930","13.1","12:45:12",0,"dummyPath");

//                    if(weather !=null) {
//                        FragmentManager fragMan = getSupportFragmentManager();
//                        content_weather_fragment fragmentCurrentWeather = (content_weather_fragment) fragMan.findFragmentById(R.id.fragment_weather);
//                        fragmentCurrentWeather.setCurrentWeather(weather);
//                        Toast.makeText(getApplicationContext(), R.string.txtUpdateWeather, Toast.LENGTH_SHORT).show();
//                    }
//                    else
//                    {
//                        Toast.makeText(getApplicationContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
//                    }
////                }
////                else
////                {
////                    Toast.makeText(getApplicationContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
////                }

            }
        });


        //Setting up Alarm to make the service run every 30 min.
        //Checking if alarm is sat
        boolean alarmUp = (PendingIntent.getBroadcast(this, 0,
                new Intent(MainActivity.this, AlarmReceiver.class).setAction("myaction"),
                PendingIntent.FLAG_NO_CREATE) != null);

        if (!alarmUp) {

            //Setting alarm if not sat
            alarmMng = (AlarmManager) MainActivity.this.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
            intent.setAction("myaction");
            alarmIntent = PendingIntent.getBroadcast(MainActivity.this, 0, intent, 0);
            alarmMng.setInexactRepeating(AlarmManager.ELAPSED_REALTIME_WAKEUP, AlarmManager.INTERVAL_HALF_HOUR, AlarmManager.INTERVAL_HALF_HOUR, alarmIntent);
        }


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

    private BroadcastReceiver onWeatherUpdate = new BroadcastReceiver() {

        private boolean isUpdatingFreshWeather = false;

        @Override
        public void onReceive(Context context, Intent intent) {


            Log.d("MainActivity", WeatherService.LOG_LINE + "onWeatherUpdate() called");

            if(!isUpdatingFreshWeather) {

                isUpdatingFreshWeather = true;

                if (weatherService != null) {


                    weather = weatherService.GetNewWeather();
                    //Checking if it works when it receives the update

                    if (weather != null) {


                        _fragmentWeather.setCurrentWeather(weather);
                        Toast.makeText(getApplicationContext(), R.string.txtUpdateWeather, Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(getApplicationContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("weather item", WeatherService.LOG_LINE + weather.getWeatherStatus());

                isUpdatingFreshWeather = false;
            }

        }
    };

    private BroadcastReceiver onListWeatherUpdate = new BroadcastReceiver() {

        private boolean isUpdatingHistory = false;

        @Override
        public void onReceive(Context context, Intent intent) {

            Log.d("MainActivity", WeatherService.LOG_LINE + "onListWeatherUpdate() called");

            if(!isUpdatingHistory) {
                isUpdatingHistory = true;
                if (weatherService != null) {
                    weatherList = weatherService.GetNewWeatherList();


                    //Making hack
                    ArrayList<weatherItem> tempList = new ArrayList<>();
                    ArrayList<String> dateList = new ArrayList<>();
                    ArrayList<String> timeList = new ArrayList<>();


                    //if both the date and the time is in the list, don't add it
                    if (weatherList != null) {



                    for (weatherItem item : weatherList) {

                        if (!(dateList.contains(item.getDate()) && timeList.contains(item.getTime()))) {
                            tempList.add(item);

                            dateList.add(item.getDate());
                            timeList.add(item.getTime());
                        }

                    }

                        _fragmentHistoryWeather.setWeatherList(tempList);

                        isUpdatingHistory = false;

                    }
                }
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
