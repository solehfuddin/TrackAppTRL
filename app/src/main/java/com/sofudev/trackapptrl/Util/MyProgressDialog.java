package com.sofudev.trackapptrl.Util;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.Gravity;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.io.IOException;

public class MyProgressDialog {

    Dialog dialog;
    EasyGifView easyGifView;

    public MyProgressDialog(Context context) {

        dialog = new Dialog(context);
        dialog.setCancelable(false);

        RelativeLayout relativeLayout = new RelativeLayout(context);
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        relativeLayout.setLayoutParams(layoutParams);

        try {
            easyGifView = new EasyGifView(context);
        } catch (IOException e) {
            e.printStackTrace();
        }
        RelativeLayout.LayoutParams layoutParams_progress = new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        layoutParams_progress.addRule(RelativeLayout.CENTER_IN_PARENT);

        LinearLayout.LayoutParams linearlayoutParams_progress = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);

        linearlayoutParams_progress.gravity = Gravity.CENTER;


        easyGifView.setLayoutParams(layoutParams_progress);

        relativeLayout.addView(easyGifView);

        dialog.getWindow().setContentView(relativeLayout, layoutParams);
        dialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(Color.TRANSPARENT));


    }

    public void setGif(int resourceId) {
        easyGifView.setGifFromResource(resourceId);
    }

    public void show() {

        if (!dialog.isShowing() && dialog != null) {
            dialog.show();
        }

    }

    public void dismiss() {

        if (dialog.isShowing() && dialog != null) {
            dialog.dismiss();
        }
    }

    public void setCancelable(boolean cancelable) {
        dialog.setCancelable(cancelable);
    }


    public void setCanceledOnTouchOutside(boolean flag) {
        dialog.setCanceledOnTouchOutside(flag);
    }


    public boolean isShowing() {
        return dialog.isShowing();
    }

}
