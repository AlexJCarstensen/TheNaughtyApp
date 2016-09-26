package com.alexcarstensen.weatherornotyoulikeit;

import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import java.util.ArrayList;

public class content_history_weather_fragment extends Fragment {

    private listAdaptor listAdaptorObj;
    private ListView weatherHistoryListView;
    private final ArrayList<weatherItem> weatherItemList = new ArrayList<weatherItem>();

    View view;

    public content_history_weather_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_fragment_weather_history,
                container, false);

        /*
        for(int i = 0; i < 5; i++){
            weatherItemList.add(new weatherItem("Weather #" + (i+1), "Date #" + (i+1),"Temp #" + (i+1),"Time #" + (i+1),0));
        }

        listAdaptorObj = new listAdaptor(view.getContext(), weatherItemList);
        weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
        weatherHistoryListView.setAdapter(listAdaptorObj);*/

        return view;

    }

    public void setWeatherObject(weatherItem weather){
        weatherItemList.add(0,weather);
        listAdaptorObj = new listAdaptor(view.getContext(), weatherItemList);
        weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
        weatherHistoryListView.setAdapter(listAdaptorObj);
    }

}
