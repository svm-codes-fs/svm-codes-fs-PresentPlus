package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.textfield.TextInputEditText;

public class cothirdyearstudclassroom extends AppCompatActivity {
    AutoCompleteTextView departmentSpinner, yearSpinner, testTypeSpinner;
    TextInputEditText rollNumberInput;
    Button submitButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studclassroom);

        // Initialize UI components
        departmentSpinner = findViewById(R.id.departmentSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        testTypeSpinner = findViewById(R.id.testTypeSpinner);
        rollNumberInput = findViewById(R.id.rollNumberInput);
        submitButton = findViewById(R.id.submitButton);

        // Populate Dropdown Data
        setDropdownOptions(departmentSpinner, new String[]{"Computer Science", "IT", "Electronics", "Civil"});
        setDropdownOptions(yearSpinner, new String[]{"1st Year", "2nd Year", "3rd Year"});
        setDropdownOptions(testTypeSpinner, new String[]{"Unit Test", "Mid-Term", "Final Exam"});

        // Button click event
        submitButton.setOnClickListener(v -> {
            Intent intent = new Intent(cothirdyearstudclassroom.this, ResultActivity.class);
            startActivity(intent);
        });
    }

    // Function to set dropdown options
    private void setDropdownOptions(AutoCompleteTextView spinner, String[] items) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_dropdown_item_1line, items);
        spinner.setAdapter(adapter);
        spinner.setKeyListener(null);  // Prevents keyboard from opening
    }
}
