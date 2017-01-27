package com.example.android.fvmpopularmovieapp.fragment_detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_model.Movie;
import com.example.android.fvmpopularmovieapp.utilities.UtilMethods;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


public class DetailRecyclerViewAdapter extends RecyclerView.Adapter<DetailRecyclerViewAdapter.MovieDetailViewHolder> {

    private Movie mMovie;
    private Context mContext;

    public DetailRecyclerViewAdapter(Context context, Movie movie) {
        this.mMovie = movie;
        this.mContext = context;
    }

    @Override
    public MovieDetailViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.detail_movie_item, parent, false);
        MovieDetailViewHolder movieDetailViewHolder = new MovieDetailViewHolder(v);

        return movieDetailViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieDetailViewHolder holder, int position) {

        setTextViewTypeFace(holder);
        setMovieDetialPageContent(holder);
        drawRatingStars(holder);
    }
    private void setMovieDetialPageContent(MovieDetailViewHolder holder){

        Picasso.with(mContext)
                .load(mMovie.getBackDropPath())
                .config(Bitmap.Config.RGB_565)
                .noPlaceholder()
                .error(R.drawable.ic_report_problem_black_24dp)
                .into(holder.big_img);

        Picasso.with(mContext)
                .load(mMovie.getPosterPath())
                .config(Bitmap.Config.RGB_565)
                .noPlaceholder()
                .error(R.drawable.ic_report_problem_black_24dp)
                .into(holder.iv_movie_poster_detail);

        holder.movie_title.setText(mMovie.getOriginalTitle());
        holder.release_date.setText(mMovie.getReleaseDate());
        holder.rating.setText(mMovie.getVoteAverage());
        holder.synopsis.setText(mMovie.getOverView());
        holder.release_date_tl.setText(mContext.getString(R.string.release_date));
        holder.rating_tl.setText(mContext.getString(R.string.rate));
        holder.synopsis_tl.setText(mContext.getString(R.string.overview));
    }
    private void setTextViewTypeFace(MovieDetailViewHolder holder){

        holder.movie_title.setTypeface(UtilMethods.getTypeface(mContext, 1));

        Typeface typeface  = UtilMethods.getTypeface(mContext, 0);
        holder.release_date.setTypeface(typeface);
        holder.rating.setTypeface(typeface);
        holder.synopsis.setTypeface(typeface);
        holder.release_date_tl.setTypeface(typeface);
        holder.rating_tl.setTypeface(typeface);
        holder.synopsis_tl.setTypeface(typeface);
    }
    private void drawRatingStars(MovieDetailViewHolder holder){
        double rate = Double.parseDouble(mMovie.getVoteAverage()) + 0.5;
        int rateFl = (int) Math.floor(rate);
        int rateMod = rateFl % 2;
        int rateHalf = rateFl / 2;
        int i;
        for(i=0; i<rateHalf; i++)
            holder.ratingStarsList.get(i).setImageResource(R.drawable.ic_star_yellow_24dp);
        if(rateMod != 0)
            holder.ratingStarsList.get(i++).setImageResource(R.drawable.ic_star_half_yellow_24dp);
        while(i<holder.ratingStarsList.size()){
            holder.ratingStarsList.get(i).setImageResource(R.drawable.ic_star_grey_24dp);
            i++;
        }

    }

    @Override
    public int getItemCount() {
        return mContext.getResources().getInteger(R.integer.nb_item_top_recycler_view);
    }


    public class MovieDetailViewHolder extends RecyclerView.ViewHolder{

        @Bind(R.id.big_img)
        ImageView big_img;
        @Bind(R.id.iv_movie_poster_detail)
        ImageView iv_movie_poster_detail;
        @Bind(R.id.movie_title)
        TextView movie_title;
        @Bind({R.id.star_1, R.id.star_2, R.id.star_3, R.id.star_4, R.id.star_5})
        List<ImageView> ratingStarsList;
        @Bind(R.id.release_date)
        TextView release_date;
        @Bind(R.id.rating)
        TextView rating;
        @Bind(R.id.synopsis)
        TextView synopsis;
        @Bind(R.id.release_date_tl)
        TextView release_date_tl;
        @Bind(R.id.rating_tl)
        TextView rating_tl;
        @Bind(R.id.synopsis_tl)
        TextView synopsis_tl;

        public MovieDetailViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

}