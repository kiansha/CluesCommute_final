package com.example.aakansha.newpool.activity.activity;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.aakansha.newpool.R;

/**
 * Created by aakansha on 7/10/2017.
 */

public class RequestReceivedFragment extends Fragment {


    View myView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        myView=inflater.inflate(R.layout.frag_my_account,container,false);
        return myView;
    }
}
