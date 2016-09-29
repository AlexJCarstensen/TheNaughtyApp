package com.alexcarstensen.weatherornotyoulikeit;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class content_city_fragment extends Fragment {

    private final String STATE_CITY = "city";
    private String _cityName;
    View view;
    TextView txtViewCity;


    public content_city_fragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.content_fragment_city,
                container, false);
        txtViewCity = (TextView) view.findViewById(R.id.textViewCityName);
        _cityName = getResources().getString(R.string.txtCity);
        txtViewCity.setText(_cityName);

        if(savedInstanceState != null){
            txtViewCity.setText(savedInstanceState.getString(STATE_CITY));
        }


        return view;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putString(STATE_CITY, _cityName);

    }

}
