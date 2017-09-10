package com.mycampus.rontikeky.myacademic.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 07/08/2017.
 */
public class SignupUserRequest {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("password")
    @Expose
    public String password;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("nim")
    @Expose
    public String nim;

    public SignupUserRequest(String username, String nama, String email, String password, String telepon, String nim) {
        this.username = username;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.telepon = telepon;
        this.nim = nim;
    }
}
