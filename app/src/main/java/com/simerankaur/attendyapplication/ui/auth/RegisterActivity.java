package com.simerankaur.attendyapplication.ui.auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityRegisterBinding;
import com.simerankaur.attendyapplication.util.Loading;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    ActivityRegisterBinding binding;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Loading loading;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        loading=new Loading(this);
        binding= DataBindingUtil. setContentView(this, R.layout.activity_register);
        binding.register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.username.getText().toString().isEmpty() && !binding.email.getText().toString().isEmpty() && !binding.password.getText().toString().isEmpty()){
                    loading.showLoading();
                    registerUser(binding.email.getText().toString().trim(),binding.password.getText().toString().trim(),binding.username.getText().toString(), binding.loginPhoneNumber.getText().toString());
                }else {
                    Toast.makeText(RegisterActivity.this, "Enter all details", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void registerUser(String email, String password, String username, String phone) {

        mAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){

                    mAuth.getCurrentUser().sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(RegisterActivity.this, "Verify your email", Toast.LENGTH_SHORT).show();
                                String UID=mAuth.getUid();

                                addEmployee(UID,email,password,username, phone);

                            }else {
                                loading.hideLoading();
                                Toast.makeText(RegisterActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    });

                }else {
                    loading.hideLoading();
                    Toast.makeText(RegisterActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private void addEmployee(String uid, String email, String password, String username, String phone) {
        Map<String,Object> map=new HashMap<>();
        map.put("name",username);
        map.put("email",email);
        map.put("jobTitle","");
        map.put("address","");
        map.put("phone", phone);
        map.put("img","");
        db.collection("Employee").document(uid).set(map)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            loading.hideLoading();
                            onBackPressed();

                        }else {
                            loading.hideLoading();
                            Toast.makeText(RegisterActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}