package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class ComputerScienceThirdYearDashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studentdashboard);

        // Initialize all buttons and views
        MaterialButton viewTimeTableButton = findViewById(R.id.viewTimeTableButton);
        MaterialButton viewAttendanceButton = findViewById(R.id.view_attendance);
        MaterialButton academicCalendarButton = findViewById(R.id.academicCalendarButton);
        MaterialButton leaveManageSystemButton = findViewById(R.id.leaveManageSystemButton);
        MaterialButton notificationButton = findViewById(R.id.notification_button);
        ImageView profileImageView = findViewById(R.id.profileImageView);
        ImageView classroomButton = findViewById(R.id.classroomButton);

        // Time Table Button
        viewTimeTableButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyearstudtimetable.class);
                startActivity(intent);
            }
        });

        // Attendance Button
        viewAttendanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this,cothirdyearstudviewattendance.class);
                startActivity(intent);
            }
        });

        // Academic Calendar Button
        academicCalendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyearAcademicCalendar.class);
                startActivity(intent);
            }
        });

        // Leave Management Button
        leaveManageSystemButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyaerstudleavemanagementsystem.class);
                startActivity(intent);
            }
        });

        // Announcements Button
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyearAnnouncement.class);
                startActivity(intent);
            }
        });

        // Profile Image
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyearstudprofile.class);
                startActivity(intent);
            }
        });

        // Classroom Image
        classroomButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ComputerScienceThirdYearDashboard.this, cothirdyearstudclassroom.class);
                startActivity(intent);
            }
        });
    }
}