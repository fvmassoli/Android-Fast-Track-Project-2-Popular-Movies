package com.example.android.fvmpopularmovieapp.data_model;

import com.google.gson.annotations.SerializedName;
import java.util.LinkedList;
import java.util.List;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.RESULTS;


public class ReviewsFetchList {

    @SerializedName(RESULTS)
    private List<Review> mReviews = new LinkedList<>();

    public List<Review> getReviewsList(){ return mReviews; }
}
