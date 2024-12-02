package com.example.coffeeshopapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
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
    private int productId; // ID produk untuk mengupdate API
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

        // Tampilkan gambar saat ini
        menuImageView.setImageURI(imageUri);

        // Inisialisasi spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                requireContext(),
                R.array.category_array, // Array kategori didefinisikan di strings.xml
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
                selectedCategory = null; // Atur kategori ke null jika tidak ada yang dipilih
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

            // Jika gambar diubah, upload gambar terlebih dahulu
            if (imageUri != null) {
                uploadImage(imageUri, name, price, selectedCategory); // Kirim data nama, harga, kategori bersama gambar
            } else {
                updateProductToApi(productId, name, price, selectedCategory, null); // Jika tidak ada gambar, lanjutkan update produk
            }
        });


        cancelButton.setOnClickListener(v -> dismiss());

        changeImageButton.setOnClickListener(v -> openImagePicker());

        return view;
    }

    private void uploadImage(Uri imageUri, String name, String price, String category) {
        String imagePath = getPathFromUri(imageUri); // Mendapatkan path dari Uri
        File imageFile = new File(imagePath);  // imagePath adalah path gambar
        RequestBody requestBody = RequestBody.create(MultipartBody.FORM, imageFile);
        MultipartBody.Part imagePart = MultipartBody.Part.createFormData("imageUrl", imageFile.getName(), requestBody);

        // Mengirim request API untuk upload gambar
        apiService.updateMenuImage(productId, "updateImage", imagePart).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    try {
                        // Parsing JSON response
                        String responseString = response.body().string();
                        JSONObject jsonResponse = new JSONObject(responseString);

                        // Periksa jika status sukses dan ambil updated_id
                        if ("success".equals(jsonResponse.getString("status"))) {
                            String updatedId = jsonResponse.getString("updated_id");

                            // Cek apakah imageUrl ada dalam respons
                            String imageUrl = null;
                            if (jsonResponse.has("imageUrl")) {
                                imageUrl = jsonResponse.getString("imageUrl"); // Ambil imageUrl jika ada
                            }

                            // Lanjutkan untuk update produk menggunakan imageUrl (jika ada)
                            updateProductToApi(productId, name, price, selectedCategory, imageUrl);
                        } else {
                            Log.e("API", "Failed to upload image: " + jsonResponse.getString("message"));
                        }
                    } catch (IOException | JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    // Handle failure
                    Log.e("API", "Failed to upload image: " + response.message());
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                // Handle error
                Log.e("API", "Error: " + t.getMessage());
            }
        });
    }

    // Fungsi untuk mendapatkan path dari URI
    private String getPathFromUri(Uri uri) {
        String path = null;
        String[] projection = { MediaStore.Images.Media.DATA };
        try (Cursor cursor = requireContext().getContentResolver().query(uri, projection, null, null, null)) {
            if (cursor != null && cursor.moveToFirst()) {
                int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                path = cursor.getString(columnIndex);
            }
        }
        return path;
    }

    private void updateProductToApi(int productId, String name, String price, String category, String imageUrl) {
        // Persiapkan data untuk dikirim
        Map<String, Object> productUpdate = new HashMap<>();
        productUpdate.put("MenuName", name);
        productUpdate.put("Price", Double.parseDouble(price)); // pastikan harga valid
        productUpdate.put("Category", category);
        productUpdate.put("ImageUrl", imageUrl != null ? imageUrl : ""); // Gambar URL jika ada

        // Panggil API
        apiService.updateMenu(productId, productUpdate).enqueue(new Callback<ResponseUpdate>() {
            @Override
            public void onResponse(Call<ResponseUpdate> call, Response<ResponseUpdate> response) {
                if (!isAdded()) { // Pastikan fragment masih terhubung
                    Log.w("EditMenuDialogFragment", "Fragment not attached to context. Skipping response handling.");
                    return;
                }

                if (response.isSuccessful()) {
                    Context context = getContext(); // Gunakan context yang aman
                    if (context != null) {
                        Toast.makeText(context, "Product updated successfully", Toast.LENGTH_SHORT).show();
                    }
                    dismiss(); // Tutup dialog jika fragment masih terhubung
                } else {
                    Log.e("EditMenuDialogFragment", "Failed to update product: " + response.message());
                    Context context = getContext();
                    if (context != null) {
                        Toast.makeText(context, "Failed to update product", Toast.LENGTH_SHORT).show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseUpdate> call, Throwable t) {
                Log.e("EditMenuDialog", "Error updating product: " + t.getMessage());
                Toast.makeText(requireContext(), "Error updating product", Toast.LENGTH_SHORT).show();
            }
        });
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





