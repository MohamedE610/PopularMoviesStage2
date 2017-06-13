package com.example.e610.popularmoviesstage2.Fragments;

import android.app.Fragment;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.e610.popularmoviesstage2.Activities.MainActivity;
import com.example.e610.popularmoviesstage2.Data.MovieContract;
import com.example.e610.popularmoviesstage2.Models.Movie;
import com.example.e610.popularmoviesstage2.Models.Review;
import com.example.e610.popularmoviesstage2.Models.Trailer;
import com.example.e610.popularmoviesstage2.R;
import com.example.e610.popularmoviesstage2.Utils.FetchData;
import com.example.e610.popularmoviesstage2.Utils.NetworkResponse;
import com.squareup.picasso.Picasso;


public class DetailedFragment extends Fragment {

    final public String imgString = "http://image.tmdb.org/t/p/w185/";


    ImageView Poster_Img;
    TextView Title;
    TextView Overview;
    TextView ReleaseDate;
    TextView Vote_Rating;
    TextView ReviewAuthor;
    TextView ReviewContent;
    TextView TrailerName;
    ImageView Favourite;
    ImageView button;


    Bundle MoviesInfo;
    Movie movie;

    View view;

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.detailed_fragment, container, false);
        view = v;
        MoviesInfo = new Bundle();
        movie = new Movie();


        Poster_Img = (ImageView) v.findViewById(R.id.Poster_Image);
        Title = (TextView) v.findViewById(R.id.Title);
        ReleaseDate = (TextView) v.findViewById(R.id.Release_Date);
        Overview = (TextView) v.findViewById(R.id.Overview);
        Vote_Rating = (TextView) v.findViewById(R.id.Vote_Rating);
        ReviewAuthor = (TextView) v.findViewById(R.id.ReviewAuthor);
        ReviewContent = (TextView) v.findViewById(R.id.ReviewContent);
        TrailerName = (TextView) v.findViewById(R.id.TrailerName);
        Favourite = (ImageView) v.findViewById(R.id.Favourite);
        button = (ImageView) v.findViewById(R.id.button);

        MoviesInfo = this.getArguments();

        if (savedInstanceState != null) {
            movie = savedInstanceState.getParcelable("Movie");
            setReviewDetails();
            setTrailerDetails();
           // movie = MoviesInfo.getParcelable("Movie");
        } else {
            movie = MoviesInfo.getParcelable("Movie");
            // this check to know if i have to fetch data from internet or get data from DataBase
            if (!FavouriteSelected) {
                FetchReview();
                FetchTrailer();
            } else {
                setReviewDetails();
                setTrailerDetails();
            }
        }

        setMovieDetails();

        movie.Favourite=isFavouriteMovie();
        if (movie.Favourite)
            Favourite.setImageResource(R.drawable.staron);
        else
            Favourite.setImageResource(R.drawable.staroff);


        Favourite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!movie.Favourite) {
                    movie.Favourite = true;
                    saveMovieInFavourite(movie);
                    Favourite.setImageResource(R.drawable.staron);
                    Toast.makeText(getActivity(), "Save in Favourite Movies", Toast.LENGTH_SHORT).show();
                } else {
                    movie.Favourite = false;
                    deleteMovieFromFavourite(movie.getId());
                    Toast.makeText(getActivity(), "Remove From Favourite Movies", Toast.LENGTH_SHORT).show();
                    Favourite.setImageResource(R.drawable.staroff);
                }

            }
        });


        return v;
    }

    private void deleteMovieFromFavourite(String id) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                if (isFavouriteMovie()) {
                    /********************/
                    getActivity().getContentResolver().delete(
                            MovieContract.MovieEntry.CONTENT_URI,MovieContract.MovieEntry.movieIdColumn + " = " + movie.getId(), null);
                }
                return null;
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);

    }


    private ContentValues setMovieContentValues(Movie favouriteMovie){
        ContentValues movieContentValues = new ContentValues();
        movieContentValues.put(MovieContract.MovieEntry.movieIdColumn,
                favouriteMovie.getId());
        movieContentValues.put(MovieContract.MovieEntry.movieTitleColumn,
                favouriteMovie.getTitle());
        movieContentValues.put(MovieContract.MovieEntry.moviePosterPathColumn,
                favouriteMovie.getPoster_ImageUrl());
        movieContentValues.put(MovieContract.MovieEntry.movieOverViewColumn,
                favouriteMovie.getOverview());
        movieContentValues.put(MovieContract.MovieEntry.movieVoteAverageColumn,
                favouriteMovie.getVote_average());
        movieContentValues.put(MovieContract.MovieEntry.movieReleaseDateColumn,
                favouriteMovie.getRelease_Date());
        movieContentValues.put(MovieContract.MovieEntry.movieTrailerNameColumn,
                favouriteMovie.getTrailerName());
        movieContentValues.put(MovieContract.MovieEntry.movieTrailerKeyColumn,
                favouriteMovie.getTrailerKey());
        movieContentValues.put(MovieContract.MovieEntry.movieReviewAuthorColumn,
                favouriteMovie.getReviewAuthor());
        movieContentValues.put(MovieContract.MovieEntry.movieReviewContentColumn,
                favouriteMovie.getReviewContent());

        return  movieContentValues;
    }
    private void saveMovieInFavourite(final Movie favouriteMovie) {
        new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... params) {
                if (!isFavouriteMovie()) {
                    ContentValues movieContentValues =setMovieContentValues(favouriteMovie);
                    /********************/
                    getActivity().getContentResolver().insert(
                            MovieContract.MovieEntry.CONTENT_URI,
                            movieContentValues
                    );


                }
                return null;
            }

        }.executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
    }


    private void setMovieDetails() {

        Picasso.with(view.getContext()).load(imgString + movie.getPoster_ImageUrl())
                .placeholder(R.drawable.loadingicon)
                .error(R.drawable.error).into(Poster_Img);

        Title.setText(movie.getTitle());
        Overview.setText(movie.getOverview());
        ReleaseDate.setText(movie.getRelease_Date());
        Vote_Rating.setText(movie.getVote_average() + "/10");
    }

    private void FetchReview() {

        if (MainActivity.NetworkState()) {
            FetchData fetchData = new FetchData("Review", movie.getId());
            fetchData.execute();

            fetchData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    Review review = new Review();
                    review = Review.ParsingReviewData(JsonData);
                    movie.setReview(review);
                    setReviewDetails();


                }

                @Override
                public void OnUpdate(boolean IsDataReceived) {

                }
            });
        } else {
            Review review = new Review();
            movie.setReview(review);
            setReviewDetails();
            Toast.makeText(getActivity(), " No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void FetchTrailer() {

        if (MainActivity.NetworkState()) {
            FetchData fetchData = new FetchData("Trailer", movie.getId());
            fetchData.execute();

            fetchData.setNetworkResponse(new NetworkResponse() {
                @Override
                public void OnSuccess(String JsonData) {
                    Trailer trailer = new Trailer();
                    trailer = Trailer.ParsingTrailerData(JsonData);
                    movie.setTrailer(trailer);

                    setTrailerDetails();
                }

                @Override
                public void OnUpdate(boolean IsDataReceived) {

                }
            });
        } else {
            Trailer trailer = new Trailer();
            movie.setTrailer(trailer);
            setTrailerDetails();
            Toast.makeText(getActivity(), " No Internet Connection", Toast.LENGTH_SHORT).show();
        }
    }

    private void setTrailerDetails() {
        TrailerName.setText(movie.getTrailerName());

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.youtube.com/watch?v=" + movie.getTrailerKey())));
            }
        });
    }

    private void setReviewDetails() {
        ReviewAuthor.setText(movie.getReviewAuthor());
        ReviewContent.setText(movie.getReviewContent());
    }


    private boolean isFavouriteMovie() {

        Cursor  cursor = null;
        /********************/
        cursor = getActivity().getContentResolver().query(MovieContract.MovieEntry.CONTENT_URI,
                new String[]{MovieContract.MovieEntry.movieIdColumn},
                MovieContract.MovieEntry.movieIdColumn + " = " + movie.getId(), null, null);

        if (cursor != null && cursor.moveToFirst()) {
            cursor.close();
            return true;
        } else {
            return false;
        }

    }

 static boolean FavouriteSelected;
    // i use this method to know if user click on favourite movies selection in spinner   return true if "favourite movies" selected and false otherwise
    public static void IsFavouriteSelected(boolean isSelected)
    {
        FavouriteSelected=isSelected;
    }



    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putParcelable("Movie", movie);
        super.onSaveInstanceState(outState);
    }


}
