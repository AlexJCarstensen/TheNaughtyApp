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
 * Created by jeppe on 03-10-2016.
 */

// REF: Made from ArniesFragmentsMovie example
public class MainListAdaptor extends BaseAdapter {

    Context context;
    ArrayList<UserItem> UserItems;
    UserItem UserItemObj;

    public MainListAdaptor(Context c, ArrayList<UserItem> UserItemList){
        this.context = c;
        this.UserItems = UserItemList;
    }

    @Override
    public int getCount() {
        if(UserItems!=null) {
            return UserItems.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getItem(int position) {
        if(UserItems!=null) {
            return UserItems.get(position);
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

        if (convertView == null) {
            LayoutInflater demoInflater = (LayoutInflater) this.context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = demoInflater.inflate(R.layout.list_user_item, null);
        }

        UserItemObj = UserItems.get(position);

        if(UserItemObj!=null){
            TextView txtContactName = (TextView) convertView.findViewById(R.id.textViewUserContact);
            txtContactName.setText(UserItemObj.get_userName());


        }
        return convertView;
    }


}




