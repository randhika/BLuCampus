package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

/**
 * Created by Anggit on 12/08/2017.
 */
public class InfoResponse {
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
     * Created by Anggit on 12/08/2017.
     */
    public static class Datum {
        @SerializedName("id")
        @Expose
        public Integer id;
        @SerializedName("judul_info")
        @Expose
        public String judulInfo;
        @SerializedName("tanggal_info")
        @Expose
        public String tanggalInfo;
        @SerializedName("foto_info")
        @Expose
        public String fotoInfo;
        @SerializedName("isi_info_depan")
        @Expose
        public String isiInfoDepan;
        @SerializedName("isi_info")
        @Expose
        public String isiInfo;
        @SerializedName("slug_url_info")
        @Expose
        public String slugUrlInfo;
        @SerializedName("id_tag_akademik")
        @Expose
        public Integer idTagAkademik;
        @SerializedName("created_at")
        @Expose
        public String createdAt;
        @SerializedName("updated_at")
        @Expose
        public String updatedAt;

        public Datum(String judulInfo, String isiInfo, String tanggalInfo, String slugUrlInfo, String fotoInfo, String tagHtml) {
            this.judulInfo = judulInfo;
            this.isiInfoDepan = isiInfo;
            this.tanggalInfo = tanggalInfo;
            this.slugUrlInfo = slugUrlInfo;
            this.fotoInfo = fotoInfo;
            this.isiInfo = tagHtml;
        }

        public Integer getId() {
            return id;
        }

        public String getJudulInfo() {
            return judulInfo;
        }

        public String getTanggalInfo() {
            return tanggalInfo;
        }

        public String getFotoInfo() {
            return fotoInfo;
        }

        public String getIsiInfoDepan() {
            return isiInfoDepan;
        }

        public String getIsiInfo() {
            return isiInfo;
        }

        public String getSlugUrlInfo() {
            return slugUrlInfo;
        }

        public Integer getIdTagAkademik() {
            return idTagAkademik;
        }

        public String getCreatedAt() {
            return createdAt;
        }

        public String getUpdatedAt() {
            return updatedAt;
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
