package com.simerankaur.attendyapplication.ui.employee;

import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.simerankaur.attendyapplication.R;
import com.simerankaur.attendyapplication.databinding.ActivityHelpBinding;

public class HelpActivity extends AppCompatActivity {
    ActivityHelpBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding= DataBindingUtil.setContentView(this, R.layout.activity_help);

        binding.buttonSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String mobileNumber = "1125670435";
                String message = binding.editMessage.getText().toString();
                boolean installed = appInstalledOrNot("com.whatsapp");
                if (installed){
                    Intent intent = new Intent(Intent.ACTION_VIEW);
                    intent.setData(Uri.parse("http://api.whatsapp.com/send?phone="+"+60"+mobileNumber + "&text="+message));
                    startActivity(intent);
                }else {
                    Toast.makeText(HelpActivity.this, "WhatsApp is not installed on your device", Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    private boolean appInstalledOrNot(String url){
        PackageManager packageManager =getPackageManager();
        boolean app_installed;
        try {
            packageManager.getPackageInfo(url,PackageManager.GET_ACTIVITIES);
            app_installed = true;
        }catch (PackageManager.NameNotFoundException e){
            app_installed = false;
        }
        return app_installed;
    }
}
