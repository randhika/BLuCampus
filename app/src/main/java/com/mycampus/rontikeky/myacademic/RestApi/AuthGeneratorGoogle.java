package com.mycampus.rontikeky.myacademic.RestApi;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anggit on 15/10/2017.
 */

public class AuthGeneratorGoogle {
    //private static final String BASE_URL = "http://anggitprayogo.com/public/api/auth/";
    //private static final String BASE_URL = "https://mycampus.rontikeky.com/api/auth/";

    //FIX
    //private static final String BASE_URL = "https://mycampus.rontikeky.com/api/";
    //private static final String BASE_URL = "https://testingmycampus.rontikeky.com/api/";
    private  static final String BASE_URL = " http://testing.anggitprayogo.com/api/";

    //Define BASE URL to Request and get Response Where its convert to JSON using Gson
    private static Retrofit.Builder builder =
            new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

    private static Retrofit retrofit = builder.build();

    private static HttpLoggingInterceptor logging =
            new HttpLoggingInterceptor()
                    .setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient.Builder httpClient =
            new OkHttpClient.Builder()
                    .readTimeout(60, TimeUnit.SECONDS)
                    .connectTimeout(60, TimeUnit.SECONDS);



    public static <S> S createService(
            Class<S> serviceClass) {

        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            builder.client(httpClient.build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }

    public static Retrofit retrofit() {
        OkHttpClient client = httpClient.build();
        return builder.client(client).build();
    }
}
