package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class CEHODDashboard extends AppCompatActivity {

    private TextView departmentText;
    private ImageButton profileButton, timetableButton, attendanceButton, notificationButton;
    private Button manageStudentsButton, attendanceRecordButton;
    private String department = "Civil"; // Change this dynamically based on login

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cehoddashboard);

        // Initialize Views

        // Set Department Name
        departmentText.setText("Department: " + department);

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
