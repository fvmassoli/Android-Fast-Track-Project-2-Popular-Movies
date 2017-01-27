package com.example.android.fvmpopularmovieapp.data_model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.RESULTS;


public class TrailersFetchList implements Serializable {

    @SerializedName(RESULTS)
    private List<Trailer> mTrailers = new LinkedList<>();

    public List<Trailer> getTrailersList(){ return mTrailers; }
}
