package com.example.capstoneproject;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;


public class cothirdyearstudviewattendance extends AppCompatActivity {

    private EditText nameEditText, rollNoEditText;
    private RadioGroup branchRadioGroup;
    private Button findButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studviewattendance); // XML Layout

        // Initialize UI components
        nameEditText = findViewById(R.id.nameEditText);
        rollNoEditText = findViewById(R.id.rollNoEditText);
        findButton = findViewById(R.id.findButton);

        // Handle find button click
        findButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Get the input values
                String name = nameEditText.getText().toString().trim();
                String rollNo = rollNoEditText.getText().toString().trim();
                int selectedBranchId = branchRadioGroup.getCheckedRadioButtonId();

                if (name.isEmpty() || rollNo.isEmpty() || selectedBranchId == -1) {
                    Toast.makeText(cothirdyearstudviewattendance.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });

    }
}
