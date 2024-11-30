package com.example.coffeeshopapplication;

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

public class Profile_Fragment extends Fragment {

    // Deklarasi elemen UI
    private TextView usernameTextView, emailTextView;
    private ImageView profileImageView;

    public Profile_Fragment() {
        // Required empty public constructor
    }

    public static Profile_Fragment newInstance(String param1, String param2) {
        Profile_Fragment fragment = new Profile_Fragment();
        Bundle args = new Bundle();
        args.putString("param1", param1);
        args.putString("param2", param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

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
    }
}
