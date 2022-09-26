package com.simerankaur.attendyapplication.ui.employee;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;
import com.simerankaur.attendyapplication.R;
// https://github.com/yuriy-budiyev/code-scanner
public class QRCodeActivity extends AppCompatActivity {
    private CodeScanner mCodeScanner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_q_r_code);
        CodeScannerView scannerView = findViewById(R.id.scanner_view);
        mCodeScanner = new CodeScanner(this, scannerView); //initialising scanner
        mCodeScanner.setDecodeCallback(new DecodeCallback() { //getting value as result
            @Override
            public void onDecoded(@NonNull final Result result) {
                runOnUiThread(new Runnable() { //it will not affect other activities
                    @Override
                    public void run() {
                        //////////////////////////
                        Intent intent=new Intent();
                        intent.putExtra("result",Integer.parseInt(result.getText()));
                        setResult(Activity.RESULT_OK,intent);
                        finish();
                    }
                });
            }
        });

        //when clicked on screen it will detect another qr code
        scannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mCodeScanner.startPreview();
            }
        });
    }



    @Override
    protected void onResume() {
        super.onResume();
        mCodeScanner.startPreview();
    }

    @Override
    protected void onPause() {
        mCodeScanner.releaseResources();
        super.onPause();
    }
}