package com.alexcarstensen.thebrandapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jeppe on 05-10-2016.
 */

// REF: Made from ArniesFragmentsMovie example
public class ChatMessageListFragment extends Fragment{



    private ChatListAdaptor listAdaptorObj;
    private GridView chatMessagesListView;
    private ArrayList<MessageItem> MessageItemList = new ArrayList<MessageItem>();

    OnChatSelectedListener chatActivityCallback;
    View view;
    TextView textView;

    public ChatMessageListFragment() {
    }



    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.message_list_fragment_chat,
                container, false);

        chatMessagesListView = (GridView)view.findViewById(R.id.listViewChatMessages);


//        if(savedInstanceState != null){
//
//            MessageItemList = savedInstanceState.getParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY);
//            listAdaptorObj = new ChatListAdaptor(view.getContext(), MessageItemList,chatActivityCallback.getMainUserName(),chatActivityCallback.getContactName());
//            chatMessagesListView.setAdapter(listAdaptorObj);
//
//        }

        // REF: http://www.android-examples.com/set-onclicklistener-in-listview-in-android-programmatically/
        chatMessagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                chatActivityCallback.hideSoftKeyboard(getActivity());

                MessageItem clickCheckMessageItem = MessageItemList.get(position);
                if(clickCheckMessageItem.get_hasImage() == false){
                    Toast.makeText(getActivity().getApplicationContext(),"CLICK", Toast.LENGTH_SHORT).show();
                    chatActivityCallback.startNavigation(clickCheckMessageItem);
                    // Todo: Lav eventuel alertdialog her?
                }
                // Lav interface her der reagere på om det er hasimage eller ej


            }
        });




        return view;
    }

    public void setMessageItemList(ArrayList<MessageItem> messageItemList_){

        if (messageItemList_!=null){
            MessageItemList = messageItemList_;
            listAdaptorObj = new ChatListAdaptor(view.getContext(), MessageItemList,chatActivityCallback.getMainUserName(),chatActivityCallback.getContactName());
            chatMessagesListView = (GridView)view.findViewById(R.id.listViewChatMessages);
            chatMessagesListView.setAdapter(listAdaptorObj);;
        }
    }




    // Container Activity must implement this interface
    // REF: https://developer.android.com/training/basics/fragments/communicating.html
    public interface OnChatSelectedListener {
        //public void startChatWithUserNumber(int messageItemNumber);
        public String getMainUserName();
        public String getContactName();
        public void hideSoftKeyboard(Activity activity);
        public void startNavigation(MessageItem msgItem);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try {
            chatActivityCallback = (OnChatSelectedListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                    + "ERROR! - must implement OnMessageSelectedListener");
        }
    }



}


