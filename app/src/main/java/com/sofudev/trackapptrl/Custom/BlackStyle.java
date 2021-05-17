package com.sofudev.trackapptrl.Custom;

import android.content.Context;
import androidx.annotation.ColorInt;

import com.beardedhen.androidbootstrap.api.attributes.BootstrapBrand;
import com.sofudev.trackapptrl.R;

public class BlackStyle implements BootstrapBrand {
    @ColorInt private static transient int defaultFill;
    @ColorInt private static transient int defaultEdge;
    @ColorInt private static transient int defaultTextColor;
    @ColorInt private static transient int activeFill;
    @ColorInt private static transient int activeEdge;
    @ColorInt private static transient int activeTextColor;
    @ColorInt private static transient int disabledFill;
    @ColorInt private static transient int disabledEdge;
    @ColorInt private static transient int disabledTextColor;

    @SuppressWarnings("deprecation") public BlackStyle(Context context) {
        defaultFill = context.getResources().getColor(android.R.color.black);
        defaultEdge = context.getResources().getColor(android.R.color.black);
        defaultTextColor = context.getResources().getColor(android.R.color.black);
        activeFill = context.getResources().getColor(android.R.color.black);
        activeEdge = context.getResources().getColor(android.R.color.black);
        activeTextColor = context.getResources().getColor(android.R.color.black);
        disabledFill = context.getResources().getColor(R.color.custom_disabled_fill);
        disabledEdge = context.getResources().getColor(R.color.custom_disabled_edge);
        disabledTextColor = context.getResources().getColor(R.color.bootstrap_gray);
    }

    @Override
    public int defaultFill(Context context) {
        return defaultFill;
    }

    @Override
    public int defaultEdge(Context context) {
        return defaultEdge;
    }

    @Override
    public int defaultTextColor(Context context) {
        return defaultTextColor;
    }

    @Override
    public int activeFill(Context context) {
        return activeFill;
    }

    @Override
    public int activeEdge(Context context) {
        return activeEdge;
    }

    @Override
    public int activeTextColor(Context context) {
        return activeTextColor;
    }

    @Override
    public int disabledFill(Context context) {
        return disabledFill;
    }

    @Override
    public int disabledEdge(Context context) {
        return disabledEdge;
    }

    @Override
    public int disabledTextColor(Context context) {
        return disabledTextColor;
    }

    @Override
    public int getColor() {
        return defaultFill;
    }
}
