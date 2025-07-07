package com.example.capstoneproject;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import okhttp3.*;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class loginadmin extends AppCompatActivity {
    private static final String TAG = "LoginAdmin";
    private EditText usernameField, passwordField, specialCodeField;
    private Button authButton;
    private TextView toggleAuthMode, authTitle;
    private ProgressDialog progressDialog;
    private boolean isLoginMode = true;
    private static final String BASE_URL = "https://4179-106-220-201-101.ngrok-free.app/";
    private static final String LOGIN_ENDPOINT = "login.php";
    private static final String REGISTER_ENDPOINT = "register.php";
    private static final String SPECIAL_ADMIN_CODE = "BCPC0109";
    private OkHttpClient client;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginadmin);

        // Initialize OkHttpClient with timeouts
        client = new OkHttpClient.Builder()
                .connectTimeout(30, TimeUnit.SECONDS)
                .readTimeout(30, TimeUnit.SECONDS)
                .writeTimeout(30, TimeUnit.SECONDS)
                .build();

        initializeViews();
        setupAuthToggle();
    }

    private void initializeViews() {
        usernameField = findViewById(R.id.usernameInput);
        passwordField = findViewById(R.id.passwordInput);
        specialCodeField = findViewById(R.id.specialCodeInput);
        authButton = findViewById(R.id.authButton);
        toggleAuthMode = findViewById(R.id.toggleAuthMode);
        authTitle = findViewById(R.id.authTitle);

        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Processing...");

        authButton.setOnClickListener(v -> authenticateUser());
    }

    private void setupAuthToggle() {
        toggleAuthMode.setOnClickListener(v -> {
            isLoginMode = !isLoginMode;
            updateAuthUI();
        });
        updateAuthUI();
    }

    private void updateAuthUI() {
        if (isLoginMode) {
            authTitle.setText("Admin Login");
            authButton.setText("Login");
            toggleAuthMode.setText("Don't have an account? Register");
            specialCodeField.setVisibility(View.GONE);
        } else {
            authTitle.setText("Admin Registration");
            authButton.setText("Register");
            toggleAuthMode.setText("Already have an account? Login");
            specialCodeField.setVisibility(View.VISIBLE);
        }
    }

    private void authenticateUser() {
        String username = usernameField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String specialCode = specialCodeField.getText().toString().trim();

        if (validateInputs(username, password, specialCode)) {
            return;
        }

        progressDialog.show();
        performAuthentication(username, password, specialCode);
    }

    private boolean validateInputs(String username, String password, String specialCode) {
        if (username.isEmpty() || username.length() < 4) {
            showToast("Username must be at least 4 characters");
            return true;
        }
        if (password.isEmpty() || password.length() < 6) {
            showToast("Password must be at least 6 characters");
            return true;
        }
        if (!isLoginMode && !specialCode.equals(SPECIAL_ADMIN_CODE)) {
            showToast("Invalid special admin code");
            return true;
        }
        return false;
    }

    private void performAuthentication(String username, String password, String specialCode) {
        try {
            JSONObject jsonBody = new JSONObject();
            jsonBody.put("username", username);
            jsonBody.put("password", password);

            if (!isLoginMode) {
                jsonBody.put("special_code", specialCode);
            }

            RequestBody body = RequestBody.create(
                    MediaType.parse("application/json"),
                    jsonBody.toString()
            );

            String endpoint = isLoginMode ? LOGIN_ENDPOINT : REGISTER_ENDPOINT;
            Request request = new Request.Builder()
                    .url(BASE_URL + endpoint)
                    .post(body)
                    .addHeader("Content-Type", "application/json")
                    .build();

            Log.d(TAG, "Sending request to: " + request.url());
            Log.d(TAG, "Request body: " + jsonBody.toString());

            client.newCall(request).enqueue(new Callback() {
                @Override
                public void onFailure(@NonNull Call call, @NonNull IOException e) {
                    runOnUiThread(() -> {
                        progressDialog.dismiss();
                        showToast("Network error: " + e.getMessage());
                        Log.e(TAG, "Network request failed", e);
                    });
                }

                @Override
                public void onResponse(@NonNull Call call, @NonNull Response response) {
                    try (ResponseBody responseBody = response.body()) {
                        String responseData = responseBody != null ? responseBody.string() : "";
                        Log.d(TAG, "Response received: " + responseData);

                        JSONObject json = new JSONObject(responseData);
                        boolean success = json.getBoolean("success");
                        String message = json.getString("message");
                        String returnedUsername = json.optString("username", username);

                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            showToast(message);
                            if (success) {
                                navigateToDashboard(returnedUsername);
                            }
                        });
                    } catch (JSONException e) {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            showToast("Invalid server response format");
                            Log.e(TAG, "JSON parsing error", e);
                        });
                    } catch (IOException e) {
                        runOnUiThread(() -> {
                            progressDialog.dismiss();
                            showToast("Error reading response");
                            Log.e(TAG, "Response reading error", e);
                        });
                    }
                }
            });
        } catch (JSONException e) {
            progressDialog.dismiss();
            showToast("Error creating request");
            Log.e(TAG, "JSON creation error", e);
        }
    }

    private void navigateToDashboard(String username) {
        Intent intent = new Intent(this, admindashboard.class);
        intent.putExtra("USERNAME", username);
        intent.putExtra("LOGIN_TIME", System.currentTimeMillis());
        startActivity(intent);
        finish();
    }

    private void showToast(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onDestroy() {
        if (progressDialog != null && progressDialog.isShowing()) {
            progressDialog.dismiss();
        }
        // Cancel ongoing network requests
        client.dispatcher().cancelAll();
        super.onDestroy();
    }
}