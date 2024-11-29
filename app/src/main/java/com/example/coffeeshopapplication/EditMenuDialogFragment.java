package com.example.coffeeshopapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Retrofit.ApiClient;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMenuDialogFragment extends DialogFragment {

    public interface OnEditCompleteListener {
        void onEditComplete(String name, String price, Uri newImageUri);
    }

    private static final int REQUEST_IMAGE_PICK = 1001;
    private final OnEditCompleteListener listener;
    private Uri imageUri;
    private ImageView menuImageView;
    private ApiService apiService;
    private int productId; // ID produk untuk mengupdate API

    public EditMenuDialogFragment(OnEditCompleteListener listener, Uri initialImageUri, int productId) {
        this.listener = listener;
        this.imageUri = initialImageUri;
        this.productId = productId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_menu, container, false);
        apiService = ApiClient.getApiService();

        menuImageView = view.findViewById(R.id.editMenuImage);
        EditText nameEditText = view.findViewById(R.id.editMenuName);
        EditText priceEditText = view.findViewById(R.id.editMenuPrice);
        Button confirmButton = view.findViewById(R.id.confirmButton);
        ImageButton cancelButton = view.findViewById(R.id.CancelButton);
        Button changeImageButton = view.findViewById(R.id.changeImageButton);

        // Tampilkan gambar saat ini
        menuImageView.setImageURI(imageUri);

        confirmButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String price = priceEditText.getText().toString();

            // Update ke API tanpa stok
            updateProductToApi(productId, name, price);

            if (listener != null) {
                listener.onEditComplete(name, price, imageUri);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        changeImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        return view;
    }

    private void updateProductToApi(int productId, String name, String price) {
        Product updatedProduct = new Product(productId, name, Double.parseDouble(price), imageUri.toString(), "", 0, ""); // 0 untuk stok, jika tidak diperlukan
        apiService.updateCartItem(productId, updatedProduct).enqueue(new Callback<Void>() {
            @Override
            public void onResponse(Call<Void> call, Response<Void> response) {
                if (response.isSuccessful()) {
                    Log.d("EditMenuDialog", "Product updated successfully");
                } else {
                    Log.e("EditMenuDialog", "Failed to update product: " + response.message());
                }
            }

            @Override
            public void onFailure(Call<Void> call, Throwable t) {
                Log.e("EditMenuDialog", "Error updating product: " + t.getMessage());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_PICK && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            menuImageView.setImageURI(imageUri);
        }
    }
}


