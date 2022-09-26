package com.simerankaur.attendyapplication.adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.model.Leave;
import com.simerankaur.attendyapplication.ui.admin.LeaveDetailsActivity;

import java.util.ArrayList;
import java.util.List;

public class AdminLeaveRequestAdapter extends RecyclerView.Adapter<AdminLeaveRequestAdapter.MyViews> {
    public AdminLeaveRequestAdapter(List<Leave> leavesList) {
        this.leavesList = leavesList;
    }

    List<Leave> leavesList=new ArrayList<>();
    Context context;
    @NonNull
    @Override
    public MyViews onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_leave_request,parent,false);
        context=parent.getContext();
        return new AdminLeaveRequestAdapter.MyViews(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViews holder, int position) {
        Leave leave=leavesList.get(position);
        holder.name.setText(leave.getName());
        holder.title.setText(leave.getLeaveTitle());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                context.startActivity(new Intent(context,
                        LeaveDetailsActivity.class)
                        .putExtra("leave", leave));
            }
        });
    }

    @Override
    public int getItemCount() {
        return leavesList.size();
    }

    public class MyViews extends RecyclerView.ViewHolder {
        TextView name,title;
        public MyViews(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.name);
            title=itemView.findViewById(R.id.title);

        }
    }
}

