package com.simerankaur.attendyapplication.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.model.Leave;

import java.util.ArrayList;
import java.util.List;

public class LeaveAdapter extends RecyclerView.Adapter<LeaveAdapter.Views> {
    public LeaveAdapter(List<Leave> leavesList) {
        this.leavesList = leavesList;
    }

    List<Leave> leavesList=new ArrayList<>();
    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_status,parent,false);
        return new Views(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Views holder, int position) {

        Leave leave=leavesList.get(position);
        holder.leaveType.setText(leave.getLeaveTitle());
        holder.leaveDate.setText(leave.getLeaveDate());
        if(leave.isStatus().equals("Approved")){
            holder.status.setImageResource(R.drawable.ic_approved);
            holder._status.setText("Approved");
        }else if(leave.isStatus().equals("Not Approved")) {
            holder.status.setImageResource(R.drawable.ic_x_button);
            holder._status.setText("Not Approved");
        }else {
            holder.status.setImageResource(R.drawable.ic_wall_clock);
            holder._status.setText("Reviewing");
        }


    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }

    public class Views extends RecyclerView.ViewHolder {
        TextView leaveType,leaveDate,_status;
        ImageView status;
        public Views(@NonNull View itemView) {
            super(itemView);
            leaveDate=itemView.findViewById(R.id.leaveDate);
            leaveType=itemView.findViewById(R.id.leaveType);
            _status=itemView.findViewById(R.id.textView5);
            status=itemView.findViewById(R.id.status);
        }
    }
}
