package com.example.capstoneproject;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class adminNotification extends AppCompatActivity {

    private Spinner departmentSpinner, yearSpinner;
    private EditText messageEditText;
    private MaterialButton btnSend, btnUpload;
    private FloatingActionButton fabAdd;
    private TextView emptyView;
    private RecyclerView recyclerViewNotifications;
    private NotificationAdapter notificationAdapter;
    private List<String> notificationList = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_notification);

        // Initialize UI Components
        departmentSpinner = findViewById(R.id.departmentSpinner);
        yearSpinner = findViewById(R.id.yearSpinner);
        messageEditText = findViewById(R.id.messageEditText);
        btnSend = findViewById(R.id.btn_send);
        btnUpload = findViewById(R.id.btn_upload);
        fabAdd = findViewById(R.id.fab_add);
        emptyView = findViewById(R.id.emptyView);
        recyclerViewNotifications = findViewById(R.id.recyclerViewNotifications);

        // Setup Spinners
        setupDepartmentSpinner();
        setupYearSpinner();

        // Setup RecyclerView
        recyclerViewNotifications.setLayoutManager(new LinearLayoutManager(this));
        notificationAdapter = new NotificationAdapter(notificationList);
        recyclerViewNotifications.setAdapter(notificationAdapter);

        // Send Notification Button
        btnSend.setOnClickListener(v -> sendNotification());

        // Upload File Button
        btnUpload.setOnClickListener(v -> uploadFile());

        // Floating Action Button Click
        fabAdd.setOnClickListener(v -> addNewNotification());
    }

    // Setup Department Spinner
    private void setupDepartmentSpinner() {
        String[] departments = {"Select Department", "Computer Science", "IT", "Mechanical", "Civil", "Electrical"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, departments);
        departmentSpinner.setAdapter(adapter);
    }

    // Setup Year Spinner
    private void setupYearSpinner() {
        String[] years = {"Select Year", "1st Year", "2nd Year", "3rd Year"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, years);
        yearSpinner.setAdapter(adapter);
    }

    // Send Notification Logic
    private void sendNotification() {
        String department = departmentSpinner.getSelectedItem().toString();
        String year = yearSpinner.getSelectedItem().toString();
        String message = messageEditText.getText().toString().trim();

        if ("Select Department".equals(department) || "Select Year".equals(year) || message.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        notificationList.add("📢 " + department + " - " + year + ":\n" + message);
        notificationAdapter.notifyDataSetChanged();
        messageEditText.setText("");

        // Hide empty view if notifications exist
        emptyView.setVisibility(notificationList.isEmpty() ? View.VISIBLE : View.GONE);
    }

    // Upload File Logic (Dummy Placeholder)
    private void uploadFile() {
        Toast.makeText(this, "Upload File Clicked", Toast.LENGTH_SHORT).show();
    }

    // Floating Button Logic (Add New Notification)
    private void addNewNotification() {
        Toast.makeText(this, "FAB Clicked - Add New Notification", Toast.LENGTH_SHORT).show();
    }
}
