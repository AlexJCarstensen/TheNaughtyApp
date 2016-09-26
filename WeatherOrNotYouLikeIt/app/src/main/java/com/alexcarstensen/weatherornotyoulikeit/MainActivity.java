package com.alexcarstensen.weatherornotyoulikeit;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    /*
    private listAdaptor listAdaptorObj;
    private ListView weatherHistoryListView;
    */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /*
        final ArrayList<weatherItem> weatherItemList = new ArrayList<weatherItem>();
        for(int i = 0; i < 1000; i++){
            weatherItemList.add(new weatherItem("Weather #" + (i+1), "Date #" + (i+1),"Temp #" + (i+1),"Time #" + (i+1),0));
        }

        listAdaptorObj = new listAdaptor(this, weatherItemList);
        weatherHistoryListView = (ListView)findViewById(R.id.listViewWeatherHistory);
        weatherHistoryListView.setAdapter(listAdaptorObj);
        */



        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

}
