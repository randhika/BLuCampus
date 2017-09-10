package com.mycampus.rontikeky.myacademic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 01/08/2017.
 */
public class Signup {
    @SerializedName("status")
    @Expose
    private Boolean status;
    @SerializedName("message")
    @Expose
    private SignupResponse signupResponse;

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }

    public SignupResponse getMessage() {
        return signupResponse;
    }

    public void setMessage(SignupResponse message) {
        this.signupResponse = message;
    }

    /**
     * Created by Anggit on 01/08/2017.
     */
    public static class SignupResponse {
        @SerializedName("username")
        @Expose
        private String username;
        @SerializedName("nama")
        @Expose
        private String nama;
        @SerializedName("email")
        @Expose
        private String email;
        @SerializedName("tanggal_lahir")
        @Expose
        private String tanggalLahir;
        @SerializedName("jenis_kelamin")
        @Expose
        private String jenisKelamin;
        @SerializedName("alamat")
        @Expose
        private String alamat;
        @SerializedName("updated_at")
        @Expose
        private String updatedAt;
        @SerializedName("created_at")
        @Expose
        private String createdAt;
        @SerializedName("id")
        @Expose
        private Integer id;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getNama() {
            return nama;
        }

        public void setNama(String nama) {
            this.nama = nama;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getTanggalLahir() {
            return tanggalLahir;
        }

        public void setTanggalLahir(String tanggalLahir) {
            this.tanggalLahir = tanggalLahir;
        }

        public String getJenisKelamin() {
            return jenisKelamin;
        }

        public void setJenisKelamin(String jenisKelamin) {
            this.jenisKelamin = jenisKelamin;
        }

        public String getAlamat() {
            return alamat;
        }

        public void setAlamat(String alamat) {
            this.alamat = alamat;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public Integer getId() {
            return id;
        }

        public void setId(Integer id) {
            this.id = id;
        }
    }

    @Override
    public String toString() {
        return "Signup{" +
                "status=" + status +
                ", signupResponse=" + signupResponse +
                '}';
    }
}
