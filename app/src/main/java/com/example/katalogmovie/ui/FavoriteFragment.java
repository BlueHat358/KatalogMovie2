package com.example.katalogmovie.ui;


import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.Support.ItemClickSupport;
import com.example.katalogmovie.adapter.MovieAdapter;
import com.example.katalogmovie.model.Favorite;
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;
import com.example.katalogmovie.network.Api;
import com.example.katalogmovie.network.NetworkInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.katalogmovie.Support.FragmentHelper.KEY_MOVIES;
import static com.example.katalogmovie.ui.DetailActivity.EXTRA_DETAIL;


/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    public static final String EXTRA_DETAIL_FAVORITE = "detail";

    public static final String TAG = "tag";

    RecyclerView rv_movie;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api api;
    Call<MovieResult> resultCall;
    MovieResult movieResult;


    public FavoriteFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        movieList = new ArrayList<>();
        movieResult = new MovieResult();

        View rootView = inflater.inflate(R.layout.fragment_favorite, container, false);
        rv_movie = rootView.findViewById(R.id.rv_Movie);

        return rootView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        BottomNavigationView navigationView = (BottomNavigationView) view.findViewById(R.id.navigation);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                switch (menuItem.getItemId()) {
                    case R.id.navigation_home:
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null){
                            UpComingFragment upComingFragment = new UpComingFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container, upComingFragment, UpComingFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_dashboard:
                        FragmentManager fragmentManager1 = getFragmentManager();
                        if (fragmentManager1 != null){
                            NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();

                            fragmentTransaction.replace(R.id.container, nowPlayingFragment, NowPlayingFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_notifications:
                        FragmentManager fragmentManager2 = getFragmentManager();
                        if (fragmentManager2 != null){
                            SearchFragment searchFragment = new SearchFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager2.beginTransaction();

                            fragmentTransaction.replace(R.id.container, searchFragment, SearchFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_favorite:
                        break;
                }
                return false;
            }
        });

        if (savedInstanceState == null && checkInternet()) {
            ArrayList<Favorite> favorites = getArguments().getParcelableArrayList(EXTRA_DETAIL_FAVORITE);
            if (favorites.size() == 0) {
                Toast.makeText(getActivity(), "No favorite has been added", 10).show();
            }
            else {

                Log.d(TAG, "onCreateView: " + favorites.size());

                for (Favorite i : favorites) {
                    Log.d(TAG, "onCreateView: " + i.getId());
                    initView();
                    getFavorite(i.getId());
                }
            }
        }
        else if (savedInstanceState != null && checkInternet()){
            ArrayList<Favorite> favorites = getArguments().getParcelableArrayList(EXTRA_DETAIL_FAVORITE);
            initView();
            Log.d(TAG, "onCreateView: " + favorites.size());

            for (Favorite i : favorites) {
                Log.d(TAG, "onCreateView: " + i.getId());
                initView();
                getFavorite(i.getId());
            }
        }
        else {
            Toast.makeText(getActivity(), "Please make sure connected Internet", 15).show();
        }
    }

    private void getFavorite(String id) {
        api = NetworkInterface.getClient().create(Api.class);
        resultCall = api.getMovieById(id, Api.key_api);

        resultCall.enqueue(new Callback<MovieResult>() {
            @Override
            public void onResponse(@NonNull Call<MovieResult> call, @NonNull Response<MovieResult> response) {
                movieList.add(response.body());
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
            }

            @Override
            public void onFailure(@NonNull Call<MovieResult> call, @NonNull Throwable t) {
                movieResult = null;
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
            Log.d(TAG, "onSaveInstanceState: " + movieAdapter.getItemCount());
            outState.putInt(KEY_MOVIES,3);
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
