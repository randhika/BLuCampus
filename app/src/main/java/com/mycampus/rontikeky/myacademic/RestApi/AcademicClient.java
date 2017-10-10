package com.mycampus.rontikeky.myacademic.RestApi;

import com.google.gson.JsonObject;
import com.mycampus.rontikeky.myacademic.Request.ChangePasswordRequest;
import com.mycampus.rontikeky.myacademic.Request.ChangeProfileDisplayRequest;
import com.mycampus.rontikeky.myacademic.Request.EditProfileRequest;
import com.mycampus.rontikeky.myacademic.Request.LoginRequest;
import com.mycampus.rontikeky.myacademic.Request.PresenceUserRequest;
import com.mycampus.rontikeky.myacademic.Request.ResetPasswordRequest;
import com.mycampus.rontikeky.myacademic.Request.SignupGuestRequest;
import com.mycampus.rontikeky.myacademic.Request.SignupUserRequest;
import com.mycampus.rontikeky.myacademic.Response.AndroidUpdateResponse;
import com.mycampus.rontikeky.myacademic.Response.ChangeDisplayProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ChangePasswordResponse;
import com.mycampus.rontikeky.myacademic.Response.DaftarAcaraResponse;
import com.mycampus.rontikeky.myacademic.Response.DetailSeminarResponse;
import com.mycampus.rontikeky.myacademic.Response.DetailWorkshopResponse;
import com.mycampus.rontikeky.myacademic.Response.EditProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.EventRegisteredUserResponse;
import com.mycampus.rontikeky.myacademic.Response.HottestEventResponse;
import com.mycampus.rontikeky.myacademic.Response.InfoResponse;
import com.mycampus.rontikeky.myacademic.Response.LoginResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceResponse;
import com.mycampus.rontikeky.myacademic.Response.PresenceUserResponse;
import com.mycampus.rontikeky.myacademic.Response.ProfileResponse;
import com.mycampus.rontikeky.myacademic.Response.ResetPasswordResponse;
import com.mycampus.rontikeky.myacademic.Response.SeminarResponse;
import com.mycampus.rontikeky.myacademic.Response.SignupGuestResponse;
import com.mycampus.rontikeky.myacademic.Response.SignupUserResponse;
import com.mycampus.rontikeky.myacademic.Response.WorkshopResponse;

import org.json.JSONObject;

import java.util.List;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Streaming;

/**
 * Created by Anggit on 07/08/2017.
 */
public interface AcademicClient {
    //FTI ; FT ; FISIP ; FE ; FIKOM ; BAAK

    @Headers("Accept: application/json")
    @POST("auth/signup")
    Call<SignupUserResponse> dosignup(@Body SignupUserRequest signupUserRequest);

    @Headers("Accept: application/json")
    @POST("auth/signup")
    Call<SignupGuestResponse> dosignupGuest(@Body SignupGuestRequest signupGuestRequest);

    @Headers("Accept: application/json")
    @POST("auth/signin")
    Call<LoginResponse> doLogin(@Body LoginRequest loginRequest);

    @POST("auth/forget")
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

    @GET("info/tag/karir/cari")
    Call<InfoResponse> getKarir(@Query("tanggal_info") String date);

    @GET("info/tag/kemahasiswaan/cari")
    Call<InfoResponse> getKemahasiswaan(@Query("tanggal_info") String date);

    @GET("info/tag/pascasarjana/cari")
    Call<InfoResponse> getPascarjana(@Query("tanggal_info") String date);

    @GET("info/tag/{faculty}/cari")
    Call<InfoResponse> getInfoFaculty(@Path("faculty") String faculty);

    @GET("acara/hotnews")
    Call<HottestEventResponse> getHotEvent();

    @GET("profile")
    Call<ProfileResponse> getProfile();

    @GET("profile/acara")
    Call<EventRegisteredUserResponse> getResgisteredEvent();

    @GET("profile/acara")
    Call<SeminarResponse> getRegisteredEventProfile();

    @PATCH("profile")
    Call<EditProfileResponse> doEditProfile(@Body EditProfileRequest editProfileRequest);

    @PATCH("profile/password")
    Call<ChangePasswordResponse> doChangePassword(@Body ChangePasswordRequest changePasswordRequest);

    @PATCH("profile/foto")
    Call<ChangeDisplayProfileResponse> doChangeFoto(@Body ChangeProfileDisplayRequest changeProfileDisplayRequest);

    @POST("acara/{slug}/daftar")
    Call<DaftarAcaraResponse> doRegisterEvent(@Path("slug") String slug);

    @DELETE("acara/{slug}/daftar")
    Call<DaftarAcaraResponse> doCancelEvent(@Path("slug") String slug);

    @GET("eo")
    Call<SeminarResponse> getEoResponse();

    @GET("eo/{slug}/user")
    Call<List<PresenceResponse>> getUserPresence(@Path("slug") String slug);

    @PATCH("eo/{slug}/user")
    Call<PresenceUserResponse> doUserPresence(@Path("slug") String slug,@Body RequestBody requestBody);

    @GET("eo/{slug}/user/status/cetak/{format}")
    @Streaming
    Call<ResponseBody> downloadPresenceUser(@Path("slug") String Slug,@Path("format") String Format);

    @GET("android")
    Call<AndroidUpdateResponse> getAndroidVersion();
}
