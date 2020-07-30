package com.modernfarm.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class FarmList {
    @SerializedName("id")
    @Expose
    private String id;
    @SerializedName("nama_tanaman")
    @Expose
    private String namaTanaman;
    @SerializedName("nama_jenis")
    @Expose
    private String namaJenis;
    @SerializedName("deskripsi")
    @Expose
    private String deskripsi;
    @SerializedName("caratanam")
    @Expose
    private String caratanam;
    @SerializedName("gambar")
    @Expose
    private String gambar;
    @SerializedName("populer")
    @Expose
    private String populer;
    @SerializedName("created_at")
    @Expose
    private String createdAt;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNamaTanaman() {
        return namaTanaman;
    }

    public void setNamaTanaman(String namaTanaman) {
        this.namaTanaman = namaTanaman;
    }

    public String getNamaJenis() {
        return namaJenis;
    }

    public void setNamaJenis(String namaJenis) {
        this.namaJenis = namaJenis;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getCaratanam() {
        return caratanam;
    }

    public void setCaratanam(String caratanam) {
        this.caratanam = caratanam;
    }

    public String getGambar() {
        return gambar;
    }

    public void setGambar(String gambar) {
        this.gambar = gambar;
    }

    public String getPopuler() {
        return populer;
    }

    public void setPopuler(String populer) {
        this.populer = populer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
