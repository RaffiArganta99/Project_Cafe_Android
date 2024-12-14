package com.example.Bonanza;

import static java.io.File.createTempFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.Bonanza.Retrofit.ApiClient;
import com.example.Bonanza.Interface_API.ApiService;

import java.io.File;
import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.Manifest;


public class Add_Item_Fragment extends Fragment {

    private static final int PICK_IMAGE_REQUEST = 1;
    private EditText etMenuName, etPrice, etStock, etDescription;
    private Spinner spCategory;
    private ImageView ivPreviewImage;
    private Button btnSelectImage, btnAddMenu;
    private Uri imageUri;

    @SuppressLint("MissingInflatedId")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add__item_, container, false);

        // Periksa izin
        checkPermissions();

        // Inisialisasi views
        etMenuName = view.findViewById(R.id.etMenuName);
        etPrice = view.findViewById(R.id.etPrice);
        etStock = view.findViewById(R.id.etStock);
        etDescription = view.findViewById(R.id.etDescription);
        spCategory = view.findViewById(R.id.spCategory);
        ivPreviewImage = view.findViewById(R.id.ivPreviewImage);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnAddMenu = view.findViewById(R.id.btnAddMenu);

        // Set listener
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnAddMenu.setOnClickListener(v -> validateAndUpload());

        // Set spinner kategori
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                getContext(),
                R.array.categories,
                android.R.layout.simple_spinner_item
        );
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spCategory.setAdapter(adapter);

        return view;
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
            ivPreviewImage.setImageURI(imageUri); // Menampilkan gambar
        }
    }

    private void validateAndUpload() {
        String menuName = etMenuName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();

        // Validasi input
        if (TextUtils.isEmpty(menuName) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr) ||
                TextUtils.isEmpty(description) || imageUri == null) {
            Toast.makeText(getContext(), "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(priceStr.trim()) || TextUtils.isEmpty(stockStr.trim()) ||
                !TextUtils.isDigitsOnly(priceStr) || !TextUtils.isDigitsOnly(stockStr)) {
            Toast.makeText(getContext(), "Price and Stock must be numeric and not empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Debug logging
        Log.d("AddMenu", "Validating inputs: " +
                "\nMenu Name: " + menuName +
                "\nPrice: " + priceStr +
                "\nStock: " + stockStr +
                "\nIs Price Numeric: " + TextUtils.isDigitsOnly(priceStr) +
                "\nIs Stock Numeric: " + TextUtils.isDigitsOnly(stockStr));


        // Mengonversi harga dan stok ke integer
        int price = Integer.parseInt(priceStr);
        int stock = Integer.parseInt(stockStr);

        // Validasi harga dan stok agar lebih besar dari 0
        if (price <= 0 || stock <= 0) {
            Toast.makeText(getContext(), "Price and Stock must be positive numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Kirim data setelah validasi berhasil
        uploadMenu(menuName, price, stock, category, description, imageUri);
    }



    private void uploadMenu(String menuName, int price, int stock, String category, String description, Uri imageUri) {
        File file = new File(getRealPathFromURI(imageUri));

        if (!file.exists()) {
            Toast.makeText(getContext(), "File does not exist", Toast.LENGTH_SHORT).show();
            return;
        }

        // PENTING: Konversi ke string dengan String.valueOf()
        RequestBody namePart = RequestBody.create(MediaType.parse("text/plain"), menuName);
        RequestBody pricePart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(price));
        RequestBody stockPart = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(stock));
        RequestBody categoryPart = RequestBody.create(MediaType.parse("text/plain"), category);
        RequestBody descriptionPart = RequestBody.create(MediaType.parse("text/plain"), description);

        RequestBody requestFile = RequestBody.create(MediaType.parse("image/*"), file);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imageUrl", file.getName(), requestFile);

        ApiService apiService = ApiClient.getApiService();
        Call<ResponseBody> call = apiService.addMenu(
                namePart,
                descriptionPart,
                pricePart,
                stockPart,
                categoryPart,
                imagePart
        );

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    // Log raw response
                    String responseString = response.body() != null ? response.body().string() : "Empty response";
                    Log.d("AddMenu", "Server Response: " + responseString);

                    if (response.isSuccessful()) {
                        Toast.makeText(getContext(), "Menu added successfully!", Toast.LENGTH_SHORT).show();
                        resetForm();
                    } else {
                        // Log error body
                        String errorBody = response.errorBody() != null ? response.errorBody().string() : "Unknown error";
                        Toast.makeText(getContext(), "Failed: " + errorBody, Toast.LENGTH_SHORT).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(getContext(), "Error processing response", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Toast.makeText(getContext(), "Network Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }



    private void resetForm() {
        etMenuName.setText("");
        etPrice.setText("");
        etStock.setText("");
        etDescription.setText("");
        spCategory.setSelection(0);
        ivPreviewImage.setImageResource(R.drawable.default_image);
        imageUri = null;
    }

    private String getRealPathFromURI(Uri uri) {
        String[] projection = {MediaStore.Images.Media.DATA};
        Cursor cursor = getContext().getContentResolver().query(uri, projection, null, null, null);
        if (cursor != null) {
            int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(columnIndex);
            cursor.close();
            return path;
        }
        return null;
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        }
    }
}