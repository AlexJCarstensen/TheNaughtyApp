package com.alexcarstensen.thebrandapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jeppe on 05-10-2016.
 */

// REF: Made from ArniesFragmentsMovie example
public class ChatMessageBarFragment extends Fragment {


    View view;

    public ChatMessageBarFragment() {
    }

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.message_bar_fragment_chat,
                container, false);


        return view;
    }
}