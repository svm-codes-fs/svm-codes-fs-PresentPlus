package com.example.capstoneproject;

public class NotificationItem {
    private String message;
    private String timestamp;

    public NotificationItem(String message, String timestamp) {
        this.message = message;
        this.timestamp = timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getTimestamp() {
        return timestamp;
    }
}
