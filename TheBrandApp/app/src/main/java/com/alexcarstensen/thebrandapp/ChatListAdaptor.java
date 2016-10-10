package com.alexcarstensen.thebrandapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by jeppe on 05-10-2016.
 */


// REF: Made from ArniesFragmentsMovie example
public class ChatListAdaptor extends BaseAdapter {

    private Context _context;
    private ArrayList<MessageItem> _MessageItems;
    private MessageItem _MessageItemObj;
    private String _mainUserName;
    private String _contactName;
    private String _mainUserEmail;
    private String _contactEmail;

    public ChatListAdaptor(Context c, ArrayList<MessageItem> MessageItemList, String mainUserName, String mainUserEmail, String contactName, String contactEmail){
        this._context = c;
        this._MessageItems = MessageItemList;
        this._mainUserName = mainUserName;
        this._mainUserEmail = mainUserEmail;
        this._contactName = contactName;
        this._contactEmail = contactEmail;
    }

    @Override
    public int getCount() {
        if(_MessageItems !=null) {
            return _MessageItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(_MessageItems !=null) {
            return _MessageItems.get(position);
        } else {
            return null;
        }
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        _MessageItemObj = _MessageItems.get(position);

        // Check for null obj
        if(_MessageItemObj!=null){

            // Check if message has a picture
            if(_MessageItemObj.get_hasImage() == false) {

                // Check sender/receiver inflate proper textView
                if (_MessageItemObj.get_sender().equals(_mainUserEmail)) {

                    LayoutInflater demoInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = demoInflater.inflate(R.layout.list_message_left_item, null);
                } else if (_MessageItemObj.get_sender().equals(_contactEmail)) {
                    LayoutInflater demoInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = demoInflater.inflate(R.layout.list_message_right_item, null);
                }

                // Check sender/receiver set text in textView
                if (_MessageItemObj.get_sender().equals(_mainUserEmail)) {

                    TextView txtMessageLeft = (TextView) convertView.findViewById(R.id.textViewMessageLeft);
                    txtMessageLeft.setText(_MessageItemObj.get_message());
                } else if (_MessageItemObj.get_sender().equals(_contactEmail)) {
                    TextView txtMessageRight = (TextView) convertView.findViewById(R.id.textViewMessageRight);
                    txtMessageRight.setText(_MessageItemObj.get_message());
                }
            }
            else if(_MessageItemObj.get_hasImage()==true){

                // Check sender/receiver inflate proper imageView
                if (_MessageItemObj.get_sender().equals(_mainUserEmail)) {
                    LayoutInflater demoInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = demoInflater.inflate(R.layout.list_picture_message_left_item, null);
                } else if (_MessageItemObj.get_sender().equals(_contactEmail)) {
                    LayoutInflater demoInflater = (LayoutInflater) this._context
                            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                    convertView = demoInflater.inflate(R.layout.list_picture_message_right_item, null);
                }

                // Check sender/receiver set bitmap in imageView
                if (_MessageItemObj.get_sender().equals(_mainUserEmail)) {
                    ImageView imgViewMessageLeft = (ImageView) convertView.findViewById(R.id.imageViewMessageLeft);
                    imgViewMessageLeft.setImageBitmap(_MessageItemObj.get_imageBitmap());
                } else if (_MessageItemObj.get_sender().equals(_contactEmail)) {
                    ImageView imgViewMessageRight = (ImageView) convertView.findViewById(R.id.imageViewMessageRight);
                    imgViewMessageRight.setImageBitmap(_MessageItemObj.get_imageBitmap());
                }
            }
        }
        return convertView;
    }


}
