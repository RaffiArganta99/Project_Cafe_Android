package com.example.coffeeshopapplication.Interface_API;

import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Product;

import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("MenuApi.php")
    Call<MenuResponse> getMenu();

    @POST("MenuApi.php")
    Call<Void> addCartItem(@Body Product product);

    @PUT("MenuApi.php/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body Product product);

    @DELETE("MenuApi.php/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);
}




