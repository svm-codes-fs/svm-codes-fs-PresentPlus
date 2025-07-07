package com.example.capstoneproject;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.File;
import java.io.IOException;

public class teacherprofile extends AppCompatActivity {

    private ImageView profileImageView;
    private FloatingActionButton changeProfileImageButton;
    private Button editProfileButton, saveButton, logoutButton;
    private TextView usernameTextView, roleTextView, departmentTextView;
    private TextInputLayout descriptionInputLayout, mobileInputLayout, emailInputLayout;
    private TextInputEditText descriptionEditText, mobileEditText, emailEditText;

    private SharedPreferences sharedPreferences;
    private static final String PREFS_NAME = "UserPrefs";

    private String username, role, department;
    private Uri cameraImageUri;
    private File photoFile;

    private final ActivityResultLauncher<Intent> cameraLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK) {
                    profileImageView.setImageURI(cameraImageUri);
                    saveProfileImage(cameraImageUri);
                }
            });

    private final ActivityResultLauncher<Intent> galleryLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                    Uri selectedImageUri = result.getData().getData();
                    profileImageView.setImageURI(selectedImageUri);
                    saveProfileImage(selectedImageUri);
                }
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_teacherprofile);

        // Initialize Views
        profileImageView = findViewById(R.id.profile);
        changeProfileImageButton = findViewById(R.id.changeprofileImageButton);
        editProfileButton = findViewById(R.id.editprofileButton);
        saveButton = findViewById(R.id.savebutton);
        logoutButton = findViewById(R.id.logoutButton);
        usernameTextView = findViewById(R.id.usernametextView);
        roleTextView = findViewById(R.id.roleTextView);
        departmentTextView = findViewById(R.id.departmentTextView);
        descriptionInputLayout = findViewById(R.id.descriptioninputLayout);
        mobileInputLayout = findViewById(R.id.mobileinputLayout);
        emailInputLayout = findViewById(R.id.emailinputLayout);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        mobileEditText = findViewById(R.id.mobileEditText);
        emailEditText = findViewById(R.id.emaileditText);

        // Load teacher details
        sharedPreferences = getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        username = sharedPreferences.getString("logged_username", "");
        department = sharedPreferences.getString("logged_department", "");
        role = sharedPreferences.getString("logged_role", "");

        usernameTextView.setText(username);
        roleTextView.setText(role);
        departmentTextView.setText(department);

        loadTeacherDetails();
        loadProfileImage();
        disableEditing();

        // Click Listeners
        changeProfileImageButton.setOnClickListener(view -> showImagePickerDialog());
        editProfileButton.setOnClickListener(view -> enableEditing());
        saveButton.setOnClickListener(view -> saveChanges());
        logoutButton.setOnClickListener(view -> logoutUser());
    }

    private void loadTeacherDetails() {
        String savedMobile = sharedPreferences.getString(username + "_mobile", "");
        String savedEmail = sharedPreferences.getString(username + "_email", "");
        String savedDescription = sharedPreferences.getString(username + "_description", "");

        mobileEditText.setText(savedMobile);
        emailEditText.setText(savedEmail);
        descriptionEditText.setText(savedDescription);
    }

    private void loadProfileImage() {
        String savedImageUri = sharedPreferences.getString(username + "_profile_image", null);
        if (savedImageUri != null) {
            profileImageView.setImageURI(Uri.parse(savedImageUri));
        }
    }

    private void disableEditing() {
        descriptionEditText.setEnabled(false);
        mobileEditText.setEnabled(false);
        emailEditText.setEnabled(false);
        saveButton.setVisibility(View.GONE);
        editProfileButton.setVisibility(View.VISIBLE);
    }

    private void enableEditing() {
        descriptionEditText.setEnabled(true);
        mobileEditText.setEnabled(true);
        emailEditText.setEnabled(true);
        saveButton.setVisibility(View.VISIBLE);
        editProfileButton.setVisibility(View.GONE);
    }

    private void saveChanges() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username + "_description", descriptionEditText.getText().toString().trim());
        editor.putString(username + "_mobile", mobileEditText.getText().toString().trim());
        editor.putString(username + "_email", emailEditText.getText().toString().trim());
        editor.apply();
        disableEditing();
        Toast.makeText(this, "Changes saved", Toast.LENGTH_SHORT).show();
    }

    private void showImagePickerDialog() {
        new AlertDialog.Builder(this)
                .setTitle("Set Profile Image")
                .setItems(new String[]{"📷 Camera", "🖼️ Gallery"}, (dialog, which) -> {
                    if (which == 0) checkCameraPermissionAndOpen();
                    else openGallery();
                })
                .setNegativeButton("Cancel", null)
                .show();
    }

    private void checkCameraPermissionAndOpen() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA}, 200);
        } else {
            openCamera();
        }
    }

    private void openCamera() {
        try {
            photoFile = createImageFile();
            if (photoFile != null) {
                cameraImageUri = FileProvider.getUriForFile(this, "com.example.capstoneproject.fileprovider", photoFile);
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraImageUri);
                cameraLauncher.launch(cameraIntent);
            }
        } catch (IOException e) {
            Toast.makeText(this, "Failed to open camera: " + e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    private File createImageFile() throws IOException {
        String imageFileName = "IMG_" + System.currentTimeMillis();
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        return File.createTempFile(imageFileName, ".jpg", storageDir);
    }

    private void openGallery() {
        Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        galleryLauncher.launch(galleryIntent);
    }

    private void saveProfileImage(Uri imageUri) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(username + "_profile_image", imageUri.toString());
        editor.apply();
    }

    private void logoutUser() {
        try {
            // Clear all user preferences
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear(); // This clears ALL preferences
            editor.apply();

            // Create intent to redirect to MainActivity
            Intent intent = new Intent(teacherprofile.this, MainActivity.class);

            // Clear the activity stack completely
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);

            startActivity(intent);

            // Finish the current activity and all activities in the back stack
            finishAffinity();

            // Optional: Add a smooth transition animation
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        } catch (Exception e) {
            // Fallback if something goes wrong
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 200 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            openCamera();
        } else {
            Toast.makeText(this, "Camera permission is required to take a photo", Toast.LENGTH_SHORT).show();
        }
    }
}