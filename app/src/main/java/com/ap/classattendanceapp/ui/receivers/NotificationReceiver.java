package com.ap.classattendanceapp.ui.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import androidx.core.app.NotificationCompat;

import com.ap.classattendanceapp.R;
import com.ap.classattendanceapp.ui.activities.StudentActivity;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String className = intent.getStringExtra("className");
        showNotification(context, className);
    }

    private void showNotification(Context context, String className) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent notificationIntent = new Intent(context, StudentActivity.class);
        int notificationId = generateUniqueId();

        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, NotificationChannelHelper.CHANNEL_ID)
                .setContentTitle("Class Reminder")
                .setContentText("Your class " + className + " starts in 2 hours!")
                .setSmallIcon(R.drawable.ic_notification)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setDefaults(Notification.DEFAULT_ALL);

        Notification notification = builder.build();
        notificationManager.notify(notificationId, notification);
    }

    private static int generateUniqueId() {
        return (int) System.currentTimeMillis();
    }

}
