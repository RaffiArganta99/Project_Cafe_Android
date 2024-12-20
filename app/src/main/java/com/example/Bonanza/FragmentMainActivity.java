package com.example.Bonanza;

import android.os.Bundle;
import android.view.WindowManager;
import android.widget.FrameLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.qamar.curvedbottomnaviagtion.CurvedBottomNavigation;

public class FragmentMainActivity extends AppCompatActivity {

    private CurvedBottomNavigation bottomNavigation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment_main);

        // Ganti dengan mode yang memungkinkan penyesuaian layout
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);


        bottomNavigation = findViewById(R.id.bottomNavigationView);

        // Nonaktifkan fokus pada bottomNavigation
        bottomNavigation.setFocusable(false);
        bottomNavigation.setFocusableInTouchMode(false);

        // Tambahkan item navigasi
        bottomNavigation.add(new CurvedBottomNavigation.Model(1, "Produk", R.drawable.ic_home));
        bottomNavigation.add(new CurvedBottomNavigation.Model(2, "Tambah", R.drawable.ic_add));
        bottomNavigation.add(new CurvedBottomNavigation.Model(3, "Transaksi", R.drawable.ic_cart));
        bottomNavigation.add(new CurvedBottomNavigation.Model(4, "Riwayat", R.drawable.ic_history));
        bottomNavigation.add(new CurvedBottomNavigation.Model(5, "Profil", R.drawable.ic_profile));

        // Set listener untuk navigasi
        bottomNavigation.setOnClickMenuListener(model -> {
            Fragment selectedFragment = null;
            switch (model.getId()) {
                case 1:
                    selectedFragment = new ProductFragment();
                    break;
                case 2:
                    selectedFragment = new Add_Item_Fragment();
                    break;
                case 3:
                    selectedFragment = new TransactionFragment();
                    break;
                case 4:
                    selectedFragment = new History_Fragment();
                    break;
                case 5:
                    selectedFragment = new Profile_Fragment();
                    break;
            }

            if (selectedFragment != null) {
                replaceFragment(selectedFragment);
            }
            return null;
        });

        // Atur fragment default
        replaceFragment(new ProductFragment());
        bottomNavigation.show(1, true);
    }

    @Override
    public void onResume() {
        super.onResume();
        FrameLayout frameLayout = findViewById(R.id.frame_layout);
        frameLayout.requestFocus();
    }

    private void replaceFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }
}
