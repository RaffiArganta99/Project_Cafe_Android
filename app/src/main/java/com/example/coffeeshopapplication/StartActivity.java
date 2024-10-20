package com.example.coffeeshopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class StartActivity extends AppCompatActivity {

    private static final int SPLASH_DISPLAY_LENGTH = 3000; // 3 seconds delay

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start); // Ensure this layout exists

        // Delay for the splash screen before starting LoginActivity
        new Handler().postDelayed(() -> {
            // Start LoginActivity after the splash screen
            Intent mainIntent = new Intent(StartActivity.this, LoginActivity.class);
            startActivity(mainIntent);
            finish(); // Close StartActivity so user can't go back to it
        }, SPLASH_DISPLAY_LENGTH);
    }
}
