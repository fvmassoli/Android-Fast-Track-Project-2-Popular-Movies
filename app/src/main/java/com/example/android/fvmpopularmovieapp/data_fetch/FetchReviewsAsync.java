package com.example.android.fvmpopularmovieapp.data_fetch;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.data_model.ReviewsFetchList;
import com.example.android.fvmpopularmovieapp.utilities.MovieEndPointInterface;
import java.io.IOException;
import java.util.List;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.BASE_QUERY_URL;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.ERROR_OCCURRED;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_DB_APY_KEY;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.TAG;


public class FetchReviewsAsync extends AsyncTaskLoader<List<Review>> {

    private List<Review> mDownlaodedReviewData;
    private String mMovieId;

    private ReviewsLoadedListener mReviewsLoadedListener;

    public interface ReviewsLoadedListener{
        void onReviewsLoaded(List<Review> reviews);
    }

    public FetchReviewsAsync(Context context, String movieId, ReviewsLoadedListener reviewsLoadedListener) {
        super(context);
        this.mMovieId = movieId;
        this.mReviewsLoadedListener = reviewsLoadedListener;
    }

    @Override protected void onStartLoading() {
        if(mDownlaodedReviewData != null) {
            deliverResult(mDownlaodedReviewData);
        }else
            forceLoad();
    }

    @Override
    public List<Review> loadInBackground() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_QUERY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieEndPointInterface movieEndPointInterface = retrofit.create(MovieEndPointInterface.class);
        Call<ReviewsFetchList> moviesCall = movieEndPointInterface.getReviews(mMovieId, MOVIE_DB_APY_KEY);

        try {
            Response<ReviewsFetchList> response = moviesCall.execute();
            ReviewsFetchList reviews = response.body();
            mDownlaodedReviewData = reviews.getReviewsList();
            return mDownlaodedReviewData;
        } catch (IOException e){
            Log.e(TAG, ERROR_OCCURRED + e.getStackTrace());
            return null;
        }
    }

    @Override
    public void deliverResult(List<Review> data) {
        if(isStarted() && data != null) {
            mReviewsLoadedListener.onReviewsLoaded(data);
            super.deliverResult(data);
        }
    }

    @Override protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected boolean onCancelLoad() {
        return super.onCancelLoad();
    }

    @Override protected void onReset() {
        super.onReset();
        onStopLoading();
        mDownlaodedReviewData = null;
    }

}
