import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.ViewHolder> {
    private Context context;
    private List<LeaveApplication> leaveList;

    public LeaveAdapter(Context context, List<LeaveApplication> leaveList) {
        this.context = context;
        this.leaveList = leaveList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_leave, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        LeaveApplication leave = leaveList.get(position);
        holder.name.setText(leave.getName());
        holder.leaveType.setText(leave.getLeaveType());
        holder.dates.setText(leave.getStartDate() + " to " + leave.getEndDate());
        holder.status.setText(leave.getStatus());
    }

    @Override
    public int getItemCount() { return leaveList.size(); }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView name, leaveType, dates, status;

        public ViewHolder(View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.leaveName);
            leaveType = itemView.findViewById(R.id.leaveType);
            dates = itemView.findViewById(R.id.leaveDates);
            status = itemView.findViewById(R.id.leaveStatus);
        }
    }
}
