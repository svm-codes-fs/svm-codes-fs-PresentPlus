package com.example.capstoneproject;

public class NotificationModel {
    private String message;
    private String timestamp;

    // Constructor
    public NotificationModel(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters
    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
