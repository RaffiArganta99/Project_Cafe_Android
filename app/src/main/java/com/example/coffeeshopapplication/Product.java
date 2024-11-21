package com.example.coffeeshopapplication;

public class Product {
    private String name;     // Nama produk
    private double price;    // Menggunakan String jika harga disertai simbol atau format lainnya
    private String imageUri; // URI gambar produk
    private int quantity;    // Jumlah produk
    private int id;          // ID produk (berhubungan dengan API)
    private String category;

    // Constructor
    public Product(int id, String name, double price, String imageUri, int quantity, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.imageUri = imageUri;
        this.quantity = quantity;
        this.category = category; // Tambahkan inisialisasi category
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

    public int getQuantity() {
        return quantity;
    }

    public String getCategory() {
        return category;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public void setPrice(double price) { // Sesuaikan tipe data dengan API
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

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price='" + price + '\'' +
                ", imageUri='" + imageUri + '\'' +
                ", quantity=" + quantity +
                '}';
    }
}
