package com.alexcarstensen.weatherornotyoulikeit;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


public class content_weather_fragment extends Fragment {

    final static String STATE_CURRENT_WEATHER = "CurrentWeather";
    final static String STATE_CURRENT_WEATHER_ICON = "CurrentWeatherIcon";
    View view;
    private weatherItem _weatherItem;
    private Resources resources = null;
    private int resNum;

    public content_weather_fragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {
        resources = getResources();
        view = inflater.inflate(R.layout.content_fragment_weather,
                container, false);

        if(savedInstanceState != null){


            _weatherItem = savedInstanceState.getParcelable(STATE_CURRENT_WEATHER);
            if(_weatherItem != null){


                TextView txtCurrentWeather = (TextView) view.findViewById(R.id.textViewCurrentWeatherStatus);
                txtCurrentWeather.setText(_weatherItem.getWeatherStatus());

                TextView txtTemp = (TextView) view.findViewById(R.id.textViewCurrentTemp);
                txtTemp.setText(_weatherItem.getTemperature().concat("°"));

                ImageView viewCurrentWeatherStatus = (ImageView) view.findViewById(R.id.imageViewCurrentWeatherStatus);
                viewCurrentWeatherStatus.setImageResource(resNum);
            }
        }

        return view;
    }


    public void setCurrentWeather(weatherItem weather){

        _weatherItem = weather;
        ImageView viewCurrentWeatherStatus = (ImageView) view.findViewById(R.id.imageViewCurrentWeatherStatus);

        //Trying to find the right icon to show
        String uri = "drawable/i" + _weatherItem.getIcon();
        resNum = resources.getIdentifier(uri, "drawable", MainActivity.PACKAGE_NAME);

        //Settting the image resource to the right icon
        viewCurrentWeatherStatus.setImageResource(resNum);


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
            outState.putInt(STATE_CURRENT_WEATHER_ICON, resNum);

        }
    }
}
