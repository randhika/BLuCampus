package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 10/08/2017.
 */
public class ProfileResponse {

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
    public String jenisKelamin;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("foto")
    @Expose
    public String foto;
    @SerializedName("id_status")
    @Expose
    public Integer idStatus;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("ni")
    @Expose
    public String ni;
    @SerializedName("created_at")
    @Expose
    public String createdAt;
    @SerializedName("updated_at")
    @Expose
    public String updatedAt;
    @SerializedName("status")
    @Expose
    public Mahasiswa mahasiswa;



    /**
     * Created by Anggit on 17/09/2017.
     */

    static class Mahasiswa {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("nama_status")
        @Expose
        public String namaStatus;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

        public Integer getId() {
            return id;
        }

        public String getNamaStatus() {
            return namaStatus;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }
    }

    public Integer getId() {
        return id;
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

    public String getTanggalLahir() {
        return tanggalLahir;
    }

    public String getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getFoto() {
        return foto;
    }

    public Integer getIdStatus() {
        return idStatus;
    }

    public String getTelepon() {
        return telepon;
    }

    public String getNi() {
        return ni;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public Mahasiswa getMahasiswa() {
        return mahasiswa;
    }
}
