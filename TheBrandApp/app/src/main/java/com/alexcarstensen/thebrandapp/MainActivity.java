package com.alexcarstensen.thebrandapp;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

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
    ArrayList<Contact> contactList = new ArrayList<>();

    public static final String SEND_MAINUSER_EMAIL_INFO = "send_user_email_info";
    public static final String SEND_CONTACT_EMAIL_INFO = "send_contact_email_info";
    public static final String SEND_CONTACT_USERNAME_INFO = "send_contact_username_info";
    public static final String SEND_MAINUSER_USERNAME_INFO = "send_mainuser_username_info";
    //Firebase
    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent getIntent = getIntent();
        String email = getIntent.getStringExtra(LoginActivity.SEND_EMAIL);
        _mainUserItem = new UserItem("GET USERNAME TO PUT HERE", email);
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


        }


        // Start "map activity" on click

        _mapFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Todo: Virker ikke helt?
                Intent mapIntent = new Intent(getApplicationContext(), MapActivity.class);
                mapIntent.putExtra(SEND_MAINUSER_EMAIL_INFO,_mainUserItem.get_email());
                startActivity(mapIntent);

            }
        });



        // Start "add user activity"
        _addFab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                // ** For debugging **
                Toast.makeText(getApplication().getApplicationContext(),"add user", Toast.LENGTH_SHORT).show();


                //TODO: Lav pænere dialog
                //creating dialog to add contact
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

                //TODO: remember to externalize string
                builder.setTitle("Add new contact");

                final EditText emailView = new EditText(MainActivity.this);
                emailView.setHint(R.string.emailHint);
                emailView.setInputType(InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
                builder.setView(emailView);

                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        ValueEventListener userListener = new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {



                                String contactEmail = emailView.getText().toString().toLowerCase();
                                String contactConvertedEmail = EmailNameHelper.ConvertEmail(contactEmail);



                                //Checking if user is in database
                                if(dataSnapshot.hasChild(contactConvertedEmail)) {

                                    //Get User object from database
                                    UserItem user = dataSnapshot.child(contactConvertedEmail).getValue(UserItem.class);

                                    Log.d("Userthingy", user.get_email());

                                    String myEmail = _mainUserItem.get_email();

                                    //String myEmail = emailView.getText().toString();
                                    String myConvertedEmail = EmailNameHelper.ConvertEmail(myEmail);
                                    //Lige meget... slet det
                                    String usersConvertedEmail = EmailNameHelper.ConvertEmail(user.get_email());
                                    String chat = myConvertedEmail + "_" + contactConvertedEmail;

                                    Contact myContact = new Contact(user.get_email(), chat);
                                    myContact.setAdded(true);
                                    Contact usersContact = new Contact(myEmail, chat);
                                    usersContact.setAdded(false);

                                    //Updating your own contact list
                                    mDatabase.child(getResources().getString(R.string.users)).child(myConvertedEmail).child("_contacts").child(contactConvertedEmail).setValue(myContact);

                                    //Updating the user you want to add' contactlist
                                    mDatabase.child(getResources().getString(R.string.users)).child(usersConvertedEmail).child("_contacts").child(myConvertedEmail).setValue(usersContact);

                                    //Creating chat in database
                                    java.util.Date time = new java.util.Date();
                                    mDatabase.child(getResources().getString(R.string.chats)).child(chat).child("First Message").setValue(new MessageItem(myEmail, user.get_email(), "Test", time.toString() , 0));



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


                dialog.show();
            }
        });


    }

    private void temp_dialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);

        builder.setTitle("Friend request");



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
            userChatInfoBundle.putString(SEND_MAINUSER_EMAIL_INFO, _mainUserItem.get_email());
            userChatInfoBundle.putString(SEND_CONTACT_EMAIL_INFO, tempUserItem.get_email());
            userChatInfoBundle.putString(SEND_MAINUSER_USERNAME_INFO, _mainUserItem.get_userName());
            userChatInfoBundle.putString(SEND_CONTACT_USERNAME_INFO, tempUserItem.get_userName());
            chatIntent.putExtras(userChatInfoBundle);
            startActivity(chatIntent);
        }

    }


    private void setUserContacts(){


        ValueEventListener ContactListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Contacs", "Getting contacts");

                contactList.clear();

                for (DataSnapshot contactSnapshot: dataSnapshot.getChildren()
                     )
                {
                    Contact contact = contactSnapshot.getValue(Contact.class);

                    if(contact.isAdded())
                        contactList.add(contact);

                }

                RetriveContactUsers();

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };




        String userDatabasePoint = EmailNameHelper.ConvertEmail(_mainUserItem.get_email());

        //Attach valueListener to users contacts
        mDatabase.child(getResources().getString(R.string.users)).child(userDatabasePoint).child("_contacts").addValueEventListener(ContactListener);
        //





        /*
        for(int i = 0; i < 100; i++){
            userItemList.add(new UserItem("User#"+i,"dummy_email"));
        }
        //**                **

        */


    }

    private void RetriveContactUsers()
    {
        userItemList.clear();

        ValueEventListener userSingleListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Log.d("Users", "Getting Users");

                for (Contact contact: contactList
                        ) {
                    // add each contact to user list
                    UserItem user = dataSnapshot.child(EmailNameHelper.ConvertEmail(contact.getEmail())).getValue(UserItem.class);
                    userItemList.add(user);
                }

                //Update the UI with new contacts/usersItems
                _fragmentContactList.setUserItemList(userItemList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        //Attach singleValue listener for users to iterate throught and find the right ones
        mDatabase.child(getResources().getString(R.string.users)).addListenerForSingleValueEvent(userSingleListener);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CONTACT_ARRAY, userItemList);
        outState.putParcelable(STATE_MAIN_USER,_mainUserItem);
    }




}
