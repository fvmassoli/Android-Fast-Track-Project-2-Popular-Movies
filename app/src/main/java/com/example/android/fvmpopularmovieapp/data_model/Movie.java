package com.example.android.fvmpopularmovieapp.data_model;

import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.BACK_DROP_PATH;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.BASE_BACK_DROP_URL;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.BASE_POSTER_URL;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_ID;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.ORIGINAL_TITLE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.OVERVIEW;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.POSTER_PATH;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.RELEASE_DATE;
import static com.example.android.fvmpopularmovieapp.utilities.Constants.VOTE_AVERAGE;


public class Movie implements Parcelable{

    @SerializedName(ORIGINAL_TITLE)
    private String mOriginalTitle;
    @SerializedName(RELEASE_DATE)
    private String mReleaseDate;
    @SerializedName(POSTER_PATH)
    private String mPosterPath;
    @SerializedName(BACK_DROP_PATH)
    private String mBackDropPath;
    @SerializedName(VOTE_AVERAGE)
    private String mVoteAverage;
    @SerializedName(OVERVIEW)
    private String mOverView;
    @SerializedName(MOVIE_ID)
    private String mId;

    public Movie() {
    }

    public void setPosterPath(String mPosterPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_POSTER_URL);
        sb.append(mPosterPath);
        this.mPosterPath = sb.toString();
    }
    public void setPosterFullPath(String posterPath){
        this.mPosterPath = posterPath;
    }

    public void setBackDropPath(String mBackDropPath) {
        StringBuilder sb = new StringBuilder();
        sb.append(BASE_BACK_DROP_URL);
        sb.append(mBackDropPath);
        this.mBackDropPath = sb.toString();
    }
    public void setBackDropFullPath(String backDropPath){ this.mBackDropPath = backDropPath; }

    public void setOriginalTitle(String mOriginalTitle) {
        this.mOriginalTitle = mOriginalTitle;
    }

    public void setReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public void setVoteAverage(String mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public void setOverView(String mOverView) {
        this.mOverView = mOverView;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public String getOriginalTitle() {
        return mOriginalTitle;
    }

    public String getReleaseDate() {
        return mReleaseDate;
    }

    public String getPosterPath() {
        return mPosterPath;
    }

    public String getBackDropPath() {
        return mBackDropPath;
    }

    public String getVoteAverage() {
        return mVoteAverage;
    }

    public String getOverView() {
        return mOverView;
    }

    public String getId() {
        return mId;
    }


    protected Movie(Parcel in) {
        mOriginalTitle = in.readString();
        mReleaseDate   = in.readString();
        mPosterPath    = in.readString();
        mBackDropPath  = in.readString();
        mVoteAverage   = in.readString();
        mOverView      = in.readString();
        mId            = in.readString();
    }

    public static final Creator<Movie> CREATOR = new Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel in) {
            return new Movie(in);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mOriginalTitle);
        dest.writeString(mReleaseDate);
        dest.writeString(mPosterPath);
        dest.writeString(mBackDropPath);
        dest.writeString(mVoteAverage);
        dest.writeString(mOverView);
        dest.writeString(mId);
    }
}
