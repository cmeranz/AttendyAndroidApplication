package com.simerankaur.attendyapplication.ui.admin;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.adapter.DateAdapter;
import com.simerankaur.attendyapplication.databinding.ActivityAttendanceDetailsBinding;
import com.simerankaur.attendyapplication.model.Clock;
import com.simerankaur.attendyapplication.util.Loading;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AttendanceDetailsActivity extends AppCompatActivity {

    ActivityAttendanceDetailsBinding binding;
    FirebaseFirestore db;
    Loading loading;
    List<Clock> dateList=new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attendance_details);
        loading=new Loading(this);
        db=FirebaseFirestore.getInstance();
        binding= DataBindingUtil. setContentView(this,R.layout.activity_attendance_details);
        getData(getIntent().getStringExtra("id"));
    }

    private void getData(String id) {
        //loading.showLoading();
        //getting data from db
        db.collection("Employee")
                .document(id)
                .collection("attendance")
                .document(getMonth())
                .collection("date").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    int total=task.getResult().size();
                    binding.circularProgressBar.setProgressMax(31);
                    binding.circularProgressBar.setProgressWithAnimation(total, 1000L);

                    binding.per.setText(total+"%");
                    for(QueryDocumentSnapshot snapshot:task.getResult()){
                        dateList.add(new Clock(snapshot.getId(),snapshot.getString("clockIn"),snapshot.getString("clockOut")));
                    }

                    if(dateList.size()>0){
                        loading.hideLoading();
                        setAdapter();
                    }else {
                        loading.hideLoading();
                        Toast.makeText(AttendanceDetailsActivity.this, "No Record Found", Toast.LENGTH_SHORT).show();
                    }
                }else {
                    loading.hideLoading();
                    Toast.makeText(AttendanceDetailsActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
    DateAdapter dateAdapter;
    private void setAdapter() {
        DateAdapter dateAdapter=new DateAdapter(dateList);
        dateAdapter.setUpButton(new DateAdapter.onClickDate() {
            @Override
            public void onClickDateListener(String clockIn, String clockOut) {
                setClockValues(clockIn,clockOut);
            }
        });
        Clock clock=dateList.get(0);
        setClockValues(clock.getClockIn(),clock.getClockOut());
        binding.recyclerView.setAdapter(dateAdapter);
    }

    private void setClockValues(String clockIn, String clockOut)  {
        //display the clockIn and clockOut
        binding.clockIn.setText(clockIn);
        binding.clockOut.setText(clockOut);

        Log.d("qwerty time",clockIn + " time "+ clockOut);

        // calculating Working Hours
        SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
        Date date1 = null;
        try {
            date1 = format.parse(clockIn);
            Date date2 = format.parse(clockOut);
            long difference = date2.getTime() - date1.getTime();
            int h = (int) ((difference / 1000) / 3600);
            int m = (int) (((difference / 1000) / 60) % 60);
            int s = (int) ((difference / 1000) % 60);
            binding.hour.setText(h+" Hours "+ m + " Minutes");
        } catch (ParseException e) {
            e.printStackTrace();
            Toast.makeText(this, ""+e, Toast.LENGTH_SHORT).show();
        }




    }


    private String getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }
}