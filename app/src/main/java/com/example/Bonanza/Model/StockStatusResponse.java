package com.example.Bonanza.Model;

import java.util.List;

public class StockStatusResponse {
    private List<StockStatusItem> data;

    public StockStatusResponse(List<StockStatusItem> data) {
        this.data = data;
    }

    public List<StockStatusItem> getData() {
        return data;
    }

    public void setData(List<StockStatusItem> data) {
        this.data = data;
    }
}
