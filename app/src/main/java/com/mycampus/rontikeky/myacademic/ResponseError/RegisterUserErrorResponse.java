package com.mycampus.rontikeky.myacademic.ResponseError;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Anggit on 29/09/2017.
 */

public class RegisterUserErrorResponse {
    @SerializedName("username")
    @Expose
    public List<String> username = null;
    @SerializedName("email")
    @Expose
    public List<String> email = null;
    @SerializedName("ni")
    @Expose
    public List<String> ni = null;

    public List<String> getUsername() {
        return username;
    }

    public List<String> getEmail() {
        return email;
    }

    public List<String> getNi() {
        return ni;
    }
}
