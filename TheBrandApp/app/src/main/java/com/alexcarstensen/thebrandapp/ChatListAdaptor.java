package com.alexcarstensen.thebrandapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
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

    public ChatListAdaptor(Context c, ArrayList<MessageItem> MessageItemList, String mainUserName, String contactName){
        this._context = c;
        this._MessageItems = MessageItemList;
        this._mainUserName = mainUserName;
        this._contactName = contactName;
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

        if(_MessageItemObj!=null){

            if(_MessageItemObj.get_sender().equals(_mainUserName)){

                LayoutInflater demoInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = demoInflater.inflate(R.layout.list_message_left_item, null);
            }
            else if(_MessageItemObj.get_sender().equals(_contactName)){
                LayoutInflater demoInflater = (LayoutInflater) this._context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = demoInflater.inflate(R.layout.list_message_right_item, null);
            }


            if(_MessageItemObj.get_sender().equals(_mainUserName)){

                TextView txtMessageLeft = (TextView) convertView.findViewById(R.id.textViewMessageLeft);
                txtMessageLeft.setText(_MessageItemObj.get_message());
            }
            else if(_MessageItemObj.get_sender().equals(_contactName)){
                TextView txtMessageRight = (TextView) convertView.findViewById(R.id.textViewMessageRight);
                txtMessageRight.setText(_MessageItemObj.get_message());
            }

        }
        return convertView;
    }


}
