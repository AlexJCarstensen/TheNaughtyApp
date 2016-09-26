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


<<<<<<< HEAD
=======

    /*
    private listAdaptor listAdaptorObj;
    private ListView weatherHistoryListView;
    */
>>>>>>> 2382cab936612d20b60546d780fea730af063d14
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
            }
        });
    }

}
