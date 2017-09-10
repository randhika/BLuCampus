package com.mycampus.rontikeky.myacademic.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 14/08/2017.
 */
public class EditProfileRequest {
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("tanggal_lahir")
    @Expose
    public String tanggalLahir;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("telepon")
    @Expose
    public String telepon;

    public EditProfileRequest(String nama, String tanggalLahir, String alamat, String telp) {
        this.nama = nama;
        this.tanggalLahir = tanggalLahir;
        this.alamat = alamat;
        this.telepon = telp;
    }
}
