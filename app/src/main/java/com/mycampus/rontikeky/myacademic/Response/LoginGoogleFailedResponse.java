package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 17/10/2017.
 */

public class LoginGoogleFailedResponse {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("email")
    @Expose
    public String email;

    public String getStatus() {
        return status;
    }

    public String getMsg() {
        return msg;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }
}
