package com.example.e610.popularmoviesstage2.Fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.e610.popularmoviesstage2.R;



public class MainFragment extends Fragment {

    SendToMainActivity sendToMainActivity;
     @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


         sendToMainActivity=(SendToMainActivity)getActivity();
         View v =  inflater.inflate(R.layout.main_fragment, container, false);

         sendToMainActivity.send(v);

        return v;

    }

// this interface to send data to MainActivity  this data is a view of MainFragment
    public interface SendToMainActivity{
        void send(View v);
    }
}

