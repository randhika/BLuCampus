package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 10/08/2017.
 */
public class ProfileResponse {
//    @SerializedName("id")
//    @Expose
//    public Integer id;
//    @SerializedName("username")
//    @Expose
//    public String username;
//    @SerializedName("nama")
//    @Expose
//    public String nama;
//    @SerializedName("email")
//    @Expose
//    public String email;
//    @SerializedName("tanggal_lahir")
//    @Expose
//    public String tanggalLahir;
//    @SerializedName("jenis_kelamin")
//    @Expose
//    public String jenisKelamin;
//    @SerializedName("alamat")
//    @Expose
//    public String alamat;
//    @SerializedName("telepon")
//    @Expose
//    public String telepon;
//    @SerializedName("id_otorisasi")
//    @Expose
//    public Integer idOtorisasi;
//    @SerializedName("created_at")
//    @Expose
//    public String createdAt;
//    @SerializedName("updated_at")
//    @Expose
//    public String updatedAt;
//
//    public Integer getId() {
//        return id;
//    }
//
//    public String getUsername() {
//        return username;
//    }
//
//    public String getNama() {
//        return nama;
//    }
//
//    public String getEmail() {
//        return email;
//    }
//
//    public String getTanggalLahir() {
//        return tanggalLahir;
//    }
//
//    public String getJenisKelamin() {
//        return jenisKelamin;
//    }
//
//    public String getAlamat() {
//        return alamat;
//    }
//
//    public String getTelepon() {
//        return telepon;
//    }
//
//    public Integer getIdOtorisasi() {
//        return idOtorisasi;
//    }
//
//    public String getCreatedAt() {
//        return createdAt;
//    }
//
//    public String getUpdatedAt() {
//        return updatedAt;
//    }

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
    public Mahasiswa mahasiswa;



    /**
     * Created by Anggit on 17/09/2017.
     */

    static class Mahasiswa {
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

        public Integer getIdUser() {
            return idUser;
        }

        public String getNim() {
            return nim;
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

    public Object getJenisKelamin() {
        return jenisKelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getTelepon() {
        return telepon;
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
