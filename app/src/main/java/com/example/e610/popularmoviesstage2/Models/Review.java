package com.example.e610.popularmoviesstage2.Models;


import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class Review   implements Parcelable {
    private String Url="";
    private String ID="";
    private String Author="";
    private String content="";

    public Review(){ }

    public String getID() {
        return ID;
    }

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {

        this.content = content;
    }

    public String getAuthor() {return Author;}

    public void setAuthor(String author) {
        Author = author;
    }

    public String getUrl() {
        return Url;
    }

    public void setUrl(String url) {
        Url = url;
    }


    public static Review ParsingReviewData(String JsonData )
    {
        Review review=new Review();
        try {

            JSONObject jj=new JSONObject(JsonData);
            JSONArray ja=jj.getJSONArray("results");
            JSONObject j=ja.getJSONObject(0);


            review.setID(j.getString("id"));
            review.setAuthor(j.getString("author"));
            review.setContent(j.getString("content"));

        } catch (JSONException e) {
            e.  printStackTrace();
        }
        finally {
            return review;
        }

    }



    protected Review(Parcel in) {
        Url = in.readString();
        ID = in.readString();
        Author = in.readString();
        content = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(Url);
        dest.writeString(ID);
        dest.writeString(Author);
        dest.writeString(content);
    }

    @SuppressWarnings("unused")
    public static final Creator<Review> CREATOR = new Creator<Review>() {
        @Override
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        @Override
        public Review[] newArray(int size) {
            return new Review[size];
        }
    };
}