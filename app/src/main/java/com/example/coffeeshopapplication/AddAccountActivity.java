package com.example.coffeeshopapplication;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class AddAccountActivity extends AppCompatActivity {

    private EditText usernameField, emailField, phoneField, genderField, dobField, addressField, roleField;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_account);

        // Bind Views
        usernameField = findViewById(R.id.usernameField);
        emailField = findViewById(R.id.emailField);
        phoneField = findViewById(R.id.phoneField);
        genderField = findViewById(R.id.genderField);
        dobField = findViewById(R.id.dobField);
        addressField = findViewById(R.id.addressField);
        roleField = findViewById(R.id.roleField);
        ImageView backButton = findViewById(R.id.backButton);

        // Back Button functionality
        backButton.setOnClickListener(v -> finish());


        // Load data from SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserProfile", MODE_PRIVATE);

        String username = sharedPreferences.getString("Username", "N/A");
        String email = sharedPreferences.getString("Email", "N/A");
        String phone = sharedPreferences.getString("Phone", "N/A");
        String gender = sharedPreferences.getString("Gender", "N/A");
        String dob = sharedPreferences.getString("DateOfBirth", "N/A");
        String address = sharedPreferences.getString("Address", "N/A");
        String role = sharedPreferences.getString("Role", "N/A");

        // Display data in EditText fields
        usernameField.setText(username);
        emailField.setText(email);
        phoneField.setText(phone);
        genderField.setText(gender);
        dobField.setText(dob);
        addressField.setText(address);
        roleField.setText(role);

        // Set EditText to be non-editable (read-only)
        makeFieldsReadOnly();
    }

    private void makeFieldsReadOnly() {
        usernameField.setFocusable(false);
        usernameField.setClickable(false);

        emailField.setFocusable(false);
        emailField.setClickable(false);

        phoneField.setFocusable(false);
        phoneField.setClickable(false);

        genderField.setFocusable(false);
        genderField.setClickable(false);

        dobField.setFocusable(false);
        dobField.setClickable(false);

        addressField.setFocusable(false);
        addressField.setClickable(false);

        roleField.setFocusable(false);
        roleField.setClickable(false);
    }
}
