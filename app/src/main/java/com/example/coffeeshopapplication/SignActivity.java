package com.example.coffeeshopapplication;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import androidx.appcompat.app.AppCompatActivity;
import com.example.coffeeshopapplication.databinding.ActivitySignBinding;
import com.example.coffeeshopapplication.databinding.ActivitySignBinding;

public class SignActivity extends AppCompatActivity {

    private ActivitySignBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Inflate the layout using ActivitySignBinding
        binding = ActivitySignBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Set onClickListener for alreadyhavebutton
        binding.alreadyhavebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start LoginActivity
                Intent intent = new Intent(SignActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
}
