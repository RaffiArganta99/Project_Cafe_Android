package com.example.Bonanza;

public class TransactionItem {
    private String name;
    private String price;
    private int quantity;

    // Constructor untuk inisialisasi data
    public TransactionItem(String name, String price, int quantity) {
        this.name = name;
        this.price = price;
        this.quantity = quantity;
    }

    // Getter dan Setter untuk 'name'
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    // Getter dan Setter untuk 'price'
    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    // Getter dan Setter untuk 'quantity'
    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}

