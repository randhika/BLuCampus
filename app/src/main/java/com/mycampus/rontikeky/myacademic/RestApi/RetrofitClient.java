package com.mycampus.rontikeky.myacademic.RestApi;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by Anggit on 01/08/2017.
 */
public class RetrofitClient {
    private static Retrofit retrofit = null;



    public static Retrofit getClient(String baseUrl) {
        Gson gson = new GsonBuilder().setLenient().create();

//        OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
//        httpClient.addInterceptor(new Interceptor() {
//            @Override
//            public Response intercept(Interceptor.Chain chain) throws IOException {
//                Request original = chain.request();
//
//                // Request customization: add request headers
//                Request.Builder requestBuilder = original.newBuilder()
//                        .addHeader("Content/Type", "application/json")
//                        .addHeader("Accept","application/json"); // <-- this is the important line
//
//                Request request = requestBuilder.build();
//                return chain.proceed(request);
//            }
//        });
//
//        OkHttpClient client = httpClient.build();

        if (retrofit==null) {
            retrofit = new Retrofit.Builder()
                    .client(new OkHttpTimeout().getRequestHeader())
                    .baseUrl(baseUrl)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build();
        }
        return retrofit;
    }
}
