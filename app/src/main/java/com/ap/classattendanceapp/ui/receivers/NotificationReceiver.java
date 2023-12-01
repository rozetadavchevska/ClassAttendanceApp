package com.ap.classattendanceapp.ui.receivers;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.ap.classattendanceapp.R;

//public class NotificationReceiver extends BroadcastReceiver {
//    @Override
//    public void onReceive(Context context, Intent intent) {
//        String className = intent.getStringExtra("className");
//        showNotification(context, "Class Reminder", "Your " + className + " class is starting soon!");
//    }
//
//    private void showNotification(Context context, String title, String content) {
//        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
//
//        NotificationCompat.Builder builder;
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            NotificationChannelHelper.createNotificationChannel(context);
//            builder = new NotificationCompat.Builder(context, NotificationChannelHelper.CHANNEL_ID);
//        } else {
//            builder = new NotificationCompat.Builder(context);
//        }
//
//        builder.setContentTitle(title)
//                .setContentText(content)
//                .setSmallIcon(R.drawable.ic_notification)
//                .setPriority(NotificationCompat.PRIORITY_DEFAULT);
//
//        Notification notification = builder.build();
//        notificationManager.notify(0, notification);
//    }
//}
