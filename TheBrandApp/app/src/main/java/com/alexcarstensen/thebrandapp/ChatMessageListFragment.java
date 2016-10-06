package com.alexcarstensen.thebrandapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by jeppe on 05-10-2016.
 */


public class ChatMessageListFragment extends Fragment{

    final static String STATE_CHAT_MESSAGE_ARRAY = "ChatMessageArray";

    private ChatListAdaptor listAdaptorObj;
    private ListView chatMessagesListView;
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

        chatMessagesListView = (ListView)view.findViewById(R.id.listViewChatMessages);


        if(savedInstanceState != null){

            MessageItemList = savedInstanceState.getParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY);
            listAdaptorObj = new ChatListAdaptor(view.getContext(), MessageItemList);
            chatMessagesListView.setAdapter(listAdaptorObj);

        }

        // REF: http://www.android-examples.com/set-onclicklistener-in-listview-in-android-programmatically/
        chatMessagesListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                //chatActivityCallback.startChatWithUserNumber(position);
                Toast.makeText(getActivity().getApplicationContext(),"CLICK", Toast.LENGTH_SHORT).show();
            }
        });



//        textView = (TextView) view.findViewById(R.id.textViewUserContact);
//        textView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //Toast.makeText(getActivity(), "", Toast.LENGTH_SHORT).show();
//
//                int x = 0;
//            }
//        });
//        else{
//            for(int i = 0; i < 100; i++){
//                MessageItemList.add(new MessageItem("dummy","User#"+i,"dummy","dummy"));
//            }
//
//            listAdaptorObj = new ChatListAdaptor(view.getContext(), MessageItemList);
//            chatMessagesListView = (ListView)view.findViewById(R.id.listViewMainContacts);
//            chatMessagesListView.setAdapter(listAdaptorObj);
//        }



        return view;
    }

    public void setMessageItemList(ArrayList<MessageItem> messageItemList_){

        if (messageItemList_!=null){
            MessageItemList = messageItemList_;
            listAdaptorObj = new ChatListAdaptor(view.getContext(), MessageItemList);
            chatMessagesListView = (ListView)view.findViewById(R.id.listViewChatMessages);
            chatMessagesListView.setAdapter(listAdaptorObj);;
        }
    }




    // Container Activity must implement this interface
    // REF: https://developer.android.com/training/basics/fragments/communicating.html
    public interface OnChatSelectedListener {
        //public void startChatWithUserNumber(int messageItemNumber);
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

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(STATE_CHAT_MESSAGE_ARRAY, MessageItemList);

    }

}


