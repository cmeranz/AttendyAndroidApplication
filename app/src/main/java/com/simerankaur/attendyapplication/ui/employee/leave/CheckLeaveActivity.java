package com.simerankaur.attendyapplication.ui.employee.leave;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.adapter.LeaveAdapter;
import com.simerankaur.attendyapplication.databinding.ActivityCheckLeaveBinding;
import com.simerankaur.attendyapplication.model.Leave;
import com.simerankaur.attendyapplication.util.Loading;

import java.util.ArrayList;
import java.util.List;

import static com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity.UID;

public class CheckLeaveActivity extends AppCompatActivity {
    ActivityCheckLeaveBinding binding;
    Loading loading;
    FirebaseFirestore db;
    List<Leave> leaveList=new ArrayList<>();
    LeaveAdapter leavesAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_leave);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_check_leave);
        db=FirebaseFirestore.getInstance();
        loading=new Loading(this);
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        getAllLeaves();
    }

    private void getAllLeaves() {
        loading.showLoading();
        db.collection("Leaves").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        if(snapshot.get("uid").equals(UID)){
                            leaveList.add(new Leave(snapshot.getId(),snapshot.getString("name"),snapshot.getString("title"),snapshot.getString("date"),snapshot.getString("type"),snapshot.getString("status"),snapshot.getString("img")));
                        }
                    }
                    if(leaveList.size()>0){
                        setAdapter();
                    }else {
                        loading.hideLoading();
                        Toast.makeText(CheckLeaveActivity.this, "No Leave Request !", Toast.LENGTH_SHORT).show();
                    }

                }else {
                    loading.hideLoading();
                    Toast.makeText(CheckLeaveActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter() {
        leavesAdapter=new LeaveAdapter(leaveList);
        binding.recycler.setAdapter(leavesAdapter);
        loading.hideLoading();
    }
}