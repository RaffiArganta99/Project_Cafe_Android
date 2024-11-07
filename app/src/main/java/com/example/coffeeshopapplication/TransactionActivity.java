package com.example.coffeeshopapplication;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.coffeeshopapplication.adapter.TransactionAdapter;
import com.example.coffeeshopapplication.TransactionItem;
import com.example.coffeeshopapplication.databinding.ActivityTransactionBinding;

import java.util.ArrayList;

public class TransactionActivity extends AppCompatActivity {

    private ArrayList<TransactionItem> transactionItems;
    private TransactionAdapter transactionAdapter;
    private ActivityTransactionBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inisialisasi binding
        binding = ActivityTransactionBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi RecyclerView dan Adapter
        transactionItems = new ArrayList<>();
        transactionAdapter = new TransactionAdapter(transactionItems);
        binding.recyclerTransaction.setAdapter(transactionAdapter);
        binding.recyclerTransaction.setLayoutManager(new LinearLayoutManager(this));

        // Menerima data dari Intent
        Intent intent = getIntent();
        String name = intent.getStringExtra("ITEM_NAME");
        String price = intent.getStringExtra("ITEM_PRICE");
        int quantity = intent.getIntExtra("ITEM_QUANTITY", 1);

        // Tambahkan item ke dalam daftar transaksi
        onAddToCart(name, price, quantity);
    }

    // Menambahkan item ke dalam RecyclerView
    public void onAddToCart(String name, String price, int quantity) {
        // Membuat TransactionItem dan menambahkannya ke dalam list
        TransactionItem item = new TransactionItem(name, price, quantity);
        transactionItems.add(item);
        transactionAdapter.notifyDataSetChanged(); // Memberitahukan adapter untuk memperbarui UI
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        binding = null; // Menghindari memory leak
    }
}
