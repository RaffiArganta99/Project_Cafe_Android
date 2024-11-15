package com.example.coffeeshopapplication.Interface_API;

import com.example.coffeeshopapplication.Product;
import com.example.coffeeshopapplication.data.MenuItem;

import java.util.List;
import retrofit2.Call;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Body;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface ApiService {
    @GET("cart/items")
    Call<List<Product>> getCartItems();

    @POST("cart/item")
    Call<Void> addCartItem(@Body Product product);

    @PUT("cart/item/{id}")
    Call<Void> updateCartItem(@Path("id") int id, @Body Product product);

    @DELETE("cart/item/{id}")
    Call<Void> deleteCartItem(@Path("id") int id);
}



