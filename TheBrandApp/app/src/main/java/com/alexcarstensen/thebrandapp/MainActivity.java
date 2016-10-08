package com.alexcarstensen.thebrandapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

// Implemented an interface between the MainContactListFragment and the MainActivity
public class MainActivity extends AppCompatActivity implements MainContactListFragment.OnContactSelectedListener {

    final static String STATE_CONTACT_ARRAY = "ContactArray";
    final static String STATE_MAIN_USER = "MainUser";

    private UserItem _mainUserItem;
    private FragmentManager _fm;
    private MainContactBarFragment _fragmentContactBar;
    private MainContactListFragment _fragmentContactList;
    private FloatingActionButton _mapFab;
    private FloatingActionButton _addFab;

    ArrayList<UserItem> userItemList = new ArrayList<UserItem>();

    public static final String SEND_USER_CHAT_INFO = "send_user_chat_info";
    public static final String SEND_CONTACT_CHAT_INFO = "send_contact_chat_info";


    //Firebase
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SetupFirebase();

        _fm = getSupportFragmentManager();
        _fragmentContactBar = (MainContactBarFragment) _fm.findFragmentById(R.id.main_fragment_bar);
        _fragmentContactList = (MainContactListFragment) _fm.findFragmentById(R.id.main_fragment_list);
        _mapFab = (FloatingActionButton) findViewById(R.id.fapMapButton);
        _addFab = (FloatingActionButton) findViewById(R.id.fapAddButton);


        // Save chat message list
        if(savedInstanceState != null) {

            userItemList = savedInstanceState.getParcelableArrayList(STATE_CONTACT_ARRAY);
            _fragmentContactList.setUserItemList(userItemList);
            _mainUserItem = savedInstanceState.getParcelable(STATE_MAIN_USER);

        }
        else {
            // Todo: Hent contacts fra data basen der passer til main useren, skal denne opdateres periodisk?
            setUserContacts();
            // ** For debugging **
//            for (int i = 0; i < 3; i++) {
//
//                if (i % 2 == 1) {
//                    messageItemList.add(new MessageItem(mainUserName, contactName, "Hi#" + i, "dummy", 0));
//                } else {
//                    messageItemList.add(new MessageItem(contactName, mainUserName, "Hi#" + i, "dummy", 0));
//                }
//            }

            // Todo: Sæt main user up fra login - Brug evt. bundle med dette i?
            // ** For debugging **
            _mainUserItem = new UserItem("Jeppe","dummy_email");
            // **               **
        }


        // Start "map activity" on click
        _mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: Virker ikke helt?
                Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
                startActivity(mapIntent);

            }
        });

        // Start "add user activity"
        _addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                // Todo: Lav add contact activity og kald herfra
                // ** For debugging **
                Toast.makeText(getApplication().getApplicationContext(),"add user", Toast.LENGTH_SHORT).show();


                //creating dialog to add contact
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //TODO: remember to externalize string
                builder.setTitle("Add new contact");

                final EditText emailView = new EditText(MainActivity.this);
                emailView.setHint(R.string.emailHint);

                builder.setView(emailView);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        //TODO: contact server and check if user is in database

                        ValueEventListener userListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {

                                //Get User object from database

                                String contactEmail = emailView.getText().toString().toLowerCase();
                                String convertedEmail = EmailNameHelper.ConvertEmail(contactEmail);




                                if(dataSnapshot.hasChild(convertedEmail)) {

                                    UserItem user = dataSnapshot.child(convertedEmail).getValue(UserItem.class);

                                    Log.d("Userthingy", user.get_email());

                                    //Todo: Then insert user as a contact in your contacs and also insert




                                    String myEmail = "pede_ring@hotmail.com";
                                    String myConvertedEmail = EmailNameHelper.ConvertEmail(myEmail);

                                    Contact myContact = new Contact(user.get_email(), myConvertedEmail + "_" + EmailNameHelper.ConvertEmail(user.get_email()));

                                    mDatabase.child("Users").child(myConvertedEmail).child("_contacs").child(EmailNameHelper.ConvertEmail(user.get_email())).setValue(myContact);
                                }

                            }



                            @Override
                            public void onCancelled(DatabaseError databaseError) {
                                Log.d("Listener", "LoadUser:onCancelled", databaseError.toException());
                            }
                        };




                            mDatabase.child("Users").addListenerForSingleValueEvent(userListener);
                        }





                });

                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });



                AlertDialog dialog = builder.create();
                // **               **

                dialog.show();
            }
        });


    }

    private void SetupFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    // Create an Intent to link to the ChatActivity
    public void startChatWithUserNumber(int userItemNumber) {

        if (userItemList != null) {
            UserItem tempUserItem = userItemList.get(userItemNumber);

            // Pass a bundle with the main user name and the contact name
            // REF: http://www.androidtracks.com/android/how-to-pass-a-data-from-one-activity-to-another-in-android.html
            Intent chatIntent = new Intent(MainActivity.this, ChatActivity.class);
            Bundle userChatInfoBundle = new Bundle();
            userChatInfoBundle.putString(SEND_USER_CHAT_INFO, _mainUserItem.get_userName());
            userChatInfoBundle.putString(SEND_CONTACT_CHAT_INFO, tempUserItem.get_userName());
            chatIntent.putExtras(userChatInfoBundle);
            startActivity(chatIntent);
        }

    }


    private void setUserContacts(){
        // Todo: Få fat i contact listen fra databasen
        // ** For debugging **
        for(int i = 0; i < 100; i++){
            userItemList.add(new UserItem("User#"+i,"dummy_email"));
        }
        //**                **
        _fragmentContactList.setUserItemList(userItemList);

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CONTACT_ARRAY, userItemList);
        outState.putParcelable(STATE_MAIN_USER,_mainUserItem);
    }




}
