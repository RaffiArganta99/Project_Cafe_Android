package com.example.coffeeshopapplication;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

public class EditMenuDialogFragment extends DialogFragment {

    public interface OnEditCompleteListener {
        void onEditComplete(String name, String price, Uri newImageUri);
    }

    private static final int REQUEST_IMAGE_PICK = 1001;
    private final OnEditCompleteListener listener;
    private Uri imageUri;
    private ImageView menuImageView;

    public EditMenuDialogFragment(OnEditCompleteListener listener, Uri initialImageUri) {
        this.listener = listener;
        this.imageUri = initialImageUri;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_edit_menu, container, false);

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
            if (listener != null) {
                listener.onEditComplete(name, price, imageUri);
            }
            dismiss();
        });

        cancelButton.setOnClickListener(v -> dismiss());

        // Buka galeri ketika changeImageButton diklik
        changeImageButton.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(intent, REQUEST_IMAGE_PICK);
        });

        return view;
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
