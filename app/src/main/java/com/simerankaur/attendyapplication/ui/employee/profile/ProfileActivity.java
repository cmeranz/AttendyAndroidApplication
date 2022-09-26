package com.simerankaur.attendyapplication.ui.employee.profile;

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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.SetOptions;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityProfileBinding;
import com.simerankaur.attendyapplication.ui.employee.EmployeeDashboardActivity;
import com.simerankaur.attendyapplication.util.Loading;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {
    ActivityProfileBinding binding;
    FirebaseFirestore db;
    Loading loading;
    FirebaseStorage storage;
    StorageReference storageReference;
    FirebaseAuth mAuth;
    String ProfilePic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil. setContentView(this, R.layout.activity_profile);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        loading=new Loading(this);
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        getProfileData();
        binding.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enable();
            }
        });
        binding.imgUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ImagePicker.Companion.with(ProfileActivity.this)
                        .compress(1024)			//Final image size will be less than 1 MB(Optional)
                        .maxResultSize(1080, 1080)	//Final image resolution will be less than 1080 x 1080(Optional)
                        .start();
            }
        });

        binding.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EmployeeDashboardActivity.UPDATED=true;
                if(fileUri!=null){
                    uploadImage();
                }else{
                    loading.showLoading();
                    updateProfile();
                }

            }
        });


    }



    String imgUrl="";
    private void uploadImage() {
        loading.showLoading();
        if(fileUri != null)
        {




            StorageReference ref = storageReference.child("images/"+ mAuth.getUid());
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
                        updateProfile();
                        Toast.makeText(ProfileActivity.this, "Uploaded", Toast.LENGTH_SHORT).show();
                    } else {
                        loading.hideLoading();
                        Toast.makeText(ProfileActivity.this, "Error Image not uploaded", Toast.LENGTH_SHORT).show();
                        // Handle failures
                        // ...
                    }
                }
            });
        }

    }

    private void updateProfile() {
        Map<String, Object> map=new HashMap<>();
        map.put("name",binding.name.getText().toString());
        map.put("email",binding.email.getText().toString());
        map.put("jobTitle",binding.job.getText().toString());
        map.put("address",binding.address.getText().toString());
        map.put("phone", binding.phone.getText().toString());
        if(imgUrl==""){
            map.put("img",""+ProfilePic);
        } else {
            map.put("img",""+imgUrl);
        }
        db.collection("Employee").document(mAuth.getUid()).set(map, SetOptions.merge())
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            loading.hideLoading();
                            disabled();
                        }else{
                            Toast.makeText(ProfileActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                
    }

    private void disabled() {
        binding.edit.setVisibility(View.VISIBLE);
        binding.update.setVisibility(View.GONE);
        binding.imgUpload.setVisibility(View.GONE);
        binding.name.setFocusable(false);
        binding.name.setClickable(
                false
        );

        binding.name.setFocusableInTouchMode(false);
        binding.email.setFocusable(false);
        binding.email.setFocusableInTouchMode(false);
        binding.email.setClickable(false);
        binding.job.setFocusable(false);
        binding.job.setFocusableInTouchMode(false);
        binding.job.setClickable(false);
        binding.address.setFocusable(false);
        binding.address.setFocusableInTouchMode(false);
        binding.address.setClickable(false);
        binding.phone.setFocusable(false);
        binding.phone.setFocusableInTouchMode(false);
        binding.phone.setClickable(false);
    }

    Uri fileUri=null;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            //Image Uri will not be null for RESULT_OK
            fileUri = data.getData();
            binding.profileImage.setImageURI(fileUri);


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show();
        }
    }

    private void enable() {


        binding.edit.setVisibility(View.GONE);
        binding.update.setVisibility(View.VISIBLE);
        binding.imgUpload.setVisibility(View.VISIBLE);
        binding.name.setFocusable(true);
        binding.name.setClickable(true);

        binding.name.setFocusableInTouchMode(true);
        binding.email.setFocusable(true);
        binding.email.setFocusableInTouchMode(true);
        binding.email.setClickable(true);
        binding.job.setFocusable(true);
        binding.job.setFocusableInTouchMode(true);
        binding.job.setClickable(true);
        binding.address.setFocusable(true);
        binding.address.setFocusableInTouchMode(true);
        binding.address.setClickable(true);
        binding.phone.setFocusable(true);
        binding.phone.setFocusableInTouchMode(true);
        binding.phone.setClickable(true);

    }

    private void getProfileData() {
        loading.showLoading();
        db.collection("Employee").document(mAuth.getUid())
                .get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){

                    DocumentSnapshot snapshot=task.getResult();
                    binding.name.setText(snapshot.getString("name"));
                    binding.email.setText(snapshot.getString("email"));
                    binding.address.setText(snapshot.getString("address"));
                    binding.job.setText(snapshot.getString("jobTitle"));
                    binding.phone.setText(snapshot.getString("phone"));
                    if(!snapshot.getString("img").isEmpty()){
                        ProfilePic=snapshot.getString("img");
                        Picasso.get().load(ProfilePic).into(binding.profileImage);
                    }
                    loading.hideLoading();



                }else {
                    loading.hideLoading();
                    Toast.makeText(ProfileActivity.this, "Error"+task.getException(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}