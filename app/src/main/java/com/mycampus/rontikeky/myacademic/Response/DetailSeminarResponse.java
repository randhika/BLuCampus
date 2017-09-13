package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 11/08/2017.
 */
public class DetailSeminarResponse {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("judul_acara")
    @Expose
    public String judulAcara;
    @SerializedName("isi_acara")
    @Expose
    public String isiAcara;
    @SerializedName("tempat_acara")
    @Expose
    public String tempatAcara;
    @SerializedName("biaya_acara")
    @Expose
    public Integer biayaAcara;
    @SerializedName("contact_person_acara")
    @Expose
    public String contactPersonAcara;
    @SerializedName("jumlah_peserta")
    @Expose
    public Integer jumlahPeserta;
    @SerializedName("tanggal_acara")
    @Expose
    public String tanggalAcara;
    @SerializedName("jam_acara")
    @Expose
    public String jamAcara;
    @SerializedName("slug")
    @Expose
    public String slug;
    @SerializedName("foto_acara")
    @Expose
    public String fotoAcara;
    @SerializedName("status_acara")
    @Expose
    public String statusAcara;
    @SerializedName("batas_akhir_daftar")
    @Expose
    public String batasAkhirDaftar;
    @SerializedName("id_user")
    @Expose
    public Integer idUser;
    @SerializedName("id_tag")
    @Expose
    public Integer idTag;
    @SerializedName("jumlah_peserta_sisa")
    @Expose
    public Integer jumlahPesertaSisa;
    @SerializedName("daftar")
    @Expose
    public String daftar;


    public Integer getId() {
        return id;
    }

    public String getJudulAcara() {
        return judulAcara;
    }

    public String getIsiAcara() {
        return isiAcara;
    }

    public String getTempatAcara() {
        return tempatAcara;
    }

    public Integer getBiayaAcara() {
        return biayaAcara;
    }

    public String getContactPersonAcara() {
        return contactPersonAcara;
    }

    public Integer getJumlahPeserta() {
        return jumlahPeserta;
    }

    public String getTanggalAcara() {
        return tanggalAcara;
    }

    public String getJamAcara() {
        return jamAcara;
    }

    public String getSlug() {
        return slug;
    }

    public String getFotoAcara() {
        return fotoAcara;
    }

    public String getStatusAcara() {
        return statusAcara;
    }

    public String getBatasAkhirDaftar() {
        return batasAkhirDaftar;
    }

    public Integer getIdUser() {
        return idUser;
    }

    public Integer getIdTag() {
        return idTag;
    }

    public Integer getJumlahPesertaSisa() {
        return jumlahPesertaSisa;
    }

    public String getDaftar() {
        return daftar;
    }
}
