package com.example.katalogmovie.ui;


import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
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
import com.example.katalogmovie.adapter.MovieAdapter;
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
import static com.example.katalogmovie.ui.DetailActivity.EXTRA_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class NowPlayingFragment extends Fragment {

    public static final String TAG = "tag";

    RecyclerView rv_movie;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api movieService;
    Call<Movie> movieCall;

    ProgressBar loading;


    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        rv_movie = rootView.findViewById(R.id.rv_Movie);
        loading = rootView.findViewById(R.id.progress_circular);

        if (savedInstanceState == null && checkInternet()) {
            initView();
            loadData();
        }
        else if (savedInstanceState != null && checkInternet()){
            movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
            initView();
            loadData();
            Log.d(TAG, "onViewCreated: " + movieList.size());
        }
        else {
            Toast.makeText(getActivity(), "Please make sure connected Internet", 15).show();
        }

        return rootView;
    }

    void loadData(){
        movieService = NetworkInterface.getClient().create(Api.class);
        movieCall = movieService.getNowPlaying(Api.key_api);

        movieList = new ArrayList<>();

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    movieList = Objects.requireNonNull(response.body()).getResults();
                }
                movieAdapter.setMovieResultList(movieList);
                rv_movie.setAdapter(movieAdapter);
                ItemClickSupport.addTo(rv_movie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showSelectedMovie(movieList.get(position));
                    }
                });
                for (MovieResult i : movieList){
                    Log.d(TAG, "onResponse: " + i.getimage());
                }
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(getActivity(), "Something went wrong"
                        , Toast.LENGTH_SHORT).show();
            }
        });
    }

    void initView() {

        movieAdapter = new MovieAdapter(getActivity());
        rv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_movie.setHasFixedSize(true);
        rv_movie.setItemAnimator(new DefaultItemAnimator());
    }

    public boolean checkInternet(){
        boolean connectStatus = true;
        ConnectivityManager ConnectionManager=(ConnectivityManager) getActivity().getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = ConnectionManager.getActiveNetworkInfo();
        if(networkInfo != null && (networkInfo).isConnected()==true ) {
            connectStatus = true;
            Log.d(TAG, "checkInternet: " + connectStatus);
        }
        else {
            connectStatus = false;
            Log.d(TAG, "checkInternet: " + connectStatus);
        }
        return connectStatus;
    }
    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if (checkInternet()) {
            outState.putParcelableArrayList(KEY_MOVIES, (ArrayList<? extends Parcelable>) movieAdapter.getMovieResultList());
            Log.d(TAG, "onSaveInstanceState: " + movieAdapter.getItemCount());
            outState.putInt(KEY_MOVIES,1);
            super.onSaveInstanceState(outState);
        }
    }


    private void showSelectedMovie(MovieResult movie){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL, movie);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getjudul());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getid());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getrating());
        startActivity(intent);
    }

}
