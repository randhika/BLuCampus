package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 07/10/2017.
 */

public class PresenceResponse {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("nama")
    @Expose
    public String nama;
    @SerializedName("username")
    @Expose
    public String username;
    @SerializedName("telepon")
    @Expose
    public String telepon;
    @SerializedName("pivot")
    @Expose
    public Pivot pivot;

    public PresenceResponse(Integer id, String nama, String username, String telepon, Pivot pivot) {
        this.id = id;
        this.nama = nama;
        this.username = username;
        this.telepon = telepon;
        this.pivot = pivot;
    }

    public static class Pivot {
        @SerializedName("id_acara")
        @Expose
        public Integer idAcara;
        @SerializedName("id_user")
        @Expose
        public Integer idUser;
        @SerializedName("status_user")
        @Expose
        public Integer statusUser;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

        public Pivot(Integer idAcara, Integer idUser, Integer statusUser, String createdAt, String updatedAt) {
            this.idAcara = idAcara;
            this.idUser = idUser;
            this.statusUser = statusUser;
            this.createdAt = createdAt;
            this.updatedAt = updatedAt;
        }

        public Integer getIdAcara() {
            return idAcara;
        }

        public Integer getIdUser() {
            return idUser;
        }

        public Integer getStatusUser() {
            return statusUser;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
        }

        public void setIdAcara(Integer idAcara) {
            this.idAcara = idAcara;
        }

        public void setIdUser(Integer idUser) {
            this.idUser = idUser;
        }

        public void setStatusUser(Integer statusUser) {
            this.statusUser = statusUser;
        }

        public void setCreatedAt(String createdAt) {
            this.createdAt = createdAt;
        }

        public void setUpdatedAt(String updatedAt) {
            this.updatedAt = updatedAt;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getNama() {
        return nama;
    }

    public String getUsername() {
        return username;
    }

    public String getTelepon() {
        return telepon;
    }

    public Pivot getPivot() {
        return pivot;
    }
}
