package com.mycampus.rontikeky.myacademic.Service;

import android.util.Log;

import com.google.firebase.messaging.RemoteMessage;

/**
 * Created by Anggit on 16/09/2017.
 */

public class FirebaseMessagingService extends com.google.firebase.messaging.FirebaseMessagingService{

    private static final String TAG = "FCM SERVICES";

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        Log.d(TAG,"From : "+remoteMessage.getFrom());
        Log.d(TAG,"Notification Message Body : "+remoteMessage.getNotification().getBody());
    }

}
