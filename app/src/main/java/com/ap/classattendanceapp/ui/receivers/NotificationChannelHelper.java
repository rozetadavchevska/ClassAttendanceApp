package com.ap.classattendanceapp.ui.receivers;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;

import android.content.Context;

public class NotificationChannelHelper {
    public static final String CHANNEL_ID = "class_reminder_channel";
    public static final String CHANNEL_NAME = "Class Reminders";

    public static void createNotificationChannel(Context context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    CHANNEL_NAME,
                    NotificationManager.IMPORTANCE_HIGH);

            channel.setDescription("Class reminders channel");

            notificationManager.createNotificationChannel(channel);
        }
    }
}
