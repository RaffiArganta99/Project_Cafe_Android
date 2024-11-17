package com.example.coffeeshopapplication.Model;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class MenuResponse {

    @SerializedName("data")
    private List<Menu> menuList;

    public List<Menu> getMenuList() {
        return menuList;
    }

    public void setMenuList(List<Menu> menuList) {
        this.menuList = menuList;
    }
}
