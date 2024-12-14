package com.example.Bonanza.Model;

public class CheckoutOrderRequest {
    private int CustomerId;
    private double Paid;
    private String PaymentMethod;

    // Constructor
    public CheckoutOrderRequest(int customerId, double paid, String paymentMethod) {
        this.CustomerId = customerId;
        this.Paid = paid;
        this.PaymentMethod = paymentMethod;
    }

    // Getters and Setters
    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public double getPaid() {
        return Paid;
    }

    public void setPaid(double paid) {
        Paid = paid;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }
}

