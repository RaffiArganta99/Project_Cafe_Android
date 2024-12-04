package com.example.coffeeshopapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.example.coffeeshopapplication.Model.ResponseUpdate;
import com.example.coffeeshopapplication.Retrofit.ApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditMenuDialogFragment extends DialogFragment {

    public interface OnEditCompleteListener {
        void onEditComplete(String name, String price, Uri newImageUri, String category);
    }

    private static final int PICK_IMAGE_REQUEST = 1;
    private final OnEditCompleteListener listener;
    private Uri imageUri;
    private ImageView menuImageView;
    private ApiService apiService;
    private int productId; // ID produk untuk update
    private String selectedCategory; // Kategori yang dipilih

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
        Spinner categorySpinner = view.findViewById(R.id.spinner_c_menu);

        // Tampilkan gambar awal
        menuImageView.setImageURI(imageUri);

        // Inisialisasi spinner kategori
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.category_array, // Sumber array kategori di strings.xml
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedCategory = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                selectedCategory = null; // Set kategori null jika tidak dipilih
            }
        });

        confirmButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String price = priceEditText.getText().toString();

            // Validasi input
            if (name.isEmpty() || price.isEmpty() || selectedCategory == null) {
                Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
                return;
            }

            // Ambil nilai stock (misalnya, nilai default 0)
            int stock = 0;  // Atau dapatkan dari input yang relevan

            // Jika gambar diubah, upload gambar terlebih dahulu
            if (imageUri != null) {
                uploadImage(imageUri, name, price, selectedCategory, stock); // Kirim data bersama gambar dan stok
            } else {
                // Anda bisa memanggil metode lain untuk memperbarui produk tanpa gambar jika diperlukan
            }
        });


        cancelButton.setOnClickListener(v -> dismiss());

        changeImageButton.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void uploadImage(Uri imageUri, String name, String price, String category, int stock) {
        // Ensure all parameters are provided
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(price) || TextUtils.isEmpty(category)) {
            Toast.makeText(requireContext(), "All fields are required", Toast.LENGTH_SHORT).show();
            return;
        }

        String imagePath = getPathFromUri(imageUri);
        File imageFile = new File(imagePath);

        RequestBody idBody = RequestBody.create(MultipartBody.FORM, String.valueOf(productId));
        RequestBody nameBody = RequestBody.create(MultipartBody.FORM, name);
        RequestBody priceBody = RequestBody.create(MultipartBody.FORM, price);
        RequestBody categoryBody = RequestBody.create(MultipartBody.FORM, category);
        RequestBody stockBody = RequestBody.create(MultipartBody.FORM, String.valueOf(stock));

        MultipartBody.Part imagePart = null;
        if (imageUri != null) {
            RequestBody requestBody = RequestBody.create(MediaType.parse("image/*"), imageFile);
            imagePart = MultipartBody.Part.createFormData("imageUrl", imageFile.getName(), requestBody);
        }

//        apiService.updateMenu(idBody, nameBody, priceBody, categoryBody, stockBody, imagePart)
//                .enqueue(new Callback<ResponseUpdate>() {
//                    @Override
//                    public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
//                        if (response.isSuccessful() && response.body() != null) {
//                            Toast.makeText(requireContext(), "Product updated successfully.", Toast.LENGTH_SHORT).show();
//                            listener.onEditComplete(name, price, imageUri, category);
//                            dismiss();
//                        } else {
//                            Toast.makeText(requireContext(), "Failed to update product: " + response.message(), Toast.LENGTH_SHORT).show();
//                        }
//                    }
//
//                    @Override
//                    public void onFailure(Call<ResponseUpdate> call, Throwable t) {
//                        Toast.makeText(requireContext(), "Error updating product: " + t.getMessage(), Toast.LENGTH_SHORT).show();
//                    }
//                });
    }



    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = {MediaStore.Images.Media.DATA};
        try (Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        }
        return path;
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            menuImageView.setImageURI(imageUri);
        }
    }
}






