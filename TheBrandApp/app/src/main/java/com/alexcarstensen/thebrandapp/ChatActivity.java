package com.alexcarstensen.thebrandapp;

import android.app.Activity;
import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

// REF: Made from ArniesFragmentsMovie example
public class ChatActivity extends AppCompatActivity implements ChatMessageListFragment.OnChatSelectedListener{

    private FragmentManager _fm;
    private ChatMessageListFragment _fragmentMessageList;
    private EditText _editTextChat;
    ArrayList<MessageItem> messageItemList = new ArrayList<MessageItem>();
    public String mainUserName;
    public String contactName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        _fm = getSupportFragmentManager();
        _fragmentMessageList = (ChatMessageListFragment) _fm.findFragmentById(R.id.chat_fragment_list);
        _editTextChat = (EditText) findViewById(R.id.editTextChat);

        Intent intent = getIntent();

        // Get data passed by using Bundle
        Bundle bundle = intent.getExtras();
        mainUserName = bundle.getString(MainActivity.SEND_USER_CHAT_INFO);
        contactName = bundle.getString(MainActivity.SEND_CONTACT_CHAT_INFO);


        // ** For debugging **
        for(int i = 0; i < 20; i++){

            if(i%2 == 1) {
                messageItemList.add(new MessageItem("dummy", mainUserName, contactName, "Hi#" + i, "dummy", "dummy", "dummy"));
            }
            else{
                messageItemList.add(new MessageItem("dummy", contactName, mainUserName, "Hi#" + i, "dummy", "dummy", "dummy"));
            }
        }
        // **               **
        _fragmentMessageList.setMessageItemList(messageItemList);

        _editTextChat.setHint(R.string.write_msg_here);
        _editTextChat.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            // REF: http://stackoverflow.com/questions/19217582/implicit-submit-after-hitting-done-on-the-keyboard-at-the-last-edittext
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String msg =_editTextChat.getText().toString();

                    messageItemList.add(setNewChatMessage(msg));
                    _fragmentMessageList.setMessageItemList(messageItemList);
                    _editTextChat.setText("");
                    return true;
                }
                return false;
            }
        });


        Toast.makeText(getApplication().getApplicationContext(),mainUserName + " chats with " + contactName, Toast.LENGTH_SHORT).show();

    }

    private MessageItem setNewChatMessage(String chatMessage){

        // Does something with this message
        MessageItem message = new MessageItem("dummy", mainUserName, contactName, chatMessage, "dummy", "dummy", "dummy");

        return message;
    }

    public String getMainUserName(){return mainUserName;}

    public String getContactName(){return contactName;}

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

}
