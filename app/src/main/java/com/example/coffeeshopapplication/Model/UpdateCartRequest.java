package com.example.coffeeshopapplication.Model;

public class UpdateCartRequest {
    private int customerId;
    private int cartId;
    private int quantityChange;

    // Constructor
    public UpdateCartRequest(int customerId, int cartId, int quantityChange) {
        this.customerId = customerId;
        this.cartId = cartId;
        this.quantityChange = quantityChange;
    }

    // Getter dan Setter
    public int getCustomerId() {
        return customerId;
    }

    public void setCustomerId(int customerId) {
        this.customerId = customerId;
    }

    public int getCartId() {
        return cartId;
    }

    public void setCartId(int cartId) {
        this.cartId = cartId;
    }

    public int getQuantityChange() {
        return quantityChange;
    }

    public void setQuantityChange(int quantityChange) {
        this.quantityChange = quantityChange;
    }
}

