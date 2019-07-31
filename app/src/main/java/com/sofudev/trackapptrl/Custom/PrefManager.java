package com.sofudev.trackapptrl.Custom;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by Fuddins on 8/17/2017.
 */

public class PrefManager {
    SharedPreferences preferences;
    SharedPreferences.Editor editor;
    Context context;

    int mode = 0;
    private static final String Pref_Name = "check-intro";
    private static final String First_launch = "first-launch";

    public PrefManager(Context context) {
        this.context = context;
        preferences = context.getSharedPreferences(Pref_Name, mode);
        editor = preferences.edit();
    }

    public void setFirstLaunch(boolean firstLaunch) {
        editor.putBoolean(First_launch, firstLaunch);
        editor.commit();
    }

    public boolean isFirstLaunch() {
        return preferences.getBoolean(First_launch, true);
    }
}
