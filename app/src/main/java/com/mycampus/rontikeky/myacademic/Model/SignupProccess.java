package com.mycampus.rontikeky.myacademic.Model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 02/08/2017.
 */
public class SignupProccess {

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
    public String tanggal_lahir;
    @SerializedName("jenis_kelamin")
    @Expose
    public String jenis_kelamin;
    @SerializedName("alamat")
    @Expose
    public String alamat;
    @SerializedName("updated_at")
    @Expose
    public String updated_at;
    @SerializedName("created_at")
    @Expose
    public String created_at;
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("message")
    @Expose
    public String[] message;
    private String password;
    private String nim;


    public SignupProccess(String username, String nama, String email, String password, String tanggal_lahir, String jenis_kelamin, String alamat, String nim) {
        this.username = username;
        this.nama = nama;
        this.email = email;
        this.password = password;
        this.tanggal_lahir = tanggal_lahir;
        this.jenis_kelamin = jenis_kelamin;
        this.alamat = alamat;
        this.nim = nim;
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

    public String getTanggal_lahir() {
        return tanggal_lahir;
    }

    public String getJenis_kelamin() {
        return jenis_kelamin;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public String getCreated_at() {
        return created_at;
    }

    public Integer getId() {
        return id;
    }

    public String getStatus() {
        return status;
    }

    public String[] getMessage() {
        return message;
    }

    public String getPassword() {
        return password;
    }

    public String getNim() {
        return nim;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setTanggal_lahir(String tanggal_lahir) {
        this.tanggal_lahir = tanggal_lahir;
    }

    public void setJenis_kelamin(String jenis_kelamin) {
        this.jenis_kelamin = jenis_kelamin;
    }

    public void setAlamat(String alamat) {
        this.alamat = alamat;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setMessage(String[] message) {
        this.message = message;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setNim(String nim) {
        this.nim = nim;
    }
}
