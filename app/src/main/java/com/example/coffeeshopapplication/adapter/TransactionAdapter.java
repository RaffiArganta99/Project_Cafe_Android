package com.example.coffeeshopapplication.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatButton;
import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Interface_API.OnTransactionItemListener;
import com.example.coffeeshopapplication.Model.ApiResponse;
import com.example.coffeeshopapplication.Model.CartResponse;
import com.example.coffeeshopapplication.Model.DeleteCartRequest;
import com.example.coffeeshopapplication.Model.UpdateCartRequest;
import com.example.coffeeshopapplication.R;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.TransactionItem;
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

        // Mengatur klik listener untuk tombol-tombol
        holder.plusButton.setOnClickListener(v -> listener.onQuantityChange(position, currentItem.getQuantity() + 1));
        holder.minusButton.setOnClickListener(v -> listener.onQuantityChange(position, currentItem.getQuantity() - 1));
        holder.deleteButton.setOnClickListener(v -> deleteItem(position));

        holder.plusButton.setOnClickListener(v -> {
            int quantityChange = 1; // Tambah 1
            updateCartItem(position, quantityChange);
        });

        holder.minusButton.setOnClickListener(v -> {
            int quantityChange = -1; // Kurangi 1
            updateCartItem(position, quantityChange);
        });


    }

    @Override
    public int getItemCount() {
        return transactionItems.size();
    }

    public void updateItem(int position, CartResponse.CartItem item) {
        transactionItems.set(position, item);
        notifyItemChanged(position);
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

        // Validasi perubahan quantity
        if (newQuantity < 1 || newQuantity > item.getStock()) {
            Toast.makeText(context, "Quantity tidak valid", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim request ke API
        UpdateCartRequest request = new UpdateCartRequest(customerId, item.getCartId(), quantityChange);

        apiService.updateCart(request).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Gunakan kuantitas yang dihitung secara lokal
                    item.setQuantity(newQuantity);
                    transactionItems.set(position, item);
                    notifyItemChanged(position); // Memperbarui RecyclerView

                    Log.d("RecyclerViewUpdate", "Item at position " + position + " updated with quantity: " + newQuantity);
                    Toast.makeText(context, "Quantity berhasil diperbarui", Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("UpdateCart", "Response Error: " + response.message());
                    Toast.makeText(context, "Gagal memperbarui quantity", Toast.LENGTH_SHORT).show();
                }
            }


            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("UpdateCart", "Request Failed: " + t.getMessage());
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


