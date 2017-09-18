package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 17/09/2017.
 */

public class ProfileEditMahasiswaResponse {
    @SerializedName("id_user")
    @Expose
    public Integer idUser;
    @SerializedName("nim")
    @Expose
    public String nim;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
}
