package com.example.coffeeshopapplication.Model;

import com.google.gson.annotations.SerializedName;

public class User {
    @SerializedName("EmployeeId")
    private int employeeId;

    @SerializedName("Username")
    private String username;

    @SerializedName("Email")
    private String email;

    @SerializedName("Role")
    private String role;

    @SerializedName("Phone")
    private String phone;

    @SerializedName("Gender")
    private String gender;

    @SerializedName("DateOfBirth")
    private String dateOfBirth;

    @SerializedName("Address")
    private String address;

    @SerializedName("ImageUrl")
    private String imageUrl;

    // Getter dan Setter
    public int getEmployeeId() {
        return employeeId;
    }

    public void setEmployeeId(int employeeId) {
        this.employeeId = employeeId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(String dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
