package com.example.coffeeshopapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("MenuId")
    private int menuId;

    @SerializedName("MenuName")
    private String menuName;

    @SerializedName("Price")
    private String price;

    @SerializedName("Stock")
    private String stock;

    @SerializedName("ImageUrl") // atau sesuai dengan nama field pada JSON dari API Anda
    private String imageUrl;

    @SerializedName("Category") // atau sesuai dengan nama field pada JSON dari API Anda
    private String category;

    @SerializedName("CreatedAt")
    private String createdAt;

    @SerializedName("Description")
    private String description;

    // Getter methods
    public int getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getPrice() {
        return price;
    }

    public String getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public String getCreatedAt() {
        return createdAt;
    }


    // setter ke kelas Menu
    public void setMenuId(int menuId) {
        this.menuId = menuId;
    }

    public void setMenuName(String menuName) {
        this.menuName = menuName;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
