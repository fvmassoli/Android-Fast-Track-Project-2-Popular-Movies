package com.example.android.fvmpopularmovieapp.fragment_detail;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.android.fvmpopularmovieapp.R;
import com.example.android.fvmpopularmovieapp.data_model.Review;
import com.example.android.fvmpopularmovieapp.utilities.UtilMethods;

import java.util.List;
import butterknife.Bind;
import butterknife.ButterKnife;


public class ReviewsAdapter extends RecyclerView.Adapter<ReviewsAdapter.ViewHolder>{

    public interface ReviewClickListener{
        void onReviewClick(Review review);
    }

    private ReviewClickListener mReviewClickListener;
    private List<Review> mReviews;
    private Context mContext;

    public ReviewsAdapter(List<Review> reviews, Context context, ReviewClickListener reviewClickListener) {
        this.mReviews = reviews;
        this.mContext = context;
        this.mReviewClickListener = reviewClickListener;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);
        View v = layoutInflater.inflate(R.layout.detail_review_card, parent, false);
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.tv_review_author.setText(mReviews.get(position).getAuthor());
        holder.tv_review_content.setText(mReviews.get(position).getContent().replace("\n\n", " ").replace("\n", " "));
        setTextViewTypeFace(holder);
    }
    private void setTextViewTypeFace(ViewHolder holder){
        holder.tv_review_author.setTypeface(UtilMethods.getTypeface(mContext, 1));
        holder.tv_review_content.setTypeface(UtilMethods.getTypeface(mContext, 0));
    }

    @Override
    public int getItemCount() {
        return mReviews.size();
    }

    public void swapCursor(List<Review> reviews){
        mReviews = reviews;
        notifyDataSetChanged();
    }

    public List<Review> getReviews(){
        return mReviews;
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @Bind(R.id.tv_review_author)
        TextView tv_review_author;
        @Bind(R.id.tv_review_content)
        TextView tv_review_content;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = getAdapterPosition();
            mReviewClickListener.onReviewClick(mReviews.get(position));
        }
    }
}
