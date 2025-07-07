package com.example.capstoneproject;

public class LeaveApplication {
    private String leaveId;
    private String leaveStatus;

    public LeaveApplication(String leaveId, String leaveStatus) {
        this.leaveId = leaveId;
        this.leaveStatus = leaveStatus;
    }

    public String getLeaveId() {
        return leaveId;
    }

    public String getLeaveStatus() {
        return leaveStatus;
    }
}
