package com.example.Bonanza;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import android.content.DialogInterface;
import android.content.Intent;
import android.widget.Button;
import androidx.appcompat.app.AlertDialog;

public class Profile_Fragment extends Fragment {

    // Deklarasi elemen UI
    private TextView usernameTextView, emailTextView;
    private ImageView profileImageView;
    private Button btnSelectImage, logoutButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate layout
        return inflater.inflate(R.layout.fragment_profile_, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Inisialisasi elemen UI
        usernameTextView = view.findViewById(R.id.blackTextView);
        emailTextView = view.findViewById(R.id.grayTextView);
        profileImageView = view.findViewById(R.id.profileImageView);
        btnSelectImage = view.findViewById(R.id.btnSelectImage);
        logoutButton = view.findViewById(R.id.LogoutButton);

        // Ambil data dari SharedPreferences
        SharedPreferences sharedPreferences = requireActivity().getSharedPreferences("UserProfile", requireContext().MODE_PRIVATE);
        String username = sharedPreferences.getString("Username", "N/A");
        String email = sharedPreferences.getString("Email", "N/A");
        String imageUrl = sharedPreferences.getString("ImageUrl", null);

        // Set data ke elemen UI
        usernameTextView.setText(username);
        emailTextView.setText(email);

        // Gunakan Glide untuk memuat gambar
        if (imageUrl != null) {
            Glide.with(requireContext())
                    .load(imageUrl) // URL gambar dari API
                    .placeholder(R.drawable.profil) // Placeholder jika gambar belum dimuat
                    .into(profileImageView);
        } else {
            profileImageView.setImageResource(R.drawable.profil); // Gambar default jika URL kosong
        }

        // Navigasi ke Edit Profil
        btnSelectImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Ganti dengan aktivitas edit profil yang sesuai
                Intent intent = new Intent(getActivity(), AddAccountActivity.class);
                startActivity(intent);
            }
        });

        // Konfirmasi log out
        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(requireContext())
                        .setTitle("Konfirmasi")
                        .setMessage("Apakah Anda yakin ingin keluar?")
                        .setPositiveButton("Ya", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Hapus data dari SharedPreferences dan kembali ke login
                                SharedPreferences.Editor editor = sharedPreferences.edit();
                                editor.clear();
                                editor.apply();

                                // Navigasi ke LoginActivity
                                Intent intent = new Intent(getActivity(), LoginActivity.class);
                                startActivity(intent);
                                getActivity().finish(); // Tutup aktivitas ini
                            }
                        })
                        .setNegativeButton("Tidak", null)
                        .show();
            }
        });
    }
}
