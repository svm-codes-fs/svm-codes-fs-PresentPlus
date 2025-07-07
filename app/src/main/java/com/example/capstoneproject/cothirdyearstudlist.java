package com.example.capstoneproject;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class cothirdyearstudlist extends AppCompatActivity {

    private int presentCount = 0;
    private int absentCount = 0;
    private final List<CheckBox> allCheckBoxes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cothirdyearstudlist);

        initializeAllCheckBoxes();
        updateCounts();

        Button submitButton = findViewById(R.id.submitButton);
        submitButton.setOnClickListener(v -> {
            Intent intent = new Intent(cothirdyearstudlist.this, TeacherSubmissionActivity.class);
            intent.putExtra("presentCount", presentCount);
            intent.putExtra("absentCount", absentCount);
            startActivity(intent);
        });
    }

    private void initializeAllCheckBoxes() {
        int[] checkBoxIds = {
                R.id.raj, R.id.gaurav, R.id.prachi, R.id.sanika, R.id.vennela,
                R.id.pranali, R.id.omkar, R.id.himanshu, R.id.prajwal, R.id.khushi,
                R.id.jyotsana, R.id.priyanka, R.id.yash, R.id.aditya, R.id.chetan,
                R.id.atharav, R.id.preksha, R.id.kunal, R.id.prapti, R.id.premshree,
                R.id.shivkant, R.id.anushka_y, R.id.prabuddha, R.id.hussain
        };

        for (int id : checkBoxIds) {
            addCheckBox(id);
        }
    }

    private void addCheckBox(int checkBoxId) {
        CheckBox checkBox = findViewById(checkBoxId);
        if (checkBox != null) {
            allCheckBoxes.add(checkBox);

            // Set initial color
            updateCheckBoxColor(checkBox, checkBox.isChecked());

            checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
                updateCounts();
                updateCheckBoxColor((CheckBox) buttonView, isChecked);
            });
        }
    }

    private void updateCheckBoxColor(CheckBox checkBox, boolean isChecked) {
        try {
            int colorRes = isChecked ? R.color.green : android.R.color.darker_gray;
            ColorStateList colorStateList = ColorStateList.valueOf(
                    ContextCompat.getColor(this, colorRes));
            checkBox.setButtonTintList(colorStateList);
        } catch (Exception e) {
            // Fallback to default colors if there's any error
            int fallbackColor = isChecked ? 0xFF4CAF50 : 0xFFAAAAAA; // Green or Gray
            checkBox.setButtonTintList(ColorStateList.valueOf(fallbackColor));
        }
    }

    private void updateCounts() {
        presentCount = 0;
        for (CheckBox checkBox : allCheckBoxes) {
            if (checkBox != null && checkBox.isChecked()) {
                presentCount++;
            }
        }
        absentCount = allCheckBoxes.size() - presentCount;
    }
}