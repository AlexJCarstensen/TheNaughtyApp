package com.alexcarstensen.thebrandapp;

import android.content.Context;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import java.util.ArrayList;

// Implemented an interface between the MainContactListFragment and the MainActivity
public class MainActivity extends AppCompatActivity implements MainContactListFragment.OnContactSelectedListener {


    private UserItem userItemObj;
    ArrayList<UserItem> userItemList = new ArrayList<UserItem>();
    private FragmentManager _fm;
    private MainContactBarFragment _fragmentContactBar;
    private MainContactListFragment _fragmentContactList;

    FloatingActionButton mapFab;
    FloatingActionButton addFab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fm = getSupportFragmentManager();
        _fragmentContactBar = (MainContactBarFragment) _fm.findFragmentById(R.id.main_fragment_bar);
        _fragmentContactList = (MainContactListFragment) _fm.findFragmentById(R.id.main_fragment_list);


        mapFab = (FloatingActionButton) findViewById(R.id.fapMapButton);
        addFab = (FloatingActionButton) findViewById(R.id.fapAddButton);


        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Map!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();


                for(int i = 0; i < 100; i++){
                    userItemList.add(new UserItem("dummy","User#"+i,"dummy","dummy"));
                }

                _fragmentContactList.setUserItemList(userItemList);

            }
        });






    }

    public void startChatWithUserNumber(int userItemNumber){

        if(userItemList != null) {
            UserItem tempUserItem = userItemList.get(userItemNumber);
            Toast.makeText(getApplication().getApplicationContext(),tempUserItem.get_userName(), Toast.LENGTH_SHORT).show();
        }

    }

//    @Override
//    public void onDataPass(String data) {
////        Log.d("LOG","hello " + data);
//        Toast.makeText(getApplication().getApplicationContext(),"TEST", Toast.LENGTH_SHORT).show();
//    }

}
