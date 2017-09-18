package com.mycampus.rontikeky.myacademic.Service;

import android.app.NotificationManager;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;
import com.google.firebase.messaging.FirebaseMessagingService;
import com.mycampus.rontikeky.myacademic.R;

/**
 * Created by Anggit on 16/09/2017.
 */

public class MyFirebaseMessagingService extends FirebaseMessagingService{

    private static final String TAG = "FCM SERVICES";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"From : "+remoteMessage.getFrom());
        Log.d(TAG,"Notification Message Body : "+remoteMessage.getNotification().getBody());

        NotificationCompat.Builder builder = new  NotificationCompat.Builder(this)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("BluCampus")
                .setContentText(remoteMessage.getData().get("Ada event tarbaru loh di BluCampus"));
        NotificationManager manager = (NotificationManager)     getSystemService(NOTIFICATION_SERVICE);
        manager.notify(0, builder.build());
    }

}
