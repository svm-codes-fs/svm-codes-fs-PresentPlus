package com.example.capstoneproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.button.MaterialButtonToggleGroup;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class loginteacher extends AppCompatActivity {

    private EditText usernameInput, passwordInput;
    private AutoCompleteTextView departmentDropdown;
    private MaterialButtonToggleGroup roleToggleGroup;
    private TextView toggleAuthMode, authTitle;
    private MaterialButton authButton;
    private ProgressDialog progressDialog;
    private boolean isLoginMode = true;

    private static final String[] DEPARTMENTS = {"CO", "EJ", "ME", "CE", "MS", "EE"};
    private static final String LOGIN_URL = "https://4179-106-220-201-101.ngrok-free.app/login_teacher.php";
    private static final String REGISTER_URL = "https://4179-106-220-201-101.ngrok-free.app/register_teacher.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginteacher);

        // Check if already logged in as teacher or HOD
        if (SharedPrefsManager.isLoggedIn(this) &&
                ("teacher".equalsIgnoreCase(SharedPrefsManager.getUserRole(this)) ||
                        "HOD".equalsIgnoreCase(SharedPrefsManager.getUserRole(this)))) {
            redirectBasedOnRole();
            return;
        }

        initializeViews();
        setupDepartmentDropdown();
    }

    private void initializeViews() {
        usernameInput = findViewById(R.id.usernameInput);
        passwordInput = findViewById(R.id.passwordInput);
        departmentDropdown = findViewById(R.id.departmentDropdown);
        roleToggleGroup = findViewById(R.id.roleToggleGroup);
        toggleAuthMode = findViewById(R.id.toggleAuthMode);
        authTitle = findViewById(R.id.authTitle);
        authButton = findViewById(R.id.authButton);

        toggleAuthMode.setOnClickListener(v -> toggleAuthMode());
        authButton.setOnClickListener(v -> performAuthAction());

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
    }

    private void redirectBasedOnRole() {
        String role = SharedPrefsManager.getUserRole(this);
        String department = SharedPrefsManager.getDepartment(this);

        if ("HOD".equalsIgnoreCase(role)) {
            switch (department) {
                case "CO":
                    startActivity(new Intent(this, COHODDashboard.class));
                    break;
                case "EJ":
                    startActivity(new Intent(this, EJHODDashboard.class));
                    break;
                case "ME":
                    startActivity(new Intent(this, MEHODDashboard.class));
                    break;
                case "CE":
                    startActivity(new Intent(this, CEHODDashboard.class));
                    break;
                case "MS":
                    startActivity(new Intent(this, MSHODDashboard.class));
                    break;
                case "EE":
                    startActivity(new Intent(this, EEHODDashboard.class));
                    break;
                default:
                    Toast.makeText(this, "Unknown department!", Toast.LENGTH_SHORT).show();
                    return;
            }
        } else {
            switch (department) {
                case "CO":
                    startActivity(new Intent(this, COTeacherDashboard.class));
                    break;
                case "EJ":
                    startActivity(new Intent(this, EJTeacherDashboard.class));
                    break;
                case "ME":
                    startActivity(new Intent(this, METeacherDashboard.class));
                    break;
                case "CE":
                    startActivity(new Intent(this, CETeacherDashboard.class));
                    break;
                case "MS":
                    startActivity(new Intent(this, MSTeacherDashboard.class));
                    break;
                case "EE":
                    startActivity(new Intent(this, EETeacherDashboard.class));
                    break;
                default:
                    Toast.makeText(this, "Unknown department!", Toast.LENGTH_SHORT).show();
                    return;
            }
        }
        finish();
    }

    private void performAuthAction() {
        if (isLoginMode) {
            loginTeacher();
        } else {
            registerTeacher();
        }
    }

    private void loginTeacher() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String department = departmentDropdown.getText().toString().trim();

        if (!validateLoginInputs(username, password, department)) return;

        showProgress("Logging in...");

        StringRequest request = new StringRequest(Request.Method.POST, LOGIN_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            JSONObject user = json.getJSONObject("user");

                            // Save login session using static method
                            SharedPrefsManager.saveLoginInfo(
                                    this,
                                    user.getString("role"),  // "teacher" or "HOD"
                                    user.getString("department"),
                                    ""  // year not used for teachers
                            );

                            redirectBasedOnRole();
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    dismissProgress();
                },
                error -> {
                    Toast.makeText(this, "Login error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    dismissProgress();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("department", department);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private void registerTeacher() {
        String username = usernameInput.getText().toString().trim();
        String password = passwordInput.getText().toString().trim();
        String department = departmentDropdown.getText().toString().trim();
        int selectedRoleId = roleToggleGroup.getCheckedButtonId();

        if (!validateRegistrationInputs(username, password, department, selectedRoleId)) return;

        String role = (selectedRoleId == R.id.radioHOD) ? "HOD" : "Teacher";
        showProgress("Registering...");

        StringRequest request = new StringRequest(Request.Method.POST, REGISTER_URL,
                response -> {
                    try {
                        JSONObject json = new JSONObject(response);
                        if (json.getBoolean("success")) {
                            Toast.makeText(this, "Registration successful!", Toast.LENGTH_LONG).show();
                            toggleAuthMode();
                        } else {
                            Toast.makeText(this, json.getString("message"), Toast.LENGTH_LONG).show();
                        }
                    } catch (JSONException e) {
                        Toast.makeText(this, "Error parsing response", Toast.LENGTH_SHORT).show();
                    }
                    dismissProgress();
                },
                error -> {
                    Toast.makeText(this, "Registration error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    dismissProgress();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", username);
                params.put("password", password);
                params.put("department", department);
                params.put("role", role);
                return params;
            }
        };
        Volley.newRequestQueue(this).add(request);
    }

    private boolean validateLoginInputs(String username, String password, String department) {
        if (TextUtils.isEmpty(username)) {
            usernameInput.setError("Username required");
            return false;
        }
        if (TextUtils.isEmpty(password)) {
            passwordInput.setError("Password required");
            return false;
        }
        if (TextUtils.isEmpty(department)) {
            departmentDropdown.setError("Department required");
            return false;
        }
        return true;
    }

    private boolean validateRegistrationInputs(String username, String password, String department, int selectedRoleId) {
        if (!validateLoginInputs(username, password, department)) return false;
        if (password.length() < 6) {
            passwordInput.setError("Password too short");
            return false;
        }
        if (selectedRoleId == -1) {
            Toast.makeText(this, "Please select a role", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void toggleAuthMode() {
        isLoginMode = !isLoginMode;
        authTitle.setText(isLoginMode ? "Teacher Login" : "Teacher Registration");
        authButton.setText(isLoginMode ? "Login" : "Register");
        toggleAuthMode.setText(isLoginMode ? "Don't have an account? Register" : "Already have an account? Login");
        roleToggleGroup.setVisibility(isLoginMode ? View.GONE : View.VISIBLE);
        findViewById(R.id.roleLabel).setVisibility(isLoginMode ? View.GONE : View.VISIBLE);
        if (isLoginMode) passwordInput.setText("");
    }

    private void setupDepartmentDropdown() {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line, DEPARTMENTS);
        departmentDropdown.setAdapter(adapter);
        departmentDropdown.setOnClickListener(v -> departmentDropdown.showDropDown());
    }

    private void showProgress(String message) {
        progressDialog.setMessage(message);
        progressDialog.show();
    }

    private void dismissProgress() {
        if (progressDialog.isShowing()) progressDialog.dismiss();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, MainActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP));
        finish();
    }
}