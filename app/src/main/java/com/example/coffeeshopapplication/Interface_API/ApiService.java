package com.example.coffeeshopapplication.Interface_API;

import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Model.LoginResponse;
import com.example.coffeeshopapplication.Model.ResponseUpdate;

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
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {
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

    @Multipart
    @POST("MenuApi.php")
    Call<ResponseBody> updateMenuImage(
            @Query("id") int id, // Menggunakan @Query untuk query string
            @Query("action") String action, // Menambahkan action sebagai query parameter
            @Part MultipartBody.Part imageUrl
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
}
