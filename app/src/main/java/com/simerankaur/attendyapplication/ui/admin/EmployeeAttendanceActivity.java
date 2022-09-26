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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.adapter.AdminEmployeeAdapter;
import com.simerankaur.attendyapplication.databinding.ActivityEmployeeAttendanceBinding;
import com.simerankaur.attendyapplication.model.Employee;
import com.simerankaur.attendyapplication.util.Loading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity.UID;

public class EmployeeAttendanceActivity extends AppCompatActivity {

    ActivityEmployeeAttendanceBinding binding;
    Loading loading;
    FirebaseFirestore db;
    List<Employee> employeeList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this, R.layout.activity_employee_attendance);
        loading=new Loading(this);
        db=FirebaseFirestore.getInstance();
        binding.search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.name.getText().toString().isEmpty()){
                    getEmployee(binding.name.getText().toString().toLowerCase().trim());
                }else {
                    Toast.makeText(EmployeeAttendanceActivity.this, "Enter employee name", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void getEmployee(String name) {
        loading.showLoading();
        employeeList.clear();
        db.collection("Employee").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        String sa =snapshot.getString("name").toLowerCase().trim();
                        if(sa.contains(name)){

                            int[] a=getAttendance(snapshot.getId());
                            employeeList.add(new Employee(snapshot.getId(),snapshot.getString("name"),String.valueOf(a[0])));

                        }

                    }
                    if(employeeList.size()>0){
                        setAdapter();
                    }else {
                        loading.hideLoading();
                        Toast.makeText(EmployeeAttendanceActivity.this, "No Employee Found !", Toast.LENGTH_SHORT).show();
                    }
                    //  Toast.makeText(EmployeeAttendenceActivity.this, "No Employee Found !", Toast.LENGTH_SHORT).show();
                }else {
                    loading.hideLoading();
                    Toast.makeText(EmployeeAttendanceActivity.this, "Error "+task.getException() , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void  setAdapter() {
        AdminEmployeeAdapter adminEmployeeAdapter=new AdminEmployeeAdapter(employeeList);
        binding.recycler.setAdapter(adminEmployeeAdapter);
        loading.hideLoading();
    }

    private int[]  getAttendance(String id) {
        final int[] total = new int[1];
        String month=getMonth();
        db.collection("Employee")
                .document(id)
                .collection("attendance")
                .document(getMonth())
                .collection("date") .get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    total[0] =task.getResult().size();

                }else {
                    Toast.makeText(EmployeeAttendanceActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
        return total;
    }

    private String getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }
}