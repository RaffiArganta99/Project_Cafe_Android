package com.example.coffeeshopapplication.Interface_API;

import com.example.coffeeshopapplication.Model.ApiResponse;
import com.example.coffeeshopapplication.Model.CartResponse;
import com.example.coffeeshopapplication.Model.DeleteCartRequest;
import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Model.LoginResponse;
import com.example.coffeeshopapplication.Model.OrdersResponse;
import com.example.coffeeshopapplication.Model.ResponseUpdate;
import com.example.coffeeshopapplication.Model.UpdateCartRequest;

import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HTTP;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface ApiService {

    // API untuk Menu
    @GET("MenuApi.php")
    Call<MenuResponse> getMenu();

    @Multipart
    @POST("MenuApi.php")
    Call<ResponseBody> addMenu(
            @Part("MenuName") RequestBody menuName,
            @Part("Description") RequestBody description,
            @Part("Price") RequestBody price,
            @Part("Stock") RequestBody stock,
            @Part("Category") RequestBody category,
            @Part MultipartBody.Part imageUrl
    );

    @PUT("MenuApi.php")
    Call<ResponseUpdate> updateMenu(
            @Query("id") int id,
            @Body Map<String, Object> productUpdate
    );

    @DELETE("MenuApi.php")
    Call<Void> deleteCartItem(@Query("id") int id);

    // Endpoint untuk login menggunakan parameter FormUrlEncoded
    @FormUrlEncoded
    @POST("EmployeeApi.php")
    Call<LoginResponse> login(
            @Query("action") String action, // Query parameter untuk action=login
            @Field("Email") String email,
            @Field("Password") String password
    );

    @GET("OrderApi.php") // Sesuaikan endpoint ini dengan API Anda
    Call<OrdersResponse> getAllOrders();

    @POST("CartApi.php")
    Call<ApiResponse> addToCart(@Body Map<String, Object> requestBody);

    // Mengambil daftar keranjang
    @GET("CartApi.php")
    Call<CartResponse> getCart();

    // Memperbarui keranjang (Quantity +/-)
    @PUT("CartApi.php")
    Call<ApiResponse> updateCart(@Body UpdateCartRequest request);

    @HTTP(method = "DELETE", path = "CartApi.php", hasBody = true)
    Call<ApiResponse> deleteCart(@Body DeleteCartRequest request);


    // Menghapus semua item dari keranjang untuk CustomerId tertentu
    @DELETE("CartApi.php?action=delete-all")
    Call<ApiResponse> deleteAllCart(@Query("customerId") int customerId);

}
