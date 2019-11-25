package com.ayra.moviecatalogue.notification;

import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.api.ApiInterface;
import com.ayra.moviecatalogue.api.ApiService;
import com.ayra.moviecatalogue.data.entity.Movie;
import com.ayra.moviecatalogue.data.response.MovieResponse;
import com.ayra.moviecatalogue.ui.view.main.MainActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.content.Context.ALARM_SERVICE;

public class NewReleaseNotification extends BroadcastReceiver {

    private static final String EXTRA_NOTIFICATION = "extra_notification";
    public static final String TYPE_NOTIFICATION = "release";
    public static final String TAG = NewReleaseNotification.class.getSimpleName();
    private static final int REQUEST_CODE = 100;
    private Context context;

    public NewReleaseNotification() {
    }

    public NewReleaseNotification(Context context) {
        this.context = context;
    }

    public void setUpRelease() {
        Intent intent = new Intent(context, MainActivity.class);
        intent.putExtra(EXTRA_NOTIFICATION, TYPE_NOTIFICATION);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);

        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 11);
        calendar.set(Calendar.MINUTE, 15);
        calendar.set(Calendar.SECOND, 0);

        if (alarmManager != null) {
            alarmManager.setInexactRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                    AlarmManager.INTERVAL_DAY, pendingIntent);
        }

        Log.d(TAG, "setUpRelease " + calendar.getTimeInMillis());
    }

    private void getReleaseToday(final Context context) {
        String pattern = "yyyy-MM-dd";
        SimpleDateFormat dateFormat = new SimpleDateFormat(pattern, Locale.getDefault());
        String now = dateFormat.format(new Date());
        ApiInterface apiInterface = ApiService.getService().create(ApiInterface.class);

        Call<MovieResponse> call = apiInterface.getReleasedMovies(now, now);
        call.enqueue(new Callback<MovieResponse>() {
            @Override
            public void onResponse(@NonNull Call<MovieResponse> call, @NonNull Response<MovieResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        ArrayList<Movie> movies = response.body().getMovies();
                        int idMovie = 1;
                        for (Movie movie : movies) {
                            String titleMovie = movie.getTitle();
                            String descMovie = titleMovie + " " + context.getString(R.string.release_message);
                            showReleaseNotification(context, titleMovie, descMovie, idMovie);
                            Log.d(TAG, "onResponse: " + descMovie);
                            idMovie++;
                        }
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<MovieResponse> call, @NonNull Throwable t) {

            }
        });
    }

    private void showReleaseNotification(Context context, String title, String desc, int id) {
        String CHANNEL_ID = "channel_01";
        String CHANNEL_NAME = "channel_name";

        Intent intent = new Intent(context, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, id, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, CHANNEL_ID)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(),
                        R.mipmap.ic_launcher_new))
                .setSmallIcon(R.mipmap.ic_launcher_new)
                .setContentTitle(title)
                .setContentText(desc)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(CHANNEL_ID);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
            Log.d(TAG, "showReleaseNotification: notify android O+");
        } else {
            Notification notification = builder.build();
            if (notificationManager != null) {
                notificationManager.notify(id, notification);
            }
            Log.d(TAG, "showReleaseNotification: notify");
        }
    }

    public void cancelReleaseNotification(Context context) {
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(context, NewReleaseNotification.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(context, REQUEST_CODE, intent, 0);
        pendingIntent.cancel();
        if (alarmManager != null) {
            alarmManager.cancel(pendingIntent);
        }
        Log.d(TAG, "cancelReleaseNotification");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive");
        String extra = intent.getStringExtra(EXTRA_NOTIFICATION);
        Log.d(TAG, "onReceive" + extra);
        if (extra != null && extra.equals(TYPE_NOTIFICATION)) {
            getReleaseToday(context);
            Log.d(TAG, "onReceive");
        }
        Log.d(TAG, "onReceive");
    }
}
