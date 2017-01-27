package com.example.android.fvmpopularmovieapp.data_fetch;

import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;
import android.util.Log;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_model.MoviesFetchList;
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


public class FetchMoviesAsync extends AsyncTaskLoader<List<Movie>> {

    private String mMovieCriteriaSelection;
    private List<Movie> mDownlaodedMovieData;
    private MoviesLoadedListener mMoviesLoadedListener;

    public interface MoviesLoadedListener{
        void onMoviesLoaded(List<Movie> movies);
    }

    public FetchMoviesAsync(Context context, String movieCriteriaSelection, MoviesLoadedListener moviesLoadedListener) {
        super(context);
        this.mMovieCriteriaSelection = movieCriteriaSelection;
        this.mMoviesLoadedListener = moviesLoadedListener;
    }

    @Override protected void onStartLoading() {
        if(mDownlaodedMovieData != null && mDownlaodedMovieData.size() != 0) {
            deliverResult(mDownlaodedMovieData);
        } else {
            forceLoad();
        }
    }

    @Override
    public List<Movie> loadInBackground() {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(BASE_QUERY_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieEndPointInterface movieEndPointInterface = retrofit.create(MovieEndPointInterface.class);
        Call<MoviesFetchList> moviesCall = movieEndPointInterface.getMovies(mMovieCriteriaSelection, MOVIE_DB_APY_KEY);

        try {
            Response<MoviesFetchList> response = moviesCall.execute();
            MoviesFetchList movies = response.body();
            mDownlaodedMovieData = movies.getMoviesList();
            for(Movie m : mDownlaodedMovieData){
                String pPath = m.getPosterPath();
                m.setPosterPath(pPath);
                String bPath = m.getBackDropPath();
                m.setBackDropPath(bPath);
            }
            return mDownlaodedMovieData;
        } catch (IOException e){
            Log.e(TAG, ERROR_OCCURRED + e.getStackTrace());
            return null;
        }
    }

    @Override
    public void deliverResult(List<Movie> data) {
        if(isStarted()) {
            mMoviesLoadedListener.onMoviesLoaded(data);
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
        mMovieCriteriaSelection = null;
    }

}
