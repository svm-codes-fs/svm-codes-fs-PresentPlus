package com.example.capstoneproject;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class cothirdyearAcademicCalendar extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cothirdyear_academic_calendar); // Assuming your XML is saved as activity_calender.xml

        // Initialize views
        TextView calendarTitle = findViewById(R.id.calendarTitle);
        CardView calendarCard = findViewById(R.id.calendarCard);
        TextView calendarFileName = findViewById(R.id.calendarFileName);

        // You can set click listeners or other functionality here
        // For example:
        calendarCard.setOnClickListener(v -> {
            // Handle card click - maybe open the calendar file
            // You would need to implement this based on your requirements
        });
    }
}