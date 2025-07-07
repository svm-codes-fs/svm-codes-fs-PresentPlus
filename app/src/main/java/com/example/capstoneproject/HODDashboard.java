package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.button.MaterialButton;
import androidx.appcompat.app.AppCompatActivity;

public class HODDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hoddashboard);

        // Toolbar Setup
        MaterialToolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Profile Image Click
        ImageView profileImage = findViewById(R.id.profile_image);
        profileImage.setOnClickListener(v -> {
            // Open Profile Activity
            Intent intent = new Intent(HODDashboard.this, hodprofile.class);
            startActivity(intent);
        });

        // Button Listeners
        MaterialButton manageStudentsButton = findViewById(R.id.button_manage_students);
        MaterialButton attendanceRecordsButton = findViewById(R.id.button_attendance_record);
        MaterialButton leaveManagementButton = findViewById(R.id.button_leave_management);

        manageStudentsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HODDashboard.this, hodstudentmanagement.class);
            startActivity(intent);
        });

        attendanceRecordsButton.setOnClickListener(v -> {
            Intent intent = new Intent(HODDashboard.this, hodattendancerecord.class);
            startActivity(intent);
        });

        leaveManagementButton.setOnClickListener(v -> {
            Intent intent = new Intent(HODDashboard.this, hodstudentmanagement.class);
            startActivity(intent);
        });

        // Notification Card Click
        findViewById(R.id.imageButton_notification).setOnClickListener(v -> {
            Intent intent = new Intent(HODDashboard.this, hodnotification.class);
            startActivity(intent);
        });
    }
}