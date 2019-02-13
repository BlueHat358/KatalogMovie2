package com.example.katalogmovie;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;

import com.example.katalogmovie.db.DatabaseContract;
import com.example.katalogmovie.model.Favorite;
import com.example.katalogmovie.ui.FavoriteFragment;
import com.example.katalogmovie.ui.NowPlayingFragment;
import com.example.katalogmovie.ui.SearchFragment;
import com.example.katalogmovie.ui.UpComingFragment;

import java.util.ArrayList;
import java.util.Objects;

import static com.example.katalogmovie.Support.FragmentHelper.KEY_MOVIES;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation1);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        if (savedInstanceState == null){
            UpComingFragment upComingFragment = new UpComingFragment();
            fragmentTransaction.replace(R.id.container, upComingFragment
                    , UpComingFragment.class.getSimpleName()).commit();
        } else {
            int position = savedInstanceState.getInt(KEY_MOVIES);
            if (position == 1) {
                NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
                fragmentTransaction.replace(R.id.container, nowPlayingFragment
                        , NowPlayingFragment.class.getSimpleName()).commit();
            } else if (position == 2) {
                SearchFragment searchFragment = new SearchFragment();
                fragmentTransaction.replace(R.id.container, searchFragment
                        , SearchFragment.class.getSimpleName()).commit();
            } else if (position == 3) {
                FavoriteFragment favoriteFragment = new FavoriteFragment();
                fragmentTransaction.replace(R.id.container, favoriteFragment
                        , FavoriteFragment.class.getSimpleName()).commit();
            }
        }

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    UpComingFragment upComingFragment = new UpComingFragment();
                    fragmentTransaction.replace(R.id.container, upComingFragment
                            , UpComingFragment.class.getSimpleName()).commit();
                    return true;
                case R.id.navigation_dashboard:
                    NowPlayingFragment nowPlayingFragment = new NowPlayingFragment();
                    fragmentTransaction.replace(R.id.container, nowPlayingFragment
                            , NowPlayingFragment.class.getSimpleName()).commit();
                    return true;
                case R.id.navigation_notifications:
                    SearchFragment searchFragment = new SearchFragment();
                    fragmentTransaction.replace(R.id.container, searchFragment
                            , SearchFragment.class.getSimpleName()).commit();
                    return true;
                case R.id.navigation_favorite:
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

                    Bundle bundle = new Bundle();
                    bundle.putParcelableArrayList(FavoriteFragment.EXTRA_DETAIL_FAVORITE, favoriteArrayList);

                    FavoriteFragment favoriteFragment = new FavoriteFragment();
                    favoriteFragment.setArguments(bundle);

                    fragmentTransaction.replace(R.id.container, favoriteFragment
                            , FavoriteFragment.class.getSimpleName()).commit();
                    return true;
            }
            return false;
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.action_change_settings){
            Intent mIntent = new Intent(Settings.ACTION_LOCALE_SETTINGS);
            startActivity(mIntent);
        }
        return super.onOptionsItemSelected(item);
    }

}
