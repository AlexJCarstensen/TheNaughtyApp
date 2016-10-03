package com.alexcarstensen.thebrandapp;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by jeppe on 03-10-2016.
 */

public class MainContactBarFragment extends Fragment {


    View view;

    public MainContactBarFragment() {
    }
    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container,
                             Bundle savedInstanceState) {


        view = inflater.inflate(R.layout.contact_bar_fragment_main,
                container, false);


        return view;
    }



}




