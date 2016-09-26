package com.alexcarstensen.weatherornotyoulikeit;


import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ListView;



import java.util.ArrayList;



public class MainActivity extends AppCompatActivity {

    // For debugging
    int i = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);





        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, R.string.txtUpdateWeather, Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                // For debugging
                weatherItem wObj = new weatherItem("Weather #" + (i+1), "Date #" + (i+1),"Temp #" + (i+1),"Time #" + (i+1),0);
                FragmentManager fm = getSupportFragmentManager();
                content_history_weather_fragment fragment = (content_history_weather_fragment) fm.findFragmentById(R.id.fragment_history_weather);
                fragment.setWeatherObject(wObj);
                i++;

            }
        });
    }

}
