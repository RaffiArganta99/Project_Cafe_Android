package com.example.coffeeshopapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("MenuId")
    private int menuId;

    @SerializedName("MenuName")
    private String menuName;

    @SerializedName("Price")
    private String price;

    @SerializedName("ImageUrl") // atau sesuai dengan nama field pada JSON dari API Anda
    private String imageUrl;

    @SerializedName("Category") // atau sesuai dengan nama field pada JSON dari API Anda
    private String category;

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

    public String getCategory() {
        return category;
    }
}
