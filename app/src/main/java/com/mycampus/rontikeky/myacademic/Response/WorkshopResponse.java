package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Anggit on 12/08/2017.
 */
public class WorkshopResponse {
    @SerializedName("current_page")
    @Expose
    public Integer currentPage;
    @SerializedName("data")
    @Expose
    public List<Datum> data = null;
    @SerializedName("from")
    @Expose
    public Integer from;
    @SerializedName("last_page")
    @Expose
    public Integer lastPage;
    @SerializedName("next_page_url")
    @Expose
    public Object nextPageUrl;
    @SerializedName("path")
    @Expose
    public String path;
    @SerializedName("per_page")
    @Expose
    public Integer perPage;
    @SerializedName("prev_page_url")
    @Expose
    public Object prevPageUrl;
    @SerializedName("to")
    @Expose
    public Integer to;
    @SerializedName("total")
    @Expose
    public Integer total;

    /**
     * Created by Anggit on 09/08/2017.
     */
    public static class Datum {
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

        public Datum(Integer id, String judulAcara, String isiAcara, String tempatAcara, Integer biayaAcara, String contactPersonAcara, Integer jumlahPeserta, String tanggalAcara, String jamAcara, String slug, String fotoAcara, Integer jumlahPesertaSisa, String daftar,String statusAcara, String batasAkhirDaftar) {
            this.id = id;
            this.judulAcara = judulAcara;
            this.isiAcara = isiAcara;
            this.tempatAcara = tempatAcara;
            this.biayaAcara = biayaAcara;
            this.contactPersonAcara = contactPersonAcara;
            this.jumlahPeserta = jumlahPeserta;
            this.tanggalAcara = tanggalAcara;
            this.jamAcara = jamAcara;
            this.slug = slug;
            this.fotoAcara = fotoAcara;
            this.jumlahPesertaSisa = jumlahPesertaSisa;
            this.daftar = daftar;
            this.batasAkhirDaftar = batasAkhirDaftar;
            this.statusAcara = statusAcara;
        }
    }

    public Integer getCurrentPage() {
        return currentPage;
    }

    public List<Datum> getData() {
        return data;
    }

    public Integer getFrom() {
        return from;
    }

    public Integer getLastPage() {
        return lastPage;
    }

    public Object getNextPageUrl() {
        return nextPageUrl;
    }

    public String getPath() {
        return path;
    }

    public Integer getPerPage() {
        return perPage;
    }

    public Object getPrevPageUrl() {
        return prevPageUrl;
    }

    public Integer getTo() {
        return to;
    }

    public Integer getTotal() {
        return total;
    }
}
