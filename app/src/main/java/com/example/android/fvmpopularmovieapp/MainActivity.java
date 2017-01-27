package com.example.android.fvmpopularmovieapp;


import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.data_model.Trailer;
import com.example.android.fvmpopularmovieapp.dialogs.ShowReviewDialog;
import com.example.android.fvmpopularmovieapp.fragment_detail.DetailFragment;
import com.example.android.fvmpopularmovieapp.fragment_detail.ReviewsAdapter;
import com.example.android.fvmpopularmovieapp.fragment_detail.TrailersAdapter;
import com.example.android.fvmpopularmovieapp.frgament_main_grid.MainGridFragment;
import com.example.android.fvmpopularmovieapp.frgament_main_grid.PosterGridRecyclerViewAdapter;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.DETAIL_FRAGMENT_TAG;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SAVED_DETAIL_MOVIE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SHOW_REVIEW_DIALOG;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SINGLE_MOVIE_DETAIL;

public class MainActivity extends AppCompatActivity implements PosterGridRecyclerViewAdapter.GridItemClickListener,
        TrailersAdapter.TrailerClickListener, ReviewsAdapter.ReviewClickListener, MainGridFragment.LoadFirstMovieListener {

    public boolean mIsTwoPaneMode;
    private DetailFragment mDetailFragment;
    private Movie mMovie = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_main);

        mIsTwoPaneMode = (findViewById(R.id.detail_container) != null) ? true : false;

        if(savedInstanceState != null && savedInstanceState.containsKey(SAVED_DETAIL_MOVIE))
            mMovie = savedInstanceState.getParcelable(SAVED_DETAIL_MOVIE);

        if(mIsTwoPaneMode && mMovie != null) {
            mDetailFragment = DetailFragment.newInstance(null);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, mDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }

    }

    @Override
    public void onGridItemClick(Movie movie) {
        if(mIsTwoPaneMode){
            mDetailFragment = DetailFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, mDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        } else {
            Intent intent = new Intent(this, DetailActivity.class)
                    .putExtra(SINGLE_MOVIE_DETAIL, movie);
            startActivity(intent);
        }
    }

    @Override
    public void onTrailerThumbnailClick(Trailer trailer) {
        Intent executeTrailer = new Intent(Intent.ACTION_VIEW, trailer.getTrailerlUrl());
        startActivity(executeTrailer);
    }

    @Override
    public void onReviewClick(Review review) {
        DialogFragment dialog = ShowReviewDialog.newInstance(review);
        dialog.setCancelable(false);
        dialog.show(getFragmentManager(), SHOW_REVIEW_DIALOG);
    }

    @Override
    public void onFirstMovieAvailable(Movie movie) {
        if(mIsTwoPaneMode){
            mDetailFragment = DetailFragment.newInstance(movie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_container, mDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
        }
    }
}
