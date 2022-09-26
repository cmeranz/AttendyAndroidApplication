package com.simerankaur.attendyapplication.ui.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;

import android.Manifest;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.infideap.drawerbehavior.AdvanceDrawerLayout;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityEmployeeDashboardBinding;
import com.simerankaur.attendyapplication.ui.admin.AttendanceDetailsActivity;
import com.simerankaur.attendyapplication.ui.auth.LoginActivity;
import com.simerankaur.attendyapplication.ui.employee.leave.LeaveActivity;
import com.simerankaur.attendyapplication.ui.employee.profile.ProfileActivity;
import com.simerankaur.attendyapplication.util.Loading;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class EmployeeDashboardActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener  {
    private AdvanceDrawerLayout drawer;
    ActivityEmployeeDashboardBinding binding;
    public static String UID;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    Loading loading;
    public static String imgUrl="",name="";
    public static boolean UPDATED=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_dashboard);

        binding= DataBindingUtil.setContentView(this,R.layout.activity_employee_dashboard);
        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();
        UID=mAuth.getUid();
        loading=new Loading(this);

        nav();
        if(name != null && name.isEmpty()){
            getProfileData();
        }

        findViewById(R.id.scan).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EmployeeDashboardActivity.this,ClockActivity.class));
            }
        });
        askPermission();
    }

    private void askPermission() {
        //To take the permission from android os
        Dexter.withContext(this)
                .withPermissions(
                        Manifest.permission.CAMERA,
                        Manifest.permission.READ_EXTERNAL_STORAGE
                ).withListener(new MultiplePermissionsListener() {
            @Override public void onPermissionsChecked(MultiplePermissionsReport report) {}
            @Override public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                askPermission();
            }
        }).check();
    }

    private void getProfileData() {
         loading.showLoading();
        db.collection("Employee").document(UID).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()){
                    DocumentSnapshot snapshot=task.getResult();
                    imgUrl=snapshot.getString("img");

                    if(imgUrl != null && !imgUrl.isEmpty()){
                        //Download the image from url and set it into image view
                        Picasso.get().load(imgUrl).into(img);
                    }
                    name=snapshot.getString("name");
                    _name.setText(name);
                    loading.hideLoading();
                }else {
                    loading.hideLoading();

                }
            }
        });

    }


    NavigationView navigationView;
    private void nav() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
         //getSupportActionBar().setTitle( " " );
        toolbar.setTitleTextColor( Color.WHITE );
        drawer = (AdvanceDrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar,R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.white));
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);

        navigationView.setNavigationItemSelectedListener(EmployeeDashboardActivity.this);


       //drawer.setViewScale( Gravity.START, 0.9f);
       // drawer.setViewElevation(Gravity.START, 20);

        View header = navigationView.getHeaderView(0);
        img=header.findViewById(R.id.profile_image);
        _name=header.findViewById(R.id.fullName);
    }
    CircleImageView img;
    TextView _name;

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.profile:
                Intent intent=new Intent(EmployeeDashboardActivity.this, ProfileActivity.class);
                startActivity(intent);

                break;

            case R.id.leave:
                Intent intent2 =new Intent(EmployeeDashboardActivity.this, LeaveActivity.class);
                startActivity(intent2);

                break;

            case R.id.attendance:
                startActivity(new Intent(EmployeeDashboardActivity.this, AttendanceDetailsActivity.class)
                        .putExtra("id",UID));
                break;
            case R.id.help:
                startActivity(new Intent(EmployeeDashboardActivity.this, HelpActivity.class));
                break;
            case R.id.logout:
                mAuth.signOut();
                startActivity(new Intent(EmployeeDashboardActivity.this, LoginActivity.class));
                finishAffinity();
                break;
        }
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(UPDATED){
            UPDATED=false;
            getProfileData();
        }
    }
}