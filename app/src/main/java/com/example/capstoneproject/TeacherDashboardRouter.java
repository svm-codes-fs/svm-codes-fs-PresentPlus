package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;

public class TeacherDashboardRouter {

    public static void redirectToDashboard(Context context, String department) {
        if (department == null) {
            context.startActivity(new Intent(context, loginteacher.class));
            return;
        }

        Intent intent;

        switch (department.toUpperCase()) {
            case "CO": intent = new Intent(context, COTeacherDashboard.class); break;
            case "EJ": intent = new Intent(context, EJTeacherDashboard.class); break;
            case "EE": intent = new Intent(context, EETeacherDashboard.class); break;
            case "ME": intent = new Intent(context, METeacherDashboard.class); break;
            case "MS": intent = new Intent(context, MSTeacherDashboard.class); break;
            case "CE": intent = new Intent(context, CETeacherDashboard.class); break;
            default: intent = new Intent(context, loginteacher.class);
        }

        context.startActivity(intent);
    }
}
