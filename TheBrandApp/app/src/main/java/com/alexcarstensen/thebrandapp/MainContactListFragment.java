package com.alexcarstensen.thebrandapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;

/**
 * Created by jeppe on 03-10-2016.
 */
// REF: Made from ArniesFragmentsMovie example
public class MainContactListFragment extends Fragment {

    final static String STATE_CONTACT_ARRAY = "ContactArray";

    private MainListAdaptor listAdaptorObj;
    private ListView mainContactsListView;
    private ArrayList<UserItem> UserItemList = new ArrayList<UserItem>();

    OnContactSelectedListener mainActivityCallback;
    View view;
    TextView textView;

    public MainContactListFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.contact_list_fragment_main,
                  container, false);

        mainContactsListView = (ListView)view.findViewById(R.id.listViewMainContacts);


        if(savedInstanceState != null){

            UserItemList = savedInstanceState.getParcelableArrayList(STATE_CONTACT_ARRAY);
            listAdaptorObj = new MainListAdaptor(view.getContext(), UserItemList);
            mainContactsListView.setAdapter(listAdaptorObj);

        }

        // REF: http://www.android-examples.com/set-onclicklistener-in-listview-in-android-programmatically/
        mainContactsListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                mainActivityCallback.startChatWithUserNumber(position);
            }
        });


        return view;
    }

    public void setUserItemList(ArrayList<UserItem> userItemList_){

        if (userItemList_!=null){
            UserItemList = userItemList_;
            listAdaptorObj = new MainListAdaptor(view.getContext(), UserItemList);
            mainContactsListView = (ListView)view.findViewById(R.id.listViewMainContacts);
            mainContactsListView.setAdapter(listAdaptorObj);;
        }
    }



    // Container Activity must implement this interface
    // REF: https://developer.android.com/training/basics/fragments/communicating.html
    public interface OnContactSelectedListener {
        public void startChatWithUserNumber(int userItemNumber);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            mainActivityCallback = (OnContactSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "ERROR! - must implement OnContactSelectedListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CONTACT_ARRAY, UserItemList);

    }

}








