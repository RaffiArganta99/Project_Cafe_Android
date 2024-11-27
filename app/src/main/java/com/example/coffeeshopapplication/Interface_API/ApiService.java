package com.example.coffeeshopapplication.Interface_API;

import android.view.Menu;

import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Model.PostMenuResponse;
import com.example.coffeeshopapplication.Product;
import com.google.gson.JsonObject;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;

public interface ApiService {
    @GET("MenuApi.php")
    Call<MenuResponse> getMenu();

    @POST("MenuApi.php")
    Call<ResponseBody> addMenu(@Body JsonObject menuData);

    @PUT("MenuApi.php/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body Product product);

    @DELETE("MenuApi.php/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);
}




