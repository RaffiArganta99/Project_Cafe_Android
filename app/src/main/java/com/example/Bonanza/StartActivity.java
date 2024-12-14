package com.example.Bonanza;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 200; // 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); // Ensure this layout exists

        // Menyembunyikan status bar (System UI)
        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_FULLSCREEN |
                        View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                        View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        );

        // Delay for the splash screen before starting LoginActivity
        new Handler().postDelayed(() -> {
            // Start LoginActivity after the splash screen
            Intent mainIntent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish(); // Close StartActivity so user can't go back to it
        }, SPLASH_DISPLAY_LENGTH);
    }
}
