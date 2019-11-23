package com.ayra.moviecatalogue.notification;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.util.Log;

import androidx.core.app.NotificationCompat;

import com.ayra.moviecatalogue.R;
import com.ayra.moviecatalogue.ui.view.notificationsetting.NotificationSettingActivity;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

public class FirebaseReminderNotification extends FirebaseMessagingService {
    private static final String TAG = "FirebaseNotification";

    @Override
    public void onNewToken(String s) {
        super.onNewToken(s);

        Log.d(TAG, "onNewToken: " + s);
    }

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            sendNotification(remoteMessage.getNotification().getBody());
        }
    }

    private void sendNotification(String body) {
        String channelId = getString(R.string.default_notification_channel_id);
        String channelName = getString(R.string.app_name);

        Intent intent = new Intent(this, NotificationSettingActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        Uri defaultSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, channelId)
                .setLargeIcon(BitmapFactory.decodeResource(this.getResources(),
                        R.mipmap.ic_launcher_new))
                .setSmallIcon(R.mipmap.ic_launcher_new)
                .setContentText(body)
                .setAutoCancel(true)
                .setSound(defaultSound)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT);
            builder.setChannelId(channelId);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(notificationChannel);
            }
        } else {
            Notification notification = builder.build();
            if (notificationManager != null) {
                notificationManager.notify(0, notification);
            }
        }

    }
}
