package com.example.katalogmovie.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.katalogmovie.R;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_JUDUL= "judul";
    public static final String EXTRA_RILIS= "rilis";
    public static final String EXTRA_DESKRIPSI = "deskripsi";
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_VOTE = "vote";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
    }
}
