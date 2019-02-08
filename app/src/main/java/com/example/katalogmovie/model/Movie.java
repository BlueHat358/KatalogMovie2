package com.example.katalogmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Movie implements Parcelable {
    public List<MovieResult> getResults() {
        return mMovieResults;
    }

    public void setResult(List<MovieResult> mMovieResults) {
        this.mMovieResults = mMovieResults;
    }

    public Movie(List<MovieResult> mMovieResults) {
        this.mMovieResults = mMovieResults;
    }

    @SerializedName("results")
    private List<MovieResult> mMovieResults;

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeTypedList(this.mMovieResults);
    }

    protected Movie(Parcel in) {
        this.mMovieResults = in.createTypedArrayList(MovieResult.CREATOR);
    }

    public static final Parcelable.Creator<Movie> CREATOR = new Parcelable.Creator<Movie>() {
        @Override
        public Movie createFromParcel(Parcel source) {
            return new Movie(source);
        }

        @Override
        public Movie[] newArray(int size) {
            return new Movie[size];
        }
    };
}
