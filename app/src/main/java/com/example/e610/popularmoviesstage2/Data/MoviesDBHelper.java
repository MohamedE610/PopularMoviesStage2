package com.example.e610.popularmoviesstage2.Data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDBHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Movies.db";

    public MoviesDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_MOVIES_TABLE = "CREATE TABLE " + MovieContract.MovieEntry.tableName
                + " ( " +
                MovieContract.MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieEntry.movieIdColumn + " INTEGER NOT NULL, " +
                MovieContract.MovieEntry.movieTitleColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.moviePosterPathColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieOverViewColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieVoteAverageColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieReleaseDateColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieTrailerNameColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieTrailerKeyColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieReviewAuthorColumn + " TEXT NOT NULL, " +
                MovieContract.MovieEntry.movieReviewContentColumn + " TEXT NOT NULL" +
                " );";
        sqLiteDatabase.execSQL(SQL_CREATE_MOVIES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieEntry.tableName);
        onCreate(sqLiteDatabase);
    }
}
