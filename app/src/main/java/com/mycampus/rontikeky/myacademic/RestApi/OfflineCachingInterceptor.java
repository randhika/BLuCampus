package com.mycampus.rontikeky.myacademic.RestApi;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Anggit on 27/08/2017.
 */

public class OfflineCachingInterceptor implements Interceptor{
    @Override
    public Response intercept(Chain chain) throws IOException {

        Request request = chain.request();
        //Checking if the device is online
//        if (!(Util.isOnline())) {
            // 1 day stale
            int maxStale = 86400;
            request = request.newBuilder()
                    .header("Cache-Control", "public, only-if-cached, max-stale=" + maxStale)
                    .build();
//        }

        return chain.proceed(request);
    }
}
