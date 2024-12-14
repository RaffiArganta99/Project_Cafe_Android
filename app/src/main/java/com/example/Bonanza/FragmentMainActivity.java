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

        // Nonaktifkan pergeseran layout saat keyboard muncul
        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_NOTHING);

        bottomNavigation = findViewById(R.id.bottomNavigationView);

        // Nonaktifkan fokus pada bottomNavigation
        bottomNavigation.setFocusable(false);
        bottomNavigation.setFocusableInTouchMode(false);

        // Tambahkan item navigasi
        bottomNavigation.add(new CurvedBottomNavigation.Model(1, "Product", R.drawable.ic_home));
        bottomNavigation.add(new CurvedBottomNavigation.Model(2, "Add", R.drawable.ic_add));
        bottomNavigation.add(new CurvedBottomNavigation.Model(3, "Cart", R.drawable.ic_cart));
        bottomNavigation.add(new CurvedBottomNavigation.Model(4, "History", R.drawable.ic_history));
        bottomNavigation.add(new CurvedBottomNavigation.Model(5, "Profile", R.drawable.ic_profile));

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
