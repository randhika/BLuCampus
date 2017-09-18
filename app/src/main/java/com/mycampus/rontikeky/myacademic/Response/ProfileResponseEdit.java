package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.lang.reflect.Type;

/**
 * Created by Anggit on 17/09/2017.
 */

public class ProfileResponseEdit{
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("tanggal_lahir")
    @Expose
    public String tanggalLahir;
    @SerializedName("jenis_kelamin")
    @Expose
    public Object jenisKelamin;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("mahasiswa")
    @Expose
    public ProfileEditMahasiswaResponse mahasiswa;
}
