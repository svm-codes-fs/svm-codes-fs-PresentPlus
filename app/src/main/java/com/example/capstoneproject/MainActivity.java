package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Button btnAdmin, btnTeacher, btnStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (SharedPrefsManager.isLoggedIn(this)) {
            String role = SharedPrefsManager.getUserRole(this);

            switch (role) {
                case "admin":
                    startActivity(new Intent(this, admindashboard.class));
                    finish();
                    return;

                case "student":
                    StudentDashboardRouter.redirectToDashboard(
                            this,
                            SharedPrefsManager.getDepartment(this),
                            SharedPrefsManager.getYear(this)
                    );
                    finish();
                    return;

                case "teacher":
                    TeacherDashboardRouter.redirectToDashboard(
                            this,
                            SharedPrefsManager.getDepartment(this)
                    );
                    finish();
                    return;

                case "hod":
                    HODDashboardRouter.redirectToDashboard(
                            this,
                            SharedPrefsManager.getDepartment(this)
                    );
                    finish();
                    return;
            }
        }

        // Not logged in - show role selection screen
        setContentView(R.layout.activity_main);

        btnAdmin = findViewById(R.id.btnAdminLogin);
        btnTeacher = findViewById(R.id.btnTeacherLogin);
        btnStudent = findViewById(R.id.btnStudentLogin);

        btnAdmin.setOnClickListener(v -> startActivity(new Intent(this, loginadmin.class)));
        btnTeacher.setOnClickListener(v -> startActivity(new Intent(this, loginteacher.class)));  // Teacher/HOD selection
        btnStudent.setOnClickListener(v -> startActivity(new Intent(this, loginstudent.class)));
    }
}
