package com.example.coffeeshopapplication;

import android.content.Intent;  // Mengimpor Intent sekali saja
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.adapter.CartAdapter;
import com.example.coffeeshopapplication.Model.Menu;  // Import Menu yang benar
import com.example.coffeeshopapplication.databinding.FragmentProductBinding;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;


public class ProductFragment extends Fragment implements CartAdapter.OnAddToCartClickListener {

    private FragmentProductBinding binding;
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
        binding = FragmentProductBinding.inflate(inflater, container, false);

        // Setup RecyclerView
        adapter = new CartAdapter(requireContext(), new ArrayList<>(), this);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        // Log tambahan untuk memeriksa setup RecyclerView
        Log.d("ProductFragment", "RecyclerView setup complete. Adapter set.");

        // Fetch data from API
        fetchMenuFromApi();

        return binding.getRoot();
    }

    private void fetchMenuFromApi() {
        apiService.getMenu().enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Ambil data menu dan perbarui adapter
                    List<Menu> menuList = response.body().getMenuList();

                    // Log untuk memeriksa ukuran menuList
                    Log.d("ProductFragment", "Menu fetched successfully. Size: " + menuList.size());

                    // Update RecyclerView dengan data menu
                    adapter.updateMenuList(menuList);
                } else {
                    Log.e("ProductFragment", "Failed to fetch menu: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<MenuResponse> call, Throwable t) {
                Log.e("ProductFragment", "Error fetching menu: " + t.getMessage());
            }
        });
    }

    @Override
    public void onAddToCart(String name, double price, int quantity) {
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





