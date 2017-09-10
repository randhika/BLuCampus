package com.mycampus.rontikeky.myacademic.RestApi;

import com.mycampus.rontikeky.myacademic.Request.ChangePasswordRequest;
import com.mycampus.rontikeky.myacademic.Request.EditProfileRequest;
import com.mycampus.rontikeky.myacademic.Request.LoginRequest;
import com.mycampus.rontikeky.myacademic.Request.ResetPasswordRequest;
import com.mycampus.rontikeky.myacademic.Request.SignupGuestRequest;
import com.mycampus.rontikeky.myacademic.Request.SignupUserRequest;
import com.mycampus.rontikeky.myacademic.Response.ChangePasswordResponse;
import com.mycampus.rontikeky.myacademic.Response.DaftarAcaraResponse;
import com.mycampus.rontikeky.myacademic.Response.DetailSeminarResponse;
import com.mycampus.rontikeky.myacademic.Response.DetailWorkshopResponse;
import com.mycampus.rontikeky.myacademic.Response.EditProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.EventRegisteredUserResponse;
import com.mycampus.rontikeky.myacademic.Response.HottestEventResponse;
import com.mycampus.rontikeky.myacademic.Response.InfoResponse;
import com.mycampus.rontikeky.myacademic.Response.LoginResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ResetPasswordResponse;
import com.mycampus.rontikeky.myacademic.Response.SeminarResponse;
import com.mycampus.rontikeky.myacademic.Response.SignupGuestResponse;
import com.mycampus.rontikeky.myacademic.Response.SignupUserResponse;
import com.mycampus.rontikeky.myacademic.Response.WorkshopResponse;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Anggit on 07/08/2017.
 */
public interface AcademicClient {
    //FTI ; FT ; FISIP ; FE ; FIKOM ; BAAK

    @POST("signup")
    Call<SignupUserResponse> dosignup(@Body SignupUserRequest signupUserRequest);

    @POST("signup")
    Call<SignupGuestResponse> dosignupGuest(@Body SignupGuestRequest signupGuestRequest);

    @POST("signin")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("forget")
    Call<ResetPasswordResponse> doReset(@Body ResetPasswordRequest resetPasswordRequest);

    @GET("acara/tag/seminar")
    Call<SeminarResponse> getSeminar();

    @GET("acara/tag/seminar")
    Call<SeminarResponse> getSeminarMore(@Query("page") int page);

    @GET("acara/{slug}")
    Call<DetailSeminarResponse> getSemanarDetail(@Path("slug") String Slug);

    @GET("acara/tag/workshop")
    Call<WorkshopResponse> getWorkshop();

    @GET("acara/tag/{slug}")
    Call<DetailWorkshopResponse> getWorkshopDetail(@Path("slug") String Slug);

//    @GET("info/{faculty}")
//    Call<InfoResponse> getInfo(@Path("faculty") String faculty);

    @GET("info/tag/baak/cari")
    Call<InfoResponse> getInfo(@Query("tanggal_info") String date);

    @GET("info/tag/fti/cari")
    Call<InfoResponse> getFTI(@Query("tanggal_info") String date);

    @GET("info/tag/ft/cari")
    Call<InfoResponse> getFT(@Query("tanggal_info") String date);

    @GET("info/tag/fe/cari")
    Call<InfoResponse> getFE(@Query("tanggal_info") String date);

    @GET("info/tag/fikom/cari")
    Call<InfoResponse> getFIKOM(@Query("tanggal_info") String date);

    @GET("info/tag/fisip/cari")
    Call<InfoResponse> getFISIP(@Query("tanggal_info") String date);

    @GET("acara/hotnews")
    Call<HottestEventResponse> getHotEvent();

    @GET("profile")
    Call<ProfileResponse> getProfile();

    @GET("profile/acara")
    Call<EventRegisteredUserResponse> getResgisteredEvent();

    @PATCH("profile")
    Call<EditProfileResponse> doEditProfile(@Body EditProfileRequest editProfileRequest);

    @PATCH("profile/password")
    Call<ChangePasswordResponse> doChangePassword(@Body ChangePasswordRequest changePasswordRequest);

    @POST("acara/{slug}/daftar")
    Call<DaftarAcaraResponse> doRegisterEvent(@Path("slug") String slug);

    @DELETE("acara/{slug}/daftar")
    Call<DaftarAcaraResponse> doCancelEvent(@Path("slug") String slug);

}
