package com.example.android.fvmpopularmovieapp.fragment_detail;

import android.app.DialogFragment;
import android.content.ContentValues;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Parcelable;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.fvmpopularmovieapp.FragmentBaseClass;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_fetch.FetchReviewsAsync;
import com.example.android.fvmpopularmovieapp.data_fetch.FetchTrailersAsync;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.data_model.Trailer;
import com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract;
import com.example.android.fvmpopularmovieapp.dialogs.NoNetworkDialog;
import com.example.android.fvmpopularmovieapp.dialogs.RemoveFavoriteMovieDialog;
import com.example.android.fvmpopularmovieapp.frgament_main_grid.MainGridFragment;
import com.example.android.fvmpopularmovieapp.utilities.UtilMethods;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_REVIEWS_LOADER_ID;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_TRAILERS_LOADER_ID;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.NETWORK_NOT_AVAILABLE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SAVED_DETAIL_MOVIE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SAVED_MOVIE_REVIEWS;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SAVED_MOVIE_TRAILERS;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SINGLE_MOVIE_DETAIL;


public class DetailFragment extends FragmentBaseClass implements View.OnClickListener, FetchTrailersAsync.TrailersLoadedListener,
        FetchReviewsAsync.ReviewsLoadedListener{

    private static final String SHOW_DIALOG = "show_dialog_detail_frag";

    private Movie mMovie;
    private LinearLayoutManager mLinearLayoutManager;
    private DetailRecyclerViewAdapter mAdapter;
    private LinearLayoutManager mTrailersLinearLayoutManager;
    private TrailersAdapter mTrailersAdapter;
    private LinearLayoutManager mReviewsLinearLayoutManager;
    private ReviewsAdapter mReviewsAdapter;
    private List<Trailer> mTrailers;
    private List<Review>  mReviews;
    private boolean mIsFavorite;
    private boolean mIsNetworkAvailable = true;
    private boolean mHideDialogs;
    private boolean mLookForReviews = true;
    private boolean mShowNoNetworkDialog = true;

    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.rv_movie_detail)
    RecyclerView mRecyclerView;
    @Bind(R.id.rv_movie_trailers)
    RecyclerView mTrailersRecyclerView;
    @Bind(R.id.rv_movie_reviews)
    RecyclerView mReviewsRecyclerView;
    @Bind(R.id.trailers_title)
    TextView mTrailersTitle;
    @Bind(R.id.reviews_title)
    TextView mReviewsTitle;

    public DetailFragment(){

    }

    public static DetailFragment newInstance(Movie movie) {

        Bundle args = new Bundle();
        if(movie != null)
            args.putParcelable(SINGLE_MOVIE_DETAIL, movie);

        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle bundle = getArguments();
        setHasOptionsMenu(true);
        getPreferencies();

        mTrailers = new LinkedList<>();
        mReviews  = new LinkedList<>();
        mLinearLayoutManager         = new LinearLayoutManager(getActivity());
        mTrailersLinearLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        mReviewsLinearLayoutManager  = new LinearLayoutManager(getActivity());

        if(savedInstanceState != null){
            if(savedInstanceState.containsKey(SAVED_DETAIL_MOVIE))
                mMovie = savedInstanceState.getParcelable(SAVED_DETAIL_MOVIE);
            if(savedInstanceState.containsKey(SAVED_MOVIE_TRAILERS))
                mTrailers = savedInstanceState.getParcelableArrayList(SAVED_MOVIE_TRAILERS);
            if(savedInstanceState.containsKey(SAVED_MOVIE_REVIEWS))
                mReviews = savedInstanceState.getParcelableArrayList(SAVED_MOVIE_REVIEWS);
            else
                mLookForReviews = false;
            if(savedInstanceState.containsKey(SHOW_DIALOG))
                mShowNoNetworkDialog = savedInstanceState.getBoolean(SHOW_DIALOG);
        } else {
            mMovie = bundle.getParcelable(SINGLE_MOVIE_DETAIL);
            mShowNoNetworkDialog = true;
        }
    }
    private void getPreferencies() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mHideDialogs = sharedPreferences.getBoolean(getString(R.string.check_box_preference_key), false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        mIsNetworkAvailable = isNetworkAvailable();
        if(mIsNetworkAvailable) {
            if(mTrailers.isEmpty()) {
                new FetchTrailers(this).startTrailersLoader();
            } else
                mTrailersAdapter.swapCursor(mTrailers);
            if (mReviews.isEmpty() && mLookForReviews) {
                new FetchReviews(this).startReviewsLoader();
            } else
                mReviewsAdapter.swapCursor(mReviews);
        } else
            showNoNetworkDialog();
        super.onActivityCreated(savedInstanceState);
    }
    private boolean isNetworkAvailable() {
        return UtilMethods.isWifiConnected(getActivity());
    }
    private void showNoNetworkDialog() {
        if(!mHideDialogs && mShowNoNetworkDialog) {
            DialogFragment dialog = NoNetworkDialog.newInstance();
            dialog.setCancelable(false);
            dialog.show(getActivity().getFragmentManager(), NETWORK_NOT_AVAILABLE);
            mShowNoNetworkDialog = false;
        } else
            Toast.makeText(getActivity(), getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.detail_fragment, container, false);
        ButterKnife.bind(this, v);

        if(mMovie != null) {
            initAdapters();
            setViewProperties();
            checkIfFavorite();
            setToolbar();
        }

        return v;
    }
    private void initAdapters(){
        mAdapter         = new DetailRecyclerViewAdapter(getActivity(), mMovie);
        mTrailersAdapter = new TrailersAdapter(mTrailers, getActivity(), (TrailersAdapter.TrailerClickListener) getActivity());
        mReviewsAdapter  = new ReviewsAdapter(mReviews, getActivity(), (ReviewsAdapter.ReviewClickListener) getActivity());
    }
    private void setViewProperties(){

        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mAdapter);

        mTrailersRecyclerView.setLayoutManager(mTrailersLinearLayoutManager);
        mTrailersRecyclerView.setAdapter(mTrailersAdapter);

        mReviewsRecyclerView.setLayoutManager(mReviewsLinearLayoutManager);
        mReviewsRecyclerView.setAdapter(mReviewsAdapter);

        Typeface typeface = UtilMethods.getTypeface(getActivity(), 1);
        mTrailersTitle.setTypeface(typeface);
        mReviewsTitle.setTypeface(typeface);

        fab.setOnClickListener(this);

        if(mTrailers.size() == 0)
            mTrailersTitle.setVisibility(View.GONE);
        if(mReviews.size() == 0)
            mReviewsTitle.setVisibility(View.GONE);
    }
    private void setToolbar(){
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle(mMovie.getOriginalTitle());
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if(mMovie != null)
            outState.putParcelable(SAVED_DETAIL_MOVIE, mMovie);
        List<Trailer> trailers = mTrailersAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty())
            outState.putParcelableArrayList(SAVED_MOVIE_TRAILERS, (ArrayList<? extends Parcelable>) trailers);
        List<Review> reviews = mReviewsAdapter.getReviews();
        if (reviews != null && !reviews.isEmpty())
            outState.putParcelableArrayList(SAVED_MOVIE_REVIEWS, (ArrayList<? extends Parcelable>) reviews);
        outState.putBoolean(SHOW_DIALOG, mShowNoNetworkDialog);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_detail_menu, menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.share:
                if(isNetworkAvailable()) {
                    Intent shareIntent = new Intent(Intent.ACTION_SEND);
                    if (mTrailersAdapter.getItemCount() != 0)
                        shareIntent.putExtra(Intent.EXTRA_TEXT,
                                String.valueOf(mTrailersAdapter.getTrailers().get(0).getTrailerlUrl()));
                    shareIntent.putExtra(Intent.EXTRA_SUBJECT, mMovie.getOriginalTitle());
                    shareIntent.setType("text/plain");
                    startActivity(Intent.createChooser(shareIntent, getString(R.string.share_trailer)));
                } else {
                    if(!mHideDialogs)
                        showNoNetworkDialog();
                    else
                        Toast.makeText(getActivity(), getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
                }
                break;
            default:
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void checkIfFavorite(){
        new AsyncTask<String, Void, Boolean>(){
            String[] MOVIE_DATA_PROJECTION = {
                    MovieDataContract.Contract.COLUMN_MOVIE_ID,
            };
            @Override
            protected Boolean doInBackground(String... params) {
                Cursor cursor = getContext().getContentResolver().query(MovieDataContract.Contract.CONTENT_URI,
                        MOVIE_DATA_PROJECTION,
                        MovieDataContract.Contract.COLUMN_MOVIE_ID + " = " + params[0],
                        null,
                        null);
                return cursor.getCount() == 1;
            }
            @Override
            protected void onPostExecute(Boolean bool) {
                mIsFavorite = bool;
                setFabImageResource();
            }
        }.execute(mMovie.getId());
    }


    @Override
    public void onTrailersLoaded(List<Trailer> trailers) {
        mTrailersAdapter.swapCursor(trailers);
        hideProgressDialog();
        if(trailers.size() == 0)
            mTrailersTitle.setVisibility(View.GONE);
    }

    @Override
    public void onReviewsLoaded(List<Review> reviews) {
        mReviewsAdapter.swapCursor(reviews);
        hideProgressDialog();
        if(reviews.size() == 0)
            mReviewsTitle.setVisibility(View.GONE);
    }


    private class FetchTrailers implements LoaderManager.LoaderCallbacks<List<Trailer>>{

        private FetchTrailersAsync.TrailersLoadedListener mTrailersLoadedListener;

        public FetchTrailers(FetchTrailersAsync.TrailersLoadedListener trailersLoadedListener) {
            this.mTrailersLoadedListener = trailersLoadedListener;
        }

        public void startTrailersLoader(){
            getLoaderManager().initLoader(MOVIE_TRAILERS_LOADER_ID, null, this);
        }

        @Override
        public Loader<List<Trailer>> onCreateLoader(int id, Bundle args) {
            overrideShowProgressDialog();
            return new FetchTrailersAsync(getActivity(), mMovie.getId(), mTrailersLoadedListener);
        }

        @Override
        public void onLoadFinished(Loader<List<Trailer>> loader, List<Trailer> data) {
            mTrailersLoadedListener.onTrailersLoaded(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Trailer>> loader) {

        }
    }

    private class FetchReviews implements LoaderManager.LoaderCallbacks<List<Review>>{

        private FetchReviewsAsync.ReviewsLoadedListener mReviewsLoadedListener;

        public FetchReviews(FetchReviewsAsync.ReviewsLoadedListener reviewsLoadedListener) {
            this.mReviewsLoadedListener = reviewsLoadedListener;
        }

        public void startReviewsLoader(){
            getLoaderManager().initLoader(MOVIE_REVIEWS_LOADER_ID, null, this);
        }

        @Override
        public Loader<List<Review>> onCreateLoader(int id, Bundle args) {
            overrideShowProgressDialog();
            return new FetchReviewsAsync(getActivity(), mMovie.getId(), mReviewsLoadedListener);
        }

        @Override
        public void onLoadFinished(Loader<List<Review>> loader, List<Review> data) {
            mReviewsLoadedListener.onReviewsLoaded(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Review>> loader) {

        }
    }

    private void overrideShowProgressDialog(){
        showProgressDialog();
        mProgressDialog.setCancelable(true);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        hideProgressDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.fab:
                MainGridFragment mainGridFragment =
                        (MainGridFragment) getFragmentManager().findFragmentById(R.id.grid_fragment);
                if(mainGridFragment != null)
                    mainGridFragment.checkIfFavorite(false);
                if(!mIsFavorite) {
                    getActivity().getContentResolver().insert(MovieDataContract.Contract.CONTENT_URI, getContentValues());
                    mIsFavorite = true;
                    Toast.makeText(getActivity(), getString(R.string.added_to_favorites), Toast.LENGTH_SHORT).show();
                    setFabImageResource();
                } else {
                    getActivity().getContentResolver().delete(MovieDataContract.Contract.CONTENT_URI,
                            MovieDataContract.Contract.COLUMN_MOVIE_ID + " = " + mMovie.getId(),
                            null);
                    mIsFavorite = false;
                    setFabImageResource();
                    showBookmarkDialog();
                }
            break;
            default:
                break;
        }
    }
    public ContentValues getContentValues(){
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_TITLE, mMovie.getOriginalTitle());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_ID, mMovie.getId());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_POSTER_PATH, mMovie.getPosterPath());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_OVERVIEW, mMovie.getOverView());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_VOTE_AVERAGE, mMovie.getVoteAverage());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_RELEASE_DATE, mMovie.getReleaseDate());
        contentValues.put(MovieDataContract.Contract.COLUMN_MOVIE_BACKDROP_PATH, mMovie.getBackDropPath());
        return contentValues;
    }
    private void showBookmarkDialog() {
        if(!mHideDialogs) {
            DialogFragment dialogFragment = new RemoveFavoriteMovieDialog().newInstance();
            dialogFragment.show(getActivity().getFragmentManager(), "RemoveFavoriteMovieDialog");
        } else
            Toast.makeText(getActivity(), getString(R.string.removed_from_favorites), Toast.LENGTH_SHORT).show();
    }
    private void setFabImageResource(){
        if(mIsFavorite)
            fab.setImageResource(R.drawable.ic_star_yellow_24dp);
        else
            fab.setImageResource(R.drawable.ic_star_border_yellow_24dp);
    }
}
