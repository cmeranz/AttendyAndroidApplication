package com.simerankaur.attendyapplication.ui.employee.leave;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityLeaveBinding;

public class LeaveActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLeaveBinding binding= DataBindingUtil.setContentView(this, R.layout.activity_leave);
        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        LeaveActivity.this, ApplyLeaveActivity.class
                ));
            }
        });
        binding.checkLeave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(
                        LeaveActivity.this, CheckLeaveActivity.class
                ));
            }
        });
    }
}