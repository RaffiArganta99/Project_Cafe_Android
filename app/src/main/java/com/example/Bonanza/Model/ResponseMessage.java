package com.example.Bonanza.Model;

public class ResponseMessage {
    private boolean success;
    private String message;

    // Getter dan Setter
    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
