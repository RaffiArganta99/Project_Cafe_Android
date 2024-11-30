package com.example.coffeeshopapplication.Model;

public class ResponseUpdateStock {
    private String status;
    private int newStock;
    private String message;

    // Konstruktor default
    public ResponseUpdateStock() {}

    // Konstruktor dengan parameter
    public ResponseUpdateStock(String status, int newStock, String message) {
        this.status = status;
        this.newStock = newStock;
        this.message = message;
    }

    // Getter untuk status
    public String getStatus() {
        return status;
    }

    // Setter untuk status
    public void setStatus(String status) {
        this.status = status;
    }

    // Getter untuk newStock
    public int getNewStock() {
        return newStock;
    }

    // Setter untuk newStock
    public void setNewStock(int newStock) {
        this.newStock = newStock;
    }

    // Getter untuk message
    public String getMessage() {
        return message;
    }

    // Setter untuk message
    public void setMessage(String message) {
        this.message = message;
    }

    // Optional: toString method untuk debugging
    @Override
    public String toString() {
        return "ResponseUpdateStock{" +
                "status='" + status + '\'' +
                ", newStock=" + newStock +
                ", message='" + message + '\'' +
                '}';
    }
}
