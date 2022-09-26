package com.simerankaur.attendyapplication.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityLeaveDetailsBinding;
import com.simerankaur.attendyapplication.model.Leave;
import com.simerankaur.attendyapplication.util.Loading;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class LeaveDetailsActivity extends AppCompatActivity {
    ActivityLeaveDetailsBinding binding;
    Loading loading;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_details);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_leave_details);
        db = FirebaseFirestore.getInstance();
        loading = new Loading(this);
        Leave leave = (Leave) getIntent().getSerializableExtra("leave");
        if (!leave.getDocument().isEmpty()) {
            Picasso.get().load(leave.getDocument()).into(binding.img);
        }
        binding.name.setText(leave.getName());
        binding.leaveDate.setText(leave.getLeaveDate());
        binding.leaveTitle.setText(leave.getLeaveTitle());
        binding.leaveReason.setText(leave.getLeaveReason());
        binding.accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                acceptLeave(leave);
            }
        });
        binding.decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                declineLeave(leave);
            }
        });
    }

    private void acceptLeave(Leave leave) {
        loading.showLoading();
        Map<String, Object> map = new HashMap<>();
        map.put("status", "Approved");
        db.collection("Leaves").document(leave.getLeaveId()).set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.hideLoading();
                            Toast.makeText(LeaveDetailsActivity.this, "Leave Approved", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            loading.hideLoading();
                            Toast.makeText(LeaveDetailsActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void declineLeave(Leave leave) {
        loading.showLoading();
        Map<String, Object> map = new HashMap<>();
        map.put("status", "Not Approved");
        db.collection("Leaves").document(leave.getLeaveId()).set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.hideLoading();
                            Toast.makeText(LeaveDetailsActivity.this, "Leave Declined", Toast.LENGTH_SHORT).show();
                            onBackPressed();
                        } else {
                            loading.hideLoading();
                            Toast.makeText(LeaveDetailsActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}

