package com.example.e610.popularmoviesstage2.Utils;


import android.os.AsyncTask;
import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;


/**
 * Created by E610 on 21/09/2016.
 */

public class FetchData extends AsyncTask<Void,Void,String> {
    private String UrlKey;
    private String ID;
    public static String BasicUrl = "http://api.themoviedb.org/3/movie";
    public static String PopMoviesUrl = "/popular?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String TopMoviesUrl = "/top_rated?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String TrailersMoviesUrl = "/videos?api_key=d6464c49a9503b302888a4a139f1ed70";
    public static String ReviewsMoviesUrl = "/reviews?api_key=d6464c49a9503b302888a4a139f1ed70";

    public FetchData() {
    }

    public FetchData(String Key, String id) {

        UrlKey = Key;
        ID=id;
    }

    NetworkResponse networkResponse;

    public void setNetworkResponse(NetworkResponse networkResponse) {
        this.networkResponse = networkResponse;
    }

    public String Fetching_Data(String UrlKey) {
        HttpURLConnection urlConnect = null;
        BufferedReader reader = null;
        String JsonData = null;
        try {
            //final String UrlWithKey = "http://api.themoviedb.org/3/movie/popular?api_key=d6464c49a9503b302888a4a139f1ed70";
            String UrlWithKey = UrlKey;
            URL url = new URL(UrlWithKey);
            urlConnect = (HttpURLConnection) url.openConnection();
            urlConnect.setRequestMethod("GET");
            urlConnect.connect();
            InputStream inputStream = urlConnect.getInputStream();
            StringBuffer buffer = new StringBuffer();
            if (inputStream == null) {
                // Nothing to do.
                return null;
            }
            reader = new BufferedReader(new InputStreamReader(inputStream));
            String line;
            while ((line = reader.readLine()) != null) {
                // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                // But it does make debugging a *lot* easier if you print out the completed
                // buffer for debugging.
                buffer.append(line + "\n");
            }

            if (buffer.length() == 0) {
                // Stream was empty.  No point in parsing.
                return null;
            }
            JsonData = buffer.toString();
            Log.d("JSON", JsonData);
        } catch (IOException e) {
            Log.e("PlaceholderFragment", "Error ", e);
            // If the code didn't successfully get the weather data, there's no point in attemping
            // to parse it.
            return null;
        } finally {
            if (urlConnect != null) {
                urlConnect.disconnect();
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (final IOException e) {
                    Log.e("PlaceholderFragment", "Error closing stream", e);
                }
            }
        }
        return JsonData;
    }


    @Override
    protected String doInBackground(Void... voids) {
        String JsonData = "";

        if(UrlKey.equals("Popular Movies"))
            JsonData = Fetching_Data(BasicUrl+PopMoviesUrl);

        else if(UrlKey.equals("Top Rated Movies"))
            JsonData = Fetching_Data( BasicUrl+TopMoviesUrl);

        else if(UrlKey.equals("Review"))
            JsonData = Fetching_Data( BasicUrl + "/" + ID + ReviewsMoviesUrl);

        else if(UrlKey.equals("Trailer"))
            JsonData = Fetching_Data(  BasicUrl + "/" + ID + TrailersMoviesUrl);

        return JsonData;
    }

    @Override
    protected void onPostExecute(String JsonData) {
           super.onPostExecute(JsonData);

            networkResponse.OnSuccess(JsonData);

        //Toast.makeText(MainActivity.this, string, Toast.LENGTH_SHORT).show();
    }

}


