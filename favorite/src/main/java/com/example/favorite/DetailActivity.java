package com.example.favorite;

import android.content.ContentValues;
import android.database.Cursor;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.favorite.db.DatabaseContract;
import com.example.favorite.model.MovieResult;

public class DetailActivity extends AppCompatActivity {

    public static final String EXTRA_DETAIL = "detail";

    public static final String TAG = "tag";

    TextView titles, release, overview, rate, vote;
    ImageView images;
    Button btn_favorite;

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

        results = getIntent().getParcelableExtra(EXTRA_DETAIL);

        getData(results);

        if (isFavorite(results.getid().toString())){
            btn_favorite.setText("Remove");
        }
        else {
            btn_favorite.setText("Add");
        }

        btn_favorite.setOnClickListener(isPressed);
        Log.d(TAG, "onCreate: " + results.getid());
    }

    View.OnClickListener isPressed = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            Log.d(TAG, "onClick: " + results.getid());

            String id = results.getid().toString();

            boolean favorite = false;

            if (!isFavorite(id)){
                Log.d(TAG, "add: " + favorite);
                ContentValues contentValues = new ContentValues();
                contentValues.put(DatabaseContract.MovieColumns.ID, id);
                contentValues.put(DatabaseContract.MovieColumns.JUDUL, results.getjudul());
                getContentResolver().insert(DatabaseContract.CONTENT_URI, contentValues);
                Toast.makeText(DetailActivity.this, "Movie has been added", Toast.LENGTH_LONG).show();
                btn_favorite.setText("Remove");
            }
            else {
                Log.d(TAG, "Erase: " + favorite);
                Uri uri = DatabaseContract.CONTENT_URI;
                uri = uri.buildUpon().appendPath(results.getid().toString()).build();
                Log.v("MovieDetail", "" + uri);

                getContentResolver().delete(uri, null, null);
                Log.v("MovieDetail", uri.toString());
                Toast.makeText(DetailActivity.this, "Movie has been removed", Toast.LENGTH_LONG).show();
                btn_favorite.setText("Add");
            }
        }
    };

    private void getData(MovieResult results) {
        String judul = results.getjudul();
        String rilis = results.getrilis();
        String deskripsi = results.getdeskripsi();
        String rating = results.getrating().toString();
        String vote = results.getvote();

        titles.setText(judul);
        release.setText(rilis);
        overview.setText(deskripsi);
        rate.setText(rating);
        this.vote.setText(vote);


        Glide.with(this).load("https://image.tmdb.org/t/p/w342/" + results.getimage())
                .apply(new RequestOptions().fitCenter())
                .into(images);
    }

    private boolean isFavorite(String id) {
        String selection = " id = ?";
        String[] selectionArgs = {id};
        String[] projection = {DatabaseContract.MovieColumns.ID};
        Uri uri = DatabaseContract.CONTENT_URI;
        uri = uri.buildUpon().appendPath(id).build();

        Cursor cursor = null;
        cursor = getContentResolver().query(uri, projection,
                selection, selectionArgs, null, null);

        assert cursor != null;
        boolean exists = (cursor.getCount() > 0);
        Log.d(TAG, "isFavorite() returned: " + cursor.getCount());
        cursor.close();
        return exists;
    }
}
