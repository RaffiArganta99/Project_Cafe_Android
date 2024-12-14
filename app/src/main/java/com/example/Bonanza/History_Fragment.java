package com.example.Bonanza;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.Bonanza.Model.Order;
import com.example.Bonanza.Model.OrdersResponse;
import com.example.Bonanza.Retrofit.ApiClient;
import com.example.Bonanza.adapter.OrderAdapter;
import com.example.Bonanza.Interface_API.ApiService;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class History_Fragment extends Fragment {

    private RecyclerView recyclerView;
    private OrderAdapter orderAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_history_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        recyclerView = view.findViewById(R.id.HistoryRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        fetchOrders();
    }

    private void fetchOrders() {
        ApiService apiService = ApiClient.getApiService();

        apiService.getAllOrders().enqueue(new Callback<OrdersResponse>() {
            @Override
            public void onResponse(Call<OrdersResponse> call, Response<OrdersResponse> response) {
                if (response.isSuccessful()) {
                    List<Order> orders = response.body().getData();
                    orderAdapter = new OrderAdapter(orders);
                    recyclerView.setAdapter(orderAdapter);
                } else {
                    Toast.makeText(getContext(), "Error: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrdersResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Failure: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
