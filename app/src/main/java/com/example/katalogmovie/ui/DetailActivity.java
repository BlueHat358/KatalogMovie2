package com.example.katalogmovie.ui;

import android.content.ContentValues;
import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.katalogmovie.R;
import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DetailActivity extends AppCompatActivity {

    public static final String MOVIE_DETAIL = "detail";

    public static final String TAG = "tag";

    TextView titles, release, overview, rate, vote;
    ImageView images;
    Button btn_favorite;
    FloatingActionButton floatingActionButton;

    MovieResult results;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        titles = findViewById(R.id.tv_judul_detail);
        release = findViewById(R.id.tv_rilis_detail);
        overview = findViewById(R.id.tv_deskripsi_detail);
        rate = findViewById(R.id.tv_rating_detail);
        this.vote = findViewById(R.id.tv_vote_detail);
        images = findViewById(R.id.img_image_detail);
        btn_favorite = findViewById(R.id.btn_toggle_favorite);

        results = getIntent().getParcelableExtra(MOVIE_DETAIL);

        getData(results);

        btn_favorite.setOnClickListener(isPressed);
    }

    View.OnClickListener isPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + results.getmId());

                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContract.MovieColumns.ID, results.getmId().toString());
                contentValues.put(DatabaseContract.MovieColumns.JUDUL, results.getmTitle());
                getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
                Snackbar.make(v, "This movie has been add to your favorite", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            Log.d(TAG, "onClick: " + DatabaseContract.MovieColumns.ID);
//
//                Uri uri = DatabaseContract.CONTENT_URI;
//                uri = uri.buildUpon().appendPath(results.getmId().toString()).build();
//                Log.v("MovieDetail", "" + uri);
//
//                getContentResolver().delete(uri, null, null);
//                Log.v("MovieDetail", uri.toString());
//                Snackbar.make(v, "This movie has been remove from your favorite", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
        }
    };

    private void getData(MovieResult results) {
        String judul = results.getmTitle();
        String rilis = results.getmReleaseDate();
        String deskripsi = results.getmOverview();
        String rating = results.getmVoteAverage().toString();
        String vote = results.getmVoteCount();

        titles.setText(judul);
        release.setText(rilis);
        overview.setText(deskripsi);
        rate.setText(rating);
        this.vote.setText(vote);

        //Picasso.get().load("https://image.tmdb.org/t/p/w700/" + results.getmPosterPath()).into(images);

        Glide.with(this).load("https://image.tmdb.org/t/p/w342/" + results.getmPosterPath())
                .apply(new RequestOptions().fitCenter())
                .into(images);
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
