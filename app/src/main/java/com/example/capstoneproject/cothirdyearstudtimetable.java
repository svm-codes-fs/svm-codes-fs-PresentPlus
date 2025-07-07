package com.example.capstoneproject;

import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class cothirdyearstudtimetable extends AppCompatActivity {

    private ImageView timetableImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studtimetable);

        timetableImageView = findViewById(R.id.timetableImageView);
        loadTimetableImage();
    }

    private void loadTimetableImage() {
        String imageUriString = getIntent().getStringExtra("imageUri");
        if (imageUriString != null) {
            Uri imageUri = Uri.parse(imageUriString);
            timetableImageView.setImageURI(imageUri);
        }
        Toast.makeText(this, "Timetable Loaded", Toast.LENGTH_SHORT).show();
    }
}
