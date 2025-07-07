package com.example.capstoneproject;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout.LayoutParams;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class cothirdyearstudprofile extends AppCompatActivity {

    private static final int PICK_IMAGE = 1;
    private static final int CAPTURE_IMAGE = 2;

    private ImageView profileImage;
    private TextView viewName, viewEmail, viewMobile, viewDescription;
    private EditText editName, editEmail, editMobile, editDescription;
    private Button editSaveButton, changePhotoButton, logoutButton;
    private SharedPreferences sharedPreferences;
    private String userEmail;
    private boolean isEditMode = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_studprofile);

        initializeViews();
        sharedPreferences = getSharedPreferences("StudentPrefs", MODE_PRIVATE);
        userEmail = sharedPreferences.getString("email", "");

        loadProfile();

        editSaveButton.setOnClickListener(v -> toggleEditMode());
        changePhotoButton.setOnClickListener(v -> openImageOptions());

        // Updated logout functionality
        logoutButton.setOnClickListener(v -> performLogout());
    }

    private void performLogout() {
        // Clear all user session data
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.remove("isLoggedIn");
        editor.remove("username");
        editor.remove("email");
        editor.remove("rollNo");
        editor.remove("department");
        editor.remove("year");
        editor.apply();

        // Navigate to login screen
        Intent intent = new Intent(this, loginstudent.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();

        Toast.makeText(this, "Logged out successfully", Toast.LENGTH_SHORT).show();
    }

    private void initializeViews() {
        profileImage = findViewById(R.id.profile_image);
        viewName = findViewById(R.id.view_name);
        viewEmail = findViewById(R.id.view_email);
        viewMobile = findViewById(R.id.view_mobile);
        viewDescription = findViewById(R.id.view_description);
        editName = findViewById(R.id.edit_name);
        editEmail = findViewById(R.id.edit_email);
        editMobile = findViewById(R.id.edit_mobile);
        editDescription = findViewById(R.id.edit_description);
        editSaveButton = findViewById(R.id.edit_save_button);
        changePhotoButton = findViewById(R.id.change_photo_button);
        logoutButton = findViewById(R.id.logout_button);
    }

    private void toggleEditMode() {
        if (isEditMode) {
            saveProfile();
            isEditMode = false;
            editSaveButton.setText("Edit Profile");
            switchToViewMode();
        } else {
            isEditMode = true;
            editSaveButton.setText("Save Changes");
            switchToEditMode();
        }
    }

    private void switchToViewMode() {
        editName.setVisibility(View.GONE);
        editEmail.setVisibility(View.GONE);
        editMobile.setVisibility(View.GONE);
        editDescription.setVisibility(View.GONE);
        changePhotoButton.setVisibility(View.GONE);

        viewName.setVisibility(View.VISIBLE);
        viewEmail.setVisibility(View.VISIBLE);
        viewMobile.setVisibility(View.VISIBLE);
        viewDescription.setVisibility(View.VISIBLE);

        loadProfile();

        LayoutParams params = (LayoutParams) editSaveButton.getLayoutParams();
        params.topToBottom = R.id.view_description;
        editSaveButton.setLayoutParams(params);
        editSaveButton.requestLayout();
    }

    private void switchToEditMode() {
        viewName.setVisibility(View.GONE);
        viewEmail.setVisibility(View.GONE);
        viewMobile.setVisibility(View.GONE);
        viewDescription.setVisibility(View.GONE);

        editName.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editMobile.setVisibility(View.VISIBLE);
        editDescription.setVisibility(View.VISIBLE);
        changePhotoButton.setVisibility(View.VISIBLE);

        LayoutParams photoParams = (LayoutParams) changePhotoButton.getLayoutParams();
        photoParams.topToBottom = R.id.profile_image;
        changePhotoButton.setLayoutParams(photoParams);
        changePhotoButton.requestLayout();

        editName.setText(viewName.getText().toString().replace("Name: ", ""));
        editEmail.setText(viewEmail.getText().toString().replace("Email: ", ""));
        editMobile.setText(viewMobile.getText().toString().replace("Mobile: ", ""));
        editDescription.setText(viewDescription.getText().toString().replace("Description: ", ""));

        LayoutParams params = (LayoutParams) editSaveButton.getLayoutParams();
        params.topToBottom = R.id.edit_description;
        editSaveButton.setLayoutParams(params);
        editSaveButton.requestLayout();
    }

    private void loadProfile() {
        viewName.setText("Name: " + sharedPreferences.getString("name", ""));
        viewEmail.setText("Email: " + sharedPreferences.getString("email", ""));
        viewMobile.setText("Mobile: " + sharedPreferences.getString("mobile", ""));
        viewDescription.setText("Description: " + sharedPreferences.getString("description", ""));

        String encodedImage = sharedPreferences.getString("profile_image", "");
        if (!encodedImage.isEmpty()) {
            byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            profileImage.setImageBitmap(bitmap);
        }
    }

    private void saveProfile() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("name", editName.getText().toString());
        editor.putString("email", editEmail.getText().toString());
        editor.putString("mobile", editMobile.getText().toString());
        editor.putString("description", editDescription.getText().toString());
        editor.apply();

        Toast.makeText(this, "Profile Updated", Toast.LENGTH_SHORT).show();
    }

    private void openImageOptions() {
        Intent pickIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        pickIntent.setType("image/*");

        Intent captureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        Intent chooser = Intent.createChooser(pickIntent, "Select or Capture Image");
        chooser.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{captureIntent});

        startActivityForResult(chooser, PICK_IMAGE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE && data != null && data.getData() != null) {
                Uri imageUri = data.getData();
                try {
                    Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                    profileImage.setImageBitmap(bitmap);
                    saveImage(bitmap);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (requestCode == CAPTURE_IMAGE && data != null && data.getExtras() != null) {
                Bitmap bitmap = (Bitmap) data.getExtras().get("data");
                if (bitmap != null) {
                    profileImage.setImageBitmap(bitmap);
                    saveImage(bitmap);
                }
            }
        }
    }

    private void saveImage(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        String encodedImage = Base64.encodeToString(baos.toByteArray(), Base64.DEFAULT);

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("profile_image", encodedImage);
        editor.apply();
    }
}