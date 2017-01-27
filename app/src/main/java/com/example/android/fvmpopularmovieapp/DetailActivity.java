package com.example.android.fvmpopularmovieapp;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.data_model.Trailer;
import com.example.android.fvmpopularmovieapp.dialogs.ShowReviewDialog;
import com.example.android.fvmpopularmovieapp.fragment_detail.DetailFragment;
import com.example.android.fvmpopularmovieapp.fragment_detail.ReviewsAdapter;
import com.example.android.fvmpopularmovieapp.fragment_detail.TrailersAdapter;
import com.example.android.fvmpopularmovieapp.frgament_main_grid.MainGridFragment;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.DETAIL_FRAGMENT_TAG;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SHOW_REVIEW_DIALOG;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SINGLE_MOVIE_DETAIL;


public class DetailActivity extends AppCompatActivity implements TrailersAdapter.TrailerClickListener,
        ReviewsAdapter.ReviewClickListener {

    private DetailFragment mDetailFragment;
    private Intent mIntent;
    private Movie mMovie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setBackgroundDrawable(null);
        setContentView(R.layout.activity_detail);

        mIntent = getIntent();
        if(mIntent != null && mIntent.hasExtra(SINGLE_MOVIE_DETAIL))
            mMovie = mIntent.getParcelableExtra(SINGLE_MOVIE_DETAIL);

        if(savedInstanceState == null) {
            mDetailFragment = DetailFragment.newInstance(mMovie);
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.detail_container, mDetailFragment, DETAIL_FRAGMENT_TAG)
                    .commit();
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

}
