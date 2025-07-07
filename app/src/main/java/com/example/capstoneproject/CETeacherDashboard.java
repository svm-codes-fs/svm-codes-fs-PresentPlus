package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.R;
import com.example.capstoneproject.teacherprofile;

public class CETeacherDashboard extends AppCompatActivity {

    private TextView departmentText;
    private ImageButton profileButton, timetableButton, attendanceButton, notificationButton;
    private Button manageStudentsButton, attendanceRecordButton;
    private String department = "Civil"; // Change this dynamically based on login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ceteacher_dashboard);

        // Initialize Views
        departmentText = findViewById(R.id.departmentText);
        profileButton = findViewById(R.id.profileButton);
        timetableButton = findViewById(R.id.imageButton_timetable);
        attendanceButton = findViewById(R.id.imageButton_attendance);
        notificationButton = findViewById(R.id.imageButton_notification);
        manageStudentsButton = findViewById(R.id.button_manage_students);
        attendanceRecordButton = findViewById(R.id.button_attendance_record);

        // Set Department Name
        departmentText.setText("Department: " + department);

        // Set Click Listeners
        profileButton.setOnClickListener(v -> openActivity(teacherprofile.class));
        timetableButton.setOnClickListener(v -> openDepartmentActivity("TimeTable"));
        attendanceButton.setOnClickListener(v -> openDepartmentActivity("Attendance"));
        notificationButton.setOnClickListener(v -> openDepartmentActivity("Notification"));
        manageStudentsButton.setOnClickListener(v -> openDepartmentActivity("ManageStudents"));
        attendanceRecordButton.setOnClickListener(v -> openDepartmentActivity("AttendanceRecord"));
    }

    private void openActivity(Class<?> activityClass) {
        Intent intent = new Intent(this, activityClass);
        startActivity(intent);
    }

    private void openDepartmentActivity(String feature) {
        String activityName = "com.example.teacherportal." + department + feature + "Activity";
        try {
            Class<?> activityClass = Class.forName(activityName);
            Intent intent = new Intent(this, activityClass);
            startActivity(intent);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
