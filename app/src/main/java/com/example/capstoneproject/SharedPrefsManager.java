package com.example.capstoneproject;

import android.content.Context;
import android.content.SharedPreferences;

public class SharedPrefsManager {

    private static final String PREF_NAME = "user_prefs";
    private static final String KEY_IS_LOGGED_IN = "is_logged_in";
    private static final String KEY_ROLE = "role";             // Admin, Student, Teacher, HOD
    private static final String KEY_DEPARTMENT = "department"; // For Teacher, HOD, Student
    private static final String KEY_YEAR = "year";             // For Student only
    private static final String KEY_NAME = "user_name";        // Optional: Store user name

    // Save login info (pass null for year if not Student)
    public static void saveLoginInfo(Context context, String role, String department, String year) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putBoolean(KEY_IS_LOGGED_IN, true);
        editor.putString(KEY_ROLE, role);
        editor.putString(KEY_DEPARTMENT, department);
        editor.putString(KEY_YEAR, year); // Can be null for non-students

        editor.apply();
    }

    // Save user name (optional)
    public static void saveUserName(Context context, String name) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putString(KEY_NAME, name).apply();
    }

    // Get user name
    public static String getUserName(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_NAME, null);
    }

    // Logout
    public static void logout(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().clear().apply();
    }

    // Check if user is logged in
    public static boolean isLoggedIn(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false);
    }

    // Getters
    public static String getUserRole(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_ROLE, null);
    }

    public static String getDepartment(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_DEPARTMENT, null);
    }

    public static String getYear(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getString(KEY_YEAR, null);
    }

    // Helpers
    public static boolean isAdmin(Context context) {
        return "Admin".equalsIgnoreCase(getUserRole(context));
    }

    public static boolean isHOD(Context context) {
        return "HOD".equalsIgnoreCase(getUserRole(context));
    }

    public static boolean isTeacher(Context context) {
        return "Teacher".equalsIgnoreCase(getUserRole(context));
    }

    public static boolean isStudent(Context context) {
        return "Student".equalsIgnoreCase(getUserRole(context));
    }

    // Optional: For redirection logic
    public static String getStudentDashboardKey(Context context) {
        String dept = getDepartment(context);
        String year = getYear(context);
        if (dept != null && year != null) {
            return dept + "_" + year; // e.g., CO_2nd
        }
        return null;
    }

    // Optional: Debug / display info
    public static String getAllInfo(Context context) {
        return "Role: " + getUserRole(context) +
                "\nDepartment: " + getDepartment(context) +
                "\nYear: " + getYear(context) +
                "\nName: " + getUserName(context);
    }
}
