package com.simerankaur.attendyapplication.ui;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import com.google.firebase.auth.FirebaseAuth;
import com.simerankaur.attendyapplication.MainActivity;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.ui.auth.LoginActivity;
import com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity;

public class SplashActivity extends AppCompatActivity {

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        auth=FirebaseAuth.getInstance();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                checkUser();
            }
        },2000);

    }

    private void checkUser() {
        if(auth.getCurrentUser()==null){
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(this, EmployeeDashboardActivity.class);
            startActivity(intent);
            finish();
        }
    }
}