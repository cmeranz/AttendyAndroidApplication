package com.simerankaur.attendyapplication.ui.admin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityADminDashboardBinding;
import com.simerankaur.attendyapplication.ui.auth.LoginActivity;

public class AdminDashboardActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActivityADminDashboardBinding binding= DataBindingUtil. setContentView(this, R.layout.activity_a_dmin_dashboard);
        binding.attendance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,EmployeeAttendanceActivity.class));
            }
        });
        binding.leave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this,EmployeeLeaveRequestActivity.class));
            }
        });
        binding.logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminDashboardActivity.this, LoginActivity.class));
                finishAffinity();
            }
        });
    }
}