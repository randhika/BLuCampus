package com.mycampus.rontikeky.myacademic.RestApi;

import com.mycampus.rontikeky.myacademic.Model.Model;

import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by Anggit on 30/07/2017.
 */
public interface UserApiInterface {
    @GET("user")
    Call<Model> getUsers();
}
