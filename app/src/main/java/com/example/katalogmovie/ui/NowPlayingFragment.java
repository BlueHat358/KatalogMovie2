package com.example.katalogmovie.ui;


import android.content.Intent;
import android.os.Bundle;
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

import static com.example.katalogmovie.ui.DetailActivity.MOVIE_DETAIL;

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


    public NowPlayingFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_now_playing, container, false);
        rv_movie = rootView.findViewById(R.id.rv_Movie);

        initView();

        loadData();

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
                        FragmentManager fragmentManager1 = getFragmentManager();
                        if (fragmentManager1 != null){
                            UpComingFragment upComingFragment = new UpComingFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager1.beginTransaction();

                            fragmentTransaction.replace(R.id.container, upComingFragment, UpComingFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_dashboard:

                        break;
                    case R.id.navigation_notifications:
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null){
                            SearchFragment searchFragment = new SearchFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container, searchFragment, SearchFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_favorite:
                        FragmentManager fragmentManager2 = getFragmentManager();
                        if (fragmentManager2 != null){
                            FavoriteFragment favoriteFragment = new FavoriteFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager2.beginTransaction();

                            fragmentTransaction.replace(R.id.container, favoriteFragment, FavoriteFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                }
                return false;
            }
        });
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
                    Log.d(TAG, "onResponse: " + i.getmPosterPath());
                }
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


    private void showSelectedMovie(MovieResult movie){
        Intent intent = new Intent(getActivity(), DetailActivity.class);
        intent.putExtra(MOVIE_DETAIL, movie);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmPosterPath());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmId());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getmVoteAverage());
        startActivity(intent);
    }

}
