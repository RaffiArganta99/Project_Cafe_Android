package com.example.coffeeshopapplication.Interface_API;

import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Model.LoginResponse;
import com.example.coffeeshopapplication.Model.User;
import com.example.coffeeshopapplication.Product;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
    // Endpoint untuk mendapatkan menu
    @GET("MenuApi.php")
    Call<MenuResponse> getMenu();

    // Endpoint untuk menambahkan item ke keranjang
    @POST("cart/item")
    Call<Void> addCartItem(@Body Product product);

    // Endpoint untuk memperbarui item di keranjang
    @PUT("cart/item/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body Product product);

    // Endpoint untuk menghapus item dari keranjang
    @DELETE("cart/item/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);


    // Endpoint untuk login menggunakan parameter FormUrlEncoded
    @FormUrlEncoded
    @POST("EmployeeApi.php")
    Call<LoginResponse> login(
            @Query("action") String action, // Query parameter untuk action=login
            @Field("Email") String email,
            @Field("Password") String password
    );
}
