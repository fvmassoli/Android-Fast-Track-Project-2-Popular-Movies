package com.example.android.fvmpopularmovieapp.data_persistance;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;


public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "movies.db";
    private static final int DATABASE_VERSION = 1;

    private final String TEXT_NOT_NULL = " TEXT NOT NULL, ";

    public MoviesDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String CREATE_FAVORITE_MOVIE_DB = "CREATE TABLE " + MovieDataContract.MOVIE_TABLE_NAME         +
                " ("                                                                                          +
                MovieDataContract.Contract._ID                        + " INTEGER PRIMARY KEY AUTOINCREMENT," +
                MovieDataContract.Contract.COLUMN_MOVIE_TITLE         + TEXT_NOT_NULL                         +
                MovieDataContract.Contract.COLUMN_MOVIE_ID            + TEXT_NOT_NULL                         +
                MovieDataContract.Contract.COLUMN_MOVIE_POSTER_PATH   + TEXT_NOT_NULL                         +
                MovieDataContract.Contract.COLUMN_MOVIE_OVERVIEW      + " TEXT, "                             +
                MovieDataContract.Contract.COLUMN_MOVIE_VOTE_AVERAGE  + TEXT_NOT_NULL                         +
                MovieDataContract.Contract.COLUMN_MOVIE_RELEASE_DATE  + TEXT_NOT_NULL                         +
                MovieDataContract.Contract.COLUMN_MOVIE_BACKDROP_PATH + " TEXT NOT NULL"                      +
                " )";
        db.execSQL(CREATE_FAVORITE_MOVIE_DB);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + MovieDataContract.MOVIE_TABLE_NAME);
        onCreate(db);
    }
}
