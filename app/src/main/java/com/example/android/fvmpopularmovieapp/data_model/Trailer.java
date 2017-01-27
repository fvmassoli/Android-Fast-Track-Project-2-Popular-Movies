package com.example.android.fvmpopularmovieapp.data_model;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;
import com.google.gson.annotations.SerializedName;

import static com.example.android.fvmpopularmovieapp.utilities.Constants.MOVIE_ID;


public class Trailer implements Parcelable{

    @SerializedName(MOVIE_ID)
    private String mId;
    @SerializedName("key")
    private String mKey;
    @SerializedName("name")
    private String mName;
    @SerializedName("site")
    private String mSite;
    @SerializedName("size")
    private String mSize;

    public Trailer(){

    }

    protected Trailer(Parcel in) {
        mId = in.readString();
        mKey = in.readString();
        mName = in.readString();
        mSite = in.readString();
        mSize = in.readString();
    }

    public static final Creator<Trailer> CREATOR = new Creator<Trailer>() {
        @Override
        public Trailer createFromParcel(Parcel in) {
            return new Trailer(in);
        }

        @Override
        public Trailer[] newArray(int size) {
            return new Trailer[size];
        }
    };

    public Uri getTrailerThubmnailUrl(){
        return Uri.parse(String.format("http://img.youtube.com/vi/%1$s/0.jpg", getKey()));
    }
    public Uri getTrailerlUrl(){
        return Uri.parse(String.format("http://www.youtube.com/watch?v=%1$s", getKey()));
    }

    public String getId() {
        return mId;
    }

    public String getKey() {
        return mKey;
    }

    public String getName() {
        return mName;
    }

    public String getSite() {
        return mSite;
    }

    public String getSize() {
        return mSize;
    }

    public void setId(String mId) {
        this.mId = mId;
    }

    public void setKey(String mKey) {
        this.mKey = mKey;
    }

    public void setName(String mName) {
        this.mName = mName;
    }

    public void setSite(String mSite) {
        this.mSite = mSite;
    }

    public void setSize(String mSize) {
        this.mSize = mSize;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(mId);
        dest.writeString(mKey);
        dest.writeString(mName);
        dest.writeString(mSite);
        dest.writeString(mSize);
    }
}
