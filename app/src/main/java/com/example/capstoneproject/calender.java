package com.example.capstoneproject;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.capstoneproject.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import androidx.cardview.widget.CardView;

public class calender extends AppCompatActivity {

    private static final int PICK_FILE_REQUEST = 1;
    private Uri fileUri;
    private String fileName = null;

    private FloatingActionButton uploadCalendarFab;
    private Button viewCalendarButton, deleteCalendarButton;
    private TextView calendarFileName;
    private CardView calendarCard;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calender);

        uploadCalendarFab = findViewById(R.id.uploadCalendarFab);
        viewCalendarButton = findViewById(R.id.viewCalendarButton);
        deleteCalendarButton = findViewById(R.id.deleteCalendarButton);
        calendarFileName = findViewById(R.id.calendarFileName);
        calendarCard = findViewById(R.id.calendarCard);

        // Upload Floating Button Click
        uploadCalendarFab.setOnClickListener(v -> openFileChooser());

        // View Button Click
        viewCalendarButton.setOnClickListener(v -> viewUploadedFile());

        // Delete Button Click
        deleteCalendarButton.setOnClickListener(v -> deleteUploadedFile());
    }

    // Open File Picker
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("application/pdf");
        startActivityForResult(intent, PICK_FILE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_FILE_REQUEST && resultCode == Activity.RESULT_OK && data != null) {
            fileUri = data.getData();
            fileName = "Calendar.pdf";
            calendarFileName.setText(fileName);

            // Show the card and buttons
            calendarCard.setVisibility(View.VISIBLE);
            viewCalendarButton.setVisibility(View.VISIBLE);
            deleteCalendarButton.setVisibility(View.VISIBLE);

            Toast.makeText(this, "File selected: " + fileName, Toast.LENGTH_SHORT).show();
        }
    }

    // View the uploaded file
    private void viewUploadedFile() {
        if (fileUri != null) {
            Intent intent = new Intent(Intent.ACTION_VIEW);
            intent.setDataAndType(fileUri, "application/pdf");
            intent.setFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
            startActivity(intent);
        } else {
            Toast.makeText(this, "No file uploaded", Toast.LENGTH_SHORT).show();
        }
    }

    // Delete uploaded file
    private void deleteUploadedFile() {
        if (fileUri != null) {
            fileUri = null;
            fileName = null;
            calendarFileName.setText("No file uploaded");

            // Hide card and buttons
            calendarCard.setVisibility(View.GONE);
            viewCalendarButton.setVisibility(View.GONE);
            deleteCalendarButton.setVisibility(View.GONE);

            Toast.makeText(this, "File deleted", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "No file to delete", Toast.LENGTH_SHORT).show();
        }
    }
}
