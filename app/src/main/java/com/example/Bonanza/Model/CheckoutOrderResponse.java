package com.example.Bonanza.Model;

import java.util.List;

public class CheckoutOrderResponse {
    private List<OrderData> data;

    public List<OrderData> getData() {
        return data;
    }

    public void setData(List<OrderData> data) {
        this.data = data;
    }

    public static class OrderData {
        private int OrderId;
        private int CustomerId;
        private double Total;
        private double Paid;
        private double Change;
        private String PaymentMethod;
        private String Status;
        private String CreatedAt;
        private List<OrderDetail> OrderDetails;

        // Getters and Setters for OrderData fields
        // ...

        public static class OrderDetail {
            private int MenuId;
            private int Quantity;
            private double Price;
            private double Subtotal;

            // Getters and Setters for OrderDetail fields
            // ...
        }
    }
}
