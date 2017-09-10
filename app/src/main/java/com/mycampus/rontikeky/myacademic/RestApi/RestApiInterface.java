package com.mycampus.rontikeky.myacademic.RestApi;

import com.mycampus.rontikeky.myacademic.Model.SigninProcess;
import com.mycampus.rontikeky.myacademic.Model.SignupGuest;
import com.mycampus.rontikeky.myacademic.Response.SignupUserResponse;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Headers;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

/**
 * Created by Anggit on 01/08/2017.
 */
public interface RestApiInterface {

    @POST("signin")
    Call<SigninProcess> doSignin(@Query("username") String username, @Query("password") String password);


    @Headers("Content-Type: application/json")
    @Multipart
    @POST("signup")
    Call<SignupUserResponse> doSignup(@Part("username") RequestBody username, @Part("nama") RequestBody nama,
                                  @Part("email") RequestBody email,@Part("password") RequestBody password,
                                  @Part("tanggal_lahir") RequestBody tanggal_lahir,@Part("jenis_kelamin") RequestBody jenis_kelamin,
                                  @Part("alamat") RequestBody alamat,@Part("nim") RequestBody nim
                                  );

    @POST("signup")
    Call<SignupGuest> doSignup(@Body SignupGuest signupGuest);
}
