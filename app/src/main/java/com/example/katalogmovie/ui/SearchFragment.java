package com.example.katalogmovie.ui;


import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.Support.ItemClickSupport;
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
import static com.example.katalogmovie.ui.DetailActivity.EXTRA_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class SearchFragment extends Fragment {

    public static final String TAG = "tag";

    RecyclerView rv_movie;
    EditText edtSearch;
    Button btnSearch;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api movieService;
    Call<Movie> movieCall;

    ProgressBar loading;


    public SearchFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             final Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_search, container, false);
        rv_movie = rootView.findViewById(R.id.rv_Movie);
        edtSearch = rootView.findViewById(R.id.edt_search);
        btnSearch = rootView.findViewById(R.id.btn_search);
        loading = rootView.findViewById(R.id.progress_circular);

        loading.setVisibility(View.INVISIBLE);
        initView();


        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (savedInstanceState == null && checkInternet()) {
                    String id = edtSearch.getText().toString();
                    initView();
                    loadData(id);
                    Log.d(TAG, "onViewCreated: " + id);
                }
                else if (savedInstanceState != null && checkInternet()){
                    //movieList = savedInstanceState.getParcelableArrayList(KEY_MOVIES);
                    String id = edtSearch.getText().toString();
                    initView();
                    loadData(id);
                    Log.d(TAG, "onViewCreated1: " + id);
                    //Log.d(TAG, "onViewCreated: " + movieList.size());
                }
                else {
                    Toast.makeText(getActivity(), "Please make sure connected Internet", 15).show();
                }
            }
        });

        return rootView;
    }

    void loadData(String id){
        loading.setVisibility(View.VISIBLE);
        movieService = NetworkInterface.getClient().create(Api.class);
        movieCall = movieService.getSearch(id, Api.key_api);

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
                Log.d(TAG, "onResponse: " + movieList.size());
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
            outState.putInt(KEY_MOVIES,2);
            super.onSaveInstanceState(outState);
        }
    }


    private void showSelectedMovie(MovieResult movie){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL, movie);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getimage());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getid());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getjudul());
        startActivity(intent);
    }

}
