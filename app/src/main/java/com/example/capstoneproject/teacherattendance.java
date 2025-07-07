package com.example.capstoneproject;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class teacherattendance extends AppCompatActivity {

    private Spinner yearSpinner;
    private Spinner schemeSpinner;
    private Button proceedButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherattendance);

        // Initialize the Spinners and Button
        yearSpinner = findViewById(R.id.yearSpinner);
        schemeSpinner = findViewById(R.id.schemeSpinner);
        proceedButton = findViewById(R.id.proceedButton);

        // Set up the year Spinner
        List<String> years = new ArrayList<>();
        years.add("Select Year");
        years.add("1st Year");
        years.add("2nd Year");
        years.add("3rd Year");

        ArrayAdapter<String> yearAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, years);
        yearAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        yearSpinner.setAdapter(yearAdapter);

        // Set up the scheme Spinner
        List<String> schemes = new ArrayList<>();
        ArrayAdapter<String> schemeAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, schemes);
        schemeAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        schemeSpinner.setAdapter(schemeAdapter);

        // Handle year Spinner selection
        yearSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedYear = parent.getItemAtPosition(position).toString();
                schemes.clear();

                if (selectedYear.equals("1st Year") || selectedYear.equals("2nd Year")) {
                    schemes.add("Kscheme");
                } else if (selectedYear.equals("3rd Year")) {
                    schemes.add("Ischeme");
                    schemes.add("Kscheme");
                }

                schemeAdapter.notifyDataSetChanged();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // Do nothing
            }
        });

        // Handle proceed Button click
        proceedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedYear = yearSpinner.getSelectedItem().toString();
                String selectedScheme = schemeSpinner.getSelectedItem().toString();

                Intent intent = null;


                if (selectedYear.equals("3rd Year") && selectedScheme.equals("Kscheme")) {
                    intent = new Intent(teacherattendance.this,cothirdyearstudlist.class);
                }

                if (intent != null) {
                    startActivity(intent);
                }

            }
        });
    }
}
