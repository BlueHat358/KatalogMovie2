package com.example.katalogmovie.ui;


import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.app.LoaderManager;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.example.katalogmovie.R;
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

import static com.example.katalogmovie.ui.DetailActivity.MOVIE_DETAIL;

/**
 * A simple {@link Fragment} subclass.
 */
public class FavoriteFragment extends Fragment {

    public static final String EXTRA_DETAIL = "detail";

    public static final String TAG = "tag";

    RecyclerView rv_movie;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api api;
    Call<Movie> movieCall;
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

        assert getArguments() != null;
        ArrayList<Favorite> favorites = getArguments().getParcelableArrayList(EXTRA_DETAIL);

        Log.d(TAG, "onCreateView: " + favorites.size());

        for (Favorite i : favorites){
            Log.d(TAG, "onCreateView: " + i.getId());
            initView();
            getFavorite(i.getId());
        }
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


    private void showSelectedMovie(MovieResult movie){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(MOVIE_DETAIL, movie);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmPosterPath());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmId());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmVoteAverage());
        startActivity(intent);
    }

}
