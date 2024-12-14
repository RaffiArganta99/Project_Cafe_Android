package com.example.Bonanza.Model;

public class Order {
    private int OrderId;
    private int CustomerId;
    private double Total;
    private double Paid;
    private double Change;
    private String PaymentMethod;
    private String Status;
    private String CreatedAt;

    // Getters and Setters
    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public double getTotal() {
        return Total;
    }

    public void setTotal(double total) {
        Total = total;
    }

    public double getPaid() {
        return Paid;
    }

    public void setPaid(double paid) {
        Paid = paid;
    }

    public double getChange() {
        return Change;
    }

    public void setChange(double change) {
        Change = change;
    }

    public String getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public String getStatus() {
        return Status;
    }

    public void setStatus(String status) {
        Status = status;
    }

    public String getCreatedAt() {
        return CreatedAt;
    }

    public void setCreatedAt(String createdAt) {
        CreatedAt = createdAt;
    }
}
