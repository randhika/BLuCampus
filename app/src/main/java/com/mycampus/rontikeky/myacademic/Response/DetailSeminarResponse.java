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
    public String biayaAcara;
    @SerializedName("contact_person_acara")
    @Expose
    public String contactPersonAcara;
    @SerializedName("jumlah_peserta")
    @Expose
    public String jumlahPeserta;
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
    @SerializedName("id_user")
    @Expose
    public String idUser;
    @SerializedName("id_tag")
    @Expose
    public String idTag;
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

    public String getIdUser() {
        return idUser;
    }

    public String getIdTag() {
        return idTag;
    }

    public String getTempatAcara() {
        return tempatAcara;
    }

    public String getBiayaAcara() {
        return biayaAcara;
    }

    public String getContactPersonAcara() {
        return contactPersonAcara;
    }

    public String getJumlahPeserta() {
        return jumlahPeserta;
    }

    public Integer getJumlahPesertaSisa() {
        return jumlahPesertaSisa;
    }

    public String getDaftar() {
        return daftar;
    }
}
