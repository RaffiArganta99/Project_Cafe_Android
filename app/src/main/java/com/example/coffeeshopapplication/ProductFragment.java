package com.example.coffeeshopapplication;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.adapter.CartAdapter;
import com.example.coffeeshopapplication.databinding.FragmentProductBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class ProductFragment extends Fragment implements CartAdapter.OnAddToCartClickListener {

    private FragmentProductBinding binding;  // Pastikan ini sesuai dengan nama layout Anda
    private CartAdapter adapter;
    private ApiService apiService;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Initialize ApiService
        apiService = ApiClient.getApiService();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate layout sesuai dengan nama yang benar
        binding = FragmentProductBinding.inflate(inflater, container, false);

        // Setup RecyclerView with empty adapter initially
        adapter = new CartAdapter(requireContext(), new ArrayList<>(), ProductFragment.this);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        // Fetch data from API
        fetchProductsFromApi();

        // Inisialisasi CartButton dan tambahkan onClickListener
        binding.CartButton.setOnClickListener(v -> {
            // Mulai ActivityTransaction
            Intent intent = new Intent(requireContext(), TransactionActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
    }

    private void fetchProductsFromApi() {
        // Panggil API untuk mengambil data produk
        apiService.getCartItems().enqueue(new Callback<List<Product>>() {
            @Override
            public void onResponse(Call<List<Product>> call, Response<List<Product>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Ambil data produk dan update adapter
                    List<Product> productList = response.body();

                    // Perbarui adapter dengan data yang diterima
                    adapter = new CartAdapter(requireContext(), new ArrayList<>(productList), ProductFragment.this);
                    binding.cartRecyclerView.setAdapter(adapter);
                } else {
                    Log.e("ProductFragment", "Failed to fetch products: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<List<Product>> call, Throwable t) {
                Log.e("ProductFragment", "Error fetching products: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAddToCart(String name, String price, int quantity) {
        // Intent untuk memulai Activity baru untuk menampilkan transaksi
        Intent intent = new Intent(requireContext(), TransactionActivity.class);

        // Kirim data yang diperlukan ke Activity
        intent.putExtra("ITEM_NAME", name);
        intent.putExtra("ITEM_PRICE", price);
        intent.putExtra("ITEM_QUANTITY", quantity);

        // Memulai Activity
        startActivity(intent);
    }
}



