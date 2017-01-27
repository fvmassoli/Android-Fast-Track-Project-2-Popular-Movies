package com.example.android.fvmpopularmovieapp.data_model;

import com.google.gson.annotations.SerializedName;

import java.util.LinkedList;
import java.util.List;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.RESULTS;


public class MoviesFetchList {

    @SerializedName(RESULTS)
    private List<Movie> mMovies = new LinkedList<>();

    public List<Movie> getMoviesList(){ return mMovies; }
}
