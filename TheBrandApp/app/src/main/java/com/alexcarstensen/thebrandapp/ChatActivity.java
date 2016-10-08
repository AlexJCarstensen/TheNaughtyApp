package com.alexcarstensen.thebrandapp;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentManager;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.util.ArrayList;

// REF: Made from ArniesFragmentsMovie example
public class ChatActivity extends AppCompatActivity implements ChatMessageListFragment.OnChatSelectedListener{

    final static String STATE_CHAT_MESSAGE_ARRAY = "ChatMessageArray";

    private static final int REQUEST_IMAGE_CAPTURE = 100;
    private static final int REQUEST_CROP_IMAGE = 101;
    private static final int REQUEST_PERMISSIONS_CAMERA = 102;
    private static final int PIC_CROP = 103;
    private static final int REQUEST_IMAGE_MAP = 104;


    private FragmentManager _fm;
    private ChatMessageListFragment _fragmentMessageList;
    private EditText _editTextChat;
    private ImageButton _imageButtonCamera;
    private ImageButton _imageButtonMap;
    private Uri picUri;
    private Bitmap picThmp;
    private int picTaken = 0;

    ArrayList<MessageItem> messageItemList = new ArrayList<MessageItem>();


    public String mainUserName;
    public String contactName;

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
        mainUserName = bundle.getString(MainActivity.SEND_USER_CHAT_INFO);
        contactName = bundle.getString(MainActivity.SEND_CONTACT_CHAT_INFO);

        // Save chat message list
        if(savedInstanceState != null) {

            messageItemList = savedInstanceState.getParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY);
            _fragmentMessageList.setMessageItemList(messageItemList);
        }
        else{
            // Todo: Hent chatten fra data basen der passer til main user og contact relationen
            // ** For debugging **
            for (int i = 0; i < 3; i++) {

                if (i % 2 == 1) {
                    messageItemList.add(new MessageItem(mainUserName, contactName, "Hi#" + i, "dummy_timestamp", 0));
                } else {
                    messageItemList.add(new MessageItem(contactName, mainUserName, "Hi#" + i, "dummy_timestamp", 0));
                }
            }
            // **               **

            // Set chat list
            _fragmentMessageList.setMessageItemList(messageItemList);
        }


        // Set up chat text edit
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

        // Start "take picture" intent on click
        _imageButtonCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Todo: Der skal måske laves noget med camera permissions til API23 og over
                dispatchTakePictureIntent();
                //handleCameraPermissions();
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

    private void updateChatListOnEvent(){
        //Todo: Opdater chatten ved event.
//        messageItemList = database??
//        _fragmentMessageList.setMessageItemList(messageItemList);

    }

    private MessageItem setNewChatMessage(String chatMessage){
        //Todo: Updater databasen
        MessageItem message = new MessageItem(mainUserName, contactName, chatMessage, "dummy_timestamp", 0);

        return message;
    }

    private MessageItem setNewChatPicture(Bitmap chatPicture, String timestamp, String pictureUrl, String latitude, String longitude){
        //Todo: Do something with this picture message
        MessageItem pictureMessage = new MessageItem(mainUserName, contactName, "Empty Message", "dummy_timestamp", 0);

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
                    messageItemList.add(setNewChatPicture(picThmp,"dummy_timestamp","dummy_pictureUrl","dummy_lat","dummy_lon"));
                    _fragmentMessageList.setMessageItemList(messageItemList);
                    //Todo: Hent timestamp, GPS coords og sæt billede ind i data base

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

    /*
    //TODO: **** Permissions for API23 an above?? ****
    // REF: https://developer.android.com/training/permissions/requesting.html
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSIONS_CAMERA:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission OK
                    dispatchTakePictureIntent();
                } else {
                    // Permission Denied
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT)
                            .show();
                }
                break;
            default:
                super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void handleCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            if (!shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                showAlertOkCancel(getResources().getString("You need to allow camera access"),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(new String[]{android.Manifest.permission.CAMERA},
                                        REQUEST_PERMISSIONS_CAMERA);
                            }
                        });
                return;
            }
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CAMERA},
                    REQUEST_PERMISSIONS_CAMERA);
            return;
        }
        dispatchTakePictureIntent();
    }

    // REF: http://stackoverflow.com/questions/15020878/i-want-to-show-ok-and-cancel-button-in-my-alert-dialog
    private void showAlertOkCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(getApplicationContext())
                .setMessage(message)
                .setPositiveButton("Ok", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    */

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
            startActivityForResult(cropIntent, PIC_CROP);
        }
        // respond to users whose devices do not support the crop action
        catch (ActivityNotFoundException anfe) {
            Toast toast = Toast
                    .makeText(this, R.string.this_device_do_not_sup_crop, Toast.LENGTH_SHORT);
            toast.show();
        }
    }
}



