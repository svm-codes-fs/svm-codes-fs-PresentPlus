package com.example.capstoneproject;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class ForgotPasswordActivity extends AppCompatActivity {

    private static final String SEND_OTP_URL = "http://10.0.2.2/student_api/send_otp.php";
    private static final String VERIFY_OTP_URL = "https://10.0.2.2/verify_otp.php";
    private TextInputEditText usernameEmailPhone;
    private MaterialButton sendOtpButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        usernameEmailPhone = findViewById(R.id.username_email_phone);
        sendOtpButton = findViewById(R.id.sendOtpButton);

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Sending OTP...");
        progressDialog.setCancelable(false);

        // Send OTP button click
        sendOtpButton.setOnClickListener(v -> sendOTP());
    }

    private void sendOTP() {
        String enteredUsername = usernameEmailPhone.getText().toString().trim();

        if (enteredUsername.isEmpty()) {
            Toast.makeText(this, "Enter username, email, or phone", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog.show();

        StringRequest request = new StringRequest(Request.Method.POST, SEND_OTP_URL,
                response -> {
                    progressDialog.dismiss();
                    Log.d("SEND_OTP_RESPONSE", response);

                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        String message = jsonObject.getString("message");
                        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(this, "Response Error", Toast.LENGTH_SHORT).show();
                    }
                },
                error -> {
                    progressDialog.dismiss();
                    Toast.makeText(this, "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("username", enteredUsername);
                return params;
            }
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(request);
    }
}
