package com.example.katalogmovie.ui;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.Support.ItemClickSupport;
import com.example.katalogmovie.Support.SettingAlarmActivity;
import com.example.katalogmovie.adapter.MovieAdapter;
import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Favorite;
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;
import com.example.katalogmovie.network.Api;
import com.example.katalogmovie.network.NetworkInterface;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.katalogmovie.Support.FragmentHelper.KEY_MOVIES;
import static com.example.katalogmovie.Support.FragmentHelper.MOVIE_ARRAY;
import static com.example.katalogmovie.ui.DetailActivity.EXTRA_DETAIL;


/**
 * A simple {@link Fragment} subclass.
 */
public class UpComingFragment extends Fragment {

    public static final String TAG = "tag";

    RecyclerView rv_movie;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api movieService;
    Call<Movie> movieCall;

    ProgressBar loading;
    ArrayList<MovieResult> list = new ArrayList<>();

    public UpComingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_up_coming, container, false);
        rv_movie = rootView.findViewById(R.id.rv_Movie);
        loading = rootView.findViewById(R.id.progress_circular);

        initView();
        if (savedInstanceState == null && checkInternet()) {
            loadData();

        }
        else if (savedInstanceState != null && !checkInternet()){
            list = savedInstanceState.getParcelableArrayList("list");
            Log.d(TAG, "onCreateView: " + String.valueOf(list.size()));
            movieAdapter.setMovieResultList(list);
            rv_movie.setAdapter(movieAdapter);
            loading.setVisibility(View.GONE);
        }
        else if (savedInstanceState != null && checkInternet()){
            loadData();
        }
        if (!checkInternet() && savedInstanceState == null){
            Toast.makeText(getActivity(), "Please make sure connected Internet", 15).show();
            loading.setVisibility(View.GONE);
        }

        return rootView;
    }


    void loadData(){
        movieService = NetworkInterface.getClient().create(Api.class);
        movieCall = movieService.getUpcoming(Api.key_api);

        movieList = new ArrayList<>();

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    List<MovieResult> movies = response.body().getResults();
                    list.addAll(movies);
                    movieAdapter.setMovieResultList(list);
                    movieAdapter = new MovieAdapter(getActivity(), list);
                    rv_movie.setAdapter(movieAdapter);
                }


                Log.d(TAG, "onResponse: " + movieList.size());

                ItemClickSupport.addTo(rv_movie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showSelectedMovie(list.get(position));
                    }
                });
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Please make sure connected Internet", 15).show();
                loading.setVisibility(View.GONE);
            }
        });
    }

    public boolean checkInternet(){
        boolean connectStatus;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && (networkInfo).isConnected()==true ) {
            connectStatus = true;
        }
        else {
            connectStatus = false;
        }
        Log.d(TAG, "checkInternet: " + connectStatus);
        return connectStatus;
    }

    @Override
    public void onSaveInstanceState(final Bundle outState) {
        outState.putParcelableArrayList("list", list);

        outState.putInt(KEY_MOVIES,0);
        super.onSaveInstanceState(outState);
    }

    void initView() {
        movieAdapter = new MovieAdapter(getActivity(), movieList);
        rv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_movie.setHasFixedSize(true);
        rv_movie.setItemAnimator(new DefaultItemAnimator());

    }

    private void showSelectedMovie(MovieResult movie){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL, movie);
        //intent.putParcelableArrayListExtra(DETAIL_ARRAY, favoriteArrayList);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getimage());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getrating());
        startActivity(intent);
    }
}
