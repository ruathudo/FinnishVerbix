package com.finnishverbix;

import android.app.Notification;
import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * The class create a broadcast receiver to start the service.
 */
public class AlarmReceiver extends BroadcastReceiver {


    @Override
    public void onReceive(Context context, Intent intent) {
        //Create an intent to start up the service.
        Intent service1 = new Intent(context,AlarmService.class);
        context.startService(service1);
        Log.d("ALARM RECEIVER","start service" );
    }
}
