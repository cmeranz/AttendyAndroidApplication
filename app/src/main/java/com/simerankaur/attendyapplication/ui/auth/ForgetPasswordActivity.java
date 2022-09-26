package com.simerankaur.attendyapplication.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityForgetPasswordBinding;
import com.simerankaur.attendyapplication.util.Loading;

public class ForgetPasswordActivity extends AppCompatActivity {
    ActivityForgetPasswordBinding binding;
    Loading loading;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loading= new Loading(this);
        mAuth=FirebaseAuth.getInstance();
        binding= DataBindingUtil.setContentView(this, R.layout.activity_forget_password);
        binding.forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.email.getText().toString().isEmpty()){
                    resetEmail(binding.email.getText().toString());
                }else {
                    Toast.makeText(ForgetPasswordActivity.this, "Enter your email !", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void resetEmail(String  email) {
        loading.showLoading();
        mAuth.sendPasswordResetEmail(email)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            loading.hideLoading();
                            onBackPressed();
                            Toast.makeText(ForgetPasswordActivity.this, "Check your email to reset your password", Toast.LENGTH_SHORT).show();

                        }else {
                            loading.hideLoading();
                            Toast.makeText(ForgetPasswordActivity.this, "Error "+task.getException() , Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
