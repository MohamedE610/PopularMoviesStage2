package com.example.e610.popularmoviesstage2.Models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Trailer  implements Parcelable {
    private String ID="";
    private String Key="";
    private String Name="";

    public Trailer(){}

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getKey() {
        return Key;
    }

    public void setKey(String key) {
        Key = key;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public static   Trailer ParsingTrailerData(String JsonData)
    {

        Trailer trailer=new Trailer();
        try {

            JSONObject jj=new JSONObject(JsonData);
            JSONArray ja=jj.getJSONArray("results");
            JSONObject j=ja.getJSONObject(0);


            trailer.setID(j.getString("id"));
            trailer.setKey(j.getString("key"));
            trailer.setName(j.getString("name"));

        } catch (JSONException e) {
            e.  printStackTrace();
        }
        finally{
            return trailer;
        }


    }


    protected Trailer(Parcel in) {
        ID = in.readString();
        Key = in.readString();
        Name = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(ID);
        dest.writeString(Key);
        dest.writeString(Name);
    }

    @SuppressWarnings("unused")
    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };
}
