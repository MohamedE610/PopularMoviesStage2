package com.example.e610.popularmoviesstage2.Data;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MovieContract {

    public static final String CONTENT_AUTHORITY = "com.example.e610.popularmoviesstage2";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String PATH_MOVIE = "Movie";

    public static final class MovieEntry implements BaseColumns {

        public static final int ID=1,TITLE=2,POSTER=3,OVERVIEW=4,
                VOTE=5,DATE=6,TrailerName=7,TrailerKey=8,
                ReviewAuthor=9,ReviewContent=10;

        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendPath(PATH_MOVIE).build();

        public static final String CONTENT_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_MOVIE;

        public static final String tableName = "Movie";
        public static final String movieIdColumn = "MovieId";
        public static final String movieTitleColumn = "MovieTitle";
        public static final String moviePosterPathColumn = "MoviePosterPath";
        public static final String movieOverViewColumn= "MovieOverview";
        public static final String movieVoteAverageColumn = "MovieVoteAverage";
        public static final String movieReleaseDateColumn = "MovieReleaseDate";
        public static final String movieTrailerNameColumn = "MovieTrailerName";
        public static final String movieTrailerKeyColumn = "MovieTrailerKey";
        public static final String movieReviewAuthorColumn = "MovieReviewAuthor";
        public static final String movieReviewContentColumn = "MovieReviewContent";



        public static Uri buildMovieUri(long id) {
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }


    }


}
