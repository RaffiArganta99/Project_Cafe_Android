package com.example.coffeeshopapplication.adapter;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.databinding.PopulerItemBinding;

import java.util.List;

public class PopulerAdapter extends RecyclerView.Adapter<PopulerAdapter.ProductViewHolder> {
    private List<String> items; // Nama item/produk
    private List<Integer> sold; // Jumlah terjual
    private List<Integer> images; // Gambar produk

    // Constructor untuk menerima data dari Activity/Fragment
    public PopulerAdapter(List<String> items, List<Integer> sold, List<Integer> images) {
        this.items = items;
        this.sold = sold;
        this.images = images;
    }

    // Method untuk meng-inflate layout XML dari item_recyclerview.xml
    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        PopulerItemBinding binding = PopulerItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new ProductViewHolder(binding);
    }

    // Menghubungkan data dengan ViewHolder pada posisi tertentu
    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        String itemName = items.get(position); // Nama produk
        int itemSold = sold.get(position); // Jumlah terjual
        int itemImage = images.get(position); // Gambar produk

        // Memanggil fungsi bind() untuk mengisi tampilan sesuai data
        holder.bind(itemName, itemSold, itemImage);
    }

    // Mengembalikan jumlah item dalam RecyclerView
    @Override
    public int getItemCount() {
        return items.size();
    }

    // ViewHolder untuk memegang referensi tampilan tiap item
    public static class ProductViewHolder extends RecyclerView.ViewHolder {
        private PopulerItemBinding binding; // Binding ke layout XML
        private ImageView imageView;

        public ProductViewHolder(PopulerItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
            this.imageView = binding.imageView5; // Mengambil referensi dari layout
        }

        // Method bind() untuk mengisi tampilan sesuai dengan data
        public void bind(String item, int sold, int imageRes) {
            binding.itemName.setText(item); // Menampilkan nama produk
            binding.itemSold.setText("Sold: " + sold); // Menampilkan jumlah terjual
            imageView.setImageResource(imageRes); // Menampilkan gambar produk
        }
    }
}
