package com.example.capstoneproject;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class cothirdyaerstudleavemanagementsystem extends AppCompatActivity {

    private EditText leaveTypeEditText, startDateEditText, endDateEditText,
            descriptionEditText, nameEditText, idEditText, mobileEditText;
    private Button submitButton;
    // Update with your ngrok URL
    private static final String SERVER_URL = "  https://0eab-2409-40c2-4031-dbb9-4c37-1226-e226-5ea2.ngrok-free.app/submit_leave.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studleavemanagementsystem);

        initializeViews();
        setupDatePickers();
        setupSubmitButton();
    }

    private void initializeViews() {
        leaveTypeEditText = findViewById(R.id.leaveTypeEditText);
        startDateEditText = findViewById(R.id.startDateEditText);
        endDateEditText = findViewById(R.id.endDateEditText);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        nameEditText = findViewById(R.id.nameEditText);
        idEditText = findViewById(R.id.idEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        submitButton = findViewById(R.id.submitButton);
    }

    private void setupDatePickers() {
        startDateEditText.setOnClickListener(v -> showDatePicker(startDateEditText));
        endDateEditText.setOnClickListener(v -> showDatePicker(endDateEditText));
    }

    private void showDatePicker(final EditText editText) {
        final Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(
                this,
                (view, year, month, dayOfMonth) -> {
                    String selectedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", year, month + 1, dayOfMonth);
                    editText.setText(selectedDate);
                },
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
        );
        datePickerDialog.show();
    }

    private void setupSubmitButton() {
        submitButton.setOnClickListener(v -> {
            if (validateForm()) {
                submitLeaveApplication();
            }
        });
    }

    private boolean validateForm() {
        boolean valid = true;

        if (leaveTypeEditText.getText().toString().trim().isEmpty()) {
            showError(leaveTypeEditText, "Leave type required");
            valid = false;
        }
        if (startDateEditText.getText().toString().trim().isEmpty()) {
            showError(startDateEditText, "Start date required");
            valid = false;
        }
        if (endDateEditText.getText().toString().trim().isEmpty()) {
            showError(endDateEditText, "End date required");
            valid = false;
        }
        if (descriptionEditText.getText().toString().trim().isEmpty()) {
            showError(descriptionEditText, "Description required");
            valid = false;
        }
        if (nameEditText.getText().toString().trim().isEmpty()) {
            showError(nameEditText, "Name required");
            valid = false;
        }
        if (idEditText.getText().toString().trim().isEmpty()) {
            showError(idEditText, "ID required");
            valid = false;
        }
        if (mobileEditText.getText().toString().trim().isEmpty()) {
            showError(mobileEditText, "Mobile number required");
            valid = false;
        } else if (mobileEditText.getText().toString().trim().length() < 10) {
            showError(mobileEditText, "Valid mobile number required");
            valid = false;
        }

        // Validate date sequence
        if (valid && !startDateEditText.getText().toString().isEmpty() && !endDateEditText.getText().toString().isEmpty()) {
            try {
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
                Date startDate = sdf.parse(startDateEditText.getText().toString());
                Date endDate = sdf.parse(endDateEditText.getText().toString());

                if (startDate.after(endDate)) {
                    showError(startDateEditText, "Start date must be before end date");
                    valid = false;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return valid;
    }

    private void showError(EditText field, String message) {
        field.setError(message);
        field.requestFocus();
    }

    private void submitLeaveApplication() {
        String leaveType = leaveTypeEditText.getText().toString().trim();
        String startDate = startDateEditText.getText().toString().trim();
        String endDate = endDateEditText.getText().toString().trim();
        String description = descriptionEditText.getText().toString().trim();
        String name = nameEditText.getText().toString().trim();
        String id = idEditText.getText().toString().trim();
        String mobile = mobileEditText.getText().toString().trim();
        String timestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(new Date());

        new SubmitLeaveTask().execute(leaveType, startDate, endDate, description, name, id, mobile, timestamp);
    }

    private class SubmitLeaveTask extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(cothirdyaerstudleavemanagementsystem.this);
            progressDialog.setMessage("Submitting leave application...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection conn = null;
            BufferedReader reader = null;
            String response;

            try {
                URL url = new URL(SERVER_URL);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type", "application/json");
                conn.setRequestProperty("Accept", "application/json");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(15000);

                // Create JSON object
                JSONObject jsonParam = new JSONObject();
                jsonParam.put("leave_type", params[0]);
                jsonParam.put("start_date", params[1]);
                jsonParam.put("end_date", params[2]);
                jsonParam.put("description", params[3]);
                jsonParam.put("student_name", params[4]);
                jsonParam.put("student_id", params[5]);
                jsonParam.put("mobile_number", params[6]);

                // Write JSON to output stream
                OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
                writer.write(jsonParam.toString());
                writer.flush();
                writer.close();
                os.close();

                // Get response code
                int responseCode = conn.getResponseCode();
                if (responseCode == HttpURLConnection.HTTP_OK) {
                    // Read response
                    StringBuilder sb = new StringBuilder();
                    reader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    String line;
                    while ((line = reader.readLine()) != null) {
                        sb.append(line);
                    }
                    response = sb.toString();
                } else {
                    response = "{\"status\":\"error\",\"message\":\"HTTP error code: " + responseCode + "\"}";
                }
            } catch (Exception e) {
                response = "{\"status\":\"error\",\"message\":\"" + e.getMessage() + "\"}";
            } finally {
                try {
                    if (reader != null) reader.close();
                    if (conn != null) conn.disconnect();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            return response;
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            Log.d("LeaveApp", "Raw server response: " + result);

            try {
                // First check if the response is empty
                if (result == null || result.trim().isEmpty()) {
                    throw new Exception("Empty server response");
                }

                // Parse the JSON response
                JSONObject jsonResponse = new JSONObject(result);
                String status = jsonResponse.getString("status");
                String message = jsonResponse.getString("message");

                if (status.equals("success")) {
                    Toast.makeText(cothirdyaerstudleavemanagementsystem.this,
                            message, Toast.LENGTH_SHORT).show();
                    clearForm();
                } else {
                    Toast.makeText(cothirdyaerstudleavemanagementsystem.this,
                            "Error: " + message, Toast.LENGTH_LONG).show();
                }
            } catch (JSONException e) {
                Log.e("LeaveApp", "JSON parsing error: " + e.getMessage());
                Toast.makeText(cothirdyaerstudleavemanagementsystem.this,
                        "Server response format error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            } catch (Exception e) {
                Log.e("LeaveApp", "General error: " + e.getMessage());
                Toast.makeText(cothirdyaerstudleavemanagementsystem.this,
                        "Error: " + e.getMessage(),
                        Toast.LENGTH_LONG).show();
            }
        }

        private String getPostDataString(HashMap<String, String> params) throws UnsupportedEncodingException {
            StringBuilder result = new StringBuilder();
            boolean first = true;
            for (Map.Entry<String, String> entry : params.entrySet()) {
                if (first)
                    first = false;
                else
                    result.append("&");

                result.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                result.append("=");
                result.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }
            return result.toString();
        }
    }

    private void clearForm() {
        leaveTypeEditText.setText("");
        startDateEditText.setText("");
        endDateEditText.setText("");
        descriptionEditText.setText("");
        // Keep student info fields filled for convenience
    }
}