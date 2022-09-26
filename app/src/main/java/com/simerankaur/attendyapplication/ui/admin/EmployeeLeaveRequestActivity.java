package com.simerankaur.attendyapplication.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.adapter.AdminLeaveRequestAdapter;
import com.simerankaur.attendyapplication.databinding.ActivityEmployeeLeaveRequestBinding;
import com.simerankaur.attendyapplication.model.Leave;
import com.simerankaur.attendyapplication.util.Loading;

import java.util.ArrayList;
import java.util.List;

public class EmployeeLeaveRequestActivity extends AppCompatActivity {

    Loading loading;
    FirebaseFirestore db;
    List<Leave> leaveList=new ArrayList<>();
    ActivityEmployeeLeaveRequestBinding binding;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading=new Loading(this);
        db=FirebaseFirestore.getInstance();

        binding = DataBindingUtil. setContentView(this, R.layout.activity_employee_leave_request);
        getAllLeaves();
    }


    private void getAllLeaves() {
        loading.showLoading();
        db.collection("Leaves")
                .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        if(snapshot.get("status").equals("Reviewing")){
                            leaveList.add(new Leave(snapshot.getId()
                                    ,snapshot.getString("name"),
                                    snapshot.getString("title"),
                                    snapshot.getString("date"),
                                    snapshot.getString("reason")
                                    ,snapshot.getString("status")
                                    ,snapshot.getString("img")));
                        }
                    }
                    if(leaveList.size()>0){
                        loading.hideLoading();
                        setAdapter(leaveList);
                    }else {
                        loading.hideLoading();
                        Toast.makeText(EmployeeLeaveRequestActivity.this, "No Leaves Available", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.hideLoading();
                    Toast.makeText(EmployeeLeaveRequestActivity.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void setAdapter(List<Leave> leaveList) {
        AdminLeaveRequestAdapter adapter=new AdminLeaveRequestAdapter(leaveList);
        binding.recycler.setAdapter(adapter);
    }
}