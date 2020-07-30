package com.modernfarm.services;

import com.modernfarm.model.FarmListResponse;
import com.modernfarm.model.GeneralResponse;
import com.modernfarm.model.JenisListResponse;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {
    // Pada Interface ini sesuai methodnya masing masing akan memanggil Hitpoint PHP sesuai tugasnya

    @GET("farmlist.php")
    Call<FarmListResponse> getFarmList();

    @GET("farmlistfavorite.php")
    Call<FarmListResponse> getFarmListFavorite();

    @Multipart
    @POST("farmlist.php")
    Call<GeneralResponse> insertFarmList(
            @Part MultipartBody.Part gambar,
            @Part("gambar") RequestBody name,
            @Part("namatanaman") RequestBody namatanaman,
            @Part("idjenis") RequestBody idjenis,
            @Part("deskripsi") RequestBody deskripsi,
            @Part("caratanam") RequestBody caratanam
    );

    @GET("jenislist.php")
    Call<JenisListResponse> getJenis();

    @FormUrlEncoded
    @POST("jenislist.php")
    Call<GeneralResponse> insertJenis(
            @Field("namajenis") String namajenis
    );

    @FormUrlEncoded
    @POST("farmlike.php")
    Call<GeneralResponse> likeFarm(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("farmunlike.php")
    Call<GeneralResponse> unlikeFarm(
            @Field("id") String id
    );

    @FormUrlEncoded
    @POST("farmlistdelete.php")
    Call<GeneralResponse> deleteFarm(
            @Field("id") String id
    );
}
