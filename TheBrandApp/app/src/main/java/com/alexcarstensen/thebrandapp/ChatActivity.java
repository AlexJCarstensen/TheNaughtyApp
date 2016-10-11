package com.alexcarstensen.thebrandapp;

import android.*;
import android.Manifest;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Location;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import com.alexcarstensen.thebrandapp.Helpers.EmailNameHelper;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.ArrayList;

// REF: Made from ArniesFragmentsMovie example
public class ChatActivity extends AppCompatActivity implements ChatMessageListFragment.OnChatSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    final static String STATE_CHAT_MESSAGE_ARRAY = "ChatMessageArray";

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_CROP_IMAGE = 101;
    private static final int REQUEST_PERMISSIONS_CAMERA = 102;
    private static final int REQUEST_PERMISSIONS_FINE_LOCATION = 103;
    private static final int REQUEST_PERMISSIONS_COARSE_LOCATION = 104;
    private static final int REQUEST_IMAGE_MAP = 105;


    private FragmentManager _fm;
    private ChatMessageListFragment _fragmentMessageList;
    private EditText _editTextChat;
    private ImageButton _imageButtonCamera;
    private ImageButton _imageButtonMap;
    private Uri picUri;
    private Bitmap picThmp;
    private int picTaken = 0;
    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;

    ArrayList<MessageItem> messageItemList = new ArrayList<MessageItem>();


    public String mainUserName;
    public String contactName;
    public String mainUserEmail;
    public String contactEmail;
    private String chatName;

    //Firebase
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



        setContentView(R.layout.activity_chat);

        // Set up references
        _fm = getSupportFragmentManager();
        _fragmentMessageList = (ChatMessageListFragment) _fm.findFragmentById(R.id.chat_fragment_list);
        _editTextChat = (EditText) findViewById(R.id.editTextChat);
        _imageButtonCamera = (ImageButton)  findViewById(R.id.imageButtonCamera);
        _imageButtonMap = (ImageButton)  findViewById(R.id.imageButtonMap);

        // Get data passed by using Bundle, mainUser and contact
        Intent initIntent = getIntent();
        Bundle bundle = initIntent.getExtras();
        mainUserName = bundle.getString(MainActivity.SEND_MAINUSER_USERNAME_INFO);
        contactName = bundle.getString(MainActivity.SEND_CONTACT_USERNAME_INFO);
        mainUserEmail = bundle.getString(MainActivity.SEND_MAINUSER_CHAT_INFO);
        contactEmail = bundle.getString(MainActivity.SEND_CONTACT_CHAT_INFO);

        SetupFirebase();

        // Save chat message list
        if(savedInstanceState != null) {

            messageItemList = savedInstanceState.getParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY);
            _fragmentMessageList.setMessageItemList(messageItemList);
        }
        else{
            // Todo: Hent chatten fra data basen der passer til main user og contact relationen
            GetChatName();

            /*
            // ** For debugging **
            for (int i = 0; i < 3; i++) {

                if (i % 2 == 1) {
                    messageItemList.add(new MessageItem(mainUserEmail, contactEmail, "Hi#" + i, "dummy_timestamp", false));
                } else {
                    messageItemList.add(new MessageItem(contactEmail, mainUserEmail, "Hi#" + i, "dummy_timestamp", false));
                }
            }
            // **               **



            // Set chat list
            _fragmentMessageList.setMessageItemList(messageItemList);

            */

        }


        // Set up chat text edit
        _editTextChat.setHint(R.string.write_msg_here);
        _editTextChat.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            // REF: http://stackoverflow.com/questions/19217582/implicit-submit-after-hitting-done-on-the-keyboard-at-the-last-edittext
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {

                    String msg =_editTextChat.getText().toString();

                   setNewChatMessage(msg);
                    _fragmentMessageList.setMessageItemList(messageItemList);
                    _editTextChat.setText("");
                    return true;
                }
                return false;
            }
        });

        // Start "take picture" intent on click
        _imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                handleCameraPermissions();
            }
        });

        // Start "map activity" intent on click
        _imageButtonMap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplication().getApplicationContext(),"MapActivity", Toast.LENGTH_SHORT).show();
                //Todo: Lav intent til at starte MapActivity - returner billede i bundle

                // ** For debugging **
                // REF: http://stackoverflow.com/questions/3035692/how-to-convert-a-drawable-to-a-bitmap
