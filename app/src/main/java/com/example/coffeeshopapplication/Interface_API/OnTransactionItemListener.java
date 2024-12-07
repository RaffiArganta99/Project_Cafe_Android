package com.example.coffeeshopapplication.Interface_API;

public interface OnTransactionItemListener {
    void onQuantityChange(int position, int newQuantity);
    void onDeleteItem(int position);
}

