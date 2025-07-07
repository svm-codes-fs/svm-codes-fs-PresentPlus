public class LeaveApplication {
    private String id, name, leaveType, startDate, endDate, description, status;

    public LeaveApplication(String id, String name, String leaveType, String startDate, String endDate, String description, String status) {
        this.id = id;
        this.name = name;
        this.leaveType = leaveType;
        this.startDate = startDate;
        this.endDate = endDate;
        this.description = description;
        this.status = status;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getLeaveType() { return leaveType; }
    public String getStartDate() { return startDate; }
    public String getEndDate() { return endDate; }
    public String getDescription() { return description; }
    public String getStatus() { return status; }
}
