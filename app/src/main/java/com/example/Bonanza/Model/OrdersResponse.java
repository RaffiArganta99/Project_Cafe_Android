package com.example.Bonanza.Model;

import java.util.List;

public class OrdersResponse {
    private List<Order> data;

    public List<Order> getData() {
        return data;
    }

    public void setData(List<Order> data) {
        this.data = data;
    }
}
