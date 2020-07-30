package com.modernfarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class JenisList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_jenis")
    @Expose
    private String namaJenis;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }
}
