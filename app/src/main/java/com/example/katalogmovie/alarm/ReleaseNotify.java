package com.example.katalogmovie.alarm;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.katalogmovie.R;
import com.example.katalogmovie.model.MovieResult;
import com.example.katalogmovie.ui.DetailActivity;

import java.util.Calendar;
import java.util.List;

import static com.example.katalogmovie.ui.DetailActivity.EXTRA_DETAIL;

public class ReleaseNotify extends BroadcastReceiver {
    private static final int NOTIFICATION_ID = 1;
    private static int notifId = 1000;
    public static final String NOTIFICATION_CHANNEL = "Alarm Manager";
    public static final String NOTIFICATION_CHANNEL_ID = "10";
    public static final String TAG = "TAG";

    @Override
    public void onReceive(Context context, Intent intent) {
        int id = intent.getIntExtra("id", 0);
        String movieTitle = intent.getStringExtra("movietitle");
        String title = "Katalog Movie";
        String descript = String.valueOf(String.format("Hi movie %s release today", movieTitle));

        long ids = intent.getLongExtra("movieid",0);
        String deskripi = intent.getStringExtra("moviedeskripsi");
        String image = intent.getStringExtra("movieimage");
        String rilis = intent.getStringExtra("movierilis");
        double rating = intent.getDoubleExtra("movierating", 0);
        String vote = intent.getStringExtra("movievote");

        MovieResult movieResult = new MovieResult(ids, deskripi, image, rilis, movieTitle, rating, vote);

        Log.e(TAG, "onReceive: " + deskripi);

        showAlarmNotification(context, title, descript, id, movieResult);
        Log.e(TAG, "onReceive: " + movieTitle);
    }

    private void showAlarmNotification(Context context, String title, String des, int notifId, MovieResult movieResult) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        Intent intent = new Intent(context, DetailActivity.class);
        intent.putExtra(EXTRA_DETAIL, movieResult);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notifId, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(des)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000})
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setSound(alarmSound)
                .setAutoCancel(true);

        Log.e(TAG, "showAlarmNotification: " + title);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(NOTIFICATION_CHANNEL_ID,
                    NOTIFICATION_CHANNEL, NotificationManager.IMPORTANCE_DEFAULT);
            channel.enableVibration(true);
            channel.enableLights(true);
            channel.setVibrationPattern(new long[]{1000, 1000, 1000, 1000, 1000});

            builder.setChannelId(NOTIFICATION_CHANNEL_ID);

            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        Notification notification = builder.build();
        if (notificationManager != null){
            notificationManager.notify(notifId, notification);
        }
    }

    public void setRepeatingAlarm(Context context, List<MovieResult> movieResults){
        for (MovieResult i : movieResults){
            cancelAlarm(context);

            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            Intent intent = new Intent(context, ReleaseNotify.class);
            intent.putExtra("id", notifId);
            intent.putExtra("movietitle", i.getjudul());
            intent.putExtra("movieid", i.getid());
            intent.putExtra("movierilis", i.getrilis());
            intent.putExtra("moviedeskripsi", i.getdeskripsi());
            intent.putExtra("movieimage", i.getimage());
            intent.putExtra("movierating", i.getrating());
            intent.putExtra("movievote", i.getvote());

            Log.e(TAG, "setRepeatingAlarm: " + i.getjudul());
            Log.e(TAG, "setRepeatingAlarm: " + i.getrilis());
            Log.e(TAG, "setRepeatingAlarm: " + i.getdeskripsi());
            Log.e(TAG, "setRepeatingAlarm: " + i.getimage());
            Log.e(TAG, "setRepeatingAlarm: " + i.getrating());
            Log.e(TAG, "setRepeatingAlarm: " + i.getvote());

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 100, intent,PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 8);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            if (alarmManager != null){
                alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                        AlarmManager.INTERVAL_DAY, pendingIntent);
            }

            notifId += 1;
        }
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Intent intent = new Intent(context, ReleaseNotify.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, NOTIFICATION_ID, intent,0);
        pendingIntent.cancel();
        if (alarmManager != null){
            alarmManager.cancel(pendingIntent);
        }
    }
}
