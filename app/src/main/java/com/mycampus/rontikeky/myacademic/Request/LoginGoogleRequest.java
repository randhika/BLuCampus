package com.mycampus.rontikeky.myacademic.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 17/10/2017.
 */

public class LoginGoogleRequest {
    @SerializedName("token")
    @Expose
    public String token;

    public LoginGoogleRequest(String token) {
        this.token = token;
    }

}
