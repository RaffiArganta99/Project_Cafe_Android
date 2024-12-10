package com.example.coffeeshopapplication;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Interface_API.OnTransactionItemListener;
import com.example.coffeeshopapplication.Model.CartResponse;
import com.example.coffeeshopapplication.Model.CheckoutOrderRequest;
import com.example.coffeeshopapplication.Model.CheckoutOrderResponse;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.adapter.TransactionAdapter;
import com.example.coffeeshopapplication.utilitas.CurrencyUtils;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TransactionFragment extends Fragment {

    private TextView totalText, tunaiText, kembalianText;
    private EditText tunaiEditText;
    private Button konfirmasiButton;
    private RecyclerView recyclerTransaction;
    private TransactionAdapter adapter;
    private List<CartResponse.CartItem> transactionItems;
    private int customerId = 1; // Pastikan ID sesuai dengan pengguna aktif.

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_transaction, container, false);


        // Inisialisasi view
        totalText = rootView.findViewById(R.id.Total_text);
        tunaiText = rootView.findViewById(R.id.textView28);
        kembalianText = rootView.findViewById(R.id.Kembalian_text);
        tunaiEditText = rootView.findViewById(R.id.editText_Tunai);
        konfirmasiButton = rootView.findViewById(R.id.konfirmasi_t);
        recyclerTransaction = rootView.findViewById(R.id.recyclerTransaction);

        transactionItems = new ArrayList<>();

        adapter = new TransactionAdapter(getContext(), transactionItems, customerId, new OnTransactionItemListener() {
            @Override
            public void onQuantityChange(int position, int newQuantity) {
                CartResponse.CartItem item = transactionItems.get(position);
                item.setQuantity(newQuantity);
                calculateTotal();
                calculateChange();
            }

            @Override
            public void onDeleteItem(int position) {
                // Reload entire cart data instead of manually removing
                loadCartData();
            }
        });

        recyclerTransaction.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerTransaction.setAdapter(adapter);

        // Modify the TextWatcher to include real-time change calculation
        tunaiEditText.addTextChangedListener(new TextWatcher() {
            private String current = "";

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                if (!s.toString().equals(current)) {
                    tunaiEditText.removeTextChangedListener(this);

                    String cleanString = s.toString().replaceAll("[Rp,.]", "").trim();
                    double parsed = cleanString.isEmpty() ? 0 : Double.parseDouble(cleanString);
                    String formatted = CurrencyUtils.formatToRupiah(parsed).replace("Rp", "").trim();

                    current = formatted;
                    tunaiEditText.setText(formatted);
                    tunaiEditText.setSelection(formatted.length());

                    // Add real-time change calculation
                    calculateChange();

                    tunaiEditText.addTextChangedListener(this);
                }
            }
        });

        // Tambahkan listener untuk tombol konfirmasi
        konfirmasiButton.setOnClickListener(v -> checkoutOrder());

        // Load data awal
        loadCartData();

        return rootView;
    }

    private void loadCartData() {
        ApiService apiService = ApiClient.getApiService();
        apiService.getCart().enqueue(new Callback<CartResponse>() {
            @Override
            public void onResponse(Call<CartResponse> call, Response<CartResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    transactionItems.clear();
                    transactionItems.addAll(response.body().getData());
                    adapter.notifyDataSetChanged();

                    // Calculate total and change
                    calculateTotal();
                    calculateChange();
                } else {
                    Toast.makeText(getContext(), "Gagal memuat keranjang", Toast.LENGTH_SHORT).show();

                    // Reset total and change texts when cart loading fails
                    if (totalText != null) totalText.setText(CurrencyUtils.formatToRupiah(0));
                    if (kembalianText != null) kembalianText.setText("Rp0");
                }
            }

            @Override
            public void onFailure(Call<CartResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Kesalahan koneksi: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                // Reset total and change texts when network fails
                if (totalText != null) totalText.setText(CurrencyUtils.formatToRupiah(0));
                if (kembalianText != null) kembalianText.setText("Rp0");
            }
        });
    }

    private void calculateTotal() {
        int total = 0;
        for (CartResponse.CartItem item : transactionItems) {
            total += item.getPrice() * item.getQuantity();
        }
        if (totalText != null) {
            totalText.setText(CurrencyUtils.formatToRupiah(total));
        }
    }

    private void calculateChange() {
        if (totalText == null || TextUtils.isEmpty(totalText.getText())) {
            return;
        }

        try {
            // Parsing total from TextView
            double total = Double.parseDouble(totalText.getText().toString()
                    .replace("Rp", "")
                    .replace(".", "")
                    .trim());

            // Parsing cash input from EditText
            String tunaiString = tunaiEditText.getText().toString()
                    .replace(".", "")
                    .trim();

            if (!tunaiString.isEmpty()) {
                double tunai = Double.parseDouble(tunaiString);
                double kembalian = tunai - total;

                // Set Kembalian_text with currency format
                // Ensure change is non-negative
                kembalianText.setText(CurrencyUtils.formatToRupiah(Math.max(0, kembalian)));
            } else {
                // If cash input is empty, change is Rp0
                kembalianText.setText("Rp0");
            }
        } catch (NumberFormatException e) {
            // Error handling if parsing fails
            kembalianText.setText("Rp0");
        }
    }


    private void checkoutOrder() {
        String paidText = tunaiEditText.getText().toString().replace(".", "").trim();
        if (paidText.isEmpty()) {
            Toast.makeText(getContext(), "Harap masukkan jumlah tunai!", Toast.LENGTH_SHORT).show();
            return;
        }

        double paid = Double.parseDouble(paidText);
        double total = Double.parseDouble(totalText.getText().toString().replace("Rp", "").replace(".", "").trim());
        if (paid < total) {
            Toast.makeText(getContext(), "Jumlah tunai kurang dari total!", Toast.LENGTH_SHORT).show();
            return;
        }

        double change = paid - total;
        kembalianText.setText(CurrencyUtils.formatToRupiah(change));

        CheckoutOrderRequest request = new CheckoutOrderRequest(customerId, paid, "Cash");
        ApiService apiService = ApiClient.getApiService();
        apiService.checkoutOrderFromCart(request).enqueue(new Callback<CheckoutOrderResponse>() {
            @Override
            public void onResponse(Call<CheckoutOrderResponse> call, Response<CheckoutOrderResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    Toast.makeText(getContext(), "Checkout berhasil!", Toast.LENGTH_SHORT).show();
                    tunaiEditText.setText("");
                    kembalianText.setText("Rp0");
                    totalText.setText("Rp0");
                    loadCartData();
                } else {
                    Toast.makeText(getContext(), "Checkout gagal!", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<CheckoutOrderResponse> call, Throwable t) {
                Toast.makeText(getContext(), "Gagal melakukan checkout: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}













