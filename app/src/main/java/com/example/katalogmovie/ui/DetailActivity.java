package com.example.katalogmovie.ui;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Movie;
import com.squareup.picasso.Picasso;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_KEY = "key";
    public static final String EXTRA_JUDUL= "judul";
    public static final String EXTRA_RILIS= "rilis";
    public static final String EXTRA_DESKRIPSI = "deskripsi";
    public static final String EXTRA_IMAGE = "image";
    public static final String EXTRA_RATING = "rating";
    public static final String EXTRA_VOTE = "vote";

    TextView tvJudul, tvRilis, tvDeskripsi, tvRating, tvVote;
    ImageView img_image;
    Button btn_favorite;

    Movie movie;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        final String key = getIntent().getStringExtra(EXTRA_KEY);

        tvJudul = findViewById(R.id.tv_judul_detail);
        tvRilis = findViewById(R.id.tv_rilis_detail);
        tvDeskripsi = findViewById(R.id.tv_deskripsi_detail);
        tvRating = findViewById(R.id.tv_rating_detail);
        tvVote = findViewById(R.id.tv_vote_detail);
        img_image = findViewById(R.id.img_image_detail);
        btn_favorite = findViewById(R.id.btn_favorite);

        final String judul = getIntent().getStringExtra(EXTRA_KEY);
        String rilis = getIntent().getStringExtra(EXTRA_KEY);
        String deskripsi = getIntent().getStringExtra(EXTRA_KEY);
        String image = getIntent().getStringExtra(EXTRA_KEY);
        String rating = getIntent().getStringExtra(EXTRA_KEY);
        String vote = getIntent().getStringExtra(EXTRA_KEY);

        tvJudul.setText(judul);
        tvRilis.setText(rilis);
        tvDeskripsi.setText(deskripsi);
        tvRating.setText(rating);
        tvVote.setText(vote);

        Picasso.with(this).load("https://image.tmdb.org/t/p/w500/" + image).into(img_image);

//        if (isFavorite(key)) {
//            if (btn_favorite != null) {
//                Log.v("MovieDetail", "" + key);
//            }
//        }

        if (btn_favorite != null) {
            btn_favorite.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (!isFavorite(movie.getKey())) {
                        ContentValues contentValues = new ContentValues();
                        contentValues.put(DatabaseContract.MovieColumns.ID, key);
                        contentValues.put(DatabaseContract.MovieColumns.JUDUL, judul);
                        getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
                        Toast.makeText(DetailActivity.this, "This movie has been add to your favorite", Toast.LENGTH_LONG).show();
                    } else {
                        Uri uri = DatabaseContract.CONTENT_URI;
                        uri = uri.buildUpon().appendPath(key).build();
                        Log.v("MovieDetail", "" + uri);

                        getContentResolver().delete(uri, null, null);
                        Log.v("MovieDetail", uri.toString());
                        Toast.makeText(DetailActivity.this, "This movie has been remove from your favorite", Toast.LENGTH_LONG).show();
                    }
                }
            });
        }
    }


    private boolean isFavorite(String id) {
        String selection = "id = ?";
        String[] selectionArgs = {id};
        String[] projection = {DatabaseContract.MovieColumns.ID};
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = null;
        cursor = getContentResolver().query(uri, projection,
                    selection, selectionArgs, null, null);

        assert cursor != null;
        boolean exists = (cursor.getCount() > 0);
        cursor.close();
        return exists;
    }
}
