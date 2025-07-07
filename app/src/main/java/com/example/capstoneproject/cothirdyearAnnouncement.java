package com.example.capstoneproject;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class cothirdyearAnnouncement extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cothirdyear_announcement); // Replace with your XML layout name

        // Initialize ListView
        ListView listViewNotifications = findViewById(R.id.listViewNotifications);

        // Create sample notification data
        ArrayList<String> notifications = new ArrayList<>();
        notifications.add("• Class test postponed to Friday");
        notifications.add("• Project submission deadline extended");
        notifications.add("• Important: Lab schedule changed");
        notifications.add("• Guest lecture on AI tomorrow at 11 AM");
        notifications.add("• Library will remain closed on Saturday");
        notifications.add("• Last date for fee payment: 25th March");
        notifications.add("• Workshop on Android Development next week");
        notifications.add("• Submit your project proposals by Friday");

        // Create custom adapter
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                R.layout.item_notification, // Custom item layout
                R.id.notificationText,     // TextView ID in item layout
                notifications);

        // Set adapter to ListView
        listViewNotifications.setAdapter(adapter);

        // Optional: Add click listener for notifications
        listViewNotifications.setOnItemClickListener((parent, view, position, id) -> {
            // Handle notification click
            String selectedNotification = notifications.get(position);
            // You can show details in a dialog or start a new activity
        });
    }
}