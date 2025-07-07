package com.example.capstoneproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class adminprofile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView usernameTextView, descriptionTextView, emailTextView;
    private TextInputLayout descriptionInputLayout, emailInputLayout;
    private TextInputEditText descriptionEditText, emailEditText;
    private MaterialButton editProfileButton, saveButton, logoutButton;
    private FloatingActionButton changeProfileImageButton;
    private ImageView profileImageView;
    private String username;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminprofile);

        // Initialize SharedPreferences
        prefs = getSharedPreferences("AdminProfile", MODE_PRIVATE);

        // Get username from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");

        // Initialize Views
        initializeViews();

        // Set up UI with saved data
        setupProfileData();

        // Set up button listeners
        setupButtonListeners();
    }

    private void initializeViews() {
        usernameTextView = findViewById(R.id.usernameTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        emailTextView = findViewById(R.id.emailTextView);
        profileImageView = findViewById(R.id.profileImage);
        changeProfileImageButton = findViewById(R.id.changeProfileImageButton);

        descriptionInputLayout = findViewById(R.id.descriptionInputLayout);
        emailInputLayout = findViewById(R.id.emailInputLayout);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        emailEditText = findViewById(R.id.emailEditText);

        editProfileButton = findViewById(R.id.editProfileButton);
        saveButton = findViewById(R.id.saveButton);
        logoutButton = findViewById(R.id.logoutButton);
    }

    private void setupProfileData() {
        // Set username
        usernameTextView.setText(username);

        // Load saved profile data
        String savedDescription = prefs.getString(username + "_description", "");
        String savedEmail = prefs.getString(username + "_email", "");
        String savedImage = prefs.getString(username + "_profile_image", "");

        // Set initial values
        descriptionTextView.setText(savedDescription.isEmpty() ? "No Description" : savedDescription);
        emailTextView.setText(savedEmail.isEmpty() ? "No Email" : savedEmail);
        descriptionEditText.setText(savedDescription);
        emailEditText.setText(savedEmail);

        // Load profile image if available
        if (!savedImage.isEmpty()) {
            Bitmap bitmap = decodeBase64(savedImage);
            profileImageView.setImageBitmap(bitmap);
        } else {
            profileImageView.setImageResource(R.drawable.profile);
        }
    }

    private void setupButtonListeners() {
        // Edit Profile Button
        editProfileButton.setOnClickListener(view -> {
            descriptionTextView.setVisibility(View.GONE);
            emailTextView.setVisibility(View.GONE);

            descriptionInputLayout.setVisibility(View.VISIBLE);
            emailInputLayout.setVisibility(View.VISIBLE);

            saveButton.setVisibility(View.VISIBLE);
            editProfileButton.setVisibility(View.GONE);
        });

        // Save Button
        saveButton.setOnClickListener(view -> {
            String newDescription = descriptionEditText.getText().toString().trim();
            String newEmail = emailEditText.getText().toString().trim();

            // Save to SharedPreferences
            SharedPreferences.Editor editor = prefs.edit();
            editor.putString(username + "_description", newDescription);
            editor.putString(username + "_email", newEmail);
            editor.apply();

            // Update UI
            descriptionTextView.setText(newDescription.isEmpty() ? "No Description" : newDescription);
            emailTextView.setText(newEmail.isEmpty() ? "No Email" : newEmail);

            descriptionTextView.setVisibility(View.VISIBLE);
            emailTextView.setVisibility(View.VISIBLE);

            descriptionInputLayout.setVisibility(View.GONE);
            emailInputLayout.setVisibility(View.GONE);

            saveButton.setVisibility(View.GONE);
            editProfileButton.setVisibility(View.VISIBLE);

            Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();

            // Set result to indicate profile was updated
            setResult(RESULT_OK);
        });

        // Change Profile Image Button
        changeProfileImageButton.setOnClickListener(view -> openImageChooser());

        // Logout Button
        logoutButton.setOnClickListener(view -> {
            Intent intent = new Intent(adminprofile.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        });
    }

    private void openImageChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                profileImageView.setImageBitmap(bitmap);

                // Save image to SharedPreferences
                String encodedImage = encodeToBase64(bitmap);
                SharedPreferences.Editor editor = prefs.edit();
                editor.putString(username + "_profile_image", encodedImage);
                editor.apply();

                Toast.makeText(this, "Profile Image Updated", Toast.LENGTH_SHORT).show();

                // Set result to indicate image was updated
                setResult(RESULT_OK);
            } catch (IOException e) {
                e.printStackTrace();
                Toast.makeText(this, "Failed to load image", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private String encodeToBase64(Bitmap image) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
        byte[] byteArray = byteArrayOutputStream.toByteArray();
        return Base64.encodeToString(byteArray, Base64.DEFAULT);
    }

    private Bitmap decodeBase64(String encodedImage) {
        byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
    }
}