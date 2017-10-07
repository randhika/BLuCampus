package com.mycampus.rontikeky.myacademic.Request;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by Anggit on 07/10/2017.
 */

public class PresenceUserRequest {
    @SerializedName("hadir")
    @Expose
    public Hadir hadir;

    public static class Hadir {
        public Hadir() {
        }
    }

    public Hadir getHadir() {
        return hadir;
    }
}
