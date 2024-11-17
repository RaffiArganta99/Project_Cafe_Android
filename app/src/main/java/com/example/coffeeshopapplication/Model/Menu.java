package com.example.coffeeshopapplication.Model;

import com.google.gson.annotations.SerializedName;

public class Menu {

    @SerializedName("MenuId")
    private int menuId;

    @SerializedName("MenuName")
    private String menuName;

    @SerializedName("Price")
    private String price;

    // Getter methods
    public int getMenuId() {
        return menuId;
    }

    public String getMenuName() {
        return menuName;
    }

    public String getPrice() {
        return price;
    }
}
