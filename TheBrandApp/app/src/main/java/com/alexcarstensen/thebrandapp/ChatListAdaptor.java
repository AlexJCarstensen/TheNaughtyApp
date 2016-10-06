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



public class ChatListAdaptor extends BaseAdapter {

    Context context;
    ArrayList<MessageItem> MessageItems;
    MessageItem MessageItemObj;

    public ChatListAdaptor(Context c, ArrayList<MessageItem> MessageItemList){
        this.context = c;
        this.MessageItems = MessageItemList;
    }

    @Override
    public int getCount() {
        if(MessageItems!=null) {
            return MessageItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(MessageItems!=null) {
            return MessageItems.get(position);
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

        MessageItemObj = MessageItems.get(position);


        if (convertView == null) {
            LayoutInflater demoInflator = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = demoInflator.inflate(R.layout.list_message_left_item, null);
        }



        if(MessageItemObj!=null){
            TextView txtMessageLeft = (TextView) convertView.findViewById(R.id.textViewMessageLeft);
            txtMessageLeft.setText(MessageItemObj.get_message());
        }
        return convertView;
    }


}