//                Bitmap img = BitmapFactory.decodeResource(getResources(),
//                        R.drawable.noinfo);
//                messageItemList.add(setNewChatPicture(img,"dummy","dummy","dummy","dummy"));
//                _fragmentMessageList.setMessageItemList(messageItemList);
                // **               **
            }
        });

        // ** For debugging **
        //Toast.makeText(getApplication().getApplicationContext(),mainUserName + " chats with " + contactName, Toast.LENGTH_SHORT).show();
        // **               **
    }

    private void SetupFirebase()
    {
        mDatabase = FirebaseDatabase.getInstance().getReference();


    }

    private void GetChatName()
    {
        String userConvertedEmail = EmailNameHelper.ConvertEmail(mainUserEmail);
        String contactConvertedEmail = EmailNameHelper.ConvertEmail(contactEmail);

        ValueEventListener chatNameListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Contact contact = dataSnapshot.getValue(Contact.class);

                chatName = contact.getChat();

                SetupChatListener(chatName);

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        };

        mDatabase.child(getResources().getString(R.string.users)).child(userConvertedEmail).child("_contacts").child(contactConvertedEmail).addListenerForSingleValueEvent(chatNameListener);


    }

    private void SetupChatListener(String chatName)
    {

        ChildEventListener chatListener = new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {

                MessageItem newMessage = dataSnapshot.getValue(MessageItem.class);

                messageItemList.add(newMessage);

                updateChatListOnEvent();


            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.d("CHat", "Failes to retrieve messages!");
            }
        };

        mDatabase.child("Chats").child(chatName).addChildEventListener(chatListener);

    }


    private void updateChatListOnEvent(){

        _fragmentMessageList.setMessageItemList(messageItemList);

    }

    private void setNewChatMessage(String chatMessage){

        java.util.Date time = new java.util.Date();

        MessageItem message = new MessageItem(mainUserEmail, contactEmail, chatMessage, time.toString(), 0);

        UpdateChatWithMessage(message);


    }

    private void UpdateChatWithMessage(MessageItem newMessage)
    {
        mDatabase.child(getResources().getString(R.string.chats)).child(chatName).push().setValue(newMessage);
    }

    private MessageItem setNewChatPicture(Bitmap chatPicture, String timestamp, String pictureUrl, String latitude, String longitude){
        MessageItem pictureMessage = new MessageItem(mainUserEmail, contactEmail, "Empty Message", timestamp, 0);

        pictureMessage.set_hasImage(1); // Should be a bool
        pictureMessage.set_imageUrl(pictureUrl);
        pictureMessage.set_imageBitmap(chatPicture);
        pictureMessage.set_latitude(latitude);
        pictureMessage.set_longitude(longitude);

        return pictureMessage;
    }

    // Interface methods
    public String getMainUserName(){return mainUserName;}
    public String getContactName(){return contactName;}
    public String getMainUserEmail(){return mainUserEmail;}
    public String getContactEmail(){return contactEmail;}

    public void hideSoftKeyboard(Activity activity) {
        InputMethodManager inputMethodManager =
                (InputMethodManager) activity.getSystemService(
                        Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(
                activity.getCurrentFocus().getWindowToken(), 0);
    }

    public void startNavigation(MessageItem msgItem){
        String lat = msgItem.get_latitude();
        String lon = msgItem.get_longitude();
        //Todo: Start navigation intent med lat og lon

    }


    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY, messageItemList);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case REQUEST_IMAGE_CAPTURE: {
                if (resultCode == RESULT_OK) {
                    File file = new File(Environment.getExternalStorageDirectory().getPath(), "picture.jpg");
                    picUri = Uri.fromFile(file);
                    pictureCrop();
                }
            } break;
            case REQUEST_CROP_IMAGE: {
                if (resultCode == RESULT_OK) {
                    picTaken = 1;
                    Bundle extras = data.getExtras();
                    picThmp = extras.getParcelable("data");


                    FirebaseStorage storage = FirebaseStorage.getInstance();
                    StorageReference storageRef = storage.getReference(getResources().getString(R.string.storageURL));

                    final Long currentMili = System.currentTimeMillis();

                    String userRef = mainUserEmail + "/" + currentMili.toString();

                    StorageReference imageRef = storageRef.child(userRef);

                    ByteArrayOutputStream baos = new ByteArrayOutputStream();

                    picThmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);

                    byte[] byteData = baos.toByteArray();

                    UploadTask uploadTask = imageRef.putBytes(byteData);

                    uploadTask.addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {

                        }
                    }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Uri downloadUrl = taskSnapshot.getDownloadUrl();

                            mDatabase.child("Pictures").child(EmailNameHelper.ConvertEmail(mainUserName)).push().setValue(downloadUrl.toString());
                            java.util.Date time = new java.util.Date();
                            MessageItem newPictureMessage = setNewChatPicture(picThmp,time.toString(),downloadUrl.toString(),String.valueOf(mLastLocation.getLatitude()),String.valueOf(mLastLocation.getLongitude()));
                            UpdateChatWithMessage(newPictureMessage);
                            //_fragmentMessageList.setMessageItemList(messageItemList);
                        }
                    });






                    

                }
            } break;
            case REQUEST_IMAGE_MAP: {
                if (resultCode == RESULT_OK) {
//                    picTaken = 1;
//                    Bundle extras = data.getExtras();
//                    picThmp = extras.getParcelable("data");
//                    messageItemList.add(setNewChatPicture(picThmp,"dummy","dummy","dummy","dummy"));
//                    _fragmentMessageList.setMessageItemList(messageItemList);
                    //Todo: Hent picture, med coordinator mm. og sæt ind i chat
                }
            } break;
        }
    }


    //TODO: **** Permissions for API23 an above?? ****
    // REF: https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission OK
                    dispatchTakePictureIntent();
                    CheckPermissionIfGrantedGetLastKnowLocation();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), R.string.txtPermissionDenied, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case REQUEST_PERMISSIONS_FINE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission OK
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), R.string.txtPermissionDenied, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            case REQUEST_PERMISSIONS_COARSE_LOCATION:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission OK
                    buildGoogleApiClient();
                    mGoogleApiClient.connect();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), R.string.txtPermissionDenied, Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }

    }

    private void handleCameraPermissions() {
        if (ContextCompat.checkSelfPermission(ChatActivity.this, android.Manifest.permission.CAMERA)!= PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(ChatActivity.this,android.Manifest.permission.CAMERA)) {

                Toast.makeText(getApplication().getApplicationContext(),R.string.txtPermission, Toast.LENGTH_SHORT).show();
            }
            else{
                ActivityCompat.requestPermissions(ChatActivity.this, new String[]{android.Manifest.permission.CAMERA}, REQUEST_PERMISSIONS_CAMERA);

                //dispatchTakePictureIntent();
            }
        }
        else{


            buildGoogleApiClient();
            mGoogleApiClient.connect();
            dispatchTakePictureIntent();

        }

    }


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            String imageFilePath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/picture.jpg";
            File imageFile = new File(imageFilePath);
            Uri  imageFileUri = Uri.fromFile(imageFile);

            takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, imageFileUri);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    //REF: http://stackoverflow.com/questions/15228812/crop-image-in-android question and answer
    private void pictureCrop() {
        // take care of exceptions
        try {
            // call the standard crop action intent (the user device may not support it)
            Intent cropIntent = new Intent("com.android.camera.action.CROP");
            // indicate image type and Uri
            cropIntent.setDataAndType(picUri, "image/*");
            // set crop properties
            cropIntent.putExtra("crop", "true");
            // indicate aspect of desired crop
            cropIntent.putExtra("aspectX", 1);
            cropIntent.putExtra("aspectY", 1);
            // indicate output X and Y
            cropIntent.putExtra("outputX", 550);
            cropIntent.putExtra("outputY", 550);
            // retrieve data on return
            cropIntent.putExtra("return-data", true);
            // start the activity - we handle returning in onActivityResult
            startActivityForResult(cropIntent, REQUEST_CROP_IMAGE);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, R.string.this_device_do_not_sup_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }


    private void CheckPermissionIfGrantedGetLastKnowLocation()
    {
        if (ActivityCompat.checkSelfPermission(ChatActivity.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(ChatActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_FINE_LOCATION);
        }
        else
            mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
    }

    private void buildGoogleApiClient()
    {
        if (mGoogleApiClient == null)
        {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        CheckPermissionIfGrantedGetLastKnowLocation();
    }


    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    protected void onStop()
    {
        if(mGoogleApiClient != null){
            mGoogleApiClient.disconnect();
        }
        super.onStop();
    }

}



