package com.alexcarstensen.thebrandapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jeppe on 03-10-2016.
 */

public class MapWeatherFragment extends Fragment {


    View view;

    public MapWeatherFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.weather_fragment_map,
                container, false);


        return view;
    }


}




