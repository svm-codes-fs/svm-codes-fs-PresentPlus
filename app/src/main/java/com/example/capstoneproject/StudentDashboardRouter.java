package com.example.capstoneproject;

import android.content.Context;
import android.content.Intent;

public class StudentDashboardRouter {

    public static void redirectToDashboard(Context context, String department, String year) {
        if (department == null || year == null) {
            context.startActivity(new Intent(context, loginstudent.class));
            return;
        }

        Intent intent;

        switch (department.toUpperCase()) {
            case "CO":
                switch (year) {
                    case "1": intent = new Intent(context, COFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, COSecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, COThirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            case "EJ":
                switch (year) {
                    case "1": intent = new Intent(context, EJFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, EJSecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, EJThirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            case "EE":
                switch (year) {
                    case "1": intent = new Intent(context, EEFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, EESecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, EEThirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            case "ME":
                switch (year) {
                    case "1": intent = new Intent(context, MEFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, MESecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, METhirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            case "MS":
                switch (year) {
                    case "1": intent = new Intent(context, MSFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, MSSecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, MSThirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            case "CE":
                switch (year) {
                    case "1": intent = new Intent(context, CEFirstYearDashboard.class); break;
                    case "2": intent = new Intent(context, CESecondYearDashboard.class); break;
                    case "3": intent = new Intent(context, CEThirdYearDashboard.class); break;
                    default: intent = new Intent(context, loginstudent.class);
                }
                break;

            default:
                intent = new Intent(context, loginstudent.class);
        }

        context.startActivity(intent);
    }
}
