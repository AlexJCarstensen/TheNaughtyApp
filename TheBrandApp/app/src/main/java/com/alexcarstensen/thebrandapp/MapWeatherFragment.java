package com.alexcarstensen.thebrandapp;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.IBinder;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.alexcarstensen.thebrandapp.Helpers.WeatherService;

/**
 * Created by jeppe on 03-10-2016.
 */

public class MapWeatherFragment extends Fragment {


    final static String STATE_CURRENT_WEATHER = "CurrentWeather";
    final static String STATE_CURRENT_WEATHER_ICON = "CurrentWeatherIcon";
    View view;
    private WeatherItem _weatherItem;
    private Resources resources = null;
    private int resNum;
    private WeatherService weatherService;
    private ServiceConnection weatherServiceConnection;
    private WeatherItem weather;
    private boolean isBound = false;



    public static String PACKAGE_NAME;

    public MapWeatherFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        resources = getResources();
        view = inflater.inflate(R.layout.weather_fragment_map,
                container, false);

        if(savedInstanceState != null){


            _weatherItem = savedInstanceState.getParcelable(STATE_CURRENT_WEATHER);
            if(_weatherItem != null){


                TextView txtCurrentWeather = (TextView) view.findViewById(R.id.textViewCurrentWeatherStatus);
                txtCurrentWeather.setText(_weatherItem.getWeatherStatus());

                TextView txtTemp = (TextView) view.findViewById(R.id.textViewCurrentTemp);
                txtTemp.setText(_weatherItem.getTemperature().concat("°"));
                resNum = savedInstanceState.getInt(STATE_CURRENT_WEATHER_ICON);

                ImageView viewCurrentWeatherStatus = (ImageView) view.findViewById(R.id.imageViewCurrentWeatherStatus);
                viewCurrentWeatherStatus.setImageResource(resNum);

            }
        }
        //Registering receiver
        IntentFilter weatherFilter = new IntentFilter();
        weatherFilter.addAction(WeatherService.BROADCAST_FRESH_WEATHER_UPDATE);
        LocalBroadcastManager.getInstance(getContext()).registerReceiver(onWeatherUpdate, weatherFilter);

        setupConnectionToWeatherService();
        Intent bindIntent = new Intent(getContext(), WeatherService.class);
        isBound = getContext().bindService(bindIntent, weatherServiceConnection, Context.BIND_AUTO_CREATE);

        return view;
    }


    public void setCurrentWeather(WeatherItem weather){

        _weatherItem = weather;
        ImageView viewCurrentWeatherStatus = (ImageView) view.findViewById(R.id.imageViewCurrentWeatherStatus);

        //Trying to find the right icon to show
        String uri = "drawable/i" + _weatherItem.getIcon();
        resNum = resources.getIdentifier(uri, "drawable", PACKAGE_NAME);

        //Settting the image resource to the right icon
        viewCurrentWeatherStatus.setImageResource(resNum);


        TextView txtCurrentWeather = (TextView) view.findViewById(R.id.textViewCurrentWeatherStatus);
        txtCurrentWeather.setText(_weatherItem.getWeatherStatus());

        TextView txtTemp = (TextView) view.findViewById(R.id.textViewCurrentTemp);
        txtTemp.setText(_weatherItem.getTemperature().concat("°"));

    }
    private void setupConnectionToWeatherService()
    {
        weatherServiceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName name, IBinder service) {
                weatherService = ((WeatherService.WeatherBinder)service).getService();
                weatherService.GetFreshWeather();
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


                        setCurrentWeather(weather);
                        //Toast.makeText(getContext(), R.string.txtUpdateWeather, Toast.LENGTH_SHORT).show();
                    } else {
                        //Toast.makeText(getContext(), R.string.txtUnableToUpdateWeather, Toast.LENGTH_SHORT).show();
                    }
                }
                Log.d("weather item", WeatherService.LOG_LINE + weather.getWeatherStatus());

                isUpdatingFreshWeather = false;
            }

        }
    };

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(_weatherItem != null) {
            outState.putParcelable(STATE_CURRENT_WEATHER, _weatherItem);
            outState.putInt(STATE_CURRENT_WEATHER_ICON, resNum);

        }
    }


}




