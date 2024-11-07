package com.example.coffeeshopapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.R;
import com.example.coffeeshopapplication.TransactionItem;
import java.util.List;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<TransactionItem> transactionItems;

    // Constructor untuk menerima data
    public TransactionAdapter(List<TransactionItem> transactionItems) {
        this.transactionItems = transactionItems;
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // Inflate layout item untuk setiap baris (pastikan menggunakan cart_transaction.xml)
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        // Menyiapkan data untuk setiap item
        TransactionItem currentItem = transactionItems.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.priceTextView.setText(currentItem.getPrice());
        holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));
    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }

    // ViewHolder untuk item transaksi
    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, quantityTextView;
        ImageView imageView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cartFoodName_t);
            priceTextView = itemView.findViewById(R.id.cartItemPrice_t);
            quantityTextView = itemView.findViewById(R.id.Quantity_t);
            imageView = itemView.findViewById(R.id.cartImage_t);
        }
    }
}
