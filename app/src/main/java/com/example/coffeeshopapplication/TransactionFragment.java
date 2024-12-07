package com.example.coffeeshopapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Interface_API.OnTransactionItemListener;
import com.example.coffeeshopapplication.Model.CartResponse;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.adapter.TransactionAdapter;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    private TextView totalText, tunaiText, kembalianText;
    private EditText tunaiEditText;
    private Button konfirmasiButton;
    private RecyclerView recyclerTransaction;
    private TransactionAdapter adapter;
    private List<CartResponse.CartItem> transactionItems;
    private int customerId = 1;  // Pastikan ID sesuai dengan pengguna aktif.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);

        totalText = rootView.findViewById(R.id.Total_text);
        tunaiText = rootView.findViewById(R.id.textView28);
        kembalianText = rootView.findViewById(R.id.Kembalian_text);
        tunaiEditText = rootView.findViewById(R.id.editText_Tunai);
        konfirmasiButton = rootView.findViewById(R.id.konfirmasi_t);
        recyclerTransaction = rootView.findViewById(R.id.recyclerTransaction);

        transactionItems = new ArrayList<>();

        adapter = new TransactionAdapter(getContext(), transactionItems, customerId, new OnTransactionItemListener() {
            @Override
            public void onQuantityChange(int position, int newQuantity) {
                CartResponse.CartItem item = transactionItems.get(position);
                item.setQuantity(newQuantity);
                adapter.updateItem(position, item);
                calculateTotal();
            }

            @Override
            public void onDeleteItem(int position) {
                adapter.deleteItem(position);
                refreshCartData();  // Pastikan data diperbarui dari server
                calculateTotal();
            }

        });

        recyclerTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerTransaction.setAdapter(adapter);

        loadCartData();

        return rootView;
    }

    private void loadCartData() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getCart().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    transactionItems.clear();
                    transactionItems.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();
                    calculateTotal();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("LoadCart", "Failed to load cart data.");
            }
        });
    }

    private void calculateTotal() {
        int total = 0;
        for (CartResponse.CartItem item : transactionItems) {
            total += item.getPrice() * item.getQuantity();
        }
        totalText.setText(String.valueOf(total));
    }

    private void refreshCartData() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getCart().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    transactionItems.clear();
                    transactionItems.addAll(response.body().getData());

                    // Memastikan Adapter mendapatkan pembaruan data
                    adapter.notifyDataSetChanged();
                    calculateTotal();
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Log.e("RefreshCart", "Failed to reload cart data.");
            }
        });
    }

}



