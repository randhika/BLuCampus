package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 05/08/2017.
 */
public class SignupUserResponse {
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
    @SerializedName("ni")
    @Expose
    public String ni;
    @SerializedName("id_status")
    @Expose
    public Integer idStatus;
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

    public SignupUserResponse() {

    }

    public Integer getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getEmail() {
        return email;
    }

    public String getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }


    public String getUsername() {
        return username;
    }

    public String getNi() {
        return ni;
    }

    public Integer getIdStatus() {
        return idStatus;
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
}
