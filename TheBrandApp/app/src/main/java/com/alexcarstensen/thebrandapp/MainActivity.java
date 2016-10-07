package com.alexcarstensen.thebrandapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

// Implemented an interface between the MainContactListFragment and the MainActivity
public class MainActivity extends AppCompatActivity implements MainContactListFragment.OnContactSelectedListener {

    public static final String SEND_USER_CHAT_INFO = "send_user_chat_info";
    public static final String SEND_CONTACT_CHAT_INFO = "send_contact_chat_info";

    private UserItem mainUserItem;
    private FragmentManager _fm;
    private MainContactBarFragment _fragmentContactBar;
    private MainContactListFragment _fragmentContactList;


    ArrayList<UserItem> userItemList = new ArrayList<UserItem>();
    FloatingActionButton mapFab;
    FloatingActionButton addFab;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        _fm = getSupportFragmentManager();
        _fragmentContactBar = (MainContactBarFragment) _fm.findFragmentById(R.id.main_fragment_bar);
        _fragmentContactList = (MainContactListFragment) _fm.findFragmentById(R.id.main_fragment_list);

        // ** For debugging **
        mainUserItem = new UserItem("Jeppe","dummy");
        // **               **

        mapFab = (FloatingActionButton) findViewById(R.id.fapMapButton);
        addFab = (FloatingActionButton) findViewById(R.id.fapAddButton);

        mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);

                startActivity(mapIntent);

            }
        });

        addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Add!", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                setUserContacts();
            }
        });


    }

    public void startChatWithUserNumber(int userItemNumber) {

        if (userItemList != null) {
            UserItem tempUserItem = userItemList.get(userItemNumber);


            // Create an Intent to link to the ChatActivity
            // REF: http://www.androidtracks.com/android/how-to-pass-a-data-from-one-activity-to-another-in-android.html
            Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);

            Bundle userChatInfoBundle = new Bundle();
            userChatInfoBundle.putString(SEND_USER_CHAT_INFO, mainUserItem.get_userName());
            userChatInfoBundle.putString(SEND_CONTACT_CHAT_INFO, tempUserItem.get_userName());

            chatIntent.putExtras(userChatInfoBundle);

            startActivity(chatIntent);
        }

    }

    private void setUserContacts(){

        // ** For debugging **
        for(int i = 0; i < 100; i++){
            userItemList.add(new UserItem("User#"+i,"dummy"));
        }
        //**                **
        _fragmentContactList.setUserItemList(userItemList);

    }


}
