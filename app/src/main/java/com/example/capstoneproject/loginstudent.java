package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class loginstudent extends AppCompatActivity {

    // Update with your ngrok URL
    private static final String BASE_URL = "https://e706-2409-40c2-4040-1a43-9939-ee15-74a4-2e9d.ngrok-free.app/";
    private static final String LOGIN_URL = BASE_URL + "login_student.php";
    private static final String REGISTER_URL = BASE_URL + "register_student.php";
    private static final String TAG = "LoginStudent";

    // UI Elements
    private TextInputEditText usernameEditText, passwordEditText, rollNoEditText;
    private AutoCompleteTextView departmentDropdown;
    private RadioGroup yearRadioGroup;
    private MaterialButton loginButton, registerButton;
    private TextView switchAuthMode;
    private LinearLayout registerFields;

    // SharedPreferences and Volley
    private SharedPreferences sharedPreferences;
    private RequestQueue requestQueue;
    private boolean isRegisterMode = false;
    private ResourceBundle response;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginstudent);

        // Initialize SharedPreferences
        sharedPreferences = getSharedPreferences("StudentPrefs", Context.MODE_PRIVATE);

        // Auto-login if already logged in
        if (sharedPreferences.getBoolean("isLoggedIn", false)) {
            navigateToDashboard();
            return;
        }

        // Initialize Volley RequestQueue
        requestQueue = Volley.newRequestQueue(this);

        // Initialize UI components
        initializeViews();
        setupDepartmentDropdown();
        setClickListeners();
    }

    private void initializeViews() {
        usernameEditText = findViewById(R.id.username);
        passwordEditText = findViewById(R.id.password);
        rollNoEditText = findViewById(R.id.roll_no);
        departmentDropdown = findViewById(R.id.department_dropdown);
        yearRadioGroup = findViewById(R.id.year_radio_group);
        loginButton = findViewById(R.id.loginButton);
        registerButton = findViewById(R.id.registerButton);
        switchAuthMode = findViewById(R.id.switch_auth_mode);
        registerFields = findViewById(R.id.register_fields);
    }

    private void setupDepartmentDropdown() {
        String[] departments = getResources().getStringArray(R.array.departments_array);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_dropdown_item_1line,
                departments
        );
        departmentDropdown.setAdapter(adapter);
    }

    private void setClickListeners() {
        switchAuthMode.setOnClickListener(v -> toggleAuthMode());
        loginButton.setOnClickListener(v -> {
            if (validateLoginForm()) {
                performLogin();
            }
        });
        registerButton.setOnClickListener(v -> {
            if (validateRegisterForm()) {
                performRegistration();
            }
        });
    }

    private void toggleAuthMode() {
        isRegisterMode = !isRegisterMode;
        registerFields.setVisibility(isRegisterMode ? View.VISIBLE : View.GONE);
        loginButton.setVisibility(isRegisterMode ? View.GONE : View.VISIBLE);
        registerButton.setVisibility(isRegisterMode ? View.VISIBLE : View.GONE);
        switchAuthMode.setText(isRegisterMode ? R.string.switch_to_login : R.string.switch_to_register);
    }

    private boolean validateLoginForm() {
        boolean isValid = true;

        if (TextUtils.isEmpty(usernameEditText.getText().toString().trim())) {
            usernameEditText.setError("Username is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(passwordEditText.getText().toString().trim())) {
            passwordEditText.setError("Password is required");
            isValid = false;
        }

        return isValid;
    }

    private boolean validateRegisterForm() {
        boolean isValid = validateLoginForm(); // Validate username and password

        if (TextUtils.isEmpty(rollNoEditText.getText().toString().trim())) {
            rollNoEditText.setError("Roll number is required");
            isValid = false;
        }

        if (TextUtils.isEmpty(departmentDropdown.getText().toString().trim())) {
            departmentDropdown.setError("Department is required");
            isValid = false;
        }

        if (yearRadioGroup.getCheckedRadioButtonId() == -1) {
            Toast.makeText(this, "Please select a year", Toast.LENGTH_SHORT).show();
            isValid = false;
        }

        return isValid;
    }

    private void performLogin() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("username", usernameEditText.getText().toString().trim());
            jsonRequest.put("password", passwordEditText.getText().toString().trim());
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            showToast("Error creating request");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                LOGIN_URL,
                jsonRequest,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject student = response.getJSONObject("student");
                            saveUserSession(
                                    student.getString("username"),
                                    student.getString("roll_no"),
                                    student.getString("department"),
                                    student.getString("year")
                            );
                            showToast("Login successful!");
                            navigateToDashboard();
                        } else {
                            showToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        showToast("Invalid server response");
                    }
                },
                error -> {
                    Log.e(TAG, "Login Error: " + error.getMessage());
                    showToast("Network error. Please try again.");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void performRegistration() {
        JSONObject jsonRequest = new JSONObject();
        try {
            jsonRequest.put("username", usernameEditText.getText().toString().trim());
            jsonRequest.put("password", passwordEditText.getText().toString().trim());
            jsonRequest.put("roll_no", rollNoEditText.getText().toString().trim());
            jsonRequest.put("department", departmentDropdown.getText().toString().trim());

            RadioButton selectedYear = findViewById(yearRadioGroup.getCheckedRadioButtonId());
            jsonRequest.put("year", selectedYear.getText().toString());
        } catch (JSONException e) {
            Log.e(TAG, "JSON Exception: " + e.getMessage());
            showToast("Error creating request");
            return;
        }

        JsonObjectRequest request = new JsonObjectRequest(
                Request.Method.POST,
                REGISTER_URL,
                jsonRequest,
                response -> {
                    try {
                        if (response.getBoolean("success")) {
                            JSONObject student = response.getJSONObject("student");
                            saveUserSession(
                                    student.getString("username"),
                                    student.getString("roll_no"),
                                    student.getString("department"),
                                    student.getString("year")
                            );
                            showToast("Registration successful!");
                            navigateToDashboard();
                        } else {
                            showToast(response.getString("message"));
                        }
                    } catch (JSONException e) {
                        Log.e(TAG, "JSON Parsing error: " + e.getMessage());
                        showToast("Invalid server response");
                    }
                },
                error -> {
                    Log.e(TAG, "Registration Error: " + error.getMessage());
                    showToast("Network error. Please try again.");
                }
        ) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json");
                headers.put("Accept", "application/json");
                return headers;
            }
        };

        requestQueue.add(request);
    }

    private void saveUserSession(String username, String rollNo, String department, String year) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("isLoggedIn", true);
        editor.putString("username", username);
        editor.putString("rollNo", rollNo);
        editor.putString("department", department);
        editor.putString("year", year);
        editor.apply();
    }



    private void navigateToDashboard() {
        String department = sharedPreferences.getString("department", "");
        String year = sharedPreferences.getString("year", "");

        Intent intent;

        switch (department.toLowerCase()) {
            case "computer science":
            case "co":  // if you store short code
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, COFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, COSecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, COThirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            case "mechanical":
            case "me":
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, MEFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, MESecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, METhirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            case "electrical":
            case "ee":
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, EEFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, EESecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, EEThirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            case "civil":
            case "ce":
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, CEFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, CESecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, CEThirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            case "electronics":
            case "ej":
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, EJFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, EJSecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, EJThirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            case "mechanical systems":
            case "ms":
                switch (year.toLowerCase()) {
                    case "1st year":
                    case "1st":
                        intent = new Intent(this, MSFirstYearDashboard.class);
                        break;
                    case "2nd year":
                    case "2nd":
                        intent = new Intent(this, MSSecondYearDashboard.class);
                        break;
                    case "3rd year":
                    case "3rd":
                        intent = new Intent(this, MSThirdYearDashboard.class);
                        break;
                    default:
                        showInvalidDashboardToast();
                        return;
                }
                break;

            default:
                showInvalidDashboardToast();
                return;
        }

        startActivity(intent);
        finish();
    }

    private void showInvalidDashboardToast() {
        Toast.makeText(this, "Dashboard not found for your department/year", Toast.LENGTH_SHORT).show();
    }


    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (requestQueue != null) {
            requestQueue.cancelAll(TAG);
        }
    }
}