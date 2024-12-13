package com.example.coffeeshopapplication;


import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Model.StockStatusItem;
import com.example.coffeeshopapplication.Model.StockStatusResponse;
import com.example.coffeeshopapplication.adapter.StockStatusAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StockStatusActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private StockStatusAdapter adapter;
    private List<StockStatusItem> stockStatusItems;
    private ApiService apiService;
    private ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notif);

        // Initialize views
        recyclerView = findViewById(R.id.recyclerViewStockStatus);
        backButton = findViewById(R.id.imageButton2);

        // Setup RecyclerView
        stockStatusItems = new ArrayList<>();
        adapter = new StockStatusAdapter(this, stockStatusItems);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(adapter);

        // Initialize API service
        apiService = ApiClient.getApiService();

        // Fetch stock status
        fetchStockStatus();

        // Setup back button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void fetchStockStatus() {
        Call<StockStatusResponse> call = apiService.getStockStatus("stockstatus", 5);
        call.enqueue(new Callback<StockStatusResponse>() {
            @Override
            public void onResponse(Call<StockStatusResponse> call, Response<StockStatusResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    stockStatusItems.clear();
                    stockStatusItems.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    if (stockStatusItems.isEmpty()) {
                        Toast.makeText(StockStatusActivity.this, "No low stock items found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(StockStatusActivity.this, "Failed to fetch stock status", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<StockStatusResponse> call, Throwable t) {
                Toast.makeText(StockStatusActivity.this, "Network error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
