package com.example.Bonanza.Retrofit;

import android.util.Log;

import com.example.Bonanza.Interface_API.ApiService;
import com.google.gson.GsonBuilder;

import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class ApiClient {
    private static final String BASE_URL = "https://wstif23.myhost.id/kelas_bws/cafe-bonanza/CafeBonanza/app/controllers/api/";
    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .addInterceptor(chain -> {
                        Response response = chain.proceed(chain.request());
                        Log.d("RawJson", response.peekBody(Long.MAX_VALUE).string()); // Respons mentah tanpa menutup body
                        return response;
                    })
                    .build();

            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create(new GsonBuilder().setLenient().create()))
                    .client(okHttpClient)
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}



