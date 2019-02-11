package com.example.katalogmovie;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.example.katalogmovie.model.Favorite;
import com.example.katalogmovie.model.FragmentHelper;
import com.example.katalogmovie.ui.FavoriteFragment;
import com.example.katalogmovie.ui.NowPlayingFragment;
import com.example.katalogmovie.ui.SearchFragment;
import com.example.katalogmovie.ui.UpComingFragment;

import static com.example.katalogmovie.Support.FragmentHelper.KEY_MOVIES;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    FragmentHelper pos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

//        Fragment fragment = fragmentManager.findFragmentByTag(UpComingFragment.class.getSimpleName());
//        if (!(fragment instanceof UpComingFragment)){
//            fragmentTransaction.replace(R.id.container, upComingFragment, UpComingFragment.class.getSimpleName());
//            fragmentTransaction.commit();
//        }

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
