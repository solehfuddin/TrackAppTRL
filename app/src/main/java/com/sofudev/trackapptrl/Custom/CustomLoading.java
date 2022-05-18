package com.sofudev.trackapptrl.Custom;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.LayoutInflater;
import android.view.View;

import com.sofudev.trackapptrl.R;

public class CustomLoading {
    Dialog dialog;
    View v;

    @SuppressLint("InflateParams")
    public CustomLoading(Context context) {
        dialog = new Dialog(context);
        v = LayoutInflater.from(context).inflate(R.layout.dialog_loading_all, null);
    }

    public void showLoadingDialog(){
        dialog.setContentView(v);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        dialog.show();
    }

    public void dismissLoadingDialog(){
        dialog.dismiss();
    }
}
