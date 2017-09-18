package com.mycampus.rontikeky.myacademic.Service;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by Anggit on 16/09/2017.
 */

public class FirebaseIDService extends FirebaseInstanceIdService{

    private static final String TAG = "FIREBASE ID SERVICE";

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);
    }
}
