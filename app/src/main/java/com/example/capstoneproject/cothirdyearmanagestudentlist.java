package com.example.capstoneproject;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class cothirdyearmanagestudentlist extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cothirdyearstudlist); // Assuming your XML is named activity_student_details.xml

        // You can optionally set a title for the activity
        setTitle("Student Details");

        // If you need to manipulate any views programmatically, you can do so here
        // For example:
        // TextView title = findViewById(R.id.title_text_view); // If you had given it an ID
        // title.setText("Updated Title");
    }
}