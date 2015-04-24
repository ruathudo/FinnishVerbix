package com.finnishverbix;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by longtran on 4/18/2015.
 */
public class AlarmReceiver extends BroadcastReceiver {
    NotificationManager notificationManager;

    @Override
    public void onReceive(Context context, Intent intent) {
        Intent service1 = new Intent(context,AlarmService.class);
        context.startService(service1);
        Log.d("ALARM RECEIVER","start service" );
    }
}
