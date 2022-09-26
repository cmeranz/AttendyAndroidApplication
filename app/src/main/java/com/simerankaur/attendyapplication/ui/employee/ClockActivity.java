package com.simerankaur.attendyapplication.ui.employee;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.SetOptions;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityClockBinding;
import com.simerankaur.attendyapplication.util.Loading;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity.UID;

public class ClockActivity extends AppCompatActivity {
    private static final int CLOCK_IN = 0;
    private static final int CLOCK_OUT = 1;
    FirebaseFirestore db;
    Loading loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_clock);
        ActivityClockBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_clock);
        db = FirebaseFirestore.getInstance();
        loading = new Loading(this);
        binding.clockIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClockActivity.this, QRCodeActivity.class);
                startActivityForResult(i, CLOCK_IN);

            }
        });
        binding.clockOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ClockActivity.this, QRCodeActivity.class);
                startActivityForResult(i, CLOCK_OUT);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CLOCK_IN) {
            if (resultCode == RESULT_OK) {
                showClockAlert("Clock In", 0);
            }
        } else {
            if (resultCode == RESULT_OK) {
                showClockAlert("Clock Out", 1);
            }
        }
    }


    private void showClockAlert(String _clock, int id) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        View layout = LayoutInflater.from(this).inflate(R.layout.layout_clock, null, false);
        TextView clock = layout.findViewById(R.id.clock);
        clock.setText(_clock);
        TextView date = layout.findViewById(R.id.date);
        date.setText(getDate());
        TextView time = layout.findViewById(R.id.time);
        time.setText(getTime());
        builder.setView(layout);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (id == 0) {
                    uploadData("clockIn", getDate(), getTime());
                } else {
                    uploadData("clockOut", getDate(), getTime());
                }

            }
        });
        builder.setCancelable(false);
        builder.show();
    }

    private void uploadData(String clock, String date, String time) {
        loading.showLoading();
        Map<String, Object> map = new HashMap<>();
        map.put(clock, time);
        map.put("date", date);
        db.collection("Employee").document(UID).collection("attendance")
                .document(getMonth()).collection("date")
                .document(getDay()).set(map, SetOptions.merge()).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    loading.hideLoading();
                    Toast.makeText(ClockActivity.this, "Attendance Saved Successfully", Toast.LENGTH_SHORT).show();
                    finish();
                } else {
                    loading.hideLoading();
                    Toast.makeText(ClockActivity.this, "Error" + task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private String getDate() {
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getDay() {
        DateFormat dateFormat = new SimpleDateFormat("dd");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getMonth() {
        DateFormat dateFormat = new SimpleDateFormat("MM");
        Date date = new Date();
        return dateFormat.format(date);
    }

    private String getTime() {
        DateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }

}