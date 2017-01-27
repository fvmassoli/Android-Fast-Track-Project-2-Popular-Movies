package com.example.android.fvmpopularmovieapp.frgament_main_grid;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract;
import com.squareup.picasso.Picasso;
import java.util.LinkedList;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;

import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_BACKDROP_PATH;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_ID;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_OVERVIEW;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_POSTER_PATH;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_RELEASE_DATE;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_TITLE;
import static com.example.android.fvmpopularmovieapp.data_persistance.MovieDataContract.Contract.COLUMN_MOVIE_VOTE_AVERAGE;


public class PosterGridRecyclerViewAdapter extends RecyclerView.Adapter<PosterGridRecyclerViewAdapter.ViewHolder> {

    public interface GridItemClickListener{
        void onGridItemClick(Movie movie);
    }

    private GridItemClickListener mGridItemClickListener;
    private MainGridFragment.LoadFirstMovieListener mLoadFirstMovieListener;
    private Context mContext;
    private List<Movie> mData = null;
    private boolean mRefresh = true;

    public PosterGridRecyclerViewAdapter(GridItemClickListener gridItemClickListener, Context context,
                                         MainGridFragment.LoadFirstMovieListener loadFirstMovieListener){
        this.mGridItemClickListener  = gridItemClickListener;
        this.mContext                = context;
        this.mLoadFirstMovieListener = loadFirstMovieListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.grid_poster_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if(position == 0 && mLoadFirstMovieListener != null && mRefresh)
            mLoadFirstMovieListener.onFirstMovieAvailable(mData.get(position));

        if(mData.get(position).getPosterPath() != null)
            Picasso.with(mContext)
                    .load(Uri.parse(mData.get(position).getPosterPath()))
                    .config(Bitmap.Config.RGB_565)
                    .noPlaceholder()
                    .error(R.drawable.ic_report_problem_black_24dp)
                    .into(holder.moviePoster);

        setFavorites(position, holder);
    }
    private void setFavorites(final int pos, final ViewHolder holder){
        new AsyncTask<String, Void, Boolean>(){

            String[] MOVIE_DATA_PROJECTION = {
                    MovieDataContract.Contract.COLUMN_MOVIE_ID,
            };
            @Override
            protected Boolean doInBackground(String... params) {
                Cursor cursor = mContext.getContentResolver().query(MovieDataContract.Contract.CONTENT_URI,
                        MOVIE_DATA_PROJECTION,
                        MovieDataContract.Contract.COLUMN_MOVIE_ID + " = " + params[0],
                        null,
                        null);
                return cursor.getCount() == 1;
            }
            @Override
            protected void onPostExecute(Boolean bool) {
                if(bool)
                    holder.bookmark.setVisibility(View.VISIBLE);
                else
                    holder.bookmark.setVisibility(View.GONE);
            }

        }.execute(mData.get(pos).getId());
    }
    public void workoutFavorite(boolean refresh){
        mRefresh = refresh;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return mData != null ? mData.size() : 0;
    }

    public void setData(List<Movie> data){
        mData = data;
        mRefresh = true;
        notifyDataSetChanged();
    }

    public List<Movie> getData(){
        return mData;
    }

    public void swapCursor(Cursor cursor){
        Movie movie;
        mData = new LinkedList<>();
        while (cursor.moveToNext()){
            movie = new Movie();
            movie.setPosterFullPath(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_POSTER_PATH)));
            movie.setOriginalTitle(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_TITLE)));
            movie.setId(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_ID)));
            movie.setOverView(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_OVERVIEW)));
            movie.setVoteAverage(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_VOTE_AVERAGE)));
            movie.setReleaseDate(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_RELEASE_DATE)));
            movie.setBackDropFullPath(cursor.getString(cursor.getColumnIndex(COLUMN_MOVIE_BACKDROP_PATH)));
            mData.add(movie);
        }
        mRefresh = true;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @Bind(R.id.iv_movie_poster)
        ImageView moviePoster;
        @Bind(R.id.iv_bookmark)
        ImageView bookmark;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mGridItemClickListener.onGridItemClick(mData.get(position));
        }
    }
}
