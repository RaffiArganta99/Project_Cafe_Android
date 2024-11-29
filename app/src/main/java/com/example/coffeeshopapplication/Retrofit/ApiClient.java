package com.example.coffeeshopapplication.Retrofit;

import com.example.coffeeshopapplication.Interface_API.ApiService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiClient {
    // Ganti dengan URL API yang benar
    private static final String BASE_URL = "http://192.168.1.6/CafeBonanza/app/controllers/";
    private static Retrofit retrofit = null;

    public static ApiService getApiService() {
        if (retrofit == null) {
            // Tambahkan HttpLoggingInterceptor untuk logging
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY); // Pilih tingkat log

            // Tambahkan interceptor ke OkHttpClient
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor) // Logging
                    .build();

            // Bangun Retrofit instance dengan OkHttpClient
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client) // Pasang OkHttpClient yang sudah diatur
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit.create(ApiService.class);
    }
}
