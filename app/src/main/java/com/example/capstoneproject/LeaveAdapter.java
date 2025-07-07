package com.example.capstoneproject;

import android.content.Context;
import android.view.*;
import android.widget.*;
import java.util.List;

public class LeaveAdapter extends BaseAdapter {
    private Context context;
    private List<LeaveApplication> leaveList;

    public LeaveAdapter(Context context, List<LeaveApplication> leaveList) {
        this.context = context;
        this.leaveList = leaveList;
    }

    @Override
    public int getCount() {
        return leaveList.size();
    }

    @Override
    public Object getItem(int position) {
        return leaveList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(android.R.layout.simple_list_item_2, parent, false);
        }

        TextView text1 = convertView.findViewById(android.R.id.text1);
        TextView text2 = convertView.findViewById(android.R.id.text2);

        LeaveApplication leave = leaveList.get(position);
        text1.setText("ID: " + leave.getLeaveId());
        text2.setText("Status: " + leave.getLeaveStatus());

        return convertView;
    }
}
