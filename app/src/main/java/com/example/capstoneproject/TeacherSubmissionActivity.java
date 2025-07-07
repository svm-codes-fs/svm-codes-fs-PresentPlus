package com.example.capstoneproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class TeacherSubmissionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacher_submission);

        TextView presentCountTextView = findViewById(R.id.presentCountTextView);
        TextView absentCountTextView = findViewById(R.id.absentCountTextView);
        TextView totalCountTextView = findViewById(R.id.totalCountTextView);
        Button backButton = findViewById(R.id.backButton);
        Button okButton = findViewById(R.id.okButton);

        int presentCount = getIntent().getIntExtra("presentCount", 0);
        int absentCount = getIntent().getIntExtra("absentCount", 0);
        int total = presentCount + absentCount;

        presentCountTextView.setText(String.valueOf(presentCount));
        absentCountTextView.setText(String.valueOf(absentCount));
        totalCountTextView.setText("Total: " + total);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close this activity and return to MainActivity
            }
        });

        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Save the attendance data (you can implement your saving logic here)
                saveAttendanceData(presentCount, absentCount);

                // Show success message
                Toast.makeText(TeacherSubmissionActivity.this,
                        "Data submitted successfully!",
                        Toast.LENGTH_SHORT).show();

                // Close the activity after a short delay
                new android.os.Handler().postDelayed(
                        new Runnable() {
                            public void run() {
                                finish();
                            }
                        },
                        1500); // 1.5 second delay
            }
        });
    }

    private void saveAttendanceData(int presentCount, int absentCount) {
        // Implement your data saving logic here
        // This could be:
        // 1. Saving to SharedPreferences
        // 2. Saving to a local database (Room, SQLite)
        // 3. Sending to a web server

        // Example using SharedPreferences:
        /*
        SharedPreferences sharedPref = getSharedPreferences("AttendanceData", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putInt("presentCount", presentCount);
        editor.putInt("absentCount", absentCount);
        editor.putLong("timestamp", System.currentTimeMillis());
        editor.apply();
        */

        // For now, we'll just simulate saving by printing to log
        System.out.println("Attendance data saved - Present: " + presentCount +
                ", Absent: " + absentCount);
    }
}
