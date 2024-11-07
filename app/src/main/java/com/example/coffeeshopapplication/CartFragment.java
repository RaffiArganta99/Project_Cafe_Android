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
import com.example.coffeeshopapplication.adapter.CartAdapter;
import com.example.coffeeshopapplication.databinding.FragmentCartBinding;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CartFragment extends Fragment implements CartAdapter.OnAddToCartClickListener {

    private FragmentCartBinding binding;
    private CartAdapter adapter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentCartBinding.inflate(inflater, container, false);

        // List item dan harga
        List<String> cartFoodName = Arrays.asList("Burger", "Sandwich", "Momo", "Item", "Sandwich", "Momo", "Burger", "Sandwich", "Momo", "Item", "Sandwich", "Momo");
        List<String> cartItemPrice = Arrays.asList("Rp15.000", "Rp57.000", "Rp79.000", "Rp38.000", "Rp56.000", "Rp66.000", "Rp71.000", "Rp90.000", "Rp12.000", "Rp5.000", "Rp5.000", "Rp5.000");

        // Ubah daftar gambar dari Integer menjadi Uri
        List<Uri> cartImageUri = new ArrayList<>();
        for (int imageRes : Arrays.asList(
                R.drawable.menu1,
                R.drawable.menu2,
                R.drawable.menu3,
                R.drawable.menu4,
                R.drawable.menu5,
                R.drawable.menu6,
                R.drawable.menu1,
                R.drawable.menu2,
                R.drawable.menu3,
                R.drawable.menu4,
                R.drawable.menu5,
                R.drawable.menu6
        )) {
            Uri imageUri = Uri.parse("android.resource://" + requireContext().getPackageName() + "/" + imageRes);
            cartImageUri.add(imageUri);
        }

        // Inisialisasi adapter dengan listener this
        adapter = new CartAdapter(requireContext(), new ArrayList<>(cartFoodName), new ArrayList<>(cartItemPrice), new ArrayList<>(cartImageUri), this);

        // Setup RecyclerView untuk menampilkan item
        binding.cartRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.cartRecyclerView.setAdapter(adapter);

        // Inisialisasi CartButton dan tambahkan onClickListener
        binding.CartButton.setOnClickListener(v -> {
            // Mulai ActivityTransaction
            Intent intent = new Intent(requireContext(), TransactionActivity.class);
            startActivity(intent);
        });

        return binding.getRoot();
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
