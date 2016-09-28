package com.alexcarstensen.weatherornotyoulikeit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jeppe on 23-09-2016.
 */


public class listAdaptor extends BaseAdapter {

    Context context;
    ArrayList<weatherItem> weatherItems;
    weatherItem weatherItemObj;

    public listAdaptor(Context c, ArrayList<weatherItem> weatherItemList){
        this.context = c;
        this.weatherItems = weatherItemList;
    }

    @Override
    public int getCount() {
        if(weatherItems!=null) {
            return weatherItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(weatherItems!=null) {
            return weatherItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            LayoutInflater demoInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = demoInflator.inflate(R.layout.list_item_weather, null);
        }

        weatherItemObj = weatherItems.get(position);
        if(weatherItemObj!=null){
            TextView txtWeather = (TextView) convertView.findViewById(R.id.textViewWeatherStatus);
            txtWeather.setText(weatherItemObj.getWeatherStatus());

            TextView txtDate = (TextView) convertView.findViewById(R.id.textViewDate);
            txtDate.setText(weatherItemObj.getDate());

            TextView txtTemp = (TextView) convertView.findViewById(R.id.textViewTemperature);
            txtTemp.setText(weatherItemObj.getTemperature());

            TextView txtTime = (TextView) convertView.findViewById(R.id.textViewUpdateTime);
            txtTime.setText(weatherItemObj.getTime());


        }
        return convertView;
    }
}