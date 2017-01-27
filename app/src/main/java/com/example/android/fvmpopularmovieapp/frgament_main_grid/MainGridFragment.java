package com.example.android.fvmpopularmovieapp.frgament_main_grid;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.example.android.fvmpopularmovieapp.FragmentBaseClass;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.SettingsActivity;
import com.example.android.fvmpopularmovieapp.data_fetch.FetchMoviesAsync;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract;
import com.example.android.fvmpopularmovieapp.dialogs.NoNetworkDialog;
import java.util.ArrayList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.IS_FAVOURITE_PARAM;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOST_POPULAR_PARAM;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_FAVORITES_LOADER_ID;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_POSTER_LOADER_ID;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MUST_IMPLEMENT_INTERFACE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.NETWORK_NOT_AVAILABLE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.OVERFLOW_MENU_QUERY_PARAM;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SAVED_MOVIES;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.SHOW_DIALOG;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.TOP_RATED_PARAM;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.CONTENT_URI;
import static com.example.android.fvmpopularmovieapp.utilities.UtilMethods.isWifiConnected;
import static com.example.android.fvmpopularmovieapp.utilities.UtilMethods.setGridColumnNumber;


public class MainGridFragment extends FragmentBaseClass implements LoaderManager.LoaderCallbacks<Cursor>,
        SharedPreferences.OnSharedPreferenceChangeListener, FetchMoviesAsync.MoviesLoadedListener {

    public interface LoadFirstMovieListener{
        void onFirstMovieAvailable(Movie movie);
    }
    private MainGridFragment.LoadFirstMovieListener mLoaderManager;

    private Context mContext;

    @Bind(R.id.recycler_grid_view)
    RecyclerView mRecyclerView;
    @Bind(R.id.empty_view)
    LinearLayout empty_view;
    @Bind(R.id.iv_empty)
    ImageView mEmptyImageView;
    @Bind(R.id.tv_empty)
    TextView mEmptyTextView;

    private GridLayoutManager mGridLayoutManager;
    private PosterGridRecyclerViewAdapter mPosterGridRecyclerViewAdapter;
    private String mMovieCriteriaSelection = MOST_POPULAR_PARAM;
    private ArrayList<Movie> mMovies;
    private SharedPreferences mSharedPreferences;
    private boolean mShowNoNetworkDialog = true;
    private boolean mIsFavorite = false;
    private boolean mHideDialogs;
    private boolean mIsFragmentAvailable;
    private boolean mLoaderAlreadyCalled;
    private Menu mMenu;
    private NoNetworkDialog mNoNetworkDialog;

    public static final String[] MOVIE_DATA_PROJECTION = {
            MovieDataContract.Contract.COLUMN_MOVIE_POSTER_PATH,
            MovieDataContract.Contract.COLUMN_MOVIE_TITLE,
            MovieDataContract.Contract.COLUMN_MOVIE_ID,
            MovieDataContract.Contract.COLUMN_MOVIE_BACKDROP_PATH,
            MovieDataContract.Contract.COLUMN_MOVIE_OVERVIEW,
            MovieDataContract.Contract.COLUMN_MOVIE_RELEASE_DATE,
            MovieDataContract.Contract.COLUMN_MOVIE_VOTE_AVERAGE
    };

    public MainGridFragment() {
    }

    public static MainGridFragment newInstance() {
        Bundle args = new Bundle();
        MainGridFragment fragment = new MainGridFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
        try {
            mLoaderManager = (MainGridFragment.LoadFirstMovieListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() + MUST_IMPLEMENT_INTERFACE);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

        this.mMovies = new ArrayList<>();
        this.mGridLayoutManager = new GridLayoutManager(mContext, setGridColumnNumber(getActivity()));

        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey(SAVED_MOVIES))
                mMovies.addAll(savedInstanceState.<Movie>getParcelableArrayList(SAVED_MOVIES));

            if (savedInstanceState.containsKey(IS_FAVOURITE_PARAM))
                mIsFavorite = savedInstanceState.getBoolean(IS_FAVOURITE_PARAM);

            if(savedInstanceState.containsKey(SHOW_DIALOG))
                mShowNoNetworkDialog = savedInstanceState.getBoolean(SHOW_DIALOG);

            if (savedInstanceState.containsKey(OVERFLOW_MENU_QUERY_PARAM))
                this.mMovieCriteriaSelection = savedInstanceState.getString(OVERFLOW_MENU_QUERY_PARAM);

        } else {
            this.mMovieCriteriaSelection = MOST_POPULAR_PARAM;
            mShowNoNetworkDialog = true;
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        mLoaderAlreadyCalled = false;
        if (!mIsFavorite) {
            if (mMovies.size() == 0) {
                initOrRestartPosterLoader(true);
            } else
                mPosterGridRecyclerViewAdapter.setData(mMovies);
        } else
            loadFavorites();
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.grid_fragment, container, false);
        ButterKnife.bind(this, v);
        mRecyclerView.setHasFixedSize(true);
        mPosterGridRecyclerViewAdapter = new PosterGridRecyclerViewAdapter(
                (PosterGridRecyclerViewAdapter.GridItemClickListener) getActivity(),
                getActivity(),
                mLoaderManager);
        mRecyclerView.setLayoutManager(mGridLayoutManager);
        mRecyclerView.setAdapter(mPosterGridRecyclerViewAdapter);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        if(!mLoaderAlreadyCalled) {
            if (mIsFavorite)
                loadFavorites();
            else
                initOrRestartPosterLoader(false);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mIsFragmentAvailable = false;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(SAVED_MOVIES, mMovies);
        outState.putString(OVERFLOW_MENU_QUERY_PARAM, mMovieCriteriaSelection);
        outState.putBoolean(IS_FAVOURITE_PARAM, mIsFavorite);
        outState.putBoolean(SHOW_DIALOG, mShowNoNetworkDialog);
    }

    private void initOrRestartPosterLoader(boolean init) {
        if (isWifiConnected(getActivity())) {
            getLoaderManager().destroyLoader(MOVIE_FAVORITES_LOADER_ID);
            if(!mLoaderAlreadyCalled) {
                mLoaderAlreadyCalled = true;
                Log.d("asdasd", "initOrRestartPosterLoader");
                FetchMoviesRatedOrPopular fetchMoviesRatedOrPopular = new FetchMoviesRatedOrPopular(this);
                fetchMoviesRatedOrPopular.startLoader(init);
            }
        } else
            showNoNetworkDialog();
    }

    private void loadFavorites() {
        showProgressDialog();
        if (!mIsFavorite) {
            getLoaderManager().initLoader(MOVIE_FAVORITES_LOADER_ID, null, this);
        } else
            getLoaderManager().restartLoader(MOVIE_FAVORITES_LOADER_ID, null, this);
    }
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        if(id == MOVIE_FAVORITES_LOADER_ID) {
            return new CursorLoader(getActivity(),
                    CONTENT_URI,
                    MOVIE_DATA_PROJECTION,
                    null,
                    null,
                    null);
        } else
            return null;
    }
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        hideProgressDialog();
        mPosterGridRecyclerViewAdapter.swapCursor(data);
        setEmptyView(mIsFavorite, data.getCount());
    }
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
    }



    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.movie_criteria_menu, menu);
        mMenu = menu;
        if (mMovieCriteriaSelection.equals(MOST_POPULAR_PARAM) && !mIsFavorite)
            menu.findItem(R.id.most_popular).setChecked(true);
        else if (mMovieCriteriaSelection.equals(TOP_RATED_PARAM) && !mIsFavorite)
            menu.findItem(R.id.highest_rate).setChecked(true);
        else
            menu.findItem(R.id.favorites).setChecked(true);
    }
    private void setCheckedMenuItem(){
        if(mMenu != null) {
            if (mMovieCriteriaSelection.equals(getString(R.string.most_popular)) && !mIsFavorite)
                mMenu.findItem(R.id.most_popular).setChecked(true);
            else if (mMovieCriteriaSelection.equals(getString(R.string.top_rated)) && !mIsFavorite)
                mMenu.findItem(R.id.highest_rate).setChecked(true);
            else
                mMenu.findItem(R.id.favorites).setChecked(true);
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (!item.isChecked())
            item.setChecked(true);
        mMovies = new ArrayList<>();
        switch (item.getItemId()) {
            case R.id.most_popular:
                mMovieCriteriaSelection = MOST_POPULAR_PARAM;
                mIsFavorite = false;
                initOrRestartPosterLoader(false);
                break;
            case R.id.highest_rate:
                mMovieCriteriaSelection = TOP_RATED_PARAM;
                mIsFavorite = false;
                initOrRestartPosterLoader(false);
                break;
            case R.id.favorites:
                loadFavorites();
                mIsFavorite = true;
                break;
            case R.id.settings:
                startActivity(new Intent(getActivity(), SettingsActivity.class));
                break;
            default:
                break;
        }
        setPreferenceCriterium(mMovieCriteriaSelection, mIsFavorite);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        if(key.equals(getString(R.string.sort_criterium))) {
            mMovieCriteriaSelection = mSharedPreferences.getString(key, getString(R.string.most_popular));
            if (mMovieCriteriaSelection.equals(getString(R.string.favorites))) {
                mIsFavorite = true;
                loadFavorites();
            } else {
                mIsFavorite = false;
                initOrRestartPosterLoader(false);
            }
        }
        if(key.equals(getString(R.string.check_box_preference_key)))
            mHideDialogs = mSharedPreferences.getBoolean(key, getResources().getBoolean(R.bool.check_box_preference_default));
        setCheckedMenuItem();
    }
    private void setPreferenceCriterium(String criterium, boolean favorite){
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        if(favorite)
            editor.putString(getString(R.string.sort_criterium), getString(R.string.favorites)).apply();
        else
            editor.putString(getString(R.string.sort_criterium), criterium).apply();
    }

    @Override
    public void onMoviesLoaded(List<Movie> movies) {
        mMovies = new ArrayList<>();
        if (movies != null)
            mMovies.addAll(movies);
        mPosterGridRecyclerViewAdapter.setData(movies);
        setEmptyView(mIsFavorite, movies.size());
        mLoaderAlreadyCalled = false;
    }
    public class FetchMoviesRatedOrPopular implements LoaderManager.LoaderCallbacks<List<Movie>> {

        private FetchMoviesAsync.MoviesLoadedListener mMoviesLoadedListener;

        public FetchMoviesRatedOrPopular(FetchMoviesAsync.MoviesLoadedListener moviesLoadedListener) {
            this.mMoviesLoadedListener = moviesLoadedListener;
        }

        private void startLoader(boolean init) {
            if (init)
                getLoaderManager().initLoader(MOVIE_POSTER_LOADER_ID, null, this);
            else
                getLoaderManager().restartLoader(MOVIE_POSTER_LOADER_ID, null, this);
        }

        @Override
        public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
            showProgressDialog();
            return new FetchMoviesAsync(mContext, mMovieCriteriaSelection, mMoviesLoadedListener);
        }

        @Override
        public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
            hideProgressDialog();
            mMoviesLoadedListener.onMoviesLoaded(data);
        }

        @Override
        public void onLoaderReset(Loader<List<Movie>> loader) {

        }

    }


    public void checkIfFavorite(boolean refresh){
        mPosterGridRecyclerViewAdapter.workoutFavorite(refresh);
    }
    private void showNoNetworkDialog() {
        if (!mHideDialogs && mIsFragmentAvailable && mShowNoNetworkDialog) {
            mNoNetworkDialog = NoNetworkDialog.newInstance();
            mNoNetworkDialog.setCancelable(true);
            mNoNetworkDialog.show(getActivity().getFragmentManager(), NETWORK_NOT_AVAILABLE);
            mShowNoNetworkDialog = false;
        } else
            Toast.makeText(getActivity(), getString(R.string.no_network_message), Toast.LENGTH_SHORT).show();
        setEmptyView(mIsFavorite, mMovies.size());
    }
    private void setEmptyView(boolean favorites, int count){
        if(count == 0){
            mRecyclerView.setVisibility(View.GONE);
            empty_view.setVisibility(View.VISIBLE);
            setEmptyViews(favorites);
        } else {
            mRecyclerView.setVisibility(View.VISIBLE);
            empty_view.setVisibility(View.GONE);
        }
    }
    private void setEmptyViews(boolean favorites){
        if(favorites){
            mEmptyImageView.setImageResource(R.drawable.ic_star_grey_24dp);
            mEmptyTextView.setText(getString(R.string.no_favorite_available));
        } else {
            mEmptyImageView.setImageResource(R.drawable.ic_theaters_grey_24dp);
            mEmptyTextView.setText(getString(R.string.no_movie_available));
        }
    }

}