package com.bl4k3.keno;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;
import android.util.Log;

import java.util.Calendar;

public class AlarmReceiver extends BroadcastReceiver {
    int id =0;
    String Title,Text;

    @Override
    public void onReceive(Context context, Intent i) {
        Calendar rightNow = Calendar.getInstance();
        int currentHour = rightNow.get(Calendar.HOUR_OF_DAY);
        Log.i("curent hour", String.valueOf(currentHour));
        if((currentHour>=7 && currentHour<=11) ||(currentHour>=19 && currentHour<=22)  ){
            if(currentHour<12)
            {
                Title="Want to Bunk ?";
                Text = "Remember to check your attendance..";
            }
            else
            {
                Title="Update your attendance !!";
                Text = "Did you marked your attendance today ?";
            }
            Log.i("AlarmReceiver", String.valueOf(currentHour));
            Intent intent = new Intent(context, AttendanceActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, 0);
            NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.logo)
                    .setContentTitle(Title)
                    .setContentText(Text)
                    .setStyle(new NotificationCompat.BigTextStyle()
                            .bigText(Text))
                    .setContentIntent(pendingIntent)
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT);
            NotificationManagerCompat notificationManager = NotificationManagerCompat.from(context);
            Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            mBuilder.setSound(alarmSound);
            notificationManager.notify(id, mBuilder.build());
            id++;
        }
    }
}

