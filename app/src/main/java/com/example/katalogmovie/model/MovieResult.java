package com.example.katalogmovie.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieResult implements Parcelable {
    @SerializedName("id")
    private Long id;
    @SerializedName("overview")
    private String deskripsi;
    @SerializedName("poster_path")
    private String image;
    @SerializedName("release_date")
    private String rilis;
    @SerializedName("title")
    private String judul;
    @SerializedName("vote_average")
    private Double rating;
    @SerializedName("vote_count")
    private String vote;

    public Long getid() {
        return id;
    }

    public void setid(Long id) {
        this.id = id;
    }

    public String getdeskripsi() {
        return deskripsi;
    }

    public void setdeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getimage() {
        return image;
    }

    public void setimage(String image) {
        this.image = image;
    }

    public String getrilis() {
        return rilis;
    }

    public void setrilis(String rilis) {
        this.rilis = rilis;
    }

    public String getjudul() {
        return judul;
    }

    public void setjudul(String judul) {
        this.judul = judul;
    }

    public Double getrating() {
        return rating;
    }

    public void setrating(Double rating) {
        this.rating = rating;
    }

    public String getvote() {
        return vote;
    }

    public void setvote(String vote) {
        this.vote = vote;
    }

    public MovieResult(Long id, String deskripsi, String image,
                       String rilis, String judul, Double rating, String vote) {
        this.id = id;
        this.deskripsi = deskripsi;
        this.image = image;
        this.rilis = rilis;
        this.judul = judul;
        this.rating = rating;
        this.vote = vote;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.id);
        dest.writeString(this.deskripsi);
        dest.writeString(this.image);
        dest.writeString(this.rilis);
        dest.writeString(this.judul);
        dest.writeValue(this.rating);
        dest.writeString(this.vote);
    }

    public MovieResult(){}

    public MovieResult(Parcel in) {
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.deskripsi = in.readString();
        this.image = in.readString();
        this.rilis = in.readString();
        this.judul = in.readString();
        this.rating = (Double) in.readValue(Double.class.getClassLoader());
        this.vote = in.readString();
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
