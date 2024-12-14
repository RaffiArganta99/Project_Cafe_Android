package com.example.Bonanza.Model;
import com.google.gson.annotations.SerializedName;

public class UpdateCartRequest {
    @SerializedName("customerId")
    private int customerId;

    @SerializedName("cartId")
    private int cartId;

    @SerializedName("quantityChange")
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



