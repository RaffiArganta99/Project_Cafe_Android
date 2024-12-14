package com.example.Bonanza.Interface_API;

public interface OnTransactionItemListener {
    void onQuantityChange(int position, int newQuantity);
    void onDeleteItem(int position);
//    void onDataChanged();  // Callback untuk memperbarui total data di fragment
}

