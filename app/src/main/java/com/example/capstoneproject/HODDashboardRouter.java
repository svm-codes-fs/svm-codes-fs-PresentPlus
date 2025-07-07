package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;

public class HODDashboardRouter {

    public static void redirectToDashboard(Context context, String department) {
        if (department == null) {
            context.startActivity(new Intent(context, loginteacher.class));
            return;
        }

        Intent intent;

        switch (department.toUpperCase()) {
            case "CO": intent = new Intent(context, COHODDashboard.class); break;
            case "EJ": intent = new Intent(context, EJHODDashboard.class); break;
            case "EE": intent = new Intent(context, EEHODDashboard.class); break;
            case "ME": intent = new Intent(context, MEHODDashboard.class); break;
            case "MS": intent = new Intent(context, MSHODDashboard.class); break;
            case "CE": intent = new Intent(context, CEHODDashboard.class); break;
            default: intent = new Intent(context, loginteacher.class);
        }

        context.startActivity(intent);
    }
}
