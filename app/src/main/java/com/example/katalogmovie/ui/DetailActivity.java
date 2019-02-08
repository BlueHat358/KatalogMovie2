package com.example.katalogmovie.ui;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_JUDUL= "judul";
    public static final String EXTRA_RILIS= "rilis";
    public static final String EXTRA_DESKRIPSI = "deskripsi";
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_VOTE = "vote";

    public static final String TAG = "tag";

    TextView titles, release, overview, rate, vote;
    ImageView image;
    FloatingActionButton floatingActionButton;
    Button btn_favorite;

    String id, title;

    MovieResult results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        String key = getIntent().getStringExtra(EXTRA_KEY);
        final String judul1 = getIntent().getStringExtra(EXTRA_JUDUL);
        String judul = getIntent().getStringExtra(EXTRA_JUDUL);
        String rilis = getIntent().getStringExtra(EXTRA_RILIS);
        String deskripsi = getIntent().getStringExtra(EXTRA_DESKRIPSI);
        String image = getIntent().getStringExtra(EXTRA_IMAGE);
        String rating = getIntent().getStringExtra(EXTRA_RATING);
        String vote = getIntent().getStringExtra(EXTRA_VOTE);

        titles = findViewById(R.id.tv_judul_detail);
        release = findViewById(R.id.tv_rilis_detail);
        overview = findViewById(R.id.tv_deskripsi_detail);
        rate = findViewById(R.id.tv_rating_detail);
        this.vote = findViewById(R.id.tv_vote_detail);
        this.image = findViewById(R.id.img_image_detail);
        floatingActionButton = findViewById(R.id.fab);
        btn_favorite = findViewById(R.id.btn_favorite);

        titles.setText(judul);
        release.setText(rilis);
        overview.setText(deskripsi);
        rate.setText(rating);
        this.vote.setText(vote);

        Picasso.with(this).load("https://image.tmdb.org/t/p/w500/" + image).into(this.image);

        btn_favorite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: " + results.getmVoteAverage());
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }
}
