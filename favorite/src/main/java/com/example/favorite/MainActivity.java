package com.example.favorite;

import android.content.Intent;
import android.database.Cursor;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;

import com.example.favorite.Support.ItemClickSupport;
import com.example.favorite.adapter.MovieAdapter;
import com.example.favorite.db.DatabaseContract;
import com.example.favorite.model.Favorite;
import com.example.favorite.model.MovieResult;
import com.example.favorite.network.Api;
import com.example.favorite.network.NetworkInterface;

import static com.example.favorite.DetailActivity.EXTRA_DETAIL;

import java.util.ArrayList;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.favorite.Support.MappingHelper.mapCursorToArrayList;
import static com.example.favorite.db.DatabaseContract.CONTENT_URI;

public class MainActivity extends AppCompatActivity
        implements LoaderManager.LoaderCallbacks<Cursor> {

    public static final String TAG = "tag";

    RecyclerView recyclerView;

    Call<MovieResult> resultCall;
    MovieAdapter adapter;
    ArrayList<MovieResult> movieResults;
    ArrayList<Favorite> favorites;
    Api api;

    private final int MOVIE_ID = 110;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        recyclerView = findViewById(R.id.rv_Movie);

        adapter = new MovieAdapter(getApplicationContext());
        initView();

        getSupportLoaderManager().initLoader(MOVIE_ID, null, this);
    }

    private void initView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setHasFixedSize(true);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }


    @NonNull
    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        favorites = new ArrayList<>();
        movieResults = new ArrayList<>();
        return new CursorLoader(this, CONTENT_URI,
                null, null, null, null);
    }

    @Override
    public void onLoadFinished(@NonNull android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        favorites = mapCursorToArrayList(data);
        for (Favorite i : favorites) {
            loadData(i.getId());
            Log.d(TAG, "onLoadFinished: " + i.getId());
            //adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onLoaderReset(@NonNull android.support.v4.content.Loader<Cursor> loader) {
        favorites = mapCursorToArrayList(null);
    }

    private void loadData(String id) {
        api = NetworkInterface.getClient().create(Api.class);
        resultCall = api.getMovieById(id, Api.key_api);

        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                movieResults.add(response.body());
                adapter.setMovieResultList(movieResults);
                recyclerView.setAdapter(adapter);
                //adapter.notifyDataSetChanged();
                Log.d(TAG, "onResponse: " + movieResults.size());

                ItemClickSupport.addTo(recyclerView).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showSelectedMovie(movieResults.get(position));
                    }
                });
            }

            @Override
            public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                movieResults = null;
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (movieResults != null) {
            movieResults.clear();
            adapter.setMovieResultList(movieResults);
            recyclerView.setAdapter(adapter);
        }
        getSupportLoaderManager().restartLoader(MOVIE_ID, null, this);
    }

    private void showSelectedMovie(MovieResult movie){
        ArrayList<Favorite> favoriteArrayList = new ArrayList<>();

        Cursor cursor = null;
        cursor = getContentResolver().query(DatabaseContract.CONTENT_URI, null,
                null, null, null, null);
        Objects.requireNonNull(cursor).moveToFirst();
        Favorite favorite;
        if (Objects.requireNonNull(cursor).getCount() > 0) {
            do {
                favorite = new Favorite(cursor.getString(cursor.getColumnIndexOrThrow(
                        DatabaseContract.MovieColumns.ID)));
                favoriteArrayList.add(favorite);
                cursor.moveToNext();
            } while (!cursor.isAfterLast());
        }

        Intent intent = new Intent(this, DetailActivity.class);


        intent.putExtra(EXTRA_DETAIL, movie);
        //intent.putParcelableArrayListExtra(DETAIL_ARRAY, favoriteArrayList);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getimage());
        Log.d(TAG, "showSelectedMovie() returned: " + favoriteArrayList.size());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getrating());
        Log.d(TAG, "showSelectedMovie: " + movie.getdeskripsi());
        startActivity(intent);
    }
}

