package com.example.katalogmovie.model;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import static com.example.katalogmovie.db.DatabaseContract.MovieColumns.ID;
import static com.example.katalogmovie.db.DatabaseContract.MovieColumns.JUDUL;

public class Favorite implements Parcelable {
    public String id, judul;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.judul);
    }

    public Favorite(String id, String judul) {
        this.id = id;
        this.judul = judul;
    }

    public Favorite(Cursor cursor) {
        this.id = cursor.getString(cursor.getColumnIndexOrThrow(ID));
        this.judul = cursor.getString(cursor.getColumnIndexOrThrow(JUDUL));
    }

    public Favorite() {
    }

    protected Favorite(Parcel in) {
        this.id = in.readString();
        this.judul = in.readString();
    }

    public static final Parcelable.Creator<Favorite> CREATOR = new Parcelable.Creator<Favorite>() {
        @Override
        public Favorite createFromParcel(Parcel source) {
            return new Favorite(source);
        }

        @Override
        public Favorite[] newArray(int size) {
            return new Favorite[size];
        }
    };
}
