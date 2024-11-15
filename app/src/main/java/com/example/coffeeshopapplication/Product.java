package com.example.coffeeshopapplication;




public class Product {
    private String name;
    private int price;
    private String imageUri;
    private int quantity;
    private int id; // Pastikan Anda memiliki ID produk jika berhubungan dengan API

    // Constructor
    public Product(int id, String name, int price, String imageUri, int quantity) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
        this.quantity = quantity;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }
}

