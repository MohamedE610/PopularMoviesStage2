 package com.example.e610.popularmoviesstage2.Activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.popularmoviesstage2.Adapters.MovieAdapter;
import com.example.e610.popularmoviesstage2.Data.MovieContract;
import com.example.e610.popularmoviesstage2.Fragments.DetailedFragment;
import com.example.e610.popularmoviesstage2.Fragments.MainFragment;
import com.example.e610.popularmoviesstage2.Models.Movie;
import com.example.e610.popularmoviesstage2.Models.Review;
import com.example.e610.popularmoviesstage2.Models.Trailer;
import com.example.e610.popularmoviesstage2.R;
import com.example.e610.popularmoviesstage2.Utils.FetchData;
import com.example.e610.popularmoviesstage2.Utils.MySharedPreferences;
import com.example.e610.popularmoviesstage2.Utils.NetworkResponse;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements MainFragment.SendToMainActivity {



    /******firstTime******/
    // I use "firstTime" variable to prevent spinner.OnItemSelectedListener()
    // from auto calling method onItemSelected() when onCreate() called
    // or "setting spinner adapter  pinner.setAdapter(SpinnerAdapter);"
    boolean firstTime;

    boolean InstanceState;

    MySharedPreferences mySharedPreferences;
    MainFragment mainFragment;
    Spinner spinner;
    Activity CurrentActivity;
    ArrayList<Movie> Movies;
    static boolean checkFrag=false;
    MovieAdapter movieAdapter;
    Bundle MoviesInfo;
    Context context;
    boolean IsTablet;
    RecyclerView MoviesRecyclerView;
    View view;
    public static Activity ctx; // for using it inside NetworkState()

    @Override
    protected void onCreate(Bundle savedInstanceState) {
       try {


           super.onCreate(savedInstanceState);
           setContentView(R.layout.activity_main);

           /******firstTime******/
           firstTime=true;

           CurrentActivity = this;
           ctx = this;
           movieAdapter = new MovieAdapter();
           mainFragment = new MainFragment();
           MoviesInfo = new Bundle();
           IsTablet = getResources().getBoolean(R.bool.isTablet);
           mySharedPreferences = new MySharedPreferences(this, "FavouriteMovies");

/******** ?????? *********/
           if (savedInstanceState == null) {
               getFragmentManager().beginTransaction().add(R.id.MainFragment, mainFragment).commit();
               InstanceState = false;
           } else {
               mainFragment = (MainFragment) getFragmentManager().findFragmentById(R.id.MainFragment);
               InstanceState = true;
               Movies = savedInstanceState.getParcelableArrayList("Movies");
           }

       }catch (Exception e){ Toast.makeText(MainActivity.ctx,"Error *_*", Toast.LENGTH_SHORT).show(); }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putParcelableArrayList("Movies",Movies);
        super.onSaveInstanceState(outState);
    }
//  I used this method to pass from MainFragment data to  MainActivity (this data is view of MainFragment)
    @Override
    public void send(View v) {
        try{
            view=v;
            context=view.getContext();
            MoviesRecyclerView=(RecyclerView) view.findViewById(R.id.MoviesRecyclerView);
            MoviesRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
            //MoviesRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3,1));

            if(IsTablet)
                MoviesRecyclerView.setLayoutManager(new GridLayoutManager(context,2));
            else
                MoviesRecyclerView.setLayoutManager(new GridLayoutManager(context,2));


            spinner=(Spinner) view.findViewById(R.id.spinner);
            String[] SpinnerData= {"Popular Movies","Top Rated Movies","Favourite Movies"};
            ArrayAdapter<String> SpinnerAdapter=new ArrayAdapter<String>(context,R.layout.spinner_item,SpinnerData);
            spinner.setAdapter(SpinnerAdapter);



            DisplayMovies();



        }catch (Exception e){
            Log.e("error","hellooooooooooooooooo",e); Toast.makeText(MainActivity.ctx,"Error *_*", Toast.LENGTH_SHORT).show(); }

    }



    public void DisplayMovies() {

            if (mySharedPreferences.IsFirstTime()) {
                mySharedPreferences.Clear();
                mySharedPreferences.FirstTime();
                collectData("Popular Movies");
            } else {
                String key = mySharedPreferences.getUserSetting();
                if (key.equals("Popular Movies") || key.equals("")) {
                    collectData("Popular Movies");
                    spinner.setSelection(0,false); // 0 index of "Popular Movies" in a Spinner also I can get it by  SpinnerAdapter.getPosition("Popular Movies");
                } else if (key.equals("Top Rated Movies")) {
                    collectData("Top Rated Movies");
                    spinner.setSelection(1,false);  // 1 index of "Top Rated Movies" in a Spinner
                } else if (key.equals("Favourite Movies")) {
                    DisplayFavouriteMovies();
                    spinner.setSelection(2,false); // 2 index of "Favourite Movies" in a Spinner
                }
            }
    }


    private void DisplayFavouriteMovies() {

            Movies = getFavouriteMovies();
            movieAdapter = new MovieAdapter(Movies, context);
            MoviesRecyclerView.setAdapter(movieAdapter);
            CheckTablet();
            ClickEvent();

    }

    private ArrayList<Movie> getFavouriteMovies() {
       ArrayList<Movie> movies =new ArrayList<>();

        Cursor cursor = getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,null,null,null,null);

        while (cursor.moveToNext()){
            Movie movie=new Movie();
            movie.setId(cursor.getString(MovieContract.MovieEntry.ID));
            movie.setTitle(cursor.getString(MovieContract.MovieEntry.TITLE));
            movie.setPoster_ImageUrl(cursor.getString(MovieContract.MovieEntry.POSTER));
            movie.setOverview(cursor.getString(MovieContract.MovieEntry.OVERVIEW));
            movie.setVote_average(cursor.getString(MovieContract.MovieEntry.VOTE));
            movie.setRelease_Date(cursor.getString(MovieContract.MovieEntry.DATE));

            Trailer trailer=new Trailer();
            trailer.setName(cursor.getString(MovieContract.MovieEntry.TrailerName));
            trailer.setKey(cursor.getString(MovieContract.MovieEntry.TrailerKey));
            movie.setTrailer(trailer);

            Review review=new Review();
            review.setAuthor(cursor.getString(MovieContract.MovieEntry.ReviewAuthor));
            review.setContent(cursor.getString(MovieContract.MovieEntry.ReviewContent));
            movie.setReview(review);

            movies.add(movie);
        }

        return movies;
    }


    // this method for fetch  movies data
    public void collectData(String Key) {

            if (MainActivity.NetworkState()) {
                FetchData fetchData = new FetchData(Key, "");
                ClickEvent();
                fetchData.execute();

                fetchData.setNetworkResponse(new NetworkResponse() {

                    @Override
                    public void OnSuccess(String JsonData) {

                        Movies = Movie.ParsingMoviesData(JsonData);
                        movieAdapter = new MovieAdapter(Movies, context);
                        MoviesRecyclerView.setAdapter(movieAdapter);

                        if (JsonData == null)
                            Toast.makeText(MainActivity.ctx, " No Internet Connection", Toast.LENGTH_SHORT).show();
                        ClickEvent();
                        CheckTablet();
                    }

                    @Override
                    public void OnUpdate(boolean IsDataReceived) {

                    }
                });
            } else {
                Movies = new ArrayList<>();
                movieAdapter = new MovieAdapter(Movies, context);
                MoviesRecyclerView.setAdapter(movieAdapter);
                Toast.makeText(this, " No Internet Connection", Toast.LENGTH_SHORT).show();
                ClickEvent();
                CheckTablet();
            }

    }





    public void ClickEvent(){
        movieAdapter.setClickListener(new MovieAdapter.RecyclerViewClickListener() {
            @Override
            public void ItemClicked(View v, int position) {

                Movie movie=new Movie();
                movie=Movies.get(position);
                MoviesInfo.putParcelable("Movie",movie);
                if (!IsTablet) {
                    Intent in = new Intent( CurrentActivity , DetailedActivity.class);
                    in.putExtra("MoviesInfo", MoviesInfo);
                    startActivity(in);
                } else {

                    DetailedFragment detailedFragment1=new DetailedFragment();
                    detailedFragment1.setArguments(MoviesInfo);
                    getFragmentManager().beginTransaction().replace(R.id.DetailedFragment,detailedFragment1).commit();

                }
            }
        });


      //firstTime=true;
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                /******firstTime******/
                if (!firstTime) {

                    if (view != null) {
                        checkFrag = false;
                        TextView textView = (TextView) view;
                        String SpinnerKey = textView.getText() + "";
                        if (SpinnerKey.equals("Popular Movies")) {
                            collectData("Popular Movies");
                            mySharedPreferences.setUserSetting(SpinnerKey);
                            DetailedFragment.IsFavouriteSelected(false);
                        } else if (SpinnerKey.equals("Top Rated Movies")) {
                            collectData("Top Rated Movies");
                            mySharedPreferences.setUserSetting(SpinnerKey);
                            DetailedFragment.IsFavouriteSelected(false);
                        } else if (SpinnerKey.equals("Favourite Movies")) {
                            mySharedPreferences.setUserSetting(SpinnerKey);
                            DisplayFavouriteMovies();
                            DetailedFragment.IsFavouriteSelected(true);
                        }
                    }

                } else {
                    /******firstTime******/
                    firstTime = false;
                }

            }


            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

    }

    public void CheckTablet(){
        Movie movie = new Movie();
        if (IsTablet ) {
            if (Movies.size() != 0)
                movie = Movies.get(0);
            MoviesInfo.putParcelable("Movie", movie);
            if (!InstanceState && !checkFrag ) {
                DetailedFragment detailedFragment1 = new DetailedFragment();
                detailedFragment1.setArguments(MoviesInfo);
                getFragmentManager().beginTransaction().replace(R.id.DetailedFragment, detailedFragment1).commit();
                checkFrag = true;
            }
        }
    }

//this method for check if internet is available or not
    public static boolean NetworkState()
    {
        ConnectivityManager connectivityManager = (ConnectivityManager) ctx.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }



}
