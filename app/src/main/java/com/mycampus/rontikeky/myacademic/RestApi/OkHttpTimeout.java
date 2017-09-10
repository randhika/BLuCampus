package com.mycampus.rontikeky.myacademic.RestApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Anggit on 05/08/2017.
 */
public class OkHttpTimeout {
    public OkHttpClient getRequestHeader() {
        return new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .build();
    }
}
