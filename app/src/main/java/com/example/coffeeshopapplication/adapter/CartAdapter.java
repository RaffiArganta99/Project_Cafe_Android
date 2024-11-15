package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.EditMenuDialogFragment;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Product;
import com.example.coffeeshopapplication.R;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.databinding.CartItemBinding;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final ArrayList<Product> cartItems; // Menggunakan ArrayList<Product> daripada ArrayList<String>
    private final OnAddToCartClickListener listener;
    private final ApiService apiService;

    public interface OnAddToCartClickListener {
        void onAddToCart(String name, String price, int quantity);
    }

    public CartAdapter(Context context, ArrayList<Product> cartItems, OnAddToCartClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;

        // Inisialisasi ApiService hanya sekali
        this.apiService = ApiClient.getApiService(); // Menggunakan ApiClient untuk mendapatkan ApiService
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return cartItems.size();
    }

    // Method untuk mengupdate daftar cart
    public void updateCartItems(List<Product> newCartItems) {
        this.cartItems.clear();
        this.cartItems.addAll(newCartItems);
        notifyDataSetChanged(); // Update RecyclerView
    }

    // ViewHolder class
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.ItemCart.setOnClickListener(view -> {
                int position = getAdapterPosition();
                Product product = cartItems.get(position);
                listener.onAddToCart(product.getName(), String.valueOf(product.getPrice()), product.getQuantity());
            });

            binding.minusButton.setOnClickListener(view -> decreaseQuantity(getAdapterPosition()));
            binding.plusButton.setOnClickListener(view -> increaseQuantity(getAdapterPosition()));
            binding.deleteButton.setOnClickListener(view -> deleteItem(getAdapterPosition()));
            binding.editButton.setOnClickListener(view -> openEditDialog(getAdapterPosition()));
        }

        public void bind(int position) {
            Product product = cartItems.get(position);

            binding.cartFoodName.setText(product.getName());
            binding.cartItemPrice.setText(String.valueOf(product.getPrice()));
            Uri itemImage = Uri.parse(product.getImageUri());

            if (itemImage != null) {
                binding.cartImage.setImageURI(itemImage);
            } else {
                binding.cartImage.setImageResource(R.drawable.default_image); // Ganti dengan default image Anda
            }

            binding.cartItemQuantity.setText(String.valueOf(product.getQuantity()));
        }

        private void decreaseQuantity(int position) {
            Product product = cartItems.get(position);
            if (product.getQuantity() > 1) {
                product.setQuantity(product.getQuantity() - 1);
                binding.cartItemQuantity.setText(String.valueOf(product.getQuantity()));
                updateCartItemToApi(position);
            }
        }

        private void increaseQuantity(int position) {
            Product product = cartItems.get(position);
            if (product.getQuantity() < 40) {
                product.setQuantity(product.getQuantity() + 1);
                binding.cartItemQuantity.setText(String.valueOf(product.getQuantity()));
                updateCartItemToApi(position);
            }
        }

        private void deleteItem(int position) {
            Product product = cartItems.get(position);
            apiService.deleteCartItem(product.getId()).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        cartItems.remove(position);
                        notifyItemRemoved(position);
                        notifyItemRangeChanged(position, cartItems.size());
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CartAdapter", "Failed to delete item from API: " + t.getMessage());
                }
            });
        }

        private void openEditDialog(int position) {
            Product product = cartItems.get(position);

            EditMenuDialogFragment dialog = new EditMenuDialogFragment((name, price, newImageUri) -> {
                product.setName(name);
                product.setPrice(Integer.parseInt(price));
                product.setImageUri(newImageUri.toString());
                notifyItemChanged(position);
                updateCartItemToApi(position);
            }, Uri.parse(product.getImageUri()));

            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "EditMenuDialog");
        }

        private void updateCartItemToApi(int position) {
            Product product = cartItems.get(position);

            apiService.updateCartItem(product.getId(), product).enqueue(new Callback<Void>() {
                @Override
                public void onResponse(Call<Void> call, Response<Void> response) {
                    if (response.isSuccessful()) {
                        Log.d("CartAdapter", "Item updated in API successfully");
                    } else {
                        Log.e("CartAdapter", "Failed to update item in API");
                    }
                }

                @Override
                public void onFailure(Call<Void> call, Throwable t) {
                    Log.e("CartAdapter", "Error updating item in API: " + t.getMessage());
                }
            });
        }
    }
}


