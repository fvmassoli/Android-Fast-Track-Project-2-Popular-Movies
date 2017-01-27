package com.example.android.fvmpopularmovieapp.utilities;


import com.example.android.fvmpopularmovieapp.BuildConfig;

public class Constants {

    public static final String MOVIE_DB_APY_KEY     = BuildConfig.MOVIE_DB_APY_KEY;

    public static final String BASE_QUERY_URL       = "https://api.themoviedb.org/3/";
    public static final String BASE_POSTER_URL      = "http://image.tmdb.org/t/p/w185/";
    public static final String BASE_BACK_DROP_URL   = "http://image.tmdb.org/t/p/w1280/";

    public static final String SAVED_MOVIES              = "saved_movies";
    public static final String SAVED_DETAIL_MOVIE        = "saved_detail_movie";
    public static final String SAVED_MOVIE_TRAILERS      = "saved_movie_trailers";
    public static final String SAVED_MOVIE_REVIEWS       = "saved_movie_reviews";
    public static final String SINGLE_MOVIE_DETAIL       = "single_movie_detail";

    public static final String TAG            = "exception_handler";
    public static final String ORIGINAL_TITLE = "original_title";
    public static final String RELEASE_DATE   = "release_date";
    public static final String POSTER_PATH    = "poster_path";
    public static final String BACK_DROP_PATH = "backdrop_path";
    public static final String VOTE_AVERAGE   = "vote_average";
    public static final String OVERVIEW       = "overview";
    public static final String MOVIE_ID       = "id";
    public static final String RESULTS        = "results";

    public static final String NETWORK_NOT_AVAILABLE     = "no_network";
    public static final String SHOW_REVIEW_DIALOG        = "show_review_dialog";
    public static final String IS_FAVOURITE_PARAM        = "is_favorite_param";
    public static final String DETAIL_FRAGMENT_TAG       = "MDFT";
    public static final String SETTINGS_FRAGMENT_TAG     = "STFT";
    public static final String SHOW_DIALOG               = "show_dialog";
    public static final String MUST_IMPLEMENT_INTERFACE  = " must implement LoadFirstMovieListener";
    public static final String ERROR_OCCURRED            = "An ERROR occurred: ";

    public static final String TOP_RATED_PARAM      = "top_rated";
    public static final String MOST_POPULAR_PARAM   = "popular";
    public static final String OVERFLOW_MENU_QUERY_PARAM = "overflow_menu_query_param";



    public static final int MOVIE_FAVORITES_LOADER_ID = 100;
    public static final int MOVIE_REVIEWS_LOADER_ID   = 200;
    public static final int MOVIE_TRAILERS_LOADER_ID  = 300;
    public static final int MOVIE_POSTER_LOADER_ID    = 400;
}
