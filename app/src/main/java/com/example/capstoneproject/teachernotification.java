package com.example.capstoneproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class teachernotification extends AppCompatActivity {

    private EditText notificationMessage;
    private Button btnSend, btnDelete;
    private ListView listViewNotifications;
    private ArrayAdapter<String> adapter;
    private ArrayList<String> notifications;
    private int selectedNotificationIndex = -1; // To track the selected notification for deletion

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teachernotification); // Make sure this matches your layout file name

        notificationMessage = findViewById(R.id.notificationMessage);
        btnSend = findViewById(R.id.btnSend);
        btnDelete = findViewById(R.id.btnDelete);
        listViewNotifications = findViewById(R.id.listViewNotifications);

        notifications = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_single_choice, notifications);
        listViewNotifications.setAdapter(adapter);
        listViewNotifications.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

        // Send button click listener
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String message = notificationMessage.getText().toString().trim();
                if (!message.isEmpty()) {
                    // Get the current date and time
                    String dateTime = getCurrentDateTime();

                    // Create the notification message with date and time
                    String notification = message + " (" + dateTime + ")";

                    // Send notification to student portal (implement your logic here)
                    sendNotificationToStudentPortal(message);

                    // Add notification to the list
                    notifications.add(notification);
                    adapter.notifyDataSetChanged();
                    notificationMessage.setText(""); // Clear the input field
                } else {
                    Toast.makeText(teachernotification.this, "Please enter a notification message", Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Long press listener for selecting notifications
        listViewNotifications.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                // Highlight the selected item
                selectedNotificationIndex = position;
                Toast.makeText(teachernotification.this, "Notification selected for deletion", Toast.LENGTH_SHORT).show();
                return true; // Indicate that the long press was handled
            }
        });

        // Delete button click listener
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (selectedNotificationIndex != -1) {
                    // Remove the selected notification
                    notifications.remove(selectedNotificationIndex);
                    adapter.notifyDataSetChanged();
                    selectedNotificationIndex = -1; // Reset the selected index
                    Toast.makeText(teachernotification.this, "Notification deleted", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(teachernotification.this, "Please select a notification to delete", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendNotificationToStudentPortal(String message) {
        // Implement your logic to send the notification to the student portal
        // This could involve making a network request to your backend server
        Toast.makeText(this, "Notification sent to student portal: " + message, Toast.LENGTH_SHORT).show();
    }

    private String getCurrentDateTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        return sdf.format(new Date());
    }
}
