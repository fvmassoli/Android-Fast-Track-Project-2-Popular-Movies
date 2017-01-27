package com.example.android.fvmpopularmovieapp.data_persistance;


import android.net.Uri;
import android.provider.BaseColumns;

public class MovieDataContract {

    public static final String CONTENT_AUTHORITY = "com.example.android.fvmpopularmovieapp";
    public static final Uri    BASE_CONTENTE_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
    public static final String MOVIE_TABLE_NAME  = "favorites";

    public static final class Contract implements BaseColumns {

        public static final Uri CONTENT_URI = BASE_CONTENTE_URI.buildUpon()
                .appendPath(MOVIE_TABLE_NAME)
                .build();

        public static final String COLUMN_MOVIE_TITLE         = "movie_title";
        public static final String COLUMN_MOVIE_ID            = "movie_id";
        public static final String COLUMN_MOVIE_RELEASE_DATE  = "movie_release_date";
        public static final String COLUMN_MOVIE_VOTE_AVERAGE  = "movie_vote_average";
        public static final String COLUMN_MOVIE_POSTER_PATH   = "movie_poster_path";
        public static final String COLUMN_MOVIE_BACKDROP_PATH = "movie_backdrop_path";
        public static final String COLUMN_MOVIE_OVERVIEW      = "movie_opverview";
    }
}
