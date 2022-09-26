package com.simerankaur.attendyapplication.util;

import android.app.ProgressDialog;
import android.content.Context;

public class Loading {
    ProgressDialog progressDialog;
    public Loading(Context context){
         progressDialog=new ProgressDialog(context);
        progressDialog.setMessage("Loading...");
        progressDialog.setCancelable(false);
    }

    public void showLoading(){
        progressDialog.show();
    }
    public void hideLoading(){
        progressDialog.hide();
    }
}
