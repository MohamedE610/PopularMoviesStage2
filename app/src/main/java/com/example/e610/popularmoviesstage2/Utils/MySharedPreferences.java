package com.example.e610.popularmoviesstage2.Utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;


public class MySharedPreferences {



    ArrayList<String> MoviesFavouriteList;
    Context context;
    String FileName;
    SharedPreferences sharedPref;
    SharedPreferences.Editor editor;

    public MySharedPreferences(Context context, String FileName){
        this.context=context;
        this.FileName=FileName;
        this.sharedPref = context.getSharedPreferences(FileName, Context.MODE_PRIVATE);
        this.editor=sharedPref.edit();
        MoviesFavouriteList=new ArrayList<>();
    }

    public void setUserSetting(String UserSetting){
        editor.putString("UserSetting",UserSetting);
        editor.commit();
    }

    public String getUserSetting(){

        String UserSetting=sharedPref.getString("UserSetting","");

        return UserSetting;
    }
    public boolean IsFirstTime(){
        String check=sharedPref.getString("FirstTime","");

        if(check.equals("yes"))
            return false;
         return true;
    }

    public void FirstTime(){
        editor.putString("FirstTime","yes");
        editor.commit();
    }

    public void Clear(){
        editor.clear();
        editor.commit();
    }

}
