package com.example.katalogmovie.ui;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
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
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.katalogmovie.MainActivity;
import com.example.katalogmovie.R;
import com.example.katalogmovie.Support.ItemClickSupport;
import com.example.katalogmovie.adapter.MovieAdapter;
import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Favorite;
import com.example.katalogmovie.model.FragmentHelper;
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
public class UpComingFragment extends Fragment {

    public static final String TAG = "tag";

    RecyclerView rv_movie;

    List<MovieResult> movieList;
    MovieAdapter movieAdapter;

    Api movieService;
    Call<Movie> movieCall;

    ProgressBar loading;


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

//        initView();
//
//        loadData();

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
                        FragmentManager fragmentManager = getFragmentManager();
                        if (fragmentManager != null){
                            SearchFragment searchFragment = new SearchFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                            fragmentTransaction.replace(R.id.container, searchFragment, SearchFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                    case R.id.navigation_favorite:
                        ArrayList<Favorite> favoriteArrayList = new ArrayList<>();

                        Cursor cursor = null;
                        cursor = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI, null,
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

                        Bundle bundle = new Bundle();
                        bundle.putParcelableArrayList(FavoriteFragment.EXTRA_DETAIL_FAVORITE, favoriteArrayList);


                        FragmentManager fragmentManager2 = getFragmentManager();
                        if (fragmentManager2 != null){
                            FavoriteFragment favoriteFragment = new FavoriteFragment();
                            FragmentTransaction fragmentTransaction = fragmentManager2.beginTransaction();
                            favoriteFragment.setArguments(bundle);

                            fragmentTransaction.replace(R.id.container, favoriteFragment, FavoriteFragment.class.getSimpleName());
                            fragmentTransaction.commit();
                        }
                        break;
                }
                return false;
            }
        });

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
            loading.setVisibility(View.GONE);
        }

        FragmentHelper position = new FragmentHelper();
        position.setPosition(0);
    }

    void loadData(){
        movieService = NetworkInterface.getClient().create(Api.class);
        movieCall = movieService.getUpcoming(Api.key_api);

        movieList = new ArrayList<>();

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    movieList = Objects.requireNonNull(response.body()).getResults();
                }
                movieAdapter.setMovieResultList(movieList);
                rv_movie.setAdapter(movieAdapter);
                Log.d(TAG, "onResponse: " + movieList.size());

                ItemClickSupport.addTo(rv_movie).setOnItemClickListener(new ItemClickSupport.OnItemClickListener() {
                    @Override
                    public void onItemClicked(RecyclerView recyclerView, int position, View v) {
                        showSelectedMovie(movieList.get(position));
                    }
                });
                loading.setVisibility(View.GONE);
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {

            }
        });
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
            outState.putInt(KEY_MOVIES,0);
            super.onSaveInstanceState(outState);
        }
    }

    void initView() {
        movieAdapter = new MovieAdapter(getActivity());
        rv_movie.setLayoutManager(new LinearLayoutManager(getActivity()));
        rv_movie.setHasFixedSize(true);
        rv_movie.setItemAnimator(new DefaultItemAnimator());
    }

    private void showSelectedMovie(MovieResult movie){
        ArrayList<Favorite> favoriteArrayList = new ArrayList<>();

        Cursor cursor = null;
        cursor = getActivity().getContentResolver().query(DatabaseContract.CONTENT_URI, null,
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

        Intent intent = new Intent(getActivity(), DetailActivity.class);


        intent.putExtra(EXTRA_DETAIL, movie);
        //intent.putParcelableArrayListExtra(DETAIL_ARRAY, favoriteArrayList);
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getimage());
        Log.d(TAG, "showSelectedMovie() returned: " + favoriteArrayList.size());
        Log.d(TAG, "showSelectedMovie() returned: " + movie.getrating());
        startActivity(intent);
    }
}
