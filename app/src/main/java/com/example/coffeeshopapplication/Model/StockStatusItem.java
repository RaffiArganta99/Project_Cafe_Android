package com.example.coffeeshopapplication.Model;
import com.google.gson.annotations.SerializedName;
public class StockStatusItem {

    @SerializedName("MenuId")
    private int MenuId;

    @SerializedName("MenuName")
    private String MenuName;

    @SerializedName("Stock")
    private int Stock;

    @SerializedName("ImageUrl")
    private String ImageUrl;


    public StockStatusItem(int menuId, String menuName, int stock, String imageUrl) {
        this.MenuId = menuId;
        this.MenuName = menuName;
        this.Stock = stock;
        this.ImageUrl = imageUrl;
    }

    // Getters and setters
    public int getMenuId() {
        return MenuId;
    }

    public void setMenuId(int menuId) {
        MenuId = menuId;
    }

    public String getMenuName() {
        return MenuName;
    }

    public void setMenuName(String menuName) {
        MenuName = menuName;
    }

    public int getStock() {
        return Stock;
    }

    public void setStock(int stock) {
        Stock = stock;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }
}
