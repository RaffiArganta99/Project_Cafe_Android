package com.example.coffeeshopapplication.adapter;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import com.example.coffeeshopapplication.EditMenuDialogFragment;
import com.example.coffeeshopapplication.databinding.CartItemBinding;
import java.util.ArrayList;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private final Context context;
    private final ArrayList<String> cartItems;
    private final ArrayList<String> cartItemPrices;
    private final ArrayList<Uri> cartImages;
    private int[] itemQuantities;
    private final OnAddToCartClickListener listener;

    // Interface untuk listener
    public interface OnAddToCartClickListener {
        void onAddToCart(String name, String price, int quantity);
    }

    // Constructor `CartAdapter` dengan listener
    public CartAdapter(Context context, ArrayList<String> cartItems, ArrayList<String> cartItemPrices, ArrayList<Uri> cartImages, OnAddToCartClickListener listener) {
        this.context = context;
        this.cartItems = cartItems;
        this.cartItemPrices = cartItemPrices;
        this.cartImages = cartImages;
        this.listener = listener;
        this.itemQuantities = new int[cartItems.size()];

        // Set initial quantity for each item to 1
        for (int i = 0; i < itemQuantities.length; i++) {
            itemQuantities[i] = 1;
        }
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

    // Fungsi untuk memperbarui gambar item di cart
    public void setImageForCartItem(Uri selectedImageUri, int position) {
        cartImages.set(position, selectedImageUri);
        notifyItemChanged(position);
    }

    // ViewHolder class
    public class CartViewHolder extends RecyclerView.ViewHolder {
        private final CartItemBinding binding;

        public CartViewHolder(CartItemBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            // Set listener untuk "Keranjang" button
            binding.ItemCart.setOnClickListener(view -> {
                int position = getAdapterPosition();
                String name = cartItems.get(position);
                String price = cartItemPrices.get(position);
                int quantity = itemQuantities[position];

                // Panggil listener untuk mengirim data ke activity
                listener.onAddToCart(name, price, quantity);
            });

            // Set click listeners untuk tombol-tombol lainnya
            binding.minusButton.setOnClickListener(view -> decreaseQuantity(getAdapterPosition()));
            binding.plusButton.setOnClickListener(view -> increaseQuantity(getAdapterPosition()));
            binding.deleteButton.setOnClickListener(view -> deleteItem(getAdapterPosition()));
            binding.editButton.setOnClickListener(view -> openEditDialog(getAdapterPosition()));
        }

        public void bind(int position) {
            String itemName = cartItems.get(position);
            String itemPrice = cartItemPrices.get(position);
            Uri itemImage = cartImages.get(position);
            int quantity = itemQuantities[position];

            binding.cartFoodName.setText(itemName);
            binding.cartItemPrice.setText(itemPrice);

            // Tampilkan gambar dari URI jika tersedia, atau gunakan placeholder jika tidak
            if (itemImage != null) {
                binding.cartImage.setImageURI(itemImage);
            } else {
                binding.cartImage.setImageResource(com.example.coffeeshopapplication.R.drawable.default_image); // Placeholder jika URI null
            }

            binding.cartItemQuantity.setText(String.valueOf(quantity));
        }

        // Method untuk mengurangi jumlah
        private void decreaseQuantity(int position) {
            if (itemQuantities[position] > 1) {
                itemQuantities[position]--;
                binding.cartItemQuantity.setText(String.valueOf(itemQuantities[position]));
            }
        }

        // Method untuk menambah jumlah
        private void increaseQuantity(int position) {
            if (itemQuantities[position] < 40) {
                itemQuantities[position]++;
                binding.cartItemQuantity.setText(String.valueOf(itemQuantities[position]));
            }
        }

        // Method untuk menghapus item
        private void deleteItem(int position) {
            if (position >= 0 && position < cartItems.size()) {
                cartItems.remove(position);
                cartImages.remove(position);
                cartItemPrices.remove(position);

                int[] newQuantities = new int[itemQuantities.length - 1];
                for (int i = 0, j = 0; i < itemQuantities.length; i++) {
                    if (i != position) {
                        newQuantities[j++] = itemQuantities[i];
                    }
                }
                itemQuantities = newQuantities;

                notifyItemRemoved(position);
                notifyItemRangeChanged(position, cartItems.size());
            }
        }

        // Method untuk membuka dialog edit
        private void openEditDialog(int position) {
            Uri imageResourceId = cartImages.get(position);

            EditMenuDialogFragment dialog = new EditMenuDialogFragment((name, price, newImageUri) -> {
                cartItems.set(position, name);
                cartItemPrices.set(position, price);
                cartImages.set(position, newImageUri);
                notifyItemChanged(position);
            }, imageResourceId);

            dialog.show(((AppCompatActivity) context).getSupportFragmentManager(), "EditMenuDialog");
        }
    }

}
