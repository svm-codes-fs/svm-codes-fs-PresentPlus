package com.example.capstoneproject;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import com.example.capstoneproject.RequestHandler;
import com.example.capstoneproject.LeaveAdapter;
import com.example.capstoneproject.LeaveApplication;




public class hodleavemanagement extends AppCompatActivity {

    private ListView leaveListView;
    private LeaveAdapter adapter;
    private ArrayList<LeaveApplication> leaveList = new ArrayList<>();
    private static final String FETCH_URL = "https://yourserver.com/fetch_leave.php";
    private static final String ACTION_URL = "https://yourserver.com/update_leave_status.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hodleavemanagement);

        leaveListView = findViewById(R.id.leaveListView);
        adapter = new LeaveAdapter(this, leaveList);
        leaveListView.setAdapter(adapter);

        new FetchLeaveApplications().execute();

        leaveListView.setOnItemClickListener((parent, view, position, id) -> {
            String leaveId;



        });
    }

    private void showActionDialog(String leaveId, String leaveStatus) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Update Leave Status");
        builder.setMessage("Choose an action for this leave request.");

        builder.setPositiveButton("Approve", (dialog, which) -> updateLeaveStatus(leaveId, "Approved"));
        builder.setNegativeButton("Reject", (dialog, which) -> updateLeaveStatus(leaveId, "Rejected"));
        builder.setNeutralButton("Cancel", (dialog, which) -> dialog.dismiss());

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private class FetchLeaveApplications extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(hodleavemanagement.this, "Loading", "Fetching applications...", false, false);
        }

        @Override
        protected String doInBackground(Void... voids) {
            return RequestHandler.sendGetRequest(FETCH_URL);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            try {
                JSONArray jsonArray = new JSONArray(result);
                leaveList.clear();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = jsonArray.getJSONObject(i);

                }
                adapter.notifyDataSetChanged();
            } catch (JSONException e) {
                Toast.makeText(hodleavemanagement.this, "Error fetching data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void updateLeaveStatus(String leaveId, String status) {
        new UpdateLeaveStatus().execute(leaveId, status);
    }

    private class UpdateLeaveStatus extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = ProgressDialog.show(hodleavemanagement.this, "Updating", "Please wait...", false, false);
        }

        @Override
        protected String doInBackground(String... params) {
            HashMap<String, String> data = new HashMap<>();
            data.put("leaveId", params[0]);
            data.put("status", params[1]);

            return RequestHandler.sendPostRequest(ACTION_URL, data);
        }

        @Override
        protected void onPostExecute(String result) {
            progressDialog.dismiss();
            if (result.equalsIgnoreCase("success")) {
                Toast.makeText(hodleavemanagement.this, "Leave status updated", Toast.LENGTH_SHORT).show();
                new FetchLeaveApplications().execute();
            } else {
                Toast.makeText(hodleavemanagement.this, "Update failed", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
