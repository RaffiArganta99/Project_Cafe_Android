package com.example.Bonanza;

import android.content.Context;
import android.content.Intent;  // Mengimpor Intent sekali saja
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Toast;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.Bonanza.Interface_API.ApiService;
import com.example.Bonanza.Model.MenuResponse;
import com.example.Bonanza.Retrofit.ApiClient;
import com.example.Bonanza.adapter.CartAdapter;
import com.example.Bonanza.Model.Menu;  // Import Menu yang benar
import com.example.Bonanza.databinding.FragmentProductBinding;
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

        int customerId = getCustomerId();  // Ambil customerId dari SharedPreferences
        adapter = new CartAdapter(requireContext(), new ArrayList<>(), this, customerId);
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        // Setup Spinner
        List<String> categories = Arrays.asList("All Menu", "Food", "Drink");
        ArrayAdapter<String> adapterSpinner = new ArrayAdapter<>(requireContext(), android.R.layout.simple_spinner_item, categories);
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

                adapter.updateMenuList(filteredList);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                adapter.updateMenuList(fullMenuList);
            }
        });

        // Tambahkan OnClickListener untuk BellNotif
        binding.BellNotif.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), StockStatusActivity.class);
            startActivity(intent);
        });

        fetchMenuFromApi();
        // Tambahkan setup SearchView
        setupSearchView();

        return binding.getRoot();
    }


    private void setupSearchView() {
        binding.searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Filter menu berdasarkan nama
                filterMenuByName(newText);
                return true;
            }
        });
    }

    private void filterMenuByName(String searchText) {
        List<Menu> filteredList = new ArrayList<>();

        // Pastikan fullMenuList tidak null
        if (fullMenuList != null) {
            for (Menu menu : fullMenuList) {
                // Lakukan pencarian case-insensitive
                if (menu.getMenuName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(menu);
                }
            }

            // Update adapter dengan daftar menu yang difilter
            adapter.updateMenuList(filteredList);
        }
    }

    private void fetchMenuFromApi() {
        apiService.getMenu().enqueue(new Callback<MenuResponse>() {
            @Override
            public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    fullMenuList.clear();
                    fullMenuList.addAll(response.body().getMenuList());
                    adapter.updateMenuList(fullMenuList);
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

    private int getCustomerId() {
        SharedPreferences sharedPreferences = requireContext().getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return sharedPreferences.getInt("customerId", -1);
    }


    @Override
    public void onAddToCart(String name, double price, int quantity) {
        Intent intent = new Intent(requireContext(), TransactionFragment.class);
        intent.putExtra("ITEM_NAME", name);
        intent.putExtra("ITEM_PRICE", price);
        intent.putExtra("ITEM_QUANTITY", quantity);
        startActivity(intent);
    }

    @Override
    public void onItemCartClick(Product product) {
        // Implement logika ketika produk di-click di RecyclerView
        Log.d("ProductFragment", "Clicked product: " + product.getName());

        Toast.makeText(getContext(), "Selected: " + product.getName(), Toast.LENGTH_SHORT).show();
    }
}