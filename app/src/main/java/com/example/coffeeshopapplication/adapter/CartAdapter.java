package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.coffeeshopapplication.EditMenuDialogFragment;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Interface_API.OnAddToCartClickListener;
import com.example.coffeeshopapplication.Product;
import com.example.coffeeshopapplication.R;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.databinding.CartItemBinding;
import com.example.coffeeshopapplication.Model.Menu;
import com.squareup.picasso.Picasso;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import java.util.ArrayList;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final ArrayList<Product> cartItems;
    private final OnAddToCartClickListener listener;
    private final ApiService apiService;
    private List<Menu> menuList;

    public interface OnAddToCartClickListener {
        void onAddToCart(String name, double price, int quantity);
    }

    public CartAdapter(Context context, ArrayList<Product> cartItems, OnAddToCartClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        this.apiService = ApiClient.getApiService();
        this.menuList = new ArrayList<>();
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        CartItemBinding binding = CartItemBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false);
        Log.d("CartAdapter", "onCreateViewHolder called");
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (cartItems != null && position < cartItems.size()) {
            Product currentProduct = cartItems.get(position);

            // Log debugging untuk memverifikasi data
            Log.d("CartAdapter", "Binding item at position: " + position);
            Log.d("CartAdapter", "Product Name: " + currentProduct.getName() + ", Price: " + currentProduct.getPrice());
            Log.d("CartAdapter", "Image URL from API: " + currentProduct.getImageUri());

            // Set data ke TextView
            holder.cartFoodName.setText(currentProduct.getName());

            // Mengatur harga ke TextView
            if (currentProduct.getPrice() != 0) {
                holder.cartItemPrice.setText(String.format("Rp %.2f", currentProduct.getPrice()));
            } else {
                Log.e("CartAdapter", "Price is null or zero for product: " + currentProduct.getName());
            }

            // Validasi untuk TextView cartFoodName
            if (holder.cartFoodName != null) {
                holder.cartFoodName.setText(currentProduct.getName());
            } else {
                Log.e("CartAdapter", "cartFoodName is null");
            }

            // Menggunakan imageUri langsung dari API
            String imageUrl = currentProduct.getImageUri(); // Menggunakan URL gambar dari API tanpa tambahan

            // Memuat gambar menggunakan Picasso (atau bisa dengan Glide jika lebih nyaman)
            Picasso.get()
                    .load(imageUrl)
                    .placeholder(R.drawable.default_image) // Gambar sementara saat loading
                    .error(R.drawable.default_image) // Gambar default jika gagal memuat
                    .into(holder.cartImage);
        } else {
            Log.e("CartAdapter", "Cart items list is null or index out of bounds");
        }
    }




    @Override
    public int getItemCount() {
        return (cartItems != null) ? cartItems.size() : 0;
    }

    public void updateMenuList(List<Menu> menuList) {
        this.menuList = menuList;
        Log.d("CartAdapter", "Menu list updated with size: " + menuList.size());
        for (Menu menu : menuList) {
            double priceValue = 0;
            try {
                // Ubah harga ke tipe double
                priceValue = Double.parseDouble(menu.getPrice());
            } catch (NumberFormatException e) {
                Log.e("CartAdapter", "Failed to parse menu price: " + menu.getPrice(), e);
            }

            // Gunakan URL gambar yang sesuai
            String imageUrl = menu.getImageUrl(); // Misalnya menu.getImageUrl() mengembalikan URL gambar

            // Pastikan untuk menggunakan konstruktor yang benar
            Product product = new Product(0, menu.getMenuName(), priceValue, imageUrl, 1);
            cartItems.add(product);
        }
        notifyDataSetChanged();
    }



    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;
        TextView cartFoodName, cartItemPrice;
        ImageView cartImage; // Tambahkan variabel cartImage

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Inisialisasi TextView
            cartFoodName = binding.cartFoodName;
            cartItemPrice = binding.cartItemPrice;

            // Inisialisasi ImageView
            cartImage = binding.cartImage; // Pastikan ID cartImage sesuai di XML layout

            // Listener untuk item cart
            binding.ItemCart.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) { // Validasi posisi
                    Product product = cartItems.get(position);
                    listener.onAddToCart(product.getName(), product.getPrice(), product.getQuantity());
                }
            });

            // Listener untuk tombol plus
            binding.plusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    increaseQuantity(position);
                }
            });

            // Listener untuk tombol minus
            binding.minusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    decreaseQuantity(position);
                }
            });

            // Listener untuk tombol edit
            binding.editButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    openEditDialog(position);
                }
            });

            // Listener untuk tombol hapus
            binding.deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteItem(position);
                }
            });
        }



        public void bind(int position) {
            Product product = cartItems.get(position);

            // Atur teks nama makanan
            binding.cartFoodName.setText(product.getName());

            // Atur harga dengan format Rupiah
            binding.cartItemPrice.setText(String.format("Rp %.2f", product.getPrice()));

            // Log URL untuk debug
            Log.d("CartAdapter", "Image URL: " + product.getImageUri()); // Gunakan imageUri langsung

            // Memuat gambar menggunakan Picasso
            Picasso.get()
                    .load(product.getImageUri()) // Langsung gunakan URL dari API
                    .placeholder(R.drawable.default_image) // Gambar sementara saat loading
                    .error(R.drawable.default_image) // Gambar default jika gagal memuat
                    .into(binding.cartImage);

            Log.d("CartAdapter", "Binding item: " + product.getName() + ", Price: " + product.getPrice());
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
                double priceValue = Double.parseDouble(price); // Pastikan konversi ke double
                product.setPrice(priceValue);
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


