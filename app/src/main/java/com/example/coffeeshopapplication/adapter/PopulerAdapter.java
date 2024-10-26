package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.R;

import java.util.List;

public class PopulerAdapter extends RecyclerView.Adapter<PopulerAdapter.ItemViewHolder> {

    private Context context;
    private List<String> itemNames;      // Nama produk
    private List<Integer> itemImages;    // Gambar produk
    private List<Integer> itemSold;      // Jumlah terjual

    public PopulerAdapter(Context context, List<String> itemNames, List<Integer> itemImages, List<Integer> itemSold) {
        this.context = context;
        this.itemNames = itemNames;
        this.itemImages = itemImages;
        this.itemSold = itemSold;
    }

    @NonNull
    @Override
    public ItemViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.populer_item, parent, false);
        return new ItemViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull ItemViewHolder holder, int position) {
        String itemName = itemNames.get(position);
        int itemImage = itemImages.get(position);
        int sold = itemSold.get(position);

        holder.bind(itemName, itemImage, sold);
    }

    @Override
    public int getItemCount() {
        return itemNames.size();
    }

    // ViewHolder untuk Item Produk
    static class ItemViewHolder extends RecyclerView.ViewHolder {
        TextView itemName, itemSold;
        ImageView imageView;

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
            itemName = itemView.findViewById(R.id.textView5);   // Nama produk
            itemSold = itemView.findViewById(R.id.itemSold);    // Jumlah terjual
            imageView = itemView.findViewById(R.id.imageView5); // Gambar produk
        }

        public void bind(String name, int imageResId, int sold) {
            itemName.setText(name);
            itemSold.setText(String.valueOf(sold));
            imageView.setImageResource(imageResId);
        }
    }
}
