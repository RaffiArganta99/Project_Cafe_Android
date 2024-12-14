package com.example.Bonanza;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.Bonanza.Model.LoginResponse;
import com.example.Bonanza.Retrofit.ApiClient;
import com.example.Bonanza.Interface_API.ApiService;
import com.example.Bonanza.databinding.ActivityLogin2Binding;
import android.text.InputType;
import android.view.MotionEvent;
import android.widget.EditText;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class LoginActivity extends AppCompatActivity {
    private ActivityLogin2Binding binding;
    private ApiService apiService;
    private boolean isPasswordVisible = false; // Variabel untuk melacak status visibilitas

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityLogin2Binding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Inisialisasi ApiService
        apiService = ApiClient.getApiService();

        // Tambahkan logika visibilitas kata sandi
        setupPasswordVisibilityToggle(binding.editTextTextPassword);

        binding.loginbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = binding.editTextTextEmailAddress.getText().toString().trim();
                String password = binding.editTextTextPassword.getText().toString().trim();

                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Email atau password tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                } else {
                    loginUser(email, password);
                }
            }
        });

//        binding.donthavebutton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(LoginActivity.this, SignActivity.class);
//                startActivity(intent);
//            }
//        });
    }

    @SuppressLint("ClickableViewAccessibility")
    private void setupPasswordVisibilityToggle(EditText passwordEditText) {
        passwordEditText.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                // Periksa apakah sentuhan terjadi pada drawableEnd (kanan)
                Drawable[] drawables = passwordEditText.getCompoundDrawables();
                Drawable rightDrawable = drawables[2]; // Drawable kanan (visibility toggle)

                if (rightDrawable != null) { // Pastikan drawable kanan tidak null
                    if (event.getX() >= (passwordEditText.getRight() - passwordEditText.getPaddingRight() - rightDrawable.getIntrinsicWidth())) {
                        Drawable leftDrawable = drawables[0]; // Drawable kiri (ic_lock)
                        Drawable topDrawable = drawables[1]; // Drawable atas (jika ada)
                        Drawable bottomDrawable = drawables[3]; // Drawable bawah (jika ada)

                        // Ubah visibilitas kata sandi
                        if (isPasswordVisible) {
                            // Mode password tersembunyi
                            passwordEditText.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                            rightDrawable = getResources().getDrawable(R.drawable.ic_visibility_off, null);
                            isPasswordVisible = false;
                        } else {
                            // Mode password terlihat
                            passwordEditText.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                            rightDrawable = getResources().getDrawable(R.drawable.ic_visibility_on, null);
                            isPasswordVisible = true;
                        }

                        // Pasang kembali semua drawable
                        passwordEditText.setCompoundDrawablesWithIntrinsicBounds(leftDrawable, topDrawable, rightDrawable, bottomDrawable);

                        // Pindahkan kursor ke akhir teks
                        passwordEditText.setSelection(passwordEditText.getText().length());

                        return true; // Mencegah event diteruskan lebih lanjut
                    }
                }
            }
            return false;
        });
    }


    private void loginUser(String Email, String Password) {
        Call<LoginResponse> call = apiService.login("login", Email, Password);

        call.enqueue(new Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, Response<LoginResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LoginResponse loginResponse = response.body();
                    if ("success".equals(loginResponse.getStatus())) {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();

                        // Simpan data profil ke SharedPreferences
                        getSharedPreferences("UserProfile", MODE_PRIVATE)
                                .edit()
                                .putInt("EmployeeId", loginResponse.getUser().getEmployeeId())
                                .putString("Username", loginResponse.getUser().getUsername())
                                .putString("Email", loginResponse.getUser().getEmail())
                                .putString("Role", loginResponse.getUser().getRole())
                                .putString("Phone", loginResponse.getUser().getPhone())
                                .putString("Gender", loginResponse.getUser().getGender())
                                .putString("DateOfBirth", loginResponse.getUser().getDateOfBirth())
                                .putString("Address", loginResponse.getUser().getAddress())
                                .putString("ImageUrl", loginResponse.getUser().getImageUrl())
                                .apply();

                        // Pindah ke FragmentMainActivity
                        Intent intent = new Intent(LoginActivity.this, FragmentMainActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(LoginActivity.this, loginResponse.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(LoginActivity.this, "Login gagal: Respons tidak valid", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.e("LoginActivity", "Error: " + t.getMessage());
                Toast.makeText(LoginActivity.this, "Terjadi kesalahan: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}

