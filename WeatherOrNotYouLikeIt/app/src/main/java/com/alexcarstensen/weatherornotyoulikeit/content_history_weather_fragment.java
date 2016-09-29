package com.alexcarstensen.weatherornotyoulikeit;

import android.database.sqlite.SQLiteDatabase;
import android.os.Parcelable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.alexcarstensen.weatherornotyoulikeit.helpers.DatabaseHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

public class content_history_weather_fragment extends Fragment {

    final static String STATE_WEATHER_ARRAY = "WeatherArray";

    private listAdaptor listAdaptorObj;
    private ListView weatherHistoryListView;
    private ArrayList<weatherItem> weatherItemList = new ArrayList<weatherItem>();

    View view;



    public content_history_weather_fragment() {
    }


    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.content_fragment_weather_history,
                container, false);


//        DatabaseHelper databaseHelper = new DatabaseHelper(view.getContext());
//        SQLiteDatabase sb = databaseHelper.getWritableDatabase();
//        List<weatherItem> weatherItemList = databaseHelper.getWeatherList();
//
//        listAdaptorObj = new listAdaptor(view.getContext(), (ArrayList<weatherItem>) weatherItemList);
//        weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
//        weatherHistoryListView.setAdapter(listAdaptorObj);

        if(savedInstanceState != null){

            weatherItemList = savedInstanceState.getParcelableArrayList(STATE_WEATHER_ARRAY);
            listAdaptorObj = new listAdaptor(view.getContext(), weatherItemList);
            weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
            weatherHistoryListView.setAdapter(listAdaptorObj);

        }
        /*
        for(int i = 0; i < 5; i++){
            weatherItemList.add(new weatherItem("Weather #" + (i+1), "Date #" + (i+1),"Temp #" + (i+1),"Time #" + (i+1),0));
        }

        listAdaptorObj = new listAdaptor(view.getContext(), weatherItemList);
        weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
        weatherHistoryListView.setAdapter(listAdaptorObj);*/

        return view;

    }

    public void setWeatherList(ArrayList<weatherItem> weatherItemList_){
        weatherItemList = weatherItemList_;
        listAdaptorObj = new listAdaptor(view.getContext(), weatherItemList);
        weatherHistoryListView = (ListView)view.findViewById(R.id.listViewWeatherHistory);
        weatherHistoryListView.setAdapter(listAdaptorObj);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_WEATHER_ARRAY, weatherItemList);

    }


}
