package com.example.android.fvmpopularmovieapp.fragment_detail;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_model.Trailer;
import com.squareup.picasso.Picasso;
import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


public class TrailersAdapter extends RecyclerView.Adapter<TrailersAdapter.ViewHolder> {

    public interface TrailerClickListener{
        void onTrailerThumbnailClick(Trailer trailer);
    }

    private TrailerClickListener mTrailerClickListener;
    private List<Trailer> mTrailers;
    private Context mContext;

    public TrailersAdapter(List<Trailer> trailers, Context context, TrailerClickListener trailerClickListener) {
        this.mTrailers = trailers;
        this.mContext = context;
        this.mTrailerClickListener = trailerClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.detail_trailer_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        Picasso.with(mContext)
                .load(mTrailers.get(position).getTrailerThubmnailUrl())
                .config(Bitmap.Config.RGB_565)
                .resizeDimen(R.dimen.video_width, R.dimen.video_height)
                .noPlaceholder()
                .error(R.drawable.ic_report_problem_black_24dp)
                .centerCrop()
                .into(holder.iv_trailer_poster);
    }

    @Override
    public int getItemCount() {
        return mTrailers.size();
    }

    public void swapCursor(List<Trailer> trailers){
        mTrailers = trailers;
        notifyDataSetChanged();
    }

    public List<Trailer> getTrailers(){
        return mTrailers;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.iv_trailer_poster)
        ImageView iv_trailer_poster;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mTrailerClickListener.onTrailerThumbnailClick(mTrailers.get(position));
        }
    }
}
