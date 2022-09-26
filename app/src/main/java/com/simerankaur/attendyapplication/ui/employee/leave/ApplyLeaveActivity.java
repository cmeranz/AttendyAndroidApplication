package com.simerankaur.attendyapplication.ui.employee.leave;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.github.dhaval2404.imagepicker.ImagePicker;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityApplyLeaveBinding;
import com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity;
import com.simerankaur.attendyapplication.util.Loading;

import java.util.HashMap;
import java.util.Map;

import static com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity.UID;

public class ApplyLeaveActivity extends AppCompatActivity {
    ActivityApplyLeaveBinding binding;
    Loading loading;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this, R.layout.activity_apply_leave);
        loading=new Loading(this);
        db=FirebaseFirestore.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference(); //to initialise reference for storage
        binding.toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) { onBackPressed();
            }
        });
        binding.img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ApplyLeaveActivity.this)
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });
        binding.upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(fileUri!=null){
                    uploadImage();
                }else {
                    Toast.makeText(ApplyLeaveActivity.this, "Select the document first", Toast.LENGTH_SHORT).show();
                }

            }
        });
        binding.apply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!binding.leaveType.getText().toString().isEmpty() && !binding.leaveDate.getText().toString().isEmpty() && !binding.leaveReason.getText().toString().isEmpty()){
                    uploadLeave(binding.leaveType.getText().toString(),binding.leaveDate.getText().toString(),binding.leaveReason.getText().toString());
                }else {
                    Toast.makeText(ApplyLeaveActivity.this, "Enter All Details and Document", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    String imgUrl="";
    FirebaseStorage storage;
    StorageReference storageReference;
    private void uploadImage() {
        loading.showLoading();
        if(fileUri != null)
        {
            StorageReference ref = storageReference.child("images/"+ System.currentTimeMillis() );
            //ref.putFile(fileUri);
            UploadTask uploadTask = ref.putFile(fileUri);
            Task<Uri> urlTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if (!task.isSuccessful()) {
                        throw task.getException();
                    }
                    // Continue with the task to get the download URL
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if (task.isSuccessful()) {
                        loading.hideLoading();
                        Uri downloadUri = task.getResult();
                        imgUrl = downloadUri.toString();
                        Toast.makeText(ApplyLeaveActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.hideLoading();
                        Toast.makeText(ApplyLeaveActivity.this, "Error Image not uploaded", Toast.LENGTH_SHORT).show();
                        // Handle failures
                        // ...
                    }
                }
            });
        }
    }

    private void uploadLeave(String type, String date, String reason) {
        loading.showLoading();
        Map<String,Object> map=new HashMap<>();
        map.put("name",""+ EmployeeDashboardActivity.name);
        map.put("title",type);
        map.put("date",date);
        map.put("reason",reason);
        map.put("uid",UID);
        map.put("status","Reviewing");
        map.put("img",""+imgUrl);
        db.collection("Leaves").document(String.valueOf(System.currentTimeMillis()))
                .set(map).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    loading.hideLoading();
                    Toast.makeText(ApplyLeaveActivity.this, "Leave Uploaded Successfully", Toast.LENGTH_SHORT).show();
                    onBackPressed();
                }else {
                    loading.hideLoading();
                    Toast.makeText(ApplyLeaveActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    Uri fileUri=null;
    @Override
    //image picker library
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            binding.img.setImageURI(fileUri);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }
}