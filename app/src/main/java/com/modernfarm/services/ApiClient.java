package com.modernfarm.services;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Inisialisasi Retrofit untuk memudahkan komunikasi dengan REST API yang udah dibuat

    public static final String BASE_URL = "http://modernfarm.000webhostapp.com/backend-modernfarm/";
    private static Retrofit retrofit;

    public static Retrofit getApiClient(){
        if (retrofit==null){
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
