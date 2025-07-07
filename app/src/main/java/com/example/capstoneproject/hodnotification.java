package com.example.capstoneproject;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;

public class hodnotification extends AppCompatActivity {

    private EditText notificationMessage;
    private Button btnSend, btnDelete;
    private ListView listViewNotifications;
    private ArrayList<String> notificationsList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodnotification); // Replace with your XML layout name

        // Initialize views
        notificationMessage = findViewById(R.id.notificationMessage);
        btnSend = findViewById(R.id.btnSend);
        btnDelete = findViewById(R.id.btnDelete);
        listViewNotifications = findViewById(R.id.listViewNotifications);

        // Initialize notifications list and adapter
        notificationsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, notificationsList);
        listViewNotifications.setAdapter(adapter);

        // Set click listeners
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendNotification();
            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteNotification();
            }
        });

        // Set long click listener for list items to delete individual notifications
        listViewNotifications.setOnItemLongClickListener((parent, view, position, id) -> {
            deleteSingleNotification(position);
            return true;
        });
    }

    private void sendNotification() {
        String message = notificationMessage.getText().toString().trim();

        if (message.isEmpty()) {
            Toast.makeText(this, "Please enter a notification message", Toast.LENGTH_SHORT).show();
            return;
        }

        // Add notification to list
        notificationsList.add(0, message); // Add at beginning to show newest first
        adapter.notifyDataSetChanged();

        // Clear input field
        notificationMessage.setText("");

        // Show success message
        Toast.makeText(this, "Notification sent", Toast.LENGTH_SHORT).show();

        // Here you would typically also send the notification to a server/database
        // sendToServer(message);
    }

    private void deleteNotification() {
        if (notificationsList.isEmpty()) {
            Toast.makeText(this, "No notifications to delete", Toast.LENGTH_SHORT).show();
            return;
        }

        // Clear all notifications
        notificationsList.clear();
        adapter.notifyDataSetChanged();

        Toast.makeText(this, "All notifications deleted", Toast.LENGTH_SHORT).show();

        // Here you would typically also delete from server/database
        // deleteAllFromServer();
    }

    private void deleteSingleNotification(int position) {
        if (position >= 0 && position < notificationsList.size()) {
            String deletedMessage = notificationsList.remove(position);
            adapter.notifyDataSetChanged();

            Toast.makeText(this, "Deleted: " + deletedMessage, Toast.LENGTH_SHORT).show();

            // Here you would typically also delete from server/database by ID
            // deleteFromServer(deletedMessageId);
        }
    }
}