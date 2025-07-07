package com.example.capstoneproject;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class admindashboard extends AppCompatActivity {

    private static final int PROFILE_REQUEST_CODE = 100;
    private TextView welcomeText, loginTimeText;
    private ImageButton profileButton, academicCalendarButton;
    private Button deptRecordsButton, notificationButton;
    private TextView recentActivity1, recentActivity2, recentActivity3, recentNotificationActivity;
    private String username;
    private long loginTime;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admindashboard);

        // Retrieve data from intent
        Intent intent = getIntent();
        username = intent.getStringExtra("USERNAME");
        loginTime = intent.getLongExtra("LOGIN_TIME", System.currentTimeMillis());

        // Initialize SharedPreferences
        prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // Initialize UI elements
        initializeViews();

        // Display user info
        displayUserInfo();

        // Load profile image
        loadProfileImage();

        // Load recent activities
        loadRecentActivities();

        // Set up button click listeners
        setupClickListeners();
    }

    private void initializeViews() {
        welcomeText = findViewById(R.id.adminPortalText); // Changed to match XML
        profileButton = findViewById(R.id.profileButton);
        deptRecordsButton = findViewById(R.id.deptRecordsButton);
        academicCalendarButton = findViewById(R.id.academicCalendarButton);
        notificationButton = findViewById(R.id.notificationButton);
        recentActivity1 = findViewById(R.id.recentActivity1);
        recentActivity2 = findViewById(R.id.recentActivity2);
        recentActivity3 = findViewById(R.id.recentActivity3);
        recentNotificationActivity = findViewById(R.id.recentNotificationActivity);
    }

    private void displayUserInfo() {
        if (welcomeText != null) {
            welcomeText.setText("Welcome, " + username + "!");
        }
        if (loginTimeText != null) {
            String formattedTime = new SimpleDateFormat("hh:mm a, dd MMM yyyy", Locale.getDefault())
                    .format(new Date(loginTime));
            loginTimeText.setText("Logged in at: " + formattedTime);
        }
    }

    private void setupClickListeners() {
        // Profile Button
        profileButton.setOnClickListener(v -> {
            Intent intent = new Intent(admindashboard.this, adminprofile.class);
            intent.putExtra("USERNAME", username);
            startActivityForResult(intent, PROFILE_REQUEST_CODE);
        });

        // Department Records Button
        deptRecordsButton.setOnClickListener(v -> {
            Intent intent = new Intent(admindashboard.this, DeptRecordsActivity.class);
            intent.putExtra("USERNAME", username);
            startActivity(intent);
            updateActivity("Viewed Department Records", "last_dept_access", recentActivity1);
        });

        // Academic Calendar Button
        academicCalendarButton.setOnClickListener(v -> {
            Intent intent = new Intent(admindashboard.this, calender.class);
            startActivity(intent);
            updateActivity("Checked Academic Calendar", "last_calendar_access", recentActivity2);
        });

        // Notification Button
        notificationButton.setOnClickListener(v -> {
            Intent intent = new Intent(admindashboard.this, adminNotification.class);
            startActivity(intent);
            updateActivity("Sent Notification", "last_notification", recentActivity3);
            updateNotificationActivity();
        });
    }

    private void updateActivity(String activityText, String prefKey, TextView textView) {
        String timestamp = getCurrentTime();
        String activityUpdate = activityText + " - " + timestamp;
        textView.setText(activityUpdate);
        saveToPreferences(prefKey, timestamp);
    }

    private void updateNotificationActivity() {
        String timestamp = getCurrentTime();
        recentNotificationActivity.setText("Latest Notification: " + timestamp);
        saveToPreferences("last_notification_detail", timestamp);
    }

    private void loadRecentActivities() {
        loadActivity("last_dept_access", "Viewed Department Records", recentActivity1);
        loadActivity("last_calendar_access", "Checked Academic Calendar", recentActivity2);
        loadActivity("last_notification", "Sent Notification", recentActivity3);

        String lastNotification = prefs.getString("last_notification_detail", "");
        if (!lastNotification.isEmpty()) {
            recentNotificationActivity.setText("Latest Notification: " + lastNotification);
        }
    }

    private void loadActivity(String prefKey, String defaultText, TextView textView) {
        String timestamp = prefs.getString(prefKey, "");
        if (!timestamp.isEmpty()) {
            textView.setText(defaultText + " - " + timestamp);
        }
    }

    private void saveToPreferences(String key, String value) {
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    private String getCurrentTime() {
        return new SimpleDateFormat("hh:mm a, dd MMM", Locale.getDefault()).format(new Date());
    }

    private void loadProfileImage() {
        String encodedImage = prefs.getString(username + "_profile_image", "");
        if (!encodedImage.isEmpty()) {
            try {
                byte[] decodedBytes = Base64.decode(encodedImage, Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
                if (bitmap != null) {
                    profileButton.setImageBitmap(getCircularBitmap(bitmap));
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        profileButton.setImageResource(R.drawable.profile);
    }

    private Bitmap getCircularBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        paint.setAntiAlias(true);
        canvas.drawOval(rectF, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PROFILE_REQUEST_CODE && resultCode == RESULT_OK) {
            loadProfileImage();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileImage();
        loadRecentActivities();
    }
}