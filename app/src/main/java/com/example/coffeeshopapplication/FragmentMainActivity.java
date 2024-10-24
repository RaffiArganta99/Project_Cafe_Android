package com.example.coffeeshopapplication;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import com.example.coffeeshopapplication.databinding.ActivityFragmentMainBinding;

public class FragmentMainActivity extends AppCompatActivity {

    // Menggunakan binding yang sesuai dengan nama class dari ViewBinding
    private ActivityFragmentMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate layout menggunakan ViewBinding
        binding = ActivityFragmentMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set listener untuk BottomNavigationView agar merespon pilihan item yang di-klik
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            // Cek ID item yang dipilih, lalu ganti fragment yang sesuai
            if (id == R.id.home_nav) {
                replaceFragment(new HomeFragment());
            } else if (id == R.id.product_nav) {
                replaceFragment(new Product_Fragment());
            } else if (id == R.id.add_nav) {
                replaceFragment(new Add_Item_Fragment());
            } else if (id == R.id.list_nav) {
                replaceFragment(new History_Fragment());
            } else if (id == R.id.profile_nav) {
                replaceFragment(new Profile_Fragment());
            }
            return true;
        });

        // Memuat fragment default ketika Activity pertama kali diluncurkan
        if (savedInstanceState == null) {
            replaceFragment(new HomeFragment()); // Fragment default adalah HomeFragment
        }
    }

    // Metode untuk mengganti fragment yang ditampilkan di dalam frame layout
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }
}
