package com.sofudev.trackapptrl.Custom;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

import com.sofudev.trackapptrl.LoggerActivity;

import java.io.PrintWriter;
import java.io.StringWriter;

public class ForceCloseHandler implements Thread.UncaughtExceptionHandler {

    private final Context context;

    public ForceCloseHandler(Context context) {
        this.context = context;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));

        StringBuilder sb = new StringBuilder();
        sb.append("************ CAUSE OF ERROR ************\n\n");
        sb.append(trace.toString());

        sb.append("\n************ DEVICE INFORMATION ***********\n");
        sb.append("Brand: ");
        sb.append(Build.BRAND);

        String LINE_SEPARATOR = "\n";
        sb.append(LINE_SEPARATOR);
        sb.append("Device: ");
        sb.append(Build.DEVICE);
        sb.append(LINE_SEPARATOR);
        sb.append("Model: ");
        sb.append(Build.MODEL);
        sb.append(LINE_SEPARATOR);
        sb.append("Id: ");
        sb.append(Build.ID);
        sb.append(LINE_SEPARATOR);
        sb.append("Product: ");
        sb.append(Build.PRODUCT);
        sb.append(LINE_SEPARATOR);
        sb.append("\n************ FIRMWARE ************\n");
        sb.append("SDK: ");
        sb.append(Build.VERSION.SDK_INT);
        sb.append(LINE_SEPARATOR);
        sb.append("Release: ");
        sb.append(Build.VERSION.RELEASE);
        sb.append(LINE_SEPARATOR);
        sb.append("Incremental: ");
        sb.append(Build.VERSION.INCREMENTAL);
        sb.append(LINE_SEPARATOR);

        Intent intent = new Intent(context, LoggerActivity.class);
        intent.putExtra("error", sb.toString());
        context.startActivity(intent);

        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(10);
    }
}
