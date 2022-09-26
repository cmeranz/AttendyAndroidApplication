package com.simerankaur.attendyapplication.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityLoginBinding;
import com.simerankaur.attendyapplication.ui.admin.AdminDashboardActivity;
import com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity;
import com.simerankaur.attendyapplication.util.Loading;

public class LoginActivity extends AppCompatActivity  {

    boolean isAdmin=false;
    ActivityLoginBinding binding;
    Loading loading;
    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this,R.layout.activity_login);
        auth=FirebaseAuth.getInstance();
        loading=new Loading(this);

       binding.radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                RadioButton radioButton = findViewById(checkedId);
                if (radioButton.getText().equals("Admin")) {
                    binding.email.setHint("Username");
                    isAdmin = true;
                } else {
                    binding.email.setHint("Email");
                    isAdmin = false;
                }
            }
        });
        binding.forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, ForgetPasswordActivity.class));
                //Method 2:  Intent qwerty = new Intent(LoginActivity.this, ForgetPasswordActivity.class);
                // startActivity(qwerty);
            }
        });
        binding.signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.email.toString().isEmpty() && !binding.password.toString().isEmpty()){
                    if(isAdmin){

                        if (binding.email.getText().toString().trim().equals("admin") && binding.password.getText().toString().trim().equals("1234")) {
                            startActivity(new Intent(LoginActivity.this, AdminDashboardActivity.class));
                            finishAffinity();
                        } else {
                            Toast.makeText(LoginActivity.this, "Wrong login details", Toast.LENGTH_SHORT).show();
                        }
                    }else {

                        loginUser(binding.email.getText().toString(),binding.password.getText().toString());
                    }
                }else {
                    Toast.makeText(LoginActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }



    private void loginUser(String email, String pass) {
        loading.showLoading();
        auth.signInWithEmailAndPassword(email,pass).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    loading.hideLoading();
                    Intent intent=new Intent(LoginActivity.this, EmployeeDashboardActivity.class);
                    startActivity(intent);
                }else {
                    loading.hideLoading();
                    Toast.makeText(LoginActivity.this, "Error "+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


}