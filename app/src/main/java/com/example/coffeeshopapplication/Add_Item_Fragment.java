package com.example.coffeeshopapplication;

import static java.io.File.createTempFile;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.provider.OpenableColumns;
import android.text.TextUtils;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.coffeeshopapplication.Model.PostMenuResponse;
import com.example.coffeeshopapplication.Retrofit.ApiClient;
import com.example.coffeeshopapplication.Interface_API.ApiService;
import com.google.gson.JsonObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

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

        // Initialize views
        etMenuName = view.findViewById(R.id.etMenuName);
        etPrice = view.findViewById(R.id.etPrice);
        etStock = view.findViewById(R.id.etStock);
        spCategory = view.findViewById(R.id.spCategory);
        ivPreviewImage = view.findViewById(R.id.ivPreviewImage);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        btnAddMenu = view.findViewById(R.id.btnAddMenu);
        etDescription = view.findViewById(R.id.etDescription);

        // Set listeners
        btnSelectImage.setOnClickListener(v -> openImagePicker());
        btnAddMenu.setOnClickListener(v -> validateAndUpload());

        // Set up spinner with category data
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
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
            return;
        }

        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            imageUri = data.getData();
            ivPreviewImage.setImageURI(imageUri); // Show preview of selected image
        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        if (imageUri != null) {
            Log.d("AddMenu", "Saving image URI: " + imageUri.toString());
            outState.putString("image_uri", imageUri.toString());
        }
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null && savedInstanceState.containsKey("image_uri")) {
            imageUri = Uri.parse(savedInstanceState.getString("image_uri"));
            Log.d("AddMenu", "Restoring image URI: " + imageUri.toString());
            ivPreviewImage.setImageURI(imageUri);
        }
    }

    private void validateAndUpload() {
        String menuName = etMenuName.getText().toString().trim();
        String priceStr = etPrice.getText().toString().trim();
        String stockStr = etStock.getText().toString().trim();
        String description = etDescription.getText().toString().trim();
        String category = spCategory.getSelectedItem().toString();

        if (TextUtils.isEmpty(menuName) || TextUtils.isEmpty(priceStr) || TextUtils.isEmpty(stockStr) ||
                TextUtils.isEmpty(description) || imageUri == null) {
            Toast.makeText(getContext(), "Please fill in all fields and select an image.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Validasi angka
        int price, stock;
        try {
            price = Integer.parseInt(priceStr);
            stock = Integer.parseInt(stockStr);
            if (price <= 0 || stock <= 0) {
                Toast.makeText(getContext(), "Price and stock must be positive numbers.", Toast.LENGTH_SHORT).show();
                return;
            }
        } catch (NumberFormatException e) {
            Toast.makeText(getContext(), "Price and stock must be valid numbers.", Toast.LENGTH_SHORT).show();
            return;
        }

        // Lakukan upload
        uploadMenu(menuName, price, stock, category, description, imageUri);
    }

    private void resetForm() {
        etMenuName.setText("");  // Reset EditText
        etPrice.setText("");     // Reset EditText
        etStock.setText("");     // Reset EditText
        etDescription.setText("");  // Reset EditText
        spCategory.setSelection(0);  // Reset Spinner to the first item
        ivPreviewImage.setImageResource(R.drawable.default_image);  // Set default image or clear the preview
        imageUri = null;  // Clear image URI
    }


    private String generateUniqueFileName(String fileExtension) {
        return UUID.randomUUID().toString().replace("-", "") + "." + fileExtension;
    }

    private String getFileExtensionFromUri(Uri uri) {
        String extension = null;
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        String mimeType = contentResolver.getType(uri);

        if (mimeType != null) {
            extension = mimeTypeMap.getExtensionFromMimeType(mimeType);
        }

        return extension;
    }

    private void uploadMenu(String menuName, int price, int stock, String category, String description, Uri imageUri) {
        try {
            // Dapatkan ekstensi file dari URI
            String fileExtension = getFileExtensionFromUri(imageUri);

            if (fileExtension == null) {
                Toast.makeText(getContext(), "Error: Cannot determine file extension.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Hasilkan nama file unik seperti di PHP
            String uniqueFileName = generateUniqueFileName(fileExtension);

            // Buat path relatif
            String relativePath = "upload/menu/" + uniqueFileName;

            // Buat JSON data
            JsonObject menuData = new JsonObject();
            menuData.addProperty("MenuName", menuName);
            menuData.addProperty("Price", price);
            menuData.addProperty("Stock", stock);
            menuData.addProperty("Category", category);
            menuData.addProperty("Description", description);
            menuData.addProperty("ImageUrl", relativePath); // Gunakan path relatif

            // Kirim data menggunakan Retrofit
            ApiService apiService = ApiClient.getApiService();
            Call<ResponseBody> call = apiService.addMenu(menuData);

            call.enqueue(new Callback<ResponseBody>() {
                @Override
                public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                    if (response.isSuccessful()) {
                        try {
                            String responseBody = response.body().string();
                            Log.d("AddMenu", "Response: " + responseBody);
                            Toast.makeText(getContext(), "Menu added successfully!", Toast.LENGTH_SHORT).show();

                            // Reset form after successful upload
                            resetForm();

                        } catch (IOException e) {
                            Log.e("AddMenu", "Error reading response: " + e.getMessage());
                        }
                    } else {
                        Log.e("AddMenu", "Response failed. Code: " + response.code());
                        Toast.makeText(getContext(), "Failed to add menu. Code: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<ResponseBody> call, Throwable t) {
                    Log.e("AddMenu", "Error: " + t.getMessage());
                    Toast.makeText(getContext(), "Error adding menu: " + t.getMessage(), Toast.LENGTH_SHORT).show();

                    // Reset form after successful upload
                    resetForm();
                }
            });

        } catch (Exception e) {
            Log.e("AddMenu", "Error: " + e.getMessage());
            Toast.makeText(getContext(), "An error occurred.", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkPermissions() {
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PICK_IMAGE_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PICK_IMAGE_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(getContext(), "Permission granted!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(getContext(), "Permission denied. App cannot access storage.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
