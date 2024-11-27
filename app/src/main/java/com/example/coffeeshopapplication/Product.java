package com.example.coffeeshopapplication;

public class Product {
    private String name;     // Nama produk
    private double price;    // Harga produk
    private String imageUri; // URI gambar produk
    private int stock;       // Stok produk
    private int id;          // ID produk (berhubungan dengan API)
    private String category; // Kategori produk
    private String description; // Deskripsi produk

    // Constructor
    public Product(int id, String name, double price, String imageUri, String category, int stock, String description) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
        this.category = category;
        this.stock = stock;
        this.description = description;
    }

    // Getter and Setter methods
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public double getPrice() {
        return price;
    }

    public String getImageUri() {
        return imageUri;
    }

    public int getStock() {
        return stock;
    }

    public String getCategory() {
        return category;
    }

    public void setStock(int stock) {
        this.stock = stock;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setImageUri(String imageUri) {
        this.imageUri = imageUri;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", imageUri='" + imageUri + '\'' +
                ", stock=" + stock +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
