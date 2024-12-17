package com.example.Bonanza.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.example.Bonanza.Model.Order;
import com.example.Bonanza.R;
import java.text.NumberFormat;
import java.util.Locale;
import java.util.List;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    private List<Order> orderList;

    public OrderAdapter(List<Order> orderList) {
        this.orderList = orderList;
    }

    @NonNull
    @Override
    public OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.history_item, parent, false);
        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderViewHolder holder, int position) {
        // Reverse the position to display in descending order
        Order order = orderList.get(orderList.size() - 1 - position);

        // Format Order ID in descending order
        holder.textViewOrderId.setText("ID: " + (orderList.size() - position));

        // Format total amount with Indonesian currency formatting
        holder.textViewTotal.setText("Total: Rp" + formatRupiah(order.getTotal()));
        holder.textViewCreatedAt.setText("Tanggal: " + order.getCreatedAt());
        holder.textViewStatus.setText(order.getStatus());
        holder.textViewPaymentMethod.setText("Payment: " + order.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
    }

    // Helper method to format Rupiah
    private String formatRupiah(double amount) {
        // Remove decimal part for simplicity
        int intAmount = (int) amount;

        // Use NumberFormat to add thousand separators
        NumberFormat formatter = NumberFormat.getInstance(new Locale("id", "ID"));
        return formatter.format(intAmount);
    }

    public static class OrderViewHolder extends RecyclerView.ViewHolder {
        TextView textViewOrderId, textViewTotal, textViewCreatedAt, textViewStatus, textViewPaymentMethod;

        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewOrderId = itemView.findViewById(R.id.textViewOrderId);
            textViewTotal = itemView.findViewById(R.id.textViewTotal);
            textViewCreatedAt = itemView.findViewById(R.id.textViewCreatedAt);
            textViewStatus = itemView.findViewById(R.id.textViewStatus);
            textViewPaymentMethod = itemView.findViewById(R.id.textViewPaymentMethod);
        }
    }
}
