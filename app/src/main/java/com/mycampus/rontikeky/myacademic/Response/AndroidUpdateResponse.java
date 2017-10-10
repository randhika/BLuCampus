package com.mycampus.rontikeky.myacademic.Response;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 29/09/2017.
 */

public class AndroidUpdateResponse {
    @SerializedName("version_name")
    @Expose
    public String versionName;
    @SerializedName("version_code")
    @Expose
    public Integer versionCode;
    @SerializedName("status_update")
    @Expose
    public Integer statusUpdate;
    @SerializedName("date_mnt")
    @Expose
    public String dateMnt;
    @SerializedName("mnt")
    @Expose
    public Integer mnt;

    public String getVersionName() {
        return versionName;
    }

    public Integer getVersionCode() {
        return versionCode;
    }

    public Integer getStatusUpdate() {
        return statusUpdate;
    }

    public String getDateMnt() {
        return dateMnt;
    }

    public Integer getMnt() {
        return mnt;
    }
}
