package com.example.katalogmovie.Support;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.Toast;

import com.example.katalogmovie.R;
import com.example.katalogmovie.alarm.DailyNotify;
import com.example.katalogmovie.alarm.ReleaseNotify;
import com.example.katalogmovie.model.Movie;
import com.example.katalogmovie.model.MovieResult;
import com.example.katalogmovie.network.Api;
import com.example.katalogmovie.network.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Objects;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class SettingAlarmActivity extends AppCompatActivity {

    public static final String TAG = "TAG";

    Switch daily, release;

    List<MovieResult> movieList;
    List<MovieResult> list;
    Api movieService;
    Call<Movie> movieCall;

    private DailyNotify dailyNotify;
    private ReleaseNotify releaseNotify;

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting_alarm);

        daily = findViewById(R.id.switch_daily);
        release = findViewById(R.id.switch_release);

        dailyNotify = new DailyNotify();
        releaseNotify = new ReleaseNotify();

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        boolean isDaily, isRelease;
        isDaily = sharedPreferences.getBoolean("daily", false);
        isRelease = sharedPreferences.getBoolean("release",false);

        Log.e(TAG, "onCreateIsDaily: " + isDaily );
        Log.e(TAG, "onCreateIsRelease: " + isRelease );

        if (isDaily){
            daily.setChecked(true);
        }
        else {
            daily.setChecked(false);
        }

        if (isRelease){
            release.setChecked(true);
        }else {
            release.setChecked(false);
        }

        daily.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked){
                    dailyNotify.setRepeatingAlarm(SettingAlarmActivity.this);
                    sharedPreferences.edit().putBoolean("daily", true).commit();
                }
                else {
                    dailyNotify.cancelAlarm(SettingAlarmActivity.this);
                    sharedPreferences.edit().putBoolean("daily", false).commit();
                }
            }
        });

        release.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    setAlarm();
                    sharedPreferences.edit().putBoolean("release", true).commit();
                } else {
                    releaseNotify.cancelAlarm(SettingAlarmActivity.this);
                    sharedPreferences.edit().putBoolean("release", false).commit();
                }
            }
        });
    }

    void setAlarm(){
        movieService = NetworkInterface.getClient().create(Api.class);
        movieCall = movieService.getUpcoming(Api.key_api);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        final String now = df.format(date);

        movieList = new ArrayList<>();
        list = new ArrayList<>();

        Log.e(TAG, "setAlarm: " + now );

        movieCall.enqueue(new Callback<Movie>() {
            @Override
            public void onResponse(@NonNull Call<Movie> call, @NonNull Response<Movie> response) {
                if (response.body() != null) {
                    movieList = Objects.requireNonNull(response.body()).getResults();

                    for (MovieResult i : movieList){
                        if (i.getrilis().equals(now)){
                            list.add(i);
                            releaseNotify.setRepeatingAlarm(SettingAlarmActivity.this, list);
                        }
                    }
                }
                Log.d(TAG, "onResponse: " + movieList.size());
                Log.e(TAG, "onResponseList: " + list.size());
            }

            @Override
            public void onFailure(@NonNull Call<Movie> call, @NonNull Throwable t) {
                Toast.makeText(SettingAlarmActivity.this, "Please make sure connected Internet", 15).show();
            }
        });
    }
}
