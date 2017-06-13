package com.example.e610.popularmoviesstage2.Activities;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.example.e610.popularmoviesstage2.Fragments.DetailedFragment;
import com.example.e610.popularmoviesstage2.R;


public class DetailedActivity extends AppCompatActivity {


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detailed_activity);

        DetailedFragment detailedFragment=new DetailedFragment();

        Bundle MoviesInfo= getIntent().getBundleExtra("MoviesInfo");
        detailedFragment.setArguments(MoviesInfo);
        getFragmentManager().beginTransaction().add(R.id.DetailedFragment,detailedFragment).commit();

    }


}

