package com.alexcarstensen.weatherornotyoulikeit;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

public class content_weather_fragment extends Fragment {

    final static String STATE_CURRENT_WEATHER = "CurrentWeather";
    View view;
    private weatherItem _weatherItem;

    public content_weather_fragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_fragment_weather,
                container, false);

        if(savedInstanceState != null){


            _weatherItem = savedInstanceState.getParcelable(STATE_CURRENT_WEATHER);
            if(_weatherItem != null){


                TextView txtCurrentWeather = (TextView) view.findViewById(R.id.textViewCurrentWeatherStatus);
                txtCurrentWeather.setText(_weatherItem.getWeatherStatus());

                TextView txtTemp = (TextView) view.findViewById(R.id.textViewCurrentTemp);
                txtTemp.setText(_weatherItem.getTemperature().concat("°"));
            }
        }

        return view;
    }


    public void setCurrentWeather(weatherItem weather){

        _weatherItem = weather;
        //ImageView viewCurrentWeatherStatus = (ImageView) view.findViewById(R.id.imageViewCurrentWeatherStatus);
        //viewCurrentWeatherStatus.setImageDrawable(SOMETHING);

        TextView txtCurrentWeather = (TextView) view.findViewById(R.id.textViewCurrentWeatherStatus);
        txtCurrentWeather.setText(_weatherItem.getWeatherStatus());

        TextView txtTemp = (TextView) view.findViewById(R.id.textViewCurrentTemp);
        txtTemp.setText(_weatherItem.getTemperature().concat("°"));

    }



    @Override
        public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(_weatherItem != null) {
            outState.putParcelable(STATE_CURRENT_WEATHER, _weatherItem);
        }
    }
}
