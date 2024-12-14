package com.example.Bonanza.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.Bonanza.Interface_API.ApiService;
import com.example.Bonanza.Interface_API.OnTransactionItemListener;
import com.example.Bonanza.Model.ApiResponse;
import com.example.Bonanza.Model.CartResponse;
import com.example.Bonanza.Model.DeleteCartRequest;
import com.example.Bonanza.Model.UpdateCartRequest;
import com.example.Bonanza.R;
import com.example.Bonanza.Retrofit.ApiClient;
import com.squareup.picasso.Picasso;

import java.util.List;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionAdapter extends RecyclerView.Adapter<TransactionAdapter.TransactionViewHolder> {

    private List<CartResponse.CartItem> transactionItems;
    private Context context;
    private int customerId;
    private OnTransactionItemListener listener;

    private ApiService apiService;

    public TransactionAdapter(Context context, List<CartResponse.CartItem> transactionItems, int customerId, OnTransactionItemListener listener) {
        this.context = context;
        this.transactionItems = transactionItems;
        this.customerId = customerId;
        this.listener = listener;
        apiService = ApiClient.getApiService();
    }

    @NonNull
    @Override
    public TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cart_transaction, parent, false);
        return new TransactionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TransactionViewHolder holder, int position) {
        CartResponse.CartItem currentItem = transactionItems.get(position);

        // Binding data ke TextView
        holder.nameTextView.setText(currentItem.getMenuName());
        holder.priceTextView.setText(String.valueOf(currentItem.getPrice()));
        holder.quantityTextView.setText(String.valueOf(currentItem.getQuantity()));
        holder.stockTextView.setText("Stock: " + currentItem.getStock());

        // Menggunakan Picasso untuk memuat gambar dari URL
        Picasso.get()
                .load(currentItem.getImageUrl())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.cartImageView);

        // Tombol Plus dengan validasi dan update real-time
        holder.plusButton.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantity();
            if (currentQuantity < currentItem.getStock()) {
                updateCartItem(position, 1);
            } else {
                Toast.makeText(context, "Stok tidak mencukupi", Toast.LENGTH_SHORT).show();
            }
        });

        // Tombol Minus dengan validasi dan update real-time
        holder.minusButton.setOnClickListener(v -> {
            int currentQuantity = currentItem.getQuantity();
            if (currentQuantity > 1) {
                updateCartItem(position, -1);
            } else {
                Toast.makeText(context, "Jumlah minimal 1", Toast.LENGTH_SHORT).show();
            }
        });

        // Delete Button
        holder.deleteButton.setOnClickListener(v -> deleteItem(position));
    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }


    public void deleteItem(int position) {
        CartResponse.CartItem item = transactionItems.get(position);
        DeleteCartRequest request = new DeleteCartRequest(item.getCartId(), customerId);

        apiService.deleteCart(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Log untuk debugging
                    Log.d("DeleteCart", "Delete response: " + response.body().getMessage());

                    // Hapus item dari list lokal
                    transactionItems.remove(position);

                    // Refresh UI
                    notifyItemRemoved(position);
                    notifyItemRangeChanged(position, transactionItems.size());

                    // Pastikan listener dipanggil
                    if (listener != null) {
                        listener.onDeleteItem(position);
                    }

                    Toast.makeText(context, "Item berhasil dihapus", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, "Gagal menghapus item", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("DeleteCart", "Delete failed", t);
                Toast.makeText(context, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }


    private void updateCartItem(int position, int quantityChange) {
        CartResponse.CartItem item = transactionItems.get(position);
        int newQuantity = item.getQuantity() + quantityChange;

        // Kirim request ke API untuk update cart
        UpdateCartRequest request = new UpdateCartRequest(customerId, item.getCartId(), quantityChange);

        apiService.updateCart(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Update lokal
                    item.setQuantity(newQuantity);
                    transactionItems.set(position, item);
                    notifyItemChanged(position);

                    // Panggil listener untuk refresh total
                    if (listener != null) {
                        listener.onQuantityChange(position, newQuantity);
                    }

//                    Toast.makeText(context, "Quantity berhasil diperbarui", Toast.LENGTH_SHORT).show();
                } else {
//                    Toast.makeText(context, "Gagal memperbarui quantity", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Toast.makeText(context, "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView, priceTextView, quantityTextView, stockTextView;
        ImageView cartImageView;
        AppCompatImageButton plusButton, minusButton;
        AppCompatImageButton deleteButton;


        @SuppressLint("WrongViewCast")
        public TransactionViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.cartFoodName_t);
            priceTextView = itemView.findViewById(R.id.cartItemPrice_t);
            quantityTextView = itemView.findViewById(R.id.Quantity_t);
            stockTextView = itemView.findViewById(R.id.cartItemStock_t);
            cartImageView = itemView.findViewById(R.id.cartImage_t);
            plusButton = itemView.findViewById(R.id.plus_t);
            minusButton = itemView.findViewById(R.id.minus_t);
            deleteButton = itemView.findViewById(R.id.delete_t);
        }
    }
}