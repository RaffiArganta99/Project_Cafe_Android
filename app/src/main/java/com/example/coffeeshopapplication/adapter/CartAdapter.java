package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.EditMenuDialogFragment;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Model.MenuResponse;
import com.example.coffeeshopapplication.Model.ResponseUpdate;
import com.example.coffeeshopapplication.Product;
import com.example.coffeeshopapplication.R;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.databinding.CartItemBinding;
import com.example.coffeeshopapplication.Model.Menu;
import com.squareup.picasso.Picasso;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final ArrayList<Product> cartItems;
    private final OnAddToCartClickListener listener;
    private final ApiService apiService;
    private List<Menu> menuList;

    public interface OnAddToCartClickListener {
        void onAddToCart(String name, double price, int stock);
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
        return new CartViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        if (cartItems != null && position < cartItems.size()) {
            Product currentProduct = cartItems.get(position);

            holder.cartFoodName.setText(currentProduct.getName());

            // Menampilkan kategori
            if (currentProduct.getCategory() != null && !currentProduct.getCategory().isEmpty()) {
                holder.cartItemCategory.setText(currentProduct.getCategory());
            } else {
                holder.cartItemCategory.setText("Unknown");
            }

            // Menampilkan harga
            holder.cartItemPrice.setText(String.format("Rp %.2f", currentProduct.getPrice()));

            // Menampilkan stok
            holder.cartItemStock.setText(String.valueOf(currentProduct.getStock()));

            // Menampilkan gambar
            Picasso.get()
                    .load(currentProduct.getImageUri())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.cartImage);

            holder.binding.editButton.setOnClickListener(v -> {
                EditMenuDialogFragment dialog = new EditMenuDialogFragment((name, price, uri, category) -> {
                    currentProduct.setName(name);
                    currentProduct.setPrice(Double.parseDouble(price));
                    currentProduct.setImageUri(uri.toString());
                    currentProduct.setCategory(category); // Tambahkan kategori
                    notifyItemChanged(position);

                }, Uri.parse(currentProduct.getImageUri()), currentProduct.getId());

                if (context instanceof FragmentActivity) {
                    FragmentActivity activity = (FragmentActivity) context;
                    dialog.show(activity.getSupportFragmentManager(), "EditMenuDialog");
                }
            });

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
        cartItems.clear();

        for (Menu menu : menuList) {
            double priceValue = 0;
            int stockValue = 0;

            // Parsing harga dari String ke double
            try {
                priceValue = Double.parseDouble(menu.getPrice()); // Menggunakan Double.parseDouble
            } catch (NumberFormatException e) {
                Log.e("CartAdapter", "Failed to parse menu price: " + menu.getPrice(), e);
            }

            // Parsing stok dari String ke integer
            try {
                stockValue = Integer.parseInt(menu.getStock()); // Tetap menggunakan Integer.parseInt
            } catch (NumberFormatException e) {
                Log.e("CartAdapter", "Failed to parse menu stock: " + menu.getStock(), e);
            }

            String category = menu.getCategory();
            if (category == null || category.isEmpty()) {
                category = "Unknown";
            }

            // Membuat objek Product berdasarkan data Menu
            Product product = new Product(
                    menu.getMenuId(),       // ID produk
                    menu.getMenuName(),     // Nama produk
                    priceValue,             // Harga produk
                    menu.getImageUrl(),     // URL gambar
                    category,               // Kategori
                    stockValue,             // Stok produk
                    ""
            );

            cartItems.add(product);
        }

        // Memperbarui tampilan RecyclerView
        notifyDataSetChanged();
    }



    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;
        TextView cartFoodName, cartItemPrice, cartItemCategory, cartItemStock;
        ImageView cartImage;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            cartFoodName = binding.cartFoodName;
            cartItemPrice = binding.cartItemPrice;
            cartItemCategory = binding.cartItemCategory;
            cartItemStock = binding.cartItemStock; // Tambahkan untuk menampilkan stock
            cartImage = binding.cartImage;

            binding.plusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    increaseStock(position);
                }
            });

            binding.minusButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    decreaseStock(position);
                }
            });

            binding.deleteButton.setOnClickListener(view -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    deleteItem(position);
                }
            });
        }

        private void increaseStock(int position) {
            Product product = cartItems.get(position);
            product.setStock(product.getStock() + 1);
            binding.cartItemStock.setText(String.valueOf(product.getStock()));

            // Update stok ke API
            updateStockToApi(product.getId(), product.getStock());
        }

        private void decreaseStock(int position) {
            Product product = cartItems.get(position);
            if (product.getStock() > 0) {
                product.setStock(product.getStock() - 1);
                binding.cartItemStock.setText(String.valueOf(product.getStock()));

                // Update stok ke API
                updateStockToApi(product.getId(), product.getStock());
            }
        }

        private void updateStockToApi(int productId, int newStock) {
            // Validasi stok tidak boleh negatif
            if (newStock < 0) {
                Log.e("CartAdapter", "Stock tidak boleh negatif. Nilai: " + newStock);
                return;
            }

            // Membuat Map untuk mengirimkan stok baru
            Map<String, Object> stockUpdate = new HashMap<>();
            stockUpdate.put("Stock", newStock); // Mengirim stok baru

            // Mengirim permintaan PUT ke API
            apiService.updateMenu(productId, stockUpdate).enqueue(new Callback<ResponseUpdate>() {
                @Override
                public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                    if (response.isSuccessful()) {
                        // Jika berhasil, cek pesan dan update UI jika stok berhasil diubah
                        if (response.body() != null && response.body().getMessage() != null && response.body().getMessage().equals("Stock updated successfully")) {
                            Log.d("CartAdapter", "Stock updated successfully.");
                            // Perbarui UI atau beri notifikasi jika diperlukan
                        } else {
                            Log.e("CartAdapter", "Error: " + response.body().getMessage());
                        }
                    } else {
                        Log.e("CartAdapter", "Failed to update stock: " + response.message());
                        Toast.makeText(context, "Failed to update stock. Please try again.", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                    Log.e("CartAdapter", "Error updating stock: " + t.getMessage());
                    Toast.makeText(context, "Failed to update stock. Please check your connection.", Toast.LENGTH_SHORT).show();
                }
            });
        }




        private void deleteItem(int position) {
            Product product = cartItems.get(position);

            // Menampilkan dialog konfirmasi sebelum menghapus
            new AlertDialog.Builder(context)
                    .setTitle("Confirm Deletion")
                    .setMessage("Are you sure you want to delete this item?")
                    .setPositiveButton("Yes", (dialog, which) -> {
                        // Jika pengguna mengkonfirmasi penghapusan
                        apiService.deleteCartItem(product.getId()).enqueue(new Callback<Void>() {
                            @Override
                            public void onResponse(Call<Void> call, Response<Void> response) {
                                if (response.isSuccessful()) {
                                    // Jika penghapusan berhasil, update UI
                                    cartItems.remove(position);
                                    notifyItemRemoved(position);
                                    notifyItemRangeChanged(position, cartItems.size());

                                    // Sinkronisasi data setelah penghapusan
                                    syncMenuData();
                                } else {
                                    Log.e("CartAdapter", "Failed to delete item from API: " + response.message());
                                    Toast.makeText(context, "Failed to delete item. Please try again.", Toast.LENGTH_SHORT).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<Void> call, Throwable t) {
                                Log.e("CartAdapter", "Failed to delete item from API: " + t.getMessage());
                                Toast.makeText(context, "Failed to delete item. Please check your connection.", Toast.LENGTH_SHORT).show();
                            }
                        });
                    })
                    .setNegativeButton("No", null)
                    .show();
        }

        private void syncMenuData() {
            apiService.getMenu().enqueue(new Callback<MenuResponse>() {
                @Override
                public void onResponse(Call<MenuResponse> call, Response<MenuResponse> response) {
                    if (response.isSuccessful() && response.body() != null) {
                        updateMenuList(response.body().getMenuList());
                        Log.d("CartAdapter", "Menu data updated from API.");
                    } else {
                        Log.e("CartAdapter", "Failed to fetch menu data: " + response.message());
                    }
                }

                @Override
                public void onFailure(Call<MenuResponse> call, Throwable t) {
                    Log.e("CartAdapter", "Error syncing menu data: " + t.getMessage());
                }
            });
        }
    }
}
