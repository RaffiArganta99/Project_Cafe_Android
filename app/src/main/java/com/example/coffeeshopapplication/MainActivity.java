package com.example.coffeeshopapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class MainActivity extends AppCompatActivity {

    // Mendeklarasikan variabel untuk View
    private AppCompatButton btnSignUp, btnLogin;
    private TextView orContinueWith, welcomeText, descriptionText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Menghubungkan ke layout XML

        // Menghubungkan elemen XML dengan variabel di Java
        btnSignUp = findViewById(R.id.btnSignUp);
        btnLogin = findViewById(R.id.btnLogin);
        orContinueWith = findViewById(R.id.orContinueWith);
        welcomeText = findViewById(R.id.welcomeText);
        descriptionText = findViewById(R.id.textView);

        // Menambahkan listener untuk tombol "Sign Up"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Sign Up clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Menambahkan listener untuk tombol "Login"
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Login clicked", Toast.LENGTH_SHORT).show();
            }
        });

        // Jika ingin menambahkan aksi lain untuk TextView, Anda juga bisa menambahkannya
        // Contoh untuk TextView "Or Continue With"
        orContinueWith.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(MainActivity.this, "Or Continue With clicked", Toast.LENGTH_SHORT).show();
            }
        });
    }
}