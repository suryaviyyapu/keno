package com.bl4k3.keno;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import static android.app.NotificationManager.IMPORTANCE_DEFAULT;


/*Sending notification to user daily*/
public class AlarmReceiver extends BroadcastReceiver {
    private static final String CHANNEL_ID = "com.bl4k3.Keno.channelId";

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onReceive(Context context, Intent intent) {
        if ("android.intent.action.BOOT_COMPLETED".equals(intent.getAction())) {
            Intent notificationIntent = new Intent(context, AttendanceActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AttendanceActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder builder = new Notification.Builder(context);

            Notification notification = builder.setContentTitle("Alert!!!")
                    .setContentText("Did you attend your college today?")
                    .setTicker("New Attendance Alert!")
                    .setSmallIcon(R.drawable.logo)

                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)

                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Attendance",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notification);

        } else {
            Intent notificationIntent = new Intent(context, AttendanceActivity.class);

            TaskStackBuilder stackBuilder = TaskStackBuilder.create(context);
            stackBuilder.addParentStack(AttendanceActivity.class);
            stackBuilder.addNextIntent(notificationIntent);

            PendingIntent pendingIntent = stackBuilder.getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

            Notification.Builder builder = new Notification.Builder(context);

            Notification notification = builder.setContentTitle("Alert!!!")
                    .setContentText("Did you attend your college today?")
                    .setTicker("New Attendance Alert!")
                    .setSmallIcon(R.drawable.logo)

                    .setCategory(NotificationCompat.CATEGORY_REMINDER)
                    .setVisibility(Notification.VISIBILITY_PUBLIC)

                    .setAutoCancel(true)
                    .setContentIntent(pendingIntent).build();
            notification.defaults |= Notification.DEFAULT_SOUND;
            notification.defaults |= Notification.DEFAULT_VIBRATE;

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                builder.setChannelId(CHANNEL_ID);
            }

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                NotificationChannel channel = new NotificationChannel(
                        CHANNEL_ID,
                        "Attendance",
                        NotificationManager.IMPORTANCE_HIGH
                );
                notificationManager.createNotificationChannel(channel);
            }

            notificationManager.notify(0, notification);
        }
    }
}
