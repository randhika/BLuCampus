package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 07/08/2017.
 */
public class SignupGuestResponse {
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String message;

    public SignupGuestResponse() {
    }

    public String getUsername() {
        return username;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
