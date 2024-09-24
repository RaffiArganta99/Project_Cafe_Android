package com.example.coffeeshopapplication;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

public class activity_login extends AppCompatActivity {

    // Deklarasi variabel untuk UI elements
    private EditText emailInput, passwordInput;
    private AppCompatButton btnLogin, btnSignUp;
    private TextView forgotPassword, dontHaveAccount;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);  // Menghubungkan layout XML ke activity

        // Menghubungkan elemen-elemen XML dengan Java menggunakan findViewById
        emailInput = findViewById(R.id.emailInput);
        passwordInput = findViewById(R.id.passwordInput);
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        forgotPassword = findViewById(R.id.forgotPassword);
        dontHaveAccount = findViewById(R.id.dontHaveAccount);

        // Listener untuk tombol Login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailInput.getText().toString();
                String password = passwordInput.getText().toString();

                // Validasi sederhana apakah email dan password sudah diisi
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity_login.this, "Please enter both email and password", Toast.LENGTH_SHORT).show();
                } else {
                    // Lakukan autentikasi atau logika login di sini
                    if (authenticateUser(email, password)) {
                        Toast.makeText(activity_login.this, "Login Successful!", Toast.LENGTH_SHORT).show();
                        // Lanjutkan ke activity lain atau lakukan sesuatu setelah login berhasil
                    } else {
                        Toast.makeText(activity_login.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        // Listener untuk TextView "Forgot Password"
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika reset password
                Toast.makeText(activity_login.this, "Forgot Password clicked", Toast.LENGTH_SHORT).show();
                // Anda bisa mengarahkan ke halaman reset password
            }
        });

        // Listener untuk TextView "Don't have an Account? Sign Up"
        dontHaveAccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika untuk sign up
                Toast.makeText(activity_login.this, "Redirecting to Sign Up Page", Toast.LENGTH_SHORT).show();
                // Anda bisa mengarahkan ke halaman pendaftaran pengguna baru
            }
        });

        // Listener untuk tombol "Sign Up with Google"
        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tambahkan logika untuk login menggunakan Google
                Toast.makeText(activity_login.this, "Google Sign Up clicked", Toast.LENGTH_SHORT).show();
                // Anda bisa menambahkan Google Sign In API di sini
            }
        });
    }

    // Contoh metode autentikasi sederhana (sesuaikan dengan logika backend Anda)
    private boolean authenticateUser(String email, String password) {
        // Di sini Anda bisa mengganti dengan logika autentikasi sebenarnya
        // Misal: cek ke database atau server backend

        // Contoh hardcoded login:
        return email.equals("test@example.com") && password.equals("password123");
    }
}