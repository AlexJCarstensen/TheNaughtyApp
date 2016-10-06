package com.alexcarstensen.thebrandapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

public class ChatActivity extends AppCompatActivity implements ChatMessageListFragment.OnChatSelectedListener{

    private FragmentManager _fm;
    private ChatMessageListFragment _fragmentMessageList;
    ArrayList<MessageItem> messageItemList = new ArrayList<MessageItem>();
    public String mainUserName;
    String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        _fm = getSupportFragmentManager();
        _fragmentMessageList = (ChatMessageListFragment) _fm.findFragmentById(R.id.chat_fragment_list);

        Intent intent = getIntent();

        // Get data passed by using Bundle
        Bundle bundle = intent.getExtras();
        mainUserName = bundle.getString(MainActivity.SEND_USER_CHAT_INFO);
        contactName = bundle.getString(MainActivity.SEND_CONTACT_CHAT_INFO);


        // ** For debugging **
        for(int i = 0; i < 20; i++){
            messageItemList.add(new MessageItem("dummy",mainUserName,contactName,"Hi#"+i,"dummy","dummy","dummy"));
        }

        _fragmentMessageList.setMessageItemList(messageItemList);
        //**                **

        Toast.makeText(getApplication().getApplicationContext(),mainUserName + " chats with " + contactName, Toast.LENGTH_SHORT).show();

    }



}
