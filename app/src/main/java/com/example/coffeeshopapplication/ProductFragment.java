package com.example.coffeeshopapplication;

import android.content.Intent;  // Mengimpor Intent sekali saja
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
import java.util.Arrays;
import java.util.List;


public class ProductFragment extends Fragment implements CartAdapter.OnAddToCartClickListener {

    private FragmentProductBinding binding;
    private CartAdapter adapter;
    private ApiService apiService;
    private List<Menu> fullMenuList = new ArrayList<>(); // Daftar lengkap menu

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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

        // Setup Spinner
        List<String> categories = Arrays.asList("All Menu", "Food", "Drink");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(
                requireContext(),
                android.R.layout.simple_spinner_item,
                categories
        );
        adapterSpinner.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        binding.spinnerCategory.setAdapter(adapterSpinner);

        binding.spinnerCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedCategory = parent.getItemAtPosition(position).toString();
                List<Menu> filteredList = new ArrayList<>();
                for (Menu menu : fullMenuList) {
                    if (selectedCategory.equalsIgnoreCase("All Menu") ||
                            menu.getCategory().trim().equalsIgnoreCase(selectedCategory.trim())) {
                        filteredList.add(menu);
                    }
                }
                adapter.updateMenuList(filteredList); // Update daftar di adapter
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.updateMenuList(fullMenuList); // Tampilkan semua menu jika tidak ada yang dipilih
            }
        });

        // Fetch data dari API
        fetchMenuFromApi();

        return binding.getRoot();
    }

    private void fetchMenuFromApi() {
        apiService.getMenu().enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullMenuList.clear();
                    fullMenuList.addAll(response.body().getMenuList()); // Simpan daftar lengkap menu
                    adapter.updateMenuList(fullMenuList); // Tampilkan semua menu di RecyclerView
                } else {
                    Log.e("ProductFragment", "API Response failed: " + response.message());
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
        Intent intent = new Intent(requireContext(), TransactionActivity.class);
        intent.putExtra("ITEM_NAME", name);
        intent.putExtra("ITEM_PRICE", price);
        intent.putExtra("ITEM_QUANTITY", quantity);
        startActivity(intent);
    }
}







