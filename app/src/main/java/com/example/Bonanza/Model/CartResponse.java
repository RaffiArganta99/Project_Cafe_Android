package com.example.Bonanza.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class CartResponse {
    @SerializedName("data")
    private List<CartItem> data;

    public List<CartItem> getData() {
        return data;
    }

    public void setData(List<CartItem> data) {
        this.data = data;
    }

    public static class CartItem {
        @SerializedName("CartId")
        private int cartId;

        @SerializedName("ImageUrl")
        private String imageUrl;

        @SerializedName("MenuName")
        private String menuName;

        @SerializedName("Description")
        private String description;

        @SerializedName("Price")
        private double price;

        @SerializedName("Stock")
        private int stock;

        @SerializedName("Quantity")
        private int quantity;

        @SerializedName("TotalPrice")
        private double totalPrice;

        @SerializedName("CreatedAt")
        private String createdAt;

        // Getter dan Setter
        public int getCartId() { return cartId; }
        public void setCartId(int cartId) { this.cartId = cartId; }

        public String getImageUrl() { return imageUrl; }
        public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

        public String getMenuName() { return menuName; }
        public void setMenuName(String menuName) { this.menuName = menuName; }

        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }

        public double getPrice() { return price; }
        public void setPrice(double price) { this.price = price; }

        public int getStock() { return stock; }
        public void setStock(int stock) { this.stock = stock; }

        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }

        public double getTotalPrice() { return totalPrice; }
        public void setTotalPrice(double totalPrice) { this.totalPrice = totalPrice; }

        public String getCreatedAt() { return createdAt; }
        public void setCreatedAt(String createdAt) { this.createdAt = createdAt; }
    }
}

