package com.example.android.fvmpopularmovieapp.utilities;


import com.example.android.fvmpopularmovieapp.data_model.MoviesFetchList;
import com.example.android.fvmpopularmovieapp.data_model.ReviewsFetchList;
import com.example.android.fvmpopularmovieapp.data_model.TrailersFetchList;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;


public interface MovieEndPointInterface {

    @GET("movie/{sort_by}")
    Call<MoviesFetchList> getMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<TrailersFetchList> getTrailers(@Path("id") String movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<ReviewsFetchList> getReviews(@Path("id") String movieId, @Query("api_key") String apiKey);
}