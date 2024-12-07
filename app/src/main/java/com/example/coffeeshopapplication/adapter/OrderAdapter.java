package com.example.coffeeshopapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.Model.Order;
import com.example.coffeeshopapplication.R;

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
        Order order = orderList.get(position);

        // Set data ke TextView sesuai dengan ID di layout XML
        holder.textViewOrderId.setText("Id Order: " + order.getOrderId());
        holder.textViewTotal.setText("Total: Rp " + order.getTotal());
        holder.textViewCreatedAt.setText("Created At: " + order.getCreatedAt());
        holder.textViewStatus.setText("Status: " + order.getStatus());
        holder.textViewPaymentMethod.setText("Payment: " + order.getPaymentMethod());
    }

    @Override
    public int getItemCount() {
        return orderList.size();
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
