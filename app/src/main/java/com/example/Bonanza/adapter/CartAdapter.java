package com.example.Bonanza.adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import java.text.NumberFormat;
import java.util.Locale;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.FragmentActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Bonanza.EditMenuDialogFragment;
import com.example.Bonanza.Interface_API.ApiService;
import com.example.Bonanza.Model.ApiResponse;
import com.example.Bonanza.Model.MenuResponse;
import com.example.Bonanza.Model.ResponseUpdate;
import com.example.Bonanza.Product;
import com.example.Bonanza.R;
import com.example.Bonanza.Retrofit.ApiClient;
import com.example.Bonanza.databinding.CartItemBinding;
import com.example.Bonanza.Model.Menu;
import com.squareup.picasso.Picasso;
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
    private final int customerId; // Tambahkan ini

    public interface OnAddToCartClickListener {
        void onAddToCart(String name, double price, int stock);
        void onItemCartClick(Product product);  // Menambahkan metode ini
    }

    public CartAdapter(Context context, ArrayList<Product> cartItems, OnAddToCartClickListener listener, int customerId) {
        this.context = context;
        this.cartItems = cartItems;
        this.listener = listener;
        this.apiService = ApiClient.getApiService();
        this.menuList = new ArrayList<>();
        this.customerId = 1;  // Mengatur customerId tetap dengan nilai 1

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

            holder.binding.ItemCart.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onItemCartClick(currentProduct);
                }
            });

            // Menampilkan kategori
            holder.cartItemCategory.setText(
                    currentProduct.getCategory() != null && !currentProduct.getCategory().isEmpty()
                            ? currentProduct.getCategory()
                            : "Unknown"
            );

            // Format price using Indonesian currency
            holder.cartItemPrice.setText(formatRupiah(currentProduct.getPrice()));

            // Menampilkan stok
            holder.cartItemStock.setText(String.valueOf(currentProduct.getStock()));

            // Menampilkan gambar
            Picasso.get()
                    .load(currentProduct.getImageUri())
                    .placeholder(R.drawable.default_image)
                    .error(R.drawable.default_image)
                    .into(holder.cartImage);

            // Tombol Add to Cart
            holder.binding.ItemCart.setOnClickListener(v -> {
                // Pastikan currentProduct berisi informasi yang dibutuhkan
                int customerId = getCustomerId(); // Anda harus memiliki metode untuk mendapatkan customerId
                int menuId = currentProduct.getId(); // Ambil ID produk dari currentProduct
                int quantity = 1; // Jumlah default yang ditambahkan ke cart (misalnya 1)

                addToCart(customerId, menuId, quantity); // Gunakan customerId
            });


            holder.binding.editButton.setOnClickListener(v -> {
                EditMenuDialogFragment dialog = new EditMenuDialogFragment((name, price, uri, category) -> {
                    currentProduct.setName(name);
                    currentProduct.setPrice(Double.parseDouble(price));
                    currentProduct.setImageUri(uri.toString());
                    currentProduct.setCategory(category);
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

    // Helper method to format Rupiah
    private String formatRupiah(double amount) {
        NumberFormat formatter = NumberFormat.getCurrencyInstance(new Locale("id", "ID"));

        // Format and remove decimal and currency symbol
        String formattedPrice = formatter.format(amount)
                .replace("Rp", "")  // Remove default currency symbol
                .split(",")[0]      // Remove decimal part
                .trim();

        return "Rp" + formattedPrice;
    }

    // Mengambil customerId dari SharedPreferences
    private int getCustomerId() {
        SharedPreferences sharedPreferences = context.getSharedPreferences("AppPrefs", Context.MODE_PRIVATE);
        return 1;  // Mengembalikan nilai tetap 1 untuk customerId
    }

    private void addToCart(int customerId, int menuId, int quantity) {
        if (customerId == -1) {
            Log.e("AddToCart", "Customer ID tidak ditemukan");
            Toast.makeText(context, "Customer ID tidak valid. Silakan login ulang.", Toast.LENGTH_SHORT).show();
            return;
        }

        Map<String, Object> requestBody = new HashMap<>();
        requestBody.put("customerId", customerId);
        requestBody.put("menuId", menuId);
        requestBody.put("quantity", quantity);

        apiService.addToCart(requestBody).enqueue(new Callback<ApiResponse>() {
            @Override
            public void onResponse(Call<ApiResponse> call, Response<ApiResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Log.d("AddToCart", "Success: " + response.body().getMessage());
                    Toast.makeText(context, response.body().getMessage(), Toast.LENGTH_SHORT).show();
                } else {
                    Log.e("AddToCart", "Failed: " + response.message());
                    Toast.makeText(context, "Failed to add to cart. Please try again.", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ApiResponse> call, Throwable t) {
                Log.e("AddToCart", "Error: " + t.getMessage());
                Toast.makeText(context, "Error adding to cart. Please check your connection.", Toast.LENGTH_SHORT).show();
            }
        });
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
                Log.e("CartAdapter", "Gagal mengurai harga menu: " + menu.getPrice(), e);
            }

            // Parsing stok dari String ke integer
            try {
                stockValue = Integer.parseInt(menu.getStock()); // Tetap menggunakan Integer.parseInt
            } catch (NumberFormatException e) {
                Log.e("CartAdapter", "Gagal mengurai stok menu: " + menu.getStock(), e);
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
                            Log.d("CartAdapter", "Stok berhasil diperbarui.");
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
                    .setTitle("Konfirmasi Penghapusan")
                    .setMessage("Apakah Anda yakin ingin menghapus item ini?")
                    .setPositiveButton("Ya.", (dialog, which) -> {
                        // Jika pengguna mengkonfirmasi penghapusan
                        apiService. deleteCartItem(product.getId()).enqueue(new Callback<Void>() {
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

        public void clearCart() {
            cartItems.clear(); // Asumsikan `cartItems` adalah daftar yang digunakan untuk menyimpan item di keranjang
            notifyDataSetChanged();
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