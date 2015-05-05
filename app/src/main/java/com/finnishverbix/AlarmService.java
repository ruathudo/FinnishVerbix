package com.finnishverbix;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

/**
 * Create the Alarm Service for daily notification.
 */
public class AlarmService extends Service {


    private NotificationManager notificationManager;

    @Override
    public IBinder onBind(Intent intent) {
        Log.d("SERVICE ", " ON Bind ");
        return null;
    }

    @Override
    public void onCreate() {
        Log.d("SERVICE ", " ON Create ");
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }

    @SuppressWarnings("static-access")
    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.d("SERVICE ", " ON START ");

        //Set  up the notification manager
        notificationManager = (NotificationManager) this.getApplicationContext().getSystemService(this.getApplicationContext().NOTIFICATION_SERVICE);
        Intent intent1 = new Intent(this.getApplicationContext(),MainActivity.class);

        //Set up the notification
        Notification notification = new Notification(R.drawable.finlan_icon,"Time to review your favorite words!", System.currentTimeMillis());
        intent1.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingNotificationIntent = PendingIntent.getActivity( this.getApplicationContext(),0, intent1,PendingIntent.FLAG_UPDATE_CURRENT);
        notification.flags |= Notification.FLAG_AUTO_CANCEL;
        notification.setLatestEventInfo(this.getApplicationContext(), "Finnish Verbix", "Time to review your favorite words!", pendingNotificationIntent);

        notificationManager.notify(0, notification);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
