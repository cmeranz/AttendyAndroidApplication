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
import com.simerankaur.attendyapplication.model.Employee;
import com.simerankaur.attendyapplication.ui.admin.AttendanceDetailsActivity;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AdminEmployeeAdapter extends RecyclerView.Adapter<AdminEmployeeAdapter.Views> {
    public AdminEmployeeAdapter(List<Employee> employeeList) {
        this.employeeList = employeeList;
    }
    List<Employee> employeeList;
    Context context;

    @NonNull
    @Override
    public Views onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_employee, parent, false);
        context = parent.getContext();
        return new AdminEmployeeAdapter.Views(view);
    }

    @Override
    public void onBindViewHolder(@NonNull Views holder, int position) {
        Employee employee = employeeList.get(position);
        holder.name.setText(employee.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                context.startActivity(new Intent(context, AttendanceDetailsActivity.class)
                        .putExtra("id", employee.getId()));
            }
        });
    }

   


    @Override
    public int getItemCount() {
        return employeeList.size();
    }

    public class Views extends RecyclerView.ViewHolder {
        TextView name, percentage;

        public Views(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.name);
            percentage = itemView.findViewById(R.id.percentage);
        }
    }
}
