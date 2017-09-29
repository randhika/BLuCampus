package com.mycampus.rontikeky.myacademic.RestApi;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.mycampus.rontikeky.myacademic.Response.ProfileEditMahasiswaResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponseEdit;

import java.io.IOException;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anggit on 29/08/2017.
 */

public class ServiceGeneratorAuth3 {
    public static <S> S createService(
            Class<S> serviceClass, final String authToken, Context context) {

        //private static final String BASE_URL = "http://anggitprayogo.com/public/api/";
        //final String BASE_URL = "https://mycampus.rontikeky.com/api/";
        final String BASE_URL = "https://testingmycampus.rontikeky.com/api/";

        int cacheSize = 10 * 1024 * 1024;
        Cache cache = new Cache(context.getCacheDir(),cacheSize);



        OkHttpClient.Builder httpClient =
                new OkHttpClient.Builder();


        OkHttpClient client = httpClient.cache(cache).build();

        //Define BASE URL to Request and get Response Where its convert to JSON using Gson
        Retrofit.Builder builder =
                new Retrofit.Builder().baseUrl(BASE_URL).addConverterFactory(GsonConverterFactory.create());

        Retrofit retrofit = builder.build();

        HttpLoggingInterceptor logging =
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BODY);


        if (!httpClient.interceptors().contains(logging)) {
            httpClient.addInterceptor(logging);
            httpClient.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Interceptor.Chain chain) throws IOException {
                    Request original = chain.request();

                    // Request customization: add request headers
                    Request.Builder requestBuilder = original.newBuilder()
                            .header("Authorization", "Bearer "+authToken); // <-- this is the important line

                    Request request = requestBuilder.build();
                    return chain.proceed(request);
                }
            });

            builder.client(httpClient.cache(cache).build());
            retrofit = builder.build();
        }

        return retrofit.create(serviceClass);
    }
}
