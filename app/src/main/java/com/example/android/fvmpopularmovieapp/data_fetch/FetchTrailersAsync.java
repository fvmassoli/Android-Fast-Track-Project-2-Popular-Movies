package com.example.android.fvmpopularmovieapp.data_fetch;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.example.android.fvmpopularmovieapp.data_model.Trailer;
import com.example.android.fvmpopularmovieapp.data_model.TrailersFetchList;
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


public class FetchTrailersAsync extends AsyncTaskLoader<List<Trailer>> {

    private List<Trailer> mDownlaodedTrailerData;
    private String mMovieId;
    private TrailersLoadedListener mTrailersLoadedListener;

    public interface TrailersLoadedListener{
        void onTrailersLoaded(List<Trailer> reviews);
    }

    public FetchTrailersAsync(Context context, String movieId, TrailersLoadedListener trailersLoadedListener) {
        super(context);
        this.mMovieId = movieId;
        this.mTrailersLoadedListener = trailersLoadedListener;
    }

    @Override protected void onStartLoading() {
        if(mDownlaodedTrailerData != null) {
            deliverResult(mDownlaodedTrailerData);
        }else
            forceLoad();
    }

    @Override
    public List<Trailer> loadInBackground() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_QUERY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieEndPointInterface movieEndPointInterface = retrofit.create(MovieEndPointInterface.class);
        Call<TrailersFetchList> trailersCall = movieEndPointInterface.getTrailers(mMovieId, MOVIE_DB_APY_KEY);

        try {
            Response<TrailersFetchList> response = trailersCall.execute();
            TrailersFetchList trailers = response.body();
            mDownlaodedTrailerData = trailers.getTrailersList();
            return mDownlaodedTrailerData;
        } catch (IOException e){
            Log.e(TAG, ERROR_OCCURRED + e.getStackTrace());
            return null;
        }
    }

    @Override
    public void deliverResult(List<Trailer> data) {
        if(isStarted() && data != null) {
            mTrailersLoadedListener.onTrailersLoaded(data);
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
        mDownlaodedTrailerData = null;
    }

}
