package com.example.Bonanza.Model;

public class DeleteCartRequest {
    private int cartId;
    private int customerId;

    public DeleteCartRequest(int cartId, int customerId) {
        this.cartId = cartId;
        this.customerId = customerId;
    }
}
