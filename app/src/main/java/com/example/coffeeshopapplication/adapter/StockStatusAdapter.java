package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.Model.StockStatusItem;
import com.example.coffeeshopapplication.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class StockStatusAdapter extends RecyclerView.Adapter<StockStatusAdapter.StockStatusViewHolder> {

    private Context context;
    private List<StockStatusItem> stockStatusItems;

    public StockStatusAdapter(Context context, List<StockStatusItem> stockStatusItems) {
        this.context = context;
        this.stockStatusItems = stockStatusItems;
    }

    @NonNull
    @Override
    public StockStatusViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_notif, parent, false);
        return new StockStatusViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StockStatusViewHolder holder, int position) {
        StockStatusItem item = stockStatusItems.get(position);

        // Set food name
        holder.foodNameTextView.setText(item.getMenuName());

        // Set stock information
        holder.stockTextView.setText("Stock: " + item.getStock());

        // Set additional text (could be a warning message)
        holder.additionalTextView.setText(item.getStock() <= 5 ? "Low Stock Alert!" : "Stock Needs Attention");

        Picasso.get()
                .load(item.getImageUrl())
                .placeholder(R.drawable.default_image)
                .into(holder.foodImageView);

    }

    @Override
    public int getItemCount() {
        return stockStatusItems.size();
    }

    static class StockStatusViewHolder extends RecyclerView.ViewHolder {
        ImageView foodImageView;
        TextView foodNameTextView;
        TextView stockTextView;
        TextView additionalTextView;

        public StockStatusViewHolder(@NonNull View itemView) {
            super(itemView);
            foodImageView = itemView.findViewById(R.id.imageView8);
            foodNameTextView = itemView.findViewById(R.id.cartFoodName_n);
            stockTextView = itemView.findViewById(R.id.cartItemStock_n);
            additionalTextView = itemView.findViewById(R.id.cart_text_n);
        }
    }
}
