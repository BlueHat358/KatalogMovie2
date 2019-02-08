package com.example.katalogmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult implements Parcelable {
    @SerializedName("id")
    private Long mId;
    @SerializedName("overview")
    private String mOverview;
    @SerializedName("poster_path")
    private String mPosterPath;
    @SerializedName("release_date")
    private String mReleaseDate;
    @SerializedName("title")
    private String mTitle;
    @SerializedName("vote_average")
    private Double mVoteAverage;
    @SerializedName("vote_count")
    private String mVoteCount;

    public Long getmId() {
        return mId;
    }

    public void setmId(Long mId) {
        this.mId = mId;
    }

    public String getmOverview() {
        return mOverview;
    }

    public void setmOverview(String mOverview) {
        this.mOverview = mOverview;
    }

    public String getmPosterPath() {
        return mPosterPath;
    }

    public void setmPosterPath(String mPosterPath) {
        this.mPosterPath = mPosterPath;
    }

    public String getmReleaseDate() {
        return mReleaseDate;
    }

    public void setmReleaseDate(String mReleaseDate) {
        this.mReleaseDate = mReleaseDate;
    }

    public String getmTitle() {
        return mTitle;
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public Double getmVoteAverage() {
        return mVoteAverage;
    }

    public void setmVoteAverage(Double mVoteAverage) {
        this.mVoteAverage = mVoteAverage;
    }

    public String getmVoteCount() {
        return mVoteCount;
    }

    public void setmVoteCount(String mVoteCount) {
        this.mVoteCount = mVoteCount;
    }

    public MovieResult(Long mId, String mOverview, String mPosterPath,
                       String mReleaseDate, String mTitle, Double mVoteAverage, String mVoteCount) {
        this.mId = mId;
        this.mOverview = mOverview;
        this.mPosterPath = mPosterPath;
        this.mReleaseDate = mReleaseDate;
        this.mTitle = mTitle;
        this.mVoteAverage = mVoteAverage;
        this.mVoteCount = mVoteCount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.mId);
        dest.writeString(this.mOverview);
        dest.writeString(this.mPosterPath);
        dest.writeString(this.mReleaseDate);
        dest.writeString(this.mTitle);
        dest.writeValue(this.mVoteAverage);
        dest.writeString(this.mVoteCount);
    }

    public MovieResult(){}

    protected MovieResult(Parcel in) {
        this.mId = (Long) in.readValue(Long.class.getClassLoader());
        this.mOverview = in.readString();
        this.mPosterPath = in.readString();
        this.mReleaseDate = in.readString();
        this.mTitle = in.readString();
        this.mVoteAverage = (Double) in.readValue(Double.class.getClassLoader());
        this.mVoteCount = in.readString();
    }

    public static final Parcelable.Creator<MovieResult> CREATOR = new Parcelable.Creator<MovieResult>() {
        @Override
        public MovieResult createFromParcel(Parcel source) {
            return new MovieResult(source);
        }

        @Override
        public MovieResult[] newArray(int size) {
            return new MovieResult[size];
        }
    };
}
